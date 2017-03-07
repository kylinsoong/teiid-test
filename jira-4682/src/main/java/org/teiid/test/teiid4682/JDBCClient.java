package org.teiid.test.teiid4682;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class JDBCClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PrestoDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        execute(conn, "CREATE LOCAL TEMPORARY TABLE #t (a integer)", false);
//        execute(conn, "INSERT INTO #t VALUES(1)");
//        execute(conn, "SELECT * FROM #t");
//        
//        execute(conn, "SELECT 'Hello World'");
//        execute(conn, "INSERT INTO #t SELECT 'Hello World'");
//        execute(conn, "SELECT * FROM #t");
        
        execute(conn, "INSERT INTO #t SELECT IntKey FROM Bqt1.Smalla");
        
        close(conn);

    }

}
