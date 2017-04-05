package com.simba.dsi.exceptions;

import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;

public final class ParsingException
  extends ErrorException
{
  private static final long serialVersionUID = 4979595851261026155L;
  private ParsingErrorID m_subcode;
  
  public ParsingException(ParsingErrorID paramParsingErrorID, int paramInt, String paramString)
  {
    super(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramInt, paramString);
    this.m_subcode = paramParsingErrorID;
  }
  
  public ParsingException(ParsingErrorID paramParsingErrorID, String paramString, int paramInt)
  {
    super(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramString, paramInt);
    this.m_subcode = paramParsingErrorID;
  }
  
  public ParsingException(ParsingErrorID paramParsingErrorID, int paramInt, String paramString, String[] paramArrayOfString)
  {
    super(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, paramInt, paramString, paramArrayOfString);
    this.m_subcode = paramParsingErrorID;
  }
  
  public int getSubcode()
  {
    return this.m_subcode.getIdentifier();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/exceptions/ParsingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */