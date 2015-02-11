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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class PrescriptionManager {
	private static Logger logger = MiscUtils.getLogger();

	@Autowired
	private PrescriptionDao prescriptionDao;

	@Autowired
	private DrugDao drugDao;

	@Autowired
	private SecurityInfoManager securityInfoManager;

	public Prescription getPrescription(LoggedInInfo loggedInInfo, Integer prescriptionId) {
		Prescription result = prescriptionDao.find(prescriptionId);

		//--- log action ---
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getPrescription", "id:" + result.getId());

		return (result);
	}

	public List<Prescription> getPrescriptionUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Prescription> results = prescriptionDao.findByUpdateDate(updatedAfterThisDateInclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getPrescriptionUpdatedAfterDate", "updatedAfterThisDateInclusive=" + updatedAfterThisDateInclusive);

		return (results);
	}

	public List<Drug> getDrugsByScriptNo(LoggedInInfo loggedInInfo, Integer scriptNo, Boolean archived) {
		List<Drug> results = drugDao.findByScriptNo(scriptNo, archived);

		//--- log action ---
		if (results.size() > 0) {
			String resultIds = Drug.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getDrugsByScriptNo", "drug ids returned=" + resultIds);
		}

		return (results);
	}

	public List<Drug> getUniqueDrugsByPatient(LoggedInInfo loggedInInfo, Integer demographicNo) {
		List<Drug> results = new ArrayList<Drug>();

		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getUniquePrescriptionsByPatient", "No Read Access");
			return results;
		}

		List<Drug> drugList = drugDao.findByDemographicId(demographicNo);
		Collections.sort(drugList, new Drug.ComparatorIdDesc());

		for (Drug drug : drugList) {

			boolean isCustomName = true;

			for (Drug p : results) {
				if (p.getGcnSeqNo() == drug.getGcnSeqNo()) {
					if (p.getGcnSeqNo() != 0) // not custom - safe GCN
					isCustomName = false;
					else if (p.getCustomName() != null && drug.getCustomName() != null) // custom
					    isCustomName = !p.getCustomName().equals(drug.getCustomName());

				}
			}

			if (isCustomName) {
				logger.info("ADDING PRESCRIPTION " + drug.getId());
				results.add(drug);
			}
		}

		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			try {

				List<CachedDemographicDrug> remoteDrugs = null;
				try {
					if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
						remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(demographicNo);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Unexpected error.", e);
					CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(), e);
				}

				if (CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
					remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, demographicNo);
				}

				logger.debug("remote Drugs : " + remoteDrugs.size());

				for (CachedDemographicDrug remoteDrug : remoteDrugs) {
					Drug drug = new Drug();// new Prescription(, remoteDrug.getCaisiProviderId(), demographicNo)) ;
					drug.setId(remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
					drug.setProviderNo(remoteDrug.getCaisiProviderId());
					drug.setDemographicId(remoteDrug.getCaisiDemographicId());
					drug.setArchived(remoteDrug.isArchived());
					if (remoteDrug.getEndDate() != null) drug.setEndDate(remoteDrug.getEndDate().getTime());
					if (remoteDrug.getRxDate() != null) drug.setRxDate(remoteDrug.getRxDate().getTime());
					drug.setSpecial(remoteDrug.getSpecial());

					// okay so I'm not exactly making it unique... that's the price of last minute conformance test changes.
					results.add(drug);
				}
			} catch (Exception e) {
				logger.error("error getting remote allergies", e);
			}
		}

		if (results.size() > 0) {
			String resultIds = Drug.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getUniquePrescriptionsByPatient", "drug ids returned=" + resultIds);
		}

		return results;
	}

	/**
	 * ProgramId is currently ignored as oscar does not support tracking by program yet.
	 */
	public List<Prescription> getPrescriptionsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateInclusive, int itemsToReturn) {
		List<Prescription> results = prescriptionDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateInclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getPrescriptionsByProgramProviderDemographicDate", "programId=" + programId+", providerNo="+providerNo+", demographicId="+demographicId+", updatedAfterThisDateInclusive="+updatedAfterThisDateInclusive.getTime());

		return (results);
	}

}
