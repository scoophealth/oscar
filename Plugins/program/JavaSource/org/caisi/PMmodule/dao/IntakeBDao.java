

package org.caisi.PMmodule.dao;


import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.model.Formintakeb;

//###############################################################################

public interface IntakeBDao
{
	public Formintakeb getCurrIntakeB(String firstName, String lastName);

	public Formintakeb getCurrIntakeBByDemographicNo(String demographicNo);

	public Formintakeb getIntakeB(String queryStr);

	public List getIntakeBs();

	public List getIntakeBs(String queryStr);  

	public int getNewDemographicNo(Database_Service databaseService, DataSource dataSource);

	public boolean insertNewClientInfoIntoDemographicTable(
			Database_Service databaseService, DataSource dataSource, 
			Formintakeb intakeB, String newDemographicNo);

	public Formintakeb setNewIntakeBObj(Formintakeb intakeB);

	public boolean isClientExistAlready(String firstName, String lastName);
	
	public String getUpdateClientStatusSqlStr(String demographicNo);

	public boolean addIntakeB(Formintakeb intakeB);
	  
	public boolean addNewClientToIntakeB(Formintakeb intakeB, String newDemographicNo, String actionType);

	public boolean updateIntakeB(Formintakeb intakeB);

	public boolean removeIntakeB(String intakeAId);



}

