package org.teiid.test.jira;

import static org.teiid.example.util.JDBCUtils.close;
import static org.teiid.example.util.JDBCUtils.execute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import org.h2.util.JdbcUtils;
import org.teiid.example.EmbeddedHelper;
import org.teiid.example.util.JDBCUtils;

public class PhoenixClient {
	
	 static final String JDBC_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
	 static final String JDBC_URL = "jdbc:phoenix:127.0.0.1:2181";
	 static final String JDBC_USER = "";
	 static final String JDBC_PASS = "";
	    

	public static void main(String[] args) throws Exception {
		
//		smalla1();
		
		smalla4();
		
//		smalla5();
		
//		test();

	}
	
	static void test() throws Exception {
		DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Connection conn = ds.getConnection();
		boolean isAutoCommit = conn.getAutoCommit();
		System.out.println(isAutoCommit);
		execute(conn, "UPSERT INTO  \"smalla5\" VALUES(6, '66')", false);
		execute(conn, "SELECT * FROM \"smalla5\"", false);
		close(conn);
		
	}
	
	static Connection getDriverConnection(String driver, String url, String user, String pass) throws Exception {
		Class.forName(driver);
		Properties prop = new Properties();
        prop.setProperty("phoenix.connection.autoCommit", "true");
		return DriverManager.getConnection(url, prop); 
	}

	/**
	 * TEIID-3623
	 */
	static void smalla1() throws Exception{
		
		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		execute(conn, "CREATE TABLE IF NOT EXISTS  smalla1(intkey integer primary key, booleankey BOOLEAN)", false);
		
		execute(conn, "SELECT * FROM smalla1", false);
		
		execute(conn, "UPSERT INTO  smalla1 VALUES(5, true)", false);
		
		execute(conn, "SELECT * FROM smalla1", false);
		
//		execute(conn, "UPSERT INTO  smalla1 VALUES(5, flase)", false);

//		execute(conn, "SELECT * FROM smalla1", false);
		
		close(conn);
	}
	
	/**
	 * TEIID-3622
	 */
	static void smalla2() throws Exception {
		
	}
	
	/**
	 * TEIID-3621
	 */
	static void smalla3() throws Exception {
		
	}
	
	/**
	 * TEIID-3620
	 */
	static void smalla4() throws Exception {
		
		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		execute(conn, "CREATE TABLE IF NOT EXISTS  smalla4(intkey integer primary key, column1 date, column2 time, column3 timestamp)", false);
		
		execute(conn, "UPSERT INTO smalla4 VALUES (10, TO_DATE('2000-01-02 03:04:05'), TO_TIME('2000-01-02 03:04:05'), TO_TIMESTAMP('2000-01-02 03:04:05'))", false);
	}
	
	/**
	 * TEIID-3619
	 */
	static void smalla5() throws Exception {
		
		DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Connection conn = ds.getConnection();
		
		execute(conn, "CREATE TABLE IF NOT EXISTS  smalla5(intkey integer primary key,stringkey varchar(10))", false);
		
		execute(conn, "SELECT * FROM smalla5", false);
		
		boolean isAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		execute(conn, "UPSERT INTO  smalla5 VALUES(5, '5')", false);
		conn.commit();
		conn.setAutoCommit(isAutoCommit);
		
		execute(conn, "SELECT * FROM smalla5", false);
		
		isAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		execute(conn, "UPSERT INTO smalla5 VALUES(5, '55')", false);
		conn.commit();
		conn.setAutoCommit(isAutoCommit);
		
		execute(conn, "SELECT * FROM smalla5", false);
		
		
		close(conn);
	}

}
