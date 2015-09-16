package pember.ecomm.eventsource

import com.thirdchannel.eventsource.Aggregate
import com.thirdchannel.eventsource.AggregateService
import groovy.util.logging.Slf4j
import org.jooq.Field
import org.jooq.Table
import pember.db.JooqService

import java.lang.reflect.ParameterizedType

import static org.jooq.impl.DSL.field

/**
 * @author Steve Pember
 */
@Slf4j
abstract class BaseAggregateService<A extends  Aggregate> implements AggregateService<A> {

    protected JooqService jooq

    protected Field<UUID> ID = field('id', UUID)
    protected Field<Integer> REVISION = field('revision', Integer)
    protected Field<Boolean> ACTIVE = field('active', Boolean)

    private Class<A> typeOfA

    BaseAggregateService() {
        // this totally feels like a hack: obtaining the class of a Generic at run time.
        // will only work if your aggregate classes directly subclass A
        // thanks stack overflow! http://stackoverflow.com/questions/4837190/java-generics-get-class
        this.typeOfA = (Class)((ParameterizedType)getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0]
        log.debug("A is ${typeOfA}, ${typeOfA.name}")
    }

    abstract Table aggregateTable()

    List<A> list() {
        jooq.create().selectFrom(aggregateTable()).fetchInto(typeOfA)
    }

    @Override
    int save(A aggregate) {
        save([aggregate])
    }

    A getOrCreate(UUID id) {
        A a = get(id)
        if (!a) {
            a = A.classLoader.loadClass(typeOfA.name).newInstance()
            a.id = id
        }
        a
    }
    @Override
    A get(UUID id) {
        jooq.create().selectFrom(aggregateTable()).where(ID.equal(id)).fetchOne().into(typeOfA)
    }

    @Override
    List<A> getAll(List<UUID> ids) {
        jooq.create().selectFrom(aggregateTable()).where(ID.in(ids)).fetchInto(typeOfA)
    }

    @Override
    boolean exists(UUID id) {
        jooq.create().fetchCount(jooq.create().selectFrom(aggregateTable()).where(ID.equal(id)))
    }

    @Override
    int getCurrentRevision(UUID id) {
        jooq.create().select(REVISION).where(ID.equal(id)).fetchInto(Integer)[0]
    }
}
