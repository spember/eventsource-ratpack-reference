package pember.ecomm.aggregates

import groovy.transform.CompileStatic

/**
 * @author Steve Pember
 */
@CompileStatic
class Person extends BaseAggregate {
    String name
    String email
    String address // this would likely not be a raw string in an actual system; it may, in fact, be a pogo depending on this Person
        // which would then make the Person a root aggregate, containing several child aggregates
    int centsSpent

}
