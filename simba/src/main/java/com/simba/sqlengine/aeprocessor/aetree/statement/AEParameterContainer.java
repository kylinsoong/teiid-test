package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class AEParameterContainer
{
  private static final Comparator<AEParameter> PARAM_COMPARATOR = new Comparator()
  {
    public int compare(AEParameter paramAnonymousAEParameter1, AEParameter paramAnonymousAEParameter2)
    {
      return paramAnonymousAEParameter1.getIndex() - paramAnonymousAEParameter2.getIndex();
    }
  };
  private List<AEParameter> m_dynParams = new ArrayList();
  
  public List<AEParameter> getParameters()
  {
    return Collections.unmodifiableList(this.m_dynParams);
  }
  
  public void initialize(IAEStatement paramIAEStatement)
  {
    AETreeWalker localAETreeWalker = new AETreeWalker(paramIAEStatement);
    while (localAETreeWalker.hasNext())
    {
      IAENode localIAENode = localAETreeWalker.next();
      if ((localIAENode instanceof AEParameter)) {
        this.m_dynParams.add((AEParameter)localIAENode);
      }
    }
    Collections.sort(this.m_dynParams, PARAM_COMPARATOR);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AEParameterContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */