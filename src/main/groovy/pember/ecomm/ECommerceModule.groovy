package pember.ecomm

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import pember.db.JooqService
import pember.ecomm.eventsource.CartAggregateService
import pember.ecomm.eventsource.CartEventSourceService
import pember.ecomm.eventsource.EcommEventService
import pember.ecomm.eventsource.PersonAggregateService
import pember.ecomm.eventsource.PersonEventSourceService
import pember.ecomm.eventsource.ProductAggregateService
import pember.ecomm.eventsource.ProductEventSourceService
import pember.ecomm.eventsource.PurchaseOrderAggregateService
import pember.ecomm.eventsource.PurchaseOrderEventSourceService
import pember.ecomm.services.ProductService

/**
 * @author Steve Pember
 */
class ECommerceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JooqService.class).in(Scopes.SINGLETON)
        bind(EcommEventService.class).in(Scopes.SINGLETON)


        bind(CartAggregateService.class).in(Scopes.SINGLETON)
        bind(CartEventSourceService.class).in(Scopes.SINGLETON)

        bind(PersonAggregateService.class).in(Scopes.SINGLETON)
        bind(PersonEventSourceService.class).in(Scopes.SINGLETON)

        bind(ProductAggregateService.class).in(Scopes.SINGLETON)
        bind(ProductEventSourceService.class).in(Scopes.SINGLETON)

        bind(PurchaseOrderAggregateService.class).in(Scopes.SINGLETON)
        bind(PurchaseOrderEventSourceService.class).in(Scopes.SINGLETON)

        bind(ProductService.class).in(Scopes.SINGLETON)

        bind(ProductUrlMappings.class).in(Scopes.SINGLETON)
        bind(AdminUrlMappings.class).in(Scopes.SINGLETON)
    }
}
