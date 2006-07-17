package org.caisi.PMmodule.service;

import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;


public interface IntakeAManager extends IntakeManager
{
	public Formintakea getCurrIntakeA(String firstName, String lastName);

	public Formintakea getCurrIntakeAByDemographicNo(String demographicNo);

	public Formintakea getIntakeA(String queryStr);

	public List getIntakeAs();

	public List getIntakeAs(String queryStr);  

	public int getNewDemographicNo(Database_Service databaseService, DataSource dataSource);

	public List getIntakeAByTimePeriod(String startDate, String endDate);
	
	public boolean insertNewClientInfoIntoDemographicTable(
			Database_Service databaseService, DataSource dataSource, 
			Formintakea intakeA, String newDemographicNo);

	public boolean insertNewClientInfoIntoDemographicPmmTable(
			Database_Service databaseService, DataSource dataSource, long agencyId, 
			Formintakea fa, String newDemographicNo);
	
	public Formintakea setNewIntakeAObj(Formintakea intakeA);
	
	public boolean isClientExistAlready(String firstName, String lastName);

	public String getUpdateClientStatusSqlStr(String demographicNo);

	public boolean addIntakeA(Formintakea intakeA);
	  
	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType);

	public boolean updateIntakeA(Formintakea intakeA);

	public boolean removeIntakeA(String intakeAId);

	public String saveNewIntakeA(String demographicNo, String providerNo, Formintakea intakeAForm);
	
	public void saveUpdatedIntakeA(Demographic demographic, String providerNo, Formintakea intakeAForm);

}
