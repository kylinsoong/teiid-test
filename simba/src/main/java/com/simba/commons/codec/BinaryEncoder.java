package com.simba.commons.codec;

public abstract interface BinaryEncoder
  extends Encoder
{
  public abstract byte[] encode(byte[] paramArrayOfByte)
    throws EncoderException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/BinaryEncoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */