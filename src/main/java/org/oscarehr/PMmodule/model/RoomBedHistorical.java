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

import org.apache.commons.lang.builder.ToStringBuilder;

public class RoomBedHistorical implements Serializable {

    public static String REF = "RoomBedHistorical";
    private int hashCode = Integer.MIN_VALUE;// primary key
    private RoomBedHistoricalPK id;// fields
    private java.util.Date containEnd;

    // constructors
    public RoomBedHistorical () {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public RoomBedHistorical (org.oscarehr.PMmodule.model.RoomBedHistoricalPK id) {
        this.setId(id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public RoomBedHistorical (
            org.oscarehr.PMmodule.model.RoomBedHistoricalPK id,
            java.util.Date containEnd) {

        this.setId(id);
        this.setContainEnd(containEnd);
        initialize();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    protected void initialize () {}

    /**
     * Return the unique identifier of this class
     * 
     */
    public RoomBedHistoricalPK getId () {
        return id;
    }

    /**
     * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (RoomBedHistoricalPK id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: contain_end
     */
    public java.util.Date getContainEnd () {
        return containEnd;
    }

    /**
     * Set the value related to the column: contain_end
     * @param containEnd the contain_end value
     */
    public void setContainEnd (java.util.Date containEnd) {
        this.containEnd = containEnd;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof RoomBedHistorical)) return false;
        else {
            RoomBedHistorical roomBedHistorical = (RoomBedHistorical) obj;
            if (null == this.getId() || null == roomBedHistorical.getId()) return false;
            else return (this.getId().equals(roomBedHistorical.getId()));
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
}
