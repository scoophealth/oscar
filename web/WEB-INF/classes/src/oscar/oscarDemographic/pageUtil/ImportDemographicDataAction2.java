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

import cds.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.apache.xmlbeans.XmlException;
import org.codehaus.xfire.service.binding.RPCBinding;
import org.jdom.Element;
import oscar.appt.AppointmentDAO;
import oscar.appt.ApptStatusData;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.*;
import oscar.oscarEncounter.data.*;
import oscar.oscarLab.ca.on.LabResultImport;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarRx.data.RxAllergyImport;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionImport;
import oscar.util.UtilDateUtilities;
import oscar.util.UtilMisc;

/**
 *
 * @author Ronnie Cheng
 */
public class ImportDemographicDataAction2 extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
       
	String proNo = (String) request.getSession().getAttribute("user");
	String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
	File importLog = null;
	if (tmpDir==null || tmpDir.trim().equals("")) {
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
		    cleanFile(ifile);
		    throw new Exception ("Error! No XML file in zip");
		} else {
		    if (logResult.size()>0) importLog = makeImportLog(logResult, tmpDir);
		}
		in.close();
		if (!cleanFile(ifile)) throw new Exception ("Error! Cannot delete import file!");

	    } else if (ifile.substring(ifile.length()-3).equals("xml")) {
		String[] logR = importXML(ifile, proNo, warnings, request);
		if (logR!=null) {
		    logResult.add(logR);
		    if (logResult.size()>0) importLog = makeImportLog(logResult, tmpDir);
		}
		
	    } else {
		cleanFile(ifile);
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
    
    String[] importXML(String xmlFile, String proNum, ArrayList errWarnings, HttpServletRequest req) {
	String demoNo="", dataGood="Yes", summaryGood="Yes", otherGood="Yes", errorImport="", errorMsg="";
	DemographicData.DemographicAddResult demoRes = null;
	try {
	    File xmlF = new File(xmlFile);

	    cds.OmdCdsDocument omdCdsDoc = cds.OmdCdsDocument.Factory.parse(xmlF);
	    cds.OmdCdsDocument.OmdCds omdCds = omdCdsDoc.getOmdCds();
	    cds.PatientRecordDocument.PatientRecord patientRec = omdCds.getPatientRecord();
	    
	    //DEMOGRAPHICS
	    cds.DemographicsDocument.Demographics demo = patientRec.getDemographics();
	    
	    cdsDt.PersonNameStandard.LegalName legalName = demo.getNames().getLegalName();
	    
	    String lastName="", firstName="";
	    if (legalName!=null) {
		lastName = legalName.getLastName().getPart();
		firstName = legalName.getFirstName().getPart();
	    } else {
		dataGood = "No";
		errorMsg = "No Legal Name";
		errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
	    }
	    String sex = demo.getGender()!=null ? demo.getGender().toString() : "";
	    if (sex.equals("")) {
		dataGood = "No";
		errorMsg = "No Gender";
		errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
	    }
	    String birthDate = demo.getDateOfBirth()!=null ? getDateFullPartial(demo.getDateOfBirth()) : "";
	    
	    if (birthDate.equals("")) {
		birthDate = "0001-01-01";
		dataGood = "No";
		errorMsg = "No Date Of Birth";
		errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
	    }
	    String roster_status = demo.getEnrollmentStatus()!=null ? demo.getEnrollmentStatus().toString() : "";
	    if (roster_status.equals("1")) roster_status = "RO";
	    else if (roster_status.equals("0")) roster_status = "NR";
	    else {
		dataGood = "No";
		errorMsg = "No Enrollment Status";
		errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
	    }
	    String patient_status = demo.getPersonStatusCode()!=null ? demo.getPersonStatusCode().toString() : "";
	    if (patient_status.equals("A")) patient_status = "AC";
	    else if (patient_status.equals("I")) patient_status = "IN";
	    else if (patient_status.equals("D")) patient_status = "DE";
	    else {
		dataGood = "No";
		errorMsg = "No Person Status Code";
		errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
	    }
	    String date_joined = demo.getEnrollmentDate()!=null ? getDateFullPartial(demo.getEnrollmentDate()) : "";
	    String end_date = demo.getEnrollmentTerminationDate()!= null ? getDateFullPartial(demo.getEnrollmentTerminationDate()) : "";
	    String sin = demo.getSIN()!=null ? demo.getSIN() : "";
	    String chart_no = demo.getChartNumber()!=null ? demo.getChartNumber() : "";
	    boolean UVId_note = false;
	    if (chart_no.equals("")) {
		if (demo.getUniqueVendorIdSequence()!=null) {
		    chart_no = demo.getUniqueVendorIdSequence();
		    errorMsg = "Note: Unique Vendor Id imported as [chart_no]";
		    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		}
	    } else {
		if (demo.getUniqueVendorIdSequence()!=null) UVId_note = true;
	    }
	    
	    String versionCode="", hin="", hc_type="", eff_date="";
	    cdsDt.HealthCard healthCard = demo.getHealthCard();
	    if (healthCard!=null) {
		hin = healthCard.getNumber()!=null ? healthCard.getNumber() : "";
		if (hin.equals("")) {
		    dataGood = "No";
		    errorMsg = "Error! No health card number!";
		    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		}
		hc_type = healthCard.getProvinceCode()!=null ? healthCard.getProvinceCode().toString() : "";
		if (hc_type.equals("")) {
		    dataGood = "No";
		    errorMsg = "Error! No Province Code for health card!";
		    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		}
		versionCode = healthCard.getVersion()!=null ? healthCard.getVersion() : "";
		try {
		    eff_date = healthCard.getExpirydate()!=null ? getCalDate(healthCard.getExpirydate()) : "";
		} catch (Exception e) {
		    errWarnings.add("Error! Invalid health card expiry date!");
		    e.printStackTrace();
		}
	    }
	    String address="", city="", province="", postalCode="";
	    if (demo.getAddressArray().length>0) {
		cdsDt.Address addr = demo.getAddressArray(0);
		if (addr!=null) {
		    if (addr.getFormatted()!=null) address = addr.getFormatted();
		    else {
			cdsDt.AddressStructured addrStr = addr.getStructured();
			if (addrStr!=null) {
			    address = addrStr.getLine1();
			    address += addrStr.getLine2()!=null ? " "+addrStr.getLine2() : "";
			    address += addrStr.getLine3()!=null ? " "+addrStr.getLine3() : "";
			    city = addrStr.getCity();
			    province = addrStr.getCountrySubdivisionCode();
			    cdsDt.PostalZipCode postalZip = addrStr.getPostalZipCode();
			    if (postalZip!=null) postalCode = postalZip.getPostalCode();
			}
		    }	    
		}
	    }
	    cdsDt.PhoneNumber[] pn = demo.getPhoneNumberArray();
	    String workPhone="", workExt="", homePhone="", homeExt="", cellPhone="", ext="", patientPhone="";
	    for (int i=0; i<pn.length; i++) {
		String phone = pn[i].getPhoneNumber();
		if (phone==null) {
		    if (pn[i].getNumber()!=null) {
			if (pn[i].getAreaCode()!=null) phone = "("+pn[i].getAreaCode()+")"+pn[i].getNumber();
			else phone = pn[i].getNumber();
		    }
		}
		if (phone!=null) {
		    if (pn[i].getExtension()!=null) ext = pn[i].getExtension();
		    else if (pn[i].getExchange()!=null) ext = pn[i].getExchange();
		    
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
		    if (!homePhone.equals("")) patientPhone = homePhone+" "+homeExt;
		} else if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.W) {
		    if (!workPhone.equals("")) patientPhone = workPhone+" "+workExt;
		} else if (demo.getPreferredPhone()==cdsDt.PhoneNumberType.C) {
		    if (!cellPhone.equals("")) patientPhone = cellPhone;
		}
	    } else {
		if (!homePhone.equals("")) patientPhone = homePhone+" "+homeExt;
		else if (!workPhone.equals("")) patientPhone = workPhone+" "+workExt;
		else if (!cellPhone.equals("")) patientPhone = cellPhone;
	    }
		    
	    String email = "";
	    if (demo.getEmail()!=null) email = demo.getEmail();
	    ProviderData provD = new ProviderData();
	    cds.DemographicsDocument.Demographics.PrimaryPhysician primp = demo.getPrimaryPhysician();
	    String providerNo = "";
	    if (primp!=null) {
		providerNo = provD.getProviderNoByOhip(primp.getOHIPPhysicianId());
		if (providerNo==null) {   //this is a new provider
		    providerNo = provD.getNewExtProviderNo();
		    provD.addProvider(providerNo, primp.getName().getFirstName(), primp.getName().getLastName(), primp.getOHIPPhysicianId());
		}
	    }
	    Date bDate = UtilDateUtilities.StringToDate(birthDate,"yyyy-MM-dd");
	    String year_of_birth = UtilDateUtilities.DateToString(bDate,"yyyy");
	    String month_of_birth = UtilDateUtilities.DateToString(bDate,"MM");
	    String date_of_birth = UtilDateUtilities.DateToString(bDate,"dd");
	    
	    DemographicData dd = new DemographicData();
	    DemographicExt dExt = new DemographicExt();
	    demoRes = dd.addDemographic(lastName, firstName, address, city, province, postalCode, homePhone, workPhone,
					year_of_birth, month_of_birth, date_of_birth, hin, versionCode, 
					roster_status, patient_status, date_joined, chart_no, providerNo, sex, 
					""/*end_date*/, eff_date, ""/*pcn_indicator*/, hc_type, ""/*hc_renew_date*/,
					""/*family_doctor*/, email, ""/*pin*/, 
					""/*alias*/, ""/*previousAddress*/, ""/*children*/, ""/*sourceOfIncome*/, 
					""/*citizenship*/, sin);
	    demoNo = demoRes.getId();
	    if (demoNo!=null)
	    {
		if (UVId_note) dd.addDemographiccust(demoNo, "Unique Vendor Id Sequence = "+demo.getUniqueVendorIdSequence());
		
		if (!workExt.equals("")) dExt.addKey(providerNo, demoNo, "wPhoneExt", workExt);
		if (!homeExt.equals("")) dExt.addKey(providerNo, demoNo, "hPhoneExt", homeExt);
		if (!cellPhone.equals("")) dExt.addKey(providerNo, demoNo, "demo_cell", cellPhone);

		cds.DemographicsDocument.Demographics.Contact[] contt = demo.getContactArray();
		for (int i=0; i<contt.length; i++) {
		    String cFirstName = contt[i].getName().getFirstName()!=null ? contt[i].getName().getFirstName() : "";
		    String cLastName = contt[i].getName().getLastName()!=null ? contt[i].getName().getLastName() : "";
		    String cEmail = contt[i].getEmailAddress()!=null ? contt[i].getEmailAddress() : "";

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
			demoRes = dd.addDemographic(cLastName, cFirstName, "", "", "", "", homePhone, workPhone, "0001", "01", "01", "", "",
						"", "", "", "", "", "F", "", "", "", "", "", "", cEmail, "", "", "", "", "", "", "");
			cDemoNo = demoRes.getId();
			if (!workExt.equals("")) dExt.addKey("", cDemoNo, "wPhoneExt", workExt);
			if (!homeExt.equals("")) dExt.addKey("", cDemoNo, "hPhoneExt", homeExt);
			if (!cellPhone.equals("")) dExt.addKey("", cDemoNo, "demo_cell", cellPhone);
		    }
		    DemographicRelationship demoRel = new DemographicRelationship();
		    if (!cDemoNo.equals("")) {
			demoRel.addDemographicRelationship(demoNo, cDemoNo, rel, sdm, emc, ""/*notes*/, providerNo);
		    }
		}

		String socialHist="", familyHist="", medicalHist="", ongConcerns="", reminders="", encounter="";
		//PERSONAL HISTORY
		cds.PersonalHistoryDocument.PersonalHistory[] pHistArray = patientRec.getPersonalHistoryArray();
		for (int i=0; i<pHistArray.length; i++) {
		    if (pHistArray[i].getCategorySummaryLine()!=null) {
			socialHist += pHistArray[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Personal History ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (pHistArray[i].getResidualInfo()!=null) {
			cdsDt.ResidualInformation.DataElement[] phRes = pHistArray[i].getResidualInfo().getDataElementArray();
			for (int j=0; j<phRes.length; j++) {
			    if (phRes[j].getName()!=null) {
				socialHist += "\nData Name: " + phRes[j].getName();
				socialHist += phRes[j].getDescription()!=null ? "\nDescription: " + phRes[j].getDescription() : "";
				socialHist += phRes[j].getDataType()!=null ? "\nData Type: " + phRes[j].getDataType() : "";
				socialHist += phRes[j].getContent()!=null ? "\nContent: " + phRes[j].getContent() : "";
			    }
			}
		    }
		    socialHist += socialHist.equals("") ? "" : "\n===\n";
		}
		//FAMILY HISTORY
		cds.FamilyHistoryDocument.FamilyHistory[] fHistArray = patientRec.getFamilyHistoryArray();
		for (int i=0; i<fHistArray.length; i++) {
		    if (fHistArray[i].getCategorySummaryLine()!=null) {
			familyHist += fHistArray[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Family History ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (fHistArray[i].getResidualInfo()!=null) {
			cdsDt.ResidualInformation.DataElement[] fhRes = fHistArray[i].getResidualInfo().getDataElementArray();
			for (int j=0; j<fhRes.length; j++) {
			    if (fhRes[j].getName()!=null) {
				familyHist += "\nData Name: " + fhRes[j].getName();
				familyHist += fhRes[j].getDescription()!=null ? "\nDescription: " + fhRes[j].getDescription() : "";
				familyHist += fhRes[j].getDataType()!=null ? "\nData Type: " + fhRes[j].getDataType() : "";
				familyHist += fhRes[j].getContent()!=null ? "\nContent: " + fhRes[j].getContent() : "";
			    }
			}
		    }
		    familyHist += familyHist.equals("") ? "" : "\n===\n";
		}
		//PAST HEALTH
		cds.PastHealthDocument.PastHealth[] pHealth = patientRec.getPastHealthArray();
		for (int i=0; i< pHealth.length; i++) {
		    if (pHealth[i].getCategorySummaryLine()!=null) {
			medicalHist += pHealth[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Past Health ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (pHealth[i].getResidualInfo()!=null) {
			cdsDt.ResidualInformation.DataElement[] pheRes = pHealth[i].getResidualInfo().getDataElementArray();
			for (int j=0; j<pheRes.length; j++) {
			    if (pheRes[j].getName()!=null) {
				medicalHist += "\nData Name: " + pheRes[j].getName();
				medicalHist += pheRes[j].getDescription()!=null ? "\nDescription: " + pheRes[j].getDescription() : "";
				medicalHist += pheRes[j].getDataType()!=null ? "\nData Type: " + pheRes[j].getDataType() : "";
				medicalHist += pheRes[j].getContent()!=null ? "\nContent: " + pheRes[j].getContent() : "";
			    }
			}
		    }
		    socialHist += socialHist.equals("") ? "" : "\n===\n";
		}
		//PROBLEM LIST
		cds.ProblemListDocument.ProblemList[] probList = patientRec.getProblemListArray();
		for (int i=0; i<probList.length; i++) {
		    if (probList[i].getCategorySummaryLine()!=null) {
			ongConcerns += probList[i].getCategorySummaryLine();
			if (probList[i].getOnsetDate()!=null) {
			    if (ongConcerns.toLowerCase().indexOf("onset date")==-1) {
				ongConcerns += "\nOnset Date: " + getDateFullPartial(probList[i].getOnsetDate());
			    }
			} else {
			    dataGood = "No";
			    errorMsg = "No Onset Date for Problem List ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			}
			if (probList[i].getResidualInfo()!=null) {
			    cdsDt.ResidualInformation.DataElement[] plRes = probList[i].getResidualInfo().getDataElementArray();
			    for (int j=0; j<plRes.length; j++) {
				if (plRes[j].getName()!=null) {
				    ongConcerns += "\nData Name: " + plRes[j].getName();
				    ongConcerns += plRes[j].getDescription()!=null ? "\nDescription: " + plRes[j].getDescription() : "";
				    ongConcerns += plRes[j].getDataType()!=null ? "\nData Type: " + plRes[j].getDataType() : "";
				    ongConcerns += plRes[j].getContent()!=null ? "\nContent: " + plRes[j].getContent() : "";
				}
			    }
			}
			ongConcerns += ongConcerns.equals("") ? "" : "\n===\n";
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Problem List ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		}
		//RISK FACTORS
		cds.RiskFactorsDocument.RiskFactors[] rFactors = patientRec.getRiskFactorsArray();
		for (int i=0; i<rFactors.length; i++) {
		    if (rFactors[i].getCategorySummaryLine()!=null) {
			reminders += rFactors[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Risk Factors ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (rFactors[i].getResidualInfo()!=null) {
			cdsDt.ResidualInformation.DataElement[] rfRes = rFactors[i].getResidualInfo().getDataElementArray();
			for (int j=0; j<rfRes.length; j++) {
			    if (rfRes[j].getName()!=null) {
				reminders += "\nData Name: " + rfRes[j].getName();
				reminders += rfRes[j].getDescription()!=null ? "\nDescription: " + rfRes[j].getDescription() : "";
				reminders += rfRes[j].getDataType()!=null ? "\nData Type: " + rfRes[j].getDataType() : "";
				reminders += rfRes[j].getContent()!=null ? "\nContent: " + rfRes[j].getContent() : "";
			    }
			}
		    }
		    ongConcerns += ongConcerns.equals("") ? "" : "\n===\n";
		}
		//CLINICAL NOTES
		cds.ClinicalNotesDocument.ClinicalNotes[] cNotes = patientRec.getClinicalNotesArray();
		for (int i=0; i<cNotes.length; i++) {
		    if (cNotes[i].getMyClinicalNotesContent()!=null) encounter += cNotes[i].getMyClinicalNotesContent() + "\n===\n";

    /* Non-mandatory fields; not match in case of multiple Notes; disabled import here to avoid confusions.
		    if (cNotes[i].getEnteredDateTime()!=null) timeStamp = cNotes[i].getEnteredDateTime().getDateTime().getTime();
		    if (cNotes[i].getPrincipalAuthor()!=null) {
			cds.ClinicalNotesDocument.ClinicalNotes.PrincipalAuthor pAuthor = cNotes[i].getPrincipalAuthor();
			if (pAuthor.getOHIPPhysicianId()!=null) providerNo = provD.getProviderNoByOhip(pAuthor.getOHIPPhysicianId());
			if (providerNo==null) {   //this is a new provider
			    providerNo = provD.getNewExtProviderNo();
			    provD.addProvider(providerNo, pAuthor.getName().getFirstName(), pAuthor.getName().getLastName(), pAuthor.getOHIPPhysicianId());
			}
		    }
     */
		}
		Echart ec = new Echart();
		ec.setDemographicNo(demoNo);
		ec.setProviderNo(providerNo);
		ec.setSocialHistory(socialHist+familyHist);
		//ec.setFamilyHistory(familyHist);
		ec.setMedicalHistory(medicalHist);
		ec.setOngoingConcerns(ongConcerns);
		ec.setReminders(reminders);
		ec.setEncounter(encounter);
		EChartDAO ecdao = new EChartDAO();
		ecdao.addEchartEntry(ec);


		//ALLERGIES & ADVERSE REACTIONS
		cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions[] aaReactArray = patientRec.getAllergiesAndAdverseReactionsArray();
		for (int i=0; i<aaReactArray.length; i++) {
		    String description="", drugrefId="", reaction="", severity="", entryDate="", typeCode="";
		    String aSummary = "";
		    if (aaReactArray[i].getCategorySummaryLine()!=null) {
			aSummary = aaReactArray[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Allergies & Adverse Reactions ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (aaReactArray[i].getReaction()!=null) {
			reaction = aaReactArray[i].getReaction();
			if (!aSummary.equals("")) {
			    summaryGood = "No";
			    errorMsg = "Allergies Summary not imported ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			}
		    } else if (!aSummary.equals("")) {
			reaction = aSummary;
			errorMsg = "Note: Allergies Summary imported as [reaction] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (aaReactArray[i].getOffendingAgentDescription()!=null) description = aaReactArray[i].getOffendingAgentDescription();
		    if (aaReactArray[i].getCode()!=null) drugrefId = aaReactArray[i].getCode().getValue();
		    if (aaReactArray[i].getRecordedDate()!=null) entryDate = getDateFullPartial(aaReactArray[i].getRecordedDate());
		    if (aaReactArray[i].getStartDate()!=null) {
			String aStartDate = "Start Date: " + getDateFullPartial(aaReactArray[i].getStartDate());
			reaction += reaction.equals("") ? aStartDate : "\n"+aStartDate;
			errorMsg = "Note: Allergies Start Date imported in [reaction] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (aaReactArray[i].getCode()!=null) {
			cdsDt.Code aCode = aaReactArray[i].getCode();
			String aCodeStr = aCode.getCodingSystem()!=null ? "\nCoding System: "+aCode.getCodingSystem() : "";
			aCodeStr += aCode.getDescription()!=null ? "\nDescription: "+aCode.getDescription() : "";
			aCodeStr += aCode.getValue()!=null ? "\nValue: "+aCode.getValue() : "";
			reaction += reaction.equals("") ? aCodeStr : "\n"+aCodeStr;
			errorMsg = "Note: Allergies Code imported in [reaction] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (aaReactArray[i].getNotes()!=null) {
			String aNotes = "Notes: " + aaReactArray[i].getNotes();
			reaction += reaction.equals("") ? aNotes : "\n"+aNotes;
			errorMsg = "Note: Allergies Notes imported in [reaction] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (aaReactArray[i].getOffendingAgentDescription()!=null) 
		    if (aaReactArray[i].getPropertyOfOffendingAgent()!=null) {
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
			    errorMsg = "Note: Allergies Severity [No Reaction] imported as [Mild] ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			}
		    }
		    boolean b = new RxAllergyImport().Save(demoNo, entryDate, description, typeCode, reaction, severity, drugrefId);
		}


		//MEDICATIONS & TREATMENTS
		cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments[] medArray = patientRec.getMedicationsAndTreatmentsArray();
		for (int i=0; i<medArray.length; i++) {
		    String rxDate="", endDate="", BN="", regionalId="", frequencyCode="", duration="1";
		    String quantity="", special="", route="", createDate="", dosage="", unit="";
		    int repeat = 0;

		    String mSummary = "";
		    if (medArray[i].getCategorySummaryLine()!=null) {
			mSummary = medArray[i].getCategorySummaryLine();
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Medications & Treatments ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getPrescriptionInstructions()!=null) {
			special = medArray[i].getPrescriptionInstructions();
			if (!mSummary.equals("")) {
			    summaryGood = "No";
			    errorMsg = "Medications Summary not imported ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			}
		    } else if (!mSummary.equals("")) {
			special = mSummary;
			errorMsg = "Note: Medications Summary imported as [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getPrescriptionWrittenDate()!=null) createDate = getDateFullPartial(medArray[i].getPrescriptionWrittenDate());
		    if (medArray[i].getStartDate()!=null) rxDate = getDateFullPartial(medArray[i].getStartDate());
		    if (medArray[i].getEndDate()!=null) endDate = getDateFullPartial(medArray[i].getEndDate());
		    if (medArray[i].getDrugIdentificationNumber()!=null) regionalId = medArray[i].getDrugIdentificationNumber();
		    if (medArray[i].getDrugName()!=null) {
			BN = medArray[i].getDrugName();
		    } else {
			dataGood = "No";
			errorMsg = "No Drug Name in Medications & Treatments ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getNumberOfRefills()!=null) repeat = Integer.parseInt(medArray[i].getNumberOfRefills());
		    if (medArray[i].getRoute()!=null) route = medArray[i].getRoute();
		    if (medArray[i].getLastRefillDate()!=null) {
			String lastRefill = "Last Refill Date: " + getDateFullPartial(medArray[i].getLastRefillDate());
			special += special.equals("") ? lastRefill : "\n"+lastRefill;
			errorMsg = "Note: Medication Last Refill Date imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getForm()!=null) {
			String mForm = "Form: " + medArray[i].getForm();
			special += special.equals("") ? mForm : "\n"+mForm;
			errorMsg = "Note: Medication Form imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getNotes()!=null) {
			String mNotes = "Notes: " + medArray[i].getNotes();
			special += special.equals("") ? mNotes : "\n"+mNotes;
			errorMsg = "Note: Medication Notes imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getPatientCompliance()!=null) {
			String mPCYN = "Patient Compliance: ";
			if (medArray[i].getPatientCompliance().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y ||
			    medArray[i].getPatientCompliance().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2)
			    mPCYN += "Yes";
			else if (medArray[i].getPatientCompliance().getBoolean()) mPCYN += "Yes";
			else mPCYN += "No";
			special += special.equals("") ? mPCYN : "\n"+mPCYN;
			errorMsg = "Note: Medication Patient Compliance imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getLongTermMedication()!=null) {
			String mltm = "Long Term Medication: ";
			if (medArray[i].getLongTermMedication().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y ||
			    medArray[i].getLongTermMedication().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2)
			    mltm += "Yes";
			else if (medArray[i].getLongTermMedication().getBoolean()) mltm += "Yes";
			else mltm += "No";
			special += special.equals("") ? mltm : "\n"+mltm;
			errorMsg = "Note: Long Term Medication indicator imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getPastMedications()!=null) {
			String mpm = "Past Medication: ";
			if (medArray[i].getPastMedications().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y ||
			    medArray[i].getPastMedications().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2)
			    mpm += "Yes";
			else if (medArray[i].getPastMedications().getBoolean()) mpm += "Yes";
			else mpm += "No";
			special += special.equals("") ? mpm : "\n"+mpm;
			errorMsg = "Note: Past Medication indicator imported in [special] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (medArray[i].getStrength()!=null) {
			dosage = medArray[i].getStrength().getAmount()!=null ? medArray[i].getStrength().getAmount() : "";
			unit = medArray[i].getStrength().getUnitOfMeasure()!=null ? medArray[i].getStrength().getUnitOfMeasure() : "";
		    }
		    if (medArray[i].getFrequency()!=null) frequencyCode = medArray[i].getFrequency();
		    if (medArray[i].getDuration()!=null) duration = medArray[i].getDuration();
		    if (duration.trim().equals("1 year")) duration = "365";
		    if (medArray[i].getQuantity()!=null) quantity = medArray[i].getQuantity();
		    if (medArray[i].getPrescribedBy()!=null) {
			cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments.PrescribedBy presb = medArray[i].getPrescribedBy();
			if (presb.getOHIPPhysicianId()!=null) providerNo = provD.getProviderNoByOhip(presb.getOHIPPhysicianId());
			if (providerNo==null) {   //this is a new provider
			    providerNo = provD.getNewExtProviderNo();
			    provD.addProvider(providerNo, presb.getName().getFirstName(), presb.getName().getLastName(), presb.getOHIPPhysicianId());
			}
		    }
		    RxPrescriptionImport rpi = new RxPrescriptionImport(providerNo,demoNo,rxDate,endDate,BN,
			regionalId,frequencyCode,duration,quantity,repeat,special,route,createDate,dosage,unit);
		    rpi.Save();
		}


		//IMMUNIZATIONS
		cds.ImmunizationsDocument.Immunizations[] immuArray = patientRec.getImmunizationsArray();
		for (int i=0; i<immuArray.length; i++) {
		    String preventionDate="", providerName="", preventionType="", refused="0";
		    ArrayList preventionExt = new ArrayList();
		    Hashtable ht = new Hashtable();
		    
		    String otherName = "";   //JAY ADDED
		    if (immuArray[i].getImmunizationName()!=null) { //JRG
			//preventionType = immuArray[i].getImmunizationName();
			Properties iProperties = getImmunizationMap();
			String incomingIn = immuArray[i].getImmunizationName();  //JRG
			preventionType = iProperties.getProperty(incomingIn); //JRG
			if (preventionType == null){ //If it's null set it to other A and add the name to comments  //JRG
			    preventionType = "OtherA";   //JRG
			}  //JRG
		    } else {
			dataGood = "No";
			errorMsg = "No Immunization Name ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (immuArray[i].getDate()!=null) preventionDate = getDateFullPartial(immuArray[i].getDate());
		    if (immuArray[i].getRefusedFlag().getYnIndicatorsimple()!=null) {
			if (immuArray[i].getRefusedFlag().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y ||
			    immuArray[i].getRefusedFlag().getYnIndicatorsimple()==cdsDt.YnIndicatorsimple.Y_2) refused = "1";
		    } else if (immuArray[i].getRefusedFlag().getBoolean()) refused = "1";

		    if (immuArray[i].getManufacturer()!=null) {
			ht.put("manufacture", immuArray[i].getManufacturer());
			preventionExt.add(ht);
			ht.clear();
		    }
		    if (immuArray[i].getLotNumber()!=null) {
			ht.put("lot", immuArray[i].getLotNumber());
			preventionExt.add(ht);
			ht.clear();
		    }
		    if (immuArray[i].getRoute()!=null) {
			ht.put("route", immuArray[i].getRoute());
			preventionExt.add(ht);
			ht.clear();
		    }
		    if (immuArray[i].getSite()!=null) {
			ht.put("location", immuArray[i].getSite());
			preventionExt.add(ht);
			ht.clear();
		    }
		    
		    String comments="", iSummary="";
		    if (immuArray[i].getCategorySummaryLine()!=null) {
			iSummary = immuArray[i].getCategorySummaryLine();
			if (!otherName.equals("")){   //JRG
			    comments += "Imm: "+otherName +" ";  //JRG
			}   //JRG
			comments += immuArray[i].getCategorySummaryLine(); //JRG
		    } else {
			summaryGood = "No";
			errorMsg = "No Summary for Immunizations ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (immuArray[i].getNotes()!=null) {
			comments += immuArray[i].getNotes();
			/*
			if (!iSummary.equals("")) {
			    summaryGood = "No";
			    errorMsg = "Immunization Summary not imported ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			}
			 */
		    } else if (!iSummary.equals("")) {
			comments = iSummary;
			errorMsg = "Note: Immunization Summary imported as [comments] ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (!comments.equals("")) {
			ht.put("comments", comments);
			preventionExt.add(ht);
			ht.clear();
		    }
		    PreventionData prevD = new PreventionData();
		    prevD.insertPreventionData(providerNo, demoNo, preventionDate, providerNo, providerName, preventionType, refused, "", "", preventionExt);
		}


		//LABORATORY RESULTS
		cds.LaboratoryResultsDocument.LaboratoryResults[] labResultArray = patientRec.getLaboratoryResultsArray();
		String pre_accession_num="", ppId="";
		for (int i=0; i<labResultArray.length; i++) {
		    String testName="", abn="", minimum="", maximum="", result="", unit="", description="", location="";
		    String accession_num="", coll_date="";
		    if (labResultArray[i].getTestName()!=null) testName = labResultArray[i].getTestName();
		    if (labResultArray[i].getResultNormalAbnormalFlag()!=null) {
			cdsDt.ResultNormalAbnormalFlag.Enum flag = labResultArray[i].getResultNormalAbnormalFlag();
			if (flag.equals(cdsDt.ResultNormalAbnormalFlag.Y)) abn = "Y";
			if (flag.equals(cdsDt.ResultNormalAbnormalFlag.N)) abn = "N";
		    } else {
			dataGood = "No";
			errorMsg = "No Normal/Abnormal Flag for Laboratory Results ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (labResultArray[i].getReferenceRange()!=null) {
			cds.LaboratoryResultsDocument.LaboratoryResults.ReferenceRange ref = labResultArray[i].getReferenceRange();
			if (ref.getHighLimit()!=null) maximum = ref.getHighLimit();
			if (ref.getLowLimit()!=null) minimum = ref.getLowLimit();
		    }
		    if (labResultArray[i].getResult()!=null) {
			if (labResultArray[i].getResult().getValue()!=null) result = labResultArray[i].getResult().getValue();
			if (labResultArray[i].getResult().getUnitOfMeasure()!=null) unit = labResultArray[i].getResult().getUnitOfMeasure();
		    }   
		    if (labResultArray[i].getNotesFromLab()!=null) description = labResultArray[i].getNotesFromLab();
		    if (labResultArray[i].getLaboratoryName()!=null) location = labResultArray[i].getLaboratoryName();
		    if (labResultArray[i].getAccessionNumber()!=null) accession_num = labResultArray[i].getAccessionNumber();
		    
		    try {
			coll_date = getDateFullPartial(labResultArray[i].getCollectionDateTime());
		    } catch (Exception e) {
			dataGood = "No";
			errorMsg = "Error! No Collection DateTime for Laboratory Results";
			errWarnings.add(errorMsg);
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			e.printStackTrace();
		    }
		    coll_date = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(coll_date,"yyyy-MM-dd"));
		    
		    LabResultImport lri = new LabResultImport();
		    if (!accession_num.equals(pre_accession_num)) {
			String print_date = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(coll_date),"yyyyMMdd");
			String RIId = lri.saveLabRI(location, print_date, "00:00:00");
			ppId = lri.saveLabPPInfo(RIId, accession_num, firstName, lastName, sex, hin, birthDate, patientPhone, coll_date);
			pre_accession_num = accession_num;
		    }
		    lri.SaveLabTR(testName, abn, minimum, maximum, result, unit, description, location, ppId);
		    lri.savePatientLR(demoNo, ppId);
		}

		//APPOINTMENTS
		cds.AppointmentsDocument.Appointments[] appArray = patientRec.getAppointmentsArray();
		Date appointmentDate = null;
		String name="", notes="", reason="", status="", startTime="", endTime="";
		providerNo="";

		HttpSession session = req.getSession();
		Properties p = (Properties) session.getAttribute("oscarVariables");
		AppointmentDAO apD = new AppointmentDAO(p);

		for (int i=0; i<appArray.length; i++) {
		    name = lastName + "," + firstName;
		    if (appArray[i].getAppointmentDate()!=null) {
			if (appArray[i].getAppointmentDate().getFullDate()!=null) {
			    appointmentDate = appArray[i].getAppointmentDate().getFullDate().getTime();
			} else if (appArray[i].getAppointmentDate().getDateTime()!=null) {
			    appointmentDate = appArray[i].getAppointmentDate().getDateTime().getTime();
			} else if (appArray[i].getAppointmentDate().getYearMonth()!=null) {
			    appointmentDate = appArray[i].getAppointmentDate().getYearMonth().getTime();
			} else if (appArray[i].getAppointmentDate().getYearOnly()!=null) {
			    appointmentDate = appArray[i].getAppointmentDate().getYearOnly().getTime();
			}
		    } else {
			dataGood = "No";
			errorMsg = "No Appointment Date ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
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
			errorMsg = "No Appointment Time ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (appArray[i].getAppointmentNotes()!=null) {
			notes = appArray[i].getAppointmentNotes();
		    } else {
			dataGood = "No";
			errorMsg = "No Appointment Notes ("+(i+1)+")";
			errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
		    }
		    if (appArray[i].getAppointmentPurpose()!=null) reason = appArray[i].getAppointmentPurpose();
		    if (appArray[i].getAppointmentStatus()!=null) {
			ApptStatusData asd = new ApptStatusData();
			String[] allStatus = asd.getAllStatus();
			String[] allTitle = asd.getAllTitle();
			status = allStatus[0];
			for (int j=1; j<allStatus.length; j++) {
			    String msg = getResources(req).getMessage(allTitle[i]);
			    if (appArray[i].getAppointmentStatus().trim().equalsIgnoreCase(msg)) {
				status = allStatus[i];
				break;
			    }
			}
		    }
		    if (appArray[i].getProvider()!=null) {
			cds.AppointmentsDocument.Appointments.Provider prov = appArray[i].getProvider();
			providerNo = provD.getProviderNoByOhip(prov.getOHIPPhysicianId());
			if (providerNo==null) {   //this is a new provider
			    providerNo = provD.getNewExtProviderNo();
			    provD.addProvider(providerNo, prov.getName().getFirstName(), prov.getName().getLastName(), prov.getOHIPPhysicianId());
			}
		    }
		    apD.addAppointment(providerNo, appointmentDate, startTime, endTime, name, demoNo, notes, reason, status);
		}
		
		//REPORTS RECEIVED
		cds.ReportsReceivedDocument.ReportsReceived[] repR = patientRec.getReportsReceivedArray();
		for (int i=0; i<repR.length; i++) {
		    cdsDt.ReportContent repCt = repR[i].getContent();
		    if (repCt!=null) {
			byte[] b = null;
			if (repCt.getMedia()!=null) b = repCt.getMedia();
			else if (repCt.getTextContent()!=null) b = repCt.getTextContent().getBytes();
			if (b==null) {
			    otherGood = "No";
			    errorMsg = "No report file in xml ("+(i+1)+")";
			    errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			} else {
			    String docFileName = "ImportReport-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+"-"+i;
			    String docDesc = docFileName;
			    String docType=null, contentType=null, observationDate=null, updateDateTime=null, docCreator=null;
			    
			    String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
			    FileOutputStream f = new FileOutputStream(docDir+docFileName);
			    f.write(b);
			    f.close();

			    if (repR[i].getFileExtensionAndVersion()!=null) {
				contentType = repR[i].getFileExtensionAndVersion();
			    } else {
				dataGood = "No";
				errorMsg = "No File Extension & Version for Reports ("+(i+1)+")";
				errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			    }
			    if (repR[i].getEventDateTime()!=null) observationDate = getDateFullPartial(repR[i].getEventDateTime());
			    if (repR[i].getReceivedDateTime()!=null) updateDateTime = getDateFullPartial(repR[i].getReceivedDateTime());
			    if (repR[i].getClass1()!=null) {
				if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT)) docType = "radiology";
				else if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT)) docType = "pathology";
				else if (repR[i].getClass1().equals(cdsDt.ReportClass.CONSULTANT_REPORT)) docType = "consult";
				else docType = "others";
			    } else {
				dataGood = "No";
				errorMsg = "No Class for Reports ("+(i+1)+")";
				errorImport += errorImport.equals("") ? errorMsg : "\n"+errorMsg;
			    }
			    if (repR[i].getAuthorPhysician()!=null) {
				String authorFirst = repR[i].getAuthorPhysician().getFirstName();
				String authorLast  = repR[i].getAuthorPhysician().getLastName();
				provD = new ProviderData();
				ArrayList pList = provD.getProviderList();
				boolean providerFound = false;
				for (int j=0; j<pList.size(); j++) {
				    Hashtable pHash = (Hashtable) pList.get(j);
				    if (authorFirst.equalsIgnoreCase((String)pHash.get("firstName")) &&
					authorLast.equalsIgnoreCase((String)pHash.get("lastName"))) {
					docCreator = (String)pHash.get("providerNo");
					providerFound = true;
				    }
				}
				if (!providerFound) {
				    docCreator = provD.getNewExtProviderNo();
				    provD.addProvider(docCreator, authorFirst, authorLast, "");
				}
			    }
			    EDocUtil.addDocument(demoNo,docFileName,docDesc,docType,contentType,observationDate,updateDateTime,docCreator);
			}
		    }
		}
	    }
	    errWarnings.addAll(demoRes.getWarningsCollection());
	    if (!cleanFile(xmlFile)) throw new Exception ("Error! Cannot delete XML file!");
	    
	} catch (Exception e) {
	    errWarnings.addAll(demoRes.getWarningsCollection());
	    e.printStackTrace();
	}
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

    boolean cleanFile(String filename) {
	File f = new File(filename);
	return f.delete();
    }
    
    String fillUp(String filled, char c, int size) {
	if (size>=filled.length()) {
	    int fill = size-filled.length();
	    for (int i=0; i<fill; i++) filled += c;
	}
	return filled;
    }
    
    String getCalDate(Calendar c) {
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	return f.format(c.getTime());
    }
    String getCalTime(Calendar c) {
	SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
	return f.format(c.getTime());
    }
    String getCalDateTime(Calendar c) {
	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return f.format(c.getTime());
    }
    String getDateFullPartial(cdsDt.DateFullOrPartial dfp) {
	if (dfp.getDateTime()!=null) return getCalDateTime(dfp.getDateTime());
	else if (dfp.getFullDate()!=null) return getCalDate(dfp.getFullDate());
	else if (dfp.getYearMonth()!=null) return getCalDate(dfp.getYearMonth());
	else if (dfp.getYearOnly()!=null) return getCalDate(dfp.getYearOnly());
	else return "";
    }

    String trim(String s){
        if (s != null){
            return s.trim();
        }
        return s;
    }
    
    Properties immProperties = null;
    Properties getImmunizationMap() throws FileNotFoundException, IOException{
        if ( immProperties == null ) { 
           immProperties = new Properties();   //JRG need to actually get some values for this 
           FileInputStream filein = new FileInputStream("/root/immMap.properties");
           immProperties.load(filein);
        }
        return immProperties;                
    }
    
    String getInternalString(Element e){
       String ret = "";
       if (e !=null){
          ret = e.getTextTrim();
       }
       return ret;
    }
    
    public ImportDemographicDataAction2() {
   }
   
}
