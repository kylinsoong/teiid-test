package com.simba.couchbase.schemamap.metadata;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.util.ArrayList;
import java.util.HashMap;

public class CBCatalogMetadata {

    private ArrayList<String> m_bucketList;
    private String m_dsiiName;
    private ILogger m_log;
    private HashMap<String, CBSchemaMetadata> m_schemaMetaLookupByDSII;

    public CBCatalogMetadata(String dsiiName, ILogger log) {
        this.m_log = log;
        this.m_bucketList = new ArrayList<>();
        this.m_dsiiName = dsiiName;
        this.m_schemaMetaLookupByDSII = new HashMap<>();
    }
    
    public void addSchemaName(String schemaName) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_bucketList.add(schemaName);
    }
    
    public String getDsiiName() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_dsiiName;
    }
    
    public ArrayList<String> getSchemaNameList() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_bucketList;
    }
    
    public CBSchemaMetadata getSchemaMeta(String schemaDSIIName) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return (CBSchemaMetadata)this.m_schemaMetaLookupByDSII.get(schemaDSIIName);
    }
  
    
    public void mapSchemaMeta(String schemaDSIIName, CBSchemaMetadata schemaMeta) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_schemaMetaLookupByDSII.put(schemaDSIIName, schemaMeta);
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(this.m_dsiiName).append("]");
        return result.toString();
    }
}