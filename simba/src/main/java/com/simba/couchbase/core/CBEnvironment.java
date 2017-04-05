package com.simba.couchbase.core;

import com.simba.dsi.core.impl.DSIEnvironment;
import com.simba.dsi.core.interfaces.IConnection;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;

public class CBEnvironment extends DSIEnvironment {

    public CBEnvironment(CBJDBCDriver driver) throws ErrorException {
        super(driver);
        LogUtilities.logFunctionEntrance(driver.getDriverLog(), new Object[] { driver });
    }
    
    public void close() {
        LogUtilities.logFunctionEntrance(getLog(), new Object[0]);
    }
    
    public IConnection createConnection() throws ErrorException {
        LogUtilities.logFunctionEntrance(getLog(), new Object[0]);
        return new CBConnection(this);
    }
}