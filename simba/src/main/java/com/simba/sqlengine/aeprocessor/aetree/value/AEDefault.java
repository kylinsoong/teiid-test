package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AEDefault
  extends AEValueExpr
{
  private ColumnMetadata m_columnMetadata;
  
  public AEDefault()
  {
    this.m_columnMetadata = AEValueExpr.createColumnMetadata();
  }
  
  private AEDefault(IColumn paramIColumn)
  {
    this.m_columnMetadata = ColumnMetadata.copyOf(paramIColumn);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AEValueExpr copy()
  {
    return new AEDefault(this.m_columnMetadata);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return Collections.emptyList().iterator();
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMetadata;
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEDefault)) {
      return false;
    }
    AEDefault localAEDefault = (AEDefault)paramIAENode;
    return this.m_columnMetadata.equals(localAEDefault.m_columnMetadata);
  }
  
  public void setMetadata(IColumn paramIColumn)
  {
    this.m_columnMetadata = ColumnMetadata.copyOf(paramIColumn);
  }
  
  public void setParent(IAENode paramIAENode)
  {
    super.setParent(paramIAENode);
  }
  
  public void updateColumn()
    throws ErrorException
  {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEDefault.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */