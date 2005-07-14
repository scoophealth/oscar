/*
 * Copyright (c) 2005. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster Unviersity
 * Hamilton Ontario, Canada
 */
package oscar.login;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.sql.SQLException;

public class LoginCheckLogin {
    boolean bWAN = true;
    Properties pvar = null;

    LoginCheckLoginBean lb = null;
    LoginInfoBean linfo = null;
    LoginList llist = null;

    String propFileName = "";
    boolean propFileFound = true;

    public LoginCheckLogin() {
    }

    public LoginCheckLogin(String propFile) {
        setOscarVariable(propFile);
    }

    public boolean isBlock(String ip) {
        boolean bBlock = false;

        //judge the local network
        if (ip.startsWith(pvar.getProperty("login_local_ip")))
            bWAN = false;

        GregorianCalendar now = new GregorianCalendar();
        while (llist == null) {
            llist = LoginList.getLoginListInstance(); //LoginInfoBean info = null;
        }
        String sTemp = null;

        //delete the old entry in the loginlist if time out
        if (bWAN && !llist.isEmpty()) {
            for (Enumeration e = llist.keys(); e.hasMoreElements();) {
                sTemp = (String) e.nextElement();
                linfo = (LoginInfoBean) llist.get(sTemp);
                if (linfo.getTimeOutStatus(now))
                    llist.remove(sTemp);
            }

            //check if it is blocked
            if (llist.get(ip) != null && ((LoginInfoBean) llist.get(ip)).getStatus() == 0)
                bBlock = true;
        }

        return bBlock;
    }

    //authenticate is used to check password
    public String[] auth(String user_name, String password, String pin, String ip) throws Exception, SQLException {
        lb = new LoginCheckLoginBean();
        lb.ini(user_name, password, pin, ip, pvar);
        return lb.authenticate();
    }

    //update login list if login failed
    public synchronized void updateLoginList(String ip) {
        if (bWAN) {
            GregorianCalendar now = new GregorianCalendar();
            if (llist.get(ip) == null) {
                linfo = new LoginInfoBean(now, Integer.parseInt(pvar.getProperty("login_max_failed_times")), Integer
                        .parseInt(pvar.getProperty("login_max_duration")));
            } else {
                linfo = (LoginInfoBean) llist.get(ip);
                linfo.updateLoginInfoBean(now, 1);
            }
            llist.put(ip, linfo);
            System.out.println(ip + "  status: " + ((LoginInfoBean) llist.get(ip)).getStatus() + " times: "
                    + linfo.getTimes() + " time: ");
        }
    }

    public void setOscarVariable(String propFile) {
        pvar = new Properties();
        pvar.setProperty("file_separator", System.getProperty("file.separator"));
        pvar.setProperty("working_dir", System.getProperty("user.dir"));
        char sep = pvar.getProperty("file_separator").toCharArray()[0];
        try {
            //This has been used to look in the users home directory that started tomcat
            propFileName = System.getProperty("user.home") + sep + propFile;
            FileInputStream fis = new FileInputStream(propFileName);

            oscar.OscarProperties p = oscar.OscarProperties.getInstance();
            p.loader(propFileName);
            pvar.load(fis);
            fis.close();
        } catch (Exception e) {
            System.out.println("*** No Property File ***");
            System.out.println("Property file not found at:");
            System.out.println(propFileName);
            //e.printStackTrace();
            propFileFound = false;
        }
    }

    public Properties getOscarVariable() {
        return pvar;
    }

    public String[] getPreferences() {
        return lb.getPreferences();
    }

}
