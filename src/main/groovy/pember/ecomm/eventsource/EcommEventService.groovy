package pember.ecomm.eventsource

import com.google.inject.Inject
import com.thirdchannel.eventsource.Aggregate
import com.thirdchannel.eventsource.Event
import com.thirdchannel.eventsource.EventService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jooq.Field
import org.jooq.InsertValuesStepN
import org.jooq.Record
import org.jooq.Table
import pember.db.JooqService
import pember.ecomm.events.EventRecordMapper

import java.sql.Timestamp

import static org.jooq.impl.DSL.table
import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class EcommEventService implements EventService {

    @Delegate private JooqService jooq

    private static final Table<Record> TABLE = table('event')
    private EventRecordMapper eventRecordMapper = new EventRecordMapper()

    // should probably use Jooq's generated code
    private Field<UUID> ID = field('id', UUID)
    private Field<UUID> AGGREGATE_ID = field('aggregate_id', UUID)
    private Field<Integer> REVISION = field('revision', Integer)
    private Field<String> CLAZZ = field('clazz', String)
    private Field<Timestamp> DATE = field('date', Timestamp)
    private Field<Timestamp> DATE_EFFECTIVE = field('date_effective', Timestamp)
    private Field<String> USER = field('user_id', String)
    private Field<String> DATA = field('data', String)
    //protected static final List<org.jooq.Field> BASE_FIELDS =
    //['id', 'aggregate_id', 'revision', 'date', 'date_effective', 'user_id', 'data', 'clazz'].collect { DSL.field(it) }
    private List<Field> BASE_FIELDS = [ID, AGGREGATE_ID, REVISION, DATE, DATE_EFFECTIVE, USER, DATA, CLAZZ]


    @Inject EcommEventService(JooqService js) {
        jooq = js
    }

    @Override
    List<Event> findAllEventsForAggregate(Aggregate aggregate) {
        create().select().from(TABLE)
                .where(AGGREGATE_ID.equal(aggregate.id))
                .orderBy(DATE_EFFECTIVE.asc()).fetch()
                .map(eventRecordMapper)

    }

    @Override
    List<Event> findAllEventsForAggregates(List<? extends Aggregate> aggregates) {
        create().select().from(TABLE).where(AGGREGATE_ID.in(aggregates*.id))
            .orderBy(DATE_EFFECTIVE.asc()).fetch()
            .map(eventRecordMapper)
    }

    @Override
    List<Event> findAllEventsForAggregateInRange(Aggregate aggregate, Date begin, Date end) {
        return null
    }

    @Override
    List<Event> findAllEventsForAggregatesInRange(List<? extends Aggregate> aggregate, Date begin, Date end) {
        return null
    }

    @Override
    List<Event> findAllEventsForAggregateUpToRevision(Aggregate aggregate, int revision) {
        return null
    }

    @Override
    List<Event> findAllEventsForAggregateUpToDateEffective(Aggregate aggregate, Date date) {
        create().select().from(TABLE).where(AGGREGATE_ID.equal(aggregate.id))
        .and(DATE_EFFECTIVE.lessOrEqual(date.toTimestamp()))
        .orderBy(DATE_EFFECTIVE.asc()).fetch()
        .map(eventRecordMapper)
    }

    @Override
    boolean save(Event event) {
        save([event])
    }

    @Override
    boolean save(List<? extends Event> events) {
        InsertValuesStepN query = jooq.create()insertInto(TABLE, BASE_FIELDS)
        events.each {Event event->
            event.userId="test@test.com" // for this dummy example, use the same data
            log.debug("Submitting event with data {}",event.data)
            query = query.values(event.id, event.aggregateId, event.revision, new Timestamp(event.date.time),
                    new Timestamp(event.dateEffective.time), event.userId, event.data, event.getClass().name)
        }
        log.info("Attempting to save {} events", events.size())
        query.execute() > 0

    }
}
