package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.util.Arrays;

public class TeiidCouchbaseClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:CouchbaseVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String[] simpleQuerys = new String[] {
        "SELECT * FROM test",
        "SELECT * FROM test_CreditCard",
        "SELECT * FROM test_Items",
        "SELECT * FROM test_SavedAddresses"
    };


    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        Arrays.asList(simpleQuerys).forEach(query -> {
            try {
                execute(conn, query, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        close(conn);
    }

}
