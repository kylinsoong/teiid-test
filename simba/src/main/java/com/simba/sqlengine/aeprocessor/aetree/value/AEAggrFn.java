package com.simba.sqlengine.aeprocessor.aetree.value;

import java.util.Iterator;

public abstract class AEAggrFn
  extends AEValueExpr
{
  private final AggrFnId m_fnId;
  
  protected AEAggrFn(AggrFnId paramAggrFnId)
  {
    this.m_fnId = paramAggrFnId;
  }
  
  public AggrFnId getAggrFnId()
  {
    return this.m_fnId;
  }
  
  public abstract Iterator<AEValueExpr> getChildItr();
  
  public abstract AggrFnQuantifier getSetQuantifier();
  
  public static enum AggrFnQuantifier
  {
    DISTINCT,  ALL;
    
    private AggrFnQuantifier() {}
  }
  
  public static enum AggrFnId
  {
    AVG("AVG"),  COUNT("COUNT"),  COUNT_STAR("COUNT"),  MAX("MAX"),  MIN("MIN"),  SUM("SUM");
    
    private String m_sqlName;
    
    private AggrFnId(String paramString)
    {
      this.m_sqlName = paramString;
    }
    
    public String toString()
    {
      return this.m_sqlName;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEAggrFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */