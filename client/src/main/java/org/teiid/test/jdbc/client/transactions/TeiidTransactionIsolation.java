package org.teiid.test.jdbc.client.transactions;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * http://www.java2s.com/Tutorial/Java/0340__Database/TurningOnAutocommitMode.htm
 * 
 * @author kylin
 *
 */
public class TeiidTransactionIsolation {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TransactionsExamplesVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);  

        System.out.println("Default transaction isolation level: " + conn.getTransactionIsolation());
//        
        supportedIsolationLevels(conn);
//        
        supportTransactions(conn);
        
        dirtyReads();
        
        close(conn);
    }
    

    private static void dirtyReads() throws Exception {

        //Create dirty data
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO MariaDB1Schema.account (id, balancer) VALUES (2, 100)");
        stmt.executeUpdate("INSERT INTO MariaDB1Schema.account (id, balancer) VALUES (3, 100)");
        
        Connection connRead = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        connRead.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        
        execute(connRead, "select * from MariaDB1Schema.account", true);
        
        conn.rollback();
        close(stmt, conn);
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

}
