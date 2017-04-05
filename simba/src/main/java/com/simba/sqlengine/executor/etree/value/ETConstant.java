package com.simba.sqlengine.executor.etree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.support.exceptions.ErrorException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ETConstant
  extends ETValueExpr
{
  private ISqlDataWrapper m_data;
  
  public ETConstant(ISqlDataWrapper paramISqlDataWrapper)
  {
    this.m_data = paramISqlDataWrapper;
  }
  
  protected ETConstant() {}
  
  public void close() {}
  
  public boolean isOpen()
  {
    return true;
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isNull()
    throws ErrorException
  {
    return (null != this.m_data) && (this.m_data.isNull());
  }
  
  public void open() {}
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = paramETDataRequest.getData();
    if (this.m_data.isNull()) {
      localISqlDataWrapper.setNull();
    } else {
      switch (paramETDataRequest.getData().getType())
      {
      case -5: 
        localISqlDataWrapper.setBigInt(this.m_data.getBigInt());
        break;
      case 2: 
      case 3: 
        localISqlDataWrapper.setExactNumber(this.m_data.getExactNumber());
        break;
      case 6: 
      case 8: 
        localISqlDataWrapper.setDouble(this.m_data.getDouble());
        break;
      case 7: 
        localISqlDataWrapper.setReal(this.m_data.getReal());
        break;
      case -4: 
      case -3: 
      case -2: 
        localISqlDataWrapper.setBinary(this.m_data.getBinary());
        break;
      case -7: 
      case 16: 
        localISqlDataWrapper.setBoolean(this.m_data.getBoolean());
        break;
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        localISqlDataWrapper.setChar(this.m_data.getChar());
        return DataRetrievalUtil.retrieveCharData(localISqlDataWrapper, paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize(), paramETDataRequest.getColumn().getColumnLength(), getWarningListener());
      case 91: 
        localISqlDataWrapper.setDate((Date)this.m_data.getDate().clone());
        break;
      case 92: 
        localISqlDataWrapper.setTime((Time)this.m_data.getTime().clone());
        break;
      case 93: 
        localISqlDataWrapper.setTimestamp((Timestamp)this.m_data.getTimestamp().clone());
        break;
      case -11: 
        localISqlDataWrapper.setGuid(this.m_data.getGuid());
        break;
      case 4: 
        localISqlDataWrapper.setInteger(this.m_data.getInteger());
        break;
      case 5: 
        localISqlDataWrapper.setSmallInt(this.m_data.getSmallInt());
        break;
      case -6: 
        localISqlDataWrapper.setTinyInt(this.m_data.getTinyInt());
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
        throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + this.m_data.getType());
      }
    }
    return false;
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    throw new IndexOutOfBoundsException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/ETConstant.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */