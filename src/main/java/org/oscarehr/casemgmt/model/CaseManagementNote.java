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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.caisi.model.BaseObject;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;


public class CaseManagementNote extends BaseObject {

	private Long id;
	private Date update_date;
	private Date create_date;
	private Date observation_date;
	private String demographic_no;
	private String note;
	private boolean signed = false;
	private boolean includeissue = true;
	private String providerNo;
	private String signing_provider_no;
	private String encounter_type = "";
	private String billing_code = "";
	private String program_no;
	private String reporter_caisi_role;
	private String reporter_program_team;
	private String history;
	private Provider provider;
	private Set<CaseManagementIssue> issues = new HashSet<CaseManagementIssue>();
	private Set<Object> extend = new HashSet<Object>();
	private List<Provider> editors = new ArrayList<Provider>();
	private String roleName;
	private String programName;
	private String uuid;
	private String revision;

	private String password;
	private boolean locked;
	private boolean archived;

	private boolean remote = false;
	private String facilityName = "None Specified";

	private int hashCode = Integer.MIN_VALUE;
	private int position = 0;

	private int appointmentNo;
	private Integer hourOfEncounterTime;
	private Integer minuteOfEncounterTime;
	private Integer hourOfEncTransportationTime;
	private Integer minuteOfEncTransportationTime;

	CaseManagementNoteLinkDAO caseManagementNoteLinkDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");

	private CaseManagementNoteLink cmnLink = null;
	private boolean cmnLinkRetrieved = false;

