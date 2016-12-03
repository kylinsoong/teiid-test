package org.teiid.test.teiid4437;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class TEIID4437Rest91 {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:sample@mm://localhost:31000";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM Txns.G1", false);
        execute(conn, "SELECT * FROM Txns.G2", false);
        
        execute(conn, "EXEC g1Table(100, 'abccd')", false);
        execute(conn, "EXEC g2Table()", false);

        close(conn);
    }

}
