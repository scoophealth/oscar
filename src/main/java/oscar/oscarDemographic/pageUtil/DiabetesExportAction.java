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


package oscar.oscarDemographic.pageUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlbeans.XmlOptions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.oscarMeasurements.data.ImportExportMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.LabMeasurements;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementMapConfig;
import oscar.oscarEncounter.oscarMeasurements.data.Measurements;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.ConversionUtils;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;
import cdsdiabetes.CareElementsDocument.CareElements;
import cdsdiabetes.DemographicsDocument.Demographics;
import cdsdiabetes.DiabetesDinList;
import cdsdiabetes.ImmunizationsDocument.Immunizations;
import cdsdiabetes.LaboratoryResultsDocument.LaboratoryResults;
import cdsdiabetes.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cdsdiabetes.OmdCdsDiabetesDocument;
import cdsdiabetes.PatientRecordDocument.PatientRecord;
import cdsdiabetes.ProblemListDocument.ProblemList;
import cdsdiabetes.ReportInformationDocument.ReportInformation;



/**
 *
 * @author jaygallagher
 */
public class DiabetesExportAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    Date startDate;
    Date endDate;
    ArrayList<String> errors;
    ArrayList<String> listOfDINS = new ArrayList<String>();

    private static final Logger logger = MiscUtils.getLogger();
    private static final CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
    private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");
    private static final DemographicExtDao demographicExtDao = (DemographicExtDao) SpringUtils.getBean("demographicExtDao");


public DiabetesExportAction(){}

    @Override
