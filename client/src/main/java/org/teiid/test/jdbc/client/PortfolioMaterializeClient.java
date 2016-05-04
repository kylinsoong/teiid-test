package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PortfolioMaterializeClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String sql_mat = "SELECT * FROM stockPricesMatView";
    static String sql_status = "select * from sysadmin.matviews";
    static String sql_viewStatus = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesMatView')";
    static String sql_viewsStatus = "EXEC SYSADMIN.matViewsStatus()";
    
    static final String SQL_GET_UID = "SELECT UID FROM Sys.Tables WHERE VDBName = 'PortfolioMaterialize' AND SchemaName = 'StocksMatModel' AND Name = 'stockPricesMatView'";
    static final String SQL_MAT_TABLE = "SELECT \"Value\" from SYS.Properties WHERE UID = 'tid:5cb132cf1822-0f07a905-00000001' AND Name = 'MATERIALIZED_TABLE'";

    static final String SQL_SYS_Properties = "SELECT * FROM SYS.Properties";
    
	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
//		execute(conn, sql_mat, false);
//		execute(conn, SQL_GET_UID, false);
//		execute(conn, SQL_MAT_TABLE, false);
//		execute(conn, SQL_SYS_Properties, false);
//		execute(conn, sql_viewStatus, false);
//		executeCallable(conn);
		
		// query status table
//		execute(conn, "select * from Accounts.status", false);
		
//		execute(conn, "SELECT Name FROM VirtualDatabases", false);
//		execute(conn, "SELECT convert(Version, integer) FROM VirtualDatabases", false);
//		execute(conn, "SELECT * FROM SYS.Properties", false);
//		execute(conn, sql_viewStatus, false);
		
//		execute(conn, sql_viewsStatus, false);
		
		String sql = "SELECT DISTINCT \"Value\" FROM SYS.Properties WHERE Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'";
		List<String> statusTables = new ArrayList<>();
		statusTables.add("Accounts.status");
		statusTables.add("Accounts.status_1");
		execute(conn, formQuerySql(statusTables), false);
		
		conn.close();
	}
	
	static String formQuerySql(List<String> tables) {
        
        String COLUMNS = "SELECT VDBName, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality FROM";
        String UNION = "UNION";
        String BLANK = " ";
        
        StringBuffer sb = new StringBuffer();
        sb.append(COLUMNS).append(BLANK);
        sb.append("SYSADMIN.MatViews").append(BLANK);
        for(String table : tables){
            sb.append(UNION).append(BLANK);
            sb.append(COLUMNS).append(BLANK);
            sb.append(table).append(BLANK);
        }
        
        return sb.toString();
    }

    static void executeCallable(Connection conn) throws SQLException {

        Statement stmt = conn.createStatement();
        stmt.execute(sql_viewStatus);
        
    }

}
