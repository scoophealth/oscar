package org.oscarehr.oscarRx;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.client.CachedProvider;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdStringCompositePk;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxProviderData;
import oscar.oscarRx.util.RxUtil;

public class StaticScriptBean {
	private static final Logger logger = LogManager.getLogger(StaticScriptBean.class);
	private static DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");

	public static class DrugDisplayData {
		public Integer localDrugId = null;
		public String providerName = null;
		public String startDate = null;
		public String endDate = null;
		public String prescriptionDetails = null;
		public String genericName = null;
		public String customName = null;
		public String brandName = null;
		public boolean isArchived = false;
		public boolean isLocal = true;
	}

	public static ArrayList<DrugDisplayData> getDrugList(int currentFacilityId, int demographicId, String regionalIdentifier, String customName) {
		regionalIdentifier=StringUtils.trimToNull(regionalIdentifier);
		customName=StringUtils.trimToNull(customName);
		
		TreeMap<Date,DrugDisplayData> results = new TreeMap<Date,DrugDisplayData>();

		// add local drugs
		List<Drug> drugs = drugDao.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName);
		for (Drug drug : drugs)
		{
			results.put(drug.getRxDate(), getDrugDisplayData(drug));
		}
		
		// add remote drugs
		if (caisiIntegratorManager.isIntegratorEnabled(currentFacilityId)) {
			try {
				DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacilityId);
				List<CachedDemographicDrug> remoteDrugs = demographicWs.getLinkedCachedDemographicDrugsByDemographicId(demographicId);
				for (CachedDemographicDrug remoteDrug : remoteDrugs) {
					if (regionalIdentifier != null)
					{
						if (regionalIdentifier.equals(remoteDrug.getRegionalIdentifier())) results.put(remoteDrug.getRxDate(), getDrugDisplayData(currentFacilityId, remoteDrug));
					}
					else if (customName != null && !"null".equals(customName) && customName.equals(remoteDrug.getCustomName())) results.put(remoteDrug.getRxDate(), getDrugDisplayData(currentFacilityId, remoteDrug));
				}
			} catch (Exception e) {
				logger.error("Unexpected error", e);
			}
		}
		
		// lists are not defined as ordered, I'm defining this as ordered.
		ArrayList<DrugDisplayData> values=new ArrayList<DrugDisplayData>(results.values());
		return (values);
	}

	private static DrugDisplayData getDrugDisplayData(int currentFacilityId, CachedDemographicDrug remoteDrug) throws MalformedURLException {
		DrugDisplayData drugDisplayData = new DrugDisplayData();

		FacilityIdStringCompositePk remoteProviderPk=new FacilityIdStringCompositePk();
		remoteProviderPk.setIntegratorFacilityId(remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		remoteProviderPk.setCaisiItemId(remoteDrug.getCaisiProviderId());
		CachedProvider cachedProvider=caisiIntegratorManager.getProvider(currentFacilityId, remoteProviderPk);
		drugDisplayData.providerName = cachedProvider.getFirstName() + ' ' + cachedProvider.getLastName();

		drugDisplayData.startDate = RxUtil.DateToString(remoteDrug.getRxDate());

		drugDisplayData.endDate = RxUtil.DateToString(remoteDrug.getEndDate());

		drugDisplayData.prescriptionDetails = RxPrescriptionData.getFullOutLine(remoteDrug.getSpecial()).replaceAll(";", " ");

		drugDisplayData.genericName = remoteDrug.getGenericName();

		drugDisplayData.customName = remoteDrug.getCustomName();

		drugDisplayData.brandName = remoteDrug.getBrandName();

		drugDisplayData.isArchived = remoteDrug.isArchived();

		drugDisplayData.isLocal = false;

		return (drugDisplayData);
    }

	private static DrugDisplayData getDrugDisplayData(Drug drug) {
		DrugDisplayData drugDisplayData = new DrugDisplayData();

		drugDisplayData.localDrugId = drug.getId();

		RxProviderData.Provider prov = new RxProviderData().getProvider(drug.getProviderNo());
		drugDisplayData.providerName = prov.getFirstName() + ' ' + prov.getSurname();

		drugDisplayData.startDate = RxUtil.DateToString(drug.getRxDate());

		drugDisplayData.endDate = RxUtil.DateToString(drug.getEndDate());

		drugDisplayData.prescriptionDetails = RxPrescriptionData.getFullOutLine(drug.getSpecial()).replaceAll(";", " ");

		drugDisplayData.genericName = drug.getGenericName();

		drugDisplayData.customName = drug.getCustomName();

		drugDisplayData.brandName = drug.getBrandName();

		drugDisplayData.isArchived = drug.isArchived();

		return (drugDisplayData);
	}
}
