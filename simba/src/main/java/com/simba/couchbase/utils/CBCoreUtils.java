/*     */ package com.simba.couchbase.utils;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeType;
/*     */ import com.simba.couchbase.dataengine.CBParamInfo;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.dsi.dataengine.utilities.DataWrapper;
/*     */ import com.simba.dsi.exceptions.IncorrectTypeException;
/*     */ import com.simba.jdbc.common.CommonCoreUtils;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBCoreUtils
/*     */   extends CommonCoreUtils
/*     */ {
/*     */   private static final String DESCRIBE_CASE = "DESCRIBE";
/*     */   private static final String EXPLAIN_CASE = "EXPLAIN";
/*     */   private static final String SELECT_CASE = "SELECT";
/*     */   private static final String FROM_CASE = "FROM";
/*     */   private static final String SHOW_CASE = "SHOW";
/*     */   private static final String VALUES_UCASE = "VALUES";
/*     */   private static final String SET_CASE = "SET";
/*     */   private static final String WITH_CASE = "WITH";
/*     */   private static final char SQL_QUERY_PARAMETER_SIGN = '?';
/*     */   
/*     */   private static void parseNativeQueryParameters(String inputQuery, char parameterFormat, int currentStrPointer, int querySize, ArrayList<CBParamInfo> outParamInfoList)
/*     */   {
/* 102 */     LinkedHashSet<String> parameters = new LinkedHashSet();
/* 103 */     int posParamIndex = 1;
/*     */     
/* 105 */     while (currentStrPointer < querySize)
/*     */     {
/* 107 */       if (inputQuery.charAt(currentStrPointer) == parameterFormat)
/*     */       {
/* 109 */         parameters.add("$" + posParamIndex);
/* 110 */         posParamIndex++;
/* 111 */         currentStrPointer++;
/*     */       }
/* 113 */       else if (inputQuery.charAt(currentStrPointer) == '$')
/*     */       {
/* 115 */         String namedParamVar = new String();
/* 116 */         String subString = inputQuery.substring(currentStrPointer);
/* 117 */         if (subString.contains(" "))
/*     */         {
/* 119 */           namedParamVar = subString.substring(0, subString.indexOf(" "));
/* 120 */           currentStrPointer += subString.indexOf(" ");
/*     */         }
/*     */         else
/*     */         {
/* 124 */           namedParamVar = subString.substring(0);
/* 125 */           currentStrPointer += subString.length();
/*     */         }
/*     */         
/* 128 */         if (namedParamVar.length() > 1)
/*     */         {
/*     */           try
/*     */           {
/*     */ 
/* 133 */             Integer.parseInt(namedParamVar.substring(1));
/* 134 */             parameters.add(namedParamVar.toString());
/*     */ 
/*     */           }
/*     */           catch (NumberFormatException ex)
/*     */           {
/* 139 */             parameters.add(namedParamVar);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 145 */         currentStrPointer++;
/*     */       }
/*     */     }
/*     */     
/* 149 */     for (String currParam : parameters)
/*     */     {
/* 151 */       CBParamInfo paramInfo = new CBParamInfo();
/*     */       try
/*     */       {
/* 154 */         int posIndex = Integer.parseInt(currParam.substring(1));
/* 155 */         paramInfo.setPositional(true);
/* 156 */         paramInfo.setPosition(posIndex);
/*     */ 
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 161 */         paramInfo.setPositional(false);
/* 162 */         paramInfo.setName(currParam);
/*     */       }
/*     */       
/* 165 */       outParamInfoList.add(paramInfo);
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
/*     */   public static boolean isRowCountResult(String query)
/*     */   {
/* 179 */     boolean isRowCountQuery = true;
/* 180 */     String compare_query = query.trim().toUpperCase();
/* 181 */     if (compare_query.startsWith("WITH"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */       compare_query = removeWithExpression(compare_query);
/*     */     }
/*     */     
/* 191 */     if ((compare_query.startsWith("SELECT")) || (compare_query.startsWith("FROM")) || (compare_query.startsWith("DESCRIBE")) || (compare_query.startsWith("EXPLAIN")) || (compare_query.startsWith("SHOW")) || (compare_query.startsWith("VALUES")) || (compare_query.startsWith("SET")))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */       isRowCountQuery = false;
/*     */     }
/*     */     
/* 202 */     return isRowCountQuery;
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
/*     */   public static String removeWithExpression(String statement)
/*     */   {
/* 215 */     if (!statement.startsWith("WITH")) {
/* 216 */       return statement;
/*     */     }
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
/* 240 */     statement = statement.substring("WITH".length());
/*     */     
/* 242 */     int parenCount = 0;
/* 243 */     ParseState state = ParseState.INITIAL;
/* 244 */     char closingQuote = '\000';
/*     */     
/* 246 */     for (int i = 0; i < statement.length(); i++)
/*     */     {
/* 248 */       switch (state)
/*     */       {
/*     */ 
/*     */       case INITIAL: 
/* 252 */         char c = statement.charAt(i);
/* 253 */         switch (c)
/*     */         {
/*     */ 
/*     */         case '(': 
/* 257 */           parenCount++;
/* 258 */           break;
/*     */         
/*     */ 
/*     */         case ')': 
/* 262 */           parenCount--;
/* 263 */           if (0 == parenCount) {
/* 264 */             state = ParseState.AFTER_SUBQ;
/*     */           }
/*     */           
/*     */           break;
/*     */         case '"': 
/*     */         case '\'': 
/*     */         case '`': 
/* 271 */           closingQuote = c;
/* 272 */           state = ParseState.QUOTE;
/* 273 */           break;
/*     */         
/*     */ 
/*     */         case '[': 
/* 277 */           closingQuote = ']';
/* 278 */           state = ParseState.QUOTE;
/*     */         }
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 286 */         break;
/*     */       
/*     */ 
/*     */       case QUOTE: 
/*     */       case AFTER_SUBQ: 
/* 291 */         while (i < statement.length())
/*     */         {
/* 293 */           char c = statement.charAt(i);
/* 294 */           if ('\\' == c)
/*     */           {
/* 296 */             i++;
/*     */           }
/* 298 */           else if (closingQuote == c)
/*     */           {
/* 300 */             state = ParseState.INITIAL;
/* 301 */             break;
/*     */           }
/* 291 */           i++; continue;
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
/* 309 */           char c = statement.charAt(i);
/* 310 */           if (',' == c)
/*     */           {
/* 312 */             state = ParseState.INITIAL;
/*     */           }
/* 314 */           else if (Character.isWhitespace(c))
/*     */           {
/* 316 */             state = ParseState.AFTER_SUBQ;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 321 */             return statement.substring(i);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 329 */     return "";
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
/*     */   public static int parameterCounter(String inputQuery, ArrayList<CBParamInfo> outParamInfoList)
/*     */   {
/* 345 */     int currentStrPointer = 0;
/* 346 */     int querySize = inputQuery.length();
/*     */     
/* 348 */     parseNativeQueryParameters(inputQuery, '?', currentStrPointer, querySize, outParamInfoList);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 355 */     return outParamInfoList.size();
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
/*     */   public static String getParameterValue(DataWrapper inputData)
/*     */     throws ErrorException
/*     */   {
/* 371 */     String paramValue = null;
/*     */     try
/*     */     {
/* 374 */       switch (inputData.getType())
/*     */       {
/*     */ 
/*     */       case -7: 
/*     */       case 16: 
/* 379 */         if (null == inputData.getBoolean())
/*     */         {
/* 381 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 385 */           paramValue = inputData.getBoolean().toString();
/*     */         }
/* 387 */         break;
/*     */       
/*     */ 
/*     */       case 4: 
/* 391 */         if (null == inputData.getInteger())
/*     */         {
/* 393 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 397 */           paramValue = inputData.getInteger().toString();
/*     */         }
/* 399 */         break;
/*     */       
/*     */ 
/*     */       case 5: 
/* 403 */         if (null == inputData.getSmallInt())
/*     */         {
/* 405 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 409 */           paramValue = inputData.getSmallInt().toString();
/*     */         }
/* 411 */         break;
/*     */       
/*     */ 
/*     */       case -5: 
/* 415 */         if (null == inputData.getBigInt())
/*     */         {
/* 417 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 421 */           paramValue = inputData.getBigInt().toString();
/*     */         }
/* 423 */         break;
/*     */       
/*     */ 
/*     */       case 3: 
/* 427 */         if (null == inputData.getDecimal())
/*     */         {
/* 429 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 433 */           paramValue = inputData.getDecimal().toString();
/*     */         }
/* 435 */         break;
/*     */       
/*     */ 
/*     */       case 2: 
/* 439 */         if (null == inputData.getNumeric())
/*     */         {
/* 441 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 445 */           paramValue = inputData.getNumeric().toString();
/*     */         }
/* 447 */         break;
/*     */       
/*     */ 
/*     */       case 6: 
/* 451 */         if (null == inputData.getFloat())
/*     */         {
/* 453 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 457 */           paramValue = inputData.getFloat().toString();
/*     */         }
/* 459 */         break;
/*     */       
/*     */ 
/*     */       case 8: 
/* 463 */         if (null == inputData.getDouble())
/*     */         {
/* 465 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 469 */           paramValue = inputData.getDouble().toString();
/*     */         }
/* 471 */         break;
/*     */       
/*     */ 
/*     */       case 7: 
/* 475 */         if (null == inputData.getReal())
/*     */         {
/* 477 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 481 */           paramValue = inputData.getReal().toString();
/*     */         }
/* 483 */         break;
/*     */       
/*     */ 
/*     */       case -8: 
/*     */       case 1: 
/* 488 */         paramValue = inputData.getChar();
/* 489 */         break;
/*     */       
/*     */ 
/*     */       case -9: 
/*     */       case 12: 
/* 494 */         paramValue = inputData.getVarChar();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 500 */         if ((null != paramValue) && (!isValidArrayOrObjectJSON(paramValue)))
/*     */         {
/* 502 */           StringBuilder paramBuilder = new StringBuilder();
/* 503 */           paramValue = paramValue.replaceAll("[\"]", "\\\\$0");
/*     */           
/* 505 */           paramBuilder.append("'").append(paramValue).append("'");
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 510 */           paramValue = paramBuilder.toString(); }
/* 511 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 93: 
/* 516 */         if (null == inputData.getTimestamp())
/*     */         {
/* 518 */           paramValue = null;
/*     */         }
/*     */         else
/*     */         {
/* 522 */           StringBuilder paramBuilder = new StringBuilder();
/* 523 */           paramBuilder.append("'").append(inputData.getTimestamp().toString()).append("'");
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 528 */           paramValue = paramBuilder.toString();
/*     */         }
/* 530 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 535 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.PARAMETER_QUERY_DATA_TYPE_ERR_NON_SUPPORT_DATA_TYPE.name(), new String[] { "Parameter data type is not supported." });
/*     */         
/*     */ 
/* 538 */         throw err;
/*     */       }
/*     */       
/*     */       
/* 542 */       return paramValue;
/*     */     }
/*     */     catch (IncorrectTypeException ex)
/*     */     {
/* 546 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.PARAMETER_QUERY_DATA_TYPE_ERR_INCORRECT_DATA_TYPE.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 549 */       throw err;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum ParseState
/*     */   {
/* 559 */     INITIAL, 
/*     */     
/*     */ 
/* 562 */     QUOTE, 
/*     */     
/*     */ 
/* 565 */     AFTER_SUBQ;
/*     */     
/*     */ 
/*     */ 
/*     */     private ParseState() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isValidArrayOrObjectJSON(String inputString)
/*     */   {
/*     */     try
/*     */     {
/* 578 */       ObjectMapper mapper = new ObjectMapper();
/* 579 */       JsonNode rootNode = mapper.readTree(inputString);
/*     */       
/* 581 */       if ((rootNode.getNodeType() == JsonNodeType.ARRAY) || (rootNode.getNodeType() == JsonNodeType.OBJECT))
/*     */       {
/*     */ 
/* 584 */         return true;
/*     */       }
/*     */       
/* 587 */       return false;
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 591 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/utils/CBCoreUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */