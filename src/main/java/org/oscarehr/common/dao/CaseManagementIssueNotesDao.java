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
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CaseManagementIssueNotesDao {
    
    @PersistenceContext
    protected EntityManager entityManager = null;
    
    public List<CaseManagementIssue> getNoteIssues(Integer noteId)
    {
    	Query query=entityManager.createNativeQuery("select casemgmt_issue.* from casemgmt_issue_notes, casemgmt_issue where note_id=?1 and casemgmt_issue_notes.id=casemgmt_issue.id", CaseManagementIssue.class);
    	query.setParameter(1, noteId);
    	
        @SuppressWarnings("unchecked")
    	List<CaseManagementIssue> results=query.getResultList();
        return(results);
    }
    
    public List<Integer> getNoteIdsWhichHaveIssues(String[] issueId)
    {
    	if(issueId == null || issueId.length==0)
    		return null;
    	if(issueId.length==1 && issueId[0].equals(""))
    		return null;
    	
    	StringBuilder issueIdList = new StringBuilder();
    	for(String i:issueId) {
    		if(issueIdList.length()>0)
    			issueIdList.append(",");
    		issueIdList.append(i);
    	}
    	String sql = "select note_id  from casemgmt_issue_notes where id in ("+issueIdList.toString() + ")";
    	
    	Query query=entityManager.createNativeQuery(sql);
    	
        @SuppressWarnings("unchecked")
    	List<Integer> results=query.getResultList();
        return(results);
    }
    
}
