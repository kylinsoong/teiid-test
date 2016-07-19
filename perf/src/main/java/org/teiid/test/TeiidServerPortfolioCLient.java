package org.teiid.test;

import static org.teiid.test.util.JDBCUtils.*;

import java.sql.Connection;

public class TeiidServerPortfolioCLient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        System.out.println(conn);
        
        execute(conn, "SELECT * FROM ITEMS", true);
    }

}
