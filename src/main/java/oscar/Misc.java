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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.MiscUtils;

public final class Misc {

	private Misc() {
		// prevent instantiation
	}

	public static Hashtable hashDefs(String[] names, String[] values) {
		Hashtable H = new Hashtable();
		if (names.length > values.length) return H;
		for (int i = 0; i < names.length; i++)
			H.put(names[i], values[i]);
		return H;
	}

	public static String getApplicationName(String sAppRootPath) {
		int idx = sAppRootPath.lastIndexOf('/');
		sAppRootPath = sAppRootPath.substring(0, idx);
		idx = sAppRootPath.lastIndexOf('/');
		sAppRootPath = sAppRootPath.substring(idx + 1);
		idx = sAppRootPath.lastIndexOf('.');
		if (idx > 0) sAppRootPath = sAppRootPath.substring(0, idx);
		return sAppRootPath;
	}

	public static String htmlEscape(String s) {
		return(StringEscapeUtils.escapeHtml(s));

//		if (null == S) return S;
//		int N = S.length();
//		StringBuilder sb = new StringBuilder(N);
//		for (int i = 0; i < N; i++) {
//			char c = S.charAt(i);
//			if (c == '&') sb.append("&amp;");
//			else if (c == '"') sb.append("&quot;");
//			else if (c == '<') sb.append("&lt;");
//			else if (c == '>') sb.append("&gt;");
//			else if (c == '\'') sb.append("&#39;");
//			else sb.append(c);
//		}
//		return sb.toString();
	}

	public static String charEscape(String S, char a) {
		if (null == S) return S;
		int N = S.length();
		StringBuilder sb = new StringBuilder(N);
		for (int i = 0; i < N; i++) {
			char c = S.charAt(i);
			if (c == '\\') sb.append("\\");
			else if (c == a) sb.append("\\" + a);
			else sb.append(c);
		}
		return sb.toString();
	}

	public static String htmlJsEscape(String s) {
		return(StringEscapeUtils.escapeHtml(s).replaceAll("\\n", "<br/>"));
		
//		if (null == S) return S;
//		int N = S.length();
//		StringBuilder sb = new StringBuilder(N);
//		for (int i = 0; i < N; i++) {
//			char c = S.charAt(i);
//			if (c == '&') sb.append("&amp;");
//			else if (c == '"') sb.append("&quot;");
//			else if (c == '<') sb.append("&lt;");
//			else if (c == '>') sb.append("&gt;");
//			else if (c == '\'') sb.append("&#39;");
//			else if (c == '\n') sb.append("<br>");
//			else sb.append(c);
//		}
//		return sb.toString();
	}

