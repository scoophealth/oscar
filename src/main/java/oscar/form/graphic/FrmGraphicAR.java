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


/*
 * 
 */


package oscar.form.graphic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

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
        s0.add(Calendar.DATE, -280);

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
        s0.add(Calendar.DATE, -280);
        return (s0.getTime());
    }
    public String getHt(String height) {
        height = height.trim();
        if (height.endsWith("cm")) {
            height = height.substring(0, height.length()-2).trim();
        }
        return height;
    }
    public String getWt(String weight) {
        weight = weight.trim();
		String ret = "";
		if (weight.length() == 0) return "-1";

		int N=weight.length();
		StringBuilder sb=new StringBuilder(N);
		for(int i=0;i<N;i++){
			char c=weight.charAt(i);
			if(c=='.')sb.append(".");
			else if(c>='0' && c<='9') sb.append(c);
			else break;
		}
		ret = sb.toString().length()>0 ? sb.toString(): "-1";
		
        return ret;
    }

    private boolean checkDateStr(String strDate) {
        return true;
    }
    private void setWeekNum(String sDate, String eDate) {
        GregorianCalendar sg = getCalendarObj(sDate);
        GregorianCalendar eg = getCalendarObj(eDate);

        int n = (int) ((eg.getTimeInMillis() - sg.getTimeInMillis() + eg.get(Calendar.DST_OFFSET) )/(24 * 60 * 60 * 1000) );

        nWeek = n / 7 ;
        nWeekDay = n % 7;
        fWeek = n / 7.f;
    }
    private GregorianCalendar getCalendarObj(String strDate) {
        GregorianCalendar strg = new GregorianCalendar();
        strg.setTime(UtilDateUtilities.StringToDate(strDate) );
        strg.set(Calendar.HOUR, 1);
        strg.set(Calendar.MINUTE, 0);
        strg.set(Calendar.SECOND, 0);
        return strg;
    }

    public static void main(String[] arg) {
        FrmGraphicAR test = new FrmGraphicAR();
        MiscUtils.getLogger().debug(test.getWeekByEDB("2002-12-10", "2002-7-24") + " : " + test.getWeekFormat() + test.getStartDate("2002-12-10") + UtilDateUtilities.StringToDate("2002-7-24"));
    }
}
