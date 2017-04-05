package com.simba.sqlengine.parser.generated;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

class Lexer
{
  public static final int YYEOF = -1;
  private static final int ZZ_BUFFERSIZE = 16384;
  public static final int YYINITIAL = 0;
  private static final int[] ZZ_LEXSTATE = { 0, 0 };
  private static final String ZZ_CMAP_PACKED = "\001\016\b\b\001\001\001\001\002\b\001\001\022\b\001\001\001\034\001\002\004\000\001\003\001\017\001\017\001\017\001\022\001\017\001\022\001\005\001\017\n\f\001\017\001\000\001\031\001\033\001\032\001\017\001\000\001\t\001\t\001\t\001\t\001\021\001\t\002\t\001\025\001\t\001\026\001\n\001\t\001\020\001\023\001\t\001\030\001\t\001\027\001\024\006\t\001\006\001\017\001\007\001\000\001\r\001\004\001\t\001\t\001\t\001\t\001\021\001\t\002\t\001\025\001\t\001\026\001\n\001\t\001\020\001\023\001\t\001\030\001\t\001\027\001\024\006\t\001\017\001\035\001\017\001\000\001\b\r\t\001\013r\t＀\000";
  private static final char[] ZZ_CMAP = zzUnpackCMap("\001\016\b\b\001\001\001\001\002\b\001\001\022\b\001\001\001\034\001\002\004\000\001\003\001\017\001\017\001\017\001\022\001\017\001\022\001\005\001\017\n\f\001\017\001\000\001\031\001\033\001\032\001\017\001\000\001\t\001\t\001\t\001\t\001\021\001\t\002\t\001\025\001\t\001\026\001\n\001\t\001\020\001\023\001\t\001\030\001\t\001\027\001\024\006\t\001\006\001\017\001\007\001\000\001\r\001\004\001\t\001\t\001\t\001\t\001\021\001\t\002\t\001\025\001\t\001\026\001\n\001\t\001\020\001\023\001\t\001\030\001\t\001\027\001\024\006\t\001\017\001\035\001\017\001\000\001\b\r\t\001\013r\t＀\000");
  private static final int[] ZZ_ACTION = zzUnpackAction();
  private static final String ZZ_ACTION_PACKED_0 = "\001\000\001\001\001\002\001\003\001\001\004\003\002\004\001\005\002\004\001\006\001\007\001\b\002\001\003\000\001\t\002\000\001\n\004\000\002\004\001\013\001\f\001\r\001\016\001\017\001\020\001\021\001\022\001\000\002\004\001\000\002\004\001\000\001\004\002\023\001\000\002\004\001\023\001\000\001\004\001\023\002\024\001\004\001\025";
  private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
  private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\036\000<\000Z\000x\000\000´\000Ò\000\036\000ð\000Ď\000Ĭ\000Ŋ\000Ũ\000Ɔ\000Ƥ\000\036\000ǂ\000Ǡ\000Ǿ\000Ȝ\000x\000Ⱥ\000ɘ\000ɶ\000ʔ\000ʲ\000ː\000ˮ\000̌\000̪\000͈\000\036\000\036\000\036\000\036\000Ȝ\000ɶ\000ˮ\000ͦ\000ͦ\000΄\000΢\000π\000Ϟ\000ϼ\000К\000и\000і\000Ѵ\000Ғ\000Ұ\000ӎ\000Ӭ\000Ԋ\000Ԩ\000Ն\000\036\000ð\000դ\000ւ";
  private static final int[] ZZ_TRANS = zzUnpackTrans();
  private static final String ZZ_TRANS_PACKED_0 = "\001\002\001\003\001\004\001\005\001\006\001\007\001\b\001\t\001\002\002\n\001\013\001\f\001\n\001\002\001\t\001\r\001\n\001\t\004\n\001\016\001\n\001\017\001\020\001\021\001\022\001\023\037\000\001\003\t\000\001\003\022\000\002\024\001\025\005\024\001\000\005\024\001\000\017\024\003\026\001\027\004\026\001\000\005\026\001\000\017\026\004\030\001\031\t\030\001\000\017\030\f\000\001\032\021\000\006\033\001\034\001\035\001\000\005\033\001\000\017\033\t\000\005\n\002\000\002\n\001\000\006\n\006\000\001\003\007\000\002\n\001\013\002\n\002\000\002\n\001\000\006\n\n\000\001\032\006\000\001\f\004\000\001\036\025\000\005\n\002\000\002\n\001\000\001\037\005\n\016\000\005\n\002\000\002\n\001\000\005\n\001 \037\000\001!\001\"\035\000\001#\035\000\001!\037\000\001$\002\024\001%\005\024\001\000\005\024\001\000\017\024\002\000\001\024\036\000\001\026\032\000\004\030\001&\t\030\001\000\017\030\004\000\001\030%\000\001\032\004\000\001\036\f\000\006\033\001\034\001'\001\000\005\033\001\000\017\033\006\000\001\033\036\000\001\033\"\000\001(\005\000\001)\024\000\005\n\002\000\002\n\001\000\001\n\001*\004\n\016\000\001\n\001+\003\n\002\000\002\n\001\000\006\n\021\000\001(\022\000\001,\007\000\002\n\001-\002\n\002\000\002\n\001\000\006\n\016\000\004\n\001.\002\000\002\n\001\000\006\n\006\000\001,\b\000\001/\001,\023\000\001,\007\000\001\n\0010\001-\002\n\002\000\002\n\001\000\006\n\016\000\0031\002\n\002\000\0021\001\000\0011\0012\0041\032\000\0013\021\000\005\n\002\000\002\n\001\000\002\n\0014\003\n\016\000\0031\001\n\0015\002\000\0021\001\000\0061\016\000\0031\001\n\0015\002\000\0021\001\000\0041\0016\0011\033\000\0017\020\000\005\n\002\000\002\n\001\000\003\n\0018\002\n\016\000\0031\002\n\002\000\0021\001\000\0061\016\000\0031\001\n\0015\002\000\0021\001\000\0021\0019\0031\026\000\001:\025\000\005\n\002\000\001\n\001;\001\000\006\n\016\000\0031\001\n\001<\002\000\0021\001\000\0061\016\000\003=\002\n\002\000\002=\001\000\006=\016\000\003=\001\n\001<\002\000\002=\001\000\006=\005\000";
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
  private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
  private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\001\t\006\001\001\t\007\001\001\t\002\001\003\000\001\001\002\000\001\001\004\000\002\001\004\t\004\001\001\000\002\001\001\000\002\001\001\000\003\001\001\000\003\001\001\000\002\001\001\t\003\001";
  private Reader zzReader;
  private int zzState;
  private int zzLexicalState = 0;
  private char[] zzBuffer = new char['䀀'];
  private int zzMarkedPos;
  private int zzCurrentPos;
  private int zzStartRead;
  private int zzEndRead;
  private int yyline;
  private int yychar;
  private int yycolumn;
  private boolean zzAtBOL = true;
  private boolean zzAtEOF;
  private boolean zzEOFDone;
  private TokenInfo m_currentToken;
  private static final TreeMap<String, Short> KWTab = new TreeMap();
  private static final HashSet<String> DataTypeTab = new HashSet(Arrays.asList(new String[] { "SQL_BIGINT", "SQL_BINARY", "SQL_BIT", "SQL_CHAR", "SQL_DATE", "SQL_DECIMAL", "SQL_DOUBLE", "SQL_FLOAT", "SQL_GUID", "SQL_INTEGER", "SQL_INTERVAL_DAY", "SQL_INTERVAL_DAY_TO_HOUR", "SQL_INTERVAL_DAY_TO_MINUTE", "SQL_INTERVAL_DAY_TO_SECOND", "SQL_INTERVAL_HOUR", "SQL_INTERVAL_HOUR_TO_MINUTE", "SQL_INTERVAL_HOUR_TO_SECOND", "SQL_INTERVAL_MINUTE", "SQL_INTERVAL_MINUTE_TO_SECOND", "SQL_INTERVAL_MONTH", "SQL_INTERVAL_SECOND", "SQL_INTERVAL_YEAR", "SQL_INTERVAL_YEAR_TO_MONTH", "SQL_LONGVARBINARY", "SQL_LONGVARCHAR", "SQL_NUMERIC", "SQL_REAL", "SQL_SMALLINT", "SQL_TIME", "SQL_TIMESTAMP", "SQL_TYPE_DATE", "SQL_TYPE_TIME", "SQL_TYPE_TIMESTAMP", "SQL_TINYINT", "SQL_VARBINARY", "SQL_VARCHAR", "SQL_WCHAR", "SQL_WLONGVARCHAR", "SQL_WVARCHAR" }));
  private static final HashSet<String> TSIDataTypeTab = new HashSet(Arrays.asList(new String[] { "SQL_TSI_DAY", "SQL_TSI_HOUR", "SQL_TSI_FRAC_SECOND", "SQL_TSI_MINUTE", "SQL_TSI_MONTH", "SQL_TSI_QUARTER", "SQL_TSI_SECOND", "SQL_TSI_WEEK", "SQL_TSI_YEAR" }));
  private Parser yyparser;
  
