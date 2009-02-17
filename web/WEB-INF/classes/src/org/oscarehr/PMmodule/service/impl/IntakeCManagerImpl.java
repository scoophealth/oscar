/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.IntakeCDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Formintakec;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
		if (StringUtils.isNotBlank(demographicNo)) {
			return intakeDao.getCurrentForm(Integer.valueOf(demographicNo));
		}
		
		return null;
	}

	public void saveNewIntake(Formintakec form) {
		if (form.getDemographicNo() == null || form.getDemographicNo().longValue() == 0) {
			// create demographic
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
			
			if (form.getYearOfBirth().equals("")) {
				form.setYearOfBirth("0001");
			}
			if (form.getMonthOfBirth().equals("")) {
				form.setMonthOfBirth("01");
			}
			if (form.getDayOfBirth().equals("")) {
				form.setDayOfBirth("01");
			}
			
			client.setYearOfBirth(form.getYearOfBirth());
			client.setMonthOfBirth(form.getMonthOfBirth());
			client.setDateOfBirth(form.getDayOfBirth());
			client.setProviderNo(String.valueOf(form.getProviderNo()));
			client.setFamilyDoctor("");
			
			if (form.getRadioGender() != null && form.getRadioGender().length() > 0) {
				int gender = Integer.parseInt(form.getRadioGender());
				switch (gender) {
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
			cal.set(Calendar.YEAR, 2100);
			client.setEffDate(new Date());
			client.setEndDate(cal.getTime());
			client.setHcRenewDate(cal.getTime());

			clientDao.saveClient(client);
			
			form.setDemographicNo(client.getDemographicNo().longValue());
		}
		
		form.setFormEdited(new Date());
		
		intakeDao.saveForm(form);
	}

	public List getCohort(Date beginDate, Date endDate) {
		return intakeDao.getCohort(beginDate, endDate, clientDao.getClients());
	}
	
}