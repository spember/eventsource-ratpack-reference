package pember.ecomm.eventsource

import org.jooq.Field
import org.jooq.InsertValuesStep4
import org.jooq.Table
import pember.ecomm.aggregates.Cart

import static org.jooq.impl.DSL.table
import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
class CartAggregateService extends BaseAggregateService<Cart> {

    private Table TABLE = table("cart")
    private Field<UUID> PERSON_ID = field('person_id', UUID)

    @Override
    Table aggregateTable() {
        TABLE
    }

//    private List<FieldBinding> customFieldBindings(Cart aggregate) {
//        [
//                new FieldBinding(field: PERSON_ID, value:aggregate.personUuid)
//        ]
//    }

    @Override
    int update(Cart aggregate, int expectedRevision) {
        jooq.create().update(TABLE)
            .set(REVISION, aggregate.revision)
            .set(ACTIVE, aggregate.active)
            .set(PERSON_ID, aggregate.personUuid)
        .where(ID.equal(aggregate.id))
        .and(REVISION.equal(expectedRevision))
        .execute()

    }

    @Override
    int save(List<Cart> aggregates) {
        log.info("Attempting to save {} aggregates", aggregates.size())
        InsertValuesStep4 query = jooq.create().insertInto(TABLE, ID, REVISION, ACTIVE, PERSON_ID)
        aggregates.each {Cart cart->
            query = query.values([cart.id, cart.revision, cart.active, cart.personUuid])
        }
        query.execute()
    }
}
