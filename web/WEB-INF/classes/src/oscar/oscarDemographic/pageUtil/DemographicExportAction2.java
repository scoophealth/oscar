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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.XmlCalendar;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.appt.ApptData;
import oscar.appt.ApptStatusData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.oscarDemographic.data.DemographicRelationship;
import oscar.oscarEncounter.data.EctEChartBean;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarReport.data.RptDemographicQueryBuilder;
import oscar.oscarReport.data.RptDemographicQueryLoader;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPatientData.Patient.Allergy;
import oscar.service.OscarSuperManager;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie Cheng
 */
public class DemographicExportAction2 extends Action {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String setName = request.getParameter("patientSet");
    String demoNo = request.getParameter("demographicNo");
    oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
    String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");
    
    ArrayList list = new ArrayList();
    if (demoNo==null) {
	list = new DemographicSets().getDemographicSet(setName);
	if (list.isEmpty()) {
	    Date asofDate = UtilDateUtilities.Today();
	    RptDemographicReportForm frm = new RptDemographicReportForm ();
	    frm.setSavedQuery(setName);
	    RptDemographicQueryLoader demoL = new RptDemographicQueryLoader();
	    frm = demoL.queryLoader(frm);
	    frm.addDemoIfNotPresent();
	    frm.setAsofDate(UtilDateUtilities.DateToString(asofDate));
	    RptDemographicQueryBuilder demoQ = new RptDemographicQueryBuilder();
	    list = demoQ.buildQuery(frm,UtilDateUtilities.DateToString(asofDate));
	}
    }else{
	list.add(demoNo);
    }    
    
    
    DemographicData d = new DemographicData();

    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(
			request.getSession().getServletContext());
	OscarSuperManager oscarSuperManager = (OscarSuperManager)webApplicationContext.getBean("oscarSuperManager");

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
    if (!filled(tmpDir)) {
	throw new Exception("Temporary Export Directory not set! Check oscar.properties.");
    } else {
	if (tmpDir.charAt(tmpDir.length()-1)!='/') tmpDir = tmpDir + '/';
	
	files = new File[list.size()];
	String data="";
	for(int i = 0 ; i < list.size(); i++){
	    Object obj = list.get(i);
	    if ( obj instanceof String){
		demoNo = (String) obj;
	    }else{
		ArrayList l2 = (ArrayList) obj;
		demoNo = (String) l2.get(0);
	    } 
	    if (!filled(demoNo)) {
		demoNo="";
		err.add("Error: No Demographic Number");
	    } else {
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
		legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);
		
		data = filledOrEmpty(demographic.getFirstName());
		String demoName = data.replace(" ","");
		if (filled(data)) {
		    firstName.setPart(data);
		    firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
		    firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
		} else {
		    err.add("Error: No First Name for Patient "+demoNo);
		}
		data = filledOrEmpty(demographic.getLastName());
		demoName += data.replace(" ","");
		if (filled(data)) {
		    lastName.setPart(data);
		    lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
		    lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
		} else {
		    err.add("Error: No Last Name for Patient "+demoNo);
		}
		if (!filled(setName)) setName = demoName;
		
		data = demographic.getSex();
		if (filled(data)) {
		    demo.setGender(cdsDt.Gender.Enum.forString(data));
		} else {
		    err.add("Error: No Gender for Patient "+demoNo);
		}

		data = filledOrEmpty(demographic.getRosterStatus());
		if (!filled(data)) {
		    data = "";
		    err.add("Error: No Enrollment Status for Patient "+demoNo);
		}
		data = data.equals("RO") ? "1" : "0";
		demo.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(data));
		
		data = filledOrEmpty(demographic.getPatientStatus());
		if (!filled(data)) {
		    data = "";
		    err.add("Error: No Person Status Code for Patient "+demoNo);
		}
		if (data.equals("AC")) {
		    data = "A";
		} else if (data.equals("DE")) {
		    data = "D";
		} else {
		    data = "I";
		}
		demo.setPersonStatusCode(cdsDt.PersonStatus.Enum.forString(data));

		data = filledOrEmpty(demographic.getDob("-"));
		if (!filled(data)) {
		    demo.addNewDateOfBirth();
		    err.add("Error: No Date Of Birth for Patient "+demoNo);
		} else {
		    if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
			demo.addNewDateOfBirth().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
		    } else {
			err.add("Note: Not exporting invalid Date of Birth for Patient "+demoNo);
		    }
		}
		data = demographic.getDateJoined();
		if (filled(data)) {
		    if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
			demo.addNewEnrollmentDate().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
		    } else {
			err.add("Note: Not exporting invalid Enrollment Date for Patient "+demoNo);
		    }
		}
		data = demographic.getEndDate();
		if (filled(data)) {
		    if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
			demo.addNewEnrollmentTerminationDate().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
		    } else {
			err.add("Note: Not exporting invalid Enrollment Termination Date for Patient "+demoNo);
		    }
		}
		data = demographic.getChartNo();
		if (filled(data)) demo.setChartNumber(data);

		data = demographic.getEmail();
		if (filled(data)) demo.setEmail(data);

		String providerNo = demographic.getProviderNo();
		if (filled(providerNo)) {
		    cds.DemographicsDocument.Demographics.PrimaryPhysician pph = demo.addNewPrimaryPhysician();
		    ProviderData provd = new ProviderData(providerNo);
		    pph.setOHIPPhysicianId(provd.getOhip_no());
		    writeNameSimple(pph.addNewName(), provd.getFirst_name(), provd.getLast_name());
		}
		if (filled(demographic.getJustHIN())) {
		    cdsDt.HealthCard healthCard = demo.addNewHealthCard();
		    healthCard.setNumber(demographic.getJustHIN());
		    healthCard.setProvinceCode(cdsDt.HealthCardProvinceCode.CA_ON);
		    if (filled(demographic.getVersionCode())) healthCard.setVersion(demographic.getVersionCode());
		    if (filled(demographic.getEffDate())) {
			data = demographic.getEffDate();
			if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
			    healthCard.setExpirydate(getCalDate(UtilDateUtilities.StringToDate(data)));
			} else {
			    err.add("Note: Not exporting invalid Health Card Expiry Date for Patient "+demoNo);
			}
		    }
		}
		if (filled(demographic.getAddress())) {
		    cdsDt.Address addr = demo.addNewAddress();		
		    cdsDt.AddressStructured address = addr.addNewStructured();

		    addr.setAddressType(cdsDt.AddressType.R);
		    address.setLine1(demographic.getAddress());		
		    if (filled(demographic.getCity())) address.setCity(demographic.getCity());
		    if (filled(demographic.getProvince())) address.setCountrySubdivisionCode(demographic.getProvince());
		    if (filled(demographic.getPostal())) address.addNewPostalZipCode().setPostalCode(demographic.getPostal());
		}
		if (filled(demographic.getPhone())) {
		    cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
		    phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
		    phoneResident.setPhoneNumber(demographic.getPhone());
		    data = (String) demoExt.get("hPhoneExt");
		    if (data!=null) {
			if (data.length()>5) {
			    data = data.substring(0,5);
			    err.add("Error! Home phone extension too long, export trimmed for Patient "+demoNo);
			}
			phoneResident.setExtension(data);
		    }
		}
		if (filled(demographic.getPhone2())) {
		    cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
		    phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
		    phoneWork.setPhoneNumber(demographic.getPhone2());
		    data = (String) demoExt.get("wPhoneExt");
		    if (data!=null) {
			if (data.length()>5) {
			    data = data.substring(0,5);
			    err.add("Error! Work phone extension too long, export trimmed for Patient "+demoNo);
			}
			phoneWork.setExtension(data);
		    }
		}
		if (filled((String)demoExt.get("demo_cell"))) {
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
		    if (filled(data)) {
			DemographicData.Demographic relDemo = d.getDemographic(data);
			Hashtable relDemoExt = ext.getAllValuesForDemo(data);

			cds.DemographicsDocument.Demographics.Contact contact = demo.addNewContact();
			writeNameSimple(contact.addNewName(), relDemo.getFirstName(), relDemo.getLastName());
			if (!filled(relDemo.getFirstName())) {
			    err.add("Error! No First Name for contact ("+j+") for Patient "+demoNo);
			}
			if (!filled(relDemo.getLastName())) {
			    err.add("Error! No Last Name for contact ("+j+") for Patient "+demoNo);
			}

			String ec = (String) r.get("emergency_contact");
			String nk = (String) r.get("sub_decision_maker");
			String rel = (String) r.get("relation");

			if (ec.equals("1")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.EC);
			else if (nk.equals("1")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.NK);
			else if (rel.equals("Administrative Staff")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.AS);
			else if (rel.equals("Care Giver")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.CG);
			else if (rel.equals("Power of Attorney")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.PA);
			else if (rel.equals("Insurance")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.IN);
			else if (rel.equals("Guarantor")) contact.setContactPurpose(cdsDt.ContactPersonPurpose.GT);
			else contact.setContactPurpose(cdsDt.ContactPersonPurpose.NK);

			if (filled(relDemo.getEmail())) contact.setEmailAddress(relDemo.getEmail());
			if (filled((String)r.get("notes"))) contact.setNote((String)r.get("notes"));

			if (filled(relDemo.getPhone())) {
			    cdsDt.PhoneNumber phoneRes = contact.addNewPhoneNumber();
			    phoneRes.setPhoneNumberType(cdsDt.PhoneNumberType.R);
			    phoneRes.setPhoneNumber(relDemo.getPhone());
			    data = (String) relDemoExt.get("hPhoneExt");
			    if (filled(data)) {
				if (data.length()>5) {
				    data = data.substring(0,5);
				    err.add("Error! Home phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+demoNo);
				}
				phoneRes.setExtension(data);
			    }
			}
			if (filled(relDemo.getPhone2())) {
			    cdsDt.PhoneNumber phoneW = contact.addNewPhoneNumber();
			    phoneW.setPhoneNumberType(cdsDt.PhoneNumberType.W);
			    phoneW.setPhoneNumber(relDemo.getPhone2());
			    data = (String) relDemoExt.get("wPhoneExt");
			    if (filled(data)) {
				if (data.length()>5) {
				    data = data.substring(0,5);
				    err.add("Error! Work phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+demoNo);
				}
				phoneW.setExtension(data);
			    }
			}
			if (filled((String)relDemoExt.get("demo_cell"))) {
			    cdsDt.PhoneNumber phoneCell = contact.addNewPhoneNumber();
			    phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
			    phoneCell.setPhoneNumber((String)relDemoExt.get("demo_cell"));
			}
			relDemoExt = null;
		    }
		}

		EctEChartBean bean = new EctEChartBean();
		bean.setEChartBean(demoNo);
		Vector dataPart = null;
		String dataIn = null;

		// PERSONAL HISTORY
		data = bean.socialHistory;
		if (filled(data)) {
		    dataPart = extract(data, "-----[[", true);
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			patientRec.addNewPersonalHistory().setCategorySummaryLine(dataIn);
		    }
		}
		// FAMILY HISTORY
		data = bean.familyHistory;
		if (filled(data)) {
		    dataPart = extract(data, "-----[[", true);
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			patientRec.addNewFamilyHistory().setCategorySummaryLine(dataIn);
		    }
		}
		// PAST HEALTH
		data = bean.medicalHistory;
		if (filled(data)) {
		    dataPart = extract(data, "-----[[", true);
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			patientRec.addNewPastHealth().setCategorySummaryLine(dataIn);
		    }
		}
		// RISK FACTORS
		data = bean.reminders;
		if (filled(data)) {
		    dataPart = extract(data, "-----[[", true);
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			patientRec.addNewRiskFactors().setCategorySummaryLine(dataIn);
		    }
		}
		// CLINCAL NOTES
		data = bean.encounter;
		if (filled(data)) {
		    dataPart = extract(data, "-----------------------------------");
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			patientRec.addNewClinicalNotes().setMyClinicalNotesContent(dataIn);
		    }
		}	    
		// PROBLEM LIST
		data = bean.ongoingConcerns;
		if (filled(data)) {
		    dataPart = extract(data, "-----[[", true);
		    for (int j=0; j<dataPart.size(); j++) {
			dataIn = (String) dataPart.get(j);
			cds.ProblemListDocument.ProblemList pList = patientRec.addNewProblemList();
			pList.setCategorySummaryLine(dataIn);

			// extract Onset Date from Summary Line
			dataIn = dataIn.toLowerCase();
			int date_index = dataIn.indexOf("onset date:");
			if (date_index>-1) {
			    date_index = date_index + "onset date:".length();
			    int date_end = dataIn.indexOf("\n", date_index);
			    if (date_end==-1) date_end = dataIn.indexOf("\r", date_index);
			    if (date_end==-1 && date_index+12>=dataIn.length()) date_end = dataIn.length()-1;
			    if (date_end>date_index) {
				String onset_date = dataIn.substring(date_index, date_end).trim();
				if (filled(onset_date)) {
				    cdsDt.DateFullOrPartial onsetDate = pList.addNewOnsetDate();
				    if (getCalDate(UtilDateUtilities.StringToDate(onset_date))!=null) {
					onsetDate.setFullDate(getCalDate(UtilDateUtilities.StringToDate(onset_date)));
				    } else {
					date_index = -1;
				    }
				} else {
				    date_index = -1;
				}
			    } else {
				date_index = -1;
			    }
			}
			if (date_index==-1) {
			    pList.addNewOnsetDate().setFullDate(getCalDate(UtilDateUtilities.StringToDate("1900-01-01")));
			    err.add("Error: Missing Onset Date (Problem List) for Patient "+demoNo+" ("+(j+1)+"). Exporting as \"1900-01-01\"");
			}
		    }
		}

		// ALLERGIES & ADVERSE REACTIONS
		Allergy[] allergies = new RxPatientData().getPatient(demoNo).getAllergies();
		for (int j=0; j<allergies.length; j++) {
		    cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions alr = patientRec.addNewAllergiesAndAdverseReactions();
		    String aSummary = "";

		    data = allergies[j].getAllergy().getDESCRIPTION();
		    if (filled(data)) {
			alr.setOffendingAgentDescription(data);
			aSummary = "Offending Agent Description: " + data;
		    }
		    data = String.valueOf(allergies[j].getAllergy().getPickID());
		    if (filled(data)) {
			cdsDt.Code drugCode = alr.addNewCode();
			drugCode.setCodingSystem("DIN");
			drugCode.setValue(data);
			aSummary = appendLine(aSummary, "DIN: ", data);
		    }
		    data = String.valueOf(allergies[j].getAllergy().getTYPECODE());
		    if (filled(data)) {
			if (data.equals("13")) {
			    alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.DR);
			} else if (data.equals("2")) {
			    alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.UK);
			} else {
			    alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.ND);
			}
			aSummary = appendLine(aSummary,"Property of Offending Agent: ",alr.getPropertyOfOffendingAgent().toString());
		    }
		    data = allergies[j].getAllergy().getReaction();
		    if (filled(data)) {
			alr.setReaction(data);
			aSummary = appendLine(aSummary, "Reaction: ", data);
		    }
		    data = allergies[j].getAllergy().getSeverityOfReaction();
		    if (filled(data)) {
			if (data.equals("1")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
			} else if (data.equals("2")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MO);
			} else if (data.equals("3")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.LT);
			} else { //SeverityOfReaction==0
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
			    err.add("Note: Severity Of Allergy Reaction [Unknown] exported as [Mild] for Patient "+demoNo+" ("+(j+1)+")");
			}
			aSummary = appendLine(aSummary,"Adverse Reaction Severity: ",alr.getSeverity().toString());
		    }
		    if (allergies[j].getEntryDate()!=null) {
			if (getCalDate(allergies[j].getEntryDate())!=null) {
			    alr.addNewRecordedDate().setFullDate(getCalDate(allergies[j].getEntryDate()));
			    aSummary = appendLine(aSummary,"Recorded Date: ",UtilDateUtilities.DateToString(allergies[j].getEntryDate(),"yyyy-MM-dd"));
			} else {
			    err.add("Note: Not exporting invalid Recorded Date (Allergies) for Patient "+demoNo+" ("+(j+1)+")");
			}
		    }
		    if (!filled(aSummary)) {
			err.add("Error: No Category Summary Line (Allergies & Adverse Reactions) for Patient "+demoNo+" ("+(j+1)+")");
		    }
		    alr.setCategorySummaryLine(aSummary);
		}

		// IMMUNIZATIONS
		ArrayList prevList2 = pd.getPreventionData(demoNo);
		for (int k =0 ; k < prevList2.size(); k++){
		    Hashtable a = (Hashtable) prevList2.get(k);  
		    if (a != null && inject.contains((String) a.get("type")) ){
			cds.ImmunizationsDocument.Immunizations immu = patientRec.addNewImmunizations();
			data = (String) a.get("type");
			if (!filled(data)) {
			    data = "";
			    err.add("Error: No Immunization Name for Patient "+demoNo+" ("+(k+1)+")");
			}
			immu.setImmunizationName(data);

			data = (String) a.get("refused");
			if (!filled(data)) {
			    immu.addNewRefusedFlag();
			    err.add("Error: No Refused Flag for Patient "+demoNo+" ("+(k+1)+")");
			} else {
			    immu.addNewRefusedFlag().setBoolean(convert10toboolean(data));
			}

			Hashtable extraData = pd.getPreventionById((String) a.get("id"));
			data = (String) extraData.get("summary");
			if (!filled(data)) {
			    data = "";
			    err.add("Error: No Category Summary Line (Immunization) for Patient "+demoNo+" ("+(k+1)+")");
			}
			immu.setCategorySummaryLine(data);

			if (filled((String)extraData.get("manufacture"))) immu.setManufacturer((String)extraData.get("manufacture"));
			if (filled((String)extraData.get("lot"))) immu.setLotNumber((String)extraData.get("lot"));
			if (filled((String)extraData.get("route"))) immu.setRoute((String)extraData.get("route"));
			if (filled((String)extraData.get("location"))) immu.setSite((String)extraData.get("location"));
			if (filled((String)extraData.get("comments"))) immu.setNotes((String)extraData.get("comments"));

			data = (String) a.get("prevention_date");
			if (filled(data)) {
			    if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
				immu.addNewDate().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
			    } else {
				err.add("Note: Not exporting invalid Immunization Date for Patient "+demoNo+" ("+(k+1)+")");
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
			    mSummary = "Prescription Written Date: "+UtilDateUtilities.DateToString(arr[p].getRxCreatedDate(),"yyyy-MM-dd");
			} else {
			    err.add("Note: Not exporting invalid Prescription Written Date for Patient "+demoNo+" ("+(p+1)+")");
			}
		    }
		    if (arr[p].getRxDate()!=null) {
			if (getCalDate(arr[p].getRxDate())!=null) {
			    medi.addNewStartDate().setFullDate(getCalDate(arr[p].getRxDate()));
			    mSummary = appendLine(mSummary,"Start Date: ",UtilDateUtilities.DateToString(arr[p].getRxDate(),"yyyy-MM-dd"));
			} else {
			    err.add("Note: Not exporting invalid Medication Start Date for Patient "+demoNo+" ("+(p+1)+")");
			}
		    }
		    if (arr[p].getEndDate()!=null) {
			if (getCalDate(arr[p].getEndDate())!=null) {
			    medi.addNewEndDate().setFullDate(getCalDate(arr[p].getEndDate()));
			    mSummary = appendLine(mSummary,"End Date: ",UtilDateUtilities.DateToString(arr[p].getEndDate(),"yyyy-MM-dd"));
			} else {
			    err.add("Note: Not exporting invalid Medication End Date for Patient "+demoNo+" ("+(p+1)+")");
			}
		    }
		    data = arr[p].getRegionalIdentifier();
		    if (filled(data)) {
			medi.setDrugIdentificationNumber(data);
			mSummary = appendLine(mSummary, "DIN: ", data);
		    }
		    data = arr[p].getDrugName();
		    if (!filled(data)) {
			data = "";
			err.add("Error: No Drug Name for Patient "+demoNo+" ("+(p+1)+")");
		    }
		    medi.setDrugName(data);
		    mSummary = appendLine(mSummary, "Drug Name: ", data);

		    if (filled(arr[p].getDosage())) {
			cdsDt.DrugMeasure drugM = medi.addNewStrength();
			drugM.setAmount(arr[p].getDosage());
			mSummary = appendLine(mSummary, "Strength: ", arr[p].getDosage());

			if (filled(arr[p].getUnit())) {
			    drugM.setUnitOfMeasure(arr[p].getUnit());
			    mSummary = appendLine(mSummary, "Unit: ", arr[p].getUnit());
			} else {
			    drugM.setUnitOfMeasure("");
			}
		    }
		    if (filled(medi.getDrugName()) || filled(medi.getDrugIdentificationNumber())) {
			medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
			mSummary = appendLine(mSummary, "Number of Refills: ", String.valueOf(arr[p].getRepeat()));
		    }
		    if (filled(arr[p].getRoute())) {
			medi.setRoute(arr[p].getRoute());
			mSummary = appendLine(mSummary, "Route: ", arr[p].getRoute());
		    }
		    if (filled(arr[p].getFreqDisplay())) {
			medi.setFrequency(arr[p].getFreqDisplay());
			mSummary = appendLine(mSummary, "Frequency: ", arr[p].getFreqDisplay());
		    }
		    if (filled(arr[p].getDuration())) {
			medi.setDuration(arr[p].getDuration());
			mSummary = appendLine(mSummary, "Duration: ", arr[p].getDuration());
		    }
		    if (filled(arr[p].getQuantity())) {
			medi.setQuantity(arr[p].getQuantity());
			mSummary = appendLine(mSummary, "Quantity: ", arr[p].getQuantity());
		    }
		    data = arr[p].getProviderNo();
		    if (filled(data)) {
			cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
			ProviderData provd = new ProviderData(data);
			pcb.setOHIPPhysicianId(provd.getOhip_no());
			writeNameSimple(pcb.addNewName(), provd.getFirst_name(), provd.getLast_name());
			mSummary = appendLine(mSummary, "Prescribed by: ", filledOrEmpty(provd.getFirst_name())
				   + " " + filledOrEmpty(provd.getLast_name()));
		    }
		    data = arr[p].getSpecial();
		    if (filled(data)) {
			medi.setPrescriptionInstructions(data);
			mSummary = appendLine(mSummary, "Prescription Instructions: ", data);
		    }

		    if (!filled(mSummary)) err.add("Error: No Category Summary Line (Medications & Treatments) for Patient "+demoNo+" ("+(p+1)+")");
		    medi.setCategorySummaryLine(mSummary);
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
		    if (!filled(data)) {
			data = "U";
			err.add("Error: No Result Normal/Abnormal Flag for Patient "+demoNo+" ("+(l+1)+")");
		    }
		    labr.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Enum.forString(data));

		    data = (String) h.get("location");
		    if (!filled(data)) {
			data = "";
			err.add("Error: No Laboratory Name for Patient "+demoNo+" ("+(l+1)+")");
		    }
		    boolean isNum = true;
		    try {
			Integer.valueOf(data);
		    } catch (NumberFormatException en) {
			isNum = false;
		    }
		    if (isNum) {
			labr.setLaboratoryName("CML-" + data);
		    } else {
			labr.setLaboratoryName(data);
		    }

		    if (filled((String)h.get("description"))) labr.setNotesFromLab((String) h.get("description"));
		    if (filled((String)h.get("accession"))) labr.setAccessionNumber((String) h.get("accession"));
		    if (filled((String)h.get("result"))) labResult.setValue((String) h.get("result"));
		    if (filled((String)h.get("units"))) labResult.setUnitOfMeasure((String) h.get("units"));
		    if (filled((String)h.get("min"))) labRef.setLowLimit((String) h.get("min"));
		    if (filled((String)h.get("max"))) labRef.setHighLimit((String) h.get("max"));
		    
		    data = (String) h.get("collDate");
		    if (!filled(data)) {
			labr.addNewCollectionDateTime();
			err.add("Error: No Collection Date (Laboratory Results) for Patient "+demoNo+" ("+(l+1)+")");
		    } else {
			if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
			    labr.addNewCollectionDateTime().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
			} else {
			    err.add("Note: Not exporting invalid Collection Date (Laboratory Results) for Patient "+demoNo+" ("+(l+1)+")");
			}
		    }
		    h = null;
		}
		labs = null;

		// APPOINTMENTS
		List appts = oscarSuperManager.populate("appointmentDao", "export_appt", new String[] {demoNo});
		ApptData ap = null;
		for (int j=0; j<appts.size(); j++) {
		    ap = (ApptData)appts.get(j);
		    cds.AppointmentsDocument.Appointments aptm = patientRec.addNewAppointments();

		    aptm.setSequenceIndex(Integer.parseInt(ap.getAppointment_no()));

		    if (ap.getDateAppointmentDate()!=null) {
			if (getCalDate(ap.getDateAppointmentDate())!=null) {
			    aptm.addNewAppointmentDate().setFullDate(getCalDate(ap.getDateAppointmentDate()));
			} else {
			    err.add("Note: Not exporting invalid Appointment Date ("+(j+1)+") for Patient "+demoNo);
			}
		    } else {
			aptm.addNewAppointmentDate();
			err.add("Error: No Appointment Date ("+j+") for Patient "+demoNo);
		    }
		    if (ap.getDateStartTime()!=null) {
			if (getCalDate(ap.getDateStartTime())!=null) {
			    aptm.setAppointmentTime(getCalDate(ap.getDateStartTime()));
			} else {
			    err.add("Note: Not exporting invalid Appointment Time ("+(j+1)+") for Patient "+demoNo);
			}
		    } else {
			err.add("Error: No Appointment Time ("+(j+1)+") for Patient "+demoNo);
		    }

		    long dLong = (ap.getDateEndTime().getTime()-ap.getDateStartTime().getTime())/60000;
		    BigInteger duration = BigInteger.valueOf(dLong);
		    aptm.setDuration(duration);

		    if (filled(ap.getStatus())) {
			ApptStatusData asd = new ApptStatusData();
			asd.setApptStatus(ap.getStatus());
			String msg = null;
                        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
                            msg = asd.getTitle();
                        else
                            msg = getResources(request).getMessage(asd.getTitle());
			if (filled(msg)) {
			    aptm.setAppointmentStatus(msg);
			} else {
			    throw new Exception ("Error! No matching message for appointment status code: " + data);
			}
		    }
		    if (filled(ap.getReason())) aptm.setAppointmentPurpose(ap.getReason());

		    if (filled(ap.getNotes())) {
			aptm.setAppointmentNotes(ap.getNotes());
		    } else {
			aptm.setAppointmentNotes("");
			err.add("Error! No Appointment Notes for Patient "+demoNo+" ("+(j+1)+")");
		    }

		    if (filled(ap.getProviderFirstName()) || filled(ap.getProviderLastName())) {
			cds.AppointmentsDocument.Appointments.Provider prov = aptm.addNewProvider();
			if (filled(ap.getOhipNo())) prov.setOHIPPhysicianId(ap.getOhipNo());
			writeNameSimple(prov.addNewName(), ap.getProviderFirstName(), ap.getProviderLastName());
		    }
		}

		// REPORTS RECEIVED
		ArrayList edoc_list = new EDocUtil().listDemoDocs(demoNo);

		if (!edoc_list.isEmpty()) {
		    for (int j=0; j<edoc_list.size(); j++) {
			EDoc edoc = (EDoc)edoc_list.get(j);
			cds.ReportsReceivedDocument.ReportsReceived rpr = patientRec.addNewReportsReceived();
			cdsDt.ReportContent rpc = rpr.addNewContent();

			File f = new File(edoc.getFilePath());
			if (!f.exists()) {
			    err.add("Error: Document \""+f.getName()+"\" does not exist!");
			} else if (f.length()>Runtime.getRuntime().freeMemory()) {
			    err.add("Error: Document \""+f.getName()+"\" too big to be exported. Not enough memory!");
			} else {
			    InputStream in = new FileInputStream(f);
			    byte[] b = new byte[(int)f.length()];

			    int offset=0, numRead=0;
			    while ((numRead=in.read(b,offset,b.length-offset)) >= 0
				   && offset < b.length) offset += numRead;

			    if (offset < b.length) throw new IOException("Could not completely read file " + f.getName());
			    in.close();
			    rpc.setMedia(b);

			    data = edoc.getContentType();
			    if (!filled(data)) err.add("Error: No File Extension & Version info for Document \""+edoc.getFileName()+"\"");
			    rpr.setFileExtensionAndVersion(data);

			    data = edoc.getType();
			    if (filled(data)) {
				if (data.trim().equalsIgnoreCase("radiology")) {
				    rpr.setClass1(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT);
				} else if (data.trim().equalsIgnoreCase("pathology")) {
				    rpr.setClass1(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT);
				} else if (data.trim().equalsIgnoreCase("consult")) {
				    rpr.setClass1(cdsDt.ReportClass.CONSULTANT_REPORT);
				} else {
				    rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
				    rpr.setSubClass(data);
				}
			    } else {
				err.add("Error: No Class type for Document \""+edoc.getFileName()+"\"");
			    }
			    data = edoc.getObservationDate();
			    if (filled(data)) {
				if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
				    rpr.addNewEventDateTime().setFullDate(getCalDate(UtilDateUtilities.StringToDate(data)));
				} else {
				    err.add("Note: Not exporting invalid Event Date (Reports) for Patient "+demoNo+" ("+(j+1)+")");
				}
			    }
			    data = edoc.getDateTimeStamp();
			    if (filled(data)) {
				if (getCalDate(UtilDateUtilities.StringToDate(data))!=null) {
				    rpr.addNewReceivedDateTime().setDateTime(getCalDate(UtilDateUtilities.StringToDate(data, "yyyy-MM-dd HH:mm:ss")));
				} else {
				    err.add("Note: Not exporting invalid Received DateTime (Reports) for Patient "+demoNo+" ("+(j+1)+")");
				}
			    }
			    data = edoc.getCreatorName();
			    if (filled(data)) {
				String[] name = data.split(", ");
				writeNameSimple(rpr.addNewAuthorPhysician(), name[0], name[1]);
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
		    inFiles = demoNo+"-"+demoName+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".xml";
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
        PGPEncrypt pet = new PGPEncrypt();
	if (!pet.encrypt(zipName, tmpDir)) throw new Exception("Error encrypting export files!");
	
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
	if (!filled(vendor)) {
	    error.add("Error: Vendor_Product not defined in oscar.properties");
	} else {
	    out.write(vendor);
	}
	out.newLine();
	out.write("Application Support Contact        : ");
	String support = oscar.OscarProperties.getInstance().getProperty("Support_Contact");
	if (!filled(support)) {
	    error.add("Error: Support_Contact not defined in oscar.properties");
	} else {
	    out.write(support);
	}
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
			if (tmp.equals(">")) {
			    whole_tag = true;
			} else {
			    tag_word += tmp;
			}
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
			    if (tmp.equals(">")) {
				whole_tag = true;
			    } else {
				tag_word += tmp;
			    }
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
    
    void writeNameSimple(cdsDt.PersonNameSimple personName, String firstName, String lastName) {
	if (!filled(firstName)) firstName = "";
	if (!filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
    }
    
    void writeNameSimple(cdsDt.PersonNameSimpleWithMiddleName personName, String firstName, String lastName) {
	if (!filled(firstName)) firstName = "";
	if (!filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
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
	return extract(source, separator, false);
    }
    
    Vector extract(String source, String separator, boolean separatorAsHeader) {
	Vector exs = new Vector();
	String src = source.toLowerCase(), sep = separator.toLowerCase();
	int a = 0, b = src.indexOf(sep)==-1 ? src.length() : src.indexOf(sep);
	while (a<b) {
	    if (filled(trimBlankLine(source.substring(a, b)))) exs.add(trimBlankLine(source.substring(a, b)));
	    a = separatorAsHeader ? b : b + sep.length();
	    int i = separatorAsHeader ? a+sep.length() : a;
	    b = src.indexOf(sep, i)==-1 ? src.length() : src.indexOf(sep, i);
	}
	return exs;
    }
    
    String trimBlankLine(String source) {
	if (!filled(source)) return "";
	
	boolean existBlank = true;
	while (existBlank) {
	    if (source.charAt(0)=='\n' || source.charAt(0)=='\r') {
		source = source.substring(1);
	    } else if (source.charAt(source.length()-1)=='\n' || source.charAt(source.length()-1)=='\r') {
		source = source.substring(0,source.length()-1);
	    } else {
		existBlank = false;
	    }
	}
	return source;
    }
    
    XmlCalendar getCalDate(Date inDate) {
	GDateBuilder gd = new GDateBuilder();
	gd.setDate(inDate);
	if (gd.getYear()<1600) {
	    return null;
	} else {
	    gd.clearTimeZone();
	    return gd.getCalendar();
	}
    }
    
    boolean filled(String textStr) {
	if (textStr==null) {
	    return false;
	} else if (textStr.trim().equals("")) {
	    return false;
	} else {
	    return true;
	}
    }
    
    String filledOrEmpty(String nullOrTextlessStr) {
	if (!filled(nullOrTextlessStr)) {
	    nullOrTextlessStr = "";
	}
	return nullOrTextlessStr;
    }
    
    String appendLine(String baseStr, String label, String addStr) {
	String retStr = filledOrEmpty(baseStr);
	if (filled(retStr)) {
	    retStr += filled(addStr) ? "\n"+label+addStr : "";
	} else {
	    retStr = filledOrEmpty(addStr);
	}
	
	return retStr;
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
