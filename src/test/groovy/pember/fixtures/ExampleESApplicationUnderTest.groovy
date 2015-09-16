package pember.fixtures

import groovy.transform.CompileStatic
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.guice.Guice
import ratpack.registry.Registry
import ratpack.remote.RemoteControl

/**
 * @author Steve Pember
 */
@CompileStatic
class ExampleESApplicationUnderTest extends GroovyRatpackMainApplicationUnderTest {
    protected Registry createOverrides(Registry serverRegistry) throws Exception {
        return Guice.registry {
            it.bindInstance RemoteControl.handlerDecorator()
        }.apply(serverRegistry)
    }
}
