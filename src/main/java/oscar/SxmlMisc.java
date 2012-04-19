/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class SxmlMisc extends Properties {

  //get the xml string 
  public static String createXmlDataString(HttpServletRequest req, String strPrefix) {
    String temp=null, content="";//default is not null
    StringBuilder sbContent=new StringBuilder("");
	  for (Enumeration e = req.getParameterNames() ; e.hasMoreElements() ;) {
		  temp=e.nextElement().toString();
		  if( !temp.startsWith(strPrefix) || req.getParameter(temp).equals("")) continue;
  	  sbContent = sbContent.append("<").append(temp).append(">").append( SxmlMisc.replaceHTMLContent(req.getParameter(temp)) ).append("</").append(temp).append(">");  	//Content+="<" +temp+ ">" +request.getParameter(temp)+ "</" +temp+ ">";
    }
    content=sbContent.toString();
    return content;
  }

  //get a string 
  public static String createDataString(HttpServletRequest req, String strPrefix, String defaultValue, int maxsize) {
    String temp=null;//default is not null
    //StringBuilder sbContent=new StringBuilder("");
    byte[] abyte = new byte[maxsize];
    int i=0,n=0;
    for (Enumeration e = req.getParameterNames() ; e.hasMoreElements() ;) {
	    temp=e.nextElement().toString();
	    if( temp.startsWith(strPrefix) ) {
	      i = Integer.parseInt(temp.substring(strPrefix.length()));
	      abyte[i] = (byte) ((req.getParameter(temp).equals("")||req.getParameter(temp).equals(" "))?defaultValue.charAt(0):req.getParameter(temp).charAt(0)) ;
	      n++;
        //sbContent = sbContent.append(req.getParameter(temp).equals("")?defaultValue:req.getParameter(temp));
      }
    }
    //String content=new String(abyte,0,n);
    //content=sbContent.toString();
    return new String(abyte,0,n);
  }

  //parse the xml string and store their properties
  public void setXmlStringProp(String strXml) {
  	//parse strXml
  	String name="", val=null;
    setProperty(name.toUpperCase(), val);
  }
  //get the value between the tags from a string
  public static String getXmlContent(String str, String sTag, String eTag) {
  	if(str==null) return null;
  	
	int s = str.indexOf(sTag);
  	int e = str.indexOf(eTag);
  	String val = "";
  	if(s==-1 || e==-1) return val;
  	val = str.substring(s+sTag.length(),e);

  	return val;
  }
  public static String getXmlContent(String str, String sTagValue) {
  	return (getXmlContent(str, "<"+sTagValue+">", "</"+sTagValue+">"));
  }
  
  //change the input string from null to "", or non-null to non-null
  public static String getReadableString(String str) {
  	String val = str==null?"":str;
  	return val;
  }

  //replace the new value between the tags from a string
  public static String replaceXmlContent(String str, String sTag, String eTag, String newVal) {
  	int s = str.indexOf(sTag);
  	int e = str.indexOf(eTag);
  	String newStr = str;
  	if(s!=-1 && e!=-1)
  	  newStr = str.substring(0,s+sTag.length()) + newVal + str.substring(e);

  	return newStr;
  }
  //replace or add the new value between the tags from a string
  public static String replaceOrAddXmlContent(String str, String sTag, String eTag, String newVal) {
  	int s = str.indexOf(sTag);
  	int e = str.indexOf(eTag);
  	String newStr = str;
  	if(s!=-1 && e!=-1)
  	  newStr = str.substring(0,s+sTag.length()) + newVal + str.substring(e);
  	else
  	  newStr = str + sTag + newVal + eTag;

  	return newStr;
  }

  //replace the new value of the old value
  public static String replaceString(String str, String oldstr, String newstr) {
  	int s = str.indexOf(oldstr);
  	int stemp = 0;
  	while(s>=0) {
  		s = stemp + s;
  	  str = str.substring(0,s) + newstr + str.substring(s+oldstr.length());
  	  stemp = s+newstr.length();

  	  s = str.substring(stemp).indexOf(oldstr);

  	} 
  	return str;
  }

  public static String replaceHTMLContent(String str) {
  	str = SxmlMisc.replaceString(str,"&","&amp;");
  	str = SxmlMisc.replaceString(str,">","&gt;");
  	str = SxmlMisc.replaceString(str,"<","&lt;");
//  	str = SxmlMisc.replaceString(str,"'","&apos;");
//  	str = SxmlMisc.replaceString(str,"\"","&quot;");
  	return str;
  }

}
