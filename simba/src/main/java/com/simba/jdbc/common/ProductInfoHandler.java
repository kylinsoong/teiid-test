/*     */ package com.simba.jdbc.common;
/*     */ 
/*     */ import com.simba.dsi.core.impl.DSIDriver;
/*     */ import com.simba.license.LicenseUtil;
/*     */ import com.simba.license.interfaces.IProductInfo;
/*     */ import com.simba.license.interfaces.SimbaLicenseException;
/*     */ import com.simba.support.exceptions.ErrorException;
/*     */ import com.simba.support.exceptions.ExceptionBuilder;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ProductInfoHandler
/*     */ {
/*     */   private final String m_licenseFileName;
/*     */   private ProductInfo m_productInfo;
/*     */   private final Class<? extends DSIDriver> m_driverClass;
/*     */   
/*     */   private static class ProductInfo
/*     */     implements IProductInfo
/*     */   {
/*     */     private final String m_productName;
/*     */     private final String m_productVersion;
/*     */     
/*     */     public ProductInfo(String productName, String productVersion)
/*     */     {
/*  62 */       this.m_productName = productName;
/*  63 */       this.m_productVersion = productVersion;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getProductName()
/*     */     {
/*  72 */       return this.m_productName;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getProductPlatform()
/*     */     {
/*  81 */       return "Java";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getProductVersion()
/*     */     {
/*  90 */       return this.m_productVersion;
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
/*     */   private static enum LicenseMessageKey
/*     */   {
/* 107 */     LICENSE_VALIDATION_ERROR, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 112 */     LICENSE_FILE_READ_ERROR;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private LicenseMessageKey() {}
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
/*     */   public ProductInfoHandler(String fileName, String productName, String productVersion, Class<? extends DSIDriver> driverClass)
/*     */   {
/* 150 */     this.m_licenseFileName = fileName;
/* 151 */     this.m_productInfo = new ProductInfo(productName, productVersion);
/* 152 */     this.m_driverClass = driverClass;
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
/*     */   public void validateLicense()
/*     */     throws ErrorException
/*     */   {
/*     */     try
/*     */     {
/* 168 */       ArrayList<String> errors = new ArrayList();
/*     */       
/* 170 */       LicenseUtil licenseUtil = new LicenseUtil(this.m_productInfo, getLicense(this.m_licenseFileName));
/*     */       
/* 172 */       if (!licenseUtil.validate(errors))
/*     */       {
/*     */ 
/* 175 */         checkErrors(errors);
/*     */       }
/*     */     }
/*     */     catch (SimbaLicenseException r)
/*     */     {
/* 180 */       throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_VALIDATION_ERROR.name(), this.m_licenseFileName, r);
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
/*     */   private String getLicense(String fileName)
/*     */     throws ErrorException
/*     */   {
/* 195 */     String jarFileName = this.m_driverClass.getProtectionDomain().getCodeSource().getLocation().getPath();
/*     */     
/*     */ 
/* 198 */     String licenseFolder = null;
/* 199 */     if (null != jarFileName)
/*     */     {
/*     */ 
/*     */ 
/* 203 */       int lastSlash = jarFileName.lastIndexOf(File.separator);
/* 204 */       if (lastSlash == -1)
/*     */       {
/* 206 */         lastSlash = jarFileName.lastIndexOf("/");
/*     */       }
/* 208 */       if (lastSlash != -1)
/*     */       {
/* 210 */         licenseFolder = jarFileName.substring(0, lastSlash);
/*     */       }
/*     */     }
/*     */     
/* 214 */     String filename = null;
/*     */     
/*     */ 
/*     */ 
/* 218 */     if (null == licenseFolder)
/*     */     {
/* 220 */       filename = ProductInfoHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + ".." + File.separator + fileName;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 229 */       licenseFolder = licenseFolder.replace(" ", "%20");
/*     */       
/* 231 */       filename = licenseFolder + File.separator + fileName;
/*     */     }
/*     */     
/*     */ 
/*     */     URL fileUrlString;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 240 */       fileUrlString = new URL("file:" + filename);
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/* 244 */       throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_FILE_READ_ERROR.name(), filename);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 252 */       File license = new File(fileUrlString.toURI().getSchemeSpecificPart());
/*     */       
/* 254 */       if ((license.exists()) && (!license.isDirectory()))
/*     */       {
/* 256 */         Scanner s = null;
/*     */         try
/*     */         {
/* 259 */           s = new Scanner(license);
/* 260 */           s.useDelimiter("\\z");
/* 261 */           if (s.hasNext())
/*     */           {
/* 263 */             return s.next();
/*     */           }
/*     */           
/*     */ 
/* 267 */           throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_FILE_READ_ERROR.name(), fileUrlString.toURI().getSchemeSpecificPart());
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (FileNotFoundException e)
/*     */         {
/*     */ 
/* 274 */           throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_FILE_READ_ERROR.name(), fileUrlString.toURI().getSchemeSpecificPart(), e);
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 281 */           if (null != s)
/*     */           {
/* 283 */             s.close();
/*     */           }
/*     */         }
/*     */       }
/* 287 */       throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_FILE_READ_ERROR.name(), fileUrlString.toURI().getSchemeSpecificPart());
/*     */ 
/*     */     }
/*     */     catch (URISyntaxException e)
/*     */     {
/*     */ 
/* 293 */       throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_FILE_READ_ERROR.name(), filename);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkErrors(List<String> errors)
/*     */     throws ErrorException
/*     */   {
/* 306 */     if (!errors.isEmpty())
/*     */     {
/* 308 */       StringBuilder sb = new StringBuilder();
/* 309 */       sb.append((String)errors.get(0));
/*     */       
/* 311 */       for (int i = 1; i < errors.size(); i++)
/*     */       {
/* 313 */         sb.append(", ");
/* 314 */         sb.append((String)errors.get(i));
/*     */       }
/* 316 */       sb.append(".");
/* 317 */       throw ProductInfoHandlerContext.s_messages.createGeneralException(LicenseMessageKey.LICENSE_VALIDATION_ERROR.name(), new String[] { sb.toString() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/common/ProductInfoHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */