package org.oscarehr.common.service.myoscar_data_transformers;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.casemgmt.model.Prescription;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.service.MyOscarMedicalDataManager;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer2;
import org.oscarehr.myoscar_server.ws.MedicalDataType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class PrescriptionMedicationTransformer {
	
	public static Document toXml(Prescription prescription) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Prescription");

		String temp = StringUtils.trimToNull(prescription.getTextView());
		if (temp != null) XmlUtils.appendChildToRoot(doc, "TextVersion", temp);

		temp = StringUtils.trimToNull(prescription.getRxComments());
		if (temp != null) XmlUtils.appendChildToRoot(doc, "Notes", temp);

		return (doc);
	}

	public static MedicalDataTransfer2 toMedicalDataTransfer(PHRAuthentication auth, Prescription prescription) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer2 medicalDataTransfer = MyOscarMedicalDataManager.getEmptyMedicalDataTransfer2(auth, prescription.getDate_prescribed(), prescription.getProviderNo(), Integer.parseInt(prescription.getDemographic_no()));

		Document doc = toXml(prescription);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.PRESCRIPTION.name());

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManager.generateSourceId(loggedInInfo.currentFacility.getName(), "prescription", prescription.getId()));

		return (medicalDataTransfer);
	}

	public static Document toXml(Drug drug) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Medication");
		Node rootNode = doc.getFirstChild();

		XmlUtils.appendChildToRootIgnoreNull(doc, "Type", "PRESCRIPTION");

		// we will assume provider is the observer is the same person
		String prescriberName=MyOscarMedicalDataManager.getObserverOfDataPersonName(drug.getProviderNo());
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

	public static MedicalDataTransfer2 toMedicalDataTransfer(PHRAuthentication auth, Drug drug) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer2 medicalDataTransfer = MyOscarMedicalDataManager.getEmptyMedicalDataTransfer2(auth, drug.getRxDate(), drug.getProviderNo(), drug.getDemographicId());

		Document doc = toXml(drug);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.MEDICATION.name());

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManager.generateSourceId(loggedInInfo.currentFacility.getName(), "drug", drug.getId()));

		return (medicalDataTransfer);
	}

}