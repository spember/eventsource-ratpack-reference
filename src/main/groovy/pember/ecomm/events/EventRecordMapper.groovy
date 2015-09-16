package pember.ecomm.events

import com.thirdchannel.eventsource.Event
import groovy.json.JsonSlurper
import org.jooq.Record
import org.jooq.RecordMapper

/**
 * @author Steve Pember
 */
class EventRecordMapper implements RecordMapper<Record, Event> {
    @Override
    Event map(Record record) {
        // todo: find a cleaner way to load the record back into the Event class. Here, we extract each record into a map
        // then use groovy's map loader to parse it into the object, which works alright... but it'd be
        // nice if we could do it straight from the record.
        // part of the issue is that the record has snake_case naming and is tricky to parse into camelCase variables on the class
        Event e = (Event)Event.classLoader.loadClass(record.getValue("clazz").toString()).newInstance(convertRecordToMap(record))
        e.restoreData(new JsonSlurper().parseText(e.data) as Map)
        e
    }

    private Map convertRecordToMap(Record record) {
        [
            id:          record.getValue('id'),
            date:        record.getValue('date'),
            aggregateId: record.getValue('aggregate_id'),
            revision:    record.getValue('revision'),
            data:        record.getValue('data'),
            userId:      record.getValue('user_id')
    ]}
}
