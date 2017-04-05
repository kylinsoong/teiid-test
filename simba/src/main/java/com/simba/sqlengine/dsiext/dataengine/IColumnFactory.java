package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public abstract interface IColumnFactory
{
  public abstract IColumn createColumn(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, List<String> paramList, Nullable paramNullable)
    throws ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/IColumnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */