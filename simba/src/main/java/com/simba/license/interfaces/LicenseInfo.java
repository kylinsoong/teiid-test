package com.simba.license.interfaces;

import com.simba.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LicenseInfo
{
  public static final String EVALUATION_STRING = "Evaluation";
  public static final int BITMASK_LENGTH = 64;
  private static final int BITMASK_BYTES = 8;
  private static final int FOUR_BYTES = 4;
  private static final int INTERLEAVED_BYTES = 16;
  private static final String NODE_NAME_INFO = "Info";
  private static final String NODE_NAME_LICENSE_TYPE = "LicenseType";
  private static final String NODE_NAME_PRODUCT_NAME = "Product";
  private static final String NODE_NAME_PRODUCT_PLATFORM = "Platform";
  private static final String NODE_NAME_PRODUCT_VERSION = "ProductVersion";
  private static final String NODE_NAME_LICENSE_KEY = "Key";
  private static final String NODE_NAME_LICENSE_EXPIRY = "Expiry";
  private static final String LICENSE_DATE_FORMAT_STRING = "yyyy-MM-dd";
  private SimpleDateFormat licenseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private Document mDoc = null;
  private String mLicenseString = null;
  private byte[] mSignature = null;
  private byte[] mBitMask = new byte[8];
  private Date mExpiry = null;
  private XPath mXpath = XPathFactory.newInstance().newXPath();
  
  public LicenseInfo(String paramString)
    throws SimbaLicenseException
  {
    this.mLicenseString = paramString;
    parseLicense();
  }
  
  public final String getLicenseInfoAsText()
  {
    String str = "No Info tag found in license";
    Pattern localPattern = Pattern.compile("<Info.*</Info>", 32);
    Matcher localMatcher = localPattern.matcher(this.mLicenseString);
    if (localMatcher.find()) {
      str = localMatcher.group(0);
    }
    return str;
  }
  
  public final byte[] getBitMask()
  {
    return (byte[])this.mBitMask.clone();
  }
  
  public final byte[] getLicenseSignature()
  {
    return (byte[])this.mSignature.clone();
  }
  
  public final String getLicenseType()
  {
    return getNodeTextContent("LicenseType");
  }
  
  public final String getLicenseVersion()
  {
    return getAttributeValue("Info", "version");
  }
  
  public final String getProduct()
  {
    return getNodeTextContent("Product");
  }
  
  public final String getProductPlatform()
  {
    return getNodeTextContent("Platform");
  }
  
  public final String getProductVersion()
  {
    return getNodeTextContent("ProductVersion");
  }
  
  public final Date getExpiry()
  {
    return this.mExpiry;
  }
  
  private void parseLicense()
    throws SimbaLicenseException
  {
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      String str2 = removeUTF8BOM(this.mLicenseString.trim());
      this.mDoc = localDocumentBuilder.parse(new InputSource(new ByteArrayInputStream(str2.getBytes("utf-8"))));
    }
    catch (SAXException localSAXException)
    {
      str1 = String.format("Invalid license text format, failed XML parsing. Exception reason: %s", new Object[] { localSAXException.getMessage() });
      throw new SimbaLicenseException(str1);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      str1 = String.format("Internal Licensing error: %s", new Object[] { localParserConfigurationException.getMessage() });
      throw new SimbaLicenseException(str1);
    }
    catch (IOException localIOException)
    {
      String str1 = String.format("Internal Licensing error: %s", new Object[] { localIOException.getMessage() });
      throw new SimbaLicenseException(str1);
    }
    parseKeyString();
    setExpiry();
  }
  
  private static String removeUTF8BOM(String paramString)
  {
    int i = paramString.indexOf("<");
    if (i > 0) {
      paramString = paramString.substring(i);
    }
    return paramString;
  }
  
  private Node getXpathObject(String paramString)
  {
    Object localObject = null;
    if (this.mDoc != null) {
      try
      {
        localObject = this.mXpath.evaluate(paramString, this.mDoc, XPathConstants.NODE);
      }
      catch (XPathExpressionException localXPathExpressionException) {}
    }
    return (Node)localObject;
  }
  
  private Node findNode(String paramString, NodeList paramNodeList)
  {
    if (null != paramNodeList) {
      for (int i = 0; i < paramNodeList.getLength(); i++)
      {
        Node localNode1 = paramNodeList.item(i);
        if ((null != localNode1) && (null != localNode1.getNodeName()) && (paramString.equals(localNode1.getNodeName()))) {
          return localNode1;
        }
        if (localNode1.hasChildNodes())
        {
          Node localNode2 = findNode(paramString, localNode1.getChildNodes());
          if (null != localNode2) {
            return localNode2;
          }
        }
      }
    }
    return null;
  }
  
  private String getNodeTextContent(String paramString)
  {
    Node localNode = findNode(paramString, this.mDoc.getChildNodes());
    if (localNode != null) {
      return localNode.getTextContent();
    }
    return "";
  }
  
  private String getAttributeValue(String paramString1, String paramString2)
  {
    Node localNode1 = findNode(paramString1, this.mDoc.getChildNodes());
    Element localElement = (Element)getXpathObject(paramString1);
    if (null != localNode1)
    {
      NamedNodeMap localNamedNodeMap = localNode1.getAttributes();
      Node localNode2 = localNamedNodeMap.getNamedItem(paramString2);
      if (null != localNode2) {
        return localNode2.getTextContent();
      }
    }
    return "";
  }
  
  private void parseKeyString()
    throws SimbaLicenseException
  {
    String str2 = getNodeTextContent("Key");
    byte[] arrayOfByte1 = str2.getBytes();
    String str1;
    if ((str2.length() > 16) && (arrayOfByte1.length % 4 == 0))
    {
      byte[] arrayOfByte2 = Base64.decodeBase64(arrayOfByte1);
      if (arrayOfByte2 != null)
      {
        this.mSignature = new byte[arrayOfByte2.length - 8];
        for (int i = 0; i < 16; i += 2)
        {
          this.mSignature[(i / 2)] = arrayOfByte2[i];
          this.mBitMask[(i / 2)] = arrayOfByte2[(i + 1)];
        }
        for (i = 16; i < arrayOfByte2.length; i++) {
          this.mSignature[(i - 8)] = arrayOfByte2[i];
        }
      }
      else
      {
        str1 = "Cannot find valid key string in license";
        throw new SimbaLicenseException(str1);
      }
    }
    else
    {
      str1 = "Cannot find valid key string in license";
      throw new SimbaLicenseException(str1);
    }
  }
  
  private void setExpiry()
    throws SimbaLicenseException
  {
    String str2 = getNodeTextContent("Expiry");
    if (str2.equals(""))
    {
      this.mExpiry = new Date();
    }
    else
    {
      String str1;
      if (str2.length() == "yyyy-MM-dd".length())
      {
        try
        {
          Date localDate = this.licenseDateFormat.parse(str2);
          Calendar localCalendar = Calendar.getInstance();
          localCalendar.setTime(localDate);
          localCalendar.set(11, 23);
          localCalendar.set(12, 59);
          localCalendar.set(13, 59);
          localCalendar.set(14, 999);
          this.mExpiry = localCalendar.getTime();
        }
        catch (ParseException localParseException)
        {
          str1 = "Invalid_expiry format";
          throw new SimbaLicenseException(str1);
        }
      }
      else
      {
        str1 = "Invalid_expiry format";
        throw new SimbaLicenseException(str1);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/license/interfaces/LicenseInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */