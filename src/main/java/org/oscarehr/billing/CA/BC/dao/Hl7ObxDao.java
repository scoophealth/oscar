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

import org.oscarehr.billing.CA.BC.model.Hl7Obx;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class Hl7ObxDao extends AbstractDao<Hl7Obx> {

	public Hl7ObxDao() {
		super(Hl7Obx.class);
	}
	
	public List<Hl7Obx> findByObrId(int obrId) {
		Query q = entityManager.createQuery("select h from Hl7Obx h where h.obrId = ?1");
		q.setParameter(1, obrId);
		
		List<Hl7Obx> results = q.getResultList();
		return results;
	}

    public List<Object[]> findObxAndObrByObrId(Integer id) {
		String sql = "FROM Hl7Obx obx, Hl7Obr obr " +
				"WHERE obr.id = :id " +
				"AND obr.id = obx.obrId ";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		return query.getResultList();
	    
    }

	public List<Object[]> findByMessageIdAndAbnormalFlags(Integer messageId, List<String> abnormalFlags) {
		String sql = "FROM Hl7Pid pid, Hl7Obr obr, Hl7Obx obx " +
				"WHERE obr.pidId = pid.id " +
				"AND obx.obrId = obr.id " +
				"AND obx.abnormalFlags IN (:abnormalFlags) " +
				"AND pid.messageId = :messageId";
	    
		Query query = entityManager.createQuery(sql);
		query.setParameter("abnormalFlags", abnormalFlags);
		query.setParameter("messageId", messageId);
		return query.getResultList();
    }
}
