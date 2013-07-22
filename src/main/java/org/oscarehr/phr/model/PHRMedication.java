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


package org.oscarehr.phr.model;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.indivo.IndivoException;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.contact.ConciseContactInformationType;
import org.indivo.xml.phr.contact.NameType;
import org.indivo.xml.phr.document.DocumentClassificationType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.IndivoDocument;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.medication.Medication;
import org.indivo.xml.phr.medication.MedicationType;
import org.indivo.xml.phr.medication.RefillType;
import org.indivo.xml.phr.types.AuthorType;
import org.indivo.xml.phr.types.CodedValueType;
import org.indivo.xml.phr.types.CodingSystemReferenceType;
import org.indivo.xml.phr.types.DurationType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.common.model.Drug;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Element;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;

/**
 * @author apavel
 */
public class PHRMedication extends PHRDocument {
	private static Logger logger = MiscUtils.getLogger();

	/** Creates a new instance of PHRMessage */
	public PHRMedication() {
		// super();
	}

	public PHRMedication(IndivoDocumentType doc, Integer demoId, Long receiverMyOscarUserId, String providerNo) throws Exception {
		setReceiverInfo(demoId, receiverMyOscarUserId);
		parseDocument(doc, providerNo);
	}

	public PHRMedication(MedicalDataTransfer4 medicalDataTransfer, Integer demoId, Long receiverMyOscarUserId, String providerNo) {
		setReceiverInfo(demoId, receiverMyOscarUserId);
		parseDocument(medicalDataTransfer, providerNo);
	}

