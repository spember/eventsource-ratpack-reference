package pember.ecomm.events.product

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
class NameChangedEvent extends AbstractEvent<Product> {

    @EventData String name = ""

    @Override
    void restoreData(Map data) {
        name = data.name
    }

    @Override
    void process(Product aggregate) {
        aggregate = name
    }
}
