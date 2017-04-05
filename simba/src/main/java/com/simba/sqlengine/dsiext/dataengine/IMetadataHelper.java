package com.simba.sqlengine.dsiext.dataengine;

public abstract interface IMetadataHelper
{
  public abstract boolean getNextProcedure(Identifier paramIdentifier);
  
  public abstract boolean getNextTable(Identifier paramIdentifier);
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/IMetadataHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */