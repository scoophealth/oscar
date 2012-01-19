package org.oscarehr.common.service.myoscar;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public final class MeasurementsManager {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String OSCAR_MEASUREMENTS_DATA_TYPE = "MEASUREMENTS";
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");

	public static void sendMeasurementsToMyOscar(PHRAuthentication auth, Integer demographicId) throws ClassCastException {
		// get last synced prescription info

		// get the measurements for the person which are changed since last sync
		// for each measurements
		//    make sure a measurement category exists (or is one of the special pre-defined categories)
		//    send the measurements or update it

		Date startSyncTime = new Date();
		SentToPHRTracking sentToPHRTracking = MyOscarMedicalDataManagerUtils.getExistingOrCreateInitialSentToPHRTracking(demographicId, OSCAR_MEASUREMENTS_DATA_TYPE, MyOscarServerWebServicesManager.getMyOscarServerBaseUrl());
		logger.debug("sendMeasurementsToMyOscar : demographicId=" + demographicId + ", lastSyncTime=" + sentToPHRTracking.getSentDatetime());

//		MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");
//		List<Prescription> changedPrescriptions = measurementsDao.findByDemographicIdUpdatedAfterDate(demographicId, sentToPHRTracking.getSentDatetime());
//		for (Prescription prescription : changedPrescriptions) {
//			logger.debug("sendPrescriptionsMedicationsToMyOscar : prescriptionId=" + prescription.getId());
//
//			MedicalDataTransfer2 medicalDataTransfer = MeasurementsManager.toMedicalDataTransfer(auth, prescription);
//
//			try {
//				Long remotePrescriptionId = MyOscarMedicalDataManagerUtils.addMedicalData(auth, medicalDataTransfer, OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId());
//				linkPrescriptionToMedications(auth, prescription, remotePrescriptionId, remoteMedicationIdMap);
//			} catch (ItemAlreadyExistsException_Exception e) {
//				MyOscarMedicalDataManagerUtils.updateMedicalData(auth, medicalDataTransfer, OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId());
//			}
//		}

		sentToPHRTracking.setSentDatetime(startSyncTime);
		sentToPHRTrackingDao.merge(sentToPHRTracking);
	}

//	private static void sendMeasuremenToMyOscar(PHRAuthentication auth, Integer demographicId, SentToPHRTracking sentToPHRTracking, HashMap<Drug, Long> remoteMedicationIdMap) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, NotAuthorisedException_Exception, NoSuchItemException_Exception, ItemCompletedException_Exception {
//		PrescriptionDao prescriptionDao = (PrescriptionDao) SpringUtils.getBean("prescriptionDao");
//		List<Prescription> changedPrescriptions = prescriptionDao.findByDemographicIdUpdatedAfterDate(demographicId, sentToPHRTracking.getSentDatetime());
//		for (Prescription prescription : changedPrescriptions) {
//			logger.debug("sendPrescriptionsMedicationsToMyOscar : prescriptionId=" + prescription.getId());
//
//			MedicalDataTransfer2 medicalDataTransfer = MeasurementsManager.toMedicalDataTransfer(auth, prescription);
//
//			try {
//				Long remotePrescriptionId = MyOscarMedicalDataManagerUtils.addMedicalData(auth, medicalDataTransfer, OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId());
//				linkPrescriptionToMedications(auth, prescription, remotePrescriptionId, remoteMedicationIdMap);
//			} catch (ItemAlreadyExistsException_Exception e) {
//				MyOscarMedicalDataManagerUtils.updateMedicalData(auth, medicalDataTransfer, OSCAR_PRESCRIPTION_DATA_TYPE, prescription.getId());
//			}
//		}
//	}
//
//	public static Document toXml(Drug drug) throws ParserConfigurationException {
//		Document doc = XmlUtils.newDocument("Medication");
//		Node rootNode = doc.getFirstChild();
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Type", "PRESCRIPTION");
//
//		// we will assume provider is the observer is the same person
//		String prescriberName = MyOscarMedicalDataManagerUtils.getObserverOfDataPersonName(drug.getProviderNo());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "PrescriberName", prescriberName);
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Reason", drug.getComment());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Name", drug.getDrugName());
//
//		if (drug.getGcnSeqNo() != 0) {
//			Element outterCode = doc.createElement("Code");
//			rootNode.appendChild(outterCode);
//
//			Element codingSystem = doc.createElement("CodingSystem");
//			outterCode.appendChild(codingSystem);
//
//			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "GCN_SEQNO");
//
//			XmlUtils.appendChild(doc, outterCode, "Code", String.valueOf(drug.getGcnSeqNo()));
//		}
//
//		if (drug.getAtc() != null) {
//			Element outterCode = doc.createElement("Code");
//			rootNode.appendChild(outterCode);
//
//			Element codingSystem = doc.createElement("CodingSystem");
//			outterCode.appendChild(codingSystem);
//
//			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "ATC");
//
//			XmlUtils.appendChild(doc, outterCode, "Code", drug.getAtc());
//		}
//
//		if (drug.getRegionalIdentifier() != null) {
//			Element outterCode = doc.createElement("Code");
//			rootNode.appendChild(outterCode);
//
//			Element codingSystem = doc.createElement("CodingSystem");
//			outterCode.appendChild(codingSystem);
//
//			XmlUtils.appendChild(doc, codingSystem, "ShortDescription", "RegionalIdentifier");
//
//			XmlUtils.appendChild(doc, outterCode, "Code", drug.getRegionalIdentifier());
//		}
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Dose", drug.getDosage());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Frequency", drug.getFreqCode());
//
//		if (drug.getRoute() != null) {
//			Element outterCode = doc.createElement("Route");
//			rootNode.appendChild(outterCode);
//
//			XmlUtils.appendChild(doc, outterCode, "Code", drug.getRoute());
//		}
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Duration", drug.getDuration());
//
//		if (drug.getDurUnit() != null) {
//			Element outterCode = doc.createElement("DurationUnit");
//			rootNode.appendChild(outterCode);
//
//			XmlUtils.appendChild(doc, outterCode, "Code", drug.getDurUnit());
//		}
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Refills", String.valueOf(drug.getRefillQuantity()));
//
//		if (drug.getEndDate() != null) {
//			Element prescriptionDuration = doc.createElement("PrescriptionDuration");
//			rootNode.appendChild(prescriptionDuration);
//
//			XmlUtils.appendChild(doc, prescriptionDuration, "EndDate", DateFormatUtils.ISO_DATETIME_FORMAT.format(drug.getEndDate()));
//		}
//
//		XmlUtils.appendChildToRootIgnoreNull(doc, "BrandName", drug.getBrandName());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "TakeMin", String.valueOf(drug.getTakeMin()));
//		XmlUtils.appendChildToRootIgnoreNull(doc, "TakeMax", String.valueOf(drug.getTakeMax()));
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Quantity", String.valueOf(drug.getQuantity()));
//		XmlUtils.appendChildToRootIgnoreNull(doc, "GenericName", drug.getGenericName());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "Method", drug.getMethod());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "DrugForm", drug.getDrugForm());
//		XmlUtils.appendChildToRootIgnoreNull(doc, "LongTerm", String.valueOf(drug.getLongTerm()));
//		XmlUtils.appendChildToRootIgnoreNull(doc, "PastMed", String.valueOf(drug.getPastMed()));
//		XmlUtils.appendChildToRootIgnoreNull(doc, "PatientCompliance", String.valueOf(drug.getPatientCompliance()));
//
//		return (doc);
//	}
//
//	public static MedicalDataTransfer2 toMedicalDataTransfer(PHRAuthentication auth, Drug drug) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
//		MedicalDataTransfer2 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer2(auth, drug.getRxDate(), drug.getProviderNo(), drug.getDemographicId());
//
//		Document doc = toXml(drug);
//		medicalDataTransfer.setData(XmlUtils.toString(doc, false));
//
//		medicalDataTransfer.setMedicalDataType(MedicalDataType.MEDICATION.name());
//
//		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
//		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.currentFacility.getName(), "drug", drug.getId()));
//
//		return (medicalDataTransfer);
//	}

}