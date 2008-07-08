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

package org.oscarehr.casemgmt.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the encounterform table.
 * Any customizations belong here.
 */
public class Encounterform implements Serializable {
    private int hashCode = Integer.MIN_VALUE;// primary key
    private String _formValue;// fields
    private Integer _hidden;
    private String _formName;
    private String _formTable;

    // constructors
	public Encounterform () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Encounterform (java.lang.String _formValue) {
		this.setFormValue(_formValue);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Encounterform (
		java.lang.String _formValue,
		java.lang.Integer _hidden,
		java.lang.String _formName,
		java.lang.String _formTable) {

		this.setFormValue(_formValue);
		this.setHidden(_hidden);
		this.setFormName(_formName);
		this.setFormTable(_formTable);
		initialize();
	}

protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="form_value"
*/
    public String getFormValue () {
        return _formValue;
    }

    /**
	 * Set the unique identifier of this class
     * @param _formValue the new ID
     */
    public void setFormValue (String _formValue) {
        this._formValue = _formValue;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: hidden
     */
    public Integer getHidden () {
        return _hidden;
    }

    /**
	 * Set the value related to the column: hidden
     * @param _hidden the hidden value
     */
    public void setHidden (Integer _hidden) {
        this._hidden = _hidden;
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
	 * Return the value associated with the column: form_table
     */
    public String getFormTable () {
        return _formTable;
    }

    /**
	 * Set the value related to the column: form_table
     * @param _formTable the form_table value
     */
    public void setFormTable (String _formTable) {
        this._formTable = _formTable;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Encounterform)) return false;
        else {
            Encounterform mObj = (Encounterform) obj;
            if (null == this.getFormValue() || null == mObj.getFormValue()) return false;
            else return (this.getFormValue().equals(mObj.getFormValue()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getFormValue()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getFormValue().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}