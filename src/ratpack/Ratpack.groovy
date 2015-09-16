package ratpack

import pember.db.DatabaseConfig
import pember.ecomm.AdminUrlMappings
import pember.ecomm.ECommerceModule
import pember.ecomm.ProductUrlMappings

import pember.flyway.FlywayModule
import ratpack.func.Action
import ratpack.groovy.template.TextTemplateModule
import ratpack.rx.RxRatpack
import ratpack.server.ServerConfig
import ratpack.server.Service
import ratpack.server.StartEvent

import static ratpack.groovy.Groovy.ratpack
import static ratpack.groovy.Groovy.groovyTemplate

/**
 * @author Steve Pember
 */
ratpack {

    bindings {
        println "hav environment of ${System.getProperty("app.env")}"
        ServerConfig serverConfig = ServerConfig.builder()
                .onError(Action.throwException()).yaml("$serverConfig.baseDir.file/config.yaml")
                .onError(Action.noop()).yaml("$serverConfig.baseDir.file/config_${System.getProperty("app.env")}.yaml")
                .onError(Action.noop()).yaml("$serverConfig.baseDir.file/../../config-local.yaml")
                .build()

        bindInstance(DatabaseConfig, serverConfig.get("/db", DatabaseConfig))

        module FlywayModule
        module TextTemplateModule, { TextTemplateModule.Config config -> config.staticallyCompile = true }
        module ECommerceModule

        bindInstance Service, new Service() {
            @Override
            void onStart(StartEvent event) throws Exception {
                RxRatpack.initialize()
            }
        }
    }

    handlers {
        fileSystem "assets", { f -> f.files() }
        prefix("api/products") {
            all chain(registry.get(ProductUrlMappings))
        }

        prefix("admin") {
            all chain(registry.get(AdminUrlMappings))

        }

        get {
            render groovyTemplate("index.html", title: "Das Coffee Haus")
        }



    }
}
