package org.teiid.test.teiid3617;

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

public class TEIID3617Reproduce {

    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3617Reproduce.class.getClassLoader().getResourceAsStream("teiid-3617/h2-schema.sql")));
        
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setAuthenticationProperties("anonymous=3");
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
                
        server.deployVDB(TEIID3617Reproduce.class.getClassLoader().getResourceAsStream("teiid-3617/teiid3617-h2-vdb.xml"));
        
        Properties info = new Properties();
        List<Connection> list = new ArrayList<>();
        
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        
        try {
            list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        for(Connection conn : list) {
            conn.close();
        }
        
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        list.add(server.getDriver().connect("jdbc:teiid:TEIID3617", info));
        
        if(list.size() != 7) {
            throw new Exception("list size should be 7");
        }
        
//        execute(conn, "SELECT * FROM share_market_data", true);
        
    }
}
