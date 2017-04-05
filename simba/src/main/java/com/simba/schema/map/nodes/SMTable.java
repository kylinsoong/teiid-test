package com.simba.schema.map.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMTable extends SMObject {
    
    private List<SMColumn> m_columns = new ArrayList<>();
    
    public SMTable(String name, SMDefinition definition, SMObject parent) {
        super(name, definition, parent);
        this.m_indexName = ("~~~SchemaMapTable-" + name);
        for (Map.Entry<String, SMAttribute> e : this.m_definition.getTableDefinition().entrySet()) {
            this.m_attributes.put(e.getKey(), null);    
        }
        this.m_definition = definition;    
    }
    
    public void addColumn(SMColumn column) {
        this.m_columns.add(column);    
    }
    
    public boolean columnsContainAttribute(String key, String value) {
        
        for (SMColumn column : this.m_columns) {
            String found = (String)column.getAttributes().get(key);
            if ((null != found) && (found.equals(value))) {
                return true;    
            }    
        }
        return false;    
    }
    
    public List<SMColumn> getColumns() {
        return this.m_columns;    
    }
    
    public void removeColumn(SMColumn column) {
        this.m_columns.remove(column);    
    }
    
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n\"");
        sb.append(this.m_indexName);
        sb.append("\":{");
        
        int j = 0;
        for (Map.Entry<String, String> attribute : getAttributes().entrySet()) {
            sb.append("\n\"");
            sb.append((String)attribute.getKey());
            sb.append("\":\"");
            String value = (String)attribute.getValue();
            if (null != value) {
                sb.append(value);    
            }
            sb.append("\"");
            if (j++ < getAttributes().entrySet().size() - 1) {
                sb.append(",");    
            }    
        }
        
        if ((null != this.m_columns) && (0 < this.m_columns.size())) {
            sb.append(",\n\"");
            sb.append("Columns");
            sb.append("\":\n[");
            
            j = 0;
            
            for (SMColumn currentColumn : this.m_columns) {
                sb.append(currentColumn.serialize());
                if (j++ < this.m_columns.size() - 1) {
                    sb.append(",");    
                }    
            }
            
            sb.append("\n]");    
        }
        
        sb.append("\n}\n}");
        return sb.toString();    
    }
}