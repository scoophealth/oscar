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
// import java.util.HashMap;

import java.util.HashMap;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;


/**
 * 
 * Authored by Colcamex Resources Inc.
 * Sponsored by OSCARWest and OSCAR BC
 * 
 * The BC Interior Health Authority uses the Meditech POI (IHAPOI) to distribute labs and hospital reports
 * to medical practices.
 * This class extends the master Meditech Handler and overrides some of the methods that are 
 * handled differently with the IHAPOI
 * 
 * The Meditech handler should be adapted as much as possible in order to deal with several difference scenarios.
 * 
 */
public class IHAPOIHandler extends MEDITECHHandler implements MessageHandler  {
	
	public enum SENDING_APPLICATION {

	    MB("MB", 1), 
	    BBK("BBK", 2), 
	    IHARAD("RAD", 3), 
	    OE("HR",4), 
	    LAB("LAB", 5),
	    PTH("PTH", 6),
	    RAD("RAD", 7);

	    private final String key;
	    private final Integer value;

	    SENDING_APPLICATION(String key, Integer value) {
	        this.key = key;
	        this.value = value;
	    }

	    public String getKey() {
	        return key;
	    }
	    public Integer getValue() {
	        return value;
	    }
	}

	public static enum STRUCTURED {LAB} 
	
	public IHAPOIHandler() {
		// default
	}
	
	@Override
	public void init(String hl7Body) throws HL7Exception {
		Parser parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		Message message = parser.parse( hl7Body.replaceAll("\n", "\n\r") );

		if( message instanceof ca.uhn.hl7v2.model.v23.message.ORU_R01 ) {
			msg = ( ca.uhn.hl7v2.model.v23.message.ORU_R01 ) message;
		} else {			
			// IHA sends their hospital reports with a message event trigger of O01 
			// this needs to be changed in order for HAPI to determine a proper message structure
			MSH messageHeader = (MSH) message.get("MSH");
			messageHeader.getMessageType().getCm_msg1_MessageType().setValue("ORU");
			messageHeader.getMessageType().getCm_msg2_TriggerEvent().setValue("R01");		
			init( message.encode() );
		}
		if( msg != null ) {
			setTerser( new Terser( msg ) );
		}
	}
	
	/**
	 * If the first OBX segment is presenting a textual report and the lab type is 
	 * not in the unstructured (PATH or ITS) lab types.  
	 * 
	 */
	@Override
	public boolean isReportData() {	
		if( OBX_DATA_TYPES.TX.name().equals( getOBXValueType(0, 0) ) 
				|| OBX_DATA_TYPES.FT.name().equals( getOBXValueType(0, 0) ) 
				|| SENDING_APPLICATION.IHARAD.name().equals(getDiagnosticServiceId())
				|| SENDING_APPLICATION.OE.name().equals(getDiagnosticServiceId()) ) {
			return true;
		}
		return false;
	}

	/**
	 * This is determined by a combination of the sending application and/or the diagnostic ID.
	 * Example: 
	 * All IHA Radiology (IHARAD) and ADT Report (OE) sending applications are are unstructured.
	 * Only some of the LAB sending applications are unstructured. Such as BBK and MB
	 */
	@Override
	public boolean isUnstructured() {
		return ( ! STRUCTURED.LAB.name().equalsIgnoreCase( getDiagnosticServiceId() ) );				
	}
	
	/**
	 * Meditech Accession Number OBR.2 AKA Accession number.
	 */
	@Override
	public String getAccessionNum(){
		String accession = null;
		
		try {
			accession = getString( msg.getRESPONSE().getORDER_OBSERVATION().getOBR().getPlacerOrderNumber(0).getEntityIdentifier().getValue() );	
		} catch (HL7Exception e) {
			// do nothing. Lab may not display correctly with out accession. 
		}
		try {
			if ( accession == null || accession.isEmpty() ) {
				accession = getString( msg.getRESPONSE().getORDER_OBSERVATION().getORC().getPlacerOrderNumber(0).getEntityIdentifier().getValue() );
			}
		} catch (HL7Exception e) {
			// do nothing. Lab may not display correctly with out accession. 
		}
		
		if ( accession == null || accession.isEmpty() ) {
			accession = getString( msg.getRESPONSE().getORDER_OBSERVATION().getORC().getPlacerGroupNumber().getEntityIdentifier().getValue() );
		}
		
		return accession;
	}


