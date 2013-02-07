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

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

public class StringUtils {

    private static Logger logger = MiscUtils.getLogger();
    public final static String ELLIPSIS = "...";

    /**
     * use to have a maximum string length view
     * ie "hello world !!!" would be "hello wor..."
     *
     *  with maxlength 13 and shorted 8 and added "..."
     *
     * BENZOICUM ACIDUM 1CH - 30CH
     *
     *  would equal
     *
     * BENZOIC ...
     * @param maxlength The maximum string length before truncating the string
     * @param shorted length the string will be truncated to if maxlength is met
     * @param added string added to original string if maxlength is met.  ie ...
     * @return either full description if its less than maxlength or shortened string if its not
     */
    public static String maxLenString(String str, int maxlength, int shorted, String added) {
        String ret = str;
        if ((str != null && maxlength > shorted) && (str.length() > maxlength)) {
            ret = str.substring(0, shorted) + added;
        }
        return ret;
    }

    public static Vector splitString(String str, String delimeter) {
        Vector result = new Vector();
        StringTokenizer st = new StringTokenizer(str, delimeter);

        while (st.hasMoreTokens()) {
            result.addElement(st.nextToken());

        }
        return result;
    }

    public static boolean existsStrInVector(String str, String delimiter, String arrayStr) {
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
        } catch (IllegalStateException e) {
            MiscUtils.getLogger().error("Error", e);
            logger.error("Erro ao validar expressao regular", e);

            return false;
        }
    }

    public static String getResourceLine(String resourceName, String resourceIten) {
        InputStream is = logger.getClass().getResourceAsStream("/" + resourceName);
        Properties props = new Properties();

        try {
            props.load(is);
            logger.debug("carregou " + resourceName);

            return props.getProperty(resourceIten);
        } catch (Exception e) {
            logger.error("Can't read the properties file. " + "Make sure " + resourceName + " is in the CLASSPATH", e);

            return null;
        }
    }

    public static String transformEmptyStringInNull(String value) {
        if (value != null) {
            return (value.equals("") ? "$null$" : value);
        } else {
            return value;
        }
    }

    public static String transformNullInEmptyString(String value) {
        return ((value == null) ? "" : value);
    }

    public static String transformNullInOtherString(String value, String str) {
        return (((value == null) || value.equals("")) ? str : value);
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
        } else if (obj.trim().equals("")) {
            return true;
        } else if (obj.trim().toUpperCase().equals("NULL")) {
            return true;
        } else {
            return false;
        }
    }

    public static String replaceChar(char oldChar, char newChar, String word) {
        return ((word == null) ? null : word.replace(oldChar, newChar));
    }

    public static String getStrIn(String[] ids) {
        String id = "";

        for (int i = 0; i < ids.length; i++) {
            if (i == 0) {
                id = ids[i];
            } else {
                id = id + "," + ids[i];
            }
        }

        return id;
    }

    /**
     * Returns true if the provided string is a numeral
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        boolean ret = false;
        if (filled(str)) {
            try {
                new Double(str);
                ret = true;
            } catch (NumberFormatException e) {
                ret = false;
            }
            return ret;
        }
        return ret;
    }

    /**
     * Returns true if the provided string is an integer
     * @param str String
     * @return boolean
     */
    public static boolean isInteger(String str) {
        boolean ret = false;
        if (filled(str)) {
            try {
                new Integer(str);
                ret = true;
            } catch (NumberFormatException e) {
                ret = false;
            }
            return ret;
        }
        return ret;
    }

    public static String returnStringToFirst(String str, String firstChar) {
        String ret = str;
        if (str != null) {
            int i = str.indexOf(firstChar);
            if (i != -1) {
                ret = str.substring(0, i);
            }
        }
        return ret;
    }

    /**
     * Returns true if the specified String represents a valid date
     *
     * @param dateString String
     * @param format String
     * @return boolean
     */
    public static boolean isValidDate(String dateString, String format) {
        boolean ret = false;
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            fmt.parse(dateString);
            ret = true;
        } catch (ParseException ex) {
        	MiscUtils.getLogger().error("Looks bad, too bad original author didn't document how bad", ex);
        }
            return ret;

    }

    public static String readFileStream(FormFile file) {
        try {
            InputStream is = file.getInputStream();
            int pointer;
            StringBuilder strb = new StringBuilder(file.getFileSize());
            while ((pointer = is.read()) != -1) {
                strb.append((char) pointer);
            }
            return (strb.toString());
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return ("");
    }

    //joins an array into a string; array elements separated by a specified delimiter
    public static String join(String[] strArray, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, arrayLength = strArray.length; i < arrayLength; i++) {
            result.append(strArray[i]);
            if (i < arrayLength - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();
    }

    //arraylist elements must be string types
    public static String join(List strArray, String delimiter) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, arrayLength = strArray.size(); i < arrayLength; i++) {
            result.append(strArray.get(i));
            if (i < arrayLength - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();
    }

    public static ArrayList<String> split(String rawString, String delimiter) {
        ArrayList<String> result = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(rawString, delimiter);

        while (st.hasMoreTokens()) {
            result.add(st.nextToken());

        }
        return result;
    }

    public static String[] splitToStringArray(String rawString, String delimiter) {
        StringTokenizer st = new StringTokenizer(rawString, delimiter);
        String[] result = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            result[i] = (st.nextToken());
            i++;
        }
        return result;
    }

    /**
     *Takes a list of String Objects and returns a String with the all values from the list separated by a comma
     */
    public static String getCSV(List l) {
        StringBuilder ret = new StringBuilder();
        ;
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                ret.append((String) l.get(i));
                if (i + 1 < l.size()) {
                    ret.append(",");
                }
            }
        }
        return ret.toString();
    }

    /**
     * Strips linebreaks
     * Replace linebreaks and multiple spaces by a single space
     * johnchwk Apr 2008
     */
    public static String lineBreaks(String str) {
        StringBuilder mystringBuffer = new StringBuilder();
        mystringBuffer.append(str);

        boolean spaces = true;

        int position = 0;
        int strlen = (mystringBuffer.length());

        strlen--;                                // since position starts at 0

        // Convert all LB to spaces
        for (position = 0; position <= strlen; position++) {
            if (mystringBuffer.charAt(position) == '\r' || mystringBuffer.charAt(position) == '\n') {
                mystringBuffer.setCharAt(position, ' ');
            }
        }

        // Leave only single spaces
        position = 0;
        while (position <= strlen) {
            if (mystringBuffer.charAt(position) == ' ' && spaces) {
                mystringBuffer.deleteCharAt(position);
                strlen--;
            } else if (mystringBuffer.charAt(position) == ' ') {
                spaces = true;
                position++;
            } else {
                spaces = false;
                position++;
            }
        }

        return mystringBuffer.toString();
    }

    public static boolean nullSafeEquals(String s1, String s2) {
    	if (s1==null && s2==null) return true;
    	if (s1!=null)
    		return s1.equals(s2);
    	else
    		return s2==null;

    }

    public static boolean nullSafeEqualsIgnoreCase(String s1, String s2) {
		return nullSafeEquals(s1.toUpperCase(), s2.toUpperCase());
    }

    public static boolean containsIgnoreCase(String text, String searchWord) {
    	if (text==null || searchWord==null) return false;

    	text = text.toUpperCase();
    	searchWord = searchWord.toUpperCase();

    	return text.contains(searchWord);
    }

    static public String noNull(String maybeNullText) {
		return filled(maybeNullText) ? maybeNullText : "";
    }

    static public boolean empty(String s) {
		return isNullOrEmpty(s);
    }

    static public boolean filled(String s) {
		return !isNullOrEmpty(s);
    }
}
