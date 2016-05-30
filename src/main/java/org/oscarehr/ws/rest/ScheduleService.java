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
package org.oscarehr.ws.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.DateUtils;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.AppointmentType;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.managers.AppointmentManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.web.PatientListApptBean;
import org.oscarehr.web.PatientListApptItemBean;
import org.oscarehr.ws.rest.conversion.AppointmentConverter;
import org.oscarehr.ws.rest.conversion.AppointmentStatusConverter;
import org.oscarehr.ws.rest.conversion.AppointmentTypeConverter;
import org.oscarehr.ws.rest.conversion.LookupListItemConverter;
import org.oscarehr.ws.rest.conversion.NewAppointmentConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.SchedulingResponse;
import org.oscarehr.ws.rest.to.model.AppointmentStatusTo1;
import org.oscarehr.ws.rest.to.model.AppointmentTo1;
import org.oscarehr.ws.rest.to.model.NewAppointmentTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/schedule")
@Component("scheduleService")
public class ScheduleService extends AbstractServiceImpl {

	Logger logger = MiscUtils.getLogger();

	@Autowired
	private ScheduleManager scheduleManager;
	@Autowired
	private AppointmentManager appointmentManager;
	@Autowired
	private DemographicManager demographicManager;
	@Autowired
	private SecurityInfoManager securityInfoManager;

	@GET
	@Path("/day/{date}")
	@Produces("application/json")
	public PatientListApptBean getAppointmentsForDay(@PathParam("date") String date) {
		String providerNo = this.getCurrentProvider().getProviderNo();
		return getAppointmentsForDay(providerNo, date);
	}

	@GET
	@Path("/{providerNo}/day/{date}")
	@Produces("application/json")
	/**
	 * Will substitute "me" to your logged in provider no, and "today" to doday's date.
	 * eg /schedule/me/day/today
	 * 
	 * @param providerNo
	 * @param date
	 * @return
	 */
	public PatientListApptBean getAppointmentsForDay(@PathParam("providerNo") String providerNo, @PathParam("date") String date) {
		SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		PatientListApptBean response = new PatientListApptBean();

		try {
			Date dateObj = null;
			if ("today".equals(date)) {
				dateObj = new Date();
			} else {
				dateObj = DateUtils.parseIso8601Date(date);
			}

			if ("".equals(providerNo)) {
				providerNo = loggedInInfo.getLoggedInProviderNo();
			}

			List<Appointment> appts = scheduleManager.getDayAppointments(loggedInInfo, providerNo, dateObj);
			for (Appointment appt : appts) {
				PatientListApptItemBean item = new PatientListApptItemBean();
				item.setDemographicNo(appt.getDemographicNo());
				if(appt.getDemographicNo() == 0){
					item.setName(appt.getName());
				}else{
					item.setName(demographicManager.getDemographicFormattedName(loggedInInfo, appt.getDemographicNo()));
				}
				item.setStartTime(timeFormatter.format(appt.getStartTime()));
				item.setReason(appt.getReason());
				item.setStatus(appt.getStatus());
				item.setAppointmentNo(appt.getId());
				item.setDate(appt.getStartTimeAsFullDate());
				response.getPatients().add(item);
			}
		} catch (ParseException e) {
			throw new RuntimeException("Invalid Date sent, use yyyy-MM-dd format");
		}
		return response;
	}

	@GET
	@Path("/statuses")
	@Produces("application/json")
	public AbstractSearchResponse<AppointmentStatusTo1> getAppointmentStatuses() {
		AbstractSearchResponse<AppointmentStatusTo1> response = new AbstractSearchResponse<AppointmentStatusTo1>();

		List<AppointmentStatus> results = scheduleManager.getAppointmentStatuses(getLoggedInInfo());
		AppointmentStatusConverter converter = new AppointmentStatusConverter();

		response.setContent(converter.getAllAsTransferObjects(getLoggedInInfo(), results));
		response.setTotal(results.size());

		return response;
	}

	@POST
	@Path("/add")
	@Produces("application/json")
	@Consumes("application/json")
	public SchedulingResponse addAppointment(NewAppointmentTo1 appointmentTo) {
		SchedulingResponse response = new SchedulingResponse();

		NewAppointmentConverter converter = new NewAppointmentConverter();

		//TODO: Need to add some more validation here

		Appointment appt = converter.getAsDomainObject(getLoggedInInfo(), appointmentTo);

		appointmentManager.addAppointment(getLoggedInInfo(), appt);

		response.setAppointment(new AppointmentConverter().getAsTransferObject(getLoggedInInfo(), appt));
		
		return response;
	}

	@POST
	@Path("/getAppointment")
	@Produces("application/json")
	@Consumes("application/json")
	public SchedulingResponse getAppointment(AppointmentTo1 appointmentTo) {
		SchedulingResponse response = new SchedulingResponse();

		AppointmentConverter converter = new AppointmentConverter(true, true);

		Appointment appt = appointmentManager.getAppointment(getLoggedInInfo(), appointmentTo.getId());

		response.setAppointment(converter.getAsTransferObject(getLoggedInInfo(), appt));

		return response;
	}

	@POST
	@Path("/deleteAppointment")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteAppointment(AppointmentTo1 appointmentTo) {

		appointmentManager.deleteAppointment(getLoggedInInfo(), appointmentTo.getId());

		return Response.status(Status.OK).build();
	}

