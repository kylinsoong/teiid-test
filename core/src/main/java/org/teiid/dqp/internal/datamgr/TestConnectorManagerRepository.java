package org.teiid.dqp.internal.datamgr;

import org.teiid.dqp.service.AutoGenDataService;

public class TestConnectorManagerRepository extends ConnectorManagerRepository {

	private static final long serialVersionUID = -7138030116337865351L;
	
	public TestConnectorManagerRepository() {
		super(true);
	}

	@Override
	public ConnectorManager getConnectorManager(String connectorName) {
		return new AutoGenDataService();
	}

}
