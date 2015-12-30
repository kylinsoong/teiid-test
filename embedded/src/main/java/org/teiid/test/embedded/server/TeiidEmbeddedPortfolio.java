/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.test.embedded.server;


import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;


/**
 * This example is same as "Teiid Quick Start Example", you can find at http://jboss.org/teiid/quickstart
 * whereas the example shows how to use Dynamic VDB using a server based deployment, this below code shows to use same
 * example using Embedded Teiid. This uses a memory based H2 database and File source as sources and provides a 
 * view layer using DDL. 
 * 
 * Note that this example shows how to integrate the traditional sources like jdbc, file, web-service etc, however you are 
 * not limited to only those sources. As long as you can extended and provide implementations for
 *  - ConnectionfactoryProvider
 *  - Translator
 *  - Metadata Repository
 *  
 *  you can integrate any kind of sources together, or provide a JDBC interface on a source or expose with with known schema.
 */
@SuppressWarnings("nls")
public class TeiidEmbeddedPortfolio {
	
	
	public static void main(String[] args) throws Exception {
	    
	    EmbeddedHelper.enableLogger(Level.INFO);
				
		
		DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
		RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidEmbeddedPortfolio.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));
		
		EmbeddedServer server = new EmbeddedServer();
		
		H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator("translator-h2", executionFactory);
		
		server.addConnectionFactory("java:/accounts-ds", ds);
		
    	FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
    	fileExecutionFactory.start();
    	server.addTranslator("file", fileExecutionFactory);
    	
    	FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
		managedconnectionFactory.setParentDirectory("src/main/resources/data");
		server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
	
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.setTransactionManager(EmbeddedHelper.getTransactionManager());
		
		SocketConfiguration s = new SocketConfiguration();
        InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
        s.setBindAddress(addr.getHostName());
        s.setPortNumber(addr.getPort());
        s.setProtocol(WireProtocol.teiid);
        config.addTransport(s);
        
        config.setSecurityDomain("teiid-security-file");
        config.setSecurityHelper(EmbeddedHelper.getSecurityHelper());
        
        server.start(config);
    	
		server.deployVDB(TeiidEmbeddedPortfolio.class.getClassLoader().getResourceAsStream("portfolio-vdb.xml"));
		
		Thread.sleep(Long.MAX_VALUE);


	}



}
