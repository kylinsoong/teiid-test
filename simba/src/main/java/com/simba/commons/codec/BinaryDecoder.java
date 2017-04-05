package com.simba.commons.codec;

public abstract interface BinaryDecoder
  extends Decoder
{
  public abstract byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/BinaryDecoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */