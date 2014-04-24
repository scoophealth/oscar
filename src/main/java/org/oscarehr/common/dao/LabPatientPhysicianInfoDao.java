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


package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class LabPatientPhysicianInfoDao extends AbstractDao<LabPatientPhysicianInfo>{

	public LabPatientPhysicianInfoDao() {
		super(LabPatientPhysicianInfo.class);
	}

    public List<Object[]> findRoutings(Integer demographicNo, String labType) {
		String sql = "FROM LabPatientPhysicianInfo lpp, PatientLabRouting r "
                +"WHERE r.labType = :labType " +
                "AND lpp.id = r.labNo " +
                "AND r.demographicNo = :demoNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("labType", labType);
		query.setParameter("demoNo", demographicNo);
		return query.getResultList();
    }

	public List<Object[]> findByPatientName(String status, String labType, String providerNo, String patientLastName, String patientFirstName, String patientHealthNumber) {
		String sql = "FROM LabPatientPhysicianInfo lpp, ProviderLabRoutingModel plr " + 
				"WHERE plr.status like :status " +
                "AND plr.providerNo like :providerNo " +
                "AND plr.labType = :labType " +
                "AND lpp.patientLastName LIKE :lastName " +
                "AND lpp.patientFirstName LIKE :firstName " +
                "AND lpp.patientHin LIKE :hin " +
                "AND plr.labNo = lpp.id";
		Query q = entityManager.createQuery(sql);
		q.setParameter("status", "%" + status + "%");
		q.setParameter("providerNo", "".equals(providerNo) ? "%" : providerNo);
		q.setParameter("labType", labType);
		q.setParameter("lastName", patientLastName + "%");
		q.setParameter("firstName", patientFirstName + "%");
		q.setParameter("hin", patientHealthNumber);
		return q.getResultList();
    }

	public List<Object[]> findByDemographic(Integer demographicNo, String labType) {
		String sql = "FROM LabPatientPhysicianInfo lpp, PatientLabRouting plr " +
                "WHERE plr.labType = :labType " +
                "AND lpp.id = plr.labNo " +
                "AND plr.demographicNo = :demoNo";
		Query q = entityManager.createQuery(sql);
		q.setParameter("labType", labType);
		q.setParameter("demoNo", demographicNo);
	    return q.getResultList();
    }
	
	public List<Object[]> findLabServiceDatesByLabId(Integer labId) {
        String sql = "SELECT DISTINCT lpp.id, lpp.serviceDate, lpp2.serviceDate " +
        		"FROM LabPatientPhysicianInfo lpp, LabPatientPhysicianInfo lpp2, LabReportInformation tr " +
        		"WHERE lpp.accessionNum = lpp2.accessionNum " +
        		"AND lpp2.id = :labId " +
        		"AND tr.id = lpp.labReportInfoId " +
        		"ORDER BY tr.printDate, tr.printTime";
		Query query = entityManager.createQuery(sql);
		query.setParameter("labId", labId);
		return query.getResultList();
	}
	
	public List<Integer> getLabResultsSince(Integer demographicNo, Date updateDate) {
		String sql = "select lpp.id from LabPatientPhysicianInfo lpp, PatientLabRouting plr WHERE plr.labNo = lpp.id and plr.demographicNo = ?1 and (lpp.lastUpdateDate > ?2 or plr.dateModified > ?3) AND plr.labType IN ('Epsilon','CML')";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, updateDate);
		query.setParameter(3, updateDate);
		
		@SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();
		return results;
		
	}
}
