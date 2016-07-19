package org.teiid.test.teiid4311;

public class GroovyTest {
    
//    public static deploymentName = System.getProperty( 'deploymentName' )
//            public static templateName = System.getProperty( 'templateName' )
//            public static driverName = System.getProperty( 'driverName' )
//            public static url = System.getProperty( 'url' )
//            public static username = System.getProperty( 'username' )
//            public static password = System.getProperty( 'password' )

    public static void main(String[] args) {
        
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("connection-url", "jdbc:h2:mem:testshell");
        props.setProperty("driver-name", "h2");
        props.setProperty("user-name", "sa");
        props.setProperty("password", "sa");
    }
}
