package com.simba.schema.map.nodes;

import java.util.LinkedHashMap;
import java.util.Map;

public class SMAttribute {
    
    private Map<String, String> m_fields = new LinkedHashMap<>();
    
    private Map<String, String> m_enums = null;
    
    private boolean m_isDistinct = false;
    
    private boolean m_isReadOnly = false;
    
    private boolean m_isRequired = false;
    
    private boolean m_isHidden = false;
    
    public SMAttribute() {
        
    }
  
    public SMAttribute(String key, String value) {
        this.m_fields.put(key, value);    
    }
    
    public void addEnum(String key, String value) {

        if (null == this.m_enums) {
            this.m_enums = new LinkedHashMap<>();
        }
        this.m_enums.put(key, value);
    }
    
    public void addField(String key, String value) {
        this.m_fields.put(key, value);
    }

    public Map<String, String> getEnums() {
        return this.m_enums;
    }
    
    public String getEnumKey(String value) {
        
        for (Map.Entry<String, String> e : this.m_enums.entrySet()) {
            if (value.equals(e.getValue())) {
                return (String)e.getKey();    
            }    
        }
        
        return null;
    }
    
    public String getField(String key) {
        return (String)this.m_fields.get(key);
    }

    public Map<String, String> getFields() {
        return this.m_fields;
    }
    
    public boolean isDistinct() {
        return this.m_isDistinct;
    }
    
    public boolean isHidden() {
        return this.m_isHidden;
    }

    public boolean isReadOnly() {
        return this.m_isReadOnly;    
    }
    
    public boolean isRequired() {
        return this.m_isRequired;    
    }
    
    public void setDistinct(boolean isDistinct) {
        this.m_isDistinct = isDistinct;     
    }
    
    public boolean hasEnumValues() {
        return (null != this.m_enums) && (!this.m_enums.isEmpty());
    }
    
    public boolean hasFieldValues() {
        return (null != this.m_fields) && (!this.m_fields.isEmpty());
    }
    
    public void setField(String key, String value) {
        if (this.m_fields.containsKey(key)) {
            this.m_fields.put(key, value);
        }
    }
    
    public void setHidden(boolean isHidden) {
        this.m_isHidden = isHidden;
    }
    
    public void setReadOnly(boolean isReadOnly) {
        this.m_isReadOnly = isReadOnly;
    }
    
    public void setRequired(boolean isRequired) {
        this.m_isRequired = isRequired;
    }
    
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("Distinct: ");
        sb.append(this.m_isDistinct);
        sb.append(", Read-only: ");
        sb.append(this.m_isReadOnly);
        sb.append(", Required: ");
        sb.append(this.m_isRequired);
        sb.append("\n");
        sb.append("Fields: \n");
        if (null != this.m_fields) {
            for (Map.Entry<String, String> field : this.m_fields.entrySet()) {
                sb.append("");
                sb.append((String)field.getKey());
                sb.append(" : ");
                sb.append((String)field.getValue());
                sb.append("\n");    
            }    
        }
        
        if (null != this.m_enums) {
            sb.append("Enums: \n");
            for (Map.Entry<String, String> currentEnum : this.m_enums.entrySet()) {
                sb.append("");
                sb.append((String)currentEnum.getKey());
                sb.append(" : ");
                sb.append((String)currentEnum.getValue());
                sb.append("\n");    
            }    
        }
        
        sb.append("\n");
        
        return sb.toString();
    }
    
    public String serialize() {
        
        StringBuilder sb = new StringBuilder();

        if (isDistinct()) {
            sb.append("\n\"");
            sb.append("Distinct");
            sb.append("\":");
            sb.append(isDistinct());
            sb.append(",");    
        }
        
        if (isHidden()) {
            sb.append("\n\"");
            sb.append("Hidden");
            sb.append("\":");
            sb.append(isHidden());
            sb.append(",");    
        }
        
        if (isReadOnly()) {
            sb.append("\n\"");
            sb.append("Read-Only");
            sb.append("\":");
            sb.append(isReadOnly());
            sb.append(",");    
        }
        
        if (isRequired()) {
            sb.append("\n\"");
            sb.append("Required");
            sb.append("\":");
            sb.append(isRequired());
            sb.append(",");    
        }
        
        int j;
        if ((hasEnumValues()) || (hasFieldValues())) {
            if (hasEnumValues()) {
                sb.append("\n\"EnumValues\":\n[");
                j = 0;
                for (Map.Entry<String, String> enumEntry : getEnums().entrySet()) {
                    sb.append("\n{\n\"");
                    sb.append((String)enumEntry.getKey());
                    sb.append("\":\"");
                    sb.append((String)enumEntry.getValue());
                    sb.append("\"\n}");
                    if (j++ < getEnums().size() - 1) {
                        sb.append(",");    
                    }    
                }
                
                sb.append("\n]");
                
                if (hasFieldValues()) {
                    sb.append(",");    
                }    
            }
            
            if (hasFieldValues()) {
                j = 0;
                for (Map.Entry<String, String> field : getFields().entrySet()) {
                    sb.append("\n\"");
                    sb.append((String)field.getKey());
                    sb.append("\":\"");
                    sb.append((String)field.getValue());
                    sb.append("\"");
                    
                    if (j++ < getFields().size() - 1) {
                        sb.append(",");    
                    }    
                }    
            }    
        } else {
            sb.append("\n");    
        }
        
        return sb.toString();
  }
}