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
import java.util.List;
import java.util.Set;

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
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import oscar.OscarProperties;
import oscar.appt.ApptData;
import oscar.appt.ApptStatusData;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
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
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cds.OmdCdsDocument;
import cds.AlertsAndSpecialNeedsDocument.AlertsAndSpecialNeeds;
import cds.AllergiesAndAdverseReactionsDocument.AllergiesAndAdverseReactions;
import cds.AppointmentsDocument.Appointments;
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
import java.util.HashMap;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import oscar.oscarRx.data.RxAllergyData.Allergy;

/**
 *
 * @author Ronnie Cheng
 */
public class DemographicExportAction4 extends Action {

        private static final Logger logger = MiscUtils.getLogger();
        private static final String PATIENTID = "Patient";
        private static final String ALERT = "Alert";
        private static final String ALLERGY = "Allergy";
        private static final String APPOINTMENT = "Appointment";
        private static final String CAREELEMENTS = "Care";
        private static final String CLINICALNOTE = "Clinical";
        private static final String FAMILYHISTORY = "Family";
        private static final String IMMUNIZATION = "Immunization";
        private static final String LABS = "Labs";
        private static final String MEDICATION = "Medication";
        private static final String PASTHEALTH = "Past";
        private static final String PROBLEMLIST = "Problem";
        private static final String REPORTBINARY = "Binary";
        private static final String REPORTTEXT = "Text";
        private static final String RISKFACTOR = "Risk";

        HashMap<String, Integer> entries = new HashMap<String, Integer>();
        Integer exportNo = 0;

@Override
    @SuppressWarnings("static-access")
public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    OscarProperties oscarp = OscarProperties.getInstance();
    String strEditable = oscarp.getProperty("ENABLE_EDIT_APPT_STATUS");
    
    DemographicExportForm defrm = (DemographicExportForm)form;
    String demographicNo = defrm.getDemographicNo();
    String setName = defrm.getPatientSet();
    String pgpReady = defrm.getPgpReady();
    boolean exFamilyHistory = WebUtils.isChecked(request, "exFamilyHistory");
    boolean exPastHealth = WebUtils.isChecked(request, "exPastHealth");
    boolean exProblemList = WebUtils.isChecked(request, "exProblemList");
    boolean exRiskFactors = WebUtils.isChecked(request, "exRiskFactors");
    boolean exAllergiesAndAdverseReactions = WebUtils.isChecked(request, "exAllergiesAndAdverseReactions");
    boolean exMedicationsAndTreatments = WebUtils.isChecked(request, "exMedicationsAndTreatments");
    boolean exImmunizations = WebUtils.isChecked(request, "exImmunizations");
    boolean exLaboratoryResults = WebUtils.isChecked(request, "exLaboratoryResults");
    boolean exAppointments = WebUtils.isChecked(request, "exAppointments");
    boolean exClinicalNotes = WebUtils.isChecked(request, "exClinicalNotes");
    boolean exReportsReceived = WebUtils.isChecked(request, "exReportsReceived");
    boolean exAlertsAndSpecialNeeds = WebUtils.isChecked(request, "exAlertsAndSpecialNeeds");
    boolean exCareElements = WebUtils.isChecked(request, "exCareElements");

