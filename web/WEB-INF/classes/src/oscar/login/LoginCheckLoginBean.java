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
 * TEAM> This software was written for the Department of Family Medicine McMaster University
 * Hamilton Ontario, Canada
 */
package oscar.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class LoginCheckLoginBean {
    private static final Logger logger = MiscUtils.getLogger();
    private static final String LOG_PRE = "Login!@#$: ";

    private String username = "";
    private String password = "";
    private String pin = "";
    private String ip = "";

    private String userpassword = null; //your password in the table

    private String firstname = null;
    private String lastname = null;
    private String profession = null;
    private String rolename = null;
    Properties oscarVariables = null;
    private MessageDigest md;

    LoginSecurityBean secBean = null;
    DBHelp accessDB = null;

    public void ini(String user_name, String password, String pin1, String ip1, Properties variables) {
        setUsername(user_name);
        setPassword(password);
        setPin(pin1);
        setIp(ip1);
        setVariables(variables);
    }

    public String[] authenticate() throws Exception, SQLException {
        secBean = getUserID();

        // the user is not in security table
        if (secBean == null) {
            return cleanNullObj(LOG_PRE + "No Such User: " + username);
        }
        // check pin if needed

        String sPin = pin;
        if (oscar.OscarProperties.getInstance().isPINEncripted()) sPin = oscar.Misc.encryptPIN(sPin);
        
        if (isWAN() && secBean.getB_RemoteLockSet().intValue() == 1
                && (!sPin.equals(secBean.getPin()) || pin.length() < 3)) {
            return cleanNullObj(LOG_PRE + "Pin-remote needed: " + username);
        } else if (!isWAN() && secBean.getB_LocalLockSet().intValue() == 1
                && (!sPin.equals(secBean.getPin()) || pin.length() < 3)) {
            return cleanNullObj(LOG_PRE + "Pin-local needed: " + username);
        }

        if (secBean.getB_ExpireSet().intValue() == 1 && ( secBean.getDate_ExpireDate() == null || secBean.getDate_ExpireDate().before(UtilDateUtilities.now() ))) {
            return cleanNullObjExpire(LOG_PRE + "Expired: " + username);
        }
        String expired_days = "";  
        if (secBean.getB_ExpireSet().intValue() == 1 ){
        //Give warning if the password will be expired in 10 days.
            
                long date_expireDate = secBean.getDate_ExpireDate().getTime();
                long date_now = UtilDateUtilities.now().getTime();
                long date_diff = (date_expireDate - date_now)/(24*3600*1000);

                if (secBean.getB_ExpireSet().intValue() == 1 && date_diff < 11) {
                        expired_days = String.valueOf(date_diff);        	
                }
        }
        
        StringBuilder sbTemp = new StringBuilder();
        byte[] btTypeInPasswd = md.digest(password.getBytes());
        for (int i = 0; i < btTypeInPasswd.length; i++)
            sbTemp = sbTemp.append(btTypeInPasswd[i]);
        password = sbTemp.toString();

        userpassword = secBean.getPassword();
        if (userpassword.length() < 20) {
            sbTemp = new StringBuilder();
            byte[] btDBPasswd = md.digest(userpassword.getBytes());
            for (int i = 0; i < btDBPasswd.length; i++)
                sbTemp = sbTemp.append(btDBPasswd[i]);
            userpassword = sbTemp.toString();
        }

        if (password.equals(userpassword)) { // login successfully           
        	String[] strAuth = new String[6];
            strAuth[0] = secBean.getProviderNo();
            strAuth[1] = firstname;
            strAuth[2] = lastname;
            strAuth[3] = profession;
            strAuth[4] = rolename;
            strAuth[5] = expired_days;            
            return strAuth;
        } else { // login failed
            return cleanNullObj(LOG_PRE + "password failed: " + username);
        }
    }

    private String[] cleanNullObj(String errorMsg) {
        logger.info(errorMsg);
        LogAction.addLogSynchronous("", "failed", LogConst.CON_LOGIN, username, ip);
        userpassword = null;
        password = null;
        return null;
    }

    private String[] cleanNullObjExpire(String errorMsg) {
        logger.info(errorMsg);
        LogAction.addLogSynchronous("", "expired", LogConst.CON_LOGIN, username, ip);
        userpassword = null;
        password = null;
        return new String[] { "expired" };
    }

    private LoginSecurityBean getUserID() throws SQLException {
        LoginSecurityBean secBean = null;

        accessDB = new DBHelp();
        
       
        
        String sql = "select * from security where user_name = '" + StringEscapeUtils.escapeSql(username) + "'";
        ResultSet rs =  DBHandler.GetSQL(sql);
        while (rs.next()) {
            secBean = new LoginSecurityBean();
            secBean.setUser_name(oscar.Misc.getString(rs, "user_name"));
            secBean.setPassword(oscar.Misc.getString(rs, "password"));
            secBean.setProviderNo(oscar.Misc.getString(rs, "provider_no"));
            secBean.setPin(oscar.Misc.getString(rs, "pin"));
            secBean.setB_ExpireSet(new Integer(rs.getInt("b_ExpireSet")));
            secBean.setDate_ExpireDate(rs.getDate("date_ExpireDate"));
            secBean.setB_LocalLockSet(new Integer(oscar.Misc.getString(rs, "b_LocalLockSet")));
            secBean.setB_RemoteLockSet(new Integer(oscar.Misc.getString(rs, "b_RemoteLockSet")));
        }
        rs.close();

        if (secBean == null)
            return null;

        // find the detail of the user
        sql = "select first_name, last_name, provider_type from provider where provider_no = '"
                + secBean.getProviderNo() + "'";
        rs = accessDB.searchDBRecord(sql);
        while (rs.next()) {
            firstname = accessDB.getString(rs,"first_name");
            lastname = accessDB.getString(rs,"last_name");
            profession = accessDB.getString(rs,"provider_type");
        }

        // retrieve the oscar roles for this Provider as a comma separated list
        sql = "select role_name from secUserRole where activeyn=1 and provider_no = '" + secBean.getProviderNo() + "'";
        rs = accessDB.searchDBRecord(sql);
        while (rs.next()) {
            if (rolename == null) {
                rolename = accessDB.getString(rs,"role_name");
            } else {
                rolename += "," + accessDB.getString(rs,"role_name");
            }
        }

        return secBean;
    }

    public boolean isWAN() {
        boolean bWAN = true;
        if (ip.startsWith(oscarVariables.getProperty("login_local_ip")))
            bWAN = false;
        return bWAN;
    }

    public void setUsername(String user_name) {
        this.username = user_name;
    }

    public void setPassword(String password) {
        this.password = password.replace(' ', '\b'); //no white space to be allowed in the password
    }

    public void setPin(String pin1) {
        this.pin = pin1.replace(' ', '\b');
    }

    public void setIp(String ip1) {
        this.ip = ip1;
    }

    public void setVariables(Properties variables) {
        this.oscarVariables = variables;
        try {
            md = MessageDigest.getInstance("SHA"); //may get from prop file, e.g. MD5
        } catch (NoSuchAlgorithmException foo) {
            logger.error(LOG_PRE + "NoSuchAlgorithmException - SHA");
        }
    }

}
