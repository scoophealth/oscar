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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import org.oscarehr.PMmodule.model.base.BaseIntake;

public class Intake extends BaseIntake {

	private static final long serialVersionUID = 1L;
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm");

	public static Intake create(IntakeNode node, Integer clientId, Integer programId, String staffId) {
		Intake intake = new Intake();
		intake.setNode(node);
		intake.setClientId(clientId);
		intake.setProgramId(programId);
		intake.setStaffId(staffId);
		intake.setCreatedOn(Calendar.getInstance());

		return intake;
	}
	
	private Demographic client;
	private Provider staff;
	private Integer programId;

	/* [CONSTRUCTOR MARKER BEGIN] */

	public Intake() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Intake(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Intake(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.Integer clientId, java.lang.String staffId, java.util.Calendar createdOn) {
		super(id, node, clientId, staffId, createdOn);
	}

	/* [CONSTRUCTOR MARKER END] */
	
	@Override
	protected void initialize() {
		setAnswers(new TreeSet<IntakeAnswer>());
	}
	
	@Override
	public void addToanswers(IntakeAnswer answer) {
		answer.setIntake(this);
		super.addToanswers(answer);
	}
	
	public IntakeAnswer getAnswerMapped(String key) {
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().getIdStr().equals(key)) {
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
	
	public List<String> getBooleanAnswerNodeIds() {
		List<String> ids = new ArrayList<String>();
		
		for (IntakeAnswer answer : getAnswers()) {
			if (answer.getNode().isAnswerBoolean()) {
				ids.add(answer.getNode().getIdStr());
			}
		}		
		
		return ids;
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
	
	public String getCreatedOnStr() {
		return DATE_FORMAT.format(getCreatedOn().getTime());
	}

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getNode()).append(")").toString();
	}

}