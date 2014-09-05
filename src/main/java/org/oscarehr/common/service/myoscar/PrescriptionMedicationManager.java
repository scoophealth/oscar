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


package org.oscarehr.common.service.myoscar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar.commons.MedicalDataRelationshipType;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class PrescriptionMedicationManager {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String OSCAR_PRESCRIPTION_DATA_TYPE = "PRESCRIPTION";
	private static final String OSCAR_MEDICATION_DATA_TYPE = "MEDICATION";
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");

	public static void sendPrescriptionsMedicationsToMyOscar(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId) throws ClassCastException {
		// get last synced prescription info

		// get the medications for the person which are changed since last sync
		// for each medication
		// send the medication or update it
		// keep the resultId in map for later relationship linking to prescription i.e. the drugs in the prescription
		//
		// get the persons prescriptions that are changed after last sync
		// for each prescription
		// send the prescription or update it
		// if it was a new prescription, add relationlinks from the previous medications sent results map.

		Date startSyncTime = new Date();
		SentToPHRTracking sentToPHRTracking = MyOscarMedicalDataManagerUtils.getExistingOrCreateInitialSentToPHRTracking(demographicId, OSCAR_PRESCRIPTION_DATA_TYPE, MyOscarLoggedInInfo.getMyOscarServerBaseUrl());
		logger.debug("sendPrescriptionsMedicationsToMyOscar : demographicId=" + demographicId + ", lastSyncTime=" + sentToPHRTracking.getSentDatetime());

		HashMap<Drug, Long> remoteMedicationIdMap = sendMedicationsToMyOscar(loggedInInfo,myOscarLoggedInInfo, demographicId, sentToPHRTracking);
		sendPrescriptionsToMyOscar(loggedInInfo,myOscarLoggedInInfo, demographicId, sentToPHRTracking, remoteMedicationIdMap);

		sentToPHRTracking.setSentDatetime(startSyncTime);
		sentToPHRTrackingDao.merge(sentToPHRTracking);
	}
	

	private static void sendPrescriptionsToMyOscar(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId, SentToPHRTracking sentToPHRTracking, HashMap<Drug, Long> remoteMedicationIdMap) {
		PrescriptionDao prescriptionDao = (PrescriptionDao) SpringUtils.getBean("prescriptionDao");
		List<Prescription> changedPrescriptions = prescriptionDao.findByDemographicIdUpdatedAfterDate(demographicId, sentToPHRTracking.getSentDatetime());
		for (Prescription prescription : changedPrescriptions) {
			logger.debug("sendPrescriptionsMedicationsToMyOscar : prescriptionId=" + prescription.getId());

			try {
				MedicalDataTransfer4 medicalDataTransfer = toMedicalDataTransfer(loggedInInfo,myOscarLoggedInInfo, prescription);
				Long remotePrescriptionId = MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(),myOscarLoggedInInfo, medicalDataTransfer, OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId(), true, true);
				linkPrescriptionToMedications(myOscarLoggedInInfo, prescription, medicalDataTransfer.getOwningPersonId(), remotePrescriptionId, remoteMedicationIdMap);
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}
	}

	private static void linkPrescriptionToMedications(MyOscarLoggedInInfo myOscarLoggedInInfo, Prescription prescription, Long ownerId, Long remotePrescriptionId, HashMap<Drug, Long> remoteMedicationIdMap) {
		for (Entry<Drug, Long> entry : remoteMedicationIdMap.entrySet()) {
			if (prescription.getId().equals(entry.getKey().getScriptNo())) {
				try {
					MyOscarMedicalDataManagerUtils.addMedicalDataRelationship(myOscarLoggedInInfo, ownerId,remotePrescriptionId, entry.getValue(), MedicalDataRelationshipType.PRESCRIPTION_MEDICATION.name());
				} catch (Exception e) {
					logger.error("Error", e);
				}
			}
		}
	}

	private static HashMap<Drug, Long> sendMedicationsToMyOscar(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId, SentToPHRTracking sentToPHRTracking) {
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		List<Drug> changedMedications = drugDao.findByDemographicIdUpdatedAfterDate(demographicId, sentToPHRTracking.getSentDatetime());
		HashMap<Drug, Long> remoteIdMap = new HashMap<Drug, Long>();
		for (Drug drug : changedMedications) {
			logger.debug("sendPrescriptionsMedicationsToMyOscar : drugId=" + drug.getId());

			try {
				MedicalDataTransfer4 medicalDataTransfer = toMedicalDataTransfer(loggedInInfo, myOscarLoggedInInfo, drug);
				Long remoteMedicationId = null;
				
				// something really odd going on here, we're allowed to update prescriptions/medications? not sure why, we're also not tracking it locally and relying on an error from the server???
				try {
					remoteMedicationId = MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEDICATION_DATA_TYPE, drug.getId(), false, true);
				} catch (Exception e) {
					MyOscarMedicalDataManagerUtils.updateMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEDICATION_DATA_TYPE, drug.getId());
					remoteMedicationId=medicalDataTransfer.getId();
				}

				remoteIdMap.put(drug, remoteMedicationId);
			} catch (Exception e) {
				logger.error("Error", e);
			}

		}

		return (remoteIdMap);
	}

	private static Document toXml(Prescription prescription) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Prescription");

		String temp = StringUtils.trimToNull(prescription.getTextView());
		if (temp != null) XmlUtils.appendChildToRootIgnoreNull(doc, "TextVersion", temp);

		temp = StringUtils.trimToNull(prescription.getComments());
		if (temp != null) XmlUtils.appendChildToRootIgnoreNull(doc, "Notes", temp);

		return (doc);
	}

	private static MedicalDataTransfer4 toMedicalDataTransfer(LoggedInInfo loggedInInfo,MyOscarLoggedInInfo myOscarLoggedInInfo, Prescription prescription) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, prescription.getDatePrescribed(), prescription.getProviderNo(), prescription.getDemographicId());
		// don't ask me why but prescription are currently changeable in oscar, therefore, they're never completed.
		medicalDataTransfer.setCompleted(false);

		Document doc = toXml(prescription);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.PRESCRIPTION.name());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId()));

		return (medicalDataTransfer);
	}

	private static Document toXml(Drug drug) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Medication");
		Node rootNode = doc.getFirstChild();

		XmlUtils.appendChildToRootIgnoreNull(doc, "Type", "PRESCRIPTION");

		// we will assume provider is the observer is the same person
		String prescriberName = MyOscarMedicalDataManagerUtils.getObserverOfDataPersonName(drug.getProviderNo());
		XmlUtils.appendChildToRootIgnoreNull(doc, "PrescriberName", prescriberName);

		XmlUtils.appendChildToRootIgnoreNull(doc, "Reason", drug.getComment());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Name", drug.getDrugName());

		if (drug.getGcnSeqNo() != 0) {
			Element outterCode = doc.createElement("Code");
			rootNode.appendChild(outterCode);

			Element codingSystem = doc.createElement("CodingSystem");
			outterCode.appendChild(codingSystem);

			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "GCN_SEQNO");

			XmlUtils.appendChild(doc, outterCode, "Code", String.valueOf(drug.getGcnSeqNo()));
		}

		if (drug.getAtc() != null) {
			Element outterCode = doc.createElement("Code");
			rootNode.appendChild(outterCode);

			Element codingSystem = doc.createElement("CodingSystem");
			outterCode.appendChild(codingSystem);

			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "ATC");

			XmlUtils.appendChild(doc, outterCode, "Code", drug.getAtc());
		}

		if (drug.getRegionalIdentifier() != null) {
			Element outterCode = doc.createElement("Code");
			rootNode.appendChild(outterCode);

			Element codingSystem = doc.createElement("CodingSystem");
			outterCode.appendChild(codingSystem);

			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "RegionalIdentifier");

			XmlUtils.appendChild(doc, outterCode, "Code", drug.getRegionalIdentifier());
		}

		XmlUtils.appendChildToRootIgnoreNull(doc, "Dose", drug.getDosage());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Frequency", drug.getFreqCode());

		if (drug.getRoute() != null) {
			Element outterCode = doc.createElement("Route");
			rootNode.appendChild(outterCode);

			XmlUtils.appendChild(doc, outterCode, "Code", drug.getRoute());
		}

		XmlUtils.appendChildToRootIgnoreNull(doc, "Duration", drug.getDuration());

		if (drug.getDurUnit() != null) {
			Element outterCode = doc.createElement("DurationUnit");
			rootNode.appendChild(outterCode);

			XmlUtils.appendChild(doc, outterCode, "Code", drug.getDurUnit());
		}

		XmlUtils.appendChildToRootIgnoreNull(doc, "Refills", String.valueOf(drug.getRefillQuantity()));

		if (drug.getEndDate() != null) {
			Element prescriptionDuration = doc.createElement("PrescriptionDuration");
			rootNode.appendChild(prescriptionDuration);

			XmlUtils.appendChild(doc, prescriptionDuration, "EndDate", DateFormatUtils.ISO_DATETIME_FORMAT.format(drug.getEndDate()));
		}

		XmlUtils.appendChildToRootIgnoreNull(doc, "BrandName", drug.getBrandName());
		XmlUtils.appendChildToRootIgnoreNull(doc, "TakeMin", String.valueOf(drug.getTakeMin()));
		XmlUtils.appendChildToRootIgnoreNull(doc, "TakeMax", String.valueOf(drug.getTakeMax()));
		XmlUtils.appendChildToRootIgnoreNull(doc, "Quantity", String.valueOf(drug.getQuantity()));
		XmlUtils.appendChildToRootIgnoreNull(doc, "GenericName", drug.getGenericName());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Method", drug.getMethod());
		XmlUtils.appendChildToRootIgnoreNull(doc, "DrugForm", drug.getDrugForm());
		XmlUtils.appendChildToRootIgnoreNull(doc, "LongTerm", String.valueOf(drug.getLongTerm()));
		XmlUtils.appendChildToRootIgnoreNull(doc, "PastMed", String.valueOf(drug.getPastMed()));
		XmlUtils.appendChildToRootIgnoreNull(doc, "PatientCompliance", String.valueOf(drug.getPatientCompliance()));

		return (doc);
	}

	private static MedicalDataTransfer4 toMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Drug drug) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, drug.getRxDate(), drug.getProviderNo(), drug.getDemographicId());

		Document doc = toXml(drug);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.MEDICATION.name());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_MEDICATION_DATA_TYPE, drug.getId()));

		medicalDataTransfer.setActive(!drug.isArchived());

		return (medicalDataTransfer);
	}

}
