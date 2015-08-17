/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest.to.model;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="noteIssue")
public class NoteIssueTo1 {

	private NoteTo1 encounterNote = new NoteTo1();
	private NoteExtTo1 groupNoteExt = new NoteExtTo1();
	private IssueTo1 issue = new IssueTo1();
	private String annotation_attrib;
	
	private List<IssueTo1> assignedIssues = new ArrayList<IssueTo1>();

	private List<CaseManagementIssueTo1> assignedCMIssues = new ArrayList<CaseManagementIssueTo1>();

	
	public NoteTo1 getEncounterNote() {
	    return encounterNote;
    }

	public void setEncounterNote(NoteTo1 encounterNote) {
	    this.encounterNote = encounterNote;
    }
	
	public NoteExtTo1 getGroupNoteExt() {
	    return groupNoteExt;
    }

	public void setGroupNoteExt(NoteExtTo1 groupNoteExt) {
	    this.groupNoteExt = groupNoteExt;
    }

	public IssueTo1 getIssue() {
	    return issue;
    }

	public void setIssue(IssueTo1 issue) {
	    this.issue = issue;
    }

	public String getAnnotation_attrib() {
	    return annotation_attrib;
    }

	public void setAnnotation_attrib(String annotation_attrib) {
	    this.annotation_attrib = annotation_attrib;
    }

	public List<IssueTo1> getAssignedIssues() {
		return assignedIssues;
	}

	public void setAssignedIssues(List<IssueTo1> assignedIssues) {
		this.assignedIssues = assignedIssues;
	}

	public List<CaseManagementIssueTo1> getAssignedCMIssues() {
		return assignedCMIssues;
	}

	public void setAssignedCMIssues(List<CaseManagementIssueTo1> assignedCMIssues) {
		this.assignedCMIssues = assignedCMIssues;
	}


}