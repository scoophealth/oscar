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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.LabTestResults;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class LabTestResultsDao extends AbstractDao<LabTestResults> {

	public LabTestResultsDao() {
		super(LabTestResults.class);
	}

	
	public List<LabTestResults> findByTitleAndLabInfoId(Integer labId) {
		Query query = createQuery("r", "r.title IS NOT EMPTY and r.labPatientPhysicianInfoId = :labId");
		query.setParameter("labId", labId);
		return query.getResultList();
	}
	
	
	public List<LabTestResults> findByLabInfoId(Integer labId) {
		Query query = createQuery("r", "r.labPatientPhysicianInfoId = :labId");
		query.setParameter("labId", labId);
		return query.getResultList();
	}

	
    public List<LabTestResults> findByAbnAndLabInfoId(String abn, Integer labId) {
		Query query = createQuery("r", "r.abn = :abn and r.labPatientPhysicianInfoId = :labId");
		query.setParameter("labId", labId);
		query.setParameter("abn", abn);
		return query.getResultList();
	}

	/**
	 * Finds unique test names for the specified patient and lab type
	 * 
	 * @param demoNo
	 * 		Demographic id of the patient
	 * @param labType
	 * 		Type of the lab to find results for
	 * @return
	 * 		Returns a list of triples containing lab type, test title and test name.
	 */
    public List<Object[]> findUniqueTestNames(Integer demoNo, String labType) {
        String jpql = "SELECT DISTINCT p.labType, ltr.title, ltr.testName " +
                "FROM " +
                "PatientLabRouting p, " +
                "LabTestResults ltr, " +
                "LabPatientPhysicianInfo lpp "+
                "WHERE p.labType = :labType "+
                "AND p.demographicNo = :demoNo "+
                "AND p.labNo = ltr.labPatientPhysicianInfoId "+
                "AND ltr.labPatientPhysicianInfoId = lpp.id " +
                "AND ltr.testName IS NOT NULL " +
                "AND ltr.testName IS NOT EMPTY "+
                "ORDER BY ltr.title";
        
        Query query = entityManager.createQuery(jpql);
        query.setParameter("labType", labType);
        query.setParameter("demoNo", demoNo);
        return query.getResultList();
    }

	public List<LabTestResults> findByAbnAndPhysicianId(String abn, Integer lppii) {
		Query q = createQuery("ltr", "ltr.abn = :abn and ltr.labPatientPhysicianInfoId = :lppii");
		q.setParameter("abn", abn);
		q.setParameter("lppii", lppii);
		return q.getResultList();
    }

	public List<LabTestResults> findByLabPatientPhysicialInfoId(Integer labid) {
		Query query = createQuery("r", "r.labPatientPhysicianInfoId = :labid");
		query.setParameter("labid", labid);
		return query.getResultList();
    }
}