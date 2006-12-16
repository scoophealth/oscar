package org.oscarehr.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.IntakeADao;
import org.oscarehr.PMmodule.model.Formintakea;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IntakeADaoHibernate  extends HibernateDaoSupport implements IntakeADao  {
	 private Log log = LogFactory.getLog(IntakeADaoHibernate.class);

	 public Formintakea getCurrIntakeAByDemographicNo(String demographicNo) {
		 Formintakea result = null;

		 if(demographicNo == null  ||  demographicNo.length() <= 0) {		
			 throw new IllegalArgumentException();				
		 }
		 
		 String queryStr = " FROM Formintakea fa WHERE  fa.DemographicNo=? ORDER  BY  fa.FormEdited  DESC  LIMIT 0,1 ";			
		 List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo});
				
		 if(!rs.isEmpty()) {
			 result =  (Formintakea )rs.get(0);		
		 }
		 
		 if(log.isDebugEnabled()) {
			 log.debug("getCurrIntakeAByDemographicNo: demographicNo=" + demographicNo + ",found=" + (result!=null)) ;
		 }
		
		return result;	
	 }
	
	 public List getIntakeAByTimePeriod(String startDate, String endDate) {
		if( startDate == null  || endDate == null ) {
			throw new IllegalArgumentException();
		}
	
		String queryStr = " FROM Formintakea fa WHERE  fa.FormEdited  BETWEEN ? AND ? " +
		" ORDER  BY  fa.DemographicNo  ASC, fa.FormEdited  DESC "; 
		
		List rs = getHibernateTemplate().find(queryStr, new Object[]{startDate, endDate});
		
		if(!rs.isEmpty()) {						
			return  rs;
		}
		
		return null;
	}
	

	 public List getIntakeAs() {		
		 List rs = getHibernateTemplate().find(" FROM Formintakea fa ORDER BY  fa.DemographicNo ");
		 
		 if(log.isDebugEnabled()) {
			 log.debug("getIntakeAs: # of results=" + rs.size());
		 }
		 return rs;	
	 }
	
	 public Formintakea setNewIntakeAObj(Formintakea fa) {
	    fa.setClientFirstName("");
	    fa.setClientSurname("");
	    fa.setNamesOfMedication("");
	    fa.setRadioHasdoctor("");
	    fa.setHealthIssueDetails("");
	    fa.setCboxOdsp("");
	    fa.setCboxFortyork("");
	    fa.setCboxIdnone("");
	    fa.setReactionToMedication("");
	    fa.setSeatonNotToured("");
	    fa.setRadioOtherhealthconcern("");
	    fa.setCboxOneillhouse("");
	    fa.setContactName("");
	    fa.setRadioDidnotreceivehealthcare("");
	    fa.setCboxTreatphysicalhealth("");
	    fa.setCboxIdfiled("");
	    fa.setCboxTreatinjury("");
	    fa.setCboxOas("");
	    fa.setCboxOtherincome("");
	    fa.setCboxNewclient("");
	    fa.setRadioSeedoctor("");
	    fa.setRadioMentalhealthconcerns("");
	    fa.setRadioObjecttoregulardoctorin4wks("");
	    fa.setHelpObtainMedication("");
	    fa.setSummary("");
	    fa.setDay("");
	    fa.setCboxSpeakfrench("");
	    fa.setDoctorPhone("");
	    fa.setRadioHasdrinkingproblem("");
	    fa.setCboxDownsviewdells("");
	    fa.setCboxLongtermprogram("");
	    fa.setCboxGotoothers("");
	    fa.setSpeakOther("");
	    fa.setLastIssueDate("");
	    fa.setRadioHasbehaviorproblem("");
	    fa.setRadioNeedregulardoctor("");
	    fa.setGoToOthers("");
	    fa.setRadioHelpobtainmedication("");
	    fa.setCboxAssistinhealth("");
	    fa.setCboxGotowalkinclinic("");
	    fa.setCboxNoid("");
	    fa.setCboxPamphletissued("");
	    fa.setCboxNorecord("");
	    fa.setCboxOw("");
	    fa.setAssessCompleteTime("");
	    fa.setAssessDate("");
	    fa.setCboxHearingimpair("");
	    fa.setCboxVisualimpair("");
	    fa.setCboxBirchmountresidence("");
	    fa.setCboxEi("");
	    fa.setRadioHasdrugproblem("");
	    fa.setCboxVisithealthcenter("");
	    fa.setCboxRotaryclub("");
	    fa.setCboxHealthcard("");
	    fa.setTreatOtherReasons("");
	    fa.setMobilityImpair("");
	    fa.setRadioSeatontour("");
	    fa.setRadioHealthissue("");
	    fa.setCboxAssistinhousing("");
	    fa.setAmtReceived("");
	    fa.setCboxVisithealthoffice("");
	    fa.setCompletedBy("");
	    fa.setContactPhone("");
	    fa.setCboxImmigrant("");
	    fa.setCboxDateofreadmission("");
	    
	    fa.setCboxIsstatementread("");
	
	    fa.setCboxTreatotherreasons("");
	    fa.setCommentsOnID("");
	    fa.setCboxVisitothers("");
	    fa.setAssessStartTime("");
	    fa.setCboxInsulin("");
	    fa.setRadioSeesamedoctor("");
	    fa.setRadioSpeaktoresearcher("");
	    fa.setCboxSpeakenglish("");
	    fa.setCboxAssistinidentification("");
	    fa.setContactRelationship("");
	    fa.setCboxTreatmentalhealth("");
	    fa.setDatesAtSeaton("");
	    fa.setRadioRateoverallhealth("");
	    fa.setHealthCardNum("");
	    fa.setHealthCardVer("");
	
	    fa.setOffice("");
	    fa.setRadioNeedseatonservice("");
	    fa.setCboxBleeding("");
	    fa.setCboxRegularcheckup("");
	    fa.setWorkerNum("");
	    fa.setCboxHasdiabetes("");
	    fa.setCboxVisitwalkinclinic("");
	    fa.setMonth("");
	    fa.setRadioAllergictomedication("");
	    fa.setCboxCpp("");
	    fa.setFrequencyOfSeeingDoctor("");
	    fa.setCboxOtherid("");
	    fa.setCboxAssistineducation("");
	    fa.setCboxAnnexharm("");
	    fa.setCboxVisitemergencyroom("");
	    fa.setCboxMobilityimpair("");
	    fa.setCboxHealthoffice("");
	    fa.setCboxEpilepsy("");
	    fa.setRadioPamphletissued("");
	    fa.setCboxCitzenshipcard("");
	    fa.setCboxAssistinaddictions("");
	    fa.setCboxAssistinfinance("");
	    fa.setRadioActive("");
	    fa.setCboxEmployment("");
	    fa.setMentalHealthConcerns("");
	    fa.setRadioTakemedication("");
	    fa.setCboxHostel("");
	    fa.setDoctorAddress("");
	    fa.setDoctorName("");
	    fa.setCboxGotohealthcenter("");
	    fa.setRadioAppmtseedoctorin3mths("");
	    fa.setCboxAssistinlegal("");
	    fa.setCboxSincard("");
	    fa.setRadioHasmentalillness("");
	    fa.setOtherIdentification("");
	    fa.setCboxBirthcertificate("");
	    fa.setCboxAssistinimmigration("");
	    fa.setContactAddress("");
	    fa.setCboxSpeakother("");
	    fa.setPamphletNotIssued("");
	    fa.setRadioHashealthproblem("");
	    fa.setDateOfReadmission("");
	    fa.setRadioOnlinecheck("");
	    fa.setAllergicToMedicationName("");
	    fa.setCboxGotoemergencyroom("");
	    fa.setFrequencyOfSeeingEmergencyRoomDoctor("");
	    fa.setCboxWsib("");
	    fa.setCboxRefugee("");
	    fa.setDoctorPhoneExt("");
	    fa.setYear("");
	    fa.setCboxAssistinemployment("");
	    fa.setOtherHealthConerns("");
	    fa.setEnterSeatonDate("");
	    fa.setReasonToSeaton("");
	
	    fa.setSinNum("");
	    fa.setBirthCertificateNum("");
	    fa.setCitzenshipCardNum("");
	    fa.setImmigrantNum("");
	    fa.setRefugeeNum("");
	    fa.setCboxIsstatement6read("");
	    
	    fa.setCboxSharing("");
	    return fa;
	}
		
	public boolean addIntakeA(Formintakea intakeA) {
		boolean result = true;
	    if(intakeA == null) {
	    	throw new IllegalArgumentException();
	    }
	    
	    try {
	    	getHibernateTemplate().save(intakeA);
	    }
	    catch(Exception ex)
	    {
	    	result =  false;
	    }
	    
	    if(log.isDebugEnabled()) {
	    	log.debug("addIntakeA: " + intakeA.getId() + ",result=" + result);
	    }
	    
	    return result;
	}
	
	public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType) {
		boolean result = true;
	    if( intakeA == null  ||  newDemographicNo == null  ||  newDemographicNo.length() <= 0  ||
	       (intakeA.getClientFirstName().length() <= 0  &&  intakeA.getClientSurname().length() <= 0) )
	    {
	    	throw new IllegalArgumentException();
	    }
	    
	    intakeA.setDemographicNo(Long.valueOf(newDemographicNo));
	    
	    try {
	    	getHibernateTemplate().save(intakeA);
	    } catch(Exception ex) {
	    	result =  false;
	    }
	    
	    if(log.isDebugEnabled()) {
	    	log.debug("addNewClientToIntakeA: result=" + result);
	    }
	    return result;
	}
	
	
	public void saveForm(Formintakea form) {
		this.getHibernateTemplate().save(form);
		
		if(log.isDebugEnabled()) {
			log.debug("saveForm:" + form.getId());
		}
	}
}
