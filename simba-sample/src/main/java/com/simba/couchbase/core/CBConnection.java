package com.simba.couchbase.core;

import com.simba.couchbase.commons.SettingsKeys;
import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.CBSchemaMapUtil;
import com.simba.couchbase.schemamap.metadata.CBSMMetadata;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.core.impl.DSIConnection;
import com.simba.dsi.core.impl.DSILogger;
import com.simba.dsi.core.interfaces.IStatement;
import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.PropertyUtilities;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadAuthException;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.schema.map.nodes.SMSchemaMap;
import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import com.simba.support.exceptions.ErrorException;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CBConnection extends DSIConnection {

    private static int s_connectionID = 0;
    
    private static final String LOG_PREFIX_NAME = "CouchbaseJDBC_connection_";
    
    private CBConnectionSettings m_settings;
    
    private CBClient m_client;
        
    protected ILogger m_log;
    
    public CBConnection(CBEnvironment environment) throws ErrorException {
        super(environment);
        LogUtilities.logFunctionEntrance(getConnectionLog(), new Object[] { environment });
        s_connectionID += 1;
        this.m_log = getConnectionLog();
        setDefaultProperties();    
    }
    
    public void close() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);    
    }
    
    public void connect(ConnSettingRequestMap requestMap) throws ErrorException, BadAuthException {

        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_settings = getConnectionSettings(requestMap);
        this.m_client = new CBClient(this.m_settings, this.m_log, getWarningListener());
        SMSchemaMap schemaMap = prepareSchemaMap(this.m_settings);
        CBSMMetadata schemaMapMeta = new CBSMMetadata(schemaMap, this.m_log);
        this.m_client.setSchemaMap(schemaMapMeta);    
    }
    
    public IStatement createStatement() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return new CBStatement(this);    
    }
    
    public void disconnect() throws ErrorException {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_client.disconnect();    
    }
    
    public ILogger getConnectionLog() {
        
        if (null == this.m_log) {
            
            this.m_log = new DSILogger(LOG_PREFIX_NAME + Integer.toString(s_connectionID));
            this.m_log.setLocale(getLocale());    
        }
        
        return this.m_log;    
    }
    
    private SMSchemaMap prepareSchemaMap(CBConnectionSettings settings) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        SMSchemaMap schemaMap = null;
        int schemaMapOperation = settings.m_schemaMapOperation;
        
        switch (schemaMapOperation) {
        case 0:
            break;
            
        case 1:
            try {
                schemaMap = CBSchemaMapUtil.generateSchema(this.m_client, this.m_settings, this.m_log);
                CBSchemaMapUtil.writeSchemaMapToFile(this.m_settings, schemaMap, this.m_log);    
            } catch (ErrorException ex) {
                throw ex;    
            }
            
        case 2:
            try {
                CBSchemaMapUtil.importSchemaToDatabase(this.m_settings.m_localSchemaFile, this.m_client, this.m_log);    
            } catch (ErrorException ex) {
                throw ex;    
            }
            
        case 3:
            try {
                CBSchemaMapUtil.exportSchemaFromDatabase(this.m_settings, this.m_client, this.m_log);    
            } catch (ErrorException ex) {
                throw ex;    
            }
            
        case 4: 
            try {
                CBSchemaMapUtil.deleteDatabaseSchemaMap(this.m_client, this.m_log);    
            } catch (ErrorException ex) {
                throw ex;    
            }    
        }
    
        
        if ((null != this.m_settings.m_localSchemaFile) && (!this.m_settings.m_localSchemaFile.isEmpty())) {
            
            File schemaMapFile = new File(this.m_settings.m_localSchemaFile);
            if (schemaMapFile.exists()) {
                schemaMap = CBSchemaMapUtil.loadSchemaMap(this.m_settings.m_localSchemaFile, this.m_log);    
            }    
        } else {
            try {
                schemaMap = CBSchemaMapUtil.loadSchemaMap(this.m_client, this.m_log);    
            } catch (ErrorException ex) {
                LogUtilities.logError(ex, this.m_log);    
            }    
        }
        
        if (null == schemaMap) {
            schemaMap = CBSchemaMapUtil.generateSchema(this.m_client, this.m_settings, this.m_log);    
        }
        
        return schemaMap;    
    }
    
    public CBConnectionSettings getConnectionSettings() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_settings;    
    }
    
    public CBClient getCouchbaseClient() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_client;    
    }
    
    public ConnSettingResponseMap updateConnectionSettings(ConnSettingRequestMap requestMap) throws BadAuthException, ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        ConnSettingResponseMap responseMap = new ConnSettingResponseMap();
        
        for (String currentKey : CBPropertyKey.getRequiredKeys()) {
            verifyRequiredSetting(currentKey, requestMap, responseMap);    
        }
        
        for (String currentKey : CBPropertyKey.getOptionalKeys()) {
            verifyOptionalSetting(currentKey, requestMap, responseMap);    
        }
        
        return responseMap;    
    }

    private CBConnectionSettings getConnectionSettings(ConnSettingRequestMap requestMap) throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        CBConnectionSettings defined_settings = new CBConnectionSettings();
        
        defined_settings.m_catalog = "default";
        defined_settings.m_authMech = SettingsKeys.CB_DEFAULT_AUTH_MECH_FLAG.booleanValue();
        defined_settings.m_schemaMapOperation = SettingsKeys.CB_DEFAULT_SCHEMA_MAP_OPERATION;
        defined_settings.m_localSchemaFile = "";
        defined_settings.m_sampleSize = 100;
        defined_settings.m_queryMode = SettingsKeys.CB_DEFAULT_QUERY_MODE_FLAG.booleanValue();
        defined_settings.m_scanConsistency = "request_plus";
        defined_settings.m_bucketLookupTypeName = new HashMap<>();
        defined_settings.m_enableSSL = SettingsKeys.CB_DEFAULT_ENABLE_SSL_FLAG.booleanValue();
        defined_settings.m_displaySchemaMapProcess = 0;
        defined_settings.m_host = getRequiredSetting("Host", requestMap).getString();
        
        try {
            defined_settings.m_port = getRequiredSetting("Port", requestMap).getInt();    
        } catch (NumericOverflowException ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_FAIL_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;    
        } catch (IncorrectTypeException ex) {
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_FAIL_ERR.name(), new String[] { ex.getMessage() });
            err.initCause(ex);
            throw err;    
        }
        
        Variant schema_variant = getOptionalSetting("ConnSchema", requestMap);
        if (schema_variant != null) {
            
            try {
                defined_settings.m_catalog = schema_variant.getString();    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_catalog = "";    
            }    
        }
        
        Variant variant = getOptionalSetting("EnableSSL", requestMap);
        if (variant != null) {
            try {
                int enableSSL = variant.getInt();
                if (enableSSL == 1) {
                    defined_settings.m_enableSSL = true;
                    Variant sslCertVariant = getOptionalSetting("SSLCertificate", requestMap);
                    
                    if (sslCertVariant != null) {
                        try {
                            defined_settings.m_sslCert = sslCertVariant.getString();    
                        } catch (Exception ex) {
                            LogUtilities.logError(ex, this.m_log);    
                        }    
                    } else {
                        ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_CONNECT_SSL_ERR.name(), new String[0]);
                        throw err;    
                    }    
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_enableSSL = SettingsKeys.CB_DEFAULT_ENABLE_SSL_FLAG.booleanValue();    
            }    
        }
        
        Variant authMechVariant = getOptionalSetting("AuthMech", requestMap);
        if (null != authMechVariant) {
            try {
                int authMech = authMechVariant.getInt();
                if (authMech == 1) {
                    defined_settings.m_authMech = true;
                    Variant authCredsVariant = getOptionalSetting("CredFile", requestMap);
                    if (authCredsVariant != null) {
                        try {
                            defined_settings.m_authCreds = authCredsVariant.getString();    
                        } catch (Exception ex) {
                            LogUtilities.logError(ex, this.m_log);    
                        }    
                    } else {
                        ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_CONNECT_CRED_ERR.name(), new String[0]);
                        throw err;    
                    }    
                } else {
                    defined_settings.m_authMech = SettingsKeys.CB_DEFAULT_AUTH_MECH_FLAG.booleanValue();    
                }    
            } catch (ErrorException err) {
                throw err;    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_authMech = SettingsKeys.CB_DEFAULT_AUTH_MECH_FLAG.booleanValue();    
            }    
        }
        
        variant = getOptionalSetting("SchemaMapOperation", requestMap);
        if (variant != null) {
            try {
                int schemaMapOperation = variant.getInt();
                switch (schemaMapOperation) {
                case 0:
                    defined_settings.m_schemaMapOperation = 0;
                    break;
                case 1:
                    defined_settings.m_schemaMapOperation = 1;
                    break;
                case 2: 
                    defined_settings.m_schemaMapOperation = 2;
                    break;
                case 3: 
                   defined_settings.m_schemaMapOperation = 3;
                   break;
                case 4: 
                   defined_settings.m_schemaMapOperation = 4;
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_schemaMapOperation = SettingsKeys.CB_DEFAULT_SCHEMA_MAP_OPERATION;    
            }    
        }

        variant = getOptionalSetting("LocalSchemaFile", requestMap);
        if (variant != null) {
            try {
                defined_settings.m_localSchemaFile = variant.getString();    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_localSchemaFile = "";    
            }    
        }
        
        variant = getOptionalSetting("SampleSize", requestMap);
        if (variant != null) {
            try {
                defined_settings.m_sampleSize = variant.getInt();    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_sampleSize = 100;    
            }    
        }
        
        variant = getOptionalSetting("queryMode", requestMap);
        if (null != variant) {
            try {
                int queryMode = variant.getInt();
                if (1 == queryMode) {
                    defined_settings.m_queryMode = true;    
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_queryMode = SettingsKeys.CB_DEFAULT_QUERY_MODE_FLAG.booleanValue();    
            }    
        }
        
        variant = getOptionalSetting("ScanConsistency", requestMap);
        if (null != variant) {
            try {
                String consistencyLevel = variant.getString();
                if (CBQueryUtils.m_consistencyLevelVariableMap.contains(consistencyLevel)) {
                    defined_settings.m_scanConsistency = consistencyLevel;    
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_scanConsistency = "request_plus";    
            }    
        }
        
        variant = getOptionalSetting("Redundancy", requestMap);
        if (null != variant) {
            try {
                int redundancy = variant.getInt();
                if (redundancy == 1) {
                    defined_settings.m_redundancy = true;    
                } else {
                    defined_settings.m_redundancy = SettingsKeys.CB_DEFAULT_USE_REDUNDANCY_FLAG.booleanValue();    
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_redundancy = SettingsKeys.CB_DEFAULT_USE_REDUNDANCY_FLAG.booleanValue();    
            }    
        }
        
        variant = getOptionalSetting("TypeNameList", requestMap);
        if (null != variant) { 
            try {
                Pattern typeNamePattern = Pattern.compile("([a-zA-Z_]\\w*|(?:`[^`]*`)+):([a-zA-Z_]\\w*|(?:`[^`]*`)+)(?:$|,)");
                Matcher typeGroupMatch = typeNamePattern.matcher(variant.getString());
                while (typeGroupMatch.find()) {
                    defined_settings.m_bucketLookupTypeName.put(typeGroupMatch.group(1), typeGroupMatch.group(2));    
                }    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);    
            }     
        }
        
        variant = getOptionalSetting("DisplaySchemaMapProcess", requestMap);
        if (null != variant) {
            try {
                defined_settings.m_displaySchemaMapProcess = variant.getInt();    
            } catch (Exception ex) {
                LogUtilities.logError(ex, this.m_log);
                defined_settings.m_displaySchemaMapProcess = 0;    
            }    
        }
        
        return defined_settings;    
    }
    
    private void setDefaultProperties() throws ErrorException {
        
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        try {
            PropertyUtilities.setCatalogSupport(this, true);
            PropertyUtilities.setSavepointSupport(this, false);
            PropertyUtilities.setSchemaSupport(this, true);
            PropertyUtilities.setStoredProcedureSupport(this, false);
            
            String reservedKeywords = "ALTER, AND, ANY, ARRAY, AS, ASC, BETWEEN, BUCKET, BY, CASE,CAST, COLLATE, CREATE, DATABASE, DELETE, DESC, DISTINCT, DROP, EACH, ELSE,END, EXCEPT, EXISTS, EXPLAIN, FALSE, FIRST, FROM, GROUP, HAVING, IF, IN,INDEX, INLINE, INSERT, INTERSECT, INTO, IS, JOIN, KEYS, LIKE, LIMIT, MISSING,NOT, NOT BETWEEN, NULL, OFFSET, ON, OR, ORDER, OVER, PATH, POOL, PRIMARY, SATISFIES, SELECT, THEN, TRUE, UNION, UNIQUE, UNNEST, UPDATE, USING, VALUED,VIEW, WHEN, WHERE";

            setProperty(101, new Variant(0, "CouchbaseJDBC"));
            setProperty(139, new Variant(0, "User"));
            setProperty(58, new Variant(0, "`"));
            setProperty(62, new Variant(0, reservedKeywords));    
        } catch (Exception e) {
            throw CBJDBCDriver.s_DriverMessages.createGeneralException(CBJDBCMessageKey.CONN_DEFAULT_PROP_ERR.name(), e);    
        }    
    }
}

