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
