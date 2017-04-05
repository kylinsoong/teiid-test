package com.simba.schema.map.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMSchema extends SMObject {

    private List<SMTable> m_tables = new ArrayList<>();
    
    public SMSchema(String name, SMDefinition definition, SMObject parent) {
        super(name, definition, parent);
        this.m_indexName = ("~~~SchemaMapSchema-" + name);
        for (Map.Entry<String, SMAttribute> currentDef : this.m_definition.getSchemaDefinition().entrySet()) {
            this.m_attributes.put(currentDef.getKey(), null);    
        }
        
        this.m_definition = definition;    
    }
    
    public void addTable(SMTable table) {
        this.m_tables.add(table);    
    }
  
    public List<SMTable> getTables() {
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
            sb.append("\"");
            sb.append(",");    
        }
        
        sb.append("\n\"");
        sb.append("Tables");
        sb.append("\":\n[");
        
        int j = 0;
        for (SMTable currentTable : this.m_tables) {
            sb.append(currentTable.serialize());
            if (j++ < this.m_tables.size() - 1) {
                sb.append(",");    
            }    
        }
        
        sb.append("\n]");
        sb.append("\n}\n}");
        return sb.toString();    
    }
}