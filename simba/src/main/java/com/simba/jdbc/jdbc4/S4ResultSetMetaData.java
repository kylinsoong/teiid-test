package com.simba.jdbc.jdbc4;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.jdbc.common.SResultSetMetaData;
import com.simba.jdbc.common.utilities.WrapperUtilities;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import java.sql.SQLException;
import java.util.List;

public class S4ResultSetMetaData
  extends SResultSetMetaData
{
  public S4ResultSetMetaData(List<? extends IColumn> paramList, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    super(paramList, paramILogger, paramIWarningListener);
  }
  
  public boolean isWrapperFor(Class<?> paramClass)
    throws SQLException
  {
    return WrapperUtilities.isWrapperFor(paramClass, this);
  }
  
  public <T> T unwrap(Class<T> paramClass)
    throws SQLException
  {
    return (T)WrapperUtilities.unwrap(paramClass, this);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4ResultSetMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */