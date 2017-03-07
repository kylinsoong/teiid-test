package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class PrestoConnection {
    
    private static final String JDBC_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
    private static final String JDBC_URL = "jdbc:presto://localhost:8080";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "sa";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        System.out.println(conn.getMetaData().getDatabaseProductName() + " - " + conn.getMetaData().getDatabaseProductVersion());
        
//        test_1(conn);
        
        execute(conn, "SHOW CATALOGS");
        execute(conn, "SHOW SCHEMAS FROM mysql");
        execute(conn, "SHOW SCHEMAS FROM system");   
        
        execute(conn, "SHOW TABLES FROM mysql.information_schema");
//        execute(conn, "SHOW COLUMNS FROM mysql.information_schema.__internal_partitions__");
        
             
        execute(conn, "SHOW TABLES FROM system.information_schema");
               
        close(conn);
        
        
    }

    static void test_1(Connection conn) throws Exception {

        execute(conn, "SHOW SCHEMAS FROM mysql");
        execute(conn, "SHOW TABLES FROM mysql.teiid");
        execute(conn, "DESCRIBE mysql.teiid.account");
        execute(conn, "SHOW COLUMNS FROM mysql.teiid.account");
        execute(conn, "SELECT * FROM mysql.teiid.account");
        
        execute(conn, "SHOW SCHEMAS FROM jmx");
        execute(conn, "SHOW TABLES FROM jmx.current");
        execute(conn, "SELECT node, vmname, vmversion FROM jmx.current.\"java.lang:type=runtime\"");
        execute(conn, "SELECT openfiledescriptorcount, maxfiledescriptorcount FROM jmx.current.\"java.lang:type=operatingsystem\"");
        execute(conn, "SELECT timestamp, uptime FROM jmx.history.\"java.lang:type=runtime\"");
    }

}
