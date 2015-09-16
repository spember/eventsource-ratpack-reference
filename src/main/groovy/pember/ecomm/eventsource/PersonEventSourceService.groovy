package pember.ecomm.eventsource

import com.google.inject.Inject
import com.thirdchannel.eventsource.EventSourceService
import groovy.transform.CompileStatic
import pember.ecomm.aggregates.Person

/**
 * @author Steve Pember
 */
@CompileStatic
class PersonEventSourceService extends EventSourceService<Person> {
    @Inject ProductEventSourceService(PersonAggregateService ps, EcommEventService es) {
        aggregateService = ps
        eventService = es
    }

    List<Person> list() {
        ((PersonAggregateService)aggregateService).list()
    }
}
