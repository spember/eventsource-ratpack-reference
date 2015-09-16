package pember.flyway

import com.google.inject.Inject
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.flywaydb.core.Flyway
import pember.db.DatabaseConfig
import ratpack.server.Service
import ratpack.server.StartEvent

/**
 * @author Steve Pember
 */
@CompileStatic
@Slf4j
class FlywayService implements Service {

    private DatabaseConfig config

    @Inject FlywayService(DatabaseConfig c) {
        config = c
    }

    @Override
    void onStart(StartEvent event) throws Exception {
        log.info("Starting Flywheel Service")
        // Create the Flyway instance
        Flyway flyway = new Flyway();

        // Point it to the database
        flyway.setDataSource(config.url, config.username, config.password);

        // Start the migration
        flyway.migrate();
    }
}
