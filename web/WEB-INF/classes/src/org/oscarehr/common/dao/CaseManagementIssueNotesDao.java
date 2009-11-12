/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.util.Collection;
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

    public void setNoteIssues(Integer noteId, Collection<CaseManagementIssue> issues)
    {
    	// delete old
    	Query query=entityManager.createNativeQuery("delete from casemgmt_issue_notes where note_id=?1");
    	query.setParameter(1, noteId);
    	query.executeUpdate();
    	
    	// add new / current list
    	for (CaseManagementIssue issue : issues)
    	{
        	query=entityManager.createNativeQuery("insert into casemgmt_issue_notes values (?1,?2)");
        	query.setParameter(1, issue.getId());
        	query.setParameter(2, noteId);
        	query.executeUpdate();
    	}
    }
    
    public List<CaseManagementIssue> getNoteIssues(Integer noteId)
    {
    	Query query=entityManager.createNativeQuery("select casemgmt_issue.* from casemgmt_issue_notes, casemgmt_issue where note_id=?1 and casemgmt_issue_notes.id=casemgmt_issue.id", CaseManagementIssue.class);
    	query.setParameter(1, noteId);
    	
        @SuppressWarnings("unchecked")
    	List<CaseManagementIssue> results=query.getResultList();
        return(results);
    }
}
