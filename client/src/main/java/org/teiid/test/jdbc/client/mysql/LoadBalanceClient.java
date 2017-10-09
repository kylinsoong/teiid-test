package org.teiid.test.jdbc.client.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

public class LoadBalanceClient {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://haproxy.node.com:3306/test";
	static final String JDBC_URL_1 = "jdbc:mysql://mysql.node1.com:3306/test";
    static final String JDBC_URL_2 = "jdbc:mysql://mysql.node2.com:3306/test";
    static final String JDBC_USER = "test_user";
    static final String JDBC_PASS = "redhat";

	public static void main(String[] args) throws Exception {

//		Connection conn1 = conn(JDBC_DRIVER, JDBC_URL_1, JDBC_USER, JDBC_PASS);
//		Connection conn2 = conn(JDBC_DRIVER, JDBC_URL_2, JDBC_USER, JDBC_PASS);
//		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
//		execute(conn(JDBC_DRIVER, JDBC_URL_1, JDBC_USER, JDBC_PASS), "SELECT * FROM FOO", true);
//		execute(conn(JDBC_DRIVER, JDBC_URL_2, JDBC_USER, JDBC_PASS), "SELECT * FROM FOO", true);
		execute(conn(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS), "SELECT * FROM FOO", true);
	}

}
