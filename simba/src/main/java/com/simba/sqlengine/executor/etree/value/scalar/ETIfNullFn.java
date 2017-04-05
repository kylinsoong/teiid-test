package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETIfNullFn
  extends ETScalarFn
{
  public ETIfNullFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert ((2 == paramList.size()) && (2 == paramList1.size()));
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    long l1 = paramETDataRequest.getOffset();
    long l2 = paramETDataRequest.getMaxSize();
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0, l1, l2);
    boolean bool = hasMoreData(0);
    if (localISqlDataWrapper.isNull())
    {
      localISqlDataWrapper = getArgumentData(1, l1, l2);
      if (localISqlDataWrapper.isNull())
      {
        paramETDataRequest.getData().setNull();
        return false;
      }
      bool = hasMoreData(1);
    }
    switch (localISqlDataWrapper.getType())
    {
    case -5: 
      paramETDataRequest.getData().setBigInt(localISqlDataWrapper.getBigInt());
      break;
    case 2: 
    case 3: 
      paramETDataRequest.getData().setExactNumber(localISqlDataWrapper.getExactNumber());
      break;
    case 6: 
    case 8: 
      paramETDataRequest.getData().setDouble(localISqlDataWrapper.getDouble());
      break;
    case 7: 
      paramETDataRequest.getData().setReal(localISqlDataWrapper.getReal());
      break;
    case -4: 
    case -3: 
    case -2: 
      paramETDataRequest.getData().setBinary(localISqlDataWrapper.getBinary());
      break;
    case -7: 
    case 16: 
      paramETDataRequest.getData().setBoolean(localISqlDataWrapper.getBoolean());
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      paramETDataRequest.getData().setChar(localISqlDataWrapper.getChar());
      break;
    case 91: 
      paramETDataRequest.getData().setDate(localISqlDataWrapper.getDate());
      break;
    case 92: 
      paramETDataRequest.getData().setTime(localISqlDataWrapper.getTime());
      break;
    case 93: 
      paramETDataRequest.getData().setTimestamp(localISqlDataWrapper.getTimestamp());
      break;
    case -11: 
      paramETDataRequest.getData().setGuid(localISqlDataWrapper.getGuid());
      break;
    case 4: 
      paramETDataRequest.getData().setInteger(localISqlDataWrapper.getInteger());
      break;
    case 5: 
      paramETDataRequest.getData().setSmallInt(localISqlDataWrapper.getSmallInt());
      break;
    case -6: 
      paramETDataRequest.getData().setTinyInt(localISqlDataWrapper.getTinyInt());
      break;
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw new IllegalArgumentException("Invalid data type to IFNULL scalar: " + localISqlDataWrapper.getType());
    }
    return bool;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETIfNullFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */