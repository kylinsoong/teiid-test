package org.teiid.test.embedded.process;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.example.util.JDBCUtils;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.embedded.plan.TeiidEmbeddedPortfolioPlan;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TeiidEmbeddedPortfolioProcessSimple {

    static String SQL_1 = "SELECT * from Product";

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
        for(int i = 0 ; i < 1 ; i ++)
            JDBCUtils.execute(conn, SQL_1, false);
    }

}
