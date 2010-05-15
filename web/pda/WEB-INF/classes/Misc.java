  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import java.sql.ResultSet; // for conversions

public class Misc {

public static Hashtable hashDefs(String [] names, String [] values){
  Hashtable H=new Hashtable();
  if(names.length>values.length)return H;
  for(int i=0;i<names.length;i++)H.put(names[i],values[i]);
  return H;
}
public static String [] lookupHash(String [] names, Hashtable H){
  if(H==null)return new String[0];
  String [] R= new String[names.length];
  for(int i=0;i<names.length;i++)R[i]=(String)H.get(names[i]);
  return R;
}

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

public static String stringArrayJoin(String [] A, String S){
  if(A==null || A.length==0)return "";
  StringBuffer sb=new StringBuffer();
  sb.append(A[0]);
  for(int i=1;i<A.length;i++){sb.append(S);sb.append(A[i]);}
  return sb.toString();
}

public static String [] stringSplit(String S,char delim){
  Vector V=new Vector();
  StringSplitter SS=new StringSplitter(S,delim);
  while(SS.hasMoreTokens())V.addElement(SS.nextToken());
  return vectorToStringArray(V);
}
public static String [] stringSplit(String S,String delim){
  Vector V=new Vector();
  StringSplitter SS=new StringSplitter(S,delim);
  while(SS.hasMoreTokens())V.addElement(SS.nextToken());
  return vectorToStringArray(V);
}
public static String [] stringSplit(String S){ // delim==S[0]
  if(S==null || S.length()==0)return new String[0];
  char delim=S.charAt(0);
  Vector V=new Vector();
  StringSplitter SS=new StringSplitter(S,delim,1);
  while(SS.hasMoreTokens())V.addElement(SS.nextToken());
  return vectorToStringArray(V);
}
public static Hashtable splitDelimHash(String S){//delim=S[0]
  // S="xjoexSchmoexagex42xcommentsxxIQx42"
  // becomes joe="Schmoe",age="42",comments="",IQ="42"
  Hashtable H=new Hashtable(1);
  if(S==null || S.length()==0)return H;
  char delim=S.charAt(0);
  StringSplitter SS=new StringSplitter(S,delim,1);
  while(SS.hasMoreTokens()){
    String k=SS.nextToken();
    if(SS.hasMoreTokens())H.put(k,evalQuotedChars(SS.nextToken()));
    }
  return H;
}
public static String stringDelimSubst(String S,String d,Dict defs){
  // S contains keys, beginning and ending with copies of delim;
  // result is to be that of replacing these with their values
  String [] A =stringSplit(S,d);
  for(int i=1;i<A.length;i+=2)A[i]=defs.getDef(A[i]);
  return stringArrayJoin(A,"");
}


public static String stringDelimSubst(String S,String d,Hashtable defs){
  // S contains keys, beginning and ending with copies of delim;
  // result is to be that of replacing these with their values
  String [] A =stringSplit(S,d);
  for(int i=1;i<A.length;i+=2)A[i]=(String)defs.get(A[i]);
  return stringArrayJoin(A,"");
}

/*
public static String substFile(String fName, String fDelim, String defs){
  Hashtable dict=splitDelimHash(defs);
  if(dict==null)return("no definitions for "+fName+" in "+defs);
  return stringDelimSubst(MiscFile.fileToString(fName),fDelim,dict);
  }
*/  
public static String indent(int Level){
  String S="";while(0<Level--)S+="  ";return S;}

public static int getInt(String S,int dval){
  if(S==null)return dval;
  try{int N=Integer.parseInt(S);return N;}
  catch(Exception e){return dval;}
}
public static String getStr(String S,String dval){
  if(S==null)return dval;
  return S;
}
public static String evalQuotedChars(String S){
  String R="";
  for(int i=0;i<S.length();i++){
    char c=S.charAt(i);
    if(c!='\\')R+=""+c;
    else {i++;R+=""+S.charAt(i);}
    }
  return R;    
}
public static String quoteSpecialChars(String S,String specials){
  String R=""; // should use stringbuffer for efficiency?
  for(int i=0;i<S.length();i++){
    char c=S.charAt(i);
    if(specials.indexOf(c)>=0)R+="\\"+c;
    else R+=""+c;
    }
  return R;
    
}
public static String hashAttribString(Hashtable H){
  // returns the attribute string joe="schmoe" john="smith" &c.
  Enumeration KK=H.keys();
  String S=""; String specialChars="\\\"";
  while(KK.hasMoreElements()){
    String k=(String)KK.nextElement();
    String v=(String)H.get(k);
    S+=" "+k+"=\""+quoteSpecialChars(v,specialChars)+"\"";}
  return S;
}
public static Hashtable attribStringHash(String S){
  // interprets the attribute string joe="schmoe" john="smith" &c.
  // or joe='schmoe' john='smith' &c.
  // or even joe=qschmoeq john=qsmithq &c
  // but closing "quote" is required, and the string must be
  // _delimited_ by  blanks; no error checking yet.
  Hashtable H=new Hashtable(); int loc=0; int lim=S.length();
  while(loc<lim && ' '==S.charAt(loc))loc++;
  while(loc<lim){ // pointing, e.g., at john="smith" 
    int eqLoc=S.indexOf("=",loc); 
    if(eqLoc<0)return H;
    String k=S.substring(loc,eqLoc);
    char q=S.charAt(eqLoc+1);
    int endLoc=eqLoc+2; char c;
    while(endLoc<lim && (c=S.charAt(endLoc))!=q)
      if(c=='\\')endLoc+=2; else endLoc++;
    if(endLoc>lim)return H; // no closing quote
    String v=S.substring(eqLoc+2,endLoc);
    H.put(k,evalQuotedChars(v));
    loc=endLoc+2;
    while((loc<lim) && ' '==S.charAt(loc))loc++;
    }
  return H;
}
  
 public static String[] vectorToStringArray(Vector V){
  String [] S = new String[V.size()];
  for(int i=0;i<S.length;i++)S[i]=(String)V.elementAt(i);
  return S;
}


public static String[] column(int N,String[][]matrix){
  String[]col=new String[matrix.length];
  for(int i=0;i<col.length;i++){ 
    String[]row=matrix[i];
    if(row.length>N)col[i]=row[N]; // otherwise null
    }
  return col;
}

}
