package pember.ecomm.eventsource

import com.google.inject.Inject
import com.thirdchannel.eventsource.EventSourceService
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Cart

/**
 * @author Steve Pember
 */
@CompileStatic
class CartEventSourceService extends EventSourceService<Cart> {

    @Inject CartEventSourceService(CartAggregateService cs, EcommEventService e) {
        aggregateService = cs
        eventService = e
    }
}
