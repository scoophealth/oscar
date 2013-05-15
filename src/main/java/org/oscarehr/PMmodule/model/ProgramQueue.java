/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the program_queue table.
 * Any customizations belong here.
 */
public class ProgramQueue implements Serializable {

    public static String STATUS_ADMITTED = "admitted";
    public static String STATUS_REJECTED = "rejected";
    public static String STATUS_REMOVED = "removed";
    public static String STATUS_ACTIVE = "active";


    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private Long _clientId;
    private java.util.Date _referralDate;
    private Long _providerNo;
    private String _notes;
    private Long _programId;
    private String _status;
    private boolean _temporaryAdmission;
    private Long _referralId;
    private String _programName;
    private String _providerLastName;
    private String _providerFirstName;
    private String _clientLastName;
    private String _clientFirstName;
    private String presentProblems;
    private Long headClientId = null;

    private String _vacancyName;    
    private String vacancyTemplateName;

    public String getVacancyName() {
        return _vacancyName;
    }

    public void setVacancyName(String _vacancyName) {
        this._vacancyName = _vacancyName;
    }

    
    public String getVacancyTemplateName() {
    	return vacancyTemplateName;
    }

	public void setVacancyTemplateName(String vacancyTemplateName) {
    	this.vacancyTemplateName = vacancyTemplateName;
    }

	// constructors
    public ProgramQueue () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public ProgramQueue (Long _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public ProgramQueue (
            Long _id,
            Long _clientId,
            Long _providerNo,
            Long _programId) {

        this.setId(_id);
        this.setClientId(_clientId);
        this.setProviderNo(_providerNo);
        this.setProgramId(_programId);
        initialize();
    }

    public String getProviderFormattedName() {
        return getProviderLastName() + "," + getProviderFirstName();
    }

    public String getClientFormattedName() {
        return getClientLastName() + "," + getClientFirstName();
    }

    protected void initialize () {
    	//empty
    }

    /**
     * Return the unique identifier of this class
     * 
     *  generator-class="native"
     *  column="queue_id"
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
     * Return the value associated with the column: referral_id
     */
    public Long getReferralId () {
        return _referralId;
    }

    /**
     * Set the value related to the column: referral_id
     * @param _referralId the referral_id value
     */
    public void setReferralId (Long _referralId) {
        this._referralId = _referralId;
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
     * Return the value associated with the column: ClientLastName
     */
    public String getClientLastName () {
        return _clientLastName;
    }

    /**
     * Set the value related to the column: ClientLastName
     * @param _clientLastName the ClientLastName value
     */
    public void setClientLastName (String _clientLastName) {
        this._clientLastName = _clientLastName;
    }

    /**
     * Return the value associated with the column: ClientFirstName
     */
    public String getClientFirstName () {
        return _clientFirstName;
    }

    /**
     * Set the value related to the column: ClientFirstName
     * @param _clientFirstName the ClientFirstName value
     */
    public void setClientFirstName (String _clientFirstName) {
        this._clientFirstName = _clientFirstName;
    }

    public String getPresentProblems() {
        return presentProblems;
    }

    public void setPresentProblems(String presentProblems) {
        this.presentProblems = presentProblems;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProgramQueue)) return false;
        else {
            ProgramQueue mObj = (ProgramQueue) obj;
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

    public Long getHeadClientId() {
        return headClientId;
    }

    public void setHeadClientId(Long headClientId) {
        this.headClientId = headClientId;
    }
    
    public Long getHeadRecord(){
        if ( headClientId != null)
            return headClientId;
        return _clientId;
    }
    
}
