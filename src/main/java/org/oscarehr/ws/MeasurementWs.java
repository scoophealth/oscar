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
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.managers.MeasurementManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.ws.transfer_objects.DataIdTransfer;
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
		Measurement measurement = measurementManager.getMeasurement(measurementId);
		return (MeasurementTransfer.toTransfer(measurement));
	}

	/**
	 * Get a list of DataIdTransfer objects for measurements starting with the passed in Id.
	 */
	public DataIdTransfer[] getMeasurementDataIds(Integer startIdInclusive, int itemsToReturn) {

		List<Measurement> measurements = measurementManager.getMeasurementsByIdStart(startIdInclusive, itemsToReturn);

		DataIdTransfer[] results = new DataIdTransfer[measurements.size()];
		for (int i = 0; i < measurements.size(); i++) {
			results[i] = getDataIdTransfer(measurements.get(i));
		}

		return (results);
	}

	private DataIdTransfer getDataIdTransfer(Measurement measurement) {
		DataIdTransfer result = new DataIdTransfer();

		Calendar cal = new GregorianCalendar();
		cal.setTime(measurement.getCreateDate());
		result.setCreateDate(cal);

		result.setCreatorProviderId(measurement.getProviderNo());
		result.setDataId(measurement.getId().toString());
		result.setDataType(Measurement.class.getSimpleName());
		result.setOwnerDemographicId(measurement.getDemographicId());

		if (measurement.getAppointmentNo() != null && measurement.getAppointmentNo() != 0) {
			Appointment appointment = scheduleManager.getAppointment(measurement.getAppointmentNo());
			if (appointment != null) {
				int programId = appointment.getProgramId();
				result.setClinicId(programId);

				Program program = programManager.getProgram(programId);
				if (program != null) {
					result.setFacilityId(program.getFacilityId());
				}
			}
		}

		return (result);
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
		measurementManager.addMeasurement(measurement);
		return (measurement.getId());
	}
}
