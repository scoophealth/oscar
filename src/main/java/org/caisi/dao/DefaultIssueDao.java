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

package org.caisi.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.caisi.model.DefaultIssue;
import org.oscarehr.common.dao.AbstractDao;

public class DefaultIssueDao extends AbstractDao<DefaultIssue> {

	public DefaultIssueDao(){
		super(DefaultIssue.class);
	}
	
	public DefaultIssue findDefaultIssue(Integer id) {
		Query query = entityManager.createQuery("select x from DefaultIssue x where x.id = ?1");
		query.setParameter(1, id);
		return getSingleResultOrNull(query);
	}
	
	@SuppressWarnings("unchecked")
	public DefaultIssue getLastestDefaultIssue() {
		Query query = entityManager.createQuery("select x from DefaultIssue x order by x.assignedtime desc");
		query.setMaxResults(1);
		List<DefaultIssue> issueList = query.getResultList();
		if (issueList == null || issueList.size() == 0) {
			return null;
		}
		return issueList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<DefaultIssue> findAll() {
		Query query = entityManager.createQuery("select x from DefaultIssue x order by x.assignedtime desc");
		return query.getResultList();
	}
	
	public void saveDefaultIssue(DefaultIssue issue) {
		if (issue.getId() != null && issue.getId() > 0) {
			merge(issue);
		} else {
			persist(issue);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String[] findAllDefaultIssueIds() {
		Query query = entityManager.createQuery("select x.issueIds from DefaultIssue x order by x.assignedtime");
		query.setMaxResults(1);
		List<String> issueIdsList = query.getResultList();
		if (issueIdsList.size() == 0) {
			return new String[0];
		}
		Set<String> issueIdsSet = new HashSet<String>();
		for (String ids : issueIdsList) {
			String[] idsArr = ids.split(",");
			for (String id : idsArr) {
				if (id.length() > 0) {
					issueIdsSet.add(id);
				}
			}
		}
	
		return issueIdsSet.toArray(new String[issueIdsSet.size()]);
	}
}
