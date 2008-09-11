package org.oscarehr.PMmodule.streethealth;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.service.StreetHealthReportManager;
import org.oscarehr.PMmodule.web.BaseAction;

/**
 * 
 * @author Marc Dumontier (marc@mdumontier.com)
 *
 */
public class StreetHealthIntakeReportAction extends BaseAction {
	
	private static Log log = LogFactory.getLog(StreetHealthIntakeReportAction.class);
	
	private static final String SDF_PATTERN = "yyyy-MM-dd";
	private static final String COHORT_CRITICAL_YM = "-03-31";
	
	/*
	 * 	these values must be retrieved by node id because they are not mapped to a label
	 *	if the form were to change, these might need to be adjusted. Possibly a good idea
	 *	to move to the properties file
	*/
	private static int NODEID_BASELINE_RESIDENCE_TYPE=8528;
	private static int NODEID_CURRENT_RESIDENCE_TYPE=8529;
	
	
	private static int NODEID_HOSPITALIZATION1_DATE;
	private static int NODEID_HOSPITALIZATION1_LENGTH;
	private static int NODEID_HOSPITALIZATION1_PSYCH;
	private static int NODEID_HOSPITALIZATION1_PHYS;
	private static int NODEID_HOSPITALIZATION1_DECLINED;
	
	private static int NODEID_HOSPITALIZATION2_DATE;
	private static int NODEID_HOSPITALIZATION2_LENGTH;
	private static int NODEID_HOSPITALIZATION2_PSYCH;
	private static int NODEID_HOSPITALIZATION2_PHYS;
	private static int NODEID_HOSPITALIZATION2_DECLINED;
	
	private static int NODEID_HOSPITALIZATION3_DATE;
	private static int NODEID_HOSPITALIZATION3_LENGTH;
	private static int NODEID_HOSPITALIZATION3_PSYCH;
	private static int NODEID_HOSPITALIZATION3_PHYS;
	private static int NODEID_HOSPITALIZATION3_DECLINED;
	
	private static int NODEID_HOSPITALIZATION4_DATE;
	private static int NODEID_HOSPITALIZATION4_LENGTH;
	private static int NODEID_HOSPITALIZATION4_PSYCH;
	private static int NODEID_HOSPITALIZATION4_PHYS;
	private static int NODEID_HOSPITALIZATION4_DECLINED;
	
	private static int NODEID_HOSPITALIZATION5_DATE;
	private static int NODEID_HOSPITALIZATION5_LENGTH;
	private static int NODEID_HOSPITALIZATION5_PSYCH;
	private static int NODEID_HOSPITALIZATION5_PHYS;
	private static int NODEID_HOSPITALIZATION5_DECLINED;
	
	
	
	private StreetHealthReportManager mgr;	
	
	public void loadNodeIdsWithNoLabels() {
		Properties p = new Properties();
		try {
			p.load(getClass().getClassLoader().getResourceAsStream("streethealth_report.properties"));
		}catch(IOException e) {log.warn(e);}
		
		try {
			NODEID_BASELINE_RESIDENCE_TYPE = Integer.parseInt((String)p.get("NODEID_BASELINE_RESIDENCE_TYPE"));
			NODEID_CURRENT_RESIDENCE_TYPE = Integer.parseInt((String)p.get("NODEID_CURRENT_RESIDENCE_TYPE"));
			//NODEID_HOSPITALIZATION_START = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION_START"));
			
			NODEID_HOSPITALIZATION1_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_DATE"));
			NODEID_HOSPITALIZATION1_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_LENGTH"));
			NODEID_HOSPITALIZATION1_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_PSYCH"));
			NODEID_HOSPITALIZATION1_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_PHYS"));
			NODEID_HOSPITALIZATION1_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION1_DECLINED"));
			
			NODEID_HOSPITALIZATION2_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_DATE"));
			NODEID_HOSPITALIZATION2_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_LENGTH"));
			NODEID_HOSPITALIZATION2_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_PSYCH"));
			NODEID_HOSPITALIZATION2_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_PHYS"));
			NODEID_HOSPITALIZATION2_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION2_DECLINED"));
			
			NODEID_HOSPITALIZATION3_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_DATE"));
			NODEID_HOSPITALIZATION3_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_LENGTH"));
			NODEID_HOSPITALIZATION3_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_PSYCH"));
			NODEID_HOSPITALIZATION3_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_PHYS"));
			NODEID_HOSPITALIZATION3_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION3_DECLINED"));
			
			NODEID_HOSPITALIZATION4_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_DATE"));
			NODEID_HOSPITALIZATION4_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_LENGTH"));
			NODEID_HOSPITALIZATION4_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_PSYCH"));
			NODEID_HOSPITALIZATION4_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_PHYS"));
			NODEID_HOSPITALIZATION4_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION4_DECLINED"));
			
			NODEID_HOSPITALIZATION5_DATE = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_DATE"));
			NODEID_HOSPITALIZATION5_LENGTH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_LENGTH"));
			NODEID_HOSPITALIZATION5_PSYCH = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_PSYCH"));
			NODEID_HOSPITALIZATION5_PHYS = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_PHYS"));
			NODEID_HOSPITALIZATION5_DECLINED = Integer.parseInt((String)p.get("NODEID_HOSPITALIZATION5_DECLINED"));
			
		}catch(NumberFormatException e) {log.warn(e);}
	}
	
