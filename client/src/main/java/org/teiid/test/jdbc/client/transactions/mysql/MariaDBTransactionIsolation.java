package org.teiid.test.jdbc.client.transactions.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * https://db.apache.org/derby/docs/10.3/devguide/cdevconcepts15366.html
 * 
 * @author kylin
 *
 */
public class MariaDBTransactionIsolation {
    
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mariadb://10.66.192.120:3306/teiid";
    private static final String JDBC_USER = "teiid_user";
    private static final String JDBC_PASS = "teiid_pass";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);  

        System.out.println("Default transaction isolation level: " + conn.getTransactionIsolation());
        
        supportedIsolationLevels(conn);
        
        supportTransactions(conn);
        
//        rollbackToSavepoint(conn);
        
//        dirtyReads();
        nonRepeatableReads();
        
        close(conn);
    }

    static void nonRepeatableReads() throws Exception {

        init();
        
        Connection connRead = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        connRead.setAutoCommit(false);
        
        // Make Non-Repeatable possible
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE account SET balancer = 150 WHERE id = 1");
        
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            execute(connRead, "select * from account", false);
        }
        
        conn.rollback();
        connRead.rollback();
        close(stmt, conn);
        close(connRead);
    }

    private static void init() throws Exception {
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);  
        execute(conn, "UPDATE account SET balancer = 100 WHERE id = 1", true);
    }

    static void dirtyReads() throws Exception {
    
        // Create dirty data
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO account (id, balancer) VALUES (2, 100)");
        stmt.executeUpdate("INSERT INTO account (id, balancer) VALUES (3, 100)");
        
        
        Connection connRead = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            execute(connRead, "select * from account", false);
        }
        
        if(connRead.getMetaData().supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)){
            connRead.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            execute(connRead, "select * from account", false);
        }
        
        releaseLock(conn);
        
        close(stmt, conn);
        close(connRead);
    }

    private static void releaseLock(Connection conn) throws SQLException {
        conn.rollback();
    }

    static void rollbackToSavepoint(Connection conn) throws Exception {

        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        
        stmt.executeUpdate("INSERT INTO account (id, balancer) VALUES (2, 100)");
        stmt.executeUpdate("INSERT INTO account (id, balancer) VALUES (3, 100)");
        
        Savepoint mySavepoint = conn.setSavepoint();
        
        stmt.executeUpdate("INSERT INTO account (id, balancer) VALUES (4, 100)");
        
        count("Before commit", "SELECT COUNT(id) AS Total_Accounts FROM account");
        
        conn.commit();
        
        count(" After commit", "SELECT COUNT(id) AS Total_Accounts FROM account");
        
        conn.rollback(mySavepoint);
        
        count("rollback to Savepoint", "SELECT COUNT(id) AS Total_Accounts FROM account");
        
        stmt.executeUpdate("DELETE FROM account WHERE id = 2");
        stmt.executeUpdate("DELETE FROM account WHERE id = 3");
        
        conn.commit();
        
        close(stmt, conn);
        
        count(" Finally", "SELECT COUNT(id) AS Total_Accounts FROM account");
    }

    static void supportTransactions(Connection conn) throws SQLException {
        System.out.println();
        DatabaseMetaData dbMd = conn.getMetaData();  
        System.out.println("supportsTransactions: " + dbMd.supportsTransactions());
    }


    static void supportedIsolationLevels(Connection conn) throws SQLException {      
        System.out.println();
        DatabaseMetaData dbMd = conn.getMetaData();        
        System.out.println("TRANSACTION_NONE: " + dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE));
        System.out.println("TRANSACTION_READ_UNCOMMITTED: " + dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED));
        System.out.println("TRANSACTION_READ_COMMITTED: " + dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED));
        System.out.println("TRANSACTION_REPEATABLE_READ: " + dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ));
        System.out.println("TRANSACTION_SERIALIZABLE: " + dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));
    }
    
    private static void count(String msg, String sql) throws Exception {
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData meta = rs.getMetaData();
        String name = meta.getColumnName(1);
        rs.next();
        int value = rs.getInt(1);
        System.out.println(msg + ", " + name + ": " + value);
        close(rs, stmt, conn);
    }

}
