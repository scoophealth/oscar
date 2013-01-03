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

package org.oscarehr.casemgmt.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the casemgmt_tmpsave table.
 * Any customizations belong here.
 */
public class CaseManagementTmpSave implements Serializable {
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Long _id;// fields
    private long _demographicNo;
    private String _providerNo;
    private long _programId;
    private String _note;
    private long _note_id;
    private java.util.Date _update_date;

    // constructors
	public CaseManagementTmpSave () {
	}

	/**
	 * Constructor for primary key
	 */
	public CaseManagementTmpSave (java.lang.Long _id) {
		this.setId(_id);
	}

    /**
	 * Return the unique identifier of this class
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
    public long getDemographicNo () {
        return _demographicNo;
    }

    /**
	 * Set the value related to the column: demographic_no
     * @param _demographicNo the demographic_no value
     */
    public void setDemographicNo (long _demographicNo) {
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
	 * Return the value associated with the column: program_id
     */
    public long getProgramId () {
        return _programId;
    }

    /**
	 * Set the value related to the column: program_id
     * @param _programId the program_id value
     */
    public void setProgramId (long _programId) {
        this._programId = _programId;
    }

    /**
	 * Return the value associated with the column: note
     */
    public String getNote () {
        return _note;
    }

    /**
	 * Set the value related to the column: note
     * @param _note the note value
     */
    public void setNote (String _note) {
        this._note = _note;
    }
    
    public long getNote_id() {
        return _note_id;
    }
    
    public void setNote_id( long note_id ) {
    	/*String tmpId = Long.toString(note_id);
    	if("".equals(tmpId)|| tmpId==null) {
    		note_id = 0;
    	}*/
        this._note_id = note_id;
    }

    /**
	 * Return the value associated with the column: update_date
     */
    public java.util.Date getUpdate_date () {
        return _update_date;
    }

    /**
	 * Set the value related to the column: update_date
     * @param _update_date the update_date value
     */
    public void setUpdate_date (java.util.Date _update_date) {
        this._update_date = _update_date;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof CaseManagementTmpSave)) return false;
        else {
            CaseManagementTmpSave mObj = (CaseManagementTmpSave) obj;
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
