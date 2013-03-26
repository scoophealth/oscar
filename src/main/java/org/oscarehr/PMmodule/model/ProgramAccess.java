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

import com.quatro.model.security.Secrole;

/**
 * This is the object class that relates to the program_access table. Any customizations belong here.
 */
public class ProgramAccess implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key
    private Long _id;// fields
    private Long _programId;
    private String _accessTypeId;
    private boolean _allRoles;// many to one
    private AccessType _accessType;// collections
    private java.util.Set _roles;

    // constructors
     public ProgramAccess () {

     }

     /**
      * Constructor for primary key
      */
     public ProgramAccess (Long _id) {
         this.setId(_id);

     }


 
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
	 * Return the value associated with the column: access_type_id
     */
    public String getAccessTypeId () {
        return _accessTypeId;
    }

    /**
	 * Set the value related to the column: access_type_id
     * @param _accessTypeId the access_type_id value
     */
    public void setAccessTypeId (String _accessTypeId) {
        this._accessTypeId = _accessTypeId;
    }

    /**
	 * Return the value associated with the column: all_roles
     */
    public boolean isAllRoles () {
        return _allRoles;
    }

    /**
	 * Set the value related to the column: all_roles
     * @param _allRoles the all_roles value
     */
    public void setAllRoles (boolean _allRoles) {
        this._allRoles = _allRoles;
    }

    /**
     * 
*  column=access_type_id
     */
    public AccessType getAccessType () {
        return this._accessType;
    }

    /**
	 * Set the value related to the column: access_type_id
     * @param _accessType the access_type_id value
     */
    public void setAccessType (AccessType _accessType) {
        this._accessType = _accessType;
    }

    /**
	 * Return the value associated with the column: roles
     */
    public java.util.Set<Secrole> getRoles () {
        return this._roles;
    }

    /**
	 * Set the value related to the column: roles
     * @param _roles the roles value
     */
    public void setRoles (java.util.Set _roles) {
        this._roles = _roles;
    }

    public void addToRoles (Object obj) {
        if (null == this._roles) this._roles = new java.util.HashSet();
        this._roles.add(obj);
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProgramAccess)) return false;
        else {
            ProgramAccess mObj = (ProgramAccess) obj;
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
