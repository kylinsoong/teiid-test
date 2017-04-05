package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.dsi.dataengine.utilities.ParameterType;
import com.simba.support.exceptions.ErrorException;

public class ProcedureParameterMetadata
  extends ParameterMetadata
{
  private boolean m_hasDefaultValue;
  
  public ProcedureParameterMetadata(int paramInt1, ParameterType paramParameterType, int paramInt2, boolean paramBoolean)
    throws ErrorException
  {
    super(paramInt1, paramParameterType, paramInt2);
    this.m_hasDefaultValue = paramBoolean;
  }
  
  public ProcedureParameterMetadata(int paramInt1, ParameterType paramParameterType, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    throws ErrorException
  {
    super(paramInt1, paramParameterType, paramInt2, paramBoolean2);
    this.m_hasDefaultValue = paramBoolean1;
  }
  
  public boolean hasDefaultValue()
  {
    return this.m_hasDefaultValue;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/ProcedureParameterMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */