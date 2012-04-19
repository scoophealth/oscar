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

package oscar.login;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class LoginInfoBean : set login status when bWAN = true 2003-01-29
 */
public final class LoginInfoBean {
    private GregorianCalendar starttime = null;
    private int times = 1;
    private int status = 1; // 1 - normal, 0 - block out

    private int maxtimes = 3;
    private int maxduration = 10;

    public LoginInfoBean() {
    }

    public LoginInfoBean(GregorianCalendar starttime1, int maxtimes1, int maxduration1) {
        starttime = starttime1;
        maxtimes = maxtimes1-1;
        maxduration = maxduration1;
    }

    public void initialLoginInfoBean(GregorianCalendar starttime1) {
        starttime = starttime1;
        int times = 0;
        int status = 1; // 1 - normal, 0 - block out
    }

    public void updateLoginInfoBean(GregorianCalendar now, int times1) {
        //if time out, initial bean again.
        if (getTimeOutStatus(now)) {
            initialLoginInfoBean(now);
            return;
        }
        //else times++. if times out, status block
        ++times;
        if (times > maxtimes)
            status = 0; // 1 - normal, 0 - block out
    }

    public boolean getTimeOutStatus(GregorianCalendar now) {
        boolean btemp = false;
        //if time out and status is 1, return true
        GregorianCalendar cal = (GregorianCalendar) starttime.clone();
        cal.add(Calendar.MINUTE, maxduration);
        if (cal.getTimeInMillis() < now.getTimeInMillis())
            btemp = true; //starttime = starttime1;

        return btemp;
    }

    public void setStarttime(GregorianCalendar starttime1) {
        starttime = starttime1;
    }

    public void setTimes(int times1) {
        times = times1;
    }

    public void setStatus(int status1) {
        status = status1;
    }

    public GregorianCalendar getStarttime() {
        return (starttime);
    }

    public int getTimes() {
        return (times);
    }

    public int getStatus() {
        return (status);
    }
}
