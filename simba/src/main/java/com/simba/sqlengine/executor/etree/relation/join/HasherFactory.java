package com.simba.sqlengine.executor.etree.relation.join;

import java.util.Random;

public class HasherFactory
{
  private final Random m_rand = new Random(1L);
  
  public IHasher nextHasher(long paramLong)
  {
    return new DefaultHasher(paramLong, this.m_rand.nextLong());
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/relation/join/HasherFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */