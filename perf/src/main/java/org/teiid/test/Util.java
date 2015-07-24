package org.teiid.test;

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
//                    System.out.print(row + ": ");
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
    
    public static void promptSQL(String sql) {
        System.out.println("\n\tExecute '" + sql + "' 10 times, this may need some times");
        System.out.println();
    }
    
    public static void print(String msg) {
        System.out.println(msg);
    }
    
    public static void prompt(String msg){
        System.out.println("\n\t" + msg + "\n");
    }
    
    @SuppressWarnings("static-access")
    public static void sleep(String msg, long time){
        prompt(msg);
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    

    
    public static void dumpResult(long[] array, long[] arrayPerf, String sql, String sqlPerf) {
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create(sql, sqlPerf));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array[i], arrayPerf[i]));
        }
        
        render.renderer();
    }
    
    public static void dumpResult(long[] array, String sql) {
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create(sql));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array[i]));
        }
        
        render.renderer();
    }

}
