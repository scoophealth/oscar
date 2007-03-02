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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.oscarehr.PMmodule.model.base.BaseIntake;

public class Intake extends BaseIntake {

	private static final long serialVersionUID = 1L;

	public static Intake create(IntakeNode node, Integer clientId, String staffId) {
		Intake intake = new Intake();
		intake.setNode(node);
		intake.setClientId(clientId);
		intake.setStaffId(staffId);
		intake.setCreatedOn(Calendar.getInstance());

		return intake;
	}

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

	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getNode()).append(")").toString();
	}

}