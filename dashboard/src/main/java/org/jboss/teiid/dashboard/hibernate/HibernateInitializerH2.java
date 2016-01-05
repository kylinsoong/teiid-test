package org.jboss.teiid.dashboard.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.naming.Context;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateInitializerH2 {
	
	static {
	    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	public static  String DIR_BASE = "/home/kylin/server/wildfly-9.0.2.Final/standalone/deployments/dashbuilder.war/WEB-INF";
	
	public static  String DIR_ETC = DIR_BASE + File.separator + "etc";
	
	public static  String DIR_LIB = DIR_BASE + File.separator + "lib";
	
	
	static final String DB_MYSQL = "mysql";
	
	static String[] nativeToSequenceReplaceableDialects = new String[]{"org.hibernate.dialect.H2Dialect",};
	 
	public static void main(String[] args) throws IOException {
		
		Application.setupH2DataSource();
		
		// Configure Hibernate using the hibernate.cfg.xml.
        String hbnCfgPath = DIR_ETC + File.separator + "hibernate.cfg.xml";
        
        Configuration hbmConfig = new Configuration().configure(new File(hbnCfgPath));
        
        hbmConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        
        loadHibernateDescriptors(hbmConfig);
        
        // Initialize the Hibernate session factory.
        ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(hbmConfig.getProperties());
//        ServiceRegistry serviceRegistry = (ServiceRegistry) Application.invokeMethod(serviceRegistryBuilder, "buildServiceRegistry", null);
        ServiceRegistry serviceRegistry = serviceRegistryBuilder.buildServiceRegistry();
        if (serviceRegistry == null){
//        	serviceRegistry = (ServiceRegistry) Application.invokeMethod(serviceRegistryBuilder, "build", null);
//            ServiceRegistry serviceRegistry = serviceRegistryBuilder
        }
        SessionFactory factory = hbmConfig.buildSessionFactory(serviceRegistry);
        
        verifyHibernateConfig();
        
        System.out.println(factory);

	}
	
	static void verifyHibernateConfig() {
		// TODO Auto-generated method stub
		
	}

	static void loadHibernateDescriptors(Configuration hbmConfig) throws IOException {
        Set<File> jars = Application.getJarFiles();
        for (File jar : jars) {
            ZipFile zf = new ZipFile(jar);
            for (Enumeration en = zf.entries(); en.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) en.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith("hbm.xml") && !entry.isDirectory()) {
                    InputStream is = zf.getInputStream(entry);
                    String xml = readXMLForFile(entryName, is);
                    xml = processXMLContents(xml);
                    hbmConfig.addXML(xml);
                    System.out.println(jar + entry.getName());
                }
            }
        }
    }
	
	static String readXMLForFile(String fileName, InputStream is) throws IOException {
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer fileContents = new StringBuffer();
                String lineRead;
                while ((lineRead = reader.readLine()) != null) {
                    fileContents.append(lineRead);
                }
                return fileContents.toString();
            } finally {
                if (reader != null) reader.close();
            }
        } catch (IOException e) {
            return null;
        }
    }
	
	static String processXMLContents(String fileContent) {
      
		String line = "class=\"hilo\"><param name=\"table\">hibernate_unique_key</param><param name=\"column\">next_hi</param><param name=\"max_lo\">0</param></generator>";
        fileContent = StringUtils.replace(fileContent, "class=\"native\"/>", line);
        fileContent = StringUtils.replace(fileContent, "class=\"native\" />", line);
        return fileContent;
    }

}
