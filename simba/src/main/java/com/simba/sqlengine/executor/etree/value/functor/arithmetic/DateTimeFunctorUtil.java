package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class DateTimeFunctorUtil
{
  public static final BigInteger DATE_ADD_BIGINT_UPPER_LIMIT = BigInteger.valueOf(2147483647L);
  public static final BigInteger DATE_ADD_BIGINT_LOWER_LIMIT = BigInteger.valueOf(-2147483648L);
  
  public static Timestamp timestampPlusNum(Timestamp paramTimestamp, long paramLong)
    throws ErrorException
  {
    long l1 = paramTimestamp.getTime();
    int i = paramTimestamp.getNanos() % 1000000;
    long l2 = paramLong + l1;
    if ((paramLong & l1 & (l2 ^ 0xFFFFFFFFFFFFFFFF) | (paramLong ^ 0xFFFFFFFFFFFFFFFF) & (l1 ^ 0xFFFFFFFFFFFFFFFF) & l2) < 0L) {
      throw SQLEngineExceptionFactory.datetimeArithOverflowException();
    }
    Timestamp localTimestamp = new Timestamp(l2);
    localTimestamp.setNanos(localTimestamp.getNanos() + i);
    return localTimestamp;
  }
  
  public static Timestamp timestampPlusBigint(Timestamp paramTimestamp, BigInteger paramBigInteger)
    throws ErrorException
  {
    long l = paramBigInteger.longValue();
    if (paramBigInteger.compareTo(BigInteger.valueOf(l)) != 0) {
      throw SQLEngineExceptionFactory.datetimeArithOverflowException();
    }
    return timestampPlusNum(paramTimestamp, l);
  }
  
  public static Date datePlusNum(Date paramDate, long paramLong)
    throws ErrorException
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    if ((paramLong > 2147483647L) || (paramLong < -2147483648L)) {
      throw SQLEngineExceptionFactory.datetimeArithOverflowException();
    }
    localCalendar.add(5, (int)paramLong);
    return new Date(localCalendar.getTimeInMillis());
  }
  
  public static Date datePlusBigInteger(Date paramDate, BigInteger paramBigInteger)
    throws ErrorException
  {
    if ((paramBigInteger.compareTo(DATE_ADD_BIGINT_LOWER_LIMIT) < 0) || (paramBigInteger.compareTo(DATE_ADD_BIGINT_UPPER_LIMIT) > 0)) {
      throw SQLEngineExceptionFactory.datetimeArithOverflowException();
    }
    return datePlusNum(paramDate, paramBigInteger.longValue());
  }
  
  public static Date negateDate(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
    localGregorianCalendar.clear();
    localGregorianCalendar.setTimeInMillis(paramDate.getTime());
    if (1 == localGregorianCalendar.get(0)) {
      localGregorianCalendar.set(0, 0);
    } else {
      localGregorianCalendar.set(0, 1);
    }
    return new Date(localGregorianCalendar.getTimeInMillis());
  }
  
  public static Timestamp negateTimestamp(Timestamp paramTimestamp)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
    localGregorianCalendar.clear();
    localGregorianCalendar.setTimeInMillis(paramTimestamp.getTime());
    if (1 == localGregorianCalendar.get(0)) {
      localGregorianCalendar.set(0, 0);
    } else {
      localGregorianCalendar.set(0, 1);
    }
    Timestamp localTimestamp = new Timestamp(localGregorianCalendar.getTimeInMillis());
    localTimestamp.setNanos(paramTimestamp.getNanos());
    return localTimestamp;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/DateTimeFunctorUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */