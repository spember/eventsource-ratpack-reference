package pember.ecomm.eventsource

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import pember.db.JooqService
import pember.fixtures.ExampleESApplicationUnderTest
import ratpack.http.client.RequestSpec
import ratpack.test.ApplicationUnderTest
import ratpack.test.http.TestHttpClient
import ratpack.test.remote.RemoteControl
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Steve Pember
 */
class ProductEventSourceServiceSpec extends Specification {

    @Shared
    ApplicationUnderTest aut = new ExampleESApplicationUnderTest()
    @Delegate
    TestHttpClient client = aut.httpClient
    RemoteControl remote = new RemoteControl(aut)

    JsonSlurper json = new JsonSlurper()

    void cleanup() {
        remote.exec {
            get(JooqService).execute("delete from event")
            get(JooqService).execute("delete from product")
        }
    }

    def "Basic Post Sanity Check" () {
        when:
            requestSpec { RequestSpec rs ->
                rs.body.type("application/json")
                rs.body.text(JsonOutput.toJson([sku: "ABCDE01", name: "Test Product", priceInCents: 500]))
            }
            post("api/products")

        then:
            Map product = json.parseText(response.body.text) as Map
            with(product) {
                sku == "ABCDE01"
                quantity > 0
                quantity < 250
                name == "Test Product"
            }
    }

    def "A Created product should appear in the product list" () {
        when:
            requestSpec { RequestSpec rs ->
                rs.body.type("application/json")
                rs.body.text(JsonOutput.toJson([sku: "ABCDE01", name: "Test Product", priceInCents: 500]))
            }
            post("api/products")
            String response = getText("api/products")
        then:
            List<Map> products = json.parseText(response)
            products.size() == 1
            with(products[0]) {
                sku == "ABCDE01"
                quantity > 0
                quantity < 250
                name == "Test Product"
            }
    }

    def "Attempting to update a product that doesn't exist should result in an empty return" () {
        when:
            requestSpec { RequestSpec rs ->
                rs.body.type("application/json")
                rs.body.text(JsonOutput.toJson([sku: "ABCDE01", name: "Test Product", priceInCents: 500]))
            }
            put("api/products/${UUID.randomUUID()}")
        then:
            response.status.code == 404
            json.parseText(response.body.text) == []
    }

    def "Updating a product should stick" () {
        given:
            requestSpec { RequestSpec rs ->
                rs.body.type("application/json")
                rs.body.text(JsonOutput.toJson([sku: "ABCDE01", name: "Test Product", priceInCents: 500, description: "Ooops"]))
            }
            post("api/products")
            Map productData = json.parseText(response.body.text) as Map
            assert productData.id != null
        when:
            requestSpec { RequestSpec rs ->
                rs.body.type("application/json")
                rs.body.text(JsonOutput.toJson([sku: "ABCDE02", name: "Test Product", priceInCents: 5000, description: "This is correct"]))
            }
            put("api/products/${productData.id}")
        then:
            Map updatedProduct = json.parseText(response.body.text) as Map
            with(updatedProduct) {
                sku == "ABCDE02"
                name == "Test Product"
                priceInCents == 5000
                description == "This is correct"
            }

    }


}
