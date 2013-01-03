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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;


public class Intake implements Serializable {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm");

	public static final String QUICK = "quick";
	public static final String INDEPTH = "indepth";
	public static final String PROGRAM = "program";
    public static String REF = "Intake";
    
    
    private int hashCode = Integer.MIN_VALUE;// primary key

    private Integer id;// fields
    private Integer clientId;
    private String staffId;
    private Calendar createdOn;// many to one    
    private IntakeNode node;// collections
    private java.util.Set<IntakeAnswer> answers;
    private String intakeStatus;
    private Integer intakeLocation;
    private Integer facilityId;
    
    public static Intake create(IntakeNode node, Integer clientId, Integer programId, String staffId) {
		Intake intake = new Intake();
		intake.setNode(node);
		intake.setClientId(clientId);
		intake.setProgramId(programId);
		intake.setStaffId(staffId);
		intake.setCreatedOn(Calendar.getInstance());		
		return intake;
	}
	
    public static Intake create(IntakeNode node, Integer clientId, Integer programId, String staffId, Integer intakeId, Integer intakeLocation) {
		Intake intake = new Intake();
		intake.setNode(node);
		intake.setClientId(clientId);
		intake.setProgramId(programId);
		intake.setStaffId(staffId);
		intake.setCreatedOn(Calendar.getInstance());
		
		intake.setId(intakeId);
		intake.setIntakeLocation(intakeLocation);
		
		return intake;
	}
    
	private Demographic client;
	private Provider staff;
	private Integer programId;

	// constructors
	public Intake() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public Intake(Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public Intake(Integer id, org.oscarehr.PMmodule.model.IntakeNode node, Integer clientId, String staffId, java.util.Calendar createdOn) {

		this.setId(id);
		this.setNode(node);
		this.setClientId(clientId);
		this.setStaffId(staffId);
		this.setCreatedOn(createdOn);
		initialize();
	}
	
	
	protected void initialize() {
		setAnswers(new TreeSet<IntakeAnswer>());
	}
	
	public void addToanswers(IntakeAnswer intakeAnswer) {
		intakeAnswer.setIntake(this);
        if (null == getAnswers())
                setAnswers(new java.util.TreeSet<org.oscarehr.PMmodule.model.IntakeAnswer>());
        getAnswers().add(intakeAnswer);
	}
	
	public Map<String, String> getAnswerKeyValues() {
		Map<String, String> keyValues = new HashMap<String, String>();
		
		for (IntakeAnswer answer : getAnswers()) {
			String key = answer.getQuestion().getLabelStr();
			StringBuilder value = new StringBuilder();
			
			if (answer.isAnswerCompound()) {
				for (IntakeNode node : answer.getNode().getChildren()) {
					value.append(getAnswerMapped(node.getIdStr()).getValue()).append(" ");
				}
			} else if (answer.isAnswerScalar() && answer.isParentQuestion()) {
				value.append(answer.getValue());
			}
			
			keyValues.put(key, value.toString().trim());
		}
		
		return keyValues;
	}
	
	public List<String> getBooleanAnswerIds() {
		List<String> ids = new ArrayList<String>();
		
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().isAnswerBoolean()) {
				ids.add(answer.getNode().getIdStr());
			}
		}		
		
