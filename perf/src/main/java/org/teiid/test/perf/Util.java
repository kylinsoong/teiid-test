package org.teiid.test.perf;

import static org.teiid.test.perf.MysqlPERFTESTClient.JDBC_DRIVER;
import static org.teiid.test.perf.MysqlPERFTESTClient.JDBC_PASS;
import static org.teiid.test.perf.MysqlPERFTESTClient.JDBC_URL;
import static org.teiid.test.perf.MysqlPERFTESTClient.JDBC_USER;
import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.util.JDBCUtils.executeUpdate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.teiid.example.util.JDBCUtils;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;

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


    public static PerfEntity executeProcedureCount(Connection conn, String sql) throws SQLException {
        PerfEntity entity = new PerfEntity();
        entity.setSql(sql);
        
        long start = System.currentTimeMillis();
        
        CallableStatement cStmt = null;
        ResultSet rs = null;
        try {
            cStmt = conn.prepareCall(sql);
            boolean hadResults = cStmt.execute();
            while (hadResults){
                rs = cStmt.getResultSet();
                
                int columns = rs.getMetaData().getColumnCount();
                for (int row = 1; rs.next(); row++) {
                    System.out.print(row + ": ");
                    for (int i = 0; i < columns; i++) {
                       
                        rs.getObject(i + 1);
                    }
                }
            }
        } finally {
            JDBCUtils.close(rs, cStmt);
        }
        
        entity.setQueryTime(System.currentTimeMillis() - start);
        
        return entity;
    }
    
    protected static void prompt(String prompt) {
        System.out.println("\n\tExecute '" + prompt + "' 10 times, this may need some times");
        System.out.println();
    }
    

    
    protected static void dumpResult(long[] array, long[] arrayPerf, String sql, String sqlPerf) {
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create(sql, sqlPerf));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array[i], arrayPerf[i]));
        }
        
        render.renderer();
    }
    
    protected static void dumpResult(long[] array, String sql) {
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create(sql));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array[i]));
        }
        
        render.renderer();
    }

}
