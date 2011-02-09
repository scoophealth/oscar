/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.model;

import java.io.Serializable;

/**
 *
 * @author toby
 */
public class DxRegistedPTInfo implements Serializable {

    private String strFirstName;
    private String strLastName;
    private String strSex;
    private String strDOB;
    private String strPhone;
    private String strHIN;
    private String strCodeSys;
    private String strCode;
    private String strStartDate;
    private String strUpdateDate;
    private String strStatus;

    public DxRegistedPTInfo() {
    }

    public DxRegistedPTInfo(String strFirstName, String strLastName, String strSex, String strDOB, String strPhone, String strHIN, String strCodeSys, String strCode, String strStartDate, String strUpdateDate, String strStatus) {
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strSex = strSex;
        this.strDOB = strDOB;
        this.strPhone = strPhone;
        this.strHIN = strHIN;
        this.strCodeSys = strCodeSys;
        this.strCode = strCode;
        this.strStartDate = strStartDate;
        this.strUpdateDate = strUpdateDate;
        this.strStatus = strStatus;
    }

    public String getStrCode() {
        return strCode;
    }

    public String getStrCodeSys() {
        return strCodeSys;
    }

    public String getStrDOB() {
        return strDOB;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public String getStrHIN() {
        return strHIN;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public String getStrSex() {
        return strSex;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public String getStrUpdateDate() {
        return strUpdateDate;
    }
}
