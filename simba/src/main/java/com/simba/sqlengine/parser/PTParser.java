package com.simba.sqlengine.parser;

import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.generated.Parser;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.type.PTStringConstraint;

public class PTParser
{
  private PTParser()
  {
    throw new UnsupportedOperationException("Not instantiable");
  }
  
  public static IPTNode parse(String paramString)
    throws SQLEngineException
  {
    return parse(paramString, new DefaultLimitChecker());
  }
  
  public static IPTNode parse(String paramString, IPTLimitChecker paramIPTLimitChecker)
    throws SQLEngineException
  {
    if (null == paramString) {
      throw new NullPointerException("sqlStatement must not be null");
    }
    if (null == paramIPTLimitChecker) {
      throw new NullPointerException("limitChecker must not be null");
    }
    paramIPTLimitChecker.checkString(PTStringConstraint.STATEMENT_LEN, paramString);
    return new Parser(paramString, paramIPTLimitChecker).parse();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/PTParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */