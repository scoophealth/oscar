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
package org.oscarehr.common.hl7.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.appt.ApptData;
import oscar.oscarClinic.ClinicData;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ADT_A04;
import ca.uhn.hl7v2.model.v23.segment.EVN;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * Class HL7A04Data
 */
public class HL7A04Data
{

	private static final Logger logger = MiscUtils.getLogger();
	private OscarProperties oscarProperties = OscarProperties.getInstance();
	
	private ApptData appData;
	private ClinicData clinicData;
	private List programs;
	private String[] demoData;
	private String message;
	private String fileName;
    
    
    /**
     * Constructor
     */
    public HL7A04Data() {
	}
	
	/**
     * Constructor
     */
	public HL7A04Data(org.oscarehr.common.model.Demographic demograph) {
		this.setDemographicData(demograph);
	}
	
	/**
     * Constructor
     */
    /*
	public HL7A04Data(oscar.oscarDemographic.data.DemographicData.Demographic demograph) {
		this.setDemographicData(demograph);
	}
	*/
    
    /**
     * Constructor
     */
	public HL7A04Data( org.oscarehr.common.model.Demographic demograph, ApptData appData, ClinicData clinicData, List programs) {
        this.setDemographicData(demograph);
        this.setAppData(appData);
        this.setClinicData(clinicData);
        this.setPrograms(programs);
    }
    
    /**
     * Constructor
     */
	public HL7A04Data( org.oscarehr.common.model.Demographic demograph, ApptData appData, ClinicData clinicData) {
        this.setDemographicData(demograph);
        this.setAppData(appData);
        this.setClinicData(clinicData);
    }
    
    /**
     * Constructor
     */
    /*
	public HL7A04Data( oscar.oscarDemographic.data.DemographicData.Demographic demograph, ApptData appData, ClinicData clinicData) throws HL7Exception {
        this.setDemographicData(demograph);
        this.setAppData(appData);
        this.setClinicData(clinicData);
    }
    */
	
	/**
	 * 
	 */
	/*
	public void setDemographicData(oscar.oscarDemographic.data.DemographicData.Demographic demograph) {
		this.demoData = new String[6];
		
		this.demoData[0] = demograph.getDemographicNo();
        this.demoData[1] = demograph.getLastName();
        this.demoData[2] = demograph.getFirstName();
        this.demoData[3] = demograph.getDob();
        this.demoData[4] = demograph.getSex();
        this.demoData[5] = demograph.getChartNo();
	}
	*/
	
	/**
	 * 
	 */
	public void setDemographicData(org.oscarehr.common.model.Demographic demograph) {
		this.demoData = new String[6];
		
		this.demoData[0] = demograph.getDemographicNo().toString();
        this.demoData[1] = demograph.getLastName();
        this.demoData[2] = demograph.getFirstName();
        this.demoData[3] = demograph.getBirthDayAsString().replaceAll("-", "");
        this.demoData[4] = demograph.getSex();
        this.demoData[5] = demograph.getChartNo();
	}
	
	/**
	 * 
	 */
	public void setAppData(ApptData appData) {
		this.appData = appData;
	}
	
	/**
	 * 
	 */
    public void setClinicData(ClinicData clinicData) {
		this.clinicData = clinicData;
	}
	
	/**
	 * 
	 */
    public void setPrograms(List programs) {
		this.programs = programs;
	}
    
    /**
	 * 
	 */
    public boolean save() throws HL7Exception {
		if (this.message == null)
			this.generateA04MessageGuelph();
		       
        logger.info("Creating HL7 A04 file with contents: " + this.message);
        
        String saveDir = oscarProperties.getHL7A04BuildDirectory();
        
        // create HL7 A04 file
        try {
        	File directory = new File(saveDir);
        	if (!directory.exists())
        		directory.mkdir();
        	
        	FileWriter fw = new FileWriter(saveDir + this.fileName, true);
        	BufferedWriter out = new BufferedWriter(fw);
        	out.write(this.message);
        	out.close();
        } catch (IOException e) {
			logger.error("ERROR while saving HL7 A04 file: " + e.toString());
			return false;
        }
        
        logger.info("Successfully saved HL7 A04 file: " + saveDir + this.fileName);
        
        return true;
	}
	
	/**
	 * 
	 */
	public String getA04Message() throws HL7Exception {
		if (this.message == null)
			this.generateA04MessageGuelph();
			
		return this.message;
	}
    
