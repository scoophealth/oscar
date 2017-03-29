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

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.log.LogAction;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxProviderData;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;

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

    public List<Prescription> getPrescriptionUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
        List<Prescription> results = prescriptionDao.findByUpdateDate(updatedAfterThisDateExclusive, itemsToReturn);

        LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getPrescriptionUpdatedAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

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
    public List<Prescription> getPrescriptionsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
        List<Prescription> results = prescriptionDao.findByProviderDemographicLastUpdateDate(providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

        LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getPrescriptionsByProgramProviderDemographicDate", "programId=" + programId + ", providerNo=" + providerNo + ", demographicId=" + demographicId + ", updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive.getTime());

        return (results);
    }

    /**
     * Creates a new prescription object and persists a copy of it in
     * the database. Does not update/change any of the Drug objects that are
     * passed in.
     *
     * @param info          regarding the current user/session.
     * @param drugs         that are associated with this prescription.
     * @param demographicNo the demographic this prescription is for.
     *
     * @return the Prescription object that was created.
     *
     * @throws AccessDeniedException if the user is not allowed to write
     *                               to the demographic's record.
     */
    public Prescription createNewPrescription(LoggedInInfo info, List<Drug> drugs, Integer demographicNo) {

        if (!this.securityInfoManager.hasPrivilege(info, "_rx", "w", demographicNo)) {
            throw new AccessDeniedException("_rx", "w", demographicNo);
        }

        Prescription p = new Prescription();

        p.setDemographicId(demographicNo);
        p.setProviderNo(info.getLoggedInProviderNo());
        p.setDatePrescribed(new Date());
        p.setTextView(this.getPrescriptionTextView(info, demographicNo, drugs));

        // if persist is successful the prescription object p will
        // have the ID assigned to it.
        this.prescriptionDao.persist(p);

        LogAction.addLogSynchronous(info, "PrescriptionManager.createNewPrescription", "prescriptionId=" + p.getId().toString());

        return p;

    }

    /**
     * Gets a textual view of the prescription that can be used to populate
     * the Prescripiton object's textView field.
     *
     * @param info  represents the logged in user
     * @param demo  the current demographic
     * @param drugs a list of drugs to add to the prescription textView
     *
     * @return a string for the text view
     */
    private String getPrescriptionTextView(LoggedInInfo info, Integer demo, List<Drug> drugs) {

        // This code uses legacy code from oscar.oscarRx package.
        // Should refactor to change this. Trying best to limit the
        // scope to this method.
        //
        // TODO: Refactor to not use code from oscar.oscarRx

        RxPatientData.Patient patient = RxPatientData.getPatient(info, demo);
        RxProviderData.Provider provider = (new RxProviderData()).getProvider(info.getLoggedInProviderNo());

        StringBuilder textView = new StringBuilder();

        ProSignatureData sig = new ProSignatureData();

        boolean hasSig = sig.hasSignature(info.getLoggedInProviderNo());

        String doctorName;

        if (hasSig) {
            doctorName = sig.getSignature(info.getLoggedInProviderNo());
        } else {
            doctorName = provider.getFirstName() + ' ' + provider.getSurname();
        }

        textView.append(doctorName + "\n");
        textView.append(provider.getClinicName() + "\n");
        textView.append(provider.getClinicAddress() + "\n");
        textView.append(provider.getClinicCity() + "\n");
        textView.append(provider.getClinicPostal() + "\n");
        textView.append(provider.getClinicPhone() + "\n");
        textView.append(provider.getClinicFax() + "\n");
        textView.append(patient.getFirstName() + " " + patient.getSurname() + "\n");
        textView.append(patient.getAddress() + "\n");
        textView.append(patient.getCity() + " " + patient.getPostal() + "\n");
        textView.append(patient.getPhone() + "\n");
        textView.append(oscar.oscarRx.util.RxUtil.DateToString(
                oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy") + "\n"
        );

        String drugTxt;
        for (Drug d : drugs) {

            drugTxt = d.getFullOutLine();

            if (drugTxt == null || drugTxt.length() < 6) {
                logger.warn("No text representation for drug found: " + drugTxt);
            } else {
                textView.append(drugTxt.replaceAll(";", "\n"));
                textView.append("\n");
            }

        }

        return textView.toString();

    }

	
	public List<Drug> getMedicationsByDemographicNo(LoggedInInfo loggedInInfo, Integer demographicNo, Boolean archived) {
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", demographicNo)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getMedicationsByDemographicNo", "No Read Access");
			return null;
		}
		
		List<Drug> drugList = drugDao.findByDemographicId(demographicNo, archived);
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getMedicationsByDemographicNo Archived=" + archived, drugList.toString());
		return drugList;
	}

	public List<Drug> getActiveMedications(LoggedInInfo loggedInInfo, String demographicNo) {
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", demographicNo)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getActiveMedications", "No Read Access");
			return null;
		}
		
		Integer id = Integer.parseInt(demographicNo.trim());
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getActiveMedications", demographicNo);
		return getActiveMedications(loggedInInfo, id);
	}

	public List<Drug> getActiveMedications(LoggedInInfo loggedInInfo, Integer demographicNo) {
		if (demographicNo == null) {
			return null;
		}
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", demographicNo)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getActiveMedications", "No Read Access");
			return null;
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getActiveMedications", demographicNo+"");
		
		return getMedicationsByDemographicNo(loggedInInfo, demographicNo, false);
	}

	public Drug findDrugById(LoggedInInfo loggedInInfo, Integer drugId) {
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.findDrugById", "No Read Access");
			return null;
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.findDrugById", "searching for drug id: " + drugId);
		return drugDao.find(drugId);
	}
	
	public List<Drug> getLongTermDrugs(LoggedInInfo loggedInInfo, Integer demographicId ) {
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
			LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getLongTermDrugs", "No Read Access");
			return null;
		}
		
		LogAction.addLogSynchronous(loggedInInfo, "PrescriptionManager.getLongtermDrugs", "Demographic: " + demographicId);
		return drugDao.findLongTermDrugsByDemographic(demographicId);
	}

}
