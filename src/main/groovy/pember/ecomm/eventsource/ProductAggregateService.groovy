package pember.ecomm.eventsource

import com.google.inject.Inject

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jooq.Field
import org.jooq.InsertValuesStep5
import org.jooq.Table
import pember.db.JooqService
import pember.ecomm.aggregates.Product

import static org.jooq.impl.DSL.table
import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
@CompileStatic
@Slf4j
class ProductAggregateService extends BaseAggregateService<Product> {

    private Table TABLE = table("product")
    private Field<String> SKU = field('sku', String)
    private Field<Integer> QUANTITY = field('quantity', Integer)

    @Inject ProductAggregateService(JooqService js) {
        jooq = js
        super
    }

    @Override
    Table aggregateTable() { TABLE }

//    @Override
//    Product getOrCreate(UUID id) {
//        Product p = get(id)
//        if (!p) {
//            p = new Product()
//            p.id = id
//        }
//        p
//    }

    @Override
    int update(Product aggregate, int expectedRevision) {
        jooq.create().update(TABLE)
            .set(REVISION, aggregate.revision)
            .set(ACTIVE, aggregate.active)
            .set(SKU, aggregate.sku)
            .set(QUANTITY, aggregate.quantity)
        .where(ID.equal(aggregate.id))
        .and(REVISION.equal(expectedRevision))
        .execute()
    }


    @Override
    int save(List<Product> aggregates) {
        log.info("Attempting to save {} aggregates", aggregates.size())
        InsertValuesStep5 query = jooq.create().insertInto(TABLE, ID, REVISION, ACTIVE, SKU, QUANTITY )
        aggregates.each {Product product->
            query = query.values(product.id, product.revision, product.active, product.sku, product.quantity)
        }
        query.execute()
    }
}
