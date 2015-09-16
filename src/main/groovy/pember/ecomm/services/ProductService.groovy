package pember.ecomm.services

import com.google.inject.Inject
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import pember.ecomm.aggregates.Product
import pember.ecomm.commands.CreateProductCommand
import pember.ecomm.events.ActivatedEvent
import pember.ecomm.events.product.DescriptionChangedEvent
import pember.ecomm.events.product.NameChangedEvent
import pember.ecomm.events.product.PriceChangedEvent
import pember.ecomm.events.product.ProductCreatedEvent
import pember.ecomm.events.product.SkuChangedEvent
import pember.ecomm.eventsource.ProductEventSourceService

/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class ProductService {
    private Random random = new Random()

    private ProductEventSourceService productEventSourceService
    @Inject ProductService(ProductEventSourceService pess) {
        productEventSourceService = pess
    }

    /**
     * Create a product
     *
     * @param command
     * @return
     */
    Product create(CreateProductCommand command) {

        log.debug("Received command: ${command}")
        // check to see if sku already exists?
        Product product = new Product()
        product.applyChange(new ProductCreatedEvent(name: command.name, initialQuantity: Math.abs(random.nextInt() % 250)+1,
                sku: command.sku, priceInCents: command.priceInCents, description: command.description))
        productEventSourceService.save(product)
        product
    }

    /**
     *
     * @param productId
     * @param command
     * @return
     */
    Product update(UUID productId, CreateProductCommand command) {
        log.debug("Received command ${command}")

        if (productEventSourceService.aggregateService.exists(productId)) {
            Product product = productEventSourceService.getCurrent(productId)
            // here we check each field on product for differences, then apply individual events for those changes.
            if (product.description != command.description) { product.applyChange(new DescriptionChangedEvent(description: command.description))}
            if (product.name != command.name) { product.applyChange(new NameChangedEvent(name: command.name))}
            if (product.priceInCents != command.name) { product.applyChange(new PriceChangedEvent(priceInCents: command.priceInCents))}
            if (product.sku != command.sku) { product.applyChange(new SkuChangedEvent(sku: command.sku))}
            if (productEventSourceService.save(product)) {
                product
            } else {
                log.error("Could not save product")
                null
            }

        } else {
            log.warn("Attempted to update a Product which doesn't exist")
            null
        }
    }

    /**
     *
     * @param productId
     */
    void delete(UUID productId) {
        log.debug("Deleting ${productId}")
        // note the get instead of getCurrent; because 'active' is persisted (and thus always reflects current state),
        // and we do not modify anything else, there's not need to fetch current state
        Product product = productEventSourceService.get(productId)
        product.applyChange(new ActivatedEvent(active: false))
        productEventSourceService.save(product)
    }
}
