package com.simba.dsi.dataengine.utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class ExecutionContexts
{
  private int m_paramSetCount;
  private int m_outputParamCount;
  private ArrayList<ParameterMetadata> m_metadata;
  private ArrayList<ArrayList<DataWrapper>> m_outputData;
  private ArrayList<ExecutionContext> m_contexts;
  
  public ExecutionContexts(ArrayList<ParameterMetadata> paramArrayList, ArrayList<ArrayList<ParameterInputValue>> paramArrayList1)
  {
    this.m_paramSetCount = paramArrayList1.size();
    this.m_metadata = paramArrayList;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    this.m_outputParamCount = 0;
    int i = paramArrayList.size();
    Object localObject1;
    Object localObject2;
    for (int j = 0; j < i; j++)
    {
      localObject1 = (ParameterMetadata)paramArrayList.get(j);
      localObject2 = Integer.valueOf(j);
      switch (localObject1.getParameterType())
      {
      case INPUT_OUTPUT: 
        localArrayList1.add(localObject2);
      case OUTPUT: 
      case RETURN_VALUE: 
        this.m_outputParamCount += 1;
        localArrayList2.add(localObject2);
        break;
      default: 
        localArrayList1.add(localObject2);
      }
    }
    this.m_outputData = new ArrayList(this.m_paramSetCount);
    this.m_contexts = new ArrayList(this.m_paramSetCount);
    for (j = 0; j < this.m_paramSetCount; j++)
    {
      localObject1 = new ArrayList(this.m_outputParamCount);
      localObject2 = new ArrayList(this.m_outputParamCount);
      for (int k = 0; k < this.m_outputParamCount; k++)
      {
        ParameterMetadata localParameterMetadata = (ParameterMetadata)paramArrayList.get(((Integer)localArrayList2.get(k)).intValue());
        ParameterOutputValue localParameterOutputValue = new ParameterOutputValue(localParameterMetadata);
        ((ArrayList)localObject2).add(localParameterOutputValue);
        ((ArrayList)localObject1).add(localParameterOutputValue.getData());
      }
      this.m_outputData.add(localObject1);
      this.m_contexts.add(new ExecutionContext((ArrayList)((ArrayList)paramArrayList1.get(j)).clone(), (ArrayList)localObject2));
    }
  }
  
  public Iterator<ExecutionContext> contextIterator()
  {
    return this.m_contexts.iterator();
  }
  
  public ExecutionContextStatus getContextStatus(int paramInt)
  {
    return ((ExecutionContext)this.m_contexts.get(paramInt)).getStatus();
  }
  
  public int getCount()
  {
    return this.m_paramSetCount;
  }
  
  public ArrayList<ParameterMetadata> getMetadata()
  {
    return this.m_metadata;
  }
  
  public Iterator<ArrayList<DataWrapper>> outputIterator()
  {
    return this.m_outputData.iterator();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ExecutionContexts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */