package org.teiid.test.teiid4873;

import static org.teiid.test.utils.JDBCUtils.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TEIID4873 {

    public static void main(String[] args) throws Exception {


        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID4873.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));

        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(TEIID4873.class.getClassLoader().getResourceAsStream("teiid4873-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        
        
        String sql = "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for(int i = 0 ; i < 2 ; i ++) {
            ps.setInt(1, 2000 + i);
            ps.setString(2, "AIR");
            ps.setString(3, "Air Plan");
            ps.addBatch();
        }

        ps.executeBatch();
        ps.close();
        
        execute(conn, "SELECT * FROM PRODUCT WHERE ID >= 2000");
        
        close(conn);
    }

}
