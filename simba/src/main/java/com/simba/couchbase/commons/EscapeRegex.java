package com.simba.couchbase.commons;

public class EscapeRegex
{
  public static final String REPLACE_FUNCTION_ESCAPE_CHARS = "'[\\\\.\\\\[\\\\]\\\\{\\\\}\\\\(\\\\)\\\\*\\\\+\\\\?\\\\|\\\\^\\\\$\\\\\\\\]'";
  public static final String REPLACE_FUNCTION_REPLACEMENT = "'\\\\\\\\$0'";
  public static final String PREPARED_STATEMENT_ESCAPE_CHARS = "[\"]";
  public static final String PREPARED_STATEMENT_REPLACEMENT = "\\\\$0";
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/commons/EscapeRegex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */