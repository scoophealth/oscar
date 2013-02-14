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


package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.group.REF_I12_PATIENT_VISIT;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.PRD;
import ca.uhn.hl7v2.model.v26.segment.PV1;
import ca.uhn.hl7v2.model.v26.segment.RF1;

public final class RefI12 {
	private static final Logger logger=MiscUtils.getLogger();
	
	public enum REF_NTE_TYPE
	{
		// APPOINTMENT_NOTES, this field is purposely removed as I was told this field specifically should not be sent with eReferrals
		REASON_FOR_CONSULTATION,
		CLINICAL_INFORMATION,
		CONCURRENT_PROBLEMS,
		CURRENT_MEDICATIONS,
		ALLERGIES
	}
	
	private RefI12()
	{
		// not meant to be instantiated
	}
	
	public static REF_I12 makeRefI12(Clinic clinic, ConsultationRequest consultationRequest) throws HL7Exception, UnsupportedEncodingException
	{
		REF_I12 referralMsg=new REF_I12();

		DataTypeUtils.fillMsh(referralMsg.getMSH(), new Date(), clinic.getClinicName(), "REF", "I12", "REF_I12", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(referralMsg.getSFT(), OscarProperties.getBuildTag(), OscarProperties.getBuildDate());

		fillRf1(referralMsg.getRF1(), null, null, null, null, consultationRequest.getId(), consultationRequest.getReferralDate(), null, null);

		ProviderDao providerDao=(ProviderDao) SpringUtils.getBean("providerDao");
		Provider referringProvider=providerDao.getProvider(consultationRequest.getProviderNo());
		DataTypeUtils.fillPrd(referralMsg.getPROVIDER_CONTACT(0).getPRD(), referringProvider, "RP", "Referring Provider", clinic);

		ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
		ProfessionalSpecialist referredToProfessionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
		DataTypeUtils.fillPrd(referralMsg.getPROVIDER_CONTACT(1).getPRD(), referredToProfessionalSpecialist, "RT", "Referred to Provider");

		DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic=demographicDao.getDemographicById(consultationRequest.getDemographicId());		
		DataTypeUtils.fillPid(referralMsg.getPID(), 1, demographic);

		addEmptyPV1(referralMsg);
		
		fillReferralNotes(referralMsg, consultationRequest);
				
		return(referralMsg);
	}
	
	/**
	 * An empty PV1 segment helps the parser get the NTE's in the right spot, otherwise sometimes the NTE's end up in the OBR segment instead of it's own
	 * top level segment. This is a glitch with the HAPI parser.
	 * @throws DataTypeException 
	 */
	private static void addEmptyPV1(REF_I12 referralMsg) throws DataTypeException {
		REF_I12_PATIENT_VISIT pv=referralMsg.getPATIENT_VISIT();
		PV1 pv1=pv.getPV1();
		pv1.getPatientClass().setValue("N"); // N is not applicable
    }

	private static void fillReferralNotes(REF_I12 referralMsg, ConsultationRequest consultationRequest) throws HL7Exception, UnsupportedEncodingException
	{
		// for each data section, we'll create a new NTE and we'll label the section some how...

		// Was specifically told that appointment notes is considered a secret field that should never be shown to the referredTo Provider
		// DataTypeUtils.fillNte(referralMsg.getNTE(0), REF_NTE_TYPE.APPOINTMENT_NOTES.name(), consultationRequest.getStatusText());
		
		int noteCounter=0;
		String temp=consultationRequest.getReasonForReferral();
		if (temp!=null) DataTypeUtils.fillNte(referralMsg.getNTE(noteCounter), REF_NTE_TYPE.REASON_FOR_CONSULTATION.name(), null, temp.getBytes());

		noteCounter++;
		temp=consultationRequest.getClinicalInfo();
		if (temp!=null) DataTypeUtils.fillNte(referralMsg.getNTE(noteCounter), REF_NTE_TYPE.CLINICAL_INFORMATION.name(), null, temp.getBytes());
		
		noteCounter++;
		temp=consultationRequest.getConcurrentProblems();
		if (temp!=null) DataTypeUtils.fillNte(referralMsg.getNTE(noteCounter), REF_NTE_TYPE.CONCURRENT_PROBLEMS.name(), null, temp.getBytes());
		
		noteCounter++;
		temp=consultationRequest.getCurrentMeds();
		if (temp!=null) DataTypeUtils.fillNte(referralMsg.getNTE(noteCounter), REF_NTE_TYPE.CURRENT_MEDICATIONS.name(), null, temp.getBytes());
		
		noteCounter++;
		temp=consultationRequest.getAllergies();
		if (temp!=null) DataTypeUtils.fillNte(referralMsg.getNTE(noteCounter), REF_NTE_TYPE.ALLERGIES.name(), null, temp.getBytes());
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
		rf1.getEffectiveDate().setValue(DataTypeUtils.getAsHl7FormattedString(referralDate));

		//Value Description
		//-----------------
		//  S   Second Opinion
		//  P   Patient Preference
		//  O   Provider Ordered
		//  W    Work Load
		rf1.getReferralReason(0).getIdentifier().setValue(referralReasonId);
		rf1.getReferralReason(0).getText().setValue(referralReasonDescription);
	}
	
	public static String getNteValue(REF_I12 referralMsg, REF_NTE_TYPE nteType) throws HL7Exception, UnsupportedEncodingException
	{
		logger.debug("Looking for nte comment type : "+nteType.name());
		logger.debug("Number of NTE segments : "+referralMsg.getNTEReps());
		for (int i=0; i<referralMsg.getNTEReps(); i++)
		{
            NTE nte=referralMsg.getNTE(i);
            String nteCommentType=nte.getCommentType().getText().getValue();

            logger.debug("NTE segment type : "+nteCommentType);

            if (nteType.name().equals(nteCommentType))
            {
            	return(new String(DataTypeUtils.getNteCommentsAsSingleDecodedByteArray(nte), MiscUtils.DEFAULT_UTF8_ENCODING));
            }
		}
		
		return(null);
	}
	
	/**
	 * Get the first PRD entry with the matching providerRoleId, the id is the 2 letter abbreviation.
	 * Value Description
	 * -----------------
	 * RP Referring Provider
	 * PP Primary Care Provider
	 * CP Consulting Provider
	 * RT Referred to Provider
	 */
	public static PRD getPrdByRoleId(REF_I12 referralMsg, String providerRoleId) throws HL7Exception
	{
		logger.debug("Looking for prd roleId : "+providerRoleId);
		logger.debug("Number of prd segments : "+referralMsg.getPROVIDER_CONTACTReps());
		for (int i=0; i<referralMsg.getPROVIDER_CONTACTReps(); i++)
		{
            PRD prd=referralMsg.getPROVIDER_CONTACT(i).getPRD();
            String prdRoleId=prd.getProviderRole(0).getIdentifier().getValue();

            logger.debug("prd role Id : "+prdRoleId);

            if (providerRoleId.equals(prdRoleId))
            {
            	return(prd);
            }
		}
		
		return(null);
	}
	
}
