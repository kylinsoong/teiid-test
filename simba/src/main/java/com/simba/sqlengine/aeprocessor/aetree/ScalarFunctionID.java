package com.simba.sqlengine.aeprocessor.aetree;

import com.simba.sqlengine.dsiext.dataengine.utils.ScalarFunctionArgType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ScalarFunctionID
{
  ASCII(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  CHAR(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER }),  CONCAT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_STRING }),  INSERT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER, ScalarFunctionArgType.FN_ARG_INTEGER, ScalarFunctionArgType.FN_ARG_STRING }),  LCASE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  LEFT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER }),  LENGTH(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  LOCATE2("LOCATE", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_STRING }),  LOCATE3("LOCATE", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER }),  LTRIM(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  REPEAT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER }),  REPLACE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_STRING }),  RIGHT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER }),  RTRIM(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  SOUNDEX(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  SPACE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER }),  SUBSTRING3("SUBSTRING", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER, ScalarFunctionArgType.FN_ARG_INTEGER }),  SUBSTRING2("SUBSTRING", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING, ScalarFunctionArgType.FN_ARG_INTEGER }),  UCASE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  CURDATE,  CURTIME,  CURRENT_DATE,  CURRENT_TIME,  CURRENT_TIME1("CURRENT_TIME", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER }),  CURRENT_TIMESTAMP,  CURRENT_TIMESTAMP1("CURRENT_TIMESTAMP", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER }),  DAYNAME(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  DAYOFMONTH(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  DAYOFWEEK(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  DAYOFYEAR(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  HOUR(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_TIME }),  MINUTE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_TIME }),  MONTH(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  MONTHNAME(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  NOW,  QUARTER(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  SECOND(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_TIME }),  TIMESTAMPADD(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTERVAL, ScalarFunctionArgType.FN_ARG_INTEGER, ScalarFunctionArgType.FN_ARG_TIMESTAMP }),  TIMESTAMPDIFF(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTERVAL, ScalarFunctionArgType.FN_ARG_TIMESTAMP, ScalarFunctionArgType.FN_ARG_TIMESTAMP }),  WEEK(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  WEEK_ISO("WEEK_ISO", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  YEAR(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_DATE }),  ABS(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  ACOS(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  ASIN(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  ATAN(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  ATAN2(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT, ScalarFunctionArgType.FN_ARG_FLOAT }),  CEILING(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  COS(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  COT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  DEGREES(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  EXP(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  FLOOR(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  LOG(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  LOG10(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  MOD(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER, ScalarFunctionArgType.FN_ARG_INTEGER }),  PI,  POWER(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC, ScalarFunctionArgType.FN_ARG_INTEGER }),  RADIANS(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  RAND0("RAND", new ScalarFunctionArgType[0]),  RAND1("RAND", new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_INTEGER }),  ROUND(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC, ScalarFunctionArgType.FN_ARG_INTEGER }),  SIGN(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC }),  SIN(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  SQRT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  TAN(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_FLOAT }),  TRUNCATE(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_NUMERIC, ScalarFunctionArgType.FN_ARG_INTEGER }),  DATABASE,  IFNULL(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_ANY, ScalarFunctionArgType.FN_ARG_ANY }),  NULL,  USER,  CONVERT(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_ANY, ScalarFunctionArgType.FN_ARG_ANY }),  CAST(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_ANY, ScalarFunctionArgType.FN_ARG_ANY }),  LOWER(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING }),  UPPER(new ScalarFunctionArgType[] { ScalarFunctionArgType.FN_ARG_STRING });
  
  private String m_name;
  private List<ScalarFunctionArgType> m_args;
  
  private ScalarFunctionID()
  {
    this((String)null, new ScalarFunctionArgType[0]);
  }
  
  private ScalarFunctionID(ScalarFunctionArgType... paramVarArgs)
  {
    this(null, paramVarArgs);
  }
  
  private ScalarFunctionID(String paramString, ScalarFunctionArgType... paramVarArgs)
  {
    this.m_name = paramString;
    this.m_args = Collections.unmodifiableList(Arrays.asList(paramVarArgs));
  }
  
  public String getName()
  {
    String str = this.m_name;
    if (null == str) {
      this.m_name = (str = name());
    }
    return str;
  }
  
  public List<ScalarFunctionArgType> getArguments()
  {
    return this.m_args;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/ScalarFunctionID.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */