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
import java.util.Comparator;

/**
 * This is the object class that relates to the provider table. Any customizations belong here.
 */
public class Provider implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SYSTEM_PROVIDER_NO = "-1";
    public static String PROP_STATUS = "Status";
    public static String PROP_SEX = "Sex";
    public static String PROP_PROVIDER_ACTIVITY = "ProviderActivity";
    public static String PROP_PHONE = "Phone";
    public static String PROP_DOB = "Dob";
    public static String PROP_PROVIDER_NO = "ProviderNo";
    public static String PROP_TEAM = "Team";
    public static String PROP_BILLING_NO = "BillingNo";
    public static String PROP_OHIP_NO = "OhipNo";
    public static String PROP_FIRST_NAME = "FirstName";
    public static String PROP_HSO_NO = "HsoNo";
    public static String PROP_PROVIDER_TYPE = "ProviderType";
    public static String PROP_COMMENTS = "Comments";
    public static String PROP_ADDRESS = "Address";
    public static String PROP_WORK_PHONE = "WorkPhone";
    public static String PROP_RMA_NO = "RmaNo";
    public static String PROP_SPECIALTY = "Specialty";
    public static String PROP_LAST_NAME = "LastName";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private String _providerNo;// fields
    private String _comments;
    private String _phone;
    private String _billingNo;
    private String _workPhone;
    private String _address;
    private String _team;
    private String _status;
    private String _lastName;
    private String _providerType;
    private String _sex;
    private String _ohipNo;
    private String _specialty;
    private java.util.Date _dob;
    private String _hsoNo;
    private String _providerActivity;
    private String _firstName;
    private String _rmaNo;

    // constructors
	public Provider () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Provider (String _providerNo) {
		this.setProviderNo(_providerNo);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Provider (
		String _providerNo,
		String _lastName,
		String _providerType,
		String _sex,
		String _specialty,
		String _firstName) {

		this.setProviderNo(_providerNo);
		this.setLastName(_lastName);
		this.setProviderType(_providerType);
		this.setSex(_sex);
		this.setSpecialty(_specialty);
		this.setFirstName(_firstName);
		initialize();
	}

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public String getProvider_no() {
		return getProviderNo();
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="provider_no"
*/
    public String getProviderNo () {
        return _providerNo;
    }

    /**
	 * Set the unique identifier of this class
     * @param _providerNo the new ID
     */
    public void setProviderNo (String _providerNo) {
        this._providerNo = _providerNo;
        this.hashCode = Integer.MIN_VALUE;
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
	 * Return the value associated with the column: phone
     */
    public String getPhone () {
        return _phone;
    }

    /**
	 * Set the value related to the column: phone
     * @param _phone the phone value
     */
    public void setPhone (String _phone) {
        this._phone = _phone;
    }

    /**
	 * Return the value associated with the column: billing_no
     */
    public String getBillingNo () {
        return _billingNo;
    }

    /**
	 * Set the value related to the column: billing_no
     * @param _billingNo the billing_no value
     */
    public void setBillingNo (String _billingNo) {
        this._billingNo = _billingNo;
    }

    /**
	 * Return the value associated with the column: work_phone
     */
    public String getWorkPhone () {
        return _workPhone;
    }

    /**
	 * Set the value related to the column: work_phone
     * @param _workPhone the work_phone value
     */
    public void setWorkPhone (String _workPhone) {
        this._workPhone = _workPhone;
    }

    /**
	 * Return the value associated with the column: address
     */
    public String getAddress () {
        return _address;
    }

    /**
	 * Set the value related to the column: address
     * @param _address the address value
     */
    public void setAddress (String _address) {
        this._address = _address;
    }

    /**
	 * Return the value associated with the column: team
     */
    public String getTeam () {
        return _team;
    }

    /**
	 * Set the value related to the column: team
     * @param _team the team value
     */
    public void setTeam (String _team) {
        this._team = _team;
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
	 * Return the value associated with the column: last_name
     */
    public String getLastName () {
        return _lastName;
    }

    /**
	 * Set the value related to the column: last_name
     * @param _lastName the last_name value
     */
    public void setLastName (String _lastName) {
        this._lastName = _lastName;
    }

    /**
	 * Return the value associated with the column: provider_type
     */
    public String getProviderType () {
        return _providerType;
    }

    /**
	 * Set the value related to the column: provider_type
     * @param _providerType the provider_type value
     */
    public void setProviderType (String _providerType) {
        this._providerType = _providerType;
    }

    /**
	 * Return the value associated with the column: sex
     */
    public String getSex () {
        return _sex;
    }

    /**
	 * Set the value related to the column: sex
     * @param _sex the sex value
     */
    public void setSex (String _sex) {
        this._sex = _sex;
    }

    /**
	 * Return the value associated with the column: ohip_no
     */
    public String getOhipNo () {
        return _ohipNo;
    }

    /**
	 * Set the value related to the column: ohip_no
     * @param _ohipNo the ohip_no value
     */
    public void setOhipNo (String _ohipNo) {
        this._ohipNo = _ohipNo;
    }

    /**
	 * Return the value associated with the column: specialty
     */
    public String getSpecialty () {
        return _specialty;
    }

    /**
	 * Set the value related to the column: specialty
     * @param _specialty the specialty value
     */
    public void setSpecialty (String _specialty) {
        this._specialty = _specialty;
    }

    /**
	 * Return the value associated with the column: dob
     */
    public java.util.Date getDob () {
        return _dob;
    }

    /**
	 * Set the value related to the column: dob
     * @param _dob the dob value
     */
    public void setDob (java.util.Date _dob) {
        this._dob = _dob;
    }

    /**
	 * Return the value associated with the column: hso_no
     */
    public String getHsoNo () {
        return _hsoNo;
    }

    /**
	 * Set the value related to the column: hso_no
     * @param _hsoNo the hso_no value
     */
    public void setHsoNo (String _hsoNo) {
        this._hsoNo = _hsoNo;
    }

    /**
	 * Return the value associated with the column: provider_activity
     */
    public String getProviderActivity () {
        return _providerActivity;
    }

    /**
	 * Set the value related to the column: provider_activity
     * @param _providerActivity the provider_activity value
     */
    public void setProviderActivity (String _providerActivity) {
        this._providerActivity = _providerActivity;
    }

    /**
	 * Return the value associated with the column: first_name
     */
    public String getFirstName () {
        return _firstName;
    }

    /**
	 * Set the value related to the column: first_name
     * @param _firstName the first_name value
     */
    public void setFirstName (String _firstName) {
        this._firstName = _firstName;
    }

    /**
	 * Return the value associated with the column: rma_no
     */
    public String getRmaNo () {
        return _rmaNo;
    }

    /**
	 * Set the value related to the column: rma_no
     * @param _rmaNo the rma_no value
     */
    public void setRmaNo (String _rmaNo) {
        this._rmaNo = _rmaNo;
    }

   
    public ComparatorName ComparatorName() {
        return new ComparatorName();
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Provider)) return false;
        else {
            Provider mObj = (Provider) obj;
            if (null == this.getProviderNo() || null == mObj.getProviderNo()) return false;
            else return (this.getProviderNo().equals(mObj.getProviderNo()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getProviderNo()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getProviderNo().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }

    public class ComparatorName implements Comparator, Serializable{

        public int compare( Object o1, Object o2 ) {
            Provider bp1 = (Provider)o1;
            Provider bp2 = (Provider)o2;
            String lhs = bp1.getLastName() + bp1.getFirstName();
            String rhs = bp2.getLastName() + bp2.getFirstName();

            return lhs.compareTo(rhs);
        }
    }
}