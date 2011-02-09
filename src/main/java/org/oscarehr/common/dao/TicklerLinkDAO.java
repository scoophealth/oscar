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
 * GNU General Public License for more details. * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;


import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.TicklerLink;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TicklerLinkDAO extends HibernateDaoSupport {

	private static Logger log = MiscUtils.getLogger();

	public TicklerLink getTicklerLink(Long id) {
	    TicklerLink noteLink = (TicklerLink) this.getHibernateTemplate().get(TicklerLink.class, id);
	    return noteLink;
	}
	
	public List getLinkByTableId(Integer tableName, Long tableId) {
	    Object[] param = {tableName, tableId};
	    String hql = "from TicklerLink cLink where cLink.tableName = ? and cLink.tableId = ? order by cLink.id";
	    return this.getHibernateTemplate().find(hql, param);
	}
	
	public List getLinkByTickler(Long ticklerNo) {
	    String hql = "from TicklerLink cLink where cLink.ticklerNo = ? order by cLink.id";
	    return this.getHibernateTemplate().find(hql, ticklerNo);
	}
	
	public void save(TicklerLink cLink) {
	    this.getHibernateTemplate().save(cLink);
	}
	
	public void update(TicklerLink cLink) {
	    this.getHibernateTemplate().update(cLink);
	}
}
