/*
 * 
 */
package oscar.appt;

import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarBilling.ca.on.data.BillingONDataHelp;

/**
 * Class ApptStatusData : set appt status and get the icon name and link
 * 2003-01-11
 */
public final class ApptStatusData {

    oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
    String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");
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
        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
            return getStr("icon");
        else
            return getStr(aStatus, aImageName);
    }

    public String getNextStatus() {
        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
            return getStr("nextstatus");
        else
            return getStr(aStatus, aNextStatus);
    }

    public String getTitle() {
        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
            return getStr("desc");
        else
            return getStr(aStatus, aTitle);
    }
    
    public String getBgColor() {
        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
                return getStr("color");
        else
            return getStr(aStatus, aBgColor);
    }

    private String getStr(String[] str, String[] tar) {
        String rstr = null;

        for (int i = 0; i < str.length; i++) {
            if (apptStatus.equals(aStatus[i])) {
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
            temp = status.substring(0, 1) + s;
        }
        return temp;
    }

    private String preStatus(String status, String s) {
        String temp = null;
        if (status.length() == 1) {
            temp = s;
        } else {
            temp = s + status.substring(1, 2);
        }
        return temp;
    }

    private String getStr(String kind) {
        String rstr = null;
        String strOtherIcon = "";
        String strStatus = "";

        BillingONDataHelp dbObj = new BillingONDataHelp();

        String sql = "select * from appointment_status where active='1' order by id asc";
        ResultSet rs = dbObj.searchDBRecord(sql);
        
        if (apptStatus.length()>=2){
            strOtherIcon = apptStatus.substring(1,2);
            strStatus = apptStatus.substring(0,1);
        }
        else
            strStatus = apptStatus;
            

        try {
            while (rs.next()) {
                if (kind.equals("nextstatus")) {
                    if (strStatus.equals("C")){
                        rs.first();
                        rstr = rs.getString("status");
                        if (strOtherIcon.length()==1)
                            return rstr + strOtherIcon;
                        else
                            return rstr;
                    }
                    if (strStatus.equals("B")){
                        return "";
                    }
                    if (strStatus.equals(rs.getString("status"))){
                        rs.next();
                        while (Integer.parseInt(rs.getString("active"))==0)
                            rs.next();
                        rstr = rs.getString("status");
                        if (strOtherIcon.length()==1)
                            return rstr + strOtherIcon;
                        else
                            return rstr;
                    }
                         
                }

                if (kind.equals("desc")) {
                    if (strStatus.equals(rs.getString("status"))){
                        rstr = rs.getString("description");
                        if (strOtherIcon.length()==1)
                            return rstr + "/" + (strOtherIcon.equals("S")?"Signed":"Verified") ;
                        else   
                            return rstr;
                    }
                }
                
                if (kind.equals("icon")) {
                    if (strStatus.equals(rs.getString("status"))){
                        rstr = rs.getString("icon");
                        if (strOtherIcon.length()==1)
                            return strOtherIcon + rstr;
                        else
                            return rstr;
                    }
                }

                if (kind.equals("color")) {
                    if (strStatus.equals(rs.getString("status"))){
                        rstr = rs.getString("color");
                        return rstr;
                    }
                }
            }
        } catch (SQLException e) {
             e.printStackTrace();
        }

        return rstr;
    }
}
