package pember.ecomm.eventsource

import com.google.inject.Inject
import com.thirdchannel.eventsource.AggregateService
import com.thirdchannel.eventsource.EventSourceService
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Product

/**
 * @author Steve Pember
 */
@CompileStatic
class ProductEventSourceService extends EventSourceService<Product> {

    @Inject ProductEventSourceService(ProductAggregateService ps, EcommEventService es) {
        aggregateService = ps
        eventService = es
    }

    List<Product> list() {
        ((ProductAggregateService)aggregateService).list()
    }


}
