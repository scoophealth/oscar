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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.oscarehr.casemgmt.model.Issue;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.security.Secrole;

public class IssueDAO extends HibernateDaoSupport {
	private Logger log = Logger.getLogger(this.getClass());
	
    public Issue getIssue(Long id) {
        return (Issue)this.getHibernateTemplate().get(Issue.class, id);
    }

    public List getIssues() {
        return this.getHibernateTemplate().find("from Issue");
    }

    public List<Issue> findIssueByCode(String[] codes) {
        String code = "'" + StringUtils.join(codes,"','") + "'";
        return this.getHibernateTemplate().find("from Issue i where i.code in (" + code + ")");
    }
    
    public Issue findIssueByCode(String code) {        
        List<Issue>list = this.getHibernateTemplate().find("from Issue i where i.code = ?", new Object[] {code});
        if( list.size() > 0 )
            return list.get(0);
        
        return null;
    }

    public void saveIssue(Issue issue) {
        this.getHibernateTemplate().saveOrUpdate(issue);
    }

    public List findIssueBySearch(String search) {
        search = "%" + search + "%";
        search = search.toLowerCase();
        String sql = "from Issue i where lower(i.code) like ? or lower(i.description) like ?";
        return this.getHibernateTemplate().find(sql, new Object[] {search, search});
    }

    public List search(String search, List roles) {
        if (roles.size() == 0) {
            return new ArrayList();
        }

        StringBuffer buf = new StringBuffer();
        for (int x = 0; x < roles.size(); x++) {
            if (x != 0) {
                buf.append(",");
            }
            buf.append("\'" + StringEscapeUtils.escapeSql(((Secrole)roles.get(x)).getName()) + "\'");
        }
        String roleList = buf.toString();

        search = "%" + search + "%";
        search = search.toLowerCase();
        String sql = "from Issue i where (lower(i.code) like ? or lower(i.description) like ?) and i.role in (" + roleList + ")";
        log.info(sql);
        return this.getHibernateTemplate().find(sql, new Object[] {search, search});

    }

    public List searchNoRolesConcerned(String search) {
        search = "%" + search + "%";
        search = search.toLowerCase();
        String sql = "from Issue i where (lower(i.code) like ? or lower(i.description) like ?)";
        log.info(sql);
        return this.getHibernateTemplate().find(sql, new Object[] {search, search});
    }
    
    /**
     * Retrieves a list of Issue codes that have a type matching what is configured in oscar_mcmaster.properties as COMMUNITY_ISSUE_CODETYPE,
     * or an empty list if this property is not found.
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getLocalCodesByCommunityType(String type)
    {
    	List<String> codes;
    	if(type == null || type.equals(""))
    	{
    		codes = new ArrayList<String>();
    	}
    	else
    	{
    		codes = (List<String>)this.getHibernateTemplate().find("FROM Issue i WHERE i.type = ?", new Object[] {type.toLowerCase()});
    	}
    	return codes;
    }
}
