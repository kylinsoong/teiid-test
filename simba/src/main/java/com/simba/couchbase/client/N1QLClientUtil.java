/*     */ package com.simba.couchbase.client;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.simba.couchbase.core.CBConnectionSettings;
/*     */ import com.simba.couchbase.exceptions.CBJDBCMessageKey;
/*     */ import com.simba.couchbase.utils.CBQueryUtils;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.net.ConnectException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.util.EntityUtils;
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
/*     */ public class N1QLClientUtil
/*     */ {
/*     */   private static final String CRED_USER_PASSWORD = "creds";
/*     */   private static final String SCAN_CONSISTENCY_PROP = "scan_consistency";
/*     */   
/*     */   public static void DISCONNECT(HttpClient httpClient)
/*     */   {
/*  65 */     httpClient.getConnectionManager().shutdown();
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
/*     */   public static HttpResponse GET(HttpClient httpClient, String host, int port, String requestType, String query, CBConnectionSettings settings, boolean setScanConsistency, ArrayList<String> posParams, HashMap<String, String> namedParams, ILogger log)
/*     */     throws ErrorException
/*     */   {
/*  96 */     LogUtilities.logFunctionEntrance(log, new Object[] { query, requestType });
/*     */     
/*     */     try
/*     */     {
/* 100 */       URI uri = new URI(URLBuilder(host, port, requestType, query, settings, setScanConsistency, posParams, namedParams));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */       URL url = uri.toURL();
/*     */       
/*     */ 
/* 112 */       HttpGet getRequest = new HttpGet(url.toString());
/*     */       
/* 114 */       getRequest.addHeader("accept", "application/json");
/*     */       
/*     */ 
/* 117 */       HttpResponse response = httpClient.execute(getRequest);
/*     */       
/*     */ 
/* 120 */       if (response.getStatusLine().getStatusCode() != 200)
/*     */       {
/* 122 */         LogUtilities.logFunctionEntrance(log, new Object[] { "Error Detected in during GET operation" });
/*     */       }
/*     */       
/* 125 */       return response;
/*     */     }
/*     */     catch (ConnectException ex)
/*     */     {
/* 129 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_FAIL_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 132 */       err.initCause(ex);
/* 133 */       throw err;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 137 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.N1QLCLIENT_GET_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 140 */       err.initCause(ex);
/* 141 */       throw err;
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
/*     */   public static HttpResponse POST(HttpClient httpClient, String host, int port, String requestType, String query, CBConnectionSettings settings, ArrayList<String> posParams, HashMap<String, String> namedParams, ILogger log)
/*     */     throws ErrorException
/*     */   {
/* 172 */     LogUtilities.logFunctionEntrance(log, new Object[] { query, requestType });
/*     */     
/*     */     try
/*     */     {
/* 176 */       URI uri = new URI(URLBuilder(host, port, requestType, query, settings, true, posParams, namedParams));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 185 */       URL url = uri.toURL();
/*     */       
/*     */ 
/* 188 */       HttpPost getRequest = new HttpPost(url.toString());
/*     */       
/* 190 */       getRequest.addHeader("accept", "application/json");
/*     */       
/*     */ 
/* 193 */       HttpResponse response = httpClient.execute(getRequest);
/*     */       
/*     */ 
/* 196 */       if (response.getStatusLine().getStatusCode() != 200)
/*     */       {
/* 198 */         LogUtilities.logFunctionEntrance(log, new Object[] { "Error Detected in during GET operation" });
/*     */       }
/*     */       
/* 201 */       return response;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 205 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.N1QLCLIENT_POST_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 208 */       err.initCause(ex);
/* 209 */       throw err;
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
/*     */   public static JsonNode toJsonNode(HttpResponse response)
/*     */     throws ErrorException
/*     */   {
/*     */     try
/*     */     {
/* 225 */       String json = EntityUtils.toString(response.getEntity(), "UTF-8");
/* 226 */       ObjectMapper mapper = new ObjectMapper();
/* 227 */       return mapper.readTree(json);
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 232 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.N1QLCLIENT_FETCH_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 235 */       err.initCause(ex);
/* 236 */       throw err;
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
/*     */   public static N1QLPlan toQueryPlan(HttpResponse response)
/*     */     throws ErrorException
/*     */   {
/*     */     try
/*     */     {
/* 253 */       JsonNode rootNode = toJsonNode(response);
/* 254 */       return new N1QLPlan(rootNode);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 258 */       ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.N1QLCLIENT_FETCH_ERR.name(), new String[] { ex.getMessage() });
/*     */       
/*     */ 
/* 261 */       err.initCause(ex);
/* 262 */       throw err;
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
/*     */   public static IN1QLResultSet toResultSet(HttpResponse response, String requestType)
/*     */     throws ErrorException
/*     */   {
/* 281 */     JsonNode rootNode = toJsonNode(response);
/* 282 */     if (requestType.equalsIgnoreCase(N1QLRequestType.update.name()))
/*     */     {
/* 284 */       return new N1QLRowCountSet(rootNode);
/*     */     }
/*     */     
/*     */ 
/* 288 */     return new N1QLQueryResult(rootNode);
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
/*     */   private static String URLBuilder(String host, int port, String requestType, String query, CBConnectionSettings settings, boolean setScanConsistency, ArrayList<String> posParams, HashMap<String, String> namedParams)
/*     */     throws Exception
/*     */   {
/* 322 */     StringBuilder connectionString = new StringBuilder();
/*     */     
/* 324 */     if (settings.m_enableSSL)
/*     */     {
/* 326 */       connectionString.append("https://").append(host).append(":").append(port).append("/");
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 332 */       connectionString.append("http://").append(host).append(":").append(port).append("/");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 337 */     connectionString.append("query?").append(requestType).append("=");
/*     */     
/*     */ 
/*     */ 
/* 341 */     connectionString.append(URLEncoder.encode(query.toString(), "UTF-8"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */     if (setScanConsistency)
/*     */     {
/* 349 */       connectionString.append("&").append("scan_consistency").append("=").append(settings.m_scanConsistency);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */     if ((null != posParams) && (!posParams.isEmpty()))
/*     */     {
/* 358 */       StringBuilder posParamsStr = new StringBuilder();
/*     */       
/* 360 */       for (int i = 0; i < posParams.size(); i++)
/*     */       {
/* 362 */         if (0 != i)
/*     */         {
/* 364 */           posParamsStr.append(",");
/*     */         }
/*     */         
/* 367 */         String posParamValue = formatNamedParam((String)posParams.get(i));
/* 368 */         posParamsStr.append(URLEncoder.encode(posParamValue, "UTF-8"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 373 */       connectionString.append("&args").append("=").append("[").append(posParamsStr).append("]");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 381 */     if ((null != namedParams) && (!namedParams.isEmpty()))
/*     */     {
/* 383 */       StringBuilder namedParamsStr = new StringBuilder();
/*     */       
/* 385 */       for (String paramName : namedParams.keySet())
/*     */       {
/* 387 */         String namedParamValue = formatNamedParam((String)namedParams.get(paramName));
/*     */         
/* 389 */         namedParamsStr.append("&").append(paramName).append("=").append(URLEncoder.encode(namedParamValue, "UTF-8"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */       connectionString.append(namedParamsStr);
/*     */     }
/*     */     
/*     */ 
/* 401 */     if (settings.m_authMech)
/*     */     {
/* 403 */       File credFile = new File(settings.m_authCreds);
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 408 */         if (credFile.exists())
/*     */         {
/* 410 */           BufferedReader br = new BufferedReader(new FileReader(credFile));
/* 411 */           StringBuilder sb = new StringBuilder();
/* 412 */           String line = br.readLine();
/* 413 */           while (line != null) {
/* 414 */             sb.append(line);
/* 415 */             line = br.readLine();
/*     */           }
/* 417 */           String credentials = sb.toString();
/*     */           
/* 419 */           connectionString.append("&").append("creds").append("=").append(URLEncoder.encode(credentials, "UTF-8"));
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 425 */           br.close();
/*     */         }
/*     */         else
/*     */         {
/* 429 */           throw CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_CONNECT_CRED_ERR.name(), new String[0]);
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/*     */         String credentials;
/*     */         
/* 436 */         ErrorException err = CBQueryUtils.buildRegularErrorMessage(CBJDBCMessageKey.CONN_CONNECT_CRED_ERR.name(), new String[0]);
/*     */         
/*     */ 
/* 439 */         err.initCause(ex);
/* 440 */         throw err;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 445 */     URI uri = new URI(connectionString.toString());
/* 446 */     URL url = uri.toURL();
/* 447 */     return url.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String formatNamedParam(String currN1qlParam)
/*     */   {
/* 457 */     String n1qlParam = null;
/* 458 */     if (null == currN1qlParam)
/*     */     {
/* 460 */       return "null";
/*     */     }
/* 462 */     if ((currN1qlParam.charAt(0) == '\'') && (currN1qlParam.charAt(currN1qlParam.length() - 1) == '\''))
/*     */     {
/*     */ 
/* 465 */       n1qlParam = currN1qlParam.replaceAll("^'(.*)'$", "\"$1\"");
/*     */     }
/*     */     else
/*     */     {
/* 469 */       n1qlParam = currN1qlParam;
/*     */     }
/* 471 */     return n1qlParam;
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/client/N1QLClientUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */