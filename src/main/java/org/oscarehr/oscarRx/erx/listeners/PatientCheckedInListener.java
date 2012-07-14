/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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

package org.oscarehr.oscarRx.erx.listeners;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.event.AppointmentStatusChangeEvent;
import org.oscarehr.oscarRx.erx.ERxPatientRecordSynchronizer;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationListener;

/**
 * Listens for PatientCheckedInEvents and triggers sending patient data.
 */
public class PatientCheckedInListener implements ApplicationListener<AppointmentStatusChangeEvent> {
	Logger logger = MiscUtils.getLogger();

	/**
	 * Create an instance of a PatientCheckedInListener object.
	 */
	public PatientCheckedInListener() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.oscarehr.event.interfaces.EventListener#onOscarEvent(org.oscarehr.event.interfaces.EventData)
	 */
	@Override
	public void onApplicationEvent(AppointmentStatusChangeEvent event) {
		try{
			String providerId = event.getProvider_no();
			logger.debug("AppointmentStatusChange for provider "+providerId+" appt "+event.getAppointment_no()+ " status is "+event.getStatus());
	
			ProviderPreferenceDao providerPreferenceDao = ((ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao"));
	
			// Load provider preferences to see if we should continue
			ProviderPreference providerPreference = providerPreferenceDao.find(providerId);
			logger.debug("proPref "+providerPreference);
			// If the provider uses an external prescription provider, and it's enabled...
			if ((providerPreference != null) && providerPreference.isERxEnabled()) {

				DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
				OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
	
				// If the appointment status is "Here" or "Empty room" or "Picked"...
				String apptStatus = event.getStatus();
				if (apptStatus.equals("H") || apptStatus.equals("E") || apptStatus.equals("P")) {
					// Get data about the appointment
					Appointment appt = appointmentDao.find(Integer.parseInt(event.getAppointment_no()));
	
					// Load the patient data from the first appointment matched
					String patientId = String.valueOf(appt.getDemographicNo());
					Demographic patient = demographicDao.getDemographic(patientId);
					logger.debug("calling sendRecord");
					// Send the patient's record
					ERxPatientRecordSynchronizer synchronizer = new ERxPatientRecordSynchronizer();
					synchronizer.sendRecord(patient, providerId);
				}else{
					logger.debug("Ignoring appt event");
				}
			}
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}

	}//End of onOscarEvent
}
