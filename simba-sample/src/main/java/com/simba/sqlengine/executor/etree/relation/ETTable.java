package com.simba.sqlengine.executor.etree.relation;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.CursorType;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.sqlengine.dsiext.dataengine.DSIExtJResultSet;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;

public class ETTable extends ETRelationalExpr {

    protected final DSIExtJResultSet m_table;
  
    private boolean m_isOpen;
    private boolean m_wasLastMoveSuccess;
    private final ArrayList<? extends IColumn> m_selectColumns;
  
    public ETTable(boolean[] paramArrayOfBoolean, DSIExtJResultSet paramDSIExtJResultSet) throws ErrorException {
        super(paramArrayOfBoolean);
        
        if (null == paramDSIExtJResultSet) {
            throw new NullPointerException("table is null.");
        }
        this.m_table = paramDSIExtJResultSet;
        this.m_isOpen = false;
        this.m_wasLastMoveSuccess = false;
        this.m_selectColumns = this.m_table.getSelectColumns();
    }
  
    public void appendRow() throws ErrorException {
        this.m_table.appendRow();
    }
  
    public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor) throws ErrorException {
        return (T)paramIETNodeVisitor.visit(this);
    }
  
    public void close() {
    
        try {
            this.m_table.closeCursor();
        } catch (ErrorException localErrorException) {}
        this.m_isOpen = false;
    }
    
    public void deleteRow() throws ErrorException {
        this.m_table.deleteRow();
    }
  
    public IColumn getColumn(int paramInt) {
        return (IColumn)this.m_selectColumns.get(paramInt);
    }
  
    public int getColumnCount() {
        return this.m_selectColumns.size();
    }
  
    public int getNumChildren() {
        return 0;
    }
  
    public long getRowCount() throws ErrorException {
        return this.m_table.getRowCount();
    }
  
    public boolean isOpen() {
        return this.m_isOpen;
    }
  
    public DSIExtJResultSet getUnderlyingTable() {
        return this.m_table;
    }
  
    public void onStartDMLBatch(DSIExtJResultSet.DMLType paramDMLType, long paramLong) throws ErrorException {
        this.m_table.onStartDMLBatch(paramDMLType, paramLong);
    }
  
    public void onStartRowUpdate() {
        this.m_table.onStartRowUpdate();
    }
  
    public void onFinishDMLBatch() throws ErrorException {
        this.m_table.onFinishDMLBatch();
    }
  
    public void onFinishRowUpdate() throws ErrorException {
        this.m_table.onFinishRowUpdate();
    }
  
    public void open(CursorType paramCursorType) throws ErrorException {
    
        this.m_table.reset();
        this.m_table.setCursorType(paramCursorType);
        this.m_isOpen = true;
    }
    
    public void reset() throws ErrorException {
        this.m_table.reset();
    }
  
    public boolean retrieveData(int paramInt, ETDataRequest paramETDataRequest) throws ErrorException {
    
        DataWrapper localDataWrapper = new DataWrapper();
        boolean bool = this.m_table.getData(paramInt, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), localDataWrapper);
        paramETDataRequest.getData().setValue(localDataWrapper);
        return bool;
    }
  
  
    public boolean writeData(int paramInt, DataWrapper paramDataWrapper, long paramLong, boolean paramBoolean) throws ErrorException {
        return this.m_table.writeData(paramInt, paramDataWrapper, paramLong, paramBoolean);
    }
  
    protected boolean doMove() throws ErrorException {
        this.m_wasLastMoveSuccess = this.m_table.moveToNextRow();
        return this.m_wasLastMoveSuccess;
    }
  
    protected IETNode getChild(int paramInt) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }
}