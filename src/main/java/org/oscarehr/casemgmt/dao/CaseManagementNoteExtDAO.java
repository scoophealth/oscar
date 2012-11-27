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

import java.util.Date;
import java.util.List;

import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CaseManagementNoteExtDAO extends HibernateDaoSupport {

	public CaseManagementNoteExt getNoteExt(Long id) {
		CaseManagementNoteExt noteExt = this.getHibernateTemplate().get(CaseManagementNoteExt.class, id);
		return noteExt;
	}

	@SuppressWarnings("unchecked")
    public List<CaseManagementNoteExt> getExtByNote(Long noteId) {
	    String hql = "from CaseManagementNoteExt cExt where cExt.noteId = ? order by cExt.id desc";
	    return this.getHibernateTemplate().find(hql, noteId);
	}

	public List getExtByKeyVal(String keyVal) {
	    String hql = "from CaseManagementNoteExt cExt where cExt.keyVal = ?";
	    return this.getHibernateTemplate().find(hql, keyVal);
	}

	public List getExtByValue(String keyVal, String value) {
	    String[] param = {keyVal, "%"+value+"%"};
	    String hql = "from CaseManagementNoteExt cExt where cExt.keyVal = ? and cExt.value like ?";
	    return this.getHibernateTemplate().find(hql, param);
	}

	public List getExtBeforeDate(String keyVal, Date dateValue) {
	    Object[] param = {keyVal, dateValue};
	    String hql = "from CaseManagementNoteExt cExt where cExt.keyVal = ? and cExt.dateValue <= ?";
	    return this.getHibernateTemplate().find(hql, param);
	}

	public List getExtAfterDate(String keyVal, Date dateValue) {
	    Object[] param = {keyVal, dateValue};
	    String hql = "from CaseManagementNoteExt cExt where cExt.keyVal = ? and cExt.dateValue >= ?";
	    return this.getHibernateTemplate().find(hql, param);
	}

	public void save(CaseManagementNoteExt cExt) {
	    this.getHibernateTemplate().save(cExt);
	}

	public void update(CaseManagementNoteExt cExt) {
	    this.getHibernateTemplate().update(cExt);
	}
}