    /**
     * generateA04MessageGuelph
     * 
     * Generates the encoded HL7 A04 message specific for St. Joe's Guelph
     * @throws HL7Exception 
     */
	public void generateA04MessageGuelph() throws HL7Exception {
		if (this.demoData == null) {
			logger.error("ERROR while generating HL7 A04: Demographic data is null");
			throw new HL7Exception("Error generating HL7 A04: Demographic data is null");
		}
			
        // Make sure input is properly formatted
        for (int i=0; i < demoData.length; i++)
			demoData[i] = (demoData[i] == null || demoData[i].trim().length() == 0? " " : demoData[i]);
			
		// get current timestamp
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddkkmmss.SSSZ");
        String currentTimestamp = formatter.format(new Date());
        
        // generate file name
        this.fileName = currentTimestamp.toString() + ".txt";
        
        // get current date/time
        formatter = new SimpleDateFormat("yyyyMMddkkmmss");
        String currentDateTime = formatter.format(new Date());
        
        // Begin generating HL7 A04
		ADT_A04 adt = new ADT_A04();
        
        // MSH Segment
        MSH mshSegment = adt.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getSendingApplication().getNamespaceID().setValue( oscarProperties.getHL7SendingApplication() );
        mshSegment.getSendingFacility().getNamespaceID().setValue( oscarProperties.getHL7SendingFacility() );
        mshSegment.getReceivingApplication().getNamespaceID().setValue( oscarProperties.getHL7ReceivingApplication() );
        mshSegment.getReceivingFacility().getNamespaceID().setValue( oscarProperties.getHL7ReceivingFacility() );
        mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue(currentDateTime);
        mshSegment.getMessageControlID().setValue(currentTimestamp.toString());
        mshSegment.getProcessingID().getProcessingID().setValue("T");
        mshSegment.getMessageType().getMessageType().setValue("ADT");
        mshSegment.getMessageType().getTriggerEvent().setValue("A04");
        mshSegment.getVersionID().setValue("2.3");
        
        // EVN Segment
        EVN evn = adt.getEVN();
        evn.getEventTypeCode().setValue("A04");
        evn.getRecordedDateTime().getTimeOfAnEvent().setValue(currentDateTime);
        
        // PID Segment
        PID pid = adt.getPID(); 
        pid.getPid1_SetIDPatientID().setValue("1");
        
        pid.getPatientIDInternalID(0).getID().setValue("R_"+demoData[0]);
        
        pid.getPatientName().getFamilyName().setValue(demoData[1]);
        pid.getPatientName().getGivenName().setValue(demoData[2]);
        pid.getPatientName().getMiddleInitialOrName().setValue(" ");
        pid.getDateOfBirth().getTimeOfAnEvent().setValue(demoData[3]);
        pid.getSex().setValue(demoData[4]);
        
        
        if (appData != null) {			
			// PV1 segment
			PV1 pv1 = adt.getPV1();
			
			// Make 'null' values a space
			String nullValue = " ";
	      	pv1.getPv110_HospitalService().setValue(nullValue);
	      	pv1.getPv111_TemporaryLocation().getPl1_PointOfCare().setValue(nullValue);
	      	pv1.getPv112_PreadmitTestIndicator().setValue(nullValue);
	      	pv1.getPv113_ReadmissionIndicator().setValue(nullValue);
	      	pv1.getPv114_AdmitSource().setValue(nullValue);
	      	pv1.getPv115_AmbulatoryStatus(0).setValue(nullValue);
	      	pv1.getPv116_VIPIndicator().setValue(nullValue);
	      	pv1.getPv117_AdmittingDoctor(0).getXcn1_IDNumber().setValue(nullValue);
	      	pv1.getPv118_PatientType().setValue(nullValue);
	      	pv1.getPv119_VisitNumber().getCx1_ID().setValue(nullValue);
	      	pv1.getPv11_SetIDPatientVisit().setValue(nullValue);
	      	pv1.getPv120_FinancialClass(0).getFc1_FinancialClass().setValue(nullValue);
	      	pv1.getPv121_ChargePriceIndicator().setValue(nullValue);
	      	pv1.getPv122_CourtesyCode().setValue(nullValue);
	      	pv1.getPv123_CreditRating().setValue(nullValue);
	      	pv1.getPv124_ContractCode(0).setValue(nullValue);
	      	pv1.getPv125_ContractEffectiveDate(0).setValue(nullValue);
	      	pv1.getPv126_ContractAmount(0).setValue(nullValue);
	      	pv1.getPv127_ContractPeriod(0).setValue(nullValue);
	      	pv1.getPv128_InterestCode().setValue(nullValue);
	      	pv1.getPv129_TransferToBadDebtCode().setValue(nullValue);
	      	pv1.getPv12_PatientClass().setValue(nullValue);
	      	pv1.getPv130_TransferToBadDebtDate().setValue(nullValue);
	      	pv1.getPv131_BadDebtAgencyCode().setValue(nullValue);
	      	pv1.getPv132_BadDebtTransferAmount().setValue(nullValue);
	      	pv1.getPv133_BadDebtRecoveryAmount().setValue(nullValue);
	      	pv1.getPv134_DeleteAccountIndicator().setValue(nullValue);
	      	pv1.getPv135_DeleteAccountDate().setValue(nullValue);
	      	pv1.getPv136_DischargeDisposition().setValue(nullValue);
	      	pv1.getPv137_DischargedToLocation().getCm_dld1_DischargeLocation().setValue(nullValue);
	      	pv1.getPv138_DietType().setValue(nullValue);
	      	pv1.getPv139_ServicingFacility().setValue(nullValue);
	      	pv1.getPv140_BedStatus().setValue(nullValue);
	      	pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(nullValue);
	      	pv1.getPv141_AccountStatus().setValue(nullValue);
	      	pv1.getPv142_PendingLocation().getPl1_PointOfCare().setValue(nullValue);
	      	pv1.getPv143_PriorTemporaryLocation().getPl1_PointOfCare().setValue(nullValue);
	      	pv1.getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().setValue(nullValue);
	      	pv1.getPv145_DischargeDateTime().getTs1_TimeOfAnEvent().setValue(nullValue);
	      	pv1.getPv146_CurrentPatientBalance().setValue(nullValue);
	      	pv1.getPv147_TotalCharges().setValue(nullValue);
	      	pv1.getPv148_TotalAdjustments().setValue(nullValue);
	      	pv1.getPv149_TotalPayments().setValue(nullValue);
	      	pv1.getPv14_AdmissionType().setValue(nullValue);
	      	pv1.getPv150_AlternateVisitID().getCx1_ID().setValue(nullValue);
	      	pv1.getPv151_VisitIndicator().setValue(nullValue);
	      	pv1.getPv152_OtherHealthcareProvider(0).getXcn1_IDNumber().setValue(nullValue);
	      	pv1.getPv15_PreadmitNumber().getCx1_ID().setValue(nullValue);
	      	pv1.getPv16_PriorPatientLocation().getPl1_PointOfCare().setValue(nullValue);
	      	pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().setValue(nullValue);
	      	pv1.getPv18_ReferringDoctor(0).getXcn1_IDNumber().setValue(nullValue);
	      	pv1.getPv19_ConsultingDoctor(0).getXcn1_IDNumber().setValue(nullValue);
			
			pv1.getSetIDPatientVisit().setValue("1");
			pv1.getPatientClass().setValue("R");
			
			/*
			if (clinicData != null) {
				pv1.getAssignedPatientLocation().getPointOfCare().setValue(clinicData.getClinicNo());	// clinic number
				pv1.getAssignedPatientLocation().getRoom().setValue(clinicData.getClinicName());		// clinic name
			}
			*/
			
			if (this.programs != null) {
				String programId = "";
				if (this.programs.size() > 0) {
					Integer pId = (Integer)this.programs.get(0);
					if (pId != null) {
						programId = pId.toString();
					}
				}
				
				/*
				for (int i=0; i < this.programs.size(); i++) {
					Integer programId = (Integer)this.programs.get(i);
					if (programId != null) {
						if (programIds.length() != 0)
							programIds += "-";
						programIds += programId.toString();
					}
					//pv1.getAssignedPatientLocation().getRoom().setValue( p.getName() ); // program name (not used anymore)
				}
				*/
				pv1.getAssignedPatientLocation().getPointOfCare().setValue( programId );	// program number
			}
			
			pv1.getAssignedPatientLocation().getBed().setValue(demoData[5]);	// demographic chart number
			
			pv1.getAttendingDoctor(0).getIDNumber().setValue(appData.getProviderNo());		// provider number
			
			// get the family doctor first and last name as seperate strings
			String familyDoctorFirstName = appData.getProviderFirstName();
			String familyDoctorLastName = appData.getProviderLastName();
			
			
			pv1.getAttendingDoctor(0).getFamilyName().setValue( (familyDoctorFirstName==null? " " : familyDoctorFirstName) );	// family doc last name
			pv1.getAttendingDoctor(0).getGivenName().setValue( (familyDoctorLastName==null? " " : familyDoctorLastName) );		// family doc first name
			
			pv1.getVisitNumber().getID().setValue("oscar"+demoData[0]+"-"+ appData.getAppointment_no());
			
			String dateTime = appData.getAppointment_date().replace("-", "") + "" + appData.getStart_time().substring(0, 5).replace(":", "");
			
			pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(dateTime);
		}
		
        Parser parser = new PipeParser();
        this.message = parser.encode(adt);
    }
}
