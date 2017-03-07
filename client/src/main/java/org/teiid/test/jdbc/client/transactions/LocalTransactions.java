package org.teiid.test.jdbc.client.transactions;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * https://teiid.gitbooks.io/documents/content/client-dev/Local_Transactions.html
 * @author kylin
 *
 */
public class LocalTransactions {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TransactionsExamplesVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String SQL_QUERY_COUNT = "SELECT COUNT(id) AS TOTAL_SKILLS FROM MariaDB1Schema.skills";
    
	public static void main(String[] args) throws Exception {

	    jdbcSpecific();
	    
	    turnOffLocalTransaction();
		
	}
	
	static void turnOffLocalTransaction() throws Exception {
	    
	    System.out.println("\nLocal Transactions Turning Off JDBC Local Transaction Controls");
	    
	    count("original", SQL_QUERY_COUNT);
	    
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", JDBC_USER);
	    connectionProps.put("password", JDBC_PASS);
	    connectionProps.put("disableLocalTxn", "true");
	    
	    Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, connectionProps);
	    conn.setAutoCommit(false);
	    
	    try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO MariaDB1Schema.skills(id, name) VALUES(4, 'teiid')");
            stmt.close();
            count("before add commit", SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        count("after commit", SQL_QUERY_COUNT);
        
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM MariaDB1Schema.skills WHERE id = 4");
            stmt.close();
            count("before delete commit", SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        close(conn);
        
        count("after delete commit", SQL_QUERY_COUNT);
    }

    static void jdbcSpecific() throws Exception {
        
        System.out.println("Local Transactions JDBC Specific");
	    
        count("original", SQL_QUERY_COUNT);

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);       
        conn.setAutoCommit(false);

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO MariaDB1Schema.skills(id, name) VALUES(4, 'teiid')");
            stmt.close();
            count("before add commit", SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        count("after commit", SQL_QUERY_COUNT);
        
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM MariaDB1Schema.skills WHERE id = 4");
            stmt.close();
            count("before delete commit", SQL_QUERY_COUNT);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
        
        close(conn);
        
        count("after delete commit", SQL_QUERY_COUNT);
	}
    
    static void count(String msg, String sql) throws Exception {
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
