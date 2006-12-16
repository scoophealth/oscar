package org.oscarehr.PMmodule.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.IntakeCDao;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Formintakec;
import org.oscarehr.PMmodule.service.IntakeCManager;

public class IntakeCManagerImpl extends BaseIntakeManager implements IntakeCManager {

	private IntakeCDao intakeDao;
	private ClientDao clientDao;
	
	public void setIntakeCDao(IntakeCDao intakeDao) {
		this.intakeDao = intakeDao;
	}
	
	public void setClientDao(ClientDao dao) {
		this.clientDao = dao;
	}
	
	public Formintakec getCurrentForm(String demographicNo) {
		if(demographicNo != null) {
			return intakeDao.getCurrentForm(Integer.valueOf(demographicNo));
		}
		return null;
	}

	public void saveNewIntake(Formintakec form) {
		if(form.getDemographicNo() == null || form.getDemographicNo().longValue() == 0) {
			//create demographic
			Demographic client = new Demographic();
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
			client.setPcnIndicator("");
			client.setPin("");
			if(form.getYearOfBirth().equals("")) {
				form.setYearOfBirth("0001");
			}
			if(form.getMonthOfBirth().equals("")) {
				form.setMonthOfBirth("01");
			}
			if(form.getDayOfBirth().equals("")){
				form.setDayOfBirth("01");
			}
			client.setYearOfBirth(form.getYearOfBirth());
			client.setMonthOfBirth(form.getMonthOfBirth());
			client.setDateOfBirth(form.getDayOfBirth());
			client.setProviderNo(String.valueOf(form.getProviderNo()));
			client.setFamilyDoctor("");
			if(form.getRadioGender() != null && form.getRadioGender().length()>0) {
				int gender = Integer.parseInt(form.getRadioGender());
				switch(gender) {
				case 1:
					client.setSex("F");
					break;
				case 2:
					client.setSex("M");
					break;
				default:
					client.setSex("");
				}
			}			
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
			form.setDemographicNo(new Long(client.getDemographicNo().longValue()));
		}
		form.setFormEdited(new Date());
		intakeDao.saveForm(form);
	}

    public List getCohort(Date BeginDate, Date EndDate) {
    	List clients = clientDao.getClients();
        return intakeDao.getCohort(BeginDate, EndDate,clients);
    }
}
