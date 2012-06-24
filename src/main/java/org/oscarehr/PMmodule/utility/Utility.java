/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class Utility {
	private static Logger log = MiscUtils.getLogger();

	// ################################################################################
	private Utility() {
	}

	/*
	 * ##########################################################################
	 * ##
	 */
	public static String[][] convertArrayListTo2DStringArr(List arr_arrayList) {
		if (arr_arrayList == null || arr_arrayList.size() <= 0) {
			return null;
		}

		String[][] returnStrArr = null;

		try {
			for (int i = 0; i < arr_arrayList.size(); i++) {
				List arr_arrayListCol = (List) arr_arrayList.get(i);
				Object[] obj = arr_arrayListCol.toArray();

				if (i == 0) {
					returnStrArr = new String[arr_arrayList.size()][obj.length];
				}

				for (int j = 0; j < obj.length; j++) {
					if (obj[j] != null) {

						returnStrArr[i][j] = obj[j].toString();
						// returnStrArr[i][j] = obj[j].toString().trim();
						if (returnStrArr[i][j] == null) {
							returnStrArr[i][j] = "_";
						}
					}
				}// end of for(int j=0; j < obj.length; j++)

			}// end of for(int i=0; i < v_vector.size(); i++)
		} catch (Exception ex) {
		}
		return returnStrArr;

	}

	// ###################################################################################
	public static String replaceHTML(String str, String what, String withWhat) {
		if (str == null || str.length() <= 0) {
			return "";
		}

		String result = "";
		while (str.indexOf(what) != -1) {
			int v1 = str.indexOf(what);
			result += str.substring(0, v1) + withWhat;
			str = str.substring(v1 + what.length());
		}
		return result + str;
	}

	// ###################################################################################
	public static String replaceStrWith(String str, String what, String withWhat) {
		if (str == null || str.length() <= 0) {
			return "";
		}

		String result = "";
		while (str.indexOf(what) != -1) {
			int v1 = str.indexOf(what);
			result += str.substring(0, v1) + withWhat;
			str = str.substring(v1 + what.length());
		}
		return result + str;
	}

	// ###################################################################################
	public static String convertToReplaceStrIfEmptyStr(String str,
			String replaceWith) {
		if (str == null || str.equals("")) {
			str = replaceWith;

			return str;
		}
		return str;

	}

	// ###################################################################################

	public static String escapeHTML(String str) {
		if (str == null)
			return "";

		str = replaceHTML(str, "&", "&amp;");
		str = replaceHTML(str, "<", "&lt;");
		str = replaceHTML(str, ">", "&gt;");
		str = replaceHTML(str, "\"", "&quot;");
		str = replaceHTML(str, "'", "&#39");
		return str;
	}

	// ###################################################################################

	public static String escapeSQL(String str) {
		if (str == null)
			return "";

		str = replaceStrWith(str, "'", "''");

		return str;
	}

	// ###################################################################################

	public static String filterSQLFromHacking(String str) {
		if (str == null)
			return "";

		str = replaceStrWith(str, "delete", " "); // ???
		str = replaceStrWith(str, "update", " "); // ???
		str = replaceStrWith(str, "drop", " "); // ???
		str = replaceStrWith(str, ";", " "); // ???

		return str;
	}

	// ###################################################################################
	public static String replaceURL(String str, String what, String withWhat) {
		if (str == null || str.length() <= 0) {
			return null;
		}

		String result = "";
		while (str.indexOf(what) != -1) {
			int v1 = str.indexOf(what);
			result += str.substring(0, v1) + withWhat;
			str = str.substring(v1 + what.length());
		}
		return result + str;
	}

	// ###################################################################################

	public static String escapeURL(String str) {
		if (str == null)
			return "";

		str = replaceURL(str, "?", "!");
		str = replaceURL(str, "&", "and");
		str = replaceURL(str, "=", "-");
		str = replaceURL(str, "\"", "_");

		str = replaceURL(str, "\\", "/"); // for mainpic updates in experiences
											// table
		return str;
	}

	// ###################################################################################
	public static String convertToEmptyStrIfNull(String str) {
		if (str != null) {
			return str;
		}

		str = "";
		return str;
	}

	// ###################################################################################
	public static String escapeNull(Object obj) {
		if (obj != null) {
			return obj.toString();
		}

		return "";
	}

	// ###################################################################################
	public static String escapeNull(String str) {
		if (str != null) {
			return str;
		}

		str = "";
		return str;
	}

	// ###################################################################################
	public static Date escapeNull(Date dateObj) {
		if (dateObj != null) {
			return dateObj;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 0001);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date defaultDate = cal.getTime();
		return defaultDate;
	}

	// ###################################################################################
	public static String convertToRelacementStrIfNull(String str,
			String replaceWith) {
		if (str != null) {
			return str;
		}

		return replaceWith;
	}

	// ################################################################################

	public static String[] separateStrComponentsIntoStrArray(String str,
			String delimiter) {

		// strTokens[10] =
		// ~MINP4~MAXA4~MAXP6~MAXC2~INFY~ROOMTYPEPHS~OCCUPCODEQAD
		String strComponents = str;

		int count = 0;
		StringTokenizer strTokenizer = new StringTokenizer(strComponents,
				delimiter);
		int tokensCount = strTokenizer.countTokens();
		String[] strTokens = new String[tokensCount];

		while (strTokenizer.hasMoreTokens()) {
			strTokens[count] = strTokenizer.nextToken();
			count++;
		}

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		return strTokens;
	}

	// ############################################################################

	public static String[] getParamsFromPosting(HttpServletRequest request,
			String keyPrefix) {
		// this method only filters out the keyNames that has a nonempty
		// keyValue -- others don't work

		if (keyPrefix == null || keyPrefix.length() <= 0 || request == null) {
			return null;
		}

		List arr_keyValues = new ArrayList();
		String keyName, keyValue;
		boolean nothingChecked = true;
		int numChecked = 0;
		String[] paramList = null;
		try {
			Enumeration params = request.getParameterNames(); // receives all
																// the posted
																// params

			while (params.hasMoreElements()) {
				keyName = (String) params.nextElement();

				if (keyName.startsWith(keyPrefix)) {
					keyValue = request.getParameter(keyName); // 0 or 1

					if (!keyValue.equals("")) // ie. checked checkboxes
					{
						arr_keyValues.add(keyValue); //
						nothingChecked = false;
					} else // ie. unchecked checkboxes
					{
						// Do nothing for the unchecked msg rows
					}
				}
			}

			if (nothingChecked == true) {
				// Display an error message saying that at least 1 checkbox
				// should be checked!
				return null;
			}
			paramList = new String[arr_keyValues.size()];

			ListIterator listIterator = arr_keyValues.listIterator();

			while (listIterator.hasNext()) {
				paramList[numChecked] = (String) listIterator.next();

				numChecked++;
			}

			return paramList;
		} catch (Exception e) {
			return null;
		}
	}

	// ############################################################################

	public static String[][] getKeyNamesAndKeyValuesFromPosting(
			HttpServletRequest request, String keyPrefix) {
		// this method only filters out the keyNames that has a nonempty
		// keyValue -- others don't work

		if (keyPrefix == null || keyPrefix.length() <= 0 || request == null) {
			return null;
		}

		List arr_keyNames = new ArrayList();
		List arr_keyValues = new ArrayList();
		String keyName, keyValue;
		boolean nothingChecked = true;
		int numChecked = 0;
		String[][] paramList = null;
		try {
			Enumeration params = request.getParameterNames(); // receives all
																// the posted
																// params

			while (params.hasMoreElements()) {
				keyName = (String) params.nextElement();

				if (keyName.startsWith(keyPrefix)) {
					keyValue = request.getParameter(keyName); // 0 or 1

					if (!keyValue.equals("")) // ie. checked checkboxes
					{
						arr_keyNames.add(keyName);
						arr_keyValues.add(keyValue); //
						nothingChecked = false;
					} else // ie. unchecked checkboxes
					{
						// Do nothing for the unchecked msg rows
					}
				}
			}

			if (nothingChecked == true) {
				// Display an error message saying that at least 1 checkbox
				// should be checked!
				return null;
			}
			paramList = new String[arr_keyNames.size()][2];

			ListIterator listIterator1 = arr_keyNames.listIterator();
			ListIterator listIterator2 = arr_keyValues.listIterator();

			while (listIterator1.hasNext()) {
				paramList[numChecked][0] = (String) listIterator1.next();
				paramList[numChecked][1] = (String) listIterator2.next();
				numChecked++;
			}

			return paramList;
		} catch (Exception e) {
			return null;
		}
	}

	// ############################################################################

	public static String[] getKeyNameSuffixFromPosting(
			HttpServletRequest request, String keyPrefix) {
		// this method only filters out the keyNames that has a nonempty
		// keyValue -- others don't work

		if (keyPrefix == null || keyPrefix.length() <= 0 || request == null) {
			return null;
		}

		List arr_keyNameSuffix = new ArrayList();
		String keyName = "";
		String keyValue = "";
		String keyNameSuffix = "";
		boolean nothingChecked = true;
		int numChecked = 0;
		String[] paramList = null;
		try {
			Enumeration params = request.getParameterNames(); // receives all
																// the posted
																// params

			while (params.hasMoreElements()) {
				keyNameSuffix = "";
				keyName = (String) params.nextElement();

				if (keyName.startsWith(keyPrefix)) {
					if (keyName.length() > keyPrefix.length()) {
						keyNameSuffix = keyName.substring(keyPrefix.length(),
								keyName.length());
						keyValue = request.getParameter(keyName);
					}
					if (!keyValue.equals("") && keyNameSuffix.length() > 0) // ie.
																			// checked
																			// checkboxes
					{
						arr_keyNameSuffix.add(keyNameSuffix);
						nothingChecked = false;
					} else // ie. unchecked checkboxes
					{
						// Do nothing for the unchecked msg rows
					}
				}
			}

			if (nothingChecked == true) {
				// Display an error message saying that at least 1 checkbox
				// should be checked!
				return null;
			}
			paramList = new String[arr_keyNameSuffix.size()];

			ListIterator listIterator = arr_keyNameSuffix.listIterator();

			while (listIterator.hasNext()) {
				paramList[numChecked] = (String) listIterator.next();
				numChecked++;
			}

			return paramList;
		} catch (Exception e) {
			return null;
		}
	}

	// ################################################################################
	public static List filterOutDuplicateStrTokens(List duplStrList) {

		if (duplStrList == null || duplStrList.size() <= 0) {
			return null;
		}

		String compareToken = "";

		List filteredStrList = new ArrayList();

		for (int i = 0; i < duplStrList.size(); i++) {
			compareToken = (String) duplStrList.get(i);

			for (int j = i + 1; j < duplStrList.size(); j++) {
				if (compareToken.length() > 0
						&& compareToken.equalsIgnoreCase((String) duplStrList
								.get(j))) {
					duplStrList.set(j, "");
				}
			}
		}// end of for(int i=0; i < duplStrList.size(); i++)

		for (int i = 0; duplStrList != null && i < duplStrList.size(); i++) {
			if (!(duplStrList.get(i).toString()).equals("")) {
				filteredStrList.add(duplStrList.get(i).toString());

			}
		}

		if (filteredStrList == null) {
			log
					.debug("Utility/filterOutDuplicateStrTokens():  filteredStrList == null");

		} else {
			log
					.debug("Utility/filterOutDuplicateStrTokens():  filteredStrList.size() = "
							+ filteredStrList.size());
		}

		return filteredStrList;
	}

	// ################################################################################

	// ###################################################################################
	public static String toCurrency(double money) {
		double rtn = (Math.round(money * 100)) / 100.00;
		// rtn = rtn * 0.0 /100.0;
		String rtnStr = "" + rtn;

		int pos = rtnStr.length() - rtnStr.indexOf(".");

		if (pos == 3)
			;
		else if (pos == 2)
			rtnStr += "0";
		else if (pos == 1)
			rtnStr += "00";
		else
			rtnStr += ".00";

		return rtnStr;
	}

	// ###################################################################################
	public static String toCurrency(String money) {
		// 649; 649.; 649.0; 649.23; 649.9000000000001

		if (money == null || money.equals("")) {
			return "0.00"; // ???<-- may be should leave it "" or null
		}

		String rtnStr = money;
		int index = 0;

		index = rtnStr.indexOf(".");

		int pos = rtnStr.length() - index;

		if (pos == 3)
			; // in xxx.xx format already
		else if (pos == 2)
			rtnStr += "0";
		else if (pos == 1)
			rtnStr += "00";
		else if (pos <= 0) {
			rtnStr += ".00";
		} else if (pos > 3) {
			rtnStr = rtnStr.substring(0, index + 3);
		}

		return rtnStr;
	}

	// ###################################################################################
	// ###################################################################################
	public static String to3DecimalDigits(String decimal) {
		// 649; 649.; 649.0; 649.23; 649.9000000000001
		double decimalDouble = 0.00;
		String rtnStr = decimal;

		try {
			decimalDouble = Double.parseDouble(decimal);
			decimalDouble = (Math.round(decimalDouble * 1000)) / 1000.00;
			rtnStr = String.valueOf(decimalDouble);
		} catch (Exception ex) {
			rtnStr = decimal;
		}

		if (decimal == null) {
			return "0.000"; // ???<-- may be should leave it "" or null
		}

		int index = 0;

		index = rtnStr.indexOf(".");

		int pos = rtnStr.length() - index;

		if (pos == 4)
			; // in xxx.xx format already
		else if (pos == 3)
			rtnStr += "00";
		else if (pos == 2)
			rtnStr += "000";
		else if (pos == 1)
			rtnStr += "000";
		else if (pos <= 0) {
			rtnStr += ".000";
		} else if (pos > 5) {
			rtnStr = rtnStr.substring(0, index + 4);
		}

		return rtnStr;
	}

	// ###################################################################################
	public static String to4DecimalDigits(String decimal) {
		// 649; 649.; 649.0; 649.23; 649.9000000000001
		double decimalDouble = 0.00;
		String rtnStr = decimal;

		try {
			decimalDouble = Double.parseDouble(decimal);
			decimalDouble = (Math.round(decimalDouble * 10000)) / 10000.00;
			rtnStr = String.valueOf(decimalDouble);
		} catch (Exception ex) {
			rtnStr = decimal;
		}

		if (decimal == null) {
			return "0.0000"; // ???<-- may be should leave it "" or null
		}

		int index = 0;

		index = rtnStr.indexOf(".");

		int pos = rtnStr.length() - index;

		if (pos == 5)
			; // in xxx.xx format already
		else if (pos == 4)
			rtnStr += "0";
		else if (pos == 3)
			rtnStr += "00";
		else if (pos == 2)
			rtnStr += "000";
		else if (pos == 1)
			rtnStr += "0000";
		else if (pos <= 0) {
			rtnStr += ".0000";
		} else if (pos > 5) {
			rtnStr = rtnStr.substring(0, index + 5);
		}

		return rtnStr;
	}

	// ###################################################################################
	public static String toInteger(String decimalValue) {
		// 649; 649.; 649.0; 649.23; 649.9000000000001

		if (decimalValue == null) {
			return "0"; // ???<-- may be should leave it "" or null
		}

		String rtnStr = decimalValue;
		int index = 0;

		index = rtnStr.indexOf(".");

		if (index >= 0) {
			rtnStr = rtnStr.substring(0, index);
		}

		return rtnStr;
	}

	// ############################################################################
	public static Date calcDate(String s, String s1, String s2) {
		if (s==null || s1==null || s2==null) return(null);
		
		int i = Integer.parseInt(s);
		int j = Integer.parseInt(s1) - 1;
		int k = Integer.parseInt(s2);
		GregorianCalendar gregoriancalendar = new GregorianCalendar(i, j, k);
		return gregoriancalendar.getTime();
	}

	public static String calcAge(Date DOB) {
		return calcAgeAtDate(DOB, new GregorianCalendar().getTime());
	}

	/**
	 * This returns the Patients Age string at a point in time. IE. How old the
	 * patient will be right now or how old will they be on march.31 of this
	 * year.
	 * 
	 * @param DOB
	 *            Demographics Date of birth
	 * @param pointInTime
	 *            The date you would like to calculate there age at.
	 * @return age string ( ie 2 months, 4 years .etc )
	 */
	public static String calcAgeAtDate(Date DOB, Date pointInTime) {
		if (DOB==null) return(null);
		
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(pointInTime);
		int curYear = now.get(Calendar.YEAR);
		int curMonth = now.get(Calendar.MONTH) + 1;
		int curDay = now.get(Calendar.DAY_OF_MONTH);

		GregorianCalendar birthDate = new GregorianCalendar();
		birthDate.setTime(DOB);
		int birthYear = birthDate.get(Calendar.YEAR);
		int birthMonth = birthDate.get(Calendar.MONTH) + 1;
		int birthDay = birthDate.get(5);

		int ageInYears = curYear - birthYear;
		String result = ageInYears
				+ " "
				+ ResourceBundle.getBundle("oscarResources").getString(
						"global.years");

		if (curMonth > birthMonth || curMonth == birthMonth
				&& curDay >= birthDay) {
			ageInYears = curYear - birthYear;
			result = ageInYears
					+ " "
					+ ResourceBundle.getBundle("oscarResources").getString(
							"global.years");
		} else {
			ageInYears = curYear - birthYear - 1;
			result = ageInYears
					+ " "
					+ ResourceBundle.getBundle("oscarResources").getString(
							"global.years");
		}
		if (ageInYears < 2) {
			int yearDiff = curYear - birthYear;
			int ageInDays;
			if (yearDiff == 2) {
				ageInDays = (birthDate.getActualMaximum(Calendar.DAY_OF_YEAR) - birthDate
						.get(Calendar.DAY_OF_YEAR))
						+ now.get(Calendar.DAY_OF_YEAR) + 365;
			} else if (yearDiff == 1) {
				ageInDays = (birthDate.getActualMaximum(Calendar.DAY_OF_YEAR) - birthDate
						.get(Calendar.DAY_OF_YEAR))
						+ now.get(Calendar.DAY_OF_YEAR);
			} else {
				ageInDays = now.get(Calendar.DAY_OF_YEAR)
						- birthDate.get(Calendar.DAY_OF_YEAR);
			}
			if (ageInDays / 7 > 9) {
				result = ageInDays
						/ 30
						+ " "
						+ ResourceBundle.getBundle("oscarResources").getString(
								"global.months");
			} else if (ageInDays >= 14) {
				result = ageInDays
						/ 7
						+ " "
						+ ResourceBundle.getBundle("oscarResources").getString(
								"global.weeks");
			} else {
				result = ageInDays
						+ " "
						+ ResourceBundle.getBundle("oscarResources").getString(
								"global.days");
			}
		}
		return result;
	}

public static int calcAge(String year_of_birth, String month_of_birth, String date_of_birth)    {
  GregorianCalendar now = new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;

  try{
	if(curMonth>Integer.parseInt(month_of_birth) ) {
		age=curYear-Integer.parseInt(year_of_birth);
	} else {
		if(curMonth==Integer.parseInt(month_of_birth) && curDay>Integer.parseInt(date_of_birth)) {
			age=curYear-Integer.parseInt(year_of_birth);
		} else {
			age=curYear-Integer.parseInt(year_of_birth)-1; 
		}
	}	
  }catch(NumberFormatException nfe){//return -1 for unparsable dates
	  log.warn("Invalid date :" + year_of_birth + ":" + month_of_birth + ":" + date_of_birth);
	  return -1;
  }
  return age;
}

	public static int getNumYears(Date dStart, Date dEnd) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(dEnd);
		int curYear = now.get(Calendar.YEAR);
		int curMonth = now.get(Calendar.MONTH) + 1;
		int curDay = now.get(Calendar.DAY_OF_MONTH);

		GregorianCalendar birthDate = new GregorianCalendar();
		birthDate.setTime(dStart);
		int birthYear = birthDate.get(Calendar.YEAR);
		int birthMonth = birthDate.get(Calendar.MONTH) + 1;
		int birthDay = birthDate.get(5);

		int ageInYears = curYear - birthYear;

		if (curMonth > birthMonth || curMonth == birthMonth
				&& curDay >= birthDay) {
			ageInYears = curYear - birthYear;
		} else {
			ageInYears = curYear - birthYear - 1;
		}
		return ageInYears;
	}

	// ############################################################################
	public static boolean isNotNullOrEmptyStr(String str) {
		if (str != null && !str.equals("")) {
			return true;
		}
		return false;
	}

	// ############################################################################
}
