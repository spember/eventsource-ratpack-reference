package pember.ecomm.events.product

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
class SkuChangedEvent extends AbstractEvent<Product> {
    @EventData String sku

    @Override
    void restoreData(Map data) {
        sku = data.sku
    }

    @Override
    void process(Product aggregate) {
        aggregate.sku = sku
    }
}
