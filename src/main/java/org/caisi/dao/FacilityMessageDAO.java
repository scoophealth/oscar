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

import java.util.List;

import org.caisi.model.FacilityMessage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class FacilityMessageDAO extends HibernateDaoSupport  {

	public FacilityMessage getMessage(Long id) {
		return this.getHibernateTemplate().get(FacilityMessage.class,id);
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessages() {
		return this.getHibernateTemplate().find("from FacilityMessage fm order by fm.expiry_date desc");
	}
	
	public void saveMessage(FacilityMessage mesg) {
		this.getHibernateTemplate().saveOrUpdate(mesg);
	}

	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessagesByFacilityId(Integer facilityId) {
		return this.getHibernateTemplate().find("from FacilityMessage fm where facilityId=? order by fm.expiry_date desc", facilityId);
	}
}
