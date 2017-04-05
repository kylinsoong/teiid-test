package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.support.exceptions.ErrorException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AECountStarAggrFn
  extends AEAggrFn
{
  private ColumnMetadata m_metadata;
  
  public AECountStarAggrFn()
  {
    super(AEAggrFn.AggrFnId.COUNT_STAR);
    try
    {
      this.m_metadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-5, false));
      this.m_metadata.setName(null);
    }
    catch (ErrorException localErrorException)
    {
      throw new IllegalStateException(localErrorException);
    }
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<AEValueExpr> getChildItr()
  {
    return asList().iterator();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    return (this == paramIAENode) || ((paramIAENode instanceof AECountStarAggrFn));
  }
  
  public IColumn getColumn()
  {
    return this.m_metadata;
  }
  
  public void updateColumn() {}
  
  public AECountStarAggrFn copy()
  {
    return new AECountStarAggrFn();
  }
  
  private List<AEValueExpr> asList()
  {
    return Collections.emptyList();
  }
  
  public AEAggrFn.AggrFnQuantifier getSetQuantifier()
  {
    return AEAggrFn.AggrFnQuantifier.ALL;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AECountStarAggrFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */