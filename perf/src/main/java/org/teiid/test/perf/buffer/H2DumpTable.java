package org.teiid.test.perf.buffer;


import static org.teiid.test.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

import org.teiid.test.util.JDBCUtils;

public class H2DumpTable {
	
	public static final String H2_JDBC_DRIVER = "org.h2.Driver";
    public static final String H2_JDBC_URL = "jdbc:h2:file:target/teiid-dump-ds;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1";
    public static final String H2_JDBC_USER = "sa";
    public static final String H2_JDBC_PASS = "sa";
	
	static final String CREATE_SQL = "CREATE TABLE DUMPTABLE(id int, col_a varchar, col_b varchar, col_c varchar)";
	static final String INSERT_SQL = "insert into DUMPTABLE values(?, ?, ?, ?)";
	
	public static void insert(int row) throws Exception {
        insert(row, H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
    }
    
    public static void insert(int row, String driver, String url, String user, String pass) throws Exception {
        
        Connection conn = getDriverConnection(driver, url, user, pass);
        
        JDBCUtils.executeUpdate(conn, CREATE_SQL);
        
        System.out.print(Thread.currentThread().getName() + " thread inserting ");
        
        conn.setAutoCommit(false);
        long start = System.currentTimeMillis();
        PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
        
        int count = 0;
        for(int i = 0 ; i < row ; i ++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, formString(1024 * 100));
            pstmt.setString(3, formString(1024 * 200));
            pstmt.setString(4, formString(1024 * 200));
            pstmt.addBatch();
            if((i + 1) % 100 == 0){
                pstmt.executeBatch();
                conn.commit();
                count ++;
                if(count == 100) {
                    System.out.print(".");
                    count = 0;
                }
            } 
        }
        
        pstmt.executeBatch();
        conn.commit();
        conn.setAutoCommit(true);
        
        System.out.println("\n" + Thread.currentThread().getName() + " thread insert "  + row + " rows spend " +  + (System.currentTimeMillis() - start) + " ms");
    
        JDBCUtils.close(pstmt, conn);
 
    }
    
    public static void printRowCount() throws Exception{
    	Connection conn = getDriverConnection(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
    	JDBCUtils.executeQuery(conn, "SELECT Count(id) FROM DUMPTABLE");
    }

    final static Random r = new Random();
    
	private static String formString(int size) {
		
		byte[] buf = new byte[size];
		r.nextBytes(buf);
		return new String(buf);
	}

	public static void main(String[] args) throws Exception {
		H2DumpTable.insert(1200);
		H2DumpTable.printRowCount();
	}

}
