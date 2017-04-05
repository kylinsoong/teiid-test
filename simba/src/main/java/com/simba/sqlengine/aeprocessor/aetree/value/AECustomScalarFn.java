package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.CustomScalarFunction;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AECustomScalarFn
  extends AEValueExpr
{
  private CustomScalarFunction m_customScalarFn;
  private AEValueExprList m_arguments;
  private List<IColumn> m_inputMetadata;
  private ColumnMetadata m_metadata;
  
  public AECustomScalarFn(CustomScalarFunction paramCustomScalarFunction, AEValueExprList paramAEValueExprList)
    throws SQLEngineException
  {
    if (null == paramCustomScalarFunction) {
      throw new NullPointerException();
    }
    this.m_customScalarFn = paramCustomScalarFunction;
    this.m_arguments = paramAEValueExprList;
    paramAEValueExprList.setParent(this);
    try
    {
      initializeColumn(true);
    }
    catch (ErrorException localErrorException)
    {
      throw new RuntimeException(localErrorException);
    }
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AECustomScalarFn copy()
  {
    try
    {
      return new AECustomScalarFn(this.m_customScalarFn, this.m_arguments.copy());
    }
    catch (SQLEngineException localSQLEngineException)
    {
      throw new AssertionError();
    }
  }
  
  public AEValueExprList getArguments()
  {
    return this.m_arguments;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    new AbstractList()
    {
      public AEValueExprList get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AECustomScalarFn.this.getArguments();
        }
        throw new IndexOutOfBoundsException("" + paramAnonymousInt);
      }
      
      public int size()
      {
        return 1;
      }
    }.iterator();
  }
  
  public IColumn getColumn()
  {
    assert (null != this.m_metadata);
    return this.m_metadata;
  }
  
  public CustomScalarFunction getDSICustomScalarFn()
  {
    return this.m_customScalarFn;
  }
  
  public List<IColumn> getInputMetadata()
  {
    assert (null != this.m_inputMetadata);
    return this.m_inputMetadata;
  }
  
  public String getLogString()
  {
    return "AECustomScalarFn: " + this.m_customScalarFn.getName();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AECustomScalarFn)) {
      return false;
    }
    AECustomScalarFn localAECustomScalarFn = (AECustomScalarFn)paramIAENode;
    return (this.m_customScalarFn == localAECustomScalarFn.m_customScalarFn) && (this.m_arguments.isEquivalent(localAECustomScalarFn.m_arguments));
  }
  
  public void updateColumn()
    throws ErrorException
  {
    initializeColumn(false);
  }
  
  private void initializeColumn(boolean paramBoolean)
    throws ErrorException
  {
    CustomScalarFunction localCustomScalarFunction = this.m_customScalarFn;
    int i = this.m_arguments.getNumChildren();
    List localList1 = columnInfoFromArguments();
    localCustomScalarFunction.updateMetadata(localList1, paramBoolean);
    List localList2 = localCustomScalarFunction.getInputMetadata();
    if ((null == localList2) || (i != localList2.size())) {
      throw new RuntimeException("");
    }
    IColumn localIColumn1 = localCustomScalarFunction.getOutputMetadata();
    if (null == localIColumn1) {
      throw new RuntimeException("");
    }
    ArrayList localArrayList = new ArrayList(i);
    Iterator localIterator = localList2.iterator();
    while (localIterator.hasNext())
    {
      IColumn localIColumn2 = (IColumn)localIterator.next();
      localArrayList.add(ColumnMetadata.copyOf(localIColumn2));
    }
    this.m_inputMetadata = localArrayList;
    this.m_metadata = ColumnMetadata.copyOf(localIColumn1);
  }
  
  private List<IColumnInfo> columnInfoFromArguments()
  {
    AEValueExprList localAEValueExprList = getArguments();
    int i = localAEValueExprList.getNumChildren();
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localAEValueExprList.getChild(j)));
    }
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AECustomScalarFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */