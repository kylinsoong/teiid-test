package org.teiid.test.jdbc.client.transactions;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class SimpleCommit {
    
    static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    static final String JDBC_URL = "jdbc:teiid:TransactionsExamplesVDB@mm://localhost:31000;version=1";
    static final String JDBC_USER = "teiidUser";
    static final String JDBC_PASS = "password1!";
    
    static String SQL_QUERY = "SELECT * FROM account";
    static String SQL_INSERET = "INSERT INTO account(balancer) VALUES(100)";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS); 
        
        conn.setAutoCommit(false);
        
        execute(conn, SQL_INSERET);
        
        conn.commit();
        
        close(conn);
    }

}
