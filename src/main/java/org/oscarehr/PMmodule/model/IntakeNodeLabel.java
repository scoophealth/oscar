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

public class IntakeNodeLabel implements Serializable {

    public static String REF = "IntakeNodeLabel";
    
    private int hashCode = Integer.MIN_VALUE;// primary key

    private Integer id;// fields
    private String label;

  // constructors
	public IntakeNodeLabel() {
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeLabel(Integer id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeLabel(Integer id, String label) {

		this.setId(id);
		this.setLabel(label);
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getLabel()).append(")").toString();
	}

    /**
	 * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_node_label_id"
     */
    public Integer getId() {
        return id;
    }

    /**
	 * Set the unique identifier of this class
     *
     * @param id
     *            the new ID
     */
    public void setId(Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: lbl
     */
    public String getLabel() {
        return label;
    }

    /**
	 * Set the value related to the column: lbl
     *
     * @param label
     *            the lbl value
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeNodeLabel))
            return false;
        else {
            IntakeNodeLabel intakeNodeLabel = (IntakeNodeLabel) obj;
            if (null == this.getId() || null == intakeNodeLabel.getId())
                return false;
            else
                return (this.getId().equals(intakeNodeLabel.getId()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId())
                return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }
}
