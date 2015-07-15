package org.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.Constants.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.teiid.test.util.JDBCUtils;

public class H2PERFTESTClient {
    
    static final String INSERT_SQL = "insert into PERFTEST values(?, ?, ?, ?)";
    
    public static void insert(long row) throws Exception {
        insert(row, H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS, true);
    }
    
    public static void insert(long row, boolean reset) throws Exception {
        insert(row, H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS, reset);
    }
    
    public static void insert(long row, String driver, String url, String user, String pass, boolean reset) throws Exception {
        
        Connection conn = getDriverConnection(driver, url, user, pass);
        
        if(reset){
            executeSchema(conn);
        }
        
        System.out.print(Thread.currentThread().getName() + " thread inserting ");
        
        conn.setAutoCommit(false);
        long start = System.currentTimeMillis();
        PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
        
        int count = 0;
        for(long i = 0 ; i < row ; i ++) {
            pstmt.setString(1, COL_ID);
            pstmt.setString(2, COL_A);
            pstmt.setString(3, COL_B);
            pstmt.setString(4, COL_C);
            pstmt.addBatch();
            if((i + 1) % 1000 == 0){
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
    

    static long countRows(long total, int num) {
        long row = total / num;
        if(row <= 0){
            row = total;
        }
        return row;
    }

    static int caculation(long row) {
        int num = (int) (row / (MB * 10));
        if(num == 0)
            num = 1;
        int max = Runtime.getRuntime().availableProcessors();
        return Math.min(num, max);
    }

    static void executeSchema(Connection conn) throws SQLException {
        RunScript.execute(conn, new InputStreamReader(H2PERFTESTClient.class.getClassLoader().getResourceAsStream("h2-schema.sql")));
    }
    
    static class InsertThread implements Runnable {
        
        private long size;
        private int id;
        
        private String driver;
        private String url;
        private String user;
        private String pass;
        
        protected InsertThread(long size, int id, String driver, String url, String user, String pass) {
            this.size = size;
            this.id = id;
            this.driver = driver;
            this.url = url;
            this.user = user;
            this.pass = pass;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(InsertThread.class.getSimpleName() + "-" + id);
           
            
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = getDriverConnection(driver, url, user, pass);
                conn.setAutoCommit(false);
                long start = System.currentTimeMillis();
                pstmt = conn.prepareStatement(INSERT_SQL);
                
                for(long i = 0 ; i < size ; i ++) {
                    pstmt.setString(1, COL_ID);
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
                conn.setAutoCommit(true);
                
                System.out.println(Thread.currentThread().getName() + " insert "  + size + " rows spend " +  + (System.currentTimeMillis() - start) + " ms");
            
                JDBCUtils.close(pstmt, conn);
            } catch (Exception e) {
                e.printStackTrace();
            }         
        }
        
    }

    public static void main(String[] args) throws Exception {
        
//        H2PERFTESTClient.insert(MB);
        H2PERFTESTClient.insert(10000);
    }

}
