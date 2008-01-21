/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.dao;

import java.util.Iterator;
import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementIssueDAO extends HibernateDaoSupport {

    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByDemographic(String demographic_no) {
        return this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ?", new Object[] {demographic_no});
    }

    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getIssuesByDemographicOrderActive(String demographic_no) {
        return this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ? order by cmi.resolved", new Object[] {demographic_no});

    }

    @SuppressWarnings("unchecked")
    public List<CaseManagementIssue> getActiveIssuesByDemographic(String demographic_no) {
        return this.getHibernateTemplate().find("from CaseManagementIssue cmi where cmi.demographic_no = ? and cmi.resolved=false", new Object[] {demographic_no});
    }

    public void deleteIssueById(CaseManagementIssue issue) {
        getHibernateTemplate().delete(issue);
        return;

    }

    @SuppressWarnings("unchecked")
    public void saveAndUpdateCaseIssues(List<CaseManagementIssue> issuelist) {
        Iterator<CaseManagementIssue> itr = issuelist.iterator();
        while (itr.hasNext()) {
            getHibernateTemplate().saveOrUpdate(itr.next());
        }

    }

    public void saveIssue(CaseManagementIssue issue) {
        getHibernateTemplate().saveOrUpdate(issue);
    }

}
