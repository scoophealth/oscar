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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.caisi.model.BaseObject;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.oscarehr.util.SpringUtils;

import com.quatro.model.security.Secrole;

public class CaseManagementIssue extends BaseObject {

	private ProgramProviderDAO programProviderDao=(ProgramProviderDAO)SpringUtils.getBean("programProviderDAO");
	private ProgramAccessDAO programAccessDao=(ProgramAccessDAO)SpringUtils.getBean("programAccessDAO");
	private RoleProgramAccessDAO roleProgramAccessDAO=(RoleProgramAccessDAO)SpringUtils.getBean("RoleProgramAccessDAO");
	
	protected Long id;
	protected Integer demographic_no;
	protected long issue_id;
	protected boolean acute;
	// protected boolean medical_diagnosis;
	protected boolean certain;
	protected boolean major;
	// protected boolean active;
	protected boolean resolved;
	protected String type;
	protected Date update_date = new Date();
	protected Set notes = new HashSet();
	protected Issue issue;
	protected Integer program_id = null;

	protected int hashCode = Integer.MIN_VALUE;

	public String toString() {
		return "CaseManagementIssue: id=" + id;
	}

	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CaseManagementIssue)) return false;
		else {
			CaseManagementIssue mObj = (CaseManagementIssue) obj;
			if (null == this.getId() || null == mObj.getId()) return false;
			else return (this.getId().equals(mObj.getId()));
		}
	}

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

	public CaseManagementIssue() {
		update_date = new Date();
	}

	/*
	 * Copy constructor performs copy
	 */
	public CaseManagementIssue(CaseManagementIssue cMgmtIssue) {

		this.setId(0L); // so hibernate will think it a non persisted obj
		this.setDemographic_no(cMgmtIssue.getDemographic_no());
		this.setIssue_id(cMgmtIssue.getIssue_id());
		this.setAcute(cMgmtIssue.isAcute());
		this.setCertain(cMgmtIssue.isCertain());
		this.setMajor(cMgmtIssue.isMajor());
		this.setResolved(cMgmtIssue.isResolved());
		this.setType(cMgmtIssue.getType());
		this.setUpdate_date(cMgmtIssue.getUpdate_date());
		this.setNotes(cMgmtIssue.getNotes());
		this.setIssue(cMgmtIssue.getIssue());
	}

	public boolean isAcute() {
		return acute;
	}

	public void setAcute(boolean acute) {
		this.acute = acute;
	}

	public boolean isCertain() {
		return certain;
	}

	public void setCertain(boolean certain) {
		this.certain = certain;
	}

	public Integer getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(Integer demographic_no) {
		this.demographic_no = demographic_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * deprecated too inefficient and too many dependencies use IssueDao
	 */
	public Issue getIssue() {
		return issue;
	}

	/**
	 * deprecated too inefficient and too many dependencies use IssueDao 
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public long getIssue_id() {
		return issue_id;
	}

	public void setIssue_id(long issue_id) {
		this.issue_id = issue_id;
	}

	public boolean isMajor() {
		return major;
	}

	public void setMajor(boolean major) {
		this.major = major;
	}

	/*
	 * public boolean isMedical_diagnosis() { return medical_diagnosis; } public void setMedical_diagnosis(boolean medical_diagnosis) { this.medical_diagnosis = medical_diagnosis; }
	 */
	/**
	 * deprecated too inefficient and too many dependencies use CaseManagementIssueNotesDao
	 */
	public Set getNotes() {
		return notes;
	}

	/**
	 * deprecated too inefficient and too many dependencies use CaseManagementIssueNotesDao
	 */
	public void setNotes(Set notes) {
		this.notes = notes;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public boolean isWriteAccess(String providerNo, int programId)
	{
		Boolean result = calculateWriteAccess(providerNo, programId);
		return(result);
	}

	private boolean calculateWriteAccess(String providerNo, int programId) {
	    List<ProgramProvider> ppList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, new Long(programId));
	    if (ppList == null || ppList.isEmpty()) {
	    	return(false);
	    }

	    ProgramProvider pp = ppList.get(0);
	    Secrole role = pp.getRole();

	    List<ProgramAccess> programAccessList = programAccessDao.getAccessListByProgramId(new Long(programId));
	    Map<String, ProgramAccess> programAccessMap = convertProgramAccessListToMap(programAccessList);
	    
	    String issueRole = getIssue().getRole().toLowerCase();
	    ProgramAccess pa = null;

	    String accessName="write " + issueRole + " issues";
	    pa = programAccessMap.get(accessName);
	    if (pa != null) {
	    	if (pa.isAllRoles() || isRoleIncludedInAccess(pa, role)) {
	    		return(true);
	    	}
	    } else {
	    	if (issueRole.equalsIgnoreCase(role.getRoleName())) {
	    		return(true);
	    	}
	    }
	    
	    //global default role access
		if(roleProgramAccessDAO.hasAccess(accessName,role.getId())) {
				return(true);
		}
	    return(false);
    }
	
	private static boolean isRoleIncludedInAccess(ProgramAccess pa, Secrole role) {
		boolean result = false;

		for (Secrole accessRole : pa.getRoles()) {
			if (role.getId().longValue() == accessRole.getId().longValue()) {
				return true;
			}
		}

		return result;
	}

	private static Map<String, ProgramAccess> convertProgramAccessListToMap(List<ProgramAccess> programAccessList) {
		Map<String, ProgramAccess> map = new HashMap<String, ProgramAccess>();

		for (Iterator<ProgramAccess> iter = programAccessList.iterator(); iter.hasNext();) {
			ProgramAccess pa = iter.next();
			map.put(pa.getAccessType().getName().toLowerCase(), pa);
		}
		return map;
	}
}
