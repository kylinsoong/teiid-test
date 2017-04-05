package com.simba.sqlengine.aeprocessor.metadatautil;

public class AECoercionProperties
{
  private short m_maxTimePrecision = 9;
  private short m_maxTimestampPrecision = 9;
  private short m_maxExactNumPrecision = 38;
  private short m_defaultExactNumScale = 6;
  private long m_maxCharLength = 8000L;
  private long m_maxVarcharlength = 8000L;
  private long m_maxWcharlength = 4000L;
  private long m_maxWvarcharLength = 4000L;
  private long m_maxBinaryLength = 8000L;
  private long m_maxVarbinaryLength = 8000L;
  
  private AECoercionProperties() {}
  
  private AECoercionProperties(AECoercionProperties paramAECoercionProperties)
  {
    this.m_maxTimePrecision = paramAECoercionProperties.m_maxTimePrecision;
    this.m_maxTimestampPrecision = paramAECoercionProperties.m_maxTimestampPrecision;
    this.m_maxExactNumPrecision = paramAECoercionProperties.m_maxExactNumPrecision;
    this.m_defaultExactNumScale = paramAECoercionProperties.m_defaultExactNumScale;
    this.m_maxCharLength = paramAECoercionProperties.m_maxCharLength;
    this.m_maxVarcharlength = paramAECoercionProperties.m_maxVarcharlength;
    this.m_maxWcharlength = paramAECoercionProperties.m_maxWcharlength;
    this.m_maxWvarcharLength = paramAECoercionProperties.m_maxWvarcharLength;
    this.m_maxBinaryLength = paramAECoercionProperties.m_maxBinaryLength;
    this.m_maxVarbinaryLength = paramAECoercionProperties.m_maxVarbinaryLength;
  }
  
  public short getMaxTimePrecision()
  {
    return this.m_maxTimePrecision;
  }
  
  public short getMaxTimestampPrecision()
  {
    return this.m_maxTimestampPrecision;
  }
  
  public short getMaxExactNumPrecision()
  {
    return this.m_maxExactNumPrecision;
  }
  
  public short getDefaultExactNumScale()
  {
    return this.m_defaultExactNumScale;
  }
  
  public long getMaxCharLength()
  {
    return this.m_maxCharLength;
  }
  
  public long getMaxVarcharlength()
  {
    return this.m_maxVarcharlength;
  }
  
  public long getMaxWcharlength()
  {
    return this.m_maxWcharlength;
  }
  
  public long getMaxWvarcharLength()
  {
    return this.m_maxWvarcharLength;
  }
  
  public long getMaxBinaryLength()
  {
    return this.m_maxBinaryLength;
  }
  
  public long getMaxVarbinaryLength()
  {
    return this.m_maxVarbinaryLength;
  }
  
  public static class Builder
  {
    private AECoercionProperties m_property = new AECoercionProperties(null);
    
    public AECoercionProperties build()
    {
      return new AECoercionProperties(this.m_property, null);
    }
    
    public void maxTimePrecision(short paramShort)
    {
      this.m_property.m_maxTimePrecision = paramShort;
    }
    
    public void maxTimestampPrecision(short paramShort)
    {
      this.m_property.m_maxTimestampPrecision = paramShort;
    }
    
    public void maxExactNumPrecision(short paramShort)
    {
      this.m_property.m_maxExactNumPrecision = paramShort;
    }
    
    public void defaultExactNumScale(short paramShort)
    {
      this.m_property.m_defaultExactNumScale = paramShort;
    }
    
    public void maxCharLength(long paramLong)
    {
      this.m_property.m_maxCharLength = paramLong;
    }
    
    public void maxVarcharlength(long paramLong)
    {
      this.m_property.m_maxVarcharlength = paramLong;
    }
    
    public void maxWcharlength(long paramLong)
    {
      this.m_property.m_maxWcharlength = paramLong;
    }
    
    public void maxWvarcharLength(long paramLong)
    {
      this.m_property.m_maxWvarcharLength = paramLong;
    }
    
    public void maxBinaryLength(long paramLong)
    {
      this.m_property.m_maxBinaryLength = paramLong;
    }
    
    public void maxVarbinaryLength(long paramLong)
    {
      this.m_property.m_maxVarbinaryLength = paramLong;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AECoercionProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */