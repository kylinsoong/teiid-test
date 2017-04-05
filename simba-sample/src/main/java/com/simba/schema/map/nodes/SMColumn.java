package com.simba.schema.map.nodes;

import java.util.Map;

public class SMColumn extends SMObject {
    
    protected static final String COLUMN_NAME_PREFIX = "Column-";
    
    public SMColumn(String name, SMDefinition definition, SMObject parent) {
        super(name, definition, parent);
        this.m_indexName = ("~~~SchemaMapColumn-" + name);
        for (Map.Entry<String, SMAttribute> columnDef : this.m_definition.getColumnDefinition().entrySet()) {
            if (!((String)columnDef.getKey()).equals("dsiiName")) {
                this.m_attributes.put(columnDef.getKey(), null);    
            }    
        }
        
        this.m_attributes.put("dsiiName", name);    
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
        
        sb.append("\n}\n}");
        return sb.toString();    
    }
}