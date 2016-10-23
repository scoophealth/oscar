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

package oscar.oscarLab.ca.all.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;


/**
 * HL7 Message Handler class SPECIFICALLY for Meditech's Physician Office Interface (POI)
 * The Meditech POI is used mainly with Ontario's Local Health Integration Network (LHIN) 
 * facilitates and with BC's Interior Health Authority.
 * 
 * Authored by Colcamex Resources Inc.
 * Sponsored by Trimara Corp.
 * 
 */
public class MEDITECHHandler implements MessageHandler {

	public static enum DIAGNOSTIC_ID {PTH,ITS,LAB,MIC,BBK}
	public static enum UNSTRUCTURED {PTH,ITS}
	public static enum STRUCTURED {LAB,MIC,BBK}
	public static enum OBX_DATA_TYPES {NM,ST,CE,TX}
	
//	public static final boolean USE_OBR_HEADERS_FOR_OBR_NAME = Boolean.TRUE; 
	public static final String DEFAULT_LAB_NAME = "LAB";
	public static final String NORMAL_LAB = "N";
	public static String DATE_FORMAT = "yyyyMMddHHmmss";
	public static String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static Logger logger = Logger.getLogger(MEDITECHHandler.class);
	protected ORU_R01 msg = null;
	private Terser terser;

	public MEDITECHHandler() {
		// Default constructor.
	}

	@Override
	public void init(String hl7Body) throws HL7Exception {
		Parser parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		msg = (ORU_R01) parser.parse(hl7Body.replaceAll( "\n", "\r\n" ).replace("\\.Zt\\", "\t"));
		terser = new Terser(msg);
	}

	public String getXML() {

		XMLParser xmlParser = new DefaultXMLParser();
		String messageInXML = "";
		try {
			messageInXML = xmlParser.encode(msg);
		} catch (HL7Exception e) {
			messageInXML = "";
		}

		return messageInXML;

	}
	
	/**
	 * If the first OBX segment is presenting a textual report and the lab type is 
	 * not in the unstructured (PATH or ITS) lab types.  
	 * 
	 */
	public boolean isReportData() {		
		return ( OBX_DATA_TYPES.TX.name().equals( getOBXValueType(0, 0) ) && ! isUnstructured() );		
	}

	/**
	 * Determines if this lab is a LTS or PATH type. 
	 */
	public boolean isUnstructured() {		
		for(UNSTRUCTURED lab : UNSTRUCTURED.values()) {
			if( lab.name().equalsIgnoreCase( getSendingApplication() ) ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * The HL7 format being sent MSH 1.1
	 */
	public String getSendingApplication() {
		return getString(msg.getMSH().getSendingApplication().getHd1_NamespaceID().getValue());
	}

	@Override
	public String getMsgType() {
		return new String("MEDITECH");
	}

	@Override
	public String getMsgDate() {
		return(formatDateTime(getString(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue())));
	}

	@Override
	public String getMsgPriority() {
		return new String("");
	}

	@Override
	public int getOBRCount() {
		
		Integer ooCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();
		int finalCount = 0;
		
		for(int i = 0; i < ooCount; i++ ) {
			try {
				if( ( msg.getRESPONSE().getORDER_OBSERVATION(i).getAll("OBR") ).length > 0 ) {
					finalCount++;
				}
			} catch (HL7Exception e) {
				finalCount = 0;
			}
		}

		return finalCount;
	}

	@Override
	public int getOBXCount(int obr) {
		try{
			return( msg.getRESPONSE().getORDER_OBSERVATION(obr).getOBSERVATIONReps() );
		}catch(Exception e){
			return(0);
		}
	}
	
	/**
	 * Returns a stringed together list of OBR headings as the discipline name.
	 * Alternatively, this could be adapted to return the proper discipline name 
	 * stored in 
	 */
	public String getDiscipline() {

		List<String> headers = new ArrayList<String>( getHeaders() );
		StringBuilder headerString = new StringBuilder("");
		int size = headers.size();
		int count = 0;
		
		for( String header : headers ) {
			count++;
			headerString.append(header);
			if(count < size) {
				headerString.append("/");
			}			
		}

		return headerString.toString();
	}
	
	/**
	 * Diagnostic Service Sect ID
	 * OBR 24.1 and OBR 24.2 
	 * For ITS this is located in OBR.21 Often times an ITS lab will have identifiers in both locations or only in one. 
	 */
	@Override
	public String getOBRName(int i){

		String labName = "";

			try{
	
				// Try OBR.20 location if the lab is ITS
				if( isUnstructured() ) {
					
					labName = getString(terser.get("/.OBR-24"));
					
					if( labName.isEmpty() ) {
						labName = getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getFillerField1().getValue() );
					}
				} 

				// if the Lab description or the ITS description is empty, look for the general ID.
				if( labName.isEmpty() ) {
					labName = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe2_Text().getValue());
				}
				
				if( labName.isEmpty() ) {
					labName = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe1_Identifier().getValue());
				}

				// try OBR.21 
				if( labName.isEmpty() ) {
					labName = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getFillerField2().getValue());
				}
	
				// if all else fails give it a default lab name.
				if( labName.isEmpty() ) {
					labName = getSendingApplication() + " " + DEFAULT_LAB_NAME;
				}
	
				return labName;			
	
			}catch(Exception e){
				labName = "";
			}

			return labName;
	}

	@Override
	public String getTimeStamp(int i, int j){
		try{
			return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue())));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public boolean isOBXAbnormal(int i, int j){
		try{
			String abn = getOBXAbnormalFlag(i, j);
			if( ! abn.equals("") && ! NORMAL_LAB.equalsIgnoreCase(abn) ){
				return(true);
			}else{
				return(false);
			}
		}catch(Exception e){
			return(false);
		}
	}

	@Override
	public String getOBXAbnormalFlag(int i, int j){
		try{
			return(getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()) );
		}catch(Exception e){
			logger.error("Error retrieving obx abnormal flag", e);
			return("");
		}
	}

	/**
	 * Universal Service ID
	 * OBR 4.1 CODE, 4.2 DESCRIPTION
	 * Use getHeaders() to avoid redundant headers. 
	 * If the Description is empty then the Code will be returned.
	 */
	@Override
	public String getObservationHeader(int i, int j){
		
		StringBuilder header = new StringBuilder("");
		String obrHeader = "";
		String obrHeaderCode = "";
		try{			
			obrHeader = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe2_Text().getValue());
			obrHeaderCode = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe1_Identifier().getValue());
			
			if( ! obrHeader.isEmpty() ) {
				header.append(obrHeader);
			} else {
				header.append(obrHeaderCode);
			}
		
		}catch(Exception e){
			return("");
		}
		
		return header.toString();
	}
	
	/**
	 * Often used in PTH and MIC lab results. 
	 * This will get the source of the specimen used in a lab.
	 * ie: Description of specimen (ie left, right, upper quadrant, etc) 
	 * location is 15 - 15.1 to 15.4
	 */
	public String getSpecimenSource(int i) {
		String specimen = "";
		try {
			specimen = getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObr15_SpecimenSource().getCm_sps2_Additives().getValue() );
		} catch (HL7Exception e) {
			return("");
		}
		
		return specimen;
	}
	
	
	/**
	 *  The facilities specific specimen id number. Located at OBR 3.1
	 */
	public String getSpecimenDescription(int i) {
		String specimenDescription = "";
		try {
			specimenDescription = getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObr15_SpecimenSource().getCm_sps3_Freetext().getValue());
		} catch (HL7Exception e) {
			return("");
		}
		
		return specimenDescription;
	}

	/**
	 * Observation Identifier
	 * OBX 3.1 3.2. 
	 * Is the name of the specific test result.
	 */
	@Override
	public String getOBXIdentifier(int i, int j){
		try{
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));
		}catch(Exception e){
			return("");
		}
	}
	
	
	@Override
	public String getOBXValueType(int i, int j){
		try{
			return(getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue()) );
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOBXName(int i, int j){
		try{
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOBXResult(int i, int j){
		try{
			return( getString( Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX() ,5 ,0 ,1 ,1 ) ) );
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOBXReferenceRange(int i, int j){
		try{
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getReferencesRange().getValue()));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOBXUnits(int i, int j){
		try{
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOBXResultStatus(int i, int j){
		try{
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservResultStatus().getValue()));
		}catch(Exception e){
			return("");
		}
	}

	/**
	 * When there are multiple redundant Observation Headers, this will only return an individual Observation Header
	 * If the Observation Result is empty, the Observation Header will be concatenated to the next.
	 * Assumption is that any last OBR will never have empty OBX.
	 *
	 */
	@Override
	public ArrayList<String> getHeaders(){

		int obrCount = this.getOBRCount();
		ArrayList<String> headers = null;
		String currentHeader = "";
		
		logger.debug("Total OBR count: " + obrCount );

		for ( int i = 0; i < obrCount; i++ ){

			logger.debug("OBX Count for OBR[" + (i) + "] : " + getOBXCount(i) );
			
			currentHeader = getObservationHeader( i, 0 );

			// try the OBR Identity
			if( currentHeader.isEmpty() ){
				
				logger.debug("Observation Header not found. Trying Observation Name [" + i + "]" );
				currentHeader = getOBRName(i);
			}
			
			if( currentHeader.isEmpty() ) {
				currentHeader = "No Headings Found [" + i + "]";
			}
			
			if( headers == null ) {
				headers = new ArrayList<String>();
			}
			
			logger.debug("Adding header: '" + currentHeader + "' to list");

			headers.add( currentHeader );

		}

		return headers;

	}

	@Override
	public int getOBRCommentCount(int i){

		int count = 0;
		try {
			count = msg.getRESPONSE().getORDER_OBSERVATION(i).getNTEReps();
		} catch (HL7Exception e) {
			//do nothing
		}		
		return count;
	}

	@Override
	public String getOBRComment(int i, int j){
		try {
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getNTE(j).getComment(0).getValue()));
		} catch (Exception e) {
			return("");
		}
	}

	@Override
	public int getOBXCommentCount(int i, int j){
		int count = 0;
		try {
			count = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTEReps();
		} catch (HL7Exception e1) {
			// do nothing
		}
		return count;
	}

	@Override
	public String getOBXComment(int i, int j, int k){
		try {
			return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k).getComment(0).getValue()));
		} catch (Exception e) {
			return("");
		}
	}

	/**
	 * PATIENT DATA
	 */
	@Override
	public String getPatientName(){
		return(getFirstName()+" "+getMiddleName()+" "+getLastName());
	}

	@Override
	public String getFirstName(){
		return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getGivenName().getValue()));
	}

	public String getMiddleName(){
		return (getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getXpn3_MiddleInitialOrName().getValue()));
	}

	@Override
	public String getLastName(){
		return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getFamilyName().getValue()));
	}

	@Override
	public String getDOB(){
		try{
			return( formatDateTimeToDate( getString( msg.getRESPONSE().getPATIENT().getPID().getDateOfBirth().getTimeOfAnEvent().getValue() ) ) );
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getAge(){
		int age = 0;
		String dob = getDOB();
		String service = getServiceDate();
		
		// get the document date if the service/observation date was not used.
		if( service.isEmpty() ) {
			service = getMsgDate();
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar a = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		try {
			java.util.Date birthDate = formatter.parse(dob);			
					a.setTime(birthDate);
			java.util.Date serviceDate = formatter.parse(service);			
					b.setTime(serviceDate);
		} catch (ParseException e) {
			return("N/A");
		}

		age = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
            (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            age--;
        }

		if( age == 0 ) {
			return "N/A";
		}
		
		return age + "";
	}

	@Override
	public String getSex(){
		return(getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue()));
	}

	/**
	 * Forced to return null because an empty HIN can be considered a match.
	 * PID 2.1
	 */
	@Override
	public String getHealthNum(){
		String healthNumber = getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDExternalID().getID().getValue());
		if( healthNumber.isEmpty() ) {
			return null;
		}
		return healthNumber;
	}
	
	/**
	 * First Nations Band Number is stored at PID 2.5
	 */
	public String getFirstNationsBandNumber() {
		return getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDExternalID().getCx5_IdentifierTypeCode().getValue());
	}

	@Override
	public String getHomePhone(){
		String phone = "";
		int i=0;
		try{
			while(!getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue()).equals("")){
				if (i==0){
					phone = getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue());
				}else{
					phone = phone + ", " + getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue());
				}
				i++;
			}
			return(phone);
		}catch(Exception e){
			logger.error("Could not return phone number", e);

			return("");
		}
	}

	@Override
	public String getWorkPhone(){
		String phone = "";
		int i=0;
		try{
			while(!getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue()).equals("")){
				if (i==0){
					phone = getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue());
				}else{
					phone = phone + ", " + getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue());
				}
				i++;
			}
			return(phone);
		}catch(Exception e){
			logger.error("Could not return phone number", e);

			return("");
		}
	}

	@Override
	/**
	 * Patient Location is PV1.3
	 */
	public String getPatientLocation(){	
		return( new String(
				getString( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAssignedPatientLocation().getPl1_PointOfCare().getValue() ) 
				+ " " 
				+ getString( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().getValue() ) ));
	}

	@Override
	public String getServiceDate(){
		try{
			return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue())));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getRequestDate(int i){
		try{
			return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getRequestedDateTime().getTimeOfAnEvent().getValue())));
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public String getOrderStatus(){
		
		try{
			String orderStatus = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultStatus().getValue());
			int obrCount = getOBRCount();
			int obxCount;
			int count = 0;
			for (int i=0; i < obrCount; i++){
				obxCount = getOBXCount(i);
				for (int j=0; j < obxCount; j++){
					String obxStatus = getOBXResultStatus(i, j);
					if (obxStatus.equalsIgnoreCase("C")) {
						count++;
					}
				}
			}
			
			if(count > 0){//if any of the OBX's have been corrected, mark the entire report as corrected
				orderStatus = "C";
			}
				
			return orderStatus;
		
		}catch(Exception e){
			return("");
		}
	}

	@Override
	public int getOBXFinalResultCount(){
		int obrCount = getOBRCount();
		int obxCount;
		int count = 0;
		for (int i=0; i < obrCount; i++){
			obxCount = getOBXCount(i);
			for (int j=0; j < obxCount; j++){
				String status = getOBXResultStatus(i, j);
				if (status.equalsIgnoreCase("F") || status.equalsIgnoreCase("C")) {
					count++;
				}
			}
		}


		String orderStatus = getOrderStatus();
		// add extra so final reports are always the ordered as the latest except
		// if the report has been changed in which case that report should be the latest
		if (orderStatus.equalsIgnoreCase("F")) {
			count = count + 100;
		} else if (orderStatus.equalsIgnoreCase("C"))  {
			count = count + 150;
		}

		return count;
	}


	@Override
	public String getClientRef(){
		String docNum = "";
		int i=0;
		try{
			while(!getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue()).equals("")){
				if (i==0){
					docNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
				}else{
					docNum = docNum + ", " + getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
				}
				i++;
			}

			if( docNum == null ) {
				docNum = "";
			}

		}catch(Exception e){
			logger.error("Could not return doctor id numbers", e);
		}

		return docNum;
	}

	/**
	 * Other Healthcare Providers PV1 52
	 * @return
	 */
	public String getOtherHealthcareProviders() {
		String otherDoctor = "";

		if( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getOtherHealthcareProviderReps() > 0 ) {
			XCN[] otherDoctorArray = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getOtherHealthcareProvider();
			for(int i = 0; i < otherDoctorArray.length; i++ ) {
				otherDoctor += ( ( i > 0) ? ", " : "" ) + getFullDocName( otherDoctorArray[i] );
			}
		}
		return otherDoctor;
	}

	/**
	 * seg PV1 7
	 * @return
	 */
	public String getAttendingPhysician() {

		String attendingDoctor = "";
		if( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAttendingDoctorReps() > 0 ) {
			XCN[] attendingDoctorArray = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAttendingDoctor();
			for(int i = 0; i < attendingDoctorArray.length; i++ ) {
				attendingDoctor += ( ( i > 0) ? ", " : "" ) + getFullDocName( attendingDoctorArray[i] );
			}
		}
		return attendingDoctor;
	}

	/**
	 * seg PV1 17
	 * @return
	 */
	public String getAdmittingPhysician() {

		String admittingDoctor = "";

		if( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAdmittingDoctorReps() > 0 ) {
			XCN[] admittingDoctorArray = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAdmittingDoctor();
			for(int i = 0; i < admittingDoctorArray.length; i++ ) {
				admittingDoctor += ( ( i > 0) ? ", " : "" ) + getFullDocName( admittingDoctorArray[i] );
			}		
		}

		return admittingDoctor;
	}

	/**
	 * "Ordering Provider" OBR.16 or "Attending Provider" PV1.7
	 */
	@Override
	public String getDocName(){

		String docName = "";
		int i=0;
		try{
			while(!getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i)).equals("")){
				if (i==0){
					docName = getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
				}else{
					docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
				}
				i++;
			}

			// try the Attending Dr. instead. Could be the Family Practitioner working in Emerg.
			if( docName.isEmpty() ) {
				docName = getAttendingPhysician();
			}

		}catch(Exception e){
			logger.error("Could not return doctor names", e);
		}

		return docName;
	}

	/**
	 * All CC'ed Physicians in PV1.52 and OBR.28
	 */
	@Override
	public String getCCDocs(){

		String docName = "";
		int i=0;

		try{
			while( ! getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i)).isEmpty() ) {

				if (i == 0){
					docName = getFullDocName( msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i) );
				}else{
					docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
				}
				i++;
			}            
		}catch(Exception e){
			logger.error("Could not return cc'ed doctors", e);
		}

		return docName;

	}

	/**
	 * A map of all providers and id numbers from this lab report.
	 * 
	 * @return
	 */
	public HashMap<String, String> getProviderMap() {

		HashMap<String, String> providerMap = new HashMap<String, String>();

		createProviderMap( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAdmittingDoctor(), providerMap );
		createProviderMap( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAttendingDoctor(), providerMap );
		createProviderMap( msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getOtherHealthcareProvider(), providerMap );

		try {
			createProviderMap( msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(), providerMap );
		} catch (HL7Exception e) {
			logger.error("Exception while adding ordering provider to map ", e);
		}

		try {
			createProviderMap( msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(), providerMap );
		} catch (HL7Exception e) {
			logger.error("Exception while adding copied to providers to map ", e);
		}

		return providerMap;
	}

	public static void createProviderMap(XCN[] providerArray, HashMap<String, String> providerMap ) {

		String id = ""; 
		String name = "";
		XCN providerData = null;
		for(int i = 0; i < providerArray.length; i++ ) {

			providerData = providerArray[i];
			id = providerData.getIDNumber().getValue(); 
			name = getFullDocName( providerData );

			if( id == null ) {
				id = "";
			}

			if( name == null ) {
				name = "";
			}

			if( ! id.isEmpty() && ! name.isEmpty() ) {
				providerMap.put(id.trim(), name.trim());
			}
		}
	}

	/**
	 * Get all CPSO ID numbers from the results regardless of category. 
	 * No duplicates.
	 */
	@Override
	public ArrayList<String> getDocNums() {
		ArrayList<String> docNums = new ArrayList<String>();
		HashMap<String, String> providerMap = getProviderMap();
		Iterator<String> iterator = providerMap.keySet().iterator();
		while( iterator.hasNext() ) {
			docNums.add( iterator.next() );
		}
		return docNums;		
	}	


	/**
	 * Meditech Accession Number OBR.2
	 * The filler order number is more unique because it includes the facility id.
	 */
	@Override
	public String getAccessionNum(){
		return getFillerOrderNumber();
	}

	@Override
	public String audit() {
		return "success";
	}


	/** 
	 * Meditech Filler Order Number OBR.3 3.1 aka accession number.
	 */
	@Override
	public String getFillerOrderNumber(){
		return getString(msg.getRESPONSE().getORDER_OBSERVATION().getOBR().getFillerOrderNumber().getEntityIdentifier().getValue());
	}

	@Override
	public String getEncounterId(){
		return "";
	}

	@Override
	public String getRadiologistInfo(){
		return "";
	}

	@Override
	public String getNteForOBX(int i, int j){
		return "";
	}

	@Override
	public String getNteForPID() {
		return "";
	}


	/* HELPER METHODS */

	private static final String getString(final String retrieve){
		String newRetrieve = new String("");
		if (retrieve == null){
			return newRetrieve;
		}
		newRetrieve = retrieve.replaceAll("^", " ");
		newRetrieve = newRetrieve.replaceAll("\\\\\\.br\\\\", "<br />");
		newRetrieve = newRetrieve.trim();

		return newRetrieve;
	}

	protected static String formatDateTime( String plain ){
		if ( plain==null || plain.isEmpty() ) { 			
			return "";
		}
		
		// remove the timezone syntax.
		if( plain.contains("-") ) {
			plain = plain.split("-")[0].trim();
		}

		String dateFormat = DATE_FORMAT.substring( 0, plain.length() );
		String stringFormat = DATE_STRING_FORMAT.substring( 0, DATE_STRING_FORMAT.lastIndexOf( dateFormat.charAt( dateFormat.length()-1) ) +1 );
		String datestring = "";
		SimpleDateFormat simpledateformat;
		
		try {
			simpledateformat = new SimpleDateFormat( dateFormat );
			Date date = simpledateformat.parse( plain );
			simpledateformat = new SimpleDateFormat( stringFormat );
			datestring = simpledateformat.format(date);
		} catch (ParseException e) {
			// do nothing
		}

		return datestring;
	}
	
	/**
	 * Filters out unwanted time zone and time strings from a date stamp
	 * Used mostly for filtering a Date of Birth Time Stamp.
	 * Assumed parameter is in the format yyyy-mm-dd
	 */
	protected static String formatDateTimeToDate( String datestring ) {
		if ( datestring == null || datestring.isEmpty() ) { 			
			return "";
		}

		return formatDateTime(datestring).substring(0, 10);
	}

	public static String getFullDocName(XCN docSeg){
		String docName = "";

		if(docSeg.getPrefixEgDR().getValue() != null)
			docName = docSeg.getPrefixEgDR().getValue();

		if(docSeg.getGivenName().getValue() != null){
			if (docName.equals("")){
				docName = docSeg.getGivenName().getValue();
			}else{
				docName = docName +" "+ docSeg.getGivenName().getValue();
			}
		}
		if(docSeg.getMiddleInitialOrName().getValue() != null)
			docName = docName +" "+ docSeg.getMiddleInitialOrName().getValue();
		if(docSeg.getFamilyName().getValue() != null)
			docName = docName +" "+ docSeg.getFamilyName().getValue();
		if(docSeg.getSuffixEgJRorIII().getValue() != null)
			docName = docName +" "+ docSeg.getSuffixEgJRorIII().getValue();
		if(docSeg.getDegreeEgMD().getValue() != null)
			docName = docName +" "+ docSeg.getDegreeEgMD().getValue();

		return (docName);
	}

}
