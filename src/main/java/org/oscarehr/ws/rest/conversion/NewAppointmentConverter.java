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
package org.oscarehr.ws.rest.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.NewAppointmentTo1;

/**
 * Conversion from the web's mininal new appt. This converter adds some defaults.
 * 
 * @author marc
 *
 */
public class NewAppointmentConverter extends AbstractConverter<Appointment, NewAppointmentTo1> {

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

	@Override
	public Appointment getAsDomainObject(LoggedInInfo loggedInInfo, NewAppointmentTo1 t) throws ConversionException {
		Appointment d = new Appointment();

		String format = t.getStartTime() != null ? "yyyy-MM-dd HH:mm" : "yyyy-MM-dd hh:mm a";

		SimpleDateFormat dateFormatter = new SimpleDateFormat(format);

		try {
			Date date = dateFormatter.parse(t.getAppointmentDate() + " " + (t.getStartTime() != null ? t.getStartTime() : t.getStartTime12hWithMedian()));
			d.setAppointmentDate(date);
			d.setStartTime(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, t.getDuration() - 1);
			d.setEndTime(cal.getTime());

		} catch (ParseException e) {
			MiscUtils.getLogger().warn("Cannot parse new appointment dates");
			throw new ConversionException("Could not parse date/times of appointment. please check format");
		}

		Demographic demographic = demographicDao.getDemographicById(t.getDemographicNo());
		if (demographic == null) {
			throw new ConversionException("Unable to find patient");
		}
		d.setName(demographic.getFormattedName());
		d.setCreateDateTime(new Date());
		d.setCreator(loggedInInfo.getLoggedInProviderNo());
		d.setDemographicNo(t.getDemographicNo());
		d.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		d.setLocation(t.getLocation());
		d.setNotes(t.getNotes());
		d.setProgramId(0);
		d.setProviderNo(t.getProviderNo());
		d.setReason(t.getReason());
		d.setRemarks("");
		d.setResources(t.getResources());
		d.setStatus(t.getStatus());
		d.setType(t.getType());
		d.setUrgency(t.getUrgency());
		d.setUpdateDateTime(d.getCreateDateTime());
		//This looks sketchy, but 17=Others
		d.setReasonCode(17);

		return d;
	}

	@Override
	public NewAppointmentTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Appointment d) throws ConversionException {
		return null;
	}

}
