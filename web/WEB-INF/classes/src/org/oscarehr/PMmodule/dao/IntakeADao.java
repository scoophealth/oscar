

package org.oscarehr.PMmodule.dao;


import java.util.List;

import org.oscarehr.PMmodule.model.Formintakea;

//###############################################################################

public interface IntakeADao
{
	public Formintakea getCurrIntakeAByDemographicNo(String demographicNo);

	public List getIntakeAs();

	public List getIntakeAByTimePeriod(String startDate, String endDate);
	
	public Formintakea setNewIntakeAObj(Formintakea intakeA);

	public boolean addIntakeA(Formintakea intakeA);
	  
	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType);
	
	public void saveForm(Formintakea intake);

}

