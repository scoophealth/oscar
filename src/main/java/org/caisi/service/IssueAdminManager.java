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

package org.caisi.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class IssueAdminManager {
    private static Logger log = MiscUtils.getLogger();
    
    @Autowired
    private IssueDAO dao;


    public List<Issue> getIssueAdmins() {
        return dao.getIssues();
    }

    public Issue getIssueAdmin(String issueAdminId) {
        Issue issueAdmin = dao.getIssue(Long.valueOf(issueAdminId));
        if (issueAdmin == null) {
            log.warn("UserId '" + issueAdminId + "' not found in database.");
        }
        return issueAdmin;
    }

    public Issue saveIssueAdmin(Issue issueAdmin) {
        dao.saveIssue(issueAdmin);
        return issueAdmin;
    }

    @Deprecated
    public void removeIssueAdmin(String issueAdminId) {
        dao.delete(Long.valueOf(issueAdminId));
    }
    
    public void archiveIssues(List<Integer> ids) {
    	for(Integer id:ids) {
    		Issue i = dao.getIssue(new Long(id));
    		i.setArchived(true);
    		dao.saveIssue(i);
    	}
    }
}
