package com.simba.couchbase.dataengine.resultset;

import com.simba.couchbase.exceptions.CBJDBCMessageKey;
import com.simba.couchbase.schemamap.metadata.CBColumnMetadata;
import com.simba.couchbase.schemamap.parser.CBArrayDimension;
import com.simba.couchbase.schemamap.parser.CBArrayIndexedElement;
import com.simba.couchbase.schemamap.parser.CBAttribute;
import com.simba.couchbase.schemamap.parser.CBName;
import com.simba.couchbase.schemamap.parser.CBSpecialIdentifier;
import com.simba.couchbase.schemamap.parser.generated.Parser;
import com.simba.couchbase.utils.CBQueryUtils;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.support.exceptions.ErrorException;
import java.util.Stack;

public class CBTableColumn extends CBResultColumn {

    private CBName m_sourceName;
    private int m_sqlType;
    private String m_tableDifferentiatorValue;

    public CBTableColumn(CBColumnMetadata inputColumnMeta, String columnIdentifierValue) throws ErrorException {
        super(inputColumnMeta, inputColumnMeta.getColumnSourceName(), inputColumnMeta.getColumnDSIIName());
        this.m_sourceName = Parser.parse(inputColumnMeta.getColumnSourceName());
        this.m_tableDifferentiatorValue = columnIdentifierValue;
        this.m_sqlType = inputColumnMeta.getTypeMetadata().getType();
        if (this.m_sourceName.isSpecialIdentifier()) {}
    }
    
    public CBName getSourceName() {
        return this.m_sourceName;
    }
    
    public boolean isTableColumn() {
        return true;
    }
    
    public String getTableDifferentiatorValue() {
        return this.m_tableDifferentiatorValue;
    }
    
    public String getValueReferenceString() throws ErrorException {
        
        StringBuilder valueRefBuilder = new StringBuilder();
        
        if (this.m_sourceName.isSpecialIdentifier()) {
            CBSpecialIdentifier specialIdentifier = this.m_sourceName.getAsSpecialIdentifier();
            if (specialIdentifier.getIdentifierName().equals("$IDX")) {
                String tableAliasForIndex = specialIdentifier.getParentName().getAlias();
                valueRefBuilder.append("UNNEST_POSITION(").append(tableAliasForIndex).append(")");
                return valueRefBuilder.toString();
            }
            
            if (specialIdentifier.getIdentifierName().equals("$PK")) {
                String bucketAlias = specialIdentifier.getParentName().getAlias();
                valueRefBuilder.append("META(").append(bucketAlias).append(").id");
                return valueRefBuilder.toString();
            }
            
            ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.LET_CLAUSE_BUILD_ERROR_INVALID_KEYCOLUMN_SOURCENAME.name(), new String[0]);
            throw err;
        }
        
        StringBuilder valueReferenceDirect = new StringBuilder();
        if (this.m_sourceName.isArrayDimension()) {
            valueReferenceDirect.append(this.m_sourceName.getAsArrayDimension().getAlias());
        } else {
            Stack<CBName> nameParts = new Stack<>();
            for (CBName sourceNamePointer = this.m_sourceName; (null != sourceNamePointer) && (null == sourceNamePointer.getAlias()); sourceNamePointer = sourceNamePointer.getParentName()) { 
                nameParts.push(sourceNamePointer);
            }
            
            if (null == sourceNamePointer) {
                ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.LET_CLAUSE_BUILD_ERROR_NO_TABLE_ALIAS_FOR_COLUMN.name(), new String[0]);
                throw err;
            }
            
            valueReferenceDirect.append(sourceNamePointer.getAlias());
            while (nameParts.size() > 0) {
                CBName namePart = (CBName)nameParts.pop();
                if (namePart.isAttribute()) {
                    valueReferenceDirect.append(".").append(namePart.getAsAttribute().getAttributeName());
                } else if (namePart.isArrayIndexedElement()) {
                    valueReferenceDirect.append("[").append(namePart.getAsArrayIndexedElement().getIndex()).append("]");
                } else {
                    ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.LET_CLAUSE_BUILD_ERROR_INVALID_FLATTENED_COLUMN_SOURCENAME.name(), new String[0]);
                    throw err;
                }
            }
        }
        
        StringBuilder valueReference = new StringBuilder();
        valueReference.append("IFMISSING(").append(valueReferenceDirect).append(",NULL)");
        switch (this.m_sqlType) {
            case -7:
            case 16:
                return "TOBOOLEAN(" + valueReference.toString() + ")";
                
            case -5: 
            case 4:
            case 8:
                return "TONUMBER(" + valueReference.toString() + ")";
                
            case 12: 
                return "TOSTRING(" + valueReference.toString() + ")";
            }
        
        return null;
    }
    
    public void rebaseSourceName(CBName inputTableSourceName) {
        this.m_sourceName = this.m_sourceName.rebaseName(this.m_sourceName, inputTableSourceName);
    }
}