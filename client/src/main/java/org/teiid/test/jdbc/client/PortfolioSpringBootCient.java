package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class PortfolioSpringBootCient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "redhat";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
//        execute(conn, "SELECT col11, col12, col13, col14, col15 FROM SampleTable", false); 
//        execute(conn, "SELECT * FROM StockPrices", false); 
        execute(conn, "INSERT INTO FOO VALUES (100)", false);
        execute(conn, "SELECT * FROM FOO", false);
        close(conn);
    }

}
