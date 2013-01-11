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

import org.oscarehr.billing.CA.BC.model.Hl7Obr;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class Hl7ObrDao extends AbstractDao<Hl7Obr>{

	public Hl7ObrDao() {
		super(Hl7Obr.class);
	}

	public List<Hl7Obr> findByPid(int id) {
		Query query = createQuery("h", "h.pidId = :id");
		query.setParameter("id", id);
		return query.getResultList();
	}

	public List<Object[]> findLabResultsByPid(Integer pid) {
		String sql = "FROM Hl7Obr hl7_obr, Hl7Obx hl7_obx " +
				"WHERE hl7_obr.id = hl7_obx.obrId " +
				"AND hl7_obr.pidId = :pid " +
				"ORDER BY hl7_obr.diagnosticServiceSectId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("pid", pid);
		return query.getResultList();
	}
	
	public List<Object[]> findMinResultStatusByMessageId(Integer messageId) {
	    String sql = "SELECT MIN(obr.resultStatus) FROM Hl7Pid pid, Hl7Obr obr, Hl7Obx obx " +
	    		"WHERE obr.pidId = pid.id " +
	    		"AND obx.obrId = obr.id " + 
	    		"AND pid.messageId = :messageId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("messageId", messageId);
		return query.getResultList();
    }
	
	public List<Object[]> findByMessageId(Integer messageId) {
	    String sql = "FROM Hl7Pid pid, Hl7Obr obr, Hl7Obx obx " +
	    		"WHERE obr.pidId = pid.id " +
	    		"AND obx.obrId = obr.id " + 
	    		"AND pid.messageId = :messageId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("messageId", messageId);
		return query.getResultList();
    }

}
