package org.teiid.test.jdbc.client.transactions;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.teiid.client.xa.XidImpl;
import org.teiid.jdbc.TeiidDataSource;


/**
 * This example use to demonstrate global transactions.
 * 
 * There are 2 accounts reside on 2 different mariadb database accordingly, each account have a initial balancer 100,
 * the transaction like credit 50 from account 1, debit 50 to account 2. So the 2 accounts' balancer like:
 *   Before transaction: account 1 balancer is 100, account 2 balancer is 100
 *   After transaction: account 1 balancer is 50, account 2 balancer is 150
 * 
 * @author kylin
 *
 */
public class UsingGlobalTransactions {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TransactionsExamplesVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    
    public static void main(String[] args) throws Exception {
        
        init();
        
        XAConnection xaConn = null;
        XAResource xaRes = null;
        Connection conn = null;
        Statement stmt = null;
        Xid xid = null;
        
        balancer("Before transaction");
        
        TeiidDataSource ds = new TeiidDataSource();
        ds.setDatabaseName("TransactionsExamplesVDB");
        ds.setUser(JDBC_USER);
        ds.setPassword(JDBC_PASS);
        ds.setServerName("localhost");
        ds.setPortNumber(31000);
        
        try {
            xaConn = ds.getXAConnection();
            xaRes = xaConn.getXAResource();
            xid = new XidImpl();
            conn = xaConn.getConnection();
            stmt = conn.createStatement();
            
            xaRes.start(xid, XAResource.TMNOFLAGS);
            
            //credit account 1
            stmt.executeUpdate("UPDATE MariaDBXA1Schema.account SET balancer = 50 WHERE id = 1");
            balancer("  Credit account 1");
            //debit account 2
            stmt.executeUpdate("UPDATE MariaDBXA2Schema.account SET balancer = 150 WHERE id = 1");
            balancer("   Dedit account 2");
            xaRes.end(xid, XAResource.TMSUCCESS);
            if (xaRes.prepare(xid) == XAResource.XA_OK){
                xaRes.commit(xid, false);
            }
        } catch (XAException e) {
            e.printStackTrace();
            xaRes.rollback(xid);
        } finally {
            close(stmt, conn);
            xaConn.close();
        }
        
        balancer(" After transaction");
    }
    
    static void init() throws Exception {
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        execute(conn, "UPDATE MariaDBXA1Schema.account SET balancer = 100 WHERE id = 1", false);
        execute(conn, "UPDATE MariaDBXA2Schema.account SET balancer = 100 WHERE id = 1", true);
    }
    
    static void balancer(String msg) throws Exception {
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        Statement stmt1 = conn.createStatement();
        ResultSet rs1 = stmt1.executeQuery("SELECT balancer FROM MariaDBXA1Schema.account WHERE id = 1");
        rs1.next();
        int v1 = rs1.getInt(1);
        Statement stmt2 = conn.createStatement();
        ResultSet rs2 = stmt1.executeQuery("SELECT balancer FROM MariaDBXA2Schema.account WHERE id = 1");
        rs2.next();
        int v2 = rs2.getInt(1);
        System.out.println(msg + ", account 1 balancer: " + v1 + ", account 2 balancer:" + v2);
        close(rs1, stmt1);
        close(rs2, stmt2, conn);
    }

}
