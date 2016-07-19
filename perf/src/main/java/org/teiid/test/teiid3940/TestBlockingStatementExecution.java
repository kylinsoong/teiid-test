package org.teiid.test.teiid3940;

import static org.teiid.test.util.JDBCUtils.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.teiid.jdbc.RequestOptions;
import org.teiid.jdbc.StatementCallback;
import org.teiid.jdbc.TeiidStatement;

public class TestBlockingStatementExecution {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        Statement stmt = conn.createStatement();
        TeiidStatement tstmt = stmt.unwrap(TeiidStatement.class);
        
        tstmt.submitExecute("SELECT * FROM STOCK", new StatementCallback(){

            @Override
            public void onRow(Statement s, ResultSet rs) throws Exception {
                System.out.println(rs.getObject(1));
            }

            @Override
            public void onException(Statement s, Exception e) throws Exception {
                System.out.println(e);
                s.close();
            }

            @Override
            public void onComplete(Statement s) throws Exception {
                System.out.println(s.getResultSet());
                s.close();
            }}, new RequestOptions());
        
        
        Thread.sleep(Long.MAX_VALUE);
        
//        execute(conn, "SELECT * FROM STOCK", false);
        
        close(conn);
        
    }

}
