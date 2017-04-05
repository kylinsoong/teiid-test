/*     */ package com.simba.commons.codec.language;
/*     */ 
/*     */ import com.simba.commons.codec.EncoderException;
/*     */ import com.simba.commons.codec.StringEncoder;
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
/*     */ 
/*     */ public class Metaphone
/*     */   implements StringEncoder
/*     */ {
/*  41 */   private String vowels = "AEIOU";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private String frontv = "EIY";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private String varson = "CSPTG";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   private int maxCodeLen = 4;
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
/*     */   public String metaphone(String txt)
/*     */   {
/*  76 */     boolean hard = false;
/*  77 */     if ((txt == null) || (txt.length() == 0)) {
/*  78 */       return "";
/*     */     }
/*     */     
/*  81 */     if (txt.length() == 1) {
/*  82 */       return txt.toUpperCase();
/*     */     }
/*     */     
/*  85 */     char[] inwd = txt.toUpperCase().toCharArray();
/*     */     
/*  87 */     StringBuffer local = new StringBuffer(40);
/*  88 */     StringBuffer code = new StringBuffer(10);
/*     */     
/*  90 */     switch (inwd[0]) {
/*     */     case 'G': 
/*     */     case 'K': 
/*     */     case 'P': 
/*  94 */       if (inwd[1] == 'N') {
/*  95 */         local.append(inwd, 1, inwd.length - 1);
/*     */       } else {
/*  97 */         local.append(inwd);
/*     */       }
/*  99 */       break;
/*     */     case 'A': 
/* 101 */       if (inwd[1] == 'E') {
/* 102 */         local.append(inwd, 1, inwd.length - 1);
/*     */       } else {
/* 104 */         local.append(inwd);
/*     */       }
/* 106 */       break;
/*     */     case 'W': 
/* 108 */       if (inwd[1] == 'R') {
/* 109 */         local.append(inwd, 1, inwd.length - 1);
/*     */ 
/*     */       }
/* 112 */       else if (inwd[1] == 'H') {
/* 113 */         local.append(inwd, 1, inwd.length - 1);
/* 114 */         local.setCharAt(0, 'W');
/*     */       } else {
/* 116 */         local.append(inwd);
/*     */       }
/* 118 */       break;
/*     */     case 'X': 
/* 120 */       inwd[0] = 'S';
/* 121 */       local.append(inwd);
/* 122 */       break;
/*     */     default: 
/* 124 */       local.append(inwd);
/*     */     }
/*     */     
/* 127 */     int wdsz = local.length();
/* 128 */     int n = 0;
/*     */     
/* 130 */     while ((code.length() < getMaxCodeLen()) && (n < wdsz))
/*     */     {
/* 132 */       char symb = local.charAt(n);
/*     */       
/* 134 */       if ((symb != 'C') && (isPreviousChar(local, n, symb))) {
/* 135 */         n++;
/*     */       } else {
/* 137 */         switch (symb) {
/*     */         case 'A': case 'E': case 'I': case 'O': case 'U': 
/* 139 */           if (n == 0) {
/* 140 */             code.append(symb);
/*     */           }
/*     */           break;
/*     */         case 'B': 
/* 144 */           if ((!isPreviousChar(local, n, 'M')) || (!isLastChar(wdsz, n)))
/*     */           {
/*     */ 
/*     */ 
/* 148 */             code.append(symb); }
/* 149 */           break;
/*     */         
/*     */         case 'C': 
/* 152 */           if ((!isPreviousChar(local, n, 'S')) || (isLastChar(wdsz, n)) || (this.frontv.indexOf(local.charAt(n + 1)) < 0))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 157 */             if (regionMatch(local, n, "CIA")) {
/* 158 */               code.append('X');
/*     */ 
/*     */             }
/* 161 */             else if ((!isLastChar(wdsz, n)) && (this.frontv.indexOf(local.charAt(n + 1)) >= 0))
/*     */             {
/* 163 */               code.append('S');
/*     */ 
/*     */             }
/* 166 */             else if ((isPreviousChar(local, n, 'S')) && (isNextChar(local, n, 'H')))
/*     */             {
/* 168 */               code.append('K');
/*     */ 
/*     */             }
/* 171 */             else if (isNextChar(local, n, 'H')) {
/* 172 */               if ((n == 0) && (wdsz >= 3) && (isVowel(local, 2)))
/*     */               {
/*     */ 
/* 175 */                 code.append('K');
/*     */               } else {
/* 177 */                 code.append('X');
/*     */               }
/*     */             } else
/* 180 */               code.append('K');
/*     */           }
/* 182 */           break;
/*     */         case 'D': 
/* 184 */           if ((!isLastChar(wdsz, n + 1)) && (isNextChar(local, n, 'G')) && (this.frontv.indexOf(local.charAt(n + 2)) >= 0))
/*     */           {
/*     */ 
/* 187 */             code.append('J');n += 2;
/*     */           } else {
/* 189 */             code.append('T');
/*     */           }
/* 191 */           break;
/*     */         case 'G': 
/* 193 */           if ((!isLastChar(wdsz, n + 1)) || (!isNextChar(local, n, 'H')))
/*     */           {
/*     */ 
/*     */ 
/* 197 */             if ((isLastChar(wdsz, n + 1)) || (!isNextChar(local, n, 'H')) || (isVowel(local, n + 2)))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 202 */               if ((n <= 0) || ((!regionMatch(local, n, "GN")) && (!regionMatch(local, n, "GNED"))))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 207 */                 if (isPreviousChar(local, n, 'G')) {
/* 208 */                   hard = true;
/*     */                 } else {
/* 210 */                   hard = false;
/*     */                 }
/* 212 */                 if ((!isLastChar(wdsz, n)) && (this.frontv.indexOf(local.charAt(n + 1)) >= 0) && (!hard))
/*     */                 {
/*     */ 
/* 215 */                   code.append('J');
/*     */                 } else
/* 217 */                   code.append('K');
/*     */               } } }
/* 219 */           break;
/*     */         case 'H': 
/* 221 */           if (!isLastChar(wdsz, n))
/*     */           {
/*     */ 
/* 224 */             if ((n <= 0) || (this.varson.indexOf(local.charAt(n - 1)) < 0))
/*     */             {
/*     */ 
/*     */ 
/* 228 */               if (isVowel(local, n + 1)) {
/* 229 */                 code.append('H');
/*     */               }
/*     */             }
/*     */           }
/*     */           break;
/*     */         case 'F': case 'J': 
/*     */         case 'L': case 'M': 
/*     */         case 'N': 
/*     */         case 'R': 
/* 238 */           code.append(symb);
/* 239 */           break;
/*     */         case 'K': 
/* 241 */           if (n > 0) {
/* 242 */             if (!isPreviousChar(local, n, 'C')) {
/* 243 */               code.append(symb);
/*     */             }
/*     */           } else {
/* 246 */             code.append(symb);
/*     */           }
/* 248 */           break;
/*     */         case 'P': 
/* 250 */           if (isNextChar(local, n, 'H'))
/*     */           {
/* 252 */             code.append('F');
/*     */           } else {
/* 254 */             code.append(symb);
/*     */           }
/* 256 */           break;
/*     */         case 'Q': 
/* 258 */           code.append('K');
/* 259 */           break;
/*     */         case 'S': 
/* 261 */           if ((regionMatch(local, n, "SH")) || (regionMatch(local, n, "SIO")) || (regionMatch(local, n, "SIA")))
/*     */           {
/*     */ 
/* 264 */             code.append('X');
/*     */           } else {
/* 266 */             code.append('S');
/*     */           }
/* 268 */           break;
/*     */         case 'T': 
/* 270 */           if ((regionMatch(local, n, "TIA")) || (regionMatch(local, n, "TIO")))
/*     */           {
/* 272 */             code.append('X');
/*     */ 
/*     */           }
/* 275 */           else if (!regionMatch(local, n, "TCH"))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 280 */             if (regionMatch(local, n, "TH")) {
/* 281 */               code.append('0');
/*     */             } else
/* 283 */               code.append('T');
/*     */           }
/* 285 */           break;
/*     */         case 'V': 
/* 287 */           code.append('F'); break;
/*     */         case 'W': case 'Y': 
/* 289 */           if ((!isLastChar(wdsz, n)) && (isVowel(local, n + 1)))
/*     */           {
/* 291 */             code.append(symb);
/*     */           }
/*     */           break;
/*     */         case 'X': 
/* 295 */           code.append('K');code.append('S');
/* 296 */           break;
/*     */         case 'Z': 
/* 298 */           code.append('S');
/*     */         }
/* 300 */         n++;
/*     */       }
/* 302 */       if (code.length() > getMaxCodeLen()) {
/* 303 */         code.setLength(getMaxCodeLen());
/*     */       }
/*     */     }
/* 306 */     return code.toString();
/*     */   }
/*     */   
/*     */   private boolean isVowel(StringBuffer string, int index) {
/* 310 */     return this.vowels.indexOf(string.charAt(index)) >= 0;
/*     */   }
/*     */   
/*     */   private boolean isPreviousChar(StringBuffer string, int index, char c) {
/* 314 */     boolean matches = false;
/* 315 */     if ((index > 0) && (index < string.length()))
/*     */     {
/* 317 */       matches = string.charAt(index - 1) == c;
/*     */     }
/* 319 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean isNextChar(StringBuffer string, int index, char c) {
/* 323 */     boolean matches = false;
/* 324 */     if ((index >= 0) && (index < string.length() - 1))
/*     */     {
/* 326 */       matches = string.charAt(index + 1) == c;
/*     */     }
/* 328 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean regionMatch(StringBuffer string, int index, String test) {
/* 332 */     boolean matches = false;
/* 333 */     if ((index >= 0) && (index + test.length() - 1 < string.length()))
/*     */     {
/* 335 */       String substring = string.substring(index, index + test.length());
/* 336 */       matches = substring.equals(test);
/*     */     }
/* 338 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean isLastChar(int wdsz, int n) {
/* 342 */     return n + 1 == wdsz;
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
/*     */   public Object encode(Object pObject)
/*     */     throws EncoderException
/*     */   {
/* 359 */     if (!(pObject instanceof String)) {
/* 360 */       throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
/*     */     }
/* 362 */     return metaphone((String)pObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encode(String pString)
/*     */   {
/* 372 */     return metaphone(pString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMetaphoneEqual(String str1, String str2)
/*     */   {
/* 384 */     return metaphone(str1).equals(metaphone(str2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxCodeLen()
/*     */   {
/* 391 */     return this.maxCodeLen;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxCodeLen(int maxCodeLen)
/*     */   {
/* 397 */     this.maxCodeLen = maxCodeLen;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/language/Metaphone.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */