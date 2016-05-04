package org.teiid.test.jdbc.client.ds;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.teiid.client.xa.XidImpl;
import org.teiid.jdbc.TeiidDataSource;

/**
 * This is an example of how to use the Java™ Transaction API (JTA) to handle 
 * a transaction in an application.
 * @author kylin
 *
 */
public class JTACommit {

    public static void main(String[] args) throws Exception {
        
        TeiidDataSource ds = PortfolioDSCient.create();
        
        //From TeiidDataSource, obtain an XAConnection object that contains an XAResource and a Connection object.
        XAConnection  xaConn = ds.getXAConnection();
        XAResource    xaRes  = xaConn.getXAResource();
        Connection    c      = xaConn.getConnection();
        c.setAutoCommit(false);
        
        //For XA transactions, create a transaction identifier is necessary.
        Xid xid = new XidImpl();
        
        Statement stmt = c.createStatement();
        
        //The XA resource must be notified before starting any transactional work.
        xaRes.start(xid, XAResource.TMNOFLAGS);
        
        stmt.executeUpdate("INSERT INTO Accounts.JTATABLE VALUES('JTA is pretty fun.')");
        
        //When the transaction work has completed, the XA resource must again be notified.
//        xaRes.end(xid, XAResource.TMSUCCESS);
        
        //The transaction represented by the transaction ID is prepared to be committed.
//        int rc = xaRes.prepare(xid);
        
        //The transaction is committed through the XAResource. The JDBC Connection object is not used to commit the transaction when using JTA.
//        xaRes.commit(xid, false);
        
        execute(c, "SELECT * FROM Accounts.JTATABLE", false);
    }

    
}
