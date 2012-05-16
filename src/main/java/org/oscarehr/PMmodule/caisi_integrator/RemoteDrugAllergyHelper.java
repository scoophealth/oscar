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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedDemographicAllergy;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.MiscUtils;

public class RemoteDrugAllergyHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static ArrayList<String> getAtcCodesFromRemoteDrugs(Integer localDemographicId) {
		ArrayList<String> atcCodes = new ArrayList<String>();

		try {
			List<CachedDemographicDrug> remoteDrugs  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline()){
				   remoteDrugs = CaisiIntegratorManager.getDemographicWs().getLinkedCachedDemographicDrugsByDemographicId(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(e);
			}
			
			if(CaisiIntegratorManager.isIntegratorOffline()){
			   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(localDemographicId);	
			}
			
			for (CachedDemographicDrug remoteDrug : remoteDrugs) {
				if (remoteDrug.getAtc() != null) atcCodes.add(remoteDrug.getAtc());
			}

		} catch (Exception e) {
			logger.error("Error ", e);
		}

		return (atcCodes);
	}

	public static ArrayList<Allergy> getRemoteAllergiesAsAllergyItems(Integer localDemographicId)
	{
		ArrayList<Allergy> results = new ArrayList<Allergy>();

		try {
			
			List<CachedDemographicAllergy> remoteAllergies  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline()){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
					remoteAllergies = CaisiIntegratorManager.getDemographicWs().getLinkedCachedDemographicAllergies(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(e);
			}
				
			if(CaisiIntegratorManager.isIntegratorOffline()){
			   remoteAllergies = IntegratorFallBackManager.getRemoteAllergies(localDemographicId);	
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
