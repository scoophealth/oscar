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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.AppointmentStatusDao;
import org.oscarehr.common.dao.LookupListDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;

import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;

import org.oscarehr.appointment.search.FilterDefinition;
import org.oscarehr.appointment.search.Provider;
import org.oscarehr.appointment.search.filters.AvailableTimeSlotFilter;






//import org.oscarehr.oscar_clinic_component.manager.BookingLearningManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.transfer_objects.CalendarScheduleCodePairTransfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.log.LogAction;

@Service
public class AppointmentSearchManager {

	protected Logger logger = MiscUtils.getLogger();

	@Autowired
	private OscarAppointmentDao appointmentDao;
	@Autowired
	private AppointmentStatusDao appointmentStatusDao;
	@Autowired
	private LookupListDao lookupListDao;
	@Autowired
	private SecurityInfoManager securityInfoManager;
	@Autowired
	private AppointmentArchiveDao appointmentArchiveDao;
	@Autowired
	private ScheduleManager scheduleManager;
	@Autowired
	private DemographicManager demographicManager;

	
	

	
	
	public List<TimeSlot> findAppointment(SearchConfig config, Integer demographicNo,Long appointmentTypeId,Calendar startDate) throws java.lang.ClassNotFoundException,java.lang.InstantiationException,java.lang.IllegalAccessException{
		List<TimeSlot> appointments = new ArrayList<TimeSlot>();
		Map<Provider, Character[]> providerMap = config.getProvidersForAppointmentType(demographicNo, appointmentTypeId);
		Demographic demographic = demographicManager.getDemographic(null, demographicNo);
		String mrp = demographic.getProviderNo();
		//need to get Providers for demographic and appt Type.
		
		//////
		for(int i=0;i<config.getDaysToSearchAheadLimit();i++){
			Calendar calDayToSearch = (Calendar) startDate.clone();
			calDayToSearch.add(Calendar.DAY_OF_YEAR, i);
			
			/// keep? or change ?  Element searchRecord = recordDateSearched(doc, calDayToSearch,clinic.getTimezone());

			for(Provider provider : providerMap.keySet()){
				DayWorkSchedule dayWorkSchedule = scheduleManager.getDayWorkSchedule(provider.getProviderNo(), calDayToSearch);
				
				/// keep? or change ? Element searchedProviderRecord = recordProviderSearched(doc,searchRecord,provider.getProviderNo(),dayWorkScheduleTransfer, providerMap.get(provider));
				
				
				if (dayWorkSchedule == null || dayWorkSchedule.isHoliday())	continue;
				// Is this still needed?  probably if (BookingLearningManager.isDaySetToSkip(clinic, provider.getProviderNo(), calDayToSearch, appointmentTypeId)) continue;
				
				List<TimeSlot> providerAppointments = getAllowedTimesByType(dayWorkSchedule, providerMap.get(provider),provider.getProviderNo());
				/// keep? or change ? recordFilterForSearchedProvider(doc,searchedProviderRecord,dayWorkScheduleTransfer,"N/A" , providerAppointments);						

				List<FilterDefinition> filterClassNames = provider.getFilter();
				if(filterClassNames == null || filterClassNames.isEmpty()){
					filterClassNames = config.getFilter();
				}
				if (filterClassNames != null){
					for (FilterDefinition className : filterClassNames){
						@SuppressWarnings("unchecked")
						Class<AvailableTimeSlotFilter> filterClass = (Class<AvailableTimeSlotFilter>) Class.forName(className.getFilterClassName());
						logger.debug("filter class null? "+filterClass.getName());
						AvailableTimeSlotFilter filterClassInstance = filterClass.newInstance();
						providerAppointments = filterClassInstance.filterAvailableTimeSlots(config,mrp,provider.getProviderNo(), appointmentTypeId, dayWorkSchedule, providerAppointments,calDayToSearch,className.getParams());
						/// keep? or change ? recordFilterForSearchedProvider(doc,searchedProviderRecord,dayWorkScheduleTransfer,filterClassInstance.getClass().getSimpleName() , providerAppointments);						
						if(providerAppointments.size() == 0) {
							break;
						}
					}
				}		
				appointments.addAll(providerAppointments);
			}
			if (appointments.size() > config.getNumberOfAppointmentOptionsToReturn()){
				break;
			}
		}
		//////
		
		
		Collections.sort(appointments,TimeSlot.getTimeSlotComparator());
		return appointments;
	}
	
	
	public static List<TimeSlot> getAllowedTimesByType(DayWorkSchedule dayWorkSchedule, Character[] codes, String providerNo){
		ArrayList<TimeSlot> allowedTimesFilteredByType = new ArrayList<TimeSlot>();
		for (CalendarScheduleCodePairTransfer entry : CalendarScheduleCodePairTransfer.toTransfer(dayWorkSchedule.getTimeSlots())  ){
			char c = entry.getScheduleCode();
			if (Arrays.binarySearch(codes, c) >= 0){
				allowedTimesFilteredByType.add(new TimeSlot(providerNo, null, entry.getDate(), c));
			}
		}
		return allowedTimesFilteredByType;
	}

	
	
