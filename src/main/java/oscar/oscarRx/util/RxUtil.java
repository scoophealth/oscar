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

package oscar.oscarRx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxCodesData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.pageUtil.RxMyDrugrefInfoAction;
import oscar.oscarRx.pageUtil.RxSessionBean;
import oscar.oscarRx.util.TimingOutCallback.TimeoutException;

public class RxUtil {

	private static String defaultPattern = "yyyy/MM/dd";
	private static Locale locale = Locale.CANADA;
	private static String defaultQuantity = "30";
	private static final Logger logger = MiscUtils.getLogger();
	private static String[] zeroToTen = { "(?i)zero", "(?i)one", "(?i)two", "(?i)three", "(?i)four", "(?i)five", "(?i)six", "(?i)seven", "(?i)eight", "(?i)nine", "(?i)ten" };

	public static void setDefaultQuantity(String quantity) {
		defaultQuantity = quantity;
	}

	public static String getDefaultQuantity() {
		return defaultQuantity;
	}

	public static Date StringToDate(String Expression) {
		return StringToDate(Expression, defaultPattern);
	}

	public static Date StringToDate(String Expression, String pattern) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern, locale);

			return df.parse(Expression);
		} catch (Exception e) {
			return null;
		}
	}

	public static String DateToString(Date Expression) {
		return DateToString(Expression, defaultPattern);
	}

	public static String DateToString(Calendar Expression) {
		if (Expression == null) return DateToString(null, defaultPattern);
		return DateToString(Expression.getTime(), defaultPattern);
	}

	public static String DateToString(Date Expression, String pattern) {
		if (Expression != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern, locale);

			return df.format(Expression);
		} else {
			return "";
		}
	}

	public static String DateToString(Date Expression, String pattern, Locale locale2) {
		if (Expression != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern, locale2);

			return df.format(Expression);
		} else {
			return "";
		}
	}

	public static Date Today() {
		return (java.util.GregorianCalendar.getInstance().getTime());
	}

	public static int BoolToInt(boolean Expression) {
		if (Expression == true) {
			return 1;
		} else {
			return 0;
		}
	}

	public static boolean IntToBool(int Expression) {
		return (Expression != 0);
	}

	public static String FloatToString(float value) {
		Float f = new Float(value);

		java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();

		String s = fmt.format(f.doubleValue());

		return s;
	}

	public static float StringToFloat(String value) {
		return Float.parseFloat(value);
	}

	public static Object IIf(boolean Expression, Object TruePart, Object FalsePart) {
		if (Expression == true) {
			return TruePart;
		} else {
			return FalsePart;
		}
	}

	public static String joinArray(Object[] array) {
		String ret = "";
		int i;
		for (i = 0; i < array.length; i++) {
			ret += "'" + String.valueOf(array[i]) + "'";
			if (i < array.length - 1) {
				ret += ", ";
			}
		}

		return ret;
	}

	public static String replace(String expression, String searchFor, String replaceWith) {
		if (expression != null) {
			StringBuilder buf = new StringBuilder(expression);

			int pos = -1;

			while (true) {
				pos = buf.indexOf(searchFor, pos);

				if (pos > -1) {
					buf.delete(pos, pos + searchFor.length());
					buf.insert(pos, replaceWith);

					pos += replaceWith.length();
				} else {
					break;
				}
			}

			return buf.toString();
		} else {
			return null;
		}
	}

	/**
	 * Method for calculating creatinine clearance takes age, weight in kg and CREATININE values an returns Clcr
	 * age must be greater than zero.
	 * weight must be greater than zero
	 */
	public static int getClcr(int age, double weight, double sCr, boolean female) throws Exception {
		if (age < 0) {
			throw new Exception("age must be greater than 0");
		}
		if (weight < 0) {
			throw new Exception("weight must be greater than 0");
		}
		if (sCr < 0) {
			throw new Exception("sCr must be greater than 0");
		}

		double Clcr = (140 - age) * weight / (sCr * 0.8);
		if (female) {
			Clcr = Clcr * 0.85;
		}

		return (int) Math.round(Clcr);
	}

	public static double findNDays(String durationUnit) {
		double nDays = 0d;
		if (durationUnit.equalsIgnoreCase("D")) {
			nDays = 1;
		} else if (durationUnit.equalsIgnoreCase("W")) {
			nDays = 7;
		} else if (durationUnit.equalsIgnoreCase("M")) {
			nDays = 30;
		}
		return nDays;
	}

	public static Double findNPerDay(String frequency) {
		double nPerDay = 0d;
		if (frequency.equalsIgnoreCase("od")) {
			nPerDay = 1;
		} else if (frequency.equalsIgnoreCase("bid")) {
			nPerDay = 2;
		} else if (frequency.equalsIgnoreCase("tid")) {
			nPerDay = 3;
		} else if (frequency.equalsIgnoreCase("qid")) {
			nPerDay = 4;
		} else if (frequency.equalsIgnoreCase("Q1H")) {
			nPerDay = 24;
		} else if (frequency.equalsIgnoreCase("Q2H")) {
			nPerDay = 12;
		} else if (frequency.equalsIgnoreCase("Q1-2H")) {
			nPerDay = 24;
		} else if (frequency.equalsIgnoreCase("Q3-4H")) {
			nPerDay = 8;
		} else if (frequency.equalsIgnoreCase("Q4H")) {
			nPerDay = 6;
		} else if (frequency.equalsIgnoreCase("Q4-6H")) {
			nPerDay = 6;
		} else if (frequency.equalsIgnoreCase("Q6H")) {
			nPerDay = 4;
		} else if (frequency.equalsIgnoreCase("Q8H")) {
			nPerDay = 3;
		} else if (frequency.equalsIgnoreCase("Q12H")) {
			nPerDay = 2;
		} else if (frequency.equalsIgnoreCase("QAM")) {
			nPerDay = 1;
		} else if (frequency.equalsIgnoreCase("QPM")) {
			nPerDay = 1;
		} else if (frequency.equalsIgnoreCase("QHS")) {
			nPerDay = 1;
		} else if (frequency.equalsIgnoreCase("Q1Week")) {
			nPerDay = 0.14285714285714285;
		} else if (frequency.equalsIgnoreCase("Q2Week")) {
			nPerDay = 0.07142857142857142;
		} else if (frequency.equalsIgnoreCase("Q1Month")) {
			nPerDay = 0.03333333333333333;
		} else if (frequency.equalsIgnoreCase("Q3Month")) {
			nPerDay = 0.011111111111111112;
		}
		return nPerDay;
	}

	public static String getUnitNameFromQuantityText(String qStr) {
		if (qStr != null) {
			Pattern p1 = Pattern.compile("\\d+");
			Matcher m1 = p1.matcher(qStr);
			if (m1.find()) {
				String qNum = qStr.substring(m1.start(), m1.end());
				//get the quantity unit
				String qUnit = qStr.replace(qNum, "").trim();
				if (qUnit != null && qUnit.length() > 0) {
					return qUnit;
				}
			}
		}
		return null;
	}

	public static String getQuantityFromQuantityText(String qStr) {
		if (qStr != null) {
			Pattern p1 = Pattern.compile("\\d+");
			Matcher m1 = p1.matcher(qStr);
			if (m1.find()) {
				String qNum = qStr.substring(m1.start(), m1.end());
				return qNum;
			}
		}
		return null;
	}

	public static boolean isMitte(String qStr) {
		boolean isMitte = false;
		String[] durationUnits = { "[0-9]+\\s+(?i)days\\s", "[0-9]+\\s+(?i)weeks\\s", "[0-9]+\\s+(?i)months\\s", "[0-9]+\\s+(?i)day\\s", "[0-9]+\\s+(?i)week\\s", "[0-9]+\\s+(?i)month\\s", "[0-9]+\\s+(?i)d\\s", "[0-9]+\\s+(?i)w\\s", "[0-9]+\\s+(?i)m\\s", "[0-9]+\\s+(?i)mo\\s", "[0-9]+\\s+(?i)days$", "[0-9]+\\s+(?i)weeks$", "[0-9]+\\s+(?i)months$", "[0-9]+\\s+(?i)day$", "[0-9]+\\s+(?i)week$", "[0-9]+\\s+(?i)month$", "[0-9]+\\s+(?i)d$", "[0-9]+\\s+(?i)w$", "[0-9]+\\s+(?i)m$", "[0-9]+\\s+(?i)mo$",
		        "\\s[0-9]+(?i)days\\s", "\\s[0-9]+(?i)weeks\\s", "\\s[0-9]+(?i)months\\s", "\\s[0-9]+(?i)day\\s", "\\s[0-9]+(?i)week\\s", "\\s[0-9]+(?i)month\\s", "\\s[0-9]+(?i)d\\s", "\\s[0-9]+(?i)w\\s", "\\s[0-9]+(?i)m\\s", "\\s[0-9]+(?i)mo\\s", "\\s[0-9]+(?i)days$", "\\s[0-9]+(?i)weeks$", "\\s[0-9]+(?i)months$", "\\s[0-9]+(?i)day$", "\\s[0-9]+(?i)week$", "\\s[0-9]+(?i)month$", "\\s[0-9]+(?i)d$", "\\s[0-9]+(?i)w$", "\\s[0-9]+(?i)m$", "\\s[0-9]+(?i)mo$", "^[0-9]+(?i)days$", "^[0-9]+(?i)weeks$",
		        "^[0-9]+(?i)months$", "^[0-9]+(?i)day$", "^[0-9]+(?i)week$", "^[0-9]+(?i)month$", "^[0-9]+(?i)d$", "^[0-9]+(?i)w$", "^[0-9]+(?i)m$", "^[0-9]+(?i)mo$", "^[0-9]+\\s+(?i)days$", "^[0-9]+\\s+(?i)weeks$", "^[0-9]+\\s+(?i)months$", "^[0-9]+\\s+(?i)day$", "^[0-9]+\\s+(?i)week$", "^[0-9]+\\s+(?i)month$", "^[0-9]+\\s+(?i)d$", "^[0-9]+\\s+(?i)w$", "^[0-9]+\\s+(?i)m$", "^[0-9]+\\s+(?i)mo$" };
		for (String s : durationUnits) {
			Pattern p = Pattern.compile(s);
			Matcher m = p.matcher(qStr);
			if (m.find()) {
				String foundStr = (qStr.substring(m.start(), m.end())).trim();
				qStr = qStr.replace(foundStr, "");
				qStr = qStr.trim();
				if (qStr.length() == 0) {
					isMitte = true;
					break;
				}
			}
		}
		return isMitte;
	}

	public static String getDurationFromQuantityText(String qStr) {
		String retStr = "";
		String[] durationUnits = { "[0-9]+\\s+(?i)days\\s", "[0-9]+\\s+(?i)weeks\\s", "[0-9]+\\s+(?i)months\\s", "[0-9]+\\s+(?i)day\\s", "[0-9]+\\s+(?i)week\\s", "[0-9]+\\s+(?i)month\\s", "[0-9]+\\s+(?i)d\\s", "[0-9]+\\s+(?i)w\\s", "[0-9]+\\s+(?i)m\\s", "[0-9]+\\s+(?i)mo\\s", "[0-9]+\\s+(?i)days$", "[0-9]+\\s+(?i)weeks$", "[0-9]+\\s+(?i)months$", "[0-9]+\\s+(?i)day$", "[0-9]+\\s+(?i)week$", "[0-9]+\\s+(?i)month$", "[0-9]+\\s+(?i)d$", "[0-9]+\\s+(?i)w$", "[0-9]+\\s+(?i)m$", "[0-9]+\\s+(?i)mo$",
		        "\\s[0-9]+(?i)days\\s", "\\s[0-9]+(?i)weeks\\s", "\\s[0-9]+(?i)months\\s", "\\s[0-9]+(?i)day\\s", "\\s[0-9]+(?i)week\\s", "\\s[0-9]+(?i)month\\s", "\\s[0-9]+(?i)d\\s", "\\s[0-9]+(?i)w\\s", "\\s[0-9]+(?i)m\\s", "\\s[0-9]+(?i)mo\\s", "\\s[0-9]+(?i)days$", "\\s[0-9]+(?i)weeks$", "\\s[0-9]+(?i)months$", "\\s[0-9]+(?i)day$", "\\s[0-9]+(?i)week$", "\\s[0-9]+(?i)month$", "\\s[0-9]+(?i)d$", "\\s[0-9]+(?i)w$", "\\s[0-9]+(?i)m$", "\\s[0-9]+(?i)mo$", };
		for (String s : durationUnits) {
			Pattern p = Pattern.compile(s);
			Matcher m = p.matcher(qStr);
			if (m.find()) {
				String foundStr = (qStr.substring(m.start(), m.end())).trim();
				Pattern p2 = Pattern.compile("[0-9]+");
				Matcher m2 = p2.matcher(foundStr);
				if (m2.find()) {
					String duration = (foundStr.substring(m2.start(), m2.end())).trim();
					retStr = duration;
				}
				break;
			}
		}
		return retStr;
	}

	public static String getDurationUnitFromQuantityText(String qStr) {
		String retStr = "";
		String[] durationUnits = { "[0-9]+\\s+(?i)days\\s", "[0-9]+\\s+(?i)weeks\\s", "[0-9]+\\s+(?i)months\\s", "[0-9]+\\s+(?i)day\\s", "[0-9]+\\s+(?i)week\\s", "[0-9]+\\s+(?i)month\\s", "[0-9]+\\s+(?i)d\\s", "[0-9]+\\s+(?i)w\\s", "[0-9]+\\s+(?i)m\\s", "[0-9]+\\s+(?i)mo\\s", "[0-9]+\\s+(?i)days$", "[0-9]+\\s+(?i)weeks$", "[0-9]+\\s+(?i)months$", "[0-9]+\\s+(?i)day$", "[0-9]+\\s+(?i)week$", "[0-9]+\\s+(?i)month$", "[0-9]+\\s+(?i)d$", "[0-9]+\\s+(?i)w$", "[0-9]+\\s+(?i)m$", "[0-9]+\\s+(?i)mo$",
		        "\\s[0-9]+(?i)days\\s", "\\s[0-9]+(?i)weeks\\s", "\\s[0-9]+(?i)months\\s", "\\s[0-9]+(?i)day\\s", "\\s[0-9]+(?i)week\\s", "\\s[0-9]+(?i)month\\s", "\\s[0-9]+(?i)d\\s", "\\s[0-9]+(?i)w\\s", "\\s[0-9]+(?i)m\\s", "\\s[0-9]+(?i)mo\\s", "\\s[0-9]+(?i)days$", "\\s[0-9]+(?i)weeks$", "\\s[0-9]+(?i)months$", "\\s[0-9]+(?i)day$", "\\s[0-9]+(?i)week$", "\\s[0-9]+(?i)month$", "\\s[0-9]+(?i)d$", "\\s[0-9]+(?i)w$", "\\s[0-9]+(?i)m$", "\\s[0-9]+(?i)mo$", };
		for (String s : durationUnits) {
			Pattern p = Pattern.compile(s);
			Matcher m = p.matcher(qStr);
			if (m.find()) {
				String foundStr = (qStr.substring(m.start(), m.end())).trim();
				Pattern p2 = Pattern.compile("[0-9]+");
				Matcher m2 = p2.matcher(foundStr);
				if (m2.find()) {
					String duration = (foundStr.substring(m2.start(), m2.end())).trim();
					String durationUnit = foundStr.replace(duration, "").trim();
					if (durationUnit.startsWith("d") || durationUnit.startsWith("D")) retStr = "D";
					else if (durationUnit.startsWith("w") || durationUnit.startsWith("W")) retStr = "W";
					else if (durationUnit.startsWith("m") || durationUnit.startsWith("M")) retStr = "M";
					else retStr = "";
					break;
				}
			}
		}
		return retStr;
	}

	public static String findDuration(RxPrescriptionData.Prescription rx) {//calculate duration based on quantity, takemax,takemin,frequency,durationUnit.
		//get frequency,takemax,takemin,durationUnit by parsing special.
		instrucParser(rx);
		MiscUtils.getLogger().debug("after  instrucParser,quantity=" + rx.getQuantity());
		String qStr = rx.getQuantity();
		if (rx.getUnitName() == null) {
			qStr = qStr.trim();
			double qtyD;
			Pattern p1 = Pattern.compile("\\d+");
			Matcher m1 = p1.matcher(qStr);
			if (m1.find()) {
				String qNum = qStr.substring(m1.start(), m1.end());
				qtyD = Double.parseDouble(qNum);
				//get the quantity unit
				String qUnit = qStr.replace(qNum, "").trim();
				if (qUnit != null && qUnit.length() > 0) {
					rx.setUnitName(qUnit);
					return null; //if a quantity unit is specified, can't calculate duration.
				} else {
					double takeMax = rx.getTakeMax();
					double nPerDay = findNPerDay(rx.getFrequencyCode());
					double nDays = findNDays(rx.getDurationUnit());
					p("qtyD--takeMax--nPerDay--nDays--" + qtyD + " " + takeMax + " " + nPerDay + " " + nDays);
					if ((int)takeMax != 0) {
						double durD = qtyD / (takeMax) * nPerDay * nDays;
						int durInt = (int) durD;
						p("durInt", Integer.toString(durInt));
						return Integer.toString(durInt);
					} else {
						return null;
					}
				}
			} else {
				logger.error("quantity is not specified");
				return null;
			}
		} else {
			return null;
		}
	}

	private static void setEmptyValues(RxPrescriptionData.Prescription rx) {
		rx.setRoute("");
		rx.setTakeMax(Float.parseFloat("0"));
		rx.setTakeMin(Float.parseFloat("0"));
		rx.setMethod("");
		rx.setFrequencyCode("");
		rx.setDuration("0");
		rx.setDurationUnit("");
		rx.setPrn(false);
		rx.setQuantity(Integer.toString(0));
		rx.setSpecial("");
	}

	private static String changeToStandardFrequencyCode(String str) {
		String retVal = str;
		if (str.equalsIgnoreCase("daily")) {
			retVal = "OD";
		} else if (str.equalsIgnoreCase("once daily")) {
			retVal = "OD";
		} else if (str.equalsIgnoreCase("twice daily")) {
			retVal = "BID";
		} else if (str.equalsIgnoreCase("3x day")) {
			retVal = "TID";
		} else if (str.equalsIgnoreCase("3x daily")) {
			retVal = "TID";
		} else if (str.equalsIgnoreCase("4x day")) {
			retVal = "QID";
		} else if (str.equalsIgnoreCase("4x daily")) {
			retVal = "QID";
		} else if (str.equalsIgnoreCase("weekly")) {
			retVal = "Q1Week";
		} else if (str.equalsIgnoreCase("monthly")) {
			retVal = "Q1Month";
		}
		return retVal;
	}

	private static String convertWordToNumerical(String s) {

		s = s.trim();
		String retVal = s;
		if (s != null && !s.equalsIgnoreCase("null")) {
			if (s.equalsIgnoreCase("zero")) {
				retVal = "0";
			} else if (s.equalsIgnoreCase("one")) {
				retVal = "1";
			} else if (s.equalsIgnoreCase("two")) {
				retVal = "2";
			} else if (s.equalsIgnoreCase("three")) {
				retVal = "3";
			} else if (s.equalsIgnoreCase("four")) {
				retVal = "4";
			} else if (s.equalsIgnoreCase("five")) {
				retVal = "5";
			} else if (s.equalsIgnoreCase("six")) {
				retVal = "6";
			} else if (s.equalsIgnoreCase("seven")) {
				retVal = "7";
			} else if (s.equalsIgnoreCase("eight")) {
				retVal = "8";
			} else if (s.equalsIgnoreCase("nine")) {
				retVal = "9";
			} else if (s.equalsIgnoreCase("ten")) {
				retVal = "10";
			} else {
				if (!isStringToNumber(retVal)) retVal = "0";
			}
		} else retVal = "0";

		return retVal;
	}

	private static String checkInstructionStr(String str) {//replace if instruction contains certain string which may confuse the text parser
		String retVal = str;
		if (str.contains("3x day")) {
			retVal = retVal.replace("3x day", "");
		} else if (str.contains("4x day")) {
			retVal = retVal.replace("4x day", "");
		}
		return retVal;
	}

	public static void instrucParser(RxPrescriptionData.Prescription rx) {
		if (rx == null) {
			return;
		}
		String instructions = rx.getSpecial();
		if (instructions == null) {
			instructions = "";
		}
		String route = "";
		String frequency = "";
		String duration = "0";
		String method = "";
		String durationUnit = "";
		String durationUnitSpec = "";
		boolean prn = false;
		String amountFrequency = "";
		String amountMethod = "";
		String takeMinFrequency = "";
		String takeMaxFrequency = "";
		String takeMinMethod = "";
		String takeMaxMethod = "";
		String takeMin = "0";
		String takeMax = "0";
		String durationSpec = "";
		int quantity = 0;
		List<String> policyViolations = new ArrayList<String>();

		if (instructions.trim().length() == 0) {
			setEmptyValues(rx);
		} else {
			//do we have some policies/restrictions we want to run?
			policyViolations.addAll(RxInstructionPolicy.checkInstructions(instructions.trim()));

			String[] prns = { "\\s(?i)prn$", "^(?i)prn\\s+", "\\s+(?i)prn\\s+" };
			for (String s : prns) {
				Pattern prnP = Pattern.compile(s);
				Matcher prnM = prnP.matcher(instructions);
				if (prnM.find()) {
					//            p("prn is true");
					prn = true;
				}
			}

			String[] routes = { "\\s(?i)PO$", "\\s(?i)SL$", "\\s(?i)IM$", "\\s(?i)SC$", "\\s(?i)PATCH$", "\\s(?i)TOP\\.$", "\\s(?i)INH$", "\\s(?i)SUPP$", "\\s(?i)O.D.$", "\\s(?i)O.S.$", "\\s(?i)O.U.$", "\\s(?i)OD$", "\\s(?i)OS$", "\\s(?i)OU$", "\\s(?i)PO\\s", "\\s(?i)SL\\s", "\\s(?i)IM\\s", "\\s(?i)SC\\s", "\\s(?i)PATCH\\s", "\\s(?i)TOP\\.\\s", "\\s(?i)INH\\s", "\\s(?i)SUPP\\s", "\\s(?i)O.D.\\s", "\\s(?i)O.S.\\s", "\\s(?i)O.U.\\s", "\\s(?i)OD\\s", "\\s(?i)OS\\s", "\\s(?i)OU\\s" };
			String[] frequences = { "\\s(?i)OD\\s", "\\s(?i)BID\\s", "\\s(?i)TID\\s", "\\s(?i)QID\\s", "\\s(?i)Q1H\\s", "\\s(?i)Q2H\\s", "\\s(?i)Q1-2H\\s", "\\s(?i)Q3-4H\\s", "\\s(?i)Q4H\\s", "\\s(?i)Q4-6H\\s", "\\s(?i)Q6H\\s", "\\s(?i)Q8H\\s", "\\s(?i)Q12H\\s", "\\s(?i)QAM\\s", "\\s(?i)QPM\\s", "\\s(?i)QHS\\s", "\\s(?i)Q1Week\\s", "\\s(?i)weekly\\s", "\\s(?i)Q2Week\\s", "\\s(?i)Q1Month\\s", "\\s(?i)Q3Month\\s", "\\s(?i)monthly\\s", "\\s(?i)once daily\\s", "\\s(?i)twice daily\\s", "\\s(?i)3x day\\s",
			        "\\s(?i)4x day\\s", "\\s(?i)3x daily\\s", "\\s(?i)4x daily\\s", "\\s(?i)OD$", "\\s(?i)BID$", "\\s(?i)TID$", "\\s(?i)QID$", "\\s(?i)Q1H$", "\\s(?i)Q2H$", "\\s(?i)Q1-2H$", "\\s(?i)Q3-4H$", "\\s(?i)Q4H$", "\\s(?i)Q4-6H$", "\\s(?i)Q6H$", "\\s(?i)Q8H$", "\\s(?i)Q12H$", "\\s(?i)QAM$", "\\s(?i)QPM$", "\\s(?i)QHS$", "\\s(?i)Q1Week$", "\\s(?i)weekly$", "\\s(?i)Q2Week$", "\\s(?i)Q1Month$", "\\s(?i)Q3Month$", "\\s(?i)monthly$", "\\s(?i)once daily$", "\\s(?i)twice daily$", "\\s(?i)3x day$",
			        "\\s(?i)4x day$", "\\s(?i)3x daily$", "\\s(?i)4x daily$", "\\s(?i)daily\\s", "\\s(?i)daily$",// put at last because if frequency is 'twice daily', it will first be detected as 'daily'
			};
			String[] methods = { "(?i)Take", "(?i)Apply", "(?i)Rub well in" };
			String[] durationUnits = { "\\s+(?i)days\\s", "\\s+(?i)weeks\\s", "\\s+(?i)months\\s", "\\s+(?i)day\\s", "\\s+(?i)week\\s", "\\s+(?i)month\\s", "\\s+(?i)d\\s", "\\s+(?i)w\\s", "\\s+(?i)m\\s", "\\s+(?i)mo\\s", "\\s+(?i)days$", "\\s+(?i)weeks$", "\\s+(?i)months$", "\\s+(?i)day$", "\\s+(?i)week$", "\\s+(?i)month$", "\\s+(?i)d$", "\\s+(?i)w$", "\\s+(?i)m$", "\\s+(?i)mo$" };
			String[] durUnits2 = { "\\s[0-9]+(?i)days\\s", "\\s[0-9]+(?i)weeks\\s", "\\s[0-9]+(?i)months\\s", "\\s[0-9]+(?i)day\\s", "\\s[0-9]+(?i)week\\s", "\\s[0-9]+(?i)month\\s", "\\s[0-9]+(?i)d\\s", "\\s[0-9]+(?i)w\\s", "\\s[0-9]+(?i)m\\s", "\\s[0-9]+(?i)mo\\s", "\\s[0-9]+(?i)days$", "\\s[0-9]+(?i)weeks$", "\\s[0-9]+(?i)months$", "\\s[0-9]+(?i)day$", "\\s[0-9]+(?i)week$", "\\s[0-9]+(?i)month$", "\\s[0-9]+(?i)d$", "\\s[0-9]+(?i)w$", "\\s[0-9]+(?i)m$", "\\s[0-9]+(?i)mo$", };

			for (String s : routes) {
				Pattern p = Pattern.compile(s);
				Matcher matcher = p.matcher(instructions);
				if (matcher.find()) {
					route = (instructions.substring(matcher.start(), matcher.end())).trim();
					if (route.equalsIgnoreCase("OD")) {
						p("route is OD");

						//remove OD from instructions
						String part = instructions.substring(0, matcher.start()) + " " + instructions.substring(matcher.end());
						//if route =od,check if there is a valid frequency, there is one, then route is od.
						//if not , set the route="",keep looping. then set frequency to be od;
						p("part is " + part);
						Pattern fPattern = Pattern.compile("\\s(?i)OD\\s*");
						Matcher fMatcher = fPattern.matcher(part);
						String frequencyStr = "";
						if (fMatcher.find()) {
							frequencyStr = part.substring(fMatcher.start(), fMatcher.end());
							break;
						}
						if (frequencyStr.equals("")) {
							frequency = "OD";
							route = "";
							continue;
						} else {
							frequency = frequencyStr;
							break;
						}
					} else {
						break;
					}

				}
			}

			p("route", route);
			if (route.equals("")) {
				MiscUtils.getLogger().debug("route is not set");
			}

			//find frequency
			for (String s : frequences) {
				Pattern p = Pattern.compile(s);
				Matcher matcher = p.matcher(instructions);
				if (matcher.find()) {
					frequency = (instructions.substring(matcher.start(), matcher.end())).trim();
					frequency = changeToStandardFrequencyCode(frequency);
					String origFrequency = (instructions.substring(matcher.start(), matcher.end())).trim();

					Pattern p2 = Pattern.compile("\\s*\\d*\\.*\\d+\\s+" + origFrequency); //allow to detect decimal number.
					Matcher m2 = p2.matcher(instructions);

					Pattern p4 = Pattern.compile("\\s*\\d*\\.*\\d+-\\s*\\d*\\.*\\d+\\s+" + frequency); //use * after the first \s because "1 OD", 1 doesn't have a space in front.
					Matcher m4 = p4.matcher(instructions);
					//     p("here11", instructions);
					//since "\\s+[0-9]+-[0-9]+\\s+" is a case in "\\s+[0-9]+\\s+", check the latter regex first.
					if (m4.find()) {
						String str2 = instructions.substring(m4.start(), m4.end());
						Pattern p5 = Pattern.compile("\\d*\\.*\\d+-\\s*\\d*\\.*\\d+");
						Matcher m5 = p5.matcher(str2);
						if (m5.find()) {
							String str3 = str2.substring(m5.start(), m5.end());
							//       p("here str3", str3);
							takeMinFrequency = str3.split("-")[0];
							takeMaxFrequency = str3.split("-")[1];
						}
					} else if (m2.find()) {
						String str = instructions.substring(m2.start(), m2.end());
						Pattern p3 = Pattern.compile("\\d*\\.*\\d+");
						Matcher m3 = p3.matcher(str);
						//     p("here22", str);
						if (m3.find()) {
							amountFrequency = str.substring(m3.start(), m3.end());
						}
					} else {
						p("word amount");
						for (String word : zeroToTen) {
							String r1 = "\\s" + word + "\\s*" + frequency;
							String r2 = "^" + word + "\\s*" + frequency;//start at the begin of instructions
							Pattern p5 = Pattern.compile(r1);
							Matcher m5 = p5.matcher(instructions);
							p("pattern word =" + r1);
							if (m5.find()) {
								amountFrequency = instructions.substring(m5.start(), m5.end());
								amountFrequency = amountFrequency.replace(frequency, "").trim();
								p("amountFreq=" + amountFrequency);
								amountFrequency = convertWordToNumerical(amountFrequency);
								p("num amountFreq=" + amountFrequency);
								break;
							}
							p5 = Pattern.compile(r2);
							m5 = p5.matcher(instructions);
							if (m5.find()) {
								amountFrequency = instructions.substring(m5.start(), m5.end());
								amountFrequency = amountFrequency.replace(frequency, "").trim();
								p("amountFreq=" + amountFrequency);
								amountFrequency = convertWordToNumerical(amountFrequency);
								p("num amountFreq=" + amountFrequency);
								break;
							}
						}
					}
					//the string before frequency maybe the amount of drug
					//check if the string is a number,if it is, get the number
					//if not a number, check if it has "min-max" pattern, if yes, get min and max, if not, ignore

					break;
				}
			}
			//check if method is specified, if yes, check the number after method ,which maybe the number to take for a frequency.
			for (String s : methods) {
				Pattern p = Pattern.compile(s);
				Matcher m = p.matcher(instructions);
				if (m.find()) {
					p("must be here");
					method = instructions.substring(m.start(), m.end());

					Pattern p2 = Pattern.compile(method + "\\s*\\d*\\.*\\d+\\s+");
					Matcher m2 = p2.matcher(instructions);

					Pattern pF1 = Pattern.compile(method + "\\s*\\d*\\/*\\d+\\s+");
					Matcher mF1 = pF1.matcher(instructions);

					Pattern p4 = Pattern.compile(method + "\\s*\\d*\\.*\\d+-\\s*\\d*\\.*\\d+\\s+");
					Matcher m4 = p4.matcher(instructions);
					
					//since "\\s+[0-9]+-[0-9]+\\s+" is a case in "\\s+[0-9]+\\s+", check the latter regex first.
					if (m4.find()) {
						p("else if 1");
						String str2 = instructions.substring(m4.start(), m4.end());
						Pattern p5 = Pattern.compile("\\d*\\.*\\d+-\\s*\\d*\\.*\\d+");
						Matcher m5 = p5.matcher(str2);
						if (m5.find()) {
							String str3 = str2.substring(m5.start(), m5.end());
							//           p("str3", str3);
							takeMinMethod = str3.split("-")[0];
							takeMaxMethod = str3.split("-")[1];
						}
					} else if (m2.find()) {
						p("if 1");
						String str = instructions.substring(m2.start(), m2.end());
						p("str1 ", str);
						Pattern p3 = Pattern.compile("\\d*\\.*\\d+");
						Matcher m3 = p3.matcher(str);
						if (m3.find()) {
							p("found1");
							amountMethod = str.substring(m3.start(), m3.end());
							//      p("amountMethod", amountMethod);
						}
					} else if(mF1.find()) {
						String partInstructions = instructions.substring(mF1.start(), mF1.end());
						Pattern pF2 = Pattern.compile("\\d*\\/*\\d+");
						Matcher mF2 = pF2.matcher(partInstructions);
						
						if(mF2.find()) {
							String fraction = partInstructions.substring(mF2.start(), mF2.end());
							amountFrequency = "0";
							if(fraction.equals("1/2"))
								amountFrequency = "0.5";
							else if(fraction.equals("1/4"))
								amountFrequency = "0.25";
						}
					}
					else {
						p("word amount");
						for (String word : zeroToTen) {
							String r1 = method + "\\s+" + word + "\\s";
							String r2 = method + "\\s+" + word + "$";
							Pattern p5 = Pattern.compile(r1);
							Matcher m5 = p5.matcher(instructions);
							p("pattern word =" + r1);
							if (m5.find()) {
								amountMethod = instructions.substring(m5.start(), m5.end());
								amountMethod = amountMethod.replace(method, "").trim();
								p("amountMethod=" + amountMethod);
								amountMethod = convertWordToNumerical(amountMethod);
								p("num amountMethod=" + amountMethod);
								break;
							} else {
								p5 = Pattern.compile(r2);
								m5 = p5.matcher(instructions);
								p("pattern word =" + r2);
								if (m5.find()) {
									amountMethod = instructions.substring(m5.start(), m5.end());
									amountMethod = amountMethod.replace(method, "").trim();
									p("amountMethod=" + amountMethod);
									amountMethod = convertWordToNumerical(amountMethod);
									p("num amountMethod=" + amountMethod);
									break;
								}
							}
						}
					}
					break;
				}
			}
			/*    p(takeMinMethod);
			p(takeMaxMethod);
			p(takeMinFrequency);
			p(takeMaxFrequency);*/
			if (!takeMinMethod.equals("") && takeMinFrequency.equals("")) {

				takeMin = takeMinMethod;
				takeMax = takeMaxMethod;
			} else if (takeMinMethod.equals("") && !takeMinFrequency.equals("")) {
				takeMin = takeMinFrequency;
				takeMax = takeMaxFrequency;
			} else if (!takeMinMethod.equals("") && !takeMinFrequency.equals("")) {// when method and frequency both gives values of takemin and takemax,
				//assume use the frequency one is correct.
				takeMin = takeMinFrequency;
				takeMax = takeMaxFrequency;
			}
			//check if a frequency is specified, if yes, check the number before the freq, which is the number to have, check if the number match the previous.
			//if yes, use that as the quantity per frequency, if not, use the frequency number as quantity.
			if (!amountMethod.equals("") && amountFrequency.equals("")) {
				takeMin = amountMethod;
				takeMax = takeMin;
			} else if (amountMethod.equals("") && !amountFrequency.equals("")) {
				takeMin = amountFrequency;
				takeMax = takeMin;
			} else if (!amountMethod.equals("") && !amountFrequency.equals("")) { // when method and frequency both gives values of takemin and takemax,
				//assume use the frequency one is correct.
				takeMin = amountFrequency;
				takeMax = amountFrequency;
			}

			//calculate the number of pills to have per frequency which is used to calculate the duration later on.
			//from frequency code we can deduce a duration unit.
			//check if a durationunit is already specified, if not, use that, if yes,check if they are equal,if not output an warning and use specified.
			for (String s : durationUnits) {
				// p(instructions);
				// p(s);
				String instructionToCheck = checkInstructionStr(instructions);
				Pattern p = Pattern.compile(s);
				Matcher m = p.matcher(instructionToCheck);

				if (m.find()) {
					p("FOUND");
					p("instructionToCheck==", instructionToCheck);
					p(s);
					durationUnitSpec = (instructionToCheck.substring(m.start(), m.end())).trim();
					p("durationUnitSpec", durationUnitSpec);
					//get the number before durationUnit
					Pattern p1 = Pattern.compile("[0-9]+" + s);
					Matcher m1 = p1.matcher(instructionToCheck);
					if (m1.find()) {
						p("" + m1.start(), "" + m.start());
						durationSpec = instructionToCheck.substring(m1.start(), m.start());
						duration = durationSpec.trim();
						p("duration here1", duration);
					}
					break;
				}
			}

			//match the pattern when there is no space between number and durationUnit.
			if (durationUnitSpec.equals("")) {
				MiscUtils.getLogger().debug("no space between duration and duration unit.");
				for (String s : durUnits2) {
					String instructionToCheck = checkInstructionStr(instructions);
					Pattern p = Pattern.compile(s);
					Matcher m = p.matcher(instructionToCheck);

					if (m.find()) {
						p("FOUND");
						p("instructionToCheck=" + instructionToCheck);
						p(s);
						String str1 = instructionToCheck.substring(m.start(), m.end());
						MiscUtils.getLogger().debug("str1=" + str1);
						//get numUnit out
						Pattern p1 = Pattern.compile("[0-9]+");
						Matcher m1 = p1.matcher(str1);
						if (m1.find()) {
							duration = str1.substring(m1.start(), m1.end());
							durationUnitSpec = (str1.substring(m1.end())).trim();
							MiscUtils.getLogger().debug("duration=" + duration);
							MiscUtils.getLogger().debug("durationUnitSpec=" + durationUnitSpec);
							break;
						}
					}
				}
			}

			//if durationUnit is not specified, deduce it
			if (durationUnitSpec.equals("")) {
				//    p("here?? if");
				String[] freq1 = { "\\s*(?i)OD\\s*", "\\s*(?i)BID\\s*", "\\s*(?i)TID\\s*", "\\s*(?i)QID\\s*", "\\s*(?i)Q1H\\s*", "\\s*(?i)Q2H\\s*", "\\s*(?i)Q1-2H\\s*", "\\s*(?i)Q3-4H\\s*", "\\s*(?i)Q4H\\s*", "\\s*(?i)Q4-6H\\s*", "\\s*(?i)Q6H\\s*", "\\s*(?i)Q8H\\s*", "\\s*(?i)Q12H\\s*", "\\s*(?i)QAM\\s*", "\\s*(?i)QPM\\s*", "\\s*(?i)QHS\\s*", "\\s*(?i)once daily\\s*", "\\s*(?i)twice daily\\s*", "\\s*(?i)3x day\\s*", "\\s*(?i)4x day\\s*", "\\s*(?i)3x daily\\s*", "\\s*(?i)4x daily\\s*", "\\s*(?i)daily\\s*"// put at last because if frequency is 'twice daily', it will first be detected as 'daily'
				};//QPM is once a day in the evening, qhs once a day at night.
				String[] freq2 = { "\\s*(?i)Q1Week\\s*", "\\s*(?i)Q2Week\\s*" };
				String[] freq3 = { "\\s*(?i)Q1Month\\s*", "\\s*(?i)Q3Month\\s*" };
				boolean found = false;
				for (String f1 : freq1) {
					Pattern p = Pattern.compile(f1);
					Matcher m = p.matcher(frequency);
					// p(f1);
					// p(frequency);
					if (m.find()) {

						durationUnit = "D";
						found = true;
					}
				}

				if (!found) {
					for (String f2 : freq2) {
						Pattern p2 = Pattern.compile(f2);
						Matcher m2 = p2.matcher(frequency);
						if (m2.find()) {
							durationUnit = "W";
							found = true;
						}
					}
				}

				if (!found) {
					for (String f3 : freq3) {
						Pattern p3 = Pattern.compile(f3);
						Matcher m3 = p3.matcher(frequency);
						if (m3.find()) {
							durationUnit = "M";
							found = true;
						}
					}
				}
			} else {

				//D, W,M
				if (durationUnitSpec.equalsIgnoreCase("week") || durationUnitSpec.equalsIgnoreCase("weeks") || durationUnitSpec.equalsIgnoreCase("w")) {

					durationUnit = "W";
				} else if (durationUnitSpec.equalsIgnoreCase("day") || durationUnitSpec.equalsIgnoreCase("days") || durationUnitSpec.equalsIgnoreCase("d")) {

					durationUnit = "D";
				} else if (durationUnitSpec.equalsIgnoreCase("month") || durationUnitSpec.equalsIgnoreCase("months") || durationUnitSpec.equalsIgnoreCase("m") || durationUnitSpec.equalsIgnoreCase("mo")) {

					durationUnit = "M";
				}
			}

			//make sure min is smaller than max
			if (takeMax.compareTo(takeMin) < 0) {
				//      p("max<min");
				String swap = takeMin;
				takeMin = takeMax;
				takeMax = swap;
			}

			double nPerDay = 0d;//number of drugs per day
			double nDays = 0d;//number of days per duration unit

			MiscUtils.getLogger().debug("in instrucParser,unitName=" + rx.getUnitName());
			boolean isUnitNameUsed = true;
			if (rx.getUnitName() == null || rx.getUnitName().trim().length() == 0) isUnitNameUsed = false;
			else if (rx.getUnitName().equalsIgnoreCase("null")) isUnitNameUsed = false;
			else isUnitNameUsed = true;
			MiscUtils.getLogger().debug("isUnitNameUsed=" + isUnitNameUsed);
			//if duration is 0 or null or length==0,it means duration is not specified by user
			//if quantity,frequency, durationUnit are valid values,isUnitNameUsed==false
			//yes,calculate duration based on quantity because duration is not specified
			//no,leave duration an invalid value
			//else if duration is a valid value
			//if frequency, durationUnit,takeMax are valid too
			//yes, calculate quantity
			//no, leave quantity intact.
			//--start new code
			rx.setQuantity(rx.getQuantity().trim());
			if (duration.equals("0") || duration.length() == 0 || duration == null) {//if duration is not specified, find duration based on quantity
				rx.setDurationSpecifiedByUser(false);
				if (!isUnitNameUsed && rx.getQuantity() != null && !rx.getQuantity().equalsIgnoreCase("null") && !rx.getQuantity().equals("") && !durationUnit.equals("") && !frequency.equals("") && !takeMax.equals("0")) {
					quantity = Integer.parseInt(rx.getQuantity());
					double quantityD = quantity;
					nPerDay = findNPerDay(frequency);
					nDays = findNDays(durationUnit);
					double durationD = quantityD / ((Double.parseDouble(takeMax)) * nPerDay * nDays);
					Integer durationI = (int) durationD;
					duration = durationI.toString();
					rx.setDuration(duration);
				} else rx.setDuration("0");
			} else {//if duration is valid, find quantity based on duration
				rx.setDurationSpecifiedByUser(true);
				rx.setDuration(duration);
				if (!isUnitNameUsed && !durationUnit.equals("") && !takeMin.equals("0") && !takeMax.equals("0") && !frequency.equals("")) {
					nPerDay = findNPerDay(frequency);
					nDays = findNDays(durationUnit);
					MiscUtils.getLogger().debug("in instrucParser duration=" + duration);
					//quantity=takeMax * nDays * duration * nPerDay
					double quantityD = (Double.parseDouble(takeMax)) * nPerDay * nDays * (Double.parseDouble(duration));
					quantity = (int) quantityD;
					MiscUtils.getLogger().debug("in instrucParser,else=" + quantity + "-- " + takeMax + " --" + nPerDay + "-- " + nDays + "-- " + duration);
				}

			}

			//if drug route is in rx is different from specified, set it to specified.
			if (!route.equals("") && !route.equalsIgnoreCase(rx.getRoute())) {
				rx.setRoute(route);
			}

			rx.setTakeMax(Float.parseFloat(takeMax));
			rx.setTakeMin(Float.parseFloat(takeMin));
			rx.setMethod(method);
			rx.setFrequencyCode(frequency);
			rx.setDurationUnit(durationUnit);
			rx.setPrn(prn);
			MiscUtils.getLogger().debug("in instrucParser,quantity=" + quantity + " ; unitName=" + rx.getUnitName());
			if (!isUnitNameUsed && quantity != 0) {
				rx.setQuantity(Integer.toString(quantity));
			}
			rx.setSpecial(instructions);
		}
		if (rx.isCustomNote()) {
			rx.setQuantity(null);
			rx.setUnitName(null);
		}
		rx.setPolicyViolations(policyViolations);

		p("below set special");
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("takeMin", rx.getTakeMin());
		hm.put("takeMax", rx.getTakeMax());
		hm.put("method", rx.getMethod());
		hm.put("route", rx.getRoute());
		hm.put("frequency", rx.getFrequencyCode());
		hm.put("duration", rx.getDuration());
		hm.put("durationUnit", rx.getDurationUnit());
		hm.put("prn", rx.getPrn());
		hm.put("quantity", rx.getQuantity());
		hm.put("policyViolations", policyViolations);
		//    p(instructions);
		MiscUtils.getLogger().debug("in parse instruction: " + hm);
		return;
	}

	public static boolean isStringToNumber(String s) {//see if string contains decimal or integer
		boolean retBool = false;
		Pattern p1 = Pattern.compile("\\d*\\.*\\d+");
		Matcher m1 = p1.matcher(s);
		if (m1.find()) {
			String numStr = s.substring(m1.start(), m1.end());
			String restStr = s.replace(numStr, "").trim();
			if (restStr != null && restStr.length() > 0) retBool = false;
			else retBool = true;
		} else retBool = false;

		return retBool;
	}

	public static String trimSpecial(RxPrescriptionData.Prescription rx) {
		String special = rx.getSpecial();
		if (special == null || special.trim().length() == 0) return "";

		//if rx has special instruction, remove it from special
		if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) {
			special = special.replace(rx.getSpecialInstruction(), "");
		}

		//remove Qty:num
		String regex1 = "Qty:\\s*[0-9]*\\.?[0-9]*\\s*";
		String unitName = rx.getUnitName();
		if (unitName != null && special.indexOf(unitName) != -1) {
			regex1 += "\\Q" + unitName + "\\E";
		}
		Pattern p = Pattern.compile(regex1);
		Matcher m = p.matcher(special);
		special = m.replaceAll("");

		//remove Repeats:num from special
		String regex2 = "Repeats:\\s*[0-9]*\\.?[0-9]*\\s*";
		p = Pattern.compile(regex2);
		m = p.matcher(special);
		special = m.replaceAll("");

		//remove brand name
		String regex3 = rx.getBrandName();
		if (regex3 != null) {
			regex3 = regex3.trim();
			special = special.replace(regex3, "");
		}

		//remove generic name
		String regex4 = rx.getGenericName();
		if (regex4 != null) {
			regex4 = regex4.trim();
			special = special.replace(regex4, "");
		}

		//remove custom name
		String regex5 = rx.getCustomName();
		if (regex5 != null) {
			regex5 = regex5.trim();
			special = special.replace(regex5, "");
		}

		//remove dispensing units
		special=special.replace("Units:", "");
		if(rx.getDispensingUnits()!=null)
			special=special.replace(rx.getDispensingUnits(), "");

		MiscUtils.getLogger().debug("before trimming mitte=" + special);
		String regex6 = "Mitte:\\s*[0-9]+\\s*\\w+";
		p = Pattern.compile(regex6);
		m = p.matcher(special);
		special = m.replaceAll("");
		MiscUtils.getLogger().debug("after trimming mitte special=" + special);
		//assume drug name is before method and drug name is the first part of the instruction.
		String rx_enhance = OscarProperties.getInstance().getProperty("rx_enhance");
		//rx_enhance changes the behavior by not deleting anything up to the words Take, apply..
		if (!(rx_enhance != null && rx_enhance.equals("true"))) {
			if (special.indexOf("Take") != -1) {
				special = special.substring(special.indexOf("Take"));
			} else if (special.indexOf("take") != -1) {
				special = special.substring(special.indexOf("take"));
			} else if (special.indexOf("TAKE") != -1) {
				special = special.substring(special.indexOf("TAKE"));
			} else if (special.indexOf("Apply") != -1) {
				special = special.substring(special.indexOf("Apply"));
			} else if (special.indexOf("apply") != -1) {
				special = special.substring(special.indexOf("apply"));
			} else if (special.indexOf("APPLY") != -1) {
				special = special.substring(special.indexOf("APPLY"));
			} else if (special.indexOf("Rub well in") != -1) {
				special = special.substring(special.indexOf("Rub well in"));
			} else if (special.indexOf("rub well in") != -1) {
				special = special.substring(special.indexOf("rub well in"));
			} else if (special.indexOf("RUB WELL IN") != -1) {
				special = special.substring(special.indexOf("RUB WELL IN"));
			} else if (special.indexOf("Rub Well In") != -1) {
				special = special.substring(special.indexOf("Rub Well In"));
			}
		}

		return special.trim();

	}

	public static void printStashContent(oscar.oscarRx.pageUtil.RxSessionBean bean) {
		p("***drugs in present stash,stash size", "" + bean.getStashSize());
		for (int j = 0; j < bean.getStashSize(); j++) {
			try {
				RxPrescriptionData.Prescription rxTemp = bean.getStashItem(j);
				p("stash index", "" + j);
				p("randomId", "" + rxTemp.getRandomId());
				p("generic name", rxTemp.getGenericName());
				p("special", rxTemp.getSpecial());
				p("quantity", rxTemp.getQuantity());
				p("repeat=" + rxTemp.getRepeat());
				p("atccode", rxTemp.getAtcCode());
				p("regional identifier", rxTemp.getRegionalIdentifier());
				p("---");
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		p("***done***");

	}

	public static void setDefaultSpecialQuantityRepeat(RxPrescriptionData.Prescription rx) {

		String defaultRx = OscarProperties.getInstance().getProperty("rx.default_instruction");
		if (defaultRx != null) {
			rx.setSpecial(defaultRx);
		} else {
			rx.setSpecial("");
		}
		rx.setQuantity(getDefaultQuantity());
		rx.setRepeat(0);

	}

	private static void setResultSpecialQuantityRepeat(RxPrescriptionData.Prescription rx, Drug d) {
		String qStr = d.getQuantity();
		Pattern p1 = Pattern.compile("\\d+");
		Matcher m1 = p1.matcher(qStr);
		if (m1.find()) {
			String qNum = qStr.substring(m1.start(), m1.end());
			rx.setQuantity(qNum);
			//get the quantity unit
			String qUnit = qStr.replace(qNum, "").trim();
			if (qUnit != null && qUnit.length() > 0) {
				MiscUtils.getLogger().debug("changing unitName in setResultSpecialQuantityRepeat ");
				rx.setUnitName(qUnit);
			}
		}
		rx.setUnitName(d.getUnitName());

		rx.setRepeat(d.getRepeat());
		rx.setSpecial(d.getSpecial());
		rx.setSpecial(trimSpecial(rx));
	}

	private static List<HashMap<String, String>> drugsTableQuery(String parameter, String value) {
		List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();

		DrugDao dao = (DrugDao) SpringUtils.getBean(DrugDao.class);
		for (Object[] i : dao.findByParameter(parameter, value)) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("instruction", String.valueOf(i[0]));
			hm.put("special_instruction", String.valueOf(i[1]));
			retList.add(hm);
		}

		MiscUtils.getLogger().debug("in drugsTableQuery,retList=" + retList);
		return retList;
	}

	private static List<HashMap<String, String>> getCustomNamePrevInstructions(String customName) {
		return drugsTableQuery("customName", customName);
	}

	private static List<HashMap<String, String>> getDinPrevInstructions(String din) {
		return drugsTableQuery("regional_identifier", din);
	}

	private static List<HashMap<String, String>> getBNPrevInstructions(String bn) {
		return drugsTableQuery("BN", bn);
	}

	public static List<HashMap<String, String>> getPreviousInstructions(RxPrescriptionData.Prescription rx) {
		List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
		if (rx.isCustom()) {
			String cn = rx.getCustomName();
			if (cn != null && cn.trim().length() > 0) retList = getCustomNamePrevInstructions(cn);
		} else {
			String din = rx.getRegionalIdentifier();
			if (din != null && din.trim().length() > 0) {
				retList = getDinPrevInstructions(din);
				if (retList.size() == 0) {
					retList = getBNPrevInstructions(rx.getBrandName());
					//if(retList.size()==0)
					//retList=getGNPrevInstructions(rx.getGenericName());
				}
			} else {
				String bn = rx.getBrandName();
				if (bn != null && bn.trim().length() > 0) retList = getBNPrevInstructions(bn);
			}
		}
		if (retList.size() > 0) retList = trimMedHistoryList(rx, retList);
		return retList;
	}

	private static List<HashMap<String, String>> trimMedHistoryList(RxPrescriptionData.Prescription rx, List<HashMap<String, String>> l) {

		String customName = rx.getCustomName();
		String bn = rx.getBrandName();
		List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
		if (l.size() > 0 || rx != null) {
			try {
				for (HashMap<String, String> hm : l) {
					String ins = hm.get("instruction");
					String specIns = hm.get("special_instruction");
					if (ins != null && ins.length() > 0) {
						if (customName != null && !customName.equalsIgnoreCase("null")) ins = ins.replace(customName, "");
						if (bn != null && !bn.equalsIgnoreCase("null")) ins = ins.replace(bn, "");
						if (specIns != null && !specIns.equalsIgnoreCase("null")) ins = ins.replace(specIns, "");
					}
					if (ins != null) ins = ins.replace("\n", " ").trim();
					if (specIns != null) specIns = specIns.replace("\n", " ").trim();
					HashMap<String, String> h = new HashMap<String, String>();
					h.put("instruction", removeQuantityMitteRepeat(ins));
					h.put("special_instruction", specIns);
					retList.add(h);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
			retList = commonUniqueMedHistory(retList);
		}

		return retList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<HashMap<String, String>> commonUniqueMedHistory(List<HashMap<String, String>> l) {
		MiscUtils.getLogger().debug("in commonUniqueMedHistory l=" + l);

		if (l != null && l.size() > 0) {
			HashMap<HashMap, Integer> elementCount = new HashMap<HashMap, Integer>();
			List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
			for (HashMap<String, String> hm : l) {
				if (!elementCount.containsKey(hm)) elementCount.put(hm, 1);
				else elementCount.put(hm, elementCount.get(hm) + 1);
			}
			List<Integer> count = new ArrayList(elementCount.values());
			Collections.sort(count);//ascending order
			try {
				for (int i = count.size() - 1; i >= 0; i--) {
					Set set = elementCount.keySet();
					Iterator iter = set.iterator();
					while (iter.hasNext()) {
						HashMap key = (HashMap) iter.next();
						Integer value = elementCount.get(key);
						if (value == count.get(i)) {
							retList.add(key);
							elementCount.remove(key);
							break;
						} else
						;
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
			MiscUtils.getLogger().debug("in commonUniqueMedHistory retList=" + retList);
			return retList;
		} else return l;
	}

	private static String removeQuantityMitteRepeat(String s) {
		Pattern p;
		Matcher m;
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat s=" + s);
		String regex2 = "Repeats:\\s*[0-9]*\\.?[0-9]*\\s*";
		p = Pattern.compile(regex2);
		m = p.matcher(s);
		s = m.replaceAll("");
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat regex=" + regex2);
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat after remove repeat s=" + s);

		String regex1 = "Qty:\\s*[0-9]*\\.?[0-9]*\\s*\\w*";
		p = Pattern.compile(regex1);
		m = p.matcher(s);
		s = m.replaceAll("");
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat regex=" + regex1);
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat after remove quantity =" + s);

		String regex6 = "Mitte:\\s*[0-9]*\\.?[0-9]*\\s*\\w*";
		p = Pattern.compile(regex6);
		m = p.matcher(s);
		s = m.replaceAll("");
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat regex=" + regex6);
		MiscUtils.getLogger().debug("in removeQuantityMitteRepeat after remove mitte =" + s);
		s = s.trim();
		return s;
	}

	public static void setSpecialQuantityRepeat(RxPrescriptionData.Prescription rx) {
		DrugDao dao = (DrugDao) SpringUtils.getBean(DrugDao.class);

		if (rx.getRegionalIdentifier() != null && rx.getRegionalIdentifier().length() > 1) {
			p("if1");
			p(rx.getRegionalIdentifier());
			//query the database to see if there is a rx with same din as this rx.
			// String sql = "SELECT * FROM drugs WHERE regional_identifier='" + rx.getRegionalIdentifier() + "' order by written_date desc"; //most recent is the first.

			List<Drug> drugs = dao.findByRegionBrandDemographicAndProvider(rx.getRegionalIdentifier(), rx.getBrandName(), rx.getDemographicNo(), rx.getProviderNo());
			if (drugs.isEmpty()) {
				setDefaultSpecialQuantityRepeat(rx);
			} else {
				setResultSpecialQuantityRepeat(rx, drugs.get(0));
			}
		} else {
			p("else2");
			if (rx.getBrandName() != null && rx.getBrandName().length() > 1) {
				p("if2");
				//String sql2 = "SELECT * FROM drugs WHERE BN='" + StringEscapeUtils.escapeSql(rx.getBrandName()) + "' order by written_date desc"; //most recent is the first.

				Drug drug = dao.findByBrandNameDemographicAndProvider(rx.getBrandName(), rx.getDemographicNo(), rx.getProviderNo());

				//if none, query database to see if there is rx with same brandname.
				//if there are multiple, use latest.
				if (drug == null)
				//else, set to special to "", quantity to "30", repeat to "0".
				setDefaultSpecialQuantityRepeat(rx);
				else setResultSpecialQuantityRepeat(rx, drug);
			} else {
				p("if3");
				if (rx.getCustomName() != null && rx.getCustomName().length() > 1) {
					p("customName is not null");

					Drug drug = dao.findByCustomNameDemographicIdAndProviderNo(rx.getCustomName(), rx.getDemographicNo(), rx.getProviderNo());
					//if none, query database to see if there is rx with same customName.
					//if there are multiple, use latest.
					if (drug != null) {
						setResultSpecialQuantityRepeat(rx, drug);
					} else {
						setDefaultSpecialQuantityRepeat(rx);
					}
				} else {
					//else, set to special to "", quantity to "30", repeat to "0".
					setDefaultSpecialQuantityRepeat(rx);
				}
			}
		}
	}

	private static boolean checkLastPrescribed(RxPrescriptionData.Prescription rx, int drugId) {
		//make a another query to get the latest drug with same name but archived not equals one and arhived reason equals to deleted.
		//check if drugId is greater than that compare id
		//if yes, return true;
		//if not, return false;
		boolean lastPrescribed = true;
		//need the max drugId, not using DIN because it doesn't work with customed drugs.
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		Integer lastId = dao.findLastNotArchivedId(rx.getBrandName(), rx.getGenericName(), rx.getDemographicNo());

		if (lastId != null) {
			int compareId = lastId.intValue();
			MiscUtils.getLogger().debug("compareId: " + compareId);
			if (drugId > compareId) {
				lastPrescribed = true;
			} else {
				lastPrescribed = false;
			}
		} else {
			lastPrescribed = true;
		}
		return lastPrescribed;
	}

	public static boolean checkDiscontinuedBefore(RxPrescriptionData.Prescription rx) {
		
		//check if this drug was discontinued before
		//String sql="SELECT * FROM drugs WHERE archived=1 AND (archived_reason>'' OR archived_reason<'' ) AND ATC='" + this.atcCode + "' AND regional_identifier='" + this.regionalIdentifier + "' AND demographic_no=" + this.demographicNo+" order by written_date desc";
		//the query will fail to check if a drug A is prescribed, and drug A is prescribed again, and then the first drug A is discontinued,when the second drug A is represcribed
		//or a third drug A is added, no warning will be given.
		boolean discontinuedLatest = false;

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		Drug drug = dao.findByDemographicIdRegionalIdentifierAndAtcCode(rx.getAtcCode(), rx.getRegionalIdentifier(), rx.getDemographicNo());

		if (drug != null) {//get the first result which has the largest drugid and hence the most recent result.

			int drugId = drug.getId();

			boolean isLastPrescribed = checkLastPrescribed(rx, drugId);//check if this drug was saved after discontinued.
			if (isLastPrescribed) {

				//get date discontinued
				//get reason for discontinued
				Date archivedDate = drug.getArchivedDate();
				// String archDate = rs.getString("archived_date");
				String archDate = RxUtil.DateToString(archivedDate);
				String archReason = drug.getArchivedReason();

				rx.setLastArchDate(archDate);
				rx.setLastArchReason(archReason);
				discontinuedLatest = true;
			} else {
				discontinuedLatest = false;
				MiscUtils.getLogger().debug("not last drug ");
			}
		} else {
			discontinuedLatest = false;
		}

		return discontinuedLatest;
	}

	//check to see if a represcription of a med is clicked twice.
	public static boolean isRxUniqueInStash(final oscar.oscarRx.pageUtil.RxSessionBean beanRx, final RxPrescriptionData.Prescription rx) {
		boolean unique = true;
		if (rx.isCustom()) {
			for (int j = 0; j < beanRx.getStashSize(); j++) {
				try {
					RxPrescriptionData.Prescription rxTemp = beanRx.getStashItem(j);
					//p(""+rxTemp.isCustom());
					//p(rxTemp.getCustomName());
					//p(rx.getCustomName());
					//p(""+rxTemp.isCustomNote());
					//p(""+rx.isCustomNote());
					//p(""+rxTemp.getRandomId());
					//p(""+rx.getRandomId());
					if (rxTemp.isCustom() && rxTemp.getCustomName().equals(rx.getCustomName()) && rxTemp.isCustomNote() == rx.isCustomNote() && rxTemp.getRandomId() != rx.getRandomId()) {
						p("1unique turning false");
						unique = false;
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}
		} else {
			for (int j = 0; j < beanRx.getStashSize(); j++) {
				try {
					RxPrescriptionData.Prescription rxTemp = beanRx.getStashItem(j);
					//p(rx.getBrandName());
					//p(rxTemp.getBrandName());

					//p(""+rxTemp.getRandomId());
					//p(""+rx.getRandomId());
					if (rx.getBrandName() != null && !rx.getBrandName().equalsIgnoreCase("null") && rx.getBrandName().equals(rxTemp.getBrandName()) && rxTemp.getRandomId() != rx.getRandomId()) { //GCN_SWQNO changes when drugref database is updated
						p("2unique turning false");
						unique = false;
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Error", e);
				}
			}
		}
		if (unique) {
			p("unique is true");
		}
		return unique;
	}

	public static String getSpecialInstructions() {
		String retStr = "";
		RxCodesData codesData = new RxCodesData();
		String[] specArr = codesData.getSpecialInstructions();
		List<String> specList = Arrays.asList(specArr);
		// get all past record spec inst from drugs table
		
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		List<String> resultSpecInst = dao.findSpecialInstructions();
		resultSpecInst.addAll(specList);
		Set<String> specIntSet = new HashSet<String>(resultSpecInst);//remove duplicates
		specArr = specIntSet.toArray(specArr);
		for (int i = 0; i < specArr.length; i++) {
			retStr += specArr[i];
			if (i < specArr.length - 1) retStr += "*"; //use * as a delimiter
		}
		return retStr;

	}

	public static String findInterDrugStr(final UserPropertyDAO propDAO, String provider, final RxSessionBean bean) {
		//quiry mydrugref database to get a vector with all interacting drugs
		//if effect is not null or effect is not empty string
		//get a list of all pending prescriptions' ATC codes
		//compare if anyone match,
		//if yes, get it's randomId and set an session attribute
		//if not, do nothing

		UserProperty prop = propDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
		String myDrugrefId = null;
		if (prop != null) {
			myDrugrefId = prop.getValue();
			MiscUtils.getLogger().debug("3myDrugrefId" + myDrugrefId);
		}
		RxPrescriptionData.Prescription[] rxs = bean.getStash();
		//acd contains all atccodes in stash
		Vector<String> acd = new Vector<String>();
		for (RxPrescriptionData.Prescription rxItem : rxs) {
			acd.add(rxItem.getAtcCode());
		}
		logger.debug("3acd=" + acd);

		String[] str = new String[] { "warnings_byATC,bulletins_byATC,interactions_byATC,get_guidelines" }; //NEW more efficent way of sending multiple requests at the same time.
		Vector allInteractions = new Vector();
		for (String command : str) {
			try {
				Vector v = getMyDrugrefInfo(command, acd, myDrugrefId);
				MiscUtils.getLogger().debug("2v in for loop: " + v);
				if (v != null && v.size() > 0) {
					allInteractions.addAll(v);
				}
				MiscUtils.getLogger().debug("3after all.addAll(v): " + allInteractions);
			} catch (Exception e) {
				log2.debug("3command :" + command + " " + e.getMessage());
				MiscUtils.getLogger().error("Error", e);
			}
		}
		String retStr = "";
		HashMap rethm = new HashMap();
		for (RxPrescriptionData.Prescription rxItem : rxs) {
			MiscUtils.getLogger().debug("rxItem=" + rxItem.getDrugName());
			Vector uniqueDrugNameList = new Vector();
			for (int i = 0; i < allInteractions.size(); i++) {
				Hashtable hb = (Hashtable) allInteractions.get(i);
				String interactingAtc = (String) hb.get("atc");
				String interactingDrugName = (String) hb.get("drug2");
				String effectStr = (String) hb.get("effect");
				String sigStr = (String) hb.get("significance");
				MiscUtils.getLogger().debug("findInterDrugStr=" + hb);
				if (sigStr != null) {
					if (sigStr.equals("1")) {
						sigStr = "minor";
					} else if (sigStr.equals("2")) {
						sigStr = "moderate";
					} else if (sigStr.equals("3")) {
						sigStr = "major";
					} else {
						sigStr = "unknown";
					}
				} else {
					sigStr = "unknown";
				}
				if (interactingAtc != null && interactingDrugName != null && rxItem.getAtcCode().equals(interactingAtc) && effectStr != null && effectStr.length() > 0 && !effectStr.equalsIgnoreCase("N") && !effectStr.equals(" ")) {
					MiscUtils.getLogger().debug("interactingDrugName=" + interactingDrugName);
					RxPrescriptionData.Prescription rrx = findRxFromDrugNameOrGN(rxs, interactingDrugName);

					if (rrx != null && !uniqueDrugNameList.contains(rrx.getDrugName())) {
						MiscUtils.getLogger().debug("rrx.getDrugName()=" + rrx.getDrugName());
						uniqueDrugNameList.add(rrx.getDrugName());

						String key = sigStr + "_" + rxItem.getRandomId();

						if (rethm.containsKey(key)) {
							String val = (String) rethm.get(key);
							val += ";" + rrx.getDrugName();
							rethm.put(key, val);
						} else {
							rethm.put(key, rrx.getDrugName());
						}

						key = sigStr + "_" + rrx.getRandomId();
						if (rethm.containsKey(key)) {
							String val = (String) rethm.get(key);
							val += ";" + rxItem.getDrugName();
							rethm.put(key, val);
						} else {
							rethm.put(key, rxItem.getDrugName());
						}
					}
				}
			}
			MiscUtils.getLogger().debug("***next rxItem***");
		}
		MiscUtils.getLogger().debug("rethm=" + rethm);
		retStr = rethm.toString();
		retStr = retStr.replace("}", "");
		retStr = retStr.replace("{", "");

		return retStr;
	}

	private static RxPrescriptionData.Prescription findRxFromDrugNameOrGN(final RxPrescriptionData.Prescription[] rxs, String interactingDrugName) {
		RxPrescriptionData.Prescription returnRx = null;
		for (RxPrescriptionData.Prescription rxItem : rxs) {
			if (rxItem.getDrugName().contains(interactingDrugName)) {
				returnRx = rxItem;
			} else if (rxItem.getGenericName().contains(interactingDrugName)) {
				returnRx = rxItem;
			}
		}
		return returnRx;

	}

	private static Vector getMyDrugrefInfo(String command, Vector drugs, String myDrugrefId) {
		MiscUtils.getLogger().debug("3in getMyDrugrefInfo");
		RxMyDrugrefInfoAction.removeNullFromVector(drugs);
		Vector params = new Vector();
		MiscUtils.getLogger().debug("3command,drugs,myDrugrefId= " + command + "--" + drugs + "--" + myDrugrefId);
		params.addElement(command);
		params.addElement(drugs);
		if (myDrugrefId != null && !myDrugrefId.trim().equals("")) {
			log2.debug("putting >" + myDrugrefId + "< in the request");
			params.addElement(myDrugrefId);
			//params.addElement("true");
		}
		Vector vec = new Vector();
		Object obj = callWebserviceLite("Fetch", params);
		log2.debug("RETURNED " + obj);
		if (obj instanceof Vector) {
			MiscUtils.getLogger().debug("3obj is instance of vector");
			vec = (Vector) obj;
			MiscUtils.getLogger().debug(vec);
		} else if (obj instanceof Hashtable) {
			MiscUtils.getLogger().debug("3obj is instace of hashtable");
			Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
			if (holbrook instanceof Vector) {
				MiscUtils.getLogger().debug("3holbrook is instance of vector ");
				vec = (Vector) holbrook;
				MiscUtils.getLogger().debug(vec);
			}
			Enumeration e = ((Hashtable) obj).keys();
			while (e.hasMoreElements()) {
				String s = (String) e.nextElement();
				MiscUtils.getLogger().debug(s);
				log2.debug(s + " " + ((Hashtable) obj).get(s) + " " + ((Hashtable) obj).get(s).getClass().getName());
			}
		}
		return vec;
	}

	private static final Logger log2 = MiscUtils.getLogger();

	public static Object callWebserviceLite(String procedureName, Vector params) {
		log2.debug("#CALLmyDRUGREF-" + procedureName);
		Object object = null;

		String server_url = OscarProperties.getInstance().getProperty("MY_DRUGREF_URL", "http://know2act.org/backend/api");
		MiscUtils.getLogger().debug("server_url: " + server_url);
		TimingOutCallback callback = new TimingOutCallback(30 * 1000);
		try {
			log2.debug("server_url :" + server_url);
			if (!System.getProperty("http.proxyHost", "").isEmpty()) {
				//The Lite client won't recgonize JAVA_OPTS as it uses a customized http
				XmlRpcClient server = new XmlRpcClient(server_url);
				server.executeAsync(procedureName, params, callback);
			} else {
				XmlRpcClientLite server = new XmlRpcClientLite(server_url);
				server.executeAsync(procedureName, params, callback);
			}
			object = callback.waitForResponse();
		} catch (TimeoutException e) {
			log2.debug("No response from server." + server_url);
		} catch (Throwable ethrow) {
			log2.debug("Throwing error." + ethrow.getMessage());
		}
		return object;
	}

	public static <T> HashMap<Long, T> createKeyValPair(List<T> lst) {
		HashMap<Long, T> ret = new HashMap<Long, T>();
		Long rand;
		for (T t : lst) {
			rand = Math.round(Math.random() * 1000000);
			ret.put(rand, t);
		}
		return ret;
	}

	public static void p(String str, String s) {
		MiscUtils.getLogger().debug(str + "=" + s);
	}

	public static void p(String str) {
		MiscUtils.getLogger().debug(str);
	}
}
