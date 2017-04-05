package com.simba.couchbase.core;

import com.simba.jdbc.common.CommonJDBCPropertyKey;
import java.util.ArrayList;
import java.util.List;

public class CBPropertyKey extends CommonJDBCPropertyKey {
    
    public static final String USERNAME = "UID";
    public static final String PASSWORD = "PWD";
    public static final String LANGUAGE = "Language";
    public static final String HOST_KEY = "Host";
    public static final String PORT_KEY = "Port";
    public static final String CATALOG_KEY = "ConnSchema";
    public static final String ENABLE_SSL_KEY = "EnableSSL";
    public static final String QUERY_MODE = "queryMode";
    public static final String SCAN_CONSISTENCY_KEY = "ScanConsistency";
    public static final String SSL_CERTIFICATE_KEY = "SSLCertificate";
    public static final String USE_REDUNDANCY_KEY = "Redundancy";
    public static final String BUCKET_TYPENAME_LIST = "TypeNameList";
    public static final String SCHEMA_MAP_OPERATION = "SchemaMapOperation";
    public static final String LOCAL_SCHEMA_FILE = "LocalSchemaFile";
    public static final String SCHEMAMAP_SAMPLE_SIZE = "SampleSize";
    public static final String SCHEMAMAP_GEN_PROCESS_DISPLAY = "DisplaySchemaMapProcess";
    public static final String AUTHMECH = "AuthMech";
    public static final String CREDENTIAL_FILE = "CredFile";
    public static final String DSI_LOG_LEVEL = "DSILogLevel";
    public static final String DRIVER_LOG_LEVEL = "loglevel";
    public static final String INTERNAL_LOG_LEVEL = "DriverLogLevel";
  
    public static List<String> getCommonRequiredKeys() {
        List<String> result = new ArrayList<>();
        return result;
    }
    
    public static List<String> getOptionalKeys() {
        List<String> result = getCommonOptionalKeys();
        return result;
    }
    
    public static List<String> getRequiredKeys() {
        List<String> result = getCommonRequiredKeys();
        return result;
    }
}