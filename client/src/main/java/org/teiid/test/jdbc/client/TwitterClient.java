package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;

import javax.sql.rowset.serial.SerialBlob;


/**
 * 
 * -Dhttp.proxyHost=squid.apac.redhat.com
 * -Dhttp.proxyPort=3128
 * 
 * @author kylin
 *
 */
public class TwitterClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:twitter@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        String sql = "select tweet.* from (call twitter.invokeHTTP(endpoint=>'https://api.twitter.com/1.1/statuses/user_timeline.json', action=>'GET')) w, "
                + "XMLTABLE('/myxml/myxml' passing JSONTOXML('myxml', w.result) columns "
                + "id string PATH 'id_str', "
                + "created_at string PATH 'created_at', "
                + "text string PATH 'text') tweet";
        
        CallableStatement cStmt = conn.prepareCall("call invokeHTTP(endpoint=>'https://api.twitter.com/1.1/statuses/user_timeline.json', action=>'GET')");
        cStmt.execute();
        SerialBlob blob =  (SerialBlob) cStmt.getObject(1);
        System.out.println(blob.length());
//        System.out.println(blob.getBinaryStream());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(blob.getBinaryStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while(line != null){
            sb.append(line);
            sb.append('\n');
            line = br.readLine();
        }
        System.out.println(sb.toString());

        execute(conn, "SELECT JSONTOXML('xml', '{\"firstName\" : \"John\" , \"children\" : [ \"Randy\", \"Judy\" ]}')", false);
        
        execute(conn, "SELECT * FROM TwitterUserTimelineView", false);
        
        execute(conn, sql, true);
    }

}