    @SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "success";   
        
        if(request.getParameter("action") != null && request.getParameter("action").equalsIgnoreCase("download")) {
        	target="download";
        }
        
        Map<StreetHealthReportKey,Integer> results = new HashMap<StreetHealthReportKey,Integer>();
        
        
        loadNodeIdsWithNoLabels();
        
        //initialize business manager
        this.mgr = (StreetHealthReportManager) getAppContext().getBean("streetHealthReportManager");
        
      
        //check to make sure use is logged in
        HttpSession session = request.getSession(false);
        if (session == null) {
            target = "timeout";
            return(mapping.findForward(target));
        }
        
        //get start date parameter
        String strStartDate = request.getParameter("startDate");
        if(strStartDate==null) {
        	request.setAttribute("ERROR_MSG", "No start date found.");
        	return(mapping.findForward(target));
        }
        Date startDate = this.stringToDate(strStartDate, SDF_PATTERN);
        if(startDate ==null) {
        	request.setAttribute("ERROR_MSG", "Invalid start date.");
        	return(mapping.findForward(target));
        }
        
        //based on the start date, return the "cohorts".
        List<DateRange> dates = getDates(startDate);        
        request.setAttribute("dates",dates);
    
        
        //for each cohort, extract the values from the intakes
        for(int x=0;x<dates.size();x++) {
        	DateRange dr = dates.get(x);       
        	List cohort = mgr.getCohort(dr.getStartDate(), dr.getEndDate());
        	if (cohort != null) {          
        		getCohortCount(results,cohort,x);
            }
        }
        
