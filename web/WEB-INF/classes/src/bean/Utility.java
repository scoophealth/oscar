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
package bean;

import java.io.*;
import java.util.*;

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
	makeContent=makeContent+"<br>"+tempString.substring(0,tempString.length()-1);
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
         // tempst = moveMoreBrs(tempst);

  return tempst ;

}

public String moveBackSlash(String Content){
String makeContent=new String();
String tempString;
StringTokenizer strToken=new StringTokenizer(Content,"\\");
	while(strToken.hasMoreTokens()){   
        tempString = strToken.nextToken();
 	makeContent=makeContent+"&#92;"+tempString.substring(0,tempString.length() );
        }
 	makeContent=makeContent.substring(5,makeContent.length());        
return makeContent;
}
public String changeSingleQuote(String Content){
String makeContent=new String();
String tempString = Content;
	while(tempString.indexOf("\'")>=0){   
	  makeContent=makeContent + tempString.substring(0,tempString.indexOf("\'"))+"&#146;" + tempString.substring(tempString.indexOf("\'"),tempString.length()-1);
           tempString = makeContent; 
        }

return tempString;
}
public String changeDoubleQuote(String Content){
String makeContent=new String();
String tempString;
StringTokenizer strToken=new StringTokenizer(Content,"\"");
	while(strToken.hasMoreTokens()){   
        tempString = strToken.nextToken();
	makeContent=makeContent+"&#34;"+tempString.substring(0,tempString.length());

        }
 	makeContent=makeContent.substring(5,makeContent.length());        
return makeContent;
}
public String moveMarks(String Content){
     String tempst = changeString(Content,"\'","&#146;"); //remove '
            tempst = changeString(tempst,"\"","&#34;");  // remove "
  return tempst ;

}
public String moveQuoteForTopic(String Content){
  String tempst = addBr(Content);       // add <br>
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
					sb.append(new Character((char)c).toString());
			}
		} catch (IOException ioe) {}
		
		return sb.toString();
	}


public String getToday(){
   String temp = ""; 
   GregorianCalendar gcl = new GregorianCalendar();
        int abc = new Integer(gcl.get(Calendar.MONTH)).intValue()+1;
        temp =gcl.get(Calendar.YEAR)+"-"+new Integer(abc).toString()+"-"+ gcl.get(Calendar.DAY_OF_MONTH);
    return temp;
}
public String getTime(){
   String temp = ""; 
   Date gcl = new Date();
         temp =new Integer(gcl.getHours()).toString()+ ":"+ new Integer(gcl.getMinutes()).toString();
     return temp;
}
public String changeString(String Content,String oldString,String newString){
String makeContent=new String();
String tempString = Content;
	while(tempString.indexOf(oldString)>=0){   
	  makeContent = tempString.substring(0,tempString.indexOf(oldString))+ newString + tempString.substring(tempString.indexOf(oldString)+1);
          tempString = makeContent; 
        }

return tempString;
}

}

