package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class PortfolioCientTeiid4622 {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT random('asdaf')", false);
        
        execute(conn, "SELECT encryptStr('12345', 'redhat')", false);
        
        execute(conn, "SELECT decryptStr('/TKCHCrbMKSBaQM7VGOQBw==', 'redhat')", false);
        
        execute(conn, "SELECT whitespaceIndex('asd af')", false);
        
        execute(conn, "SELECT cosineDistance('123', 'abc1')", false);
        
        close(conn);
    }

}
