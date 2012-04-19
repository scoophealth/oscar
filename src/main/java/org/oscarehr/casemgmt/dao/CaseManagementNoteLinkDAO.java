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

import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementNoteLinkDAO extends HibernateDaoSupport {

	public CaseManagementNoteLink getNoteLink(Long id) {
		CaseManagementNoteLink noteLink = this.getHibernateTemplate().get(CaseManagementNoteLink.class, id);
		return noteLink;
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteLink> getLinkByTableId(Integer tableName, Long tableId) {
		Object[] param = {tableName, tableId};
		String hql = "from CaseManagementNoteLink cLink where cLink.tableName = ? and cLink.tableId = ? order by cLink.id";
		return this.getHibernateTemplate().find(hql, param);
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteLink> getLinkByTableId(Integer tableName, Long tableId,String otherId) {
		Object[] param = {tableName, tableId, otherId};
		String hql = "from CaseManagementNoteLink cLink where cLink.tableName = ? and cLink.tableId = ? and cLink.otherId=? order by cLink.id";
		return this.getHibernateTemplate().find(hql, param);
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteLink> getLinkByTableIdDesc(Integer tableName, Long tableId) {
		Object[] param = {tableName, tableId};
		String hql = "from CaseManagementNoteLink cLink where cLink.tableName = ? and cLink.tableId = ? order by cLink.id desc";
		return this.getHibernateTemplate().find(hql, param);
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteLink> getLinkByTableIdDesc(Integer tableName, Long tableId,String otherId) {
		Object[] param = {tableName, tableId, otherId};
		String hql = "from CaseManagementNoteLink cLink where cLink.tableName = ? and cLink.tableId = ? and cLink.otherId=? order by cLink.id desc";
		return this.getHibernateTemplate().find(hql, param);
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteLink> getLinkByNote(Long noteId) {
		String hql = "from CaseManagementNoteLink cLink where cLink.noteId = ? order by cLink.id";
		return this.getHibernateTemplate().find(hql, noteId);
	}

	public CaseManagementNoteLink getLastLinkByTableId(Integer tableName, Long tableId,String otherId) {
		return getLast(getLinkByTableId(tableName, tableId,otherId));
	}

	public CaseManagementNoteLink getLastLinkByTableId(Integer tableName, Long tableId) {
		return getLast(getLinkByTableId(tableName, tableId));
	}

	public CaseManagementNoteLink getLastLinkByNote(Long noteId) {
		return getLast(getLinkByNote(noteId));
	}

	private CaseManagementNoteLink getLast(List<CaseManagementNoteLink> listLink) {
		if (listLink.isEmpty()) return null;
		return listLink.get(listLink.size()-1);
	}

	public void save(CaseManagementNoteLink cLink) {
		this.getHibernateTemplate().save(cLink);
	}

	public void update(CaseManagementNoteLink cLink) {
		this.getHibernateTemplate().update(cLink);
	}
}
