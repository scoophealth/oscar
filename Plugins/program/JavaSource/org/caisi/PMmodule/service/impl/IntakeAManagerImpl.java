package org.caisi.PMmodule.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ClientDao;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.dao.IntakeADao;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.service.IntakeAManager;
import org.caisi.PMmodule.utility.Utility;

public class IntakeAManagerImpl extends BaseIntakeManager implements IntakeAManager
{
	private static Log log = LogFactory.getLog(IntakeAManagerImpl.class);
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private IntakeADao dao;
	private ClientDao clientDao;
	
	public void setIntakeADao(IntakeADao dao)
	{
		this.dao = dao;
	}
	
	public void setClientDao(ClientDao dao) {
		this.clientDao = dao;
	}
	
	public Formintakea getCurrIntakeA(String firstName, String lastName)
	{
		return dao.getCurrIntakeA(firstName, lastName);
	}

	public Formintakea getCurrIntakeAByDemographicNo(String demographicNo)
	{
		return dao.getCurrIntakeAByDemographicNo(demographicNo);
	}

	public Formintakea getIntakeA(String queryStr)
	{
		return dao.getIntakeA(queryStr);
	}

	public List getIntakeAs()
	{
		return dao.getIntakeAs();
	}

	public List getIntakeAs(String queryStr)  
	{
		return dao.getIntakeAs(queryStr); 
	}

	public int getNewDemographicNo(Database_Service databaseService, DataSource dataSource)
	{
		return dao.getNewDemographicNo(databaseService, dataSource);
	}

	public List getIntakeAByTimePeriod(String startDate, String endDate)
	{
		return dao.getIntakeAByTimePeriod(startDate, endDate);
	}

	public boolean insertNewClientInfoIntoDemographicTable(
			Database_Service databaseService, DataSource dataSource, 
			Formintakea intakeA, String newDemographicNo)
	{
		return dao.insertNewClientInfoIntoDemographicTable(
				databaseService, dataSource, intakeA, newDemographicNo);
	}

	public boolean insertNewClientInfoIntoDemographicPmmTable(
			Database_Service databaseService, DataSource dataSource, long agencyId, 
			Formintakea fa, String newDemographicNo)
	{
		return dao.insertNewClientInfoIntoDemographicPmmTable(
				 databaseService, dataSource, agencyId, fa, newDemographicNo);
	}

	public Formintakea setNewIntakeAObj(Formintakea intakeA)
	{
		return dao.setNewIntakeAObj(intakeA);
	}

	public boolean isClientExistAlready(String firstName, String lastName)
	{
		return dao.isClientExistAlready(firstName, lastName);
	}
	
	public String getUpdateClientStatusSqlStr(String demographicNo)
	{
		return dao.getUpdateClientStatusSqlStr(demographicNo);
	}

	public boolean addIntakeA(Formintakea intakeA)
	{
		return dao.addIntakeA(intakeA);
	}

	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType)
	{
		return dao.addNewClientToIntakeA(intakeA, newDemographicNo, actionType);
	}

	public boolean updateIntakeA(Formintakea intakeA)
	{
		return dao.updateIntakeA(intakeA);
	}

	public boolean removeIntakeA(String intakeAId)
	{
		return dao.removeIntakeA(intakeAId);
	}

	/**
	 * 1) if client not in oscar, add them
	 * 2) save intake form
	 */
	public String saveNewIntakeA(String demographicNo, String providerNo,Formintakea form) {
		Demographic client = null;
		if(demographicNo == null) {
			client = new Demographic();
			client.setFirstName(form.getClientFirstName());
			client.setLastName(form.getClientSurname());
			client.setAddress("");
			client.setChartNo("");
			client.setCity("");
			client.setDateJoined(new Date());
			client.setEmail("");
			client.setPhone("");
			client.setPhone2("");
			client.setPostal("");
			client.setProvince("");
			client.setRosterStatus("");
			client.setPatientStatus("AC");
			client.setHealthCardNum(form.getHealthCardNum());
			client.setHealthCardVersion(form.getHealthCardVer());
			if(form.getYear().equals("")) {
				form.setYear("0001");
			}
			if(form.getMonth().equals("")) {
				form.setMonth("01");
			}
			if(form.getDay().equals("")){
				form.setDay("01");
			}
			client.setYearOfBirth(form.getYear());
			client.setMonthOfBirth(form.getMonth());
			client.setDateOfBirth(form.getDay());
			client.setProviderNo(providerNo);
			client.setFamilyDoctor(form.getDoctorName());
			client.setSex("");
			client.setDateJoined(new Date());
			client.setAddress("");
			client.setCity("");
			client.setEmail("");
			client.setHcType("");
			client.setHin("");
			client.setVer("");
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,2100);
			client.setEffDate(new Date());
			client.setEndDate(cal.getTime());
			client.setHcRenewDate(cal.getTime());
			clientDao.saveClient(client);
		}
		this.addNewClientToIntakeA(form,String.valueOf(client.getDemographicNo()),"insert");
		
		return String.valueOf(client.getDemographicNo());
	}

	public void saveUpdatedIntakeA(Demographic client, String providerNo, Formintakea intakeAForm) {
		intakeAForm.setDemographicNo(new Long(client.getDemographicNo().longValue()));
		this.addIntakeA(intakeAForm);
		
		boolean changed = false;

		if(!Utility.escapeNull(intakeAForm.getYear()).equals("")  &&
			!Utility.escapeNull(intakeAForm.getMonth()).equals("") &&
			Utility.escapeNull(intakeAForm.getDay()).equals("")) {
			
			client.setYearOfBirth(intakeAForm.getYear());
			client.setMonthOfBirth(intakeAForm.getMonth());
			client.setDateOfBirth(intakeAForm.getDay());
			changed=true;
		}
		
		if(!Utility.escapeNull((String)intakeAForm.getHealthCardNum()).equals("") )
		{
			client.setHealthCardNum(intakeAForm.getHealthCardNum());
			client.setHealthCardVersion(intakeAForm.getHealthCardVer());
			changed=true;
		}
		
		if(!Utility.escapeNull((String)intakeAForm.getDoctorName()).equals("") )
		{
			client.setFamilyDoctor(Utility.escapeNull(intakeAForm.getDoctorName()));
			changed=true;
		}
		
		if(changed) {
			clientDao.saveClient(client);
		}
	}
}
