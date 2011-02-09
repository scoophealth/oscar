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
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.xmlbeans.XmlException;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.appt.ApptStatusData;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicExt;
import oscar.oscarDemographic.data.DemographicRelationship;
import oscar.oscarDemographic.data.DemographicData.DemographicAddResult;
import oscar.oscarEncounter.data.EctProgram;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;
import oscar.oscarLab.LabRequestReportLink;
import oscar.oscarLab.ca.on.LabResultImport;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarRx.data.RxAllergyImport;
import oscar.oscarRx.data.RxPrescriptionImport;
import oscar.service.OscarSuperManager;
import oscar.util.StringUtils;
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
import cds.PersonalHistoryDocument.PersonalHistory;
import cds.ProblemListDocument.ProblemList;
import cds.ReportsReceivedDocument.ReportsReceived;
import cds.RiskFactorsDocument.RiskFactors;

/**
 *
 * @author Ronnie Cheng
 */
	public class ImportDemographicDataAction3 extends Action {
	
	private static final Logger logger = MiscUtils.getLogger();
		
	boolean matchProviderNames = true;
	String admProviderNo = null;
	String demographicNo = null;
	String patientName = null;
	String programId = null;
    
    ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
    AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");
	AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
	OscarSuperManager oscarSuperManager = (OscarSuperManager) SpringUtils.getBean("oscarSuperManager");

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
		admProviderNo = (String) request.getSession().getAttribute("user");
		programId = new EctProgram(request.getSession()).getProgram(admProviderNo);
		String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
		tmpDir = Util.fixDirName(tmpDir);
		if (!Util.checkDir(tmpDir)) {
			MiscUtils.getLogger().debug("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
		}

		ImportDemographicDataForm frm = (ImportDemographicDataForm) form;
        logger.info("import to course id "  + frm.getCourseId() + " using timeshift value " + frm.getTimeshiftInDays());
        List<Provider> students = new ArrayList<Provider>();
        int courseId = 0;
        if(frm.getCourseId()!=null && frm.getCourseId().length()>0) {
        	courseId = Integer.valueOf(frm.getCourseId());
        	if(courseId>0) {
        		logger.info("need to apply this import to a learning environment");
        		//get all the students from this course        		
        		List<ProgramProvider> courseProviders = programManager.getProgramProviders(String.valueOf(courseId));
        		for(ProgramProvider pp:courseProviders) {
        			if(pp.getRole().getName().equalsIgnoreCase("student")) {
        				students.add(pp.getProvider());
        			}
        		}        		
        	}
        }
        logger.info("apply this patient to " + students.size() + " students");
		matchProviderNames = frm.getMatchProviderNames();
		FormFile imp = frm.getImportFile();
		String ifile = tmpDir + imp.getFileName();
		ArrayList warnings = new ArrayList();
		Vector logs = new Vector();
		File importLog = null;

	try {
		InputStream is = imp.getInputStream();
		OutputStream os = new FileOutputStream(ifile);
		byte[] buf = new byte[1024];
		int len;
		while ((len=is.read(buf)) > 0) os.write(buf,0,len);
		is.close();
		os.close();

		if (matchFileExt(ifile, "zip")) {
			ZipInputStream in = new ZipInputStream(new FileInputStream(ifile));
			boolean noXML = true;
			ZipEntry entry = in.getNextEntry();
			String entryDir = "";

			while (entry!=null) {
				String entryName = entry.getName();
				if (entry.isDirectory()) entryDir = entryName;
				if (entryName.startsWith(entryDir)) entryName = entryName.substring(entryDir.length());

				String ofile = tmpDir + entryName;
				if (matchFileExt(ofile, "xml")) {
					noXML = false;
					OutputStream out = new FileOutputStream(ofile);
					while ((len=in.read(buf)) > 0) out.write(buf,0,len);
					out.close();					
					logs.add(importXML(ofile, warnings, request,frm.getTimeshiftInDays(),students,courseId));
				}
				entry = in.getNextEntry();
			}
			if (noXML) {
				Util.cleanFile(ifile);
				throw new Exception ("Error! No XML file in zip");
			} else {
				importLog = makeImportLog(logs, tmpDir);
			}
			in.close();
			Util.cleanFile(ifile);

		} else if (matchFileExt(ifile, "xml")) {
			logs.add(importXML(ifile, warnings, request,frm.getTimeshiftInDays(),students,courseId));
			importLog = makeImportLog(logs, tmpDir);		
		} else {
			Util.cleanFile(ifile);
			throw new Exception ("Error! Import file must be XML or ZIP");
		}
	} catch (Exception e) {
		warnings.add("Error processing file: " + imp.getFileName());
		MiscUtils.getLogger().error("Error", e);
	}

		//channel warnings and importlog to browser
		request.setAttribute("warnings",warnings);
		if (importLog!=null) request.setAttribute("importlog",importLog.getPath());

		return mapping.findForward("success");
	}

    String[] importXML(String xmlFile, ArrayList warnings, HttpServletRequest request, int timeShiftInDays,List<Provider> students, int courseId) throws SQLException, Exception {
        if(students == null || students.size()==0) {
        	return importXML(xmlFile,warnings,request,timeShiftInDays,null,null,0);
        }
        
        List<String> logs = new ArrayList<String>();
        
        for(Provider student:students) {
        	logger.info("importing patient for student " +  student.getFormattedName());
        	//need that student's personal program
        	Integer pid = programManager.getProgramIdByProgramName("program"+student.getProviderNo());
        	if(pid == null) {
        		logger.warn("student's program not found");
        		continue;
        	}
        	Program p = programManager.getProgram(pid);
        	
        	String[] result = importXML(xmlFile,warnings,request,timeShiftInDays,student,p,courseId);
        	logs.addAll(convertLog(result));
        }
        return logs.toArray(new String[logs.size()]);
    }
    
    private List<String> convertLog(String[] logs) {
    	List<String> tmp = new ArrayList<String>();
    	for(int x=0;x<logs.length;x++) {
    		tmp.add(logs[x]);
    	}
    	return tmp;
    }
    


	String[] importXML(String xmlFile, ArrayList warnings, HttpServletRequest request, int timeShiftInDays, Provider student, Program admitTo, int courseId) throws SQLException, Exception {
		ArrayList err_demo = new ArrayList(); //errors: duplicate demographics
		ArrayList err_data = new ArrayList(); //errors: discrete data
		ArrayList err_summ = new ArrayList(); //errors: summary
		ArrayList err_othe = new ArrayList(); //errors: other categories
		ArrayList err_note = new ArrayList(); //non-errors: notes

		String defaultProvider = getDefaultProvider();
		String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		docDir = Util.fixDirName(docDir);
		if (!Util.checkDir(docDir)) {
			MiscUtils.getLogger().debug("Error! Cannot write to DOCUMENT_DIR - Check oscar.properties or dir permissions.");
		}

		File xmlF = new File(xmlFile);
		OmdCdsDocument.OmdCds omdCds=null;
	try {
		omdCds = OmdCdsDocument.Factory.parse(xmlF).getOmdCds();
	} catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
	} catch (XmlException ex) {MiscUtils.getLogger().error("Error", ex);
	}
		PatientRecord patientRec = omdCds.getPatientRecord();

		//DEMOGRAPHICS
		Demographics demo = patientRec.getDemographics();
		cdsDt.PersonNameStandard.LegalName legalName = demo.getNames().getLegalName();
		String lastName="", firstName="";
		if (legalName!=null) {
			if (legalName.getLastName()!=null) lastName = StringUtils.noNull(legalName.getLastName().getPart());
			if (legalName.getFirstName()!=null) firstName = StringUtils.noNull(legalName.getFirstName().getPart());
			patientName = lastName+","+firstName;
		} else {
			err_data.add("Error! No Legal Name");
		}
		String title = demo.getNames().getNamePrefix()!=null ? demo.getNames().getNamePrefix().toString() : "";
		String sex = demo.getGender()!=null ? demo.getGender().toString() : "";
		if (StringUtils.empty(sex)) {
			err_data.add("Error! No Gender");
		}
		String birthDate = dateFullPartial(demo.getDateOfBirth(), timeShiftInDays);
		if (StringUtils.empty(birthDate)) {
			birthDate = null;
			err_data.add("Error! No Date Of Birth");
		}
		String roster_status = demo.getEnrollmentStatus()!=null ? demo.getEnrollmentStatus().toString() : "";
		if	(roster_status.equals("1")) roster_status = "RO";
		else if (roster_status.equals("0")) roster_status = "NR";
		else {
			err_data.add("Error! No Enrollment Status");
		}
		String patient_status = demo.getPersonStatusCode()!=null ? demo.getPersonStatusCode().toString() : "";
		if	(patient_status.equals("A")) patient_status = "AC";
		else if (patient_status.equals("I")) patient_status = "IN";
		else if (patient_status.equals("D")) patient_status = "DE";
		else if (patient_status.equals("O")) patient_status = "OTHER";
		else {
			err_data.add("Error! No Person Status Code");
		}
		String roster_date = dateFullPartial(demo.getEnrollmentDate(), timeShiftInDays); //roster_date=hc_renew_date in table
		String end_date = dateFullPartial(demo.getEnrollmentTerminationDate(), timeShiftInDays);
		String sin = StringUtils.noNull(demo.getSIN());

		String chart_no = StringUtils.noNull(demo.getChartNumber());
		String official_lang = "";
		if (demo.getPreferredOfficialLanguage()!=null) {
			official_lang = demo.getPreferredOfficialLanguage().toString();
			official_lang = official_lang.equals("ENG") ? "English" : official_lang;
			official_lang = official_lang.equals("FRE") ? "French" : official_lang;
		}
		String spoken_lang = StringUtils.noNull(demo.getPreferredSpokenLanguage());
		String dNote = StringUtils.noNull(demo.getNoteAboutPatient());
		String uvID = demo.getUniqueVendorIdSequence();
		String fmLink = demo.getFamilyMemberLink();
		String pwFlag = demo.getPatientWarningFlags();
		String psDate = dateFullPartial(demo.getPersonStatusDate(), timeShiftInDays);

		if (StringUtils.filled(uvID)) {
			if (StringUtils.empty(chart_no)) {
				chart_no = uvID;
				err_note.add("Unique Vendor Id imported as Chart No");
			} else {
				dNote = Util.appendLine(dNote, "Unique Vendor ID: ", uvID);
				err_note.add("Unique Vendor Id imported to Patient Note");
			}
		}
		if (StringUtils.filled(fmLink)) {
			dNote = Util.appendLine(dNote, "Family Member Link: ", fmLink);
			err_note.add("Family Member Link imported to Patient Note");
		}
		if (StringUtils.filled(pwFlag)) {
			dNote = Util.appendLine(dNote, "Patient Warning Flag: ", pwFlag);
			err_note.add("Patient Warning Flag imported to Patient Note");
		}
		if (StringUtils.filled(psDate)) {
			dNote = Util.appendLine(dNote, "Person Status Date: ", psDate);
			err_note.add("Person Status Date imported to Patient Note");
		}

		String versionCode="", hin="", hc_type="", eff_date="";
		cdsDt.HealthCard healthCard = demo.getHealthCard();
		if (healthCard!=null) {
			hin = StringUtils.noNull(healthCard.getNumber());
			if (hin.equals("")) {
				err_data.add("Error! No health card number!");
			}
			hc_type = getProvinceCode(healthCard.getProvinceCode());
			if (hc_type.equals("")) {
				err_data.add("Error! No Province Code for health card!");
			}
			versionCode = StringUtils.noNull(healthCard.getVersion());
			eff_date = getCalDate(healthCard.getExpirydate());
		}
		String address="", city="", province="", postalCode="";
		if (demo.getAddressArray().length>0) {
			cdsDt.Address addr = demo.getAddressArray(0);	//only get 1st address, other ignored
			if (addr!=null) {
				if (StringUtils.filled(addr.getFormatted())) {
					address = addr.getFormatted();
				} else {
					cdsDt.AddressStructured addrStr = addr.getStructured();
					if (addrStr!=null) {
						address = StringUtils.noNull(addrStr.getLine1()) + StringUtils.noNull(addrStr.getLine2()) + StringUtils.noNull(addrStr.getLine3());
						city = StringUtils.noNull(addrStr.getCity());
						province = getCountrySubDivCode(addrStr.getCountrySubdivisionCode());
						cdsDt.PostalZipCode postalZip = addrStr.getPostalZipCode();
						if (postalZip!=null) postalCode = StringUtils.noNull(postalZip.getPostalCode());
					}
				}
			}
		}
		cdsDt.PhoneNumber[] pn = demo.getPhoneNumberArray();
		String workPhone="", workExt="", homePhone="", homeExt="", cellPhone="", ext="", patientPhone="";
		for (int i=0; i<pn.length; i++) {
			String phone = pn[i].getPhoneNumber();
			if (StringUtils.empty(phone)) {
				if (StringUtils.filled(pn[i].getNumber())) {
					String areaCode = StringUtils.filled(pn[i].getAreaCode()) ? "("+pn[i].getAreaCode()+")" : "";
					phone = areaCode + pn[i].getNumber();
				}
			}
			if (StringUtils.filled(phone)) {
				if (StringUtils.filled(pn[i].getExtension())) ext = pn[i].getExtension();
				else if (StringUtils.filled(pn[i].getExchange())) ext = pn[i].getExchange();

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
				if (StringUtils.filled(homePhone)) patientPhone = homePhone+" "+homeExt;
			}
			if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.W) {
				if (StringUtils.filled(workPhone)) patientPhone = workPhone+" "+workExt;
			}
			if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.C) {
				if (StringUtils.filled(cellPhone)) patientPhone = cellPhone;
			}
		} else {
			if      (StringUtils.filled(homePhone)) patientPhone = homePhone+" "+homeExt;
			else if (StringUtils.filled(workPhone)) patientPhone = workPhone+" "+workExt;
			else if (StringUtils.filled(cellPhone)) patientPhone = cellPhone;
		}
		String email = StringUtils.noNull(demo.getEmail());

       String primaryPhysician = "";
        if(student == null){
	        if (demo.getPrimaryPhysician()!=null) {
	            String[] personName = getPersonName(demo.getPrimaryPhysician().getName());
	            String personOHIP = demo.getPrimaryPhysician().getOHIPPhysicianId();
	            primaryPhysician = writeProviderData(personName[0], personName[1], personOHIP);
	        }
	        if (StringUtils.empty(primaryPhysician)) {
	            primaryPhysician = defaultProvider;
	            err_data.add("Error! No Primary Physician; patient assigned to \"doctor oscardoc\"");
	        }
        } else {
        	primaryPhysician = student.getProviderNo();
        }

		String year_of_birth = null;
		String month_of_birth = null;
		String date_of_birth = null;
		if (birthDate!=null)
		{			
			Date bDate = UtilDateUtilities.StringToDate(birthDate,"yyyy-MM-dd");
			year_of_birth = UtilDateUtilities.DateToString(bDate,"yyyy");
			month_of_birth = UtilDateUtilities.DateToString(bDate,"MM");
			date_of_birth = UtilDateUtilities.DateToString(bDate,"dd");
		}
	
		DemographicData dd = new DemographicData();
		DemographicExt dExt = new DemographicExt();
		DemographicAddResult demoRes = null;

		//Check if Contact-only demographic exists
		DemographicData.Demographic demographic = null;
		
		if(courseId == 0) {
			demographicNo = dd.getDemoNoByNamePhoneEmail(firstName, lastName, homePhone, workPhone, email);
			demographic = dd.getDemographic(demographicNo);
		}
		if (demographic==null) { //demo not found, add patient
			demoRes = dd.addDemographic(title, lastName, firstName, address, city, province, postalCode, homePhone, workPhone, year_of_birth, month_of_birth, date_of_birth, hin, versionCode, roster_status, patient_status, ""/*date_joined*/, chart_no, official_lang, spoken_lang, primaryPhysician, sex, end_date, eff_date, ""/*pcn_indicator*/, hc_type, roster_date, ""/*family_doctor*/, email, ""/*pin*/, ""/*alias*/, ""/*previousAddress*/, ""/*children*/, ""/*sourceOfIncome*/, ""/*citizenship*/, sin);
			demographicNo = demoRes.getId();
		} else if (StringUtils.nullSafeEqualsIgnoreCase(demographic.getPatientStatus(), "Contact-only")) { //replace contact
			demographic.setTitle(title);
			demographic.setAddress(address);
			demographic.setCity(city);
			demographic.setProvince(province);
			demographic.setPostal(postalCode);
			demographic.setYearOfBirth(year_of_birth);
			demographic.setMonthOfBirth(month_of_birth);
			demographic.setDateOfBirth(date_of_birth);
			demographic.setJustHIN(hin);
			demographic.setVersionCode(versionCode);
			demographic.setRosterStatus(roster_status);
			demographic.setPatientStatus(patient_status);
			demographic.setChartNo(chart_no);
			demographic.setOfficialLang(official_lang);
			demographic.setSpokenLang(spoken_lang);
			demographic.setFamilyDoctor(primaryPhysician);
			demographic.setSex(sex);
			demographic.setEndDate(end_date);
			demographic.setEffDate(eff_date);
			demographic.setHCType(hc_type);
			demographic.setDateJoined(roster_date);
			demographic.setSin(sin);
			dd.setDemographic(demographic);
			err_note.add("Replaced Contact-only patient "+patientName+" (Demo no="+demographicNo+")");
			demoRes = dd.addDemographic(title, lastName, firstName, address, city, province, postalCode, homePhone, workPhone, year_of_birth, month_of_birth, date_of_birth, hin, versionCode, roster_status, patient_status, ""/*date_joined*/, chart_no, official_lang, spoken_lang, primaryPhysician, sex, end_date, eff_date, ""/*pcn_indicator*/, hc_type, roster_date, ""/*family_doctor*/, email, ""/*pin*/, ""/*alias*/, ""/*previousAddress*/, ""/*children*/, ""/*sourceOfIncome*/, ""/*citizenship*/, sin);
		}
		
		if (StringUtils.filled(demographicNo))
		{
        	//TODO: Course - Admit to student program
        	if(admitTo == null) {
        		insertIntoAdmission();
        	} else {
        		admissionManager.processAdmission(Integer.valueOf(demographicNo), student.getProviderNo(), admitTo, "", "batch import");
        	}
			
			if (StringUtils.filled(dNote)) dd.addDemographiccust(demographicNo, dNote);

			if (!workExt.equals("")) dExt.addKey(primaryPhysician, demographicNo, "wPhoneExt", workExt);
			if (!homeExt.equals("")) dExt.addKey(primaryPhysician, demographicNo, "hPhoneExt", homeExt);
			if (!cellPhone.equals("")) dExt.addKey(primaryPhysician, demographicNo, "demo_cell", cellPhone);
			if(courseId>0) dExt.addKey(primaryPhysician, demographicNo, "course", String.valueOf(courseId));
			
			Demographics.Contact[] contt = demo.getContactArray();
			for (int i=0; i<contt.length; i++) {
				String[] contactName = getPersonName(contt[i].getName());
				String cFirstName = contactName[0];
				String cLastName  = contactName[1];
				String cEmail = StringUtils.noNull(contt[i].getEmailAddress());

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

				String contactNote = contt[i].getNote();
				String cDemoNo = dd.getDemoNoByNamePhoneEmail(cFirstName, cLastName, homePhone, workPhone, cEmail);
				String cPatient = cLastName+","+cFirstName;
				if (StringUtils.empty(cDemoNo)) {   //add new demographic as contact
					demoRes = dd.addDemographic("", cLastName, cFirstName, "", "", "", "", homePhone, workPhone, null, null,
					null, "", "", "", "Contact-only", "", "", "", "", "", "F", "", "", "", "", "", "", cEmail, "", "", "", "", "", "", "");
					cDemoNo = demoRes.getId();
					err_note.add("Contact-only patient "+cPatient+" (Demo no="+cDemoNo+") created");

					if (StringUtils.filled(contactNote)) dd.addDemographiccust(cDemoNo, contactNote);
					if (!workExt.equals("")) dExt.addKey("", cDemoNo, "wPhoneExt", workExt);
					if (!homeExt.equals("")) dExt.addKey("", cDemoNo, "hPhoneExt", homeExt);
					if (!cellPhone.equals("")) dExt.addKey("", cDemoNo, "demo_cell", cellPhone);
				}
				if (StringUtils.filled(cDemoNo)) {
					DemographicRelationship demoRel = new DemographicRelationship();
					demoRel.addDemographicRelationship(demographicNo, cDemoNo, rel, sdm, emc, ""/*notes*/, primaryPhysician, null);
					err_note.add("Added relationship with patient "+cPatient+" (Demo no="+cDemoNo+")");
				}
			}


			//PERSONAL HISTORY
			Set<CaseManagementIssue> scmi = null;	//Declare a set for CaseManagementIssues
			PersonalHistory[] pHist = patientRec.getPersonalHistoryArray();
			for (int i=0; i<pHist.length; i++) {
				if (i==0) scmi = getCMIssue("SocHistory");
				CaseManagementNote cmNote = prepareCMNote();
				cmNote.setIssues(scmi);
				String socialHist = "";
				if (StringUtils.filled(pHist[i].getCategorySummaryLine())) {
					socialHist = Util.appendLine(socialHist, pHist[i].getCategorySummaryLine().trim());
				} else {
					err_summ.add("No Summary for Personal History ("+(i+1)+")");
				}
				socialHist = Util.appendLine(socialHist, getResidual(pHist[i].getResidualInfo()));
				if (StringUtils.filled(socialHist)) {
					cmNote.setNote(socialHist);
					caseManagementManager.saveNoteSimple(cmNote);
				}
			}
			//FAMILY HISTORY
			FamilyHistory[] fHist = patientRec.getFamilyHistoryArray();
			for (int i=0; i<fHist.length; i++) {
				if (i==0) scmi = getCMIssue("FamHistory");
				CaseManagementNote cmNote = prepareCMNote();
				if (fHist[i].getDiagnosisCode()==null) {
					cmNote.setIssues(scmi);
				} else {
					cmNote.setIssues(getCMIssue("FamHistory", fHist[i].getDiagnosisCode()));
				}
				String familyHist = "";
				familyHist = Util.appendLine(familyHist, fHist[i].getDiagnosisProblemDescription());
				familyHist = Util.appendLine(familyHist, getCode(fHist[i].getDiagnosisCode(),"Diagnosis"));

				String summary = fHist[i].getCategorySummaryLine();
				if (StringUtils.empty(summary)) {
					err_summ.add("No Summary for Family History ("+(i+1)+")");
					summary = "Family History "+(i+1);
				}

				Long hostNoteId = null;
				cmNote.setNote(StringUtils.filled(familyHist) ? familyHist : summary);
				caseManagementManager.saveNoteSimple(cmNote);
				hostNoteId = cmNote.getId();

				cmNote = prepareCMNote();
				String note = StringUtils.noNull(fHist[i].getNotes());

				if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
				note = Util.appendLine(note, summary);

				note = Util.appendLine(note, getResidual(fHist[i].getResidualInfo()));
				cmNote.setNote(note);
				saveLinkNote(hostNoteId, cmNote);

				CaseManagementNoteExt cme = new CaseManagementNoteExt();
				cme.setNoteId(hostNoteId);
				if (fHist[i].getStartDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.STARTDATE);
					cme.setDateValue(dDateFullPartial(fHist[i].getStartDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				}
				if (fHist[i].getAgeAtOnset()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.AGEATONSET);
					cme.setValue(fHist[i].getAgeAtOnset().toString());
					caseManagementManager.saveNoteExt(cme);
				}
				if (StringUtils.filled(fHist[i].getRelationship())) {
					cme.setKeyVal(CaseManagementNoteExt.RELATIONSHIP);
					cme.setValue(fHist[i].getRelationship());
					caseManagementManager.saveNoteExt(cme);
				}
				if (StringUtils.filled(fHist[i].getTreatment())) {
					cme.setKeyVal(CaseManagementNoteExt.TREATMENT);
					cme.setValue(fHist[i].getTreatment());
					caseManagementManager.saveNoteExt(cme);
				}
			}
			//PAST HEALTH
			PastHealth[] pHealth = patientRec.getPastHealthArray();
			for (int i=0; i< pHealth.length; i++) {
				if (i==0) scmi = getCMIssue("MedHistory");
				CaseManagementNote cmNote = prepareCMNote();
				if (pHealth[i].getDiagnosisOrProcedureCode()==null) {
					cmNote.setIssues(scmi);
				} else {
					cmNote.setIssues(getCMIssue("MedHistory", pHealth[i].getDiagnosisOrProcedureCode()));
				}
				String medicalHist = "";
				medicalHist = Util.appendLine(medicalHist, getCode(pHealth[i].getDiagnosisOrProcedureCode(),"Diagnosis/Procedure"));
				if (pHealth[i].getMedicalSurgicalFlag()!=null) medicalHist = Util.appendLine(medicalHist,"Medical Surgical Flag: ",pHealth[i].getMedicalSurgicalFlag().toString());
				medicalHist = Util.appendLine(medicalHist,"Resolved ? ",pHealth[i].getResolvedIndicator());

				String summary = pHealth[i].getCategorySummaryLine();
				if (StringUtils.empty(summary)) {
					err_summ.add("No Summary for Past Health ("+(i+1)+")");
					summary = "Medical History "+(i+1);
				}

				Long hostNoteId = null;
				cmNote.setNote(StringUtils.filled(medicalHist) ? medicalHist : summary);
				caseManagementManager.saveNoteSimple(cmNote);
				hostNoteId = cmNote.getId();

				cmNote = prepareCMNote();
				String note = StringUtils.noNull(pHealth[i].getNotes());

				if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
				note = Util.appendLine(note, summary);

				note = Util.appendLine(note, getResidual(pHealth[i].getResidualInfo()));
				cmNote.setNote(note);
				saveLinkNote(hostNoteId, cmNote);

				CaseManagementNoteExt cme = new CaseManagementNoteExt();
				cme.setNoteId(hostNoteId);
				if (StringUtils.filled(pHealth[i].getPastHealthProblemDescriptionOrProcedures())) {
					cme.setKeyVal(CaseManagementNoteExt.TREATMENT);
					cme.setValue(pHealth[i].getPastHealthProblemDescriptionOrProcedures());
					caseManagementManager.saveNoteExt(cme);
				}
				if (pHealth[i].getResolvedDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
					cme.setDateValue(dDateFullPartial(pHealth[i].getResolvedDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				}
			}
			//PROBLEM LIST
			ProblemList[] probList = patientRec.getProblemListArray();
			for (int i=0; i<probList.length; i++) {
				if (i==0) scmi = getCMIssue("Concerns");
				CaseManagementNote cmNote = prepareCMNote();
				if (probList[i].getDiagnosisCode()==null) {
					cmNote.setIssues(scmi);
				} else {
					cmNote.setIssues(getCMIssue("Concerns", probList[i].getDiagnosisCode()));
				}
				String ongConcerns = "";
				ongConcerns = Util.appendLine(ongConcerns, probList[i].getProblemDescription());
				ongConcerns = Util.appendLine(ongConcerns, getCode(probList[i].getDiagnosisCode(),"Diagnosis"));

				String summary = probList[i].getCategorySummaryLine();
				if (StringUtils.empty(summary)) {
					err_summ.add("No Summary for Problem List ("+(i+1)+")");
					summary = "Ongoing Concerns "+(i+1);
				}

				Long hostNoteId = null;
				cmNote.setNote(StringUtils.filled(ongConcerns) ? ongConcerns : summary);
				caseManagementManager.saveNoteSimple(cmNote);
				hostNoteId = cmNote.getId();

				cmNote = prepareCMNote();
				String note = StringUtils.noNull(probList[i].getNotes());

				if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
				note = Util.appendLine(note, summary);

				note = Util.appendLine(note, getResidual(probList[i].getResidualInfo()));
				cmNote.setNote(note);
				saveLinkNote(hostNoteId, cmNote);

				CaseManagementNoteExt cme = new CaseManagementNoteExt();
				cme.setNoteId(hostNoteId);
				if (probList[i].getOnsetDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.STARTDATE);
					cme.setDateValue(dDateFullPartial(probList[i].getOnsetDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				} else {
					err_data.add("Error! No Onset Date for Problem List ("+(i+1)+")");
				}
				if (probList[i].getResolutionDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
					cme.setDateValue(dDateFullPartial(probList[i].getResolutionDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				}
				if (StringUtils.filled(probList[i].getProblemStatus())) {
					cme.setKeyVal(CaseManagementNoteExt.PROBLEMSTATUS);
					cme.setValue(probList[i].getProblemStatus());
					caseManagementManager.saveNoteExt(cme);
				}
			}
			//RISK FACTORS
			RiskFactors[] rFactors = patientRec.getRiskFactorsArray();
			for (int i=0; i<rFactors.length; i++) {
				if (i==0) scmi = getCMIssue("RiskFactors");
				CaseManagementNote cmNote = prepareCMNote();
				cmNote.setIssues(scmi);
				String riskFactors = "";
				riskFactors = Util.appendLine(riskFactors,"Risk Factor: ",rFactors[i].getRiskFactor());

				String summary = rFactors[i].getCategorySummaryLine();
				if (StringUtils.empty(summary)) {
					err_summ.add("No Summary for Risk Factors ("+(i+1)+")");
					summary = "Risk Factors "+(i+1);
				}

				Long hostNoteId = null;
				cmNote.setNote(StringUtils.filled(riskFactors) ? riskFactors : summary);
				caseManagementManager.saveNoteSimple(cmNote);
				hostNoteId = cmNote.getId();

				cmNote = prepareCMNote();
				String note = StringUtils.noNull(rFactors[i].getNotes());

				if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
				note = Util.appendLine(note, summary);

				note = Util.appendLine(note, getResidual(rFactors[i].getResidualInfo()));
				cmNote.setNote(note);
				saveLinkNote(hostNoteId, cmNote);

				CaseManagementNoteExt cme = new CaseManagementNoteExt();
				cme.setNoteId(hostNoteId);
				if (rFactors[i].getStartDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.STARTDATE);
					cme.setDateValue(dDateFullPartial(rFactors[i].getStartDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				}
				if (rFactors[i].getEndDate()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.RESOLUTIONDATE);
					cme.setDateValue(dDateFullPartial(rFactors[i].getEndDate(), timeShiftInDays));
					caseManagementManager.saveNoteExt(cme);
				}
				if (rFactors[i].getAgeOfOnset()!=null) {
					cme.setKeyVal(CaseManagementNoteExt.AGEATONSET);
					cme.setValue(rFactors[i].getAgeOfOnset().toString());
					caseManagementManager.saveNoteExt(cme);
				}
				if (StringUtils.filled(rFactors[i].getExposureDetails())) {
					cme.setKeyVal(CaseManagementNoteExt.EXPOSUREDETAIL);
					cme.setValue(rFactors[i].getExposureDetails());
					caseManagementManager.saveNoteExt(cme);
				}
			}
			//CLINICAL NOTES
			ClinicalNotes[] cNotes = patientRec.getClinicalNotesArray();
			for (int i=0; i<cNotes.length; i++) {
				CaseManagementNote cmNote = prepareCMNote();
				if (cNotes[i].getEventDateTime()==null) cmNote.setObservation_date(new Date());
				else cmNote.setObservation_date(dDateFullPartial(cNotes[i].getEventDateTime(), timeShiftInDays));
				if (cNotes[i].getSignedDateTime()==null) cmNote.setUpdate_date(new Date());
				else cmNote.setUpdate_date(dDateFullPartial(cNotes[i].getSignedDateTime(), timeShiftInDays));

				String encounter = cNotes[i].getMyClinicalNotesContent();
				encounter = Util.appendLine(encounter,"Note Type: ",cNotes[i].getNoteType());
				if (cNotes[i].getPrincipalAuthor()!=null) {
					ClinicalNotes.PrincipalAuthor cpAuthor = cNotes[i].getPrincipalAuthor();
					String[] authorName = getPersonName(cpAuthor.getName());
					String authorOHIP = cpAuthor.getOHIPPhysicianId();
					String authorProvider = writeProviderData(authorName[0], authorName[1], authorOHIP);
					if (StringUtils.empty(authorProvider)) {
						authorProvider = defaultProvider;
						err_note.add("Clinical notes have no author; assigned to \"doctor oscardoc\" ("+(i+1)+")");
					}
					cmNote.setProviderNo(authorProvider);
					encounter = Util.appendLine(encounter, "Principal Author Function: ", cNotes[i].getPrincipalAuthorFunction());
				}
				if (cNotes[i].getSigningOHIPPhysicianId()!=null) {
					String signPhysicianOHIP = cNotes[i].getSigningOHIPPhysicianId();
					String signProvider = writeProviderData("", "", signPhysicianOHIP);
					if (StringUtils.empty(signProvider)) {
						signProvider = defaultProvider;
						err_note.add("Clinical notes have no signer; assigned to \"doctor oscardoc\" ("+(i+1)+")");
					}
					cmNote.setSigning_provider_no(signProvider);
				}
				encounter = Util.appendLine(encounter,"Signing Physician OHIP Id: ",cNotes[i].getSigningOHIPPhysicianId());
				encounter = Util.appendLine(encounter,"Entered DateTime: ",dateFullPartial(cNotes[i].getEnteredDateTime(), timeShiftInDays));
				if (StringUtils.filled(encounter)) {
					cmNote.setNote(encounter);
					caseManagementManager.saveNoteSimple(cmNote);
				}
			}

			//ALLERGIES & ADVERSE REACTIONS
			AllergiesAndAdverseReactions[] aaReactArray = patientRec.getAllergiesAndAdverseReactionsArray();
			for (int i=0; i<aaReactArray.length; i++) {
				String description="", regionalId="", reaction="", severity="", entryDate="", startDate="", typeCode="";

				reaction = StringUtils.noNull(aaReactArray[i].getReaction());
				description = StringUtils.noNull(aaReactArray[i].getOffendingAgentDescription());
				entryDate = dateFullPartial(aaReactArray[i].getRecordedDate(), timeShiftInDays);
				startDate = dateFullPartial(aaReactArray[i].getStartDate(), timeShiftInDays);
				if (StringUtils.empty(entryDate)) entryDate = null;
				if (StringUtils.empty(startDate)) startDate = null;

				if (aaReactArray[i].getCode()!=null) regionalId = StringUtils.noNull(aaReactArray[i].getCode().getValue());
				reaction = Util.appendLine(reaction,"Offending Agent Description: ",aaReactArray[i].getOffendingAgentDescription());
				if (aaReactArray[i].getReactionType()!=null) reaction = Util.appendLine(reaction,"Reaction Type: ",aaReactArray[i].getReactionType().toString());
				if (!getYN(aaReactArray[i].getKnownAllergies()).equals("")) reaction = Util.appendLine(reaction,"Known Allergies: ",getYN(aaReactArray[i].getKnownAllergies()));
				if (aaReactArray[i].getHealthcarePractitionerType()!=null) reaction = Util.appendLine(reaction,"Healthcare Practitioner Type: ",aaReactArray[i].getHealthcarePractitionerType().toString());

				if (typeCode.equals("") && aaReactArray[i].getPropertyOfOffendingAgent()!=null) {
					if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.DR) typeCode="13"; //drug
					else if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.ND) typeCode="0"; //non-drug
					else if (aaReactArray[i].getPropertyOfOffendingAgent()==cdsDt.PropertyOfOffendingAgent.UK) typeCode="0"; //unknown
				}
				if (aaReactArray[i].getSeverity()!=null) {
					if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.MI) severity="1"; //mild
					else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.MO) severity="2"; //moderate
					else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.LT) severity="3"; //severe
					else if (aaReactArray[i].getSeverity()==cdsDt.AdverseReactionSeverity.NO) severity="4"; //unknown
				}
				Long allergyId = RxAllergyImport.save(demographicNo, entryDate, description, typeCode, reaction, startDate, severity, regionalId);

				CaseManagementNote cmNote = prepareCMNote();
				String note = StringUtils.noNull(aaReactArray[i].getNotes());

				if (StringUtils.filled(aaReactArray[i].getCategorySummaryLine())) {
					if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
					note = Util.appendLine(note, aaReactArray[i].getCategorySummaryLine().trim());
				} else {
					err_summ.add("No Summary for Allergies & Adverse Reactions ("+(i+1)+")");
				}
				note = Util.appendLine(note, getResidual(aaReactArray[i].getResidualInfo()));

				cmNote.setNote(note);
				saveLinkNote(cmNote, CaseManagementNoteLink.ALLERGIES, allergyId);
			}


			//MEDICATIONS & TREATMENTS
			MedicationsAndTreatments[] medArray = patientRec.getMedicationsAndTreatmentsArray();
			for (int i=0; i<medArray.length; i++) {
				String rxDate="", endDate="", writtenDate="", BN="", regionalId="", frequencyCode="", duration="1";
				String quantity="", special="", route="", drugForm="", dosage="", unit="", lastRefillDate="";
				String createDate = UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
				boolean longTerm=false, pastMed=false;
				int takemin=0, takemax=0, repeat=0, patientCompliance=0;

				writtenDate		= dateFullPartial(medArray[i].getPrescriptionWrittenDate(), timeShiftInDays);
				rxDate			= dateFullPartial(medArray[i].getStartDate(), timeShiftInDays);
				endDate			= dateFullPartial(medArray[i].getEndDate(), timeShiftInDays);
				lastRefillDate	= dateFullPartial(medArray[i].getLastRefillDate(), timeShiftInDays);
				regionalId		= StringUtils.noNull(medArray[i].getDrugIdentificationNumber());
				quantity		= Util.getNum(medArray[i].getQuantity());
				duration		= Util.getNum(medArray[i].getDuration());
				frequencyCode	= procFreq(frequencyCode);
				route			= StringUtils.noNull(medArray[i].getRoute());
				drugForm		= StringUtils.noNull(medArray[i].getForm());
				longTerm		= getYN(medArray[i].getLongTermMedication()).equals("Yes");
				pastMed			= getYN(medArray[i].getPastMedications()).equals("Yes");

				rxDate = StringUtils.filled(rxDate) ? rxDate : null;
				endDate = StringUtils.filled(endDate) ? endDate : null;

				String pc = getYN(medArray[i].getPatientCompliance());
				if (pc.equals("Yes")) patientCompliance = 1;
				else if (pc.equals("No")) patientCompliance = -1;
				else patientCompliance = 0;

				if (duration.trim().equals("1 year")) duration = "365"; //coping with scenario in CMS 2.0

				if (StringUtils.filled(medArray[i].getNumberOfRefills())) {
					repeat = Integer.parseInt(medArray[i].getNumberOfRefills());
				}

				if (StringUtils.filled(medArray[i].getDrugName())) {
					BN = medArray[i].getDrugName();
					special = medArray[i].getDrugName();
				} else {
					err_data.add("Error! No Drug Name in Medications & Treatments ("+(i+1)+")");
				}

				if (medArray[i].getStrength()!=null) {
					dosage = StringUtils.noNull(medArray[i].getStrength().getAmount())+" "+StringUtils.noNull(medArray[i].getStrength().getUnitOfMeasure());
					special = Util.appendLine(special, "Strength: ", dosage);
				}
				//special = Util.appendLine(special, "DIN: ", regionalId);
				//special = Util.appendLine(special, "Quantity: ", medArray[i].getQuantity());
				special = Util.appendLine(special, "Take ", medArray[i].getDosage());
				special += StringUtils.filled(route) ? " "+route : "";
				special += StringUtils.filled(frequencyCode) ? " "+frequencyCode : "";
				special += StringUtils.filled(duration) ? " for "+duration : "";

				special = Util.appendLine(special, "Prescription Instructions: ", medArray[i].getPrescriptionInstructions());
				special += StringUtils.filled(special) ? "\n" : "";

				String dose = StringUtils.noNull(medArray[i].getDosage());
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
					takemin = Integer.parseInt(Util.getNum(dose));
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

					ProviderData pd = getProviderByOhip(personOHIP);
					if (pd!=null) prescribedBy = pd.getProviderNo();
						else { //outside provider
						outsiderName = personName[1] + ", " + personName[0];
						outsiderOhip = personOHIP;
					}
				}

				Long drugId = RxPrescriptionImport.save(prescribedBy,demographicNo,rxDate,endDate,writtenDate,createDate,
				BN,regionalId,frequencyCode,duration,quantity,repeat,lastRefillDate,special,route,drugForm,
				dosage,takemin,takemax,unit,longTerm,pastMed,patientCompliance,outsiderName,outsiderOhip,(i+1));

				CaseManagementNote cmNote = prepareCMNote();
				String note = StringUtils.noNull(medArray[i].getNotes());

				if (StringUtils.filled(medArray[i].getCategorySummaryLine())) {
					if (StringUtils.filled(note)) note = Util.appendLine(note, "- Summary -");
					note = Util.appendLine(note, medArray[i].getCategorySummaryLine().trim());
				} else {
					err_summ.add("No Summary for Medications & Treatments ("+(i+1)+")");
				}
				note = Util.appendLine(note, getResidual(medArray[i].getResidualInfo()));

				cmNote.setNote(note);
				saveLinkNote(cmNote, CaseManagementNoteLink.DRUGS, drugId);
			}


			//IMMUNIZATIONS
			Immunizations[] immuArray = patientRec.getImmunizationsArray();
			if (immuArray.length>0) {
				err_note.add("All Immunization info assigned to doctor oscardoc");
			}
			for (int i=0; i<immuArray.length; i++) {
				String preventionDate="", preventionType="", refused="0", comments="";
				ArrayList preventionExt = new ArrayList();

				if (StringUtils.filled(immuArray[i].getImmunizationName())) {
					preventionType = mapPreventionType(immuArray[i].getImmunizationCode());
					if (preventionType.equals("")) {
						preventionType = "OtherA";
						err_note.add("Immunization "+immuArray[i].getImmunizationName()+" contains no DIN number. Item mapped to Other Layout A");
					}
					comments = Util.appendLine(comments, "Immunization Name: ", immuArray[i].getImmunizationName());
				} else {
					err_data.add("Error! No Immunization Name ("+(i+1)+")");
				}
				preventionDate = dateFullPartial(immuArray[i].getDate(), timeShiftInDays);
				refused = getYN(immuArray[i].getRefusedFlag()).equals("Yes") ? "1" : "0";

				String iSummary="";
				if (immuArray[i].getCategorySummaryLine()!=null) {
					iSummary = immuArray[i].getCategorySummaryLine().trim();
				} else {
					err_summ.add("No Summary for Immunizations ("+(i+1)+")");
				}
				comments = Util.appendLine(comments, immuArray[i].getNotes());
				if (StringUtils.filled(iSummary)) {
					comments = Util.appendLine(comments, "Summary: ", iSummary);
					err_note.add("Immunization Summary imported in [comments] ("+(i+1)+")");
				}
				comments = Util.appendLine(comments, getCode(immuArray[i].getImmunizationCode(),"Immunization Code"));
				comments = Util.appendLine(comments, "Instructions: ", immuArray[i].getInstructions());
				comments = Util.appendLine(comments, getResidual(immuArray[i].getResidualInfo()));
				if (StringUtils.filled(comments)) {
					Hashtable ht = new Hashtable();
					ht.put("comments", comments);
					preventionExt.add(ht);
				}
				if (StringUtils.filled(immuArray[i].getManufacturer())) {
					Hashtable ht = new Hashtable();
					ht.put("manufacture", immuArray[i].getManufacturer());
					preventionExt.add(ht);
				}
				if (StringUtils.filled(immuArray[i].getLotNumber())) {
					Hashtable ht = new Hashtable();
					ht.put("lot", immuArray[i].getLotNumber());
					preventionExt.add(ht);
				}
				if (StringUtils.filled(immuArray[i].getRoute())) {
					Hashtable ht = new Hashtable();
					ht.put("route", immuArray[i].getRoute());
					preventionExt.add(ht);
				}
				if (StringUtils.filled(immuArray[i].getSite())) {
					Hashtable ht = new Hashtable();
					ht.put("location", immuArray[i].getSite());
					preventionExt.add(ht);
				}
				if (StringUtils.filled(immuArray[i].getDose())) {
					Hashtable ht = new Hashtable();
					ht.put("dose", immuArray[i].getDose());
					preventionExt.add(ht);
				}
				PreventionData prevD = new PreventionData();
				prevD.insertPreventionData(admProviderNo, demographicNo, preventionDate, defaultProvider, "", preventionType, refused, "", "", preventionExt);
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
			String[] _lab_no      = new String[labResultArr.length];
			String[] _rev_date    = new String[labResultArr.length];
			String[] _req_date    = new String[labResultArr.length];

			// Save to labPatientPhysicianInfo, labTestResults, patientLabRouting
			for (int i=0; i<labResultArr.length; i++) {
				_testName[i] = StringUtils.noNull(labResultArr[i].getLabTestCode());
				_location[i] = StringUtils.noNull(labResultArr[i].getLaboratoryName());
				_accession[i] = StringUtils.noNull(labResultArr[i].getAccessionNumber());
				_comments[i] = StringUtils.noNull(labResultArr[i].getPhysiciansNotes());
				_coll_date[i] = dateOnly(dateFullPartial(labResultArr[i].getCollectionDateTime(), timeShiftInDays));
				_req_date[i] = dateFullPartial(labResultArr[i].getLabRequisitionDateTime(), timeShiftInDays);
				_rev_date[i] = dateFullPartial(labResultArr[i].getDateTimeResultReviewed(), timeShiftInDays);

				_title[i] = StringUtils.noNull(labResultArr[i].getTestName());
				if (StringUtils.filled(labResultArr[i].getTestNameReportedByLab())) {
					_title[i] += StringUtils.filled(_title[i]) ? "/" : "";
					_title[i] += labResultArr[i].getTestNameReportedByLab();
				}
				_description[i] = Util.appendLine(_description[i], "Test Results Info: ", labResultArr[i].getTestResultsInformationReportedByTheLab());
				_description[i] = Util.appendLine(_description[i], "Notes from Lab: ", labResultArr[i].getNotesFromLab());
				_description[i] = Util.appendLine(_description[i], "Received Datetime: ", dateFullPartial(labResultArr[i].getDateTimeResultReceivedByCMS(), timeShiftInDays));

				if (labResultArr[i].getResultNormalAbnormalFlag()!=null) {
					cdsDt.ResultNormalAbnormalFlag.Enum flag = labResultArr[i].getResultNormalAbnormalFlag();
					if (flag.equals(cdsDt.ResultNormalAbnormalFlag.Y)) _abn[i] = "A";
					if (flag.equals(cdsDt.ResultNormalAbnormalFlag.N)) _abn[i] = "N";
				}

				if (labResultArr[i].getResult()!=null) {
					_result[i] = StringUtils.noNull(labResultArr[i].getResult().getValue());
					_unit[i] = StringUtils.noNull(labResultArr[i].getResult().getUnitOfMeasure());
				}

				if (labResultArr[i].getReferenceRange()!=null) {
					LaboratoryResults.ReferenceRange ref = labResultArr[i].getReferenceRange();
					if (StringUtils.filled(ref.getReferenceRangeText())) {
						_minimum[i] = ref.getReferenceRangeText();
					} else {
						_maximum[i] = StringUtils.noNull(ref.getHighLimit());
						_minimum[i] = StringUtils.noNull(ref.getLowLimit());
					}
				}

				LaboratoryResults.ResultReviewer resultRev = labResultArr[i].getResultReviewer();
				if (resultRev!=null) {
					String[] revName = getPersonName(resultRev.getName());
					String revOhip = StringUtils.noNull(resultRev.getOHIPPhysicianId());
					_reviewer[i] = writeProviderData(revName[0], revName[1], revOhip);
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

							Long plrId = LabResultImport.savePatientLabRouting(demographicNo, paPhysId);
							LabRequestReportLink.save(null,null,_req_date[i],"labPatientPhysicianInfo",Long.valueOf(paPhysId));

							String status = StringUtils.filled(_reviewer[i]) ? "A" : "N";
							_reviewer[i] = status.equals("A") ? _reviewer[i] : "0";
							LabResultImport.saveProviderLabRouting(_reviewer[i], paPhysId, status, _comments[i], _rev_date[i]);

							accNew = false;
						}
						_lab_no[i] = paPhysId;
						String last = StringUtils.filled(_description[i]) ? "N" : "Y";
						LabResultImport.saveLabTestResults(_title[i], _testName[i], _abn[i], _minimum[i], _maximum[i], _result[i], _unit[i], "", _location[i], paPhysId, "C", last);
						if (last.equals("N")) {
							LabResultImport.saveLabTestResults(_title[i], _testName[i], "", "", "", "", "", _description[i], _location[i], paPhysId, "D", "Y");
						}
					}
				}
			}
			/*
			String labEverything = getLabDline(labResultArr[i]);
			if (StringUtils.filled(labEverything)){
				LabResultImport.SaveLabDesc(labEverything,patiPhysId);
			}
			*/

			// Save to measurements, measurementsExt
			for (LaboratoryResults labResults : labResultArr) {
				Measurements meas = new Measurements(Long.valueOf(demographicNo), admProviderNo);
				LaboratoryResults.Result result = labResults.getResult();
				String unit = null;
				if (result!=null) {
					meas.setDataField(StringUtils.noNull(result.getValue()));
					unit = StringUtils.noNull(result.getUnitOfMeasure());
				} else {
					meas.setDataField("");
				}
				if (labResults.getDateTimeResultReceivedByCMS()!=null) {
					meas.setDateEntered(dDateFullPartial(labResults.getDateTimeResultReceivedByCMS(), timeShiftInDays));
				} else {
					meas.setDateEntered(new Date());
				}
				ImportExportMeasurements.saveMeasurements(meas);
				Long measId = meas.getId();
				saveMeasurementsExt(measId, "unit", unit);
				String testCode = StringUtils.filled(labResults.getLabTestCode()) ? labResults.getLabTestCode() : "";
				String testName = StringUtils.noNull(labResults.getTestName());
				if (StringUtils.filled(labResults.getTestNameReportedByLab())) {
					testName += StringUtils.filled(testName) ? "/" : "";
					testName += labResults.getTestNameReportedByLab();
				}
				saveMeasurementsExt(measId, "identifier", testCode);
				saveMeasurementsExt(measId, "name", testName);

				cdsDt.DateFullOrPartial collDate = labResults.getCollectionDateTime();
				if (collDate!=null) {
					saveMeasurementsExt(measId, "datetime", dateFullPartial(collDate, timeShiftInDays));
				} else {
					err_data.add("Error! No Collection DateTime for Lab Test "+testCode+" for Patient "+demographicNo);
				}
				String labname = StringUtils.noNull(labResults.getLaboratoryName());
				if (StringUtils.filled(labname)) {
					saveMeasurementsExt(measId, "labname", labname);
				} else {
					err_data.add("Error! No Laboratory Name for Lab Test "+testCode+" for Patient "+demographicNo);
				}
				cdsDt.ResultNormalAbnormalFlag.Enum abnFlag = labResults.getResultNormalAbnormalFlag();
				if (abnFlag!=null) {
					String abn = abnFlag.toString();
					if (!abn.equals("U")) {
						saveMeasurementsExt(measId, "abnormal", (abn.equals("Y")?"A":abn));
					}
				} else {
					err_data.add("Error! No Normal/Abnormal Flag for Lab Test "+testCode+" for Patient "+demographicNo);
				}

				String labNotes = Util.appendLine("", "Test Results Info: ", labResults.getTestResultsInformationReportedByTheLab());
				labNotes = Util.appendLine(labNotes, "Notes from Lab: ", labResults.getNotesFromLab());
				saveMeasurementsExt(measId, "comments", labNotes);
				String accnum = labResults.getAccessionNumber();
				saveMeasurementsExt(measId, "accession", accnum);

				LaboratoryResults.ReferenceRange refRange = labResults.getReferenceRange();
				if (refRange!=null) {
					if (StringUtils.filled(refRange.getReferenceRangeText())) {
						saveMeasurementsExt(measId, "range", refRange.getReferenceRangeText());
					} else {
						saveMeasurementsExt(measId, "maximum", refRange.getHighLimit());
						saveMeasurementsExt(measId, "minimum", refRange.getLowLimit());
					}
				}

				for (int i=0; i<labResultArr.length; i++) {
					if (!(_location[i].equals(labname) && _accession[i].equals(accnum))) continue;
					saveMeasurementsExt(measId, "lab_no", _lab_no[i]);
					break;
				}
			}

			//APPOINTMENTS
			Appointments[] appArray = patientRec.getAppointmentsArray();
			Date appointmentDate = null;
			String name="", notes="", reason="", status="", startTime="", endTime="", apptProvider="";

			Properties p = (Properties) request.getSession().getAttribute("oscarVariables");

			for (int i=0; i<appArray.length; i++) {
				String apptDateStr = dateFullPartial(appArray[i].getAppointmentDate(), timeShiftInDays);
				if (StringUtils.filled(apptDateStr)) {
					appointmentDate = UtilDateUtilities.StringToDate(apptDateStr);
				} else {
					err_data.add("Error! No Appointment Date ("+(i+1)+")");
				}
				if (appArray[i].getAppointmentTime()!=null) {
					startTime = getCalTime(appArray[i].getAppointmentTime());
					if (appArray[i].getDuration()!=null) {
						Date d_startTime = appArray[i].getAppointmentTime().getTime();
						Date d_endTime = new Date();
						d_endTime.setTime(d_startTime.getTime() + (appArray[i].getDuration().longValue()-1)*60000);
						endTime = UtilDateUtilities.DateToString(d_endTime,"HH:mm:ss");
					} else {
						Date d_startTime = appArray[i].getAppointmentTime().getTime();
						Date d_endTime = new Date();
						d_endTime.setTime(d_startTime.getTime() + 14*60000);
						endTime = UtilDateUtilities.DateToString(d_endTime,"HH:mm:ss");
					}
				} else {
					err_data.add("Error! No Appointment Time ("+(i+1)+")");
				}
				if (StringUtils.filled(appArray[i].getAppointmentNotes())) {
					notes = appArray[i].getAppointmentNotes();
				} else {
					err_data.add("Error! No Appointment Notes ("+(i+1)+")");
				}
				if (appArray[i].getAppointmentStatus()!=null) {
					ApptStatusData asd = new ApptStatusData();
					String[] allStatus = asd.getAllStatus();
					String[] allTitle = asd.getAllTitle();
					status = allStatus[0];
					for (int j=1; j<allStatus.length; j++) {
						String msg = getResources(request).getMessage(allTitle[j]);
						if (appArray[i].getAppointmentStatus().trim().equalsIgnoreCase(msg)) {
							status = allStatus[j];
							break;
						}
					}
				}
				reason = StringUtils.noNull(appArray[i].getAppointmentPurpose());
				if (appArray[i].getProvider()!=null) {
					String[] providerName = getPersonName(appArray[i].getProvider().getName());
					String personOHIP = appArray[i].getProvider().getOHIPPhysicianId();
					apptProvider = writeProviderData(providerName[0], providerName[1], personOHIP);
					if (StringUtils.empty(apptProvider)) {
						apptProvider = defaultProvider;
						err_note.add("Appointment has no provider; assigned to \"doctor oscardoc\" ("+(i+1)+")");
					}
				}
				oscarSuperManager.update("appointmentDao", "import_appt", new Object [] {apptProvider,
				appointmentDate, startTime, endTime, patientName, demographicNo, notes, reason, status});
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
						err_othe.add("Error! No report file in xml ("+(i+1)+")");
					} else {
						String docFileName = "ImportReport"+(i+1)+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss");
						String docType=null, contentType="", observationDate=null, updateDateTime=null, docCreator=admProviderNo;
						String reviewer=null, reviewDateTime=null, source=null;

						if (StringUtils.filled(repR[i].getFileExtensionAndVersion())) {
							contentType = repR[i].getFileExtensionAndVersion();
							docFileName += Util.mimeToExt(contentType);
						} else {
							err_data.add("Error! No File Extension & Version for Report ("+(i+1)+")");
						}
						String docDesc = repR[i].getSubClass();
						if (StringUtils.empty(docDesc)) docDesc = "ImportReport"+(i+1);
						FileOutputStream f = new FileOutputStream(docDir + docFileName);
						f.write(b);
						f.close();

						if (repR[i].getClass1()!=null) {
							if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT)) docType = "radiology";
							else if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT)) docType = "pathology";
							else if (repR[i].getClass1().equals(cdsDt.ReportClass.CONSULTANT_REPORT)) docType = "consult";
							else docType = "others";
						} else {
							err_data.add("Error! No Class for Report ("+(i+1)+")");
						}

						String[] author = getPersonName(repR[i].getAuthorPhysician());
						source = StringUtils.noNull(author[0]) + (StringUtils.filled(author[1]) ? " "+author[1] : "");

						String revOHIP = repR[i].getReviewingOHIPPhysicianId();
						if (StringUtils.filled(revOHIP)) {
							reviewer = writeProviderData("", "", revOHIP);
						}

						observationDate = dateFullPartial(repR[i].getEventDateTime(), timeShiftInDays);
						updateDateTime = dateFullPartial(repR[i].getReceivedDateTime(), timeShiftInDays);
						reviewDateTime = dateFullPartial(repR[i].getReviewedDateTime(), timeShiftInDays);

						EDocUtil.addDocument(demographicNo,docFileName,docDesc,docType,contentType,observationDate,updateDateTime,docCreator,admProviderNo,reviewer,reviewDateTime, source);
						if (StringUtils.filled(repR[i].getSubClass())) {
							err_note.add("Subclass not imported - Report ("+(i+1)+")");
						}
					}
				}
			}

			//AUDIT INFORMATION
			String fileTime = UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss");
			String auditInfoFile = "importAuditInfo-"+fileTime;
			String auditInfoSummary = "";
			String contentType = "text/html";

			/* Read data from xml */
			AuditInformation[] audInf = patientRec.getAuditInformationArray();
			for (int i=0; i<audInf.length; i++) {
				if (audInf.length>1) auditInfoFile += "("+i+")";
				String sAudInfo = audInf[i].getCategorySummaryLine();
				if (StringUtils.empty(sAudInfo)) {
					err_summ.add("No Summary for Audit Information ("+(i+1)+")");
				}
				cdsDt.ResidualInformation audRes = audInf[i].getResidualInfo();
				if (audRes!=null) {
					cdsDt.ResidualInformation.DataElement[] audEArr = audRes.getDataElementArray();
					for (cdsDt.ResidualInformation.DataElement audE : audEArr) {
						sAudInfo = Util.appendLine(sAudInfo, "Name: ", audE.getName());
						sAudInfo = Util.appendLine(sAudInfo, "Data Type: ", audE.getDataType());
						if (audE.getDescription()!=null) sAudInfo = Util.appendLine(sAudInfo, "Description: ", audE.getDescription());
						sAudInfo = Util.appendLine(sAudInfo, "Content: ", audE.getContent());
						sAudInfo = Util.appendLine(sAudInfo, fillUp("",'-',40));
					}
				}
				if (audInf[i].getFormat().equals(cdsDt.AuditFormat.TEXT)) {
					if (audInf[i].getContent()!=null) {
						sAudInfo = Util.appendLine(sAudInfo, audInf[i].getContent().getTextContent());
					}
					if (StringUtils.filled(sAudInfo)) {
						FileWriter f = new FileWriter(docDir + auditInfoFile);
						f.write(sAudInfo);
						f.close();
					}
				} else if (audInf[i].getFormat().equals(cdsDt.AuditFormat.FILE)) {
					if (StringUtils.filled(sAudInfo)) {
						auditInfoSummary = "importAuditSummary-"+fileTime;
					try {
						FileWriter f = new FileWriter(docDir + auditInfoSummary);
						f.write(sAudInfo);
						f.close();
					} catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
					}
					}
					contentType = audInf[i].getFileExtensionAndVersion();
					if (audInf[i].getContent()!=null) {
						auditInfoFile += Util.mimeToExt(contentType);
					try {
						FileOutputStream f = new FileOutputStream(docDir + auditInfoFile);
						f.write(audInf[i].getContent().getMedia());
						f.close();
					} catch (Exception ex) {MiscUtils.getLogger().error("Error", ex);
					}
					}
				}
				/***** Write to document table *****/
				EDocUtil.addDocument(demographicNo,auditInfoFile,"Imported Audit Information","others",contentType,fileTime,fileTime,admProviderNo,admProviderNo);
				if (StringUtils.filled(auditInfoSummary)) {
					EDocUtil.addDocument(demographicNo,auditInfoSummary,"Imported Audit Summary","others",contentType,fileTime,fileTime,admProviderNo,admProviderNo);
				}
			}

			//CARE ELEMENTS
			CareElements[] careElems = patientRec.getCareElementsArray();
			for (CareElements ce : careElems) {
				cdsDt.Height[] heights = ce.getHeightArray();
				for (cdsDt.Height ht : heights) {
					Date dateObserved = ht.getDate().getTime();
					String dataField = StringUtils.noNull(ht.getHeight());
					ImportExportMeasurements.saveMeasurements("HT", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.Weight[] weights = ce.getWeightArray();
				for (cdsDt.Weight wt : weights) {
					Date dateObserved = wt.getDate().getTime();
					String dataField = StringUtils.noNull(wt.getWeight());
					ImportExportMeasurements.saveMeasurements("WT", demographicNo, admProviderNo, dataField, "in kg", dateObserved);
				}
				cdsDt.WaistCircumference[] waists = ce.getWaistCircumferenceArray();
				for (cdsDt.WaistCircumference wc : waists) {
					Date dateObserved = wc.getDate().getTime();
					String dataField = StringUtils.noNull(wc.getWaistCircumference());
					ImportExportMeasurements.saveMeasurements("WC", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.BloodPressure[] bloodp = ce.getBloodPressureArray();
				for (cdsDt.BloodPressure bp : bloodp) {
					Date dateObserved = bp.getDate().getTime();
					String dataField = StringUtils.noNull(bp.getSystolicBP())+"/"+StringUtils.noNull(bp.getDiastolicBP());
					ImportExportMeasurements.saveMeasurements("BP", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.SmokingPacks[] smokp = ce.getSmokingPacksArray();
				for (cdsDt.SmokingPacks sp : smokp) {
					Date dateObserved = sp.getDate().getTime();
					String dataField = sp.getPerDay().toString();
					ImportExportMeasurements.saveMeasurements("POSK", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.SmokingStatus[] smoks = ce.getSmokingStatusArray();
				for (cdsDt.SmokingStatus ss : smoks) {
					Date dateObserved = ss.getDate().getTime();
					String dataField = ss.getStatus().toString();
					ImportExportMeasurements.saveMeasurements("SKST", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.SelfMonitoringBloodGlucose[] smbg = ce.getSelfMonitoringBloodGlucoseArray();
				for (cdsDt.SelfMonitoringBloodGlucose sg : smbg) {
					Date dateObserved = sg.getDate().getTime();
					String dataField = sg.getSelfMonitoring().toString();
					ImportExportMeasurements.saveMeasurements("SMBG", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.DiabetesEducationalSelfManagement[] desm = ce.getDiabetesEducationalSelfManagementArray();
				for (cdsDt.DiabetesEducationalSelfManagement dm : desm) {
					Date dateObserved = dm.getDate().getTime();
					String dataField = dm.getEducationalTrainingPerformed().toString();
					ImportExportMeasurements.saveMeasurements("DMME", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.DiabetesSelfManagementChallenges[] dsmc = ce.getDiabetesSelfManagementChallengesArray();
				for (cdsDt.DiabetesSelfManagementChallenges dc : dsmc) {
					Date dateObserved = dc.getDate().getTime();
					String dataField = dc.getChallengesIdentified().toString();
					ImportExportMeasurements.saveMeasurements("SMCD", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.DiabetesMotivationalCounselling[] dmc = ce.getDiabetesMotivationalCounsellingArray();
				for (cdsDt.DiabetesMotivationalCounselling dc : dmc) {
					Date dateObserved = dc.getDate().getTime();
					String dataField = "Yes";
					cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed cp = null;
					if (dc.getCounsellingPerformed().equals(cp.NUTRITION)) {
						ImportExportMeasurements.saveMeasurements("MCCN", demographicNo, admProviderNo, dataField, dateObserved);
					}
					else if (dc.getCounsellingPerformed().equals(cp.EXERCISE)) {
						ImportExportMeasurements.saveMeasurements("MCCE", demographicNo, admProviderNo, dataField, dateObserved);
					}
					else if (dc.getCounsellingPerformed().equals(cp.SMOKING_CESSATION)) {
						ImportExportMeasurements.saveMeasurements("MCCS", demographicNo, admProviderNo, dataField, dateObserved);
					}
					else if (dc.getCounsellingPerformed().equals(cp.OTHER)) {
						ImportExportMeasurements.saveMeasurements("MCCO", demographicNo, admProviderNo, dataField, dateObserved);
					}
				}
				cdsDt.DiabetesComplicationScreening[] dcs = ce.getDiabetesComplicationsScreeningArray();
				for (cdsDt.DiabetesComplicationScreening ds : dcs) {
					Date dateObserved = ds.getDate().getTime();
					String dataField = "Yes";
					cdsDt.DiabetesComplicationScreening.ExamCode ec = null;
					if (ds.getExamCode().equals(ec.X_32468_1)) {
						ImportExportMeasurements.saveMeasurements("EYEE", demographicNo, admProviderNo, dataField, dateObserved);
					} else if (ds.getExamCode().equals(ec.X_11397_7)) {
						ImportExportMeasurements.saveMeasurements("FTE", demographicNo, admProviderNo, dataField, dateObserved);
					} else if (ds.getExamCode().equals(ec.NEUROLOGICAL_EXAM)) {
						ImportExportMeasurements.saveMeasurements("FTLS", demographicNo, admProviderNo, dataField, dateObserved);
					}
				}
				cdsDt.DiabetesSelfManagementCollaborative[] dsco = ce.getDiabetesSelfManagementCollaborativeArray();
				for (cdsDt.DiabetesSelfManagementCollaborative dc : dsco) {
					Date dateObserved = dc.getDate().getTime();
					String dataField = dc.getDocumentedGoals();
					ImportExportMeasurements.saveMeasurements("CGSD", demographicNo, admProviderNo, dataField, dateObserved);
				}
				cdsDt.HypoglycemicEpisodes[] hype = ce.getHypoglycemicEpisodesArray();
				for (cdsDt.HypoglycemicEpisodes he : hype) {
					Date dateObserved = he.getDate().getTime();
					String dataField = he.getNumOfReportedEpisodes().toString();
					ImportExportMeasurements.saveMeasurements("HYPE", demographicNo, admProviderNo, dataField, dateObserved);
				}
			}
		}
		if(demoRes != null) {
			err_demo.addAll(demoRes.getWarningsCollection());
		}
	//	Util.cleanFile(xmlFile);

		return packMsgs(err_demo, err_data, err_summ, err_othe, err_note, warnings);
	}


	File makeImportLog(Vector demo, String dir) throws IOException {
		String[][] keyword = new String[3][5];
		keyword[0][0] = "PatientID";
		keyword[0][1] = "Discrete Data";
		keyword[0][2] = "Summary Line";
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
		keyword[2][3] = "Successful";
		keyword[2][4] = "";
		File importLog = new File(dir, "ImportEvent-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".log");
		BufferedWriter out = new BufferedWriter(new FileWriter(importLog));
		int[] colWidth = new int[5];
		colWidth[0] = keyword[0][0].length()+2;
		colWidth[1] = keyword[1][1].length()+2;
		colWidth[2] = keyword[0][2].length()+2;
		colWidth[3] = keyword[0][3].length()+2;
		colWidth[4] = 80;
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


	boolean matchFileExt(String filename, String ext) {
		if (StringUtils.empty(filename) || StringUtils.empty(ext)) return false;
		if (filename.length()<ext.length()+2) return false;
		if (filename.charAt(filename.length()-ext.length()-1)!='.') return false;

		if (filename.substring(filename.length()-ext.length()).equals(ext)) return true;
		else return false;
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
		if (StringUtils.empty(countrySubDivCode)) return "";
		String[] csdc = countrySubDivCode.split("-");
		if (csdc.length==2) {
			if (csdc[0].trim().equals("CA")) return csdc[1].trim(); //return w/o "CA-"
			if (csdc[1].trim().equals("US")) return countrySubDivCode.trim(); //return w/ "US-"
		}
		return "OT"; //Other
	}

    String dateFullPartial(cdsDt.DateFullOrPartial dfp, int timeshiftInDays) {
		if (dfp==null) return "";
				
		if (dfp.getDateTime()!=null) {
			dfp.getDateTime().add(Calendar.DAY_OF_YEAR, timeshiftInDays);
			return getCalDateTime(dfp.getDateTime());
		}
		else if (dfp.getFullDate()!=null)  {
			dfp.getFullDate().add(Calendar.DAY_OF_YEAR, timeshiftInDays);
			return getCalDate(dfp.getFullDate());
		}		
		else if (dfp.getYearMonth()!=null) {
			dfp.getYearMonth().add(Calendar.DAY_OF_YEAR, timeshiftInDays);
			return getCalDate(dfp.getYearMonth());
		}
		else if (dfp.getYearOnly()!=null)  
		{
			dfp.getYearOnly().add(Calendar.DAY_OF_YEAR, timeshiftInDays);
			return getCalDate(dfp.getYearOnly());
		}
		else 
			return "";
	   
    }
	    
    Date dDateFullPartial(cdsDt.DateFullOrPartial dfp, int timeShiftInDays) {
		String sdate = dateFullPartial(dfp,timeShiftInDays);
		Date dDate = UtilDateUtilities.StringToDate(sdate, "yyyy-MM-dd HH:mm:ss");
		if (dDate==null) 
			dDate = UtilDateUtilities.StringToDate(sdate, "yyyy-MM-dd");
		if (dDate==null) 
			dDate = UtilDateUtilities.StringToDate(sdate, "HH:mm:ss");
		
		
		return dDate;
    }

	String dateOnly(String d) {
		return UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(d),"yyyy-MM-dd");
	}

	String[] getPersonName(cdsDt.PersonNameSimple person) {
		String[] name = new String[2];
		if (person!=null) {
			name[0] = StringUtils.noNull(person.getFirstName());
			name[1] = StringUtils.noNull(person.getLastName());
		}
		return name;
	}

	String[] getPersonName(cdsDt.PersonNameSimpleWithMiddleName person) {
		String[] name = new String[2];
		if (person!=null) {
			name[0] = StringUtils.noNull(person.getFirstName())+" "+StringUtils.noNull(person.getMiddleName());
			name[1] = StringUtils.noNull(person.getLastName());
		}
		return name;
	}

	String getDefaultProvider() {
		ProviderData pd = getProviderByNames("doctor", "oscardoc", true);
		if (pd!=null) return pd.getProviderNo();

		return writeProviderData("doctor", "oscardoc", "");
	}

	Set<CaseManagementIssue> getCMIssue(String code) {
		CaseManagementIssue cmIssu = new CaseManagementIssue();
		cmIssu.setDemographic_no(demographicNo);
		Issue isu = caseManagementManager.getIssueInfoByCode(StringUtils.noNull(code));
		cmIssu.setIssue_id(isu.getId());
		cmIssu.setType(isu.getType());
		caseManagementManager.saveCaseIssue(cmIssu);

		Set<CaseManagementIssue> sCmIssu = new HashSet<CaseManagementIssue>();
		sCmIssu.add(cmIssu);
		return sCmIssu;
	}

	Set<CaseManagementIssue> getCMIssue(String cppName, cdsDt.Code diagCode) {
		Set<CaseManagementIssue> sCmIssu = new HashSet<CaseManagementIssue>();
		Issue isu = caseManagementManager.getIssueInfoByCode(StringUtils.noNull(cppName));
		if (isu!=null) {
			CaseManagementIssue cmIssu = new CaseManagementIssue();
			cmIssu.setDemographic_no(demographicNo);
			cmIssu.setIssue_id(isu.getId());
			cmIssu.setType(isu.getType());
			caseManagementManager.saveCaseIssue(cmIssu);
			sCmIssu.add(cmIssu);
		}
		if (diagCode==null) return sCmIssu;

		if (StringUtils.noNull(diagCode.getCodingSystem()).equalsIgnoreCase("icd9")) {
			isu = caseManagementManager.getIssueInfoByCode(StringUtils.noNull(diagCode.getValue()));
			if (isu!=null) {
				CaseManagementIssue cmIssu = new CaseManagementIssue();
				cmIssu.setDemographic_no(demographicNo);
				cmIssu.setIssue_id(isu.getId());
				cmIssu.setType(isu.getType());
				caseManagementManager.saveCaseIssue(cmIssu);
				sCmIssu.add(cmIssu);
			}
		}
		return sCmIssu;
	}

	String getCode(cdsDt.Code dCode, String dTitle) {
		if (dCode==null) return "";

		String ret = StringUtils.filled(dTitle) ? dTitle+" -" : "";
		ret = Util.appendLine(ret, "Coding System: ", dCode.getCodingSystem());
		ret = Util.appendLine(ret, "Value: ",         dCode.getValue());
		ret = Util.appendLine(ret, "Description: ",   dCode.getDescription());

		return ret;
	}

	ProviderData getProviderByNames(String firstName, String lastName, boolean matchAll) {
		ProviderData pd = new ProviderData();
		if (matchAll) {
			pd.getProviderWithNames(firstName, lastName);
		} else {
			pd.getExternalProviderWithNames(firstName, lastName);
		}
		if (StringUtils.filled(pd.getProviderNo())) return pd;
		else return null;
	}

	ProviderData getProviderByOhip(String OhipNo) {
		ProviderData pd = new ProviderData();
		pd.getProviderWithOHIP(OhipNo);
		if (StringUtils.filled(pd.getProviderNo())) return pd;
		else return null;
	}

	String getProvinceCode(cdsDt.HealthCardProvinceCode.Enum provinceCode) {
		String pcStr = provinceCode.toString();
		return getCountrySubDivCode(pcStr);
	}

	String getResidual(cdsDt.ResidualInformation resInfo) {
		String ret = "";
		if (resInfo==null) return ret;

		cdsDt.ResidualInformation.DataElement[] resData = resInfo.getDataElementArray();
		if (resData.length>0) ret = "- Residual Information -";
		for (int i=0; i<resData.length; i++) {
			if (StringUtils.filled(resData[i].getName())) {
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

	String mapPreventionType(cdsDt.Code imCode) {
		if (imCode==null) return "";
		if (!imCode.getCodingSystem().equalsIgnoreCase("DIN")) return "";

		ArrayList<String> dinFlu = new ArrayList<String>();
		ArrayList<String> dinHebAB = new ArrayList<String>();
		ArrayList<String> dinCHOLERA = new ArrayList<String>();

		dinFlu.add("01914510");
		dinFlu.add("02015986");
		dinFlu.add("02269562");
		dinFlu.add("02223929");

		dinHebAB.add("02230578");
		dinHebAB.add("02237548");

		dinCHOLERA.add("00074969");
		dinCHOLERA.add("02247208");

		if (dinFlu.contains(StringUtils.noNull(imCode.getValue()))) return "Flu";
		if (dinHebAB.contains(StringUtils.noNull(imCode.getValue()))) return "HebAB";
		if (dinCHOLERA.contains(StringUtils.noNull(imCode.getValue()))) return "CHOLERA";
		return "OtherA";
	}

	String[] packMsgs(ArrayList err_demo, ArrayList err_data, ArrayList err_summ, ArrayList err_othe, ArrayList err_note, ArrayList warnings) {
		if (!(err_demo.isEmpty() && err_data.isEmpty() && err_summ.isEmpty() && err_othe.isEmpty() && err_note.isEmpty())) {
			String title = "Fail to import patient "+patientName;
			if (StringUtils.filled(demographicNo)) {
				title = "Patient "+patientName+" (Demographic no="+demographicNo+")";
			}
			warnings.add(fillUp("---- "+title, '-', 150));
		}
		warnings.addAll(err_demo);
		warnings.addAll(err_data);
		warnings.addAll(err_summ);
		warnings.addAll(err_othe);
		warnings.addAll(err_note);

		String err_all = aListToMsg(err_demo);
		err_all = Util.appendLine(err_all, aListToMsg(err_data));
		err_all = Util.appendLine(err_all, aListToMsg(err_summ));
		err_all = Util.appendLine(err_all, aListToMsg(err_othe));
		err_all = Util.appendLine(err_all, aListToMsg(err_note));

		String[] msgs = {demographicNo, err_data.isEmpty()?"Yes":"No",
		err_summ.isEmpty()?"Yes":"No",
		err_othe.isEmpty()?"Yes":"No", err_all};
		return msgs;
	}

	CaseManagementNote prepareCMNote() {
		CaseManagementNote cmNote = new CaseManagementNote();
		cmNote.setUpdate_date(new Date());
		cmNote.setObservation_date(new Date());
		cmNote.setDemographic_no(demographicNo);
		cmNote.setProviderNo(admProviderNo);
		cmNote.setSigning_provider_no(admProviderNo);
		cmNote.setSigned(true);
		cmNote.setHistory("");
		cmNote.setReporter_caisi_role("1");  //caisi_role for "doctor"
		cmNote.setReporter_program_team("0");
		cmNote.setProgram_no(programId);
		cmNote.setUuid(UUID.randomUUID().toString());
		return cmNote;
	}

	String procFreq(String freqCode) {
		if (StringUtils.empty(freqCode)) return "";
		freqCode = freqCode.toUpperCase();
		freqCode = freqCode.replace(".","");
		freqCode = freqCode.replace(" ","");
		return freqCode;
	}

	void saveLinkNote(Long hostId, CaseManagementNote cmn) {
		saveLinkNote(cmn, CaseManagementNoteLink.CASEMGMTNOTE, hostId);
	}

	void saveLinkNote(CaseManagementNote cmn, Integer tableName, Long tableId) {
		if (StringUtils.filled(cmn.getNote())) {
			caseManagementManager.saveNoteSimple(cmn);    //new note id created

			CaseManagementNoteLink cml = new CaseManagementNoteLink();
			cml.setTableName(tableName);
			cml.setTableId(tableId);
			cml.setNoteId(cmn.getId()); //new note id
			caseManagementManager.saveNoteLink(cml);
		}
	}

	void saveMeasurementsExt(Long measurementId, String key, String val) throws SQLException {
		if (measurementId!=null && StringUtils.filled(key)) {
			MeasurementsExt mx = new MeasurementsExt(measurementId.intValue());
			mx.setKeyVal(key);
			mx.setVal(StringUtils.noNull(val));
			ImportExportMeasurements.saveMeasurementsExt(mx);
		}
	}

	String updateExternalProviderNames(String firstName, String lastName, ProviderData pd) {
		// For external provider only
		if (pd.getProviderNo().charAt(0)=='-') {
			if (StringUtils.empty(pd.getFirst_name()) && StringUtils.empty(pd.getLast_name())) {
				pd.setFirst_name(StringUtils.noNull(firstName));
				pd.setLast_name(StringUtils.noNull(lastName));
			}
		}
		return pd.getProviderNo();
	}

	String updateExternalProviderOhip(String ohipNo, ProviderData pd) {
		// For external provider only
		if (pd.getProviderNo().charAt(0)=='-') {
			if (StringUtils.empty(pd.getOhip_no())) {
				pd.setOhip_no(StringUtils.noNull(ohipNo));
			}
		}
		return pd.getProviderNo();
	}

	String writeProviderData(String firstName, String lastName, String ohipNo) {
		ProviderData pd = getProviderByOhip(ohipNo);
		if (pd!=null) return updateExternalProviderNames(firstName, lastName, pd);

		pd = getProviderByNames(firstName, lastName, matchProviderNames);
		if (pd!=null) return updateExternalProviderOhip(ohipNo, pd);

		//Write as a new provider
		if (StringUtils.empty(firstName) && StringUtils.empty(lastName) && StringUtils.empty(ohipNo)) return ""; //no information at all!
		pd = new ProviderData();
		pd.addExternalProvider(firstName, lastName, ohipNo);
		return pd.getProviderNo();
	}

	String aListToMsg(ArrayList alist) {
		String msgs = "";
		for (int i=0; i<alist.size(); i++) {
			String msg = (String) alist.get(i);
			if (i>0) msgs += "\n";
			msgs += msg;
		}
		return msgs;
	}

	void insertIntoAdmission() {
		Admission admission = new Admission();
		admission.setClientId(Integer.valueOf(demographicNo));
		admission.setProviderNo(admProviderNo);
		admission.setProgramId(Integer.valueOf(programId));
		admission.setAdmissionDate(new Date());
		admission.setAdmissionFromTransfer(false);
		admission.setDischargeFromTransfer(false);
		admission.setAdmissionStatus("current");
		admission.setTeamId(0);
		admission.setTemporaryAdmission(false);
		admission.setRadioDischargeReason("0");
		admission.setClientStatusId(0);
		admission.setAutomaticDischarge(false);
		
		admissionDao.saveAdmission(admission);
	}

	String getLabDline(LaboratoryResults labRes, int timeShiftInDays){
		StringBuilder s = new StringBuilder();
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
		appendIfNotNull(s,"LabRequisitionDateTime",dateFullPartial(labRes.getLabRequisitionDateTime (), timeShiftInDays));
		//<xs:element name="CollectionDateTime" type="cdsd:dateTimeYYYYMMDDHHMM"/>
		appendIfNotNull(s,"CollectionDateTime",dateFullPartial( labRes.getCollectionDateTime(), timeShiftInDays));
		//<xs:element name="DateTimeResultReceivedByCMS" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
		appendIfNotNull(s,"DateTimeResultReceivedByCMS",dateFullPartial(labRes.getDateTimeResultReceivedByCMS(), timeShiftInDays));
		//<xs:element name="DateTimeResultReviewed" type="cdsd:dateTimeYYYYMMDDHHMM" minOccurs="0"/>
		appendIfNotNull(s,"DateTimeResultReviewed",dateFullPartial(labRes.getDateTimeResultReviewed(), timeShiftInDays));

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

	void appendIfNotNull(StringBuilder s, String name, String object){
		if (object != null){
			s.append(name+": "+object+"\n");
		}
	}
}
