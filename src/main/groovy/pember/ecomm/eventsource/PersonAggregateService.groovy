package pember.ecomm.eventsource

import com.google.inject.Inject
import org.jooq.Field
import org.jooq.InsertValuesStep5
import org.jooq.Table
import pember.db.JooqService
import pember.ecomm.aggregates.Person

import static org.jooq.impl.DSL.table
import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
class PersonAggregateService extends BaseAggregateService<Person> {

    private Table TABLE = table('person')

    private Field<String> EMAIL = field('email', String)
    private Field<String> NAME = field('name', String)


    @Inject PersonAggregateService(JooqService js) {
        jooq = js
        super
    }

    @Override
    Table aggregateTable() {
        TABLE
    }

    @Override
    int update(Person aggregate, int expectedRevision) {
        jooq.create().update(TABLE)
                .set(REVISION, aggregate.revision)
                .set(ACTIVE, aggregate.active)
                .set(EMAIL, aggregate.email)
                .set(NAME, aggregate.name)
                .where(ID.equal(aggregate.id))
                .and(REVISION.equal(expectedRevision))
                .execute()
    }

    @Override
    int save(List<Person> aggregates) {
        log.info("Attempting to save {} aggregates", aggregates.size())
        InsertValuesStep5 query = jooq.create().insertInto(TABLE, ID, REVISION, ACTIVE, EMAIL, NAME)
        aggregates.each {Person person->
            query = query.values([person.id, person.revision, person.active, person.email, person.name])
        }
        query.execute()
    }
}
