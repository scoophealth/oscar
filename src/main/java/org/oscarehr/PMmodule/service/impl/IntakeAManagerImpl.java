/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.PMmodule.dao.IntakeADao;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IntakeAManagerImpl extends BaseIntakeManager implements IntakeAManager {
	
	private static Logger log = MiscUtils.getLogger();
	
	private IntakeADao dao;
	private DemographicDao demographicDao;
	
	public void setIntakeADao(IntakeADao dao) {
		this.dao = dao;
	}
	
	public void setDemographicDao(DemographicDao dao) {
		this.demographicDao = dao;
	}
	
	public Formintakea getCurrIntakeAByDemographicNo(String demographicNo) {
		if(demographicNo == null || demographicNo.length()==0) {
			return null;
		}
		return dao.getCurrIntakeAByDemographicNo(demographicNo);
	}

	public List getIntakeAs() {
		return dao.getIntakeAs();
	}

	public List getIntakeAByTimePeriod(String startDate, String endDate) {
		return dao.getIntakeAByTimePeriod(startDate, endDate);
	}
	
	public Formintakea setNewIntakeAObj(Formintakea intakeA) {
		return dao.setNewIntakeAObj(intakeA);
	}

	public boolean addIntakeA(Formintakea intakeA) {
		return dao.addIntakeA(intakeA);
	}

	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType) {
		return dao.addNewClientToIntakeA(intakeA, newDemographicNo, actionType);
	}
	
	public void saveNewIntake(Formintakea form) {
		if(form.getDemographicNo() == null || form.getDemographicNo().longValue() == 0) {
			//create demographic
			Demographic client = new Demographic();
			client.setFirstName(form.getClientFirstName());
			client.setLastName(form.getClientSurname());
			client.setDateJoined(new Date());
			client.setPatientStatus("AC");
			if(form.getYear().equals("")) {
				form.setYear(null);
			}
			if(form.getMonth().equals("")) {
				form.setMonth(null);
			}
			if(form.getDay().equals("")){
				form.setDay(null);
			}
			client.setYearOfBirth(form.getYear());
			client.setMonthOfBirth(form.getMonth());
			client.setDateOfBirth(form.getDay());
			client.setProviderNo(String.valueOf(form.getProviderNo()));
			if(form.getRadioSex() != null && form.getRadioSex().length() >0) {
				client.setSex(form.getRadioSex().substring(0,1).toUpperCase());
			} else {
				client.setSex("");
			}
			client.setDateJoined(new Date());
			client.setHin(form.getHealthCardNum());
			client.setVer(form.getHealthCardVer());
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,2100);
			client.setEffDate(new Date());
			client.setEndDate(cal.getTime());
			client.setHcRenewDate(cal.getTime());
			
			demographicDao.saveClient(client);
			form.setDemographicNo(new Long(client.getDemographicNo().longValue()));
		}
		form.setFormEdited(new Date());
		dao.saveForm(form);
	}
}
