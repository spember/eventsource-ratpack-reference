package pember.ecomm.events.cart

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Cart

/**
 * @author Steve Pember
 */
@CompileStatic
class CartCreatedEvent extends AbstractEvent<Cart> {
    @EventData UUID personUUID

    @Override
    void restoreData(Map data) {
        personUUID = UUID.fromString(data.personUUID.toString())
    }

    @Override
    void process(Cart aggregate) {
        aggregate.personUuid = personUUID
    }
}
