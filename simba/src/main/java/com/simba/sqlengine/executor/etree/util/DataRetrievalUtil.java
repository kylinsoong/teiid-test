package com.simba.sqlengine.executor.etree.util;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.temptable.IRowView;
import com.simba.sqlengine.executor.etree.temptable.LongDataStore;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile;
import com.simba.sqlengine.executor.etree.temptable.TemporaryFile.FileMarker;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.IWarningListener;
import com.simba.support.Warning;
import com.simba.support.WarningCode;
import com.simba.support.exceptions.ErrorException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public final class DataRetrievalUtil
{
  public static boolean retrieveBinaryData(ISqlDataWrapper paramISqlDataWrapper, long paramLong1, long paramLong2)
    throws ErrorException
  {
    return retrieveBinaryData(paramISqlDataWrapper, paramLong1, paramLong2, Long.MAX_VALUE, null);
  }
  
  public static boolean retrieveCharData(ISqlDataWrapper paramISqlDataWrapper, long paramLong1, long paramLong2)
    throws ErrorException
  {
    return retrieveCharData(paramISqlDataWrapper, paramLong1, paramLong2, Long.MAX_VALUE, null);
  }
  
  public static boolean retrieveBinaryData(ISqlDataWrapper paramISqlDataWrapper, long paramLong1, long paramLong2, long paramLong3, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    byte[] arrayOfByte1 = paramISqlDataWrapper.getBinary();
    if ((paramLong1 != 0L) && ((paramLong1 >= arrayOfByte1.length) || (paramLong1 >= paramLong3))) {
      throw new IllegalArgumentException("Invalid offset: " + paramLong1);
    }
    int i;
    if ((paramLong2 == -1L) || (paramLong2 > Long.MAX_VALUE - paramLong1) || (arrayOfByte1.length <= paramLong2 + paramLong1)) {
      i = arrayOfByte1.length;
    } else {
      i = (int)(paramLong2 + paramLong1);
    }
    if ((paramLong1 == 0L) && (arrayOfByte1.length <= paramLong2) && (arrayOfByte1.length <= paramLong3)) {
      return false;
    }
    int j = (int)Math.min(i, paramLong3);
    byte[] arrayOfByte2 = new byte[(int)(j - paramLong1)];
    for (int k = 0; k < arrayOfByte2.length; k++) {
      arrayOfByte2[k] = arrayOfByte1[((int)(k + paramLong1))];
    }
    paramISqlDataWrapper.setBinary(arrayOfByte2);
    k = paramLong3 < i ? 1 : 0;
    boolean bool = (k == 0) && (j < arrayOfByte1.length);
    if ((paramIWarningListener != null) && (k != 0)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
    }
    return bool;
  }
  
  public static boolean retrieveCharData(ISqlDataWrapper paramISqlDataWrapper, long paramLong1, long paramLong2, long paramLong3, IWarningListener paramIWarningListener)
    throws ErrorException
  {
    if (paramLong1 % 2L == 1L) {
      throw new IllegalStateException("Invalid offset value: " + paramLong1);
    }
    paramLong1 /= 2L;
    String str = paramISqlDataWrapper.getChar();
    int i = str.length();
    if ((paramLong1 != 0L) && ((paramLong1 >= i) || (paramLong1 >= paramLong3)))
    {
      paramISqlDataWrapper.setChar("");
      return false;
    }
    int j;
    if ((paramLong2 == -1L) || (paramLong2 / 2L > Long.MAX_VALUE - paramLong1) || (i <= paramLong2 / 2L + paramLong1)) {
      j = i;
    } else {
      j = (int)(paramLong1 + paramLong2 / 2L);
    }
    if ((paramLong1 == 0L) && (i == j) && (i <= paramLong3)) {
      return false;
    }
    int k = (int)Math.min(paramLong3, j);
    paramISqlDataWrapper.setChar(str.substring((int)paramLong1, k));
    int m = paramLong3 < j ? 1 : 0;
    boolean bool = (m == 0) && (k < i);
    if ((paramIWarningListener != null) && (m != 0)) {
      paramIWarningListener.postWarning(new Warning(WarningCode.STRING_RIGHT_TRUNCATION_WARNING, 7, SQLEngineMessageKey.STRING_RIGHT_TRUNCATION.name(), -1, -1));
    }
    return bool;
  }
  
  public static boolean retrieveLongDataFromFile(TemporaryFile paramTemporaryFile, TemporaryFile.FileMarker paramFileMarker, ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    assert (paramETDataRequest.getColumn().getTypeMetadata().isCharacterOrBinaryType());
    long l1 = paramETDataRequest.getOffset();
    long l2 = paramFileMarker.m_length - l1;
    long l3 = paramETDataRequest.getMaxSize();
    if ((-1L == l3) || (l3 > 2147483647L)) {
      l3 = 2147483647L;
    }
    long l4 = Math.min(l3, l2);
    boolean bool = false;
    if (l4 < l2) {
      bool = true;
    }
    paramFileMarker = new TemporaryFile.FileMarker(paramFileMarker.m_pos + l1, l4);
    byte[] arrayOfByte = paramTemporaryFile.get(paramFileMarker);
    if (paramETDataRequest.getColumn().getTypeMetadata().isCharacterType()) {
      paramETDataRequest.getData().setChar(bytesToString(arrayOfByte));
    } else {
      paramETDataRequest.getData().setBinary(arrayOfByte);
    }
    return bool;
  }
  
  public static String bytesToString(byte[] paramArrayOfByte)
  {
    ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte).order(ByteOrder.nativeOrder());
    char[] arrayOfChar = new char[paramArrayOfByte.length / 2];
    localByteBuffer.asCharBuffer().get(arrayOfChar);
    return new String(arrayOfChar);
  }
  
  public static String rtrim(String paramString)
  {
    for (int i = paramString.length() - 1; (i >= 0) && (Character.isWhitespace(paramString.charAt(i))); i--) {}
    if (i == paramString.length() - 1) {
      return paramString;
    }
    return paramString.substring(0, i + 1);
  }
  
  public static byte[] stringToBytes(String paramString)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(paramString.length() * 2).order(ByteOrder.nativeOrder());
    localByteBuffer.asCharBuffer().put(paramString);
    return localByteBuffer.array();
  }
  
  public static boolean retrieveFromRowView(int paramInt, boolean paramBoolean, ETDataRequest paramETDataRequest, IRowView paramIRowView, TemporaryFile paramTemporaryFile)
    throws ErrorException
  {
    if (paramIRowView.isNull(paramInt))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    boolean bool = false;
    if (paramBoolean)
    {
      TemporaryFile.FileMarker localFileMarker = paramIRowView.getFileMarker(paramInt);
      bool = retrieveLongDataFromFile(paramTemporaryFile, localFileMarker, paramETDataRequest);
    }
    else
    {
      bool = retrieveNotLongData(paramInt, paramETDataRequest, paramIRowView);
    }
    return bool;
  }
  
  public static boolean retrieveFromRowView(int paramInt, boolean paramBoolean, ETDataRequest paramETDataRequest, IRowView paramIRowView, LongDataStore paramLongDataStore)
    throws ErrorException
  {
    if (paramIRowView.isNull(paramInt))
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    boolean bool = false;
    if (paramBoolean)
    {
      TemporaryFile.FileMarker localFileMarker = paramIRowView.getFileMarker(paramInt);
      bool = paramLongDataStore.retrieveData(localFileMarker, paramETDataRequest);
    }
    else
    {
      bool = retrieveNotLongData(paramInt, paramETDataRequest, paramIRowView);
    }
    return bool;
  }
  
  private static boolean retrieveNotLongData(int paramInt, ETDataRequest paramETDataRequest, IRowView paramIRowView)
    throws ErrorException
  {
    boolean bool = false;
    readToDataWrapper(paramInt, paramETDataRequest.getColumn(), paramETDataRequest.getData(), paramIRowView);
    if (paramETDataRequest.getColumn().getTypeMetadata().isCharacterType()) {
      bool = retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
    } else if (paramETDataRequest.getColumn().getTypeMetadata().isBinaryType()) {
      bool = retrieveBinaryData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
    }
    return bool;
  }
  
  private static void readToDataWrapper(int paramInt, IColumn paramIColumn, ISqlDataWrapper paramISqlDataWrapper, IRowView paramIRowView)
    throws ErrorException
  {
    if (paramIRowView.isNull(paramInt))
    {
      paramISqlDataWrapper.setNull();
      return;
    }
    int i;
    switch (paramISqlDataWrapper.getType())
    {
    case -5: 
      long l = paramIRowView.getBigInt(paramInt);
      paramISqlDataWrapper.setBigInt(CompressionUtil.getlongAsBigInteger(l, paramIColumn.getTypeMetadata().isSigned()));
      break;
    case 2: 
    case 3: 
      paramISqlDataWrapper.setExactNumber(paramIRowView.getExactNumber(paramInt));
      break;
    case 6: 
    case 8: 
      paramISqlDataWrapper.setDouble(paramIRowView.getDouble(paramInt));
      break;
    case 7: 
      paramISqlDataWrapper.setReal(paramIRowView.getReal(paramInt));
      break;
    case -7: 
    case 16: 
      paramISqlDataWrapper.setBoolean(paramIRowView.getBoolean(paramInt));
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      paramISqlDataWrapper.setChar(paramIRowView.getString(paramInt));
      break;
    case -4: 
    case -3: 
    case -2: 
      paramISqlDataWrapper.setBinary(paramIRowView.getBytes(paramInt));
      break;
    case 91: 
      paramISqlDataWrapper.setDate(paramIRowView.getDate(paramInt));
      break;
    case 92: 
      paramISqlDataWrapper.setTime(paramIRowView.getTime(paramInt));
      break;
    case 93: 
      paramISqlDataWrapper.setTimestamp(paramIRowView.getTimestamp(paramInt));
      break;
    case -11: 
      paramISqlDataWrapper.setGuid(paramIRowView.getGuid(paramInt));
      break;
    case 4: 
      i = paramIRowView.getInteger(paramInt);
      paramISqlDataWrapper.setInteger(CompressionUtil.getIntAsLong(i, paramIColumn.getTypeMetadata().isSigned()));
      break;
    case 5: 
      i = paramIRowView.getSmallInt(paramInt);
      paramISqlDataWrapper.setSmallInt(CompressionUtil.getSmallIntAsInteger(i, paramIColumn.getTypeMetadata().isSigned()));
      break;
    case -6: 
      byte b = paramIRowView.getTinyInt(paramInt);
      paramISqlDataWrapper.setTinyInt(CompressionUtil.getTinyIntAsShort(b, paramIColumn.getTypeMetadata().isSigned()));
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
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + paramISqlDataWrapper.getType());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/util/DataRetrievalUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */