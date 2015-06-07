package org.teiid.dqp.internal.datamgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.junit.Test;
import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedServer.SimpleConnectionFactoryProvider;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TestConnectorManager {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_1() throws ResourceException, TranslatorException, ConnectorManagerException, TeiidComponentException {
		
		DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
		ConnectorManagerRepository connectorManagerRepository = new ProviderAwareConnectorManagerRepository("java:/accounts-ds", new SimpleConnectionFactoryProvider<DataSource>(ds));
		
		ExecutionFactory<?, ?> factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
		
        ConnectorManager cm = connectorManagerRepository.createConnectorManager("translator-h2", "java:/accounts-ds", (ExecutionFactory<Object, Object>)factory);
        
        assertNotNull(cm.getCapabilities());
        assertNotNull(cm.getPushDownFunctions());
        assertNotNull(cm.getId());

        assertEquals("java:/accounts-ds", cm.getConnectionName());
        assertEquals("translator-h2", cm.getTranslatorName());
        assertEquals(factory, cm.getExecutionFactory());
        assertEquals(ds, cm.getConnectionFactory());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_2() throws ResourceException, TranslatorException, ConnectorManagerException, TeiidComponentException {
		
		DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
		ConnectorManagerRepository connectorManagerRepository = new ConnectionAwareConnectorManagerRepository("java:/accounts-ds", ds);
		
		ExecutionFactory<?, ?> factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
		
        ConnectorManager cm = connectorManagerRepository.createConnectorManager("translator-h2", "java:/accounts-ds", (ExecutionFactory<Object, Object>)factory);
        
        assertNotNull(cm.getCapabilities());
        assertNotNull(cm.getPushDownFunctions());
        assertNotNull(cm.getId());

        assertEquals("java:/accounts-ds", cm.getConnectionName());
        assertEquals("translator-h2", cm.getTranslatorName());
        assertEquals(factory, cm.getExecutionFactory());
        assertEquals(ds, cm.getConnectionFactory());
	}

}
