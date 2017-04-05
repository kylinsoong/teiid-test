package com.simba.couchbase.core;

import com.simba.couchbase.dataengine.CBSQLDataEngine;
import com.simba.dsi.core.impl.DSIStatement;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;

public class CBStatement extends DSIStatement {
    
    private CBConnectionSettings m_settings;
    private CBClient m_client;
  
    CBStatement(CBConnection connection) throws ErrorException {
        super(connection);
        this.m_settings = connection.getConnectionSettings();
        this.m_client = connection.getCouchbaseClient();
    }
    
    public void close() {
        LogUtilities.logFunctionEntrance(getLog(), new Object[0]);
    }
    
    public IDataEngine createDataEngine() throws ErrorException {
        CBSQLDataEngine cbEngine = new CBSQLDataEngine(this, this.m_settings, this.m_client);
        this.m_client.setDataEngineRef(cbEngine);
        return new CBSQLDataEngine(this, this.m_settings, this.m_client);
    }
}