        request.setAttribute("results", results);
        request.setAttribute("nCohorts", dates.size());              
        return(mapping.findForward(target));
    }
    

    
    /*
     * For each intake in the cohort, pull out the values, and add to the results map 
     * 
     */
    @SuppressWarnings("unchecked")
	public void getCohortCount(Map<StreetHealthReportKey,Integer> results, List cohortList, int idx) {
        int minAge=0;
        int maxAge=0;
        int avgAgeTotal=0;
        int avgAgeSize=0;
        
        for (int x = 0; x < cohortList.size(); x++) {
        	Intake intake = new Intake();
            Demographic demographic = new Demographic();
            Object[] cohort = (Object[]) cohortList.get(x);            
            boolean preAdmission = false;

            demographic = (Demographic) cohort[1];           
            intake = (Intake) cohort[0];
            
            //pre-admission
            String strPreAdmission =  intake.getAnswerKeyValues().get("Pre-Admission");
            if(strPreAdmission!=null && strPreAdmission.equals("T")) {
            	preAdmission=true;
            	addToResults(results,idx,"Total Service Recipients","Unique individuals - pre-admission");
            }  else {
            	addToResults(results,idx,"Total Service Recipients","Unique individuals - admitted");
            }
            
            
            //gender
            String gender = demographic.getSex();
            if(gender.equalsIgnoreCase("m")) {
            	addToResults(results,idx,"Gender","Male");
            } else if(gender.equalsIgnoreCase("f")) {
            	addToResults(results,idx,"Gender","Female");
            } else {
            	addToResults(results,idx,"Gender","Other");
            }
            
            
            //age
            int age = demographic.getAgeInYears();
            if(age <= 15) {
            	addToResults(results,idx,"Age","0-15");
            } else if(age >= 16 && age <= 17) {
            	addToResults(results,idx,"Age","16-17");
            } else if(age >= 18 && age <= 24) {
            	addToResults(results,idx,"Age","18-24");
            } else if(age >= 25 && age <= 34) {
            	addToResults(results,idx,"Age","25-34");
            } else if(age >= 35 && age <= 44) {
            	addToResults(results,idx,"Age","35-44");
            } else if(age >= 45 && age <= 54) {
            	addToResults(results,idx,"Age","45-54");
            } else if(age > 55 && age <= 64) {
            	addToResults(results,idx,"Age","55-64");
            } else if(age > 65 && age <= 74) {
            	addToResults(results,idx,"Age","65-74");
            } else if(age > 75 && age <= 84) {
            	addToResults(results,idx,"Age","75-84");
            } else {
            	addToResults(results,idx,"Age","85 and over");
            }
                   
            //extra age stats
            if(age < minAge || idx==0) {
            	minAge=age;
            }
            
            if(age > maxAge || idx==0) {
            	maxAge=age;
            }
            
            if(age != 0 ) {
            	avgAgeTotal+=age;
            	avgAgeSize++;
            }
            
            //service recipient - hard coded to toronto
            addToResults(results,idx,"Service Recipient Location","Toronto");
            
            //Aboriginal Origin
            String aboriginal = getIntakeAnswer(intake,"Ethno-racial Background");            
            addToResults(results,idx,"Aboriginal Origin",aboriginal);
            
            
            //language
            String languageEnglish = getIntakeAnswer(intake,"First Language English");
            String languageOther = getIntakeAnswer(intake,"If No, Service Recipient Preferred Language");
            String language="";
            if(languageEnglish!=null && languageEnglish.equalsIgnoreCase("yes")) {
            	language="English";
            }
            if(languageOther!=null && languageOther.indexOf("french") != -1) {
            	language="French";
            } else {
            	language="Other";
            }            
            addToResults(results,idx,"Service Recipient Preferred Language",language);
            
            
            //baseline legal
            if(!preAdmission) {
            	String bLegal = getIntakeAnswer(intake,"Baseline Legal Status");           
            	addToResults(results,idx,"Baseline Legal Status",bLegal);
            }
            
            //current legal
            if(!preAdmission) {
            	String cLegal = getIntakeAnswer(intake,"Current Legal Status");            
            	addToResults(results,idx,"Current Legal Status",cLegal);
            }
            
            //CTO
            if(!preAdmission) {
            	String cto = getIntakeAnswer(intake,"Community Treatment Orders");            
            	addToResults(results,idx,"Community Treatment Orders",cto);
            }
            
            //primary diagnosis
            String primaryDiagnosis = getIntakeAnswer(intake,"Primary Diagnosis");
            addToResults(results,idx,"Diagnostic Categories",primaryDiagnosis);
            
            //other illness
            String otherIllness = getIntakeAnswer(intake,"Other Illness Information");
            addToResults(results,idx,"Other Illness Information",otherIllness);
            
            //presenting issues
            String presentingIssues = getIntakeAnswer(intake,"Presenting Issues");
            addToResults(results,idx,"Presenting Issues",presentingIssues);
            
          	//source of referral
            if(!preAdmission) {
            	String srcOfReferral = getIntakeAnswer(intake,"Source of Referral");
            	if(srcOfReferral!=null && srcOfReferral.equals("Criminal Justice System")) {
            		srcOfReferral = getIntakeAnswer(intake,"If Criminal Justice System");
            	}
            	addToResults(results,idx,"Source of Referral",srcOfReferral);
            }
            
            //exit disposition
            if(!preAdmission) {
            	String exitDisposition = getIntakeAnswer(intake,"Exit Disposition");
            	addToResults(results,idx,"Exit Disposition",exitDisposition);
            }
            
            //hospitalizations
            if(!preAdmission) {
	            List<HospitalizationBean> hospitalizations = new ArrayList<HospitalizationBean>();
	            hospitalizations.add(getHospitalizationInfo(intake,"h1",NODEID_HOSPITALIZATION1_DATE,NODEID_HOSPITALIZATION1_LENGTH,NODEID_HOSPITALIZATION1_PSYCH,NODEID_HOSPITALIZATION1_PHYS,NODEID_HOSPITALIZATION1_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h2",NODEID_HOSPITALIZATION2_DATE,NODEID_HOSPITALIZATION2_LENGTH,NODEID_HOSPITALIZATION2_PSYCH,NODEID_HOSPITALIZATION2_PHYS,NODEID_HOSPITALIZATION2_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h3",NODEID_HOSPITALIZATION3_DATE,NODEID_HOSPITALIZATION3_LENGTH,NODEID_HOSPITALIZATION3_PSYCH,NODEID_HOSPITALIZATION3_PHYS,NODEID_HOSPITALIZATION3_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h4",NODEID_HOSPITALIZATION4_DATE,NODEID_HOSPITALIZATION4_LENGTH,NODEID_HOSPITALIZATION4_PSYCH,NODEID_HOSPITALIZATION4_PHYS,NODEID_HOSPITALIZATION4_DECLINED));
	            hospitalizations.add(getHospitalizationInfo(intake,"h5",NODEID_HOSPITALIZATION5_DATE,NODEID_HOSPITALIZATION5_LENGTH,NODEID_HOSPITALIZATION5_PSYCH,NODEID_HOSPITALIZATION5_PHYS,NODEID_HOSPITALIZATION5_DECLINED));

	            
	            //current psychiatric hospitalizations
	            int numDaysHospitalized=0;
	            int numPsychHospitalizations=0;
	            
	            for(HospitalizationBean hospitalization:hospitalizations) {
	            	try {
	            		if(hospitalization.getPsychiatric().equals("T")) {
	            			numPsychHospitalizations++;
	            			int length = Integer.parseInt(hospitalization.getLength());
	            			numDaysHospitalized += length;
	            		}
	            	} catch(NumberFormatException e) {}
	            }
	            addToResults(results,idx,"Current Psychiatric Hospitalizations","Total Number of Episodes",numPsychHospitalizations);
	            addToResults(results,idx,"Current Psychiatric Hospitalizations","Total Number of Hospitalization Days",numDaysHospitalized);
            }
            
            //baseline living arrangements
            if(!preAdmission) {
            	String bLivingArrangements = getIntakeAnswer(intake,"Baseline Living Arrangement");
            	addToResults(results,idx,"Baseline Living Arrangement",bLivingArrangements);
            }
            
          	//current living arrangements
            if(!preAdmission) {
            	String cLivingArrangements = getIntakeAnswer(intake,"Current Living Arrangement");
            	addToResults(results,idx,"Current Living Arrangement",cLivingArrangements);
            }
            
            //baseline residence type
            if(!preAdmission) {
            	String bResidenceType = getIntakeAnswerByNodeId(intake,"Baseline Primary Residence Type",NODEID_BASELINE_RESIDENCE_TYPE);
            	addToResults(results,idx,"Baseline Primary Residence Type",bResidenceType);
            }
            
            //baseline residence status
            if(!preAdmission) {
            	String bResidenceStatus = getIntakeAnswer(intake,"Baseline Residence Status");
            	addToResults(results,idx,"Baseline Residence Status",bResidenceStatus);
            }
            
            //current residence type
            if(!preAdmission) {
            	String cResidenceType = getIntakeAnswerByNodeId(intake,"Current Primary Residence Type",NODEID_CURRENT_RESIDENCE_TYPE);
            	addToResults(results,idx,"Current Primary Residence Type",cResidenceType);
            }
            
            //current residence status
            if(!preAdmission) {
            	String cResidenceStatus = getIntakeAnswer(intake,"Current Residence Status");
            	addToResults(results,idx,"Current Residence Status",cResidenceStatus);
            }
            
            //baseline employment status
            if(!preAdmission) {
            	String bEmploymentStatus = getIntakeAnswer(intake,"Baseline Employment Status");
            	addToResults(results,idx,"Baseline Employment Status",bEmploymentStatus);
            }
            
          	//current employment status
            if(!preAdmission) {
            	String cEmploymentStatus = getIntakeAnswer(intake,"Current Employment Status");
            	addToResults(results,idx,"Current Employment Status",cEmploymentStatus);
            }
                     
         	//baseline educational status
            if(!preAdmission) {
            	String bEducationalStatus = getIntakeAnswer(intake,"Baseline Educational Status (participating in education at intake)");
            	addToResults(results,idx,"Baseline Educational Status (participating in education at intake)",bEducationalStatus);
            }
            
          	//current educational status
            if(!preAdmission) {
            	String cEducationalStatus = getIntakeAnswer(intake,"Current Educational Status (participating in education at intake)");
            	addToResults(results,idx,"Current Educational Status (participating in education at intake)",cEducationalStatus);
            }
                       
            //highest level of education
            if(!preAdmission) {
            	String highestLevelOfEducation = getIntakeAnswer(intake,"Current Highest Level of Education at Intake");
            	addToResults(results,idx,"Current Highest Level of Education at Intake",highestLevelOfEducation);
            }
            
            //baseline primary income source
            if(!preAdmission) {
            	String bPrimaryIncomeSource = getIntakeAnswer(intake,"Baseline Primary Income Source");
            	addToResults(results,idx,"Baseline Primary Income Source",bPrimaryIncomeSource);
            }
            
            //current primary income source
            if(!preAdmission) {
            	String cPrimaryIncomeSource = getIntakeAnswer(intake,"Current Primary Income Source");
            	addToResults(results,idx,"Current Primary Income Source",cPrimaryIncomeSource);
            }
            
        }
        
        addToResults(results,idx,"Age","Minimum Age (ACT only)",minAge);
        addToResults(results,idx,"Age","Maximum Age (ACT only)",maxAge);
        addToResults(results,idx,"Age","Average Age (ACT only)",(int)((double)avgAgeTotal/(double)avgAgeSize));
    }
    
    public HospitalizationBean getHospitalizationInfo(Intake intake, String label, int date, int length, int psych, int phys, int declined) {
    	HospitalizationBean bean = new HospitalizationBean();
    	bean.setDate(getIntakeAnswerByNodeId(intake, label + " year",date));
    	bean.setLength(getIntakeAnswerByNodeId(intake, label + " length",length));
    	bean.setPsychiatric(getIntakeAnswerByNodeId(intake, label + " psychiatric",psych));
    	bean.setPhysical(getIntakeAnswerByNodeId(intake, label + " physical",phys));
    	bean.setDeclined(getIntakeAnswerByNodeId(intake, label + " declined",declined));    
    	return bean;
    }
     
    /*
     * Creates a set of 10 date ranges.
     * The first one will be yyyy-03-01 to startdate.
     * The next ones will each by 1 yr in length
     */
    List<DateRange> getDates(Date startDate) {
    	List<DateRange> ranges = new ArrayList<DateRange>();
    	
    	//first one is special
    	Date cohort0StartDate=null;
    	Date cohort0EndDate=startDate;
    	String strDate = dateToString(startDate, SDF_PATTERN);

        if (strDate.substring(4).compareTo(COHORT_CRITICAL_YM) > 0) {
            cohort0StartDate = stringToDate(strDate.substring(0, 4) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }
        else {
        	cohort0StartDate = stringToDate(Integer.toString(Integer.parseInt(strDate.substring(0, 4)) - 1) + COHORT_CRITICAL_YM, SDF_PATTERN);
        }
        ranges.add(new DateRange(cohort0StartDate,cohort0EndDate));
        
        
        for(int x=0;x<9;x++) {
        	DateRange lastDateRange = ranges.get(ranges.size()-1);
        	
        	Date eDate = lastDateRange.getStartDate();        	
        	Date sDate = this.addYears(eDate,-1);
        	
        	ranges.add(new DateRange(sDate,eDate));        	
        }
        
    	return ranges;
    	
    }
    
    public void addToResults(Map<StreetHealthReportKey,Integer> results,int cohortIdx, String label, String answer) {
    	log.info("adding to result=" + cohortIdx + "," + label  + "," + answer);
    	StreetHealthReportKey key = new StreetHealthReportKey(cohortIdx,label,answer);
    	if(results.get(key) == null) {
    		results.put(key,1);
    	} else {
    		int x = results.get(key);
    		x++;
    		results.put(key,x);
    	}
    }
    
    public void addToResults(Map<StreetHealthReportKey,Integer> results, int cohortIdx, String label, String answer, int increment) {
    	log.info("adding to result=" + cohortIdx + "," + label  + "," + answer);
    	StreetHealthReportKey key = new StreetHealthReportKey(cohortIdx,label,answer);
    	if(results.get(key) == null) {
    		results.put(key,increment);
    	} else {
    		int x = results.get(key);
    		x+=increment;
    		results.put(key,x);
    	}
    }

    /* DATE FUNCTIONS */
    private Date stringToDate(String date, String pattern){
    	try {
    		return new SimpleDateFormat(pattern).parse(date);
    	}catch(ParseException e) {e.printStackTrace();}
    	return null;
    }
    
    private String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date).toString();
    }
    
    private Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }
    
    
    /* INTAKE FUNCTIONS */
    
    public String getIntakeAnswer(Intake intake, String key) {
    	String value = intake.getAnswerKeyValues().get(key);
    	if(value!=null && value.equals("Declined")) {
    		value="Unknown or Service Recipient Declined";
    	}
    	log.info(key + "=" + value);
    	return value;
    }
    
    public String getIntakeAnswerByNodeId(Intake intake, String key, int nodeId) {
    	IntakeAnswer answer = intake.getAnswerMapped(String.valueOf(nodeId));
    	String value = answer.getValue();
    	log.info(key + "=" + value);
    	return value;
    }
}