    ArrayList<String> list = new ArrayList<String>();
    if (demographicNo==null) {
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
	    ArrayList<ArrayList> list2 = demoQ.buildQuery(frm,UtilDateUtilities.DateToString(asofDate));
            for (ArrayList<String> listDemo : list2) {
                list.add(listDemo.get(0));
            }
	}
    } else {
	list.add(demographicNo);
    }    
    
    DemographicData d = new DemographicData();
	OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");

    ArrayList<String> inject = new ArrayList<String>();

    PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();//new PreventionDisplayConfig();         
    ArrayList<HashMap> prevList = pdc.getPreventions();
    for (int k =0 ; k < prevList.size(); k++){
        HashMap<String,String> a = new HashMap<String,String>();
        a.putAll(prevList.get(k));
	if (a != null && a.get("layout") != null &&  a.get("layout").equals("injection")){
	    inject.add(a.get("name"));
	}	     	
    }
    
    pdc = null;
    prevList = null;

    RxPrescriptionData prescriptData = new RxPrescriptionData();
    RxPrescriptionData.Prescription[] arr = null;

    CommonLabTestValues comLab = new CommonLabTestValues();
    PreventionData pd = new PreventionData();
    DemographicExt ext = new DemographicExt();

    String ffwd = "fail";
    String tmpDir = oscarp.getProperty("TMP_DIR");
    if (!Util.checkDir(tmpDir)) {
        logger.debug("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
    } else {
	XmlOptions options = new XmlOptions();
	options.put( XmlOptions.SAVE_PRETTY_PRINT );
	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );
	options.setSaveOuter();
        
        ArrayList<String> err = new ArrayList<String>();
        ArrayList<File> files = new ArrayList<File>();
	String data="";
        for (String demoNo : list) {
	    if (StringUtils.empty(demoNo)) {
		err.add("Error! No Demographic Number");
                continue;
            }

            // DEMOGRAPHICS
            DemographicData.Demographic demographic = d.getDemographic(demoNo);
            HashMap<String,String> demoExt = new HashMap<String,String>();
            demoExt.putAll(ext.getAllValuesForDemo(demoNo));

            OmdCdsDocument omdCdsDoc = OmdCdsDocument.Factory.newInstance();
            OmdCdsDocument.OmdCds omdCds = omdCdsDoc.addNewOmdCds();
            PatientRecord patientRec = omdCds.addNewPatientRecord();
            Demographics demo = patientRec.addNewDemographics();

            demo.setUniqueVendorIdSequence(demoNo);
            entries.put(PATIENTID+exportNo, Integer.valueOf(demoNo));

            cdsDt.PersonNameStandard personName = demo.addNewNames();
            cdsDt.PersonNameStandard.LegalName legalName = personName.addNewLegalName();
            cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
            cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
            legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);

            data = StringUtils.noNull(demographic.getFirstName());
            if (StringUtils.filled(data)) {
                firstName.setPart(data);
                firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
                firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
            } else {
                err.add("Error! No First Name for Patient "+demoNo);
            }
            data = StringUtils.noNull(demographic.getLastName());
            if (StringUtils.filled(data)) {
                lastName.setPart(data);
                lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
                lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
            } else {
                err.add("Error! No Last Name for Patient "+demoNo);
            }

            data = demographic.getTitle();
            if (StringUtils.filled(data)) {
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
                err.add("Error! No Name Prefix for Patient "+demoNo);
            }

            data = demographic.getOfficialLang();
            if (StringUtils.filled(data)) {
                if (data.equalsIgnoreCase("English"))     demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.ENG);
                else if (data.equalsIgnoreCase("French")) demo.setPreferredOfficialLanguage(cdsDt.OfficialSpokenLanguageCode.FRE);
            } else {
                err.add("Error! No Preferred Official Language for Patient "+demoNo);
            }

            data = demographic.getSpokenLang();
            if (StringUtils.filled(data)) {
                demo.setPreferredSpokenLanguage(data);
            }

            data = demographic.getSex();
            if (StringUtils.filled(data)) {
                demo.setGender(cdsDt.Gender.Enum.forString(data));
            } else {
                err.add("Error! No Gender for Patient "+demoNo);
            }

            data = StringUtils.noNull(demographic.getRosterStatus());
            if (StringUtils.empty(data)) {
                data = "";
                err.add("Error! No Enrollment Status for Patient "+demoNo);
            }
            data = data.equals("RO") ? "1" : "0";
            Demographics.Enrolment enrolment = demo.addNewEnrolment();
            enrolment.setEnrollmentStatus(cdsDt.EnrollmentStatus.Enum.forString(data));
            data = demographic.getRosterDate();
            if (UtilDateUtilities.StringToDate(data)!=null) {
                enrolment.setEnrollmentDate(Util.calDate(data));
            }
            data = demographic.getRosterTerminationDate();
            if (UtilDateUtilities.StringToDate(data)!=null) {
                enrolment.setEnrollmentTerminationDate(Util.calDate(data));
            }

            data = StringUtils.noNull(demographic.getPatientStatus());
            Demographics.PersonStatusCode personStatusCode = demo.addNewPersonStatusCode();
            if (StringUtils.empty(data)) {
                data = "";
                err.add("Error! No Person Status Code for Patient "+demoNo);
            }
            if (data.equals("AC")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.A);
            else if (data.equals("IN")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.I);
            else if (data.equals("DE")) personStatusCode.setPersonStatusAsEnum(cdsDt.PersonStatus.D);
            else personStatusCode.setPersonStatusAsPlainText(data);

            data = demographic.getPatientStatusDate();
            if (StringUtils.filled(data)) {
                demo.setPersonStatusDate(Util.calDate(data));
            }

            data = StringUtils.noNull(demographic.getDob("-"));
            demo.setDateOfBirth(Util.calDate(data));
            if (UtilDateUtilities.StringToDate(data)==null) {
                err.add("Error! No Date Of Birth for Patient "+demoNo);
            } else if (UtilDateUtilities.StringToDate(data)==null) {
                err.add("Note: Not exporting invalid Date of Birth for Patient "+demoNo);
            }
            data = demographic.getChartNo();
            if (StringUtils.filled(data)) demo.setChartNumber(data);

            data = demographic.getEmail();
            if (StringUtils.filled(data)) demo.setEmail(data);

            String providerNo = demographic.getProviderNo();
            if (StringUtils.filled(providerNo)) {
                Demographics.PrimaryPhysician pph = demo.addNewPrimaryPhysician();
                ProviderData prvd = new ProviderData(providerNo);
                pph.setOHIPPhysicianId(prvd.getOhip_no());
                Util.writeNameSimple(pph.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
                String cpso = prvd.getPractitionerNo();
                if (cpso!=null && cpso.length()==5) pph.setPrimaryPhysicianCPSO(cpso);
            }
            if (StringUtils.filled(demographic.getJustHIN())) {
                cdsDt.HealthCard healthCard = demo.addNewHealthCard();

                healthCard.setNumber(demographic.getJustHIN());
                healthCard.setProvinceCode(Util.setProvinceCode(demographic.getHCType()));
                if (healthCard.getProvinceCode()==null) {
                    err.add("Error! No Health Card Province Code for Patient "+demoNo);
                }
                if (StringUtils.filled(demographic.getVersionCode())) healthCard.setVersion(demographic.getVersionCode());
                data = demographic.getHCRenewDate();
                if (UtilDateUtilities.StringToDate(data)!=null) {
                    healthCard.setExpirydate(Util.calDate(data));
                }
            }
            if (StringUtils.filled(demographic.getAddress())) {
                cdsDt.Address addr = demo.addNewAddress();
                cdsDt.AddressStructured address = addr.addNewStructured();

                addr.setAddressType(cdsDt.AddressType.R);
                address.setLine1(demographic.getAddress());
                if (StringUtils.filled(demographic.getCity()) || StringUtils.filled(demographic.getProvince()) || StringUtils.filled(demographic.getPostal())) {
                    address.setCity(StringUtils.noNull(demographic.getCity()));
                    address.setCountrySubdivisionCode(Util.setCountrySubDivCode(demographic.getProvince()));
                    address.addNewPostalZipCode().setPostalCode(StringUtils.noNull(demographic.getPostal()).replace(" ",""));
                }
            }
            String phoneNo = demographic.getPhone();
            if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
                phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
                phoneResident.setPhoneNumber(phoneNo);
                data = demoExt.get("hPhoneExt");
                if (data!=null) {
                    if (data.length()>5) {
                        data = data.substring(0,5);
                        err.add("Note: Home phone extension too long - trimmed for Patient "+demoNo);
                    }
                    phoneResident.setExtension(data);
                }
            }
            phoneNo = demographic.getPhone2();
            if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                cdsDt.PhoneNumber phoneWork = demo.addNewPhoneNumber();
                phoneWork.setPhoneNumberType(cdsDt.PhoneNumberType.W);
                phoneWork.setPhoneNumber(phoneNo);
                data = demoExt.get("wPhoneExt");
                if (data!=null) {
                    if (data.length()>5) {
                        data = data.substring(0,5);
                        err.add("Note: Work phone extension too long, export trimmed for Patient "+demoNo);
                    }
                    phoneWork.setExtension(data);
                }
            }
            phoneNo = demoExt.get("demo_cell");
            if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                cdsDt.PhoneNumber phoneCell = demo.addNewPhoneNumber();
                phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
                phoneCell.setPhoneNumber(phoneNo);
            }
            demoExt = null;

            DemographicRelationship demoRel = new DemographicRelationship();
            ArrayList<HashMap> demoR = demoRel.getDemographicRelationships(demoNo);
            for (int j=0; j<demoR.size(); j++) {
                HashMap<String,String> r = new HashMap<String,String>();
                r.putAll(demoR.get(j));
                data = r.get("demographic_no");
                if (StringUtils.filled(data)) {
                    DemographicData.Demographic relDemo = d.getDemographic(data);
                    HashMap<String,String> relDemoExt = new HashMap<String,String>();
                    relDemoExt.putAll(ext.getAllValuesForDemo(data));

                    Demographics.Contact contact = demo.addNewContact();
                    Util.writeNameSimple(contact.addNewName(), relDemo.getFirstName(), relDemo.getLastName());
                    if (StringUtils.empty(relDemo.getFirstName())) {
                        err.add("Error! No First Name for contact ("+j+") for Patient "+demoNo);
                    }
                    if (StringUtils.empty(relDemo.getLastName())) {
                        err.add("Error! No Last Name for contact ("+j+") for Patient "+demoNo);
                    }

                    String ec = r.get("emergency_contact");
                    String sdm = r.get("sub_decision_maker");
                    String rel = r.get("relation");

                    if (ec.equals("1")) {
                        contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.EC);
                    }
                    if (sdm.equals("1")) {
                        contact.addNewContactPurpose().setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.SDM);
                    }
                    if (StringUtils.filled(rel)) {
                        cdsDt.PurposeEnumOrPlainText contactPurpose = contact.addNewContactPurpose();
                        if (rel.equals("Next of Kin")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.NK);
                        else if (rel.equals("Administrative Staff")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.AS);
                        else if (rel.equals("Care Giver")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.CG);
                        else if (rel.equals("Power of Attorney")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.PA);
                        else if (rel.equals("Insurance")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.IN);
                        else if (rel.equals("Guarantor")) contactPurpose.setPurposeAsEnum(cdsDt.PurposeEnumOrPlainText.PurposeAsEnum.GT);
                        else contactPurpose.setPurposeAsPlainText(rel);
                    }

                    if (StringUtils.filled(relDemo.getEmail())) contact.setEmailAddress(relDemo.getEmail());
                    if (StringUtils.filled(r.get("notes"))) contact.setNote(r.get("notes"));

                    phoneNo = relDemo.getPhone();
                    if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                        cdsDt.PhoneNumber phoneRes = contact.addNewPhoneNumber();
                        phoneRes.setPhoneNumberType(cdsDt.PhoneNumberType.R);
                        phoneRes.setPhoneNumber(phoneNo);
                        data = relDemoExt.get("hPhoneExt");
                        if (StringUtils.filled(data)) {
                            if (data.length()>5) {
                                data = data.substring(0,5);
                                err.add("Note: Home phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+demoNo);
                            }
                            phoneRes.setExtension(data);
                        }
                    }
                    phoneNo = relDemo.getPhone2();
                    if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                        cdsDt.PhoneNumber phoneW = contact.addNewPhoneNumber();
                        phoneW.setPhoneNumberType(cdsDt.PhoneNumberType.W);
                        phoneW.setPhoneNumber(phoneNo);
                        data = relDemoExt.get("wPhoneExt");
                        if (StringUtils.filled(data)) {
                            if (data.length()>5) {
                                data = data.substring(0,5);
                                err.add("Note: Work phone extension too long, export trimmed for contact ("+(j+1)+") of Patient "+demoNo);
                            }
                            phoneW.setExtension(data);
                        }
                    }
                    phoneNo = relDemoExt.get("demo_cell");
                    if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
                        cdsDt.PhoneNumber phoneCell = contact.addNewPhoneNumber();
                        phoneCell.setPhoneNumberType(cdsDt.PhoneNumberType.C);
                        phoneCell.setPhoneNumber(phoneNo);
                    }
                    relDemoExt = null;
                }
            }

            CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
            List<CaseManagementNote> lcmn = cmm.getNotes(demoNo);
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

                    // PERSONAL HISTORY (SocHistory)
                    if (StringUtils.filled(socHist)) {
                        summary = socHist;
                        summary = Util.addLine(summary, "Notes: ", annotation);
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                summary = Util.addLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                            }
                        }
                        patientRec.addNewPersonalHistory().setCategorySummaryLine(summary);
                    }
                
                if (exFamilyHistory) {
                    // FAMILY HISTORY (FamHistory)
                    if (StringUtils.filled(famHist)) {
                        FamilyHistory fHist = patientRec.addNewFamilyHistory();
                        fHist.setProblemDiagnosisProcedureDescription(famHist);
                        summary = "Problem Description: "+famHist;

                        boolean diagnosisAssigned = false;
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                if (diagnosisAssigned) {
                                    summary = Util.addLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                                } else {
                                    cdsDt.StandardCoding diagnosis = fHist.addNewDiagnosisProcedureCode();
                                    diagnosis.setStandardCodingSystem(codeSystem);
                                    diagnosis.setStandardCode(isu.getIssue().getCode());
                                    diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
                                    summary = Util.addLine(summary, "Diagnosis: ", diagnosis.getStandardCodeDescription());
                                    diagnosisAssigned = true;
                                }
                            }
                        }
                        addOneEntry(FAMILYHISTORY);
                        boolean bSTARTDATE=false, bAGEATONSET=false, bRELATIONSHIP=false, bTREATMENT=false, bLIFESTAGE=false;
                        for (CaseManagementNoteExt cme : cmeList) {
                            if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
                                if (bSTARTDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    fHist.addNewStartDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.STARTDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bSTARTDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
                                if (bAGEATONSET) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    fHist.setAgeAtOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.AGEATONSET+": ", cme.getValue());
                                }
                                bAGEATONSET = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)) {
                                if (bRELATIONSHIP) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    fHist.setRelationship(cme.getValue());
                                    summary = Util.addLine(summary, CaseManagementNoteExt.RELATIONSHIP+": ", cme.getValue());
                                }
                                bRELATIONSHIP = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)) {
                                if (bTREATMENT) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    fHist.setTreatment(cme.getValue());
                                    summary = Util.addLine(summary, CaseManagementNoteExt.TREATMENT+": ", cme.getValue());
                                }
                                bTREATMENT = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
                                if (bLIFESTAGE) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    fHist.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.LIFESTAGE+": ", cme.getValue());
                                }
                                bLIFESTAGE = true;
                            }
                        }
                        if (StringUtils.filled(annotation)) {
                            fHist.setNotes(annotation);
                            summary = Util.addLine(summary, "Notes: ", annotation);
                        }
                        fHist.setCategorySummaryLine(summary);
                    }
                }
                if (exPastHealth) {
                    // PAST HEALTH (MedHistory)
                    if (StringUtils.filled(medHist)) {
                        PastHealth pHealth = patientRec.addNewPastHealth();
                        summary = "Problem Description: " + medHist;

                        boolean diagnosisAssigned = false;
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                if (diagnosisAssigned) {
                                    summary = Util.addLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                                } else {
                                    cdsDt.StandardCoding diagnosis = pHealth.addNewDiagnosisProcedureCode();
                                    diagnosis.setStandardCodingSystem(codeSystem);
                                    diagnosis.setStandardCode(isu.getIssue().getCode());
                                    diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
                                    summary = Util.addLine(summary, "Diagnosis: ", diagnosis.getStandardCodeDescription());
                                    diagnosisAssigned = true;
                                }
                            }
                        }
                        addOneEntry(PASTHEALTH);
                        boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bPROCEDUREDATE=false, bTREATMENT=false, bLIFESTAGE=false;;
                        for (CaseManagementNoteExt cme : cmeList) {
                            if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
                                if (bSTARTDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    pHealth.addNewOnsetOrEventDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, "Onset/Event Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bSTARTDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                                if (bRESOLUTIONDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    pHealth.addNewResolvedDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, "Resolved Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bRESOLUTIONDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)) {
                                if (bPROCEDUREDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    pHealth.addNewProcedureDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.PROCEDUREDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bPROCEDUREDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
                                if (bLIFESTAGE) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    pHealth.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.LIFESTAGE+": ", cme.getValue());
                                }
                                bLIFESTAGE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)) {
                                if (bTREATMENT) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    medHist = Util.addLine(medHist, "Procedure/Intervention: ", cme.getValue());
                                    summary = Util.addLine(summary, "Procedure/Intervention: ", cme.getValue());
                                }
                                bTREATMENT = true;
                            }
                        }
                        pHealth.setPastHealthProblemDescriptionOrProcedures(medHist);
                        if (StringUtils.filled(annotation)) {
                            pHealth.setNotes(annotation);
                            summary = Util.addLine(summary, "Notes: ", annotation);
                        }
                        pHealth.setCategorySummaryLine(summary);
                    }
                }
                if (exProblemList) {
                    // PROBLEM LIST (Concerns)
                    if (StringUtils.filled(concerns)) {
                        ProblemList pList = patientRec.addNewProblemList();
                        pList.setProblemDescription(concerns);
                        summary = "Problem Description: "+concerns;

                        boolean diagnosisAssigned = false;
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                if (diagnosisAssigned) {
                                    summary = Util.addLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                                } else {
                                    cdsDt.StandardCoding diagnosis = pList.addNewDiagnosisCode();
                                    diagnosis.setStandardCodingSystem(codeSystem);
                                    diagnosis.setStandardCode(isu.getIssue().getCode());
                                    diagnosis.setStandardCodeDescription(isu.getIssue().getDescription());
                                    summary = Util.addLine(summary, "Diagnosis: ", diagnosis.getStandardCodeDescription());
                                    diagnosisAssigned = true;
                                }
                            }
                        }
                        addOneEntry(PROBLEMLIST);
                        boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bPROBLEMSTATUS=false, bLIFESTAGE=false;
                        for (CaseManagementNoteExt cme : cmeList) {
                            if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
                                if (bSTARTDATE) continue;
                                pList.addNewOnsetDate().setFullDate(Util.calDate(cme.getDateValue()));
                                summary = Util.addLine(summary, "Onset Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                if (cme.getDateValue()==null) {
                                    err.add("Error! No Onset Date for Problem List for Patient "+demoNo);
                                }
                                bSTARTDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                                if (bRESOLUTIONDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    pList.addNewResolutionDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.RESOLUTIONDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bRESOLUTIONDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
                                if (bLIFESTAGE) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    pList.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.LIFESTAGE+": ", cme.getValue());
                                }
                                bLIFESTAGE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.PROBLEMSTATUS)) {
                                if (bPROBLEMSTATUS) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    pList.setProblemStatus(cme.getValue());
                                    summary = Util.addLine(summary, CaseManagementNoteExt.PROBLEMSTATUS+": ", cme.getValue());
                                }
                                bPROBLEMSTATUS = true;
                            }
                        }

                        if (StringUtils.filled(annotation)) {
                            pList.setNotes(annotation);
                            summary = Util.addLine(summary, "Notes: ", annotation);
                        }
                        pList.setCategorySummaryLine(summary);
                    }
                }
                if (exRiskFactors) {
                    // RISK FACTORS
                    if (StringUtils.filled(riskFactors)) {
                        RiskFactors rFact = patientRec.addNewRiskFactors();
                        rFact.setRiskFactor(riskFactors);
                        summary = "Risk Factor: "+riskFactors;
                        addOneEntry(RISKFACTOR);

                        boolean bSTARTDATE=false, bRESOLUTIONDATE=false, bAGEATONSET=false, bEXPOSUREDETAIL=false, bLIFESTAGE=false;
                        for (CaseManagementNoteExt cme : cmeList) {
                            if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
                                if (bSTARTDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    rFact.addNewStartDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.STARTDATE+": ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bSTARTDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                                if (bRESOLUTIONDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    rFact.addNewEndDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    summary = Util.addLine(summary, "End Date: ", UtilDateUtilities.DateToString(cme.getDateValue(), "yyyy-MM-dd"));
                                }
                                bRESOLUTIONDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)) {
                                if (bAGEATONSET) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    rFact.setAgeOfOnset(BigInteger.valueOf(Long.valueOf(cme.getValue())));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.AGEATONSET+": ", cme.getValue());
                                }
                                bAGEATONSET = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)) {
                                if (bLIFESTAGE) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    rFact.setLifeStage(cdsDt.LifeStage.Enum.forString(cme.getValue()));
                                    summary = Util.addLine(summary, CaseManagementNoteExt.LIFESTAGE+": ", cme.getValue());
                                }
                                bLIFESTAGE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.EXPOSUREDETAIL)) {
                                if (bEXPOSUREDETAIL) continue;
                                if (StringUtils.filled(cme.getValue())) {
                                    rFact.setExposureDetails(cme.getValue());
                                    summary = Util.addLine(summary, CaseManagementNoteExt.EXPOSUREDETAIL+": ", cme.getValue());
                                }
                                bEXPOSUREDETAIL = true;
                            }
                        }
                        if (StringUtils.filled(annotation)) {
                            rFact.setNotes(annotation);
                            summary = Util.addLine(summary, "Notes: ", annotation);
                        }
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                summary = Util.addLine(summary, "Diagnosis: ", isu.getIssue().getDescription());
                            }
                        }
                        rFact.setCategorySummaryLine(summary);
                    }
                }

                if (exClinicalNotes) {
                    // CLINCAL NOTES
                    if (StringUtils.filled(encounter)) {
                        ClinicalNotes cNote = patientRec.addNewClinicalNotes();
                        for (CaseManagementIssue isu : sisu) {
                            String codeSystem = isu.getIssue().getType();
                            if (!codeSystem.equals("system")) {
                                encounter = Util.addLine(encounter, "Diagnosis: ", isu.getIssue().getDescription());
                            }
                        }
                        cNote.setMyClinicalNotesContent(encounter);
                        addOneEntry(CLINICALNOTE);

                        if (cmn.getUpdate_date()!=null) {
                            cNote.addNewEnteredDateTime().setFullDateTime(Util.calDate(cmn.getUpdate_date()));
                        }
                        if (cmn.getObservation_date()!=null) {
                            cNote.addNewEventDateTime().setFullDateTime(Util.calDate(cmn.getObservation_date()));
                        }
                        if (StringUtils.filled(cmn.getProviderNo())) {
                            ClinicalNotes.ParticipatingProviders pProvider = cNote.addNewParticipatingProviders();
                            ProviderData prvd = new ProviderData(cmn.getProviderNo());
                            Util.writeNameSimple(pProvider.addNewName(), StringUtils.noNull(prvd.getFirst_name()), StringUtils.noNull(prvd.getLast_name()));
                            pProvider.setOHIPPhysicianId(StringUtils.noNull(prvd.getOhip_no()));
                        }
                        if (StringUtils.filled(cmn.getSigning_provider_no())) {
                            ProviderData prvd = new ProviderData(cmn.getSigning_provider_no());
                            cNote.addNewNoteReviewer().setOHIPPhysicianId(StringUtils.noNull(prvd.getOhip_no()));
                        }
                    }
                }

                if (exAlertsAndSpecialNeeds) {
                    // ALERTS AND SPECIAL NEEDS (Reminders)
                    if (StringUtils.filled(reminders)) {
                        AlertsAndSpecialNeeds alerts = patientRec.addNewAlertsAndSpecialNeeds();
                        alerts.setAlertDescription(reminders);
                        addOneEntry(ALERT);

                        boolean bSTARTDATE=false, bRESOLUTIONDATE=false;
                        for (CaseManagementNoteExt cme : cmeList) {
                            if (cme.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)) {
                                if (bSTARTDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    alerts.addNewDateActive().setFullDate(Util.calDate(cme.getDateValue()));
                                    reminders = Util.addLine(reminders, "Date Active: ", UtilDateUtilities.DateToString(cme.getDateValue()));
                                }
                                bSTARTDATE = true;
                            } else if (cme.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)) {
                                if (bRESOLUTIONDATE) continue;
                                if (cme.getDateValue()!=null) {
                                    alerts.addNewEndDate().setFullDate(Util.calDate(cme.getDateValue()));
                                    reminders = Util.addLine(reminders, "End Date: ", UtilDateUtilities.DateToString(cme.getDateValue()));
                                }
                                bRESOLUTIONDATE = true;
                            }
                        }
                        if (StringUtils.filled(annotation)) {
                            alerts.setNotes(annotation);
                            reminders = Util.addLine(reminders, "Notes: ", annotation);
                        }
                        alerts.setCategorySummaryLine(reminders);
                    }
                }
            }

            if (exAllergiesAndAdverseReactions) {
                // ALLERGIES & ADVERSE REACTIONS
                RxPatientData.Patient.Allergy[] allergies = RxPatientData.getPatient(demoNo).getAllergies();
                for (int j=0; j<allergies.length; j++) {
                    AllergiesAndAdverseReactions alr = patientRec.addNewAllergiesAndAdverseReactions();
                    Allergy allergy = allergies[j].getAllergy();
                    String aSummary = "";
                    addOneEntry(ALLERGY);

                    data = allergy.getDESCRIPTION();
                    if (StringUtils.filled(data)) {
                        alr.setOffendingAgentDescription(data);
                        aSummary = "Offending Agent Description: " + data;
                    }
                    data = allergy.getRegionalIdentifier();
                    if (StringUtils.filled(data) && !data.trim().equalsIgnoreCase("null")) {
                        cdsDt.DrugCode drugCode = alr.addNewCode();
                        drugCode.setCodeType("DIN");
                        drugCode.setCodeValue(data);
                        aSummary = Util.addLine(aSummary, "DIN: ", data);
                    }
                    data = String.valueOf(allergy.getTYPECODE());
                    if (StringUtils.filled(data)) {
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
                        aSummary = Util.addLine(aSummary,"Property of Offending Agent: ",alr.getPropertyOfOffendingAgent().toString());
                    }
                    data = allergy.getReaction();
                    if (StringUtils.filled(data)) {
                        alr.setReaction(data);
                        aSummary = Util.addLine(aSummary, "Reaction: ", data);
                    }
                    data = allergy.getSeverityOfReaction();
                    if (StringUtils.filled(data)) {
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
                        aSummary = Util.addLine(aSummary,"Adverse Reaction Severity: ",alr.getSeverity().toString());
                    }
                    if (allergy.getStartDate()!=null) {
                        alr.addNewStartDate().setFullDate(Util.calDate(allergy.getStartDate()));
                        aSummary = Util.addLine(aSummary,"Start Date: ",UtilDateUtilities.DateToString(allergy.getStartDate()));
                    }
                    if (allergy.getLifeStage()!=null) {
                        alr.setLifeStage(cdsDt.LifeStage.Enum.forString(allergy.getLifeStage()));
                        aSummary = Util.addLine(aSummary,"Life Stage at Onset: ", allergy.getLifeStageDesc());
                    }

                    if (allergies[j].getEntryDate()!=null) {
                        alr.addNewRecordedDate().setFullDate(Util.calDate(allergies[j].getEntryDate()));
                        aSummary = Util.addLine(aSummary,"Recorded Date: ",UtilDateUtilities.DateToString(allergies[j].getEntryDate(),"yyyy-MM-dd"));
                    }
                    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.ALLERGIES, (long)allergies[j].getAllergyId());
                    if (cml!=null) {
                        CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
                        alr.setNotes(StringUtils.noNull(n.getNote()));
                        aSummary = Util.addLine(aSummary, "Notes: ", n.getNote());
                    }

                    if (StringUtils.empty(aSummary)) {
                        err.add("Error! No Category Summary Line (Allergies & Adverse Reactions) for Patient "+demoNo+" ("+(j+1)+")");
                    }
                    alr.setCategorySummaryLine(aSummary);
                }
            }

            if (exImmunizations) {
                // IMMUNIZATIONS
                ArrayList<HashMap> prevList2 = pd.getPreventionData(demoNo);
                for (int k =0 ; k < prevList2.size(); k++){
                    HashMap<String,String> a = new HashMap<String,String>();
                    a.putAll(prevList2.get(k));
                    if (a != null && inject.contains(a.get("type")) ){
                        Immunizations immu = patientRec.addNewImmunizations();
                        data = StringUtils.noNull(a.get("type"));
                        if (StringUtils.empty(data)) {
                            err.add("Error! No Immunization Name for Patient "+demoNo+" ("+(k+1)+")");
                        }
                        immu.setImmunizationName(data);
                        addOneEntry(IMMUNIZATION);
                        String imSummary = "Immunization Name: "+data;

                        data = a.get("refused");
                        if (StringUtils.empty(data)) {
                            immu.addNewRefusedFlag();
                            err.add("Error! No Refused Flag for Patient "+demoNo+" ("+(k+1)+")");
                        } else {
                            immu.addNewRefusedFlag().setBoolean(Util.convert10toboolean(data));
                            imSummary = Util.addLine(imSummary, "Refused Flag: ", Util.convert10toboolean(data)?"Y":"N");
                        }

                        data = a.get("prevention_date");
                        if (UtilDateUtilities.StringToDate(data)!=null) {
                            immu.addNewDate().setFullDate(Util.calDate(data));
                            imSummary = Util.addLine(imSummary, "Date: ", data);
                        }

                        HashMap<String,String> extraData = new HashMap<String,String>();
                        extraData.putAll(pd.getPreventionById(a.get("id")));
                        if (StringUtils.filled(extraData.get("manufacture"))) immu.setManufacturer(extraData.get("manufacture"));
                        if (StringUtils.filled(extraData.get("lot"))) immu.setLotNumber(extraData.get("lot"));
                        if (StringUtils.filled(extraData.get("route"))) immu.setRoute(extraData.get("route"));
                        if (StringUtils.filled(extraData.get("location"))) immu.setSite(extraData.get("location"));
                        if (StringUtils.filled(extraData.get("dose"))) immu.setDose(extraData.get("dose"));
                        if (StringUtils.filled(extraData.get("comments"))) immu.setNotes(extraData.get("comments"));

                        imSummary = Util.addLine(imSummary, "Manufacturer: ", immu.getManufacturer());
                        imSummary = Util.addLine(imSummary, "Lot No: ", immu.getLotNumber());
                        imSummary = Util.addLine(imSummary, "Route: ", immu.getRoute());
                        imSummary = Util.addLine(imSummary, "Site: ", immu.getSite());
                        imSummary = Util.addLine(imSummary, "Dose: ", immu.getDose());
                        imSummary = Util.addLine(imSummary, "Notes: ", immu.getNotes());

                        if (StringUtils.empty(imSummary)) {
                            err.add("Error! No Category Summary Line (Immunization) for Patient "+demoNo+" ("+(k+1)+")");
                        }
                        immu.setCategorySummaryLine(StringUtils.noNull(imSummary));
                        extraData = null;
                    }
                    a = null;
                }
                prevList2 = null;
            }

            if (exMedicationsAndTreatments) {
                // MEDICATIONS & TREATMENTS

                arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
                for (int p = 0; p < arr.length; p++){
                    MedicationsAndTreatments medi = patientRec.addNewMedicationsAndTreatments();
                    String mSummary = "";
                    if (arr[p].getWrittenDate()!=null) {
                        medi.addNewPrescriptionWrittenDate().setFullDate(Util.calDate(arr[p].getWrittenDate()));
                        mSummary = "Prescription Written Date: "+UtilDateUtilities.DateToString(arr[p].getWrittenDate(),"yyyy-MM-dd");
                    }
                    if (arr[p].getRxDate()!=null) {
                        medi.addNewStartDate().setFullDate(Util.calDate(arr[p].getRxDate()));
                        mSummary = Util.addLine(mSummary,"Start Date: ",UtilDateUtilities.DateToString(arr[p].getRxDate(),"yyyy-MM-dd"));
                    }
                    data = arr[p].getRegionalIdentifier();
                    if (StringUtils.filled(data)) {
                        medi.setDrugIdentificationNumber(data);
                        mSummary = Util.addLine(mSummary, "DIN: ", data);
                    }
                    String drugName = StringUtils.noNull(arr[p].getDrugName());
                    if (StringUtils.empty(drugName)) {
                        err.add("Error! No Drug Name for Patient "+demoNo+" ("+(p+1)+")");
                    }
                    medi.setDrugName(drugName);
                    addOneEntry(MEDICATION);
                    mSummary = Util.addLine(mSummary, "Drug Name: ", drugName);

                    if (StringUtils.filled(arr[p].getDosage())) {
                        String strength = arr[p].getDosage();
                        int sep = strength.indexOf("/");

                        strength = sep<0 ? strength : strength.substring(0,sep);
                        if (sep>=0) {
                            strength = strength.substring(0,sep);
                            err.add("Note: Multiple components exist for Drug "+drugName+" for Patient "+demoNo+". Exporting 1st one as Strength.");
                        }
                        cdsDt.DrugMeasure drugM = medi.addNewStrength();
                        int space = strength.indexOf(" ");
                        if (space>0) {
                            drugM.setAmount(strength.substring(0,space));
                            drugM.setUnitOfMeasure(strength.substring(strength.indexOf(" ")+1));
                        }
                        else drugM.setAmount(strength);
                        
                        mSummary = Util.addLine(mSummary, "Strength: ", arr[p].getGenericName()+" "+strength);
                    }
                    if (StringUtils.filled(arr[p].getDosageDisplay())) {
                        medi.setDosage(arr[p].getDosageDisplay());
                        medi.setDosageUnitOfMeasure(StringUtils.noNull(arr[p].getUnit()));
                        mSummary = Util.addLine(mSummary, "Dosage: ", arr[p].getDosageDisplay()+" "+StringUtils.noNull(arr[p].getUnit()));
                    }
                    if (StringUtils.filled(arr[p].getRoute())) {
                        medi.setRoute(arr[p].getRoute());
                        mSummary = Util.addLine(mSummary, "Route: ", arr[p].getRoute());
                    }
                    if (StringUtils.filled(arr[p].getDrugForm())) {
                        medi.setForm(arr[p].getDrugForm());
                        mSummary = Util.addLine(mSummary, "Form: ", arr[p].getDrugForm());
                    }
                    if (StringUtils.filled(arr[p].getFreqDisplay())) {
                        medi.setFrequency(arr[p].getFreqDisplay());
                        mSummary = Util.addLine(mSummary, "Frequency: ", arr[p].getFreqDisplay());
                    }
                    data = arr[p].getDuration();
                    if (StringUtils.filled(data)) {
                        String durunit = StringUtils.noNull(arr[p].getDurationUnit());
                        Integer fctr = 1;
                        if (durunit.equals("W")) fctr = 7;
                        else if (durunit.equals("M")) fctr = 30;

                        if (NumberUtils.isDigits(data)) {
                            data = String.valueOf(Integer.parseInt(data)*fctr);
                            medi.setDuration(data);
                            mSummary = Util.addLine(mSummary, "Duration: ", data+" Day(s)");
                        }
                    }
                    if (StringUtils.filled(arr[p].getQuantity())) {
                        medi.setQuantity(arr[p].getQuantity());
                        mSummary = Util.addLine(mSummary, "Quantity: ", arr[p].getQuantity());
                    }
                    if (arr[p].getNosubs()) medi.setSubstitutionNotAllowed("Y");
                    else medi.setSubstitutionNotAllowed("N");
                    mSummary = Util.addLine(mSummary, "Substitution not Allowed: ", arr[p].getNosubs()?"Yes":"No");

                    if (StringUtils.filled(medi.getDrugName()) || StringUtils.filled(medi.getDrugIdentificationNumber())) {
                        medi.setNumberOfRefills(String.valueOf(arr[p].getRepeat()));
                        mSummary = Util.addLine(mSummary, "Number of Refills: ", String.valueOf(arr[p].getRepeat()));
                    }
                    if (StringUtils.filled(arr[p].getETreatmentType())) {
                        medi.setTreatmentType(arr[p].getETreatmentType());
                        mSummary = Util.addLine(mSummary, "Treatment Type: ", arr[p].getETreatmentType());
                    }
                    if (StringUtils.filled(arr[p].getRxStatus())) {
                        medi.setPrescriptionStatus(arr[p].getRxStatus());
                        mSummary = Util.addLine(mSummary, "Prescription Status: ", arr[p].getRxStatus());
                    }
                    if (arr[p].getDispenseInterval()!=null) {
                        medi.setDispenseInterval(String.valueOf(arr[p].getDispenseInterval()));
                        mSummary = Util.addLine(mSummary, "Dispense Interval: ", arr[p].getDispenseInterval().toString());
                    }
                    if (arr[p].getRefillDuration()!=null) {
                        medi.setRefillDuration(String.valueOf(arr[p].getRefillDuration()));
                        mSummary = Util.addLine(mSummary, "Refill Duration: ", arr[p].getRefillDuration().toString());
                    }
                    if (arr[p].getRefillQuantity()!=null) {
                        medi.setRefillQuantity(String.valueOf(arr[p].getRefillQuantity()));
                        mSummary = Util.addLine(mSummary, "Refill Quantity: ", arr[p].getRefillQuantity().toString());
                    }
                    if (arr[p].getLongTerm()) {
                        medi.addNewLongTermMedication().setBoolean(arr[p].getLongTerm());
                        mSummary = Util.addLine(mSummary, "Long Term Medication");
                    }
                    if (arr[p].getPastMed()) {
                        medi.addNewPastMedications().setBoolean(arr[p].getPastMed());
                        mSummary = Util.addLine(mSummary, "Past Medcation");
                    }
                    cdsDt.YnIndicatorAndBlank pc = medi.addNewPatientCompliance();
                    if (arr[p].getPatientCompliance()==null) {
                        pc.setBlank(cdsDt.Blank.X);
                    } else {
                        String patientCompliance = arr[p].getPatientCompliance() ? "Yes" : "No";
                        pc.setBoolean(arr[p].getPatientCompliance());
                        mSummary = Util.addLine(mSummary, "Patient Compliance: ", patientCompliance);
                    }
                    data = arr[p].getOutsideProviderName();
                    if (StringUtils.filled(data)) {
                        MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
                        pcb.setOHIPPhysicianId(StringUtils.noNull(arr[p].getOutsideProviderOhip()));
                        Util.writeNameSimple(pcb.addNewName(), data);
                        mSummary = Util.addLine(mSummary, "Prescribed by: ", StringUtils.noNull(data));
                    } else {
                        data = arr[p].getProviderNo();
                        if (StringUtils.filled(data)) {
                            MedicationsAndTreatments.PrescribedBy pcb = medi.addNewPrescribedBy();
                            ProviderData prvd = new ProviderData(data);
                            pcb.setOHIPPhysicianId(prvd.getOhip_no());
                            Util.writeNameSimple(pcb.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
                            mSummary = Util.addLine(mSummary, "Prescribed by: ", StringUtils.noNull(prvd.getFirst_name())+" "+StringUtils.noNull(prvd.getLast_name()));
                        }
                    }
                    data = arr[p].getSpecial();
                    if (StringUtils.filled(data)) {
                        data = Util.extractDrugInstr(data);
                        medi.setPrescriptionInstructions(data);
                        mSummary = Util.addLine(mSummary, "Prescription Instructions: ", data);
                    }

                    data = arr[p].isNonAuthoritative() ? "Y" : "N";
                    medi.setNonAuthoritativeIndicator(data);
                    mSummary = Util.addLine(mSummary, "Non-Authoritative: ", data);

                    medi.setPrescriptionIdentifier(String.valueOf(arr[p].getDrugId()));
                    mSummary = Util.addLine(mSummary, "Prescription Identifier: ", medi.getPrescriptionIdentifier());

                    CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.DRUGS, (long)arr[p].getDrugId());
                    if (cml!=null) {
                        CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
                        medi.setNotes(StringUtils.noNull(n.getNote()));
                        mSummary = Util.addLine(mSummary, "Notes: ", n.getNote());
                    }

                    if (StringUtils.empty(mSummary)) err.add("Error! No Category Summary Line (Medications & Treatments) for Patient "+demoNo+" ("+(p+1)+")");
                    medi.setCategorySummaryLine(mSummary);
                }
                arr = null;
            }

            if (exLaboratoryResults) {
                // LABORATORY RESULTS
                List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(demoNo);

                for (LabMeasurements labMea : labMeaList) {

                    LaboratoryResults labResults = patientRec.addNewLaboratoryResults();
                    labResults.setLabTestCode(StringUtils.noNull(labMea.getExtVal("identifier")));
                    labResults.setTestName(StringUtils.noNull(labMea.getExtVal("name")));
                    labResults.setTestNameReportedByLab(StringUtils.noNull(labMea.getExtVal("name")));

                    labResults.setLaboratoryName(StringUtils.noNull(labMea.getExtVal("labname")));
                    addOneEntry(LABS);
                    if (StringUtils.empty(labResults.getLaboratoryName())) {
                        err.add("Error! No Laboratory Name for Lab Test "+labResults.getLabTestCode()+" for Patient "+demoNo);
                    }

                    cdsDt.DateTimeFullOrPartial collDate = labResults.addNewCollectionDateTime();
                    Date dateTime = labMea.getMeasure().getDateObserved();
                    String sDateTime = labMea.getExtVal("datetime");
                    if (dateTime!=null) collDate.setFullDateTime(Util.calDate(dateTime));
                    else collDate.setFullDateTime(Util.calDate(sDateTime));

                    if (dateTime==null && sDateTime==null) {
                        err.add("Error! No Collection Datetime for Lab Test "+labResults.getLabTestCode()+" for Patient "+demoNo);
                    }

                    labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.U);
                    data = StringUtils.noNull(labMea.getExtVal("abnormal"));
                    if (data.equals("A")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Y);
                    if (data.equals("N")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.N);

                    data = StringUtils.noNull(labMea.getMeasure().getDataField());
                    if (StringUtils.filled(data)) {
                        LaboratoryResults.Result result = labResults.addNewResult();
                        result.setValue(data);
                        data = labMea.getExtVal("unit");
                        if (StringUtils.filled(data)) result.setUnitOfMeasure(data);
                    }

                    data = StringUtils.noNull(labMea.getExtVal("accession"));
                    if (StringUtils.filled(data)) {
                        labResults.setAccessionNumber(data);
                    }

                    data = StringUtils.noNull(labMea.getExtVal("comments"));
                    if (StringUtils.filled(data)) {
                        labResults.setNotesFromLab(data);
                    }
                    String range = StringUtils.noNull(labMea.getExtVal("range"));
                    String min = StringUtils.noNull(labMea.getExtVal("minimum"));
                    String max = StringUtils.noNull(labMea.getExtVal("maximum"));
                    LaboratoryResults.ReferenceRange refRange = LaboratoryResults.ReferenceRange.Factory.newInstance();
                    if (StringUtils.filled(range)) refRange.setReferenceRangeText(range);
                    else {
                        if (StringUtils.filled(min)) refRange.setLowLimit(min);
                        if (StringUtils.filled(max)) refRange.setHighLimit(max);
                    }
                    if (refRange.getLowLimit()!=null && refRange.getHighLimit()!=null) {
                        cds.LaboratoryResultsDocument.LaboratoryResults.ReferenceRange rr = labResults.addNewReferenceRange();
                        rr.setLowLimit(refRange.getLowLimit());
                        rr.setHighLimit(refRange.getHighLimit());
                    } else if (refRange.getReferenceRangeText()!=null) {
                        labResults.addNewReferenceRange().setReferenceRangeText(refRange.getReferenceRangeText());
                    }

                    String lab_no = StringUtils.noNull(labMea.getExtVal("lab_no"));
                    if (StringUtils.filled(lab_no)) {
                        HashMap<String,String> labRoutingInfo = new HashMap<String,String>();
                        labRoutingInfo.putAll(ProviderLabRouting.getInfo(lab_no));

                        String info = labRoutingInfo.get("comment");
                        if (StringUtils.filled(info)) labResults.setPhysiciansNotes(info);
                        String timestamp = labRoutingInfo.get("timestamp");
                        if (UtilDateUtilities.StringToDate(timestamp,"yyyy-MM-dd HH:mm:ss")!=null) {
                            LaboratoryResults.ResultReviewer reviewer = labResults.addNewResultReviewer();
                            reviewer.addNewDateTimeResultReviewed().setFullDateTime(Util.calDate(timestamp));
                            String lab_provider_no = labRoutingInfo.get("provider_no");
                            if (!"0".equals(lab_provider_no)) {
                                ProviderData pvd = new ProviderData(lab_provider_no);
                                Util.writeNameSimple(reviewer.addNewName(), pvd.getFirst_name(), pvd.getLast_name());
                                if (StringUtils.filled(pvd.getOhip_no())) reviewer.setOHIPPhysicianId(pvd.getOhip_no());
                            }
                        }

                        HashMap<String,Date> link = new HashMap<String,Date>();
                        link.putAll(LabRequestReportLink.getLinkByReport("hl7TextMessage", Long.valueOf(lab_no)));
                        Date reqDate = link.get("request_date");
                        if (reqDate!=null) labResults.addNewLabRequisitionDateTime().setFullDateTime(Util.calDate(reqDate));
                    }
                }
            }

            if (exAppointments) {
                // APPOINTMENTS
                List appts = oscarSuperManager.populate("appointmentDao", "export_appt", new String[] {demoNo});
                ApptData ap = null;
                for (int j=0; j<appts.size(); j++) {
                    ap = (ApptData)appts.get(j);
                    Appointments aptm = patientRec.addNewAppointments();
                    String apNotes = "Appointment No: "+ap.getAppointment_no();

                    cdsDt.DateFullOrPartial apDate = aptm.addNewAppointmentDate();
                    apDate.setFullDate(Util.calDate(ap.getAppointment_date()));
                    if (ap.getAppointment_date()!=null) {
                        apNotes = Util.addLine(apNotes, "Date: ", UtilDateUtilities.DateToString(ap.getDateAppointmentDate(),"yyyy-MM-dd"));
                    } else {
                        err.add("Error! No Appointment Date ("+j+") for Patient "+demoNo);
                    }

                    String startTime = ap.getStart_time();
                    aptm.setAppointmentTime(Util.calDate(ap.getStart_time()));
                    addOneEntry(APPOINTMENT);
                    if (UtilDateUtilities.StringToDate(startTime,"HH:mm:ss")!=null) {
                        apNotes = Util.addLine(apNotes, "Start Time: ", startTime);
                    } else {
                        err.add("Error! No Appointment Time ("+(j+1)+") for Patient "+demoNo);
                    }

                    long dLong = (ap.getDateEndTime().getTime()-ap.getDateStartTime().getTime())/60000+1;
                    BigInteger duration = BigInteger.valueOf(dLong); //duration in minutes
                    aptm.setDuration(duration);
                    apNotes = Util.addLine(apNotes, "Duration: ", duration.toString())+" min";

                    if (StringUtils.filled(ap.getStatus())) {
                        ApptStatusData asd = new ApptStatusData();
                        asd.setApptStatus(ap.getStatus());
                        String msg = null;
                        if (strEditable!=null&&strEditable.equalsIgnoreCase("yes"))
                            msg = asd.getTitle();
                        else
                            msg = getResources(request).getMessage(asd.getTitle());
                        if (StringUtils.filled(msg)) {
                            aptm.setAppointmentStatus(msg);
                            apNotes = Util.addLine(apNotes, "Status: ", msg);

                        } else {
                            throw new Exception ("Error! No matching message for appointment status code: " + data);
                        }
                    }
                    if (StringUtils.filled(ap.getReason())) {
                        aptm.setAppointmentPurpose(ap.getReason());
                        apNotes = Util.addLine(apNotes, "Purpose: ", ap.getReason());
                    }
                    if (StringUtils.filled(ap.getProviderNo())) {
                        Appointments.Provider prov = aptm.addNewProvider();

                        ProviderData appd = new ProviderData(ap.getProviderNo());
                        if (StringUtils.filled(appd.getOhip_no())) prov.setOHIPPhysicianId(appd.getOhip_no());
                        Util.writeNameSimple(prov.addNewName(), appd.getFirst_name(), appd.getLast_name());
                        apNotes = Util.addLine(apNotes, "Provider: ", appd.getFirst_name()+" "+appd.getLast_name());
                    }
                    if (StringUtils.filled(ap.getNotes())) apNotes = Util.addLine(apNotes, "Notes: ", ap.getNotes());
                    if (StringUtils.filled(apNotes)) {
                        aptm.setAppointmentNotes(apNotes);
                    } else {
                        aptm.setAppointmentNotes("");
                        err.add("Error! No Appointment Notes for Patient "+demoNo+" ("+(j+1)+")");
                    }
                }
            }

            if (exReportsReceived) {
                // REPORTS RECEIVED
                /*
                HRMDocumentToDemographicDao hrmDocToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
                HRMDocumentToProviderDao hrmDocToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
                HRMDocumentDao hrmDocDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");

                List<HRMDocumentToDemographic> hrmDocToDemographics = hrmDocToDemographicDao.findByDemographicNo(demoNo);
                for (HRMDocumentToDemographic hrmDocToDemographic : hrmDocToDemographics) {
                    String hrmDocumentId = hrmDocToDemographic.getHrmDocumentId();
                    List<HRMDocumentToProvider> hrmDocToProviders = hrmDocToProviderDao.findByHrmDocumentId(hrmDocumentId);
                    List<HRMDocument> hrmDocs = hrmDocDao.findById(Integer.valueOf(hrmDocumentId));
                    for (HRMDocument hrmDoc : hrmDocs) {
                        hrmDoc

                        ReportsReceived rpr = patientRec.addNewReportsReceived();
                        rpr.setClass1();
                        rpr.setContent();
                        rpr.setEventDateTime();
                        rpr.setFileExtensionAndVersion();
                        rpr.setFormat();
                        rpr.setHRMResultStatus();
                        rpr.setMedia();
                        rpr.setMessageUniqueID();
                        rpr.setNotes();
                        rpr.setOBRContentArray();
                        rpr.setReceivedDateTime();
                        rpr.setReportReviewedArray();
                        rpr.setSendingFacilityId();
                        rpr.setSendingFacilityReport();
                        rpr.setSourceAuthorPhysician();
                        rpr.setSourceFacility();
                        rpr.setSubClass();
                    }
                }
                 *
                 */

                ArrayList edoc_list = new EDocUtil().listDemoDocs(demoNo);

                if (!edoc_list.isEmpty()) {
                    for (int j=0; j<edoc_list.size(); j++) {
                        EDoc edoc = (EDoc)edoc_list.get(j);
                        ReportsReceived rpr = patientRec.addNewReportsReceived();
                        rpr.setFileExtensionAndVersion("");
                        rpr.setClass1(cdsDt.ReportClass.OTHER_LETTER);
                        rpr.setSubClass(StringUtils.noNull(edoc.getDescription()));


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
                            if (edoc.getContentType()!=null && edoc.getContentType().startsWith("text")) {
                                String str = new String(b);
                                rpc.setTextContent(str);
                                rpr.setFormat(cdsDt.ReportFormat.TEXT);
                                addOneEntry(REPORTTEXT);
                            } else {
                                rpc.setMedia(b);
                                rpr.setFormat(cdsDt.ReportFormat.BINARY);
                                addOneEntry(REPORTBINARY);
                            }

                            data = Util.mimeToExt(edoc.getContentType());
                            if (StringUtils.empty(data)) data = cutExt(edoc.getFileName());
                            if (StringUtils.empty(data)) err.add("Error! No File Extension&Version info for Document \""+edoc.getFileName()+"\"");
                            rpr.setFileExtensionAndVersion(data);

                            data = edoc.getType();
                            if (StringUtils.filled(data)) {
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
                                err.add("Note: Not exporting invalid Event Date (Reports) for Patient "+demoNo+" ("+(j+1)+")");
                            }
                            data = edoc.getDateTimeStamp();
                            if (UtilDateUtilities.StringToDate(data,"yyyy-MM-dd HH:mm:ss")!=null) {
                                rpr.addNewReceivedDateTime().setFullDateTime(Util.calDate(data));
                            } else {
                                err.add("Note: Not exporting invalid Received DateTime (Reports) for Patient "+demoNo+" ("+(j+1)+")");
                            }
                            data = edoc.getReviewDateTime();
                            if (UtilDateUtilities.StringToDate(data,"yyyy-MM-dd HH:mm:ss")!=null) {
                                ReportsReceived.ReportReviewed reportReviewed = rpr.addNewReportReviewed();
                                reportReviewed.addNewDateTimeReportReviewed().setFullDate(Util.calDate(data));
                                Util.writeNameSimple(reportReviewed.addNewName(), edoc.getReviewerName());
                                data = edoc.getReviewerOhip();
                                if (StringUtils.filled(data)) reportReviewed.setReviewingOHIPPhysicianId(data);
                            }
                            Util.writeNameSimple(rpr.addNewSourceAuthorPhysician().addNewAuthorName(), edoc.getSource());
                        }
                        CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.DOCUMENT, Long.valueOf(edoc.getDocId()));
                        if (cml!=null) {
                            CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
                            if (n.getNote()!=null) rpr.setNotes(n.getNote());
                        }

                    }
                }
            }

