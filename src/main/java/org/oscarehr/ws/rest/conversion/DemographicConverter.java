/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest.conversion;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.util.AgeCalculator;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.AgeTo1;
import org.oscarehr.ws.rest.to.model.DemographicTo1;

public class DemographicConverter extends AbstractConverter<Demographic, DemographicTo1> {
	
	private static Logger logger = Logger.getLogger(DemographicConverter.class);
	
	private DemographicExtConverter demoExtConverter = new DemographicExtConverter();
	private ProviderConverter providerConverter = new ProviderConverter();

	/**
	 * Converts TO, excluding provider and extras.
	 */
	@Override
	public Demographic getAsDomainObject(LoggedInInfo loggedInInfo,DemographicTo1 t) throws ConversionException {
		Demographic d = new Demographic();
		
		d.setDemographicNo(t.getDemographicNo());
		d.setPhone(t.getPhone());
		d.setPatientStatus(t.getPatientStatus());
		d.setPatientStatusDate(t.getPatientStatusDate());
		d.setRosterStatus(t.getRosterStatus());
		d.setProviderNo(t.getProviderNo());
		d.setMyOscarUserName(t.getMyOscarUserName());
		d.setHin(t.getHin());
		d.setAddress(t.getAddress().getAddress());
		d.setProvince(t.getAddress().getProvince());
		d.setVer(t.getVer());
		d.setSex(t.getSex());
		d.setDateOfBirth(t.getDobDay());
		d.setMonthOfBirth(t.getDobMonth());
		d.setYearOfBirth(t.getDobYear());
//		d.setDateOfBirth(ConversionUtils.toDateString(t.getDateOfBirth(), ConversionUtils.DATE_PATTERN_DAY));
//		d.setMonthOfBirth(ConversionUtils.toDateString(t.getDateOfBirth(), ConversionUtils.DATE_PATTERN_MONTH));
//		d.setYearOfBirth(ConversionUtils.toDateString(t.getDateOfBirth(), ConversionUtils.DATE_PATTERN_YEAR));
		d.setSexDesc(t.getSexDesc());
		d.setDateJoined(t.getDateJoined());
		d.setFamilyDoctor(t.getFamilyDoctor());
		d.setCity(t.getAddress().getCity());
		d.setFirstName(t.getFirstName());
		d.setPostal(t.getAddress().getPostal());
		d.setHcRenewDate(t.getHcRenewDate());
		d.setPhone2(t.getAlternativePhone());
		d.setPcnIndicator(t.getPcnIndicator());
		d.setEndDate(t.getEndDate());
		d.setLastName(t.getLastName());
		d.setHcType(t.getHcType());
		d.setChartNo(t.getChartNo());
		d.setEmail(t.getEmail());
		
		d.setEffDate(t.getEffDate());
		d.setRosterDate(t.getRosterDate());
		d.setRosterTerminationDate(t.getRosterTerminationDate());
		d.setRosterTerminationReason(t.getRosterTerminationReason());
		d.setLinks(t.getRosterTerminationReason());
		d.setAlias(t.getAlias());
		d.setPreviousAddress(t.getPreviousAddress().getAddress());
		d.setChildren(t.getChildren());
		d.setSourceOfIncome(t.getSourceOfIncome());
		d.setCitizenship(t.getCitizenship());
		d.setSin(t.getSin());
		d.setAnonymous(t.getAnonymous());
		d.setSpokenLanguage(t.getSpokenLanguage());
		d.setActiveCount(t.getActiveCount());
		d.setHsAlertCount(t.getHsAlertCount());
		d.setTitle(t.getTitle());
		d.setOfficialLanguage(t.getOfficialLanguage());
		d.setCountryOfOrigin(t.getCountryOfOrigin());
		d.setNewsletter(t.getNewsletter());

		DemographicExt[] exts = new DemographicExt[t.getExtras().size()];
		for (int i = 0; i < t.getExtras().size(); i++) {
			exts[i] = demoExtConverter.getAsDomainObject(loggedInInfo,t.getExtras().get(i));
			
			if (exts[i].getDemographicNo()==null) exts[i].setDemographicNo(d.getDemographicNo());
			if (exts[i].getProviderNo()==null) exts[i].setProviderNo(loggedInInfo.getLoggedInProviderNo());
		}
		d.setExtras(exts);

		if (t.getProvider() != null) {
			d.setProvider(providerConverter.getAsDomainObject(loggedInInfo, t.getProvider()));
		}

		return d;
	}

