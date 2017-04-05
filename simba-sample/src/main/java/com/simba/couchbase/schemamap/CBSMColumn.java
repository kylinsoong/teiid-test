package com.simba.couchbase.schemamap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.simba.schema.map.nodes.SMColumn;
import com.simba.schema.map.nodes.SMDefinition;
import com.simba.schema.map.nodes.SMObject;

public class CBSMColumn extends SMColumn implements Comparable<CBSMColumn> {
    
    private boolean m_isIndexColumn;
  
    public CBSMColumn(String sourceColumnName, String dsiiColumnName, SMDefinition definition, SMObject parent, boolean isIndex) {
        super(dsiiColumnName, definition, parent);
        this.m_isIndexColumn = isIndex;
        setAttribute((String)CBSMSchemaMap.SOURCE_NAME.key(), sourceColumnName);
        setAttribute((String)CBSMSchemaMap.DSII_NAME.key(), dsiiColumnName);
    }
    
    public int compareTo(CBSMColumn otherColumn) {
        int BEFORE = -1;
        int EQUAL = 0;
        int AFTER = 1;
        
        if (getName().compareTo(otherColumn.getName()) < 0) {
            return BEFORE;    
        }
        
        if (getName().compareTo(otherColumn.getName()) == 0) {
            return EQUAL;
        }
        return AFTER;
    }
    
    public void initColumnForTable(JsonNode columnValue) {
        JsonNodeType jsonDataType = columnValue.getNodeType();
        setColumnTypeAttribute(jsonDataType, columnValue);
    }
    
    public void updateColumnForTable(JsonNodeType jsonDataType, JsonNode columnValue) {
        if (null != columnValue) {
            setColumnTypeAttribute(jsonDataType, columnValue);
        }
    }
    
    public boolean isIndexColumn() {
        return this.m_isIndexColumn;
    }
    
    private void setColumnTypeAttribute(JsonNodeType jsonDataType, JsonNode columnValue) {
        
        switch (jsonDataType) {
        case NUMBER:
            setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), jsonDataType.toString());
            if (columnValue.isInt()) {
                setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(4));
            } else if (columnValue.isBigInteger()) {
                setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(-5));
            } else if (columnValue.isDouble()) {
                setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(8));
            }
            break;
            
        case STRING:
            setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), jsonDataType.toString());
            setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(12));
            break;
            
        case BOOLEAN:
            setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), jsonDataType.toString());
            setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(16));
            break;
            
        case NULL:
            setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), jsonDataType.toString());
            setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(12));
            break;
            
        case MISSING:
            setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), jsonDataType.toString());
            setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(12));
            break;
        }
    }
}