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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementMapDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class MeasurementManager {
	@Autowired
	private MeasurementDao measurementDao;

	@Autowired
	private MeasurementMapDao measurementMapDao;

	public List<Measurement> getCreatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> results = measurementDao.findByCreateDate(updatedAfterThisDateExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getCreatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public Measurement getMeasurement(LoggedInInfo loggedInInfo, Integer id) {
		Measurement result = measurementDao.find(id);

		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getMeasurement", "id=" + id);
		}

		return (result);
	}

	public List<MeasurementMap> getMeasurementMaps() {
		// should be safe to get all as they're a defined set of loinic codes or human entered entries
		List<MeasurementMap> results = measurementMapDao.getAllMaps();

		// not logging the read, this is not medicalData

		return (results);
	}

	public Measurement addMeasurement(LoggedInInfo loggedInInfo, Measurement measurement) {
		measurementDao.persist(measurement);
		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.addMeasurement", "id=" + measurement.getId());
		return(measurement);
	}

	/**
	 * ProgramId is not available in oscar right now but the method signature is correct for when it is available.
	 */
	public List<Measurement> getMeasurementsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> results = measurementDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "MeasurementManager.getMeasurementsByProgramProviderDemographicDate", "programId=" + programId + ", providerNo=" + providerNo + ", demographicId=" + demographicId + ", updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive.getTime());

		return (results);
    }
}
