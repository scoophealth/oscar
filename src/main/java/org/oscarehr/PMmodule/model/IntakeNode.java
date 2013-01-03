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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntakeNode implements Serializable {

    private IntakeNodeWalker walker;

    public static String REF = "IntakeNode";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer id;// many to one
    private IntakeNodeTemplate nodeTemplate;
    private IntakeNodeLabel label;
    private Integer pos;
    private boolean mandatory;
    private boolean repeating;
    private boolean commonList;
    private Integer eq_to_id;
    private Integer form_version;
    private Calendar publish_date;
    private String publish_by;
    private IntakeNode parent;// collections
    private List<IntakeNode> children;
    private Set<Intake> intakes;
    private Set<IntakeAnswer> answers;
    private Integer formType;
    private String questionId;	//user supplied label.
    private String validations;
    

    public String getValidations() {
		return validations;
	}

	public void setValidations(String validations) {
		this.validations = validations;
	}

	// constructors
    public IntakeNode() {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public IntakeNode(Integer id) {
        this.setId(id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public IntakeNode(Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate) {

        this.setId(id);
        this.setNodeTemplate(nodeTemplate);
        initialize();
    }


    protected void initialize() {
        setChildren(new ArrayList<IntakeNode>());
        setIntakes(new TreeSet<Intake>());
        setAnswers(new TreeSet<IntakeAnswer>());

        walker = new IntakeNodeWalker(this);
    }

    public void addTochildren(IntakeNode child) {
        child.setParent(this);
        addTochildren(child);
    }

    public boolean isIntake() {
        return getParent() == null && getNodeTemplate().isIntake();
    }

    public boolean isPage() {
        return getNodeTemplate().isPage();
    }

    public boolean isQuestion() {
        return getNodeTemplate().isQuestion();
    }

    public boolean isSection() {
        return getNodeTemplate().isSection();
    }

    public boolean isAnswerCompound() {
        return getNodeTemplate().isAnswerCompound();
    }

    public boolean isAnswerScalar() {
        return getNodeTemplate().isAnswerScalar();
    }

    public boolean isAnswerChoice() {
        return getNodeTemplate().isAnswerChoice();
    }

    public boolean isAnswerText() {
        return getNodeTemplate().isAnswerText();
    }

    public boolean isAnswerNote() {
        return getNodeTemplate().isAnswerNote();
    }

    public boolean isAnswerDate() {
        return getNodeTemplate().isAnswerDate();
    }
    
    public boolean isAnswerBoolean() {
        return getNodeTemplate().isAnswerBoolean();
    }

    public boolean hasPages() {
        for (IntakeNode child : getChildren()) {
            if (child.isPage()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasSections() {
        for (IntakeNode child : getChildren()) {
            if (child.isSection()) {
                return true;
            }
        }

        return false;
    }

    public IntakeNode getGrandParent() {
        return getParent() != null ? getParent().getParent() : null;
    }

    public String getType() {
        return getNodeTemplate().getType().getType();
    }

    public String getIdStr() {
        return getId() != null ? getId().toString() : "";
    }

    public String getLabelStr() {
        return getLabel() != null ? getLabel().getLabel() : getNodeTemplate().getLabelStr();
    }

    public Integer getIndex() {
        if (getParent() != null) {
            List<IntakeNode> siblings = getParent().getChildren();

            for (int i = 0; i < siblings.size(); i++) {
                IntakeNode sibling = siblings.get(i);

                if (sibling.equals(this)) {
                    return i;
                }
            }
        }

        return 0;
    }

    public Set<IntakeNode> getChoiceAnswers() {
        Set<IntakeNode> choiceAnswers = new HashSet<IntakeNode>();

        for (IntakeNode child : getChildren()) {
            if (child.isAnswerChoice()) {
                choiceAnswers.add(child);
            }
        }

        return choiceAnswers;
    }

    public int getLevel() {
        return walker.getLevel();
    }

    public int getQuestionLevel() {
        return walker.getQuestionLevel();
    }

    public int getNumLevels() {
        return walker.getNumLevels();
    }

    public Set<IntakeNode> getQuestionsWithChoiceAnswers() {
        return walker.getQuestionsWithChoiceAnswers();
    }

    public SortedSet<Integer> getChoiceAnswerIds() {
        return walker.getChoiceAnswerIds();
    }

    @Override
    public String toString() {
        return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getLabel()).append(", ").append(getNodeTemplate()).append(")").toString();
    }

    /**
     * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_node_id"
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
     * Return the value associated with the column: intake_node_template_id
     */
    public IntakeNodeTemplate getNodeTemplate() {
        return nodeTemplate;
    }

    /**
     * Set the value related to the column: intake_node_template_id
     *
     * @param nodeTemplate
     *            the intake_node_template_id value
     */
    public void setNodeTemplate(IntakeNodeTemplate nodeTemplate) {
        this.nodeTemplate = nodeTemplate;
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
     * Return the value associated with the column: parent_intake_node_id
     */
    public IntakeNode getParent() {
        return parent;
    }

    /**
     * Set the value related to the column: parent_intake_node_id
     *
     * @param parent
     *            the parent_intake_node_id value
     */
    public void setParent(IntakeNode parent) {
        this.parent = parent;
    }

    /**
     * Return the value associated with the column: children
     */
    public List<IntakeNode> getChildren() {
        return children;
    }

    /**
     * Set the value related to the column: children
     *
     * @param children
     *            the children value
     */
    public void setChildren(List<IntakeNode> children) {
        this.children = children;
    }

    /**
     * Return the value associated with the column: intakes
     */
    public Set<Intake> getIntakes() {
        return intakes;
    }

    /**
     * Set the value related to the column: intakes
     *
     * @param intakes
     *            the intakes value
     */
    public void setIntakes(Set<Intake> intakes) {
        this.intakes = intakes;
    }

    public void addTointakes(Intake intake) {
        if (null == getIntakes())
            setIntakes(new TreeSet<Intake>());
        getIntakes().add(intake);
    }

    /**
     * Return the value associated with the column: answers
     */
    public Set<IntakeAnswer> getAnswers() {
        return answers;
    }

    /**
     * Set the value related to the column: answers
     *
     * @param answers
     *            the answers value
     */
    public void setAnswers(Set<IntakeAnswer> answers) {
        this.answers = answers;
    }

    public void addToanswers(IntakeAnswer intakeAnswer) {
        if (null == getAnswers())
            setAnswers(new TreeSet<IntakeAnswer>());
        getAnswers().add(intakeAnswer);
    }
    
    
    
    public Integer getPos() {
        return pos;
    }

   
    public void setPos(Integer pos) {
        this.pos = pos;
    }
    
    
    public boolean getMandatory() {
        return mandatory;
    }

   
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    public boolean getRepeating() {
        return repeating;
    }

   
    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }
    
    public boolean getCommonList() {
        return commonList;
    }

   
    public void setCommonList(boolean commonList) {
        this.commonList = commonList;
    }    
    
    
    public Integer getEq_to_id() {
	return eq_to_id;
    }
    
    
    public void setEq_to_id(Integer eq_to_id) {
	this.eq_to_id = eq_to_id;
    }
    
    
    public Integer getForm_version() {
	return form_version;
    }
    
    
    public void setForm_version(Integer form_version) {
	this.form_version = form_version;
    }
    
    
    public Calendar getPublish_date() {
        return publish_date;
    }
    
    public String getPublishDateStr() {
    	Calendar c = getPublish_date();
    	if(c!=null) {
    		return DATE_FORMAT.format(c.getTime());
    	} else {
    		return null;
    	}
	//return DATE_FORMAT.format(getPublish_date().getTime());
    }
    
    
    public void setPublish_date(Calendar publishDate) {
        this.publish_date = publishDate;
    }

    public void setPublishDateCurrent() {
        setPublish_date(Calendar.getInstance());
    }

    
    public String getPublish_by() {
	return publish_by;
    }
    
    
    public void setPublish_by(String publishBy) {
	this.publish_by = publishBy;
    }
    

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeNode))
            return false;
        else {
            IntakeNode intakeNode = (IntakeNode) obj;
            if (null == this.getId() || null == intakeNode.getId())
                return false;
            else
                return (this.getId().equals(intakeNode.getId()));
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

	public void setFormType(Integer type) {
		this.formType = type;
	}
	
	public Integer getFormType() {
		return this.formType;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	
}
