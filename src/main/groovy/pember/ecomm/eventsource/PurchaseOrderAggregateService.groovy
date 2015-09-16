package pember.ecomm.eventsource

import groovy.transform.CompileStatic
import org.jooq.Field
import org.jooq.InsertValuesStep5
import org.jooq.Table
import pember.ecomm.aggregates.PurchaseOrder

import java.sql.Timestamp

import static org.jooq.impl.DSL.table
import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
@CompileStatic
class PurchaseOrderAggregateService extends BaseAggregateService<PurchaseOrder>{

    private Table TABLE = table('purchase_order')

    private Field<UUID> PERSON_ID = field('person_id', UUID)
    private Field<Timestamp> DATE = field('date', Timestamp)

    @Override
    Table aggregateTable() {
        TABLE
    }

    @Override
    int update(PurchaseOrder aggregate, int expectedRevision) {
        jooq.create().update(TABLE)
                .set(REVISION, aggregate.revision)
                .set(ACTIVE, aggregate.active)
                .set(PERSON_ID, aggregate.personUuid)
                .set(DATE, aggregate.date)
                .where(ID.equal(aggregate.id))
                .and(REVISION.equal(expectedRevision))
                .execute()
    }

    @Override
    int save(List<PurchaseOrder> aggregates) {
        log.info("Attempting to save {} aggregates", aggregates.size())
        InsertValuesStep5 query = jooq.create().insertInto(TABLE, ID, REVISION, ACTIVE, PERSON_ID, DATE)
        aggregates.each {PurchaseOrder po->
            query = query.values([po.id, po.revision, po.active, po.personUuid, po.date])
        }
        query.execute()
    }
}
