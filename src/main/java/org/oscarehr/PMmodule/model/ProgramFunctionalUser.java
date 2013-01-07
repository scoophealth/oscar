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

import org.oscarehr.common.model.Provider;

/**
 * This is the object class that relates to the program_functional_user table. Any customizations belong here.
 */
public class ProgramFunctionalUser implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Long _id;// fields
    private Long _programId;
    private long _userTypeId;
    private Long _providerNo;// many to one
    private FunctionalUserType _userType;
    private Provider _provider;

                   // constructors
	public ProgramFunctionalUser () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramFunctionalUser (Long _id) {
		this.setId(_id);
		initialize();
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* 
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
	 * Return the value associated with the column: user_type_id
     */
    public long getUserTypeId () {
        return _userTypeId;
    }

    /**
	 * Set the value related to the column: user_type_id
     * @param _userTypeId the user_type_id value
     */
    public void setUserTypeId (long _userTypeId) {
        this._userTypeId = _userTypeId;
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
     * 
*  column=user_type_id
     */
    public FunctionalUserType getUserType () {
        return this._userType;
    }

    /**
	 * Set the value related to the column: user_type_id
     * @param _userType the user_type_id value
     */
    public void setUserType (FunctionalUserType _userType) {
        this._userType = _userType;
    }

    /**
     * 
*  column=provider_no
     */
    public Provider getProvider () {
        return this._provider;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param _provider the provider_no value
     */
    public void setProvider (Provider _provider) {
        this._provider = _provider;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProgramFunctionalUser)) return false;
        else {
            ProgramFunctionalUser mObj = (ProgramFunctionalUser) obj;
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
