package org.teiid.test.jira;

import static org.teiid.example.util.JDBCUtils.close;
import static org.teiid.example.util.JDBCUtils.execute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.example.EmbeddedHelper;
import org.teiid.example.util.JDBCUtils;

public class PhoenixClient {
	
	 static final String JDBC_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
	 static final String JDBC_URL = "jdbc:phoenix:127.0.0.1:2181";
	 static final String JDBC_USER = "";
	 static final String JDBC_PASS = "";
	    

	public static void main(String[] args) throws Exception {
		
//		smalla1();
		
//		smalla();
		
//		smalla_1();
		
//		smalla_2();
		
//		smalla2();
		
//		smalla2_1();
		
//		smalla2_2();
		
//		smalla2_3();
		
//		smalla2_4();
		
		smalla2_5();
		
//		smalla_3();
		
//		smalla_4();
		
//		smalla4();
		
//		smalla5();
		
//		test();
		
//		test1();

	}
	
	static void smalla2_5() throws SQLException, ResourceException {
		
		DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Connection conn = ds.getConnection();
		System.out.println(conn);
		System.out.println(conn.getAutoCommit());
		conn.setAutoCommit(true);
		close(conn);
	}

	static void smalla2_4() throws Exception {

		DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS, "phoenix.connection.autoCommit=true");
		Connection conn = ds.getConnection();
		System.out.println(conn);
		System.out.println(conn.getAutoCommit());
		
		execute(conn, "SELECT * FROM smalla", false);
		
		execute(conn, "UPSERT INTO smalla VALUES(8, '88')", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		close(conn);
	}

	static void smalla2_3() throws Exception {

		DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Connection conn = ds.getConnection();
		System.out.println(conn.getAutoCommit());
		
		execute(conn, "SELECT * FROM smalla", false);
		
		conn.setAutoCommit(true);
		execute(conn, "UPSERT INTO smalla VALUES(6, '66')", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		close(conn);
	}

	static void smalla2_2() throws Exception {

		Connection c2 = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		System.out.println(c2.getAutoCommit());
		
		execute(c2, "SELECT * FROM smalla", false);
		
		execute(c2, "UPSERT INTO smalla VALUES(7, '77')", false);

		execute(c2, "SELECT * FROM smalla", false);
		
		close(c2);
	}

	static void smalla2_1() throws Exception {

		Connection c1 = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		System.out.println(c1.getAutoCommit());
		
		execute(c1, "SELECT * FROM smalla", false);
		
		c1.setAutoCommit(true);
		execute(c1, "UPSERT INTO smalla VALUES(6, '66')", false);

		execute(c1, "SELECT * FROM smalla", false);
		
		close(c1);
	}

	static void smalla_4() throws Exception {
		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		conn.setAutoCommit(true);
		execute(conn, "UPSERT INTO smalla(stringkey) VALUES('66')", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		close(conn);
	}

	static void smalla_3() throws Exception {
		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		conn.setAutoCommit(true);
		String sql = "UPSERT INTO smalla (stringkey, intkey) SELECT '1234', smalla.intkey FROM smalla WHERE smalla.stringkey = 'xxx'";
		execute(conn, sql, false);
		execute(conn, "SELECT * FROM smalla", false);
		close(conn);
	}

	static void smalla_2() throws Exception {

		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		execute(conn, "SELECT * FROM smalla", false);
		conn.setAutoCommit(true);
		execute(conn, "UPSERT INTO smalla (stringkey, intkey) SELECT 'xxx', smalla.intkey FROM smalla WHERE smalla.stringkey IS NULL", false);
		execute(conn, "SELECT * FROM smalla", false);
		execute(conn, "SELECT * FROM smalla", false);
		close(conn);
	}

	static void smalla_1() throws Exception {

		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		execute(conn, "CREATE TABLE IF NOT EXISTS  smalla(intkey integer primary key,stringkey varchar(10))", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		conn.setAutoCommit(true);
		
		execute(conn, "UPSERT INTO  smalla(intkey) VALUES(1)", false);
		execute(conn, "UPSERT INTO  smalla(intkey) VALUES(2)", false);
		execute(conn, "UPSERT INTO  smalla(intkey) VALUES(3)", false);
		execute(conn, "UPSERT INTO  smalla(intkey) VALUES(4)", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		close(conn);
	}

	static void test1() throws Exception {

		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		boolean isAutoCommit = conn.getAutoCommit();
		System.out.println(isAutoCommit);
		
		close(conn);
	}

	static void smalla() throws Exception {
		
		Connection conn = JDBCUtils.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		execute(conn, "CREATE TABLE IF NOT EXISTS  smalla(intkey integer primary key,stringkey varchar(10))", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		conn.setAutoCommit(true);
		
		execute(conn, "UPSERT INTO  smalla VALUES(5, '5')", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		execute(conn, "UPSERT INTO  smalla VALUES(5, '55')", false);
		
		execute(conn, "SELECT * FROM smalla", false);
		
		close(conn);
		
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
	
	
	static void smalla2() throws Exception {
		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		System.out.println(conn.getAutoCommit());
		close(conn);
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
		
		execute(conn, "UPSERT INTO smalla4 VALUES (10, DATE '2000-01-02 03:04:05', TIME '2000-01-02 03:04:05', TIMESTAMP '2000-01-02 03:04:05')", false);
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
