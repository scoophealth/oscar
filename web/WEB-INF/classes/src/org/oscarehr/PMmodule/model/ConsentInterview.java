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
 * This is the object class that relates to the consent_interview table.
 * Any customizations belong here.
 */
public class ConsentInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String PROP_PRESSURE = "pressure";
    public static String PROP_EDUCATION = "education";
    public static String PROP_FORM_VERSION = "formVersion";
    public static String PROP_FOLLOWUP_OTHER = "followupOther";
    public static String PROP_FOLLOWUP = "followup";
    public static String PROP_INFORMATION = "information";
    public static String PROP_CONSENT_ID = "consentId";
    public static String PROP_REVIEW_OTHER = "reviewOther";
    public static String PROP_COMMENTS_OTHER = "commentsOther";
    public static String PROP_LANGUAGE = "language";
    public static String PROP_LANGUAGE_OTHER = "languageOther";
    public static String PROP_PROVIDER_NO = "ProviderNo";
    public static String PROP_PRESSURE_OTHER = "pressureOther";
    public static String PROP_FORM_NAME = "formName";
    public static String PROP_LANGUAGE_READ_OTHER = "languageReadOther";
    public static String PROP_REVIEW = "review";
    public static String PROP_DEMOGRAPHIC_NO = "demographicNo";
    public static String PROP_COMMENTS = "comments";
    public static String PROP_LANGUAGE_READ = "languageRead";
    public static String PROP_INFORMATION_OTHER = "informationOther";
    public static String PROP_ID = "Id";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private long _consentId;
    private Long _demographicNo;
    private String _providerNo;
    private String _formName;
    private String _formVersion;
    private String _language;
    private String _languageOther;
    private String _languageRead;
    private String _languageReadOther;
    private String _education;
    private String _review;
    private String _reviewOther;
    private String _pressure;
    private String _pressureOther;
    private String _information;
    private String _informationOther;
    private String _followup;
    private String _followupOther;
    private String _comments;
    private String _commentsOther;

    // constructors
    public ConsentInterview () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public ConsentInterview (Long _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public ConsentInterview (
            Long _id,
            long _consentId,
            Long _demographicNo,
            String _providerNo) {

        this.setId(_id);
        this.setConsentId(_consentId);
        this.setDemographicNo(_demographicNo);
        this.setProviderNo(_providerNo);
        initialize();
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
     * Return the value associated with the column: consent_id
     */
    public long getConsentId () {
        return _consentId;
    }

    /**
     * Set the value related to the column: consent_id
     * @param _consentId the consent_id value
     */
    public void setConsentId (long _consentId) {
        this._consentId = _consentId;
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
     * Return the value associated with the column: language
     */
    public String getLanguage () {
        return _language;
    }

    /**
     * Set the value related to the column: language
     * @param _language the language value
     */
    public void setLanguage (String _language) {
        this._language = _language;
    }

    /**
     * Return the value associated with the column: language_other
     */
    public String getLanguageOther () {
        return _languageOther;
    }

    /**
     * Set the value related to the column: language_other
     * @param _languageOther the language_other value
     */
    public void setLanguageOther (String _languageOther) {
        this._languageOther = _languageOther;
    }

    /**
     * Return the value associated with the column: language_read
     */
    public String getLanguageRead () {
        return _languageRead;
    }

    /**
     * Set the value related to the column: language_read
     * @param _languageRead the language_read value
     */
    public void setLanguageRead (String _languageRead) {
        this._languageRead = _languageRead;
    }

    /**
     * Return the value associated with the column: language_read_other
     */
    public String getLanguageReadOther () {
        return _languageReadOther;
    }

    /**
     * Set the value related to the column: language_read_other
     * @param _languageReadOther the language_read_other value
     */
    public void setLanguageReadOther (String _languageReadOther) {
        this._languageReadOther = _languageReadOther;
    }

    /**
     * Return the value associated with the column: education
     */
    public String getEducation () {
        return _education;
    }

    /**
     * Set the value related to the column: education
     * @param _education the education value
     */
    public void setEducation (String _education) {
        this._education = _education;
    }

    /**
     * Return the value associated with the column: review
     */
    public String getReview () {
        return _review;
    }

    /**
     * Set the value related to the column: review
     * @param _review the review value
     */
    public void setReview (String _review) {
        this._review = _review;
    }

    /**
     * Return the value associated with the column: review_other
     */
    public String getReviewOther () {
        return _reviewOther;
    }

    /**
     * Set the value related to the column: review_other
     * @param _reviewOther the review_other value
     */
    public void setReviewOther (String _reviewOther) {
        this._reviewOther = _reviewOther;
    }

    /**
     * Return the value associated with the column: pressure
     */
    public String getPressure () {
        return _pressure;
    }

    /**
     * Set the value related to the column: pressure
     * @param _pressure the pressure value
     */
    public void setPressure (String _pressure) {
        this._pressure = _pressure;
    }

    /**
     * Return the value associated with the column: pressure_other
     */
    public String getPressureOther () {
        return _pressureOther;
    }

    /**
     * Set the value related to the column: pressure_other
     * @param _pressureOther the pressure_other value
     */
    public void setPressureOther (String _pressureOther) {
        this._pressureOther = _pressureOther;
    }

    /**
     * Return the value associated with the column: information
     */
    public String getInformation () {
        return _information;
    }

    /**
     * Set the value related to the column: information
     * @param _information the information value
     */
    public void setInformation (String _information) {
        this._information = _information;
    }

    /**
     * Return the value associated with the column: information_other
     */
    public String getInformationOther () {
        return _informationOther;
    }

    /**
     * Set the value related to the column: information_other
     * @param _informationOther the information_other value
     */
    public void setInformationOther (String _informationOther) {
        this._informationOther = _informationOther;
    }

    /**
     * Return the value associated with the column: followup
     */
    public String getFollowup () {
        return _followup;
    }

    /**
     * Set the value related to the column: followup
     * @param _followup the followup value
     */
    public void setFollowup (String _followup) {
        this._followup = _followup;
    }

    /**
     * Return the value associated with the column: followup_other
     */
    public String getFollowupOther () {
        return _followupOther;
    }

    /**
     * Set the value related to the column: followup_other
     * @param _followupOther the followup_other value
     */
    public void setFollowupOther (String _followupOther) {
        this._followupOther = _followupOther;
    }

    /**
     * Return the value associated with the column: comments
     */
    public String getComments () {
        return _comments;
    }

    /**
     * Set the value related to the column: comments
     * @param _comments the comments value
     */
    public void setComments (String _comments) {
        this._comments = _comments;
    }

    /**
     * Return the value associated with the column: comments_other
     */
    public String getCommentsOther () {
        return _commentsOther;
    }

    /**
     * Set the value related to the column: comments_other
     * @param _commentsOther the comments_other value
     */
    public void setCommentsOther (String _commentsOther) {
        this._commentsOther = _commentsOther;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ConsentInterview)) return false;
        else {
            ConsentInterview mObj = (ConsentInterview) obj;
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