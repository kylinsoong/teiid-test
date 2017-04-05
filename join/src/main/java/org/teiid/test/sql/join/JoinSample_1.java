package org.teiid.test.sql.join;

import static org.teiid.test.sql.join.Constants.*;
import static org.teiid.test.jdbc.client.util.JDBCUtils.*;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.language.QueryExpression;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.jdbc.client.util.EmbeddedHelper;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;


public class JoinSample_1 {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        EmbeddedServer server = new EmbeddedServer();
        
        MySQL5ExecutionFactory executionFactory = new TestExecutionFactory();
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-mariadb", executionFactory);
        
        server.addConnectionFactory("java:/MariaDBDS", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(Constants.class.getClassLoader().getResourceAsStream("join-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:JoinExamplesVDB", null);
        
//        execute(conn, "SELECT * FROM T1");
//        execute(conn, "SELECT * FROM T2");
//        execute(conn, "SELECT * FROM T1 INNER JOIN T2 ON T1.a = T2.b");
//        execute(conn, "SELECT * FROM T1 LEFT OUTER JOIN T2 ON T1.a = T2.b");
//        execute(conn, "SELECT * FROM T1 RIGHT OUTER JOIN T2 ON T1.a = T2.b");
//        execute(conn, "SELECT * FROM T1 FULL OUTER JOIN T2 ON T1.a = T2.b");
//        execute(conn, "SELECT * FROM T1 CROSS JOIN T2");
//        
        execute(conn, "SELECT * FROM T1 INNER JOIN T2 ON T1.a = T2.b");
        
        close(conn);
    }
    
    private static class TestExecutionFactory extends  MySQL5ExecutionFactory {
        
        @Override
        public boolean supportsInnerJoins() {
            return true;
        }

        @Override
        public ResultSetExecution createResultSetExecution(QueryExpression command, ExecutionContext executionContext, RuntimeMetadata metadata, Connection conn) throws TranslatorException {
            return super.createResultSetExecution(command, executionContext, metadata, conn);
        }
        
    }

}
