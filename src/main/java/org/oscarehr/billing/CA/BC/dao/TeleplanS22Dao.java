/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.billing.CA.BC.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.TeleplanS22;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS22Dao extends AbstractDao<TeleplanS22>{

	public TeleplanS22Dao() {
		super(TeleplanS22.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<TeleplanS22> search_taS22 (Integer s21Id, String type, String practitionerNo) {
		Query q = entityManager.createQuery("select t from TeleplanS22 t where t.s21Id=? and t.s22Type<>? and t.practitionerNo like ? order by t.id");
		q.setParameter(1, s21Id);
		q.setParameter(2, type);
		q.setParameter(3, practitionerNo);
		return q.getResultList();
	}
}
