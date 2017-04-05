/*     */ package com.simba.couchbase.schemamap.parser.generated;
/*     */ 
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.schemamap.parser.CBArrayDimension;
/*     */ import com.simba.couchbase.schemamap.parser.CBArrayIndexedElement;
/*     */ import com.simba.couchbase.schemamap.parser.CBAttribute;
/*     */ import com.simba.couchbase.schemamap.parser.CBName;
/*     */ import com.simba.couchbase.schemamap.parser.CBSpecialIdentifier;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.sqlengine.exceptions.SQLEngineException;
/*     */ import com.simba.sqlengine.utilities.SQLEngineMessageKey;
/*     */ import com.simba.support.exceptions.DiagState;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
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
/*     */ 
/*     */ public class Parser
/*     */ {
/*     */   boolean yydebug;
/*     */   int yynerrs;
/*     */   int yyerrflag;
/*     */   int yychar;
/*     */   static final int YYSTACKSIZE = 500;
/*     */   
/*     */   void debug(String msg)
/*     */   {
/*  55 */     if (this.yydebug) {
/*  56 */       System.out.println(msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  61 */   int[] statestk = new int['Ǵ'];
/*     */   int stateptr;
/*     */   int stateptrmax;
/*     */   int statemax;
/*     */   String yytext;
/*     */   ParserVal yyval;
/*     */   
/*     */   final void state_push(int state)
/*     */   {
/*     */     try {
/*  71 */       this.stateptr += 1;
/*  72 */       this.statestk[this.stateptr] = state;
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/*  75 */       int oldsize = this.statestk.length;
/*  76 */       int newsize = oldsize * 2;
/*  77 */       int[] newstack = new int[newsize];
/*  78 */       System.arraycopy(this.statestk, 0, newstack, 0, oldsize);
/*  79 */       this.statestk = newstack;
/*  80 */       this.statestk[this.stateptr] = state;
/*     */     }
/*     */   }
/*     */   
/*     */   final int state_pop() {
/*  85 */     return this.statestk[(this.stateptr--)];
/*     */   }
/*     */   
/*     */   final void state_drop(int cnt) {
/*  89 */     this.stateptr -= cnt;
/*     */   }
/*     */   
/*     */   final int state_peek(int relative) {
/*  93 */     return this.statestk[(this.stateptr - relative)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final boolean init_stacks()
/*     */   {
/* 100 */     this.stateptr = -1;
/* 101 */     val_init();
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   ParserVal yylval;
/*     */   ParserVal[] valstk;
/*     */   int valptr;
/*     */   public static final short T_INVALID_CHAR = 257;
/*     */   public static final short END = 0;
/* 110 */   void dump_stacks(int count) { System.out.println("=index==state====value=     s:" + this.stateptr + "  v:" + this.valptr);
/* 111 */     for (int i = 0; i < count; i++)
/* 112 */       System.out.println(" " + i + "    " + this.statestk[i] + "      " + this.valstk[i]);
/* 113 */     System.out.println("======================");
/*     */   }
/*     */   
/*     */ 
/*     */   public static final short T_DOT = 259;
/*     */   
/*     */   public static final short T_LBRACKET = 260;
/*     */   
/*     */   public static final short T_RBRACKET = 261;
/*     */   public static final short T_IDX = 262;
/*     */   public static final short T_PK = 264;
/*     */   public static final short T_IDENTIFIER = 266;
/*     */   public static final short Identifier = 267;
/*     */   public static final short T_NUMBER = 268;
/*     */   public static final short Number = 269;
/*     */   public static final short YYERRCODE = 256;
/*     */   void val_init()
/*     */   {
/* 131 */     this.valstk = new ParserVal['Ǵ'];
/* 132 */     this.yyval = new ParserVal();
/* 133 */     this.yylval = new ParserVal();
/* 134 */     this.valptr = -1;
/*     */   }
/*     */   
/*     */   void val_push(ParserVal val) {
/* 138 */     if (this.valptr >= 500)
/* 139 */       return;
/* 140 */     this.valstk[(++this.valptr)] = val;
/*     */   }
/*     */   
/*     */   ParserVal val_pop() {
/* 144 */     if (this.valptr < 0)
/* 145 */       return new ParserVal();
/* 146 */     return this.valstk[(this.valptr--)];
/*     */   }
/*     */   
/*     */   void val_drop(int cnt)
/*     */   {
/* 151 */     int ptr = this.valptr - cnt;
/* 152 */     if (ptr < 0)
/* 153 */       return;
/* 154 */     this.valptr = ptr;
/*     */   }
/*     */   
/*     */   ParserVal val_peek(int relative)
/*     */   {
/* 159 */     int ptr = this.valptr - relative;
/* 160 */     if (ptr < 0)
/* 161 */       return new ParserVal();
/* 162 */     return this.valstk[ptr];
/*     */   }
/*     */   
/*     */   final ParserVal dup_yyval(ParserVal val) {
/* 166 */     ParserVal dup = new ParserVal();
/* 167 */     dup.ival = val.ival;
/* 168 */     dup.dval = val.dval;
/* 169 */     dup.sval = val.sval;
/* 170 */     dup.obj = val.obj;
/* 171 */     return dup;
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
/* 186 */   static final short[] yylhs = { -1, 0, 2, 2, 2, 1, 1, 1, 1 };
/*     */   
/*     */ 
/* 189 */   static final short[] yylen = { 2, 1, 1, 2, 4, 1, 3, 4, 3 };
/*     */   
/*     */ 
/* 192 */   static final short[] yydefred = { 0, 0, 0, 0, 1, 3, 0, 0, 8, 0, 0, 4, 7 };
/*     */   
/*     */ 
/*     */ 
/* 196 */   static final short[] yydgoto = { 2, 3, 4 };
/*     */   
/*     */ 
/* 199 */   static final short[] yysindex = { 65272, 65278, 0, 65281, 0, 0, 65279, 65275, 0, 65282, 65285, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/* 203 */   static final short[] yyrindex = { 0, 1, 0, 11, 0, 0, 0, 0, 0, 3, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/* 207 */   static final short[] yygindex = { 0, 0, 0 };
/*     */   static final int YYTABLESIZE = 263;
/*     */   static short[] yytable;
/*     */   static short[] yycheck;
/*     */   static final short YYFINAL = 2;
/*     */   static final short YYMAXTOKEN = 269;
/*     */   
/* 214 */   static void yytable() { yytable = new short[] { 9, 5, 1, 6, 6, 7, 5, 10, 11, 8, 12, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 6, 6 };
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 212 */     yytable();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */     yycheck(); }
/*     */   
/* 247 */   static void yycheck() { yycheck = new short[] { 261, 0, 266, 0, 259, 260, 264, 268, 262, 266, 261, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 259, 260, 259, 260 }; }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 279 */   static final String[] yyname = { "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "'.'", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "'['", null, "']'", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "T_INVALID_CHAR", "\"End of file\"", "T_DOT", "T_LBRACKET", "T_RBRACKET", "T_IDX", "\"Index Special Identifier\"", "T_PK", "\"PK Special Identifier\"", "T_IDENTIFIER", "\"Identifier\"", "T_NUMBER", "\"Number\"" };
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
/* 301 */   static final String[] yyrule = { "$accept : top", "top : sourceName", "sourceName : cbname", "sourceName : T_IDENTIFIER T_PK", "sourceName : cbname T_LBRACKET T_RBRACKET T_IDX", "cbname : T_IDENTIFIER", "cbname : cbname T_LBRACKET T_RBRACKET", "cbname : cbname T_LBRACKET T_NUMBER T_RBRACKET", "cbname : cbname T_DOT T_IDENTIFIER" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final Lexer lexer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CBName m_rootNode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int yyn;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int yym;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int yystate;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String yys;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Parser(String sourceName)
/*     */   {
/* 343 */     this.lexer = new Lexer(new StringReader(sourceName), this);
/* 344 */     this.m_rootNode = null;
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
/*     */   private int yylex()
/*     */   {
/* 358 */     int yyl_return = -1;
/*     */     try
/*     */     {
/* 361 */       this.yylval = new ParserVal();
/* 362 */       yyl_return = this.lexer.yylex();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 366 */       yyl_return = -1;
/*     */     }
/*     */     catch (Error err)
/*     */     {
/* 370 */       yyl_return = -1;
/*     */     }
/*     */     
/* 373 */     return yyl_return;
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
/*     */   public void yyerror(String error)
/*     */     throws ErrorException
/*     */   {
/* 387 */     ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.SOURCENAME_PARSE_ERROR.name(), new String[0]);
/*     */     
/*     */ 
/* 390 */     throw err;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CBName parse(String sourceName)
/*     */     throws ErrorException
/*     */   {
/* 398 */     return new Parser(sourceName).doParse();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CBName doParse()
/*     */     throws ErrorException
/*     */   {
/* 409 */     yyparse();
/* 410 */     return this.m_rootNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void notImplemented()
/*     */     throws SQLEngineException
/*     */   {
/* 418 */     throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.SYNTAX_NOT_SUPPORTED.name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void yylexdebug(int state, int ch)
/*     */   {
/* 428 */     String s = null;
/* 429 */     if (ch < 0) ch = 0;
/* 430 */     if (ch <= 269)
/* 431 */       s = yyname[ch];
/* 432 */     if (s == null)
/* 433 */       s = "illegal-symbol";
/* 434 */     debug("state " + state + ", reading " + ch + " (" + s + ")");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   int yyparse()
/*     */     throws ErrorException
/*     */   {
/* 455 */     init_stacks();
/* 456 */     this.yynerrs = 0;
/* 457 */     this.yyerrflag = 0;
/* 458 */     this.yychar = -1;
/* 459 */     this.yystate = 0;
/* 460 */     state_push(this.yystate);
/* 461 */     val_push(this.yylval);
/*     */     for (;;)
/*     */     {
/* 464 */       boolean doaction = true;
/* 465 */       if (this.yydebug) { debug("loop");
/*     */       }
/* 467 */       for (this.yyn = yydefred[this.yystate]; this.yyn == 0; this.yyn = yydefred[this.yystate])
/*     */       {
/* 469 */         if (this.yydebug) debug("yyn:" + this.yyn + "  state:" + this.yystate + "  yychar:" + this.yychar);
/* 470 */         if (this.yychar < 0)
/*     */         {
/* 472 */           this.yychar = yylex();
/* 473 */           if (this.yydebug) { debug(" next yychar:" + this.yychar);
/*     */           }
/* 475 */           if (this.yychar < 0)
/*     */           {
/* 477 */             this.yychar = 0;
/* 478 */             if (this.yydebug)
/* 479 */               yylexdebug(this.yystate, this.yychar);
/*     */           }
/*     */         }
/* 482 */         this.yyn = yysindex[this.yystate];
/* 483 */         if ((this.yyn != 0) && (this.yyn += this.yychar >= 0) && (this.yyn <= 263) && (yycheck[this.yyn] == this.yychar))
/*     */         {
/*     */ 
/* 486 */           if (this.yydebug) {
/* 487 */             debug("state " + this.yystate + ", shifting to state " + yytable[this.yyn]);
/*     */           }
/* 489 */           this.yystate = yytable[this.yyn];
/* 490 */           state_push(this.yystate);
/* 491 */           val_push(this.yylval);
/* 492 */           this.yychar = -1;
/* 493 */           if (this.yyerrflag > 0)
/* 494 */             this.yyerrflag -= 1;
/* 495 */           doaction = false;
/* 496 */           break;
/*     */         }
/*     */         
/* 499 */         this.yyn = yyrindex[this.yystate];
/* 500 */         if ((this.yyn != 0) && (this.yyn += this.yychar >= 0) && (this.yyn <= 263) && (yycheck[this.yyn] == this.yychar))
/*     */         {
/*     */ 
/* 503 */           if (this.yydebug) debug("reduce");
/* 504 */           this.yyn = yytable[this.yyn];
/* 505 */           doaction = true;
/* 506 */           break;
/*     */         }
/*     */         
/*     */ 
/* 510 */         if (this.yyerrflag == 0)
/*     */         {
/* 512 */           yyerror("syntax error");
/* 513 */           this.yynerrs += 1;
/*     */         }
/* 515 */         if (this.yyerrflag < 3)
/*     */         {
/* 517 */           this.yyerrflag = 3;
/*     */           for (;;)
/*     */           {
/* 520 */             if (this.stateptr < 0)
/*     */             {
/* 522 */               yyerror("stack underflow. aborting...");
/* 523 */               return 1;
/*     */             }
/* 525 */             this.yyn = yysindex[state_peek(0)];
/* 526 */             if ((this.yyn != 0) && (this.yyn += 256 >= 0) && (this.yyn <= 263) && (yycheck[this.yyn] == 256))
/*     */             {
/*     */ 
/* 529 */               if (this.yydebug)
/* 530 */                 debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[this.yyn] + " ");
/* 531 */               this.yystate = yytable[this.yyn];
/* 532 */               state_push(this.yystate);
/* 533 */               val_push(this.yylval);
/* 534 */               doaction = false;
/* 535 */               break;
/*     */             }
/*     */             
/*     */ 
/* 539 */             if (this.yydebug)
/* 540 */               debug("error recovery discarding state " + state_peek(0) + " ");
/* 541 */             if (this.stateptr < 0)
/*     */             {
/* 543 */               yyerror("Stack underflow. aborting...");
/* 544 */               return 1;
/*     */             }
/* 546 */             state_pop();
/* 547 */             val_pop();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 553 */         if (this.yychar == 0)
/* 554 */           return 1;
/* 555 */         if (this.yydebug)
/*     */         {
/* 557 */           this.yys = null;
/* 558 */           if (this.yychar <= 269) this.yys = yyname[this.yychar];
/* 559 */           if (this.yys == null) this.yys = "illegal-symbol";
/* 560 */           debug("state " + this.yystate + ", error recovery discards token " + this.yychar + " (" + this.yys + ")");
/*     */         }
/* 562 */         this.yychar = -1;
/*     */       }
/*     */       
/*     */ 
/* 566 */       if (doaction)
/*     */       {
/* 568 */         this.yym = yylen[this.yyn];
/* 569 */         if (this.yydebug)
/* 570 */           debug("state " + this.yystate + ", reducing " + this.yym + " by rule " + this.yyn + " (" + yyrule[this.yyn] + ")");
/* 571 */         if (this.yym > 0)
/* 572 */           this.yyval = val_peek(this.yym - 1);
/* 573 */         this.yyval = dup_yyval(this.yyval);
/* 574 */         switch (this.yyn)
/*     */         {
/*     */ 
/*     */ 
/*     */         case 1: 
/* 579 */           this.m_rootNode = ((CBName)val_peek(0).obj);
/* 580 */           break;
/*     */         
/*     */         case 2: 
/* 583 */           this.yyval.obj = val_peek(0).obj;
/* 584 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case 3: 
/* 589 */           this.yyval.obj = new CBSpecialIdentifier("$PK", new CBAttribute(val_peek(1).sval));
/*     */           
/* 591 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case 4: 
/* 596 */           this.yyval.obj = new CBSpecialIdentifier("$IDX", new CBArrayDimension((CBName)val_peek(3).obj));
/*     */           
/* 598 */           break;
/*     */         
/*     */         case 5: 
/* 601 */           this.yyval.obj = new CBAttribute(val_peek(0).sval);
/* 602 */           break;
/*     */         
/*     */         case 6: 
/* 605 */           this.yyval.obj = new CBArrayDimension((CBName)val_peek(2).obj);
/* 606 */           break;
/*     */         
/*     */         case 7: 
/* 609 */           this.yyval.obj = new CBArrayIndexedElement(val_peek(1).ival, (CBName)val_peek(3).obj);
/* 610 */           break;
/*     */         
/*     */         case 8: 
/* 613 */           this.yyval.obj = new CBAttribute(val_peek(0).sval, (CBName)val_peek(2).obj);
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/* 619 */         if (this.yydebug) debug("reduce");
/* 620 */         state_drop(this.yym);
/* 621 */         this.yystate = state_peek(0);
/* 622 */         val_drop(this.yym);
/* 623 */         this.yym = yylhs[this.yyn];
/* 624 */         if ((this.yystate == 0) && (this.yym == 0))
/*     */         {
/* 626 */           if (this.yydebug) debug("After reduction, shifting from state 0 to state 2");
/* 627 */           this.yystate = 2;
/* 628 */           state_push(2);
/* 629 */           val_push(this.yyval);
/* 630 */           if (this.yychar < 0)
/*     */           {
/* 632 */             this.yychar = yylex();
/* 633 */             if (this.yychar < 0) this.yychar = 0;
/* 634 */             if (this.yydebug)
/* 635 */               yylexdebug(this.yystate, this.yychar);
/*     */           }
/* 637 */           if (this.yychar == 0) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         else {
/* 642 */           this.yyn = yygindex[this.yym];
/* 643 */           if ((this.yyn != 0) && (this.yyn += this.yystate >= 0) && (this.yyn <= 263) && (yycheck[this.yyn] == this.yystate))
/*     */           {
/* 645 */             this.yystate = yytable[this.yyn];
/*     */           } else
/* 647 */             this.yystate = yydgoto[this.yym];
/* 648 */           if (this.yydebug) debug("after reduction, shifting from state " + state_peek(0) + " to state " + this.yystate + "");
/* 649 */           state_push(this.yystate);
/* 650 */           val_push(this.yyval);
/*     */         }
/*     */       } }
/* 653 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/schemamap/parser/generated/Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */