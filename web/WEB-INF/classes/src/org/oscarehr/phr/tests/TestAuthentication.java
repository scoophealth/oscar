/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.tests;

import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.indivo.xml.talk.AuthenticateResultType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.indivo.IndivoAuthentication;
import oscar.oscarEncounter.data.EctProviderData;

/**
 *
 * @author apavel
 */
public class TestAuthentication implements PHRAuthentication {
    private static Log log = LogFactory.getLog(IndivoAuthentication.class);

    public static Log getLog() {
        return log;
    }

    public static void setLog(Log aLog) {
        log = aLog;
    }
    private String providerNo = null;
    private String token = null;
    private String userId = null;
    private String role = null;
    private String name = null;
    
    /** Creates a new instance of IndivoAuthentication */
    public TestAuthentication() {
    }
    
    public TestAuthentication(String token, String userId, String name, String role, String providerNo) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.providerNo = providerNo;
    }
    
    public Date getExpirationDate(){
        return new Date();
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }
    
    public String getNamePHRFormat() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
