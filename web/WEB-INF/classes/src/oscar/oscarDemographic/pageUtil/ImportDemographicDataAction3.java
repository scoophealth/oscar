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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of ImportDemographicDataAction
 *
 *
 * * ImportDemographicDataAction2.java
 *
 * Created on Oct 2, 2007
 */

package oscar.oscarDemographic.pageUtil;

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
import cds.OmdCdsDocument;
import cds.PastHealthDocument.PastHealth;
import cds.PatientRecordDocument.PatientRecord;
import cds.PersonalHistoryDocument.PersonalHistory;
import cds.ProblemListDocument.ProblemList;
import cds.ReportsReceivedDocument.ReportsReceived;
import cds.RiskFactorsDocument.RiskFactors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.xmlbeans.XmlException;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.appt.AppointmentDAO;
import oscar.appt.ApptStatusData;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.oscarDemographic.data.DemographicRelationship;
import oscar.oscarEncounter.data.EctProgram;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementsExt;
import oscar.oscarLab.LabRequestReportLink;
import oscar.oscarLab.ca.on.LabResultImport;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarRx.data.RxAllergyImport;
import oscar.oscarRx.data.RxPrescriptionImport;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie Cheng
 */
public class ImportDemographicDataAction3 extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
       
	String proNo = (String) request.getSession().getAttribute("user");
	String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
	File importLog = null;
	if (!Util.filled(tmpDir)) {
	    throw new Exception("Temporary Import Directory not set! Check oscar.properties.");
	} else {
	    if (tmpDir.charAt(tmpDir.length()-1)!='/') tmpDir = tmpDir + '/';
	}
        ImportDemographicDataForm frm = (ImportDemographicDataForm) form; 
        FormFile imp = frm.getImportFile();
	String ifile = tmpDir + imp.getFileName();
	
        ArrayList warnings = new ArrayList();
	try {
	    InputStream is = imp.getInputStream();
	    OutputStream os = new FileOutputStream(ifile);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len=is.read(buf)) > 0) os.write(buf,0,len);
	    is.close();
	    os.close();
	    
	    Vector logResult = new Vector();
	    if (ifile.substring(ifile.length()-3).equals("zip")) {
		ZipInputStream in = new ZipInputStream(new FileInputStream(ifile));
		boolean noXML = true;
		ZipEntry entry = in.getNextEntry();
		
		while (entry!=null) {
		    String ofile = tmpDir + entry.getName();
		    if (ofile.substring(ofile.length()-3).equals("xml")) {
			noXML = false;
			OutputStream out = new FileOutputStream(ofile);
			while ((len=in.read(buf)) > 0) out.write(buf,0,len);
			out.close();
			String[] logR = importXML(ofile, proNo, warnings, request);
			if (logR!=null) logResult.add(logR);
		    }
		    entry = in.getNextEntry();
		}
		if (noXML) {
		    Util.cleanFile(ifile);
		    throw new Exception ("Error! No XML file in zip");
		} else {
		    if (logResult.size()>0) importLog = makeImportLog(logResult, tmpDir);
		}
		in.close();
		if (!Util.cleanFile(ifile)) throw new Exception ("Error! Cannot delete import file!");

	    } else if (ifile.substring(ifile.length()-3).equals("xml")) {
		String[] logR = importXML(ifile, proNo, warnings, request);
		if (logR!=null) {
		    logResult.add(logR);
		    if (logResult.size()>0) importLog = makeImportLog(logResult, tmpDir);
		}
		
	    } else {
		Util.cleanFile(ifile);
		throw new Exception ("Error! Import file must be XML or ZIP");
	    }

	} catch (Exception e) {
	    warnings.add("Error processing file: " + imp.getFileName());
	    e.printStackTrace();
	}
        request.setAttribute("warnings",warnings);
	if (importLog!=null) request.setAttribute("importlog",importLog.getPath());
        
        System.out.println("warnings size "+warnings.size());
        for( int i = 0; i < warnings.size(); i++ ){
           String str = (String) warnings.get(i);
           System.out.println(str);
        }
        return mapping.findForward("success");
    }
    
    
    
    
    String[] importXML(String xmlFile, String proNum, ArrayList errWarnings, HttpServletRequest req) throws SQLException, Exception {
	String demoNo="", dataGood="Yes", summaryGood="Yes", otherGood="Yes", errorImport="";
	DemographicData.DemographicAddResult demoRes = null;
//	try {
	    File xmlF = new File(xmlFile);
		OmdCdsDocument.OmdCds omdCds=null;
		try {
			omdCds = OmdCdsDocument.Factory.parse(xmlF).getOmdCds();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (XmlException ex) {
			ex.printStackTrace();
		}
	    PatientRecord patientRec = omdCds.getPatientRecord();
	    
	    //DEMOGRAPHICS
	    Demographics demo = patientRec.getDemographics();
	    cdsDt.PersonNameStandard.LegalName legalName = demo.getNames().getLegalName();
	    String lastName="", firstName="";
	    if (legalName!=null) {
		if (legalName.getLastName()!=null) lastName = Util.noNull(legalName.getLastName().getPart());
		if (legalName.getFirstName()!=null) firstName = Util.noNull(legalName.getFirstName().getPart());
	    } else {
		dataGood = "No";
		errorImport = Util.appendLine(errorImport,"No Legal Name");
	    }
	    String title = demo.getNames().getNamePrefix()!=null ? demo.getNames().getNamePrefix().toString() : "";
	    String sex = demo.getGender()!=null ? demo.getGender().toString() : "";
	    if (!Util.filled(sex)) {
		dataGood = "No";
		errorImport = Util.appendLine(errorImport,"No Gender");
	    }
	    String birthDate = dateFullPartial(demo.getDateOfBirth());
	    if (!Util.filled(birthDate)) {
		birthDate = "0001-01-01";
		dataGood = "No";
		errorImport = Util.appendLine(errorImport,"No Date Of Birth");
	    }
	    String roster_status = demo.getEnrollmentStatus()!=null ? demo.getEnrollmentStatus().toString() : "";
	    if	    (roster_status.equals("1")) roster_status = "RO";
	    else if (roster_status.equals("0")) roster_status = "NR";
	    else {
		dataGood = "No";
		errorImport = Util.appendLine(errorImport,"No Enrollment Status");
	    }
	    String patient_status = demo.getPersonStatusCode()!=null ? demo.getPersonStatusCode().toString() : "";
	    if	    (patient_status.equals("A")) patient_status = "AC";
	    else if (patient_status.equals("I")) patient_status = "IN";
	    else if (patient_status.equals("D")) patient_status = "DE";
	    else if (patient_status.equals("O")) patient_status = "OTHER";
	    else {
		dataGood = "No";
		errorImport = Util.appendLine(errorImport,"No Person Status Code");
	    }
	    String date_joined = dateFullPartial(demo.getEnrollmentDate());
	    String end_date = dateFullPartial(demo.getEnrollmentTerminationDate());
	    String sin = Util.noNull(demo.getSIN());
	    String dNote = "";
	    dNote = Util.appendLine(dNote, "SIN: ", sin);
	    
	    String chart_no = Util.noNull(demo.getChartNumber());
	    String official_lang = "";
	    if (demo.getPreferredOfficialLanguage()!=null) {
		official_lang = demo.getPreferredOfficialLanguage().toString();
		official_lang = official_lang.equals("ENG") ? "English" : official_lang;
		official_lang = official_lang.equals("FRE") ? "French" : official_lang;
	    }
	    String spoken_lang = Util.noNull(demo.getPreferredSpokenLanguage());
	    String uvID = Util.noNull(demo.getUniqueVendorIdSequence());
	    if (Util.filled(uvID)) {
		if (!Util.filled(chart_no)) {
		    chart_no = uvID;
		    errorImport = Util.appendLine(errorImport,"Note: Unique Vendor Id imported as [chart_no]");
		} else {
		    dNote = Util.appendLine(dNote, "Unique Vendor ID: ", uvID);
		}
	    }
	    String versionCode="", hin="", hc_type="", eff_date="";
	    cdsDt.HealthCard healthCard = demo.getHealthCard();
	    if (healthCard!=null) {
		hin = Util.noNull(healthCard.getNumber());
		if (hin.equals("")) {
		    dataGood = "No";
		    errorImport = writeError(errorImport,errWarnings,"No health card number!");
		}
		hc_type = getProvinceCode(healthCard.getProvinceCode());
		if (hc_type.equals("")) {
		    dataGood = "No";
		    errorImport = writeError(errorImport,errWarnings,"No Province Code for health card!");
		}
		versionCode = Util.noNull(healthCard.getVersion());
		eff_date = getCalDate(healthCard.getExpirydate());
	    }
	    String address="", city="", province="", postalCode="";
	    if (demo.getAddressArray().length>0) {
		cdsDt.Address addr = demo.getAddressArray(0);	//only get 1st address, other ignored
		if (addr!=null) {
		    if (Util.filled(addr.getFormatted())) {
			address = addr.getFormatted();
		    } else {
			cdsDt.AddressStructured addrStr = addr.getStructured();
			if (addrStr!=null) {
			    address = Util.noNull(addrStr.getLine1()) + Util.noNull(addrStr.getLine2()) + Util.noNull(addrStr.getLine3());
			    city = Util.noNull(addrStr.getCity());
			    province = getCountrySubDivCode(addrStr.getCountrySubdivisionCode());
			    cdsDt.PostalZipCode postalZip = addrStr.getPostalZipCode();
			    if (postalZip!=null) postalCode = Util.noNull(postalZip.getPostalCode());
			}
		    }	    
		}
	    }
	    cdsDt.PhoneNumber[] pn = demo.getPhoneNumberArray();
	    String workPhone="", workExt="", homePhone="", homeExt="", cellPhone="", ext="", patientPhone="";
	    for (int i=0; i<pn.length; i++) {
		String phone = pn[i].getPhoneNumber();
		if (!Util.filled(phone)) {
		    if (Util.filled(pn[i].getNumber())) {
			String areaCode = Util.filled(pn[i].getAreaCode()) ? "("+pn[i].getAreaCode()+")" : "";
			phone = areaCode + pn[i].getNumber();
		    }
		}
		if (Util.filled(phone)) {
		    if (Util.filled(pn[i].getExtension())) ext = pn[i].getExtension();
		    else if (Util.filled(pn[i].getExchange())) ext = pn[i].getExchange();
		    
		    if (pn[i].getPhoneNumberType()==cdsDt.PhoneNumberType.W) {
			workPhone = phone;
			workExt   = ext;		    
		    } else if (pn[i].getPhoneNumberType()==cdsDt.PhoneNumberType.R) {
			homePhone = phone;
			homeExt   = ext;
		    } else if (pn[i].getPhoneNumberType()==cdsDt.PhoneNumberType.C) {
			cellPhone = phone;
		    }
		}
	    }
	    if (demo.getPreferredPhone()!=null) {
		if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.R) {
		    if (Util.filled(homePhone)) patientPhone = homePhone+" "+homeExt;
		}
		if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.W) {
		    if (Util.filled(workPhone)) patientPhone = workPhone+" "+workExt;
		}
		if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.C) {
		    if (Util.filled(cellPhone)) patientPhone = cellPhone;
		}
	    } else {
		if      (Util.filled(homePhone)) patientPhone = homePhone+" "+homeExt;
		else if (Util.filled(workPhone)) patientPhone = workPhone+" "+workExt;
		else if (Util.filled(cellPhone)) patientPhone = cellPhone;
	    }
	    String email = Util.noNull(demo.getEmail());
	    
	    String primaryPhysician = "";
	    if (demo.getPrimaryPhysician()!=null) {
		String[] personName = getPersonName(demo.getPrimaryPhysician().getName());
		String personOHIP = demo.getPrimaryPhysician().getOHIPPhysicianId();
		primaryPhysician = writeProviderData(personName, personOHIP);
	    } else {
		dataGood = "No";
		errorImport = writeError(errorImport,errWarnings,"No Primary Physician!");
	    }
	    
	    Date bDate = UtilDateUtilities.StringToDate(birthDate,"yyyy-MM-dd");
	    String year_of_birth = UtilDateUtilities.DateToString(bDate,"yyyy");
	    String month_of_birth = UtilDateUtilities.DateToString(bDate,"MM");
	    String date_of_birth = UtilDateUtilities.DateToString(bDate,"dd");
	    
	    DemographicData dd = new DemographicData();
	    DemographicExt dExt = new DemographicExt();
	    demoRes = dd.addDemographic(title, lastName, firstName, address, city, province, postalCode, homePhone, workPhone,
					year_of_birth, month_of_birth, date_of_birth, hin, versionCode, roster_status, patient_status,
					date_joined, chart_no, official_lang, spoken_lang, primaryPhysician, sex, end_date, eff_date,
					""/*pcn_indicator*/, hc_type, ""/*hc_renew_date*/, ""/*family_doctor*/, email, ""/*pin*/, 
					""/*alias*/, ""/*previousAddress*/, ""/*children*/, ""/*sourceOfIncome*/, ""/*citizenship*/, sin);
	    demoNo = demoRes.getId();
	    if (demoNo!=null)
	    {
		if (Util.filled(dNote)) dd.addDemographiccust(demoNo, dNote);
		
		if (!workExt.equals("")) dExt.addKey(primaryPhysician, demoNo, "wPhoneExt", workExt);
		if (!homeExt.equals("")) dExt.addKey(primaryPhysician, demoNo, "hPhoneExt", homeExt);
		if (!cellPhone.equals("")) dExt.addKey(primaryPhysician, demoNo, "demo_cell", cellPhone);

		Demographics.Contact[] contt = demo.getContactArray();
		for (int i=0; i<contt.length; i++) {
		    String[] contactName = getPersonName(contt[i].getName());
		    String cFirstName = contactName[0];
		    String cLastName  = contactName[1];
		    String cEmail = Util.noNull(contt[i].getEmailAddress());

		    pn = contt[i].getPhoneNumberArray();
		    workPhone=""; workExt=""; homePhone=""; homeExt=""; cellPhone=""; ext="";
		    for (int j=0; j<pn.length; j++) {
			String phone = pn[j].getPhoneNumber();
			if (phone==null) {
			    if (pn[j].getNumber()!=null) {
				if (pn[j].getAreaCode()!=null) phone = pn[j].getAreaCode()+"-"+pn[j].getNumber();
				else phone = pn[j].getNumber();
			    }
			}
			if (phone!=null) {
			    if (pn[j].getExtension()!=null) ext = pn[j].getExtension();
			    else if (pn[j].getExchange()!=null) ext = pn[j].getExchange();
			    
			    if (pn[j].getPhoneNumberType()==cdsDt.PhoneNumberType.W) {
				workPhone = phone;
				workExt   = ext;		    
			    } else if (pn[j].getPhoneNumberType()==cdsDt.PhoneNumberType.R) {
				homePhone = phone;
				homeExt   = ext;
			    } else if (pn[j].getPhoneNumberType()==cdsDt.PhoneNumberType.C) {
				cellPhone = phone;
			    }
			}
		    }
		    
		    String cPurpose = contt[i].getContactPurpose()!=null ? contt[i].getContactPurpose().toString() : "";
		    boolean sdm = false;
		    boolean emc = false;
		    String rel = "Other";
		    if (cPurpose.equals("EC")) emc = true;
		    else if (cPurpose.equals("NK")) sdm = true;
		    else if (cPurpose.equals("AS")) rel = "Administrative Staff";
		    else if (cPurpose.equals("CG")) rel = "Care Giver";
		    else if (cPurpose.equals("PA")) rel = "Power of Attorney";
		    else if (cPurpose.equals("IN")) rel = "Insurance";
		    else if (cPurpose.equals("GT")) rel = "Guarantor";

		    String cDemoNo = dd.getDemoNoByNamePhoneEmail(cFirstName, cLastName, homePhone, workPhone, cEmail);
		    if (cDemoNo.equals("")) {   //add new demographic
			demoRes = dd.addDemographic("", cLastName, cFirstName, "", "", "", "", homePhone, workPhone, "0001", "01", "01", "", "",
						"", "", "", "", "", "", "", "F", "", "", "", "", "", "", cEmail, "", "", "", "", "", "", "");
			cDemoNo = demoRes.getId();
			if (!workExt.equals("")) dExt.addKey("", cDemoNo, "wPhoneExt", workExt);
			if (!homeExt.equals("")) dExt.addKey("", cDemoNo, "hPhoneExt", homeExt);
			if (!cellPhone.equals("")) dExt.addKey("", cDemoNo, "demo_cell", cellPhone);
		    }
		    DemographicRelationship demoRel = new DemographicRelationship();
		    if (!cDemoNo.equals("")) {
		        demoRel.addDemographicRelationship(demoNo, cDemoNo, rel, sdm, emc, ""/*notes*/, primaryPhysician, null);
		    }
		}
		
		HttpSession se = req.getSession();
		WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
		CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
		
		//Prepare cmNote
		CaseManagementNote cmNote = new CaseManagementNote();
		CaseManagementIssue cmIssu = new CaseManagementIssue();
		cmNote.setUpdate_date(new Date());
		cmNote.setObservation_date(new Date());
		cmNote.setDemographic_no(demoNo);
		cmNote.setProviderNo(proNum);
		cmNote.setSigning_provider_no(proNum);
		cmNote.setSigned(true);
		cmNote.setHistory("");
		cmNote.setReporter_caisi_role("1");  //caisi_role for "doctor"
		cmNote.setReporter_program_team("0");
		cmNote.setProgram_no(new EctProgram(se).getProgram(proNum));
		
		cmIssu.setDemographic_no(demoNo);
		cmIssu.setType("system");
		Set<CaseManagementIssue> sCmIssu = new HashSet<CaseManagementIssue>();
		sCmIssu.add(cmIssu);
		cmNote.setIssues(sCmIssu);
		
		//PERSONAL HISTORY
		Issue isu = cmm.getIssueInfoByCode("SocHistory");
		cmIssu.setIssue(isu);
		PersonalHistory[] pHist = patientRec.getPersonalHistoryArray();
		for (int i=0; i<pHist.length; i++) {
		    String socialHist = "";
		    if (Util.filled(pHist[i].getCategorySummaryLine())) {
			socialHist = pHist[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Personal History ("+(i+1)+")");
		    }		    
		    socialHist = Util.appendLine(socialHist, getResidual(pHist[i].getResidualInfo()));
		    if (Util.filled(socialHist)) {
			cmNote.setNote(socialHist);
			cmm.saveCaseIssue(cmIssu);
			cmm.saveNoteSimple(cmNote);
		    }
		}
		//FAMILY HISTORY
		isu = cmm.getIssueInfoByCode("FamHistory");
		cmIssu.setIssue(isu);
		FamilyHistory[] fHist = patientRec.getFamilyHistoryArray();
		for (int i=0; i<fHist.length; i++) {
		    String familyHist = "";
		    familyHist = Util.appendLine(familyHist, fHist[i].getDiagnosisProblemDescription());
		    familyHist = Util.appendLine(familyHist, getCode(fHist[i].getDiagnosisCode(),"Diagnosis"));
		    if (Util.filled(fHist[i].getCategorySummaryLine())) {
			familyHist = "Summary: " + fHist[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Family History ("+(i+1)+")");
		    }
		    familyHist = Util.appendLine(familyHist, getResidual(fHist[i].getResidualInfo()));
		    if (Util.filled(familyHist)) {
			cmNote.setNote(familyHist);
			cmm.saveCaseIssue(cmIssu);
			cmm.saveNoteSimple(cmNote);
		    }
		    cmNote.setNote(fHist[i].getNotes());
		    saveLinkNote(cmNote, cmm);
		    
		    CaseManagementNoteExt cme = new CaseManagementNoteExt();
		    cme.setNoteId(cmNote.getId());
		    if (fHist[i].getStartDate()!=null) {
			cme.setKeyVal(cme.STARTDATE);
			cme.setDateValue(dDateFullPartial(fHist[i].getStartDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    }
		    if (fHist[i].getAgeAtOnset()!=null) {
			cme.setKeyVal(cme.AGEATONSET);
			cme.setValue(fHist[i].getAgeAtOnset().toString());
			cmm.saveNoteExt(cme);
		    }
		    if (Util.filled(fHist[i].getRelationship())) {
			cme.setKeyVal(cme.RELATIONSHIP);
			cme.setValue(fHist[i].getRelationship());
			cmm.saveNoteExt(cme);
		    }
		    if (Util.filled(fHist[i].getTreatment())) {
			cme.setKeyVal(cme.TREATMENT);
			cme.setValue(fHist[i].getTreatment());
			cmm.saveNoteExt(cme);
		    }
		}
		//PAST HEALTH
		isu = cmm.getIssueInfoByCode("MedHistory");
		cmIssu.setIssue(isu);
		PastHealth[] pHealth = patientRec.getPastHealthArray();
		for (int i=0; i< pHealth.length; i++) {
		    String medicalHist = "";
		    medicalHist = Util.appendLine(medicalHist, pHealth[i].getPastHealthProblemDescriptionOrProcedures());
		    medicalHist = Util.appendLine(medicalHist, getCode(pHealth[i].getDiagnosisOrProcedureCode(),"Diagnosis/Procedure"));
		    if (pHealth[i].getMedicalSurgicalFlag()!=null) medicalHist = Util.appendLine(medicalHist,"Medical Surgical Flag: ",pHealth[i].getMedicalSurgicalFlag().toString());
		    medicalHist = Util.appendLine(medicalHist,"Resolved ? ",pHealth[i].getResolvedIndicator());
		    if (Util.filled(pHealth[i].getCategorySummaryLine())) {
			medicalHist = pHealth[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Past Health ("+(i+1)+")");
		    }
		    medicalHist = Util.appendLine(medicalHist, getResidual(pHealth[i].getResidualInfo()));
		    if (Util.filled(medicalHist)) {
			cmNote.setNote(medicalHist);
			cmm.saveCaseIssue(cmIssu);
			cmm.saveNoteSimple(cmNote);
		    }
		    cmNote.setNote(pHealth[i].getNotes());
		    saveLinkNote(cmNote, cmm);
		    
		    if (pHealth[i].getResolvedDate()!=null) {
			CaseManagementNoteExt cme = new CaseManagementNoteExt();
			cme.setNoteId(cmNote.getId());
			cme.setKeyVal(cme.RESOLUTIONDATE);
			cme.setDateValue(dDateFullPartial(pHealth[i].getResolvedDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    }
		}
		//PROBLEM LIST
		isu = cmm.getIssueInfoByCode("Concerns");
		cmIssu.setIssue(isu);
		ProblemList[] probList = patientRec.getProblemListArray();
		for (int i=0; i<probList.length; i++) {
		    String ongConcerns = "";
		    ongConcerns = Util.appendLine(ongConcerns, probList[i].getProblemDescription());
		    cdsDt.Code diagCode = probList[i].getDiagnosisCode();
		    CaseManagementIssue concernIssu = null;
		    if (diagCode!=null) {
			boolean code_unknown = false;
			if (diagCode.getCodingSystem().equalsIgnoreCase("icd9")) {
			    Issue disu = cmm.getIssueInfoByCode(diagCode.getValue());
			    if (disu!=null) {
				concernIssu = new CaseManagementIssue();
				concernIssu.setDemographic_no(demoNo);
				concernIssu.setIssue(disu);
				cmm.saveCaseIssue(concernIssu);
				sCmIssu.add(concernIssu);
			    } else code_unknown = true;
			} else code_unknown = true;
			if (code_unknown) ongConcerns = Util.appendLine(ongConcerns, getCode(probList[i].getDiagnosisCode(),"Diagnosis"));
		    }
		    if (Util.filled(probList[i].getCategorySummaryLine())) {
			ongConcerns = probList[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Problem List ("+(i+1)+")");
		    }
		    ongConcerns = Util.appendLine(ongConcerns, getResidual(probList[i].getResidualInfo()));
		    if (Util.filled(ongConcerns)) {
			cmNote.setNote(ongConcerns);
			cmm.saveCaseIssue(cmIssu);
			cmm.saveNoteSimple(cmNote);
			if (concernIssu!=null) sCmIssu.remove(concernIssu);
		    }
		    cmNote.setNote(probList[i].getNotes());
		    saveLinkNote(cmNote, cmm);
		    
		    CaseManagementNoteExt cme = new CaseManagementNoteExt();
		    cme.setNoteId(cmNote.getId());
		    if (probList[i].getOnsetDate()!=null) {
			cme.setKeyVal(cme.STARTDATE);
			cme.setDateValue(dDateFullPartial(probList[i].getOnsetDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Onset Date for Problem List ("+(i+1)+")");
		    }
		    if (probList[i].getResolutionDate()!=null) {
			cme.setKeyVal(cme.RESOLUTIONDATE);
			cme.setDateValue(dDateFullPartial(probList[i].getResolutionDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    }
		    if (Util.filled(probList[i].getProblemStatus())) {
			cme.setKeyVal(cme.PROBLEMSTATUS);
			cme.setValue(probList[i].getProblemStatus());
			cmm.saveNoteExt(cme);
		    }
		}
		//RISK FACTORS
		isu = cmm.getIssueInfoByCode("RiskFactors");
		cmIssu.setIssue(isu);
		RiskFactors[] rFactors = patientRec.getRiskFactorsArray();
		for (int i=0; i<rFactors.length; i++) {
		    String riskFactors = "";
		    riskFactors = Util.appendLine(riskFactors,"Risk Factor: ",rFactors[i].getRiskFactor());
		    if (Util.filled(rFactors[i].getCategorySummaryLine())) {
			riskFactors = rFactors[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Risk Factors ("+(i+1)+")");
		    }
		    riskFactors = Util.appendLine(riskFactors, getResidual(rFactors[i].getResidualInfo()));
		    if (Util.filled(riskFactors)) {
			cmNote.setNote(riskFactors);
			cmm.saveCaseIssue(cmIssu);
			cmm.saveNoteSimple(cmNote);
		    }
		    cmNote.setNote(rFactors[i].getNotes());
		    saveLinkNote(cmNote, cmm);
		    
		    CaseManagementNoteExt cme = new CaseManagementNoteExt();
		    cme.setNoteId(cmNote.getId());
		    if (rFactors[i].getStartDate()!=null) {
			cme.setKeyVal(cme.STARTDATE);
			cme.setDateValue(dDateFullPartial(rFactors[i].getStartDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    }
		    if (rFactors[i].getEndDate()!=null) {
			cme.setKeyVal(cme.RESOLUTIONDATE);
			cme.setDateValue(dDateFullPartial(rFactors[i].getEndDate(),"yyyy-MM-dd"));
			cmm.saveNoteExt(cme);
		    }
		    if (rFactors[i].getAgeOfOnset()!=null) {
			cme.setKeyVal(cme.AGEATONSET);
			cme.setValue(rFactors[i].getAgeOfOnset().toString());
			cmm.saveNoteExt(cme);
		    }
		    if (Util.filled(rFactors[i].getExposureDetails())) {
			cme.setKeyVal(cme.EXPOSUREDETAIL);
			cme.setValue(rFactors[i].getExposureDetails());
			cmm.saveNoteExt(cme);
		    }
		}
		//REMINDERS
		if (demo.getPatientWarningFlags()!=null && demo.getPatientWarningFlags().equals("1")) {
		    isu = cmm.getIssueInfoByCode("Reminders");
		    cmIssu.setIssue(isu);
		    cmNote.setNote(demo.getNoteAboutPatient());
		    cmm.saveCaseIssue(cmIssu);
		    cmm.saveNoteSimple(cmNote);
		}
		//CLINICAL NOTES
		cmNote.setIssues(null);
		ClinicalNotes[] cNotes = patientRec.getClinicalNotesArray();
		for (int i=0; i<cNotes.length; i++) {
		    if (cNotes[i].getEventDateTime()==null) cmNote.setObservation_date(new Date());
		    else cmNote.setObservation_date(dDateFullPartial(cNotes[i].getEventDateTime(),null));
		    if (cNotes[i].getEnteredDateTime()==null) cmNote.setUpdate_date(new Date());
		    else cmNote.setUpdate_date(dDateFullPartial(cNotes[i].getEnteredDateTime(),null));
		    
		    String encounter = cNotes[i].getMyClinicalNotesContent();
		    encounter = Util.appendLine(encounter,"Note Type: ",cNotes[i].getNoteType());
		    if (cNotes[i].getPrincipalAuthor()!=null) {
			ClinicalNotes.PrincipalAuthor cpAuthor = cNotes[i].getPrincipalAuthor();
			String[] personName = getPersonName(cpAuthor.getName());
			cmNote.setProviderNo(writeProviderData(personName, cpAuthor.getOHIPPhysicianId()));
			encounter = Util.appendLine(encounter, "Principal Author Function: ", cNotes[i].getPrincipalAuthorFunction());
		    }
		    if (cNotes[i].getSigningOHIPPhysicianId()!=null) {
			String[] signPhysicianName = {"",""};
			String signPhysicianOHIP = cNotes[i].getSigningOHIPPhysicianId();
			cmNote.setSigning_provider_no(writeProviderData(signPhysicianName, signPhysicianOHIP));
		    }
		    encounter = Util.appendLine(encounter,"Signed DateTime: ",dateFullPartial(cNotes[i].getSignedDateTime()));
		    encounter = Util.appendLine(encounter,"Signing Physician OHIP Id: ",cNotes[i].getSigningOHIPPhysicianId());
		    if (Util.filled(encounter)) {
			cmNote.setNote(encounter);
			cmm.saveNoteSimple(cmNote);
		    }
		}
		
		//ALLERGIES & ADVERSE REACTIONS
		AllergiesAndAdverseReactions[] aaReactArray = patientRec.getAllergiesAndAdverseReactionsArray();
		for (int i=0; i<aaReactArray.length; i++) {
		    String description="", regionalId="", reaction="", severity="", entryDate="", startDate="", typeCode="";
		    String aSummary = "";
		    if (Util.filled(aaReactArray[i].getCategorySummaryLine())) {
			aSummary = aaReactArray[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Allergies & Adverse Reactions ("+(i+1)+")");
		    }
		    
		    reaction = Util.noNull(aaReactArray[i].getReaction());
		    if (Util.filled(aSummary)) {
			reaction = Util.appendLine(reaction, "Summary: ", aSummary);
			errorImport = Util.appendLine(errorImport,"Note: Allergies Summary imported in [reaction] ("+(i+1)+")");
		    }
		    description = Util.noNull(aaReactArray[i].getOffendingAgentDescription());
		    entryDate = dateFullPartial(aaReactArray[i].getRecordedDate());
		    startDate = dateFullPartial(aaReactArray[i].getStartDate());
		    if (aaReactArray[i].getCode()!=null) regionalId = Util.noNull(aaReactArray[i].getCode().getValue());
		    reaction = Util.appendLine(reaction,"Offending Agent Description: ",aaReactArray[i].getOffendingAgentDescription());
		    if (aaReactArray[i].getReactionType()!=null) reaction = Util.appendLine(reaction,"Reaction Type: ",aaReactArray[i].getReactionType().toString());
		    if (!getYN(aaReactArray[i].getKnownAllergies()).equals("")) reaction = Util.appendLine(reaction,"Known Allergies: ",getYN(aaReactArray[i].getKnownAllergies()));
		    if (aaReactArray[i].getHealthcarePractitionerType()!=null) reaction = Util.appendLine(reaction,"Healthcare Practitioner Type: ",aaReactArray[i].getHealthcarePractitionerType().toString());
		    reaction = Util.appendLine(reaction, getResidual(aaReactArray[i].getResidualInfo()));
		    
		    if (aaReactArray[i].getReactionType()!=null) {
			if (aaReactArray[i].getReactionType()==cdsDt.AdverseReactionType.AL) typeCode="0"; //allergies
		    }
		    if (typeCode.equals("") && aaReactArray[i].getPropertyOfOffendingAgent()!=null) {
			if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.DR) typeCode="13"; //drug
			else if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.ND) typeCode="1"; //non-drug
			else if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.UK) typeCode="2"; //unknown
		    }
		    if (aaReactArray[i].getSeverity()!=null) {
			if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.MI) severity="1"; //mild
			else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.MO) severity="2"; //moderate
			else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.LT) severity="3"; //severe
			else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.NO) {
			    severity="1";
			    errorImport = Util.appendLine(errorImport,"Note: Allergies Severity [No Reaction] imported as [Mild] ("+(i+1)+")");
			}
		    }
		    Long allergyId = RxAllergyImport.save(demoNo, entryDate, description, typeCode, reaction, startDate, severity, regionalId);
		    
		    cmNote.setNote(aaReactArray[i].getNotes());
		    saveLinkNote(cmNote, CaseManagementNoteLink.ALLERGIES, allergyId, cmm);

		}
		
		
		//MEDICATIONS & TREATMENTS
		MedicationsAndTreatments[] medArray = patientRec.getMedicationsAndTreatmentsArray();
		for (int i=0; i<medArray.length; i++) {
		    String rxDate="", endDate="", writtenDate="", BN="", regionalId="", frequencyCode="", duration="1";
		    String quantity="", special="", route="", drugForm="", dosage="", unit="", lastRefillDate="";
		    String createDate = UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		    boolean longTerm=false, pastMed=false;
		    int takemin=0, takemax=0, repeat=0, patientCompliance=0;

		    String mSummary = "";
		    if (Util.filled(medArray[i].getCategorySummaryLine())) {
			mSummary = medArray[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Medications & Treatments ("+(i+1)+")");
		    }
		    writtenDate	   = dateFullPartial(medArray[i].getPrescriptionWrittenDate());
		    rxDate	   = dateFullPartial(medArray[i].getStartDate());
		    endDate	   = dateFullPartial(medArray[i].getEndDate());
		    lastRefillDate = dateFullPartial(medArray[i].getLastRefillDate());
		    regionalId	   = Util.noNull(medArray[i].getDrugIdentificationNumber());
		    quantity	   = numOnly(medArray[i].getQuantity());
		    duration	   = numOnly(medArray[i].getDuration());
		    frequencyCode  = procFreq(frequencyCode);
		    route	   = Util.noNull(medArray[i].getRoute());
		    drugForm	   = Util.noNull(medArray[i].getForm());
		    longTerm	   = getYN(medArray[i].getLongTermMedication())=="Yes";
		    pastMed	   = getYN(medArray[i].getPastMedications())=="Yes";
		    
		    rxDate = Util.filled(rxDate) ? rxDate : "0001-01-01";
		    endDate = Util.filled(endDate) ? endDate : "0001-01-01";
		    
		    String pc = getYN(medArray[i].getPatientCompliance());
		    if (pc=="Yes") patientCompliance = 1;
		    else if (pc=="No") patientCompliance = -1;
		    else patientCompliance = 0;
		    
		    if (duration.trim().equals("1 year")) duration = "365"; //coping with scenario in CMS 2.0
		    
		    if (Util.filled(medArray[i].getNumberOfRefills())) {
			repeat = Integer.parseInt(medArray[i].getNumberOfRefills());
		    }
		    
		    if (Util.filled(medArray[i].getDrugName())) {
			BN = medArray[i].getDrugName();
			special = medArray[i].getDrugName();
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Drug Name in Medications & Treatments ("+(i+1)+")");
		    }
		    
		    if (Util.filled(mSummary)) {
			special = Util.appendLine(special, "Summary: ", mSummary);
			errorImport = Util.appendLine(errorImport,"Note: Medications Summary imported in [special] ("+(i+1)+")");
		    }
		    if (medArray[i].getStrength()!=null) {
			dosage = Util.noNull(medArray[i].getStrength().getAmount())+" "+Util.noNull(medArray[i].getStrength().getUnitOfMeasure());
			special = Util.appendLine(special, "Strength: ", dosage);
		    }
		    //special = Util.appendLine(special, "DIN: ", regionalId);
		    //special = Util.appendLine(special, "Quantity: ", medArray[i].getQuantity());
		    special = Util.appendLine(special, "Take ", medArray[i].getDosage());
		    special += Util.filled(route) ? " "+route : "";
		    special += Util.filled(frequencyCode) ? " "+frequencyCode : "";
		    special += Util.filled(duration) ? " for "+duration : "";
		    
		    special = Util.appendLine(special, "Prescription Instructions: ", medArray[i].getPrescriptionInstructions());
		    special = Util.appendLine(special, getResidual(medArray[i].getResidualInfo()));
		    special += Util.filled(special) ? "\n" : "";
		    
		    String dose = Util.noNull(medArray[i].getDosage());
		    int sep1 = dose.indexOf("-");
		    int sep2 = dose.indexOf(" ");
		    int sep3 = dose.indexOf(" ", sep2+1);
		    if (sep2>sep1 && sep3>sep2) {
			if (sep1>0) {
			    takemin = Integer.parseInt(dose.substring(0,sep1));
			    takemax = Integer.parseInt(dose.substring(sep1+1,sep2));
			} else {
			    takemin = Integer.parseInt(dose.substring(0,sep2));
			    takemax = takemin;
			}
			unit = dose.substring(sep2+1,sep3);
		    } else {
			takemin = Integer.parseInt(numOnly(dose));
			takemax = takemin;
			String[] dose_split = dose.split(" ");
			if (dose_split.length>1) {
			    unit = dose_split[1];
			}
		    }
		    String prescribedBy="", outsiderName="", outsiderOhip="";
		    if (medArray[i].getPrescribedBy()!=null) {
			String[] personName = getPersonName(medArray[i].getPrescribedBy().getName());
			String personOHIP = medArray[i].getPrescribedBy().getOHIPPhysicianId();
			String providerNo = getProviderNoByOhip(personOHIP);
			if (Util.filled(providerNo) && Integer.valueOf(providerNo)>0) {
			    prescribedBy = providerNo;
			} else { //outsider provider
			    outsiderName = personName[1] + ", " + personName[0];
			    outsiderOhip = personOHIP;
			}
		    }
		    
		    Long drugId = RxPrescriptionImport.save(prescribedBy,demoNo,rxDate,endDate,writtenDate,createDate,
			    BN,regionalId,frequencyCode,duration,quantity,repeat,lastRefillDate,special,route,drugForm,
			    dosage,takemin,takemax,unit,longTerm,pastMed,patientCompliance,outsiderName,outsiderOhip,(i+1));
		    
		    cmNote.setNote(medArray[i].getNotes());
		    saveLinkNote(cmNote, CaseManagementNoteLink.DRUGS, drugId, cmm);
		}


		//IMMUNIZATIONS
		Immunizations[] immuArray = patientRec.getImmunizationsArray();
		for (int i=0; i<immuArray.length; i++) {
		    String preventionDate="", preventionType="", refused="0";
		    ArrayList preventionExt = new ArrayList();
		   
		    if (Util.filled(immuArray[i].getImmunizationName())) {
			preventionType = immuArray[i].getImmunizationName();
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Immunization Name ("+(i+1)+")");
		    }
		    preventionDate = dateFullPartial(immuArray[i].getDate());
		    refused = getYN(immuArray[i].getRefusedFlag()).equals("Yes") ? "1" : "0";
		    
		    String comments="", iSummary="";
		    if (immuArray[i].getCategorySummaryLine()!=null) {
			iSummary = immuArray[i].getCategorySummaryLine().trim();
		    } else {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Immunizations ("+(i+1)+")");
		    }
		    comments = Util.appendLine(comments, immuArray[i].getNotes());
		    if (Util.filled(iSummary)) {
			comments = Util.appendLine(comments, "Summary", iSummary);
			errorImport = Util.appendLine(errorImport,"Note: Immunization Summary imported in [comments] ("+(i+1)+")");
		    }
		    comments = Util.appendLine(comments, getCode(immuArray[i].getImmunizationCode(),"Immunization Code"));
		    comments = Util.appendLine(comments, "Instructions: ", immuArray[i].getInstructions());
		    comments = Util.appendLine(comments, getResidual(immuArray[i].getResidualInfo()));
		    if (Util.filled(comments)) {
			Hashtable ht = new Hashtable();
			ht.put("comments", comments);
			preventionExt.add(ht);
		    }
		    if (Util.filled(immuArray[i].getManufacturer())) {
			Hashtable ht = new Hashtable();
			ht.put("manufacture", immuArray[i].getManufacturer());
			preventionExt.add(ht);
		    }
		    if (Util.filled(immuArray[i].getLotNumber())) {
			Hashtable ht = new Hashtable();
			ht.put("lot", immuArray[i].getLotNumber());
			preventionExt.add(ht);
		    }
		    if (Util.filled(immuArray[i].getRoute())) {
			Hashtable ht = new Hashtable();
			ht.put("route", immuArray[i].getRoute());
			preventionExt.add(ht);
		    }
		    if (Util.filled(immuArray[i].getSite())) {
			Hashtable ht = new Hashtable();
			ht.put("location", immuArray[i].getSite());
			preventionExt.add(ht);
		    }
		    if (Util.filled(immuArray[i].getDose())) {
			Hashtable ht = new Hashtable();
			ht.put("dose", immuArray[i].getDose());
			preventionExt.add(ht);
		    }
		    PreventionData prevD = new PreventionData();
		    prevD.insertPreventionData(primaryPhysician, demoNo, preventionDate, proNum, "", preventionType, refused, "", "", preventionExt);
		}

		//LABORATORY RESULTS
		LaboratoryResults[] labResultArr = patientRec.getLaboratoryResultsArray();
		String[] _accession   = new String[labResultArr.length];
		String[] _coll_date   = new String[labResultArr.length];
		String[] _title	      = new String[labResultArr.length];
		String[] _testName    = new String[labResultArr.length];
		String[] _abn	      = new String[labResultArr.length];
		String[] _minimum     = new String[labResultArr.length];
		String[] _maximum     = new String[labResultArr.length];
		String[] _result      = new String[labResultArr.length];
		String[] _unit	      = new String[labResultArr.length];
		String[] _description = new String[labResultArr.length];
		String[] _location    = new String[labResultArr.length];
		String[] _reviewer    = new String[labResultArr.length];
		String[] _comments    = new String[labResultArr.length];
		Date[] _rev_date = new Date[labResultArr.length];
		Date[] _req_date = new Date[labResultArr.length];
		
		// Save to labPatientPhysicianInfo, labTestResults, patientLabRouting
                for (int i=0; i<labResultArr.length; i++) {
		    _testName[i] = Util.noNull(labResultArr[i].getLabTestCode());
		    _location[i] = Util.noNull(labResultArr[i].getLaboratoryName());
		    _accession[i] = Util.noNull(labResultArr[i].getAccessionNumber());
		    _comments[i] = Util.noNull(labResultArr[i].getPhysiciansNotes());
		    _coll_date[i] = dateFullPartial(labResultArr[i].getCollectionDateTime());
		    _req_date[i] = dDateFullPartial(labResultArr[i].getLabRequisitionDateTime(),null);
                    _rev_date[i] = dDateFullPartial(labResultArr[i].getDateTimeResultReviewed(),null);
		    
		    _title[i] = Util.noNull(labResultArr[i].getTestName());
		    if (Util.filled(labResultArr[i].getTestNameReportedByLab())) {
			_title[i] += Util.filled(_title[i]) ? "/" : "";
			_title[i] += labResultArr[i].getTestNameReportedByLab();
		    }
		    _description[i] = Util.appendLine(_description[i], "Test Results Info: ", labResultArr[i].getTestResultsInformationReportedByTheLab());
		    _description[i] = Util.appendLine(_description[i], "Notes from Lab: ", labResultArr[i].getNotesFromLab());
		    _description[i] = Util.appendLine(_description[i], "Received Datetime: ", dateFullPartial(labResultArr[i].getDateTimeResultReceivedByCMS()));
		    
		    if (labResultArr[i].getResultNormalAbnormalFlag()!=null) {
			cdsDt.ResultNormalAbnormalFlag.Enum flag = labResultArr[i].getResultNormalAbnormalFlag();
			if (flag.equals(cdsDt.ResultNormalAbnormalFlag.Y)) _abn[i] = "A";
			if (flag.equals(cdsDt.ResultNormalAbnormalFlag.N)) _abn[i] = "N";
		    }
		    
		    if (labResultArr[i].getResult()!=null) {
			_result[i] = Util.noNull(labResultArr[i].getResult().getValue());
			_unit[i] = Util.noNull(labResultArr[i].getResult().getUnitOfMeasure());
		    }
		    
		    if (labResultArr[i].getReferenceRange()!=null) {
			LaboratoryResults.ReferenceRange ref = labResultArr[i].getReferenceRange();
			if (Util.filled(ref.getReferenceRangeText())) {
			    _minimum[i] = ref.getReferenceRangeText();
			} else {
			    _maximum[i] = Util.noNull(ref.getHighLimit());
			    _minimum[i] = Util.noNull(ref.getLowLimit());
			}
		    }
		    
		    LaboratoryResults.ResultReviewer resultRev = labResultArr[i].getResultReviewer();
		    if (resultRev!=null) {
			String[] revName = getPersonName(resultRev.getName());
			String revOhip = Util.noNull(resultRev.getOHIPPhysicianId());
			_reviewer[i] = writeProviderData(revName, revOhip);
		    }
		}
		
		Vector<String> uniq_labs = getUniques(_location);
		Vector<String> uniq_accs = getUniques(_accession);
		for (String lab : uniq_labs) {
		    boolean labNew = true;
		    String rptInfoId="";
		    for (String acc : uniq_accs) {
			boolean accNew = true;
			String paPhysId="";
			for (int i=0; i<_location.length; i++) {
			    if (!(_location[i].equals(lab) && _accession[i].equals(acc))) continue;
			    
			    if (labNew) {
				rptInfoId = LabResultImport.saveLabReportInfo(_location[i]);
				labNew = false;
			    }
			    if (accNew) {
				paPhysId = LabResultImport.saveLabPatientPhysicianInfo(rptInfoId, _accession[i], _coll_date[i], firstName, lastName, sex, hin, birthDate, patientPhone);
				
				Long plrId = LabResultImport.savePatientLabRouting(demoNo, paPhysId);
				LabRequestReportLink.save(null,null,_req_date[i],"patientLabRouting",plrId);
				
				String status = Util.filled(_reviewer[i]) ? "A" : "N";
				_reviewer[i] = status.equals("A") ? _reviewer[i] : "0";
				LabResultImport.saveProviderLabRouting(_reviewer[i], paPhysId, status, _comments[i], _rev_date[i]);
				
				accNew = false;
			    }
			    String last = Util.filled(_description[i]) ? "N" : "Y";
			    LabResultImport.saveLabTestResults(_title[i], _testName[i], _abn[i], _minimum[i], _maximum[i], _result[i], _unit[i], "", _location[i], paPhysId, "C", last);
			    if (last.equals("N")) {
				LabResultImport.saveLabTestResults(_title[i], _testName[i], "", "", "", "", "", _description[i], _location[i], paPhysId, "D", "Y");
			    }
			}
		    }
		}
		/*
		String labEverything = getLabDline(labResultArr[i]);
		if (Util.filled(labEverything)){
		   LabResultImport.SaveLabDesc(labEverything,patiPhysId);
		}
		*/
		
		// Save to measurements, measurementsExt
                for (LaboratoryResults labResults : labResultArr) {
                    Measurements meas = new Measurements(Long.valueOf(demoNo), proNum);
                    LaboratoryResults.Result result = labResults.getResult();
                    String unit = null;
                    if (result!=null) {
                        if (Util.filled(result.getValue())) meas.setDataField(result.getValue());
                        if (Util.filled(result.getUnitOfMeasure())) unit = result.getUnitOfMeasure();
                    }
                    if (labResults.getDateTimeResultReceivedByCMS()!=null) {
                        meas.setDateEntered(dDateFullPartial(labResults.getDateTimeResultReceivedByCMS(),null));
                    } else {
                        meas.setDateEntered(new Date());
                    }
                    ImportExportMeasurements.saveMeasurements(meas);
                    Long measId = meas.getId();
                    saveMeasurementsExt(measId, "unit", unit);
                    String testCode = Util.filled(labResults.getLabTestCode()) ? labResults.getLabTestCode() : "";
                    String testName = Util.noNull(labResults.getTestName());
                    if (Util.filled(labResults.getTestNameReportedByLab())) {
                        testName += Util.filled(testName) ? "/" : "";
                        testName += labResults.getTestNameReportedByLab();
                    }
                    saveMeasurementsExt(measId, "identifier", testCode);
                    saveMeasurementsExt(measId, "name", testName);
                    
                    cdsDt.DateFullOrPartial collDate = labResults.getCollectionDateTime();
                    if (collDate!=null) {
                        saveMeasurementsExt(measId, "datetime", dateFullPartial(collDate));
                    } else {
			dataGood = "No";
                        errorImport = writeError(errorImport,errWarnings,"No Collection DateTime for Lab Test "+testCode+" for Patient "+demoNo);
                    }
                    if (Util.filled(labResults.getLaboratoryName())) {
                        saveMeasurementsExt(measId, "labname", labResults.getLaboratoryName());
                    } else {
			dataGood = "No";
                        errorImport = writeError(errorImport,errWarnings,"No Laboratory Name for Lab Test "+testCode+" for Patient "+demoNo);
                    }
                    cdsDt.ResultNormalAbnormalFlag.Enum abnFlag = labResults.getResultNormalAbnormalFlag();
                    if (abnFlag!=null) {
                        String abn = abnFlag.toString();
                        if (!abn.equals("U")) {
                            saveMeasurementsExt(measId, "abnormal", (abn.equals("Y")?"A":abn));
                        }
                    } else {
			dataGood = "No";
                        errorImport = writeError(errorImport,errWarnings,"No Normal/Abnormal Flag for Lab Test "+testCode+" for Patient "+demoNo);
                    }
                    
		    String labNotes = Util.appendLine("", "Test Results Info: ", labResults.getTestResultsInformationReportedByTheLab());
                    labNotes = Util.appendLine(labNotes, "Notes from Lab: ", labResults.getNotesFromLab());
                    saveMeasurementsExt(measId, "comments", labNotes);
                    saveMeasurementsExt(measId, "accession", labResults.getAccessionNumber());
                    
                    LaboratoryResults.ReferenceRange refRange = labResults.getReferenceRange();
                    if (refRange!=null) {
                        saveMeasurementsExt(measId, "maximum", refRange.getHighLimit());
                        saveMeasurementsExt(measId, "minimum", refRange.getLowLimit());
                        saveMeasurementsExt(measId, "range", refRange.getReferenceRangeText());
                    }
                }
		
		//APPOINTMENTS
		Appointments[] appArray = patientRec.getAppointmentsArray();
		Date appointmentDate = null;
		String name="", notes="", reason="", status="", startTime="", endTime="", providerNo="";

		Properties p = (Properties) se.getAttribute("oscarVariables");
		AppointmentDAO apD = new AppointmentDAO(p);

		for (int i=0; i<appArray.length; i++) {
		    name = lastName + "," + firstName;
		    
		    String apptDateStr = dateFullPartial(appArray[i].getAppointmentDate());
		    if (Util.filled(apptDateStr)) {
			appointmentDate = UtilDateUtilities.StringToDate(apptDateStr);
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Appointment Date ("+(i+1)+")");
		    }
		    if (appArray[i].getAppointmentTime()!=null) {
			startTime = getCalTime(appArray[i].getAppointmentTime());
			if (appArray[i].getDuration()!=null) {
			    Date d_startTime = appArray[i].getAppointmentTime().getTime();
			    Date d_endTime = new Date();
			    d_endTime.setTime(d_startTime.getTime() + appArray[i].getDuration().longValue()*60000);
			    endTime = UtilDateUtilities.DateToString(d_endTime,"HH:mm:ss");
			} else {
			    Date d_startTime = appArray[i].getAppointmentTime().getTime();
			    Date d_endTime = new Date();
			    d_endTime.setTime(d_startTime.getTime() + 15*60000);
			    endTime = UtilDateUtilities.DateToString(d_endTime,"HH:mm:ss");
			}
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Appointment Time ("+(i+1)+")");
		    }
		    if (Util.filled(appArray[i].getAppointmentNotes())) {
			notes = appArray[i].getAppointmentNotes();
		    } else {
			dataGood = "No";
			errorImport = Util.appendLine(errorImport,"No Appointment Notes ("+(i+1)+")");
		    }
		    if (appArray[i].getAppointmentStatus()!=null) {
			ApptStatusData asd = new ApptStatusData();
			String[] allStatus = asd.getAllStatus();
			String[] allTitle = asd.getAllTitle();
			status = allStatus[0];
			for (int j=1; j<allStatus.length; j++) {
			    String msg = getResources(req).getMessage(allTitle[j]);
			    if (appArray[i].getAppointmentStatus().trim().equalsIgnoreCase(msg)) {
				status = allStatus[j];
				break;
			    }
			}
		    }
		    reason = Util.noNull(appArray[i].getAppointmentPurpose());
		    if (appArray[i].getProvider()!=null) {
			String[] personName = getPersonName(appArray[i].getProvider().getName());
			String personOHIP = appArray[i].getProvider().getOHIPPhysicianId();
			providerNo = writeProviderData(personName, personOHIP);
		    }
		    apD.addAppointment(providerNo, appointmentDate, startTime, endTime, name, demoNo, notes, reason, status);
		}
		
		//REPORTS RECEIVED
		ReportsReceived[] repR = patientRec.getReportsReceivedArray();
		for (int i=0; i<repR.length; i++) {
		    cdsDt.ReportContent repCt = repR[i].getContent();
		    if (repCt!=null) {
			byte[] b = null;
			if (repCt.getMedia()!=null) b = repCt.getMedia();
			else if (repCt.getTextContent()!=null) b = repCt.getTextContent().getBytes();
			if (b==null) {
			    otherGood = "No";
			    errorImport = Util.appendLine(errorImport,"No report file in xml ("+(i+1)+")");
			} else {
			    String docFileName = "ImportReport-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+"-"+i;
			    String docType=null, contentType=null, observationDate=null, updateDateTime=null, docCreator=null;
			    String reviewer=null, reviewDateTime=null;
			    
			    if (Util.filled(repR[i].getFileExtensionAndVersion())) {
				contentType = repR[i].getFileExtensionAndVersion();
				docFileName += getFileExt(contentType);
			    } else {
				dataGood = "No";
				errorImport = Util.appendLine(errorImport,"No File Extension & Version for Reports ("+(i+1)+")");
			    }
			    String docDesc = docFileName;
			    String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
			    if (!Util.filled(docDir)) {
				throw new Exception("Document Directory not set! Check oscar.properties.");
			    } else {
				if (docDir.charAt(docDir.length()-1)!='/') docDir = docDir + '/';
			    }
			    FileOutputStream f = new FileOutputStream(docDir+docFileName);
			    f.write(b);
			    f.close();
			    
			    observationDate = dateFullPartial(repR[i].getEventDateTime());
			    updateDateTime = dateFullPartial(repR[i].getReceivedDateTime());
			    reviewDateTime = dateFullPartial(repR[i].getReviewedDateTime());
			    if (repR[i].getClass1()!=null) {
				if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT)) docType = "radiology";
				else if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT)) docType = "pathology";
				else if (repR[i].getClass1().equals(cdsDt.ReportClass.CONSULTANT_REPORT)) docType = "consult";
				else docType = "others";
			    } else {
				dataGood = "No";
				errorImport = Util.appendLine(errorImport,"No Class for Reports ("+(i+1)+")");
			    }
			    if (repR[i].getAuthorPhysician()!=null) {
				String[] personName = getPersonName(repR[i].getAuthorPhysician());
				
				ProviderData provData = new ProviderData();
				ArrayList pList = provData.getProviderList();
				for (int j=0; j<pList.size(); j++) {
				    Hashtable pHash = (Hashtable) pList.get(j);
				    if (personName[0].equalsIgnoreCase((String)pHash.get("firstName")) &&
					personName[1].equalsIgnoreCase((String)pHash.get("lastName"))) {
					docCreator = (String)pHash.get("providerNo");
				    }
				    if (repR[i].getReviewingOHIPPhysicianId().equals((String)pHash.get("ohipNo"))) {
					reviewer = (String)pHash.get("providerNo");
				    }
				}
				if (docCreator==null) {
				    docCreator = provData.getNewExtProviderNo();
				    provData.addProvider(docCreator, personName[0], personName[1], "");
				}
			    }
			    EDocUtil.addDocument(demoNo,docFileName,docDesc,docType,contentType,observationDate,updateDateTime,docCreator,proNum,reviewer,reviewDateTime);
			}
		    }
		}
		
		//AUDIT INFORMATION
		String fileTime = UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss");
		String auditInfoFile = "importAuditInfo-"+fileTime;
		String auditInfoSummary = "";
		String contentType = "text/html";
		String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		if (!Util.filled(docDir)) {
		    throw new Exception("Document Directory not set! Check oscar.properties.");
		} else {
		    if (docDir.charAt(docDir.length()-1)!='/') docDir = docDir + '/';
		}
		
		/* Read data from xml */
		AuditInformation[] audInf = patientRec.getAuditInformationArray();
		for (int i=0; i<audInf.length; i++) {
		    if (audInf.length>1) auditInfoFile += "("+i+")";
		    String sAudInfo = audInf[i].getCategorySummaryLine();
		    if (!Util.filled(sAudInfo)) {
			summaryGood = "No";
			errorImport = Util.appendLine(errorImport,"No Summary for Audit Information ("+(i+1)+")");
		    }
		    cdsDt.ResidualInformation audRes = audInf[i].getResidualInfo();
		    if (audRes!=null) {
			cdsDt.ResidualInformation.DataElement[] audEArr = audRes.getDataElementArray();
			for (cdsDt.ResidualInformation.DataElement audE : audEArr) {
			    sAudInfo = Util.appendLine(sAudInfo, "Name: ", audE.getName());
			    sAudInfo = Util.appendLine(sAudInfo, "Data Type: ", audE.getDataType());
			    if (audE.getDescription()!=null) sAudInfo = Util.appendLine(sAudInfo, "Description: ", audE.getDescription());
			    sAudInfo = Util.appendLine(sAudInfo, "Content: ", audE.getContent());
			    sAudInfo = Util.appendLine(sAudInfo, "----------------------------------------");
			}
		    }
		    if (audInf[i].getFormat().equals(cdsDt.AuditFormat.TEXT)) {
			if (audInf[i].getContent()!=null) {
			    sAudInfo = Util.appendLine(sAudInfo, audInf[i].getContent().getTextContent());
			}
			if (Util.filled(sAudInfo)) {
			    FileWriter f = new FileWriter(docDir+auditInfoFile);
			    f.write(sAudInfo);
			    f.close();
			}
		    } else if (audInf[i].getFormat().equals(cdsDt.AuditFormat.FILE)) {
			if (Util.filled(sAudInfo)) {
			    auditInfoSummary = "importAuditSummary-"+fileTime;
			    FileWriter f = new FileWriter(docDir+auditInfoSummary);
			    f.write(sAudInfo);
			    f.close();
			}
			contentType = audInf[i].getFileExtensionAndVersion();
			if (audInf[i].getContent()!=null) {
			    auditInfoFile += getFileExt(contentType);
			    FileOutputStream f = new FileOutputStream(docDir+auditInfoFile);
			    f.write(audInf[i].getContent().getMedia());
			    f.close();
			}
		    }
		    /***** Write to document table *****/
		    EDocUtil.addDocument(demoNo,auditInfoFile,"Imported Audit Information","others",contentType,fileTime,fileTime,proNum,proNum,""/*reviewer*/,""/*reviewDateTime*/);
		    if (Util.filled(auditInfoSummary)) {
			EDocUtil.addDocument(demoNo,auditInfoSummary,"Imported Audit Summary","others",contentType,fileTime,fileTime,proNum,proNum,""/*reviewer*/,""/*reviewDateTime*/);
		    }
		}
		
		//CARE ELEMENTS
		CareElements[] careElems = patientRec.getCareElementsArray();
		for (CareElements ce : careElems) {
		    cdsDt.Height[] heights = ce.getHeightArray();
		    for (cdsDt.Height ht : heights) {
			Date dateObserved = ht.getDate().getTime();
			String dataField = Util.noNull(ht.getHeight());
			ImportExportMeasurements.saveMeasurements("HT", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.Weight[] weights = ce.getWeightArray();
		    for (cdsDt.Weight wt : weights) {
			Date dateObserved = wt.getDate().getTime();
			String dataField = Util.noNull(wt.getWeight());
			ImportExportMeasurements.saveMeasurements("WT", demoNo, proNum, dataField, "in kg", dateObserved);
		    }
		    cdsDt.WaistCircumference[] waists = ce.getWaistCircumferenceArray();
		    for (cdsDt.WaistCircumference wc : waists) {
			Date dateObserved = wc.getDate().getTime();
			String dataField = Util.noNull(wc.getWaistCircumference());
			ImportExportMeasurements.saveMeasurements("WAIS", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.BloodPressure[] bloodp = ce.getBloodPressureArray();
		    for (cdsDt.BloodPressure bp : bloodp) {
			Date dateObserved = bp.getDate().getTime();
			String dataField = Util.noNull(bp.getSystolicBP())+"/"+Util.noNull(bp.getDiastolicBP());
			ImportExportMeasurements.saveMeasurements("BP", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.SmokingPacks[] smokp = ce.getSmokingPacksArray();
		    for (cdsDt.SmokingPacks sp : smokp) {
			Date dateObserved = sp.getDate().getTime();
			String dataField = sp.getPerDay().toString();
			ImportExportMeasurements.saveMeasurements("POSK", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.SmokingStatus[] smoks = ce.getSmokingStatusArray();
		    for (cdsDt.SmokingStatus ss : smoks) {
			Date dateObserved = ss.getDate().getTime();
			String dataField = ss.getStatus().toString();
			ImportExportMeasurements.saveMeasurements("SMK", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.SelfMonitoringBloodGlucose[] smbg = ce.getSelfMonitoringBloodGlucoseArray();
		    for (cdsDt.SelfMonitoringBloodGlucose sg : smbg) {
			Date dateObserved = sg.getDate().getTime();
			String dataField = sg.getSelfMonitoring().toString();
			ImportExportMeasurements.saveMeasurements("SMBG", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.DiabetesEducationalSelfManagement[] desm = ce.getDiabetesEducationalSelfManagementArray();
		    for (cdsDt.DiabetesEducationalSelfManagement dm : desm) {
			Date dateObserved = dm.getDate().getTime();
			String dataField = dm.getEducationalTrainingPerformed().toString();
			ImportExportMeasurements.saveMeasurements("DMME", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.DiabetesSelfManagementChallenges[] dsmc = ce.getDiabetesSelfManagementChallengesArray();
		    for (cdsDt.DiabetesSelfManagementChallenges dc : dsmc) {
			Date dateObserved = dc.getDate().getTime();
			String dataField = dc.getChallengesIdentified().toString();
			ImportExportMeasurements.saveMeasurements("SMCD", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.DiabetesMotivationalCounselling[] dmc = ce.getDiabetesMotivationalCounsellingArray();
		    for (cdsDt.DiabetesMotivationalCounselling dc : dmc) {
			Date dateObserved = dc.getDate().getTime();
			String dataField = "Yes";
			cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed cp = null;
			if (dc.getCounsellingPerformed().equals(cp.NUTRITION)) {
			    ImportExportMeasurements.saveMeasurements("MCCN", demoNo, proNum, dataField, dateObserved);
			}
			else if (dc.getCounsellingPerformed().equals(cp.EXERCISE)) {
			    ImportExportMeasurements.saveMeasurements("MCCE", demoNo, proNum, dataField, dateObserved);
			}
			else if (dc.getCounsellingPerformed().equals(cp.SMOKING_CESSATION)) {
			    ImportExportMeasurements.saveMeasurements("MCCS", demoNo, proNum, dataField, dateObserved);
			}
			else if (dc.getCounsellingPerformed().equals(cp.OTHER)) {
			    ImportExportMeasurements.saveMeasurements("MCCO", demoNo, proNum, dataField, dateObserved);
			}
		    }
		    cdsDt.DiabetesComplicationScreening[] dcs = ce.getDiabetesComplicationsScreeningArray();
		    for (cdsDt.DiabetesComplicationScreening ds : dcs) {
			Date dateObserved = ds.getDate().getTime();
			String dataField = "Yes";
			cdsDt.DiabetesComplicationScreening.ExamCode ec = null;
			if (ds.getExamCode().equals(ec.X_32468_1)) {
			    ImportExportMeasurements.saveMeasurements("EYEE", demoNo, proNum, dataField, dateObserved);
			} else if (ds.getExamCode().equals(ec.X_11397_7)) {
			    ImportExportMeasurements.saveMeasurements("FTE", demoNo, proNum, dataField, dateObserved);
			} else if (ds.getExamCode().equals(ec.NEUROLOGICAL_EXAM)) {
			    ImportExportMeasurements.saveMeasurements("FTLS", demoNo, proNum, dataField, dateObserved);
			}
		    }
		    cdsDt.DiabetesSelfManagementCollaborative[] dsco = ce.getDiabetesSelfManagementCollaborativeArray();
		    for (cdsDt.DiabetesSelfManagementCollaborative dc : dsco) {
			Date dateObserved = dc.getDate().getTime();
			String dataField = dc.getDocumentedGoals();
			ImportExportMeasurements.saveMeasurements("CGSD", demoNo, proNum, dataField, dateObserved);
		    }
		    cdsDt.HypoglycemicEpisodes[] hype = ce.getHypoglycemicEpisodesArray();
		    for (cdsDt.HypoglycemicEpisodes he : hype) {
			Date dateObserved = he.getDate().getTime();
			String dataField = he.getNumOfReportedEpisodes().toString();
			ImportExportMeasurements.saveMeasurements("HYPE", demoNo, proNum, dataField, dateObserved);
		    }
		}
	    }
	    errWarnings.addAll(demoRes.getWarningsCollection());
	    if (!Util.cleanFile(xmlFile)) throw new Exception ("Error! Cannot delete XML file!");
/*	    
	} catch (Exception e) {
	    errWarnings.addAll(demoRes.getWarningsCollection());
	    e.printStackTrace();
	}
 */
	if (demoNo.equals("")) {
	    return null;
	} else {
	    String[] d = {demoNo, dataGood, summaryGood, otherGood, errorImport};
	    return d;
	}
    }
    
    File makeImportLog(Vector demo, String tDir) throws IOException {
	String[][] keyword = new String[3][5];
	keyword[0][0] = "PatientID";
	keyword[0][1] = "Discrete Data";
	keyword[0][2] = "Summary LIne";
	keyword[0][3] = "Other Import";
	keyword[0][4] = "Errors";
	keyword[1][0] = "";
	keyword[1][1] = "Elements Import";
	keyword[1][2] = "Import";
	keyword[1][3] = "Categories";
	keyword[1][4] = "";
	keyword[2][0] = "";
	keyword[2][1] = "Successful";
	keyword[2][2] = "Successful";
	keyword[2][3] = "";
	keyword[2][4] = "";
	File importLog = new File(tDir, "ImportEvent-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".log");
	BufferedWriter out = new BufferedWriter(new FileWriter(importLog));
	int[] colWidth = new int[5];
	colWidth[0] = keyword[0][0].length()+2;
	colWidth[1] = keyword[1][1].length()+2;
	colWidth[2] = keyword[0][2].length()+2;
	colWidth[3] = keyword[0][3].length()+2;
	colWidth[4] = 66;
	int tableWidth = colWidth[0]+colWidth[1]+colWidth[2]+colWidth[3]+colWidth[4]+1;
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	for (int i=0; i<keyword.length; i++) {
	    for (int j=0; j<keyword[i].length; j++) {
		out.write(fillUp("|" + keyword[i][j],' ',colWidth[j]));
	    }
	    out.write("|");
	    out.newLine();
	}
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	
	for (int i=0; i<demo.size(); i++) {
	    String[] info = (String[]) demo.get(i);
	    for (int j=0; j<info.length; j++) {
		if (j==info.length-1) {
		    String[] text = info[j].split("\n");
		    for (int k=0; k<text.length; k++) {
			text[k] = fillUp("|"+text[k],' ',colWidth[j]);
		    }
		    out.write(text[0] + "|");
		    for (int k=1; k<text.length; k++) {
			out.newLine();
			out.write(fillUp("|",' ',colWidth[0]));
			out.write(fillUp("|",' ',colWidth[1]));
			out.write(fillUp("|",' ',colWidth[2]));
			out.write(fillUp("|",' ',colWidth[3]));
			out.write(text[k] + "|");
		    }
		} else {
		    out.write(fillUp("|" + info[j],' ',colWidth[j]));
		}
	    }
	    out.newLine();
	    out.write(fillUp("",'-',tableWidth));
	    out.newLine();
	}
	out.close();
	return importLog;
    }
    
    String fillUp(String filled, char c, int size) {
	if (size>=filled.length()) {
	    int fill = size-filled.length();
	    for (int i=0; i<fill; i++) filled += c;
	}
	return filled;
    }
    
    String getCalDate(Calendar c) {
	if (c==null) return "";
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	return f.format(c.getTime());
    }
    String getCalDateTime(Calendar c) {
	if (c==null) return "";
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return f.format(c.getTime());
    }
    String getCalTime(Calendar c) {
	if (c==null) return "";
	SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
	return f.format(c.getTime());
    }
    
    String getCountrySubDivCode(String countrySubDivCode) {
	if (!Util.filled(countrySubDivCode)) return "";
	String[] csdc = countrySubDivCode.split("-");
	if (csdc.length==2) {
	    if (csdc[0].trim().equals("CA")) return csdc[1].trim(); //return w/o "CA-"
	    if (csdc[1].trim().equals("US")) return countrySubDivCode.trim(); //return w/ "US-"
	}
	return "OT"; //Other
    }
    
    String dateFullPartial(cdsDt.DateFullOrPartial dfp) {
	if (dfp==null) return "";
	
	if (dfp.getDateTime()!=null) return getCalDateTime(dfp.getDateTime());
	else if (dfp.getFullDate()!=null) return getCalDate(dfp.getFullDate());
	else if (dfp.getYearMonth()!=null) return getCalDate(dfp.getYearMonth());
	else if (dfp.getYearOnly()!=null) return getCalDate(dfp.getYearOnly());
	else return "";
    }
    
    Date dDateFullPartial(cdsDt.DateFullOrPartial dfp, String format) {
	if (!Util.filled(format)) format = "yyyy-MM-dd HH:mm:ss";
	String sdate = dateFullPartial(dfp);
	return UtilDateUtilities.StringToDate(sdate, format);
    }
    
    String getFileExt(String mimeType) {
	String ret = "";
	if (!Util.filled(mimeType)) return ret;
	
	String type_ext = "application/envoy=evy|application/fractals=fif|application/futuresplash=spl|application/hta=hta|application/internet-property-stream=acx|application/mac-binhex40=hqx|application/msword=doc|application/msword=dot|application/octet-stream=bin|application/octet-stream=class|application/octet-stream=dms|application/octet-stream=exe|application/octet-stream=lha|application/octet-stream=lzh|application/oda=oda|application/olescript=axs|application/pdf=pdf|application/pics-rules=prf|application/pkcs10=p10|application/pkix-crl=crl|application/postscript=ai|application/postscript=eps|application/postscript=ps|application/rtf=rtf|application/set-payment-initiation=setpay|application/set-registration-initiation=setreg|application/vnd.ms-excel=xla|application/vnd.ms-excel=xlc|application/vnd.ms-excel=xlm|application/vnd.ms-excel=xls|application/vnd.ms-excel=xlt|application/vnd.ms-excel=xlw|application/vnd.ms-outlook=msg|application/vnd.ms-pkicertstore=sst|application/vnd.ms-pkiseccat=cat|application/vnd.ms-pkistl=stl|application/vnd.ms-powerpoint=pot|application/vnd.ms-powerpoint=pps|application/vnd.ms-powerpoint=ppt|application/vnd.ms-project=mpp|application/vnd.ms-works=wcm|application/vnd.ms-works=wdb|application/vnd.ms-works=wks|application/vnd.ms-works=wps|application/winhlp=hlp|application/x-bcpio=bcpio|application/x-cdf=cdf|application/x-compress=z|application/x-compressed=tgz|application/x-cpio=cpio|application/x-csh=csh|application/x-director=dcr|application/x-director=dir|application/x-director=dxr|application/x-dvi=dvi|application/x-gtar=gtar|application/x-gzip=gz|application/x-hdf=hdf|application/x-internet-signup=ins|application/x-internet-signup=isp|application/x-iphone=iii|application/x-javascript=js|application/x-latex=latex|application/x-msaccess=mdb|application/x-mscardfile=crd|application/x-msclip=clp|application/x-msdownload=dll|application/x-msmediaview=m13|application/x-msmediaview=m14|application/x-msmediaview=mvb|application/x-msmetafile=wmf|application/x-msmoney=mny|application/x-mspublisher=pub|application/x-msschedule=scd|application/x-msterminal=trm|application/x-mswrite=wri|application/x-netcdf=cdf|application/x-netcdf=nc|application/x-perfmon=pma|application/x-perfmon=pmc|application/x-perfmon=pml|application/x-perfmon=pmr|application/x-perfmon=pmw|application/x-pkcs12=p12|application/x-pkcs12=pfx|application/x-pkcs7-certificates=p7b|application/x-pkcs7-certificates=spc|application/x-pkcs7-certreqresp=p7r|application/x-pkcs7-mime=p7c|application/x-pkcs7-mime=p7m|application/x-pkcs7-signature=p7s|application/x-sh=sh|application/x-shar=shar|application/x-shockwave-flash=swf|application/x-stuffit=sit|application/x-sv4cpio=sv4cpio|application/x-sv4crc=sv4crc|application/x-tar=tar|application/x-tcl=tcl|application/x-tex=tex|application/x-texinfo=texi|application/x-texinfo=texinfo|application/x-troff=roff|application/x-troff=t|application/x-troff=tr|application/x-troff-man=man|application/x-troff-me=me|application/x-troff-ms=ms|application/x-ustar=ustar|application/x-wais-source=src|application/x-x509-ca-cert=cer|application/x-x509-ca-cert=crt|application/x-x509-ca-cert=der|application/ynd.ms-pkipko=pko|application/zip=zip|audio/basic=au|audio/basic=snd|audio/mid=mid|audio/mid=rmi|audio/mpeg=mp3|audio/x-aiff=aif|audio/x-aiff=aifc|audio/x-aiff=aiff|audio/x-mpegurl=m3u|audio/x-pn-realaudio=ra|audio/x-pn-realaudio=ram|audio/x-wav=wav|image/bmp=bmp|image/cis-cod=cod|image/gif=gif|image/ief=ief|image/jpeg=jpe|image/jpeg=jpeg|image/jpeg=jpg|image/pipeg=jfif|image/svg+xml=svg|image/tiff=tif|image/tiff=tiff|image/x-cmu-raster=ras|image/x-cmx=cmx|image/x-icon=ico|image/x-portable-anymap=pnm|image/x-portable-bitmap=pbm|image/x-portable-graymap=pgm|image/x-portable-pixmap=ppm|image/x-rgb=rgb|image/x-xbitmap=xbm|image/x-xpixmap=xpm|image/x-xwindowdump=xwd|message/rfc822=mht|message/rfc822=mhtml|message/rfc822=nws|text/css=css|text/h323=323|text/html=htm|text/html=html|text/html=stm|text/iuls=uls|text/plain=bas|text/plain=c|text/plain=h|text/plain=txt|text/richtext=rtx|text/scriptlet=sct|text/tab-separated-values=tsv|text/webviewhtml=htt|text/x-component=htc|text/x-setext=etx|text/x-vcard=vcf|video/mpeg=mp2|video/mpeg=mpa|video/mpeg=mpe|video/mpeg=mpeg|video/mpeg=mpg|video/mpeg=mpv2|video/quicktime=mov|video/quicktime=qt|video/x-la-asf=lsf|video/x-la-asf=lsx|video/x-ms-asf=asf|video/x-ms-asf=asr|video/x-ms-asf=asx|video/x-msvideo=avi|video/x-sgi-movie=movie|x-world/x-vrml=flr|x-world/x-vrml=vrml|x-world/x-vrml=wrl|x-world/x-vrml=wrz|x-world/x-vrml=xaf|x-world/x-vrml=xof|";
	mimeType = mimeType.toLowerCase();
	type_ext = type_ext.toLowerCase();
	int pos = type_ext.indexOf(mimeType);
	if (pos>-1) {
	    pos = pos + mimeType.length() + 1;
	    int end_pos = type_ext.indexOf('|', pos);
	    ret = "." + type_ext.substring(pos, end_pos);
	}
	return ret;
    }
    
    String[] getPersonName(cdsDt.PersonNameSimple person) {
	String[] name = new String[2];
	if (person!=null) {
	    name[0] = Util.noNull(person.getFirstName());
	    name[1] = Util.noNull(person.getLastName());
	}
	return name;
    }
    
    String[] getPersonName(cdsDt.PersonNameSimpleWithMiddleName person) {
	String[] name = new String[2];
	if (person!=null) {
	    name[0] = Util.noNull(person.getFirstName())+" "+Util.noNull(person.getMiddleName());
	    name[1] = Util.noNull(person.getLastName());
	}
	return name;
    }
    
    String getProviderNoByOhip(String ohip) throws SQLException {
	String providerNo = "";
	if (Util.filled(ohip)) providerNo = new ProviderData().getProviderNoByOhip(ohip);
	return providerNo;
    }
    
    String getCode(cdsDt.Code dCode, String dTitle) {
	if (dCode==null) return "";
	
	String ret = Util.filled(dTitle) ? dTitle+" -" : "";
	ret = Util.appendLine(ret, "Coding System: ", dCode.getCodingSystem());
	ret = Util.appendLine(ret, "Value: ",         dCode.getValue());
	ret = Util.appendLine(ret, "Description: ",   dCode.getDescription());
	
	return ret;
    }
    
    String getProvinceCode(cdsDt.HealthCardProvinceCode.Enum provinceCode) {
	String pcStr = provinceCode.toString();
	return getCountrySubDivCode(pcStr);
    }
    
    String getResidual(cdsDt.ResidualInformation resInfo) {
	String ret = "";
	if (resInfo==null) return ret;
	
	cdsDt.ResidualInformation.DataElement[] resData = resInfo.getDataElementArray();
	for (int i=0; i<resData.length; i++) {
	    if (Util.filled(resData[i].getName())) {
		ret = Util.appendLine(ret, "Data Name: ",   resData[i].getName());
		ret = Util.appendLine(ret, "Description: ", resData[i].getDescription());
		ret = Util.appendLine(ret, "Data Type: ",   resData[i].getDataType());
		ret = Util.appendLine(ret, "Content: ",     resData[i].getContent());
	    }
	}
	return ret;
    }
    
    Vector<String> getUniques(String[] arr) {
	Vector<String> uniques = new Vector<String>();
	for (int i=0; i<arr.length; i++) {
	    boolean match = false;
	    for (String x : uniques) {
		if (arr[i].equals(x)) {
		    match = true;
		    break;
		}
	    }
	    if (!match) uniques.add(arr[i]);
	}
	return uniques;
    }
    
    String getYN(cdsDt.YnIndicator yn) {
	if (yn==null) return "";
	String ret = "No";
	if (yn.getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y || yn.getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2) {
	    ret = "Yes";
	} else if (yn.getBoolean()) {
	    ret = "Yes";
	}
	return ret;
    }
    
    String getYN(cdsDt.YnIndicatorAndBlank yn) {
	if (yn==null) return "";
	String ret = "No";
	if (yn.getBlank()==cdsDt.Blank.X) {
	    ret = "";
	} else if (yn.getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y || yn.getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2) {
	    ret = "Yes";
	} else if (yn.getBoolean()) {
	    ret = "Yes";
	}
	return ret;
    }
    
    String numOnly(String s) {
	if (!Util.filled(s)) return "0";
	String numbers = "1234567890";
	Integer cut = -1;
	for (int i=0; i<s.length(); i++) {
	    if (!numbers.contains(s.substring(i,i+1))) {
		cut = i;
		break;
	    }
	}
	if (cut>0) s = s.substring(0,cut);
	return s;
    }
    
    String procFreq(String freqCode) {
	if (!Util.filled(freqCode)) return "";
	freqCode = freqCode.toUpperCase();
	freqCode = freqCode.replace(".","");
	freqCode = freqCode.replace(" ","");
	return freqCode;
    }
    
    void saveLinkNote(CaseManagementNote cmn, CaseManagementManager cmm) {
	saveLinkNote(cmn, CaseManagementNoteLink.CASEMGMTNOTE, cmn.getId(), cmm);
    }
    
    void saveLinkNote(CaseManagementNote cmn, Integer tableName, Long tableId, CaseManagementManager cmm) {
	if (Util.filled(cmn.getNote())) {
	    cmm.saveNoteSimple(cmn);    //new note id created
	    
	    CaseManagementNoteLink cml = new CaseManagementNoteLink();
	    cml.setTableName(tableName);
	    cml.setTableId(tableId);
	    cml.setNoteId(cmn.getId()); //new note id
	    cmm.saveNoteLink(cml);
	}
    }
    
    void saveMeasurementsExt(Long measurementId, String key, String val) throws SQLException {
        if (measurementId!=null && Util.filled(key) && Util.filled(val)) {
            MeasurementsExt mx = new MeasurementsExt(measurementId);
            mx.setKeyVal(key);
            mx.setVal(val);
            ImportExportMeasurements.saveMeasurementsExt(mx);
        }
    }
    
    String writeError(String log, ArrayList warnings, String msg) {
	msg = "Error! "+msg;
	warnings.add(msg);
	log = Util.appendLine(log, msg);
	return log;
    }
    
    String writeProviderData(String[] name, String ohip) throws SQLException {
	String providerNo = getProviderNoByOhip(ohip);
	if (!Util.filled(providerNo)) {   //this is a new provider
	    ProviderData pd = new ProviderData();
	    providerNo = pd.getNewExtProviderNo();
	    pd.addProvider(providerNo, name[0], name[1], Util.noNull(ohip));
	}
	return providerNo;
    }
    
    String getLabDline(LaboratoryResults labRes){
        StringBuffer s = new StringBuffer();
        appendIfNotNull(s,"LaboratoryName",labRes.getLaboratoryName());
        appendIfNotNull(s,"TestNameReportedByLab", labRes.getTestNameReportedByLab());
        appendIfNotNull(s,"LabTestCode",labRes.getLabTestCode()); 
	appendIfNotNull(s,"TestName", labRes.getTestName());
	appendIfNotNull(s,"AccessionNumber",labRes.getAccessionNumber());
        
        if (labRes.getResult ()!=null) {
            appendIfNotNull(s,"Value",labRes.getResult().getValue());
            appendIfNotNull(s,"UnitOfMeasure",labRes.getResult().getUnitOfMeasure());
	}   
        if (labRes.getReferenceRange()!=null) {
            LaboratoryResults.ReferenceRange ref = labRes.getReferenceRange();
            appendIfNotNull(s,"LowLimit",ref.getLowLimit());
            appendIfNotNull(s,"HighLimit",ref.getHighLimit());
            appendIfNotNull(s,"ReferenceRangeText", ref.getReferenceRangeText());                                             
	}
        
//<xs:element name="LabRequisitionDateTime" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
        appendIfNotNull(s,"LabRequisitionDateTime",dateFullPartial(labRes.getLabRequisitionDateTime ()));     
//<xs:element name="CollectionDateTime" type="cdsd:dateTimeYYYYMMDDHHMM"/>
        appendIfNotNull(s,"CollectionDateTime",dateFullPartial( labRes.getCollectionDateTime()));     
//<xs:element name="DateTimeResultReceivedByCMS" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
        appendIfNotNull(s,"DateTimeResultReceivedByCMS",dateFullPartial(labRes.getDateTimeResultReceivedByCMS()));     
//<xs:element name="DateTimeResultReviewed" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
        appendIfNotNull(s,"DateTimeResultReviewed",dateFullPartial(labRes.getDateTimeResultReviewed()));     
        
        if (labRes.getResultReviewer() != null){ //ResultReviewer){
//<xs:element name="ResultReviewer" type="cdsd:ohipBillingNumber" minOccurs="0"/>
	if (labRes.getResultReviewer().getName()!=null) {
        appendIfNotNull(s,"Reviewer First Name:", labRes.getResultReviewer().getName().getFirstName());
        appendIfNotNull(s,"Reviewer Last Name:",labRes.getResultReviewer().getName().getLastName());
	}
        appendIfNotNull(s,"OHIP ID :", labRes.getResultReviewer().getOHIPPhysicianId());
        }
//<xs:element name="ResultNormalAbnormalFlag">xs:restriction base="xs:token"> xs:enumeration value="Y"/><xs:enumeration value="N"/<xs:enumeration value="U"/>
        appendIfNotNull(s,"ResultNormalAbnormalFlag",""+labRes.getResultNormalAbnormalFlag());     
//<xs:element name="TestResultsInformationreportedbytheLaboratory" type="cdsd:text32K"/>
        appendIfNotNull(s,"TestResultsInformationreportedbytheLaboratory",labRes.getTestResultsInformationReportedByTheLab());     
//<xs:element name="NotesFromLab" type="cdsd:text32K" minOccurs="0"/>
        appendIfNotNull(s,"NotesFromLab",labRes.getNotesFromLab());     
//<xs:element name="PhysiciansNotes" type="cdsd:text32K" minOccurs="0"/>
        appendIfNotNull(s,"PhysiciansNotes",labRes.getPhysiciansNotes());     
        
        /*
<xs:element name="LaboratoryName" <xs:restriction base="cdsd:text"xs:maxLength value="120"/>
<xs:element name="TestNameReportedByLab" minOccurs="0">xs:restriction base="xs:token"xs:maxLength value="120"/>
<xs:element name="LabTestCode" minOccurs="0"xs:restriction base="xs:token"xs:maxLength value="50"/>
<xs:element name="TestName" minOccurs="0"xs:restriction base="cdsd:text"xs:maxLength value="120"/>
<xs:element name="AccessionNumber" minOccurs="0"xs:restriction base="cdsd:text"xs:maxLength value="120"/>
<xs:element name="Result" minOccurs="0"
<xs:all>
<xs:element name="Value" type="cdsd:labResultValue"/>
<xs:element name="UnitOfMeasure" type="xs:token"/>
<xs:element name="ReferenceRange" minOccurs="0"
<xs:element name="LowLimit" type="cdsd:labResultValue"/>
<xs:element name="HighLimit" type="cdsd:labResultValue"/>
<xs:element name="ReferenceRangeText">xs:restriction base="cdsd:text"> xs:maxLength value="1024"/>
<xs:element name="LabRequisitionDateTime" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
<xs:element name="CollectionDateTime" type="cdsd:dateTimeYYYYMMDDHHMM"/>
<xs:element name="DateTimeResultReceivedByCMS" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
<xs:element name="DateTimeResultReviewed" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
<xs:element name="ResultReviewer" type="cdsd:ohipBillingNumber" minOccurs="0"/>
<xs:element name="ResultNormalAbnormalFlag">xs:restriction base="xs:token"> xs:enumeration value="Y"/><xs:enumeration value="N"/<xs:enumeration value="U"/>
<xs:element name="TestResultsInformationreportedbytheLaboratory" type="cdsd:text32K"/>
<xs:element name="NotesFromLab" type="cdsd:text32K" minOccurs="0"/>
<xs:element name="PhysiciansNotes" type="cdsd:text32K" minOccurs="0"/>
         */
	return s.toString();
    }
    
    void appendIfNotNull(StringBuffer s, String name, String object){
        if (object != null){
            s.append(name+": "+object+"\n");
        }
    }
    
    public ImportDemographicDataAction3() {
   }
   
}
