package org.oscarehr.oscarRx;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxProviderData;
import oscar.oscarRx.util.RxUtil;

public class StaticScriptBean {
	private static final Logger logger = MiscUtils.getLogger();
	private static DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");

	public static class DrugDisplayData {
		public static final Comparator<DrugDisplayData> DATE_COMPARATOR = new Comparator<DrugDisplayData>()
		{
			public int compare(DrugDisplayData o1, DrugDisplayData o2) {
				if (o1.dateStartDate.after(o2.dateStartDate)) return(-1);
				else if (o1.dateStartDate.before(o2.dateStartDate)) return(1);
				else 
				{
					if (o1.isLocal) return(-1);
					else return(1);
				}
			}	
		};

		public Integer localDrugId = null;
		public String providerName = null;
		public String startDate = null;
		protected Date dateStartDate=null;
		public String endDate = null;
		public String writtenDate = null;
		public String prescriptionDetails = null;
		public String genericName = null;
		public String customName = null;
		public String brandName = null;
		public boolean isArchived = false;
		public boolean isLocal = true;
	}

	public static ArrayList<DrugDisplayData> getDrugList(int demographicId, String regionalIdentifier, String customName, String brandName) {
		regionalIdentifier=StringUtils.trimToNull(regionalIdentifier);
		customName=StringUtils.trimToNull(customName);
		
		ArrayList<DrugDisplayData> results = new ArrayList<DrugDisplayData>();

		// add local drugs
		List<Drug> drugs = drugDao.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName,brandName);
		for (Drug drug : drugs)
		{
			results.add(getDrugDisplayData(drug));
		}
		
		// add remote drugs
		if (LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled()) {
			try {
				DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
				List<CachedDemographicDrug> remoteDrugs = demographicWs.getLinkedCachedDemographicDrugsByDemographicId(demographicId);
				for (CachedDemographicDrug remoteDrug : remoteDrugs) {
					if (regionalIdentifier != null)
					{
						if (regionalIdentifier.equals(remoteDrug.getRegionalIdentifier())) results.add(getDrugDisplayData(remoteDrug));
					}
					else if (customName != null && !"null".equals(customName) && customName.equals(remoteDrug.getCustomName())) results.add(getDrugDisplayData(remoteDrug));
				}
			} catch (Exception e) {
				logger.error("Unexpected error", e);
			}
		}
		
		Collections.sort(results, DrugDisplayData.DATE_COMPARATOR);
		return (results);
	}

	private static DrugDisplayData getDrugDisplayData(CachedDemographicDrug remoteDrug) throws MalformedURLException {
		DrugDisplayData drugDisplayData = new DrugDisplayData();

		FacilityIdStringCompositePk remoteProviderPk=new FacilityIdStringCompositePk();
		int remoteFacilityId=remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId();
		remoteProviderPk.setIntegratorFacilityId(remoteFacilityId);
		remoteProviderPk.setCaisiItemId(remoteDrug.getCaisiProviderId());
		CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(remoteProviderPk);
		CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(remoteFacilityId);
		drugDisplayData.providerName = cachedProvider.getFirstName() + ' ' + cachedProvider.getLastName()+" @ "+cachedFacility.getName();

		drugDisplayData.startDate = RxUtil.DateToString(remoteDrug.getRxDate());
		drugDisplayData.dateStartDate = remoteDrug.getRxDate();

		drugDisplayData.writtenDate = RxUtil.DateToString(remoteDrug.getCreateDate());
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
		drugDisplayData.dateStartDate = drug.getRxDate();

		drugDisplayData.endDate = RxUtil.DateToString(drug.getEndDate());
		drugDisplayData.writtenDate = RxUtil.DateToString(drug.getWrittenDate());

		drugDisplayData.prescriptionDetails = RxPrescriptionData.getFullOutLine(drug.getSpecial()).replaceAll(";", " ");

		drugDisplayData.genericName = drug.getGenericName();

		drugDisplayData.customName = drug.getCustomName();

		drugDisplayData.brandName = drug.getBrandName();

		drugDisplayData.isArchived = drug.isArchived();

		return (drugDisplayData);
	}
}
