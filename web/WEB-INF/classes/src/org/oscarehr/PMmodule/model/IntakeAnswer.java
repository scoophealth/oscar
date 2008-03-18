/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

public class IntakeAnswer implements Comparable<IntakeAnswer>, Serializable {

	private static final long serialVersionUID = 1L;
    public static String REF = "IntakeAnswer";
    public static String PROP_VALUE = "value";
    public static String PROP_NODE = "node";
    public static String PROP_INTAKE = "intake";
    public static String PROP_ID = "id";
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer id;// fields
    private String value;// many to one
    private Intake intake;
    private IntakeNode node;

    public static IntakeAnswer create(IntakeNode node) {
		IntakeAnswer answer = new IntakeAnswer();
		answer.setNode(node);
		
		return answer;
	}

    public static IntakeAnswer create(IntakeNode node,Integer intake_answer_id) {
		IntakeAnswer answer = new IntakeAnswer();
		answer.setNode(node);
		answer.setId(intake_answer_id);
		return answer;
	}

    
    // constructors
	public IntakeAnswer() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeAnswer(Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeAnswer(Integer id, org.oscarehr.PMmodule.model.Intake intake, org.oscarehr.PMmodule.model.IntakeNode node, String value) {

		this.setId(id);
		this.setIntake(intake);
		this.setNode(node);
		this.setValue(value);
		initialize();
	}


	public String getValue() {
		//return getValue() != null ? getValue() : "";
		return value!=null ? value: "";
	}
	
	public boolean isAnswerScalar() {
		return getNode().isAnswerScalar();
	}
	
	public boolean isAnswerCompound() {
		return getNode().isAnswerCompound();
	}
	
	public boolean isParentQuestion() {
		return getNode().getParent().isQuestion();
	}
	
	public IntakeNode getQuestion() {
		IntakeNode question = null;
		
		IntakeNode parent = getNode().getParent();
		IntakeNode grandParent = getNode().getGrandParent();
		
		if (parent.isQuestion()) {
			question = parent;
		} else if (grandParent.isQuestion()) {
			question = grandParent;
		}
		
		return question;
	}
	
	/**
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(IntakeAnswer answer) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getId(), answer.getId());
		compareToBuilder.append(getNode().getId(), answer.getNode().getId());
		
		return compareToBuilder.toComparison();
	}
	
	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getValue()).append(")").toString();
	}

    protected void initialize() {
    }

    /**
	 * Return the unique identifier of this class
     *
     * @hibernate.id generator-class="native" column="intake_answer_id"
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
	 * Set the value related to the column: val
     *
     * @param value
     *            the val value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * Return the value associated with the column: intake_id
     */
    public Intake getIntake() {
        return intake;
    }

    /**
	 * Set the value related to the column: intake_id
     *
     * @param intake
     *            the intake_id value
     */
    public void setIntake(Intake intake) {
        this.intake = intake;
    }

    /**
	 * Return the value associated with the column: intake_node_id
     */
    public IntakeNode getNode() {
        return node;
    }

    /**
	 * Set the value related to the column: intake_node_id
     *
     * @param node
     *            the intake_node_id value
     */
    public void setNode(IntakeNode node) {
        this.node = node;
    }

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeAnswer))
            return false;
        else {
            IntakeAnswer intakeAnswer = (IntakeAnswer) obj;
            if (null == this.getId() || null == intakeAnswer.getId())
                return false;
            else
                return (this.getId().equals(intakeAnswer.getId()));
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