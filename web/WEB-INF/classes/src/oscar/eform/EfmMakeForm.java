/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.eform ; 

import java.util.*; 
import java.net.*; 
import oscar.util.*; 

public class EfmMakeForm { 
  private String content ; 
 
  public String addContent(String str, String [] meta, String [] value, String [][] tag) {  //str - form_html
    String strTemp = null;
    strTemp = getRlt(str, meta, value, tag) ; 
    return strTemp ; 
  }

  private String getRlt(String str, String[] meta, String[] value, String[][] afterStr) {
    String strTemp = str;
    int fromIndex = 0;
    int posIndex = 0;
    int lPos = 0; // < pos
    int mPos = 0; // > pos

    for(int i=0; i<meta.length; i++) {
      for(;;) {
        posIndex = strTemp.toLowerCase().indexOf(meta[i].toLowerCase(), fromIndex) ; //find the meta name
        //System.out.println(meta[i]+": - :"+strTemp.charAt(posIndex+meta[i].length()) + ": :" +value[i]);
        if ( posIndex  > 0 ) { 
          if (!(strTemp.charAt(posIndex+meta[i].length())=='"' || strTemp.charAt(posIndex+meta[i].length())==' ' || strTemp.charAt(posIndex+meta[i].length())=='\'' || strTemp.charAt(posIndex+meta[i].length())=='>')) {
            fromIndex = posIndex + meta[i].length();continue; //should find exact the meta name, not partly
          }

          mPos = strTemp.indexOf('>', posIndex) ;
          lPos = strTemp.indexOf('<', posIndex) ;
          if (mPos<lPos || (lPos<0 && mPos>0) ) { //in tag
            //if the posIndex+meta[i].length()(char) = " or ', then posIndex should be ++ to avoid inside " " or ' '.
            if (strTemp.charAt(posIndex-1)=='"' || strTemp.charAt(posIndex+meta[i].length())=='\'' )
            {  posIndex++;  }
            String aftStr = getSymbol(strTemp.substring(strTemp.substring(0,posIndex).lastIndexOf('<'), posIndex) , afterStr);
            //System.out.println("meta is :"+meta[i]+"  |value is :"+ value[i] + " after :"+ aftStr +" at :"+posIndex+value[i]);
            strTemp = changeMeta(strTemp, meta[i], value[i], aftStr, posIndex) ;
            break ;
          }
          fromIndex = posIndex + meta[i].length();
        } else break;
      }
      fromIndex = 0;
    }
    return strTemp ;
  }
  private String getSymbol(String strHTML, String[][] afterStr) { // is that "" or ">" or somethingelse? 
    String temp = "";
    for(int i = 0; i < afterStr.length; i++) {
      if(strHTML.toLowerCase().lastIndexOf(afterStr[i][0].toLowerCase()) >= 0) temp = afterStr[i][1];
    }
    return temp;
  }
  private String changeMeta(String strHTML, String strMeta, String metaVar, String fromSymbol, int fromIndex) {
    String temp = null;
    int pos = fromIndex+strMeta.length() ; //
    if(fromSymbol.length() > 0 ) pos = strHTML.indexOf(fromSymbol, pos ) + fromSymbol.length() ;

    temp = strHTML.substring(0,pos ) + metaVar + strHTML.substring(pos )  ;
    return temp;
  }

  public String mixNewForm(String str, String[] speMeta, String[] speValue, String[][] speAfterStr, String[] genName) {
    String temp = new String();
    temp = getRlt(str, speMeta, speValue, speAfterStr) ; 

    return temp; 
  }
}
