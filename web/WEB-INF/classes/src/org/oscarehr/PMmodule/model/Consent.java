/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
package org.oscarehr.PMmodule.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the consent table.
 * Any customizations belong here.
 */
public class Consent  implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String PROP_STATUS = "status";
    public static String PROP_ANSWER1 = "answer1";
    public static String PROP_DATE_SIGNED = "dateSigned";
    public static String PROP_FORM_VERSION = "formVersion";
    public static String PROP_SIGNATURE_DECLARATION = "signatureDeclaration";
    public static String PROP_PROVIDER_NAME = "ProviderName";
    public static String PROP_REFUSED_TO_SIGN = "refusedToSign";
    public static String PROP_ANSWER2 = "answer2";
    public static String PROP_EXCLUSION_STRING = "exclusionString";
    public static String PROP_PROVIDER_NO = "ProviderNo";
    public static String PROP_FORM_NAME = "formName";
    public static String PROP_ANSWER3 = "answer3";
    public static String PROP_DEMOGRAPHIC_NO = "demographicNo";
    public static String PROP_ID = "Id";
    public static String PROP_LOCATION = "location";
    public static String PROP_HARDCOPY = "hardcopy";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private Long _demographicNo;
    private String _providerNo;
    private String _providerName;
    private java.util.Date _dateSigned;
    private Integer _answer1;
    private Integer _answer2;
    private Integer _answer3;
    private String _status;
    private boolean _hardcopy;
    private String _location;
    private String _formName;
    private String _formVersion;
    private boolean _signatureDeclaration;
    private String _exclusionString;
    private boolean _refusedToSign;

    // constructors
    public Consent () {
        initialize();
        this.setHardcopy(false);
        this.setSignatureDeclaration(false);
        this.setRefusedToSign(false);
    }

    /**
     * Constructor for primary key
     */
    public Consent (Long _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public Consent (
            Long _id,
            Long _demographicNo,
            String _providerNo) {

        this.setId(_id);
        this.setDemographicNo(_demographicNo);
        this.setProviderNo(_providerNo);
        initialize();
    }

    private String optout;
    private String exclusionString;

    public String getOptout() {
        return optout;
    }

    public void setOptout(String optout) {
        this.optout = optout;
    }

    public String getExclusionString() {
        return exclusionString;
    }

    public void setExclusionString(String exclusionString) {
        this.exclusionString = exclusionString;
    }

    protected void initialize () {}

    /**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
     */
    public Long getId () {
        return _id;
    }

    /**
     * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (Long _id) {
        this._id = _id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: demographic_no
     */
    public Long getDemographicNo () {
        return _demographicNo;
    }

    /**
     * Set the value related to the column: demographic_no
     * @param _demographicNo the demographic_no value
     */
    public void setDemographicNo (Long _demographicNo) {
        this._demographicNo = _demographicNo;
    }

    /**
     * Return the value associated with the column: provider_no
     */
    public String getProviderNo () {
        return _providerNo;
    }

    /**
     * Set the value related to the column: provider_no
     * @param _providerNo the provider_no value
     */
    public void setProviderNo (String _providerNo) {
        this._providerNo = _providerNo;
    }

    /**
     * Return the value associated with the column: provider_name
     */
    public String getProviderName () {
        return _providerName;
    }

    /**
     * Set the value related to the column: provider_name
     * @param _providerName the provider_name value
     */
    public void setProviderName (String _providerName) {
        this._providerName = _providerName;
    }

    /**
     * Return the value associated with the column: date_signed
     */
    public java.util.Date getDateSigned () {
        return _dateSigned;
    }

    /**
     * Set the value related to the column: date_signed
     * @param _dateSigned the date_signed value
     */
    public void setDateSigned (java.util.Date _dateSigned) {
        this._dateSigned = _dateSigned;
    }

    /**
     * Return the value associated with the column: answer_1
     */
    public Integer getAnswer1 () {
        return _answer1;
    }

    /**
     * Set the value related to the column: answer_1
     * @param _answer1 the answer_1 value
     */
    public void setAnswer1 (Integer _answer1) {
        this._answer1 = _answer1;
    }

    /**
     * Return the value associated with the column: answer_2
     */
    public Integer getAnswer2 () {
        return _answer2;
    }

    /**
     * Set the value related to the column: answer_2
     * @param _answer2 the answer_2 value
     */
    public void setAnswer2 (Integer _answer2) {
        this._answer2 = _answer2;
    }

    /**
     * Return the value associated with the column: answer_3
     */
    public Integer getAnswer3 () {
        return _answer3;
    }

    /**
     * Set the value related to the column: answer_3
     * @param _answer3 the answer_3 value
     */
    public void setAnswer3 (Integer _answer3) {
        this._answer3 = _answer3;
    }

    /**
     * Return the value associated with the column: status
     */
    public String getStatus () {
        return _status;
    }

    /**
     * Set the value related to the column: status
     * @param _status the status value
     */
    public void setStatus (String _status) {
        this._status = _status;
    }

    /**
     * Return the value associated with the column: hardcopy
     */
    public boolean isHardcopy () {
        return _hardcopy;
    }

    /**
     * Set the value related to the column: hardcopy
     * @param _hardcopy the hardcopy value
     */
    public void setHardcopy (boolean _hardcopy) {
        this._hardcopy = _hardcopy;
    }

    /**
     * Return the value associated with the column: location
     */
    public String getLocation () {
        return _location;
    }

    /**
     * Set the value related to the column: location
     * @param _location the location value
     */
    public void setLocation (String _location) {
        this._location = _location;
    }

    /**
     * Return the value associated with the column: form_name
     */
    public String getFormName () {
        return _formName;
    }

    /**
     * Set the value related to the column: form_name
     * @param _formName the form_name value
     */
    public void setFormName (String _formName) {
        this._formName = _formName;
    }

    /**
     * Return the value associated with the column: form_version
     */
    public String getFormVersion () {
        return _formVersion;
    }

    /**
     * Set the value related to the column: form_version
     * @param _formVersion the form_version value
     */
    public void setFormVersion (String _formVersion) {
        this._formVersion = _formVersion;
    }

    /**
     * Return the value associated with the column: signature_declaration
     */
    public boolean isSignatureDeclaration () {
        return _signatureDeclaration;
    }

    /**
     * Set the value related to the column: signature_declaration
     * @param _signatureDeclaration the signature_declaration value
     */
    public void setSignatureDeclaration (boolean _signatureDeclaration) {
        this._signatureDeclaration = _signatureDeclaration;
    }

    /**
     * Return the value associated with the column: refused
     */
    public boolean isRefusedToSign () {
        return _refusedToSign;
    }

    /**
     * Set the value related to the column: refused
     * @param _refusedToSign the refused value
     */
    public void setRefusedToSign (boolean _refusedToSign) {
        this._refusedToSign = _refusedToSign;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Consent)) return false;
        else {
            Consent mObj = (Consent) obj;
            if (null == this.getId() || null == mObj.getId()) return false;
            else return (this.getId().equals(mObj.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}