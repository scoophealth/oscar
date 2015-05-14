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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.FlowSheetCustomizationDao;
import org.oscarehr.common.dao.ProviderStudyDao;
import org.oscarehr.common.dao.StudyDao;
import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.oscarehr.common.model.ProviderStudy;
import org.oscarehr.common.model.ProviderStudyPK;
import org.oscarehr.common.model.Study;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementInfo;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBeanHandler;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;

/**
 *
 * @author apavel
 */
public class DSDemographicAccess {
    private static final Logger logger = MiscUtils.getLogger();

    //To add new modules/types, add to enum with the access method, add the appropriate
    //functions for all, any, not, notall, notany (see examples below), and add to
    //getDemogrpahicValues list, that's it.
    public enum Module {
        dxcodes("hasDxCodes"),
        drugs("hasRxCodes"),
        age("isAge"),
        sex("isSex"),
        notes("noteContains"),
        billedFor("billedFor"),
        paid("paid"),
        flowsheet("flowsheetUptoDate"),
        providerInStudy("providerInStudy"),
        studyActive("studyActive"),
        hasATCcode("hasATCcode"),
        demoHasData("demoHasData"),
        hasRxClass("hasRxClass");
        
        
        //define more here....

        private final String accessMethod;
        Module(String accessMethod) {
            this.accessMethod = accessMethod;
        }

        public String getAccessMethod() { return accessMethod; }
    }

    private String demographicNo;
    private String providerNo = null;
    private List<Object> dynamicArgs = null;
    private boolean passedGuideline = false;
    private org.oscarehr.common.model.Demographic demographicData;
    private List<Prescription> prescriptionData;

    private LoggedInInfo loggedInInfo;
    
    private DSDemographicAccess() {
    	
    }
    
    public DSDemographicAccess(LoggedInInfo loggedInInfo, String demographicNo) {
    	this.loggedInInfo = loggedInInfo;
        this.demographicNo = demographicNo;
    }

    public DSDemographicAccess(LoggedInInfo loggedInInfo, String demographicNo, String providerNo) {
        this(loggedInInfo,demographicNo);
        this.providerNo = providerNo;
    }
    
    public DSDemographicAccess(LoggedInInfo loggedInInfo, String demographicNo, String providerNo, List<Object> dynamicArgs) {
        this(loggedInInfo,demographicNo,providerNo);
        this.dynamicArgs = dynamicArgs;
        logger.debug("dynamic args size " + this.dynamicArgs.size());
    }
    
    public boolean hasRxClassNotany(String atcCodes) {
    	List<Prescription>undeletedPrescriptions = this.getPrescriptionData();
    	List<Prescription>prescriptions = new ArrayList<Prescription>();
    	
    	Date now = new Date();
    	Date end_date;
    	for( Prescription prescription : undeletedPrescriptions ) {
    		end_date = prescription.getEndDate();
    		if( end_date != null ) {
    			if( end_date.after(now) ) {
    				prescriptions.add(prescription);
    			}
    		}
    	}
    	
    	boolean notInClass = true;
    	String atcCode;
    	for( Object atcCodeObj : this.dynamicArgs ) {
    		atcCode = (String)atcCodeObj;
    		for( Prescription prescription : prescriptions ) {
    			logger.debug("Comparing " + prescription.getAtcCode() + " to " + atcCode);
    			if( prescription.getAtcCode() != null && prescription.getAtcCode().equals(atcCode)) {
    				notInClass = false;
    				break;
    			}
    		}
    		
    		if( !notInClass ) {
    			break;
    		}
    	}
    	
    	return notInClass;
    }
    
