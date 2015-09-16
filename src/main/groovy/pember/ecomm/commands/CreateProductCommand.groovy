package pember.ecomm.commands

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @author Steve Pember
 */
@ToString
@CompileStatic
class CreateProductCommand {
    String name
    String sku
    String description
    int priceInCents
}
