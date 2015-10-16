package org.teiid.test.sql.functions.json;

import static org.teiid.example.util.JDBCUtils.close;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.core.types.SQLXMLImpl;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class JSONFunctionsTest {
    
    private Connection conn = null;
    
    public JSONFunctionsTest() throws Exception {
        
        EmbeddedHelper.enableLogger(Level.OFF);
        
        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(this.getClass().getClassLoader().getResourceAsStream("json-vdb.xml"));
        
        conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
    }

    public static void main(String[] args) throws Exception {

        JSONFunctionsTest test = new JSONFunctionsTest();
        
        test.jsonToxml();
    }

    protected void jsonToxml() throws Exception {

        String json = readJson("person.json");
        convert("person", json);
        
        json = readJson("twitter_user_timeline.json");
        convert("twitter", json);
    }

    private void convert(String root, String json) throws Exception {

        System.out.println("JSON:\n" + json + "\nXML:");
        String result = execute(conn, "SELECT JSONTOXML('" + root + "', '" + json + "')", false);
        System.out.println(result);
        System.out.println();
    }

    private String readJson(String name) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("json/" +name);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String json = br.readLine();
        return json == null ? null : json;
    }
    
    private String execute(Connection connection, String sql, boolean closeConn) throws Exception {        
        
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.createStatement();
            boolean hasResults = stmt.execute(sql);
            if (hasResults) {
                rs = stmt.getResultSet();
                rs.next();
                SQLXMLImpl xml = (SQLXMLImpl) rs.getObject(1);
                return xml.getString();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs, stmt);
            if(closeConn)
                close(connection);
        } 
        return null;
    }
    
    

}
