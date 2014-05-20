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
package org.oscarehr.managers;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class PrescriptionManager {
	@Autowired
	private PrescriptionDao prescriptionDao;

	@Autowired
	private DrugDao drugDao;

	public static class PrescriptionAndDrugs
	{
		public Prescription prescription;
		public List<Drug> drugs;
	}
	
	public Prescription getPrescription(Integer prescriptionId) {
		Prescription result = prescriptionDao.find(prescriptionId);

		//--- log action ---
		LogAction.addLogSynchronous("PrescriptionManager.getPrescription" , "id:"+result.getId());

		return (result);
	}


	/**
	 * @deprecated 2014-05-20 remove after calling ws method is removed
	 */
	public List<Prescription> getPrescriptionsByIdStart(Integer startIdInclusive, int itemsToReturn) {
		List<Prescription> results = prescriptionDao.findByIdStart(startIdInclusive, itemsToReturn);

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Prescription.getIdsAsStringList(results);
			LogAction.addLogSynchronous("PrescriptionManager.getPrescriptionsByIdStart", "ids returned=" + resultIds);
		}

		return (results);
	}

	public List<Prescription> getPrescriptionUpdatedAfterDate(Date updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Prescription> results = prescriptionDao.findByUpdateDate(updatedAfterThisDateInclusive, itemsToReturn);

		LogAction.addLogSynchronous("PrescriptionManager.getPrescriptionUpdatedAfterDate", "updatedAfterThisDateInclusive=" + updatedAfterThisDateInclusive);

		return (results);
	}

	public List<Drug> getDrugsByScriptNo(Integer scriptNo, Boolean archived) {
		List<Drug> results = drugDao.findByScriptNo(scriptNo, archived);

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Drug.getIdsAsStringList(results);
			LogAction.addLogSynchronous("PrescriptionManager.getDrugsByScriptNo", "drug ids returned=" + resultIds);
		}

		return (results);
	}
}
