// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.util;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.io.*;
import java.net.*; 
import sun.misc.*;
import java.text.*;

public class UtilMisc {

public static String htmlEscape(String S){
  if(null==S)return S;
  int N=S.length();
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);
    if(c=='&')sb.append("&amp;");
    else if(c=='"')sb.append("&quot;");
    else if(c=='<')sb.append("&lt;");
    else if(c=='>')sb.append("&gt;");
    else if(c=='\'')sb.append("&#39;");
    else sb.append(c);
    }
  return sb.toString();
}
public static String charEscape(String S, char a){
  if(null==S) return S;
  int N=S.length();
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);
    if(c=='\\')sb.append("\\");
    else if(c==a) sb.append("\\"+a);
    else sb.append(c);
  }
  return sb.toString();
}
public static String htmlJsEscape(String S){
  if(null==S)return S;
  int N=S.length();
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);
    if(c=='&')sb.append("&amp;");
    else if(c=='"')sb.append("&quot;");
    else if(c=='<')sb.append("&lt;");
    else if(c=='>')sb.append("&gt;");
    else if(c=='\'')sb.append("&#39;");
    else if(c=='\n')sb.append("<br>");
    else sb.append(c);
    }
  return sb.toString();
}
public static String mysqlEscape(String S){
  if(null==S) return S;
  int N=S.length();
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);
    if(c=='\\')sb.append("\\");
    else if(c=='\'')sb.append("\\'");
    else if(c=='\n')sb.append("\\r\\n");
    else sb.append(c);
  }
  return sb.toString();
}
public static String JSEscape(String S){
  if(null==S)return S;
  int N=S.length();
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);
    if(c=='"')sb.append("&quot;");
    else if(c=='\'')sb.append("&#39;");
    else if(c=='\n')sb.append("<br>");
    else sb.append(c);
    }
  return sb.toString();
}
public static String toUpperLowerCase(String S){
  if(S==null)return S;
  S = S.trim().toLowerCase();
  int N=S.length();
  boolean bUpper = false;
  StringBuffer sb=new StringBuffer(N);
  for(int i=0;i<N;i++){
    char c=S.charAt(i);            
    if(i==0 || bUpper) { 
        sb.append(Character.toUpperCase(c)); 
        bUpper = false; 
    } else {
        sb.append(c);
    }
    if(c==' ' || c==',') { 
        bUpper = true; 
    }    
    
  }
  return sb.toString();
}
public static String getShortStr(String s, String dflt, int nLimit){
 	if(s==null) s = dflt;
 	int nLength = s.length();
 	if(nLength>nLimit) {
 	  s = s.substring(0,nLimit);	
 	} 
 	return s;
}

    public static String encode64(String plainText)
    {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(plainText.getBytes());
    }

    public static String decode64(String encodedText)
        throws IOException
    {
        BASE64Decoder decoder = new BASE64Decoder();
        return new String(decoder.decodeBuffer(encodedText));
    }

    public static int BoolToInt(boolean Expression)
    {
        return !Expression ? 0 : 1;
    }

    public static boolean IntToBool(int Expression)
    {
        return Expression != 0;
    }

    public static String FloatToString(float value)
    {
        Float f = new Float(value);
        NumberFormat fmt = NumberFormat.getNumberInstance();
        String s = fmt.format(f.doubleValue());
        return s;
    }

    public static float StringToFloat(String value)
    {
        return Float.parseFloat(value);
    }

    public static Object IIf(boolean Expression, Object TruePart, Object FalsePart)
    {
        if(Expression)
            return TruePart;
        else
            return FalsePart;
    }

    public static String joinArray(Object array[])
    {
        String ret = "";
        for(int i = 0; i < array.length; i++)
        {
            ret = String.valueOf(ret) + String.valueOf(String.valueOf(String.valueOf((new StringBuffer("'")).append(String.valueOf(array[i])).append("'"))));
            if(i < array.length - 1)
                ret = String.valueOf(String.valueOf(ret)).concat(", ");
        }

        return ret;
    }

    public static String replace(String expression, String searchFor, String replaceWith)
    {
        if(expression != null)
        {
            StringBuffer buf = new StringBuffer(expression);
            int pos = -1;
            do
            {
                pos = buf.indexOf(searchFor, pos);
                if(pos > -1)
                {
                    buf.delete(pos, pos + searchFor.length());
                    buf.insert(pos, replaceWith);
                    pos += replaceWith.length();
                } else
                {
                    return buf.toString();
                }
            } while(true);
        } else
        {
            return null;
        }
    }

}
