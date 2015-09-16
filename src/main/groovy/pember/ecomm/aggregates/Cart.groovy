package pember.ecomm.aggregates

import groovy.transform.CompileStatic

/**
 * @author Steve Pember
 */
@CompileStatic
class Cart extends BaseAggregate {
    UUID personUuid
    // map of sku to item count
    Map<String, Integer> items = [:]
}
