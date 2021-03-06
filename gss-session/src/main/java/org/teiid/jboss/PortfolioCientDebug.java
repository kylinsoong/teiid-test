package org.teiid.jboss;

import static org.teiid.jboss.JDBCUtils.execute;
import static org.teiid.jboss.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PortfolioCientDebug {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String sql_Stock = "SELECT * FROM Stock";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, sql_Stock, true);
        
    }

}
