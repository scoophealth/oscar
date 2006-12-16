package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.Formintakea;


public interface IntakeAManager extends IntakeManager
{
	public Formintakea getCurrIntakeAByDemographicNo(String demographicNo);

	public List getIntakeAs();

	public List getIntakeAByTimePeriod(String startDate, String endDate);
	
	public Formintakea setNewIntakeAObj(Formintakea intakeA);
	
	public boolean addIntakeA(Formintakea intakeA);
	  
	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType);
	
	public void saveNewIntake(Formintakea form);
}
