package org.teiid.test.teiid4311;

import java.util.Properties;
import java.util.UUID;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.jboss.AdminFactory;

public class Main {
    
    static Properties props = new Properties();
    static Properties xaprops = new Properties();
    
    static String dsName = UUID.randomUUID().toString();
    
    static {       
        props.setProperty("user-name", "test_user");
        props.setProperty("password", "test_pass");
        props.setProperty("use-java-context", "true");
        props.setProperty("enabled", "true");
        props.setProperty("min-pool-size", "5");
        props.setProperty("max-pool-size", "25");
        
    }
    
    static String xadsName = UUID.randomUUID().toString();
    
    static {
        xaprops.putAll(props);
        String url = "jdbc:h2:mem:" + xadsName + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        xaprops.setProperty("xa-datasource-properties", "User=sa,Password=sa,URL=" + url + ",Description=\"this is description\"");
    }

    public static void main(String[] args) throws AdminException {

        Admin admin = AdminFactory.getInstance().createAdmin("localhost", 9990, "admin", "password1!".toCharArray());
                
        admin.createDataSource(xadsName, "h2-no-xa", xaprops);
        admin.createDataSource(xadsName, "h2-xa", xaprops);
        
        System.out.println(admin.getDataSourceNames());
        
        System.exit(0);
    }

}