	public static String phoneNumber(String num) {
		String retval = num;
		try {
			retval = cleanNumber(num).substring(3);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return retval;
	}

	public static String areaCode(String num) {
		String retval = num;
		try {
			retval = cleanNumber(num).substring(0, 3);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return retval;
	}

	public static String cleanNumber(String Num) {
		Num = safeString(Num);
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\D");
		java.util.regex.Matcher m = p.matcher(Num);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return (0 == sb.toString().compareTo("")) ? "0" : sb.toString();
	}

	public static String safeString(String str) {
		return (null != str) ? str : "";
	}

	public static String mysqlEscape(String S) {
		if (null == S) return S;
		int N = S.length();
		StringBuilder sb = new StringBuilder(N);
		for (int i = 0; i < N; i++) {
			char c = S.charAt(i);
			if (c == '\\') sb.append("\\");
			else if (c == '\'') sb.append("\\'");
			else if (c == '\n') sb.append("\\r\\n");
			else sb.append(c);
		}
		return sb.toString();
	}

	public static String JSEscape(String S) {
		if (null == S) return S;
		int N = S.length();
		StringBuilder sb = new StringBuilder(N);
		for (int i = 0; i < N; i++) {
			char c = S.charAt(i);
			if (c == '"') sb.append("&quot;");
			else if (c == '\'') sb.append("&#39;");
			else if (c == '\n') sb.append("<br>");
			else sb.append(c);
		}
		return sb.toString();
	}

	public static String toUpperLowerCase(String S) {
		if (S == null) return S;
		S = S.trim().toLowerCase();
		int N = S.length();
		boolean bUpper = false;
		StringBuilder sb = new StringBuilder(N);
		for (int i = 0; i < N; i++) {
			char c = S.charAt(i);
			if (i == 0 || bUpper) {
				sb.append(Character.toUpperCase(c));
				bUpper = false;
			} else {
				sb.append(c);
			}
			if (c == ' ' || c == ',') {
				bUpper = true;
			}
		}
		return sb.toString();
	}

	public static String getShortStr(java.lang.String s, java.lang.String dflt, int nLimit) {
		if (s == null) s = dflt;
		int nLength = s.length();
		if (nLength > nLimit) {
			s = s.substring(0, nLimit);
		}
		return s;
	}

	public static String stringArrayJoin(String[] A, String S) {
		if (A == null || A.length == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(A[0]);
		for (int i = 1; i < A.length; i++) {
			sb.append(S);
			sb.append(A[i]);
		}
		return sb.toString();
	}

	public static String[] stringSplit(String S, char delim) {
		Vector V = new Vector();
		StringSplitter SS = new StringSplitter(S, delim);
		while (SS.hasMoreTokens())
			V.addElement(SS.nextToken());
		return vectorToStringArray(V);
	}

	public static String[] stringSplit(String S, String delim) {
		Vector V = new Vector();
		StringSplitter SS = new StringSplitter(S, delim);
		while (SS.hasMoreTokens())
			V.addElement(SS.nextToken());
		return vectorToStringArray(V);
	}

	public static String[] stringSplit(String S) { // delim==S[0]
		if (S == null || S.length() == 0) return new String[0];
		char delim = S.charAt(0);
		Vector V = new Vector();
		StringSplitter SS = new StringSplitter(S, delim, 1);
		while (SS.hasMoreTokens())
			V.addElement(SS.nextToken());
		return vectorToStringArray(V);
	}

	public static Hashtable splitDelimHash(String S) {// delim=S[0]
		// S="xjoexSchmoexagex42xcommentsxxIQx42"
		// becomes joe="Schmoe",age="42",comments="",IQ="42"
		Hashtable H = new Hashtable(1);
		if (S == null || S.length() == 0) return H;
		char delim = S.charAt(0);
		StringSplitter SS = new StringSplitter(S, delim, 1);
		while (SS.hasMoreTokens()) {
			String k = SS.nextToken();
			if (SS.hasMoreTokens()) H.put(k, evalQuotedChars(SS.nextToken()));
		}
		return H;
	}

	public static String stringDelimSubst(String S, String d, Dict defs) {
		// S contains keys, beginning and ending with copies of delim;
		// result is to be that of replacing these with their values
		String[] A = stringSplit(S, d);
		for (int i = 1; i < A.length; i += 2)
			A[i] = defs.getDef(A[i]);
		return stringArrayJoin(A, "");
	}

	public static String stringDelimSubst(String S, String d, Hashtable defs) {
		// S contains keys, beginning and ending with copies of delim;
		// result is to be that of replacing these with their values
		String[] A = stringSplit(S, d);
		for (int i = 1; i < A.length; i += 2)
			A[i] = (String) defs.get(A[i]);
		return stringArrayJoin(A, "");
	}

	/*
	 * public static String substFile(String fName, String fDelim, String defs){ Hashtable dict=splitDelimHash(defs); if(dict==null)return("no definitions for "+fName+" in "+defs); return stringDelimSubst(MiscFile.fileToString(fName),fDelim,dict); }
	 */
	public static String indent(int Level) {
		String S = "";
		while (0 < Level--)
			S += "  ";
		return S;
	}

	public static int getInt(String S, int dval) {
		if (S == null) return dval;
		try {
			int N = Integer.parseInt(S);
			return N;
		} catch (Exception e) {
			return dval;
		}
	}

	public static String getStr(String S, String dval) {
		if (S == null) return dval;
		return S;
	}

	public static String evalQuotedChars(String S) {
		String R = "";
		for (int i = 0; i < S.length(); i++) {
			char c = S.charAt(i);
			if (c != '\\') R += "" + c;
			else {
				i++;
				R += "" + S.charAt(i);
			}
		}
		return R;
	}

	public static String quoteSpecialChars(String S, String specials) {
		String R = ""; // should use stringbuffer for efficiency?
		for (int i = 0; i < S.length(); i++) {
			char c = S.charAt(i);
			if (specials.indexOf(c) >= 0) R += "\\" + c;
			else R += "" + c;
		}
		return R;

	}

	public static String hashAttribString(Hashtable H) {
		// returns the attribute string joe="schmoe" john="smith" &c.
		Enumeration KK = H.keys();
		String S = "";
		String specialChars = "\\\"";
		while (KK.hasMoreElements()) {
			String k = (String) KK.nextElement();
			String v = (String) H.get(k);
			S += " " + k + "=\"" + quoteSpecialChars(v, specialChars) + "\"";
		}
		return S;
	}

	public static Hashtable attribStringHash(String S) {
		// interprets the attribute string joe="schmoe" john="smith" &c.
		// or joe='schmoe' john='smith' &c.
		// or even joe=qschmoeq john=qsmithq &c
		// but closing "quote" is required, and the string must be
		// _delimited_ by blanks; no error checking yet.
		Hashtable H = new Hashtable();
		int loc = 0;
		int lim = S.length();
		while (loc < lim && ' ' == S.charAt(loc))
			loc++;
		while (loc < lim) { // pointing, e.g., at john="smith"
			int eqLoc = S.indexOf("=", loc);
			if (eqLoc < 0) return H;
			String k = S.substring(loc, eqLoc);
			char q = S.charAt(eqLoc + 1);
			int endLoc = eqLoc + 2;
			char c;
			while (endLoc < lim && (c = S.charAt(endLoc)) != q)
				if (c == '\\') endLoc += 2;
				else endLoc++;
			if (endLoc > lim) return H; // no closing quote
			String v = S.substring(eqLoc + 2, endLoc);
			H.put(k, evalQuotedChars(v));
			loc = endLoc + 2;
			while ((loc < lim) && ' ' == S.charAt(loc))
				loc++;
		}
		return H;
	}

	public static String stripLineBreaks(String input) {
		if (input != null) {
			input = input.replaceAll("\\n", " ").replaceAll("\\r", "");
		}
		return input;
	}

	public static String insertDecimalPoint(String input) {
		String moneyStr = "0.00";
		try {
			moneyStr = new java.math.BigDecimal(input).movePointLeft(2).toString();
		} catch (Exception moneyException) {
			MiscUtils.getLogger().error("Error", moneyException);
		}
		return moneyStr;
	}

	public static String check(String check, String checkAgainst, String defaultValue) {
		return ((check == checkAgainst) ? defaultValue : check);
	}

	public static String check(String check, String defaultValue) {
		return check(check, null, defaultValue);
	}

	public static String[] vectorToStringArray(Vector V) {
		String[] S = new String[V.size()];
		for (int i = 0; i < S.length; i++)
			S[i] = (String) V.elementAt(i);
		return S;
	}

	public static String[] column(int N, String[][] matrix) {
		String[] col = new String[matrix.length];
		for (int i = 0; i < col.length; i++) {
			String[] row = matrix[i];
			if (row.length > N) col[i] = row[N]; // otherwise null
		}
		return col;
	}

	public static String removeNewLine(String str) {
		StringBuilder stringBuffer = new java.lang.StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			int a = str.charAt(i);
			if (a == 13 || a == 10) {
				stringBuffer.append(" ");
			} else {
				stringBuffer.append((char) a);
			}
		}
		return stringBuffer.toString();
	}

	// /

	public static String space(int i) {
		String returnValue = new String();
		for (int j = 0; j < i; j++) {
			returnValue += " ";
		}
		return returnValue;
	}

	public static String backwardSpace(String y, int i) {
		String returnValue = new String();
		y = safeString(y);
		for (int j = y.length(); j < i; j++) {
			returnValue += " ";
		}
		return cutBackString(y + returnValue, i);
	}

	public static String zero(int x) {
		String returnZeroValue = new String();
		for (int y = 0; y < x; y++) {
			returnZeroValue += "0";
		}
		return returnZeroValue;
	}

	public static String forwardZero(String y, int x) {
		String returnZeroValue = new String();
		y = safeString(y);
		for (int i = y.length(); i < x; i++) {
			returnZeroValue += "0";
		}
		return cutFrontString(returnZeroValue + y, x);
	}

	public static String backwardZero(String y, int i) {
		String returnValue = new String();
		y = safeString(y);
		for (int j = y.length(); j < i; j++) {
			returnValue += "0";
		}
		return cutBackString(y + returnValue, i);
	}

	public static String cutFrontString(String str, int len) {
		return str.substring(str.length() - len, str.length());
	}

	public static String cutBackString(String str, int len) {
		if (str != null && str.length() < len) {
			return str;
		}
		return str.substring(0, len);
	}

	public static String forwardSpace(String y, int x) {
		String returnZeroValue = new String();
		y = safeString(y);
		for (int i = y.length(); i < x; i++) {
			returnZeroValue += " ";
		}
		return cutFrontString(returnZeroValue + y, x);
	}

	public static String moneyFormatPaddedZeroNoDecimal(String y, int x) {
		String returnZeroValue = "";
		try {
			returnZeroValue = forwardZero(y.replaceAll("\\.", ""), x);
		} catch (Exception e2) {
			returnZeroValue = zero(x);
		}
		return cutFrontString(returnZeroValue, x);
	}

	public static String moneyFormat(String str) {
		String moneyStr = "0.00";
		try {
			moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
		} catch (Exception moneyException) {
		}
		return moneyStr;
	}

	public static String getString(ResultSet rs, String columnName) throws SQLException {
		return(StringUtils.trimToEmpty(rs.getString(columnName)));
		
//		String text = rs.getString(columnName);
//		if (rs.wasNull()) {
//			text = "";
//		}
//		return text;
	}

	public static String getString(ResultSet rs, int columnIndex) throws SQLException {
		return(StringUtils.trimToEmpty(rs.getString(columnIndex)));

//		String text = rs.getString(columnIndex);
//		if (rs.wasNull()) {
//			text = "";
//		}
//		return text;
	}

	public static String getString(Object s) {
		if (s == null) return "";
		return (String) s;
	}

	public static String replace(String str, String pattern, String replaceTo) {
		String[] buff = str.split(pattern);
		StringBuilder sb = new StringBuilder();

		sb.append(buff[0]);
		for (int i = 1; i < buff.length; i++) {
			sb.append(replaceTo);
			sb.append(buff[i]);
		}
		if (str.endsWith(pattern)) sb.append(replaceTo);

		return sb.toString();
	}

	public static String getStringJs(Object s) {
		if (s == null) return "";
		String s1 = replace((String) s, "'", "\\'");
		return replace(s1, "\"", "&#34;");
		// return ((String) s).replace("'", "\\'");
	}

	public static String encryptPIN(String sPin) {
		StringBuilder sb = new StringBuilder();
		int i, j;
		if (sPin == null) return null;

		j = 0;
		for (i = 0; i < sPin.length(); i++) {
			char c = sPin.charAt(i);
			j = j + c;
			if (j > 127) j = j - 127;
			sb.append((char) j);
		}
		return sb.toString();
	}

	public static String getRandomNumber(int digits) {
		int max = (int) Math.pow(10, digits) - 1;
		java.util.Date dt = new java.util.Date();
		long seed = dt.getTime();
		java.util.Random rnd = new java.util.Random(seed);
		int rn = rnd.nextInt(max);
		return forwardZero(String.valueOf(rn), digits);
	}
}
