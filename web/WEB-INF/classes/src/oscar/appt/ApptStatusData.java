/*
 * 
 */


package oscar.appt;


/**
 * Class ApptStatusData : set appt status and get the icon name and link
 * 2003-01-11
*/
public final class ApptStatusData {
    String apptStatus = null;
    String[] aStatus =    {"t",            "T",        "H",       "P",          "E",            "N",          "C",          "B",          "tS",          "TS",     "HS",      "PS",      "ES",        "NS",       "CS",       "BS",        "tV",          "TV",     "HV",      "PV",      "EV",       "NV",       "CV",       "BV"};
    String[] aNextStatus ={"T",            "H",        "P",       "E",          "N",            "C",          "t",          "",           "TS",          "HS",      "PS",     "ES",      "NS",        "CS",       "tS",       "",          "TV",          "HV",      "PV",     "EV",      "NV",       "CV",       "tV",       ""};
    String[] aImageName = {"starbill.gif", "todo.gif", "here.gif","picked.gif", "empty.gif",    "noshow.gif", "cancel.gif", "billed.gif", "lts.gif",     "uts.gif", "hs.gif", "ps.gif",  "es.gif",    "noshow.gif","cancel.gif", "bs.gif", "ltv.gif",     "utv.gif", "hv.gif", "pv.gif",  "ev.gif",   "noshow.gif","cancel.gif", "bv.gif"};
    /* Here we have the keys for the message on oscarResource_*.properties 
       The page need take this String to call the i18n message             */
    String[] aTitle =     {
	"oscar.appt.ApptStatusData.msgTodo",
	"oscar.appt.ApptStatusData.magDaySheetPrinted",
	"oscar.appt.ApptStatusData.msgHere",
	"oscar.appt.ApptStatusData.msgPicked",
    "oscar.appt.ApptStatusData.msgEmpty",
	"oscar.appt.ApptStatusData.msgNoShow",
	"oscar.appt.ApptStatusData.msgCanceled",
	"oscar.appt.ApptStatusData.msgBilled",
	"oscar.appt.ApptStatusData.msgSignedTodo",
	"oscar.appt.ApptStatusData.msgSignedDaysheet",
        "oscar.appt.ApptStatusData.msgSignedHere",
	"oscar.appt.ApptStatusData.msgSignedPicked",
    "oscar.appt.ApptStatusData.msgSignedEmpty",
	"oscar.appt.ApptStatusData.msgSignedNoShow",
	"oscar.appt.ApptStatusData.msgSignedCanceled",
	"oscar.appt.ApptStatusData.msgSignedBilled",
	"oscar.appt.ApptStatusData.msgVerifiedTodo",
	"oscar.appt.ApptStatusData.msgVerifiedDaySheet",
	"oscar.appt.ApptStatusData.msgVerifiedHere",
	"oscar.appt.ApptStatusData.msgVerifiedPicked",
    "oscar.appt.ApptStatusData.msgVerifiedEmpty",
	"oscar.appt.ApptStatusData.msgVerifiedNoShow",
	"oscar.appt.ApptStatusData.msgVerifiedCanceled",
	"oscar.appt.ApptStatusData.msgVerifiedBilled",
                          };
    String[] aBgColor =   {"#FDFEC7",      "#FDFEC7",  "#00ee00", "#FFBBFF",     "#FFFF33",    "#cccccc",    "#999999",    "#3ea4e1",    "#FDFEC7",     "#FDFEC7", "#00ee00", "#FFBBFF",     "#FFFF33", "#cccccc", "#999999", "#3ea4e1",    "#FDFEC7",     "#FDFEC7", "#00ee00", "#FFBBFF",     "#FFFF33", "#cccccc", "#999999", "#3ea4e1"};
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
    public String[] getAllStatus() {
	return this.aStatus;
    }
    public String[] getAllTitle() {
	return this.aTitle;
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
