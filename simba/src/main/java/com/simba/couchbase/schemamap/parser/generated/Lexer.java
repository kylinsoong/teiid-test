/*     */ package com.simba.couchbase.schemamap.parser.generated;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Lexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*  39 */   private static final int[] ZZ_LEXSTATE = { 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String ZZ_CMAP_PACKED = "\001\000#\003\001\t\t\003\001\b\001\003\001\007\t\001\007\003\003\002\001\013\004\002\001\n\001\002\001\016\004\002\001\r\007\002\001\f\002\002\001\005\001\003\001\006\001\003\001\002\001\004\003\002\001\013\004\002\001\n\001\002\001\016\004\002\001\r\007\002\001\f\002\002\005\003\002＀\000";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\001\000#\003\001\t\t\003\001\b\001\003\001\007\t\001\007\003\003\002\001\013\004\002\001\n\001\002\001\016\004\002\001\r\007\002\001\f\002\002\001\005\001\003\001\006\001\003\001\002\001\004\003\002\001\013\004\002\001\n\001\002\001\016\004\002\001\r\007\002\001\f\002\002\005\003\002＀\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\001\001\001\002\001\000\001\003\001\004\001\001\001\005\005\000\001\006\001\000\001\007\001\b";
/*     */   
/*     */ 
/*     */   private static int[] zzUnpackAction()
/*     */   {
/*  67 */     int[] result = new int[17];
/*  68 */     int offset = 0;
/*  69 */     offset = zzUnpackAction("\001\000\001\001\001\002\001\000\001\003\001\004\001\001\001\005\005\000\001\006\001\000\001\007\001\b", offset, result);
/*  70 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  74 */     int i = 0;
/*  75 */     int j = offset;
/*  76 */     int l = packed.length();
/*  77 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*  80 */         count > 0)
/*     */     {
/*  78 */       count = packed.charAt(i++);
/*  79 */       int value = packed.charAt(i++);
/*  80 */       result[(j++)] = value;count--;
/*     */     }
/*  82 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   
/*     */ 
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\017\000\036\000-\000<\000<\000<\000<\000K\000Z\000i\000x\000\000i\000\000<\000<";
/*     */   
/*     */ 
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/*  97 */     int[] result = new int[17];
/*  98 */     int offset = 0;
/*  99 */     offset = zzUnpackRowMap("\000\000\000\017\000\036\000-\000<\000<\000<\000<\000K\000Z\000i\000x\000\000i\000\000<\000<", offset, result);
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 104 */     int i = 0;
/* 105 */     int j = offset;
/* 106 */     int l = packed.length();
/* 107 */     while (i < l) {
/* 108 */       int high = packed.charAt(i++) << '\020';
/* 109 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/* 111 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 117 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\000\001\002\001\003\001\000\001\004\001\005\001\006\001\007\001\b\001\t\005\003\001\000\001\002\005\000\001\002\b\000\002\003\004\000\001\003\002\000\005\003\001\000\003\n\001\013\n\n\031\000\001\f\002\000\001\r\002\000\003\n\001\016\n\n\004\000\001\n\025\000\001\017\021\000\001\020\f\000\001\021\002\000";
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans()
/*     */   {
/* 128 */     int[] result = new int['¥'];
/* 129 */     int offset = 0;
/* 130 */     offset = zzUnpackTrans("\001\000\001\002\001\003\001\000\001\004\001\005\001\006\001\007\001\b\001\t\005\003\001\000\001\002\005\000\001\002\b\000\002\003\004\000\001\003\002\000\005\003\001\000\003\n\001\013\n\n\031\000\001\f\002\000\001\r\002\000\003\n\001\016\n\n\004\000\001\n\025\000\001\017\021\000\001\020\f\000\001\021\002\000", offset, result);
/* 131 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 135 */     int i = 0;
/* 136 */     int j = offset;
/* 137 */     int l = packed.length();
/* 138 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 142 */         count > 0)
/*     */     {
/* 139 */       count = packed.charAt(i++);
/* 140 */       int value = packed.charAt(i++);
/* 141 */       value--;
/* 142 */       result[(j++)] = value;count--;
/*     */     }
/* 144 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\002\001\001\000\004\t\005\000\001\001\001\000\002\t";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 169 */     int[] result = new int[17];
/* 170 */     int offset = 0;
/* 171 */     offset = zzUnpackAttribute("\001\000\002\001\001\000\004\t\005\000\001\001\001\000\002\t", offset, result);
/* 172 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 176 */     int i = 0;
/* 177 */     int j = offset;
/* 178 */     int l = packed.length();
/* 179 */     int count; for (; i < l; 
/*     */         
/*     */ 
/* 182 */         count > 0)
/*     */     {
/* 180 */       count = packed.charAt(i++);
/* 181 */       int value = packed.charAt(i++);
/* 182 */       result[(j++)] = value;count--;
/*     */     }
/* 184 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */   private int zzLexicalState = 0;
/*     */   
/*     */ 
/*     */ 
/* 198 */   private char[] zzBuffer = new char['䀀'];
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzMarkedPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzCurrentPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzStartRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzEndRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yyline;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yychar;
/*     */   
/*     */ 
/*     */   private int yycolumn;
/*     */   
/*     */ 
/* 228 */   private boolean zzAtBOL = true;
/*     */   
/*     */   private boolean zzAtEOF;
/*     */   private boolean zzEOFDone;
/*     */   private TokenInfo m_currentToken;
/*     */   private Parser yyparser;
/*     */   
/*     */   public class TokenInfo
/*     */   {
/*     */     private String m_token;
/*     */     
/*     */     public TokenInfo() {}
/*     */     
/*     */     public String getToken()
/*     */     {
/* 243 */       return this.m_token;
/*     */     }
/*     */     
/*     */ 
/*     */     public void updateToken(String token)
/*     */     {
/* 249 */       this.m_token = token;
/* 250 */       Lexer.this.yychar = token.length();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenInfo getCurrentToken()
/*     */   {
/* 262 */     return this.m_currentToken;
/*     */   }
/*     */   
/*     */ 
/*     */   public Lexer(Reader r, Parser yyparser)
/*     */   {
/* 268 */     this(r);
/* 269 */     this.yyparser = yyparser;
/* 270 */     this.m_currentToken = new TokenInfo();
/*     */   }
/*     */   
/*     */   private String updateToken()
/*     */   {
/* 275 */     String text = yytext();
/* 276 */     if (null != this.m_currentToken)
/*     */     {
/* 278 */       this.m_currentToken.updateToken(text);
/*     */     }
/* 280 */     return text;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Lexer(Reader in)
/*     */   {
/* 292 */     this.zzReader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Lexer(InputStream in)
/*     */   {
/* 302 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 312 */     char[] map = new char[65536];
/* 313 */     int i = 0;
/* 314 */     int j = 0;
/* 315 */     int count; for (; i < 80; 
/*     */         
/*     */ 
/* 318 */         count > 0)
/*     */     {
/* 316 */       count = packed.charAt(i++);
/* 317 */       char value = packed.charAt(i++);
/* 318 */       map[(j++)] = value;count--;
/*     */     }
/* 320 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean zzRefill()
/*     */     throws IOException
/*     */   {
/* 334 */     if (this.zzStartRead > 0) {
/* 335 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 340 */       this.zzEndRead -= this.zzStartRead;
/* 341 */       this.zzCurrentPos -= this.zzStartRead;
/* 342 */       this.zzMarkedPos -= this.zzStartRead;
/* 343 */       this.zzStartRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 347 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 349 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 350 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 351 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 355 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */     
/*     */ 
/* 358 */     if (numRead > 0) {
/* 359 */       this.zzEndRead += numRead;
/* 360 */       return false;
/*     */     }
/*     */     
/* 363 */     if (numRead == 0) {
/* 364 */       int c = this.zzReader.read();
/* 365 */       if (c == -1) {
/* 366 */         return true;
/*     */       }
/* 368 */       this.zzBuffer[(this.zzEndRead++)] = ((char)c);
/* 369 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 374 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 382 */     this.zzAtEOF = true;
/* 383 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 385 */     if (this.zzReader != null) {
/* 386 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyreset(Reader reader)
/*     */   {
/* 401 */     this.zzReader = reader;
/* 402 */     this.zzAtBOL = true;
/* 403 */     this.zzAtEOF = false;
/* 404 */     this.zzEOFDone = false;
/* 405 */     this.zzEndRead = (this.zzStartRead = 0);
/* 406 */     this.zzCurrentPos = (this.zzMarkedPos = 0);
/* 407 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 408 */     this.zzLexicalState = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 416 */     return this.zzLexicalState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 426 */     this.zzLexicalState = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 434 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final char yycharat(int pos)
/*     */   {
/* 450 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 458 */     return this.zzMarkedPos - this.zzStartRead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void zzScanError(int errorCode)
/*     */   {
/*     */     String message;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 479 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 482 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */     
/* 485 */     throw new Error(message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void yypushback(int number)
/*     */   {
/* 498 */     if (number > yylength()) {
/* 499 */       zzScanError(2);
/*     */     }
/* 501 */     this.zzMarkedPos -= number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void zzDoEOF()
/*     */     throws IOException
/*     */   {
/* 510 */     if (!this.zzEOFDone) {
/* 511 */       this.zzEOFDone = true;
/* 512 */       yyclose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int yylex()
/*     */     throws IOException
/*     */   {
/* 531 */     int zzEndReadL = this.zzEndRead;
/* 532 */     char[] zzBufferL = this.zzBuffer;
/* 533 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 535 */     int[] zzTransL = ZZ_TRANS;
/* 536 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 537 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 540 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 542 */       int zzAction = -1;
/*     */       
/* 544 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 546 */       this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
/*     */       
/*     */       int zzInput;
/*     */       for (;;)
/*     */       {
/*     */         int zzInput;
/* 552 */         if (zzCurrentPosL < zzEndReadL) {
/* 553 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/* 554 */         } else { if (this.zzAtEOF) {
/* 555 */             int zzInput = -1;
/* 556 */             break;
/*     */           }
/*     */           
/*     */ 
/* 560 */           this.zzCurrentPos = zzCurrentPosL;
/* 561 */           this.zzMarkedPos = zzMarkedPosL;
/* 562 */           boolean eof = zzRefill();
/*     */           
/* 564 */           zzCurrentPosL = this.zzCurrentPos;
/* 565 */           zzMarkedPosL = this.zzMarkedPos;
/* 566 */           zzBufferL = this.zzBuffer;
/* 567 */           zzEndReadL = this.zzEndRead;
/* 568 */           if (eof) {
/* 569 */             int zzInput = -1;
/* 570 */             break;
/*     */           }
/*     */           
/* 573 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */         
/* 576 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 577 */         if (zzNext == -1) break;
/* 578 */         this.zzState = zzNext;
/*     */         
/* 580 */         int zzAttributes = zzAttrL[this.zzState];
/* 581 */         if ((zzAttributes & 0x1) == 1) {
/* 582 */           zzAction = this.zzState;
/* 583 */           zzMarkedPosL = zzCurrentPosL;
/* 584 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 591 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 593 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 2: 
/* 595 */         String text = updateToken();
/* 596 */         text = "`" + text + "`";
/* 597 */         this.yyparser.yylval = new ParserVal(text);
/* 598 */         return 266;
/*     */       case 9: 
/*     */         break;
/*     */       
/*     */       case 1: 
/* 603 */         String text = updateToken();
/* 604 */         this.yyparser.yylval = new ParserVal(Integer.parseInt(text));
/*     */         
/* 606 */         return 268;
/*     */       case 10: 
/*     */         break;
/*     */       case 5: 
/* 610 */         updateToken();
/* 611 */         return 259;
/*     */       case 11: 
/*     */         break;
/*     */       case 6: 
/* 615 */         String text = updateToken();
/*     */         
/* 617 */         this.yyparser.yylval = new ParserVal(text);
/* 618 */         return 266;
/*     */       case 12: 
/*     */         break;
/*     */       case 4: 
/* 622 */         updateToken();
/* 623 */         return 261;
/*     */       case 13: 
/*     */         break;
/*     */       case 8: 
/* 627 */         updateToken();
/* 628 */         return 262;
/*     */       case 14: 
/*     */         break;
/*     */       case 7: 
/* 632 */         updateToken();
/* 633 */         return 264;
/*     */       case 15: 
/*     */         break;
/*     */       case 3: 
/* 637 */         updateToken();
/* 638 */         return 260;
/*     */       case 16: 
/*     */         break;
/*     */       default: 
/* 642 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 643 */           this.zzAtEOF = true;
/* 644 */           zzDoEOF();
/* 645 */           return 0;
/*     */         }
/*     */         
/* 648 */         zzScanError(1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/schemamap/parser/generated/Lexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */