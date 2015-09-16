package pember.ecomm.events.product

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
@CompileStatic
class NameChangedEvent extends AbstractEvent<Product> {

    @EventData String name = ""

    @Override
    void restoreData(Map data) {
        name = data.name
    }

    @Override
    void process(Product aggregate) {
        aggregate.name = name
    }
}
