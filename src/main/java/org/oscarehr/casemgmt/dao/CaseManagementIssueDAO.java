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

package org.oscarehr.casemgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.caisi_integrator.ws.CodeType;
import org.oscarehr.caisi_integrator.ws.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementIssueDAO extends HibernateDaoSupport {

	private static Logger log = MiscUtils.getLogger();
	
    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByDemographic(String demographic_no) {
        return this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ?", new Object[] {Integer.valueOf(demographic_no)});
    }

    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByDemographicOrderActive(Integer demographic_no, Boolean resolved) {
        return getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ? "+(resolved!=null?" and cmi.resolved="+resolved:"")+" order by cmi.resolved", new Object[] {demographic_no});
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByNote(Integer noteId, Boolean resolved) {
        return getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.notes.id = ? "+(resolved!=null?" and cmi.resolved="+resolved:"")+" order by cmi.resolved", new Object[] {noteId});
    }
    
    @SuppressWarnings("unchecked")
    public Issue getIssueByCmnId(Integer cmnIssueId) {
        List<Issue> result = getHibernateTemplate().find("select issue from CaseManagementIssue cmi where cmi.id = ?", new Object[] {Long.valueOf(cmnIssueId)});
        if(result.size()>0)
        	return result.get(0);
        return null;
    }

    public CaseManagementIssue getIssuebyId(String demo, String id) {
        @SuppressWarnings("unchecked")
        List<CaseManagementIssue> list = this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.issue_id = ? and demographic_no = ?",new Object[]{Long.parseLong(id),Integer.valueOf(demo)});
        if( list != null && list.size() == 1 )
            return list.get(0);
        
        return null;                    
    }

    public CaseManagementIssue getIssuebyIssueCode(String demo, String issueCode) {
    	@SuppressWarnings("unchecked")
        List<CaseManagementIssue> list = this.getHibernateTemplate().find("select cmi from CaseManagementIssue cmi, Issue issue where cmi.issue_id=issue.id and issue.code = ? and cmi.demographic_no = ?",new Object[]{issueCode,Integer.valueOf(demo)});
        
        if(list.size()>1){ 
        log.error("Expected 1 result got more : "+list.size() + "(" + demo + "," + issueCode + ")");
        }
        
        if (list.size() == 1 || list.size()>1) return list.get(0);

        return null;
    }

    public void deleteIssueById(CaseManagementIssue issue) {
        getHibernateTemplate().delete(issue);
        return;

    }

    public void saveAndUpdateCaseIssues(List<CaseManagementIssue> issuelist) {
        Iterator<CaseManagementIssue> itr = issuelist.iterator();
        while (itr.hasNext()) {
        	CaseManagementIssue cmi = itr.next();
        	cmi.setUpdate_date(new Date());
        	if(cmi.getId()!=null && cmi.getId().longValue()>0) {
        		getHibernateTemplate().update(cmi);
        	} else {
        		getHibernateTemplate().save(cmi);
        	}            
        }

    }

    public void saveIssue(CaseManagementIssue issue) {
    	issue.setUpdate_date(new Date());
        getHibernateTemplate().saveOrUpdate(issue);
    }
    
    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getAllCertainIssues() {
        return getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.certain = true");
    }

    //for integrator
    @SuppressWarnings("unchecked")
    public List<Integer> getIssuesByProgramsSince(Date date, List<Program> programs) {
    	StringBuilder sb = new StringBuilder();
    	int i=0;
    	for(Program p:programs) {
    		if(i++ > 0)
    			sb.append(",");
    		sb.append(p.getId());
    	}
        return this.getHibernateTemplate().find("select cmi.demographic_no from CaseManagementIssue cmi where cmi.update_date > ? and program_id in ("+sb.toString()+")", new Object[] {date});
    }

    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByDemographicSince(String demographic_no,Date date) {
        return this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ? and cmi.update_date > ?", new Object[] {Integer.valueOf(demographic_no),date});
    }
    
    @SuppressWarnings("unchecked")
    public List<FacilityIdDemographicIssueCompositePk> getIssueIdsForIntegrator(Integer facilityId, Integer demographicNo) {
        List<Object[]> rs =  this.getHibernateTemplate().find("select i.code,i.type from CaseManagementIssue cmi, Issue i where cmi.issue_id = i.id and cmi.demographic_no = ?", new Object[] {demographicNo});
        List<FacilityIdDemographicIssueCompositePk> results = new ArrayList<FacilityIdDemographicIssueCompositePk>();
        for(Object[] item:rs) {
        	FacilityIdDemographicIssueCompositePk key = new FacilityIdDemographicIssueCompositePk();
        	key.setIntegratorFacilityId(facilityId);
        	key.setCaisiDemographicId(demographicNo);
        	key.setIssueCode((String)item[0]);
        	
        	if("icd9".equals(item[1])) {
				key.setCodeType(CodeType.ICD_9);
			}
			else if("icd10".equals(item[1])) {
				key.setCodeType(CodeType.ICD_10);
			} else {
				key.setCodeType(CodeType.CUSTOM_ISSUE);
			}
        	results.add(key);
        }
        return results;
    }

}
