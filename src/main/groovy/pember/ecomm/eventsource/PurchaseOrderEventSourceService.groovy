package pember.ecomm.eventsource

import com.google.inject.Inject
import com.thirdchannel.eventsource.EventSourceService
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.PurchaseOrder

/**
 * @author Steve Pember
 */
@CompileStatic
class PurchaseOrderEventSourceService extends EventSourceService<PurchaseOrder> {

    @Inject PurchaseOrderEventSourceService(PurchaseOrderAggregateService poas, EcommEventService e) {
        aggregateService = poas
        eventService = e
    }
}
