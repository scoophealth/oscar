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


public class StringSplitter{
  // to be used like StringTokenizer, but it can
  // return empty "tokens", and takes only one delim;
  // this may be a character or a string.
  String theString; char theDelim; int thePos;
  String theDelimStr=null; int theDelimLength;
  // nextToken is the token beginning at thePos

  public StringSplitter(String S,char d,int p){
    theString=S; theDelim=d; thePos=p; theDelimLength=1;
    if(thePos>=theString.length())thePos=-1;
    }

  public StringSplitter(String S,char d){this(S,d,0);}

  public StringSplitter(String S,String d,int p){
    theString=S; theDelimStr=d; thePos=p;
    theDelimLength=d.length();
    if(thePos>=theString.length())thePos=-1;
    }

  public StringSplitter(String S,String d){this(S,d,0);}

  public boolean hasMoreTokens(){return thePos>=0;}

  public String nextToken(){
    if(thePos<0) return null;
    int nextPos;
    if(theDelimStr==null) nextPos=theString.indexOf(theDelim,thePos);
    else nextPos=theString.indexOf(theDelimStr,thePos);
    String R;
    if(nextPos>=0){
       R=theString.substring(thePos,nextPos);
       thePos=nextPos+theDelimLength;
       }
    else {
      R=theString.substring(thePos);
      thePos=nextPos;
      }
    return R;
   } 
   
  } // end of StringSplitter utility class
