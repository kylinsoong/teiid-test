/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.test.embedded.plan;

import static org.teiid.example.util.JDBCUtils.close;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.client.plan.PlanNode;
import org.teiid.example.EmbeddedHelper;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

@SuppressWarnings("nls")
public class TeiidEmbeddedPortfolioPlan {
    
    static String SQL_1 = "SELECT A.ID, A.SYMBOL, A.COMPANY_NAME from Accounts.Product AS A";
    static String SQL_1_1 = "SELECT ID, SYMBOL, COMPANY_NAME from Product WHERE ID < 1008";
    static String SQL_2 = "EXEC MarketData.getTextFiles('*.txt')";
    static String SQL_3 = "SELECT * from StockPrices";
    static String SQL_4 = "SELECT * from Stock";
    static String SQL_5 = "SELECT * FROM PRODUCT p INNER JOIN HOLDINGS h ON p.ID = h.PRODUCT_ID JOIN ACCOUNT a ON h.ACCOUNT_ID = a.ACCOUNT_ID";
	static String SQL_6 = "SELECT e.title, e.lastname FROM Employees AS e JOIN Departments AS d ON e.dept_id = d.dept_id WHERE year(e.birthday) >= 1970 AND d.dept_name = 'Engineering'";	
    
    
	public static void main(String[] args) throws Exception {
	    
	    EmbeddedHelper.enableLogger(Level.ALL);
				
	    DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
	    RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidEmbeddedPortfolioPlan.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
        fileExecutionFactory.start();
        server.addTranslator("file", fileExecutionFactory);
        
        FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
        managedconnectionFactory.setParentDirectory("src/main/resources/data");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
    
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(TeiidEmbeddedPortfolioPlan.class.getClassLoader().getResourceAsStream("portfolio-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
//        JDBCUtils.execute(conn, "SELECT COUNT(*) FROM CUSTOMER", false);
//        JDBCUtils.execute(conn, "SELECT COUNT(*) FROM ACCOUNT", false);
//        JDBCUtils.execute(conn, "SELECT COUNT(*) FROM PRODUCT", false);
//        JDBCUtils.execute(conn, "SELECT COUNT(*) FROM HOLDINGS", false);
//        JDBCUtils.execute(conn, SQL_5, false);
        
        Statement stmt = conn.createStatement();
        
        stmt.execute("set showplan debug");
        
        ResultSet rs = stmt.executeQuery(SQL_1);
        
        TeiidStatement tstmt = stmt.unwrap(TeiidStatement.class);
        PlanNode queryPlan = tstmt.getPlanDescription();
        System.out.println(queryPlan);
        
        close(rs, stmt, conn);
	}

}
