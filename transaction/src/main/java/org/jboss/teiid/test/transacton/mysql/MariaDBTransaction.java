package org.jboss.teiid.test.transacton.mysql;

import static org.jboss.teiid.test.transacton.utils.JDBCUtils.execute;
import static org.jboss.teiid.test.transacton.utils.JDBCUtils.close;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.jboss.teiid.test.transacton.XidImpl;
import org.mariadb.jdbc.MariaDbDataSource;

public class MariaDBTransaction {

    /*
     * https://www.ibm.com/support/knowledgecenter/ssw_i5_54/rzaha/jtatxend.htm
     */
    
    static final String DatabaseName = "products";
    static final String ServerName = "localhost";
    static final Integer PortNumber = 3306;
    static final String JDBC_USER = "jdv_user";
    static final String JDBC_PASS = "jdv_pass";
    
    public static MariaDbDataSource newDataSource() {
        
        MariaDbDataSource ds = new MariaDbDataSource();
        ds.setDatabaseName(DatabaseName);
        ds.setServerName(ServerName);
        ds.setPortNumber(PortNumber);
        ds.setUser(JDBC_USER);
        ds.setPassword(JDBC_PASS);
        
        return ds;
    }
    
    public static void main(String[] args) throws SQLException, Exception {
        
        test_ping();
        
        test_setup();
        
        test_JTACommit();
        
        test_JTARollback();
        
        test_JTAMultiConn();
    }

    static void test_JTAMultiConn() throws Exception {
        
        MariaDbDataSource ds = newDataSource();
        
        Connection c1 = null;
        Connection c2 = null;
        Connection c3 = null;
        
        // Obtain an XAConnection object that contains an XAResource and a Connection object.
        XAConnection xaConn1 = ds.getXAConnection();
        XAConnection xaConn2 = ds.getXAConnection();
        XAConnection xaConn3 = ds.getXAConnection();
        XAResource xaRes1 = xaConn1.getXAResource();
        XAResource xaRes2 = xaConn2.getXAResource();
        XAResource xaRes3 = xaConn3.getXAResource();
        c1 = xaConn1.getConnection();
        c2 = xaConn2.getConnection();
        c3 = xaConn3.getConnection();
        
        Statement stmt1 = c1.createStatement();
        Statement stmt2 = c2.createStatement();
        Statement stmt3 = c3.createStatement();
        
        // For XA transactions, a transaction identifier is required.
        //  An implementation of the XID interface is not included with the JDBC driver.
        Xid xid = new XidImpl(1, "globalTransactionId".getBytes(), "branchQualifier".getBytes());
        
        // Perform some transactional work under each of the three connections that have been created.
        xaRes1.start(xid, XAResource.TMNOFLAGS);
        stmt1.executeUpdate("INSERT INTO JTATABLE VALUES('Value 1-A')");
        xaRes1.end(xid, XAResource.TMSUCCESS);
        
        xaRes2.start(xid, XAResource.TMNOFLAGS);
        stmt2.executeUpdate("INSERT INTO JTATABLE VALUES('Value 1-B')");
        xaRes2.end(xid, XAResource.TMSUCCESS);
        
        xaRes3.start(xid, XAResource.TMNOFLAGS);
        stmt3.executeUpdate("INSERT INTO JTATABLE VALUES('Value 1-C')");
        xaRes3.end(xid, XAResource.TMSUCCESS);
        
        /*
         * When completed, commit the transaction as a single unit.
         * 
         * A prepare() and commit() or 1 phase commit() is required for 
         * each separate database (XAResource) that participated in the
         * transaction. Since the resources accessed (xaRes1, xaRes2, and xaRes3)
         * all refer to the same database, only one prepare or commit is required.
         */
        xaRes1.prepare(xid);
        xaRes1.commit(xid, false);
        
        execute(ds.getConnection(), "SELECT * FROM JTATABLE", false);
    }

    static void test_JTARollback() throws Exception {

        MariaDbDataSource ds = newDataSource();
        
        // Obtain an XAConnection object that contains an XAResource and a Connection object.
        XAConnection  xaConn = ds.getXAConnection();
        XAResource    xaRes  = xaConn.getXAResource();
        Connection    conn   = xaConn.getConnection();
        
        // For XA transactions, a transaction identifier is required.
        //  An implementation of the XID interface is not included with the JDBC driver.
        Xid xid = new XidImpl(1, "globalTransactionId".getBytes(), "branchQualifier".getBytes());
        
        Statement stmt = conn.createStatement();
        
        // The XA resource must be notified before starting any transactional work.
        xaRes.start(xid, XAResource.TMNOFLAGS);
        
        stmt.executeUpdate("INSERT INTO JTATABLE VALUES('JTA is pretty fun. roolback')");
        
        // When the transaction work has completed, the XA resource must again be notified.
        xaRes.end(xid, XAResource.TMSUCCESS);
        
        xaRes.prepare(xid);
        
        // The transaction is rollback through the XAResource.
        // The JDBC Connection object is not used to rollback the transaction when using JTA.
        xaRes.rollback(xid);
        
        execute(conn, "SELECT * FROM JTATABLE", false);
        
        close(conn);
    }

    static void test_JTACommit() throws Exception {

        MariaDbDataSource ds = newDataSource();
        
        // Obtain an XAConnection object that contains an XAResource and a Connection object.
        XAConnection  xaConn = ds.getXAConnection();
        XAResource    xaRes  = xaConn.getXAResource();
        Connection    conn   = xaConn.getConnection();
        
        // For XA transactions, a transaction identifier is required.
        //  An implementation of the XID interface is not included with the JDBC driver.
        Xid xid = new XidImpl(1, "globalTransactionId".getBytes(), "branchQualifier".getBytes());
        
        Statement stmt = conn.createStatement();
        
        // The XA resource must be notified before starting any transactional work.
        xaRes.start(xid, XAResource.TMNOFLAGS);
        
        stmt.executeUpdate("INSERT INTO JTATABLE VALUES('JTA is pretty fun.')");
        
        // When the transaction work has completed, the XA resource must again be notified.
        xaRes.end(xid, XAResource.TMSUCCESS);
        
        int rc = xaRes.prepare(xid);
        
        // The transaction is committed through the XAResource.
        // The JDBC Connection object is not used to commit the transaction when using JTA.
        xaRes.commit(xid, false);
        
        execute(conn, "SELECT * FROM JTATABLE", false);
        close(conn);
    }

    static void test_setup() throws Exception {
        DataSource ds = newDataSource();
        Connection conn = ds.getConnection();       
        execute(conn, "DROP TABLE IF EXISTS JTATABLE", false);
        execute(conn, "CREATE TABLE JTATABLE(COL1 CHAR (50))", false);
        execute(conn, "SELECT * FROM JTATABLE", false);
        close(conn);
    }

    static void test_ping() throws SQLException, Exception {

        DataSource ds = newDataSource();
        execute(ds.getConnection(), "show tables", true);
    }
}
