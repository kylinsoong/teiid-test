package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AENull
  extends AEValueExpr
{
  private ColumnMetadata m_columnMetadata = null;
  
  public AENull()
  {
    try
    {
      this.m_columnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(1));
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }
  
  public AENull(AENull paramAENull)
  {
    this();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    return (this == paramIAENode) || ((paramIAENode instanceof AENull));
  }
  
  public IColumn getColumn()
  {
    return this.m_columnMetadata;
  }
  
  public AEValueExpr copy()
  {
    return new AENull(this);
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return 0;
      }
    };
  }
  
  public void updateColumn()
    throws ErrorException
  {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AENull.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */