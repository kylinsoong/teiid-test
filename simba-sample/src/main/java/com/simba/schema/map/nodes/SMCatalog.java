package com.simba.schema.map.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SMCatalog extends SMObject {
    
    private List<SMSchema> m_schemas = new ArrayList<>();

    private Set<SMTable> m_tables = new HashSet<>();

    public SMCatalog(String name, SMDefinition definition, SMObject parent) {
        super(name, definition, parent);
        this.m_indexName = ("~~~SchemaMapCatalog-" + name);
        for (Map.Entry<String, SMAttribute> catalogDef : this.m_definition.getCatalogDefinition().entrySet()) {
            this.m_attributes.put(catalogDef.getKey(), null);    
        }
        this.m_definition = definition;    
    }
    
    public void addSchema(SMSchema schema) {
        this.m_schemas.add(schema);
    }
    
    public void addTable(SMTable table) {
        this.m_tables.add(table);
    }
    
    public List<SMSchema> getSchemas() {
        return this.m_schemas;
    }
    
    public Set<SMTable> getTables() {
        return this.m_tables;    
    }
    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n\"");
        sb.append(this.m_indexName);
        sb.append("\":{");
        
        for (Map.Entry<String, String> attribute : getAttributes().entrySet()) {
            sb.append("\n\"");
            sb.append((String)attribute.getKey());
            sb.append("\":\"");
            sb.append((String)attribute.getValue());
            sb.append("\",");    
        }
        
        int j = 0;
        if (this.m_definition.supportsSchemas()) {
            sb.append("\n\"");
            sb.append("Schemas");
            sb.append("\":\n[");
            for (SMSchema currentSchema : this.m_schemas) {
                sb.append(currentSchema.serialize());
                if (j++ < this.m_schemas.size() - 1) {
                    sb.append(",");    
                }    
            }
            sb.append("\n]");    
        } else {
            sb.append("\n\"");
            sb.append("Tables");
            sb.append("\":\n[");
            
            for (SMTable currentTable : this.m_tables) {
                sb.append(currentTable.serialize());
                if (j++ < this.m_tables.size() - 1) {
                    sb.append(",");    
                }    
            }
            
            sb.append("\n]");    
        }
        
        sb.append("\n}\n}");
        return sb.toString();    
    }
}