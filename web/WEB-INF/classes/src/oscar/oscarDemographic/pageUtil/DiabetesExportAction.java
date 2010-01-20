package oscar.oscarDemographic.pageUtil;

import cdsdiabetes.OmdCdsDiabetesDocument;
import cdsdiabetes.CareElementsDocument.CareElements;
import cdsdiabetes.DemographicsDocument.Demographics;
import cdsdiabetes.ImmunizationsDocument.Immunizations;
import cdsdiabetes.LaboratoryResultsDocument.LaboratoryResults;
import cdsdiabetes.MedicationsAndTreatmentsDocument.MedicationsAndTreatments;
import cdsdiabetes.PatientRecordDocument.PatientRecord;
import cdsdiabetes.ProblemListDocument.ProblemList;
import cdsdiabetes.ReportInformationDocument.ReportInformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.dx.model.DxResearch;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.oscarDemographic.data.*;
import oscar.oscarEncounter.oscarMeasurements.data.*;
import oscar.oscarLab.LabRequestReportLink;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarReport.data.DemographicSets;
import oscar.oscarReport.data.RptDemographicQueryBuilder;
import oscar.oscarReport.data.RptDemographicQueryLoader;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.util.UtilDateUtilities;


/**
 *
 * @author jaygallagher
 */
public class DiabetesExportAction extends Action {
    Date startDate;
    Date endDate;
    CaseManagementManager cmm;
    ArrayList<String> errors;

public DiabetesExportAction(){}

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    DiabetesExportForm defrm = (DiabetesExportForm)form;
    String setName = defrm.getPatientSet();
    this.startDate = UtilDateUtilities.StringToDate(defrm.getstartDate());
    this.endDate = UtilDateUtilities.StringToDate(defrm.getendDate());
    
    HttpSession se = request.getSession();
    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
    this.cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
    this.errors = new ArrayList();
    
    //Create Patient List from Patient Set
    ArrayList patientList = new DemographicSets().getDemographicSet(setName);
    /*
    Date asofDate = UtilDateUtilities.Today();
    RptDemographicReportForm frm = new RptDemographicReportForm ();
    frm.setSavedQuery(setName);
    RptDemographicQueryLoader demoL = new RptDemographicQueryLoader();
    frm = demoL.queryLoader(frm);
    frm.addDemoIfNotPresent();
    frm.setAsofDate(UtilDateUtilities.DateToString(asofDate));
    RptDemographicQueryBuilder demoQ = new RptDemographicQueryBuilder();
    patientList = demoQ.buildQuery(frm,UtilDateUtilities.DateToString(asofDate));
    */
    
    //Create export files
    String tmpDir = oscar.OscarProperties.getInstance().getProperty("TMP_DIR");
    if (!Util.checkDir(tmpDir)) {
        System.out.println("Error! Cannot write to TMP_DIR - Check oscar.properties or dir permissions.");
    } else {
        tmpDir = Util.fixDirName(tmpDir);
    }
    File[] exportFiles = this.make(patientList, tmpDir);
    
    //Create & put error.log into the file list
    File errorLog = makeErrorLog("error.log", tmpDir);
    if (errorLog!=null) {
	File[] tmp_f = new File[exportFiles.length+1];
	for (int i=0; i<exportFiles.length; i++) tmp_f[i]=exportFiles[i];
	tmp_f[tmp_f.length-1] = errorLog;
	exportFiles = tmp_f;
    }
    
    //Zip export files
    String zipName = "omd_diabetes-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".zip";
    if (!Util.zipFiles(exportFiles, zipName, tmpDir)) System.out.println("Error! Failed zipping export files");
    
    //Download zip file
    Util.downloadFile(zipName, tmpDir, response);

    //Remove zip & export files from temp dir
    Util.cleanFile(zipName, tmpDir);
    Util.cleanFiles(exportFiles);
    
