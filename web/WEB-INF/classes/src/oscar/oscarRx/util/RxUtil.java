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
package oscar.oscarRx.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import oscar.oscarRx.data.RxPrescriptionData;
import java.util.regex.*;

public class RxUtil {

    private static String defaultPattern = "yyyy/MM/dd";
    private static Locale locale = Locale.CANADA;

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
            StringBuffer buf = new StringBuffer(expression);

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

    public static HashMap instrucParser(String instructions, RxPrescriptionData.Prescription rx) {

        String amount = "0";
        String route = "";
        String frequency = "";
        String form = "";
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

        Pattern prnP = Pattern.compile("\\s(?i)prn");
        Matcher prnM = prnP.matcher(instructions);
        if (prnM.find()) {
            p("prn is true");
            prn = true;
        }


        String[] routes = {"(?i)PO", "(?i)SL", "(?i)IM", "(?i)SC", "(?i)PATCH", "(?i)TOP.", "(?i)INH",
            "(?i)SUPP", "(?i)O.D.", "(?i)O.S.", "(?i)O.U.", "(?i)OD", "(?i)OS", "(?i)OU"};
        String[] frequences = {"\\s(?i)OD\\s*", "\\s(?i)BID\\s*", "\\s(?i)TID\\s*", "\\s(?i)QID\\s*", "\\s(?i)Q1H\\s*", "\\s(?i)Q2H\\s*", "\\s(?i)Q1-2H\\s*", "\\s(?i)Q3-4H\\s*", "\\s(?i)Q4H\\s*", "\\s(?i)Q4-6H\\s*",
            "\\s(?i)Q6H\\s*", "\\s(?i)Q8H\\s*", "\\s(?i)Q12H\\s*", "\\s(?i)QAM\\s*", "\\s(?i)QPM\\s*", "\\s(?i)QHS\\s*", "\\s(?i)Q1Week\\s*", "\\s(?i)Q2Week\\s*", "\\s(?i)Q1Month\\s*", "\\s(?i)Q3Month\\s*"};
        String[] methods = {"(?i)Take", "(?i)Apply", "(?i)Rub well in"};
        String[] durationUnits = {"\\s+(?i)days\\s*", "\\s+(?i)weeks\\s*", "\\s+(?i)months\\s*", "\\s+(?i)day\\s*", "\\s+(?i)week\\s*", "\\s+(?i)month\\s*",
            "\\s+(?i)d\\s*", "\\s+(?i)w\\s*", "\\s+(?i)m\\s*"};


        for (String s : routes) {
            Pattern p = Pattern.compile(s);
            Matcher matcher = p.matcher(instructions);
            if (matcher.find()) {
                route = (instructions.substring(matcher.start(), matcher.end())).trim();
                if (route.equalsIgnoreCase("OD")) {
                    //remove OD from instructions
                    String part = instructions.substring(0, matcher.start()) + " " + instructions.substring(matcher.end());
                    //if route =od,check if there is a valid frequency, there is one, then route is od.
                    //if not , set the route="",keep looping. then set frequency to be od;

                    for (String f : frequences) {
                        Pattern fPattern = Pattern.compile(f);
                        Matcher fMatcher = fPattern.matcher(part);
                        if (fMatcher.find()) {
                            frequency = part.substring(fMatcher.start(), fMatcher.end());
                            break;
                        }
                    }
                    if (frequency.equals("")) {
                        frequency = "OD";
                        route = "";
                        continue;
                    } else {
                        break;
                    }
                } else {
                    break;
                }

            }
        }
        if (route.equals("")) {
            System.out.println("route is not set");
        }

        //find frequency
        for (String s : frequences) {
            Pattern p = Pattern.compile(s);
            Matcher matcher = p.matcher(instructions);
            if (matcher.find()) {
                frequency = (instructions.substring(matcher.start(), matcher.end())).trim();
                Pattern p2 = Pattern.compile("\\s*[0-9]+\\s+" + frequency);
                Matcher m2 = p2.matcher(instructions);


                Pattern p4 = Pattern.compile("\\s*[0-9]+-[0-9]+\\s+" + frequency);
                Matcher m4 = p4.matcher(instructions);
                p("here11", instructions);
                //since "\\s+[0-9]+-[0-9]+\\s+" is a case in "\\s+[0-9]+\\s+", check the latter regex first.
                if (m4.find()) {
                    String str2 = instructions.substring(m4.start(), m4.end());
                    Pattern p5 = Pattern.compile("[0-9]+-[0-9]+");
                    Matcher m5 = p5.matcher(str2);
                    if (m5.find()) {
                        String str3 = str2.substring(m5.start(), m5.end());
                        p("here str3", str3);
                        takeMinFrequency = str3.split("-")[0];
                        takeMaxFrequency = str3.split("-")[1];
                    }
                } else if (m2.find()) {
                    String str = instructions.substring(m2.start(), m2.end());
                    Pattern p3 = Pattern.compile("[0-9]+");
                    Matcher m3 = p3.matcher(str);
                    p("here22", str);
                    if (m3.find()) {
                        amountFrequency = str.substring(m3.start(), m3.end());
                    }
                } else;
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
                method = instructions.substring(m.start(), m.end());

                Pattern p2 = Pattern.compile(method + "\\s+[0-9]+\\s+");
                Matcher m2 = p2.matcher(instructions);

                Pattern p4 = Pattern.compile(method + "\\s+[0-9]+-[0-9]+\\s+");
                Matcher m4 = p4.matcher(instructions);
                //since "\\s+[0-9]+-[0-9]+\\s+" is a case in "\\s+[0-9]+\\s+", check the latter regex first.
                if (m4.find()) {
                    p("else if 1");
                    String str2 = instructions.substring(m4.start(), m4.end());
                    Pattern p5 = Pattern.compile("[0-9]+-[0-9]+");
                    Matcher m5 = p5.matcher(str2);
                    if (m5.find()) {
                        String str3 = str2.substring(m5.start(), m5.end());
                        p("str3", str3);
                        takeMinMethod = str3.split("-")[0];
                        takeMaxMethod = str3.split("-")[1];
                    }
                } else if (m2.find()) {
                    p("if 1");
                    String str = instructions.substring(m2.start(), m2.end());
                    p("str1 ", str);
                    Pattern p3 = Pattern.compile("[0-9]+");
                    Matcher m3 = p3.matcher(str);
                    if (m3.find()) {
                        p("found1");
                        amountMethod = str.substring(m3.start(), m3.end());
                        p("amountMethod", amountMethod);
                    }
                } else;

                break;
            }
        }
        p(takeMinMethod);
        p(takeMaxMethod);
        p(takeMinFrequency);
        p(takeMaxFrequency);
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
            Pattern p = Pattern.compile(s);
            Matcher m = p.matcher(instructions);
            if (m.find()) {
                p(instructions);
                p(s);
                durationUnitSpec = (instructions.substring(m.start(), m.end())).trim();
                p("durationUnitSpec", durationUnitSpec);
                //get the number before durationUnit
                Pattern p1 = Pattern.compile("[0-9]+" + s);
                Matcher m1 = p1.matcher(instructions);
                if (m1.find()) {
                    p("" + m1.start(), "" + m.start());
                    durationSpec = instructions.substring(m1.start(), m.start());
                    duration = durationSpec.trim();
                    p("duration here1", duration);
                }
                break;
            }
        }
        String[] durUnits2 = {"\\s[0-9]+(?i)days\\s*", "\\s[0-9]+(?i)weeks\\s*", "\\s[0-9]+(?i)months\\s*", "\\s[0-9]+(?i)day\\s*", "\\s[0-9]+(?i)week\\s*", "\\s[0-9]+(?i)month\\s*",
            "\\s[0-9]+(?i)d\\s*", "\\s[0-9]+(?i)w\\s*", "\\s[0-9]+(?i)m\\s*"};
        //match the pattern when there is no space between number and durationUnit.
        if (durationUnitSpec.equals("")) {
            System.out.println("no space between duration and duration unit.");
            for (String s : durUnits2) {
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(instructions);
                if (m.find()) {
                    String str1 = instructions.substring(m.start(), m.end() - 1);
                    System.out.println("str1=" + str1);
                    //get numUnit out
                    Pattern p1 = Pattern.compile("[0-9]+");
                    Matcher m1 = p1.matcher(str1);
                    if (m1.find()) {
                        duration = str1.substring(m1.start(), m1.end());
                        durationUnitSpec = (str1.substring(m1.end())).trim();
                        System.out.println("duration=" + duration);
                        System.out.println("durationUnitSpec=" + durationUnitSpec);
                        break;
                    }
                }
            }
        }

