/*
 * 
 */


package oscar.appt;

import java.io.*;
import java.util.*;

/**
 * Class ApptStatusData : set appt status and get the icon name and link
 * 2003-01-11
*/
public final class ApptStatusData {
    String apptStatus = null;
    String[] aStatus =    {"t",            "T",        "H",       "P",           "N",          "C",          "B",          "tS",          "TS",     "HS",      "PS",      "NS",       "CS",       "BS",        "tV",          "TV",     "HV",      "PV",      "NV",       "CV",       "BV"};
    String[] aNextStatus ={"T",            "H",        "P",       "N",           "C",          "t",          "",           "TS",          "HS",      "PS",     "NS",      "CS",       "tS",       "",          "TV",          "HV",      "PV",     "NV",      "CV",       "tV",       ""};
    String[] aImageName = {"starbill.gif", "todo.gif", "here.gif","picked.gif",  "noshow.gif", "cancel.gif", "billed.gif", "lts.gif",     "uts.gif", "hs.gif", "ps.gif",  "noshow.gif","cancel.gif", "bs.gif", "ltv.gif",     "utv.gif", "hv.gif", "pv.gif",  "noshow.gif","cancel.gif", "bv.gif"};
    String[] aTitle =     {"To Do","Daysheet Printed", "Here",    "Picked", "No Show",    "Cancelled",  "Billed", "Todo/Signed", "Daysheet Printed/Signed", "Here/Signed", "Picked/Signed", "NoShow/Signed", "Cancelled/Signed", "Billed/Signed", "Todo/Verified", "Daysheet Printed/Verified", "Here/Verified", "Picked/Verified", "NoShow/Verified", "Cancelled/Verified", "Billed/Verified"};
    String[] aBgColor =   {"#FDFEC7",      "#FDFEC7",  "#00ee00", "#FFBBFF",     "#cccccc",    "#999999",    "#3ea4e1",    "#FDFEC7",     "#FDFEC7", "#00ee00", "#FFBBFF", "#cccccc", "#999999", "#3ea4e1",    "#FDFEC7",     "#FDFEC7", "#00ee00", "#FFBBFF", "#cccccc", "#999999", "#3ea4e1"};
    //"S",          "V","",          "", "signed.gif", "verified.gif", "Signed", "Verified",   "#FFBBFF", "#FFBBFF",

    public void setApptStatus(String status) {
         apptStatus = status;
    }

    public String getImageName() {
        return getStr(aStatus,aImageName);
    }

    public String getNextStatus() {
        return getStr(aStatus,aNextStatus);
    }

    public String getTitle() {
        return getStr(aStatus,aTitle);
    }

    public String getBgColor() {
        return getStr(aStatus,aBgColor);
    }

    private String getStr(String[] str, String[] tar) { 
        String rstr = null;

        for (int i = 0 ; i < str.length ; i++) {
            if(apptStatus.equals(aStatus[i])) {
                rstr = tar[i];
                break;
            }
        }
        return rstr;
    }

    public String signStatus() {
        return appendStatus(apptStatus, "S");
    }
    public String verifyStatus() {
        return appendStatus(apptStatus, "V");
    }

    public String billStatus(String fstatus) {
        return preStatus(fstatus, "B");
    }
    public String unbillStatus(String fstatus) {
        return preStatus(fstatus, "P");
    }

    private String appendStatus(String status, String s) {
        String temp = null;
        if (status.length() == 1) {
            temp = status + s;
        } else {
            temp = status.substring(0,1) + s;
        }
        return temp;
    }
    private String preStatus(String status, String s) {
        String temp = null;
        if (status.length() == 1) {
            temp = s;
        } else {
            temp = s + status.substring(1,2);
        }
        return temp;
    }
}