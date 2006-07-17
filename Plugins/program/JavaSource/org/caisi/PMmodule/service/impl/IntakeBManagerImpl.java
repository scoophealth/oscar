package org.caisi.PMmodule.service.impl;

import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.dao.IntakeBDao;
import org.caisi.PMmodule.model.Formintakeb;
import org.caisi.PMmodule.service.IntakeBManager;

public class IntakeBManagerImpl implements IntakeBManager
{
//	private static Log log = LogFactory.getLog(ProgramQueueManagerImpl.class);
	private IntakeBDao dao;
	
	public void setIntakeBDao(IntakeBDao dao)
	{
		this.dao = dao;
	}
	
	public Formintakeb getCurrIntakeB(String firstName, String lastName)
	{
		return dao.getCurrIntakeB(firstName, lastName);
	}

	public Formintakeb getCurrIntakeBByDemographicNo(String demographicNo)
	{
		return dao.getCurrIntakeBByDemographicNo(demographicNo);
	}

	public Formintakeb getIntakeB(String queryStr)
	{
		return dao.getIntakeB(queryStr);
	}

	public List getIntakeBs()
	{
		return dao.getIntakeBs();
	}

	public List getIntakeBs(String queryStr)  
	{
		return dao.getIntakeBs(queryStr); 
	}

	public int getNewDemographicNo(Database_Service databaseService, DataSource dataSource)
	{
		return dao.getNewDemographicNo(databaseService, dataSource);
	}

	public boolean insertNewClientInfoIntoDemographicTable(
			Database_Service databaseService, DataSource dataSource, 
			Formintakeb intakeB, String newDemographicNo)
	{
		return dao.insertNewClientInfoIntoDemographicTable(
				databaseService, dataSource, intakeB, newDemographicNo);
	}

	public Formintakeb setNewIntakeBObj(Formintakeb intakeB)
	{
		return dao.setNewIntakeBObj(intakeB);
	}

	public boolean isClientExistAlready(String firstName, String lastName)
	{
		return dao.isClientExistAlready(firstName, lastName);
	}
	

	public String getUpdateClientStatusSqlStr(String demographicNo)
	{
		return dao.getUpdateClientStatusSqlStr(demographicNo);
	}

	public boolean addIntakeB(Formintakeb intakeB)
	{
		return dao.addIntakeB(intakeB);
	}

	public boolean addNewClientToIntakeB(Formintakeb intakeB, String newDemographicNo, String actionType)
	{
		return dao.addNewClientToIntakeB(intakeB, newDemographicNo, actionType);
	}

	public boolean updateIntakeB(Formintakeb intakeB)
	{
		return dao.updateIntakeB(intakeB);
	}

	public boolean removeIntakeB(String intakeAId)
	{
		return dao.removeIntakeB(intakeAId);
	}

}
