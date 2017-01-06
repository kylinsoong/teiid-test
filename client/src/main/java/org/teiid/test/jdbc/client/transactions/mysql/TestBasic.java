package org.teiid.test.jdbc.client.transactions.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class TestBasic {
    
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mariadb://10.66.192.120:3306/teiid";
    private static final String JDBC_USER = "teiid_user";
    private static final String JDBC_PASS = "teiid_pass";
    
    static String SQL_QUERY_COUNT = "SELECT COUNT(id) AS TOTAL_SKILLS FROM skills";

    public static void main(String[] args) throws Exception {
        
        count(SQL_QUERY_COUNT);

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);       
        conn.setAutoCommit(false);

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO skills(id, name) VALUES(4, 'teiid')");
            stmt.close();
            count(SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        count(SQL_QUERY_COUNT);
        
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM skills WHERE id = 4");
            stmt.close();
            count(SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        close(conn);
        
        count(SQL_QUERY_COUNT);
    }
    
    static void count(String sql) throws Exception {
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData meta = rs.getMetaData();
        String name = meta.getColumnName(1);
        rs.next();
        int value = rs.getInt(1);
        System.out.println(name + ": " + value + "\n");
        close(rs, stmt, conn);
    }

}
