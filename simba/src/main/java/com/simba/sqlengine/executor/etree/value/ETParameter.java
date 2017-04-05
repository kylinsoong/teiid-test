package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.exceptions.ErrorException;

public class ETParameter
  extends ETValueExpr
{
  private DataWrapper m_inputData;
  private boolean m_isOpened;
  private short m_type;
  
  public ETParameter(IColumn paramIColumn)
  {
    this.m_type = paramIColumn.getTypeMetadata().getType();
    this.m_inputData = null;
    this.m_isOpened = false;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public void close()
  {
    this.m_isOpened = false;
  }
  
  public boolean isOpen()
  {
    return this.m_isOpened;
  }
  
  public void setInputData(DataWrapper paramDataWrapper)
  {
    if (isOpen()) {
      throw new IllegalStateException("Changing parameter value during execution.");
    }
    if (paramDataWrapper.getType() != this.m_type) {
      throw new IllegalArgumentException(String.format("Incompitable type. required : %s, set : %s", new Object[] { TypeUtilities.sqlTypeToString(this.m_type), TypeUtilities.sqlTypeToString((short)paramDataWrapper.getType()) }));
    }
    this.m_inputData = paramDataWrapper;
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public void open()
  {
    this.m_isOpened = true;
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    if (this.m_inputData == null) {
      throw new IllegalStateException("Unresolved parameter.");
    }
    ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
    localISqlDataWrapper.setValue(this.m_inputData);
    if (localISqlDataWrapper.isNull()) {
      return false;
    }
    TypeMetadata localTypeMetadata = paramETDataRequest.getColumn().getTypeMetadata();
    if (localTypeMetadata.isCharacterType()) {
      return DataRetrievalUtil.retrieveCharData(localISqlDataWrapper, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), paramETDataRequest.getColumn().getColumnLength(), getWarningListener());
    }
    if (localTypeMetadata.isBinaryType()) {
      return DataRetrievalUtil.retrieveBinaryData(localISqlDataWrapper, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), paramETDataRequest.getColumn().getColumnLength(), getWarningListener());
    }
    return false;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */