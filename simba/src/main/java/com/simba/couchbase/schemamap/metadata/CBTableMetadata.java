package com.simba.couchbase.schemamap.metadata;

import java.util.ArrayList;
import java.util.HashMap;

public class CBTableMetadata {

    private String m_columnIdentifierName;
    private String m_columnIdentifierValue;
    private HashMap<String, CBColumnMetadata> m_columnMetaMap;
    private ArrayList<CBColumnMetadata> m_columnMetaList;
    private String m_dsiiName;
    private String m_sourceName;
  
    public CBTableMetadata(String dsiiName, String sourceName) {
        this.m_dsiiName = dsiiName;
        this.m_sourceName = sourceName;
        this.m_columnMetaMap = new HashMap<>();
        this.m_columnMetaList = new ArrayList<>();
    }
    
    public void addColumnMetadata(CBColumnMetadata columnMeta) {
        this.m_columnMetaList.add(columnMeta);
    }
    
    public String getColumnIdentifierName() {
        return this.m_columnIdentifierName;
    }

    public String getColumnIdentifierValue() {
        return this.m_columnIdentifierValue;
    }
    
    public CBColumnMetadata getColumnMetadata(String columnDSIIName) {
        return (CBColumnMetadata)this.m_columnMetaMap.get(columnDSIIName);
    }
    
    public ArrayList<CBColumnMetadata> getColumnMetadataList() {
        return this.m_columnMetaList;
    }
    
    public String getDsiiName() {
        return this.m_dsiiName;
    }
    
    public String getSourceName() {
        return this.m_sourceName;
    }
    
    public void mapColumnMetadata(String columnDSIIName, CBColumnMetadata columnMeta) {
        this.m_columnMetaMap.put(columnDSIIName, columnMeta);
    }
    
    public void setColumnIdentifierName(String columnIdentifierName) {
        this.m_columnIdentifierName = columnIdentifierName;
    }

    public void setColumnIdentifierValue(String columnIdentifierValue) {
        this.m_columnIdentifierValue = columnIdentifierValue;
    }
    
    public void setDsiiName(String dsiiName) {
        this.m_dsiiName = dsiiName;
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(this.m_sourceName).append("\t").append(this.m_dsiiName).append("\t").append("]");
        return result.toString();
    }
}