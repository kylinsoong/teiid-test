package com.simba.commons.codec;

public abstract interface StringDecoder
  extends Decoder
{
  public abstract String decode(String paramString)
    throws DecoderException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/StringDecoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */