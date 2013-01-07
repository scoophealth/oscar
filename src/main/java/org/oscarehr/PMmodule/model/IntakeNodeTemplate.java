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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class IntakeNodeTemplate implements Serializable {

    public static String REF = "IntakeNodeTemplate";

    private int hashCode = Integer.MIN_VALUE;// primary key
    
    private Integer id;// fields
    private Integer remoteId;// many to one
    private IntakeNodeType type;
    private IntakeNodeLabel label;// collections
    private Set<IntakeAnswerElement> answerElements;

     // constructors
	public IntakeNodeTemplate() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeNodeTemplate(Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeNodeTemplate(Integer id, org.oscarehr.PMmodule.model.IntakeNodeType type) {

		this.setId(id);
		this.setType(type);
		initialize();
	}


	protected void initialize() {
		setAnswerElements(new TreeSet<IntakeAnswerElement>());
	}
	
	public void addToanswerElements(IntakeAnswerElement intakeAnswerElement) {
		intakeAnswerElement.setNodeTemplate(this);
		addToanswerElements(intakeAnswerElement);
	}
	
	public boolean isIntake() {
		return getType().isIntakeType();
	}

	public boolean isPage() {
		return getType().isPageType();
	}

	public boolean isQuestion() {
		return getType().isQuestionType();
	}

	public boolean isSection() {
		return getType().isSectionType();
	}

	public boolean isAnswerCompound() {
		return getType().isCompoundAnswerType();
	}

	public boolean isAnswerScalar() {
		return getType().isScalarAnswerType();
	}

	public boolean isAnswerChoice() {
		return getType().isChoiceAnswerType();
	}

	public boolean isAnswerText() {
		return getType().isTextAnswerType();
	}

	public boolean isAnswerNote() {
		return getType().isNoteAnswerType();
	}

	public boolean isAnswerDate() {
		return getType().isDateAnswerType();
	}

	public boolean isAnswerBoolean() {
		Set<String> elements = new HashSet<String>();
		
		if (getAnswerElements().size() == 2) {
			for (IntakeAnswerElement answerElement : getAnswerElements()) {
				elements.add(answerElement.getElement());
			}
		}
		
		return elements.contains(IntakeAnswerElement.TRUE) && elements.contains(IntakeAnswerElement.FALSE);
	}

	public String getLabelStr() {
		return getLabel() != null ? getLabel().getLabel() : "";
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getType()).append(", ").append(getLabel()).append(")").toString();
	}

    /**
	 * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_node_template_id"
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
	 * Return the value associated with the column: remote_intake_node_template_id
     */
    public Integer getRemoteId() {
        return remoteId;
    }

    /**
	 * Set the value related to the column: remote_intake_node_template_id
     *
     * @param remoteId
     *            the remote_intake_node_template_id value
     */
    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    /**
	 * Return the value associated with the column: intake_node_type_id
     */
    public IntakeNodeType getType() {
        return type;
    }

    /**
	 * Set the value related to the column: intake_node_type_id
     *
     * @param type
     *            the intake_node_type_id value
     */
    public void setType(IntakeNodeType type) {
        this.type = type;
    }

    /**
	 * Return the value associated with the column: intake_node_label_id
     */
    public IntakeNodeLabel getLabel() {
        return label;
    }

    /**
	 * Set the value related to the column: intake_node_label_id
     *
     * @param label
     *            the intake_node_label_id value
     */
    public void setLabel(IntakeNodeLabel label) {
        this.label = label;
    }

    /**
	 * Return the value associated with the column: answerElements
     */
    public Set<IntakeAnswerElement> getAnswerElements() {
        return answerElements;
    }

    /**
	 * Set the value related to the column: answerElements
     *
     * @param answerElements
     *            the answerElements value
     */
    public void setAnswerElements(Set<IntakeAnswerElement> answerElements) {
        this.answerElements = answerElements;
    }

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeNodeTemplate))
            return false;
        else {
            IntakeNodeTemplate intakeNodeTemplate = (IntakeNodeTemplate) obj;
            if (null == this.getId() || null == intakeNodeTemplate.getId())
                return false;
            else
                return (this.getId().equals(intakeNodeTemplate.getId()));
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
