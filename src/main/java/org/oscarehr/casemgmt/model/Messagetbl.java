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
 * This is the object class that relates to the messagetbl table.
 * Any customizations belong here.
 */
public class Messagetbl implements Serializable {
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer _messageid;// fields
    private String _themessage;
    private byte[] _pdfattachment;
    private String _attachment;
    private Integer _sentByLocation;
    private String _sentbyNo;
    private String _thesubject;
    private String _actionstatus;
    private String _sentto;
    private String _sentby;
    private java.util.Date _theime;
    private java.util.Date _thedate;

      // constructors
	public Messagetbl () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Messagetbl (java.lang.Integer _messageid) {
		this.setMessageid(_messageid);
		initialize();
	}

protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* @hibernate.id
*  generator-class="native"
*  column="messageid"
*/
    public Integer getMessageid () {
        return _messageid;
    }

    /**
	 * Set the unique identifier of this class
     * @param _messageid the new ID
     */
    public void setMessageid (Integer _messageid) {
        this._messageid = _messageid;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: themessage
     */
    public String getThemessage () {
        return _themessage;
    }

    /**
	 * Set the value related to the column: themessage
     * @param _themessage the themessage value
     */
    public void setThemessage (String _themessage) {
        this._themessage = _themessage;
    }

    /**
	 * Return the value associated with the column: pdfattachment
     */
    public byte[] getPdfattachment () {
        return _pdfattachment;
    }

    /**
	 * Set the value related to the column: pdfattachment
     * @param _pdfattachment the pdfattachment value
     */
    public void setPdfattachment (byte[] _pdfattachment) {
        this._pdfattachment = _pdfattachment;
    }

    /**
	 * Return the value associated with the column: attachment
     */
    public String getAttachment () {
        return _attachment;
    }

    /**
	 * Set the value related to the column: attachment
     * @param _attachment the attachment value
     */
    public void setAttachment (String _attachment) {
        this._attachment = _attachment;
    }

    /**
	 * Return the value associated with the column: sentByLocation
     */
    public Integer getSentByLocation () {
        return _sentByLocation;
    }

    /**
	 * Set the value related to the column: sentByLocation
     * @param _sentByLocation the sentByLocation value
     */
    public void setSentByLocation (Integer _sentByLocation) {
        this._sentByLocation = _sentByLocation;
    }

    /**
	 * Return the value associated with the column: sentbyNo
     */
    public String getSentbyNo () {
        return _sentbyNo;
    }

    /**
	 * Set the value related to the column: sentbyNo
     * @param _sentbyNo the sentbyNo value
     */
    public void setSentbyNo (String _sentbyNo) {
        this._sentbyNo = _sentbyNo;
    }

    /**
	 * Return the value associated with the column: thesubject
     */
    public String getThesubject () {
        return _thesubject;
    }

    /**
	 * Set the value related to the column: thesubject
     * @param _thesubject the thesubject value
     */
    public void setThesubject (String _thesubject) {
        this._thesubject = _thesubject;
    }

    /**
	 * Return the value associated with the column: actionstatus
     */
    public String getActionstatus () {
        return _actionstatus;
    }

    /**
	 * Set the value related to the column: actionstatus
     * @param _actionstatus the actionstatus value
     */
    public void setActionstatus (String _actionstatus) {
        this._actionstatus = _actionstatus;
    }

    /**
	 * Return the value associated with the column: sentto
     */
    public String getSentto () {
        return _sentto;
    }

    /**
	 * Set the value related to the column: sentto
     * @param _sentto the sentto value
     */
    public void setSentto (String _sentto) {
        this._sentto = _sentto;
    }

    /**
	 * Return the value associated with the column: sentby
     */
    public String getSentby () {
        return _sentby;
    }

    /**
	 * Set the value related to the column: sentby
     * @param _sentby the sentby value
     */
    public void setSentby (String _sentby) {
        this._sentby = _sentby;
    }

    /**
	 * Return the value associated with the column: theime
     */
    public java.util.Date getTheime () {
        return _theime;
    }

    /**
	 * Set the value related to the column: theime
     * @param _theime the theime value
     */
    public void setTheime (java.util.Date _theime) {
        this._theime = _theime;
    }

    /**
	 * Return the value associated with the column: thedate
     */
    public java.util.Date getThedate () {
        return _thedate;
    }

    /**
	 * Set the value related to the column: thedate
     * @param _thedate the thedate value
     */
    public void setThedate (java.util.Date _thedate) {
        this._thedate = _thedate;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Messagetbl)) return false;
        else {
            Messagetbl mObj = (Messagetbl) obj;
            if (null == this.getMessageid() || null == mObj.getMessageid()) return false;
            else return (this.getMessageid().equals(mObj.getMessageid()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getMessageid()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getMessageid().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}
