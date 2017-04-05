package com.simba.jdbc.jdbc4;

import com.simba.dsi.core.interfaces.IConnection;
import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.dataengine.interfaces.IResultSet;
import com.simba.exceptions.ExceptionConverter;
import com.simba.jdbc.common.SArray;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class S4Array
  extends SArray
{
  public S4Array(IArray paramIArray, IConnection paramIConnection, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    super(paramIArray, paramIConnection, paramILogger, paramIWarningListener);
  }
  
  protected Object createArray(long paramLong, int paramInt)
    throws SQLException
  {
    try
    {
      Object localObject = getDSIArray().createArray(paramLong - 1L, paramInt);
      if (2003 == getBaseType())
      {
        IArray[] arrayOfIArray = (IArray[])localObject;
        int i = arrayOfIArray.length;
        IConnection localIConnection = getParentConnection();
        ILogger localILogger = getLogger();
        IWarningListener localIWarningListener = getWarningListener();
        S4Array[] arrayOfS4Array = new S4Array[i];
        for (int j = 0; j < i; j++) {
          arrayOfS4Array[j] = new S4Array(arrayOfIArray[j], localIConnection, localILogger, localIWarningListener);
        }
        return arrayOfS4Array;
      }
      return localObject;
    }
    catch (ErrorException localErrorException)
    {
      throw ExceptionConverter.getInstance().toSQLException(localErrorException, super.getWarningListener());
    }
  }
  
  protected ResultSet createResultSet(long paramLong, int paramInt)
    throws SQLException
  {
    if (0L >= paramLong) {
      throw new IndexOutOfBoundsException("" + paramLong);
    }
    IResultSet localIResultSet = getDSIArray().createResultSet(paramLong - 1L, paramInt);
    assert (null != localIResultSet);
    return new S4ArrayResultSet(this, localIResultSet, getLogger());
  }
  
  protected IConnection getParentConnection()
  {
    return super.getParentConnection();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */