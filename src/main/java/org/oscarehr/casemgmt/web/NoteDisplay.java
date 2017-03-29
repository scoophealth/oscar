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

package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.oscarehr.casemgmt.model.CaseManagementNoteLink;

import oscar.oscarRx.data.RxPrescriptionData;

public interface NoteDisplay {
	public static Comparator<NoteDisplay> noteProviderComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}

			return note1.getProviderName().compareTo(note2.getProviderName());
		}
	};

	public static Comparator<NoteDisplay> noteProgramComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note1.getProgramName() == null || note2 == null || note2.getProgramName() == null) {
				return 0;
			}
			return note1.getProgramName().compareTo(note2.getProgramName());
		}
	};

	public static Comparator<NoteDisplay> noteRoleComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}
			return note1.getRoleName().compareTo(note2.getRoleName());
		}
	};

	public static Comparator<NoteDisplay> noteObservationDateComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}
			
			if( note2.getObservationDate() == null || note1.getObservationDate() == null ) {
				return 0;
			}
			
			return note2.getObservationDate().compareTo(note1.getObservationDate());
		}
	};

	public Integer getNoteId();

	public boolean isSigned();

	public boolean isEditable();

	public Date getObservationDate();

	public String getRevision();

	public Date getUpdateDate();

	public String getProviderName();

	public String getProviderNo();

	public String getStatus();

	public String getProgramName();

	public String getLocation();

	public String getRoleName();

	public Integer getRemoteFacilityId();

	public String getUuid();

	public boolean getHasHistory();

	public boolean isLocked();

	public String getNote();

	public boolean isDocument();

	public boolean isRxAnnotation();

	public boolean isEformData();
	
	public boolean isFreeDraw();

	public boolean isEncounterForm();

	public boolean isInvoice();

	public boolean isTicklerNote();
	
	public CaseManagementNoteLink getNoteLink();

	public RxPrescriptionData.Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl);

	public String getEncounterType();

	public ArrayList<String> getEditorNames();

	public ArrayList<String> getIssueDescriptions();

	//not controlled by note attributes / business logic like "editable".
	//use this for a category of notes - like integrator, group notes, etc
	public boolean isReadOnly();

	public boolean isGroupNote();

	public boolean isCpp();

	public boolean containsIssue(String issueCode);

	public String getEncounterTime();

	public String getEncounterTransportationTime();
	
	public Integer getAppointmentNo();
}
