/*
 * 
 */


package oscar.form.graphic;

import java.io.*;
import java.util.*;
import oscar.util.*;

/**
 * Class FrmGraphicAR : ar2 week-height plot data
 * 2003-02-08
*/
public class FrmGraphicAR {
    int nWeek = 0;
    int nWeekDay = 0;
    float fWeek = 0f;

    public float getWeekByEDB(String fEDB, String eDate) {
        fEDB = fEDB.replace('/','-');
        eDate = eDate.replace('/','-');

        GregorianCalendar s0 = getCalendarObj(fEDB);
        s0.add(s0.DATE, -280);

        setWeekNum(s0.get(Calendar.YEAR)+"-"+(s0.get(Calendar.MONTH)+1)+"-"+s0.get(Calendar.DATE), eDate);
        return fWeek;
    }
    public String getWeekFormat() {
        return nWeek + "w" + (nWeekDay == 0 ? "" : ("+" + nWeekDay));
    }

    public int getWeekInt() {
        return nWeek;
    }

    public float getWeekNum(String sDate, String eDate) {
        setWeekNum(sDate, eDate);
        return fWeek;
    }
    public Date getStartDate(String edb) {
        edb = edb.replace('/','-');
        GregorianCalendar s0 = getCalendarObj(edb);
        s0.add(s0.DATE, -280);
        return (s0.getTime());
    }
    public String getHt(String height) {
        height = height.trim();
        if (height.endsWith("cm")) {
            height = height.substring(0, height.length()-2).trim();
        }
        return height;
    }

    private boolean checkDateStr(String strDate) {
        return true;
    }
    private void setWeekNum(String sDate, String eDate) {
        GregorianCalendar sg = getCalendarObj(sDate);
        GregorianCalendar eg = getCalendarObj(eDate);

        int n = (int) ((eg.getTimeInMillis() - sg.getTimeInMillis() + eg.get(Calendar.DST_OFFSET) )/(24 * 60 * 60 * 1000) );
        //System.out.println( eg.get(Calendar.DST_OFFSET) + " | " + sg.get(Calendar.DST_OFFSET) );
        nWeek = n / 7 ;
        nWeekDay = n % 7;
        fWeek = n / 7.f;
    }
    private GregorianCalendar getCalendarObj(String strDate) {
        GregorianCalendar strg = new GregorianCalendar();
        strg.setTime(UtilDateUtilities.StringToDate(strDate) );
        strg.set(strg.HOUR, 1);
        strg.set(strg.MINUTE, 0);
        strg.set(strg.SECOND, 0);
        return strg;
    }

    public static void main(String[] arg) {
        FrmGraphicAR test = new FrmGraphicAR();
        System.out.println(test.getWeekByEDB("2002-12-10", "2002-7-24") + " : " + test.getWeekFormat() + test.getStartDate("2002-12-10") + UtilDateUtilities.StringToDate("2002-7-24"));
    }
}
