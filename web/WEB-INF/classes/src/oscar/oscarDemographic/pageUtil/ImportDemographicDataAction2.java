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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
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

/**
 *
 * @author Ronnie Cheng
 */
public class ImportDemographicDataAction2 extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {
       
	String proNo = (String) request.getSession().getAttribute("user");
	String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
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
			importXML(ofile, tmpDir, proNo, warnings, request);
		    }
		    entry = in.getNextEntry();
		}
		if (noXML) {
		    cleanFile(ifile);
		    throw new Exception ("Error! No XML file in zip");
		}
		in.close();
		if (!cleanFile(ifile)) throw new Exception ("Error! Cannot delete import file!");

	    } else if (ifile.substring(ifile.length()-3).equals("xml")) {
		importXML(ifile, tmpDir, proNo, warnings, request);
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
    
    void importXML(String xmlFile, String xmlDir, String proNum, ArrayList errWarnings, HttpServletRequest req) throws Exception {
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
	    }
	    String sex = demo.getGender()!=null ? demo.getGender().toString() : "";
	    String birthDate = demo.getDateOfBirth()!=null ? demo.getDateOfBirth().toString() : "";
	    String roster_status = demo.getEnrollmentStatus()!=null ? demo.getEnrollmentStatus().toString() : "";
	    String patient_status = demo.getPersonStatusCode()!=null ? demo.getPersonStatusCode().toString() : "";
	    String date_joined = demo.getEnrollmentDate()!=null ? demo.getEnrollmentDate().getTime().toString() : "";
	    String end_date = demo.getEnrollmentTerminationDate()!= null ? demo.getEnrollmentTerminationDate().getTime().toString() : "";
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
			    address = addrStr.getLine1();
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
		if (!phone.equals("")) {
		    if (pn[i].getAreaCode()!=null) {
			if (pn[i].getNumber()!=null) phone = "("+pn[i].getAreaCode()+")"+pn[i].getNumber();
			else phone = "("+pn[i].getAreaCode()+")"+phone;
		    }
		    String ext = null;
		    if (pn[i].getExtension()!=null) ext = pn[i].getExtension();
		    else if (pn[i].getExchange()!=null) ext = pn[i].getExchange();

		    if (pn[i].getPhoneNumberType()==cdsDt.PhoneNumberType.W) {
			workPhone = phone;
			workExt   = ext;		    
		    } else {
			homePhone = phone;
			homeExt   = ext;
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
	    
	    String demoNo = new DemographicData().getLastDemographicNo();
	    
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
		if (pHistArray[i].getCategorySummaryLine()!=null) socialHist += pHistArray[i].getCategorySummaryLine() + "\n";
	    }
	    //FAMILY HISTORY
	    cds.FamilyHistoryDocument.FamilyHistory[] fHistArray = patientRec.getFamilyHistoryArray();
	    for (int i=0; i<fHistArray.length; i++) {
		if (fHistArray[i].getCategorySummaryLine()!=null) familyHist += fHistArray[i].getCategorySummaryLine() + "\n";
	    }
	    //PAST HEALTH
	    cds.PastHealthDocument.PastHealth[] pHealth = patientRec.getPastHealthArray();
	    for (int i=0; i< pHealth.length; i++) {
		if (pHealth[i].getCategorySummaryLine()!=null) medicalHist += pHealth[i].getCategorySummaryLine();
	    }
	    //PROBLEM LIST
	    cds.ProblemListDocument.ProblemList[] probList = patientRec.getProblemListArray();
	    for (int i=0; i<probList.length; i++) {
		if (probList[i].getCategorySummaryLine()!=null) ongConcerns += probList[i].getCategorySummaryLine();
	    }
	    //RISK FACTORS
	    cds.RiskFactorsDocument.RiskFactors[] rFactors = patientRec.getRiskFactorsArray();
	    for (int i=0; i<rFactors.length; i++) {
		if (rFactors[i].getCategorySummaryLine()!=null) reminders += rFactors[i].getCategorySummaryLine();
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
		if (aaReactArray[i].getCategorySummaryLine()!=null) description = aaReactArray[i].getCategorySummaryLine();
		if (aaReactArray[i].getCode()!=null) drugrefId = aaReactArray[i].getCode().getValue();
		if (aaReactArray[i].getReaction()!=null) reaction = aaReactArray[i].getReaction();
		if (aaReactArray[i].getRecordedDate()!=null) entryDate = aaReactArray[i].getRecordedDate().toString();
		
		boolean b = new RxAllergyImport().Save(demoNo, entryDate, description, typeCode, reaction, drugrefId);
	    }
	    
	    
	    //MEDICATIONS & TREATMENTS
	    cds.MedicationsAndTreatmentsDocument.MedicationsAndTreatments[] medArray = patientRec.getMedicationsAndTreatmentsArray();
	    for (int i=0; i<medArray.length; i++) {
		String rxDate="", endDate="", BN="", GCN_SEQNO="", frequencyCode="", duration="";
		String quantity="", repeat="", special="", route="", createDate="", dosage="";
		
		if (medArray[i].getCategorySummaryLine()!=null) special = medArray[i].getCategorySummaryLine();
		if (medArray[i].getPrescriptionWrittenDate()!=null) createDate = medArray[i].getPrescriptionWrittenDate().getFullDate().toString();
		if (medArray[i].getStartDate()!=null) rxDate = medArray[i].getStartDate().toString();
		if (medArray[i].getEndDate()!=null) endDate = medArray[i].getEndDate().toString();
		if (medArray[i].getDrugIdentificationNumber()!=null) GCN_SEQNO = medArray[i].getDrugIdentificationNumber();
		if (medArray[i].getDrugName()!=null) BN = medArray[i].getDrugName();
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
		
		if (immuArray[i].getImmunizationName()!=null) preventionType = immuArray[i].getImmunizationName();
		if (immuArray[i].getDate()!=null) preventionDate = immuArray[i].getDate().toString();
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
		    ppId = lri.saveLabPPInfo(labResultArray[i].getCollectionDateTime().toString());
		}
		boolean b = lri.Save(testName, abn, minimum, maximum, result, unit, description, ppId);
	    }
	    
	    
	    //APPOINTMENTS
	    cds.AppointmentsDocument.Appointments[] appArray = patientRec.getAppointmentsArray();
	    String name = "", notes = "", reason = "", status = "";
	    Date appointmentDate = null;
	    Date startTime = null;
	    Date endTime = null;
	    providerNo = "";

	    HttpSession session = req.getSession();
	    Properties p = (Properties) session.getAttribute("oscarVariables");
	    AppointmentDAO apD = new AppointmentDAO(p);

	    for (int i=0; i<appArray.length; i++) {
		name = lastName + "," + firstName;
		if (appArray[i].getAppointmentDate()!=null) appointmentDate = appArray[i].getAppointmentDate().getTime();
		if (appArray[i].getAppointmentTime()!=null) {
		    startTime = appArray[i].getAppointmentTime().getTime();
		    if (appArray[i].getDuration()!=null) {
			endTime = new Date();
			endTime.setTime(startTime.getTime() + appArray[i].getDuration().longValue()*60000);
		    }
		}
		if (appArray[i].getAppointmentNotes()!=null) notes = appArray[i].getAppointmentNotes();
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
		    if (b!=null) {
			String docDesc=null, docType=null, contentType=null, observationDate=null, updateDateTime=null, docCreator=null;
			String docFileName = "ImportReport-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss");

			FileOutputStream f = new FileOutputStream(xmlDir+docFileName);
			f.write(b);
			f.close();
			
			if (repCt.getTextContent()!=null) docDesc = repCt.getTextContent();
			if (repR[i].getFileExtensionAndVersion()!=null) contentType = repR[i].getFileExtensionAndVersion();
			if (repR[i].getEventDateTime()!=null) observationDate = repR[i].getEventDateTime().toString();
			if (repR[i].getReceivedDateTime()!=null) updateDateTime = repR[i].getReceivedDateTime().toString();
			if (repR[i].getClass1()!=null) {
			    if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_IMAGING_REPORT)) docType = "radiology";
			    else if (repR[i].getClass1().equals(cdsDt.ReportClass.DIAGNOSTIC_TEST_REPORT)) docType = "pathology";
			    else if (repR[i].getClass1().equals(cdsDt.ReportClass.CONSULTANT_REPORT)) docType = "consult";
			    else docType = "others";
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
    }
    
    boolean cleanFile(String filename) {
	File f = new File(filename);
	return f.delete();
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





