package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.support.exceptions.ErrorException;

public enum AEComparisonType
{
  EQUAL("="),  NOT_EQUAL("!="),  GREATER_THAN(">"),  LESS_THAN("<"),  GREATER_THAN_OR_EQUAL(">="),  LESS_THAN_OR_EQUAL("<=");
  
  private String m_val;
  
  private AEComparisonType(String paramString)
  {
    this.m_val = paramString;
  }
  
  public abstract AEComparisonType flip();
  
  public abstract AEComparisonType complement();
  
  public static AEComparisonType getComparisonType(PTNonterminalType paramPTNonterminalType)
    throws ErrorException
  {
    switch (paramPTNonterminalType)
    {
    case EQUALS_OP: 
      return EQUAL;
    case NOT_EQUALS_OP: 
      return NOT_EQUAL;
    case LESS_THAN_OP: 
      return LESS_THAN;
    case GREATER_THAN_OP: 
      return GREATER_THAN;
    case LESS_THAN_OR_EQUALS_OP: 
      return LESS_THAN_OR_EQUAL;
    case GREATER_THAN_OR_EQUALS_OP: 
      return GREATER_THAN_OR_EQUAL;
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  public String toString()
  {
    return this.m_val;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/AEComparisonType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */