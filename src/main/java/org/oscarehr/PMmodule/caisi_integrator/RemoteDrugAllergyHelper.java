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


package org.oscarehr.PMmodule.caisi_integrator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class RemoteDrugAllergyHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static ArrayList<String> getAtcCodesFromRemoteDrugs(LoggedInInfo loggedInInfo,Integer localDemographicId) {
		ArrayList<String> atcCodes = new ArrayList<String>();

		try {
			List<CachedDemographicDrug> remoteDrugs  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
			
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, localDemographicId);	
			}
			Calendar now = new GregorianCalendar();
			for (CachedDemographicDrug remoteDrug : remoteDrugs) {
				if (remoteDrug.getAtc() != null){
					if(remoteDrug.isLongTerm() ||  (remoteDrug.getEndDate() == null || now.before(remoteDrug.getEndDate()))){
						atcCodes.add(remoteDrug.getAtc());
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error ", e);
		}

		return (atcCodes);
	}

	public static ArrayList<String> getRegionalIdentiferCodesFromRemoteDrugs(LoggedInInfo loggedInInfo,Integer localDemographicId) {
		ArrayList<String> regionalIdentifierCodes = new ArrayList<String>();

		try {
			List<CachedDemographicDrug> remoteDrugs  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
			
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, localDemographicId);	
			}
			Calendar now = new GregorianCalendar();
			for (CachedDemographicDrug remoteDrug : remoteDrugs) {
				if (remoteDrug.getRegionalIdentifier() != null ){
					if(remoteDrug.isLongTerm() ||  (remoteDrug.getEndDate() == null || now.before(remoteDrug.getEndDate()))){
						regionalIdentifierCodes.add(remoteDrug.getRegionalIdentifier());
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error ", e);
		}

		return (regionalIdentifierCodes);
	}
	
	
	public static ArrayList<Allergy> getRemoteAllergiesAsAllergyItems(LoggedInInfo loggedInInfo,Integer localDemographicId)
	{
		ArrayList<Allergy> results = new ArrayList<Allergy>();

		try {
			
			List<CachedDemographicAllergy> remoteAllergies  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
					remoteAllergies = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicAllergies(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
				
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			   remoteAllergies = IntegratorFallBackManager.getRemoteAllergies(loggedInInfo, localDemographicId);	
			} 
			
			for (CachedDemographicAllergy remoteAllergy : remoteAllergies) {
				results.add(convertRemoteAllergyToLocal(remoteAllergy));
			}

		} catch (Exception e) {
			logger.error("Error retriving remote drugs", e);
		}

		return (results);
	}

	/**
	 * These returned allergies aren't real / local allergies, they shouldn't be used for anything other than sending to drug ref for allergy checking.
	 */
	private static Allergy convertRemoteAllergyToLocal(CachedDemographicAllergy remoteAllergy) {

		Date entryDate=null;
		if (remoteAllergy.getEntryDate()!=null) entryDate=remoteAllergy.getEntryDate().getTime();


		Allergy allergy = new Allergy();
		allergy.setDemographicNo(-1);
		allergy.setId(remoteAllergy.getFacilityIdIntegerCompositePk().getCaisiItemId());
		allergy.setDescription( remoteAllergy.getDescription());
		allergy.setHiclSeqno(remoteAllergy.getHiclSeqNo());
		allergy.setHicSeqno(remoteAllergy.getHicSeqNo());
		allergy.setAgcsp(remoteAllergy.getAgcsp());
		allergy.setAgccs(remoteAllergy.getAgccs());
		allergy.setTypeCode(remoteAllergy.getTypeCode());

		return(allergy);
    }
}
