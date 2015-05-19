package org.jboss.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.util.JDBCUtils.executeUpdate;
import static org.jboss.teiid.test.perf.MysqlPERFTESTClient.JDBC_DRIVER;
import static org.jboss.teiid.test.perf.MysqlPERFTESTClient.JDBC_URL;
import static org.jboss.teiid.test.perf.MysqlPERFTESTClient.JDBC_USER;
import static org.jboss.teiid.test.perf.MysqlPERFTESTClient.JDBC_PASS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.teiid.example.util.JDBCUtils;

public class Util {
	
	public static PerfEntity executeQueryCount(Connection conn, String sql) throws SQLException {
		
//		System.out.println("Query SQL: " + sql);
		
		PerfEntity entity = new PerfEntity();
		entity.setSql(sql);
		
		long start = System.currentTimeMillis();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			entity.setQueryTime(System.currentTimeMillis() - start);
			int columns = rs.getMetaData().getColumnCount();
			while(rs.next()) {
				for (int i = 0 ; i < columns ; ++i) {
					rs.getObject(i+1);
				}
			}
		} finally {
			JDBCUtils.close(rs, stmt);
		}
		
		entity.setDeserializeTime(System.currentTimeMillis() - start - entity.getQueryTime());
		
		return entity;
	}
	

	public static void persitResult(PerfEntity entity) throws Exception {
		
		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		conn.setAutoCommit(false);

		try {
			String sql = "SELECT id FROM QUERYSQL WHERE content = '" + entity.getSql() + "'";
			int id = querySQLID(conn, sql);
			String queryTimeSQL = "INSERT INTO PERFRESULT (value, item_id, querysql_id) VALUES (" + entity.getQueryTime() + ", 1, " + id + ")";
			String deserializeTimeSQL = "INSERT INTO PERFRESULT (value, item_id, querysql_id) VALUES (" + entity.getDeserializeTime() + ", 2, " + id + ")";
			executeUpdate(conn, queryTimeSQL);
			executeUpdate(conn, deserializeTimeSQL);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn);
		}
		
	}


	private static int querySQLID(Connection conn, String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		int id = -1;
		while(rs.next()) {
			id = rs.getInt(1);
		}
		return id;
	}

}
