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

package oscar.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.oscarehr.util.MiscUtils;

public class UtilMisc {
  /**
   * @deprecated use apache's StringEscapeUtils instead.
   */
  public static String htmlEscape(String S) {

    if (null == S) {
      return S;
    }
    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (c == '&') {
        sb.append("&amp;");
      }
      else if (c == '"') {
        sb.append("&quot;");
      }
      else if (c == '<') {
        sb.append("&lt;");
      }
      else if (c == '>') {
        sb.append("&gt;");
      }
      else if (c == '\'') {
        sb.append("&#39;");
      }
    else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

 /**
  * For eformGenerator to Edit-Html window
  * This method is used to generate html symbols
  * eg. change '&lt' to  '<'
  *            '&gt' to '>'
  *
  */
  public static String rhtmlEscape(String S) {
    if (null == S) return S;

    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
        char c = S.charAt(i);
        if (c == '&') {//the read one more char and encode
            String temp =new String();
            if (i+1<N) temp += S.charAt(i+1);
            if(temp.equalsIgnoreCase("a")) {//&amp
              sb.append("&");
              i+=4;
              continue;
            } else if (temp.equalsIgnoreCase("l")) {//&lt
              sb.append("<");
              i+=3;
              continue;
            } else if (temp.equalsIgnoreCase("g")) {//&gt
              sb.append(">");
              i+=3;
              continue;
            } else if (temp.equalsIgnoreCase("q")) {//&quot
              sb.append("\"");
              i+=5;
              continue;
            } else if (temp.equals("#")) {//&#
                if (i+2<N) temp += S.charAt(i+2);//&#?
                if (i+3<N) temp += S.charAt(i+3);//&#??
                if (i+4<N) temp += S.charAt(i+4);//&#???
                if (temp.equals("&#39;")) {//'
                    sb.append("\'");
                    i+=5;
                    continue;
                }
            }
        }
        sb.append(c);
    }
    return sb.toString();
  }
  
