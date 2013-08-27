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


package org.oscarehr.casemgmt.model;


public class CaseManagementNoteLink {
	
	// Table Name constants
	public static final Integer CASEMGMTNOTE = 1;
	public static final Integer DRUGS = 2;
	public static final Integer ALLERGIES = 3;
	public static final Integer LABTEST = 4; //represents the hl7TextMessage table
	public static final Integer DOCUMENT = 5;
	public static final Integer EFORMDATA = 6;
	public static final Integer DEMOGRAPHIC = 7;
	public static final Integer PREVENTIONS = 8;
	public static final Integer LABTEST2 = 9; //repesents the labPatientPhysicianInfo table
	public static final Integer TICKLER = 10;
	
	public static final String DISP_PRESCRIP = "Prescriptions";
	public static final String DISP_ALLERGY = "Allergies";
	public static final String DISP_LABTEST = "Lab Reports";
	public static final String DISP_LABTEST2 = "Lab Reports_";
	public static final String DISP_DOCUMENT = "Documents";
	public static final String DISP_DEMO = "Patient Record";
	public static final String DISP_PREV = "Preventions";
	public static final String DISP_TICKLER = "Ticklers";
	
	private Long id;
	private Integer tableName;
	private Long tableId;
	private Long noteId;
	private String otherId;

	public CaseManagementNoteLink() {}
	public CaseManagementNoteLink(Integer tableName, Long tableId, Long noteId) {
		this.tableName = tableName;
		this.tableId = tableId;
		this.noteId = noteId;
	}


	public Long getId() {
	    return this.id;
	}
	public void setId(Long id) {
	    this.id = id;
	}
	
	public Integer getTableName() {
	    return this.tableName;
	}
	public void setTableName(Integer tableName) {
	    this.tableName = tableName;
	}
	
	public Long getTableId() {
	    return this.tableId;
	}
	public void setTableId(Long tableId) {
	    this.tableId = tableId;
	}
	
	public Long getNoteId() {
	    return this.noteId;
	}
	public void setNoteId(Long noteId) {
	    this.noteId = noteId;
	}
	public void setNoteId(Integer noteId) {
	    setNoteId(noteId.longValue());
	}
	public String getOtherId() {
    	return otherId;
    }
	public void setOtherId(String otherId) {
    	this.otherId = otherId;
    }
	
	
}
