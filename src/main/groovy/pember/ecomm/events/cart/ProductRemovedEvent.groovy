package pember.ecomm.events.cart

import pember.ecomm.aggregates.Cart

/**
 * @author Steve Pember
 */
class ProductRemovedEvent extends BaseCartItemEvent {

    @Override
    void process(Cart aggregate) {
        if (sku in aggregate.items.keySet()) {
            aggregate.items[sku] -= quantity
        }
        // prevent going below zero
        if (aggregate.items[sku] < 0) {
            aggregate.items[sku] = 0
        }
    }
}
