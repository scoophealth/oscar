/**
 * Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
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
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.dao;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.common.model.CdsClientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CdsClientFormDao extends AbstractDao {

	@Autowired
	private AdmissionDao admissionDao=null;
	
	public CdsClientForm findLatestByFacilityClient(Integer facilityId, Integer clientId) {

		String sqlCommand = "select x from CdsClientForm x where x.facilityId=?1 and x.clientId=?2 order by x.created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, facilityId);
		query.setParameter(2, clientId);
		query.setMaxResults(1);
		
		return ((CdsClientForm)getSingleResultOrNull(query));
	}

	/**
	 * transaction is mandatory as it populates a temp table for other calls to use.
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public void populateTempTableWithLatestSignedCdsForm(int programId, GregorianCalendar startDate, GregorianCalendar endDate) {
		
		List<Admission> admissions=admissionDao.getAdmissionsByProgramAndDate(programId, startDate.getTime(), endDate.getTime());

		createTempTable();

		for (Admission admission : admissions)
		{
			CdsClientForm latestForm=findLatestBySignedAndAdmission(admission.getId().intValue());
			insertIntoTempTable(latestForm.getAdmissionId());
		}		

    }

	@Transactional(propagation=Propagation.MANDATORY)
	private void insertIntoTempTable(Integer cdsClientFormId)
	{
		String sqlCommand="insert into validCdsClientForms values (?)";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, cdsClientFormId);
		query.executeUpdate();
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	private void createTempTable()
	{
		String sqlCommand="create temporary table validCdsClientForms (cdsClientFormId int primary key)";
		Query query = entityManager.createQuery(sqlCommand);
		query.executeUpdate();
	}
	
	public CdsClientForm findLatestBySignedAndAdmission(Integer admissionId)
	{
		String sqlCommand="select x from CdsClientForm x where admissionId=? and signed=? order by created desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, admissionId);
		query.setParameter(2, true);
		query.setMaxResults(1);
		
		return((CdsClientForm)query.getSingleResult());
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public int countUniqueIndividualsMultipleAdmission()
	{
		String sqlCommand="";
		
		return -1;
	}
}
