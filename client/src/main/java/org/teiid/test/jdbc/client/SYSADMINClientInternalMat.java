package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class SYSADMINClientInternalMat {

    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioInterMaterialize@mm://localhost:31000;version=1";
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
    static final String SQL_refreshMatView = "EXEC SYSADMIN.refreshMatView('StocksMatModel.stockPricesInterMatView', true)";

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