        System.out.println("durationUnitSpec2=" + durationUnitSpec);
        //if durationUnit is not specified, deduce it
        if (durationUnitSpec.equals("")) {
            p("here?? if");
            String[] freq1 = {"\\s*(?i)OD\\s*", "\\s*(?i)BID\\s*", "\\s*(?i)TID\\s*", "\\s*(?i)QID\\s*", "\\s*(?i)Q1H\\s*", "\\s*(?i)Q2H\\s*", "\\s*(?i)Q1-2H\\s*", "\\s*(?i)Q3-4H\\s*", "\\s*(?i)Q4H\\s*", "\\s*(?i)Q4-6H\\s*",
                "\\s*(?i)Q6H\\s*", "\\s*(?i)Q8H\\s*", "\\s*(?i)Q12H\\s*", "\\s*(?i)QAM\\s*", "\\s*(?i)QPM\\s*", "\\s*(?i)QHS\\s*"};//QPM is once a day in the evening, qhs once a day at night.
            String[] freq2 = {"\\s*(?i)Q1Week\\s*", "\\s*(?i)Q2Week\\s*"};
            String[] freq3 = {"\\s*(?i)Q1Month\\s*", "\\s*(?i)Q3Month\\s*"};
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
            } else if (durationUnitSpec.equalsIgnoreCase("month") || durationUnitSpec.equalsIgnoreCase("months") || durationUnitSpec.equalsIgnoreCase("m")) {

                durationUnit = "M";
            }
        }

        //make sure min is smaller than max
        if (takeMax.compareTo(takeMin) < 0) {
            p("max<min");
            String swap = takeMin;
            takeMin = takeMax;
            takeMax = swap;
        }

        double nPerDay = 0d;//number of drugs per day
        double nDays = 0d;//number of days per duration unit


        //calculate quantity based on duration, frequency, duration unit, takeMin , takeMax
        if (duration.equals("0") || durationUnit.equals("") || takeMin.equals("0") || takeMax.equals("0") || frequency.equals("")) {
        } else {
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

            if (durationUnit.equalsIgnoreCase("D")) {
                nDays = 1;
            } else if (durationUnit.equalsIgnoreCase("W")) {
                nDays = 7;
            } else if (durationUnit.equalsIgnoreCase("M")) {
                nDays = 30;
            }

            //quantity=takeMax * nDays * duration * nPerDay
            double quantityD = (Double.parseDouble(takeMax)) * nPerDay * nDays * (Double.parseDouble(duration));
            quantity = (int) quantityD;
        }

        //if drug route is in rx is different from specified, set it to specified.
        if (!route.equals("") && !route.equalsIgnoreCase(rx.getRoute())) {
            rx.setRoute(route);
        }

        rx.setTakeMax(Float.parseFloat(takeMax));
        rx.setTakeMin(Float.parseFloat(takeMin));
        rx.setMethod(method);
        rx.setFrequencyCode(frequency);
        rx.setDuration(duration);
        rx.setDurationUnit(durationUnit);
        rx.setPrn(prn);
        rx.setQuantity(Integer.toString(quantity));
        rx.setSpecial(instructions);
        HashMap hm = new HashMap();
        hm.put("takeMin", rx.getTakeMin());
        hm.put("takeMax", rx.getTakeMax());
        hm.put("method", rx.getMethod());
        hm.put("route", rx.getRoute());
        hm.put("frequency", rx.getFrequencyCode());
        hm.put("duration", rx.getDuration());
        hm.put("durationUnit", rx.getDurationUnit());
        hm.put("prn", rx.getPrn());
        hm.put("quantity", rx.getQuantity());
        p(instructions);
        System.out.println(hm);
        return hm;
    }

    public static String trimSpecial(RxPrescriptionData.Prescription rx) {
        String special = rx.getSpecial();
        //remove Qty:num
        String regex1 = "Qty:[0-9]*";
        Pattern p = Pattern.compile(regex1);
        Matcher m = p.matcher(special);
        special = m.replaceAll("");
        //remove Repeats:num from special
        String regex2 = "Repeats:[0-9]*";
        p = Pattern.compile(regex2);
        m = p.matcher(special);
        special = m.replaceAll("");
        //remove brand name
        String regex3 = rx.getBrandName();
        if (regex3 != null) {
            p = Pattern.compile(regex3);
            m = p.matcher(special);
            special = m.replaceAll("");
        }
        //remove generic name
        String regex4 = rx.getGenericName();
        if (regex4 != null) {
            p = Pattern.compile(regex4);
            m = p.matcher(special);
            special = m.replaceAll("");
        }
        //remove custom name
        String regex5 = rx.getCustomName();
        if (regex5 != null) {
            p = Pattern.compile(regex5);
            m = p.matcher(special);
            special = m.replaceAll("");
        }
        //assume drug name is before method and drug name is the first part of the instruction.
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

        return special;

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
                e.printStackTrace();
            }
        }
        p("***done***");

    }
    public static void p(String str, String s) {
        System.out.println(str + "=" + s);
    }

    public static void p(String str) {
        System.out.println(str);
    }
}
