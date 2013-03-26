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
 * This is the object class that relates to the log table.
 * Any customizations belong here.
 */
public class Log implements Serializable {

    private int hashCode = Integer.MIN_VALUE;// primary key
    private long _id;// fields
    private String _providerNo;
    private java.util.Date _dateTime;
    private String _action;
    private String _contentId;
    private String _content;
    private String _ip;

       // constructors
	public Log () {

	}

	/**
	 * Constructor for primary key
	 */
	public Log (long _id) {
		this.setId(_id);

	}

	/**
	 * Constructor for required fields
	 */
	public Log (
		long _id,
		String _content) {

		this.setId(_id);
		this.setContent(_content);

	}

    /**
	 * Return the unique identifier of this class
* 
*  generator-class="native"
*  column="id"
*/
    public long getId () {
        return _id;
    }

    /**
	 * Set the unique identifier of this class
     * @param _id the new ID
     */
    public void setId (long _id) {
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
	 * Return the value associated with the column: dateTime
     */
    public java.util.Date getDateTime () {
        return _dateTime;
    }

    /**
	 * Set the value related to the column: dateTime
     * @param _dateTime the dateTime value
     */
    public void setDateTime (java.util.Date _dateTime) {
        this._dateTime = _dateTime;
    }

    /**
	 * Return the value associated with the column: action
     */
    public String getAction () {
        return _action;
    }

    /**
	 * Set the value related to the column: action
     * @param _action the action value
     */
    public void setAction (String _action) {
        this._action = _action;
    }

    /**
	 * Return the value associated with the column: contentId
     */
    public String getContentId () {
        return _contentId;
    }

    /**
	 * Set the value related to the column: contentId
     * @param _contentId the contentId value
     */
    public void setContentId (String _contentId) {
        this._contentId = _contentId;
    }

    /**
	 * Return the value associated with the column: content
     */
    public String getContent () {
        return _content;
    }

    /**
	 * Set the value related to the column: content
     * @param _content the content value
     */
    public void setContent (String _content) {
        this._content = _content;
    }

    /**
	 * Return the value associated with the column: ip
     */
    public String getIp () {
        return _ip;
    }

    /**
	 * Set the value related to the column: ip
     * @param _ip the ip value
     */
    public void setIp (String _ip) {
        this._ip = _ip;
    }
    
    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Log)) return false;
        else {
            Log mObj = (Log) obj;
            return (this.getId() == mObj.getId());
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            return (int) this.getId();
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}
