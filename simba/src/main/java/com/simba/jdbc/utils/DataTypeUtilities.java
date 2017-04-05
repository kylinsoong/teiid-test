/*      */ package com.simba.jdbc.utils;
/*      */ 
/*      */ import com.simba.support.exceptions.ErrorException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DataTypeUtilities
/*      */ {
/*      */   private static final int NUMBER_OF_POWERS_OF_10 = 18;
/*   41 */   private static final long[] POWERS_OF_10 = new long[19];
/*      */   
/*      */ 
/*   44 */   private static final CalculateMantissa[] MANTISSA_CALCULATORS = new CalculateMantissa[19];
/*      */   
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*   50 */     for (int i = 18; i > 0; i--)
/*      */     {
/*   52 */       POWERS_OF_10[i] = (Math.pow(10.0D, i));
/*      */     }
/*      */     
/*      */ 
/*   56 */     MANTISSA_CALCULATORS[1] = new CalculateMantissa_Length_1(null);
/*   57 */     MANTISSA_CALCULATORS[2] = new CalculateMantissa_Length_2(null);
/*   58 */     MANTISSA_CALCULATORS[3] = new CalculateMantissa_Length_3(null);
/*   59 */     MANTISSA_CALCULATORS[4] = new CalculateMantissa_Length_4(null);
/*   60 */     MANTISSA_CALCULATORS[5] = new CalculateMantissa_Length_5(null);
/*   61 */     MANTISSA_CALCULATORS[6] = new CalculateMantissa_Length_6(null);
/*   62 */     MANTISSA_CALCULATORS[7] = new CalculateMantissa_Length_7(null);
/*   63 */     MANTISSA_CALCULATORS[8] = new CalculateMantissa_Length_8(null);
/*   64 */     MANTISSA_CALCULATORS[9] = new CalculateMantissa_Length_9(null);
/*   65 */     MANTISSA_CALCULATORS[10] = new CalculateMantissa_Length_10(null);
/*   66 */     MANTISSA_CALCULATORS[11] = new CalculateMantissa_Length_11(null);
/*   67 */     MANTISSA_CALCULATORS[12] = new CalculateMantissa_Length_12(null);
/*   68 */     MANTISSA_CALCULATORS[13] = new CalculateMantissa_Length_13(null);
/*   69 */     MANTISSA_CALCULATORS[14] = new CalculateMantissa_Length_14(null);
/*   70 */     MANTISSA_CALCULATORS[15] = new CalculateMantissa_Length_15(null);
/*   71 */     MANTISSA_CALCULATORS[16] = new CalculateMantissa_Length_16(null);
/*   72 */     MANTISSA_CALCULATORS[17] = new CalculateMantissa_Length_17(null);
/*   73 */     MANTISSA_CALCULATORS[18] = new CalculateMantissa_Length_18(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract class CalculateMantissa
/*      */   {
/*      */     public abstract long calculate(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class NumericRepresentation
/*      */   {
/*      */     boolean isValueNegative;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     long mantissaValue;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     long[] mantissaElements;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int mantissaValueLength;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  135 */     boolean hasFractionalValue = false;
/*      */     
/*      */ 
/*  138 */     short fractionalSignificantDigits = 0;
/*      */     
/*      */ 
/*      */ 
/*  142 */     short fractionalLeadingZeros = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     long fractionalValue;
/*      */     
/*      */ 
/*      */ 
/*      */     long[] fractionalElements;
/*      */     
/*      */ 
/*  153 */     boolean hasExponentValue = false;
/*      */     
/*      */ 
/*  156 */     boolean isExponentNegative = false;
/*      */     
/*      */ 
/*      */ 
/*  160 */     short exponentValue = 0;
/*      */     
/*      */ 
/*  163 */     boolean isInfinity = false;
/*      */     
/*      */ 
/*  166 */     boolean isNan = false;
/*      */     
/*      */ 
/*      */ 
/*      */     long totalMantissaValue;
/*      */     
/*      */ 
/*      */ 
/*  174 */     short totalExponent = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isSmallValue()
/*      */     {
/*  181 */       return 18 > this.mantissaValueLength + (this.fractionalSignificantDigits != -1 ? this.fractionalSignificantDigits : 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  188 */       StringBuilder mantissaString = new StringBuilder();
/*  189 */       if (null != this.mantissaElements)
/*      */       {
/*  191 */         mantissaString.append("{");
/*  192 */         for (long l : this.mantissaElements)
/*      */         {
/*  194 */           mantissaString.append(l);
/*  195 */           mantissaString.append(",");
/*      */         }
/*  197 */         mantissaString.append("}");
/*      */       }
/*  199 */       StringBuilder fractionString = new StringBuilder();
/*  200 */       if (null != this.fractionalElements)
/*      */       {
/*  202 */         fractionString.append("{");
/*  203 */         for (long l : this.fractionalElements)
/*      */         {
/*  205 */           fractionString.append(l);
/*  206 */           fractionString.append(",");
/*      */         }
/*  208 */         fractionString.append("}");
/*      */       }
/*  210 */       return "isValueNegative: " + this.isValueNegative + "\nmantissaValue: " + this.mantissaValue + "\nmantissaElements :" + mantissaString + "\nhasFractionalValue: " + this.hasFractionalValue + "\nfractionalSignificantDigits: " + this.fractionalSignificantDigits + "\nfractionalLeadingZeros: " + this.fractionalLeadingZeros + "\nfractionalValue: " + this.fractionalValue + "\nfractionalElements: " + fractionString + "\nhasExponentValue: " + this.hasExponentValue + "\nisExponentNegative: " + this.isExponentNegative + "\nexponentValue: " + this.exponentValue + "\ntotalMantissaValue: " + this.totalMantissaValue + "\ntotalMantissaValue: " + this.totalExponent;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] toHexString(byte[] bytes)
/*      */   {
/*  239 */     byte[] hexArray = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*  240 */     byte[] hexChars = new byte[bytes.length * 2];
/*  241 */     for (int j = 0; j < bytes.length; j++)
/*      */     {
/*  243 */       int v = bytes[j] & 0xFF;
/*  244 */       hexChars[(j * 2)] = hexArray[(v >>> 4)];
/*  245 */       hexChars[(j * 2 + 1)] = hexArray[(v & 0xF)];
/*      */     }
/*  247 */     return hexChars;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String toUTF8String(byte[] value)
/*      */   {
/*  258 */     String tempString = null;
/*      */     try
/*      */     {
/*  261 */       tempString = new String(value, "UTF-8");
/*      */     }
/*      */     catch (UnsupportedEncodingException e)
/*      */     {
/*  265 */       tempString = new String(value);
/*      */     }
/*  267 */     return tempString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String toUTF8String(byte[] value, int begin, int length)
/*      */   {
/*  282 */     String tempString = null;
/*      */     try
/*      */     {
/*  285 */       tempString = new String(value, begin, length, "UTF-8");
/*      */     }
/*      */     catch (UnsupportedEncodingException e)
/*      */     {
/*  289 */       tempString = new String(value, begin, length);
/*      */     }
/*  291 */     return tempString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final byte[] toUTF8ByteArray(String value)
/*      */   {
/*  301 */     byte[] tempArray = null;
/*      */     try
/*      */     {
/*  304 */       tempArray = value.getBytes("UTF-8");
/*      */     }
/*      */     catch (UnsupportedEncodingException e)
/*      */     {
/*  308 */       tempArray = value.getBytes();
/*      */     }
/*  310 */     return tempArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int toInt(byte[] bytes)
/*      */   {
/*  322 */     switch (bytes.length)
/*      */     {
/*      */     case 4: 
/*  325 */       return (bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | bytes[3] & 0xFF;
/*      */     
/*      */ 
/*      */ 
/*      */     case 3: 
/*  330 */       return (bytes[0] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | bytes[2] & 0xFF;
/*      */     
/*      */ 
/*      */     case 2: 
/*  334 */       return (bytes[0] & 0xFF) << 8 | bytes[1] & 0xFF;
/*      */     }
/*      */     
/*  337 */     return bytes[0] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long toLong(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  358 */     if (18 < length)
/*      */     {
/*  360 */       String str = toUTF8String(bytes, beginIndex, length);
/*  361 */       BigDecimal bigDecimal = new BigDecimal(str);
/*  362 */       return bigDecimal.longValue();
/*      */     }
/*      */     
/*  365 */     int currentIndex = beginIndex;
/*      */     
/*      */ 
/*  368 */     char currentCharacter = (char)bytes[currentIndex];
/*      */     
/*      */ 
/*  371 */     boolean isSign = '-' == currentCharacter;
/*      */     
/*  373 */     if (isSign)
/*      */     {
/*  375 */       currentCharacter = (char)bytes[(++currentIndex)];
/*  376 */       length--;
/*      */     }
/*      */     
/*  379 */     long value = toMantissa(bytes, currentIndex, length);
/*      */     
/*  381 */     return isSign ? -value : value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static short toShort(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  400 */     return (short)(int)toLong(bytes, beginIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int toInteger(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  419 */     return (int)toLong(bytes, beginIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static float toFloat(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  438 */     return (float)toDouble(bytes, beginIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double toDouble(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  457 */     NumericRepresentation result = null;
/*      */     
/*      */     try
/*      */     {
/*  461 */       result = buildTotalMantissa(bytes, beginIndex, length);
/*      */       
/*      */ 
/*  464 */       if (result.isSmallValue())
/*      */       {
/*  466 */         if (result.isInfinity)
/*      */         {
/*  468 */           if (result.isValueNegative)
/*      */           {
/*  470 */             return Double.NEGATIVE_INFINITY;
/*      */           }
/*      */           
/*      */ 
/*  474 */           return Double.POSITIVE_INFINITY;
/*      */         }
/*      */         
/*      */ 
/*  478 */         if (result.isNan)
/*      */         {
/*  480 */           return NaN.0D;
/*      */         }
/*      */         
/*  483 */         if (0 != result.totalExponent)
/*      */         {
/*      */ 
/*  486 */           if ((result.totalExponent >= 18) || (-result.totalExponent >= 18))
/*      */           {
/*      */ 
/*  489 */             return BigDecimal.valueOf(result.totalMantissaValue).scaleByPowerOfTen(result.totalExponent).doubleValue();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  494 */           if (0 < result.totalExponent)
/*      */           {
/*  496 */             return result.totalMantissaValue * POWERS_OF_10[result.totalExponent];
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  504 */           return result.totalMantissaValue / POWERS_OF_10[(-result.totalExponent)];
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  510 */         return result.totalMantissaValue;
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */     return getBigDecimal(result).doubleValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static BigDecimal toBigDecimal(byte[] bytes, int beginIndex, int length)
/*      */     throws ErrorException
/*      */   {
/*  540 */     NumericRepresentation result = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  545 */       result = buildTotalMantissa(bytes, beginIndex, length);
/*      */       
/*  547 */       BigDecimal value = null;
/*  548 */       if (result.isSmallValue())
/*      */       {
/*  550 */         value = new BigDecimal(result.totalMantissaValue);
/*  551 */         if (0 != result.totalExponent)
/*      */         {
/*      */ 
/*  554 */           return value.scaleByPowerOfTen(result.totalExponent);
/*      */         }
/*      */         
/*      */ 
/*  558 */         return value;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  563 */       return getBigDecimal(result);
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  571 */     return getBigDecimal(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static NumericRepresentation parseNumericRepresentation(byte[] bytes, int beginIndex, int length)
/*      */   {
/*  595 */     NumericRepresentation result = new NumericRepresentation(null);
/*      */     
/*      */ 
/*  598 */     result.mantissaValueLength = length;
/*      */     
/*      */ 
/*  601 */     int exponentValueIndex = -1;
/*      */     
/*      */ 
/*  604 */     int fractionalValueLength = -1;
/*      */     
/*      */ 
/*  607 */     int fractionalValueBeginIndex = -1;
/*      */     
/*      */ 
/*  610 */     int exponentValueLength = 0;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  615 */       switch (bytes[beginIndex])
/*      */       {
/*      */ 
/*      */       case 45: 
/*  619 */         result.isValueNegative = true;
/*  620 */         beginIndex++;
/*  621 */         length--;
/*  622 */         result.mantissaValueLength -= 1;
/*      */         
/*      */ 
/*  625 */         if (73 == bytes[beginIndex])
/*      */         {
/*  627 */           result.isInfinity = true;
/*  628 */           return result;
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       case 73: 
/*  634 */         result.isInfinity = true;
/*  635 */         return result;
/*      */       
/*      */ 
/*      */       case 78: 
/*  639 */         result.isNan = true;
/*  640 */         return result;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  647 */       for (int i = beginIndex; i < beginIndex + length; i++)
/*      */       {
/*  649 */         switch (bytes[i])
/*      */         {
/*      */ 
/*      */         case 46: 
/*  653 */           result.hasFractionalValue = true;
/*  654 */           fractionalValueBeginIndex = i + 1;
/*  655 */           fractionalValueLength = beginIndex + length - fractionalValueBeginIndex;
/*      */           
/*  657 */           result.mantissaValueLength = (i - beginIndex);
/*  658 */           break;
/*      */         
/*      */ 
/*      */         case 69: 
/*      */         case 101: 
/*  663 */           result.hasExponentValue = true;
/*      */           
/*  665 */           exponentValueIndex = i + 1;
/*  666 */           exponentValueLength = beginIndex + length - exponentValueIndex;
/*      */           
/*  668 */           if (!result.hasFractionalValue)
/*      */           {
/*  670 */             result.mantissaValueLength = (i - beginIndex);
/*      */             
/*      */             break label247;
/*      */           }
/*  674 */           fractionalValueLength = i - fractionalValueBeginIndex;
/*      */           
/*      */           break label247;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */       label247:
/*      */       
/*  683 */       if (result.hasExponentValue)
/*      */       {
/*      */ 
/*  686 */         switch (bytes[exponentValueIndex])
/*      */         {
/*      */ 
/*      */ 
/*      */         case 45: 
/*  691 */           result.isExponentNegative = true;
/*      */         
/*      */ 
/*      */ 
/*      */         case 43: 
/*  696 */           exponentValueIndex++;
/*  697 */           exponentValueLength--;
/*      */         }
/*      */         
/*      */         
/*      */ 
/*  702 */         result.exponentValue = ((short)(int)toMantissa(bytes, exponentValueIndex, exponentValueLength));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  707 */       if (result.hasFractionalValue)
/*      */       {
/*      */ 
/*  710 */         result.fractionalSignificantDigits = ((short)fractionalValueLength);
/*      */         
/*  712 */         int decimalEndIndex = fractionalValueLength - 1 + fractionalValueBeginIndex;
/*      */         
/*      */ 
/*  715 */         while (fractionalValueBeginIndex <= decimalEndIndex)
/*      */         {
/*  717 */           if (48 != bytes[fractionalValueBeginIndex])
/*      */             break;
/*  719 */           NumericRepresentation tmp349_348 = result;tmp349_348.fractionalLeadingZeros = ((short)(tmp349_348.fractionalLeadingZeros + 1));
/*  720 */           fractionalValueLength--;
/*  721 */           fractionalValueBeginIndex++;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  729 */       if (result.hasExponentValue)
/*      */       {
/*      */ 
/*      */ 
/*  733 */         result.totalExponent = ((short)((result.isExponentNegative ? -result.exponentValue : result.exponentValue) - result.fractionalSignificantDigits));
/*      */ 
/*      */ 
/*      */       }
/*  737 */       else if (result.hasFractionalValue)
/*      */       {
/*      */ 
/*  740 */         result.totalExponent = ((short)-result.fractionalSignificantDigits);
/*      */       }
/*      */       
/*      */ 
/*  744 */       if ((result.fractionalSignificantDigits > 0) && (result.fractionalLeadingZeros != result.fractionalSignificantDigits))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  750 */         if (18 >= result.fractionalSignificantDigits)
/*      */         {
/*  752 */           result.fractionalValue = toMantissa(bytes, fractionalValueBeginIndex, fractionalValueLength);
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*  760 */           result.fractionalElements = toMantissaElements(bytes, fractionalValueBeginIndex, fractionalValueLength);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  769 */       if (18 >= result.mantissaValueLength)
/*      */       {
/*  771 */         result.mantissaValue = toMantissa(bytes, beginIndex, result.mantissaValueLength);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  778 */         result.mantissaElements = toMantissaElements(bytes, beginIndex, result.mantissaValueLength);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  792 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long[] toMantissaElements(byte[] bytes, int beginIndex, int length)
/*      */   {
/*  806 */     int numElements = length / 18;
/*      */     
/*      */ 
/*      */ 
/*  810 */     if (length % 18 != 0)
/*      */     {
/*  812 */       numElements++;
/*      */     }
/*      */     
/*  815 */     long[] elements = new long[numElements];
/*      */     
/*      */ 
/*  818 */     for (int elementArrayIndex = 0; elementArrayIndex < numElements; elementArrayIndex++)
/*      */     {
/*  820 */       int elementLength = 18 > length ? length : 18;
/*      */       
/*      */ 
/*  823 */       elements[elementArrayIndex] = toMantissa(bytes, beginIndex, elementLength);
/*      */       
/*      */ 
/*  826 */       beginIndex += 18;
/*      */       
/*      */ 
/*  829 */       length -= 18;
/*      */     }
/*      */     
/*  832 */     return elements;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static BigDecimal getBigDecimal(NumericRepresentation numericRepresentation)
/*      */   {
/*  848 */     BigDecimal bigDecimal = null;
/*  849 */     BigDecimal fractional = null;
/*      */     
/*      */ 
/*  852 */     if (null != numericRepresentation.mantissaElements)
/*      */     {
/*  854 */       bigDecimal = toBigDecimalMantissaValue(numericRepresentation.mantissaElements, numericRepresentation.mantissaValueLength);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  859 */       bigDecimal = new BigDecimal(numericRepresentation.mantissaValue);
/*      */     }
/*      */     
/*      */ 
/*  863 */     if (numericRepresentation.hasFractionalValue)
/*      */     {
/*  865 */       if (null != numericRepresentation.fractionalElements)
/*      */       {
/*      */ 
/*      */ 
/*  869 */         fractional = toBigDecimalMantissaValue(numericRepresentation.fractionalElements, numericRepresentation.fractionalSignificantDigits - numericRepresentation.fractionalLeadingZeros);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  877 */         fractional = new BigDecimal(numericRepresentation.fractionalValue);
/*      */       }
/*      */       
/*      */ 
/*  881 */       bigDecimal = fractional.scaleByPowerOfTen(-numericRepresentation.fractionalSignificantDigits).add(bigDecimal);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  886 */     if (numericRepresentation.hasExponentValue)
/*      */     {
/*  888 */       bigDecimal = bigDecimal.scaleByPowerOfTen(numericRepresentation.exponentValue);
/*      */     }
/*      */     
/*      */ 
/*  892 */     if (numericRepresentation.isValueNegative)
/*      */     {
/*      */ 
/*  895 */       bigDecimal = bigDecimal.negate();
/*      */     }
/*      */     
/*  898 */     return bigDecimal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static BigDecimal toBigDecimalMantissaValue(long[] elements, int length)
/*      */   {
/*  910 */     BigDecimal bigD = new BigDecimal(elements[0]);
/*      */     
/*      */ 
/*      */ 
/*  914 */     for (int elementIndex = 1; elementIndex < elements.length; elementIndex++)
/*      */     {
/*      */ 
/*  917 */       length -= 18;
/*  918 */       int scaleby = 18 > length ? length : 18;
/*      */       
/*  920 */       bigD = bigD.scaleByPowerOfTen(scaleby).add(new BigDecimal(elements[elementIndex]));
/*      */     }
/*      */     
/*  923 */     return bigD;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static NumericRepresentation buildTotalMantissa(byte[] bytes, int beginIndex, int length)
/*      */   {
/*  943 */     NumericRepresentation numericRepresentation = parseNumericRepresentation(bytes, beginIndex, length);
/*      */     
/*      */ 
/*      */ 
/*  947 */     if (numericRepresentation.isSmallValue())
/*      */     {
/*  949 */       if (numericRepresentation.hasFractionalValue)
/*      */       {
/*      */ 
/*  952 */         numericRepresentation.totalMantissaValue = (numericRepresentation.mantissaValue * POWERS_OF_10[numericRepresentation.fractionalSignificantDigits] + numericRepresentation.fractionalValue);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  960 */         numericRepresentation.totalMantissaValue = numericRepresentation.mantissaValue;
/*      */       }
/*      */       
/*      */ 
/*  964 */       if (numericRepresentation.isValueNegative)
/*      */       {
/*      */ 
/*  967 */         numericRepresentation.totalMantissaValue = (-numericRepresentation.totalMantissaValue);
/*      */       }
/*      */     }
/*      */     
/*  971 */     return numericRepresentation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long toMantissa(byte[] bytes, int beginIndex, int length)
/*      */   {
/*  989 */     return MANTISSA_CALCULATORS[length].calculate(bytes, beginIndex, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_18
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1004 */       if (0 == beginIndex)
/*      */       {
/* 1006 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[17] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[16] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[12] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[13] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[14] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[15] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[16] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[17] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1027 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[17] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[16] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 12)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 13)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 14)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 15)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 16)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 17)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_17
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1052 */       if (0 == beginIndex)
/*      */       {
/* 1054 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[16] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[12] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[13] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[14] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[15] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[16] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1074 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[16] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 12)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 13)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 14)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 15)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 16)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_16
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1098 */       if (0 == beginIndex)
/*      */       {
/* 1100 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[12] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[13] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[14] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[15] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1119 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[15] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 12)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 13)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 14)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 15)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_15
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1142 */       if (0 == beginIndex)
/*      */       {
/* 1144 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[12] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[13] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[14] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1162 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[14] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 12)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 13)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 14)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_14
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1184 */       if (0 == beginIndex)
/*      */       {
/* 1186 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[12] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[13] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1203 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[13] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 12)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 13)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_13
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1224 */       if (0 == beginIndex)
/*      */       {
/* 1226 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[11] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[12] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1242 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[12] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 11)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 12)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_12
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1262 */       if (0 == beginIndex)
/*      */       {
/* 1264 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[10] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[11] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1279 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[11] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 10)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 11)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_11
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1298 */       if (0 == beginIndex)
/*      */       {
/* 1300 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[9] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[10] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1314 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[10] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 9)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 10)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_10
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1332 */       if (0 == beginIndex)
/*      */       {
/* 1334 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[8] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[9] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1347 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[9] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 8)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 9)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_9
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1364 */       if (0 == beginIndex)
/*      */       {
/* 1366 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[7] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[8] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1378 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[8] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 7)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 8)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_8
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1394 */       if (0 == beginIndex)
/*      */       {
/* 1396 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[6] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[7] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1407 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[7] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 6)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 7)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_7
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1422 */       if (0 == beginIndex)
/*      */       {
/* 1424 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[5] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[6] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1434 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[6] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 5)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 6)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_6
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1448 */       if (0 == beginIndex)
/*      */       {
/* 1450 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[4] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[5] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1459 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[5] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 4)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 5)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_5
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1472 */       if (0 == beginIndex)
/*      */       {
/* 1474 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[3] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[4] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1482 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[4] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 3)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 4)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_4
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1494 */       if (0 == beginIndex)
/*      */       {
/* 1496 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[2] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[3] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1503 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[3] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 2)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 3)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_3
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1514 */       if (0 == beginIndex)
/*      */       {
/* 1516 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[1] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[2] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1522 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[2] + (bytes[(beginIndex + 1)] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 2)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class CalculateMantissa_Length_2
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1533 */       if (0 == beginIndex)
/*      */       {
/* 1535 */         return (bytes[0] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[1] - 48);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1540 */       return (bytes[beginIndex] - 48) * DataTypeUtilities.POWERS_OF_10[1] + (bytes[(beginIndex + 1)] - 48);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class CalculateMantissa_Length_1
/*      */     extends DataTypeUtilities.CalculateMantissa
/*      */   {
/*      */     public long calculate(byte[] bytes, int beginIndex, int length)
/*      */     {
/* 1550 */       return bytes[beginIndex] - 48;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/utils/DataTypeUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */