package org.teiid.test.teiid4437;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

import org.teiid.jdbc.TeiidDriver;

public class TEIID4437Rest {

    public static void main(String[] args) throws Exception {

        Connection conn = TeiidDriver.getInstance().connect("jdbc:teiid:sample@mm://localhost:31000;user=user;password=user", null);
        
        execute(conn, "SELECT * FROM Txns.G1", false);
        execute(conn, "SELECT * FROM Txns.G2", false);
        
        execute(conn, "EXEC g1Table(100, 'abccd')", false);
        execute(conn, "EXEC g2Table()", false);
        
        close(conn);
    }

}