	@POST
	@Path("/updateAppointment")
	@Consumes("application/json")
	@Produces("application/json")
	public SchedulingResponse updateAppointment(AppointmentTo1 appointmentTo) {
		SchedulingResponse response = new SchedulingResponse();

		AppointmentConverter converter = new AppointmentConverter();
		Appointment appt = converter.getAsDomainObject(getLoggedInInfo(), appointmentTo);

		scheduleManager.updateAppointment(getLoggedInInfo(), appt);

		response.setAppointment(converter.getAsTransferObject(getLoggedInInfo(), appt));
		return response;
	}

	@POST
	@Path("/{demographicNo}/appointmentHistory")
	@Produces("application/json")
	public SchedulingResponse findExistAppointments(@PathParam("demographicNo") Integer demographicNo) {
		SchedulingResponse response = new SchedulingResponse();
		List<Appointment> appts = appointmentManager.getAppointmentHistoryWithoutDeleted(getLoggedInInfo(), demographicNo, 0, OscarAppointmentDao.MAX_LIST_RETURN_SIZE);
		if(appts.size() == OscarAppointmentDao.MAX_LIST_RETURN_SIZE) {
			logger.warn("appointment history over MAX_LIST_RETURN_SIZE for demographic " + demographicNo);
		}
		AppointmentConverter converter = new AppointmentConverter();
		response.setAppointments(converter.getAllAsTransferObjects(getLoggedInInfo(), appts));
		
		return response;
	}

	@POST
	@Path("/appointment/{id}/updateStatus")
	@Produces("application/json")
	@Consumes("application/json")
	public SchedulingResponse updateAppointmentStatus(@PathParam("id") Integer id, AppointmentTo1 appt) {
		SchedulingResponse response = new SchedulingResponse();
		AppointmentConverter converter = new AppointmentConverter();
		String status = appt.getStatus();

		Appointment appointment = appointmentManager.updateAppointmentStatus(getLoggedInInfo(), id, status);

		response.setAppointment(converter.getAsTransferObject(getLoggedInInfo(), appointment));

		return response;
	}

	@POST
	@Path("/appointment/{id}/updateType")
	@Produces("application/json")
	@Consumes("application/json")
	public SchedulingResponse updateAppointmentType(@PathParam("id") Integer id, AppointmentTo1 appt) {
		SchedulingResponse response = new SchedulingResponse();
		AppointmentConverter converter = new AppointmentConverter();
		String type = appt.getType();

		Appointment appointment = appointmentManager.updateAppointmentType(getLoggedInInfo(), id, type);

		response.setAppointment(converter.getAsTransferObject(getLoggedInInfo(), appointment));

		return response;
	}

	@Path("/appointment/{id}/updateUrgency")
	@Produces("application/json")
	@Consumes("application/json")
	public SchedulingResponse updateAppointmentUrgency(@PathParam("id") Integer id, AppointmentTo1 appt) {
		SchedulingResponse response = new SchedulingResponse();
		AppointmentConverter converter = new AppointmentConverter();
		String urgency = appt.getUrgency();

		Appointment appointment = appointmentManager.updateAppointmentUrgency(getLoggedInInfo(), id, urgency);

		response.setAppointment(converter.getAsTransferObject(getLoggedInInfo(), appointment));

		return response;
	}

	@GET
	@Path("/fetchMonthly/{providerNo}/{year}/{month}")
	@Produces("application/json")
	public SchedulingResponse fetchMonthlyData(@PathParam("year") Integer year, @PathParam("month") Integer month, @PathParam("providerNo") String providerNo) {
		SchedulingResponse response = new SchedulingResponse();
		
		List<Appointment> appts = appointmentManager.findMonthlyAppointments(getLoggedInInfo(), providerNo, year, month);
		
		AppointmentConverter converter = new AppointmentConverter();
		response.setAppointments(converter.getAllAsTransferObjects(getLoggedInInfo(), appts));
		
		return response;
	}

	/*
	 * These are some method from the ERO branch which I didn't get to.
	 * 
	@GET
	@Path("/{startDate}/{endDate}/{providerId}/fetchFlipView")
	@Produces("application/json")
	public Response fetchFlipView(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("providerId") String providerId) {
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/roomDetails/get")
	@Produces("application/json")
	public Response getRoomDetails() {
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/{appDate}/{providerId}/{startTime}/{endTime}/checkProvAvali")
	@Produces("application/json")
	public Response checkProviderAvaliablity(@PathParam("appDate") String appDate, @PathParam("providerId") String providerId, @PathParam("startTime") String startTime, @PathParam("endTime") String endTime) {
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/blockreason/get")
	@Produces("application/json")
	public Response getBlockTimeReason() {
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("/scheduleTempCode/get")
	@Produces("application/json")
	public Response fetchScheduleTempCode() {
		return Response.status(Status.OK).build();
	}
*/
	
	@GET
	@Path("/types")
	@Produces("application/json")
	public SchedulingResponse getAppointmentTypes() {
		SchedulingResponse response = new SchedulingResponse();

		List<AppointmentType> types = scheduleManager.getAppointmentTypes();

		AppointmentTypeConverter converter = new AppointmentTypeConverter();

		response.setTypes(converter.getAllAsTransferObjects(getLoggedInInfo(), types));

		return response;
	}

	@GET
	@Path("/reasons")
	@Produces("application/json")
	public SchedulingResponse getAppointmentReasons() {

		SchedulingResponse response = new SchedulingResponse();

		List<LookupListItem> items = appointmentManager.getReasons();

		LookupListItemConverter converter = new LookupListItemConverter();

		response.setReasons(converter.getAllAsTransferObjects(getLoggedInInfo(), items));
		
		return response;
	}

}
