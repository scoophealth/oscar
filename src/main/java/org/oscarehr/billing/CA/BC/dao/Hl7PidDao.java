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

import java.sql.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.Hl7Pid;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class Hl7PidDao extends AbstractDao<Hl7Pid>{

	public Hl7PidDao() {
		super(Hl7Pid.class);
	}

    public List<Hl7Pid> findByMessageId(int messageId) {
		Query q = createQuery("h", "h.messageId = :msgId");
		q.setParameter("msgId", messageId);
		return q.getResultList();
    }

    public List<Object[]> findPidsByStatus(String status) {
		String sql = "FROM Hl7Pid p, Hl7Link l " +
				"WHERE p.id = l.id " +
				"AND ( l.status = :status " +
				"OR l.status IS NULL)";
		Query query = entityManager.createQuery(sql);
		query.setParameter("status", status);
		return query.getResultList();
    }

    public List<Object[]> findPidsAndMshByMessageId(Integer messageId) {
		String sql = "FROM Hl7Pid pid, Hl7Msh msh " +
				"WHERE pid.messageId = :msgId " +
				"AND msh.messageId = pid.messageId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("msgId", messageId);
		return query.getResultList();
    }
    
    public List<Object[]> findSigned(Integer pid) {
    	String sql = "FROM Hl7Pid hl7_pid, Hl7Link hl7_link, Provider provider " +
    			"WHERE hl7_pid.id = hl7_link.id " +
    			"AND provider.ProviderNo = hl7_link.providerNo " +
    			"AND hl7_pid.id = :pid";
		Query query = entityManager.createQuery(sql);
		query.setParameter("pid", pid);
		return query.getResultList();
    }
    
    public List<Object[]> findDocNotes(Integer pid) {
    	String sql = "FROM Hl7Pid hl7_pid, Hl7Message hl7_message " +
    			"WHERE hl7_pid.id = :pid " +
    			"AND hl7_pid.messageId = hl7_message.id";
    	Query query = entityManager.createQuery(sql);
		query.setParameter("pid", pid);
	return query.getResultList();
	}

	public Date findObservationDateByMessageId(Integer messageId) {
	    String sql = "SELECT MAX(obr.resultsReportStatusChange) " +
	    		"FROM Hl7Pid pid, Hl7Obr obr " +
	    		"WHERE obr.pidId = pid.id " +
	    		"AND pid.messageId = :messageId";
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(1);
		query.setParameter("messageId", messageId);
		List<Object> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return (Date) resultList.get(0);
		}
    }

	public List<Object[]> findByObservationResultStatusAndMessageId(String observationResultStatus, Integer messageId) {
	    String sql = "FROM Hl7Pid pid, Hl7Obr obr, Hl7Obx obx " +
	    		"WHERE obx.observationResultStatus like :observationResultStatus " +
	    		"AND obx.obrId = obr.id " +
	    		"AND obr.pidId = pid.id " +
	    		"AND pid.messageId = :messageId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("observationResultStatus", observationResultStatus);
		query.setParameter("messageId", messageId);
		return query.getResultList();
    }

	public List<Object[]> findByFillerOrderNumber(String fillerOrderNumber) {
	    String sql = "SELECT DISTINCT pid.messageId, MAX(obr.resultsReportStatusChange) " +
	    		"FROM Hl7Pid pid, Hl7Orc orc, Hl7Obr obr " +
	    		"WHERE orc.fillerOrderNumber like :fillerOrderNumber " +
	    		"AND orc.pidId = pid.id " +
	    		"AND obr.pidId = pid.id " +
	    		"GROUP BY pid.messageId " +
	    		"ORDER BY obr.resultsReportStatusChange";
		Query query = entityManager.createQuery(sql);
		query.setParameter("fillerOrderNumber", fillerOrderNumber);
		return query.getResultList();
    }
}
