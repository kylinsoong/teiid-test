package com.simba.dsi.dataengine.utilities;

import com.simba.dsi.dataengine.interfaces.IArray;
import com.simba.dsi.exceptions.IncorrectTypeException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class DataWrapper
{
  private static Map<Integer, List<Class<?>>> TYPE_CLASSES_MAP;
  private int m_type = 0;
  private Object m_value = null;
  private boolean m_bigIntWasSetAsLongThroughSetData;
  
  private static void addTypeClassesMap()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(BigInteger.class);
    localArrayList.add(Long.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-5), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Boolean.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-7), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(java.sql.Date.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(91), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(BigDecimal.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(3), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Double.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(8), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Double.class);
    localArrayList.add(Float.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(6), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(UUID.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-11), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Long.class);
    localArrayList.add(Integer.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(4), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(BigDecimal.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(2), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Float.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(7), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Integer.class);
    localArrayList.add(Short.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(5), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Time.class);
    localArrayList.add(TimeTz.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(92), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Timestamp.class);
    localArrayList.add(TimestampTz.class);
    localArrayList.add(java.sql.Date.class);
    localArrayList.add(Time.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(93), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Short.class);
    localArrayList.add(Byte.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-6), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(byte[].class);
    localArrayList.add(ByteArrayInputStream.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-2), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(byte[].class);
    localArrayList.add(ByteArrayInputStream.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-4), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(byte[].class);
    localArrayList.add(ByteArrayInputStream.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-3), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    localArrayList.add(BigInteger.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(1), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    localArrayList.add(BigInteger.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-1), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    localArrayList.add(java.util.Date.class);
    localArrayList.add(Calendar.class);
    localArrayList.add(BigInteger.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(12), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(Boolean.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(16), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-8), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-9), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(String.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(-10), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(DSITimeSpan.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(103), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(108), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(109), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(110), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(104), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(111), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(112), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(105), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(113), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(106), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(DSIMonthSpan.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(102), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(101), localArrayList);
    TYPE_CLASSES_MAP.put(Integer.valueOf(107), localArrayList);
    localArrayList = new ArrayList();
    localArrayList.add(IArray.class);
    TYPE_CLASSES_MAP.put(Integer.valueOf(2003), localArrayList);
  }
  
  public void copyData(DataWrapper paramDataWrapper)
  {
    this.m_type = paramDataWrapper.m_type;
    this.m_value = paramDataWrapper.m_value;
    this.m_bigIntWasSetAsLongThroughSetData = paramDataWrapper.m_bigIntWasSetAsLongThroughSetData;
  }
  
  public IArray getArray()
    throws IncorrectTypeException
  {
    if (2003 == this.m_type) {
      return (IArray)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public BigInteger getBigInt()
    throws IncorrectTypeException
  {
    if (-5 == this.m_type)
    {
      if ((null == this.m_value) || ((this.m_value instanceof BigInteger))) {
        return (BigInteger)this.m_value;
      }
      return BigInteger.valueOf(((Long)this.m_value).longValue());
    }
    throw new IncorrectTypeException();
  }
  
  public Long getBigIntAsLong()
    throws IncorrectTypeException
  {
    if (-5 == this.m_type)
    {
      if ((this.m_value instanceof BigInteger))
      {
        BigInteger localBigInteger = (BigInteger)this.m_value;
        return Long.valueOf(localBigInteger.longValue());
      }
      return (Long)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public byte[] getBinary()
    throws IncorrectTypeException
  {
    if (-2 == this.m_type) {
      return getByteArray(this.m_value);
    }
    throw new IncorrectTypeException();
  }
  
  public Boolean getBit()
    throws IncorrectTypeException
  {
    if ((-7 == this.m_type) || (16 == this.m_type)) {
      return (Boolean)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Boolean getBoolean()
    throws IncorrectTypeException
  {
    if ((-7 == this.m_type) || (16 == this.m_type)) {
      return (Boolean)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public String getChar()
    throws IncorrectTypeException
  {
    if ((1 == this.m_type) || (-8 == this.m_type)) {
      return (String)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public java.sql.Date getDate()
    throws IncorrectTypeException
  {
    if (91 == this.m_type)
    {
      java.sql.Date localDate = (java.sql.Date)this.m_value;
      if (null == localDate) {
        return localDate;
      }
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(localDate.getTime());
      localCalendar.set(11, 0);
      localCalendar.set(12, 0);
      localCalendar.set(13, 0);
      localCalendar.set(14, 0);
      return new java.sql.Date(localCalendar.getTimeInMillis());
    }
    throw new IncorrectTypeException();
  }
  
  public BigDecimal getDecimal()
    throws IncorrectTypeException
  {
    if (3 == this.m_type) {
      return (BigDecimal)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Double getDouble()
    throws IncorrectTypeException
  {
    if (8 == this.m_type) {
      return (Double)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Double getFloat()
    throws IncorrectTypeException
  {
    if (6 == this.m_type)
    {
      if ((null == this.m_value) || ((this.m_value instanceof Double))) {
        return (Double)this.m_value;
      }
      return Double.valueOf(((Float)this.m_value).floatValue());
    }
    throw new IncorrectTypeException();
  }
  
  public UUID getGuid()
    throws IncorrectTypeException
  {
    if (-11 == this.m_type) {
      return (UUID)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Long getInteger()
    throws IncorrectTypeException
  {
    if (4 == this.m_type)
    {
      if ((null == this.m_value) || ((this.m_value instanceof Long))) {
        return (Long)this.m_value;
      }
      return Long.valueOf(((Integer)this.m_value).intValue());
    }
    throw new IncorrectTypeException();
  }
  
  public Object getInterval()
    throws IncorrectTypeException
  {
    if (TypeUtilities.isIntervalType(this.m_type)) {
      return this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public byte[] getLongVarBinary()
    throws IncorrectTypeException
  {
    if (-4 == this.m_type) {
      return getByteArray(this.m_value);
    }
    throw new IncorrectTypeException();
  }
  
  public String getLongVarChar()
    throws IncorrectTypeException
  {
    if ((-1 == this.m_type) || (-10 == this.m_type)) {
      return (String)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public BigDecimal getNumeric()
    throws IncorrectTypeException
  {
    if (2 == this.m_type) {
      return (BigDecimal)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Object getObject()
  {
    if ((-5 == this.m_type) && ((this.m_value instanceof Long)) && (!this.m_bigIntWasSetAsLongThroughSetData)) {
      this.m_value = BigInteger.valueOf(((Long)this.m_value).longValue());
    }
    return this.m_value;
  }
  
  public Float getReal()
    throws IncorrectTypeException
  {
    if (7 == this.m_type) {
      return (Float)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Integer getSmallInt()
    throws IncorrectTypeException
  {
    if (5 == this.m_type)
    {
      if ((null == this.m_value) || ((this.m_value instanceof Integer))) {
        return (Integer)this.m_value;
      }
      return Integer.valueOf(((Short)this.m_value).shortValue());
    }
    throw new IncorrectTypeException();
  }
  
  public Time getTime()
    throws IncorrectTypeException
  {
    if (92 == this.m_type)
    {
      Object localObject;
      if ((this.m_value instanceof TimeTz))
      {
        localObject = (TimeTz)this.m_value;
        Calendar localCalendar = ((TimeTz)localObject).getTimezoneCalendar();
        localCalendar.clear();
        localCalendar.setTimeInMillis(((TimeTz)localObject).getTime());
        localCalendar.set(1, 1970);
        localCalendar.set(2, 0);
        localCalendar.set(5, 1);
        return new TimeTz(localCalendar.getTimeInMillis(), localCalendar);
      }
      if ((this.m_value instanceof Time))
      {
        localObject = Calendar.getInstance();
        ((Calendar)localObject).setTimeInMillis(((Time)this.m_value).getTime());
        ((Calendar)localObject).set(1, 1970);
        ((Calendar)localObject).set(2, 0);
        ((Calendar)localObject).set(5, 1);
        return new Time(((Calendar)localObject).getTimeInMillis());
      }
      if (null == this.m_value) {
        return (Time)this.m_value;
      }
    }
    throw new IncorrectTypeException();
  }
  
  public Timestamp getTimestamp()
    throws IncorrectTypeException
  {
    if (93 == this.m_type) {
      return (Timestamp)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public TimestampTz getTimestampTz()
    throws IncorrectTypeException
  {
    if (93 == this.m_type)
    {
      if (null == this.m_value) {
        return null;
      }
      if (((this.m_value instanceof Timestamp)) && (!(this.m_value instanceof TimestampTz))) {
        return new TimestampTz((Timestamp)this.m_value, Calendar.getInstance());
      }
      return (TimestampTz)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public TimeTz getTimeTz()
    throws IncorrectTypeException
  {
    if (92 == this.m_type)
    {
      if (null == this.m_value) {
        return null;
      }
      if (((this.m_value instanceof Time)) && (!(this.m_value instanceof TimeTz))) {
        return new TimeTz((Time)this.m_value, Calendar.getInstance());
      }
      return (TimeTz)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public Short getTinyInt()
    throws IncorrectTypeException
  {
    if (-6 == this.m_type)
    {
      if ((null == this.m_value) || ((this.m_value instanceof Short))) {
        return (Short)this.m_value;
      }
      return Short.valueOf((short)((Byte)this.m_value).byteValue());
    }
    throw new IncorrectTypeException();
  }
  
  public int getType()
  {
    return this.m_type;
  }
  
  public byte[] getVarBinary()
    throws IncorrectTypeException
  {
    if (-3 == this.m_type) {
      return getByteArray(this.m_value);
    }
    throw new IncorrectTypeException();
  }
  
  public String getVarChar()
    throws IncorrectTypeException
  {
    if ((12 == this.m_type) || (-9 == this.m_type)) {
      return (String)this.m_value;
    }
    throw new IncorrectTypeException();
  }
  
  public boolean isNull()
  {
    return null == this.m_value;
  }
  
  public boolean isSet()
  {
    return 0 != this.m_type;
  }
  
  public void setArray(IArray paramIArray)
  {
    this.m_type = 2003;
    this.m_value = paramIArray;
  }
  
  public void setBigInt(BigInteger paramBigInteger)
  {
    this.m_type = -5;
    this.m_value = paramBigInteger;
    this.m_bigIntWasSetAsLongThroughSetData = false;
  }
  
  public void setBigInt(long paramLong)
  {
    this.m_type = -5;
    this.m_value = Long.valueOf(paramLong);
    this.m_bigIntWasSetAsLongThroughSetData = false;
  }
  
  public void setBinary(byte[] paramArrayOfByte)
  {
    this.m_type = -2;
    this.m_value = paramArrayOfByte;
  }
  
  public void setBit(boolean paramBoolean)
  {
    this.m_type = -7;
    this.m_value = Boolean.valueOf(paramBoolean);
  }
  
  public void setBit(Boolean paramBoolean)
  {
    this.m_type = -7;
    this.m_value = paramBoolean;
  }
  
  public void setBoolean(boolean paramBoolean)
  {
    this.m_type = 16;
    this.m_value = Boolean.valueOf(paramBoolean);
  }
  
  public void setBoolean(Boolean paramBoolean)
  {
    this.m_type = 16;
    this.m_value = paramBoolean;
  }
  
  public void setChar(String paramString)
  {
    this.m_type = 1;
    this.m_value = paramString;
  }
  
  public void setData(int paramInt, Object paramObject)
    throws IncorrectTypeException
  {
    if (null == paramObject)
    {
      setNull(paramInt);
      return;
    }
    Object localObject1 = paramObject;
    Object localObject2;
    if (TypeUtilities.isCharacterType(paramInt))
    {
      if ((paramObject instanceof StringReader))
      {
        localObject1 = getString((StringReader)paramObject);
      }
      else if (((paramObject instanceof ByteArrayInputStream)) || ((paramObject instanceof byte[])))
      {
        localObject2 = getByteArray(paramObject);
        if (TypeUtilities.isBinaryType(this.m_type)) {
          localObject1 = localObject2;
        } else {
          localObject1 = new String((byte[])localObject2);
        }
      }
      else
      {
        localObject1 = paramObject.toString();
      }
    }
    else if (TypeUtilities.isBinaryType(paramInt))
    {
      localObject1 = getByteArray(paramObject);
    }
    else
    {
      localObject2 = (List)TYPE_CLASSES_MAP.get(Integer.valueOf(paramInt));
      if (null == localObject2) {
        throw new IncorrectTypeException();
      }
      int i = 0;
      Iterator localIterator = ((List)localObject2).iterator();
      while (localIterator.hasNext())
      {
        Class localClass = (Class)localIterator.next();
        if (localClass.isInstance(localObject1))
        {
          i = 1;
          break;
        }
      }
      if (i == 0) {
        throw new IncorrectTypeException();
      }
      if (-5 == paramInt) {
        this.m_bigIntWasSetAsLongThroughSetData = (this.m_value instanceof Long);
      }
    }
    this.m_type = paramInt;
    if (93 == paramInt)
    {
      if ((localObject1 instanceof Time)) {
        this.m_value = new Timestamp(((Time)localObject1).getTime());
      } else if ((localObject1 instanceof java.util.Date))
      {
        if ((localObject1 instanceof Timestamp)) {
          this.m_value = localObject1;
        } else {
          this.m_value = new Timestamp(((java.util.Date)localObject1).getTime());
        }
      }
      else if ((localObject1 instanceof Calendar)) {
        this.m_value = new Timestamp(((Calendar)localObject1).getTime().getTime());
      }
    }
    else {
      this.m_value = localObject1;
    }
  }
  
  public void setDate(int paramInt1, int paramInt2, int paramInt3)
  {
    this.m_type = 91;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    if (paramInt1 < 0)
    {
      paramInt1 *= -1;
      localCalendar.set(0, 0);
    }
    localCalendar.set(paramInt1, paramInt2, paramInt3, 0, 0, 0);
    this.m_value = new java.sql.Date(localCalendar.getTimeInMillis());
  }
  
  public void setDate(java.sql.Date paramDate)
  {
    this.m_type = 91;
    this.m_value = paramDate;
  }
  
  public void setDecimal(BigDecimal paramBigDecimal)
  {
    this.m_type = 3;
    this.m_value = paramBigDecimal;
  }
  
  public void setDouble(double paramDouble)
  {
    this.m_type = 8;
    this.m_value = new Double(paramDouble);
  }
  
  public void setDouble(Double paramDouble)
  {
    this.m_type = 8;
    this.m_value = paramDouble;
  }
  
  public void setFloat(double paramDouble)
  {
    this.m_type = 6;
    this.m_value = new Double(paramDouble);
  }
  
  public void setFloat(Double paramDouble)
  {
    this.m_type = 6;
    this.m_value = paramDouble;
  }
  
  public void setGuid(UUID paramUUID)
  {
    this.m_type = -11;
    this.m_value = paramUUID;
  }
  
  public void setInteger(long paramLong)
  {
    this.m_type = 4;
    this.m_value = Long.valueOf(paramLong);
  }
  
  public void setInteger(Long paramLong)
  {
    this.m_type = 4;
    this.m_value = paramLong;
  }
  
  public void setInterval(Object paramObject)
  {
    if ((paramObject instanceof DSIMonthSpan))
    {
      this.m_type = ((DSIMonthSpan)paramObject).getIntervalType();
      this.m_value = paramObject;
    }
    else if ((paramObject instanceof DSITimeSpan))
    {
      this.m_type = ((DSITimeSpan)paramObject).getIntervalType();
      this.m_value = paramObject;
    }
  }
  
  public void setLongVarBinary(byte[] paramArrayOfByte)
  {
    this.m_type = -4;
    this.m_value = paramArrayOfByte;
  }
  
  public void setLongVarChar(String paramString)
  {
    this.m_type = -1;
    this.m_value = paramString;
  }
  
  public void setNull(int paramInt)
  {
    this.m_type = paramInt;
    this.m_value = null;
  }
  
  public void setNumeric(BigDecimal paramBigDecimal)
  {
    this.m_type = 2;
    this.m_value = paramBigDecimal;
  }
  
  public void setReal(float paramFloat)
  {
    this.m_type = 7;
    this.m_value = new Float(paramFloat);
  }
  
  public void setReal(Float paramFloat)
  {
    this.m_type = 7;
    this.m_value = paramFloat;
  }
  
  public void setSmallInt(int paramInt)
  {
    this.m_type = 5;
    this.m_value = Integer.valueOf(paramInt);
  }
  
  public void setSmallInt(Integer paramInteger)
  {
    this.m_type = 5;
    this.m_value = paramInteger;
  }
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3)
  {
    this.m_type = 92;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(1970, 0, 1, paramInt1, paramInt2, paramInt3);
    localCalendar.set(14, 0);
    this.m_value = new Time(localCalendar.getTimeInMillis());
  }
  
  public void setTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.m_type = 92;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(1970, 0, 1, paramInt1, paramInt2, paramInt3);
    localCalendar.set(14, paramInt4 / 1000000);
    this.m_value = new Time(localCalendar.getTimeInMillis());
  }
  
  public void setTime(Time paramTime)
  {
    this.m_type = 92;
    this.m_value = paramTime;
  }
  
  public void setTime(TimeTz paramTimeTz)
  {
    this.m_type = 92;
    this.m_value = paramTimeTz;
  }
  
  public void setTimestamp(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    this.m_type = 93;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    if (paramInt1 < 0)
    {
      paramInt1 *= -1;
      localCalendar.set(0, 0);
    }
    localCalendar.set(paramInt1, paramInt2, paramInt3);
    localCalendar.set(11, paramInt4);
    localCalendar.set(12, paramInt5);
    localCalendar.set(13, paramInt6);
    Timestamp localTimestamp = new Timestamp(localCalendar.getTimeInMillis());
    localTimestamp.setNanos(paramInt7);
    this.m_value = localTimestamp;
  }
  
  public void setTimestamp(Timestamp paramTimestamp)
  {
    this.m_type = 93;
    this.m_value = paramTimestamp;
  }
  
  public void setTimestamp(TimestampTz paramTimestampTz)
  {
    this.m_type = 93;
    this.m_value = paramTimestampTz;
  }
  
  public void setTinyInt(short paramShort)
  {
    this.m_type = -6;
    this.m_value = Short.valueOf(paramShort);
  }
  
  public void setTinyInt(Short paramShort)
  {
    this.m_type = -6;
    this.m_value = paramShort;
  }
  
  public void setVarBinary(byte[] paramArrayOfByte)
  {
    this.m_type = -3;
    this.m_value = paramArrayOfByte;
  }
  
  public void setVarChar(String paramString)
  {
    this.m_type = 12;
    this.m_value = paramString;
  }
  
  public void setWVarChar(String paramString)
  {
    this.m_type = -9;
    this.m_value = paramString;
  }
  
  public void setWChar(String paramString)
  {
    this.m_type = -8;
    this.m_value = paramString;
  }
  
  public void setWLongVarChar(String paramString)
  {
    this.m_type = -10;
    this.m_value = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.m_type);
    localStringBuilder.append("|");
    if (isNull()) {
      localStringBuilder.append("null");
    } else {
      localStringBuilder.append(this.m_value.toString());
    }
    return localStringBuilder.toString();
  }
  
  private byte[] getByteArray(Object paramObject)
    throws IncorrectTypeException
  {
    if ((null == paramObject) || ((paramObject instanceof byte[]))) {
      return (byte[])paramObject;
    }
    if ((paramObject instanceof ByteArrayInputStream))
    {
      ByteArrayInputStream localByteArrayInputStream = (ByteArrayInputStream)paramObject;
      byte[] arrayOfByte = new byte[localByteArrayInputStream.available()];
      try
      {
        localByteArrayInputStream.read(arrayOfByte);
      }
      catch (IOException localIOException)
      {
        return null;
      }
      return arrayOfByte;
    }
    throw new IncorrectTypeException();
  }
  
  private String getString(Reader paramReader)
  {
    BufferedReader localBufferedReader = new BufferedReader(paramReader);
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      int i = 0;
      while (-1 != (i = localBufferedReader.read())) {
        localStringBuffer.append((char)i);
      }
    }
    catch (IOException localIOException) {}
    return localStringBuffer.toString();
  }
  
  private final byte[] getCharAsUTF8()
  {
    try
    {
      return ((String)this.m_value).getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return null;
  }
  
  private final byte[] getVarCharAsUTF8()
    throws IncorrectTypeException
  {
    try
    {
      return ((String)this.m_value).getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return null;
  }
  
  private final byte[] getLongVarCharAsUTF8()
    throws IncorrectTypeException
  {
    try
    {
      return ((String)this.m_value).getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return null;
  }
  
  private final void setCharWithUTF8(byte[] paramArrayOfByte)
  {
    try
    {
      this.m_type = 1;
      if (null != paramArrayOfByte) {
        this.m_value = new String(paramArrayOfByte, "UTF-8");
      } else {
        this.m_value = null;
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
  }
  
  private final void setVarCharWithUTF8(byte[] paramArrayOfByte)
  {
    try
    {
      this.m_type = 12;
      if (null != paramArrayOfByte) {
        this.m_value = new String(paramArrayOfByte, "UTF-8");
      } else {
        this.m_value = null;
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
  }
  
  private final void setLongVarCharWithUTF8(byte[] paramArrayOfByte)
  {
    try
    {
      this.m_type = -1;
      if (null != paramArrayOfByte) {
        this.m_value = new String(paramArrayOfByte, "UTF-8");
      } else {
        this.m_value = null;
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
  }
  
  private final String getUnsignedBigintAsString()
  {
    return this.m_value.toString();
  }
  
  private final long getSignedBigint()
  {
    return ((Number)this.m_value).longValue();
  }
  
  private final boolean getBitPrimitive()
  {
    return ((Boolean)this.m_value).booleanValue();
  }
  
  private final double getDoublePrimitive()
  {
    return ((Number)this.m_value).doubleValue();
  }
  
  private final double getFloatPrimitive()
  {
    return ((Number)this.m_value).doubleValue();
  }
  
  private final long getUnsignedInteger()
  {
    return ((Number)this.m_value).longValue();
  }
  
  private final int getSignedInteger()
  {
    return ((Number)this.m_value).intValue();
  }
  
  private final float getRealPrimitive()
  {
    return ((Number)this.m_value).floatValue();
  }
  
  private final int getUnsignedSmallint()
  {
    return ((Number)this.m_value).intValue();
  }
  
  private final short getSignedSmallint()
  {
    return ((Number)this.m_value).shortValue();
  }
  
  private final short getUnsignedTinyint()
  {
    return ((Number)this.m_value).shortValue();
  }
  
  private final byte getSignedTinyint()
  {
    return ((Number)this.m_value).byteValue();
  }
  
  private final void setMonthSpan(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    this.m_value = new DSIMonthSpan(paramInt1, paramInt2, paramInt3, paramBoolean);
    this.m_type = paramInt1;
  }
  
  private final void setTimeSpan(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
  {
    this.m_value = new DSITimeSpan(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean);
    this.m_type = paramInt1;
  }
  
  private final void unpackDate(long paramLong)
    throws IncorrectTypeException
  {
    assert (0L != paramLong) : "The given native pointer should not be NULL.";
    java.sql.Date localDate;
    if (91 == this.m_type) {
      localDate = (java.sql.Date)this.m_value;
    } else {
      throw new IncorrectTypeException();
    }
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    localCalendar.setTimeInMillis(localDate.getTime());
    localCalendar.set(10, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    int i = localCalendar.get(0) == 0 ? 1 : 0;
    int j = i != 0 ? localCalendar.get(1) * -1 : localCalendar.get(1);
    int k = localCalendar.get(2) + 1;
    int m = localCalendar.get(5);
    unpackDateNative(paramLong, j, k, m);
  }
  
  private final native void unpackDateNative(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  private final void unpackTime(long paramLong)
    throws IncorrectTypeException
  {
    assert (0L != paramLong) : "The given native pointer should not be NULL.";
    Calendar localCalendar = Calendar.getInstance();
    if (92 == this.m_type)
    {
      Time localTime = (Time)this.m_value;
      if ((this.m_value instanceof TimeTz))
      {
        localCalendar = ((TimeTz)this.m_value).getTimezoneCalendar();
        localCalendar.clear();
      }
      localCalendar.setTimeInMillis(localTime.getTime());
    }
    else
    {
      throw new IncorrectTypeException();
    }
    unpackTimeNative(paramLong, localCalendar.get(11), localCalendar.get(12), localCalendar.get(13), localCalendar.get(14) * 1000000);
  }
  
  private final native void unpackTimeNative(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private final void unpackTimestamp(long paramLong)
    throws IncorrectTypeException
  {
    assert (0L != paramLong) : "The given native pointer should not be NULL.";
    Timestamp localTimestamp = getTimestamp();
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    localCalendar.setTimeInMillis(localTimestamp.getTime());
    int i = localCalendar.get(0) == 0 ? 1 : 0;
    int j = i != 0 ? localCalendar.get(1) * -1 : localCalendar.get(1);
    int k = localCalendar.get(2) + 1;
    int m = localCalendar.get(5);
    int n = localCalendar.get(11);
    int i1 = localCalendar.get(12);
    int i2 = localCalendar.get(13);
    int i3 = localTimestamp.getNanos();
    unpackTimestampNative(paramLong, j, k, m, n, i1, i2, i3);
  }
  
  private final native void unpackTimestampNative(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
  
  private final void unpackInterval(long paramLong)
  {
    assert (0L != paramLong) : "The given native pointer should not be NULL.";
    assert (null != this.m_value) : "The stored value should be non-null.";
    Object localObject;
    switch (this.m_type)
    {
    case 101: 
    case 102: 
    case 107: 
      localObject = (DSIMonthSpan)this.m_value;
      unpackMonthSpan(paramLong, this.m_type, ((DSIMonthSpan)localObject).getYear(), ((DSIMonthSpan)localObject).getMonth(), ((DSIMonthSpan)localObject).isNegative());
      break;
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      localObject = (DSITimeSpan)this.m_value;
      unpackTimeSpan(paramLong, this.m_type, ((DSITimeSpan)localObject).getDay(), ((DSITimeSpan)localObject).getHour(), ((DSITimeSpan)localObject).getMinute(), ((DSITimeSpan)localObject).getSecond(), ((DSITimeSpan)localObject).getFraction(), ((DSITimeSpan)localObject).isNegative());
      break;
    default: 
      if (!$assertionsDisabled) {
        throw new AssertionError(this.m_type + " is not an interval type!");
      }
      break;
    }
  }
  
  private static final native void unpackMonthSpan(long paramLong, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  private static final native void unpackTimeSpan(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean);
  
  static
  {
    TYPE_CLASSES_MAP = new HashMap();
    addTypeClassesMap();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/DataWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */