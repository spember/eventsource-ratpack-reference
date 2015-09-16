package pember.ecomm

import com.google.inject.Inject
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import pember.ecomm.aggregates.Product
import pember.ecomm.commands.CreateProductCommand
import pember.ecomm.eventsource.ProductEventSourceService
import pember.ecomm.services.ProductService
import ratpack.groovy.handling.GroovyChainAction

/**
 * @author Steve Pember
 */
@Slf4j
class ProductUrlMappings extends GroovyChainAction {
    private ProductService productService
    private ProductEventSourceService productEventSourceService

    @Inject
    ProductUrlMappings(ProductService ps, ProductEventSourceService pess) {
        productService = ps
        productEventSourceService = pess
    }


    @Override
    void execute() throws Exception {
        path (":productId") {
            UUID productId = UUID.fromString(pathTokens["productId"].toString())

            Date date = new Date()
            if (request.queryParams["dateEffective"]) {
                date = Date.parse("yyyy-MM-dd HH:mm a z", request.queryParams["dateEffective"].toString())
            }

            byMethod {
                get {
                    Product product = productEventSourceService.get(productId)
                    productEventSourceService.loadHistoryUpTo(product, date)
                    render JsonOutput.toJson(product)
                }
                put {
                    parse(CreateProductCommand)
                    .observe()
                    .map { CreateProductCommand input -> productService.update(productId, input) }
                    .single()
                    .subscribe { Product product ->
                        if (product) {
                            render JsonOutput.toJson(product)
                        } else {
                            response.status(404)
                            render JsonOutput.toJson([])
                        }
                    }
                }
                delete {
                    productService.delete(productId)
                    response.status(204)
                    render ""
                }
            }
        }
        all {
            byMethod {
                get {
                    List<Product> products = productEventSourceService.list()
                    productEventSourceService.loadCurrentState(products)
                    render JsonOutput.toJson(products)
                }
                post {
                    parse(CreateProductCommand)
                        .observe()
                        .map { CreateProductCommand input ->
                            productService.create(input)
                        }
                        .single()
                        .subscribe { Product product ->
                            render JsonOutput.toJson(product)
                        }
                }
            }
        }
    }
}
