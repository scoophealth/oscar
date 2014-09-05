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
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxProviderData;
import oscar.oscarRx.util.RxUtil;
import oscar.util.DateUtils;

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
        public boolean nonAuthoritative = false;
        public String pickupDate=null;
        public String pickupTime=null;
        public String eTreatmentType=null;
        public String rxStatus=null;
        public Integer dispenseInterval;
        public Integer refillQuantity;
        public Integer refillDuration;
	}

	public static ArrayList<DrugDisplayData> getDrugList(LoggedInInfo loggedInInfo, int demographicId, String regionalIdentifier, String customName, String brandName) {
		return getDrugList(loggedInInfo, demographicId,regionalIdentifier,customName,brandName,null);
	}
	
	public static ArrayList<DrugDisplayData> getDrugList(LoggedInInfo loggedInInfo, int demographicId, String regionalIdentifier, String customName, String brandName, String atc) {
		regionalIdentifier=StringUtils.trimToNull(regionalIdentifier);
		customName=StringUtils.trimToNull(customName);
		atc = StringUtils.trimToNull(atc);
		
		ArrayList<DrugDisplayData> results = new ArrayList<DrugDisplayData>();

		// add local drugs
		List<Drug> drugs = drugDao.findByDemographicIdSimilarDrugOrderByDate(demographicId, regionalIdentifier, customName,brandName, atc);
		for (Drug drug : drugs)
		{
			results.add(getDrugDisplayData(drug));
		}
		
		// add remote drugs
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			try {
				List<CachedDemographicDrug> remoteDrugs  = null;
				try {
					if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					   remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(demographicId);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Unexpected error.", e);
					CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
				}
				
				if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, demographicId);	
				}
				
				if(remoteDrugs != null){
					for (CachedDemographicDrug remoteDrug : remoteDrugs) {
						if (regionalIdentifier != null)
						{
							if (regionalIdentifier.equals(remoteDrug.getRegionalIdentifier())) results.add(getDrugDisplayData(loggedInInfo,remoteDrug));
						}
						else if (customName != null && !"null".equals(customName) && customName.equals(remoteDrug.getCustomName())) results.add(getDrugDisplayData(loggedInInfo,remoteDrug));
					}
				}
			} catch (Exception e) {
				logger.error("Unexpected error", e);
			}
		}
		
		Collections.sort(results, DrugDisplayData.DATE_COMPARATOR);
		return (results);
	}

	private static DrugDisplayData getDrugDisplayData(LoggedInInfo loggedInInfo, CachedDemographicDrug remoteDrug) throws MalformedURLException {
		DrugDisplayData drugDisplayData = new DrugDisplayData();

		FacilityIdStringCompositePk remoteProviderPk=new FacilityIdStringCompositePk();
		int remoteFacilityId=remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId();
		remoteProviderPk.setIntegratorFacilityId(remoteFacilityId);
		remoteProviderPk.setCaisiItemId(remoteDrug.getCaisiProviderId());
		CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteProviderPk);
		CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),remoteFacilityId);
		drugDisplayData.providerName = cachedProvider.getFirstName() + ' ' + cachedProvider.getLastName()+" @ "+cachedFacility.getName();

		drugDisplayData.startDate = RxUtil.DateToString(remoteDrug.getRxDate());
		drugDisplayData.dateStartDate = DateUtils.toDate(remoteDrug.getRxDate());

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

                drugDisplayData.nonAuthoritative = drug.isNonAuthoritative();

		drugDisplayData.genericName = drug.getGenericName();

		drugDisplayData.customName = drug.getCustomName();

		drugDisplayData.brandName = drug.getBrandName();

		drugDisplayData.isArchived = drug.isArchived();
                
        drugDisplayData.pickupDate = RxUtil.DateToString(drug.getPickUpDateTime(),"yyyy-MM-dd");
                
        drugDisplayData.pickupTime = RxUtil.DateToString(drug.getPickUpDateTime(),"hh:mm aa");
        
        drugDisplayData.eTreatmentType = drug.getETreatmentType();

        drugDisplayData.rxStatus = drug.getRxStatus();
                
        drugDisplayData.dispenseInterval = drug.getDispenseInterval();
        
        drugDisplayData.refillDuration = drug.getRefillDuration();
        
        drugDisplayData.refillQuantity = drug.getRefillQuantity();
        
		return (drugDisplayData);
	}
}
