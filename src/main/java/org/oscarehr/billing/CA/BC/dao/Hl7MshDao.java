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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.Hl7Msh;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class Hl7MshDao extends AbstractDao<Hl7Msh>{

	public Hl7MshDao() {
		super(Hl7Msh.class);
	}

    public List<Object[]> findPathnetResultsDataByPatientNameHinStatusAndProvider(String patientName, String patientHealthNumber, String status, String providerNo, String labType) {
        String sql = "SELECT msh, pid, orc, obr, providerLabRouting, MIN(obr.resultStatus) " +
                "FROM Hl7Msh msh, Hl7Pid pid, Hl7Orc orc, Hl7Obr obr, ProviderLabRoutingModel providerLabRouting " +
                "WHERE providerLabRouting.labNo = pid.messageId " +
                "AND pid.messageId = msh.messageId " +
                "AND pid.id = orc.pidId " +
                "AND pid.id = obr.pidId  "+
                "AND providerLabRouting.status like :status " +
                "AND providerLabRouting.providerNo like :providerNo " +
                "AND providerLabRouting.labType = :labType " +
                "AND pid.patientName like :patientName " +
                "AND pid.externalId like :patientHealthNumber " +
                "GROUP BY pid.id";
        
		Query query = entityManager.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("providerNo", providerNo);
		query.setParameter("labType", labType);
		query.setParameter("patientName", patientName);
		query.setParameter("patientHealthNumber", patientHealthNumber);
		return query.getResultList();
    }

    public List<Object[]> findPathnetResultsByLabNo(Integer labNo) {
        String sql = "SELECT msh, pid, orc, obr, providerLabRouting, MIN(obr.resultStatus) " +
                "FROM Hl7Msh msh, Hl7Pid pid, Hl7Orc orc, Hl7Obr obr, ProviderLabRoutingModel providerLabRouting " +
                "WHERE providerLabRouting.labNo = pid.messageId " +
                "AND pid.messageId = msh.messageId " +
                "AND pid.id = orc.pidId " +
                "AND pid.id = obr.pidId  "+
                "AND pid.messageId= :labNo " +
                "GROUP BY pid.id";
        
		Query query = entityManager.createQuery(sql);
		query.setParameter("labNo", labNo);
		return query.getResultList();
    }
    
	public List<Object[]> findPathnetResultsDeomgraphicNo(Integer demographicNo, String labType) {		
	    String sql =  "SELECT msh, pid, orc, obr, patientLabRouting, MIN(obr.resultStatus) " +
                "FROM Hl7Msh msh, Hl7Pid pid, Hl7Orc orc, Hl7Obr obr, PatientLabRouting patientLabRouting " +
                "WHERE patientLabRouting.labNo = pid.id " +
                "AND pid.id = orc.pidId " +
                "AND pid.id = obr.pidId " +
                "AND msh.messageId = pid.id "+
                "AND patientLabRouting.labType = :labType " +
                "AND patientLabRouting.demographicNo = :demographicNo " +
                "GROUP BY pid.id";

		Query query = entityManager.createQuery(sql);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("labType", labType);
		return query.getResultList();
    }
	
	public List<Integer> getLabResultsSince(Integer demographicNo, Date updateDate) {
		String query = "select m.id from Hl7Msh m, PatientLabRouting p WHERE m.id = p.labNo and p.labType='BCP' and p.demographicNo = ?1 and (m.dateTime > ?2 or p.dateModified > ?3) ";
		Query q = entityManager.createQuery(query);
		
		q.setParameter(1, demographicNo);
		q.setParameter(2, updateDate);
		q.setParameter(3, updateDate);
		
		return q.getResultList();    
	}
	
}
