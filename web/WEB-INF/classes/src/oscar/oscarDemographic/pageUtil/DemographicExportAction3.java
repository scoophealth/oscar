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
 * DemographicExportAction3.java
 *
 * Created on Nov 4, 2008
 */

package oscar.oscarDemographic.pageUtil;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.appt.ApptData;
import oscar.appt.ApptStatusData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.model.Log;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.oscarDemographic.data.DemographicRelationship;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.LabMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarLab.LabRequestReportLink;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarLab.ca.on.CommonLabTestValues;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarReport.data.RptDemographicQueryBuilder;
import oscar.oscarReport.data.RptDemographicQueryLoader;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.service.OscarSuperManager;
import oscar.util.UtilDateUtilities;
import cds.OmdCdsDocument;
import cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cds.AppointmentsDocument.Appointments;
import cds.AuditInformationDocument.AuditInformation;
import cds.CareElementsDocument.CareElements;
import cds.ClinicalNotesDocument.ClinicalNotes;
import cds.DemographicsDocument.Demographics;
import cds.FamilyHistoryDocument.FamilyHistory;
import cds.ImmunizationsDocument.Immunizations;
import cds.LaboratoryResultsDocument.LaboratoryResults;
import cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cds.PastHealthDocument.PastHealth;
import cds.PatientRecordDocument.PatientRecord;
import cds.ProblemListDocument.ProblemList;
import cds.ReportsReceivedDocument.ReportsReceived;
import cds.RiskFactorsDocument.RiskFactors;

/**
 *
 * @author Ronnie Cheng
 */
public class DemographicExportAction3 extends Action {
    String demographicNo=null;

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    OscarProperties oscarp = OscarProperties.getInstance();
    String strEditable = oscarp.getProperty("ENABLE_EDIT_APPT_STATUS");
    
    DemographicExportForm defrm = (DemographicExportForm)form;
    this.demographicNo = defrm.getDemographicNo();
    String setName = defrm.getPatientSet();
    String mediaType = defrm.getMediaType();
    String noOfMedia = defrm.getNoOfMedia();
    String pgpReady = defrm.getPgpReady();
    boolean exPersonalHistory = defrm.getExPersonalHistory();
    boolean exFamilyHistory = defrm.getExFamilyHistory();
    boolean exPastHealth = defrm.getExPastHealth();
    boolean exProblemList = defrm.getExProblemList();
    boolean exRiskFactors = defrm.getExRiskFactors();
    boolean exAllergiesAndAdverseReactions = defrm.getExAllergiesAndAdverseReactions();
    boolean exMedicationsAndTreatments = defrm.getExMedicationsAndTreatments();
    boolean exImmunizations = defrm.getExImmunizations();
    boolean exLaboratoryResults = defrm.getExLaboratoryResults();
    boolean exAppointments = defrm.getExAppointments();
    boolean exClinicalNotes = defrm.getExClinicalNotes();
    boolean exReportsReceived = defrm.getExReportsReceived();
    boolean exAuditInformation = defrm.getExAuditInformation();
    boolean exCareElements = defrm.getExCareElements();

