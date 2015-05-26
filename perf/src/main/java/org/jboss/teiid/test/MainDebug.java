package org.jboss.teiid.test;

import static org.jboss.teiid.test.perf.Constants.H2_JDBC_DRIVER;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_PASS;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_URL;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;

import org.teiid.test.util.JDBCUtils;

public class MainDebug {

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(id) AS total_rows FROM PERFTEST");
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(id) AS total_rows FROM PERFTEST_MAT");
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(id) AS total_rows FROM PERFTEST_STAGING");
        JDBCUtils.close(conn);
    }

}
