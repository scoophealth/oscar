package org.oscarehr.PMmodule.caisi_integrator;

import java.net.MalformedURLException;
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
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			List<CachedDemographicDrug> remoteDrugs = demographicWs.getLinkedCachedDemographicDrugsByDemographicId(localDemographicId);
			for (CachedDemographicDrug remoteDrug : remoteDrugs) {
				if (remoteDrug.getAtc() != null) atcCodes.add(remoteDrug.getAtc());
			}

		} catch (MalformedURLException e) {
			logger.error("Error retriving remote drugs", e);
		}

		return (atcCodes);
	}

	public static ArrayList<Allergy> getRemoteAllergiesAsAllergyItems(Integer localDemographicId)
	{
		ArrayList<Allergy> results = new ArrayList<Allergy>();

		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			List<CachedDemographicAllergy> remoteAllergies = demographicWs.getLinkedCachedDemographicAllergies(localDemographicId);
			for (CachedDemographicAllergy remoteAllergy : remoteAllergies) {
				results.add(convertRemoteAllergyToLocal(remoteAllergy));
			}

		} catch (MalformedURLException e) {
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