/*
            if (exAuditInformation) {
                // AUDIT INFORMATION
                if (StringUtils.filled(demoNo)) {
                    String[] rName = {"dateTime", "provider_no", "action", "content", "contentId", "ip"};
                    String[] rCont = new String[6];
                    String audReport = "";
                    String audSummary = "";

                    OscarLogDao oscarLogDao=(OscarLogDao) SpringUtils.getBean("oscarLogDao");

                    List<OscarLog> logList = oscarLogDao.findByDemographicId(Integer.parseInt(demoNo));

                    for (OscarLog lg : logList) {
                        rCont[0] = lg.getCreated().toString();
                        rCont[1] = lg.getProviderNo();
                        rCont[2] = lg.getAction();
                        rCont[3] = lg.getContent();
                        rCont[4] = lg.getContentId();
                        rCont[5] = lg.getIp();

                        for (int j=0; j<rName.length; j++) {
                            audReport = Util.addLine(audReport, rName[j]+": ", rCont[j]);
                        }
                        audReport = Util.addLine(audReport, "------------------------------------------------------------");
                    }
                    audSummary = audReport;
                        AuditInformation audInf = patientRec.addNewAuditInformation();
                    if (StringUtils.empty(audSummary)) {
                        err.add("Error! No Category Summary Line (Audit Information) for Patient "+demoNo);
                    } else {
                            audInf.setCategorySummaryLine(audSummary);
                    }

                    /*****************************************
                     * write to xml (as Report Text Content) *
                     *****************************************
                        audInf.addNewContent().setTextContent(audReport);
                        audInf.setFormat(cdsDt.AuditFormat.TEXT);
                }
            }
 *
 */

            if (exCareElements) {
                //CARE ELEMENTS
                List<Measurements> measList = ImportExportMeasurements.getMeasurements(demoNo);
                for (Measurements meas : measList) {
                    if (meas.getType().equals("HT")) { //Height in cm
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.Height height = careElm.addNewHeight();
                        height.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Height (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        height.setHeight(meas.getDataField());
                        height.setHeightUnit(cdsDt.Height.HeightUnit.CM);
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("WT") && meas.getMeasuringInstruction().equalsIgnoreCase("in kg")) { //Weight in kg
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.Weight weight = careElm.addNewWeight();
                        weight.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Weight (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        weight.setWeight(meas.getDataField());
                        weight.setWeightUnit(cdsDt.Weight.WeightUnit.KG);
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("WAIS") || meas.getType().equals("WC")) { //Waist Circumference in cm
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.WaistCircumference waist = careElm.addNewWaistCircumference();
                        waist.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Waist Circumference (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        waist.setWaistCircumference(meas.getDataField());
                        waist.setWaistCircumferenceUnit(cdsDt.WaistCircumference.WaistCircumferenceUnit.CM);
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("BP")) { //Blood Pressure
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.BloodPressure bloodp = careElm.addNewBloodPressure();
                        bloodp.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Blood Pressure (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        String[] sdbp = meas.getDataField().split("/");
                        bloodp.setSystolicBP(sdbp[0]);
                        bloodp.setDiastolicBP(sdbp[1]);
                        bloodp.setBPUnit(cdsDt.BloodPressure.BPUnit.MM_HG);
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("POSK")) { //Packs of Cigarettes per day
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.SmokingPacks smokp = careElm.addNewSmokingPacks();
                        smokp.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Smoking Packs (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        smokp.setPerDay(new BigDecimal(meas.getDataField()));
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("SKST")) { //Smoking Status
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.SmokingStatus smoks = careElm.addNewSmokingStatus();
                        smoks.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Smoking Status (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        smoks.setStatus(Util.yn(meas.getDataField()));
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("SMBG")) { //Self Monitoring Blood Glucose
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.SelfMonitoringBloodGlucose bloodg = careElm.addNewSelfMonitoringBloodGlucose();
                        bloodg.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Self-monitoring Blood Glucose (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        bloodg.setSelfMonitoring(Util.yn(meas.getDataField()));
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("DMME")) { //Diabetes Education
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesEducationalSelfManagement des = careElm.addNewDiabetesEducationalSelfManagement();
                        des.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Educational Self-management (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        des.setEducationalTrainingPerformed(Util.yn(meas.getDataField()));
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("SMCD")) { //Self Management Challenges
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesSelfManagementChallenges dsc = careElm.addNewDiabetesSelfManagementChallenges();
                        dsc.setCodeValue(cdsDt.DiabetesSelfManagementChallenges.CodeValue.X_44941_3);
                        dsc.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Self-management Challenges (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dsc.setChallengesIdentified(cdsDt.YnIndicatorsimple.Y);
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("MCCN")) { //Motivation Counseling Completed Nutrition
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
                        dmc.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Motivational Counselling on Nutrition (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.NUTRITION);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Nutrition) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("MCCE")) { //Motivation Counseling Completed Exercise
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
                        dmc.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Motivational Counselling on Exercise (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.EXERCISE);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Exercise) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("MCCS")) { //Motivation Counseling Completed Smoking Cessation
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
                        dmc.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Motivational Counselling on Smoking Cessation (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.SMOKING_CESSATION);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Smoking Cessation) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("MCCO")) { //Motivation Counseling Completed Other
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesMotivationalCounselling dmc = careElm.addNewDiabetesMotivationalCounselling();
                        dmc.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Motivational Counselling on Other Matters (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.OTHER);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Other) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("EYEE")) { //Dilated Eye Exam
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
                        dcs.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Complication Screening on Eye Exam (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_32468_1);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Retinal Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("FTE")) { //Foot Exam
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
                        dcs.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Complication Screening on Foot Exam (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_11397_7);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Foot Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("FTLS")) { // Foot Exam Test Loss of Sensation (Neurological Exam)
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesComplicationScreening dcs = careElm.addNewDiabetesComplicationsScreening();
                        dcs.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Complication Screening on Neurological Exam (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.NEUROLOGICAL_EXAM);
                        if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
                            err.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Neurological Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
                        }
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("CGSD")) { //Collaborative Goal Setting
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.DiabetesSelfManagementCollaborative dsco = careElm.addNewDiabetesSelfManagementCollaborative();
                        dsco.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Diabetes Self-management Collaborative Goal Setting (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        dsco.setCodeValue(cdsDt.DiabetesSelfManagementCollaborative.CodeValue.X_44943_9);
                        dsco.setDocumentedGoals(meas.getDataField());
                        addOneEntry(CAREELEMENTS);
                    } else if (meas.getType().equals("HYPE")) { //Hypoglycemic Episodes
                        CareElements careElm = patientRec.addNewCareElements();
                        cdsDt.HypoglycemicEpisodes he = careElm.addNewHypoglycemicEpisodes();
                        he.setDate(Util.calDate(meas.getDateObserved()));
                        if (meas.getDateObserved()==null) {
                            err.add("Error! No Date for Hypoglycemic Episodes (id="+meas.getId()+") for Patient "+demoNo);
                        }
                        he.setNumOfReportedEpisodes(new BigInteger(meas.getDataField()));
                        addOneEntry(CAREELEMENTS);
                    }
                }
            }


            //export file to temp directory
            try{
                File directory = new File(tmpDir);
                if(!directory.exists()){
                    throw new Exception("Temporary Export Directory does not exist!");
                }

                //Standard format for xml exported file : PatientFN_PatientLN_PatientUniqueID_DOB (DOB: ddmmyyyy)
                String expFile = demographic.getFirstName()+"_"+demographic.getLastName();
                expFile += "_"+demoNo;
                expFile += "_"+demographic.getDateOfBirth()+demographic.getMonthOfBirth()+demographic.getYearOfBirth();
                files.add(new File(directory, expFile+".xml"));
            }catch(Exception e){
                logger.error("Error", e);
            }
            try {
                    omdCdsDoc.save(files.get(files.size()-1), options);
            } catch (IOException ex) {logger.error("Error", ex);
                    throw new Exception("Cannot write .xml file(s) to export directory.\n Please check directory permissions.");
	    }
	}
	
	//create ReadMe.txt & ExportEvent.log
        files.add(makeReadMe(files, err));
        files.add(makeExportLog(files.get(0).getParentFile(), err));
	
	//zip all export files
        String zipName = files.get(0).getName().replace(".xml", ".zip");
	if (setName!=null) zipName = "export_"+setName.replace(" ","")+"_"+UtilDateUtilities.getToday("yyyyMMddHHmmss")+".zip";
	if (!Util.zipFiles(files, zipName, tmpDir)) {
            logger.debug("Error! Failed to zip export files");
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
            logger.debug("Warning: PGP Encryption NOT available - unencrypted file exported!");
            Util.downloadFile(zipName, tmpDir, response);
            ffwd = "success";
        }

        //Remove zip & export files from temp dir
        Util.cleanFile(zipName, tmpDir);
        Util.cleanFiles(files);
    }

    return mapping.findForward(ffwd);
}

    File makeReadMe(ArrayList<File> fs, ArrayList error) throws IOException {
        OscarProperties oscarp = oscar.OscarProperties.getInstance();
	File readMe = new File(fs.get(0).getParentFile(), "ReadMe.txt");
	BufferedWriter out = new BufferedWriter(new FileWriter(readMe));
	out.write("Physician Group                    : ");
	out.write(new ClinicData().getClinicName());
	out.newLine();
	out.write("CMS Vendor, Product & Version      : ");
	String vendor = oscarp.getProperty("Vendor_Product");
	if (StringUtils.empty(vendor)) {
	    error.add("Error! Vendor_Product not defined in oscar.properties");
	} else {
	    out.write(vendor);
	}
	out.newLine();
	out.write("Application Support Contact        : ");
	String support = oscarp.getProperty("Support_Contact");
	if (StringUtils.empty(support)) {
	    error.add("Error! Support_Contact not defined in oscar.properties");
	} else {
	    out.write(support);
	}
	out.newLine();
	out.write("Date and Time stamp                : ");
	out.write(UtilDateUtilities.getToday("yyyy-MM-dd hh:mm:ss aa"));
	out.newLine();
	out.write("Total patients files extracted     : ");
	out.write(String.valueOf(fs.size()));
	out.newLine();
	out.write("Number of errors                   : ");
	out.write(String.valueOf(error.size()));
	if (error.size()>0) out.write(" (See ExportEvent.log for detail)");
	out.newLine();
	out.write("Patient ID range                   : ");
	out.write(getIDInExportFilename(fs.get(0).getName()));
	out.write("-");
	out.write(getIDInExportFilename(fs.get(fs.size()-1).getName()));
	out.newLine();
	out.close();
	
	return readMe;
    }

    File makeExportLog(File dir, ArrayList<String> error) throws IOException {
            String[][] keyword = new String[2][15];
            keyword[0][0] = PATIENTID;
            keyword[1][0] = "ID";
            keyword[0][1] = " "+FAMILYHISTORY;
            keyword[1][1] = " History";
            keyword[0][2] = " "+PASTHEALTH;
            keyword[1][2] = " Health";
            keyword[0][3] = " "+PROBLEMLIST;
            keyword[1][3] = " List";
            keyword[0][4] = " "+RISKFACTOR;
            keyword[1][4] = " Factor";
            keyword[0][5] = " "+ALLERGY;
            keyword[0][6] = " "+MEDICATION;
            keyword[0][7] = " "+IMMUNIZATION;
            keyword[0][8] = " "+LABS;
            keyword[0][9] = " "+APPOINTMENT;
            keyword[0][10] = " "+CLINICALNOTE;
            keyword[1][10] = " Note";
            keyword[0][11] = "    Report    ";
            keyword[1][11] = " "+REPORTTEXT;
            keyword[1][12] = " "+REPORTBINARY;
            keyword[0][13] = " "+CAREELEMENTS;
            keyword[1][13] = " Elements";
            keyword[0][14] = " "+ALERT;

            for (int i=0; i<keyword[0].length; i++) {
                if (keyword[0][i].contains("Report")) {
                    keyword[0][i+1] = "Report2";
                    i++;
                    continue;
                }
                if (keyword[1][i]==null) keyword[1][i] = " ";
                if (keyword[0][i].length()>keyword[1][i].length()) keyword[1][i] = fillUp(keyword[1][i], ' ', keyword[0][i].length());
                if (keyword[0][i].length()<keyword[1][i].length()) keyword[0][i] = fillUp(keyword[0][i], ' ', keyword[1][i].length());
            }

            File exportLog = new File(dir, "ExportEvent.log");
            BufferedWriter out = new BufferedWriter(new FileWriter(exportLog));
            int tableWidth = 0;
            for (int i=0; i<keyword.length; i++) {
                for (int j=0; j<keyword[i].length; j++) {
                    out.write(keyword[i][j]+" |");
                    if (keyword[i][j].trim().equals("Report")) j++;
                    if (i==1) tableWidth += keyword[i][j].length()+2;
                }
                out.newLine();
            }
            out.write(fillUp("",'-',tableWidth)); out.newLine();
            exportNo++;
            for (int i=0; i<exportNo; i++) {
                for (int j=0; j<keyword[0].length; j++) {
                    String category = keyword[0][j].trim();
                    if (category.contains("Report")) category = keyword[1][j].trim();
                    Integer occurs = entries.get(category+i);
                    if (occurs==null) occurs = 0;
                    out.write(fillUp(occurs.toString(), ' ', keyword[1][j].length()));
                    out.write(" |");
                }
                out.newLine();
                out.write(fillUp("",'-',tableWidth)); out.newLine();
            }
            out.newLine();
            out.newLine();
            out.newLine();
            String column1 = "Patient ID";
            out.write(column1+" |");
            out.write("Errors/Notes");
            out.newLine();
            out.write(fillUp("",'-',tableWidth)); out.newLine();
            for (int i=0; i<exportNo; i++) {
                Integer id = entries.get(PATIENTID+i);
                if (id==null) id = 0;
                out.write(fillUp(id.toString(), ' ', column1.length()));
                out.write(" |");

                //write any error that has occurred
                out.write(error.get(0));
                out.newLine();
                for (int j=1; j<error.size(); j++) {
                    out.write(fillUp("",' ',column1.length()));
                    out.write(" |");
                    out.write(error.get(j));
                    out.newLine();
                }
                out.write(fillUp("",'-',tableWidth)); out.newLine();
            }

            out.close();
            return exportLog;
    }

/*
    File makeExportLog(ArrayList<File> fs, ArrayList<String> error) throws IOException {
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
	File exportLog = new File(fs.get(0).getParentFile(), "ExportEvent.log");
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

        for (File f : fs) {
	    field = getIDInExportFilename(f.getName()); //field=PatientID
            if (field==null) continue;

	    content = countByte(f, keyword);
	    out.write("|");
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
	    out.write(error.get(i));
	}
	out.newLine();
	out.close();
	
	return exportLog;
    }
 *
 */


    //------------------------------------------------------------
    
    private String getIDInExportFilename(String filename) {
        if (filename==null) return null;

        //PatientFN_PatientLN_PatientUniqueID_DOB
        String[] sects = filename.split("_");
        if (sects.length==4) return sects[2];

        return null;
    }

    private int[] countByte(File fin, String[] kwd) throws FileNotFoundException, IOException {
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
    
    private String cutExt(String filename) {
	if (StringUtils.empty(filename)) return "";
	String[] parts = filename.split(".");
	if (parts.length>1) return "."+parts[parts.length-1];
	else return "";
    }
    
    private String fillUp(String tobefilled, char c, int size) {
	if (size>=tobefilled.length()) {
	    int fill = size-tobefilled.length();
	    for (int i=0; i<fill; i++) tobefilled += c;
	}
	return tobefilled;
    }
    
    private void addOneEntry(String category) {
        if (StringUtils.isNullOrEmpty(category)) return;

        Integer n = entries.get(category+exportNo);
        n = n==null ? 1 : n+1;
        entries.put(category+exportNo, n);
    }

    public DemographicExportAction4() {
    }
}
