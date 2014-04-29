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
package org.oscarehr.integration.mchcv;

public class HCValidationResult {

    private String responseCode;
    private String responseDescription;
    private String responseAction;
    
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String expiryDate;
    private String issueDate;
    
    public HCValidationResult() {
    }
    
    public boolean isValid() {
        
        if (responseCode == null) {
            return false;
        }
        int response = Integer.parseInt(responseCode);
        return (response >= 50) && (response <= 59);
    }

    /**
     * A two character representation of the validation response code for given health number and/or version code
     * Response code is mandatory field and is returned for each validation request submitted. 
     */
    public String getResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(String value) {
        responseCode = value;
    }

    /**
     * A description for the validation response code for given health number and/or version code. 
     * Response description is optional field and can be returned for each validation request submitted. 
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String value) {
        this.responseDescription = value;
    }
    
    /**
     * The action required of the caller for the returned response code. 
     * Response action is optional field and can be returned for each validation request submitted. 
     */
    public String getResponseAction() {
        return responseAction;
    }

    public void setResponseAction(String value) {
        this.responseAction = value;
    }

    /**
     * MOHLTC stores this value as upper case characters.  
     * A maximum of 20 characters are kept on card.  
     * No accents or other diacritic marks are stored or returned. 
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * MOHLTC stores this value as upper case characters.  
     * A maximum of 30 characters are kept on card.  
     * No accents or other diacritic marks are stored or returned.
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * The card holder’s date of birth. 
     */ 
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * The gender is returned as either an M or F, for male or female respectively. 
     */
    public String getGender() {
        return gender;
    }

    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * The date the card expires.
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String value) {
        this.issueDate = value;
    }
}
