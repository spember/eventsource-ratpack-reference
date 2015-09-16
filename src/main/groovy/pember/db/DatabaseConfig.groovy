package pember.db

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @author Steve Pember
 */
@ToString
@CompileStatic
class DatabaseConfig {
    String url
    String username
    String password
}
