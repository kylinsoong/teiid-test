package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;
import static org.teiid.test.jdbc.client.JDBCUtils.execute;

import java.sql.Connection;

public class PortfolioCientEmbedded {
	
	static {
		System.setProperty("org.teiid.sockets.DisablePing", "true");
	}
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1;fetchSize=8";
    private static final String JDBC_USER = "testUser";
    private static final String JDBC_PASS = "password";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM Product", true);
    }

}
