package pember.ecomm.events

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import pember.ecomm.aggregates.BaseAggregate

/**
 * @author Steve Pember
 */
class ActivatedEvent extends AbstractEvent<BaseAggregate> {

    @EventData boolean active

    @Override
    void restoreData(Map data) {
        active = data.active as boolean
    }

    @Override
    void process(BaseAggregate aggregate) {
        aggregate.active = active
    }
}
