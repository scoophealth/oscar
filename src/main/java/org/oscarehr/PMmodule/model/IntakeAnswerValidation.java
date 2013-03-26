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

public class IntakeAnswerValidation implements Serializable {

    public static String REF = "IntakeAnswerValidation";
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer id;// fields
    private String type;

    // constructors
    public IntakeAnswerValidation() {
    }

    /**
     * Constructor for primary key
     */
    public IntakeAnswerValidation(Integer id) {
        this.setId(id);
    }

    /**
     * Constructor for required fields
     */
    public IntakeAnswerValidation(Integer id, String type) {

        this.setId(id);
        this.setType(type);
    }



    @Override
    public String toString() {
        return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(")").toString();
    }

    /**
     * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_answer_validation_id"
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
     * Return the value associated with the column: type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value related to the column: type
     *
     * @param type
     *            the type value
     */
    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeAnswerValidation))
            return false;
        else {
            IntakeAnswerValidation intakeAnswerValidation = (IntakeAnswerValidation) obj;
            if (null == this.getId() || null == intakeAnswerValidation.getId())
                return false;
            else
                return (this.getId().equals(intakeAnswerValidation.getId()));
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
