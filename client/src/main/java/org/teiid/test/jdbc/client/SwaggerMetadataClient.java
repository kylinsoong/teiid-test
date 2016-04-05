package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.CallableStatement;
import java.sql.Connection;

public class SwaggerMetadataClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:restwebservice@mm://localhost:31100;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    private static String[] calls = new String[] {"EXEC customer_customerList()",
        "EXEC customer_getAll()",
        "EXEC customer_getByNumber('161')",
        "EXEC customer_getByCity('Burlingame')",
        "EXEC customer_getByCountry('USA')"};

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        for(String call : calls) {
            CallableStatement cStmt = conn.prepareCall(call);
            cStmt.execute();
            Object blob = cStmt.getObject(1);
            System.out.println(blob);
        }
        
        System.out.println(conn);
    }

}
