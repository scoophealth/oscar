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
 * This is the object class that relates to the client_referral table.
 * Any customizations belong here.
 */
public class ClientReferral implements Serializable {

    public static String STATUS_REJECTED = "rejected";        
    public static String STATUS_ACTIVE = "active";
    public static String STATUS_CURRENT = "current";
    public static String STATUS_UNKNOWN = "unknown";
    public static String STATUS_PENDING = "pending";


    private static final long serialVersionUID = 1L;
    public static String PROP_STATUS = "Status";
    public static String PROP_PROGRAM_NAME = "ProgramName";
    public static String PROP_AGENCY_ID = "AgencyId";
    public static String PROP_NOTES = "Notes";
    public static String PROP_PROGRAM_ID = "ProgramId";
    public static String PROP_PROGRAM_TYPE = "programType";
    public static String PROP_COMPLETION_NOTES = "CompletionNotes";
    public static String PROP_PROVIDER_LAST_NAME = "ProviderLastName";
    public static String PROP_CLIENT_ID = "ClientId";
    public static String PROP_PROVIDER_NO = "ProviderNo";
    public static String PROP_SOURCE_AGENCY_ID = "SourceAgencyId";
    public static String PROP_PROVIDER_FIRST_NAME = "ProviderFirstName";
    public static String PROP_COMPLETION_DATE = "CompletionDate";
    public static String PROP_ID = "Id";
    public static String PROP_REFERRAL_DATE = "ReferralDate";
    public static String PROP_TEMPORARY_ADMISSION = "TemporaryAdmission";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private Long _sourceAgencyId;
    private Long _agencyId;
    private Long _clientId;
    private java.util.Date _referralDate;
    private Long _providerNo;
    private String _notes;
    private String presentProblems;
    private String radioRejectionReason;
    private String _completionNotes;
    private Long _programId;
    private String _status;
    private boolean _temporaryAdmission;
    private java.util.Date _completionDate;
    private String _providerLastName;
    private String _providerFirstName;
    private String _programName;
    private String _programType;


    // constructors
	public ClientReferral () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ClientReferral (Long _id) {
		this.setId(_id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public ClientReferral (
		Long _id,
		Long _sourceAgencyId,
		Long _agencyId,
		Long _clientId,
		Long _providerNo,
		Long _programId) {

		this.setId(_id);
		this.setSourceAgencyId(_sourceAgencyId);
		this.setAgencyId(_agencyId);
		this.setClientId(_clientId);
		this.setProviderNo(_providerNo);
		this.setProgramId(_programId);
		initialize();
	}

	
	public String getProviderFormattedName() {
		return getProviderLastName() + "," + getProviderFirstName();
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="referral_id"
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
	 * Return the value associated with the column: source_agency_id
     */
    public Long getSourceAgencyId () {
        return _sourceAgencyId;
    }

    /**
	 * Set the value related to the column: source_agency_id
     * @param _sourceAgencyId the source_agency_id value
     */
    public void setSourceAgencyId (Long _sourceAgencyId) {
        this._sourceAgencyId = _sourceAgencyId;
    }

    /**
	 * Return the value associated with the column: agency_id
     */
    public Long getAgencyId () {
        return _agencyId;
    }

    /**
	 * Set the value related to the column: agency_id
     * @param _agencyId the agency_id value
     */
    public void setAgencyId (Long _agencyId) {
        this._agencyId = _agencyId;
    }

    /**
	 * Return the value associated with the column: client_id
     */
    public Long getClientId () {
        return _clientId;
    }

    /**
	 * Set the value related to the column: client_id
     * @param _clientId the client_id value
     */
    public void setClientId (Long _clientId) {
        this._clientId = _clientId;
    }

    /**
	 * Return the value associated with the column: referral_date
     */
    public java.util.Date getReferralDate () {
        return _referralDate;
    }

    /**
	 * Set the value related to the column: referral_date
     * @param _referralDate the referral_date value
     */
    public void setReferralDate (java.util.Date _referralDate) {
        this._referralDate = _referralDate;
    }

    /**
	 * Return the value associated with the column: provider_no
     */
    public Long getProviderNo () {
        return _providerNo;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param _providerNo the provider_no value
     */
    public void setProviderNo (Long _providerNo) {
        this._providerNo = _providerNo;
    }

    /**
	 * Return the value associated with the column: notes
     */
    public String getNotes () {
        return _notes;
    }

    /**
	 * Set the value related to the column: notes
     * @param _notes the notes value
     */
    public void setNotes (String _notes) {
        this._notes = _notes;
    }

    public String getPresentProblems() {
        return presentProblems;
    }

    public void setPresentProblems(String presentProblems) {
        this.presentProblems = presentProblems;
    }

    public String getRadioRejectionReason() {
        return radioRejectionReason;
    }

    public void setRadioRejectionReason(String radioRejectionReason) {
        this.radioRejectionReason = radioRejectionReason;
    }

    /**
	 * Return the value associated with the column: completion_notes
     */
    public String getCompletionNotes () {
        return _completionNotes;
    }

    /**
	 * Set the value related to the column: completion_notes
     * @param _completionNotes the completion_notes value
     */
    public void setCompletionNotes (String _completionNotes) {
        this._completionNotes = _completionNotes;
    }

    /**
	 * Return the value associated with the column: program_id
     */
    public Long getProgramId () {
        return _programId;
    }

    /**
	 * Set the value related to the column: program_id
     * @param _programId the program_id value
     */
    public void setProgramId (Long _programId) {
        this._programId = _programId;
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
	 * Return the value associated with the column: temporary_admission_flag
     */
    public boolean isTemporaryAdmission () {
        return _temporaryAdmission;
    }

    /**
	 * Set the value related to the column: temporary_admission_flag
     * @param _temporaryAdmission the temporary_admission_flag value
     */
    public void setTemporaryAdmission (boolean _temporaryAdmission) {
        this._temporaryAdmission = _temporaryAdmission;
    }

    /**
	 * Return the value associated with the column: completion_date
     */
    public java.util.Date getCompletionDate () {
        return _completionDate;
    }

    /**
	 * Set the value related to the column: completion_date
     * @param _completionDate the completion_date value
     */
    public void setCompletionDate (java.util.Date _completionDate) {
        this._completionDate = _completionDate;
    }

    /**
	 * Return the value associated with the column: ProviderLastName
     */
    public String getProviderLastName () {
        return _providerLastName;
    }

    /**
	 * Set the value related to the column: ProviderLastName
     * @param _providerLastName the ProviderLastName value
     */
    public void setProviderLastName (String _providerLastName) {
        this._providerLastName = _providerLastName;
    }

    /**
	 * Return the value associated with the column: ProviderFirstName
     */
    public String getProviderFirstName () {
        return _providerFirstName;
    }

    /**
	 * Set the value related to the column: ProviderFirstName
     * @param _providerFirstName the ProviderFirstName value
     */
    public void setProviderFirstName (String _providerFirstName) {
        this._providerFirstName = _providerFirstName;
    }

    /**
	 * Return the value associated with the column: ProgramName
     */
    public String getProgramName () {
        return _programName;
    }

    /**
	 * Set the value related to the column: ProgramName
     * @param _programName the ProgramName value
     */
    public void setProgramName (String _programName) {
        this._programName = _programName;
    }

    /**
	 * Return the value associated with the column: programType
     */
    public String getProgramType () {
        return _programType;
    }

    /**
	 * Set the value related to the column: programType
     * @param _programType the programType value
     */
    public void setProgramType (String _programType) {
        this._programType = _programType;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ClientReferral)) return false;
        else {
            ClientReferral mObj = (ClientReferral) obj;
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