	/*
	DaysToSearchAheadLimit = Number of days it searches before giving up. ie search for the next 60 days before giving up
	NumberOfAppointmentOptionsToReturn = Number of appts that seems like it gives a reasonable choice.
	 ========================
		get Circle of Care Providers capable of dealing with appt Type
		for each provider retrieve their DayWorkSchedule
		
		//check if it's a holiday
		//get providers timecodes for appt Type
		//how to eliminate 30 min codes represented as 2 15min segments eg 1112211___ codes really should just be eg 1112_11___
		//Then filter for future	
	*/
/*
 
	public static List<TimeSlot> findAppointments(Clinic clinic,String providerNo,Long appointmentTypeId,Calendar startDate,Document doc) throws java.lang.ClassNotFoundException,java.lang.InstantiationException,java.lang.IllegalAccessException{
		Map<Provider, Character[]> providerMap = clinic.getProvidersForAppointmentType(providerNo, appointmentTypeId);
		List<TimeSlot> appointments = new ArrayList<TimeSlot>();
		for(int i=0;i<clinic.getDaysToSearchAheadLimit();i++){
			Calendar calDayToSearch = (Calendar) startDate.clone();
			calDayToSearch.add(Calendar.DAY_OF_YEAR, i);
			
			Element searchRecord = recordDateSearched(doc, calDayToSearch,clinic.getTimezone());

			for(Provider provider : providerMap.keySet()){
				DayWorkScheduleTransfer dayWorkScheduleTransfer = getDayWorkSchedule(clinic,provider.getProviderNo(), calDayToSearch);
				Element searchedProviderRecord = recordProviderSearched(doc,searchRecord,provider.getProviderNo(),dayWorkScheduleTransfer, providerMap.get(provider));
				if (dayWorkScheduleTransfer == null || dayWorkScheduleTransfer.isHoliday())	continue;
				if (BookingLearningManager.isDaySetToSkip(clinic, provider.getProviderNo(), calDayToSearch, appointmentTypeId)) continue;
				
				List<TimeSlot> providerAppointments = Clinic.getAllowedTimesByType(dayWorkScheduleTransfer, providerMap.get(provider),provider.getProviderNo());
				recordFilterForSearchedProvider(doc,searchedProviderRecord,dayWorkScheduleTransfer,"N/A" , providerAppointments);						

				List<FilterDefinition> filterClassNames = provider.getFilter();
				if(filterClassNames == null || filterClassNames.isEmpty()){
					filterClassNames = clinic.getFilter();
				}
				if (filterClassNames != null){
					for (FilterDefinition className : filterClassNames){
						@SuppressWarnings("unchecked")
						Class<AvailableTimeSlotFilter> filterClass = (Class<AvailableTimeSlotFilter>) Class.forName(className.getFilterClassName());
						logger.debug("filter class null? "+filterClass.getName());
						AvailableTimeSlotFilter filterClassInstance = filterClass.newInstance();
						providerAppointments = filterClassInstance.filterAvailableTimeSlots(clinic,providerNo,provider.getProviderNo(), appointmentTypeId, dayWorkScheduleTransfer, providerAppointments,calDayToSearch,className.getParams());
						recordFilterForSearchedProvider(doc,searchedProviderRecord,dayWorkScheduleTransfer,filterClassInstance.getClass().getSimpleName() , providerAppointments);						
						if(providerAppointments.size() == 0) {
							break;
						}
					}
				}		
				appointments.addAll(providerAppointments);
			}
			if (appointments.size() > clinic.getNumberOfAppointmentOptionsToReturn()){
				break;
			}
		}
		Collections.sort(appointments,TimeSlot.getTimeSlotComparator());
		return appointments;
	}  
 */




	
	
}
