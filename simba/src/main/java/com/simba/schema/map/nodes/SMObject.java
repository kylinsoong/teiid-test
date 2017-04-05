package com.simba.schema.map.nodes;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SMObject {
    
    protected String m_name;
    protected String m_indexName;
    protected SMObject m_parent;
    
    protected Map<String, String> m_attributes = new LinkedHashMap<>();
    
    protected SMDefinition m_definition;
    
    protected static final String DELIMITER = "~~~SchemaMap";
    
    public SMObject(String name, SMDefinition definition, SMObject parent) {
        this.m_name = name;
        this.m_definition = definition;
        this.m_parent = parent;    
    }
    
    public void addAttribute(String key, String value) {
        this.m_attributes.put(key, value);    
    }
    
    public Map<String, String> getAttributes() { 
        return this.m_attributes;    
    }
    
    public SMDefinition getDefinition() {
        return this.m_definition;    
    }
    
    public String getIndexName() {
        return this.m_indexName;    
    }
    
    public String getName() {
        return this.m_name;    
    }
    
    public SMObject getParent() {
        return this.m_parent;    
    }
    
    public boolean setAttribute(String key, String value) { 
        if (this.m_attributes.containsKey(key)) {
            this.m_attributes.put(key, value);
            return true;    
        }
        return false;    
    }
    
    public void print(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(level);
        sb.append("");
        sb.append(this.m_name);
        sb.append(level);
        sb.append(" - <Attributes>");
        for (Map.Entry<String, String> attribute : this.m_attributes.entrySet()) {
            sb.append(level);
            sb.append(" - ");
            sb.append((String)attribute.getKey());
            sb.append(" : ");
            sb.append((String)attribute.getValue());    
        }
        
        System.out.println(sb.toString());    
    }
    
    public abstract String serialize();
    
    public String toString() {
        return this.m_name;    
    }
}