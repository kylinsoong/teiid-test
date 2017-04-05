/*     */ package com.simba.jdbc.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ParseQueryUtils
/*     */ {
/*     */   public static List<String> splitQueries(String query, boolean isDollarQuoted)
/*     */     throws ParsingException
/*     */   {
/*  35 */     List<String> results = new ArrayList();
/*     */     
/*  37 */     int startingIndex = 0;
/*  38 */     int i = 0;
/*  39 */     String subQuery = null;
/*  40 */     StringBuilder subQueryBuilder = new StringBuilder();
/*     */     
/*  42 */     for (i = 0; (i < query.length()) && (0 <= query.indexOf(';', i)); i++)
/*     */     {
/*  44 */       switch (query.charAt(i))
/*     */       {
/*     */ 
/*     */ 
/*     */       case ';': 
/*  49 */         subQueryBuilder.append(query.substring(startingIndex, i));
/*  50 */         if (0 < subQueryBuilder.length())
/*     */         {
/*  52 */           subQuery = subQueryBuilder.toString().trim();
/*  53 */           if ((null != subQuery) && (subQuery.length() > 0))
/*     */           {
/*  55 */             results.add(subQuery);
/*     */           }
/*     */         }
/*  58 */         subQueryBuilder = new StringBuilder();
/*  59 */         startingIndex = i + 1;
/*  60 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case '"': 
/*  66 */         if ((i <= 0) || ('\\' != query.charAt(i - 1)))
/*     */         {
/*  68 */           i = query.indexOf('"', i + 1);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         break;
/*     */       case '\'': 
/*  76 */         i = query.indexOf('\'', i + 1);
/*     */       case '-': case '#': case '/': case '$': 
/*  78 */         while (((i < query.length() - 1) && ('\'' == query.charAt(i + 1))) || ((i > 0) && ('\\' == query.charAt(i - 1))))
/*     */         {
/*     */ 
/*  81 */           i = query.indexOf('\'', i + 1); continue;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */           if (query.charAt(i + 1) == '-')
/*     */           {
/*  91 */             int j = query.indexOf('\n', i + 2);
/*     */             
/*  93 */             subQueryBuilder.append(query.substring(startingIndex, i));
/*  94 */             i = j;
/*  95 */             startingIndex = i + 1;
/*  96 */             break;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */             int j = query.indexOf('\n', i + 1);
/*     */             
/* 104 */             subQueryBuilder.append(query.substring(startingIndex, i));
/* 105 */             i = j;
/* 106 */             startingIndex = i + 1;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 111 */             if (query.charAt(i + 1) == '*')
/*     */             {
/* 113 */               int j = query.indexOf("*/", i + 2);
/* 114 */               if (-1 != j)
/*     */               {
/* 116 */                 subQueryBuilder.append(query.substring(startingIndex, i));
/* 117 */                 i = j;
/* 118 */                 startingIndex = i + 2;
/*     */               }
/* 120 */               break;
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 125 */               if (isDollarQuoted)
/*     */               {
/* 127 */                 if ((i + 1 < query.length()) && (query.charAt(i + 1) == '$'))
/*     */                 {
/* 129 */                   int j = query.indexOf("$$", i + 2);
/* 130 */                   if (j < 0)
/*     */                   {
/*     */ 
/*     */ 
/* 134 */                     i = query.length();
/*     */ 
/*     */                   }
/*     */                   else
/*     */                   {
/* 139 */                     i = j + 1;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 148 */       if (i < 0)
/*     */       {
/* 150 */         throw new ParsingException();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 155 */     if (startingIndex != query.length())
/*     */     {
/* 157 */       subQuery = query.substring(startingIndex, query.length());
/* 158 */       subQuery = parseCommentsOnSingleQuery(subQuery).trim();
/* 159 */       if ((null != subQuery) && (subQuery.length() > 0))
/*     */       {
/* 161 */         results.add(subQuery.trim());
/*     */       }
/*     */     }
/*     */     
/* 165 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> splitQueries(String query)
/*     */     throws ParsingException
/*     */   {
/* 177 */     return splitQueries(query, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String removeComments(String query)
/*     */   {
/* 189 */     String result = query;
/*     */     
/* 191 */     if (query.contains("/*"))
/*     */     {
/* 193 */       StringBuilder sb = new StringBuilder();
/* 194 */       int commentStart = query.indexOf("/*");
/* 195 */       int commentStop = query.indexOf("*/");
/* 196 */       sb.append(query.substring(0, commentStart));
/* 197 */       if (-1 != commentStop)
/*     */       {
/* 199 */         sb.append(query.substring(commentStop, query.length()));
/*     */       }
/*     */       
/* 202 */       result = sb.toString().trim();
/*     */     }
/* 204 */     else if (query.contains("--"))
/*     */     {
/* 206 */       StringBuilder sb = new StringBuilder();
/* 207 */       int commentStart = query.indexOf("--");
/* 208 */       int commentStop = query.indexOf("\n");
/*     */       
/* 210 */       sb.append(query.substring(0, commentStart));
/* 211 */       if (-1 != commentStop)
/*     */       {
/* 213 */         sb.append(query.substring(commentStop + 1, query.length()));
/*     */       }
/*     */       
/* 216 */       result = sb.toString().trim();
/*     */     }
/* 218 */     else if (query.contains("#"))
/*     */     {
/* 220 */       StringBuilder sb = new StringBuilder();
/* 221 */       int commentStart = query.indexOf("#");
/* 222 */       int commentStop = query.indexOf("\n");
/* 223 */       sb.append(query.substring(0, commentStart));
/* 224 */       if (-1 != commentStop)
/*     */       {
/* 226 */         sb.append(query.substring(commentStop + 1, query.length()));
/*     */       }
/*     */       
/* 229 */       result = sb.toString().trim();
/*     */     }
/*     */     
/* 232 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String parseCommentsOnSingleQuery(String sql)
/*     */   {
/* 243 */     StringBuilder result = new StringBuilder();
/*     */     
/* 245 */     boolean inSingle = false;
/* 246 */     boolean inDouble = false;
/* 247 */     int queryLength = sql.length();
/* 248 */     for (int i = 0; i < queryLength; i++)
/*     */     {
/* 250 */       char cur = sql.charAt(i);
/* 251 */       switch (cur)
/*     */       {
/*     */ 
/*     */       case '\'': 
/* 255 */         int indexOfNextSingleQuote = sql.indexOf('\'', i + 1);
/* 256 */         int indexOfNextOpenBrace = sql.indexOf('(', i + 1);
/* 257 */         if ((!inDouble) && (!inSingle))
/*     */         {
/* 259 */           if (((0 < indexOfNextOpenBrace) && (0 < indexOfNextSingleQuote) && (indexOfNextOpenBrace > indexOfNextSingleQuote)) || ((0 > indexOfNextOpenBrace) && (0 <= indexOfNextSingleQuote)))
/*     */           {
/*     */ 
/*     */ 
/* 263 */             inSingle = true;
/*     */           }
/*     */           else {
/* 266 */             inSingle = false;
/*     */           }
/*     */         }
/*     */         else {
/* 270 */           inSingle = false;
/*     */         }
/* 272 */         result.append(cur);
/* 273 */         break;
/*     */       
/*     */ 
/*     */       case '"': 
/* 277 */         int indexOfNextQuote = sql.indexOf('"', i + 1);
/* 278 */         int indexOfNextOpenBrace = sql.indexOf('(', i + 1);
/* 279 */         if ((!inDouble) && (!inSingle))
/*     */         {
/* 281 */           if (((0 < indexOfNextOpenBrace) && (0 < indexOfNextQuote) && (indexOfNextOpenBrace > indexOfNextQuote)) || ((0 > indexOfNextOpenBrace) && (0 <= indexOfNextQuote)))
/*     */           {
/*     */ 
/* 284 */             inDouble = true;
/*     */           }
/*     */           else
/*     */           {
/* 288 */             inDouble = false;
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 293 */           inDouble = false;
/*     */         }
/* 295 */         result.append(cur);
/* 296 */         break;
/*     */       
/*     */ 
/*     */       case '/': 
/* 300 */         if ((!inSingle) && (!inDouble) && (i + 1 < queryLength))
/*     */         {
/* 302 */           if ('*' == sql.charAt(i + 1))
/*     */           {
/* 304 */             int end = sql.indexOf("*/", i);
/* 305 */             if (end <= 0)
/*     */             {
/* 307 */               return sql;
/*     */             }
/*     */             
/*     */ 
/* 311 */             i = end + 1;
/* 312 */             continue;
/*     */           }
/*     */         }
/*     */         
/* 316 */         result.append(cur);
/* 317 */         break;
/*     */       
/*     */ 
/*     */       case '-': 
/* 321 */         if ((!inSingle) && (!inDouble) && (i + 1 < queryLength))
/*     */         {
/* 323 */           if ('-' == sql.charAt(i + 1))
/*     */           {
/* 325 */             int ret = sql.indexOf("\n", i);
/* 326 */             if (ret == -1)
/*     */             {
/* 328 */               i = queryLength; continue;
/*     */             }
/*     */             
/*     */ 
/* 332 */             i = ret;
/*     */             
/* 334 */             continue;
/*     */           }
/*     */         }
/* 337 */         result.append(cur);
/* 338 */         break;
/*     */       
/*     */ 
/*     */       case '#': 
/* 342 */         if ((!inSingle) && (!inDouble))
/*     */         {
/* 344 */           int ret = sql.indexOf("\n", i);
/*     */           
/* 346 */           if (ret == -1)
/*     */           {
/* 348 */             i = queryLength;
/*     */           }
/*     */           else
/*     */           {
/* 352 */             i = ret;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 357 */           result.append(cur); }
/* 358 */         break;
/*     */       case '$': case '%': case '&': case '(': 
/*     */       case ')': case '*': case '+': 
/*     */       case ',': case '.': default: 
/* 362 */         result.append(cur);
/*     */       }
/*     */       
/*     */     }
/* 366 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/utils/ParseQueryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */