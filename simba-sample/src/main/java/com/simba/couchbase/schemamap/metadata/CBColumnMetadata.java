package com.simba.couchbase.schemamap.metadata;

import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;

public class CBColumnMetadata extends ColumnMetadata {

    private String m_columnDSIIName;
    private String m_columnSourceName;
    private boolean m_isPKColumn;
  
    public CBColumnMetadata(String columnSourceName, String columnDSIIName, TypeMetadata typeMetadata) throws NullPointerException {

        super(typeMetadata);
        this.m_columnSourceName = columnSourceName;
        this.m_columnDSIIName = columnDSIIName;
        this.m_isPKColumn = false;
    }
    
    public boolean isPKColumn() {
        return this.m_isPKColumn;
    }
    
    public String getColumnDSIIName() {
        return this.m_columnDSIIName;
    }
    
    public String getColumnSourceName() {
        return this.m_columnSourceName;
    }
    
    public void setPKColumn(boolean isPKColumn) {
        this.m_isPKColumn = isPKColumn;
    }
}