	private MedicationType med;
	private Drug drug;

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug d) {
		drug = d;
	}

	private void setReceiverInfo(Integer demoId, Long receiverMyOscarUserId) {
		this.setReceiverOscar(demoId);
		this.setReceiverMyOscarUserId(receiverMyOscarUserId);
	}

	private void parseDocument(IndivoDocumentType document, String providerNo) throws Exception {
		logger.debug("------------------start parseDocument----------------------");
		JAXBContext docContext = JAXBContext.newInstance("org.indivo.xml.phr.document");
		byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
		String docContent = new String(docContentBytes);

		logger.debug("docContent=" + docContent);

		DocumentHeaderType docHeaderType = document.getDocumentHeader();
		DocumentClassificationType theType = docHeaderType.getDocumentClassification();
		String classification = theType.getClassification();
		String documentIndex = docHeaderType.getDocumentIndex();
		AuthorType author = docHeaderType.getAuthor();
		String phr_id = author.getIndivoId();
		if (docHeaderType.getCreationDateTime() == null) this.setDateSent(null);
		else {
			this.setDateSent(docHeaderType.getCreationDateTime().toGregorianCalendar().getTime());
			logger.debug("Date Created set to " + docHeaderType.getCreationDateTime().toGregorianCalendar().getTime());
		}
		this.setPhrClassification(classification);
		this.setPhrIndex(documentIndex);
		JAXBContext messageContext = JAXBContext.newInstance("org.indivo.xml.phr.medication");
		med = (MedicationType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(document, messageContext.createUnmarshaller());
		createDrugFromPhrMed(providerNo);
		this.setPhrClassification(MedicalDataType.MEDICATION.name());
		this.setReceiverType(PHRDocument.TYPE_DEMOGRAPHIC);
		this.setSenderOscar(null);// outside provider's oscar id is not useful
		this.setSenderType(PHRDocument.TYPE_PROVIDER);
		this.setSenderMyOscarUserId(Long.parseLong(phr_id));
		this.setSent(PHRDocument.STATUS_NOT_SET);// need to change
		this.setDocContent(docContent);
		this.setDateExchanged(new Date());
	}

	private void parseDocument(MedicalDataTransfer4 medicalDataTransfer, String providerNo) {
		logger.debug("------------------start parseDocument----------------------");

		if (medicalDataTransfer.getDateOfData() != null) setDateSent(medicalDataTransfer.getDateOfData().getTime());

		setPhrIndex(medicalDataTransfer.getId().toString());

		setPhrClassification(MedicalDataType.MEDICATION.name());
		setReceiverType(TYPE_DEMOGRAPHIC);
		setSenderOscar(null);// outside provider's oscar id is not useful
		setSenderType(TYPE_PROVIDER);
		setSenderMyOscarUserId(medicalDataTransfer.getObserverOfDataPersonId());
		setSent(STATUS_NOT_SET);// need to change
		setDocContent(medicalDataTransfer.getData());
		setDateExchanged(new Date());
	}

	// sending new meds to PHR
	public PHRMedication(EctProviderData.Provider prov, Integer demographicNo, Long receiverMyOscarUserId, RxPrescriptionData.Prescription drug) throws JAXBException, IndivoException {
		// super();
		IndivoDocumentType document = getPhrMedicationDocument(prov, drug);
		JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
		byte[] docContentBytes = JAXBUtils.marshalToByteArray(new IndivoDocument(document), docContext);
		String docContentStr = new String(docContentBytes);

		this.setPhrClassification(MedicalDataType.MEDICATION.name());
		this.setReceiverOscar(demographicNo);
		this.setReceiverType(PHRDocument.TYPE_DEMOGRAPHIC);
		this.setReceiverMyOscarUserId(receiverMyOscarUserId);
		this.setSenderOscar(prov.getProviderNo());
		this.setSenderType(PHRDocument.TYPE_PROVIDER);
		this.setSenderMyOscarUserId(Long.parseLong(prov.getIndivoId()));
		this.setSent(PHRDocument.STATUS_SEND_PENDING);
		this.setDocContent(docContentStr);
	}

	// when adding a new medication
	private IndivoDocumentType getPhrMedicationDocument(EctProviderData.Provider prov, RxPrescriptionData.Prescription drug) throws JAXBException, IndivoException {
		String providerFullName = prov.getFirstName() + " " + prov.getSurname();
		MedicationType medType = createPhrMedication(prov, drug);
		org.indivo.xml.phr.medication.ObjectFactory medFactory = new org.indivo.xml.phr.medication.ObjectFactory();
		Medication med = medFactory.createMedication(medType);

		Element element = JAXBUtils.marshalToElement(med, JAXBContext.newInstance("org.indivo.xml.phr.medication"));
		IndivoDocumentType document = DocumentGenerator.generateDefaultDocument(prov.getIndivoId(), providerFullName, PHRDocument.PHR_ROLE_PROVIDER, DocumentClassificationUrns.MEDICATION, ContentTypeQNames.MEDICATION, element);
		return document;
	}

	public void initMedication() {
		try {
			JAXBContext docContext = JAXBContext.newInstance("org.indivo.xml.phr.document");
			Unmarshaller unmarshaller = docContext.createUnmarshaller();
			StringReader strr = new StringReader(this.getDocContent());
			JAXBElement docEle = (JAXBElement) unmarshaller.unmarshal(strr);
			IndivoDocumentType doc = (IndivoDocumentType) docEle.getValue();
			JAXBContext medContext = JAXBContext.newInstance("org.indivo.xml.phr.medication");
			try {
				med = (MedicationType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(doc, medContext.createUnmarshaller());
			} catch (IndivoException ex) {
				java.util.logging.Logger.getLogger(PHRMedication.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (JAXBException ex) {
			java.util.logging.Logger.getLogger(PHRMedication.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// create drug from medtype, but lose lots of drug information because set to custom drug
	public void createDrugFromPhrMed(String providerNo) {
		drug = new Drug();
		drug.setHideFromDrugProfile(med.isHideFromDrugProfile());
		drug.setProviderNo(providerNo);
		if (this.getReceiverOscar() != null) drug.setDemographicId(this.getReceiverOscar());
		else drug.setDemographicId(0);
		DurationType dt = med.getPrescriptionDuration();
		if (dt != null) {
			drug.setRxDate(dt.getStartDate().toGregorianCalendar().getTime());
			drug.setEndDate(dt.getEndDate().toGregorianCalendar().getTime());
		} else {
			drug.setRxDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
			drug.setEndDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
		}
		drug.setBrandName(med.getBrandName());
		drug.setFreqCode(med.getFrequency());
		String durUnit = null;
		CodedValueType durationUnit = med.getDurationUnit();
		if (durationUnit != null && durationUnit.getCode() != null) {
			durUnit = durationUnit.getCode();
		}
		drug.setDurUnit(durUnit);
		String duration = med.getDuration();
		if (duration != null) {
			if (durUnit != null) {
				duration = duration.replace(durUnit, "").trim();
			} else {
				drug.setDuration(duration.trim());
			}
		}
		drug.setQuantity(med.getQuantity());
		List<RefillType> rft = med.getRefillHistory();
		RefillType refill = null;
		if (rft != null && rft.size() > 0) {
			refill = rft.get(rft.size() - 1);
		}
		if (refill != null) {
			drug.setLastRefillDate(refill.getFillDate().toGregorianCalendar().getTime());
		} else drug.setLastRefillDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
		if (med.isPrn() != null) drug.setPrn(med.isPrn());
		else drug.setPrn(false);
		drug.setSpecialInstruction(med.getSpecialInstruction());
		if (med.isArchived() != null) drug.setArchived(med.isArchived());
		else drug.setArchived(false);
		drug.setGenericName(med.getGenericName());
		List<CodedValueType> code = med.getCode();
		for (CodedValueType c : code) {
			String cs = c.getCode();
			CodingSystemReferenceType sys = c.getCodingSystem();
			if (sys.getShortDescription().equals(PHRDocument.CODE_ATC)) {
				drug.setAtc(cs);
			} else if (sys.getShortDescription().equals(PHRDocument.CODE_GCN_SEQNO)) {
				drug.setGcnSeqNo(Integer.parseInt(cs));
			} else if (sys.getShortDescription().equals(PHRDocument.CODE_REGIONALIDENTIFIER)) {
				drug.setRegionalIdentifier(cs);
			}
		}
		if (med.getScriptNo() != null) drug.setScriptNo(med.getScriptNo().intValue());
		else drug.setScriptNo(0);
		drug.setMethod(med.getMethod());
		CodedValueType cvt = med.getRoute();
		if (cvt != null) drug.setRoute(cvt.getCode());
		else drug.setRoute("");
		drug.setDrugForm(med.getDrugForm());
		if (med.getCreateDate() != null) drug.setCreateDate(med.getCreateDate().toGregorianCalendar().getTime());
		else drug.setCreateDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
		drug.setUnitName(med.getUnitNameOscar());
		drug.setLongTerm(med.isLongTerm());
		drug.setCustomNote(med.isCustomNote());
		drug.setPastMed(med.isPastMed());
		drug.setPatientCompliance(med.isPatientCompliance());
		if (med.getWrittenDate() != null) drug.setWrittenDate(med.getWrittenDate().toGregorianCalendar().getTime());
		else drug.setWrittenDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
		List<NameType> pnames = med.getProvider().getPersonName();
		NameType nt = new NameType();
		if (pnames != null && pnames.size() > 0) {
			nt = pnames.get(0);// assume every medication has only one provider.
			// drug.setOutsideProviderName(med.getOutsideProviderName());
			drug.setOutsideProviderName(nt.getFirstName() + " " + nt.getLastName());
		}
		// drug.setOutsideProviderOhip(med.getOutsideProviderOhip());//not sending OHIP
		drug.setArchivedReason(med.getArchivedReason());
		if (med.getArchivedDate() != null) drug.setArchivedDate(med.getArchivedDate().toGregorianCalendar().getTime());
		else drug.setArchivedDate(RxUtil.StringToDate("0000-00-00", "yyyy-MM-dd"));
		String dose = med.getDose();
		CodedValueType doseUnit = med.getDoseUnit();
		if (dose != null) {
			if (doseUnit != null) {
				String doUnit = doseUnit.getCode();
				if (doUnit != null) {
					drug.setDosage(dose.replace(doUnit, ""));
					drug.setUnit(doUnit);
				} else {
					drug.setDosage(dose);
				}
			} else {
				drug.setDosage(dose);
			}
		}
		if (med.getTakeMin() != null) drug.setTakeMin(med.getTakeMin());
		else drug.setTakeMin(0);
		if (med.getTakeMax() != null) drug.setTakeMax(med.getTakeMax());
		else drug.setTakeMax(0);
		if (med.isSubstitutionPermitted() != null) drug.setNoSubs(med.isSubstitutionPermitted());
		else drug.setNoSubs(false);
		if (med.getRefills() != null) drug.setRepeat(Integer.parseInt(med.getRefills()));
		else drug.setRepeat(0);
		if (med.isCustomInstructions() != null) drug.setCustomInstructions(med.isCustomInstructions());
		else drug.setCustomInstructions(false);
		drug.setCustomName(med.getCustomName());

		if (med.getInstructions() != null) drug.setSpecial(med.getInstructions().replace("<pre>", "").replace("</pre>", "").trim());
		else drug.setSpecial("");
	}

	private MedicationType createPhrMedication2(EctProviderData.Provider prov, Drug drug) {
		NameType name = new NameType();
		name.setFirstName(prov.getFirstName());
		name.setLastName(prov.getSurname());

		ConciseContactInformationType contactInfo = new ConciseContactInformationType();
		contactInfo.getPersonName().add(name);

		MedicationType medType = new MedicationType();
		medType.setPrescription(true);

		if (drug.isCustomInstructions() == false) {
			medType.setDose(drug.getDosageDisplay() + " " + drug.getUnit());
			CodedValueType unit = new CodedValueType();
			unit.setCode(drug.getUnit());
			medType.setDoseUnit(unit);

			medType.setDuration(drug.getDuration() + " " + drug.getDurUnit());
			unit.setCode(drug.getDurUnit());
			medType.setDurationUnit(unit);
			medType.setRefills(String.valueOf(drug.getRepeat()));
			medType.setSubstitutionPermitted(drug.isNoSubs());
		} else medType.setDose(ResourceBundle.getBundle("oscarResources").getString("Send2Indivo.prescription.Instruction"));

		try {
			GregorianCalendar gc = new GregorianCalendar();
			XMLGregorianCalendar d2;
			if (drug.getWrittenDate() != null) {
				gc.setTime(drug.getWrittenDate());
				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				medType.setWrittenDate(d2);
			}
			if (drug.getCreateDate() != null) {
				gc.setTime(drug.getCreateDate());

				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

				medType.setCreateDate(d2);
			}
			if (drug.getArchivedDate() != null) {
				gc.setTime(drug.getArchivedDate());
				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				medType.setArchivedDate(d2);
			}
			DurationType rxDuration = new DurationType();
			if (drug.getRxDate() != null) {
				gc.setTime(drug.getRxDate());
				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				rxDuration.setStartDate(d2);
			}
			if (drug.getEndDate() != null) {
				gc.setTime(drug.getEndDate());
				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				rxDuration.setEndDate(d2);
			}
			medType.setPrescriptionDuration(rxDuration);
			if (drug.getLastRefillDate() != null) {
				gc.setTime(drug.getLastRefillDate());
				d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				RefillType rt = new RefillType();
				rt.setFillDate(d2);
				medType.getRefillHistory().add(rt);
			}
			medType.setBrandName(drug.getBrandName());

			// GCN_SEQN
			CodedValueType gcncode = new CodedValueType();
			CodingSystemReferenceType gcncsrt = new CodingSystemReferenceType();
			gcncode.setCode(String.valueOf(drug.getGcnSeqNo()));
			gcncsrt.setShortDescription(PHRDocument.CODE_GCN_SEQNO);
			gcncode.setCodingSystem(gcncsrt);
			medType.getCode().add(gcncode);

			// ATC
			CodedValueType atccode = new CodedValueType();
			CodingSystemReferenceType atccsrt = new CodingSystemReferenceType();
			atccode.setCode(drug.getAtc());

			atccsrt.setShortDescription(PHRDocument.CODE_ATC);
			atccode.setCodingSystem(atccsrt);
			medType.getCode().add(atccode);

			// Regional Identifier
			CodedValueType ricode = new CodedValueType();
			CodingSystemReferenceType ricsrt = new CodingSystemReferenceType();
			ricode.setCode(drug.getRegionalIdentifier());
			ricsrt.setShortDescription(PHRDocument.CODE_REGIONALIDENTIFIER);
			ricode.setCodingSystem(ricsrt);
			medType.getCode().add(ricode);
			medType.setCustomName(drug.getCustomName());
			medType.setTakeMin(drug.getTakeMin());
			medType.setTakeMax(drug.getTakeMax());
			medType.setQuantity(drug.getQuantity());
			medType.setPrn(drug.isPrn());
			medType.setSpecialInstruction(drug.getSpecialInstruction());
			medType.setArchived(drug.isArchived());
			medType.setGenericName(drug.getGenericName());
			medType.setScriptNo(BigInteger.valueOf(drug.getScriptNo()));
			medType.setMethod(drug.getMethod());
			medType.setDrugForm(drug.getDrugForm());
			medType.setFrequency(drug.getFreqCode());
			CodedValueType cvt = new CodedValueType();
			cvt.setCode(drug.getRoute());
			CodingSystemReferenceType routecsrt = new CodingSystemReferenceType();
			routecsrt.setServiceLocation("English CA");
			routecsrt.setShortDescription("text");
			cvt.setCodingSystem(routecsrt);
			cvt.setHistoricalValue(null);
			medType.setRoute(cvt);

			medType.setCustomInstructions(drug.isCustomInstructions());
			medType.setCustomNote(drug.isCustomNote());
			medType.setLongTerm(drug.isLongTerm());
			medType.setPastMed(drug.getPastMed());
			medType.setPatientCompliance(drug.getPatientCompliance());
			medType.setOutsideProviderName(drug.getOutsideProviderName());
			medType.setOutsideProviderOhip(drug.getOutsideProviderOhip());
			medType.setArchivedReason(drug.getArchivedReason());
			medType.setHideFromDrugProfile(drug.isHideFromDrugProfile());
			medType.setUnitNameOscar(drug.getUnitName());
			medType.setName(drug.getDrugName());
			medType.setInstructions("<pre>" + drug.getSpecial() + "</pre>");
			medType.setProvider(contactInfo);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return medType;
	}

	private MedicationType createPhrMedication(EctProviderData.Provider prov, RxPrescriptionData.Prescription drug) {
		Drug d = new Drug(drug);
		return createPhrMedication2(prov, d);
		/*
		 * NameType name = new NameType(); name.setFirstName(prov.getFirstName()); name.setLastName(prov.getSurname());
		 * 
		 * ConciseContactInformationType contactInfo = new ConciseContactInformationType(); contactInfo.getPersonName().add(name);
		 * 
		 * MedicationType medType = new MedicationType(); medType.setPrescription(true);
		 * 
		 * if( drug.getCustomInstr() == false ) { medType.setDose(drug.getDosageDisplay() + " " + drug.getUnit()); medType.setDuration(drug.getDuration() + " " + drug.getDurationUnit()); //CodedValueType cvt = new CodedValueType();
		 * //CodingSystemReferenceType csrt = new CodingSystemReferenceType(); //csrt.setServiceLocation(""); //cvt.setCodingSystem(csrt); //cvt.setCode(""); //cvt.setHistoricalValue(drug.getDurationUnit()); //medType.setDurationUnit(cvt);
		 * medType.setRefills(String.valueOf(drug.getRepeat())); medType.setSubstitutionPermitted(drug.getNosubs()); } else medType.setDose(ResourceBundle.getBundle("oscarResources").getString("Send2Indivo.prescription.Instruction"));
		 */
		/*
		 * medType.setWrittenDate(); medType.setBrandName(drug.getBrandName()); medType.setGCN_SEQNO(drug.getGCN_SEQNO()); medType.setCustomName(drug.getCustomName()); medType.setTakeMin(drug.getTakeMinString());
		 * medType.setTakeMax(drug.getTakeMaxString()); medType.setQuantity(drug.getQuantity()); medType.setPrn(drug.getPrn()); medType.setSpecialInstruction(drug.getSpecialInstruction()); medType.setArchived(drug.isArchived());
		 * medType.setGenericName(drug.getGenericName()); medType.setATC(drug.getAtcCode()); medType.setScriptNo(drug.getScript_no()); medType.setRegionalIdentifier(drug.getRegionalIdentifier()); medType.setMethod(drug.getMethod());
		 * medType.setDrugForm(drug.getDrugForm()); medType.setCreateDate(drug.getRxCreatedDate()); medType.setCustomInstructions(drug.getCustomInstr()); medType.setCustomNote(drug.isCustomNote()); medType.setLongTerm(drug.isLongTerm());
		 * medType.setPastMed(drug.isPastMed()); medType.setPatientCompliance(drug); medType.setOutsideProviderName(drug.getOutsideProviderName()); medType.setOutsideProviderOhip(drug.getOutsideProviderOhip());
		 * medType.setArchivedReason(drug.getLastArchReason()); medType.setArchivedDate(drug.getArchivedDate()); medType.setHideFromDrugProfile(drug);
		 *//*
			 * medType.setName(drug.getDrugName()); medType.setInstructions("<pre>" + drug.getSpecial() + "</pre>"); medType.setProvider(contactInfo); return medType;
			 */
	}

	/*
	 * For updating a document on the IndivoServer: IndivoServer --> OSCAR (sends back the latest doc) OSCARmySQLdb --> OSCAR--> IndivoServer (OSCAR sends back the appended version of the doc) try { IndivoDocumentType currentDoc = new IndivoDocumentType();
	 * //the new version of the doc //DocumentHeaderType docHeaderType = currentDoc.getDocumentHeader(); //String docIndex = docHeaderType.getDocumentIndex(); Element documentElement = DocumentUtils.getDocumentAnyElement(currentDoc); //Retrieve current
	 * file record from indivo ReadDocumentResultType readResult = client.readDocument(auth.getToken(), demographicPhrId, oldDrugPhrId); IndivoDocumentType phrDoc = readResult.getIndivoDocument(); DocumentVersionType version =
	 * phrDoc.getDocumentVersion().get(phrDoc.getDocumentVersion().size() - 1);
	 * 
	 * VersionBodyType body = version.getVersionBody(); body.setAny(documentElement); version.setVersionBody(body); client.updateDocument(sessionTicket, recipientId, docIndex, version); } catch(ActionNotPerformedException anpe) {
	 * aMiscUtils.getLogger().error("Error", npe); } catch(IndivoException ie ) { iMiscUtils.getLogger().error("Error", e); }
	 */
}
