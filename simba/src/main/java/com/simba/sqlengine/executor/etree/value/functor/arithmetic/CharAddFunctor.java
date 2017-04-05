package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Pair;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;

public class CharAddFunctor
  implements IBinaryArithmeticFunctor
{
  public boolean execute(ETDataRequest paramETDataRequest, ISqlDataWrapper paramISqlDataWrapper1, ISqlDataWrapper paramISqlDataWrapper2, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    if ((paramISqlDataWrapper1.isNull()) || (paramISqlDataWrapper2.isNull()))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    Pair localPair = getRetrievalIndices(paramETDataRequest);
    long l1 = ((Long)localPair.key()).longValue();
    long l2 = ((Long)localPair.value()).longValue();
    String str1 = paramISqlDataWrapper1.getChar();
    String str2 = paramISqlDataWrapper2.getChar();
    String str3 = subString(str1, str2, l1, l2);
    long l3 = paramETDataRequest.getColumn().getColumnLength();
    paramETDataRequest.getData().setChar(str3);
    boolean bool = l2 < str1.length() + str2.length();
    if ((null != paramIWarningListener) && (l2 == l3) && (bool))
    {
      paramIWarningListener.postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
      return false;
    }
    return bool;
  }
  
  private String subString(String paramString1, String paramString2, long paramLong1, long paramLong2)
  {
    assert (paramLong1 >= 0L);
    assert ((paramLong2 > paramLong1) || ((paramLong2 == 0L) && (paramLong1 == 0L)));
    if (paramLong1 > paramString1.length() + paramString2.length()) {
      throw new IllegalArgumentException("Invalid offset for data retrieval");
    }
    if (paramLong1 >= paramString1.length())
    {
      paramLong1 -= paramString1.length();
      paramLong2 -= paramString1.length();
      if (paramLong2 > paramString2.length()) {
        paramLong2 = paramString2.length();
      }
      return paramString2.substring((int)paramLong1, (int)paramLong2);
    }
    if (paramLong2 <= paramString1.length()) {
      return paramString1.substring((int)paramLong1, (int)paramLong2);
    }
    String str = paramString1.substring((int)paramLong1);
    paramLong2 -= paramString1.length();
    paramLong2 = Math.min(Integer.MAX_VALUE - str.length(), Math.min(paramString2.length(), paramLong2));
    return str + paramString2.substring(0, (int)paramLong2);
  }
  
  private Pair<Long, Long> getRetrievalIndices(ETDataRequest paramETDataRequest)
  {
    long l1 = paramETDataRequest.getMaxSize();
    long l2 = paramETDataRequest.getOffset();
    if ((l1 != -1L) && (l1 < 0L)) {
      throw new IllegalArgumentException("Invalid max retrieval size: " + l1);
    }
    if ((l2 % 2L == 1L) || (l2 < 0L)) {
      throw new IllegalStateException("Invalid offset value: " + l2);
    }
    l2 /= 2L;
    long l3 = paramETDataRequest.getColumn().getColumnLength();
    if (l1 == -1L) {
      return new Pair(Long.valueOf(l2), Long.valueOf(l3));
    }
    return new Pair(Long.valueOf(l2), Long.valueOf(Math.min(l3, l2 + l1 / 2L)));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/CharAddFunctor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */