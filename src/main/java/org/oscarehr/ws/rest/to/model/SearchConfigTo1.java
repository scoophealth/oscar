package org.oscarehr.ws.rest.to.model;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.oscarehr.appointment.search.Provider;
import org.oscarehr.appointment.search.SearchConfig;


public class SearchConfigTo1 {

	Integer daysToSearchAheadLimit =10;
	Integer numberOfAppointmentOptionsToReturn =10;
	Boolean appointmentBookingOnline ;
	String timezone = "America/Toronto";
	List<BookingProviderTransfer> bookingProviders = new ArrayList<BookingProviderTransfer>();
	List<AppointmentTypeTransfer> bookingAppointmentTypes = new ArrayList<AppointmentTypeTransfer>();
	List<BookingScheduleTemplateCodeTransfer> apptCodes = new ArrayList<BookingScheduleTemplateCodeTransfer>();
	Map<Character, Integer> appointmentCodeDurations = null;
	private List<Character> openAccessList = null;
	Integer numberOfMinutesAdvance;
	private String appointmentLocation = null;
	int defaultAppointmentCount =1;
	private String title ;
	private Integer id;
    /**
	 * @return the defaultAppointmentCount
	 */
	public int getDefaultAppointmentCount() {
		return defaultAppointmentCount;
	}

	/**
	 * @param defaultAppointmentCount the defaultAppointmentCount to set
	 */
	public void setDefaultAppointmentCount(int defaultAppointmentCount) {
		this.defaultAppointmentCount = defaultAppointmentCount;
	}
	
	private static List<String> removeBlanks(List<String> strList){
		List<String> retval = new ArrayList<String>(); 
		if(strList != null){
			for(String str: strList){
				if(!str.equals("")){
					retval.add(str);
				}
			}
		}
		return retval;
	}
	
	public static SearchConfigTo1 fromClinic(SearchConfig clinic) {
		SearchConfigTo1 clinicTransfer = new SearchConfigTo1();
		clinicTransfer.daysToSearchAheadLimit = clinic.getDaysToSearchAheadLimit();
		clinicTransfer.numberOfAppointmentOptionsToReturn = clinic.getNumberOfAppointmentOptionsToReturn();
		clinicTransfer.timezone = clinic.getTimezone();
		clinicTransfer.title = clinic.getTitle();
		
		clinicTransfer.numberOfMinutesAdvance = clinic.getNumberOfMinutesAdvance();
		clinicTransfer.appointmentLocation = clinic.getAppointmentLocation();
		clinicTransfer.defaultAppointmentCount = clinic.getDefaultAppointmentCount();
		Set<String> providerSet = clinic.getProviderNo();
		
		for(String pNumber: providerSet){
			Provider provider = clinic.getProvider(pNumber, pNumber);
			clinicTransfer.bookingProviders.add(BookingProviderTransfer.fromProvider(provider));
			
		}
		
		if(clinic.getAppointmentTypes() != null){
			for(org.oscarehr.appointment.search.AppointmentType appointmentType: clinic.getAppointmentTypes()){
				clinicTransfer.bookingAppointmentTypes.add(AppointmentTypeTransfer.getFromTransfer(appointmentType));
			}
		}
		clinicTransfer.appointmentCodeDurations = clinic.getAppointmentCodeDurations();
		List<Character> openA = new ArrayList<Character>();
		if(clinic.getOpenAccessCodes() != null) {
			for(Character c : clinic.getOpenAccessCodes()) {
				openA.add(c);
			}
		}
		clinicTransfer.openAccessList = openA;
		
		return clinicTransfer;
	}

	public Integer getDaysToSearchAheadLimit() {
		return daysToSearchAheadLimit;
	}

	public void setDaysToSearchAheadLimit(Integer daysToSearchAheadLimit) {
		this.daysToSearchAheadLimit = daysToSearchAheadLimit;
	}

	public Integer getNumberOfAppointmentOptionsToReturn() {
		return numberOfAppointmentOptionsToReturn;
	}

	public void setNumberOfAppointmentOptionsToReturn(
			Integer numberOfAppointmentOptionsToReturn) {
		this.numberOfAppointmentOptionsToReturn = numberOfAppointmentOptionsToReturn;
	}

	public Boolean getAppointmentBookingOnline() {
		return appointmentBookingOnline;
	}
	
	public void setAppointmentBookingOnline(Boolean appointmentBookingOnline) {
		this.appointmentBookingOnline = appointmentBookingOnline;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public List<BookingProviderTransfer> getBookingProviders() {
		return bookingProviders;
	}

	public void setBookingProviders(List<BookingProviderTransfer> bookingProviders) {
		this.bookingProviders = bookingProviders;
	}

	public List<AppointmentTypeTransfer> getBookingAppointmentTypes() {
		return bookingAppointmentTypes;
	}

	public void setBookingAppointmentTypes(
			List<AppointmentTypeTransfer> bookingAppointmentTypes) {
		this.bookingAppointmentTypes = bookingAppointmentTypes;
	}

	public Map<Character, Integer> getAppointmentCodeDurations() {
		return appointmentCodeDurations;
	}

	public void setAppointmentCodeDurations(
			Map<Character, Integer> appointmentCodeDurations) {
		this.appointmentCodeDurations = appointmentCodeDurations;
	}

	public List<BookingScheduleTemplateCodeTransfer> getApptCodes() {
		return apptCodes;
	}

	public void setApptCodes(List<BookingScheduleTemplateCodeTransfer> apptCodes) {
		this.apptCodes = apptCodes;
	}

	public Integer getNumberOfMinutesAdvance() {
		return numberOfMinutesAdvance;
	}

	public void setNumberOfMinutesAdvance(Integer numberOfMinutesAdvance) {
		this.numberOfMinutesAdvance = numberOfMinutesAdvance;
	}

	public String getAppointmentLocation() {
		return appointmentLocation;
	}

	public void setAppointmentLocation(String appointmentLocation) {
		if(appointmentLocation != null){
			this.appointmentLocation = appointmentLocation;//Jsoup.clean(appointmentLocation, Whitelist.none());
		}
	}

	public List<Character> getOpenAccessList() {
		return openAccessList;
	}

	public void setOpenAccessList(List<Character> openAccessList) {
		this.openAccessList = openAccessList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
