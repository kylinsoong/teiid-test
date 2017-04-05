package com.simba.dsi.dataengine.utilities;

import com.simba.support.exceptions.ErrorException;

public class ParameterMetadata
{
  protected int m_parameterNumber;
  protected ParameterType m_parameterType;
  protected TypeMetadata m_typeMetadata;
  protected long m_columnLength;
  protected String m_name;
  protected boolean m_isCaseSensitive;
  protected Nullable m_nullable;
  private boolean m_convertInputToString = false;
  
  public ParameterMetadata(int paramInt1, ParameterType paramParameterType, int paramInt2)
    throws ErrorException
  {
    this.m_parameterNumber = paramInt1;
    this.m_parameterType = paramParameterType;
    this.m_typeMetadata = TypeMetadata.createTypeMetadata(paramInt2);
    this.m_columnLength = 0L;
    this.m_name = null;
    this.m_isCaseSensitive = false;
    this.m_nullable = Nullable.NULLABLE;
  }
  
  public ParameterMetadata(int paramInt1, ParameterType paramParameterType, int paramInt2, boolean paramBoolean)
    throws ErrorException
  {
    this.m_parameterNumber = paramInt1;
    this.m_parameterType = paramParameterType;
    this.m_typeMetadata = TypeMetadata.createTypeMetadata(paramInt2, paramBoolean);
    this.m_columnLength = 0L;
    this.m_name = null;
    this.m_isCaseSensitive = false;
    this.m_nullable = Nullable.NULLABLE;
  }
  
  public ParameterMetadata(int paramInt, ParameterType paramParameterType, TypeMetadata paramTypeMetadata, long paramLong, String paramString, boolean paramBoolean, Nullable paramNullable)
  {
    this.m_parameterNumber = paramInt;
    this.m_parameterType = paramParameterType;
    this.m_typeMetadata = paramTypeMetadata;
    this.m_columnLength = paramLong;
    this.m_name = paramString;
    this.m_isCaseSensitive = paramBoolean;
    this.m_nullable = paramNullable;
  }
  
  public long getColumnLength()
  {
    return this.m_columnLength;
  }
  
  public String getName()
  {
    return this.m_name;
  }
  
  public Nullable getNullable()
  {
    return this.m_nullable;
  }
  
  public int getParameterNumber()
  {
    return this.m_parameterNumber;
  }
  
  public ParameterType getParameterType()
  {
    return this.m_parameterType;
  }
  
  public TypeMetadata getTypeMetadata()
  {
    return this.m_typeMetadata;
  }
  
  public boolean isCaseSensitive()
  {
    return this.m_isCaseSensitive;
  }
  
  public boolean isUnnamed()
  {
    return null == this.m_name;
  }
  
  public void setCaseSensitive(boolean paramBoolean)
  {
    this.m_isCaseSensitive = paramBoolean;
  }
  
  public void setColumnLength(long paramLong)
  {
    this.m_columnLength = paramLong;
  }
  
  public void setConvertInputToString(boolean paramBoolean)
  {
    this.m_convertInputToString = paramBoolean;
  }
  
  public void setName(String paramString)
  {
    this.m_name = paramString;
  }
  
  public void setNullable(Nullable paramNullable)
  {
    this.m_nullable = paramNullable;
  }
  
  public boolean shouldConvertInputToString()
  {
    return this.m_convertInputToString;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/dataengine/utilities/ParameterMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */