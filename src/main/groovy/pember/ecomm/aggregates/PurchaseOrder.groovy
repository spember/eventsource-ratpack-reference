package pember.ecomm.aggregates

import groovy.transform.CompileStatic
import pember.ecomm.pogo.OrderItem

/**
 * @author Steve Pember
 */
@CompileStatic
class PurchaseOrder extends BaseAggregate {
    UUID personUuid
    List<OrderItem> orderItems
    int priceInCents
    Date date
    String address // IRL this would probably be something more invovled

}
