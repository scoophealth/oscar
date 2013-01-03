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

public class IntakeNodeType implements Serializable {

	public static final Integer INTAKE_ID = 1;
	public static final Integer PAGE_ID = 2;
	public static final Integer SECTION_ID = 3;
	public static final Integer QUESTION_ID = 4;
	public static final Integer ANSWER_COMPOUND_ID = 5;
	public static final Integer ANSWER_SCALAR_CHOICE_ID = 6;
	public static final Integer ANSWER_SCALAR_TEXT_ID = 7;
	public static final Integer ANSWER_SCALAR_NOTE_ID = 8;
	public static final Integer ANSWER_SCALAR_DATE_ID = 9;
    public static String REF = "IntakeNodeType";

    private int hashCode = Integer.MIN_VALUE;// primary key
    
    private Integer id;// fields
    private String type;

     // constructors
	public IntakeNodeType() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeType(Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeType(Integer id, String type) {

		this.setId(id);
		this.setType(type);
		initialize();
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean isIntakeType() {
		return isType(INTAKE_ID);
	}

	public boolean isPageType() {
		return isType(PAGE_ID);
	}

	public boolean isSectionType() {
		return isType(SECTION_ID);
	}

	public boolean isQuestionType() {
		return isType(QUESTION_ID);
	}

	public boolean isCompoundAnswerType() {
		return isType(ANSWER_COMPOUND_ID);
	}

	public boolean isScalarAnswerType() {
		return isChoiceAnswerType() || isTextAnswerType() || isNoteAnswerType() || isDateAnswerType();
	}

	public boolean isChoiceAnswerType() {
		return isType(ANSWER_SCALAR_CHOICE_ID);
	}

	public boolean isTextAnswerType() {
		return isType(ANSWER_SCALAR_TEXT_ID);
	}

	public boolean isNoteAnswerType() {
		return isType(ANSWER_SCALAR_NOTE_ID);
	}
	
	public boolean isDateAnswerType() {
		return isType(ANSWER_SCALAR_DATE_ID);
	}

	private boolean isType(Integer id) {
		return getId() != null ? getId().equals(id) : false;
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(")").toString();
	}

    protected void initialize() {
    	//empty function?
    }

    /**
	 * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_node_type_id"
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
        if (!(obj instanceof IntakeNodeType))
            return false;
        else {
            IntakeNodeType intakeNodeType = (IntakeNodeType) obj;
            if (null == this.getId() || null == intakeNodeType.getId())
                return false;
            else
                return (this.getId().equals(intakeNodeType.getId()));
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
