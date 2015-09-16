package pember.flyway

import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * @author Steve Pember
 */
class FlywayModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FlywayService.class).in(Scopes.SINGLETON)
    }
}