    return null;
}

    void fillPatientRecord(PatientRecord patientRecord,String demoNo) throws SQLException, Exception{
	if (setProblemList(patientRecord, demoNo)) {
	    setReportInformation(patientRecord);
	    setDemographicDetails(patientRecord, demoNo);
	    setCareElements(patientRecord, demoNo);
	    setImmunizations(patientRecord, demoNo);
	    setLaboratoryResults(patientRecord, demoNo);
	    setMedicationsAndTreatments(patientRecord, demoNo);
	}
    }
    
    
    File[] make(List demographicNos, String tmpDir) throws Exception{
	XmlOptions options = new XmlOptions();
	options.put( XmlOptions.SAVE_PRETTY_PRINT );
	options.put( XmlOptions.SAVE_PRETTY_PRINT_INDENT, 3 );
	options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES );
	options.setSaveOuter();
        File[] files = new File[demographicNos.size()];

        for (int i=0; i<demographicNos.size(); i++) {
	    OmdCdsDiabetesDocument omdCdsDiabetesDoc = OmdCdsDiabetesDocument.Factory.newInstance();
	    OmdCdsDiabetesDocument.OmdCdsDiabetes omdcdsdiabetes = omdCdsDiabetesDoc.addNewOmdCdsDiabetes();
	    
	    String demoNo = null;
	    Object obj = demographicNos.get(i);
	    if (obj instanceof String) {
		demoNo = (String)obj;
	    } else {
		ArrayList<String> l2 = (ArrayList<String>)obj;
		demoNo = l2.get(0);
	    }
	    PatientRecord patientRecord = omdcdsdiabetes.addNewPatientRecord();
	    fillPatientRecord(patientRecord, demoNo);
	    
            //export file to temp directory
            try{
                File dir = new File(tmpDir);
                if(!dir.exists()){
                    throw new Exception("Temporary Export Directory (as set in oscar.properties) does not exist!");
                }
                String inFile = "omd_diabetes"+i+"-"+UtilDateUtilities.getToday("yyyy-MM-dd.HH.mm.ss")+".xml";
                files[i] = new File(dir,inFile);
            }catch(Exception e){
                e.printStackTrace();
            }
            try {
                    omdCdsDiabetesDoc.save(files[i], options);
            } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new Exception("Cannot write .xml file(s) to export directory.\nPlease check directory permissions.");
            }
	}
        return files;
    }
    
    File makeErrorLog(String errorLogName, String dirName) throws IOException {
	if (this.errors.isEmpty()) return null;

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

    private List parse() throws Exception{
        
        String omdDmlink = "https://www.ontariomd.ca/cms/xml/ontariomd_cds_diabetes.xsd";
        
        URL omdDmURL = new URL(omdDmlink);
	BufferedReader in = new BufferedReader(
				new InputStreamReader(
				omdDmURL.openStream()));

	ArrayList listOfDINS = new ArrayList();

            try {
                SAXBuilder parser = new SAXBuilder();
                Document doc = parser.build(in);
                Element root = doc.getRootElement();
                //Element formulary = root.getChild("formulary");
                Iterator<Element> items = root.getDescendants(new ElementFilter("restriction",root.getNamespace("xs")));

                while (items.hasNext()) {
                    Element pcgGroup = items.next();
                    String s = pcgGroup.getAttributeValue("base");
                    //System.out.println(pcgGroup.getName()+ "  "+s);
                    
                    
                    if ("cdsd:drugIdentificationNumber".equals(s)){
                        List<Element> l = pcgGroup.getChildren();
                        for (Element e:l){
                            //System.out.println(e.getAttributeValue("value"));
                            listOfDINS.add(e.getAttributeValue("value"));
                        }
                    }
                    
                    /*List<Element> lccNoteList = pcgGroup.getChildren("lccNote");

                    if (lccNoteList.size() > 0) {
                        ArrayList<LimitedUseCode> luList = new ArrayList();
                        for (Element lccNo : lccNoteList) {
                            luList.add(makeLUNote(lccNo));
                        }

                        Iterator<Element> drugs = pcgGroup.getDescendants(new ElementFilter("drug"));
                        while (drugs.hasNext()) {
                            Element drug = drugs.next();
                            String din = drug.getAttribute("id").getValue();
                            luLookup.put(din, luList);
                        }
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           return listOfDINS;
        
    }
    
    /*
    <xs:element name="OmdCdsDiabetes">
        <xs:complexType>
            <xs:sequence>
                <xs:element ref="PatientRecord"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
    <xs:element name="PatientRecord">
        <xs:complexType>
            <xs:sequence>
                <xs:element ref="ReportInformation"/>
                <xs:element ref="Demographics"/>
                <xs:element ref="ProblemList"/>
                <xs:element ref="MedicationsAndTreatments" minOccurs="0" maxOccurs="unbounded"/>
                <xs:element ref="Immunizations" minOccurs="0" maxOccurs="unbounded"/>
                <xs:element ref="LaboratoryResults" minOccurs="0" maxOccurs="unbounded"/>
                <xs:element ref="CareElements" minOccurs="0" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
     */
    
    
    
    void setCareElements(PatientRecord patientRecord, String demoNo) throws SQLException {
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
		dsc.setChallengesIdentified(cdsDt.YnIndicatorsimple.Y);
		dsc.setDate(Util.calDate(dateObserved));
                if (dateObserved==null) {
                    errors.add("Error! No Date for Diabetes Self-management Challenges (id="+meas.getId()+") for Patient "+demoNo);
                }
            } else if (meas.getType().equals("MCCN")) { //Motivation Counseling Completed Nutrition
                CareElements careElements = patientRecord.addNewCareElements();
		cdsDt.DiabetesMotivationalCounselling dmc = careElements.addNewDiabetesMotivationalCounselling();
		dmc.setCounsellingPerformed(cdsDt.DiabetesMotivationalCounselling.CounsellingPerformed.NUTRITION);
		if (Util.yn(meas.getDataField())==cdsDt.YnIndicatorsimple.N) {
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Nutrition) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Exercise) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Smoking Cessation) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Counselling (Other) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Retinal Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Foot Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
		    errors.add("Note: Patient "+demoNo+" didn't do Diabetes Complications Screening (Neurological Exam) on "+UtilDateUtilities.DateToString(meas.getDateObserved(),"yyyy-MM-dd"));
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
    
    void setDemographicDetails(PatientRecord patientRec, String demoNo){
	Demographics demo = patientRec.addNewDemographics();
        DemographicData.Demographic demographic = new DemographicData().getDemographic(demoNo);
        
        cdsDt.PersonNameStandard.LegalName legalName = demo.addNewNames().addNewLegalName();
        cdsDt.PersonNameStandard.LegalName.FirstName firstName = legalName.addNewFirstName();
        cdsDt.PersonNameStandard.LegalName.LastName  lastName  = legalName.addNewLastName();
        legalName.setNamePurpose(cdsDt.PersonNamePurposeCode.L);
		
        String data = Util.noNull(demographic.getFirstName());
	firstName.setPart(data);
	firstName.setPartType(cdsDt.PersonNamePartTypeCode.GIV);
	firstName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
        if (Util.empty(data)) {
            errors.add("Error! No First Name for Patient "+demoNo);
        }
        data = Util.noNull(demographic.getLastName());
	lastName.setPart(data);
	lastName.setPartType(cdsDt.PersonNamePartTypeCode.FAMC);
	lastName.setPartQualifier(cdsDt.PersonNamePartQualifierCode.BR);
        if (Util.empty(data)) {
            errors.add("Error! No Last Name for Patient "+demoNo);
        }

        data = Util.noNull(demographic.getSex());
	demo.setGender(cdsDt.Gender.Enum.forString(data));
        if (Util.empty(data)) {
            errors.add("Error! No Gender for Patient "+demoNo);
        }
        
        data = demographic.getProviderNo();
        if (Util.filled(data)) {
            ProviderData provider = new ProviderData(data);
            data = Util.noNull(provider.getOhip_no());
	    demo.setOHIPPhysicianId(data);
            if (Util.empty(data))
	      errors.add("Error! No OHIP Physician ID for Patient "+demoNo);
        }
        
        data = Util.noNull(demographic.getJustHIN());
	cdsDt.HealthCard healthCard = demo.addNewHealthCard();
	healthCard.setNumber(data);
	if (Util.empty(data)) errors.add("Error! No Health Card Number for Patient "+demoNo);
	healthCard.setProvinceCode(Util.setProvinceCode(demographic.getProvince()));
	if (healthCard.getProvinceCode()==null) {
	    errors.add("Error! No Health Card Province Code for Patient "+demoNo);
	}
	if (Util.filled(demographic.getVersionCode())) healthCard.setVersion(demographic.getVersionCode());
	data = demographic.getEffDate();
	if (UtilDateUtilities.StringToDate(data)!=null) {
	    healthCard.setExpirydate(Util.calDate(data));
        }
        
        data = Util.noNull(demographic.getDob("-"));
        if (UtilDateUtilities.StringToDate(data)!=null) {
            demo.addNewDateOfBirth().setFullDate(Util.calDate(data));
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
        
        data = demographic.getEmail();
        if (Util.filled(data)) demo.setEmail(data);
        
        DemographicExt ext = new DemographicExt();
        Hashtable demoExt = ext.getAllValuesForDemo(demoNo);
	
        String phoneNo = demographic.getPhone();
	if (Util.filled(phoneNo) && phoneNo.length()>=7) {
            cdsDt.PhoneNumber phoneResident = demo.addNewPhoneNumber();
            phoneResident.setPhoneNumberType(cdsDt.PhoneNumberType.R);
            phoneResident.setPhoneNumber(phoneNo);
            data = (String) demoExt.get("hPhoneExt");
            if (data!=null) {
                if (data.length()>5) {
                    data = data.substring(0,5);
                    errors.add("Note: Home phone extension too long - trimmed for Patient "+demoNo);
                }
                phoneResident.setExtension(data);
            }
        }
    }
    
    void setImmunizations(PatientRecord patientRecord, String demoNo) {
        ArrayList inject = new ArrayList();
        ArrayList preventionList = PreventionDisplayConfig.getInstance().getPreventions();
        for (int i=0; i<preventionList.size(); i++){
            Hashtable h = (Hashtable) preventionList.get(i);
            if (h!= null && h.get("layout")!= null && h.get("layout").equals("injection")){
                inject.add((String)h.get("name"));
            }	     	
        }
        preventionList = new PreventionData().getPreventionData(demoNo);
        for (int i=0; i<preventionList.size(); i++){
            Hashtable h = (Hashtable) preventionList.get(i);  
            if (h!= null && inject.contains((String)h.get("type")) ){
                Immunizations immunizations = null;
                Date preventionDate = UtilDateUtilities.StringToDate((String)h.get("prevention_date"),"yyyy-MM-dd");
                if (preventionDate!=null) {
                    if (startDate.after(preventionDate) || endDate.before(preventionDate)) continue;
                }
                String data = (String) h.get("type");
                if (Util.filled(data)) {
		    if (data.equalsIgnoreCase("Flu")) {
                        immunizations = patientRecord.addNewImmunizations();
                        immunizations.setImmunizationName(Immunizations.ImmunizationName.INFLUENZA);
                    } else if (data.equalsIgnoreCase("Pneumovax")) {
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
                if (Util.empty(data)) {
                    errors.add("Error! No Refused Flag for Patient "+demoNo+" ("+immunizations.getImmunizationName()+")");
		    refused.setYnIndicatorsimple(null);
                } else {
                    refused.setBoolean(Util.convert10toboolean(data));
                }
            }                                                       
        }
    }
    
    void setLaboratoryResults(PatientRecord patientRecord, String demoNo) throws SQLException {
	List<LabMeasurements> labMeaList = ImportExportMeasurements.getLabMeasurements(demoNo);
	for (LabMeasurements labMea : labMeaList) {
	    String data = Util.noNull(labMea.getExtVal("identifier"));
	    String loinc = new MeasurementMapConfig().getLoincCodeByIdentCode(data);
            if (Util.filled(loinc)) {
                loinc = loinc.trim();
                if (loinc.equals("9318-7")) loinc = "14959-1"; //Urine Albumin-Creatinine Ratio
            }
            
	    LaboratoryResults.TestName.Enum testName = LaboratoryResults.TestName.Enum.forString(loinc);
	    if (testName==null) continue;
	    
	    LaboratoryResults labResults = patientRecord.addNewLaboratoryResults();
	    labResults.setTestName(testName); //LOINC code
	    labResults.setLabTestCode(data);
	    
	    cdsDt.DateFullOrPartial collDate = labResults.addNewCollectionDateTime();
            Date dateTime = UtilDateUtilities.StringToDate(labMea.getExtVal("datetime"),"yyyy-MM-dd HH:mm:ss");
            collDate.setDateTime(Util.calDate(dateTime));
            if (dateTime==null) {
                errors.add("Error! No Collection Datetime for Lab Test "+testName+" for Patient "+demoNo);
            }
            
	    data = labMea.getMeasure().getDataField();
	    LaboratoryResults.Result result = labResults.addNewResult();
	    result.setValue(data);
	    if (Util.empty(data)) {
		errors.add("Error! No Result Value for Lab Test "+testName+" for Patient "+demoNo);
	    }
	    
	    data = labMea.getExtVal("unit");
	    result.setUnitOfMeasure(data);
	    if (Util.empty(data)) {
		errors.add("Error! No Unit for Lab Test "+testName+" for Patient "+demoNo);
	    }
	    
	    labResults.setLaboratoryName(Util.noNull(labMea.getExtVal("labname")));
	    if (Util.empty(labResults.getLaboratoryName())) {
		errors.add("Error! No Laboratory Name for Lab Test "+testName+" for Patient "+demoNo);
	    }
	    
	    labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.U);
	    data = Util.noNull(labMea.getExtVal("abnormal"));
	    if (data.equals("A")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.Y);
	    if (data.equals("N")) labResults.setResultNormalAbnormalFlag(cdsDt.ResultNormalAbnormalFlag.N);
	    
	    data = labMea.getExtVal("accession");
	    if (Util.filled(data)) {
		labResults.setAccessionNumber(data);
	    }
	    
	    data = labMea.getExtVal("name");
	    if (Util.filled(data)) {
		labResults.setTestNameReportedByLab(data);
	    }
            
            data = labMea.getExtVal("comments");
            if (Util.filled(data)) {
                labResults.setNotesFromLab(data);
            }
            
            String range = labMea.getExtVal("range");
            String min = labMea.getExtVal("minimum");
            String max = labMea.getExtVal("maximum");
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
	    
	    String lab_no = labMea.getExtVal("lab_no");
	    if (Util.filled(lab_no)) {
		Hashtable labRoutingInfo = ProviderLabRouting.getInfo(lab_no);
		
		String info = (String)labRoutingInfo.get("comment");
		if (Util.filled(info)) labResults.setPhysiciansNotes(info);
		info = (String)labRoutingInfo.get("provider_no");
		if (!"0".equals(info)) {
		    ProviderData pd = new ProviderData(info);
		    if (Util.filled(pd.getOhip_no())) {
			LaboratoryResults.ResultReviewer reviewer = labResults.addNewResultReviewer();
			reviewer.setOHIPPhysicianId(pd.getOhip_no());
			Util.writeNameSimple(reviewer.addNewName(), pd.getFirst_name(), pd.getLast_name());
		    }
		}
		String timestamp = (String)labRoutingInfo.get("timestamp");
		if (Util.filled(timestamp)) {
		    labResults.addNewDateTimeResultReviewed().setDateTime(Util.calDate(timestamp));
		}
		
		Hashtable link = LabRequestReportLink.getLinkByReport("hl7TextMessage", Long.valueOf(lab_no));
		Date reqDate = (Date)link.get("request_date");
		if (reqDate!=null) labResults.addNewLabRequisitionDateTime().setDateTime(Util.calDate(reqDate));
	    }
	}
    }
    
    void setMedicationsAndTreatments(PatientRecord patientRecord, String demoNo) throws Exception {
        List<String> din = parse();
        RxPrescriptionData.Prescription[] pa = new RxPrescriptionData().getUniquePrescriptionsByPatient(Integer.parseInt(demoNo));
        for (int p=0; p<pa.length; p++){
            Date prescribeDate = pa[p].getWrittenDate();
            if (prescribeDate!=null) {
                if (startDate.after(prescribeDate) || endDate.before(prescribeDate)) continue;
            }
            String data = Util.noNull(pa[p].getRegionalIdentifier());
            if (!din.contains(data)) continue;
                
            MedicationsAndTreatments medications = patientRecord.addNewMedicationsAndTreatments();
            medications.setDrugIdentificationNumber(data);
            
            if (Util.calDate(pa[p].getWrittenDate())!=null) {
                medications.addNewPrescriptionWrittenDate().setFullDate(Util.calDate(pa[p].getWrittenDate()));
            }
            if (Util.calDate(pa[p].getRxDate())!=null) {
                medications.addNewStartDate().setFullDate(Util.calDate(pa[p].getRxDate()));
            }
            if (Util.calDate(pa[p].getEndDate())!=null) {
                medications.addNewEndDate().setFullDate(Util.calDate(pa[p].getEndDate()));
            }
            
            String drugName = Util.noNull(pa[p].getDrugName());
            medications.setDrugName(drugName);
            
            data = String.valueOf(pa[p].getRepeat());
            medications.setNumberOfRefills(data);
            
            if (Util.calDate(pa[p].getLastRefillDate())!=null) {
                medications.addNewLastRefillDate().setFullDate(Util.calDate(pa[p].getLastRefillDate()));
            }
            
            data = Util.noNull(pa[p].getRoute());
            medications.setRoute(pa[p].getRoute());
            
            data = Util.noNull(pa[p].getDrugForm());
            medications.setForm(pa[p].getDrugForm());
                
            data = Util.noNull(pa[p].getFreqDisplay());
            medications.setFrequency(pa[p].getFreqDisplay());
                
            data = Util.noNull(pa[p].getDuration());
	    if (Util.filled(data)) {
		String durunit = Util.noNull(pa[p].getDurationUnit());
		Integer fctr = 1;
		if (durunit.equals("W")) fctr = 7;
		else if (durunit.equals("M")) fctr = 30;
		Integer meddur = Integer.parseInt(Util.getNum(data)) * fctr;
		medications.setDuration(meddur.toString());
	    }
            
            data = Util.noNull(pa[p].getQuantity());
            medications.setQuantity(data);
            
	    data = Util.extractDrugInstr(pa[p].getSpecial());
            medications.setPrescriptionInstructions(data);
            
            data = Util.noNull(pa[p].getDosageDisplay()) + " " + Util.noNull(pa[p].getUnit());
            medications.setDosage(data);
            
            if (Util.filled(pa[p].getDosage())) {
                String strength = pa[p].getDosage();
                int sep = strength.indexOf("/");
                strength = sep<0 ? strength : strength.substring(0,sep);
		if (sep>0) {
		    strength = strength.substring(0,sep);
		    errors.add("Note: Multiple components exist for Drug "+drugName+" for Patient "+demoNo+". Exporting 1st one as Strength.");
		}
                cdsDt.DrugMeasure drugM = medications.addNewStrength();
                drugM.setAmount(strength.substring(0,strength.indexOf(" ")));
                drugM.setUnitOfMeasure(strength.substring(strength.indexOf(" ")+1));
            }
            
            medications.addNewLongTermMedication().setBoolean(pa[p].getLongTerm());
            medications.addNewPastMedications().setBoolean(pa[p].getPastMed());
            cdsDt.YnIndicatorAndBlank pc = medications.addNewPatientCompliance();
            if (pa[p].getPatientCompliance()==0) pc.setBlank(cdsDt.Blank.X);
            else pc.setBoolean(pa[p].getPatientCompliance()==1);
            
            CaseManagementNoteLink cml = cmm.getLatestLinkByTableId(CaseManagementNoteLink.DRUGS, (long)pa[p].getDrugId());
            if (cml!=null) {
                CaseManagementNote n = cmm.getNote(cml.getNoteId().toString());
                medications.setNotes(Util.noNull(n.getNote()));
            }
            
            data = pa[p].getOutsideProviderName();
            if (Util.filled(data)) {
                MedicationsAndTreatments.PrescribedBy pcb = medications.addNewPrescribedBy();
                pcb.setOHIPPhysicianId(Util.noNull(pa[p].getOutsideProviderOhip()));
                Util.writeNameSimple(pcb.addNewName(), data);
            } else {
                data = pa[p].getProviderNo();
                if (Util.filled(data)) {
                    MedicationsAndTreatments.PrescribedBy pcb = medications.addNewPrescribedBy();
                    ProviderData prvd = new ProviderData(data);
                    pcb.setOHIPPhysicianId(prvd.getOhip_no());
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
	List<DxResearch> dxList = cmm.getDxByDemographicNo(demoNo);
	for (DxResearch dx : dxList) {
	    if (diagValue.equals("E10.9")) break;
	    if (dx.getCode().equals("25001")) diagValue = "E10.9";  //Type 1 diabetes
	    else if (dx.getCode().equals("250")) diagValue = "E11.9";  //Type 2 diabetes
	    else continue;
	    
	    dateValue = dx.getStartDate();
        }
	if (Util.filled(diagValue)) {
	    ProblemList problemList = patientRecord.addNewProblemList();
	    cdsDt.Code diagnosis = problemList.addNewDiagnosisCode();
	    
	    diagnosis.setValue(diagValue);
	    diagnosis.setCodingSystem("ICD10-CA");
	    problemList.addNewOnsetDate().setDateTime(Util.calDate(dateValue));
	    if (dateValue==null) {
		errors.add("Error! No Onset Date for Diabetes Diagnosis for Patient "+demoNo);
	    }
	} else {
	    errors.add("Error! No Diabetes Diagnosis for Patient "+demoNo);
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
}
