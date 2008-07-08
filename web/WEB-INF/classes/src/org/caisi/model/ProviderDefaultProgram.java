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

package org.caisi.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the provider_default_program table.
 * Any customizations belong here.
 */
public class ProviderDefaultProgram implements Serializable {
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer _id;// fields
    private String _providerNo;
    private boolean _signnote;
    private Integer _programId;


    // constructors
    public ProviderDefaultProgram () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public ProviderDefaultProgram (Integer _id) {
        this.setId(_id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public ProviderDefaultProgram (
            Integer _id,
            String _providerNo,
            Integer _programId) {

        this.setId(_id);
        this.setProviderNo(_providerNo);
        this.setProgramId(_programId);
        initialize();
    }

    /*[CONSTRUCTOR MARKER END]*/
    protected void initialize () {}

    /**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
     */
    public Integer getId () {
        return _id;
    }

    /**
     * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (Integer _id) {
        this._id = _id;
        this.hashCode = Integer.MIN_VALUE;
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
     * Return the value associated with the column: signnote
     */
    public boolean isSignnote () {
        return _signnote;
    }

    /**
     * Set the value related to the column: signnote
     * @param _signnote the signnote value
     */
    public void setSignnote (boolean _signnote) {
        this._signnote = _signnote;
    }

    /**
     * Return the value associated with the column: program_id
     */
    public Integer getProgramId () {
        return _programId;
    }

    /**
     * Set the value related to the column: program_id
     * @param _programId the program_id value
     */
    public void setProgramId (Integer _programId) {
        this._programId = _programId;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ProviderDefaultProgram)) return false;
        else {
            ProviderDefaultProgram mObj = (ProviderDefaultProgram) obj;
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