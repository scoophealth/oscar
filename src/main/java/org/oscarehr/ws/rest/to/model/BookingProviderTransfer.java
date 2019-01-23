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

import org.apache.log4j.Logger;
import org.oscarehr.appointment.search.FilterDefinition;
import org.oscarehr.appointment.search.Provider;

import org.oscarehr.util.MiscUtils;



public class BookingProviderTransfer {
	private static Logger logger = MiscUtils.getLogger();

	private String providerNo = null;
	private String lastName = null;
	private String firstName = null;
	private boolean allowedToBook;
		
	private String messageUserId;
	private List<AllowedAppointmentTypeTransfer> appointmentTypes = new ArrayList<AllowedAppointmentTypeTransfer>();
	private Map<Long, Integer> appointmentDurations = null;
	private List<BookingProviderTransfer> teamMembers = null;
	private List<FilterDefinitionTransfer> filters = null;
	
	private boolean filterFAF =false;
	private boolean filterEAF = false;
	private boolean filterMUF = false;
	private boolean filterOAF = false;
	private boolean filterSCTF = false;
	
	private String role = null;

	private int filterFAFbuffer;

	private String filterOAFCodes;

	
	
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public boolean isAllowedToBook() {
		return allowedToBook;
	}
	public void setAllowedToBook(boolean allowedToBook) {
		this.allowedToBook = allowedToBook;
	}
	public static BookingProviderTransfer fromProvider(Provider provider) {
		BookingProviderTransfer providerTransfer = new BookingProviderTransfer();
		providerTransfer.appointmentDurations = provider.getAppointmentDurations();
		
		for(Map.Entry<String,Character[]> mapEntry : provider.getAppointmentTypes().entrySet()){
			AllowedAppointmentTypeTransfer aatt = new AllowedAppointmentTypeTransfer();
			aatt.setId(Long.valueOf(mapEntry.getKey()));
			aatt.setCodes(mapEntry.getValue());
			if (provider.getAppointmentDurations() != null && provider.getAppointmentDurations().containsKey(aatt.getId())){
				aatt.setDuration(provider.getAppointmentDurations().get(aatt.getId()));
			}
			providerTransfer.appointmentTypes.add(aatt);
		}
		
		providerTransfer.messageUserId = provider.getMessageUserId();
		providerTransfer.providerNo = provider.getProviderNo();
		providerTransfer.teamMembers = new ArrayList<BookingProviderTransfer>();
		if(provider.getTeamMembers() != null){
			for(Provider p: provider.getTeamMembers()){
				providerTransfer.teamMembers.add(BookingProviderTransfer.fromProvider(p));
			}
		}
		
		providerTransfer.filters = new ArrayList<FilterDefinitionTransfer>();
		if(provider.getFilter() != null){
			for(FilterDefinition f: provider.getFilter()){			
				if("org.oscarehr.appointment.search.filters.FutureApptFilter".equals(f.getFilterClassName())){
					providerTransfer.filterFAF =true;
					providerTransfer.filterFAFbuffer = Integer.parseInt(f.getParams().get("buffer"));
				}else if("org.oscarehr.appointment.search.filters.ExistingAppointmentFilter".equals(f.getFilterClassName())){
					providerTransfer.filterEAF = true;
			    }else if("org.oscarehr.appointment.search.filters.MultiUnitFilter".equals(f.getFilterClassName())){
			    	providerTransfer.filterMUF = true;
			    }else if("org.oscarehr.appointment.search.filters.OpenAccessFilter".equals(f.getFilterClassName())){
			    	providerTransfer.filterOAF = true;
			    	providerTransfer.filterOAFCodes = f.getParams().get("codes");
			    }else if("org.oscarehr.appointment.search.filters.SufficientContiguousTimeFilter".equals(f.getFilterClassName())){
			    	providerTransfer.filterSCTF = true;
			    }				
			}
		}
		
		return providerTransfer;
	}
	public String getMessageUserId() {
		return messageUserId;
	}
	public void setMessageUserId(String messageUserId) {
		this.messageUserId = messageUserId;
	}
	public List<AllowedAppointmentTypeTransfer> getAppointmentTypes() {
		return appointmentTypes;
	}
	public void setAppointmentTypes(List<AllowedAppointmentTypeTransfer> appointmentTypes) {
		this.appointmentTypes = appointmentTypes;
	}
	public Map<Long, Integer> getAppointmentDurations() {
		return appointmentDurations;
	}
	public void setAppointmentDurations(Map<Long, Integer> appointmentDurations) {
		this.appointmentDurations = appointmentDurations;
	}
	public List<BookingProviderTransfer> getTeamMembers() {
		return teamMembers;
	}
	public void setTeamMembers(List<BookingProviderTransfer> teamMembers) {
		this.teamMembers = teamMembers;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<FilterDefinitionTransfer> getFilters() {
		return filters;
	}
	public void setFilters(List<FilterDefinitionTransfer> filters) {
		this.filters = filters;
	}
	public boolean isFilterFAF() {
		return filterFAF;
	}
	public void setFilterFAF(boolean filterFAF) {
		this.filterFAF = filterFAF;
	}
	public boolean isFilterEAF() {
		return filterEAF;
	}
	public void setFilterEAF(boolean filterEAF) {
		this.filterEAF = filterEAF;
	}
	public boolean isFilterMUF() {
		return filterMUF;
	}
	public void setFilterMUF(boolean filterMUF) {
		this.filterMUF = filterMUF;
	}
	public boolean isFilterOAF() {
		return filterOAF;
	}
	public void setFilterOAF(boolean filterOAF) {
		this.filterOAF = filterOAF;
	}
	public boolean isFilterSCTF() {
		return filterSCTF;
	}
	public void setFilterSCTF(boolean filterSCTF) {
		this.filterSCTF = filterSCTF;
	}
	public int getFilterFAFbuffer() {
		return filterFAFbuffer;
	}
	public void setFilterFAFbuffer(int filterFAFbuffer) {
		this.filterFAFbuffer = filterFAFbuffer;
	}
	public String getFilterOAFCodes() {
		return filterOAFCodes;
	}
	public void setFilterOAFCodes(String filterOAFCodes) {
		this.filterOAFCodes = filterOAFCodes;
	}
	
}
