package com.simba.schema.map.nodes;

import java.util.LinkedHashMap;
import java.util.Map;

public class SMDefinition {

    private Map<String, SMAttribute> m_catalogDefinition = new LinkedHashMap<>();

    private Map<String, SMAttribute> m_schemaDefinition = new LinkedHashMap<>();

    private Map<String, SMAttribute> m_tableDefinition = new LinkedHashMap<>();

    private Map<String, SMAttribute> m_columnDefinition = new LinkedHashMap<>();

    private boolean m_supportsCatalogs = false;

    private boolean m_supportsSchemas = false;
 
    public SMDefinition() {
        SMAttribute attribute = new SMAttribute("Type", "string");
        attribute.setDistinct(true);
        attribute.setRequired(true);
        this.m_catalogDefinition.put("dsiiName", attribute);
        this.m_schemaDefinition.put("dsiiName", attribute);
        this.m_columnDefinition.put("dsiiName", attribute);
        this.m_tableDefinition.put("dsiiName", attribute);
    }
    
    public void addCatalogAttribute(String key, SMAttribute value) {
        this.m_catalogDefinition.put(key, value);
    }
    
    public void addColumnAttribute(String key, SMAttribute value) {
        this.m_columnDefinition.put(key, value);
    }
    
    public void addSchemaAttribute(String key, SMAttribute value) {
        this.m_schemaDefinition.put(key, value);
    }
    
    public void addStandardColumnAttributes() {
        this.m_columnDefinition.put("sourceName", new SMAttribute("Type", "string"));
        this.m_columnDefinition.put("dsiiType", new SMAttribute("Type", "integer"));
        this.m_columnDefinition.put("sourceType", new SMAttribute("Type", "integer"));
    }
    
    public void addStandardTableAttributes() {
        this.m_tableDefinition.put("sourceName", new SMAttribute("Type", "string"));
    }
    
    public void addTableAttribute(String key, SMAttribute value) {
        this.m_tableDefinition.put(key, value);
    }
    
    public Map<String, SMAttribute> getCatalogDefinition() {
        return this.m_catalogDefinition;
    }
    
    public Map<String, SMAttribute> getColumnDefinition() {
        return this.m_columnDefinition;
    }
    
    public Map<String, SMAttribute> getDefinition(SMObject object) {
        
        if ((object instanceof SMCatalog)) {
            return this.m_catalogDefinition;    
        }
        
        if ((object instanceof SMSchema)) {
            return this.m_schemaDefinition;
        }

        if ((object instanceof SMTable)) {
            return this.m_tableDefinition;
        }
        
        if ((object instanceof SMColumn)) {
            return this.m_columnDefinition;
        }
        
        return null;
    }
    
    public Map<String, SMAttribute> getSchemaDefinition() {
        return this.m_schemaDefinition;
    }
    
    public Map<String, SMAttribute> getTableDefinition() {
        return this.m_tableDefinition;
    }
    
    public void setSupportsCatalogs(boolean supportsCatalogs) {
        this.m_supportsCatalogs = supportsCatalogs;
    }
    
    public void setSupportsSchemas(boolean supportsSchema) {
        this.m_supportsSchemas = supportsSchema;    
    }
    
    public boolean supportsCatalogs() {
        return this.m_supportsCatalogs;
    }
    
    public boolean supportsSchemas() {
        return this.m_supportsSchemas;
    }
    
    public String serialize() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n\"");
        sb.append("SchemaMapDefinition");
        sb.append("\": {");
        
        if (supportsCatalogs()) {
            sb.append("\n\"");
            sb.append("CatalogDefinition");
            sb.append("\": {");
            int j = 0;
            for (Map.Entry<String, SMAttribute> attr : this.m_catalogDefinition.entrySet()) {
                sb.append("\n\"");
                sb.append((String)attr.getKey());
                sb.append("\":{");
                sb.append(((SMAttribute)attr.getValue()).serialize());
                sb.append("\n}");
                
                if (j++ < this.m_catalogDefinition.entrySet().size() - 1) {
                    sb.append(",");    
                }    
            }
            sb.append("\n},");    
        }
        
        if (supportsSchemas()) {
            sb.append("\n\"");
            sb.append("SchemaDefinition");
            sb.append("\": {");
            int j = 0;
            for (Map.Entry<String, SMAttribute> attr : this.m_schemaDefinition.entrySet()) {
                sb.append("\n\"");
                sb.append((String)attr.getKey());
                sb.append("\":{");
                sb.append(((SMAttribute)attr.getValue()).serialize());
                sb.append("\n}");
                
                if (j++ < this.m_schemaDefinition.entrySet().size() - 1) {
                    sb.append(",");    
                }    
            }
            sb.append("\n},");    
        }
        
        sb.append("\n\"");
        sb.append("TableDefinition");
        sb.append("\": {");
        int j = 0;
        for (Map.Entry<String, SMAttribute> attr : this.m_tableDefinition.entrySet()) {
            sb.append("\n\"");
            sb.append((String)attr.getKey());
            sb.append("\":{");
            sb.append(((SMAttribute)attr.getValue()).serialize());
            sb.append("\n}");
            
            if (j++ < this.m_tableDefinition.entrySet().size() - 1) {
                sb.append(",");    
            }    
        }
        sb.append("\n},");

        sb.append("\n\"");
        sb.append("ColumnDefinition");
        sb.append("\": {");
        j = 0;
        for (Map.Entry<String, SMAttribute> attr : this.m_columnDefinition.entrySet()) {
            sb.append("\n\"");
            sb.append((String)attr.getKey());
            sb.append("\":{");
            sb.append(((SMAttribute)attr.getValue()).serialize());
            sb.append("\n}");
            
            if (j++ < this.m_columnDefinition.entrySet().size() - 1) {
                sb.append(",");    
            }    
        }
        sb.append("\n}");
        sb.append("\n},");
        return sb.toString();    
    }
}