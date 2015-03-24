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

package org.oscarehr.ws;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.managers.MeasurementManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.ws.transfer_objects.MeasurementMapTransfer;
import org.oscarehr.ws.transfer_objects.MeasurementTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class MeasurementWs extends AbstractWs {
	@Autowired
	private MeasurementManager measurementManager;

	@Autowired
	private ScheduleManager scheduleManager;

	@Autowired
	private ProgramManager2 programManager;

	public MeasurementTransfer getMeasurement(Integer measurementId) {
		Measurement measurement = measurementManager.getMeasurement(getLoggedInInfo(), measurementId);
		return (MeasurementTransfer.toTransfer(measurement));
	}

	public MeasurementTransfer[] getMeasurementsCreatedAfterDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> results = measurementManager.getCreatedAfterDate(getLoggedInInfo(), updatedAfterThisDateExclusive, itemsToReturn);
		return (MeasurementTransfer.toTransfers(results));
	}

	public MeasurementMapTransfer[] getMeasurementMaps() {
		List<MeasurementMap> measurementMaps = measurementManager.getMeasurementMaps();
		return (MeasurementMapTransfer.toTransfers(measurementMaps));
	}

	/**
	 * @return the ID of the added measurement
	 */
	public Integer addMeasurement(MeasurementTransfer measurementTransfer) {
		Measurement measurement = new Measurement();
		measurementTransfer.copyTo(measurement);
		measurementManager.addMeasurement(getLoggedInInfo(), measurement);
		return (measurement.getId());
	}

	public MeasurementTransfer[] getMeasurementsByProgramProviderDemographicDate(Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Measurement> measurements = measurementManager.getMeasurementsByProgramProviderDemographicDate(getLoggedInInfo(), programId, providerNo, demographicId, updatedAfterThisDateExclusive, itemsToReturn);
		return (MeasurementTransfer.toTransfers(measurements));
	}

}
