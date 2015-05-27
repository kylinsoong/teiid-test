package org.teiid.dqp.internal.datamgr;

import java.util.concurrent.ConcurrentHashMap;

import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;

public class ProviderAwareConnectorManagerRepository extends ConnectorManagerRepository {

	private static final long serialVersionUID = -7745466647625190860L;
	
	private ConcurrentHashMap<String, ConnectionFactoryProvider<?>> connectionFactoryProviders = new ConcurrentHashMap<String, ConnectionFactoryProvider<?>>();
	
	public ProviderAwareConnectorManagerRepository(String name, ConnectionFactoryProvider<?> connectionFactoryProvider) {
		super(true);
		this.connectionFactoryProviders.put(name, connectionFactoryProvider);
	}
	
	public void addConnectionFactoryProvider(String name,ConnectionFactoryProvider<?> connectionFactoryProvider) {
		this.connectionFactoryProviders.put(name, connectionFactoryProvider);
	}

	@Override
	protected ConnectorManager createConnectorManager(String translatorName, String connectionName, ExecutionFactory<Object, Object> ef)throws ConnectorManagerException {
		return new ConnectorManager(translatorName, connectionName, ef){

			@Override
			public Object getConnectionFactory() throws TranslatorException {
				
				if (getConnectionName() == null) {
					return null;
				}
				ConnectionFactoryProvider<?> connectionFactoryProvider = connectionFactoryProviders.get(getConnectionName());
				if (connectionFactoryProvider != null) {
					return connectionFactoryProvider.getConnectionFactory();
				}
				return super.getConnectionFactory();
			}};
	}

}
