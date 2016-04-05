package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 
 * MatViews=Table name=MatViews, nameInSource=null, uuid=tid:60b87e792634-14d6450e-0000000e, 
 * StoredProcedures=Table name=StoredProcedures, nameInSource=null, uuid=tid:60b87e792634-c0d02c23-00000030, 
 * Triggers=Table name=Triggers, nameInSource=null, uuid=tid:60b87e792634-5d71c5db-0000001d, 
 * Usage=Table name=Usage, nameInSource=null, uuid=tid:60b87e792634-04e38d81-00000001, 
 * VDBResources=Table name=VDBResources, nameInSource=null, uuid=tid:60b87e792634-1e9b1131-00000019, 
 * Views=Table name=Views, nameInSource=null, uuid=tid:60b87e792634-04ed2a4e-00000028
 * 
 * isLoggable=Procedure name=isLoggable, nameInSource=null, uuid=tid:60b87e792634-492678c7-00000038, 
 * loadMatView=Procedure name=loadMatView, nameInSource=null, uuid=tid:60b87e792634-1388485f-00000069, 
 * logMsg=Procedure name=logMsg, nameInSource=null, uuid=tid:60b87e792634-be97959d-0000003c, 
 * matViewStatus=Procedure name=matViewStatus, nameInSource=null, uuid=tid:60b87e792634-f786f077-0000005d, 
 * refreshMatView=Procedure name=refreshMatView, nameInSource=null, uuid=tid:60b87e792634-d197ccca-00000041, 
 * refreshMatViewRow=Procedure name=refreshMatViewRow, nameInSource=null, uuid=tid:60b87e792634-9021c8b0-00000045, 
 * refreshMatViewRows=Procedure name=refreshMatViewRows, nameInSource=null, uuid=tid:60b87e792634-74174dc3-0000004a, 
 * setColumnStats=Procedure name=setColumnStats, nameInSource=null, uuid=tid:60b87e792634-3dd16127-0000004e, 
 * setProperty=Procedure name=setProperty, nameInSource=null, uuid=tid:60b87e792634-3b607c57-00000055, 
 * setTableStats=Procedure name=setTableStats, nameInSource=null, uuid=tid:60b87e792634-c20fcf73-0000005a, 
 * updateMatView=Procedure name=updateMatView, nameInSource=null, uuid=tid:60b87e792634-d8ee3f1c-0000006e
 *
 */
public class SYSADMINClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static final String SQL_MatViews = "SELECT * from SYSADMIN.MatViews";
    static final String SQL_StoredProcedures ="SELECT * from SYSADMIN.StoredProcedures";
    static final String SQL_Triggers = "SELECT * from SYSADMIN.Triggers";
    static final String SQL_Usage = "SELECT * from SYSADMIN.Usage";
    static final String SQL_VDBResources = "SELECT * from SYSADMIN.VDBResources";
    static final String SQL_Views = "SELECT * from SYSADMIN.Views";
    
    static final String SQL_isLoggable = "EXEC SYSADMIN.isLoggable('INFO', 'org.teiid.PROCESSOR')";
    static final String SQL_logMsg = "EXEC SYSADMIN.logMsg('INFO', 'org.teiid.PROCESSOR', 'Hello World, this external call to SYSADMIN logMsg procedure')";
    static final String SQL_refreshMatView = "EXEC SYSADMIN.refreshMatView('StocksMatModel.stockPricesMatView', true)";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        execute(conn, SQL_MatViews, false);
//        execute(conn, SQL_StoredProcedures, false);
//        execute(conn, SQL_Triggers, false);
//        execute(conn, SQL_Usage, false);
//        execute(conn, SQL_VDBResources, false);
//        execute(conn, SQL_Views, false);
        
//        executeProcesdure_isLoggable(conn);
//        executeProcesdure_logMsg(conn);
        executeProcesdure_refreshMatView(conn);
        
        conn.close();
    }


    static void executeProcesdure_refreshMatView(Connection conn) throws SQLException {

        System.out.println("Callable SQL: " + SQL_refreshMatView);

        CallableStatement cStmt = conn.prepareCall(SQL_refreshMatView);
        cStmt.execute();
        System.out.println(cStmt.getObject(1));
        cStmt.close();
        
        System.out.println();
    }


    static void executeProcesdure_logMsg(Connection conn) throws SQLException {
        
        System.out.println("Callable SQL: " + SQL_logMsg);

        CallableStatement cStmt = conn.prepareCall(SQL_logMsg);
        cStmt.execute();
        System.out.println(cStmt.getBoolean(1));
        cStmt.close();
        
        System.out.println();
        
    }


    static void executeProcesdure_isLoggable(Connection conn) throws SQLException {
        
        System.out.println("Callable SQL: " + SQL_isLoggable);

        CallableStatement cStmt = conn.prepareCall(SQL_isLoggable);
        cStmt.execute();
        System.out.println(cStmt.getBoolean(1));
        cStmt.close();
        
        System.out.println();
    }

}
