package org.jboss.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.util.JDBCUtils.executeUpdate;
import static org.teiid.test.util.JDBCUtils.executeQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.teiid.test.util.JDBCUtils;
/** 
 * An Utils class for insert data to Mysql.
 * 
 * @author kylin
 *
 */
public class MysqlPERFTESTClient {
	
	/**
	 * Each row's size = 100 bytes
	 * 		int      -> 4 bytes
	 * 		char(16) -> 16 bytes
	 * 		char(40) -> 40 bytes
	 * 		char(40) -> 40 bytes
	 */
	static final String PERFTEST_SQL_DROP = "DROP TABLE PERFTEST";
	static final String PERFTEST_SQL_CREATE = "CREATE TABLE PERFTEST(id INTEGER, col_a CHAR(16), col_b CHAR(40), col_c CHAR(40))";
	
	static final String PERFTEST_SQL_ROWS = "SELECT sum(table_rows) from information_schema.TABLES WHERE table_name = 'PERFTEST'";
	static final String PERFTEST_SQL_SIZE = "SELECT sum(data_length) from information_schema.TABLES WHERE table_name = 'PERFTEST'";
	static final String PERFTEST_SQL_ROWS_SIZE = "SELECT sum(table_rows), sum(data_length) from information_schema.TABLES WHERE table_name = 'PERFTEST'";
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";
	
	static final String COL_A = "abcdefghabcdefgh";
	static final String COL_B = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
	static final String COL_C = "1234567890123456789012345678901234567890";
	
	static final String INSERT_SQL = "insert into PERFTEST values(?, ?, ?, ?)";
	
	public static final int KB = 1<<10;
	public static final int MB = 1<<20;
	public static final int GB = 1<<30;
	
	public static void insert(int row) throws Exception {
		insert(row, false, JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
	}
	
	/**
	 * This method will create a connection and insert a num of rows
	 * @param row
	 * @param reset
	 * @throws Exception 
	 */
	public static void insert(int row, boolean reset) throws Exception {
		insert(row, reset, JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
	}
	
	public static void insert(int row, boolean reset, String driver, String url, String user, String pass) throws Exception {
		
		Connection conn = getDriverConnection(driver, url, user, pass);
		
		if(reset) {
			try {
				executeUpdate(conn, PERFTEST_SQL_DROP);
				executeUpdate(conn, PERFTEST_SQL_CREATE);
			} catch (Throwable e) {
				System.out.println("Reset Failed: " + e.getMessage());
			}
		}
		
		conn.setAutoCommit(false);
		long start = System.currentTimeMillis();
		PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
		
		for(int i = 0 ; i < row ; i ++) {
			
			pstmt.setInt(1, i);
			pstmt.setString(2, COL_A);
			pstmt.setString(3, COL_B);
			pstmt.setString(4, COL_C);
			pstmt.addBatch();
			if((i + 1) % 1000 == 0){
				pstmt.executeBatch();
				conn.commit();
			} 
		}
		
		pstmt.executeBatch();
		conn.commit();
		
		System.out.println("Insert " + row + " rows spend " + (System.currentTimeMillis() - start) + " ms");
		
		executeQuery(conn, PERFTEST_SQL_ROWS_SIZE);
		
		JDBCUtils.close(pstmt, conn);
		
	}

}
