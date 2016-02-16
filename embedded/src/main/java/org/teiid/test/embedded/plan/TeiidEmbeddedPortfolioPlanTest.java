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
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TeiidEmbeddedPortfolioPlanTest {
    
    static String SQL_1 = "SELECT e.title, e.lastname FROM Employees AS e JOIN Departments AS d ON e.dept_id = d.dept_id WHERE year(e.birthday) >= 1970 AND d.dept_name = 'Engineering'";
    static String SQL_2 = "SELECT max(e1) FROM g1 WHERE e2 = 1";
    static String SQL_2_1 = "SELECT max(pm1.g1.e1) FROM pm1.g1 WHERE e2 = 1";
    static String SQL_3 = "select * from A inner join B on (A.x = B.x) where B.y = 3";
    static String SQL_3_1 = "select * from B inner join A on (A.x = B.x) where B.y = 3";
    static String SQL_4 = "SELECT e1 FROM (SELECT e1 FROM pm1.g1) AS x";
    static String SQL_5 = "SELECT pm1.g1.e1, pm1.g2.e2, pm1.g3.e3 from pm1.g1 inner join (pm1.g2 left outer join pm1.g3 on pm1.g2.e1=pm1.g3.e1) on pm1.g1.e1=pm1.g3.e1";
    
    
    public static void main(String[] args) throws Exception{

        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account-test", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidEmbeddedPortfolioPlanTest.class.getClassLoader().getResourceAsStream("data/h2-test-schema.sql")));
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
        managedconnectionFactory.setParentDirectory("src/main/resources/data");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
    
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        server.start(config);
        
        server.deployVDB(TeiidEmbeddedPortfolioPlan.class.getClassLoader().getResourceAsStream("portfolio-vdb-test.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        Statement stmt = conn.createStatement();
        
        stmt.execute("set showplan on");
        
        ResultSet rs = stmt.executeQuery(SQL_5);
        
        TeiidStatement tstmt = stmt.unwrap(TeiidStatement.class);
        PlanNode queryPlan = tstmt.getPlanDescription();
        System.out.println(queryPlan);
        
        close(rs, stmt, conn);
    }

}
