package com.teiid.test.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;

import org.teiid.client.plan.PlanNode;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

/*
 * CREATE TABLE SERIALTEST(id BIGINT, col_a CHAR(8), col_b CHAR(12), col_c CHAR(12));
 * 
 * SELECT sum(table_rows) from information_schema.TABLES WHERE table_name = 'SERIALTEST';
 * SELECT sum(data_length) from information_schema.TABLES WHERE table_name = 'SERIALTEST';
 * 
 * mvn clean install dependency:copy-dependencies
 * 
 * java -cp target/teiid-jdbc-client-1.0-SNAPSHOT.jar:target/dependency/* -Xms5096m -Xmx5096m -XX:MaxPermSize=512m -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps com.jboss.teiid.SerializationCaculation plan -s 1000000000
 * 
 * 
 */
public class SerializationCaculation {

	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	public SerializationCaculation() throws Exception {
		init();
	}

	public void init() throws Exception {
		init("translator-mysql", new MySQL5ExecutionFactory());
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("mysql-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);
	}
	
	private void init(String name, ExecutionFactory<?, ?> factory) throws TranslatorException {
		server = new EmbeddedServer();
		factory.start();
		factory.setSupportsDirectQueryProcedure(true);
		server.addTranslator(name, factory);
	}

	
	static Integer[] array = new Integer[] {5, 50, 500, 5000, 50000, 500000, 5000000};
	
	public void query() throws Exception {
		
		for(long size : array){

			String query = "SELECT * FROM SERIALTESTVIEW WHERE id < " + size;
			Connection conn = getConn();
			
			System.out.println("Serialize " + size * 200 + " bytes spend time: " + caculation(query, conn) + " ms\n");
		}
		
	}
	
	private Connection getConn() throws SQLException {
		return server.getDriver().connect("jdbc:teiid:MysqlVDB", null);
	}
	
	private static boolean showPlan = false; 

	private static long caculation(String query, Connection conn) throws Exception {

		Statement stmt = conn .createStatement();
		if(showPlan){
			stmt.execute("set showplan on");
		}
		ResultSet rs = null;
		long time = 0;
		
		try {
			rs = stmt.executeQuery(query);
			printQueryPlan(stmt, query);
			Thread.currentThread().sleep(1000 * 20);
			System.out.println("Start count: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
			long start = System.currentTimeMillis();
			while(rs.next()) {
				rs.getLong(1);
				rs.getString(2);
				rs.getString(3);
				rs.getString(4);
			}
			time = System.currentTimeMillis() - start;
			System.out.println("End count: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
		} catch (Exception e) {
			throw e;
		} finally {
			JDBCUtils.close(rs, stmt, conn);
		}
		return time;
	}
	
	private static void printQueryPlan(Statement stmt, String sql) throws SQLException {
		if(!showPlan){
			return;
		}
		System.out.println("Query Plans for execute " + sql);
		TeiidStatement tstatement = stmt.unwrap(TeiidStatement.class);
		PlanNode queryPlan = tstatement.getPlanDescription();
		System.out.println(queryPlan);
	}

	public static void main(String[] args) throws Exception {
		SerializationCaculation caculation = new SerializationCaculation();

		if(args.length > 0) {
			for(int i = 0; i < args.length; i++) {
				
				if(args[i].equals("-s") || args[i].equals("--size")){
					int size = Integer.parseInt(args[++i]);
					String query = "SELECT * FROM SERIALTESTVIEW WHERE id < " + size;
					System.out.println("Serialize " + size * 200 + " bytes spend time: " + caculation(query, conn) + " ms\n");
				}
				
				if(args[i].equals("-p") || args[i].equals("--plan")){
					showPlan = true;
				}
			}
		} else {
			caculation.query();
		}	
	}
}
