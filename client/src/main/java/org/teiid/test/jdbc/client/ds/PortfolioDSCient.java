package org.teiid.test.jdbc.client.ds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.teiid.client.xa.XidImpl;
import org.teiid.jdbc.TeiidDataSource;

public class PortfolioDSCient {
    
    static final String DatabaseName = "Portfolio";
    static final Integer DatabaseVersion = 1;
    static final String ServerName = "localhost";
    static final Integer PortNumber = 31000;
    static final String JDBC_USER = "teiidUser";
    static final String JDBC_PASS = "password1!";
    
    public static TeiidDataSource create(){

        TeiidDataSource ds = new TeiidDataSource();
        
        ds.setDatabaseName(DatabaseName);
        ds.setDatabaseVersion(DatabaseVersion.toString());
        ds.setServerName(ServerName);
        ds.setPortNumber(PortNumber);
        ds.setUser(JDBC_USER);
        ds.setPassword(JDBC_PASS);
        
        return ds;
    }
    

    public static void main(String[] args) throws SQLException, XAException {

        TeiidDataSource ds = new TeiidDataSource();
        
        ds.setDatabaseName(DatabaseName);
        ds.setDatabaseVersion(DatabaseVersion.toString());
        ds.setServerName(ServerName);
        ds.setPortNumber(PortNumber);
        ds.setUser(JDBC_USER);
        ds.setPassword(JDBC_PASS);
        
        XAConnection  xaConn = ds.getXAConnection();
        XAResource    xaRes  = xaConn.getXAResource();
        Connection    c      = xaConn.getConnection();
        
        System.out.println(c);
    }

}