	/**
	 * If the diagnostic serviceid is not found then the sending application Id 
	 * is used instead.
	 */
	protected String getDiagnosticServiceId() {
		
		String serviceId = "";
		
		try {
			serviceId = getString(getTerser().get("/.OBR-24"));
		} catch (HL7Exception e) {
			return serviceId;
		}
		
		if( serviceId.isEmpty() ) {
			serviceId = super.getSendingApplication();
		}
		
		return serviceId;
	}
	
	/**
	 * Universal Service ID
	 * OBR 4.1 CODE, 4.2 DESCRIPTION. 
	 * If the Description is empty then the Code will be returned.
	 */
	@Override
	public String getObservationHeader(int i, int j){
		return this.getObservationHeader(i);
	}

	private String getObservationHeader(int i){
		
		StringBuilder header = new StringBuilder("");
		String obrAlternate = "";
		String obrHeader = "";
		try{	
			// IHA likes to use the occasional custom identifier.			
			obrHeader = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe5_AlternateText().getValue());
			obrAlternate = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe2_Text().getValue());
			
			if( ! obrHeader.isEmpty() ) {
				header.append(obrHeader);
			} else if( ! obrAlternate.isEmpty() ) {
				header.append(obrAlternate);
			} 

		}catch(Exception e){
			return("");
		}
		
		return header.toString();
	}
	
	/**
	 * Observation Identifier
	 * AKA LOINC code
	 * OBX 3.1 3.2. 
	 * Is the name of the specific test result.
	 * L201.2800^ALT^00025570^1742-6^Alanine Aminotransferase^pCLOCD
	 * LOINC is located in number 4 of the composite. 
	 */
	@Override
	public String getOBXIdentifier(int i, int j){
		String loinc = "";
		try{
			loinc = getString( msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getCe4_AlternateIdentifier().getValue() );
		}catch(Exception e){
			return("");
		}
		return loinc;
	}
	
	@Override
	public String getOBRName(int i){
		return this.getObservationHeader(0);
	}
	
	/**
	 * Done a little differently with IHA
	 * 
	 */
	@Override
	public String getDiscipline() {
		
		String observation = this.getObservationHeader(0);
		String sendingApplication = getDiagnosticServiceId();
		String discipline = "";
		
		if( ! sendingApplication.isEmpty() ) {
			switch(SENDING_APPLICATION.valueOf(sendingApplication)) {
				case BBK: discipline = SENDING_APPLICATION.BBK.getKey();
				break;
				case IHARAD: discipline = SENDING_APPLICATION.IHARAD.getKey();
				break;
				case RAD: discipline = SENDING_APPLICATION.RAD.getKey();
				break;
				case LAB: discipline = SENDING_APPLICATION.LAB.getKey();
				break;
				case MB: discipline = SENDING_APPLICATION.MB.getKey();
				break;
				case OE: discipline = SENDING_APPLICATION.OE.getKey();
				break;
				case PTH: discipline = SENDING_APPLICATION.PTH.getKey();
				break;
				default: discipline = "LAB";
			}
		}
		return discipline + ":" + observation; 
	}
	
	
	/**
	 * IHA provides the provider list at the end of a report in custom Z segements 
	 * as a convenience. 
	 */
	@Override
	public HashMap<String, String> getProviderMap() {
		
		HashMap<String, String> providerMap = new HashMap<String, String>();
		
		try {
			// ZDR segments are buried in the very last observation group.
			int observationReps = msg.getRESPONSE().getORDER_OBSERVATION().getOBSERVATIONReps();
			int fieldCount = getTerser().getSegment("/.OBSERVATION(" + (observationReps -1) + ")/ZDR").numFields();

			for(int i = 1; i < fieldCount + 1; i++) {
				String id = getTerser().get("/.OBSERVATION(" + (observationReps -1) + ")/ZDR-" + i + "-1" );
				if( id != null && ! id.isEmpty() ) {
					id = id.trim();
					providerMap.put(id, "");
				}
			}

		} catch (HL7Exception e) {
			logger.warn("Could not parse HL7 Z segment for providers. Ordering Dr. Could not be identified. Trying alternate method.");
			// try the Meditech method.
			providerMap = super.getProviderMap();
		}
		
		return providerMap;
	}
	
	/**
	 * IHA likes to put this in the alternate ID field. PID.4
	 */
	@Override
	public String getHealthNum(){
		String healthNumber = getString(msg.getRESPONSE().getPATIENT().getPID().getAlternatePatientID().getCx1_ID().getValue());
		if( healthNumber.isEmpty() ) {
			return null;
		}
		return healthNumber;
	}
	
}
