package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.SqlDataIntegrityChecker;
import com.simba.support.exceptions.ErrorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ETAbsFn
  extends ETScalarFn
{
  private short m_argType;
  private boolean m_isSigned;
  
  public ETAbsFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (1 == getNumChildren());
    TypeMetadata localTypeMetadata = ((IColumn)paramList1.get(0)).getTypeMetadata();
    short s = localTypeMetadata.getType();
    if (s != paramIColumn.getTypeMetadata().getType()) {
      throw new IllegalArgumentException("Invalid return type for ABS scalar function.");
    }
    switch (s)
    {
    case -6: 
    case -5: 
    case 4: 
    case 5: 
      if (paramIColumn.getTypeMetadata().isSigned()) {
        throw new IllegalArgumentException("Invalid return type for ABS scalar function.");
      }
    case -7: 
    case 2: 
    case 3: 
    case 6: 
    case 7: 
    case 8: 
      this.m_argType = s;
      this.m_isSigned = localTypeMetadata.isSigned();
      break;
    }
    throw new IllegalArgumentException("Invalid argument type: " + s);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    int i;
    switch (this.m_argType)
    {
    case 6: 
    case 8: 
      paramETDataRequest.getData().setDouble(Math.abs(localISqlDataWrapper.getDouble()));
      break;
    case 7: 
      paramETDataRequest.getData().setReal(Math.abs(localISqlDataWrapper.getReal()));
      break;
    case 4: 
      long l = localISqlDataWrapper.getInteger();
      assert (SqlDataIntegrityChecker.checkInteger(l, this.m_isSigned));
      paramETDataRequest.getData().setInteger(Math.abs(l));
      break;
    case 5: 
      i = localISqlDataWrapper.getSmallInt();
      assert (SqlDataIntegrityChecker.checkSmallInt(i, this.m_isSigned));
      paramETDataRequest.getData().setSmallInt(Math.abs(i));
      break;
    case -6: 
      i = localISqlDataWrapper.getTinyInt();
      assert (SqlDataIntegrityChecker.checkSmallInt(i, this.m_isSigned));
      paramETDataRequest.getData().setTinyInt((short)Math.abs(i));
      break;
    case -5: 
      BigInteger localBigInteger = localISqlDataWrapper.getBigInt();
      assert (SqlDataIntegrityChecker.checkBigInt(localBigInteger, this.m_isSigned));
      paramETDataRequest.getData().setBigInt(localBigInteger.abs());
      break;
    case 2: 
    case 3: 
      paramETDataRequest.getData().setExactNumber(localISqlDataWrapper.getExactNumber().abs());
      break;
    case -7: 
      paramETDataRequest.getData().setBoolean(localISqlDataWrapper.getBoolean());
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 0: 
    case 1: 
    default: 
      throw new IllegalStateException("unknown type: " + this.m_argType);
    }
    return false;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETAbsFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */