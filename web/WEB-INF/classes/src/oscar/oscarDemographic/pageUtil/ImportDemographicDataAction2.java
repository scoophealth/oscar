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
			String[] logR = importXML(ofile, tmpDir, proNo, warnings, request);
			if (logR!=null) logResult.add(logR);
		    }
		    entry = in.getNextEntry();
		}
		if (noXML) {
		    cleanFile(ifile);
		    throw new Exception ("Error! No XML file in zip");
		} else {
		    importLog = makeImportLog(logResult, tmpDir);
		}
		in.close();
		if (!cleanFile(ifile)) throw new Exception ("Error! Cannot delete import file!");

	    } else if (ifile.substring(ifile.length()-3).equals("xml")) {
		String[] logR = importXML(ifile, tmpDir, proNo, warnings, request);
		if (logR!=null) {
		    logResult.add(logR);
		    importLog = makeImportLog(logResult, tmpDir);
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
        
        System.out.println("warnings size "+warnings.size());
        for( int i = 0; i < warnings.size(); i++ ){
           String str = (String) warnings.get(i);
           System.out.println(str);
        }
        return mapping.findForward("success");
    }
    
    String[] importXML(String xmlFile, String xmlDir, String proNum, ArrayList errWarnings, HttpServletRequest req) {
	String demoNo="", dataGood="Yes", summaryGood="Yes", otherGood="Yes", errorImport="";
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
		if (errorImport.equals("")) errorImport = "No Legal Name";
		else errorImport += "\nNo Legal Name";
	    }
	    String sex = demo.getGender()!=null ? demo.getGender().toString() : "";
	    if (sex.equals("")) {
		dataGood = "No";
		if (errorImport.equals("")) errorImport = "No Gender";
		else errorImport += "\nNo Gender";
	    }
	    String birthDate = demo.getDateOfBirth()!=null ? getCalDate(demo.getDateOfBirth()) : "";

	    if (birthDate.equals("")) {
		dataGood = "No";
		if (errorImport.equals("")) errorImport = "No Date of Birth";
		else errorImport += "\nNo Date of Birth";
	    }
	    String roster_status = demo.getEnrollmentStatus()!=null ? demo.getEnrollmentStatus().toString() : "";
	    if (roster_status.equals("")) {
		dataGood = "No";
		if (errorImport.equals("")) errorImport = "No Enrollment Status";
		else errorImport += "\nNo Enrollment Status";
	    }
	    String patient_status = demo.getPersonStatusCode()!=null ? demo.getPersonStatusCode().toString() : "";
	    if (patient_status.equals("")) {
		dataGood = "No";
		if (errorImport.equals("")) errorImport = "No Person Status Code";
		else errorImport += "\nNo Person Status Code";
	    }
	    String date_joined = demo.getEnrollmentDate()!=null ? getCalDate(demo.getEnrollmentDate()) : "";
	    String end_date = demo.getEnrollmentTerminationDate()!= null ? getCalDate(demo.getEnrollmentTerminationDate()) : "";
	    String chart_no = demo.getChartNumber()!=null ? demo.getChartNumber() : "";
	    
	    String versionCode="", hin="";
	    cdsDt.HealthCard healthCard = demo.getHealthCard();
	    if (healthCard!=null) {
		versionCode = healthCard.getVersion();
		hin = healthCard.getNumber();	    
	    }
	    
	    String address="", city="", province="", postalCode="";
	    if (demo.getAddressArray().length>0) {
		cdsDt.Address addr = demo.getAddressArray(0);
		if (addr!=null) {
		    if (addr.getFormatted()!=null) address = addr.getFormatted();
		    else {
			cdsDt.AddressStructured addrStr = addr.getStructured();
			if (addrStr!=null) {
			    address = addrStr.getLine1() + " " + addrStr.getLine2() + " " + addrStr.getLine3();
			    city = addrStr.getCity();
			    province = addrStr.getCountrySubdivisionCode();
			    cdsDt.PostalZipCode postalZip = addrStr.getPostalZipCode();
			    if (postalZip!=null) postalCode = postalZip.getPostalCode();
			}
		    }	    
		}
	    }
	    cdsDt.PhoneNumber[] pn = demo.getPhoneNumberArray();
	    String workPhone="", workExt="", homePhone="", homeExt="";
	    for (int i=0; i<pn.length; i++) {
		String phone = pn[i].getPhoneNumber();
		if (phone==null) {
		    if (pn[i].getNumber()!=null) {
			if (pn[i].getAreaCode()!=null) phone = "("+pn[i].getAreaCode()+")"+pn[i].getNumber();
			else phone = pn[i].getNumber();
		    }
		    String ext = "";
		    if (pn[i].getExtension()!=null) ext = pn[i].getExtension();
		    else if (pn[i].getExchange()!=null) ext = pn[i].getExchange();
		    
		    if (phone!=null) {
			if (pn[i].getPhoneNumberType()==cdsDt.PhoneNumberType.W) {
			    workPhone = phone;
			    workExt   = ext;		    
			} else {
			    homePhone = phone;
			    homeExt   = ext;
			}
		    }
		}
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

	    DemographicData.DemographicAddResult demoRes = null;
	    DemographicData dd = new DemographicData();
	    DemographicExt dExt = new DemographicExt();

	    demoRes = dd.addDemographic(lastName, firstName, address, city, province, postalCode, homePhone, workPhone,
					year_of_birth, month_of_birth, date_of_birth, hin, versionCode, 
					roster_status, patient_status, date_joined, chart_no, providerNo, sex, 
					""/*end_date*/, ""/*eff_date*/, ""/*pcn_indicator*/, ""/*hc_type*/, ""/*hc_renew_date*/,
					""/*family_doctor*/, email, ""/*pin*/, 
					""/*alias*/, ""/*previousAddress*/, ""/*children*/, ""/*sourceOfIncome*/, 
					""/*citizenship*/, ""/*sin*/);
	    
	    demoNo = new DemographicData().getLastDemographicNo();
	    
	    if (!"".equals(homeExt)){
	       dExt.addKey(proNum,demoRes.getId(),"hPhoneExt",homeExt,"");
	    }
	    if (!"".equals(workExt)){
	       dExt.addKey(proNum,demoRes.getId(),"wPhoneExt",workExt,"");
	    }
	    
	    String socialHist="", familyHist="", medicalHist="", ongConcerns="", reminders="", encounter="";
	    Date timeStamp = null;
	    
	    //PERSONAL HISTORY
	    cds.PersonalHistoryDocument.PersonalHistory[] pHistArray = patientRec.getPersonalHistoryArray();
	    for (int i=0; i<pHistArray.length; i++) {
		if (pHistArray[i].getCategorySummaryLine()!=null) {
		    socialHist += pHistArray[i].getCategorySummaryLine() + "\n";
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Personal History";
		    else errorImport += "\nNo Summary for Personal History";
		}
	    }
	    //FAMILY HISTORY
	    cds.FamilyHistoryDocument.FamilyHistory[] fHistArray = patientRec.getFamilyHistoryArray();
	    for (int i=0; i<fHistArray.length; i++) {
		if (fHistArray[i].getCategorySummaryLine()!=null) {
		    familyHist += fHistArray[i].getCategorySummaryLine() + "\n";
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Family History";
		    else errorImport += "\nNo Summary for Family History";
		}
	    }
	    //PAST HEALTH
	    cds.PastHealthDocument.PastHealth[] pHealth = patientRec.getPastHealthArray();
	    for (int i=0; i< pHealth.length; i++) {
		if (pHealth[i].getCategorySummaryLine()!=null) {
		    medicalHist += pHealth[i].getCategorySummaryLine();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Past Health";
		    else errorImport += "\nNo Summary for Past Health";
		}
	    }
	    //PROBLEM LIST
	    cds.ProblemListDocument.ProblemList[] probList = patientRec.getProblemListArray();
	    for (int i=0; i<probList.length; i++) {
		if (probList[i].getCategorySummaryLine()!=null) {
		    ongConcerns += probList[i].getCategorySummaryLine();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Problem List";
		    else errorImport += "\nNo Summary for Problem List";
		}
	    }
	    //RISK FACTORS
	    cds.RiskFactorsDocument.RiskFactors[] rFactors = patientRec.getRiskFactorsArray();
	    for (int i=0; i<rFactors.length; i++) {
		if (rFactors[i].getCategorySummaryLine()!=null) {
		    reminders += rFactors[i].getCategorySummaryLine();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Risk Factors";
		    else errorImport += "\nNo Summary for Risk Factors";
		}
	    }
	    //CLINICAL NOTES
	    cds.ClinicalNotesDocument.ClinicalNotes[] cNotes = patientRec.getClinicalNotesArray();
	    for (int i=0; i<cNotes.length; i++) {
		if (cNotes[i].getMyClinicalNotesContent()!=null) encounter += cNotes[i].getMyClinicalNotesContent();		
		if (cNotes[i].getEnteredDateTime()!=null) timeStamp = cNotes[i].getEnteredDateTime().getTime();
		if (cNotes[i].getPrincipalAuthor()!=null) {
		    cds.ClinicalNotesDocument.ClinicalNotes.PrincipalAuthor pAuthor = cNotes[i].getPrincipalAuthor();
		    if (pAuthor.getOHIPPhysicianId()!=null) providerNo = provD.getProviderNoByOhip(pAuthor.getOHIPPhysicianId());
		    if (providerNo==null) {   //this is a new provider
			providerNo = provD.getNewExtProviderNo();
			provD.addProvider(providerNo, pAuthor.getName().getFirstName(), pAuthor.getName().getLastName(), pAuthor.getOHIPPhysicianId());
		    }
		}
	    }
	    Echart ec = new Echart();
	    ec.setTimeStamp(timeStamp);
	    ec.setDemographicNo(demoNo);
	    ec.setProviderNo(providerNo);
	    ec.setSocialHistory(socialHist);
	    ec.setFamilyHistory(familyHist);
	    ec.setMedicalHistory(medicalHist);
	    ec.setOngoingConcerns(ongConcerns);
	    ec.setReminders(reminders);
	    ec.setEncounter(encounter);
	    EChartDAO ecdao = new EChartDAO();
	    ecdao.addEchartEntry(ec);

	    
	    //ALLERGIES & ADVERSE REACTIONS
	    cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions[] aaReactArray = patientRec.getAllergiesAndAdverseReactionsArray();
	    for (int i=0; i<aaReactArray.length; i++) {
		String description="", drugrefId="", reaction="", entryDate="", typeCode="";
		if (aaReactArray[i].getCategorySummaryLine()!=null) {
		    description = aaReactArray[i].getCategorySummaryLine();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Allergies & Adverse Reactions";
		    else errorImport += "\nNo Summary for Allergies & Adverse Reactions";
		}
		if (aaReactArray[i].getCode()!=null) drugrefId = aaReactArray[i].getCode().getValue();
		if (aaReactArray[i].getReaction()!=null) reaction = aaReactArray[i].getReaction();
		if (aaReactArray[i].getRecordedDate()!=null) entryDate = getCalDate(aaReactArray[i].getRecordedDate().getFullDate());
		
		boolean b = new RxAllergyImport().Save(demoNo, entryDate, description, typeCode, reaction, drugrefId);
	    }
	    
	    
	    //MEDICATIONS & TREATMENTS
	    cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments[] medArray = patientRec.getMedicationsAndTreatmentsArray();
	    for (int i=0; i<medArray.length; i++) {
		String rxDate="", endDate="", BN="", GCN_SEQNO="", frequencyCode="", duration="";
		String quantity="", repeat="", special="", route="", createDate="", dosage="";
		
		if (medArray[i].getCategorySummaryLine()!=null) {
		    special = medArray[i].getCategorySummaryLine();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Medications & Treatments";
		    else errorImport += "\nNo Summary for Medications & Treatments";
		}
		if (medArray[i].getPrescriptionWrittenDate()!=null) createDate = getCalDate(medArray[i].getPrescriptionWrittenDate().getFullDate());
		if (medArray[i].getStartDate()!=null) rxDate = getCalDate(medArray[i].getStartDate());
		if (medArray[i].getEndDate()!=null) endDate = getCalDate(medArray[i].getEndDate());
		if (medArray[i].getDrugIdentificationNumber()!=null) GCN_SEQNO = medArray[i].getDrugIdentificationNumber();
		if (medArray[i].getDrugName()!=null) {
		    BN = medArray[i].getDrugName();
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Drug Name in Medications & Treatments";
		    else errorImport += "\nNo Drug Name in Medications & Treatments";
		}
		if (medArray[i].getNumberOfRefills()!=null) repeat = medArray[i].getNumberOfRefills().toString();
		if (medArray[i].getDosage()!=null) dosage = medArray[i].getDosage();
		if (medArray[i].getFrequency()!=null) frequencyCode = medArray[i].getFrequency();
		if (medArray[i].getDuration()!=null) duration = medArray[i].getDuration();
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
			GCN_SEQNO,frequencyCode,duration,quantity,repeat,special,route,createDate,dosage);
		boolean b = rpi.Save();
	    }
	    
	    
	    //IMMUNIZATIONS
	    cds.ImmunizationsDocument.Immunizations[] immuArray = patientRec.getImmunizationsArray();
	    for (int i=0; i<immuArray.length; i++) {
		String preventionDate="", providerName="", preventionType="", refused="0";
		ArrayList preventionExt = new ArrayList();
		Hashtable ht = new Hashtable();
		
		if (immuArray[i].getImmunizationName()!=null) {
		    preventionType = immuArray[i].getImmunizationName();
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Immunization Name";
		    else errorImport += "\nNo Immunization Name";
		}
		if (immuArray[i].getDate()!=null) preventionDate = getCalDate(immuArray[i].getDate());
		if (immuArray[i].getRefusedFlag()) refused = "1";
		
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
		if (immuArray[i].getCategorySummaryLine()!=null) {
		    String comments = immuArray[i].getCategorySummaryLine();
		    if (immuArray[i].getNotes()!=null) comments += immuArray[i].getNotes();
		    ht.put("comments", comments);
		    preventionExt.add(ht);
		    ht.clear();
		} else {
		    summaryGood = "No";
		    if (errorImport.equals("")) errorImport = "No Summary for Immunizations";
		    else errorImport += "\nNo Summary for Immunizations";
		}
		PreventionData prevD = new PreventionData();
		prevD.insertPreventionData(providerNo, demoNo, preventionDate, providerNo, providerName, preventionType, refused, "", "", preventionExt);
	    }
	    
	    
	    //LABORAORY RESULTS
	    cds.LaboratoryResultsDocument.LaboratoryResults[] labResultArray = patientRec.getLaboratoryResultsArray();
	    for (int i=0; i<labResultArray.length; i++) {
		String testName="", abn="", minimum="", maximum="", result="", unit="", description="", ppId="";
		if (labResultArray[i].getTestName()!=null) testName = labResultArray[i].getTestName();
		if (labResultArray[i].getResultNormalAbnormalFlag()!=null) {
		    cdsDt.ResultNormalAbnormalFlag.Enum flag = labResultArray[i].getResultNormalAbnormalFlag();
		    if (flag.equals(cdsDt.ResultNormalAbnormalFlag.Y)) abn = "Y";
		    if (flag.equals(cdsDt.ResultNormalAbnormalFlag.N)) abn = "N";
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Normal/Abnormal Flag for Laboratory Results";
		    else errorImport += "\nNo Normal/Abnormal Flag for Laboratory Results";
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
		
		LabResultImport lri = new LabResultImport();
		if (labResultArray[i].getCollectionDateTime()!=null) {
		    ppId = lri.saveLabPPInfo(getCalDate(labResultArray[i].getCollectionDateTime()));
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Collection DateTime for Laboratory Results";
		    else errorImport += "\nNo Collection DateTime for Laboratory Results";
		}
		boolean b = lri.Save(testName, abn, minimum, maximum, result, unit, description, ppId);
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
		    appointmentDate = appArray[i].getAppointmentDate().getTime();
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Appointment Date";
		    else errorImport += "\nNo Appointment Date";
		}
		if (appArray[i].getAppointmentTime()!=null) {
		    startTime = getCalTime(appArray[i].getAppointmentTime());
		    if (appArray[i].getDuration()!=null) {
			Date d_startTime = appArray[i].getAppointmentTime().getTime();
			Date d_endTime = new Date();
			d_endTime.setTime(d_startTime.getTime() + appArray[i].getDuration().longValue()*60000);
			endTime = UtilDateUtilities.DateToString(d_endTime,"HH:mm:ss");
		    }
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Appointment Time";
		    else errorImport += "\nNo Appointment Time";
		}
		if (appArray[i].getAppointmentNotes()!=null) {
		    notes = appArray[i].getAppointmentNotes();
		} else {
		    dataGood = "No";
		    if (errorImport.equals("")) errorImport = "No Appointment Notes";
		    else errorImport += "\nNo Appointment Notes";
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
		    byte[] b = repCt.getMedia();
		    if (b==null) {
			otherGood = "No";
			if (errorImport.equals("")) errorImport = "No report file in xml";
			else errorImport += "\nNo report file in xml";
		    } else {
			String docDesc=null, docType=null, contentType=null, observationDate=null, updateDateTime=null, docCreator=null;
			String docFileName = "ImportReport-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss");

			FileOutputStream f = new FileOutputStream(xmlDir+docFileName);
			f.write(b);
			f.close();
			
			if (repCt.getTextContent()!=null) docDesc = repCt.getTextContent();
			if (repR[i].getFileExtensionAndVersion()!=null) {
			    contentType = repR[i].getFileExtensionAndVersion();
			} else {
			    dataGood = "No";
			    if (errorImport.equals("")) errorImport = "No File Extension & Version for Reports";
			    else errorImport += "\nNo File Extension & Version for Reports";
			}
			if (repR[i].getEventDateTime()!=null) observationDate = repR[i].getEventDateTime().toString();
			if (repR[i].getReceivedDateTime()!=null) updateDateTime = repR[i].getReceivedDateTime().toString();
			if (repR[i].getClass1()!=null) {
			    if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT)) docType = "radiology";
			    else if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT)) docType = "pathology";
			    else if (repR[i].getClass1().equals(cdsDt.ReportClass.CONSULTANT_REPORT)) docType = "consult";
			    else docType = "others";
			} else {
			    dataGood = "No";
			    if (errorImport.equals("")) errorImport = "No Class for Reports";
			    else errorImport += "\nNo Class for Reports";
			}
			if (repR[i].getAuthorPhysician()!=null) {
			    String authorFirst = repR[i].getAuthorPhysician().getFirstName();
			    String authorLast  = repR[i].getAuthorPhysician().getLastName();
			    provD = new ProviderData();
			    ArrayList pList = provD.getProviderList();
			    for (int j=0; j<pList.size(); j++) {
				Hashtable pHash = (Hashtable) pList.get(j);
				if (authorFirst.equalsIgnoreCase((String)pHash.get("firstName")) &&
				    authorLast.equalsIgnoreCase((String)pHash.get("lastName"))) docCreator = (String)pHash.get("providerNo");
				else {
				    docCreator = provD.getNewExtProviderNo();
				    provD.addProvider(docCreator, authorFirst, authorLast, "");
				}
			    }
			}
			EDocUtil.addDocument(demoNo,docFileName,docDesc,docType,contentType,observationDate,updateDateTime,docCreator);
		    }
		}
	    }
	    errWarnings.addAll(demoRes.getWarningsCollection());
	    if (!cleanFile(xmlFile)) throw new Exception ("Error! Cannot delete XML file!");
	    
	} catch (Exception e) {
	    errWarnings.add("Error processing XML file");
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
	File importLog = new File(tDir, "ImportEvent.log");
	BufferedWriter out = new BufferedWriter(new FileWriter(importLog));
	int[] colWidth = new int[5];
	colWidth[0] = keyword[0][0].length()+1;
	colWidth[1] = keyword[1][1].length()+1;
	colWidth[2] = keyword[0][2].length()+1;
	colWidth[3] = keyword[0][3].length()+1;
	colWidth[4] = 40;
	int tableWidth = colWidth[0]+colWidth[1]+colWidth[2]+colWidth[3]+colWidth[4]+11;
	out.newLine();
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	for (int i=0; i<keyword.length; i++) {
	    out.write("|");
	    for (int j=0; j<keyword[i].length; j++) {
		out.write(fillUp(keyword[i][j],' ',colWidth[j]) + " |");
	    }
	    out.newLine();
	}
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
	
	for (int i=0; i<demo.size(); i++) {
	    out.write("|");
	    String[] info = (String[]) demo.get(i);
	    for (int j=0; j<info.length; j++) {
		out.write(fillUp(info[j],' ',colWidth[j]) + " |");
	    }
	    out.newLine();
	}
	out.write(fillUp("",'-',tableWidth));
	out.newLine();
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
