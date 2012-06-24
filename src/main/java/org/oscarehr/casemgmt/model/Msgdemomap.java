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
 * This is the object class that relates to the msgdemomap table.
 * Any customizations belong here.
 */
public class Msgdemomap implements Serializable {
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer _messageID;
    private Integer _demographicNo;


    // constructors
    public Msgdemomap () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public Msgdemomap (
            java.lang.Integer _messageID,
            java.lang.Integer _demographicNo) {

        this.setMessageID(_messageID);
        this.setDemographicNo(_demographicNo);
        initialize();
    }

    protected void initialize () {}

    /**
     * @hibernate.property
     *  column=messageID
     * not-null=true
     */
    public Integer getMessageID () {
        return this._messageID;
    }

    /**
     * Set the value related to the column: messageID
     * @param _messageID the messageID value
     */
    public void setMessageID (Integer _messageID) {
        this._messageID = _messageID;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * @hibernate.property
     *  column=demographic_no
     * not-null=true
     */
    public Integer getDemographicNo () {
        return this._demographicNo;
    }

    /**
     * Set the value related to the column: demographic_no
     * @param _demographicNo the demographic_no value
     */
    public void setDemographicNo (Integer _demographicNo) {
        this._demographicNo = _demographicNo;
        this.hashCode = Integer.MIN_VALUE;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Msgdemomap)) return false;
        else {
            Msgdemomap mObj = (Msgdemomap) obj;
            if (null != this.getMessageID() && null != mObj.getMessageID()) {
                if (!this.getMessageID().equals(mObj.getMessageID())) {
                    return false;
                }
            }
            else {
                return false;
            }
            if (null != this.getDemographicNo() && null != mObj.getDemographicNo()) {
                if (!this.getDemographicNo().equals(mObj.getDemographicNo())) {
                    return false;
                }
            }
            else {
                return false;
            }
            return true;
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            StringBuilder sb = new StringBuilder();
            if (null != this.getMessageID()) {
                sb.append(this.getMessageID().hashCode());
                sb.append(":");
            }
            else {
                return super.hashCode();
            }
            if (null != this.getDemographicNo()) {
                sb.append(this.getDemographicNo().hashCode());
                sb.append(":");
            }
            else {
                return super.hashCode();
            }
            this.hashCode = sb.toString().hashCode();
        }
        return this.hashCode;
    }

    public String toString () {
        return super.toString();
    }
}