    ArrayList list = new ArrayList();
    if (this.demographicNo==null) {
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
	list.add(this.demographicNo);
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

    RxPrescriptionData prescriptData = new RxPrescriptionData();
    RxPrescriptionData.Prescription [] arr = null;

    CommonLabTestValues comLab = new CommonLabTestValues();
    PreventionData pd = new PreventionData();
    DemographicExt ext = new DemographicExt();

    String ffwd = "fail";
    String tmpDir = oscarp.getProperty("TMP_DIR");
    if (!Util.checkDir(tmpDir)) {
        System.out.println("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
    } else {
	XmlOptions options = new XmlOptions();
	options.put( XmlOptions.SAVE_PRETTY_PRINT );
	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );
	options.setSaveOuter();
        
        Vector err = new Vector();
	File[] files = new File[list.size()];
	String data="";
	for(int i = 0 ; i < list.size(); i++){
	    Object obj = list.get(i);
	    if ( obj instanceof String){
		this.demographicNo = (String) obj;
	    }else{
		ArrayList l2 = (ArrayList) obj;
		this.demographicNo = (String) l2.get(0);
	    } 
	    if (Util.empty(this.demographicNo)) {
		this.demographicNo="";
		err.add("Error! No Demographic Number");
	    } else {
		// DEMOGRAPHICS
		DemographicData.Demographic demographic = d.getDemographic(this.demographicNo);
		Hashtable demoExt = ext.getAllValuesForDemo(this.demographicNo);
		
		OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
		OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
		PatientRecord patientRec = omdCds.addNewPatientRecord();
		Demographics demo = patientRec.addNewDemographics();
		
		demo.setUniqueVendorIdSequence(this.demographicNo);
                
		cdsDt.PersonNameStandard personName = demo.addNewNames();
		cdsDt.PersonNameStandard.LegalName legalName = personName.addNewLegalName();
		cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
		cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
		legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);
		
		data = Util.noNull(demographic.getFirstName());
		String demoName = data.replace(" ","");
		if (Util.filled(data)) {
		    firstName.setPart(data);
		    firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
		    firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
		} else {
		    err.add("Error! No First Name for Patient "+this.demographicNo);
		}
		data = Util.noNull(demographic.getLastName());
		demoName += data.replace(" ","");
		if (Util.filled(data)) {
		    lastName.setPart(data);
		    lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
		    lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
		} else {
		    err.add("Error! No Last Name for Patient "+this.demographicNo);
		}
		if (Util.empty(setName)) setName = demoName;
		
		data = demographic.getTitle();
		if (Util.filled(data)) {
		    if (data.equalsIgnoreCase("MISS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MISS);
		    if (data.equalsIgnoreCase("MR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MR);
		    if (data.equalsIgnoreCase("MRS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MRS);
		    if (data.equalsIgnoreCase("MS")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MS);
		    if (data.equalsIgnoreCase("MSSR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.MSSR);
		    if (data.equalsIgnoreCase("PROF")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.PROF);
		    if (data.equalsIgnoreCase("REEVE")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.REEVE);
		    if (data.equalsIgnoreCase("REV")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.REV);
		    if (data.equalsIgnoreCase("RT_HON")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.RT_HON);
		    if (data.equalsIgnoreCase("SEN")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SEN);
		    if (data.equalsIgnoreCase("SGT")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SGT);
		    if (data.equalsIgnoreCase("SR")) personName.setNamePrefix(cdsDt.PersonNamePrefixCode.SR);
		} else {
		    err.add("Error! No Name Prefix for Patient "+this.demographicNo);
		}
		
		data = demographic.getOfficialLang();
		if (Util.filled(data)) {
		    if (data.equalsIgnoreCase("English"))     demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.ENG);
		    else if (data.equalsIgnoreCase("French")) demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.FRE);
		} else {
		    err.add("Error! No Preferred Official Language for Patient "+this.demographicNo);
		}
		
		data = demographic.getSpokenLang();
		if (Util.filled(data)) {
		    demo.setPreferredSpokenLanguage(data);
		}
		
		data = demographic.getSex();
		if (Util.filled(data)) {
		    demo.setGender(cdsDt.Gender.Enum.forString(data));
		} else {
		    err.add("Error! No Gender for Patient "+this.demographicNo);
		}

		data = Util.noNull(demographic.getRosterStatus());
		if (Util.empty(data)) {
		    data = "";
		    err.add("Error! No Enrollment Status for Patient "+this.demographicNo);
		}
		data = data.equals("RO") ? "1" : "0";
		demo.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(data));
		
		data = Util.noNull(demographic.getPatientStatus());
		if (Util.empty(data)) {
		    data = "";
		    err.add("Error! No Person Status Code for Patient "+this.demographicNo);
		}
		if (data.equals("AC")) data = "A";
		else if (data.equals("IN")) data = "I";
		else if (data.equals("DE")) data = "D";
		else data = "O";
		demo.setPersonStatusCode(cdsDt.PersonStatus.Enum.forString(data));

		data = Util.noNull(demographic.getDob("-"));
		demo.addNewDateOfBirth().setFullDate(Util.calDate(data));
		if (UtilDateUtilities.StringToDate(data)==null) {
		    err.add("Error! No Date Of Birth for Patient "+this.demographicNo);
		} else if (UtilDateUtilities.StringToDate(data)==null) {
		    err.add("Note: Not exporting invalid Date of Birth for Patient "+this.demographicNo);
		}
		data = demographic.getHCRenewDate(); //HCRenewDate = RosterDate
		if (UtilDateUtilities.StringToDate(data)!=null) {
		    demo.addNewEnrollmentDate().setFullDate(Util.calDate(data));
		}
		data = demographic.getEndDate();
		if (UtilDateUtilities.StringToDate(data)!=null) {
		    demo.addNewEnrollmentTerminationDate().setFullDate(Util.calDate(data));
		}
		data = demographic.getChartNo();
		if (Util.filled(data)) demo.setChartNumber(data);

		data = demographic.getEmail();
		if (Util.filled(data)) demo.setEmail(data);

		String providerNo = demographic.getProviderNo();
		if (Util.filled(providerNo)) {
		    Demographics.PrimaryPhysician pph = demo.addNewPrimaryPhysician();
		    ProviderData prvd = new ProviderData(providerNo);
		    pph.setOHIPPhysicianId(prvd.getOhip_no());
		    Util.writeNameSimple(pph.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
		}
		if (Util.filled(demographic.getJustHIN())) {
		    cdsDt.HealthCard healthCard = demo.addNewHealthCard();
		    healthCard.setNumber(demographic.getJustHIN());
		    healthCard.setProvinceCode(Util.setProvinceCode(demographic.getHCType()));
		    if (healthCard.getProvinceCode()==null) {
			err.add("Error! No Health Card Province Code for Patient "+this.demographicNo);
		    }
		    if (Util.filled(demographic.getVersionCode())) healthCard.setVersion(demographic.getVersionCode());
		    data = demographic.getEffDate();
		    if (UtilDateUtilities.StringToDate(data)!=null) {
			healthCard.setExpirydate(Util.calDate(data));
		    }
		}
		if (Util.filled(demographic.getAddress())) {
		    cdsDt.Address addr = demo.addNewAddress();		
		    cdsDt.AddressStructured address = addr.addNewStructured();

		    addr.setAddressType(cdsDt.AddressType.R);
		    address.setLine1(demographic.getAddress());
		    if (Util.filled(demographic.getCity()) || Util.filled(demographic.getProvince()) || Util.filled(demographic.getPostal())) {
			address.setCity(Util.noNull(demographic.getCity()));
			address.setCountrySubdivisionCode(Util.setCountrySubDivCode(demographic.getProvince()));
			address.addNewPostalZipCode().setPostalCode(Util.noNull(demographic.getPostal()).replace(" ",""));
		    }
		}
		String phoneNo = demographic.getPhone();
		if (Util.filled(phoneNo) && phoneNo.length()>=7) {
		    cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
		    phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
		    phoneResident.setPhoneNumber(phoneNo);
		    data = (String) demoExt.get("hPhoneExt");
		    if (data!=null) {
			if (data.length()>5) {
			    data = data.substring(0,5);
			    err.add("Note: Home phone extension too long - trimmed for Patient "+this.demographicNo);
			}
			phoneResident.setExtension(data);
		    }
		}
		phoneNo = demographic.getPhone2();
		if (Util.filled(phoneNo) && phoneNo.length()>=7) {
		    cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
		    phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
		    phoneWork.setPhoneNumber(phoneNo);
		    data = (String) demoExt.get("wPhoneExt");
		    if (data!=null) {
			if (data.length()>5) {
			    data = data.substring(0,5);
			    err.add("Note: Work phone extension too long, export trimmed for Patient "+this.demographicNo);
			}
			phoneWork.setExtension(data);
		    }
		}
		phoneNo = (String)demoExt.get("demo_cell");
		if (Util.filled(phoneNo) && phoneNo.length()>=7) {
		    cdsDt.PhoneNumber phoneCell = demo.addNewPhoneNumber();
		    phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
		    phoneCell.setPhoneNumber(phoneNo);
		}
		demoExt = null;
		
		DemographicRelationship demoRel = new DemographicRelationship();
		ArrayList demoR = demoRel.getDemographicRelationships(this.demographicNo);
		for (int j=0; j<demoR.size(); j++) {
		    Hashtable r = (Hashtable) demoR.get(j);
		    data = (String) r.get("demographic_no");
		    if (Util.filled(data)) {
			DemographicData.Demographic relDemo = d.getDemographic(data);
			Hashtable relDemoExt = ext.getAllValuesForDemo(data);

			Demographics.Contact contact = demo.addNewContact();
			Util.writeNameSimple(contact.addNewName(), relDemo.getFirstName(), relDemo.getLastName());
			if (Util.empty(relDemo.getFirstName())) {
			    err.add("Error! No First Name for contact ("+j+") for Patient "+this.demographicNo);
			}
			if (Util.empty(relDemo.getLastName())) {
			    err.add("Error! No Last Name for contact ("+j+") for Patient "+this.demographicNo);
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
			else contact.setContactPurpose(cdsDt.ContactPersonPurpose.O);

			if (Util.filled(relDemo.getEmail())) contact.setEmailAddress(relDemo.getEmail());
			if (Util.filled((String)r.get("notes"))) contact.setNote((String)r.get("notes"));
			
			phoneNo = relDemo.getPhone();
			if (Util.filled(phoneNo) && phoneNo.length()>=7) {
			    cdsDt.PhoneNumber phoneRes = contact.addNewPhoneNumber();
			    phoneRes.setPhoneNumberType(cdsDt.PhoneNumberType.R);
			    phoneRes.setPhoneNumber(phoneNo);
			    data = (String) relDemoExt.get("hPhoneExt");
			    if (Util.filled(data)) {
				if (data.length()>5) {
				    data = data.substring(0,5);
				    err.add("Note: Home phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+this.demographicNo);
				}
				phoneRes.setExtension(data);
			    }
			}
			phoneNo = relDemo.getPhone2();
			if (Util.filled(phoneNo) && phoneNo.length()>=7) {
			    cdsDt.PhoneNumber phoneW = contact.addNewPhoneNumber();
			    phoneW.setPhoneNumberType(cdsDt.PhoneNumberType.W);
			    phoneW.setPhoneNumber(phoneNo);
			    data = (String) relDemoExt.get("wPhoneExt");
			    if (Util.filled(data)) {
				if (data.length()>5) {
				    data = data.substring(0,5);
				    err.add("Note: Work phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+this.demographicNo);
				}
				phoneW.setExtension(data);
			    }
			}
			phoneNo = (String)relDemoExt.get("demo_cell");
			if (Util.filled(phoneNo) && phoneNo.length()>=7) {
			    cdsDt.PhoneNumber phoneCell = contact.addNewPhoneNumber();
			    phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
			    phoneCell.setPhoneNumber(phoneNo);
			}
			relDemoExt = null;
		    }
		}
		
		HttpSession se = request.getSession();
		WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
		CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
		
		String alerts = "";
		List<CaseManagementNote> lcmn = cmm.getNotes(this.demographicNo);
		for (CaseManagementNote cmn : lcmn) {
		    String famHist="", socHist="", medHist="", concerns="", reminders="", riskFactors="", encounter="", annotation="", summary="";
		    Set<CaseManagementIssue> sisu = cmn.getIssues();
		    boolean systemIssue = false;
		    for (CaseManagementIssue isu : sisu) {
			String _issue = isu.getIssue()!=null ? isu.getIssue().getCode() : "";
                        if (_issue.equals("SocHistory")) {
			    systemIssue = true;
                            socHist = cmn.getNote();
                            break;
			} else if (_issue.equals("FamHistory")) {
			    systemIssue = true;
                            famHist = cmn.getNote();
                            break;
                        } else if (_issue.equals("MedHistory")) {
			    systemIssue = true;
                            medHist = cmn.getNote();
                            break;
                        } else if (_issue.equals("Concerns")) {
			    systemIssue = true;
                            concerns = cmn.getNote();
                            break;
                        } else if (_issue.equals("Reminders")) {
			    systemIssue = true;
                            reminders = cmn.getNote();
                            break;
			} else if (_issue.equals("RiskFactors")) {
			    systemIssue = true;
			    riskFactors = cmn.getNote();
			    break;
			} else continue;
		    }
		    if (!systemIssue && cmm.getLinkByNote(cmn.getId()).isEmpty()) { //this is not an annotation
                            encounter = cmn.getNote();
		    }
		    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.CASEMGMTNOTE, cmn.getId());
		    if (cml!=null) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			annotation = n.getNote();
		    }
		    List<CaseManagementNoteExt> cmeList = cmm.getExtByNote(cmn.getId());
		    
		if (exPersonalHistory) {
		    // PERSONAL HISTORY (SocHistory)
		    if (Util.filled(socHist)) {
			summary = socHist;
			summary = Util.appendLine(summary, "Notes: ", annotation);
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                            }
			}
			patientRec.addNewPersonalHistory().setCategorySummaryLine(summary);
		    }
		}
		if (exFamilyHistory) {
		    // FAMILY HISTORY (FamHistory)
		    if (Util.filled(famHist)) {
			FamilyHistory fHist = patientRec.addNewFamilyHistory();
			fHist.setDiagnosisProblemDescription(famHist);
			summary = "Problem Description: "+famHist;
			
			boolean diagnosisAssigned = false;
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
				if (diagnosisAssigned) {
				    summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
				} else {
				    cdsDt.Code diagnosis = fHist.addNewDiagnosisCode();
				    diagnosis.setCodingSystem(codeSystem);
				    diagnosis.setValue(isu.getIssue().getCode());
				    diagnosis.setDescription(isu.getIssue().getDescription());
				    summary = Util.appendLine(summary, "Diagnosis: ", diagnosis.getDescription());
				    diagnosisAssigned = true;
				}
                            }
			}
			boolean bSTARTDATE=false, bAGEATONSET=false, bRELATIONSHIP=false, bTREATMENT=false;
			for (CaseManagementNoteExt cme : cmeList) {
			    if (cme.getKeyVal().equals(cme.STARTDATE)) {
				if (bSTARTDATE) continue;
				if (cme.getDateValue()!=null) {
				    fHist.addNewStartDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, cme.STARTDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bSTARTDATE = true;
			    } else if (cme.getKeyVal().equals(cme.AGEATONSET)) {
				if (bAGEATONSET) continue;
				if (Util.filled(cme.getValue())) {
				    fHist.setAgeAtOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
				    summary = Util.appendLine(summary, cme.AGEATONSET+": ", cme.getValue());
				}
				bAGEATONSET = true;
			    } else if (cme.getKeyVal().equals(cme.RELATIONSHIP)) {
				if (bRELATIONSHIP) continue;
				if (Util.filled(cme.getValue())) {
				    fHist.setRelationship(cme.getValue());
				    summary = Util.appendLine(summary, cme.RELATIONSHIP+": ", cme.getValue());
				}
				bRELATIONSHIP = true;
			    } else if (cme.getKeyVal().equals(cme.TREATMENT)) {
				if (bTREATMENT) continue;
				if (Util.filled(cme.getValue())) {
				    fHist.setTreatment(cme.getValue());
				    summary = Util.appendLine(summary, cme.TREATMENT+": ", cme.getValue());
				}
				bTREATMENT = true;
			    }
			}
			if (Util.filled(annotation)) {
			    fHist.setNotes(annotation);
			    summary = Util.appendLine(summary, "Notes: ", annotation);
			}
			fHist.setCategorySummaryLine(summary);
		    }
		}
		if (exPastHealth) {
		    // PAST HEALTH (MedHistory)
		    if (Util.filled(medHist)) {
			PastHealth pHealth = patientRec.addNewPastHealth();
			summary = "Problem Description: " + medHist;
			
                        boolean diagnosisAssigned = false;
			for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
				if (diagnosisAssigned) {
				    summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
				} else {
				    cdsDt.Code diagnosis = pHealth.addNewDiagnosisOrProcedureCode();
				    diagnosis.setCodingSystem(codeSystem);
				    diagnosis.setValue(isu.getIssue().getCode());
				    diagnosis.setDescription(isu.getIssue().getDescription());
				    summary = Util.appendLine(summary, "Diagnosis: ", diagnosis.getDescription());
				    diagnosisAssigned = true;
				}
                            }
			}
			boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bTREATMENT=false;
			for (CaseManagementNoteExt cme : cmeList) {
			    if (cme.getKeyVal().equals(cme.STARTDATE)) {
				if (bSTARTDATE) continue;
				if (cme.getDateValue()!=null) {
				    pHealth.addNewOnsetOrEventDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, "Onset/Event Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bSTARTDATE = true;
			    } else if (cme.getKeyVal().equals(cme.RESOLUTIONDATE)) {
				if (bRESOLUTIONDATE) continue;
				if (cme.getDateValue()!=null) {
				    pHealth.addNewResolvedDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, "Resolved Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bRESOLUTIONDATE = true;
			    } else if (cme.getKeyVal().equals(cme.TREATMENT)) {
				if (bTREATMENT) continue;
				if (Util.filled(cme.getValue())) {
				    medHist = Util.appendLine(medHist, "Procedure/Intervention: ", cme.getValue());
				    summary = Util.appendLine(summary, "Procedure/Intervention: ", cme.getValue());
				}
				bTREATMENT = true;
			    }
			}
			pHealth.setPastHealthProblemDescriptionOrProcedures(medHist);
			if (Util.filled(annotation)) {
			    pHealth.setNotes(annotation);
			    summary = Util.appendLine(summary, "Notes: ", annotation);
			}
			pHealth.setCategorySummaryLine(summary);
		    }
		}
		if (exProblemList) {
		    // PROBLEM LIST (Concerns)
		    if (Util.filled(concerns)) {
			ProblemList pList = patientRec.addNewProblemList();
			pList.setProblemDescription(concerns);
			summary = "Problem Description: "+concerns;
			
			boolean diagnosisAssigned = false;
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
				if (diagnosisAssigned) {
				    summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
				} else {
				    cdsDt.Code diagnosis = pList.addNewDiagnosisCode();
				    diagnosis.setCodingSystem(codeSystem);
				    diagnosis.setValue(isu.getIssue().getCode());
				    diagnosis.setDescription(isu.getIssue().getDescription());
				    summary = Util.appendLine(summary, "Diagnosis: ", diagnosis.getDescription());
				    diagnosisAssigned = true;
				}
                            }
			}
			boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bPROBLEMSTATUS=false;
			for (CaseManagementNoteExt cme : cmeList) {
			    if (cme.getKeyVal().equals(cme.STARTDATE)) {
				if (bSTARTDATE) continue;
				pList.addNewOnsetDate().setFullDate(Util.calDate(cme.getDateValue()));
				summary = Util.appendLine(summary, "Onset Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				if (cme.getDateValue()==null) {
				    err.add("Error! No Onset Date for Problem List for Patient "+this.demographicNo);
				}
				bSTARTDATE = true;
			    } else if (cme.getKeyVal().equals(cme.RESOLUTIONDATE)) {
				if (bRESOLUTIONDATE) continue;
				if (cme.getDateValue()!=null) {
				    pList.addNewResolutionDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, cme.RESOLUTIONDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bRESOLUTIONDATE = true;
			    } else if (cme.getKeyVal().equals(cme.PROBLEMSTATUS)) {
				if (bPROBLEMSTATUS) continue;
				if (Util.filled(cme.getValue())) {
				    pList.setProblemStatus(cme.getValue());
				    summary = Util.appendLine(summary, cme.PROBLEMSTATUS+": ", cme.getValue());
				}
				bPROBLEMSTATUS = true;
			    }
			}
			
			if (Util.filled(annotation)) {
			    pList.setNotes(annotation);
			    summary = Util.appendLine(summary, "Notes: ", annotation);
			}
			pList.setCategorySummaryLine(summary);
		    }
		}
		if (exRiskFactors) {
		    // RISK FACTORS
		    if (Util.filled(riskFactors)) {
			RiskFactors rFact = patientRec.addNewRiskFactors();
			rFact.setRiskFactor(riskFactors);
			summary = "Risk Factor: "+riskFactors;
			
			boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bAGEATONSET=false, bEXPOSUREDETAIL=false;
			for (CaseManagementNoteExt cme : cmeList) {
			    if (cme.getKeyVal().equals(cme.STARTDATE)) {
				if (bSTARTDATE) continue;
				if (cme.getDateValue()!=null) {
				    rFact.addNewStartDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, cme.STARTDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bSTARTDATE = true;
			    } else if (cme.getKeyVal().equals(cme.RESOLUTIONDATE)) {
				if (bRESOLUTIONDATE) continue;
				if (cme.getDateValue()!=null) {
				    rFact.addNewEndDate().setDateTime(Util.calDate(cme.getDateValue()));
				    summary = Util.appendLine(summary, "End Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
				}
				bRESOLUTIONDATE = true;
			    } else if (cme.getKeyVal().equals(cme.AGEATONSET)) {
				if (bAGEATONSET) continue;
				if (Util.filled(cme.getValue())) {
				    rFact.setAgeOfOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
				    summary = Util.appendLine(summary, cme.AGEATONSET+": ", cme.getValue());
				}
				bAGEATONSET = true;
			    } else if (cme.getKeyVal().equals(cme.EXPOSUREDETAIL)) {
				if (bEXPOSUREDETAIL) continue;
				if (Util.filled(cme.getValue())) {
				    rFact.setExposureDetails(cme.getValue());
				    summary = Util.appendLine(summary, cme.EXPOSUREDETAIL+": ", cme.getValue());
				}
				bEXPOSUREDETAIL = true;
			    }
			}
			if (Util.filled(annotation)) {
			    rFact.setNotes(annotation);
			    summary = Util.appendLine(summary, "Notes: ", annotation);
			}
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                            }
			}
			rFact.setCategorySummaryLine(summary);
		    }
		}
		if (exClinicalNotes) {
		    // CLINCAL NOTES
		    if (Util.filled(encounter)) {
			ClinicalNotes cNote = patientRec.addNewClinicalNotes();
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                encounter = Util.appendLine(encounter, "Diagnosis: ", isu.getIssue().getDescription());
                            }
			}
			cNote.setMyClinicalNotesContent(encounter);
			if (cmn.getUpdate_date()!=null) {
			    cNote.addNewEnteredDateTime().setDateTime(Util.calDate(cmn.getUpdate_date()));
			}
			if (cmn.getObservation_date()!=null) {
			    cNote.addNewEventDateTime().setDateTime(Util.calDate(cmn.getObservation_date()));
			}
			if (Util.filled(cmn.getProviderNo())) {
			    ClinicalNotes.PrincipalAuthor pAuthor = cNote.addNewPrincipalAuthor();
			    ProviderData prvd = new ProviderData(cmn.getProviderNo());
			    Util.writeNameSimple(pAuthor.addNewName(), Util.noNull(prvd.getFirst_name()), Util.noNull(prvd.getLast_name()));
			    pAuthor.setOHIPPhysicianId(Util.noNull(prvd.getOhip_no()));
			}
			if (Util.filled(cmn.getSigning_provider_no())) {
			    ProviderData prvd = new ProviderData(cmn.getSigning_provider_no());
			    cNote.setSigningOHIPPhysicianId(Util.noNull(prvd.getOhip_no()));
			}
		    }
		}
		    // ALERTS AND SPECIAL NEEDS (Reminders)
		    if (Util.filled(reminders)) {
			alerts = Util.appendLine(alerts, reminders);
			
			boolean bSTARTDATE=false, bRESOLUTIONDATE=false;
			for (CaseManagementNoteExt cme : cmeList) {
			    if (cme.getKeyVal().equals(cme.STARTDATE)) {
				if (bSTARTDATE) continue;
				if (cme.getDateValue()!=null) {
				    alerts = Util.appendLine(alerts, "Date Active: ", UtilDateUtilities.DateToString(cme.getDateValue()));
				}
				bSTARTDATE = true;
			    } else if (cme.getKeyVal().equals(cme.RESOLUTIONDATE)) {
				if (bRESOLUTIONDATE) continue;
				if (cme.getDateValue()!=null) {
				    alerts = Util.appendLine(alerts, "End Date: ", UtilDateUtilities.DateToString(cme.getDateValue()));
				}
				bRESOLUTIONDATE = true;
			    }
			}
			if (Util.filled(annotation)) {
			    alerts = Util.appendLine(alerts, "Notes: ", annotation);
			}
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                summary = Util.appendLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                            }
			}
			alerts = Util.appendLine(alerts, "----------------------------------------");
		    }
		}
		if (Util.filled(alerts)) {
		    demo.setNoteAboutPatient(alerts);
		    demo.setPatientWarningFlags("1");
		}
		
	    if (exAllergiesAndAdverseReactions) {
		// ALLERGIES & ADVERSE REACTIONS
		RxPatientData.Patient.Allergy[] allergies = new RxPatientData().getPatient(this.demographicNo).getAllergies();
		for (int j=0; j<allergies.length; j++) {
		    AllergiesAndAdverseReactions alr = patientRec.addNewAllergiesAndAdverseReactions();
		    String aSummary = "";

		    data = allergies[j].getAllergy().getDESCRIPTION();
		    if (Util.filled(data)) {
			alr.setOffendingAgentDescription(data);
			aSummary = "Offending Agent Description: " + data;
		    }
		    data = allergies[j].getAllergy().getRegionalIdentifier();
		    if (Util.filled(data) && !data.trim().equalsIgnoreCase("null")) {
			cdsDt.Code drugCode = alr.addNewCode();
			drugCode.setCodingSystem("DIN");
			drugCode.setValue(data);
			aSummary = Util.appendLine(aSummary, "DIN: ", data);
		    }
		    data = String.valueOf(allergies[j].getAllergy().getTYPECODE());
		    if (Util.filled(data)) {
                        if (data.equals("0")) {
                            //alr.setReactionType(cdsDt.AdverseReactionType.AL);
                            alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.ND);
                        } else {
                            //alr.setReactionType(cdsDt.AdverseReactionType.AR);
                            if (data.equals("13")) {
                                alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.DR);
                            } else {
                                alr.setPropertyOfOffendingAgent(cdsDt.PropertyOfOffendingAgent.ND);
                            }
                        }
			aSummary = Util.appendLine(aSummary,"Property of Offending Agent: ",alr.getPropertyOfOffendingAgent().toString());
		    }
		    data = allergies[j].getAllergy().getReaction();
		    if (Util.filled(data)) {
			alr.setReaction(data);
			aSummary = Util.appendLine(aSummary, "Reaction: ", data);
		    }
		    data = allergies[j].getAllergy().getSeverityOfReaction();
		    if (Util.filled(data)) {
			if (data.equals("1")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
			} else if (data.equals("2")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MO);
			} else if (data.equals("3")) {
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.LT);
			} else { //SeverityOfReaction==0
			    alr.setSeverity(cdsDt.AdverseReactionSeverity.MI);
			    err.add("Note: Severity Of Allergy Reaction [Unknown] exported as [Mild] for Patient "+this.demographicNo+" ("+(j+1)+")");
			}
			aSummary = Util.appendLine(aSummary,"Adverse Reaction Severity: ",alr.getSeverity().toString());
		    }
		    if (allergies[j].getAllergy().getStartDate()!=null) {
			alr.addNewStartDate().setFullDate(Util.calDate(allergies[j].getAllergy().getStartDate()));
			aSummary = Util.appendLine(aSummary,"Start Date: ",UtilDateUtilities.DateToString(allergies[j].getAllergy().getStartDate()));
		    }
		    if (allergies[j].getEntryDate()!=null) {
			alr.addNewRecordedDate().setFullDate(Util.calDate(allergies[j].getEntryDate()));
			aSummary = Util.appendLine(aSummary,"Recorded Date: ",UtilDateUtilities.DateToString(allergies[j].getEntryDate(),"yyyy-MM-dd"));
		    }
		    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.ALLERGIES, (long)allergies[j].getAllergyId());
		    if (cml!=null) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			alr.setNotes(Util.noNull(n.getNote()));
			aSummary = Util.appendLine(aSummary, "Notes: ", n.getNote());
		    }
		    
		    if (Util.empty(aSummary)) {
			err.add("Error! No Category Summary Line (Allergies & Adverse Reactions) for Patient "+this.demographicNo+" ("+(j+1)+")");
		    }
		    alr.setCategorySummaryLine(aSummary);
		}
	    }
		
	    if (exImmunizations) {
		// IMMUNIZATIONS
		ArrayList prevList2 = pd.getPreventionData(this.demographicNo);
		for (int k =0 ; k < prevList2.size(); k++){
		    Hashtable a = (Hashtable) prevList2.get(k);  
		    if (a != null && inject.contains((String) a.get("type")) ){
			Immunizations immu = patientRec.addNewImmunizations();
			data = Util.noNull((String) a.get("type"));
			if (Util.empty(data)) {
			    err.add("Error! No Immunization Name for Patient "+this.demographicNo+" ("+(k+1)+")");
			}
			immu.setImmunizationName(data);
			String imSummary = "Immunization Name: "+data;

			data = (String) a.get("refused");
			if (Util.empty(data)) {
			    immu.addNewRefusedFlag();
			    err.add("Error! No Refused Flag for Patient "+this.demographicNo+" ("+(k+1)+")");
			} else {
			    immu.addNewRefusedFlag().setBoolean(Util.convert10toboolean(data));
			    imSummary = Util.appendLine(imSummary, "Refused Flag: ", Util.convert10toboolean(data)?"Y":"N");
			}
			
			data = (String) a.get("prevention_date");
			if (UtilDateUtilities.StringToDate(data)!=null) {
			    immu.addNewDate().setFullDate(Util.calDate(data));
			    imSummary = Util.appendLine(imSummary, "Date: ", data);
			}

			Hashtable extraData = pd.getPreventionById((String) a.get("id"));
			if (Util.filled((String)extraData.get("manufacture"))) immu.setManufacturer((String)extraData.get("manufacture"));
			if (Util.filled((String)extraData.get("lot"))) immu.setLotNumber((String)extraData.get("lot"));
			if (Util.filled((String)extraData.get("route"))) immu.setRoute((String)extraData.get("route"));
			if (Util.filled((String)extraData.get("location"))) immu.setSite((String)extraData.get("location"));
			if (Util.filled((String)extraData.get("dose"))) immu.setDose((String)extraData.get("dose"));
			if (Util.filled((String)extraData.get("comments"))) immu.setNotes((String)extraData.get("comments"));
			
			imSummary = Util.appendLine(imSummary, "Manufacturer: ", immu.getManufacturer());
			imSummary = Util.appendLine(imSummary, "Lot No: ", immu.getLotNumber());
			imSummary = Util.appendLine(imSummary, "Route: ", immu.getRoute());
			imSummary = Util.appendLine(imSummary, "Site: ", immu.getSite());
			imSummary = Util.appendLine(imSummary, "Dose: ", immu.getDose());
			imSummary = Util.appendLine(imSummary, "Notes: ", immu.getNotes());
			
			if (Util.empty(imSummary)) {
			    err.add("Error! No Category Summary Line (Immunization) for Patient "+this.demographicNo+" ("+(k+1)+")");
			}
			immu.setCategorySummaryLine(Util.noNull(imSummary));
			extraData = null;
		    }                                                       
		    a = null;
		}
		prevList2 = null;
	    }
		
	    if (exMedicationsAndTreatments) {
		// MEDICATIONS & TREATMENTS
		arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(this.demographicNo));
		for (int p = 0; p < arr.length; p++){
		    MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
		    String mSummary = "";

		    if (arr[p].getWrittenDate()!=null) {
			medi.addNewPrescriptionWrittenDate().setFullDate(Util.calDate(arr[p].getWrittenDate()));
			mSummary = "Prescription Written Date: "+UtilDateUtilities.DateToString(arr[p].getWrittenDate(),"yyyy-MM-dd");
		    }
		    if (arr[p].getRxDate()!=null) {
			medi.addNewStartDate().setFullDate(Util.calDate(arr[p].getRxDate()));
			mSummary = Util.appendLine(mSummary,"Start Date: ",UtilDateUtilities.DateToString(arr[p].getRxDate(),"yyyy-MM-dd"));
		    }
		    if (arr[p].getEndDate()!=null) {
			medi.addNewEndDate().setFullDate(Util.calDate(arr[p].getEndDate()));
			mSummary = Util.appendLine(mSummary,"End Date: ",UtilDateUtilities.DateToString(arr[p].getEndDate(),"yyyy-MM-dd"));
		    }
		    data = arr[p].getRegionalIdentifier();
		    if (Util.filled(data)) {
			medi.setDrugIdentificationNumber(data);
			mSummary = Util.appendLine(mSummary, "DIN: ", data);
		    }
		    String drugName = Util.noNull(arr[p].getDrugName());
		    if (Util.empty(drugName)) {
			err.add("Error! No Drug Name for Patient "+this.demographicNo+" ("+(p+1)+")");
		    }
		    medi.setDrugName(drugName);
		    mSummary = Util.appendLine(mSummary, "Drug Name: ", drugName);
		    
		    if (Util.filled(arr[p].getDosage())) {
			String strength = arr[p].getDosage();
			int sep = strength.indexOf("/");
			
			strength = sep<0 ? strength : strength.substring(0,sep);
			if (sep>0) {
			    strength = strength.substring(0,sep);
			    err.add("Note: Multiple components exist for Drug "+drugName+" for Patient "+this.demographicNo+". Exporting 1st one as Strength.");
			}
			cdsDt.DrugMeasure drugM = medi.addNewStrength();
			drugM.setAmount(strength.substring(0,strength.indexOf(" ")));
			drugM.setUnitOfMeasure(strength.substring(strength.indexOf(" ")+1));
			mSummary = Util.appendLine(mSummary, "Strength: ", arr[p].getGenericName()+" "+strength);
		    }
		    if (Util.filled(arr[p].getDosageDisplay())) {
			data = arr[p].getDosageDisplay()+" "+Util.noNull(arr[p].getUnit());
			medi.setDosage(data);
			mSummary = Util.appendLine(mSummary, "Dosage: ", data);
		    }
		    if (Util.filled(medi.getDrugName()) || Util.filled(medi.getDrugIdentificationNumber())) {
			medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
			mSummary = Util.appendLine(mSummary, "Number of Refills: ", String.valueOf(arr[p].getRepeat()));
		    }
		    if (arr[p].getLastRefillDate()!=null) {
			medi.addNewLastRefillDate().setFullDate(Util.calDate(arr[p].getLastRefillDate()));
			mSummary = Util.appendLine(mSummary,"Last Refill Date: ", UtilDateUtilities.DateToString(arr[p].getLastRefillDate(),"yyyy-MM-dd"));
		    }
		    if (Util.filled(arr[p].getRoute())) {
			medi.setRoute(arr[p].getRoute());
			mSummary = Util.appendLine(mSummary, "Route: ", arr[p].getRoute());
		    }
		    if (Util.filled(arr[p].getDrugForm())) {
			medi.setForm(arr[p].getDrugForm());
			mSummary = Util.appendLine(mSummary, "Form: ", arr[p].getDrugForm());
		    }
		    if (Util.filled(arr[p].getFreqDisplay())) {
			medi.setFrequency(arr[p].getFreqDisplay());
			mSummary = Util.appendLine(mSummary, "Frequency: ", arr[p].getFreqDisplay());
		    }
		    data = arr[p].getDuration();
		    if (Util.filled(data)) {
			String durunit = Util.noNull(arr[p].getDurationUnit());
			Integer fctr = 1;
			if (durunit.equals("W")) fctr = 7;
			else if (durunit.equals("M")) fctr = 30;
			Integer meddur = Integer.parseInt(Util.getNum(data)) * fctr;
			
			medi.setDuration(meddur.toString());
			mSummary = Util.appendLine(mSummary, "Duration: ", meddur.toString()+" Day(s)");
		    }
		    if (Util.filled(arr[p].getQuantity())) {
			medi.setQuantity(arr[p].getQuantity());
			mSummary = Util.appendLine(mSummary, "Quantity: ", arr[p].getQuantity());
		    }
		    if (arr[p].getLongTerm()) {
			medi.addNewLongTermMedication().setBoolean(arr[p].getLongTerm());
			mSummary = Util.appendLine(mSummary, "Long Term Medication");
		    }
		    if (arr[p].getPastMed()) {
			medi.addNewPastMedications().setBoolean(arr[p].getPastMed());
			mSummary = Util.appendLine(mSummary, "Past Medcation");
		    }
                    cdsDt.YnIndicatorAndBlank pc = medi.addNewPatientCompliance();
                    if (arr[p].getPatientCompliance()==0) {
                        pc.setBlank(cdsDt.Blank.X);
                    } else {
                        pc.setBoolean(arr[p].getPatientCompliance()==1);
                        if (pc.getBoolean()) mSummary = Util.appendLine(mSummary, "Patient Compliance: ", "Yes");
                        else mSummary = Util.appendLine(mSummary, "Patient Compliance: ", "No");
                    }
                    data = arr[p].getOutsideProviderName();
                    if (Util.filled(data)) {
                        MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
                        pcb.setOHIPPhysicianId(Util.noNull(arr[p].getOutsideProviderOhip()));
                        Util.writeNameSimple(pcb.addNewName(), data);
                        mSummary = Util.appendLine(mSummary, "Prescribed by: ", Util.noNull(data));
                    } else {
                        data = arr[p].getProviderNo();
                        if (Util.filled(data)) {
                            MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
                            ProviderData prvd = new ProviderData(data);
                            pcb.setOHIPPhysicianId(prvd.getOhip_no());
                            Util.writeNameSimple(pcb.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
                            mSummary = Util.appendLine(mSummary, "Prescribed by: ", Util.noNull(prvd.getFirst_name())+" "+Util.noNull(prvd.getLast_name()));
                        }
                    }
		    data = arr[p].getSpecial();
		    if (Util.filled(data)) {
			data = Util.extractDrugInstr(data);
			medi.setPrescriptionInstructions(data);
			mSummary = Util.appendLine(mSummary, "Prescription Instructions: ", data);
		    }
		    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.DRUGS, (long)arr[p].getDrugId());
		    if (cml!=null) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			medi.setNotes(Util.noNull(n.getNote()));
			mSummary = Util.appendLine(mSummary, "Notes: ", n.getNote());
		    }
		    
		    if (Util.empty(mSummary)) err.add("Error! No Category Summary Line (Medications & Treatments) for Patient "+this.demographicNo+" ("+(p+1)+")");
		    medi.setCategorySummaryLine(mSummary);
		}
		arr = null;
	    }
		
	    if (exLaboratoryResults) {
		// LABORATORY RESULTS
                List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(this.demographicNo);
		
                for (LabMeasurements labMea : labMeaList) {
		    
                    LaboratoryResults labResults = patientRec.addNewLaboratoryResults();
                    labResults.setLabTestCode(Util.noNull(labMea.getExtVal("identifier")));
                    labResults.setTestName(Util.noNull(labMea.getExtVal("name")));
		    labResults.setTestNameReportedByLab(Util.noNull(labMea.getExtVal("name")));
                    
                    labResults.setLaboratoryName(Util.noNull(labMea.getExtVal("labname")));
                    if (Util.empty(labResults.getLaboratoryName())) {
                        err.add("Error! No Laboratory Name for Lab Test "+labResults.getLabTestCode()+" for Patient "+this.demographicNo);
                    }
                    
                    cdsDt.DateFullOrPartial collDate = labResults.addNewCollectionDateTime();
                    String dateTime = labMea.getExtVal("datetime");
                    collDate.setDateTime(Util.calDate(dateTime));
                    if (UtilDateUtilities.StringToDate(data,"yyyy-MM-dd HH:mm:ss")==null) {
                        err.add("Error! No Collection Datetime for Lab Test "+labResults.getLabTestCode()+" for Patient "+this.demographicNo);
                    }
                    
                    labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.U);
                    data = Util.noNull(labMea.getExtVal("abnormal"));
                    if (data.equals("A")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Y);
                    if (data.equals("N")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.N);
                    
                    data = Util.noNull(labMea.getMeasure().getDataField());
                    if (Util.filled(data)) {
                        LaboratoryResults.Result result = labResults.addNewResult();
                        result.setValue(data);
                        data = labMea.getExtVal("unit");
                        if (Util.filled(data)) result.setUnitOfMeasure(data);
                    }
                    
                    data = Util.noNull(labMea.getExtVal("accession"));
                    if (Util.filled(data)) {
                        labResults.setAccessionNumber(data);
                    }
                    
                    data = Util.noNull(labMea.getExtVal("comments"));
                    if (Util.filled(data)) {
                        labResults.setNotesFromLab(data);
                    }
                    String range = Util.noNull(labMea.getExtVal("range"));
                    String min = Util.noNull(labMea.getExtVal("minimum"));
                    String max = Util.noNull(labMea.getExtVal("maximum"));
                    LaboratoryResults.ReferenceRange refRange = LaboratoryResults.ReferenceRange.Factory.newInstance();
                    if (Util.filled(range)) refRange.setReferenceRangeText(range);
                    else {
                        if (Util.filled(min)) refRange.setLowLimit(min);
                        if (Util.filled(max)) refRange.setHighLimit(max);
                    }
                    if (refRange.getLowLimit()!=null || refRange.getHighLimit()!=null || refRange.getReferenceRangeText()!=null) {
                        labResults.setReferenceRange(refRange);
                    }
		    
		    if (labMea.getMeasure().getDateEntered()!=null) {
			labResults.addNewDateTimeResultReceivedByCMS().setDateTime(Util.calDate(labMea.getMeasure().getDateEntered()));
		    }
		    
                    String lab_no = Util.noNull(labMea.getExtVal("lab_no"));
                    if (Util.filled(lab_no)) {
                        Hashtable labRoutingInfo = ProviderLabRouting.getInfo(lab_no);
			
			String info = (String)labRoutingInfo.get("comment");
			if (Util.filled(info)) labResults.setPhysiciansNotes(info);
			info = (String)labRoutingInfo.get("provider_no");
			if (!"0".equals(info)) {
			    ProviderData pvd = new ProviderData(info);
			    if (Util.filled(pvd.getOhip_no())) {
				LaboratoryResults.ResultReviewer reviewer = labResults.addNewResultReviewer();
				reviewer.setOHIPPhysicianId(pvd.getOhip_no());
				Util.writeNameSimple(reviewer.addNewName(), pvd.getFirst_name(), pvd.getLast_name());
			    }
			}
			String timestamp = (String)labRoutingInfo.get("timestamp");
			if (UtilDateUtilities.StringToDate(timestamp,"yyyy-MM-dd HH:mm:ss")!=null) {
			    labResults.addNewDateTimeResultReviewed().setDateTime(Util.calDate(timestamp));
			}

                        Hashtable link = LabRequestReportLink.getLinkByReport("hl7TextMessage", Long.valueOf(lab_no));
                        Date reqDate = (Date)link.get("request_date");
                        if (reqDate!=null) labResults.addNewLabRequisitionDateTime().setDateTime(Util.calDate(reqDate));
                    }
                }
            }

