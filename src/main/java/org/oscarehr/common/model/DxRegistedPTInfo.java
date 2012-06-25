/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

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
