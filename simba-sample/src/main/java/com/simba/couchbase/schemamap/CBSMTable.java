package com.simba.couchbase.schemamap;

import com.simba.schema.map.nodes.SMDefinition;
import com.simba.schema.map.nodes.SMObject;
import com.simba.schema.map.nodes.SMTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CBSMTable extends SMTable implements Comparable<CBSMTable> {
    
    public Set<String> m_columnNameList = new TreeSet<>();
    
    public Map<String, CBSMColumn> m_columnValueList = new HashMap<>();
    
    public int m_tableDimension;
    
    public ArrayList<CBSMColumn> m_indexColumnList = new ArrayList<>();
    
    private boolean m_isFromVirtualTable;
    
    private String m_parentSchemaName;
    
    private String m_primaryKeySourceName;
    
    public CBSMTable(String tableSourceName, String tableDSIIName, String primaryKeySourceName, SMDefinition definition, SMObject parentObject, boolean isVirtualTable) {

        super(tableDSIIName, definition, parentObject);
        this.m_isFromVirtualTable = isVirtualTable;
        this.m_primaryKeySourceName = primaryKeySourceName;
        this.m_parentSchemaName = parentObject.getName();
        
        addAttribute((String)CBSMSchemaMap.SOURCE_NAME.key(), tableSourceName);
        addAttribute((String)CBSMSchemaMap.DSII_NAME.key(), tableDSIIName);
        
        if (!this.m_isFromVirtualTable) {
            this.m_tableDimension = 0;
            CBSMColumn pkColumn = new CBSMColumn(this.m_primaryKeySourceName, "PK", getDefinition(), this, false);
            pkColumn.setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), "STRING");
            pkColumn.setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(12));
            addColumn(pkColumn);    
        }    
    }
    
    public int compareTo(CBSMTable otherTable) {
        
        int BEFORE = -1;
        int EQUAL = 0;
        int AFTER = 1;
        
        if (getName().compareTo(otherTable.getName()) < 0) {
            return BEFORE;    
        }
        
        if (getName().compareTo(otherTable.getName()) == 0) {
            return EQUAL;    
        }
        
        return AFTER;    
    }
    
    public void creatColumnForVirtual(CBSMTable parentSMTable, String tableSourceName, String tableDSIIName) {
        
        CBSMColumn pkColumn = new CBSMColumn(this.m_primaryKeySourceName, "PK", getDefinition(), this, false);
        pkColumn.setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), "STRING");
        pkColumn.setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(12));
        addColumn(pkColumn);
        
        for (int columnIndex = 0; columnIndex < parentSMTable.m_indexColumnList.size(); columnIndex++) {
            CBSMColumn tempIndexColumn = (CBSMColumn)parentSMTable.m_indexColumnList.get(columnIndex);
            addColumn(tempIndexColumn);
            this.m_indexColumnList.add(tempIndexColumn);    
        }
        
        StringBuilder columnSourceName = new StringBuilder();
        StringBuilder columnDSIIName = new StringBuilder();
        columnSourceName.append(tableSourceName).append("$IDX");
        columnDSIIName.append(tableDSIIName).append("_idx");
        CBSMColumn idxColumn = new CBSMColumn(columnSourceName.toString(), columnDSIIName.toString(), getDefinition(), this, true);
        
        idxColumn.setAttribute((String)CBSMSchemaMap.DSII_TYPE.key(), String.valueOf(-5));
        idxColumn.setAttribute((String)CBSMSchemaMap.SOURCE_TYPE.key(), "NUMBER");
        addColumn(idxColumn);
        this.m_indexColumnList.add(idxColumn);    
    }
    
    public boolean isVirtualTable() {
        return this.m_isFromVirtualTable;    
    }
    
    public Set<String> getColumnNameList() {
        return this.m_columnNameList;    
    }
    
    public Map<String, CBSMColumn> getColumnValueList() {
        return this.m_columnValueList;    
    }
    
    public int getTableDimension() {
        return this.m_tableDimension;    
    }
    
    public String getParentSchemaName() {
        return this.m_parentSchemaName;    
    }
    
    public void setColumnNameList(Set<String> columnNameList) {
        this.m_columnNameList = columnNameList;    
    }
    
    public void setColumnValueList(Map<String, CBSMColumn> columnValueList) {
        this.m_columnValueList = columnValueList;    
    }
    
    public void setTableDimension(int tableDimension) {
        this.m_tableDimension = tableDimension;    
    }
    
    public String toString() {
        return getName();    
    }
}
