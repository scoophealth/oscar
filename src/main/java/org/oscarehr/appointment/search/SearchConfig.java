package org.oscarehr.appointment.search;

import java.util.List;
import java.util.Map;









public class SearchConfig {

	private int daysToSearchAheadLimit = 10; //Number of days it searches before giving up. ie search for the next 60 days before giving up
	private int numberOfAppointmentOptionsToReturn  = 10; //Number of appts that seems like it gives a reasonable choice.	
	
	
	
	List<AppointmentType> appointmentTypes = null;
	List<String> debugUsers = null;
	Map<Character, Integer> appointmentCodeDurations = null;
	Map<String, Provider> providers = null;
	String timezone = "America/Toronto";
	int defaultAppointmentCount=1;
	List<FilterDefinition> filters = null;
    private String appointmentLocation = null;
	
	public Map<Provider, Character[]>  getProvidersForAppointmentType(Integer demographicNo, Long appointmentTypeId){
		Map<Provider, Character[]> providerForAppointmentType = null;
		return providerForAppointmentType;
	}
	
	
	public int getDaysToSearchAheadLimit() {
		return daysToSearchAheadLimit;
	}
	public void setDaysToSearchAheadLimit(int daysToSearchAheadLimit) {
		this.daysToSearchAheadLimit = daysToSearchAheadLimit;
	}
	public int getNumberOfAppointmentOptionsToReturn() {
		return numberOfAppointmentOptionsToReturn;
	}
	public void setNumberOfAppointmentOptionsToReturn(int numberOfAppointmentOptionsToReturn) {
		this.numberOfAppointmentOptionsToReturn = numberOfAppointmentOptionsToReturn;
	}
		
	
	public Integer getAppointmentDuration(String mrp, String provider, Long appointmentId, Character code){
		Integer appointmentDuration = appointmentCodeDurations.get(code);
		if(mrp != null && mrp.equals(provider)){
			appointmentDuration = getAppointmentDuration(provider,appointmentId,code);
		} else {
			if (providers.get(mrp) != null){
				Provider p = providers.get(mrp);
				for(Provider teamMember:p.getTeamMembers()){
					if(provider.equals(teamMember.getProviderNo()) && teamMember.appointmentDurations.get(appointmentId) != null){
						appointmentDuration = teamMember.getAppointmentDurations().get(appointmentId);
					}
				}
			}
		}
		return appointmentDuration;
	}
	
	//BUG:doesnot look at team me
	public Integer getAppointmentDuration(String provider, Long appointmentId, Character code){
		Integer appointmentDuration = appointmentCodeDurations.get(code);
		if (providers.get(provider) != null && providers.get(provider).getAppointmentDurations() != null && providers.get(provider).getAppointmentDurations().get(appointmentId) != null){
			appointmentDuration = providers.get(provider).getAppointmentDurations().get(appointmentId);
		}
		return appointmentDuration;
	}
	
	
	public List<FilterDefinition> getFilter(){
		return filters;
	}
}
