package com.simba.commons.codec;

public abstract interface StringEncoder
  extends Encoder
{
  public abstract String encode(String paramString)
    throws EncoderException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/StringEncoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */