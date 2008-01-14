/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jay Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicExportAction
 *
 *
 * DemographicExportAction2.java
 *
 * Created on July 23, 2007, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import cds.*;
import cdsDt.DateYYYY;
import cdsDt.DateYYYYMM;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
import java.util.zip.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import javax.servlet.jsp.JspFactory;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import oscar.appt.AppointmentDAO;
import oscar.appt.AppointmentDAO.Appointment;
import oscar.appt.ApptStatusData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.*;
import oscar.oscarEncounter.data.EctEChartBean;
import oscar.oscarFax.client.OSCARFAXClient;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarPrevention.*;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPatientData.Patient.Allergy;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie Cheng
 */
public class DemographicExportAction2 extends Action {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String setName = request.getParameter("patientSet");
    
    DemographicSets dsets = new DemographicSets();
    ArrayList list = dsets.getDemographicSet(setName);

    DemographicData d = new DemographicData();

    ArrayList inject = new ArrayList();

    PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();//new PreventionDisplayConfig();         
    ArrayList prevList  = pdc.getPreventions();
    for (int k =0 ; k < prevList.size(); k++){
	Hashtable a = (Hashtable) prevList.get(k);   
	if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
	    inject.add((String) a.get("name"));
	}	     	
    }
    
    pdc = null;
    prevList = null;

    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = null;

    CommonLabTestValues comLab = new CommonLabTestValues();
    PreventionData pd = new PreventionData();
    DemographicExt ext = new DemographicExt();

    String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
    File[] files = null;
    Vector err = new Vector();
    if (tmpDir==null || tmpDir.trim().equals("")) {
	throw new Exception("Temporary Export Directory not set! Check oscar.properties.");
    } else {
	if (tmpDir.charAt(tmpDir.length()-1)!='/') tmpDir = tmpDir + '/';
	
	files = new File[list.size()];
	String data = null;
	for(int i = 0 ; i < list.size(); i++){	
	    String demoNo = (String) list.get(i);
	    if (demoNo==null || demoNo.trim().equals("")) err.add("Error: No Demographic Number");

	    // DEMOGRAPHICS
	    DemographicData.Demographic demographic = d.getDemographic(demoNo);
	    Hashtable demoExt = ext.getAllValuesForDemo(demoNo);

	    cds.OmdCdsDocument omdCdsDoc = cds.OmdCdsDocument.Factory.newInstance();
	    cds.OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
	    cds.PatientRecordDocument.PatientRecord patientRec = omdCds.addNewPatientRecord();
	    cds.DemographicsDocument.Demographics demo = patientRec.addNewDemographics();

	    demo.setUniqueVendorIdSequence(demoNo);
	    
	    cdsDt.PersonNameStandard.LegalName legalName = demo.addNewNames().addNewLegalName();
	    cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
	    cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
	    
	    data = demographic.getFirstName();
	    if (data==null || data.trim().equals("")) err.add("Error: No First Name for Patient "+demoNo);
	    else {
		firstName.setPart(data);
		firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
		firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
	    }
	    
	    data = demographic.getLastName();
	    if (data==null || data.trim().equals("")) err.add("Error: No Last Name for Patient "+demoNo);
	    else {
		lastName.setPart(data);
		lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
		lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
	    }
	    
	    data = demographic.getSex();
	    if (data==null || data.trim().equals("")) err.add("Error: No Gender for Patient "+demoNo);
	    else demo.setGender(cdsDt.Gender.Enum.forString(data));
	    
	    data = demographic.getRosterStatus();
	    if (data==null || data.trim().equals("")) err.add("Error: No Enrollment Status for Patient "+demoNo);
	    else {
		if (data.equals("RO")) data = "1";
		else data = "0";
		demo.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(data));
	    }
	    
	    data = demographic.getPatientStatus();
	    if (data==null || data.trim().equals("")) err.add("Error: No Person Status Code for Patient "+demoNo);
	    else {
		if (data.equals("AC")) data = "A";
		else if (data.equals("DE")) data = "D";
		else data = "I";
		demo.setPersonStatusCode(cdsDt.PersonStatus.Enum.forString(data));
	    }
	    
	    data = demographic.getDob("-");
	    if (data==null || data.trim().equals("")) {
		err.add("Error: No Date Of Birth for Patient "+demoNo);
	    } else {
		if (getCalDate(data)!=null) {
		    demo.addNewDateOfBirth().setFullDate(getCalDate(data));
		} else {
		    err.add("Note: Not exporting invalid Date of Birth for Patient "+demoNo);
		}
	    }
	    data = demographic.getDateJoined();
	    if (data!=null && !data.trim().equals("")) {
		if (getCalDate(data)!=null) {
		    demo.addNewEnrollmentDate().setFullDate(getCalDate(data));
		} else {
		    err.add("Note: Not exporting invalid Enrollment Date for Patient "+demoNo);
		}
	    }
	    data = demographic.getEndDate();
	    if (data!=null && !data.trim().equals("")) {
		if (getCalDate(data)!=null) {
		    demo.addNewEnrollmentTerminationDate().setFullDate(getCalDate(data));
		} else {
		    err.add("Note: Not exporting invalid Enrollment Termination Date for Patient "+demoNo);
		}
	    }
	    data = demographic.getChartNo();
	    if (data!=null && !data.trim().equals("")) demo.setChartNumber(data);
	    
	    data = demographic.getEmail();
	    if (data!=null && !data.trim().equals("")) demo.setEmail(data);
	    
	    data = demographic.getProviderNo();
	    if (data!=null && !data.trim().equals("")) {
		cds.DemographicsDocument.Demographics.PrimaryPhysician pph = demo.addNewPrimaryPhysician();
		cdsDt.PersonNameSimple pphName = pph.addNewName();
		
		ProviderData provd = new ProviderData(data);
		pphName.setFirstName(provd.getFirst_name());
		pphName.setLastName(provd.getLast_name());
		pph.setOHIPPhysicianId(provd.getOhip_no());
	    }
	    
	    if (demographic.getJustHIN()!=null && !demographic.getJustHIN().trim().equals("")) {
		cdsDt.HealthCard healthCard = demo.addNewHealthCard();
		healthCard.setNumber(demographic.getJustHIN());
		healthCard.setProvinceCode(cdsDt.HealthCardProvinceCode.CA_ON);
		if (demographic.getVersionCode()!=null && !demographic.getVersionCode().trim().equals("")) healthCard.setVersion(demographic.getVersionCode());
		if (demographic.getEffDate()!=null && !demographic.getEffDate().trim().equals("")) {
		    if (getCalDate(demographic.getEffDate())!=null) {
			healthCard.setExpirydate(getCalDate(demographic.getEffDate()));
		    } else {
			err.add("Note: Not exporting invalid Health Card Expiry Date for Patient "+demoNo);
		    }
		}
	    }
	    if (demographic.getAddress()!=null && !demographic.getAddress().trim().equals("")) {
		cdsDt.Address addr = demo.addNewAddress();		
		cdsDt.AddressStructured address = addr.addNewStructured();
		
		addr.setAddressType(cdsDt.AddressType.R);
		address.setLine1(demographic.getAddress());		
		if (demographic.getCity()!=null && !demographic.getCity().trim().equals("")) address.setCity(demographic.getCity());
		if (demographic.getProvince()!=null && !demographic.getProvince().trim().equals("")) address.setCountrySubdivisionCode(demographic.getProvince());
		if (demographic.getPostal()!=null && !demographic.getPostal().trim().equals("")) address.addNewPostalZipCode().setPostalCode(demographic.getPostal());
	    }
	    if (demographic.getPhone()!=null && !demographic.getPhone().trim().equals("")) {
		cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
		phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
		phoneResident.setPhoneNumber(demographic.getPhone());
		if (demoExt.get("hPhoneExt")!=null) phoneResident.setExtension((String)demoExt.get("hPhoneExt"));
	    }
	    if (demographic.getPhone2()!=null && !demographic.getPhone2().trim().equals("")) {
		cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
		phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
		phoneWork.setPhoneNumber(demographic.getPhone2());
		if (demoExt.get("wPhoneExt")!=null) phoneWork.setExtension((String)demoExt.get("wPhoneExt"));
	    }
	    if (demoExt.get("demo_cell")!=null) {
		cdsDt.PhoneNumber phoneCell = demo.addNewPhoneNumber();
		phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
		phoneCell.setPhoneNumber((String)demoExt.get("demo_cell"));
	    }
	    demoExt = null;
	    
	    DemographicRelationship demoRel = new DemographicRelationship();
	    ArrayList demoR = demoRel.getDemographicRelationships(demoNo);
	    for (int j=0; j<demoR.size(); j++) {
		Hashtable r = (Hashtable) demoR.get(j);
		data = (String) r.get("demographic_no");
		if (data!=null || !data.trim().equals("")) {
		    DemographicData.Demographic relDemo = d.getDemographic(data);
		    Hashtable relDemoExt = ext.getAllValuesForDemo(data);
		    
		    cds.DemographicsDocument.Demographics.Contact contact = demo.addNewContact();
		    cdsDt.PersonNameSimpleWithMiddleName contactName = contact.addNewName();
		    contactName.setFirstName(relDemo.getFirstName());
		    contactName.setLastName(relDemo.getLastName());
		    
		    String ec = (String) r.get("emergency_contact");
		    String nk = (String) r.get("sub_decision_maker");
		    String rel = (String) r.get("relation");
		    
		    if (ec.equals("1")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.EC);
		    else if (data.equals("1")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.NK);
		    else if (rel.equals("Administrative Staff")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.AS);
		    else if (rel.equals("Care Giver")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.CG);
		    else if (rel.equals("Power of Attorney")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.PA);
		    else if (rel.equals("Insurance")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.IN);
		    else if (rel.equals("Guarantor")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.GT);
		    else contact.setContactPurpose(cdsDt.ContactPersonPurpose.NK);
		    
		    data = relDemo.getEmail();
		    if (data!=null && !data.trim().equals("")) contact.setEmailAddress(data);
		    data = (String) r.get("notes");
		    if (data!=null && !data.trim().equals("")) contact.setNote(data);
		    
		    if (relDemo.getPhone()!=null && !relDemo.getPhone().trim().equals("")) {
			cdsDt.PhoneNumber phoneRes = contact.addNewPhoneNumber();
			phoneRes.setPhoneNumberType(cdsDt.PhoneNumberType.R);
			phoneRes.setPhoneNumber(relDemo.getPhone());
			if (relDemoExt.get("hPhoneExt")!=null) phoneRes.setExtension((String)relDemoExt.get("hPhoneExt"));
		    }
		    if (relDemo.getPhone2()!=null && !relDemo.getPhone2().trim().equals("")) {
			cdsDt.PhoneNumber phoneW = contact.addNewPhoneNumber();
			phoneW.setPhoneNumberType(cdsDt.PhoneNumberType.W);
			phoneW.setPhoneNumber(relDemo.getPhone2());
			if (relDemoExt.get("wPhoneExt")!=null) phoneW.setExtension((String)relDemoExt.get("wPhoneExt"));
		    }
		}
	    }
	    
	    EctEChartBean bean = new EctEChartBean();
	    bean.setEChartBean(demoNo);
	    Vector dataPart = null;
	    String dataIn = null;

	    // PERSONAL HISTORY
	    data = bean.socialHistory;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    patientRec.addNewPersonalHistory().setCategorySummaryLine(dataIn);
		}
	    }
	    // FAMILY HISTORY
	    data = bean.familyHistory;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    patientRec.addNewFamilyHistory().setCategorySummaryLine(dataIn);
		}
	    }
	    // PAST HEALTH
	    data = bean.medicalHistory;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    patientRec.addNewPastHealth().setCategorySummaryLine(dataIn);
		}
	    }
	    // RISK FACTORS
	    data = bean.reminders;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    patientRec.addNewRiskFactors().setCategorySummaryLine(dataIn);
		}
	    }
	    // PROBLEM LIST
	    data = bean.ongoingConcerns;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    cds.ProblemListDocument.ProblemList pList = patientRec.addNewProblemList();
		    pList.setCategorySummaryLine(dataIn);
		    
		    // extract Onset Date from Summary Line
		    dataIn = dataIn.toLowerCase();
		    int date_index = dataIn.indexOf("onset date:");
		    if (date_index!=-1) {
			date_index = date_index + "onset date:".length();
			int date_end = dataIn.indexOf("\n", date_index);
			if (date_end>date_index) {
			    String onset_date = dataIn.substring(date_index, date_end).trim();
			    if (!onset_date.equals("")) {
				cdsDt.DateFullOrPartial onsetDate = pList.addNewOnsetDate();
				if (getCalDate(onset_date)!=null) {
				    onsetDate.setFullDate(getCalDate(onset_date));
				} else date_index = -1;

			    } else date_index = -1;
			} else date_index = -1;
		    }
		    if (date_index==-1) {
			err.add("Error: Missing Onset Date (Problem List) for Patient "+demoNo);
		    }
		}
	    }
	    // CLINCAL NOTES
	    data = bean.encounter;
	    if (!data.equals("")) {
		dataPart = extract(data, "\n===\r");
		for (int j=0; j<dataPart.size(); j++) {
		    dataIn = (String) dataPart.get(j);
		    patientRec.addNewClinicalNotes().setMyClinicalNotesContent(dataIn);
		}
		
/* Non-mandatory fields; not match in case of multiple Notes; disabled export here to avoid confusions.
		if (getCalDate(bean.eChartTimeStamp)!=null) {
		    c_notes.addNewEnteredDateTime().setDateTime(getCalDate(bean.eChartTimeStamp));
		}
		
		data = bean.providerNo;
		if (!data.equals("")) {
		    cds.ClinicalNotesDocument.ClinicalNotes.PrincipalAuthor pAuthor = c_notes.addNewPrincipalAuthor();
		    ProviderData provd = new ProviderData(data);
		    cdsDt.PersonNameSimple authorName = pAuthor.addNewName();
		    authorName.setFirstName(provd.getFirst_name());
		    authorName.setLastName(provd.getLast_name());
		    pAuthor.setOHIPPhysicianId(provd.getOhip_no());
		}
 */
	    }	    
	    
	    // ALLERGIES & ADVERSE REACTIONS
	    Allergy[] allergies = new RxPatientData().getPatient(demoNo).getAllergies();
	    for (int j=0; j<allergies.length; j++) {
		cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions alr = patientRec.addNewAllergiesAndAdverseReactions();
		String aSummary = "";
		
		data = allergies[j].getAllergy().getDESCRIPTION();
		if (data!=null || !data.trim().equals("")) {
		    alr.setOffendingAgentDescription(data);
		    aSummary = "Offending Agent Description: " + data;
		}
		data = String.valueOf(allergies[j].getAllergy().getPickID());
		if (data!=null && !data.trim().equals("")) {
		    cdsDt.Code drugCode = alr.addNewCode();
		    drugCode.setCodingSystem("Drug Identification Number");
		    drugCode.setValue(data);
		    aSummary += aSummary.equals("") ? "DIN: "+data : "\nDIN: "+data;
		}
		data = String.valueOf(allergies[j].getAllergy().getTYPECODE());
		if (data!=null && !data.trim().equals("")) {
		    if (data.equals("13")) alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.DR);
		    else if (data.equals("2")) alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.UK);
		    else alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.ND);
		    data = "Property of Offending Agent: "+alr.getPropertyOfOffendingAgent().toString();
		    aSummary += aSummary.equals("") ? data : "\n"+data;
		}
		data = allergies[j].getAllergy().getReaction();
		if (data!=null && !data.trim().equals("")) {
		    alr.setReaction(data);
		    aSummary += aSummary.equals("") ? "Reaction: "+data : "\nReaction: "+data;
		}
		data = allergies[j].getAllergy().getSeverityOfReaction();
		if (data!=null && !data.trim().equals("")) {
		    if (data.equals("1")) alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
		    else if (data.equals("2")) alr.setSeverity(cdsDt.AdverseReactionSeverity.MO);
		    else if (data.equals("3")) alr.setSeverity(cdsDt.AdverseReactionSeverity.LT);
		    else { //SeverityOfReaction==0
			alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
			err.add("Note: Severity Of Allergy Reaction [Unknown] exported as [Mild] for Patient "+demoNo);
		    }
		    data = "Adverse Reaction Severity: "+alr.getSeverity().toString();
		    aSummary += aSummary.equals("") ? data : "\n"+data;
		}
		if (allergies[j].getEntryDate()!=null) {
		    if (getCalDate(allergies[j].getEntryDate())!=null) {
			alr.addNewRecordedDate().setFullDate(getCalDate(allergies[j].getEntryDate()));
			data = "Recorded Date: " + UtilDateUtilities.DateToString(allergies[j].getEntryDate(), "yyyy-MM-dd");
			aSummary += aSummary.equals("") ? data : "\n"+data;
		    } else {
			err.add("Note: Not exporting invalid Recorded Date (Allergies) for Patient "+demoNo);
		    }
		}
		if (aSummary.trim().equals("")) err.add("Error: No Category Summary Line (Allergies & Adverse Reactions) for Patient "+demoNo);
		else alr.setCategorySummaryLine(aSummary);
	    }

	    // IMMUNIZATIONS
	    ArrayList prevList2 = pd.getPreventionData(demoNo);
	    for (int k =0 ; k < prevList2.size(); k++){
		Hashtable a = (Hashtable) prevList2.get(k);  
		if (a != null && inject.contains((String) a.get("type")) ){
		    cds.ImmunizationsDocument.Immunizations immu = patientRec.addNewImmunizations();
		    data = (String) a.get("type");
		    if (data==null || data.trim().equals("")) err.add("Error: No Immunization Name for Patient "+demoNo);
		    else immu.setImmunizationName(data);
		    
		    data = (String) a.get("refused");
		    if (data==null || data.trim().equals("")) err.add("Error: No Refused Flag for Patient "+demoNo);
		    else immu.addNewRefusedFlag().setBoolean(convert10toboolean(data));
		    
		    Hashtable extraData = pd.getPreventionById((String) a.get("id"));
		    data = (String) extraData.get("summary");
		    if (data==null || data.trim().equals("")) err.add("Error: No Category Summary Line (Immunization) for Patient "+demoNo);
		    else immu.setCategorySummaryLine(data);
		    
		    data = (String) extraData.get("manufacture");
		    if (data!=null && !data.trim().equals("")) immu.setManufacturer(data);
		    data = (String) extraData.get("lot");
		    if (data!=null && !data.trim().equals("")) immu.setLotNumber(data);
		    data = (String) extraData.get("route");
		    if (data!=null && !data.trim().equals("")) immu.setRoute(data);
		    data = (String) extraData.get("location");
		    if (data!=null && !data.trim().equals("")) immu.setSite(data);
		    data = (String) extraData.get("comments");
		    if (data!=null && !data.trim().equals("")) immu.setNotes((String)extraData.get("comments"));
		    
		    data = (String) a.get("prevention_date");
		    if (data!=null && !data.trim().equals("")) {
			if (getCalDate((String)a.get("prevention_date"))!=null) {
			    immu.addNewDate().setFullDate(getCalDate((String)a.get("prevention_date")));
			} else {
			    err.add("Note: Not exporting invalid Immunization Date for Patient "+demoNo);
			}
		    }
		    extraData = null;
		}                                                       
		a = null;
	    }
	    prevList2 = null;

	    // MEDICATIONS & TREATMENTS
	    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
	    for (int p = 0; p < arr.length; p++){
		cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
		String mSummary = "";
		
		if (arr[p].getRxCreatedDate()!=null) {
		    if (getCalDate(arr[p].getRxCreatedDate())!=null) {
			medi.addNewPrescriptionWrittenDate().setFullDate(getCalDate(arr[p].getRxCreatedDate()));
			mSummary = "Prescription Written Date: " + UtilDateUtilities.DateToString(arr[p].getRxCreatedDate(), "yyyy-MM-dd");
		    } else {
			err.add("Note: Not exporting invalid Prescription Written Date for Patient "+demoNo);
		    }
		}
		if (arr[p].getRxDate()!=null) {
		    if (getCalDate(arr[p].getRxDate())!=null) {
			medi.addNewStartDate().setFullDate(getCalDate(arr[p].getRxDate()));
			data = "Start Date: " + UtilDateUtilities.DateToString(arr[p].getRxDate(), "yyyy-MM-dd");
			mSummary += mSummary.equals("") ? data : "\n"+data;
		    } else {
			err.add("Note: Not exporting invalid Medication Start Date for Patient "+demoNo);
		    }
		}
		if (arr[p].getEndDate()!=null) {
		    if (getCalDate(arr[p].getEndDate())!=null) {
			medi.addNewEndDate().setFullDate(getCalDate(arr[p].getEndDate()));
			data = "End Date: " + UtilDateUtilities.DateToString(arr[p].getEndDate(), "yyyy-MM-dd");
			mSummary += mSummary.equals("") ? data : "\n"+data;
		    } else {
			err.add("Note: Not exporting invalid Medication End Date for Patient "+demoNo);
		    }
		}
		data = arr[p].getRegionalIdentifier();
		if (data!=null || !data.trim().equals("")) {
		    medi.setDrugIdentificationNumber(data);
		    mSummary += mSummary.equals("") ? "DIN: "+data : "\nDIN: "+data;
		}
		data = arr[p].getDrugName();
		if (data==null || data.trim().equals("")) err.add("Error: No Drug Name for Patient "+demoNo);
		else {
		    medi.setDrugName(data);
		    mSummary += mSummary.equals("") ? "Drug Name: "+data : "\nDrug Name: "+data;
		}
		
		if (arr[p].getDosage()!=null) {
		    cdsDt.DrugMeasure drugM = medi.addNewStrength();
		    drugM.setAmount(arr[p].getDosage());
		    data = "Strength: " + arr[p].getDosage();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		    
		    if (arr[p].getUnit()!=null) {
			drugM.setUnitOfMeasure(arr[p].getUnit());
			data = "Unit: " + arr[p].getUnit();
			mSummary += mSummary.equals("") ? data : "\n"+data;
		    }
		    else drugM.setUnitOfMeasure("");
		}
		if (medi.getDrugName()!=null || medi.getDrugIdentificationNumber()!=null) {
		    medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
		    data = "Number of Refills: " + arr[p].getRepeat();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		if (arr[p].getRoute()!=null) {
		    medi.setRoute(arr[p].getRoute());
		    data = "Route: " + arr[p].getRoute();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		if (arr[p].getFreqDisplay()!=null) {
		    medi.setFrequency(arr[p].getFreqDisplay());
		    data = "Frequency: " + arr[p].getFreqDisplay();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		if (arr[p].getDuration()!=null) {
		    medi.setDuration(arr[p].getDuration());
		    data = "Duration: " + arr[p].getDuration();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		if (arr[p].getQuantity()!=null) {
		    medi.setQuantity(arr[p].getQuantity());
		    data = "Quantity: " + arr[p].getQuantity();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		data = arr[p].getProviderNo();
		if (data!=null && !data.trim().equals("")) {
		    cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
		    cdsDt.PersonNameSimple pcbName = pcb.addNewName();
		    ProviderData provd = new ProviderData(data);
		    pcbName.setFirstName(provd.getFirst_name());
		    pcbName.setLastName(provd.getLast_name());
		    pcb.setOHIPPhysicianId(provd.getOhip_no());
		    data = "Prescribed by: " + provd.getFirst_name() + " " + provd.getLast_name();
		    mSummary += mSummary.equals("") ? data : "\n"+data;
		}
		data = arr[p].getSpecial();
		if (data!=null || !data.trim().equals("")) {
		    medi.setPrescriptionInstructions(data);
		    mSummary += mSummary.equals("") ? "Prescription Instructions: "+data : "\nPrescription Instructions: "+data;
		}

		if (mSummary.trim().equals("")) err.add("Error: No Category Summary Line (Medications & Treatments) for Patient "+demoNo);
		else medi.setCategorySummaryLine(mSummary);
	    }
	    arr = null;

	    // LABORATORY RESULTS
	    ArrayList labs = comLab.findValuesForDemographic(demoNo);
	    for (int l = 0 ; l < labs.size(); l++){
		Hashtable h = (Hashtable) labs.get(l);

		cds.LaboratoryResultsDocument.LaboratoryResults labr = patientRec.addNewLaboratoryResults();
		cds.LaboratoryResultsDocument.LaboratoryResults.Result labResult = labr.addNewResult();
		cds.LaboratoryResultsDocument.LaboratoryResults.ReferenceRange labRef = labr.addNewReferenceRange();

		labr.setTestName((String) h.get("testName"));
		data = (String) h.get("abn");
		if (data==null || data.trim().equals("")) err.add("Error: No Result Normal/Abnormal Flag for Patient "+demoNo);
		else labr.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Enum.forString(data));
		data = (String) h.get("location");
		if (data==null || data.trim().equals("")) err.add("Error: No Laboratory Name for Patient "+demoNo);
		else labr.setLaboratoryName("CML-" + data);
		
		if (h.get("description")!=null) labr.setNotesFromLab((String) h.get("description"));
		if (h.get("result")!=null) labResult.setValue((String) h.get("result"));
		if (h.get("unit")!=null) labResult.setUnitOfMeasure((String) h.get("unit"));
		if (h.get("range")!=null) labRef.setReferenceRangeText((String) h.get("range"));
		data = (String) h.get("collDate");
		if (data==null || data.trim().equals("")) err.add("Error: Missing Collection Date (Laboratory Results) for Patient "+demoNo);
		else {
		    if (getCalDate(data)!=null) {
			labr.addNewCollectionDateTime().setFullDate(getCalDate(data));
		    } else {
			err.add("Note: Not exporting invalid Collection Date (Laboratory Results) for Patient "+demoNo);
		    }
		}
		h = null;
	    }
	    labs = null;
	    
	    // APPOINTMENTS
	    HttpSession session = request.getSession();
	    Properties p = (Properties) session.getAttribute("oscarVariables");
	    Vector appts = new AppointmentDAO(p).retrieve(demoNo);
	    Appointment ap = null;
	    for (int j=0; j<appts.size(); j++) {
		ap = (Appointment)appts.get(j);
		cds.AppointmentsDocument.Appointments aptm = patientRec.addNewAppointments();
		
		aptm.setSequenceIndex(Integer.parseInt(ap.getAppointment_no()));
		
		if (getCalDate(ap.getDateAppointment())!=null) {
		    aptm.addNewAppointmentDate().setFullDate(getCalDate(ap.getDateAppointment()));
		} else {
		    err.add("Note: Not exporting invalid Appointment Date for Patient "+demoNo);
		}
		if (getCalDate(ap.getDateStartTime())!=null) {
		    aptm.setAppointmentTime(getCalDate(ap.getDateStartTime()));
		} else {
		    err.add("Note: Not exporting invalid Appointment Time for Patient "+demoNo);
		}
		
		long dLong = (ap.getDateEndTime().getTime()-ap.getDateStartTime().getTime())/60000;
		BigInteger duration = BigInteger.valueOf(dLong);
		aptm.setDuration(duration);
		
		data = ap.getStatus();
		if (data!=null && !data.equals("")) {
		    ApptStatusData asd = new ApptStatusData();
		    asd.setApptStatus(data);
		    String msg = getResources(request).getMessage(asd.getTitle());
		    if (msg!=null) aptm.setAppointmentStatus(data);
		    else throw new Exception ("Error! No matching message for appointment status code: " + data);
		}
		
		data = ap.getReason();
		if (data!=null && !data.equals("")) aptm.setAppointmentPurpose(data);
		
		data = ap.getNotes();
		if (data!=null && !data.equals("")) aptm.setAppointmentNotes(data);
		
		if (ap.getProviderFirstN()!=null || ap.getProviderLastN()!=null) {
		    cds.AppointmentsDocument.Appointments.Provider prov = aptm.addNewProvider();
		    cdsDt.PersonNameSimple p_name = prov.addNewName();
		    if (ap.getProviderFirstN()!=null) p_name.setFirstName(ap.getProviderFirstN());
		    if (ap.getProviderLastN()!=null) p_name.setLastName(ap.getProviderLastN());
		    if (ap.getOhipNo()!=null) prov.setOHIPPhysicianId(ap.getOhipNo());
		}
 	    }
	    
	    // REPORTS RECEIVED
	    ArrayList edoc_list = new EDocUtil().listDemoDocs(demoNo);
	    
	    if (!edoc_list.isEmpty()) {
		for (int j=0; j<edoc_list.size(); j++) {
		    EDoc edoc = (EDoc)edoc_list.get(j);
		    cds.ReportsReceivedDocument.ReportsReceived rpr = patientRec.addNewReportsReceived();
		    
		    data = edoc.getFileName();
		    if (!data.equals("")) {
			cdsDt.ReportContent rpc = rpr.addNewContent();
			rpc.setTextContent(data);
			
			File f = new File(edoc.getFilePath());
			if (!f.exists()) err.add("Error: Document \""+f.getName()+"\" does not exist!");
			else if (f.length()>Runtime.getRuntime().freeMemory()) err.add("Error: Document \""+f.getName()+"\" too big to be exported. Not enough memory!");
			else {
			    InputStream in = new FileInputStream(f);
			    byte[] b = new byte[(int)f.length()];
			    
			    int offset=0, numRead=0;
			    while ((numRead=in.read(b,offset,b.length-offset)) >= 0
				   && offset < b.length) offset += numRead;
			    
			    if (offset < b.length) throw new IOException("Could not completely read file " + f.getName());
			    in.close();
			    rpc.setMedia(b);
			    
			    data = edoc.getContentType();
			    if (data.equals("")) err.add("Error: No File Extension & Version info for Document \""+edoc.getFileName()+"\"");
			    else rpr.setFileExtensionAndVersion(data);

			    data = edoc.getType();
			    if (!data.equals("")) {
				if (data.trim().equalsIgnoreCase("radiology")) rpr.setClass1(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT);
				else if (data.trim().equalsIgnoreCase("pathology")) rpr.setClass1(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT);
				else if (data.trim().equalsIgnoreCase("consult")) rpr.setClass1(cdsDt.ReportClass.CONSULTANT_REPORT);
				else {
				    rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
				    rpr.setSubClass(data);
				}
			    }

			    data = edoc.getObservationDate();
			    if (!data.equals("")) {
				if (getCalDate(data)!=null) {
				    rpr.addNewEventDateTime().setFullDate(getCalDate(data));
				} else {
				    err.add("Note: Not exporting invalid Event Date (Reports) for Patient "+demoNo);
				}
			    }
			    data = edoc.getDateTimeStamp();
			    if (!data.equals("")) {
				if (getCalDate(data)!=null) {
				    rpr.addNewReceivedDateTime().setDateTime(getCalDate(data));
				} else {
				    err.add("Note: Not exporting invalid Received DateTime (Reports) for Patient "+demoNo);
				}
			    }
			    data = edoc.getCreatorName();
			    if (!data.equals("")) {
				cdsDt.PersonNameSimple author = rpr.addNewAuthorPhysician();
				String[] name = data.split(", ");
				if (!name[0].equals("")) author.setFirstName(name[0]);
				if (!name[1].equals("")) author.setLastName(name[1]);
			    }
			}
		    }
		}
	    }
	    
	    //export file to temp directory
	    String inFiles = null;
	    try{
		File directory = new File(tmpDir);
		if(!directory.exists()){
		    throw new Exception("Temporary Export Directory (as set in oscar.properties) does not exist!");
		}
		inFiles = demoNo+"-"+lastName.getPart().replace(" ","")+firstName.getPart().replace(" ","")+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".xml";
		files[i] = new File(directory,inFiles);
	    }catch(Exception e){
		e.printStackTrace();
	    }
	    try {
		    omdCdsDoc.save(files[i]);
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
	    }
	}
	
	//create ReadMe.txt & ExportEvent.log
	File[] exportFiles = new File[files.length+2];	
	for (int i=0; i<files.length; i++) exportFiles[i] = files[i];
	exportFiles[exportFiles.length-2] = makeReadMe(files, err, request);
	exportFiles[exportFiles.length-1] = makeExportLog(files, err);
	
	//zip all export files
	String zipName = "export-"+setName.replace(" ","")+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
	if (!zipFiles(exportFiles, zipName)) {
	    throw new Exception("Error! Failed zipping export files");
	}
	
	//PGP encrypt zip file (source zip file will be deleted)
	PGPEncrypt pet = new PGPEncrypt(zipName);
	if (!pet.doEncrypt()) throw new Exception("Error encrypting export files!");
	
	//Remove the temporary export files
	for (int i=0; i<exportFiles.length; i++) {
	    exportFiles[i].delete();
	}
	
	//Download & remove .pgp file
	String pgpFile = zipName+".pgp";
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename=\""+pgpFile+"\"" );
	
	InputStream in = new FileInputStream(tmpDir+pgpFile);
	OutputStream out = response.getOutputStream();
	byte[] buf = new byte[1024];
	int len;
	while ((len=in.read(buf)) > 0) out.write(buf,0,len);
	in.close();
	out.close();
	
	File pgpF = new File(tmpDir+pgpFile);
	if (!pgpF.delete()) throw new Exception("Error! Cannot remove .pgp file from temporary directory");
    }
    return null;
}

    File makeReadMe(File[] f, Vector error, HttpServletRequest req) throws IOException {
	String mediaType = req.getParameter("mediaType");
	String noOfMedia = req.getParameter("noOfMedia");
	
	File readMe = new File(f[0].getParentFile(), "ReadMe.txt");
	BufferedWriter out = new BufferedWriter(new FileWriter(readMe));
	out.write("Physician Group                    : ");
	out.write(new ClinicData().getClinicName());
	out.newLine();
	out.write("CMS Vendor, Product & Version      : ");
	String vendor = oscar.OscarProperties.getInstance().getProperty("Vendor_Product");
	if (vendor==null || vendor.trim().equals("")) error.add("Error: Vendor_Product not defined in oscar.properties");
	else out.write(vendor);
	out.newLine();
	out.write("Application Support Contact        : ");
	String support = oscar.OscarProperties.getInstance().getProperty("Support_Contact");
	if (support==null || support.trim().equals("")) error.add("Error: Support_Contact not defined in oscar.properties");
	else out.write(support);
	out.newLine();
	out.write("Media type                         : ");
	out.write(mediaType);
	out.newLine();
	out.write("Number of media                    : ");
	out.write(noOfMedia);
	out.newLine();
	out.write("Date and Time stamp                : ");
	out.write(UtilDateUtilities.getToday("yyyy-MM-dd hh:mm:ss aa"));
	out.newLine();
	out.write("Total byte count of export files(s): ");
	int fileBytes=0;
	for (int i=0; i<f.length; i++) fileBytes += f[i].length();
	out.write(String.valueOf(fileBytes));
	out.newLine();
	out.write("Total patients files extracted     : ");
	out.write(String.valueOf(f.length));
	out.newLine();
	out.write("Number of errors                   : ");
	out.write(String.valueOf(error.size()));
	if (error.size()>0) out.write(" (See ExportEvent.log for detail)");
	out.newLine();
	out.write("Patient ID range                   : ");
	out.write(f[0].getName().substring(0,f[0].getName().indexOf("-")));
	out.write("-");
	out.write(f[f.length-1].getName().substring(0,f[f.length-1].getName().indexOf("-")));
	out.newLine();
	out.close();
	
	return readMe;
    }
    
    File makeExportLog(File[] f, Vector error) throws IOException {
	String[] keyword = new String[13];
	keyword[0] = "Demographics";
	keyword[1] = "PersonalHistory";
	keyword[2] = "FamilyHistory";
	keyword[3] = "PastHealth";
	keyword[4] = "ProblemList";
	keyword[5] = "RiskFactors";
	keyword[6] = "AllergiesAndAdverseReactions";
	keyword[7] = "MedicationsAndTreatments";
	keyword[8] = "Immunizations";
	keyword[9] = "LaboratoryResults";
	keyword[10] = "Appointments";
	keyword[11] = "ClinicalNotes";
	keyword[12] = "ReportsReceived";
	int[] content = new int[keyword.length];
	String patientID = "Patient ID";
	String totalByte = "Total Bytes";
	String field = null;
	File exportLog = new File(f[0].getParentFile(), "ExportEvent.log");
	BufferedWriter out = new BufferedWriter(new FileWriter(exportLog));
	
	int tableWidth = patientID.length() + totalByte.length() + 5; //add 3+2 for left & right + PatientID delimiters
	for (int i=0; i<keyword.length; i++) tableWidth += keyword[i].length()+2; //add 3 for delimitors
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	out.write("|"+patientID+" |");
	for (int i=0; i<keyword.length; i++) out.write(keyword[i]+" |");
	out.write(totalByte+" |");
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	    
	for (int i=0; i<f.length; i++) {
	    content = countByte(f[i], keyword);

	    out.write("|");
	    field = f[i].getName().substring(0,f[i].getName().indexOf("-")); //field=PatientID
	    out.write(fillUp(field,' ',patientID.length()));
	    out.write(" |");
	    int total=0;
	    for (int j=0; j<content.length; j++) {
		field = "" + content[j];   //field = data size matching each keyword
		total += Integer.parseInt(field);
		out.write(fillUp(field,' ',keyword[j].length()));
		out.write(" |");
	    }

	    out.write(fillUp(String.valueOf(total),' ',totalByte.length()));
	    out.write(" |");
	    out.newLine();
	}
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	
	//write any error that has occurred
	for (int i=0; i<error.size(); i++) {
	    out.newLine();
	    out.write((String)error.get(i));
	}
	out.newLine();
	out.close();
	
	return exportLog;
    }

    boolean zipFiles(File[] exports, String zipFileName) throws Exception {
	byte[] buf = new byte[1024];
	ZipOutputStream zout = null;	
	try {		
		zout = new ZipOutputStream(new FileOutputStream(exports[0].getParent()+"/"+zipFileName));
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot create ZIP file");
	}

	// Compress the input files
	for (int i=0; i<exports.length; i++) {
	    String filePath = exports[i].getAbsolutePath();
	    String fileName = exports[i].getName();
	    FileInputStream fin = null;
	    try {
		    fin = new FileInputStream(filePath);
	    } catch (FileNotFoundException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: While zipping, Export File not found - " + fileName);
	    }
	    try {
		    // Add ZIP entry to output stream
		    zout.putNextEntry(new ZipEntry(fileName));
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot add file to ZIP - " + fileName);
	    }

	    // Transfer bytes from the input files to the ZIP file
	    int len;
	    try {
		    while ((len = fin.read(buf)) > 0) zout.write(buf, 0, len);
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot write data to ZIP - " + fileName);
	    }
	    try {
		    // Complete the entry
		    zout.closeEntry();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot complete ZIP entry - " + fileName);
	    }
	    try {
		    fin.close();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot close input file - " + fileName);
	    }
	}
	try {
		// Complete the ZIP file
		zout.close();
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot close ZIP file");
	}
	return true;
    }

    int[] countByte(File fin, String[] kwd) throws FileNotFoundException, IOException {
	int[] cat_cnt = new int[kwd.length];
	String[] tag = new String[kwd.length];
	
	FileInputStream fis = new FileInputStream(fin);
	BufferedInputStream bis = new BufferedInputStream(fis);
	DataInputStream dis = new DataInputStream(bis);
	
	int cnt=0, tag_in_list=0;
	boolean tag_fnd=false;
	
	while (dis.available()!=0) {
	    if (!tag_fnd) {   //looking for a start tag
		if ((char)dis.read()=='<') {   //a possible tag
		    boolean whole_tag=false;
		    
		    //retrieve the whole tag word
		    String tag_word = "";
		    while (dis.available()!=0 && !whole_tag) {
			String tmp = "" + (char)dis.read();
			if (tmp.equals(">")) whole_tag = true;
			else tag_word += tmp;
		    }
		    
		    //compare the tag word with the list
		    for (int i=0; i<kwd.length; i++) {
			if (tag_word.equals("cds:"+kwd[i])) {
			    tag_in_list = i;
			    tag_fnd = true;
			    cnt = kwd[i].length() +1 +4 +1;   //byte count +"<" +"cds:" +">"
			}
		    }
		}
	    } else {   //a start tag was found, counting...
		//look for an end tag
		if ((char)dis.read()=='<') {   //a possible tag
		    if ((char)dis.read()=='/') {   //a possible end tag
			boolean whole_tag=false;

			//retrieve the whole tag word
			String tag_word = "";
			while (dis.available()!=0 & !whole_tag) {
			    String tmp = "" + (char)dis.read();
			    if (tmp.equals(">")) whole_tag = true;
			    else tag_word += tmp;
			    cnt++;
			}
			
			//compare tag word with the start tag - if matched, stop counting
			if (tag_word.equals("cds:"+kwd[tag_in_list])) {
			    tag_fnd = false;
			    cat_cnt[tag_in_list] += cnt;
			    cnt = 0;
			}
		    }
		    cnt++;
		}
		cnt++;
	    }
	}
	fis.close();
	bis.close();
	dis.close();
	
	return cat_cnt;
    }
   
    boolean convert10toboolean(String s){
	Boolean ret = false;
	if ( s!= null && s.trim().equals("1") ){
	    ret = true; 
	}
	return ret;
    }
    
    String fillUp(String filled, char c, int size) {
	if (size>=filled.length()) {
	    int fill = size-filled.length();
	    for (int i=0; i<fill; i++) filled += c;
	}
	return filled;
    }
    
    Vector extract(String source, String separator) {
	Vector exs = new Vector();
	String src = source.toLowerCase(), sep = separator.toLowerCase();
	
	int a = 0, b = src.indexOf(sep)==-1 ? src.length() : src.indexOf(sep);
	boolean ended = false;
	while (!ended) {
	    exs.add(source.substring(a, b));
	    a = b + sep.length();
	    b = src.indexOf(sep, a)==-1 ? src.length() : src.indexOf(sep, a);
	    if (b-a <= sep.length()) ended = true;
	}
	return exs;
    }
    
    XmlCalendar getCalDate(String inDate) {
	GDateBuilder gd = new GDateBuilder();
	gd.setDate(UtilDateUtilities.StringToDate(inDate));
	if (gd.getYear()<1600) return null;
	else {
	    gd.clearTimeZone();
	    return gd.getCalendar();
	}
    }
    
    XmlCalendar getCalDate(Date inDate) {
	GDateBuilder gd = new GDateBuilder();
	gd.setDate(inDate);
	if (gd.getYear()<1600) return null;
	else {
	    gd.clearTimeZone();
	    return gd.getCalendar();
	}
    }
    
    public DemographicExportAction2() {
    }
   
    public void getMembers(Object obj){
	Class cls = obj.getClass();
	Method[] methods = cls.getMethods();
	for (int i=0; i < methods.length; i++){
	    Class ret = methods[i].getReturnType();
	    Class[] params = methods[i].getParameterTypes();
	    System.out.print(ret.getName()+" ");
	    System.out.print(methods[i].getName());
	    System.out.print("(");
	    for (int j=0; j<params.length; j++){
		System.out.print(" "+params[j].getName());
	    }
	    System.out.println(")");
	}
    }
    
    public String currentMem(){        
	long total = Runtime.getRuntime().totalMemory();
	long free  = Runtime.getRuntime().freeMemory();
	long Used = total -  free;
	return "Total "+total+" Free "+free+" USED "+Used;
    }
}