		return ids;
	}
	
	/*
	 * Added the eq_to_id check for getting mapped answers from older versions.
	 */
	public IntakeAnswer getAnswerMapped(String key) {
		if(key.indexOf("-")!= -1) {
			return getRepeatingAnswerMapped(key.substring(0,key.indexOf("-")),Integer.parseInt(key.substring(key.indexOf("-")+1)));
		}
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().getIdStr().equals(key) || String.valueOf(answer.getNode().getEq_to_id()).equals(key) ) {
				return answer;
			}
		}
		
		throw new IllegalStateException("No answer found with key: " + key);
	}
	
	public void setAnswerMapped(String key, String value) {
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().getIdStr().equals(key)) {
				answer.setValue(value);
				return;
			}
		}
		
		throw new IllegalStateException("No answer found with key: " + key);
	}

	public IntakeAnswer getRepeatingAnswerMapped(String key, int index) {
		return getRepeatingAnswerMapped(key,index,true);
	}
	
	public IntakeAnswer getRepeatingAnswerMapped(String key, int index, boolean create) {
		IntakeAnswer temp=null;
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getIndex()==0 && (answer.getNode().getIdStr().equals(key) || String.valueOf(answer.getNode().getEq_to_id()).equals(key)) ) {
				temp=answer;
			}
			if (answer.getIndex()==index && (answer.getNode().getIdStr().equals(key) || String.valueOf(answer.getNode().getEq_to_id()).equals(key)) ) {
				return answer;
			}
		}
		//we should add it
		if(create && temp!=null) {
			IntakeAnswer ia = IntakeAnswer.create(temp.getNode(),index);		
			addToanswers(ia);
			return ia;
		}
		return null;
	}

	public void setRepeatingAnswerMapped(String key, String value, int index) {
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().getIdStr().equals(key) && answer.getIndex()==index) {
				answer.setValue(value);
				return;
			}
		}
		
		throw new IllegalStateException("No answer found with key: " + key);
	}

	public String getClientName() {
		return client != null ? client.getFormattedName() : null;
	}
	
	public void setClient(Demographic client) {
		this.client = client;
	}
	
	public String getStaffName() {
		return staff != null ? staff.getFormattedName() : null;
	}

	public void setStaff(Provider staff) {
		this.staff = staff;
	}
	
	public Integer getProgramId() {
		return programId;
	}
	
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}	
	
	public String getIntakeStatus() {
		return intakeStatus;
	}

	public void setIntakeStatus(String intakeStatus) {
		this.intakeStatus = intakeStatus;
	}

	
	public Integer getIntakeLocation() {
		return intakeLocation;
	}

	public void setIntakeLocation(Integer intakeLocation) {
		this.intakeLocation = intakeLocation;
	}
	
	
	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getCreatedOnStr() {
		return DATE_FORMAT.format(getCreatedOn().getTime());
	}
	
	public String getType() {
		String type = PROGRAM;
		/*
		if (getNode().getId().equals(1)) {
			type = QUICK;
		} else if (getNode().getId().equals(2)) {
			type = INDEPTH;
		}
		*/
		if(node.getFormType() == 1) {
			type = QUICK;
		}
		else type = INDEPTH;
		return type;
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getNode()).append(")").toString();
	}

    /**
	 * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_id"
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
	 * Return the value associated with the column: client_id
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
	 * Set the value related to the column: client_id
     *
     * @param clientId
     *            the client_id value
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
	 * Return the value associated with the column: staff_id
     */
    public String getStaffId() {
        return staffId;
    }

    /**
	 * Set the value related to the column: staff_id
     *
     * @param staffId
     *            the staff_id value
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
	 * Return the value associated with the column: creation_date
     */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
	 * Set the value related to the column: creation_date
     *
     * @param createdOn
     *            the creation_date value
     */
    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
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

    /**
	 * Return the value associated with the column: answers
     */
    public java.util.Set<IntakeAnswer> getAnswers() {
        return answers;
    }

    /**
	 * Set the value related to the column: answers
     *
     * @param answers
     *            the answers value
     */
    public void setAnswers(java.util.Set<IntakeAnswer> answers) {
        this.answers = answers;
    }

    public void cleanRepeatingAnswers(int nodeId, int size) {
    	Iterator<IntakeAnswer> i = this.getAnswers().iterator();
    	while(i.hasNext()) {
    		IntakeAnswer answer = i.next();
    		if(answer.getNode().getId() == nodeId && answer.getIndex()>=size) {
    			getAnswers().remove(answer);
    		}
    	}
    }
    
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof Intake))
            return false;
        else {
            Intake intake = (Intake) obj;
            if (null == this.getId() || null == intake.getId())
                return false;
            else
                return (this.getId().equals(intake.getId()));
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
