package org.jboss.teiid.test.transacton;

import static org.jboss.teiid.test.transacton.utils.JDBCUtils.getDriverConnection;
import static org.jboss.teiid.test.transacton.utils.JDBCUtils.execute;

import java.sql.Connection;

public class MariaDBJDBCClient {
    
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String JDBC_URL = "jdbc:mariadb://localhost:3306/products";
    static final String JDBC_USER = "jdv_user";
    static final String JDBC_PASS = "jdv_pass";
    

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        execute(conn, "show tables", true);
    }

}
