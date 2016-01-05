package org.jboss.teiid.dashboard.hibernate;

import static org.jboss.teiid.dashboard.hibernate.HibernateInitializerH2.loadHibernateDescriptors;
import static org.jboss.teiid.dashboard.hibernate.HibernateInitializerH2.verifyHibernateConfig;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.naming.Context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jboss.dashboard.error.ErrorManager;
import org.jboss.teiid.dashboard.LoggingHelper;

public class HibernateInitializerMysql {
    
    static {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
    }
    
    public static  String DIR_BASE = "/home/kylin/server/wildfly-9.0.2.Final/standalone/deployments/dashbuilder.war/WEB-INF";
    
    public static  String DIR_ETC = DIR_BASE + File.separator + "etc";
    
    public static  String DIR_LIB = DIR_BASE + File.separator + "lib";
    
    
    static final String DB_MYSQL = "mysql";

    public static void main(String[] args) throws IOException {
        
        LoggingHelper.enableLogger(Level.INFO, "");

        Application.setupDataSource();
        
        String hbnCfgPath = DIR_ETC + File.separator + "hibernate-1.cfg.xml";
        
        Configuration hbmConfig = new Configuration().configure(new File(hbnCfgPath));
        
        hbmConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        
        loadHibernateDescriptors(hbmConfig);
        
        ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(hbmConfig.getProperties());
        ServiceRegistry serviceRegistry = serviceRegistryBuilder.buildServiceRegistry();
        SessionFactory factory = hbmConfig.buildSessionFactory(serviceRegistry);
        
        verifyHibernateConfig();
        
        Work work = new Work(){

            public void execute(Connection connection) throws SQLException {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    
                    System.out.println(statement);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
                
            }};
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.doWork(work);
        session.flush();
        
        
        System.out.println(factory);
    }

}
