package pember.ecomm.aggregates

import groovy.transform.CompileStatic

/**
 * @author Steve Pember
 */
@CompileStatic
class Product extends BaseAggregate {
    String sku = ""
    String name = ""
    String description = ""
    String imageUrl = ""
    int priceInCents = 0
    int quantity = 0
}