/*
		ArrayList labs = comLab.findValuesForDemographic(this.demographicNo);
		for (int l = 0 ; l < labs.size(); l++){
		    Hashtable h = (Hashtable) labs.get(l);

		    LaboratoryResults labr = patientRec.addNewLaboratoryResults();
		    LaboratoryResults.Result labResult = labr.addNewResult();
		    LaboratoryResults.ReferenceRange labRef = labr.addNewReferenceRange();
		    
		    labr.setTestName((String) h.get("testName"));
		    data = (String) h.get("abn");
		    if (Util.empty(data)) {
			data = "U";
			err.add("Error! No Result Normal/Abnormal Flag for Patient "+this.demographicNo+" ("+(l+1)+")");
		    }
		    labr.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Enum.forString(data));

		    data = (String) h.get("location");
		    if (Util.empty(data)) {
			data = "";
			err.add("Error! No Laboratory Name for Patient "+this.demographicNo+" ("+(l+1)+")");
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

		    if (Util.filled((String)h.get("description"))) labr.setNotesFromLab((String) h.get("description"));
		    if (Util.filled((String)h.get("accession"))) labr.setAccessionNumber((String) h.get("accession"));
		    if (Util.filled((String)h.get("result"))) labResult.setValue((String) h.get("result"));
		    if (Util.filled((String)h.get("units"))) labResult.setUnitOfMeasure((String) h.get("units"));
		    if (Util.filled((String)h.get("min"))) labRef.setLowLimit((String) h.get("min"));
		    if (Util.filled((String)h.get("max"))) labRef.setHighLimit((String) h.get("max"));
		    
		    data = (String) h.get("collDate");
		    labr.addNewCollectionDateTime().setFullDate(Util.calDate(data));
		    if (UtilDateUtilities.StringToDate(data)==null) {
			err.add("Error! No Collection Date (Laboratory Results) for Patient "+this.demographicNo+" ("+(l+1)+")");
		    } else {
			if (UtilDateUtilities.StringToDate(data)==null) {
			    err.add("Note: Not exporting invalid Collection Date (Laboratory Results) for Patient "+this.demographicNo+" ("+(l+1)+")");
			}
		    }
		    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.LABTEST, (Long)h.get("id"));
		    if (cml!=null) {
			CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
			labr.setPhysiciansNotes(Util.noNull(n.getNote()));
		    }
		    h = null;
		}
		labs = null;
*/

	    if (exAppointments) {
		// APPOINTMENTS
		HttpSession session = request.getSession();
		Properties p = (Properties) session.getAttribute("oscarVariables");
		List appts = oscarSuperManager.populate("appointmentDao", "export_appt", new String[] {this.demographicNo});
		ApptData ap = null;
		for (int j=0; j<appts.size(); j++) {
		    ap = (ApptData)appts.get(j);
		    Appointments aptm = patientRec.addNewAppointments();
		    
		    aptm.setSequenceIndex(Integer.parseInt(ap.getAppointment_no()));
		    String apNotes = "Appointment No: "+ap.getAppointment_no();
		    
		    cdsDt.DateFullOrPartial apDate = aptm.addNewAppointmentDate();
		    apDate.setFullDate(Util.calDate(ap.getAppointment_date()));
		    if (ap.getAppointment_date()!=null) {
			apNotes = Util.appendLine(apNotes, "Date: ", UtilDateUtilities.DateToString(ap.getDateAppointmentDate(),"yyyy-MM-dd"));
		    } else {
			err.add("Error! No Appointment Date ("+j+") for Patient "+this.demographicNo);
		    }
		    
		    String startTime = ap.getStart_time();
		    aptm.setAppointmentTime(Util.calDate(ap.getStart_time()));
		    if (UtilDateUtilities.StringToDate(startTime,"HH:mm:ss")!=null) {
			apNotes = Util.appendLine(apNotes, "Start Time: ", startTime);
		    } else {
			err.add("Error! No Appointment Time ("+(j+1)+") for Patient "+this.demographicNo);
		    }
		    
		    long dLong = (ap.getDateEndTime().getTime()-ap.getDateStartTime().getTime())/60000+1;
		    BigInteger duration = BigInteger.valueOf(dLong); //duration in minutes
		    aptm.setDuration(duration);
		    apNotes = Util.appendLine(apNotes, "Duration: ", duration.toString())+" min";

		    if (Util.filled(ap.getStatus())) {
			ApptStatusData asd = new ApptStatusData();
			asd.setApptStatus(ap.getStatus());
			String msg = null;
                        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
                            msg = asd.getTitle();
                        else
                            msg = getResources(request).getMessage(asd.getTitle());
			if (Util.filled(msg)) {
			    aptm.setAppointmentStatus(msg);
			    apNotes = Util.appendLine(apNotes, "Status: ", msg);

			} else {
			    throw new Exception ("Error! No matching message for appointment status code: " + data);
			}
		    }
		    if (Util.filled(ap.getReason())) {
			aptm.setAppointmentPurpose(ap.getReason());
			apNotes = Util.appendLine(apNotes, "Purpose: ", ap.getReason());
		    }
		    if (Util.filled(ap.getProviderFirstName()) || Util.filled(ap.getProviderLastName())) {
			Appointments.Provider prov = aptm.addNewProvider();
			if (Util.filled(ap.getOhipNo())) prov.setOHIPPhysicianId(ap.getOhipNo());
			Util.writeNameSimple(prov.addNewName(), ap.getProviderFirstName(), ap.getProviderLastName());
			apNotes = Util.appendLine(apNotes, "Provider: ", ap.getProviderFirstName()+" "+ap.getProviderLastName());
		    }
		    if (Util.filled(ap.getNotes())) apNotes = Util.appendLine(apNotes, "Notes: ", ap.getNotes());
		    if (Util.filled(apNotes)) {
			aptm.setAppointmentNotes(apNotes);
		    } else {
			aptm.setAppointmentNotes("");
			err.add("Error! No Appointment Notes for Patient "+this.demographicNo+" ("+(j+1)+")");
		    }
		}
	    }
		
	    if (exReportsReceived) {
		// REPORTS RECEIVED
		ArrayList edoc_list = new EDocUtil().listDemoDocs(this.demographicNo);
		
		if (!edoc_list.isEmpty()) {
		    for (int j=0; j<edoc_list.size(); j++) {
			EDoc edoc = (EDoc)edoc_list.get(j);
			ReportsReceived rpr = patientRec.addNewReportsReceived();
			rpr.setFileExtensionAndVersion("");
			rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
			rpr.setSubClass(Util.noNull(edoc.getDescription()));
			
			File f = new File(edoc.getFilePath());
			if (!f.exists()) {
			    err.add("Error! Document \""+f.getName()+"\" does not exist!");
			} else if (f.length()>Runtime.getRuntime().freeMemory()) {
			    err.add("Error! Document \""+f.getName()+"\" too big to be exported. Not enough memory!");
			} else {
			    cdsDt.ReportContent rpc = rpr.addNewContent();
			    InputStream in = new FileInputStream(f);
			    byte[] b = new byte[(int)f.length()];

			    int offset=0, numRead=0;
			    while ((numRead=in.read(b,offset,b.length-offset)) >= 0
				   && offset < b.length) offset += numRead;

			    if (offset < b.length) throw new IOException("Could not completely read file " + f.getName());
			    in.close();
			    rpc.setMedia(b);

			    data = Util.mimeToExt(edoc.getContentType());
			    if (Util.empty(data)) data = cutExt(edoc.getFileName());
			    if (Util.empty(data)) err.add("Error! No File Extension&Version info for Document \""+edoc.getFileName()+"\"");
			    rpr.setFileExtensionAndVersion(data);

			    data = edoc.getType();
			    if (Util.filled(data)) {
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
				err.add("Error! No Class Type for Document \""+edoc.getFileName()+"\"");
			    }
			    data = edoc.getObservationDate();
			    if (UtilDateUtilities.StringToDate(data)!=null) {
				rpr.addNewEventDateTime().setFullDate(Util.calDate(data));
			    } else {
				err.add("Note: Not exporting invalid Event Date (Reports) for Patient "+this.demographicNo+" ("+(j+1)+")");
			    }
			    data = edoc.getDateTimeStamp();
			    if (UtilDateUtilities.StringToDate(data,"yyyy-MM-dd HH:mm:ss")!=null) {
				rpr.addNewReceivedDateTime().setDateTime(Util.calDate(data));
			    } else {
				err.add("Note: Not exporting invalid Received DateTime (Reports) for Patient "+this.demographicNo+" ("+(j+1)+")");
			    }
			    data = edoc.getReviewDateTime();
			    if (UtilDateUtilities.StringToDate(data,"yyyy-MM-dd HH:mm:ss")!=null) {
				rpr.addNewReviewedDateTime().setDateTime(Util.calDate(data));
			    }
			    data = edoc.getReviewerOhip();
			    if (Util.filled(data)) {
				rpr.setReviewingOHIPPhysicianId(data);
			    }
			    data = edoc.getSource();
			    Util.writeNameSimple(rpr.addNewAuthorPhysician(), data);
			}
		    }
		}
	    }
		
	    if (exAuditInformation) {
		// AUDIT INFORMATION
		if (Util.filled(this.demographicNo)) {
		    String[] rName = {"dateTime", "provider_no", "action", "content", "contentId", "ip"};
		    String[] rCont = new String[6];
		    String audReport = "";
		    String audSummary = "";
		    
		    ArrayList<Log> logList = LogAction.getLogByDemo(this.demographicNo);
		    for (Log lg : logList) {
			rCont[0] = lg.getDateTime()==null ? "" : lg.getDateTime().toString();
			rCont[1] = lg.getProviderNo();
			rCont[2] = lg.getAction();
			rCont[3] = lg.getContent();
			rCont[4] = lg.getContentId();
			rCont[5] = lg.getIp();
			
			for (int j=0; j<rName.length; j++) {
			    audReport = Util.appendLine(audReport, rName[j]+": ", rCont[j]);
			}
			audReport = Util.appendLine(audReport, "------------------------------------------------------------");
		    }
		    audSummary = audReport;
		    AuditInformation audInf = patientRec.addNewAuditInformation();
		    if (Util.empty(audSummary)) {
			err.add("Error! No Category Summary Line (Audit Information) for Patient "+this.demographicNo);
		    } else {
			audInf.setCategorySummaryLine(audSummary);
		    }
		    
		    /*****************************************
		     * write to xml (as Report Text Content) *
		     *****************************************/
		    audInf.addNewContent().setTextContent(audReport);
		    audInf.setFormat(cdsDt.AuditFormat.TEXT);
		}
	    }
		
	    if (exCareElements) {
		//CARE ELEMENTS
		List<Measurements> measList = ImportExportMeasurements.getMeasurements(this.demographicNo);
		for (Measurements meas : measList) {
		    if (meas.getType().equals("HT")) { //Height in cm
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.Height height = careElm.addNewHeight();
			height.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Height (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			height.setHeight(meas.getDataField());
			height.setHeightUnit(cdsDt.Height.HeightUnit.CM);
		    } else if (meas.getType().equals("WT") && meas.getMeasuringInstruction().equalsIgnoreCase("in kg")) { //Weight in kg
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.Weight weight = careElm.addNewWeight();
			weight.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Weight (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			weight.setWeight(meas.getDataField());
			weight.setWeightUnit(cdsDt.Weight.WeightUnit.KG);
		    } else if (meas.getType().equals("WAIS") || meas.getType().equals("WC")) { //Waist Circumference in cm
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.WaistCircumference waist = careElm.addNewWaistCircumference();
			waist.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Waist Circumference (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			waist.setWaistCircumference(meas.getDataField());
			waist.setWaistCircumferenceUnit(cdsDt.WaistCircumference.WaistCircumferenceUnit.CM);
		    } else if (meas.getType().equals("BP")) { //Blood Pressure
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.BloodPressure bloodp = careElm.addNewBloodPressure();
			bloodp.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Blood Pressure (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			String[] sdbp = meas.getDataField().split("/");
			bloodp.setSystolicBP(sdbp[0]);
			bloodp.setDiastolicBP(sdbp[1]);
			bloodp.setBPUnit(cdsDt.BloodPressure.BPUnit.MM_HG);
		    } else if (meas.getType().equals("POSK")) { //Packs of Cigarettes per day
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.SmokingPacks smokp = careElm.addNewSmokingPacks();
			smokp.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Smoking Packs (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			smokp.setPerDay(new BigDecimal(meas.getDataField()));
		    } else if (meas.getType().equals("SKST")) { //Smoking Status
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.SmokingStatus smoks = careElm.addNewSmokingStatus();
			smoks.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Smoking Status (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			smoks.setStatus(Util.yn(meas.getDataField()));
		    } else if (meas.getType().equals("SMBG")) { //Self Monitoring Blood Glucose
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.SelfMonitoringBloodGlucose bloodg = careElm.addNewSelfMonitoringBloodGlucose();
			bloodg.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Self-monitoring Blood Glucose (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			bloodg.setSelfMonitoring(Util.yn(meas.getDataField()));
		    } else if (meas.getType().equals("DMME")) { //Diabetes Education
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesEducationalSelfManagement des = careElm.addNewDiabetesEducationalSelfManagement();
			des.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Educational Self-management (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			des.setEducationalTrainingPerformed(Util.yn(meas.getDataField()));
		    } else if (meas.getType().equals("SMCD")) { //Self Management Challenges
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesSelfManagementChallenges dsc = careElm.addNewDiabetesSelfManagementChallenges();
			dsc.setCodeValue(cdsDt.DiabetesSelfManagementChallenges.CodeValue.X_44941_3);
			dsc.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Self-management Challenges (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dsc.setChallengesIdentified(cdsDt.YnIndicatorsimple.Y);
		    } else if (meas.getType().equals("MCCN")) { //Motivation Counseling Completed Nutrition
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
			dmc.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Motivational Counselling on Nutrition (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.NUTRITION);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Counselling (Nutrition) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("MCCE")) { //Motivation Counseling Completed Exercise
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
			dmc.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Motivational Counselling on Exercise (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.EXERCISE);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Counselling (Exercise) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("MCCS")) { //Motivation Counseling Completed Smoking Cessation
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
			dmc.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Motivational Counselling on Smoking Cessation (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.SMOKING_CESSATION);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Counselling (Smoking Cessation) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("MCCO")) { //Motivation Counseling Completed Other
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
			dmc.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Motivational Counselling on Other Matters (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.OTHER);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Counselling (Other) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("EYEE")) { //Dilated Eye Exam
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
			dcs.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Complication Screening on Eye Exam (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_32468_1);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Complications Screening (Retinal Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("FTE")) { //Foot Exam
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
			dcs.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Complication Screening on Foot Exam (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_11397_7);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Complications Screening (Foot Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("FTLS")) { // Foot Exam Test Loss of Sensation (Neurological Exam)
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
			dcs.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Complication Screening on Neurological Exam (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.NEUROLOGICAL_EXAM);
			if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
			    err.add("Note: Patient "+this.demographicNo+" didn't do Diabetes Complications Screening (Neurological Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
			}
		    } else if (meas.getType().equals("CGSD")) { //Collaborative Goal Setting
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.DiabetesSelfManagementCollaborative dsco = careElm.addNewDiabetesSelfManagementCollaborative();
			dsco.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Diabetes Self-management Collaborative Goal Setting (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			dsco.setCodeValue(cdsDt.DiabetesSelfManagementCollaborative.CodeValue.X_44943_9);
			dsco.setDocumentedGoals(meas.getDataField());
		    } else if (meas.getType().equals("HYPE")) { //Hypoglycemic Episodes
			CareElements careElm = patientRec.addNewCareElements();
			cdsDt.HypoglycemicEpisodes he = careElm.addNewHypoglycemicEpisodes();
			he.setDate(Util.calDate(meas.getDateObserved()));
			if (meas.getDateObserved()==null) {
			    err.add("Error! No Date for Hypoglycemic Episodes (id="+meas.getId()+") for Patient "+this.demographicNo);
			}
			he.setNumOfReportedEpisodes(new BigInteger(meas.getDataField()));
		    }
		}
	    }


		//export file to temp directory
		try{
		    File directory = new File(tmpDir);
		    if(!directory.exists()){
			throw new Exception("Temporary Export Directory does not exist!");
		    }
		    String inFile = this.demographicNo+"-"+demoName+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".xml";
		    files[i] = new File(directory,inFile);
		}catch(Exception e){
		    e.printStackTrace();
		}
		try {
			omdCdsDoc.save(files[i], options);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
		}
	    }
	}
	
	//create ReadMe.txt & ExportEvent.log
	File[] exportFiles = new File[files.length+2];	
	for (int i=0; i<files.length; i++) exportFiles[i] = files[i];
	exportFiles[exportFiles.length-2] = makeReadMe(files, err, mediaType, noOfMedia);
	exportFiles[exportFiles.length-1] = makeExportLog(files, err);
	
	//zip all export files
	String zipName = "export-"+setName.replace(" ","")+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
	if (!Util.zipFiles(exportFiles, zipName, tmpDir)) {
            System.out.println("Error! Failed to zip export files");
	}

        if (pgpReady.equals("Yes")) {
            //PGP encrypt zip file
            PGPEncrypt pgp = new PGPEncrypt();
            if (pgp.encrypt(zipName, tmpDir)) {
                Util.downloadFile(zipName+".pgp", tmpDir, response);
                Util.cleanFile(zipName+".pgp", tmpDir);
                ffwd = "success";
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("pgp_ready", "No");
            }
        } else {
            System.out.println("Warning: PGP Encryption NOT available - unencrypted file exported!");
            Util.downloadFile(zipName, tmpDir, response);
            ffwd = "success";
        }

        //Remove zip & export files from temp dir
        Util.cleanFile(zipName, tmpDir);
        Util.cleanFiles(exportFiles);
    }

    return mapping.findForward(ffwd);
}

    File makeReadMe(File[] f, Vector error, String mediaType, String noOfMedia) throws IOException {
        OscarProperties oscarp = oscar.OscarProperties.getInstance();
	File readMe = new File(f[0].getParentFile(), "ReadMe.txt");
	BufferedWriter out = new BufferedWriter(new FileWriter(readMe));
	out.write("Physician Group                    : ");
	out.write(new ClinicData().getClinicName());
	out.newLine();
	out.write("CMS Vendor, Product & Version      : ");
	String vendor = oscarp.getProperty("Vendor_Product");
	if (Util.empty(vendor)) {
	    error.add("Error! Vendor_Product not defined in oscar.properties");
	} else {
	    out.write(vendor);
	}
	out.newLine();
	out.write("Application Support Contact        : ");
	String support = oscarp.getProperty("Support_Contact");
	if (Util.empty(support)) {
	    error.add("Error! Support_Contact not defined in oscar.properties");
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
    
    String cutExt(String filename) {
	if (Util.empty(filename)) return "";
	String[] parts = filename.split(".");
	if (parts.length>1) return "."+parts[parts.length-1];
	else return "";
    }
    
    String fillUp(String filled, char c, int size) {
	if (size>=filled.length()) {
	    int fill = size-filled.length();
	    for (int i=0; i<fill; i++) filled += c;
	}
	return filled;
    }
    
    public DemographicExportAction3() {
    }
}
