package org.teiid.test.jdbc.client.transactions;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class TestLocalTempTable {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TransactionsExamplesVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM MariaDB1Schema.skills", false);
        
        execute(conn, "INSERT INTO #t SELECT id FROM MariaDB1Schema.skills", false);
        
        execute(conn, "SELECT * FROM #t", false);
        
        close(conn);
    }

}
