/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Ronnie Cheng
 *
 * UserPropertyDAO.java
 *
 * Created on May 26, 2010
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.List;
import org.oscarehr.common.model.OtherId;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Jason Gallagher
 */
public class OtherIdDAO extends HibernateDaoSupport {
    
	/** Creates a new instance of UserPropertyDAO */
	public OtherIdDAO() {}

	public OtherId getOtherId(Integer tableName, Integer tableId, String otherKey){
		//Get a list of OtherIds in reverse order
		List<OtherId> otherIdList = this.getHibernateTemplate().find(
				"from OtherId where tableName=? and tableId=? and otherKey=? and deleted=false order by id desc limit 1",
				new Object[] {tableName, tableId, otherKey});
		return otherIdList.size()>0 ? otherIdList.get(0) : null;
	}

	public void save(OtherId otherId) {
	this.getHibernateTemplate().saveOrUpdate(otherId);
	}
}
