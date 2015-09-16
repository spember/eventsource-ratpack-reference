package pember.ecomm.events.cart

import pember.ecomm.aggregates.Cart

/**
 * @author Steve Pember
 */
class ProductAddedEvent extends BaseCartItemEvent{
    @Override
    void process(Cart aggregate) {
        if (!(sku in aggregate.items.keySet())) {
            aggregate.items[sku] = quantity
        } else {
            aggregate.items[sku] += quantity
        }
    }
}
