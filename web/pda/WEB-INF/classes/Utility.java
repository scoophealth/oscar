package bean;

import java.io.*;
import java.util.*;

/*
 * $RCSfile: AbstractApplication.java,v1.0 $
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * Tom Zhu
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

public class Utility{

public String RemoveComment(String Content){
String makeContent=new String();
StringTokenizer strToken = new StringTokenizer(Content,"\n");
String tempToken=null;

while(strToken.hasMoreTokens()){
tempToken=strToken.nextToken();
if(tempToken.indexOf(":")!=0)
makeContent=makeContent+tempToken+"\n";
makeContent=makeContent.substring(4);
makeContent=makeContent.substring(0,(makeContent.length()-1));
}

return makeContent;
}


public String addBr(String Content){
String makeContent=new String();
String tempString;
StringTokenizer strToken=new StringTokenizer(Content,"\n");
	while(strToken.hasMoreTokens()){
	//makeContent=makeContent+"<br>"+strToken.nextToken();
	tempString = strToken.nextToken();
	makeContent=makeContent+"<br>"+tempString.substring(0,tempString.length() );
	}
        makeContent=makeContent.substring(4,makeContent.length());

return makeContent;
}

public String moveMoreBrs(String Content){
  String a = new String();
  if (Content.indexOf("<br><br>")<0){
         a = Content;
  }else{
	 while (Content.indexOf("<br><br>")>=0){
	  a = Content.substring(0,Content.indexOf("<br><br>")+3)+Content.substring(Content.indexOf("<br><br>")+7 ,Content.length());	
	  Content = a;
	 }
  }
 return a;
}
	
	
public String moveSingleQuote(String Content){
String makeContent=new String();
String tempString;
StringTokenizer strToken=new StringTokenizer(Content,"\'");
	while(strToken.hasMoreTokens()){   
        tempString = strToken.nextToken();
//	makeContent=makeContent+"\\'"+tempString.substring(0,tempString.length()-1);
	makeContent=makeContent+"&#146;"+tempString.substring(0,tempString.length() );

        }
 	makeContent=makeContent.substring(6,makeContent.length());        
return makeContent;
}

public String moveDoubleQuote(String Content){
String makeContent=new String();
String tempString;
StringTokenizer strToken=new StringTokenizer(Content,"\"");
	while(strToken.hasMoreTokens()){   
        tempString = strToken.nextToken();
// 	makeContent=makeContent+"\\\""+tempString.substring(0,tempString.length()-1);
 	makeContent=makeContent+"&#34;"+tempString.substring(0,tempString.length() );
        }
 	makeContent=makeContent.substring(5,makeContent.length());        
return makeContent;
}

public String moveQuote(String Content){
  String tempst = addBr(Content);       // add <br>
        // tempst = RemoveComment(tempst);
        // tempst = moveDoubleQuote(moveSingleQuote(tempst));  // remove ", ' 
        
          tempst = moveSingleQuote(tempst); 
          tempst = moveDoubleQuote(tempst);  // remove "
          tempst = moveMoreBrs(tempst);

  return tempst ;

}

public String moveQuoteForTopic(String Content){
  String tempst = addBr(Content);       // add <br>
        // tempst = RemoveComment(tempst);
        // tempst = moveDoubleQuote(moveSingleQuote(tempst));  // remove ", ' 
        
          tempst = moveSingleQuote(tempst); 
          tempst = moveDoubleQuote(tempst);  // remove "
 

  return tempst ;

}

public String addSpace(String Content){
  String a = new String();
  if (Content.indexOf(" ")<0){
         a = Content;
  }else{
	 while (Content.indexOf(" ")>=0){
	  a = Content.substring(0,Content.indexOf(" "))+"&nbsp;"+Content.substring(Content.indexOf(" ")+1 ,Content.length());	
	  Content = a;
	 }
          
  }
 return a;
}

public String highLight(String Content,String keyWord){
  String a = new String();
  if (Content.indexOf(keyWord)<0){
         a = Content;
  }else{
	 int i = 0;
 	 while (Content.indexOf(keyWord,i)>=0){
	     a = Content.substring(0,Content.indexOf(keyWord,i))+"<b>"+keyWord+"</b>" +Content.substring(Content.indexOf(keyWord,i)+ keyWord.length() ,Content.length());	
             i = Content.indexOf(keyWord,i) + keyWord.length() + 7;
             Content = a;
 	 }
          
  }
 return a;
}

public String detectHyperlink(String Content ){
  String a = new String();
  String b = new String();
  int i, j; 
      i = Content.indexOf("http://");

      
  if (i>=0){
           j = Content.indexOf("&#34",i);
            if (j>=0){
              a = Content.substring(i,j).trim();
           }else{
              a = "";
           }  
  }else{
         a = "";
          
  }

 return a;
}



public static String showAsciiStream(InputStream in, int show) {
	
		if ( in == null)
			return "null";
			
		StringBuffer sb = new StringBuffer("");
		
		int i = 0;
		int c = 0;

		try {		
			while ((i++ < show) && ((c = in.read()) != -1) ) {
//				if ( Character.isISOControl((char)c) )
//					sb.append("0x").append(Integer.toHexString(c).toString()).append(",");
// System.out.println(i+"---2-----"+new Character((char)c).charValue()+"==");

//  			     if ( Character.isISOControl((char)c)) {
//                                 if ( (new Character(c)).equals("\n")){  

// System.out.println(i+"---1*****"+Character.getNumericValue((char)c)+"==");
// System.out.println(i+"---2-----"+new Character((char)c).charValue()+"==");
//                                  if ( (new Character((char)c).charValue()=='\u0010')||(new Character((char)c).charValue()==13) ){  
//                                  if ( (new Character((char)c).charValue()==10)||(new Character((char)c).charValue()==13) ){  

// System.out.println("---1++++++++++==+++++++++++++++++++++++++++++++++++++++===");
//                                        if ( (Character.getNumericValue((char)c)== '\u0010')||(Character.getNumericValue((char)c)== '\u0013' )){  
//					sb.append(Integer.toHexString(c).toString());
//      					sb.append("<br>");
//  				       }else{
//					sb.append(Integer.toHexString(c).toString());
                                        
 //					sb.append("");
//					sb.append("<br>");
  
//                                       } 
//				}else{
//					sb.append("'").append(new Character((char)c).toString()).append("',");
					sb.append(new Character((char)c).toString());
//                                } 
			}
		} catch (IOException ioe) {}
		
		return sb.toString();
	}

}

