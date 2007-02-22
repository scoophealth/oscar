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

import java.util.Calendar;

import org.oscarehr.PMmodule.model.base.BaseIntakeInstance;

public class IntakeInstance extends BaseIntakeInstance {

	private static final long serialVersionUID = 1L;

	public static IntakeInstance create(IntakeNode node, Integer clientId, String staffId) {
		IntakeInstance intakeInstance = new IntakeInstance();
		intakeInstance.setNode(node);
		intakeInstance.setClientId(clientId);
		intakeInstance.setStaffId(staffId);
		intakeInstance.setCreatedOn(Calendar.getInstance());

		return intakeInstance;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public IntakeInstance () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeInstance (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeInstance(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNode node, java.lang.Integer clientId, java.lang.String staffId, java.util.Calendar createdOn) {
		super(id, node, clientId, staffId, createdOn);
	}

	/* [CONSTRUCTOR MARKER END] */
	
	public IntakeAnswer getAnswerMapped(String key) {
	    for (IntakeAnswer answer : getAnswers()) {
	        if (answer.getNode().getIdStr().equals(key)) {
	        	return answer;
	        }
        }
	    
	    throw new IllegalStateException("No answer found with node id: " + key);
    }
	
	public void setAnswerMapped(String key, String value) {
	    for (IntakeAnswer answer : getAnswers()) {
	        if (answer.getNode().getIdStr().equals(key)) {
	        	answer.setValue(value);
	        	return;
	        }
        }
	    
	    throw new IllegalStateException("No answer found with node id: " + key);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		toString(builder, 0, getNode());

		return builder.toString();
	}

	private void toString(StringBuilder builder, int level, IntakeNode parent) {
		level += 1;

		builder.append(indent(level)).append(parent).append("\n");

		for (IntakeNode child : parent.getChildren()) {
			toString(builder, level, child);
		}
	}

	private StringBuilder indent(int level) {
		StringBuilder builder = new StringBuilder();

		for (int i = 1; i < level; i++) {
			builder.append("\t");
		}

		return builder;
	}

}