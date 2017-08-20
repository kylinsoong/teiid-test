package org.teiid.test.jdbc.client.petrochina;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class EpBankClient {

    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:test10-1-1-1-vdb@mm://11.11.208.135:31100;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    
    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM SYSADMIN.MatViews");
        
        close(conn);
    }

}