  private static int[] zzUnpackAction()
  {
    int[] arrayOfInt = new int[61];
    int i = 0;
    i = zzUnpackAction("\001\000\001\001\001\002\001\003\001\001\004\003\002\004\001\005\002\004\001\006\001\007\001\b\002\001\003\000\001\t\002\000\001\n\004\000\002\004\001\013\001\f\001\r\001\016\001\017\001\020\001\021\001\022\001\000\002\004\001\000\002\004\001\000\001\004\002\023\001\000\002\004\001\023\001\000\001\004\001\023\002\024\001\004\001\025", i, arrayOfInt);
    return arrayOfInt;
  }
  
  private static int zzUnpackAction(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      } while (m > 0);
    }
    return j;
  }
  
  private static int[] zzUnpackRowMap()
  {
    int[] arrayOfInt = new int[61];
    int i = 0;
    i = zzUnpackRowMap("\000\000\000\036\000<\000Z\000x\000\000´\000Ò\000\036\000ð\000Ď\000Ĭ\000Ŋ\000Ũ\000Ɔ\000Ƥ\000\036\000ǂ\000Ǡ\000Ǿ\000Ȝ\000x\000Ⱥ\000ɘ\000ɶ\000ʔ\000ʲ\000ː\000ˮ\000̌\000̪\000͈\000\036\000\036\000\036\000\036\000Ȝ\000ɶ\000ˮ\000ͦ\000ͦ\000΄\000΢\000π\000Ϟ\000ϼ\000К\000и\000і\000Ѵ\000Ғ\000Ұ\000ӎ\000Ӭ\000Ԋ\000Ԩ\000Ն\000\036\000ð\000դ\000ւ", i, arrayOfInt);
    return arrayOfInt;
  }
  
  private static int zzUnpackRowMap(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++) << '\020';
      paramArrayOfInt[(j++)] = (m | paramString.charAt(i++));
    }
    return j;
  }
  
  private static int[] zzUnpackTrans()
  {
    int[] arrayOfInt = new int['֠'];
    int i = 0;
    i = zzUnpackTrans("\001\002\001\003\001\004\001\005\001\006\001\007\001\b\001\t\001\002\002\n\001\013\001\f\001\n\001\002\001\t\001\r\001\n\001\t\004\n\001\016\001\n\001\017\001\020\001\021\001\022\001\023\037\000\001\003\t\000\001\003\022\000\002\024\001\025\005\024\001\000\005\024\001\000\017\024\003\026\001\027\004\026\001\000\005\026\001\000\017\026\004\030\001\031\t\030\001\000\017\030\f\000\001\032\021\000\006\033\001\034\001\035\001\000\005\033\001\000\017\033\t\000\005\n\002\000\002\n\001\000\006\n\006\000\001\003\007\000\002\n\001\013\002\n\002\000\002\n\001\000\006\n\n\000\001\032\006\000\001\f\004\000\001\036\025\000\005\n\002\000\002\n\001\000\001\037\005\n\016\000\005\n\002\000\002\n\001\000\005\n\001 \037\000\001!\001\"\035\000\001#\035\000\001!\037\000\001$\002\024\001%\005\024\001\000\005\024\001\000\017\024\002\000\001\024\036\000\001\026\032\000\004\030\001&\t\030\001\000\017\030\004\000\001\030%\000\001\032\004\000\001\036\f\000\006\033\001\034\001'\001\000\005\033\001\000\017\033\006\000\001\033\036\000\001\033\"\000\001(\005\000\001)\024\000\005\n\002\000\002\n\001\000\001\n\001*\004\n\016\000\001\n\001+\003\n\002\000\002\n\001\000\006\n\021\000\001(\022\000\001,\007\000\002\n\001-\002\n\002\000\002\n\001\000\006\n\016\000\004\n\001.\002\000\002\n\001\000\006\n\006\000\001,\b\000\001/\001,\023\000\001,\007\000\001\n\0010\001-\002\n\002\000\002\n\001\000\006\n\016\000\0031\002\n\002\000\0021\001\000\0011\0012\0041\032\000\0013\021\000\005\n\002\000\002\n\001\000\002\n\0014\003\n\016\000\0031\001\n\0015\002\000\0021\001\000\0061\016\000\0031\001\n\0015\002\000\0021\001\000\0041\0016\0011\033\000\0017\020\000\005\n\002\000\002\n\001\000\003\n\0018\002\n\016\000\0031\002\n\002\000\0021\001\000\0061\016\000\0031\001\n\0015\002\000\0021\001\000\0021\0019\0031\026\000\001:\025\000\005\n\002\000\001\n\001;\001\000\006\n\016\000\0031\001\n\001<\002\000\0021\001\000\0061\016\000\003=\002\n\002\000\002=\001\000\006=\016\000\003=\001\n\001<\002\000\002=\001\000\006=\005\000", i, arrayOfInt);
    return arrayOfInt;
  }
  
  private static int zzUnpackTrans(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      n--;
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      } while (m > 0);
    }
    return j;
  }
  
  private static int[] zzUnpackAttribute()
  {
    int[] arrayOfInt = new int[61];
    int i = 0;
    i = zzUnpackAttribute("\001\000\001\t\006\001\001\t\007\001\001\t\002\001\003\000\001\001\002\000\001\001\004\000\002\001\004\t\004\001\001\000\002\001\001\000\002\001\001\000\003\001\001\000\003\001\001\000\002\001\001\t\003\001", i, arrayOfInt);
    return arrayOfInt;
  }
  
  private static int zzUnpackAttribute(String paramString, int paramInt, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt;
    int k = paramString.length();
    while (i < k)
    {
      int m = paramString.charAt(i++);
      int n = paramString.charAt(i++);
      do
      {
        paramArrayOfInt[(j++)] = n;
        m--;
      } while (m > 0);
    }
    return j;
  }
  
  public TokenInfo getCurrentToken()
  {
    return this.m_currentToken;
  }
  
  public Lexer(Reader paramReader, Parser paramParser)
  {
    this(paramReader);
    this.yyparser = paramParser;
    this.m_currentToken = new TokenInfo();
  }
  
  static Short getKeywordId(String paramString)
  {
    return (Short)KWTab.get(paramString.toUpperCase());
  }
  
  static boolean isRelaxedKeyword(short paramShort)
  {
    switch (paramShort)
    {
    case 266: 
    case 282: 
    case 286: 
    case 287: 
    case 310: 
    case 327: 
    case 328: 
    case 329: 
    case 330: 
    case 351: 
    case 357: 
    case 360: 
    case 361: 
    case 372: 
    case 381: 
      return true;
    }
    return false;
  }
  
  static String getDataType(String paramString)
  {
    String str = paramString.toUpperCase();
    if (DataTypeTab.contains(str)) {
      return str;
    }
    return null;
  }
  
  static boolean isValidTSIDataType(String paramString)
  {
    String str = paramString.toUpperCase();
    return TSIDataTypeTab.contains(str);
  }
  
  private String updateToken()
  {
    String str = yytext();
    if (null != this.m_currentToken) {
      this.m_currentToken.updateToken(str);
    }
    return str;
  }
  
  Lexer(Reader paramReader)
  {
    this.zzReader = paramReader;
  }
  
  Lexer(InputStream paramInputStream)
  {
    this(new InputStreamReader(paramInputStream));
  }
  
  private static char[] zzUnpackCMap(String paramString)
  {
    char[] arrayOfChar = new char[65536];
    int i = 0;
    int j = 0;
    while (i < 166)
    {
      int k = paramString.charAt(i++);
      int m = paramString.charAt(i++);
      do
      {
        arrayOfChar[(j++)] = m;
        k--;
      } while (k > 0);
    }
    return arrayOfChar;
  }
  
  private boolean zzRefill()
    throws IOException
  {
    if (this.zzStartRead > 0)
    {
      System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
      this.zzEndRead -= this.zzStartRead;
      this.zzCurrentPos -= this.zzStartRead;
      this.zzMarkedPos -= this.zzStartRead;
      this.zzStartRead = 0;
    }
    if (this.zzCurrentPos >= this.zzBuffer.length)
    {
      char[] arrayOfChar = new char[this.zzCurrentPos * 2];
      System.arraycopy(this.zzBuffer, 0, arrayOfChar, 0, this.zzBuffer.length);
      this.zzBuffer = arrayOfChar;
    }
    int i = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
    if (i > 0)
    {
      this.zzEndRead += i;
      return false;
    }
    if (i == 0)
    {
      int j = this.zzReader.read();
      if (j == -1) {
        return true;
      }
      this.zzBuffer[(this.zzEndRead++)] = ((char)j);
      return false;
    }
    return true;
  }
  
  public final void yyclose()
    throws IOException
  {
    this.zzAtEOF = true;
    this.zzEndRead = this.zzStartRead;
    if (this.zzReader != null) {
      this.zzReader.close();
    }
  }
  
  public final void yyreset(Reader paramReader)
  {
    this.zzReader = paramReader;
    this.zzAtBOL = true;
    this.zzAtEOF = false;
    this.zzEOFDone = false;
    this.zzEndRead = (this.zzStartRead = 0);
    this.zzCurrentPos = (this.zzMarkedPos = 0);
    this.yyline = (this.yychar = this.yycolumn = 0);
    this.zzLexicalState = 0;
  }
  
  public final int yystate()
  {
    return this.zzLexicalState;
  }
  
  public final void yybegin(int paramInt)
  {
    this.zzLexicalState = paramInt;
  }
  
  public final String yytext()
  {
    return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
  }
  
  public final char yycharat(int paramInt)
  {
    return this.zzBuffer[(this.zzStartRead + paramInt)];
  }
  
  public final int yylength()
  {
    return this.zzMarkedPos - this.zzStartRead;
  }
  
  private void zzScanError(int paramInt)
  {
    String str;
    try
    {
      str = ZZ_ERROR_MSG[paramInt];
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      str = ZZ_ERROR_MSG[0];
    }
    throw new Error(str);
  }
  
  public void yypushback(int paramInt)
  {
    if (paramInt > yylength()) {
      zzScanError(2);
    }
    this.zzMarkedPos -= paramInt;
  }
  
  private void zzDoEOF()
    throws IOException
  {
    if (!this.zzEOFDone)
    {
      this.zzEOFDone = true;
      yyclose();
    }
  }
  
  public int yylex()
    throws IOException
  {
    int n = this.zzEndRead;
    char[] arrayOfChar1 = this.zzBuffer;
    char[] arrayOfChar2 = ZZ_CMAP;
    int[] arrayOfInt1 = ZZ_TRANS;
    int[] arrayOfInt2 = ZZ_ROWMAP;
    int[] arrayOfInt3 = ZZ_ATTRIBUTE;
    for (;;)
    {
      int m = this.zzMarkedPos;
      int j = -1;
      int k = this.zzCurrentPos = this.zzStartRead = m;
      this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
      int i;
      for (;;)
      {
        if (k < n)
        {
          i = arrayOfChar1[(k++)];
        }
        else
        {
          if (this.zzAtEOF)
          {
            i = -1;
            break;
          }
          this.zzCurrentPos = k;
          this.zzMarkedPos = m;
          boolean bool = zzRefill();
          k = this.zzCurrentPos;
          m = this.zzMarkedPos;
          arrayOfChar1 = this.zzBuffer;
          n = this.zzEndRead;
          if (bool)
          {
            i = -1;
            break;
          }
          i = arrayOfChar1[(k++)];
        }
        int i1 = arrayOfInt1[(arrayOfInt2[this.zzState] + arrayOfChar2[i])];
        if (i1 == -1) {
          break;
        }
        this.zzState = i1;
        int i2 = arrayOfInt3[this.zzState];
        if ((i2 & 0x1) == 1)
        {
          j = this.zzState;
          m = k;
          if ((i2 & 0x8) == 8) {
            break;
          }
        }
      }
      this.zzMarkedPos = m;
      String str;
      Object localObject;
      switch (j < 0 ? j : ZZ_ACTION[j])
      {
      case 19: 
        str = updateToken();
        localObject = getDataType(str);
        if (null == localObject)
        {
          this.yyparser.yylval = new ParserVal(str);
          return 311;
        }
        this.yyparser.yylval = new ParserVal((String)localObject);
        return 285;
      case 22: 
        break;
      case 16: 
        str = updateToken();
        str = str.substring(1, str.length() - 1).replace("``", "`");
        this.yyparser.yylval = new ParserVal(str);
        return 311;
      case 23: 
        break;
      case 15: 
        str = updateToken();
        str = str.substring(1, str.length() - 1).replace("\"\"", "\"");
        this.yyparser.yylval = new ParserVal(str);
        return 311;
      case 24: 
        break;
      case 10: 
        str = updateToken();
        this.yyparser.yylval = new ParserVal(str);
        return 288;
      case 25: 
        break;
      case 12: 
        updateToken();
        return 322;
      case 26: 
        break;
      case 3: 
        updateToken();
        return yycharat(0);
      case 27: 
        break;
      case 6: 
        updateToken();
        return 326;
      case 28: 
        break;
      case 11: 
        updateToken();
        return 331;
      case 29: 
        break;
      case 20: 
        updateToken();
        return 333;
      case 30: 
        break;
      case 14: 
        updateToken();
        return 278;
      case 31: 
        break;
      case 4: 
        str = updateToken();
        localObject = getKeywordId(str);
        if (null != localObject)
        {
          if (isRelaxedKeyword(((Short)localObject).shortValue())) {
            this.yyparser.yylval = new ParserVal(str);
          }
          return ((Short)localObject).shortValue();
        }
        this.yyparser.yylval = new ParserVal(str);
        return 311;
      case 32: 
        break;
      case 2: 
        updateToken();
      case 33: 
        break;
      case 8: 
        updateToken();
        return 296;
      case 34: 
        break;
      case 13: 
        updateToken();
        return 305;
      case 35: 
        break;
      case 7: 
        updateToken();
        return 308;
      case 36: 
        break;
      case 18: 
        str = updateToken();
        this.yyparser.yylval = new ParserVal(str);
        return 263;
      case 37: 
        break;
      case 9: 
        str = updateToken();
        localObject = new StringBuffer(str.length() - 2);
        ((StringBuffer)localObject).append(str.substring(1, str.length() - 1).replace("''", "'"));
        str = ((StringBuffer)localObject).toString();
        this.yyparser.yylval = new ParserVal(str);
        return 274;
      case 38: 
        break;
      case 5: 
        str = updateToken();
        this.yyparser.yylval = new ParserVal(str);
        try
        {
          Long.parseLong(str);
          return 373;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          return 288;
        }
      case 39: 
        break;
      case 17: 
        str = updateToken();
        str = str.substring(1, str.length() - 1).replace("[[", "[").replace("]]", "]");
        this.yyparser.yylval = new ParserVal(str);
        return 311;
      case 40: 
        break;
      case 1: 
        updateToken();
        return 257;
      case 41: 
        break;
      case 21: 
        str = updateToken();
        this.yyparser.yylval = new ParserVal(str);
        if (!isValidTSIDataType(str)) {
          return 311;
        }
        return 366;
      case 42: 
        break;
      default: 
        if ((i == -1) && (this.zzStartRead == this.zzCurrentPos))
        {
          this.zzAtEOF = true;
          zzDoEOF();
          return 0;
        }
        zzScanError(1);
      }
    }
  }
  
  static
  {
    KWTab.put("ADD", Short.valueOf((short)258));
    KWTab.put("ALL", Short.valueOf((short)259));
    KWTab.put("ALTER", Short.valueOf((short)260));
    KWTab.put("AND", Short.valueOf((short)261));
    KWTab.put("ANY", Short.valueOf((short)262));
    KWTab.put("AS", Short.valueOf((short)264));
    KWTab.put("ASC", Short.valueOf((short)265));
    KWTab.put("AVG", Short.valueOf((short)266));
    KWTab.put("BETWEEN", Short.valueOf((short)267));
    KWTab.put("BY", Short.valueOf((short)268));
    KWTab.put("CALL", Short.valueOf((short)269));
    KWTab.put("CASCADE", Short.valueOf((short)271));
    KWTab.put("CASE", Short.valueOf((short)270));
    KWTab.put("CAST", Short.valueOf((short)272));
    KWTab.put("CATALOG", Short.valueOf((short)273));
    KWTab.put("CHECK", Short.valueOf((short)275));
    KWTab.put("COALESCE", Short.valueOf((short)276));
    KWTab.put("COLUMN", Short.valueOf((short)277));
    KWTab.put("CONSTRAINT", Short.valueOf((short)279));
    KWTab.put("CONVERT", Short.valueOf((short)280));
    KWTab.put("CORRESPONDING", Short.valueOf((short)281));
    KWTab.put("COUNT", Short.valueOf((short)282));
    KWTab.put("CREATE", Short.valueOf((short)283));
    KWTab.put("CROSS", Short.valueOf((short)284));
    KWTab.put("DATE", Short.valueOf((short)286));
    KWTab.put("DAY", Short.valueOf((short)287));
    KWTab.put("DEFAULT", Short.valueOf((short)289));
    KWTab.put("DELETE", Short.valueOf((short)290));
    KWTab.put("DESC", Short.valueOf((short)291));
    KWTab.put("DISTINCT", Short.valueOf((short)292));
    KWTab.put("DROP", Short.valueOf((short)293));
    KWTab.put("ELSE", Short.valueOf((short)294));
    KWTab.put("END", Short.valueOf((short)295));
    KWTab.put("ESCAPE", Short.valueOf((short)297));
    KWTab.put("EXCEPT", Short.valueOf((short)298));
    KWTab.put("EXISTS", Short.valueOf((short)299));
    KWTab.put("FN", Short.valueOf((short)300));
    KWTab.put("FOR", Short.valueOf((short)301));
    KWTab.put("FOREIGN", Short.valueOf((short)302));
    KWTab.put("FROM", Short.valueOf((short)303));
    KWTab.put("FULL", Short.valueOf((short)304));
    KWTab.put("GRANT", Short.valueOf((short)306));
    KWTab.put("GROUP", Short.valueOf((short)307));
    KWTab.put("HAVING", Short.valueOf((short)309));
    KWTab.put("HOUR", Short.valueOf((short)310));
    KWTab.put("IF", Short.valueOf((short)312));
    KWTab.put("IN", Short.valueOf((short)313));
    KWTab.put("INDEX", Short.valueOf((short)314));
    KWTab.put("INNER", Short.valueOf((short)315));
    KWTab.put("INSERT", Short.valueOf((short)316));
    KWTab.put("INTERVAL", Short.valueOf((short)317));
    KWTab.put("INTO", Short.valueOf((short)318));
    KWTab.put("IS", Short.valueOf((short)319));
    KWTab.put("JOIN", Short.valueOf((short)320));
    KWTab.put("KEY", Short.valueOf((short)321));
    KWTab.put("LEFT", Short.valueOf((short)323));
    KWTab.put("LIKE", Short.valueOf((short)324));
    KWTab.put("LIMIT", Short.valueOf((short)325));
    KWTab.put("MAX", Short.valueOf((short)327));
    KWTab.put("MIN", Short.valueOf((short)328));
    KWTab.put("MINUTE", Short.valueOf((short)329));
    KWTab.put("MONTH", Short.valueOf((short)330));
    KWTab.put("NOT", Short.valueOf((short)332));
    KWTab.put("NULL", Short.valueOf((short)334));
    KWTab.put("NULLIF", Short.valueOf((short)335));
    KWTab.put("ON", Short.valueOf((short)336));
    KWTab.put("OPTION", Short.valueOf((short)337));
    KWTab.put("OR", Short.valueOf((short)338));
    KWTab.put("ORDER", Short.valueOf((short)339));
    KWTab.put("OUTER", Short.valueOf((short)340));
    KWTab.put("PERCENT", Short.valueOf((short)341));
    KWTab.put("PRIMARY", Short.valueOf((short)342));
    KWTab.put("PRIVILEGES", Short.valueOf((short)343));
    KWTab.put("PROCEDURE", Short.valueOf((short)344));
    KWTab.put("PUBLIC", Short.valueOf((short)345));
    KWTab.put("REFERENCES", Short.valueOf((short)346));
    KWTab.put("RESTRICT", Short.valueOf((short)347));
    KWTab.put("REVOKE", Short.valueOf((short)348));
    KWTab.put("RIGHT", Short.valueOf((short)349));
    KWTab.put("SCHEMA", Short.valueOf((short)350));
    KWTab.put("SECOND", Short.valueOf((short)351));
    KWTab.put("SELECT", Short.valueOf((short)352));
    KWTab.put("SET", Short.valueOf((short)353));
    KWTab.put("SOME", Short.valueOf((short)354));
    KWTab.put("STDDEV", Short.valueOf((short)355));
    KWTab.put("STDDEV_POP", Short.valueOf((short)356));
    KWTab.put("SUM", Short.valueOf((short)357));
    KWTab.put("TABLE", Short.valueOf((short)358));
    KWTab.put("THEN", Short.valueOf((short)359));
    KWTab.put("TIME", Short.valueOf((short)360));
    KWTab.put("TIMESTAMP", Short.valueOf((short)361));
    KWTab.put("TIMESTAMPADD", Short.valueOf((short)362));
    KWTab.put("TIMESTAMPDIFF", Short.valueOf((short)363));
    KWTab.put("TO", Short.valueOf((short)364));
    KWTab.put("TOP", Short.valueOf((short)365));
    KWTab.put("UNION", Short.valueOf((short)367));
    KWTab.put("UNIQUE", Short.valueOf((short)368));
    KWTab.put("UPDATE", Short.valueOf((short)369));
    KWTab.put("UPSERT", Short.valueOf((short)370));
    KWTab.put("USAGE", Short.valueOf((short)371));
    KWTab.put("USER", Short.valueOf((short)372));
    KWTab.put("VALUES", Short.valueOf((short)374));
    KWTab.put("VAR", Short.valueOf((short)375));
    KWTab.put("VAR_POP", Short.valueOf((short)376));
    KWTab.put("VIEW", Short.valueOf((short)377));
    KWTab.put("WHEN", Short.valueOf((short)378));
    KWTab.put("WHERE", Short.valueOf((short)379));
    KWTab.put("WITH", Short.valueOf((short)380));
    KWTab.put("YEAR", Short.valueOf((short)381));
  }
  
  public class TokenInfo
  {
    private String m_token;
    private int m_firstColumn;
    private int m_lastColumn;
    
    public TokenInfo() {}
    
    public String getToken()
    {
      return this.m_token;
    }
    
    public int getFirstColumn()
    {
      return this.m_firstColumn;
    }
    
    public int getLastColumn()
    {
      return this.m_lastColumn;
    }
    
    public void updateToken(String paramString)
    {
      this.m_token = paramString;
      this.m_firstColumn = (this.m_lastColumn + 1);
      this.m_lastColumn += paramString.length();
      Lexer.this.yycolumn = (Lexer.this.yycolumn + paramString.length());
      Lexer.this.yychar = paramString.length();
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/generated/Lexer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */