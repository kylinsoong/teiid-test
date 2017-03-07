package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;
import static org.teiid.test.utils.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PrestoConnectionLab {
    
    private static final String JDBC_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
    private static final String JDBC_URL = "jdbc:presto://dvqe07.mw.lab.eng.bos.redhat.com:8888/mysql/bqt2";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";


    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SHOW CATALOGS");
        
        execute(conn, "SHOW SCHEMAS FROM mysql");
        execute(conn, "SHOW SCHEMAS FROM system");   
        
        execute(conn, "SHOW TABLES FROM mysql.information_schema");
        execute(conn, "SHOW TABLES FROM system.information_schema");
        
        close(conn);
    }

}
