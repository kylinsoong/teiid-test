package org.teiid.test.teiid4311;

import java.io.IOException;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

public class ModelControllerClientTest {
    
    static String ADD_XA_DATASOURCE = "xa-data-source add --name=MysqlXADS --driver-name=mysql-xa --jndi-name=java:jboss/datasources/MysqlXADS --user-name=test_user --password=test_pass --use-java-context=true --xa-datasource-properties=[DatabaseName=>products, PortNumber=>3306, ServerName=>localhost]";
    static String ADD_DATASOURCE = "/subsystem=datasources/data-source=MysqlDS:add(driver-name=mysql, jndi-name=java:jboss/datasources/MysqlDS, connection-url=jdbc:mysql://localhost:3306/products, user-name=test_user, password=test_pass, enabled=true, use-java-context=true, min-pool-size=5, max-pool-size=30)";
    static String JDBC_DRIVER_INFO = "jdbc-driver-info teiid";
    static String RA_READ_CHILDREN_NAMES = "/subsystem=resource-adapters:read-children-names(child-type=resource-adapter)";
    static String READ_ATTRIBUTE = "/subsystem=datasources/jdbc-driver=h2:read-attribute(name=driver-xa-datasource-class-name)";
    
    
    public static void main(String[] args) throws IOException, CliInitializationException, CommandFormatException {

        ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9990);
        
        DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
        builder.addProperty("name", "testXADS");
        
        
        builder.setOperationName("xa-data-source add");
        
        CommandContext ctx = CommandContextFactory.getInstance().newCommandContext();
        ctx.bindClient(client);
        
//        System.out.println(ADD_XA_DATASOURCE + ":\n" + ctx.buildRequest(ADD_XA_DATASOURCE));
//        System.out.println(ADD_DATASOURCE + ":\n" + ctx.buildRequest(ADD_DATASOURCE));
//        System.out.println(JDBC_DRIVER_INFO + ":\n" + ctx.buildRequest(JDBC_DRIVER_INFO));
//        System.out.println(RA_READ_CHILDREN_NAMES + ":\n" + ctx.buildRequest(RA_READ_CHILDREN_NAMES));
//        System.out.println(READ_ATTRIBUTE + ":\n" + ctx.buildRequest(READ_ATTRIBUTE));
        
        ModelNode node = ctx.buildRequest(ADD_XA_DATASOURCE);
        System.out.println(node.get("steps").getType());
        System.out.println(node.get("steps").get(0).getType());
        
        
//        ModelNode result = client.execute(builder.buildRequest());            
//        System.out.println(result);
        client.close();
        
    }

}
