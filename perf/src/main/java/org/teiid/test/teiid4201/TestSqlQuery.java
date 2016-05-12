package org.teiid.test.teiid4201;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestSqlQuery {
    
    String username="user";
    String pwd="";

           public void execute(String query)  {
                  System.out.println("Query:"+query);
                  
                  String url = "jdbc:teiid:apm_public@mm://localhost:54321";
                  Connection connection=null;
                  
                  Properties prop = new Properties();
                  prop.setProperty("FetchSize", "1");
                  prop.setProperty("user", username);
                  prop.setProperty("Password", pwd);
               try{
                   Class.forName("org.teiid.jdbc.TeiidDriver");
                   connection = DriverManager.getConnection(url, prop);
                   System.out.println("Connection ="+connection);
                   Statement statement = connection.createStatement();
                   ResultSet results = statement.executeQuery(query);
                   long n=0;
                   while(results.next()) {
                      ++n;
                   }
                   results.close();
                   statement.close();
                   System.out.println("Total number of record is :"+n);
               } catch (Exception e){
                   e.printStackTrace();
               } finally {
                   try{
                     connection.close();
                   }catch(SQLException e1){
                     // ignore
                   }
               }
           }
           
    public static void main(String as[]){
        new TestSqlQuery().execute("select * from public.share_market_data where frequency=5000 and ts between {ts '2016-04-08 01:00:00.0'} and {ts '2016-04-09 13:00:00.0'}");
    }
}
