package pember.db

import com.google.inject.Inject
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultRecordListener
import org.jooq.impl.DefaultRecordListenerProvider
import ratpack.server.Service
import ratpack.server.StartEvent
import ratpack.server.StopEvent

import java.sql.Connection
import java.sql.DriverManager

/**
 * @author Steve Pember
 */
@CompileStatic
@Slf4j
class JooqService implements Service {

    private DatabaseConfig config
    private Connection connection
    private DSLContext ctx

    @Inject JooqService(DatabaseConfig c) {
        config = c
    }

    Connection getConnection() {
        if (!connection) {
            connection = connect()
        }
        connection
    }

    private Connection connect() {
        log.info("Connecting to Database with config: ${config}")
        try {
            connection = DriverManager.getConnection(config.url, config.username, config.password)

        }
        catch (Exception e) {
            log.error("Could not connect: ", e)
        }
        null
    }

    /**
     *
     *
     * @return
     */
    DSLContext create() {
        if (!ctx && getConnection()) {
            log.info("Creating with connection ${getConnection()}")
            ctx = new DefaultDSLContext(
                    new DefaultConfiguration()
                            .derive(getConnection())
                            .derive(SQLDialect.POSTGRES)
//                            .derive(executeListenerProvider())
                            .derive(new DefaultRecordListenerProvider(new DefaultRecordListener()))
//
//                            .derive(settings())
//                            .derive(transactionProvider())
//                            .derive(visitListenerProvider())
            )
            //ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4)
        }
        ctx
    }

    def execute(String sql) {
        connection.createStatement().execute(sql)
    }

    @Override
    void onStart(StartEvent event) throws Exception {
        connect()
    }

    @Override
    void onStop(StopEvent event) throws Exception {
        log.info("Closing DB connection")
        connection.close()
    }
}
