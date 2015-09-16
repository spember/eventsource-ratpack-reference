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
class DescriptionChangedEvent extends AbstractEvent<Product> {
    @EventData String description

    @Override
    void restoreData(Map data) {
        description = data.description
    }

    @Override
    void process(Product aggregate) {
        aggregate.description = description
    }
}
