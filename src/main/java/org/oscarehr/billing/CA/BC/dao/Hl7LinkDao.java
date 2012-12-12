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

import org.oscarehr.billing.CA.BC.model.Hl7Link;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class Hl7LinkDao extends AbstractDao<Hl7Link>{

	public Hl7LinkDao() {
		super(Hl7Link.class);
	}
	
    public List<Object[]> findLabs() {
		String sql = "FROM Hl7Pid pid, Hl7Link link, Hl7Obr obr, Demographic demo " +
				"WHERE link.demographicNo = demo.id " +
				"AND pid.id = obr.pidId " +
				"AND ( link.status = 'P' OR link.status IS NULL ) " +
				"AND link.id = pid.id";

		Query q = entityManager.createQuery(sql);
		return q.getResultList();
	}
    
    public List<Object[]> findMagicLinks() {
    	String sql = "FROM Demographic demo, Hl7Pid pid, Hl7Link link " +
    	"WHERE pid.id = link.id " +
    	"AND demo.Hin = pid.externalId " +
    	"AND link.id IS NULL";
    	Query q = entityManager.createQuery(sql);
		return q.getResultList();
    }
    
    public List<Object[]> findLinksAndRequestDates(Integer demoId) {
    	String sql = "SELECT DISTINCT link.id, obr.requestedDateTime, obr.diagnosticServiceSectId " +
				"FROM Hl7Link link, Hl7Obr obr " +
				"WHERE link.demographicNo = :demoId " + 
				"AND link.id = obr.id " +
				"AND (" +
				"	link.status = 'N' " +
				"	OR link.status = 'A' " +
				"	OR link.status = 'S'" +
				") " +
				"ORDER BY obr.requestedDateTime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoId", demoId);
		return query.getResultList();
	
    }

}
