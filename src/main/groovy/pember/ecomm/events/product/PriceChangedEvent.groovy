package pember.ecomm.events.product

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
class PriceChangedEvent extends AbstractEvent<Product>{
    @EventData int priceInCents
    @Override
    void restoreData(Map data) {
        priceInCents = data.priceInCents as int
    }

    @Override
    void process(Product aggregate) {
        aggregate.priceInCents = priceInCents
    }
}