public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
			throw new SecurityException("missing required security object (_demographic)");
		}
    	
    DiabetesExportForm defrm = (DiabetesExportForm)form;
    String setName = defrm.getPatientSet();
    this.startDate = UtilDateUtilities.StringToDate(defrm.getstartDate());
    this.endDate = UtilDateUtilities.StringToDate(defrm.getendDate());
    this.errors = new ArrayList<String>();
    getListOfDINS();

    //Create Patient List from Patient Set
    List<String> patientList = new DemographicSets().getDemographicSet(setName);

    //Create export files
    String tmpDir = OscarProperties.getInstance().getProperty("TMP_DIR");
    if (!Util.checkDir(tmpDir)) {
        logger.info("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
    } else {
        tmpDir = Util.fixDirName(tmpDir);
    }
    ArrayList<File> exportFiles = this.make(LoggedInInfo.getLoggedInInfoFromSession(request) , patientList, tmpDir);

    //Create & put error.log into the file list
    File errorLog = makeErrorLog("error.log", tmpDir);
    if (errorLog!=null) exportFiles.add(errorLog);

    //Zip export files
    String zipName = "omd_diabetes-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
    if (!Util.zipFiles(exportFiles, zipName, tmpDir)) logger.error("Error! Failed zipping export files");

    //Download zip file
    Util.downloadFile(zipName, tmpDir, response);

    //Remove zip & export files from temp dir
    Util.cleanFile(zipName, tmpDir);
    Util.cleanFiles(exportFiles);

    return null;
}

    boolean fillPatientRecord(LoggedInInfo loggedInInfo, PatientRecord patientRecord,String demoNo) throws SQLException, Exception{
	if (setProblemList(patientRecord, demoNo)) {
	    setReportInformation(patientRecord);
	    setDemographicDetails(loggedInInfo, patientRecord, demoNo);
	    setCareElements(patientRecord, demoNo);
	    setImmunizations(loggedInInfo, patientRecord, Integer.valueOf(demoNo));
	    setLaboratoryResults(patientRecord, demoNo);
	    setMedicationsAndTreatments(patientRecord, demoNo);
            return true;
	}
        else return false;
    }


    ArrayList<File> make(LoggedInInfo loggedInInfo, List<String> demographicNos, String tmpDir) throws Exception{
	XmlOptions options = new XmlOptions();
	options.put( XmlOptions.SAVE_PRETTY_PRINT );
	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );

        HashMap<String,String> suggestedPrefix = new HashMap<String,String>();
        suggestedPrefix.put("cds_dt","cdsd");
        options.setSaveSuggestedPrefixes(suggestedPrefix);

	options.setSaveOuter();
        ArrayList<File> files = new ArrayList<File>();

        int count = 0;
        for (String demoNo : demographicNos) {
	    OmdCdsDiabetesDocument omdCdsDiabetesDoc = OmdCdsDiabetesDocument.Factory.newInstance();
	    OmdCdsDiabetesDocument.OmdCdsDiabetes omdcdsdiabetes = omdCdsDiabetesDoc.addNewOmdCdsDiabetes();

	    PatientRecord patientRecord = omdcdsdiabetes.addNewPatientRecord();
	    if (!fillPatientRecord(loggedInInfo, patientRecord, demoNo)) continue;


            //export file to temp directory
            try{
                File dir = new File(tmpDir);
                if(!dir.exists()){
                    throw new Exception("Temporary Export Directory (as set in oscar.properties) does not exist!");
                }
                String inFile = "omd_diabetes"+count+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".xml";
                count++;
                files.add(new File(dir,inFile));
            }catch(Exception e){
                logger.error("Error", e);
            }
            try {
                    omdCdsDiabetesDoc.save(files.get(files.size()-1), options);
            } catch (IOException ex) {logger.error("Error", ex);
                    throw new Exception("Cannot write .xml file(s) to export directory.\nPlease check directory permissions.");
            }
	}
        return files;
    }

    File makeErrorLog(String errorLogName, String dirName) throws IOException {
	if (errors.isEmpty()) return null;

        dirName = Util.fixDirName(dirName);
	File errorLog = new File(dirName+errorLogName);
	BufferedWriter out = new BufferedWriter(new FileWriter(errorLog));

	for (String err : errors) {
	    out.write(err);
	    out.newLine();
	}
	out.close();
	return errorLog;
    }

    private void getListOfDINS() {
        if (listOfDINS.size()>0) return;

        String omdDmlink = OscarProperties.getInstance().getProperty("ontariomd_cds_diabetes_link");
        if (omdDmlink==null || omdDmlink.trim().isEmpty()) {
            errors.add("Error loading schema file! Property ontariomd_cds_diabetes_link not set!");
            return;
        }

        try {
            URL omdDmURL = new URL(omdDmlink);
            logger.info("Diabetes export schema: "+omdDmlink);

            BufferedReader in = new BufferedReader(new InputStreamReader(omdDmURL.openStream()));
            SAXBuilder parser = new SAXBuilder();
            Document doc;
            doc = parser.build(in);
            Element root = doc.getRootElement();
            Iterator<Element> items = root.getDescendants(new ElementFilter("restriction",root.getNamespace("xs")));

            while (items.hasNext()) {
                Element pcgGroup = items.next();
                String s = pcgGroup.getAttributeValue("base");
                if ("cdsd:drugIdentificationNumber".equals(s)){
                    List<Element> l = pcgGroup.getChildren();
                    for (Element e:l){

                        listOfDINS.add(e.getAttributeValue("value"));
                    }
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(DiabetesExportAction.class.getName()).log(Level.ERROR, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(DiabetesExportAction.class.getName()).log(Level.ERROR, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DiabetesExportAction.class.getName()).log(Level.ERROR, null, ex);
        }

        if (listOfDINS.isEmpty())
            errors.add("Error loading schema file! Cannot obtain DIN list!");
    }

    void setCareElements(PatientRecord patientRecord, String demoNo)  {
    	List<Measurements> measList = ImportExportMeasurements.getMeasurements(demoNo);
        for (Measurements meas : measList) {
            Date dateObserved = meas.getDateObserved();
            if (dateObserved!=null) {
                if (startDate.after(dateObserved) || endDate.before(dateObserved)) continue;
            }

            if (meas.getType().equals("HT")) { //Height in cm
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.Height height = careElements.addNewHeight();
		height.setHeight(meas.getDataField());
		height.setHeightUnit(cdsDt.Height.HeightUnit.CM);
		height.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
                    errors.add("Error! No Date for Height (id="+meas.getId()+") for Patient "+demoNo);
		}
            } else if (meas.getType().equals("WT") && meas.getMeasuringInstruction().equalsIgnoreCase("in kg")) { //Weight in kg
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.Weight weight = careElements.addNewWeight();
		weight.setWeight(meas.getDataField());
		weight.setWeightUnit(cdsDt.Weight.WeightUnit.KG);
                weight.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
                    errors.add("Error! No Date for Weight (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("WAIS") || meas.getType().equals("WC")) { //Waist Circumference in cm
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.WaistCircumference waist = careElements.addNewWaistCircumference();
		waist.setWaistCircumference(meas.getDataField());
		waist.setWaistCircumferenceUnit(cdsDt.WaistCircumference.WaistCircumferenceUnit.CM);
		waist.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Waist Circumference (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("BP")) { //Blood Pressure
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.BloodPressure bloodp = careElements.addNewBloodPressure();
		String[] sdbp = meas.getDataField().split("/");
		bloodp.setSystolicBP(sdbp[0]);
		bloodp.setDiastolicBP(sdbp[1]);
		bloodp.setBPUnit(cdsDt.BloodPressure.BPUnit.MM_HG);
		bloodp.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Blood Pressure (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("POSK")) { //Packs of Cigarettes per day
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.SmokingPacks smokp = careElements.addNewSmokingPacks();
		smokp.setPerDay(new BigDecimal(meas.getDataField()));
		smokp.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Smoking Packs (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("SKST")) { //Smoking
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.SmokingStatus smoks = careElements.addNewSmokingStatus();
		smoks.setStatus(Util.yn(meas.getDataField()));
		smoks.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Smoking Status (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("SMBG")) { //Self Monitoring Blood Glucose
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.SelfMonitoringBloodGlucose bloodg = careElements.addNewSelfMonitoringBloodGlucose();
		bloodg.setSelfMonitoring(Util.yn(meas.getDataField()));
		bloodg.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Self-monitoring Blood Glucose (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("DMME")) { //Diabetes Education
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesEducationalSelfManagement des = careElements.addNewDiabetesEducationalSelfManagement();
		des.setEducationalTrainingPerformed(Util.yn(meas.getDataField()));
		des.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Diabetes Educational Self-management (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("SMCD")) { //Self Management Challenges
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesSelfManagementChallenges dsc = careElements.addNewDiabetesSelfManagementChallenges();
		dsc.setCodeValue(cdsDt.DiabetesSelfManagementChallenges.CodeValue.X_44941_3);
		dsc.setChallengesIdentified(Util.yn(meas.getDataField()));
		dsc.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Diabetes Self-management Challenges (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("MCCN")) { //Motivation Counseling Completed Nutrition
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesMotivationalCounselling dmc = careElements.addNewDiabetesMotivationalCounselling();
		dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.NUTRITION);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Counselling (Nutrition) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dmc.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Motivational Counselling on Nutrition (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("MCCE")) { //Motivation Counseling Completed Exercise
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesMotivationalCounselling dmc = careElements.addNewDiabetesMotivationalCounselling();
		dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.EXERCISE);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Counselling (Exercise) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dmc.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Motivational Counselling on Exercise (id="+meas.getId()+") for Patient "+demoNo);
		}
            } else if (meas.getType().equals("MCCS")) { //Motivation Counseling Completed Smoking Cessation
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesMotivationalCounselling dmc = careElements.addNewDiabetesMotivationalCounselling();
		dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.SMOKING_CESSATION);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Counselling (Smoking Cessation) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dmc.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Motivational Counselling on Smoking Cessation (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("MCCO")) { //Motivation Counseling Completed Other
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesMotivationalCounselling dmc = careElements.addNewDiabetesMotivationalCounselling();
		dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.OTHER);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Counselling (Other) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dmc.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Motivational Counselling on Other Matters (id="+meas.getId()+") for Patient "+demoNo);
		}
            } else if (meas.getType().equals("EYEE")) { //Dilated Eye Exam
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesComplicationScreening dcs = careElements.addNewDiabetesComplicationsScreening();
		dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_32468_1);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Retinal Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dcs.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Complication Screening on Eye Exam (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("FTE")) { //Foot Exam
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesComplicationScreening dcs = careElements.addNewDiabetesComplicationsScreening();
		dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.X_11397_7);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Foot Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dcs.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Complication Screening on Foot Exam (id="+meas.getId()+") for Patient "+demoNo);
		}
            } else if (meas.getType().equals("FTLS")) { // Foot Exam Test Loss of Sensation (Neurological Exam)
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesComplicationScreening dcs = careElements.addNewDiabetesComplicationsScreening();
		dcs.setExamCode(cdsDt.DiabetesComplicationScreening.ExamCode.NEUROLOGICAL_EXAM);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Patient "+demoNo+" didn't do Diabetes Complications Screening (Neurological Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
		}
		dcs.setDate(Util.calDate(dateObserved));
		if (dateObserved==null) {
		    errors.add("Error! No Date for Diabetes Complication Screening on Neurological Exam (id="+meas.getId()+") for Patient "+demoNo);
		}
            } else if (meas.getType().equals("CGSD")) { //Collaborative Goal Setting
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesSelfManagementCollaborative dsco = careElements.addNewDiabetesSelfManagementCollaborative();
		dsco.setCodeValue(cdsDt.DiabetesSelfManagementCollaborative.CodeValue.X_44943_9);
		dsco.setDocumentedGoals(meas.getDataField());
		dsco.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Diabetes Self-management Collaborative Goal Setting (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("HYPE")) { //Hypoglycemic Episodes
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.HypoglycemicEpisodes he = careElements.addNewHypoglycemicEpisodes();
		he.setNumOfReportedEpisodes(new BigInteger(meas.getDataField()));
		he.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Hypoglycemic Episodes (id="+meas.getId()+") for Patient "+demoNo);
                }
            }
        }
    }

    void setDemographicDetails(LoggedInInfo loggedInInfo, PatientRecord patientRec, String demoNo){
	Demographics demo = patientRec.addNewDemographics();
        org.oscarehr.common.model.Demographic demographic = new DemographicData().getDemographic(loggedInInfo, demoNo);

        cdsDt.PersonNameStandard.LegalName legalName = demo.addNewNames().addNewLegalName();
        cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
        cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
        legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);

        String data = StringUtils.noNull(demographic.getFirstName());
	firstName.setPart(data);
	firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
	firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
        if (StringUtils.empty(data)) {
            errors.add("Error! No First Name for Patient "+demoNo);
        }
        data = StringUtils.noNull(demographic.getLastName());
	lastName.setPart(data);
	lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
	lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
        if (StringUtils.empty(data)) {
            errors.add("Error! No Last Name for Patient "+demoNo);
        }

        data = StringUtils.noNull(demographic.getSex());
	demo.setGender(cdsDt.Gender.Enum.forString(data));
        if (StringUtils.empty(data)) {
            errors.add("Error! No Gender for Patient "+demoNo);
        }

        data = demographic.getProviderNo();
        demo.setOHIPPhysicianId("");
        if (StringUtils.filled(data)) {
            ProviderData provider = new ProviderData(data);

            data = StringUtils.noNull(provider.getOhip_no());
	    if (data.length()<=6) demo.setOHIPPhysicianId(data);
            else errors.add("Error! No OHIP Physician ID for Patient "+demoNo);

            data = provider.getPractitionerNo();
            if (data!=null && data.length()==5) demo.setPrimaryPhysicianCPSO(data);
        }

        data = StringUtils.noNull(demographic.getHin());
	cdsDt.HealthCard healthCard = demo.addNewHealthCard();
	healthCard.setNumber(data);
	if (StringUtils.empty(data)) errors.add("Error! No Health Card Number for Patient "+demoNo);
        if (Util.setProvinceCode(demographic.getProvince())!=null) healthCard.setProvinceCode(Util.setProvinceCode(demographic.getProvince()));
        else healthCard.setProvinceCode(cdsDt.HealthCardProvinceCode.X_70); //Asked, unknown
	if (healthCard.getProvinceCode()==null) {
	    errors.add("Error! No Health Card Province Code for Patient "+demoNo);
	}
	if (StringUtils.filled(demographic.getVer())) healthCard.setVersion(demographic.getVer());
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
	if(demographic.getHcRenewDate()!=null)
		data =formatter.format(demographic.getHcRenewDate());
	else
		data=null;
	if (UtilDateUtilities.StringToDate(data)!=null) {
	    healthCard.setExpirydate(Util.calDate(data));
        }

        data = StringUtils.noNull(DemographicData.getDob(demographic,"-"));
        if (UtilDateUtilities.StringToDate(data)!=null) {
            demo.addNewDateOfBirth().setFullDate(Util.calDate(data));
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

        data = demographic.getEmail();
        if (StringUtils.filled(data)) demo.setEmail(data);

        HashMap<String,String> demoExt = new HashMap<String,String>();
        demoExt.putAll(demographicExtDao.getAllValuesForDemo(Integer.parseInt(demoNo)));

        String phoneNo = Util.onlyNum(demographic.getPhone());
        if (StringUtils.filled(phoneNo) && phoneNo.length()>=7) {
            cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
            phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
            phoneResident.setPhoneNumber(phoneNo);
            data = demoExt.get("hPhoneExt");
            if (data!=null) {
                if (data.length()>5) {
                    data = data.substring(0,5);
                    errors.add("Home phone extension too long - trimmed for Patient "+demoNo);
                }
                phoneResident.setExtension(data);
            }
        }
    }

    void setImmunizations(LoggedInInfo loggedInInfo, PatientRecord patientRecord, Integer demoNo) {
        ArrayList<String> inject = new ArrayList<String>();
        ArrayList<? extends Map<String,? extends Object>> preventionList = PreventionDisplayConfig.getInstance(loggedInInfo).getPreventions(loggedInInfo);
        for (int i=0; i<preventionList.size(); i++){
            HashMap<String,Object> h = new HashMap<String,Object>();
            h.putAll(preventionList.get(i));
            if (h!= null && h.get("layout")!= null && h.get("layout").equals("injection")){
                inject.add((String) h.get("name"));
            }
        }
        preventionList = PreventionData.getPreventionData(loggedInInfo, demoNo);
        for (int i=0; i<preventionList.size(); i++){
            HashMap<String,Object> h = new HashMap<String,Object>();
            h.putAll(preventionList.get(i));
            if (h!= null && inject.contains(h.get("type")) ){
                Immunizations immunizations = null;
                Date preventionDate = UtilDateUtilities.StringToDate((String)h.get("prevention_date"),"yyyy-MM-dd");
                if (preventionDate!=null) {
                    if (startDate.after(preventionDate) || endDate.before(preventionDate)) continue;
                }
                String data = (String) h.get("type");
                if (StringUtils.filled(data)) {
		    if (data.equalsIgnoreCase("Flu")) {
                        immunizations = patientRecord.addNewImmunizations();
                        immunizations.setImmunizationName(Immunizations.ImmunizationName.INFLUENZA);
                    } else if (data.equalsIgnoreCase("Pneumovax") || data.equalsIgnoreCase("Pneu-C")) {
                        immunizations = patientRecord.addNewImmunizations();
                        immunizations.setImmunizationName(Immunizations.ImmunizationName.PNEUMOCOCCAL);
                    } else {
                        continue;
                    }
                }
		immunizations.addNewDate().setFullDate(Util.calDate(preventionDate));
                if (preventionDate==null) {
                    errors.add("Error! Missing/Invalid Immunization Date (id="+h.get("id")+") for Patient "+demoNo+" ("+immunizations.getImmunizationName()+")");
                }
                data = (String) h.get("refused");
                cdsDt.YnIndicator refused = immunizations.addNewRefusedFlag();
                if (StringUtils.empty(data)) {
                    errors.add("Error! No Refused Flag for Patient "+demoNo+" ("+immunizations.getImmunizationName()+")");
		    refused.setYnIndicatorsimple(null);
                } else {
                    refused.setBoolean(Util.convert10toboolean(data));
                }
            }
        }
    }

    void setLaboratoryResults(PatientRecord patientRecord, String demoNo) {
	List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(demoNo);
	for (LabMeasurements labMea : labMeaList) {
	    String data = StringUtils.noNull(labMea.getExtVal("identifier"));
	    String loinc = new MeasurementMapConfig().getLoincCodeByIdentCode(data);
	    if (StringUtils.filled(loinc)) {
	    	loinc = loinc.trim();
	    	if (loinc.equals("9318-7")) loinc = "14959-1"; //Urine Albumin-Creatinine Ratio
    	}

	    LaboratoryResults.TestName.Enum testName = LaboratoryResults.TestName.Enum.forString(loinc);
	    if (testName==null) continue;

	    LaboratoryResults labResults = patientRecord.addNewLaboratoryResults();
	    labResults.setTestName(testName); //LOINC code
	    labResults.setLabTestCode(data);

	    cdsDt.DateFullOrPartial collDate = labResults.addNewCollectionDateTime();
        String sDateTime = labMea.getExtVal("datetime");
        if (StringUtils.filled(sDateTime)) {
        	collDate.setFullDate(Util.calDate(sDateTime));
        } else {
            errors.add("Error! No Collection Datetime for Lab Test "+testName+" for Patient "+demoNo);
            collDate.setFullDate(Util.calDate("0001-01-01"));
        }

	    data = labMea.getMeasure().getDataField();
	    LaboratoryResults.Result result = labResults.addNewResult();
	    if (StringUtils.filled(data)) {
	    	result.setValue(data);
	    } else {
	    	errors.add("Error! No Result Value for Lab Test "+testName+" for Patient "+demoNo);
	    }

	    data = labMea.getExtVal("unit");
	    if (StringUtils.filled(data)) {
	    	result.setUnitOfMeasure(data);
	    } else {
	    	errors.add("Error! No Unit for Lab Test "+testName+" for Patient "+demoNo);
	    }

	    labResults.setLaboratoryName(StringUtils.noNull(labMea.getExtVal("labname")));
	    if (StringUtils.empty(labResults.getLaboratoryName())) {
		errors.add("Error! No Laboratory Name for Lab Test "+testName+" for Patient "+demoNo);
	    }

	    labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.U);
	    data = StringUtils.noNull(labMea.getExtVal("abnormal"));
	    if (data.equals("A") || data.equals("L")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Y);
	    if (data.equals("N")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.N);

	    data = labMea.getExtVal("accession");
	    if (StringUtils.filled(data)) {
		labResults.setAccessionNumber(data);
	    }

	    data = labMea.getExtVal("name");
	    if (StringUtils.filled(data)) {
	    	labResults.setTestNameReportedByLab(data);
	    }

        data = labMea.getExtVal("comments");
        if (StringUtils.filled(data)) {
            labResults.setNotesFromLab(Util.replaceTags(data));
        }

        String range = labMea.getExtVal("range");
        String min = labMea.getExtVal("minimum");
        String max = labMea.getExtVal("maximum");
        LaboratoryResults.ReferenceRange refRange = labResults.addNewReferenceRange();
        if (StringUtils.filled(range)) refRange.setReferenceRangeText(range);
        else {
            if (StringUtils.filled(min)) refRange.setLowLimit(min);
            if (StringUtils.filled(max)) refRange.setHighLimit(max);
        }

        String reqDate = labMea.getExtVal("request_datetime");
        if (StringUtils.filled(reqDate)) labResults.addNewLabRequisitionDateTime().setFullDate(Util.calDate(reqDate));

	    String lab_no = labMea.getExtVal("lab_no");
	    if (StringUtils.empty(lab_no)) lab_no = labMea.getExtVal("lab_ppid");
	    if (StringUtils.filled(lab_no)) {

            //lab annotation
            String other_id = StringUtils.noNull(labMea.getExtVal("other_id"));
            String annotation = getNonDumpNote(CaseManagementNoteLink.LABTEST, Long.valueOf(lab_no), other_id);
            if (StringUtils.filled(annotation)) labResults.setPhysiciansNotes(annotation);

	    	HashMap<String,Object> labRoutingInfo = new HashMap<String,Object>();
	    	labRoutingInfo.putAll(ProviderLabRouting.getInfo(lab_no));

	    	String info = (String)labRoutingInfo.get("provider_no");
	    	if (info!=null && !"0".equals(info)) {
	    		ProviderData pd = new ProviderData(info);
	    		if (StringUtils.noNull(pd.getOhip_no()).length()<=6) {
	    			LaboratoryResults.ResultReviewer reviewer = labResults.addNewResultReviewer();
	    			reviewer.setOHIPPhysicianId(pd.getOhip_no());
	    			Util.writeNameSimple(reviewer.addNewName(), pd.getFirst_name(), pd.getLast_name());
    			}
	    	}
	    	String timestamp = ConversionUtils.toTimestampString((java.sql.Timestamp)labRoutingInfo.get("timestamp"));
    		if (StringUtils.filled(timestamp)) {
    			labResults.addNewDateTimeResultReviewed().setFullDate(Util.calDate(timestamp));
    		}
	    }
	}
    }

    void setMedicationsAndTreatments(PatientRecord patientRecord, String demoNo) throws Exception {
        if (listOfDINS.isEmpty()) {
            return;
        }

        RxPrescriptionData.Prescription[] pa = new RxPrescriptionData().getPrescriptionsByPatient(Integer.parseInt(demoNo));
        String annotation = null;
        for (int p=0; p<pa.length; p++){
            Date prescribeDate = pa[p].getWrittenDate();
            if (prescribeDate==null) continue;
            else if (startDate.after(prescribeDate) || endDate.before(prescribeDate)) continue;

            String data = StringUtils.noNull(pa[p].getRegionalIdentifier());
            if (!listOfDINS.contains(data)) continue;

            MedicationsAndTreatments medications = patientRecord.addNewMedicationsAndTreatments();
            medications.setDrugIdentificationNumber(DiabetesDinList.Enum.forString(data));

        	String dateFormat = partialDateDao.getFormat(PartialDate.DRUGS, pa[p].getDrugId(), PartialDate.DRUGS_WRITTENDATE);
        	Util.putPartialDate(medications.addNewPrescriptionWrittenDate(), prescribeDate, dateFormat);

            if (Util.calDate(pa[p].getRxDate())!=null) {
                medications.addNewStartDate().setFullDate(Util.calDate(pa[p].getRxDate()));
            }
            String drugName = StringUtils.noNull(pa[p].getBrandName());
            medications.setDrugName(StringUtils.noNull(drugName));

            if (StringUtils.filled(pa[p].getRoute())) medications.setRoute(pa[p].getRoute());
            if (StringUtils.filled(pa[p].getDrugForm())) medications.setForm(pa[p].getDrugForm());
            if (StringUtils.filled(pa[p].getFreqDisplay())) medications.setFrequency(pa[p].getFreqDisplay());

            data = pa[p].getDuration();
            if (StringUtils.filled(data)) {
            	String durunit = StringUtils.noNull(pa[p].getDurationUnit());
            	Integer fctr = 1;
            	if (durunit.equals("W")) fctr = 7;
            	else if (durunit.equals("M")) fctr = 30;
            	if (NumberUtils.isDigits(data)) {
            		Integer meddur = Integer.parseInt(data) * fctr;
            		medications.setDuration(meddur.toString());
        		} else {
        			errors.add("Error! Invalid prescrption duration for Drug "+drugName+" for Patient "+demoNo);
    			}
        	}

            if (StringUtils.filled(pa[p].getQuantity())) medications.setQuantity(pa[p].getQuantity());
            if (StringUtils.filled(pa[p].getSpecialInstruction())) medications.setPrescriptionInstructions(pa[p].getSpecialInstruction());
            if (StringUtils.filled(pa[p].getDosageDisplay())) medications.setDosage(pa[p].getDosageDisplay());
            if (StringUtils.filled(pa[p].getUnit())) medications.setDosageUnitOfMeasure(pa[p].getUnit());
            if (pa[p].getRepeat()>0) medications.setNumberOfRefills(String.valueOf(pa[p].getRepeat()));

            if (pa[p].getRefillDuration()!=null && pa[p].getRefillDuration()>0) medications.setRefillDuration(pa[p].getRefillDuration().toString());
            if (pa[p].getRefillQuantity()!=null && pa[p].getRefillQuantity()>0) medications.setRefillQuantity(pa[p].getRefillQuantity().toString());

            if (StringUtils.filled(pa[p].getDosage())) {
            	String[] strength = pa[p].getDosage().split(" ");

            	cdsDt.DrugMeasure drugM = medications.addNewStrength();
            	if (Util.leadingNum(strength[0]).equals(strength[0])) {//amount & unit separated by space
            		drugM.setAmount(strength[0]);
            		if (strength.length>1) drugM.setUnitOfMeasure(strength[1]);
            		else drugM.setUnitOfMeasure("unit"); //UnitOfMeasure cannot be null

            	} else {//amount & unit not separated, probably e.g. 50mg / 2tablet
            		if (strength.length>1 && strength[1].equals("/")) {
            			if (strength.length>2) {
            				//String unit1 = Util.leadingNum(strength[2]).equals("") ? "1" : Util.leadingNum(strength[2]);
            				String unit2 = Util.trailingTxt(strength[2]).equals("") ? "unit" : Util.trailingTxt(strength[2]);

                    		drugM.setAmount(Util.leadingNum(strength[0])+"/"+Util.leadingNum(strength[2]));
                    		drugM.setUnitOfMeasure(Util.trailingTxt(strength[0])+"/"+unit2);
            			}
            		} else {
                		drugM.setAmount(Util.leadingNum(strength[0]));
                		drugM.setUnitOfMeasure(Util.trailingTxt(strength[0]));
            		}
            	}
            }

            medications.addNewLongTermMedication().setBoolean(pa[p].getLongTerm());
            medications.addNewPastMedications().setBoolean(pa[p].getPastMed());

            cdsDt.YnIndicatorAndBlank pc = medications.addNewPatientCompliance();
            if (pa[p].getPatientCompliance()==null) pc.setBlank(cdsDt.Blank.X);
            else pc.setBoolean(pa[p].getPatientCompliance());

            annotation = getNonDumpNote(CaseManagementNoteLink.DRUGS, (long)pa[p].getDrugId(), null);
            if (StringUtils.filled(annotation)) medications.setNotes(annotation);

            data = pa[p].getOutsideProviderName();
            if (StringUtils.filled(data)) {
                MedicationsAndTreatments.PrescribedBy pcb = medications.addNewPrescribedBy();
                String ohip = pa[p].getOutsideProviderOhip();
                if (ohip!=null && ohip.trim().length()<=6)
                    pcb.setOHIPPhysicianId(ohip.trim());
                Util.writeNameSimple(pcb.addNewName(), data);
            } else {
                data = pa[p].getProviderNo();
                if (StringUtils.filled(data)) {
                    MedicationsAndTreatments.PrescribedBy pcb = medications.addNewPrescribedBy();
                    ProviderData prvd = new ProviderData(data);
                    String ohip = prvd.getOhip_no();
                    if (ohip!=null && ohip.trim().length()<=6)
                        pcb.setOHIPPhysicianId(ohip.trim());
                    Util.writeNameSimple(pcb.addNewName(), prvd.getFirst_name(), prvd.getLast_name());
                }
            }
        }
    }

    boolean setProblemList(PatientRecord patientRecord, String demoNo) {
	/*                                     */
	/* Note: Look for Diabetes Type 1 or 2 */
	/*	 Only 1 item is allowed        */
	/*                                     */
	String diagValue = "";
	Date dateValue = null;
	String dateFormat = null;
	List<Dxresearch> dxList = cmm.getDxByDemographicNo(demoNo);
	for (Dxresearch dx : dxList) {
	    if (diagValue.equals("E10.9")) break;
	    if (dx.getDxresearchCode().equals("25001")) diagValue = "E10.9";  //Type 1 diabetes
	    else if (dx.getDxresearchCode().equals("250")) diagValue = "E11.9";  //Type 2 diabetes
	    else continue;

	    dateValue = dx.getStartDate();
	    dateFormat = partialDateDao.getFormat(PartialDate.DXRESEARCH, dx.getId().intValue(), PartialDate.DXRESEARCH_STARTDATE);
	}
	if (StringUtils.filled(diagValue)) {
	    ProblemList problemList = patientRecord.addNewProblemList();
	    cdsDt.Code diagnosis = problemList.addNewDiagnosisCode();

	    diagnosis.setValue(diagValue);
	    diagnosis.setCodingSystem("ICD10-CA");

	    Util.putPartialDate(problemList.addNewOnsetDate(), dateValue, dateFormat);
		if (dateValue==null) {
			errors.add("Error! No Onset Date for Diabetes Diagnosis for Patient "+demoNo);
		}
	} else {
	    return false;
	}
	return true;
    }

    void setReportInformation(PatientRecord patientRecord) {
        ReportInformation rptInfo = patientRecord.addNewReportInformation();
        rptInfo.setReportRunDate(Util.calDate(new Date()));
        rptInfo.setReportStartDate(Util.calDate(this.startDate));
	if (this.endDate!=null) {
	    rptInfo.setReportEndDate(Util.calDate(this.endDate));
	} else {
	    rptInfo.setReportEndDate(Util.calDate("9999-12-31"));
	}

    }

    private String getNonDumpNote(Integer tableName, Long tableId, String otherId) {
    	String note = null;

    	List<CaseManagementNoteLink> cmll;
    	if (StringUtils.empty(otherId))
    		cmll = cmm.getLinkByTableIdDesc(tableName, tableId);
		else
			cmll = cmm.getLinkByTableIdDesc(tableName, tableId, otherId);

        for (CaseManagementNoteLink cml : cmll) {
        	CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
        	if (n.getNote()!=null && !n.getNote().startsWith("imported.cms4.2011.06")) {//not from dumpsite
        		note = n.getNote();
        		break;
        	}
        }
        return note;
    }
}
