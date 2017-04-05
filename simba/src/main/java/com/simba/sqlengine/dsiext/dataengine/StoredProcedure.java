package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.utilities.ExecutionResults;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;

public abstract class StoredProcedure
{
  private String m_catalogName;
  private String m_procedureName;
  private String m_schemaName;
  
  protected StoredProcedure(String paramString1, String paramString2, String paramString3)
  {
    this.m_catalogName = paramString1;
    this.m_schemaName = paramString2;
    this.m_procedureName = paramString3;
  }
  
  public abstract void execute(ArrayList<ProcedureParameterValue> paramArrayList)
    throws ErrorException;
  
  public String getCatalogName()
  {
    return this.m_catalogName;
  }
  
  public abstract ArrayList<ProcedureParameterMetadata> getParameters()
    throws ErrorException;
  
  public String getProcedureName()
  {
    return this.m_procedureName;
  }
  
  public abstract ExecutionResults getResults()
    throws ErrorException;
  
  public String getSchemaName()
  {
    return this.m_schemaName;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/StoredProcedure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */