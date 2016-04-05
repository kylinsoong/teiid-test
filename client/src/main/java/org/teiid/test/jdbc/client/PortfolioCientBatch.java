package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PortfolioCientBatch {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        List<Connection> list = new ArrayList<>();
        
        for(int i = 0 ; i < 10 ; i ++) {
            list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
        }
        
        Thread.sleep(1000 * 60);

        for(Connection conn : list) {
            conn.close();
        }
    }

}
