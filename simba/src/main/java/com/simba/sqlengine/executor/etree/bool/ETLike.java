package com.simba.sqlengine.executor.etree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETBoolean;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.IETNodeVisitor;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ETLike
  extends ETBooleanExpr
{
  private static final boolean[] REGEX_SPECIAL_CHAR;
  private ETValueExpr m_leftOperand;
  private ETValueExpr m_rightOperand;
  private ETValueExpr m_escapeChar;
  private ETDataRequest m_leftData;
  private ETDataRequest m_rightData;
  private ETDataRequest m_escpData;
  private boolean m_hasEscapeChar = false;
  
  public ETLike(IColumn paramIColumn, ETValueExpr paramETValueExpr1, ETValueExpr paramETValueExpr2, ETValueExpr paramETValueExpr3)
    throws ErrorException
  {
    if ((paramETValueExpr1 == null) || (paramIColumn == null) || (paramETValueExpr2 == null)) {
      throw new NullPointerException();
    }
    if (!paramIColumn.getTypeMetadata().isCharacterType()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("LIKE predicate on type : " + paramIColumn.getTypeMetadata().getTypeName());
    }
    this.m_leftData = new ETDataRequest(paramIColumn);
    this.m_rightData = new ETDataRequest(paramIColumn);
    if (paramETValueExpr3 != null)
    {
      this.m_hasEscapeChar = true;
      ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(1));
      try
      {
        localColumnMetadata.setColumnLength(2L);
      }
      catch (NumericOverflowException localNumericOverflowException)
      {
        if (!$assertionsDisabled) {
          throw new AssertionError();
        }
      }
      this.m_escpData = new ETDataRequest(localColumnMetadata);
    }
    else
    {
      this.m_hasEscapeChar = false;
      this.m_escpData = null;
    }
    this.m_leftOperand = paramETValueExpr1;
    this.m_rightOperand = paramETValueExpr2;
    this.m_escapeChar = paramETValueExpr3;
  }
  
  public void close()
  {
    this.m_leftOperand.close();
    this.m_rightOperand.close();
    if (this.m_hasEscapeChar) {
      this.m_escapeChar.close();
    }
  }
  
  public boolean isOpen()
  {
    boolean bool = (this.m_leftOperand.isOpen()) && (this.m_rightOperand.isOpen());
    if (this.m_hasEscapeChar) {
      return (bool) && (this.m_escapeChar.isOpen());
    }
    return bool;
  }
  
  public void reset()
    throws ErrorException
  {
    this.m_leftOperand.reset();
    this.m_rightOperand.reset();
    if (this.m_hasEscapeChar) {
      this.m_escapeChar.reset();
    }
  }
  
  public <T> T acceptVisitor(IETNodeVisitor<T> paramIETNodeVisitor)
    throws ErrorException
  {
    return (T)paramIETNodeVisitor.visit(this);
  }
  
  public int getNumChildren()
  {
    return this.m_hasEscapeChar ? 3 : 2;
  }
  
  public ETBoolean evaluate()
    throws ErrorException
  {
    this.m_leftOperand.retrieveData(this.m_leftData);
    this.m_rightOperand.retrieveData(this.m_rightData);
    if ((this.m_leftData.getData().isNull()) || (this.m_rightData.getData().isNull())) {
      return ETBoolean.SQL_BOOLEAN_UNKNOWN;
    }
    char c = '\000';
    if (this.m_hasEscapeChar)
    {
      this.m_escapeChar.retrieveData(this.m_escpData);
      if (this.m_escpData.getData().isNull()) {
        return ETBoolean.SQL_BOOLEAN_UNKNOWN;
      }
      str1 = this.m_escpData.getData().getChar();
      if (str1.length() != 1) {
        throw new SQLEngineException(DiagState.DIAG_INVALID_ESC_CHAR, SQLEngineMessageKey.INVALID_ESC_CHAR.name(), new String[] { str1 });
      }
      c = str1.charAt(0);
    }
    String str1 = this.m_leftData.getData().getChar();
    String str2 = this.m_rightData.getData().getChar();
    String str3 = createPattern(str2, c);
    return ETBoolean.fromBoolean(Pattern.compile(str3, 32).matcher(str1).matches());
  }
  
  private String createPattern(String paramString, char paramChar)
    throws ErrorException
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString.length());
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if ((this.m_hasEscapeChar) && (paramChar == c))
      {
        i++;
        if (i >= paramString.length()) {
          throw SQLEngineExceptionFactory.invalidEscapeSequenceException();
        }
        c = paramString.charAt(i);
        if (('_' == c) || ('%' == c))
        {
          localStringBuilder.append(c);
          continue;
        }
        if (paramChar != c) {
          throw SQLEngineExceptionFactory.invalidEscapeSequenceException();
        }
      }
      if ('%' == c)
      {
        localStringBuilder.append(".*");
        for (int j = i + 1;; j++)
        {
          if (j == paramString.length()) {
            return localStringBuilder.toString();
          }
          if ('%' != paramString.charAt(j))
          {
            i = j - 1;
            break;
          }
        }
      }
      else if ('_' == c)
      {
        localStringBuilder.append('.');
      }
      else if (isRegexSpecialChar(c))
      {
        localStringBuilder.append('\\').append(c);
      }
      else
      {
        localStringBuilder.append(c);
      }
    }
    return " *";
  }
  
  private static boolean isRegexSpecialChar(char paramChar)
  {
    return (paramChar < '') && (REGEX_SPECIAL_CHAR[paramChar] != 0);
  }
  
  protected IETNode getChild(int paramInt)
    throws IndexOutOfBoundsException
  {
    switch (paramInt)
    {
    case 0: 
      return this.m_leftOperand;
    case 1: 
      return this.m_rightOperand;
    case 2: 
      if (this.m_hasEscapeChar) {
        return this.m_escapeChar;
      }
      throw new IndexOutOfBoundsException("index: " + paramInt);
    }
    throw new IndexOutOfBoundsException("index: " + paramInt);
  }
  
  public void open()
  {
    this.m_leftOperand.open();
    this.m_rightOperand.open();
    if (this.m_hasEscapeChar) {
      this.m_escapeChar.open();
    }
  }
  
  static
  {
    REGEX_SPECIAL_CHAR = new boolean[''];
    REGEX_SPECIAL_CHAR[91] = true;
    REGEX_SPECIAL_CHAR[123] = true;
    REGEX_SPECIAL_CHAR[40] = true;
    REGEX_SPECIAL_CHAR[41] = true;
    REGEX_SPECIAL_CHAR[43] = true;
    REGEX_SPECIAL_CHAR[42] = true;
    REGEX_SPECIAL_CHAR[46] = true;
    REGEX_SPECIAL_CHAR[94] = true;
    REGEX_SPECIAL_CHAR[36] = true;
    REGEX_SPECIAL_CHAR[124] = true;
    REGEX_SPECIAL_CHAR[63] = true;
    REGEX_SPECIAL_CHAR[92] = true;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/bool/ETLike.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */