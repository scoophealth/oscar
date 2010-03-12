/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model;

import java.util.ArrayList;
import org.oscarehr.decisionSupport.model.conditionValue.DSValue;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.decisionSupport.web.DSGuidelineAction;
import org.oscarehr.util.SpringUtils;
import oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBean;
import oscar.oscarResearch.oscarDxResearch.bean.dxResearchBeanHandler;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;

/**
 *
 * @author apavel
 */
public class DSDemographicAccess {
    private static Log _log = LogFactory.getLog(DSGuidelineAction.class);

    //To add new modules/types, add to enum with the access method, add the appropriate
    //functions for all, any, not, notall, notany (see examples below), and add to
    //getDemogrpahicValues list, that's it.
    public enum Module {
        dxcodes("hasDxCodes"),
        drugs("hasRxCodes"),
        age("isAge"),
        sex("isSex"),
        notes("noteContains"),
        billedFor("billedFor");
        //define more here....

        private final String accessMethod;
        Module(String accessMethod) {
            this.accessMethod = accessMethod;
        }

        public String getAccessMethod() { return accessMethod; }
    }

    private String demographicNo;
    private boolean passedGuideline = false;
    private DemographicData.Demographic demographicData;
    private List<Prescription> prescriptionData;

    public DSDemographicAccess(String demographicNo) {
        this.demographicNo = demographicNo;
    }

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
        _log.debug("HAS DX CODES CALLED");
        List<dxResearchBean> dxCodes = this.getDxCodes();
        for (dxResearchBean dxCode: dxCodes) {
            if (dxCode.getDxSearchCode().equals(code) && dxCode.getType().equalsIgnoreCase(codeType))
                return true;
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


    public List<Prescription> getRxCodes() throws DecisionSupportException {
        _log.debug("GET RX CODES CALLED");
        try {
            Prescription[] prescriptions = new RxPrescriptionData().getPrescriptionsByPatientHideDeleted(Integer.parseInt(this.demographicNo));
            List<Prescription> prescribedDrugs = Arrays.asList(prescriptions);
            return prescribedDrugs;
        } catch (NumberFormatException nfe) {
            _log.error("Decision Support Exception, could not format demographicNo: " + demographicNo);
        }
        return new ArrayList();
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
        return getDemographicData().getAge() + " y";
    }

    public boolean isAge(DSValue statement) throws DecisionSupportException {
        _log.debug("IS AGE CALLED");
        String compareAge = getDemographicData().getAgeInYears() + "";
        if (statement.getValueUnit() != null) {
            if (statement.getValueUnit().equals("y")) ;
            else if (statement.getValueUnit().equals("m")) compareAge = getDemographicData().getAgeInMonths() + "";
            else if (statement.getValueUnit().equals("d")) compareAge = getDemographicData().getAgeInDays() + "";
            else throw new DecisionSupportException("Cannot recognize unit: " + statement.getValueUnit());
        }
        return statement.testValue(compareAge);
    }

    public long getAgeDays() {
        return getDemographicData().getAgeInDays();
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
    public boolean isAgeNot(String ageStatement) throws DecisionSupportException { return isAgeNotany(ageStatement); }

    public boolean isAgeNotall(String ageStatement) throws DecisionSupportException { return !isAgeAll(ageStatement); }

    public boolean isAgeNotany(String ageStatement) throws DecisionSupportException { return !isAgeAny(ageStatement); }

    public String getSex() {
        return getDemographicData().getSex();
    }

    public boolean isSex(DSValue sexStatement) throws DecisionSupportException {
        _log.debug("IS SEX CALLED");
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

    
     public boolean noteContains(DSValue searchValue) throws DecisionSupportException {
        CaseManagementNoteDAO dao = (CaseManagementNoteDAO) SpringUtils.getBean("CaseManagementNoteDAO");
        List<CaseManagementNote> notes = dao.searchDemographicNotes(demographicNo, "%" + searchValue.getValue() + "%");
        if (notes != null && notes.size() > 0) return true;
        else return false;
    }

    public boolean noteContainsAny(String searchStrings) throws DecisionSupportException {
        List<DSValue> searchValues = DSValue.createDSValues(searchStrings);
        for (DSValue searchValue: searchValues) {
            if (noteContains(searchValue)) return true;
        }
        return false;
    }

    public boolean noteContainsAll(String searchStrings) throws DecisionSupportException {
        List<DSValue> searchValues = DSValue.createDSValues(searchStrings);
        for (DSValue searchValue: searchValues) {
            if (!noteContains(searchValue)) return false;
        }
        return true;
    }

    public boolean noteContainsNot(String searchStrings) throws DecisionSupportException { return noteContainsNotany(searchStrings); }

    public boolean noteContainsNotall(String searchStrings) throws DecisionSupportException { return !noteContainsAll(searchStrings); }

    public boolean noteContainsNotany(String searchStrings) throws DecisionSupportException { return !noteContainsAny(searchStrings); }


    /////New Billing Functionality
    //Days since last billed
//    public boolean billedFor(String searchString,Hashtable options) throws DecisionSupportException {
//        System.out.println("billedFor");
//        return true;
//    }

    //Look for any of the billing codes that have been billed for this patient
    //Options:  notInDays=999              limit to the number of days to check for this code
    //          notInCalendarYear=true
    //          unitsBilledToday=<4
    //          requiresStartTime=true     not implemented yet.
    public boolean billedForAny(String searchStrings,Hashtable options) throws DecisionSupportException {
        boolean retval = false;
        if(options.containsKey("payer") && options.get("payer").equals("MSP")){
            _log.debug("PAYER:MSP ");
            ServiceCodeValidationLogic bcCodeValidation = new ServiceCodeValidationLogic();
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
                for (String code: codes){
                    //This returns how many days since the last time this code was billed and -1 if it never has been billed
                    int numDays = bcCodeValidation.daysSinceCodeLastBilled(demographicNo,code) ;

                    //If any of the codes has been billed in the number of days then return false
                    if (numDays < notInDays && numDays != -1){
                        retval = false;  // should it just return false here,  why go on once it finds a false?
                    }
                    _log.debug("PAYER:MSP demo "+demographicNo+" Code:"+code+" numDays"+numDays+" notInDays:"+notInDays+ " Answer: "+!(numDays < notInDays && numDays != -1)+" Setting return val to :"+retval);

                }

            }
        }

        _log.debug("In Billed For Any  look for "+searchStrings+" returning :"+retval);
        return retval;
    }

     public boolean billedForAny2(String searchStrings,Hashtable options) throws DecisionSupportException {
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

        _log.debug("In Billed For Any  look for "+searchStrings);
        return retval;
    }

    public int getAsInt(Hashtable options,String key){
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
            _log.error("Cannot get demographic data for decision support, module: '" + module + "'", dse);
            return null;
        }
        _log.error("Decision Support Display Error: Cannot get text for module: " + module);
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
    public DemographicData.Demographic getDemographicData() {
        if (this.demographicData == null) {
            this.demographicData = new DemographicData().getDemographic(demographicNo);
        }
        return demographicData;
    }

    /**
     * @param demographicData the demographicData to set
     */
    public void setDemographicData(DemographicData.Demographic demographicData) {
        this.demographicData = demographicData;
    }

    /**
 * @return the prescriptionData
     */
    public List<Prescription> getPrescriptionData() throws Exception{
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
