package org.teiid.test.teiid3398;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TEIID3398Reproduce {
    
    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3398Reproduce.class.getClassLoader().getResourceAsStream("teiid-3398/h2-schema.sql")));
        
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
                
        server.deployVDB(TEIID3398Reproduce.class.getClassLoader().getResourceAsStream("teiid-3398/teiid3398-h2-vdb.xml"));
        
        Properties info = new Properties();
        info.setProperty("FetchSize", "2");
        conn = server.getDriver().connect("jdbc:teiid:TEIID-3398", info);
        
        List<Connection> list = new ArrayList<>();
        list.add(server.getDriver().connect("jdbc:teiid:TEIID-3398", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID-3398", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID-3398", info));
        
        System.out.println(server.getAdmin().getSessions());
        
        Thread.sleep(Long.MAX_VALUE);
        
//        execute(conn, "SELECT * FROM share_market_data", true);
        
    }

}
