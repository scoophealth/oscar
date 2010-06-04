package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.util.Date;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.util.BuildInfo;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.REF_I12;
import ca.uhn.hl7v2.model.v25.segment.RF1;

public final class RefI12 {
	private enum REF_NTE_TYPE
	{
		APPOINTMENT_NOTES,
		REASON_FOR_CONSULTATION,
		CLINICAL_INFORMATION,
		CONCURRENT_PROBLEMS,
		CURRENT_MEDICATIONS,
		ALLERGIES
	}
	
	public static REF_I12 makeRefI12(String facilityName, ConsultationRequest consultationRequest, StreetAddressDataHolder referringOfficeStreetAddress) throws HL7Exception
	{
		REF_I12 referralMsg=new REF_I12();

		DataTypeUtils.fillMsh(referralMsg.getMSH(), new Date(), facilityName, "REF", "I12", "REF_I12", "2.5");
		DataTypeUtils.fillSft(referralMsg.getSFT(), BuildInfo.getBuildTag(), BuildInfo.getBuildDate());

		fillRf1(referralMsg.getRF1(), null, null, null, null, consultationRequest.getId(), consultationRequest.getReferralDate(), null, null);

		ProviderDao providerDao=(ProviderDao) SpringUtils.getBean("providerDao");
		Provider referringProvider=providerDao.getProvider(consultationRequest.getProviderNo());
		DataTypeUtils.fillPrd(referralMsg.getPROVIDER_CONTACT(0).getPRD(), referringProvider, "RP", "Referring Provider", referringOfficeStreetAddress);

		ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
		ProfessionalSpecialist referredToProfessionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
		StreetAddressDataHolder referredToOfficeStreetAddress=new StreetAddressDataHolder();
		referredToOfficeStreetAddress.streetAddress=referredToProfessionalSpecialist.getStreetAddress();
		DataTypeUtils.fillPrd(referralMsg.getPROVIDER_CONTACT(1).getPRD(), referredToProfessionalSpecialist, "RT", "Referred to Provider", referringOfficeStreetAddress);

		DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic=demographicDao.getDemographicById(consultationRequest.getDemographicId());		
		DataTypeUtils.fillPid(referralMsg.getPID(), 1, demographic);

		fillReferralNotes(referralMsg, consultationRequest);
				
		return(referralMsg);
	}
	
	private static void fillReferralNotes(REF_I12 referralMsg, ConsultationRequest consultationRequest) throws HL7Exception
	{
		// for each data section, we'll create a new NTE and we'll label the section some how...

		DataTypeUtils.fillNte(referralMsg.getNTE(0), REF_NTE_TYPE.APPOINTMENT_NOTES.name(), consultationRequest.getStatusText());
		DataTypeUtils.fillNte(referralMsg.getNTE(1), REF_NTE_TYPE.REASON_FOR_CONSULTATION.name(), consultationRequest.getReasonForReferral());
		DataTypeUtils.fillNte(referralMsg.getNTE(2), REF_NTE_TYPE.CLINICAL_INFORMATION.name(), consultationRequest.getClinicalInfo());
		DataTypeUtils.fillNte(referralMsg.getNTE(3), REF_NTE_TYPE.CONCURRENT_PROBLEMS.name(), consultationRequest.getConcurrentProblems());
		DataTypeUtils.fillNte(referralMsg.getNTE(4), REF_NTE_TYPE.CURRENT_MEDICATIONS.name(), consultationRequest.getCurrentMeds());
		DataTypeUtils.fillNte(referralMsg.getNTE(5), REF_NTE_TYPE.ALLERGIES.name(), consultationRequest.getAllergies());
	}

	
	/**
	 * see comments in code for values of the ID and descriptions
	 */
	private static void fillRf1(RF1 rf1, String referralTypeId, String referralTypeDescription, String referralDispositionId, String referralDispositionDescription, Integer oscarReferralId, Date referralDate, String referralReasonId, String referralReasonDescription) throws HL7Exception
	{
		//Value Description
		//-----------------
		//Lab  Laboratory
		//Rad  Radiology
		//Med  Medical
		//Skn  Skilled Nursing
		//Psy  Psychiatric
		//Hom  Home Care
		rf1.getReferralType().getIdentifier().setValue(referralTypeId);
		rf1.getReferralType().getText().setValue(referralTypeDescription);

		//Value Description
		//-----------------
		// WR   Send Written Report
		// RP   Return Patient After Evaluation
		// AM   Assume Management
		// SO   Second Opinion				
		rf1.getReferralDisposition(0).getIdentifier().setValue(referralDispositionId);
		rf1.getReferralDisposition(0).getText().setValue(referralDispositionDescription);
		
		// 15 character oscar referralId from source facility
		rf1.getOriginatingReferralIdentifier().getEntityIdentifier().setValue(oscarReferralId.toString());
		
		// date referral is effective
		rf1.getEffectiveDate().getTime().setValue(DataTypeUtils.getAsHl7FormattedString(referralDate));

		//Value Description
		//-----------------
		//  S   Second Opinion
		//  P   Patient Preference
		//  O   Provider Ordered
		//  W    Work Load
		rf1.getReferralReason(0).getIdentifier().setValue(referralReasonId);
		rf1.getReferralReason(0).getText().setValue(referralReasonDescription);
	}
}
