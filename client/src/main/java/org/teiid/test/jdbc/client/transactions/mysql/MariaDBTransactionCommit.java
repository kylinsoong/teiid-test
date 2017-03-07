package org.teiid.test.jdbc.client.transactions.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class MariaDBTransactionCommit {
    
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mariadb://10.66.192.120:3306/teiid";
    private static final String JDBC_USER = "teiid_user";
    private static final String JDBC_PASS = "teiid_pass";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        System.out.println(conn.getAutoCommit());
        
        conn.commit();
        conn.commit();
        conn.commit();
        
        close(conn);
        
    }

}
