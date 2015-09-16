package pember.ecomm.events.cart

import com.thirdchannel.eventsource.AbstractEvent
import com.thirdchannel.eventsource.annotation.EventData
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Cart

/**
 * @author Steve Pember
 */
@CompileStatic
abstract class BaseCartItemEvent extends AbstractEvent<Cart> {
    @EventData String sku
    @EventData int quantity

    @Override
    void restoreData(Map data) {
        sku = data.sku
        quantity = data.quantity as int
    }
}
