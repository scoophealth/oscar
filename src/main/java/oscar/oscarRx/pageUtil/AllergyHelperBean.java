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


package oscar.oscarRx.pageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPatientData.Patient;
import oscar.util.DateUtils;

public final class AllergyHelperBean {
	private static Logger logger = MiscUtils.getLogger();
	private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");

	public static List<AllergyDisplay> getAllergiesToDisplay(LoggedInInfo loggedInInfo, Integer demographicId, Locale locale)  {
		ArrayList<AllergyDisplay> results = new ArrayList<AllergyDisplay>();

		addLocalAllergies(loggedInInfo, demographicId, results, locale);

		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			addIntegratorAllergies(loggedInInfo, demographicId, results, locale);
		}

		return (results);
	}

	private static void addLocalAllergies(LoggedInInfo loggedInInfo, Integer demographicId, ArrayList<AllergyDisplay> results, Locale locale)  {
		Patient pt = RxPatientData.getPatient(loggedInInfo, demographicId);
		if(pt == null) {
			return;
		}
		Allergy[] allergies = pt.getActiveAllergies();

		if (allergies == null) return;

		for (Allergy allergy : allergies) {
			AllergyDisplay allergyDisplay = new AllergyDisplay();

			allergyDisplay.setId(allergy.getAllergyId());

			allergyDisplay.setDescription(allergy.getDescription());
			allergyDisplay.setOnSetCode(allergy.getOnsetOfReaction());
			allergyDisplay.setReaction(allergy.getReaction());
			allergyDisplay.setSeverityCode(allergy.getSeverityOfReaction());
			allergyDisplay.setTypeCode(allergy.getTypeCode());
			allergyDisplay.setArchived(allergy.getArchived()?"1":"0");

			String entryDate = partialDateDao.getDatePartial(allergy.getEntryDate(), PartialDate.ALLERGIES, allergy.getAllergyId(), PartialDate.ALLERGIES_ENTRYDATE);
			String startDate = partialDateDao.getDatePartial(allergy.getStartDate(), PartialDate.ALLERGIES, allergy.getAllergyId(), PartialDate.ALLERGIES_STARTDATE);
			allergyDisplay.setEntryDate(entryDate);
			allergyDisplay.setStartDate(startDate);

			results.add(allergyDisplay);
		}
	}

	private static void addIntegratorAllergies(LoggedInInfo loggedInInfo, Integer demographicId, ArrayList<AllergyDisplay> results, Locale locale) {
		try {
			List<CachedDemographicAllergy> remoteAllergies  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					remoteAllergies = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicAllergies(demographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
			
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				remoteAllergies = IntegratorFallBackManager.getRemoteAllergies(loggedInInfo,demographicId);	
			}
			

			for (CachedDemographicAllergy remoteAllergy : remoteAllergies) {
				AllergyDisplay allergyDisplay = new AllergyDisplay();

				allergyDisplay.setRemoteFacilityId(remoteAllergy.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
				allergyDisplay.setId(remoteAllergy.getFacilityIdIntegerCompositePk().getCaisiItemId());

				allergyDisplay.setDescription(remoteAllergy.getDescription());
				allergyDisplay.setEntryDate(DateUtils.formatDate(remoteAllergy.getEntryDate(), locale));
				allergyDisplay.setOnSetCode(remoteAllergy.getOnSetCode());
				allergyDisplay.setReaction(remoteAllergy.getReaction());
				allergyDisplay.setSeverityCode(remoteAllergy.getSeverityCode());
				allergyDisplay.setStartDate(DateUtils.formatDate(remoteAllergy.getStartDate(), locale));
				allergyDisplay.setTypeCode(remoteAllergy.getTypeCode());

				results.add(allergyDisplay);
			}
		} catch (Exception e) {
			logger.error("error getting remote allergies", e);
		}
	}
}
