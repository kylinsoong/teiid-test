package org.teiid.test.sql.join.mariadb;

import static org.teiid.test.jdbc.client.util.JDBCUtils.*;

import java.sql.Connection;

public class JoinSamples {

    public static void main(String[] args) throws Exception {

        Connection conn = PING.newConnection();
        
        execute(conn, "SELECT * FROM T1 INNER JOIN T2 ON T1.a = T2.b");
        execute(conn, "SELECT * FROM T1 LEFT OUTER JOIN T2 ON T1.a = T2.b");
        execute(conn, "SELECT * FROM T1 RIGHT OUTER JOIN T2 ON T1.a = T2.b");
        execute(conn, "SELECT * FROM T1 CROSS JOIN T2", true);
    }

}
