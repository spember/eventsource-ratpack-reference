package pember.ecomm

import pember.ecomm.aggregates.Product
import ratpack.groovy.handling.GroovyChainAction

import static ratpack.groovy.Groovy.groovyTemplate

/**
 * @author Steve Pember
 */
class AdminUrlMappings extends GroovyChainAction {

    @Override
    void execute() throws Exception {
        get {
            render groovyTemplate("admin/index.html")
        }
        get("events") {
            render groovyTemplate("admin/events.html")
        }
        prefix("products") {
            get {
                render groovyTemplate("admin/products/index.html")
            }
            get("create") {
                render groovyTemplate("admin/products/edit.html", title: "Add New Product", product: new Product(), submitUrl: "/admin/products/save")
            }
        }
    }
}
