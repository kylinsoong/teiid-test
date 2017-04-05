package com.simba.sqlengine.executor.materializer;

import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.support.exceptions.ErrorException;

public abstract class MaterializerBase<T extends IETNode>
  extends AEDefaultVisitor<T>
{
  private final IQueryPlan m_plan;
  private final MaterializerContext m_context;
  
  public MaterializerBase(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    assert (paramIQueryPlan != null);
    assert (paramMaterializerContext != null);
    this.m_plan = paramIQueryPlan;
    this.m_context = paramMaterializerContext;
  }
  
  protected T defaultVisit(IAENode paramIAENode)
    throws ErrorException
  {
    throw new UnsupportedOperationException("Logic Error: Default visit method is called with " + paramIAENode + " from base AE tree builder class");
  }
  
  protected IQueryPlan getQueryPlan()
  {
    return this.m_plan;
  }
  
  protected MaterializerContext getContext()
  {
    return this.m_context;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/MaterializerBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */