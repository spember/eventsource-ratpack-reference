package pember.ecomm.events.product

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import groovy.transform.CompileStatic
import groovy.transform.ToString
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
@ToString
@CompileStatic
class ProductCreatedEvent extends AbstractEvent<Product>{
    @EventData String name = ""
    @EventData String sku = ""
    @EventData String description = ""
    @EventData int priceInCents = 0
    @EventData int initialQuantity = 0

    @Override
    void restoreData(Map data) {
        name = data.name
        sku = data.sku
        description = data.description
        priceInCents = data.priceInCents as int
        initialQuantity = data.initialQuantity as int
    }

    @Override
    void process(Product aggregate) {
        aggregate.name = name
        aggregate.sku = sku
        aggregate.description = description
        aggregate.priceInCents = priceInCents
        aggregate.quantity = initialQuantity
    }
}
