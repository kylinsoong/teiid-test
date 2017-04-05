package com.simba.couchbase.schemamap.metadata;

import com.simba.support.ILogger;
import com.simba.support.LogUtilities;
import java.util.ArrayList;
import java.util.HashMap;

public class CBSchemaMetadata {

    private String m_dsiiName;
    private ILogger m_log;
    private HashMap<String, CBTableMetadata> m_tableMetaLookupByDSII;
    private ArrayList<String> m_tableNameList;
  
    public CBSchemaMetadata(String dsiiName, ILogger log) {
        this.m_log = log;
        this.m_tableNameList = new ArrayList<>();
        this.m_dsiiName = dsiiName;
        this.m_tableMetaLookupByDSII = new HashMap<>();
    }

    public void addTableMeta(String tableName, CBTableMetadata tableMeta) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_tableMetaLookupByDSII.put(tableName, tableMeta);
    }

    public void addTableName(String schemaName) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        this.m_tableNameList.add(schemaName);
    }
 
    public String getDsiiName() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_dsiiName;
    }

    public CBTableMetadata getTableMeta(String tableDSIIName) {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return (CBTableMetadata)this.m_tableMetaLookupByDSII.get(tableDSIIName);
    }
    
    public ArrayList<String> getTableNameList() {
        LogUtilities.logFunctionEntrance(this.m_log, new Object[0]);
        return this.m_tableNameList;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(this.m_dsiiName).append("]");
        return result.toString();
    }
}