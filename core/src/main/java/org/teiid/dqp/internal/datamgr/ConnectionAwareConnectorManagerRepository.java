package org.teiid.dqp.internal.datamgr;

import java.util.HashMap;

import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;


public class ConnectionAwareConnectorManagerRepository extends ConnectorManagerRepository {

	private static final long serialVersionUID = -2272767946731239198L;
	
	private HashMap<String, Object> connections = new HashMap<String, Object>();

	public ConnectionAwareConnectorManagerRepository(String name, Object connection) {
		super(true);
		this.connections.put(name, connection);
	}
	
	public void addConnection(String name, Object connection) {
		this.connections.put(name, connection);
	}
	
	@Override
	protected ConnectorManager createConnectorManager(String translatorName, String connectionName, ExecutionFactory<Object, Object> ef)throws ConnectorManagerException {
		return new ConnectorManager(translatorName, connectionName, ef){

			@Override
			public Object getConnectionFactory() throws TranslatorException {
				
				if (getConnectionName() == null) {
					return null;
				}
				return connections.get(getConnectionName());
			}};
	}
}