	@Override
	public DemographicTo1 getAsTransferObject(LoggedInInfo loggedInInfo,Demographic d) throws ConversionException {
		DemographicTo1 t = new DemographicTo1();
		
		t.setDemographicNo(d.getDemographicNo());
		t.setPhone(d.getPhone());
		t.setPatientStatus(d.getPatientStatus());
		t.setPatientStatusDate(d.getPatientStatusDate());
		t.setRosterStatus(d.getRosterStatus());
		t.setProviderNo(d.getProviderNo());
		t.setMyOscarUserName(d.getMyOscarUserName());
		t.setHin(d.getHin());
		t.getAddress().setAddress(d.getAddress());
		t.getAddress().setProvince(d.getProvince());
		t.setVer(d.getVer());
		t.setSex(d.getSex());
		try {
			t.setDateOfBirth(d.getBirthDay().getTime());
		} catch (Exception e ) {
			logger.warn("Unable to parse date: " + d.getBirthDayAsString());
		}
		t.setDobYear(d.getYearOfBirth());
		t.setDobMonth(d.getMonthOfBirth());
		t.setDobDay(d.getDateOfBirth());
		t.setSexDesc(d.getSexDesc());
		t.setDateJoined(d.getDateJoined());
		t.setFamilyDoctor(d.getFamilyDoctor());
		t.getAddress().setCity(d.getCity());
		t.setFirstName(d.getFirstName());
		t.getAddress().setPostal(d.getPostal());
		t.setHcRenewDate(d.getHcRenewDate());
		t.setAlternativePhone(d.getPhone2());
		t.setPcnIndicator(d.getPcnIndicator());
		t.setEndDate(d.getEndDate());
		t.setLastName(d.getLastName());
		t.setHcType(d.getHcType());
		t.setChartNo(d.getChartNo());
		t.setEmail(d.getEmail());
		t.setEffDate(d.getEffDate());
		t.setRosterDate(d.getRosterDate());
		t.setRosterTerminationDate(d.getRosterTerminationDate());
		t.setRosterTerminationReason(d.getRosterTerminationReason());
		t.setLinks(d.getRosterTerminationReason());
		t.setAlias(d.getAlias());
		t.getPreviousAddress().setAddress(d.getPreviousAddress());
		t.setChildren(d.getChildren());
		t.setSourceOfIncome(d.getSourceOfIncome());
		t.setCitizenship(d.getCitizenship());
		t.setSin(d.getSin());
		t.setAnonymous(d.getAnonymous());
		t.setSpokenLanguage(d.getSpokenLanguage());
		t.setActiveCount(d.getActiveCount());
		t.setHsAlertCount(d.getHsAlertCount());
		t.setLastUpdateUser(d.getLastUpdateUser());
		t.setLastUpdateDate(d.getLastUpdateDate());
		t.setTitle(d.getTitle());
		t.setOfficialLanguage(d.getOfficialLanguage());
		t.setCountryOfOrigin(d.getCountryOfOrigin());
		t.setNewsletter(d.getNewsletter());

		if (d.getExtras() != null) {
			for (DemographicExt ext : d.getExtras()) {
				t.getExtras().add(demoExtConverter.getAsTransferObject(loggedInInfo,ext));
			}
		}

		if (d.getProvider() != null) {
			t.setProvider(providerConverter.getAsTransferObject(loggedInInfo,d.getProvider()));
		}
		
		t.setAge(new AgeTo1(AgeCalculator.calculateAge(d.getBirthDay())));

		return t;
	}

	
}
