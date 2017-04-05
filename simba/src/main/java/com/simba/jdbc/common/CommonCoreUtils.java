/*     */ package com.simba.jdbc.common;
/*     */ 
/*     */ import com.simba.dsi.core.utilities.ConnSettingRequestMap;
/*     */ import com.simba.support.ILogger;
/*     */ import com.simba.support.LogUtilities;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonCoreUtils
/*     */ {
/*     */   protected static final String HOST_DELIMITER = "//";
/*     */   protected static final String PORT_DELIMITER = ":";
/*     */   protected static final String SCHEMA_DELIMITER = "/";
/*     */   protected static final String KEY_VALUE_DELIMITER = ";";
/*     */   protected static final String VALUE_DELIMITER = "=";
/*     */   
/*     */   public static boolean parseSubName(String subname, Properties properties)
/*     */   {
/*  50 */     if ((null == subname) || (0 == subname.length()) || (!subname.startsWith("//")))
/*     */     {
/*     */ 
/*     */ 
/*  54 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  58 */     String keyValueStr = subname.trim().substring("//".length());
/*     */     
/*  60 */     if (0 == keyValueStr.length())
/*     */     {
/*     */ 
/*  63 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  68 */     TreeMap<String, String> map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */     
/*     */ 
/*  71 */     String[] keyValuePairs = keyValueStr.split(";");
/*     */     
/*     */ 
/*     */ 
/*  75 */     for (int i = 0; i < keyValuePairs.length; i++)
/*     */     {
/*     */ 
/*  78 */       if (i == 0)
/*     */       {
/*  80 */         String[] Conn_Type = { "Host", "Port", "ConnSchema" };
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */         String connectionInfo = keyValuePairs[0];
/*     */         
/*  88 */         String host = null;
/*  89 */         String port = null;
/*  90 */         String schema = null;
/*     */         
/*  92 */         int portIndex = connectionInfo.indexOf(":");
/*  93 */         int schemaIndex = connectionInfo.indexOf("/");
/*     */         
/*  95 */         if (-1 == portIndex)
/*     */         {
/*     */ 
/*  98 */           if (-1 != schemaIndex)
/*     */           {
/* 100 */             host = connectionInfo.substring(0, schemaIndex);
/* 101 */             schema = connectionInfo.substring(schemaIndex + 1, connectionInfo.length());
/* 102 */             map.put(Conn_Type[2], schema);
/*     */           }
/*     */           else
/*     */           {
/* 106 */             host = connectionInfo;
/*     */           }
/* 108 */           map.put(Conn_Type[0], host);
/*     */         }
/*     */         else
/*     */         {
/* 112 */           host = connectionInfo.substring(0, portIndex);
/* 113 */           map.put(Conn_Type[0], host);
/*     */           
/* 115 */           if (-1 != schemaIndex)
/*     */           {
/* 117 */             port = connectionInfo.substring(portIndex + 1, schemaIndex);
/* 118 */             map.put(Conn_Type[1], port);
/* 119 */             schema = connectionInfo.substring(schemaIndex + 1, connectionInfo.length());
/* 120 */             map.put(Conn_Type[2], schema);
/*     */           }
/*     */           else
/*     */           {
/* 124 */             port = connectionInfo.substring(portIndex + 1, connectionInfo.length());
/* 125 */             map.put(Conn_Type[1], port);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 131 */         String[] keyValue = keyValuePairs[i].split("=");
/* 132 */         if (2 > keyValue.length)
/*     */         {
/*     */ 
/* 135 */           map.put(keyValue[0], "");
/*     */         }
/*     */         else
/*     */         {
/* 139 */           map.put(keyValue[0], keyValue[1]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 146 */     Enumeration<Object> keys = properties.keys();
/*     */     
/*     */ 
/* 149 */     while (keys.hasMoreElements())
/*     */     {
/*     */ 
/* 152 */       String key = (String)keys.nextElement();
/* 153 */       if (!map.containsKey(key))
/*     */       {
/* 155 */         map.put(key, properties.getProperty(key));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 160 */     properties.clear();
/* 161 */     properties.putAll(map);
/*     */     
/* 163 */     return true;
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
/*     */   @Deprecated
/*     */   public static void logConnectionFunctionEntrance(ILogger logger, ConnSettingRequestMap requestMap, int majorVersion, int minorVersion, int hotFixVersion, int driverBuildNumber)
/*     */   {
/* 189 */     logConnectionFunctionEntrance(logger, requestMap, String.valueOf(majorVersion), String.valueOf(minorVersion), String.valueOf(hotFixVersion), String.valueOf(driverBuildNumber));
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
/*     */   public static void logConnectionFunctionEntrance(ILogger logger, ConnSettingRequestMap requestMap, String majorVersion, String minorVersion, String hotFixVersion, String driverBuildNumber)
/*     */   {
/* 216 */     if (logger.isEnabled())
/*     */     {
/*     */ 
/* 219 */       StringBuffer classPathStr = new StringBuffer();
/* 220 */       classPathStr.append("URLClassLoader.getURLs(): ");
/*     */       
/*     */       try
/*     */       {
/* 224 */         ClassLoader cl = ClassLoader.getSystemClassLoader();
/*     */         
/* 226 */         if ((cl instanceof URLClassLoader))
/*     */         {
/* 228 */           URL[] urls = ((URLClassLoader)cl).getURLs();
/* 229 */           boolean isFirst = true;
/*     */           
/* 231 */           for (URL url : urls) {
/* 232 */             if (!isFirst)
/*     */             {
/* 234 */               classPathStr.append(", ");
/*     */             }
/* 236 */             isFirst = false;
/*     */             
/* 238 */             classPathStr.append(url.getFile());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 243 */           classPathStr.append("No URLClassLoader available.");
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       catch (SecurityException ex)
/*     */       {
/* 250 */         LogUtilities.logError(ex, logger);
/* 251 */         classPathStr.append("SecurityException thrown: " + ex.getMessage());
/*     */ 
/*     */       }
/*     */       catch (IllegalStateException ex)
/*     */       {
/*     */ 
/* 257 */         LogUtilities.logError(ex, logger);
/* 258 */         classPathStr.append("IllegalStateException thrown: " + ex.getMessage());
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Error ex)
/*     */       {
/*     */ 
/*     */ 
/* 266 */         classPathStr.append("Error thrown: " + ex.getMessage());
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 271 */         LogUtilities.logFunctionEntrance(logger, new Object[] { requestMap, "Major Version: " + majorVersion, "Minor Version: " + minorVersion, "Hot Fix Version: " + hotFixVersion, "Build Number: " + driverBuildNumber, "java.vendor:" + System.getProperty("java.vendor"), "java.version:" + System.getProperty("java.version"), "os.arch:" + System.getProperty("os.arch"), "os.name:" + System.getProperty("os.name"), "os.version:" + System.getProperty("os.version"), "Runtime.totalMemory:" + Runtime.getRuntime().totalMemory(), "Runtime.maxMemory:" + Runtime.getRuntime().maxMemory(), "Runtime.avaialableProcessors:" + Runtime.getRuntime().availableProcessors(), classPathStr });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 290 */         LogUtilities.logError(e, logger);
/*     */         
/* 292 */         LogUtilities.logFunctionEntrance(logger, new Object[] { requestMap, "Major Version: " + majorVersion, "Minor Version: " + minorVersion, "Hot Fix Version: " + hotFixVersion, "Build Number: " + driverBuildNumber, "Runtime.totalMemory:" + Runtime.getRuntime().totalMemory(), "Runtime.maxMemory:" + Runtime.getRuntime().maxMemory(), "Runtime.avaialableProcessors:" + Runtime.getRuntime().availableProcessors(), classPathStr });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/CommonCoreUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */