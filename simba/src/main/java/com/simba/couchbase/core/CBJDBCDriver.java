package com.simba.couchbase.core;

import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.dsi.core.impl.DSIDriver;
import com.simba.dsi.core.impl.DSILogger;
import com.simba.dsi.core.interfaces.IEnvironment;
import com.simba.dsi.core.utilities.Variant;
import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionUtilities;
import java.text.MessageFormat;

public class CBJDBCDriver extends DSIDriver {
    
    public static final String DRIVER_NAME = "CouchbaseJDBC";
    public static final int DRIVER_MAJOR_VERSION = 1;
    public static final int DRIVER_MINOR_VERSION = 0;
    public static final int DRIVER_HOT_FIX_VERSION = 4;
    public static final int DRIVER_BUILD_NUMBER = 1004;
    public static final String VENDOR_NAME = "Simba";
    public static final String RESOURCE_NAME = "messages";
    public static ExceptionBuilder s_DriverMessages = new ExceptionBuilder(101);
    
    private static final String LOG_NAME = "CouchbaseJDBC_driver";

    private DSILogger m_log;

    public CBJDBCDriver() throws ErrorException {
        setDefaultProperties();
        StringBuilder messagesFileName = new StringBuilder(ExceptionUtilities.getPackageName(getClass()));
        messagesFileName.append(".");
        messagesFileName.append("messages");
        this.m_msgSrc.registerMessages(messagesFileName.toString(), 101, "CouchbaseJDBCDriver");
        
        SQLEngineGenericContext.setDefaultMsgSource(this.m_msgSrc);
        this.m_msgSrc.setVendorName("Simba");
    }
    
    public IEnvironment createEnvironment() throws ErrorException {
        return new CBEnvironment(this);
    }

    public DSILogger getDriverLog() {
        if (null == this.m_log) {
            this.m_log = new DSILogger(LOG_NAME);
        }
        return this.m_log;
    }
    
    private void setDefaultProperties() throws ErrorException {

        try {
            setProperty(3, new Variant(0, DRIVER_NAME));
            String driverVer = MessageFormat.format("{0,number,00}.{1,number,00}.{2,number,00}.{3,number,0000}", new Object[] { Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(4), Integer.valueOf(1004) });
            setProperty(5, new Variant(0, driverVer));
            setProperty(1000, new Variant(5, Short.valueOf((short)1)));
            setProperty(10, new Variant(6, Integer.valueOf(0)));
        } catch (Throwable ex) {
            ErrorException err = s_DriverMessages.createGeneralException(CBJDBCMessageKey.DRIVER_DEFAULT_PROP_ERR.name(), "Driver property set error");
            err.initCause(ex);
            throw err;
        }
    }
}