	public Map<String, Object> getMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("id", id);
		map.put("update_date", update_date);
		map.put("create_date", create_date);
		map.put("observation_date", observation_date);
		map.put("demographic_no", demographic_no);
		map.put("note", note);
		map.put("signed", signed);
		map.put("includeissue", includeissue);
		map.put("provider_no", providerNo);
		map.put("signing_provider_no", signing_provider_no);
		map.put("encounter_type", encounter_type);
		map.put("billing_code", billing_code);
		map.put("program_no", program_no);
		map.put("reporter_caisi_role", reporter_caisi_role);
		map.put("reporter_caisi_team", reporter_program_team);
		map.put("history", history);
		map.put("provider", provider);
		map.put("editors", editors);
		map.put("role_name", roleName);
		map.put("program_name", programName);
		map.put("uuid", uuid);
		map.put("revision", revision);
		map.put("locked", locked);
		map.put("archived", archived);
		map.put("remote", remote);
		map.put("facility_name", facilityName);
		map.put("appointment_no", appointmentNo);

		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CaseManagementNote)) return false;
		else {
			CaseManagementNote mObj = (CaseManagementNote) obj;
			if (null == this.getId() || null == mObj.getId()) return false;
			else return (this.getId().equals(mObj.getId()));
		}
	}

	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public CaseManagementNote() {
		update_date = new Date();
	}

	public String getAuditString() {
		StringBuilder auditStr = new StringBuilder(getNote());
		Iterator<CaseManagementIssue> iter = issues.iterator();
		auditStr.append("\nIssues\n");
		int idx = 0;
		while (iter.hasNext()) {
			auditStr.append(iter.next().getIssue().getDescription() + "\n");
			++idx;
		}
		if (idx == 0) {
			auditStr.append("None");
		}
		return auditStr.toString();
	}

	public String getBilling_code() {
		return billing_code;
	}

	public void setBilling_code(String billing_code) {
		this.billing_code = billing_code;
	}

	public String getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}

	public String getEncounter_type() {
		return encounter_type;
	}

	public void setEncounter_type(String encounter_type) {
		this.encounter_type = encounter_type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public String getSigning_provider_no() {
		return signing_provider_no;
	}

	public void setSigning_provider_no(String signing_provider_no) {
		this.signing_provider_no = signing_provider_no;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getObservation_date() {
		return this.observation_date;
	}

	public void setObservation_date(Date observation_date) {
		this.observation_date = observation_date;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String provider_no) {
		this.providerNo = provider_no;
	}

	// nys
	public String getProgram_no() {
		return program_no;
	}

	public void setProgram_no(String program_no) {
		this.program_no = program_no;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	/**
	 * deprecated too inefficient and too many dependencies use CaseManagementIssueNotesDao
	 */
	public Set<CaseManagementIssue> getIssues() {
		return issues;
	}

	/**
	 * deprecated too inefficient and too many dependencies use CaseManagementIssueNotesDao
	 */
	public void setIssues(Set issues) {
		this.issues = issues;
	}

	public Set getExtend() {
		return extend;
	}

	public void setExtend(Set extend) {
		this.extend = extend;
	}

	public List<Provider> getEditors() {
		return editors;
	}

	public void setEditors(List editors) {
		this.editors = editors;
	}

	public boolean isIncludeissue() {
		return includeissue;
	}

	public void setIncludeissue(boolean includeissue) {
		this.includeissue = includeissue;
	}

	public String getReporter_caisi_role() {
		return reporter_caisi_role;
	}

	public void setReporter_caisi_role(String reporter_caisi_role) {
		this.reporter_caisi_role = reporter_caisi_role;
	}

	public String getReporter_program_team() {
		return reporter_program_team;
	}

	public void setReporter_program_team(String reporter_program_team) {
		this.reporter_program_team = reporter_program_team;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getProgramName() {
		return programName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setRevision(String rev) {
		this.revision = rev;
	}

	public String getRevision() {
		return this.revision;
	}

	public String getProviderName() {
		if (getProvider() == null) {
			return "DELETED";
		}
		return getProvider().getFormattedName();
	}

	public String getProviderNameFirstLast() {
		if (getProvider() == null) {
			return "DELETED";
		}
		return getProvider().getFullName();
	}

	public static Comparator<CaseManagementNote> getProviderComparator() {
		return new Comparator<CaseManagementNote>() {
			public int compare(CaseManagementNote note1, CaseManagementNote note2) {
				if (note1 == null || note2 == null) {
					return 0;
				}

				return note1.getProviderName().compareTo(note2.getProviderName());
			}
		};

	}

	public static Comparator<CaseManagementNote> getProgramComparator() {
		return new Comparator<CaseManagementNote>() {
			public int compare(CaseManagementNote note1, CaseManagementNote note2) {
				if (note1 == null || note1.getProgramName() == null || note2 == null || note2.getProgramName() == null) {
					return 0;
				}
				return note1.getProgramName().compareTo(note2.getProgramName());
			}
		};

	}

	public static Comparator<CaseManagementNote> getRoleComparator() {
		return new Comparator<CaseManagementNote>() {
			public int compare(CaseManagementNote note1, CaseManagementNote note2) {
				if (note1 == null || note2 == null) {
					return 0;
				}
				return note1.getRoleName().compareTo(note2.getRoleName());
			}
		};

	}
	public static Comparator<CaseManagementNote> noteObservationDateComparator = new Comparator<CaseManagementNote>() {

            public int compare(CaseManagementNote note1, CaseManagementNote note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}

			return note2.getObservation_date().compareTo(note1.getObservation_date());
		}
	};

	public static Comparator<CaseManagementNote> getPositionComparator() {
		return new Comparator<CaseManagementNote>() {
			public int compare(CaseManagementNote note1, CaseManagementNote note2) {
				if (note1 == null || note2 == null) {
					return 0;
				}

				return new Integer(note1.getPosition()).compareTo(new Integer(note2.getPosition()));
			}
		};

	}
	
	public boolean getHasHistory() {
		if (getHistory() != null) {
			if (getHistory().indexOf("----------------History Record----------------") != -1) {
				return true;
			}
		}
		return false;
	}

	public boolean isLocked() {
		return locked;
	}

	public String getPassword() {
		return password;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		String status = "";
		if (isSigned()) {
			status = "Signed";
		} else {
			status = "Unsigned";
		}

		if (getPassword() != null && getPassword().length() > 0) {
			// locked note - can be temporarily unlocked
			if (locked) {
				status += "/Locked";
			} else {
				status += "/Unlocked";
			}
		}
		return status;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean isRemote) {
		this.remote = isRemote;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public boolean isDocumentNote() {
		return isLinkTo(CaseManagementNoteLink.DOCUMENT);
	}


    public boolean isRxAnnotation() {
		return isLinkTo(CaseManagementNoteLink.DRUGS);
	}

	public boolean isEformData() {
		return isLinkTo(CaseManagementNoteLink.EFORMDATA);
	}

	private boolean isLinkTo(Integer tableName) {
		if (!cmnLinkRetrieved) {
			cmnLink = caseManagementNoteLinkDao.getLastLinkByNote(this.id);
			cmnLinkRetrieved = true;
		}

		if (cmnLink!=null && cmnLink.getTableName().equals(tableName)) {
			return true;
		}
		return false;
	}

	public RxPrescriptionData.Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl){
        if(this.isRxAnnotation()){
            String drugId=cmnl.getTableId().toString();

            //get drug id from cmn_link table
            RxPrescriptionData rxData = new RxPrescriptionData();
            // create Prescription
            RxPrescriptionData.Prescription rx = rxData.getLatestPrescriptionScriptByPatientDrugId(Integer.parseInt(this.getDemographic_no()), drugId);
            return rx;
        }

        return null;
    }



	public int getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public Integer getHourOfEncounterTime() {
    	return hourOfEncounterTime;
    }

	public void setHourOfEncounterTime(Integer hourOfEncounterTime) {
    	this.hourOfEncounterTime = hourOfEncounterTime;
    }

	public Integer getMinuteOfEncounterTime() {
    	return minuteOfEncounterTime;
    }

	public void setMinuteOfEncounterTime(Integer minuteOfEncounterTime) {
    	this.minuteOfEncounterTime = minuteOfEncounterTime;
    }

	public Integer getHourOfEncTransportationTime() {
    	return hourOfEncTransportationTime;
    }

	public void setHourOfEncTransportationTime(Integer hourOfEncTransportationTime) {
    	this.hourOfEncTransportationTime = hourOfEncTransportationTime;
    }

	public Integer getMinuteOfEncTransportationTime() {
    	return minuteOfEncTransportationTime;
    }

	public void setMinuteOfEncTransportationTime(Integer minuteOfEncTransportationTime) {
    	this.minuteOfEncTransportationTime = minuteOfEncTransportationTime;
    }


}