  public static String charEscape(String S, char a) {
    if (null == S) {
      return S;
    }
    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (c == '\\') {
        sb.append("\\");
      }
      else if (c == a) {
        sb.append("\\" + a);
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * @deprecated use apache's StringEscapeUtils instead.
   */
  public static String htmlJsEscape(String S) {
    if (null == S) {
      return S;
    }
    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (c == '&') {
        sb.append("&amp;");
      }
      else if (c == '"') {
        sb.append("&quot;");
      }
      else if (c == '<') {
        sb.append("&lt;");
      }
      else if (c == '>') {
        sb.append("&gt;");
      }
      else if (c == '\'') {
        sb.append("&#39;");
      }
      else if (c == '\n') {
        sb.append("<br>");
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String nullMySQLEscape(String S) {
    if (null == S) {
      return S;
    }
    else {
      return "'" + mysqlEscape(S) + "'";
    }
  }

  public static String mysqlEscape(String S) {
    if (null == S) {
      return S;
    }
    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (c == '\\') {
        sb.append("\\");
      }
      else if (c == '\'') {
        sb.append("\\'");
      }
      else if (c == '\n') {
        sb.append("\\r\\n");
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * @deprecated use apache's StringEscapeUtils instead.
   */
  public static String JSEscape(String S) {
    if (null == S) {
      return S;
    }
    int N = S.length();
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (c == '"') {
        sb.append("&quot;");
      }
      else if (c == '\'') {
        sb.append("&#39;");
      }
      else if (c == '\n') {
        sb.append("<br>");
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String toUpperLowerCase(String S) {
    if (S == null) {
      return S;
    }
    S = S.trim().toLowerCase();
    int N = S.length();
    boolean bUpper = false;
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i < N; i++) {
      char c = S.charAt(i);
      if (i == 0 || bUpper) {
        sb.append(Character.toUpperCase(c));
        bUpper = false;
      }
      else {
        sb.append(c);
      }
      if (c == ' ' || c == ',') {
        bUpper = true;
      }
    }
    return sb.toString();
  }

  public static String getShortStr(String s, String dflt, int nLimit) {
    if (s == null) {
      s = dflt;
    }
    int nLength = s.length();
    if (nLength > nLimit) {
      s = s.substring(0, nLimit);
    }
    return s;
  }

  public static String encode64(String plainText) {
      return(new String(Base64.encodeBase64(plainText.getBytes())));
  }

  public static String decode64(String encodedText) {
      return(new String(Base64.decodeBase64(encodedText.getBytes())));
  }

  public static int BoolToInt(boolean Expression) {
    return!Expression ? 0 : 1;
  }

  public static boolean IntToBool(int Expression) {
    return Expression != 0;
  }

  public static String FloatToString(float value) {
    Float f = new Float(value);
    NumberFormat fmt = NumberFormat.getNumberInstance();
    String s = fmt.format(f.doubleValue());
    return s;
  }

  public static float StringToFloat(String value) {
    return Float.parseFloat(value);
  }

  /**
   * This method attempts to parse the provided String to a double value
   * If the value is non-numeric a value of 0.0 is returned.
   * @param value String
   * @return double
   */
  public static double safeParseDouble(String value) {
    double ret = 0.0;
    try {
      ret = Double.parseDouble(value);
    }
    catch (Exception ex) {MiscUtils.getLogger().error("Error", ex);
    }
    
      return ret;
    
  }

  public static Object IIf(boolean Expression, Object TruePart,
                           Object FalsePart) {
    if (Expression) {
      return TruePart;
    }
    else {
      return FalsePart;
    }
  }

  public static String joinArray(Object array[]) {
    String ret = "";
    for (int i = 0; i < array.length; i++) {
      ret = String.valueOf(ret)
          + String.valueOf(String.valueOf(String
                                          .valueOf( (new StringBuilder("'")).
          append(
              String.valueOf(array[i])).append("'"))));
      if (i < array.length - 1) {
        ret = String.valueOf(String.valueOf(ret)).concat(", ");
      }
    }
    return ret;
  }

  public static String replace(String expression, String searchFor,
                               String replaceWith) {
    if (expression != null) {
      StringBuilder buf = new StringBuilder(expression);
      int pos = -1;
      do {
        pos = buf.indexOf(searchFor, pos);
        if (pos > -1) {
          buf.delete(pos, pos + searchFor.length());
          buf.insert(pos, replaceWith);
          pos += replaceWith.length();
        }
        else {
          return buf.toString();
        }
      }
      while (true);
    }
    else {
      return null;
    }
  }

  /**
   * not quite qorking yet
    public static int[] range(int start, int stop, int step) {
      stop = stop < step ? start : step++;
      step = step < 1 ? 1 : step;
      int arrayLen = (stop - start) / step + (stop - start) % step;
      MiscUtils.getLogger().debug(arrayLen);
      int[] rangeArray = new int[arrayLen];
      for (int i = 0; i < arrayLen; i++) {
        if (i == 0) {
          rangeArray[i] = start;
        }
        else {
          rangeArray[i] = rangeArray[i - 1] + step;
        }
      }
      return rangeArray;
    }

    public static int[] range(int start, int stop) {
      return range(start, stop, 1);
    }
   }

   **/
  /**
   * Returns an int array with the specified number of elements in
   * @param length int
   * @return int[]
   */
  public static int[] range(int length) {
    int[] rangeArray = new int[length];
    for (int i = 0; i < length; i++) {
      rangeArray[i] = i;
    }
    return rangeArray;
  }

  /**
   * This method attempts to parse the provided String to an int value
   * If the value is non-numeric a value of 0 is returned.
   * @param value String
   * @return int
   */

  public static int safeParseInt(String value) {
    int ret = 0;
    try {
      ret = Integer.parseInt(value);
    }
    catch (Exception ex) {MiscUtils.getLogger().error("Error", ex);
    }
    
      return ret;
    
  }

  /**
   * Returns the provided double value rounded up to 2 decimal places
   * @param value double
   * @return double
   */
  public static double toCurrencyDouble(double value){
    BigDecimal bd = BigDecimal.valueOf(value );
    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
  }

  
  //returns a 2d array from the result set
  //Used by 'report by template'
  public static String[][] getArrayFromResultSet(ResultSet rs) throws SQLException {
	ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        ArrayList rows = new ArrayList();
        ArrayList cols = new ArrayList();
	for (int i=0; i<columns; i++) {  // for each column in result set
            cols.add(rsmd.getColumnName(i+1));
	}
        rows.add(cols);
        rs.first();
        do {
            cols = new ArrayList();
            for(int j=0; j<columns; j++) {
                cols.add(oscar.Misc.getString(rs,j+1));
            }
            rows.add(cols);
        } while (rs.next());
        String[][] data = new String[rows.size()][columns];
        for (int i=0; i<rows.size(); i++) {
            data[i] = (String[]) ((ArrayList) rows.get(i)).toArray(data[i]);
        }
	return data;
    }
}