    public boolean hasRxClassAny(String atcCode) { return !hasRxClassNotany(atcCode); }
    public boolean hasRxClassNotall(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean hasRxClassNot(String atcCode)  { return hasRxClassNotany(atcCode); }
    public boolean hasRxClassAll(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    
    public boolean demoHasDataNot(String studyId) {
    	logger.debug("demoHasData called");
    	StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);
    	
    	studyId = studyId.replaceAll("'", "");
    	Study study = studyDao.findByName(studyId);
    	StudyDataDao studyDataDao = (StudyDataDao)SpringUtils.getBean(StudyDataDao.class);
    	List<StudyData> studyDataList = studyDataDao.findByDemoAndStudy(Integer.parseInt(demographicNo), study.getId());
    	
    	boolean isEmpty = true;
    	for( StudyData studyData : studyDataList ) {
    		if( !studyData.getContent().equalsIgnoreCase("Eligible - ask later") ) {
    			isEmpty = false;
    		}
    	}
    	
    	return isEmpty;
    }

    public boolean demoHasDataAny(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean demoHasDataNotall(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean demoHasDataNotany(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean demoHasDataAll(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }

    
    public boolean hasATCcode(DSValue rxCode) {
    	logger.debug("hasATCcode dynamicArgs size " + this.dynamicArgs.size());
    	boolean found = false;
    	String atcCode = "";
    	
    	for( Object obj : this.dynamicArgs ) {
    		atcCode = (String)obj;
    		logger.debug("comparing " + rxCode.getValue() + " with " + atcCode);
    		if( atcCode.startsWith(rxCode.getValue())) {
    			found = true;
    			break;
    		}
    	}
    	
    	
    	return found;
    }
    
    public boolean hasATCcodeAny(String atcCodes) {
    	logger.debug("HASATCCODEANY CALLED");
    	boolean found = false;
    	List<DSValue> testATCcodes = DSValue.createDSValues(atcCodes);
    	for( DSValue testATCcode : testATCcodes ) {
    		if( this.hasATCcode(testATCcode) ) {
    			found = true;
    			break;
    		}
    	}
    	
    	return found;
    }
    
    public boolean hasATCcodeNot(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean hasATCcodeNotall(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean hasATCcodeNotany(String atcCode) { return !this.hasATCcodeAny(atcCode); }
    public boolean hasATCcodeAll(String atcCode) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    
    public boolean studyActiveAll(String strStudyId) {
    	StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);
    	strStudyId = strStudyId.replaceAll("'", "");
    	Study study = studyDao.findByName(strStudyId);
    	
    	return (study.getCurrent1() == 1 );
    }
    
    public boolean StudyActiveNot(String strStudyId) { return !studyActiveAll(strStudyId); }
    public boolean StudyActiveNotall(String strStudyId) { return !studyActiveAll(strStudyId); }
    public boolean StudyActiveNotany(String strStudyId) { return !studyActiveAll(strStudyId); }
    public boolean StudyActiveAny(String strStudyId) { return studyActiveAll(strStudyId); }

    
    public boolean providerInStudyAll(String strStudyId ) {
    	StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);    	
    	ProviderStudyDao providerStudyDao = (ProviderStudyDao)SpringUtils.getBean(ProviderStudyDao.class);
    	strStudyId = strStudyId.replaceAll("'", "");
    	logger.info("Looking up " + strStudyId);
    	Study study = studyDao.findByName(strStudyId);
    	logger.info("STUDY found " + String.valueOf(study != null));
    	ProviderStudyPK providerStudyPK = new ProviderStudyPK();
    	providerStudyPK.setProviderNo(this.providerNo);
    	providerStudyPK.setStudyNo(study.getId());
    	
    	ProviderStudy providerStudy = providerStudyDao.find(providerStudyPK);
    	
    	return (providerStudy != null );
    }
    
    public boolean providerInStudyNot(String strStudyId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean providerInStudyNotall(String strStudyId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean providerInStudyNotany(String strStudyId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean providerInStudyAny(String strStudyId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    
    public List<dxResearchBean> getDxCodes() {
        dxResearchBeanHandler handler = new dxResearchBeanHandler(demographicNo);
        List<dxResearchBean> dxCodes = handler.getDxResearchBeanVector();
        return dxCodes;
    }

    public String getDxCodesStr() {
        List<dxResearchBean> dxCodes = this.getDxCodes();
        String returnStr = "";
        for (dxResearchBean dxCode: dxCodes) {
            returnStr = returnStr + dxCode.getType() + ":" + dxCode.getDxSearchCode() + ",";
        }
        if (returnStr.length() > 1)
            returnStr = returnStr.substring(0, returnStr.length()-1); //remove the trailing comma
        return returnStr;

    }

    public boolean hasDxCode(String codeType, String code) {
    	logger.debug("HAS DX CODES CALLED");
        List<dxResearchBean> dxCodes = this.getDxCodes();
        for (dxResearchBean dxCode: dxCodes) {
            if (dxCode.getDxSearchCode().equals(code) && dxCode.getType().equalsIgnoreCase(codeType)) {
                return true;
        }
        }

        return false;
    }

    public boolean hasDxCodesAny(String dxCodesStr) {
        List<DSValue> testDxCodes = DSValue.createDSValues(dxCodesStr);
        for (DSValue testDxCode: testDxCodes) {
            if (this.hasDxCode(testDxCode.getValueType(), testDxCode.getValue()))
                return true;
        }
        return false;
    }

    public boolean hasDxCodesAll(String dxCodesStr) {
        List<DSValue> testDxCodes = DSValue.createDSValues(dxCodesStr);
        for (DSValue testDxCode: testDxCodes) {
            if (!this.hasDxCode(testDxCode.getValueType(), testDxCode.getValue()))
                return false;
        }
        return true;
    }

    public boolean hasDxCodesNot(String dxCodesStr) { return hasDxCodesNotany(dxCodesStr); }

    public boolean hasDxCodesNotall(String dxCodesStr) { return !hasDxCodesAll(dxCodesStr); }

    public boolean hasDxCodesNotany(String dxCodesStr) { return !hasDxCodesAny(dxCodesStr); }


    public List<Prescription> getRxCodes() {
    	logger.debug("GET RX CODES CALLED");
        try {
            Prescription[] prescriptions = new RxPrescriptionData().getActivePrescriptionsByPatient(Integer.parseInt(this.demographicNo));
            List<Prescription> prescribedDrugs = Arrays.asList(prescriptions);
            return prescribedDrugs;
        } catch (NumberFormatException nfe) {
        	logger.error("Decision Support Exception, could not format demographicNo: " + demographicNo);
        }
        return new ArrayList<Prescription>();
    }

    //generally for testing
    public String getRxCodesStr() throws DecisionSupportException {
        String returnStr = "";
        try {
            for (Prescription prescription: getPrescriptionData()) {
                returnStr = returnStr + "atc:" + prescription.getAtcCode() + ",";
            }
            if (returnStr.length() > 1)
                returnStr = returnStr.substring(0, returnStr.length()-1);  //stop the last comma
            return returnStr;
        } catch (Exception e) {
            throw new DecisionSupportException("Cannot get dugs for patient", e);
        }
    }

    public boolean hasRxCode(DSValue rxCode) throws DecisionSupportException {
        String codeType = rxCode.getValueType();
        if (codeType == null) codeType = "atc";
        try {
            for (Prescription prescription: getPrescriptionData()) {
                if (codeType.equalsIgnoreCase("atc") && rxCode.testValue(prescription.getAtcCode())) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new DecisionSupportException("Cannot get prescription data for this patient", e);
        }
        return false;
    }

    public boolean hasRxCodesAny(String rxCodesStr) throws DecisionSupportException {
        List<DSValue> testRxCodes = DSValue.createDSValues(rxCodesStr);
        for (DSValue testRxCode: testRxCodes) {
            if (this.hasRxCode(testRxCode))
                return true;
        }
        return false;
    }

    public boolean hasRxCodesAll(String rxCodesStr) throws DecisionSupportException {
        List<DSValue> testRxCodes = DSValue.createDSValues(rxCodesStr);
        for (DSValue testRxCode: testRxCodes) {
            if (!this.hasRxCode(testRxCode))
                return false;
        }
        return true;
    }

    public boolean hasRxCodesNot(String rxCodesStr) throws DecisionSupportException { return hasRxCodesNotany(rxCodesStr); }

    public boolean hasRxCodesNotall(String rxCodesStr) throws DecisionSupportException { return !hasRxCodesAll(rxCodesStr); }

    public boolean hasRxCodesNotany(String rxCodesStr) throws DecisionSupportException { return !hasRxCodesAny(rxCodesStr); }

    //not used by isAge
    public String getAge() {
        return getDemographicData(loggedInInfo).getAge() + " y";
    }

    public boolean isAge(DSValue statement) throws DecisionSupportException {
    	logger.debug("IS AGE CALLED");
        String compareAge = getDemographicData(loggedInInfo).getAgeInYears() + "";
        if (statement.getValueUnit() != null) {
            if (statement.getValueUnit().equals("y")){/*empty*/}
            else if (statement.getValueUnit().equals("m")) compareAge = DemographicData.getAgeInMonths(getDemographicData(loggedInInfo)) + "";
            else if (statement.getValueUnit().equals("d")) compareAge = DemographicData.getAgeInDays(getDemographicData(loggedInInfo)) + "";
            else throw new DecisionSupportException("Cannot recognize unit: " + statement.getValueUnit());
        }
        return statement.testValue(compareAge);
    }

    public long getAgeDays() {
        return DemographicData.getAgeInDays(getDemographicData(loggedInInfo));
    }

    public boolean isAgeAny(String ageStatements) throws DecisionSupportException {
        List<DSValue> statements =  DSValue.createDSValues(ageStatements);
        for (DSValue statement: statements) {
            if (isAge(statement)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAgeAll(String ageStatements) throws DecisionSupportException {
        List<DSValue> statements =  DSValue.createDSValues(ageStatements);
        for (DSValue statement: statements) {
            if (!isAge(statement)) {
                return false;
            }
        }
        return true;
    }

    //ageStatement: ">2y"
    public boolean isAgeNot( String ageStatement) throws DecisionSupportException { return isAgeNotany(ageStatement); }

    public boolean isAgeNotall(String ageStatement) throws DecisionSupportException { return !isAgeAll(ageStatement); }

    public boolean isAgeNotany( String ageStatement) throws DecisionSupportException { return !isAgeAny(ageStatement); }

    public String getSex() {
        return getDemographicData(loggedInInfo).getSex();
    }

    public boolean isSex(DSValue sexStatement) throws DecisionSupportException {
    	logger.debug("IS SEX CALLED");
        if (sexStatement.getValue().equalsIgnoreCase("male")) sexStatement.setValue("M");
        else if (sexStatement.getValue().equalsIgnoreCase("female")) sexStatement.setValue("F");
        return sexStatement.testValue(this.getSex());
    }

    public boolean isSexAny(String sexStatements) throws DecisionSupportException {
        List<DSValue> statements =  DSValue.createDSValues(sexStatements);
        for (DSValue statement: statements) {
            if (isSex(statement)) {
                return true;
            }
        }
        return false;
    }


    //makes no sense, but for consistency...
    public boolean isSexAll(String sexStatements) throws DecisionSupportException {
        List<DSValue> statements =  DSValue.createDSValues(sexStatements);
        for (DSValue statement: statements) {
            if (isSex(statement)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSexNot(String sexStatement) throws DecisionSupportException { return isSexNotany(sexStatement); }

    public boolean isSexNotall(String sexStatement) throws DecisionSupportException { return !isSexAll(sexStatement); }

    public boolean isSexNotany(String sexStatement) throws DecisionSupportException { return !isSexAny(sexStatement); }


     public boolean noteContains(DSValue searchValue) {
        CaseManagementNoteDAO dao = (CaseManagementNoteDAO) SpringUtils.getBean("CaseManagementNoteDAO");
        List<CaseManagementNote> notes = dao.searchDemographicNotes(demographicNo, "%" + searchValue.getValue() + "%");
        if (notes != null && notes.size() > 0) return true;
        else return false;
    }

    public boolean noteContainsAny(String searchStrings) {
        List<DSValue> searchValues = DSValue.createDSValues(searchStrings);
        for (DSValue searchValue: searchValues) {
            if (noteContains(searchValue)) return true;
        }
        return false;
    }

    public boolean noteContainsAll(String searchStrings) {
        List<DSValue> searchValues = DSValue.createDSValues(searchStrings);
        for (DSValue searchValue: searchValues) {
            if (!noteContains(searchValue)) return false;
        }
        return true;
    }

    public boolean noteContainsNot(String searchStrings) { return noteContainsNotany(searchStrings); }

    public boolean noteContainsNotall(String searchStrings) { return !noteContainsAll(searchStrings); }

    public boolean noteContainsNotany(String searchStrings) { return !noteContainsAny(searchStrings); }

    @SuppressWarnings("unchecked")
    public boolean flowsheetUptoDateAny(String flowsheetId) {
    	boolean retval = false;
    	flowsheetId = flowsheetId.replaceAll("'", "");
    	FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) SpringUtils.getBean("flowSheetCustomizationDao");

    	dxResearchBeanHandler dxRes = new dxResearchBeanHandler(demographicNo);
        List<String> dxCodes = dxRes.getActiveCodeListWithCodingSystem();
        MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
        ArrayList<String> flowsheets = templateConfig.getFlowsheetsFromDxCodes(dxCodes);

        boolean hasFlowSheet = false;
        for( int idx = 0; idx < flowsheets.size(); ++idx ) {
        	if( flowsheets.get(idx).equals(flowsheetId) ) {
        		hasFlowSheet = true;
        		break;
        	}
        }

    	if(hasFlowSheet) {

			List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheetId,providerNo,Integer.parseInt(demographicNo));

	        MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(flowsheetId,custList);

	        MeasurementInfo mi = new MeasurementInfo(demographicNo);
	        List<String> measurementLs = mFlowsheet.getMeasurementList();

	        mi.getMeasurements(measurementLs);
	        try{
	        	mFlowsheet.getMessages(mi);
	        }catch(Exception e){
	        	MiscUtils.getLogger().error("Error getting messages for flowsheet ",e);
	        }

	        ArrayList<String> warnings = mi.getWarnings();
	        if( warnings.size() == 0 ) {
	        	retval = true;
	        }

    	}

    	return retval;
    }

    public boolean flowsheetUptoDateAll(String flowsheetId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean flowsheetUptoDateNot(String flowsheetId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean flowsheetUptoDateNotall(String flowsheetId) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean flowsheetUptoDateNotany(String flowsheetId) { return !flowsheetUptoDateAny(flowsheetId); };

    public boolean paidAny(String searchStrings, Map<String,String> options ) {

    	boolean retval = true;  //Set this optimistically that it has not been paid in the said number of days
    	if(options.containsKey("payer") && options.get("payer").equals("MSP")){
    		BillingONCHeader1Dao billingONCHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean("billingONCHeader1Dao");
    		String[] codes = searchStrings.replaceAll("'","" ).split(",");

    		if(options.containsKey("notInDays")){
                int notInDays = getAsInt(options,"notInDays");
                int numDays = -1;
                for (String code: codes){
                    //This returns how many days since the last time this code was paid and -1 if it never has been settled
                    numDays = billingONCHeader1Dao.getDaysSincePaid(code, Integer.parseInt(demographicNo));

                    //If any of the codes has been paid in the number of days then return false
                    if (numDays < notInDays && numDays != -1){
                        retval = false;
                        break;
                    }
                    else {
                    	//if no paid bills in last number of days check to see if it has been billed within last 2 months and waits to be settled
                    	numDays = billingONCHeader1Dao.getDaysSinceBilled(code, Integer.parseInt(demographicNo));

                    	if( numDays < 60 && numDays != -1 ) {
                    		retval = false;
                    		break;
                    	}

                    }

                    logger.debug("PAYER:MSP demo "+demographicNo+" Code:"+code+" numDays"+numDays+" notInDays:"+notInDays+ " Answer: "+!(numDays < notInDays && numDays != -1)+" Setting return val to :"+retval);
                }


    		}
    	}

    	return retval;
    }

    public boolean paidAll(String searchStrings,Map options) {

        int countPaid = 0;
        int numCodes = 0;

	if(options.containsKey("payer") && options.get("payer").equals("MSP")){

            BillingONCHeader1Dao billingONCHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean("billingONCHeader1Dao");
            String[] codes = searchStrings.replaceAll("'","" ).split(",");
            numCodes = codes.length;

            if(options.containsKey("inDays")){
                int inDays = getAsInt(options,"inDays");

                for (String code: codes){
                    //This returns how many days since the last time this code was paid and -1 if it never has been settled
                    int numDaysSinceSettled = billingONCHeader1Dao.getDaysSincePaid(code, Integer.parseInt(demographicNo));
                    int numDaysSinceBilled =  billingONCHeader1Dao.getDaysSinceBilled(code, Integer.parseInt(demographicNo));

                    if  (((numDaysSinceSettled <= inDays) && (numDaysSinceSettled != -1))
                      ||((numDaysSinceBilled <= inDays) && (numDaysSinceBilled != -1))){
                        countPaid++;
                    }
                    logger.debug("PAYER:MSP demo "+demographicNo+" Code:"+code+" numDaysSinceSettled "+numDaysSinceSettled+"  numDaysSinceBilled "+numDaysSinceBilled+" inDays:"+inDays+ " Answer: "+((countPaid > 0) && (countPaid == numCodes))+" Setting number paid to :"+countPaid);
                }
            }
        }

	return ((countPaid > 0) && (countPaid == numCodes));
    }
    public boolean paidNot(String searchStrings,Map options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean paidNotall(String searchStrings,Map options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean paidNotany(String searchStrings,Map options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }

    /////New Billing Functionality
    //Days since last billed
//    public boolean billedFor(String searchString,Hashtable options) throws DecisionSupportException {

//        return true;
//    }

    //Look for any of the billing codes that have been billed for this patient
    //Options:  notInDays=999              limit to the number of days to check for this code
    //          notInCalendarYear=true
    //          unitsBilledToday=<4
    //          requiresStartTime=true     not implemented yet.
    public boolean billedForAny(String searchStrings,Hashtable<String,String> options) {
        boolean retval = false;
        if(options.containsKey("payer") && options.get("payer").equals("MSP")){
        	logger.debug("PAYER:MSP ");
            ServiceCodeValidationLogic bcCodeValidation = null;
            BillingONCHeader1Dao billingONCHeader1Dao = null;
            String billregion = OscarProperties.getInstance().getProperty("billregion", "");
            if( billregion.equalsIgnoreCase("BC") ) {
                bcCodeValidation = new ServiceCodeValidationLogic();
            }
            else if( billregion.equalsIgnoreCase("ON") ) {
                billingONCHeader1Dao = (BillingONCHeader1Dao)SpringUtils.getBean("billingONCHeader1Dao");
            }
            String[] codes = searchStrings.replaceAll("\'","" ).split(",");


            /* Has any of these codes been billed for in the past number of days.   ( if any
             *
             * If:
                icd9 code 250 is in the disease registry
                AND
                icd9 code 401 is not in the disease registry
                AND
                Any of the billing codes (13050,14050,14051,14052) Payer MSP have not been billed in the past 365 days.
             */
            if(options.containsKey("notInDays")){
                int notInDays = getAsInt(options,"notInDays");
                retval = true;  //Set this optimistically that it has not been billed in the said number of days
                int numDays = -1;
                for (String code: codes){
                    //This returns how many days since the last time this code was billed and -1 if it never has been billed
                     if( billregion.equalsIgnoreCase("BC") ) {
                        numDays = bcCodeValidation.daysSinceCodeLastBilled(demographicNo,code) ;
                     }
                     else if( billregion.equalsIgnoreCase("ON") ) {
                         numDays = billingONCHeader1Dao.getDaysSinceBilled(code, Integer.parseInt(demographicNo));
                     }

                    //If any of the codes has been billed in the number of days then return false
                    if (numDays < notInDays && numDays != -1){
                        retval = false;  // should it just return false here,  why go on once it finds a false?
                    }
                    logger.debug("PAYER:MSP demo "+demographicNo+" Code:"+code+" numDays"+numDays+" notInDays:"+notInDays+ " Answer: "+!(numDays < notInDays && numDays != -1)+" Setting return val to :"+retval);

                }

            }
        }

        logger.debug("In Billed For Any  look for "+searchStrings+" returning :"+retval);
        return retval;
    }

     public boolean billedForAny2(String searchStrings,Hashtable<String,String> options) {
        boolean retval = false;
        String[] codes = searchStrings.replaceAll("\'","" ).split(",");
        for (String code: codes){

            if(options.containsKey("payer") && options.get("payer").equals("MSP")){
                ServiceCodeValidationLogic bcCodeValidation = new ServiceCodeValidationLogic();
                if(options.containsKey("notInDays")){
                    int notInDays = getAsInt(options,"notInDays");
                    int numDays = bcCodeValidation.daysSinceCodeLastBilled(demographicNo,code) ;
                    if (numDays > notInDays || numDays == -1){
                        retval = true;
                    }
                }
//                if (options.containsKey("notInCalendarYear")){
//
//                }
            }


        }

        logger.debug("In Billed For Any  look for "+searchStrings);
        return retval;
    }

    public int getAsInt(Map options,String key){
        String str = (String) options.get(key);
        int intval = Integer.parseInt(str);
        return intval;
    }

    public boolean billedForAll(String searchStrings,Hashtable options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean billedForNot(String searchStrings,Hashtable options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean billedForNotall(String searchStrings,Hashtable options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean billedForNotany(String searchStrings,Hashtable options) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }


    public boolean billedForAny(String searchStrings) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean billedForAll(String searchStrings) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean billedForNot(String searchStrings) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED"); }
    public boolean billedForNotall(String searchStrings) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }
    public boolean billedForNotany(String searchStrings) throws DecisionSupportException { throw new DecisionSupportException("NOT IMPLEMENTED");  }

    //number of units billed this year
    //number of units billed this calendar year
    //number of units billed today







    //for testing purposes mostly (used to list patient values in echart
    public String getDemogrpahicValues(Module module) {
        try {
            if (module == Module.dxcodes) return this.getDxCodesStr();
            if (module == Module.drugs) return this.getRxCodesStr();
            if (module == Module.age) return this.getAge();
            if (module == Module.sex) return this.getSex();
            if (module == Module.notes) return "";
        } catch (Exception dse) {
            logger.error("Cannot get demographic data for decision support, module: '" + module + "'", dse);
            return null;
        }
        logger.error("Decision Support Display Error: Cannot get text for module: " + module);
        return null;
    }

    public String getDemographicNo() {
        return demographicNo;
    }

    /**
     * @return the passedGuideline
     */
    public boolean isPassedGuideline() {
        return passedGuideline;
    }

    /**
     * @param passedGuideline the passedGuideline to set
     */
    public void setPassedGuideline(boolean passedGuideline) {
        this.passedGuideline = passedGuideline;
    }

    /**
     * @return the demographicData
     */
    public org.oscarehr.common.model.Demographic getDemographicData(LoggedInInfo loggedInInfo) {
        if (this.demographicData == null) {
            this.demographicData = new DemographicData().getDemographic(loggedInInfo, demographicNo);
        }
        return demographicData;
    }

    /**
     * @param demographicData the demographicData to set
     */
    public void setDemographicData(org.oscarehr.common.model.Demographic demographicData) {
        this.demographicData = demographicData;
    }

    /**
 * @return the prescriptionData
     */
    public List<Prescription> getPrescriptionData() {
        if (this.prescriptionData == null)
            setPrescriptionData(this.getRxCodes());
        return prescriptionData;
    }

    /**
     * @param prescriptionData the prescriptionData to set
     */
    public void setPrescriptionData(List<Prescription> prescriptionData) {
        this.prescriptionData = prescriptionData;
    }
}
