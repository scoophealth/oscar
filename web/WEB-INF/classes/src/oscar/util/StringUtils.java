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
package oscar.util;

import org.apache.log4j.Category;

import java.io.*;

import java.util.*;
import java.util.regex.*;

public class StringUtils {
  static Category cat = Category.getInstance(StringUtils.class.getName());

  public static Vector splitString(String str, String delimeter) {
    Vector result = new Vector();
    StringTokenizer st = new StringTokenizer(str, delimeter);

    while (st.hasMoreTokens()) {
      result.addElement(st.nextToken());

    }
    return result;
  }

  public static boolean existsStrInVector(String str, String delimiter,
                                          String arrayStr) {
    Vector vector = splitString(arrayStr, delimiter);

    for (int i = 0; i < vector.size(); i++) {
      if (vector.get(i).equals(str)) {
        return true;
      }
    }

    return false;
  }

  public static boolean validateRegEx(String regex, String value) {
    try {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(value);
      boolean matchFound = matcher.matches();

      return matchFound;
    }
    catch (IllegalStateException e) {
      e.printStackTrace();
      cat.error("Erro ao validar expressao regular", e);

      return false;
    }
  }

  public static String getResourceLine(String resourceName,
                                       String resourceIten) {
    InputStream is = cat.getClass().getResourceAsStream("/" + resourceName);
    Properties props = new Properties();

    try {
      props.load(is);
      cat.debug("carregou " + resourceName);

      return props.getProperty(resourceIten);
    }
    catch (Exception e) {
      cat.error("Can't read the properties file. " + "Make sure " +
                resourceName + " is in the CLASSPATH", e);

      return null;
    }
  }

  public static String transformEmptyStringInNull(String value) {
    if (value != null) {
      return (value.equals("") ? "$null$" : value);
    }
    else {
      return value;
    }
  }

  public static String transformNullInEmptyString(String value) {
    return ( (value == null) ? "" : value);
  }

  public static String transformNullInOtherString(String value, String str) {
    return ( ( (value == null) || value.equals("")) ? str : value);
  }

  public static String preencheBranco(int n) {
    String espaco = "";

    for (int i = 0; i < n; i++) {
      espaco = espaco + " ";
    }

    return espaco;
  }

  public static String preenchimentoEsquerda(int n, String c, String c1) {
    String result = "";

    for (int i = 0; i < n; i++) {
      result = result + c;
    }

    return result + c1;
  }

  public static String preenchimento(int n, String c) {
    String result = "";

    for (int i = 0; i < n; i++) {
      result = result + c;
    }

    return result;
  }

  public static boolean isNullOrEmpty(String obj) {
    if (obj == null) {
      return true;
    }
    else if (obj.trim().equals("")) {
      return true;
    }
    else if (obj.trim().toUpperCase().equals("NULL")) {
      return true;
    }
    else {
      return false;
    }
  }

  public static String replaceChar(char oldChar, char newChar, String word) {
    return ( (word == null) ? null : word.replace(oldChar, newChar));
  }

  public static String getStrIn(String[] ids) {
    String id = "";

    for (int i = 0; i < ids.length; i++) {
      if (i == 0) {
        id = ids[i];
      }
      else {
        id = id + "," + ids[i];
      }
    }

    return id;
  }
  /**
  * Returns true if the provided string is a numeral
  * @todo Use a regex or find a suitable non-hackish way to do this!
  * @param str String
  * @return boolean
  */
 public static boolean isNumeric(String str){
   boolean ret = false;
   try{
     Double d = new Double(str);
     ret = true;
   }catch(NumberFormatException e){
   }finally{
     return ret;
   }
 }



}
