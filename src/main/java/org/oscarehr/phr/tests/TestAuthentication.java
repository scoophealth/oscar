/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.tests;

import java.util.Date;

import org.oscarehr.phr.PHRAuthentication;

/**
 *
 * @author apavel
 */
public class TestAuthentication implements PHRAuthentication {
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
