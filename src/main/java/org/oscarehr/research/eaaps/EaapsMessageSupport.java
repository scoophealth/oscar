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
package org.oscarehr.research.eaaps;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v22.datatype.CN;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_PATIENT;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.MSH;
import ca.uhn.hl7v2.model.v22.segment.NTE;
import ca.uhn.hl7v2.model.v22.segment.OBR;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class EaapsMessageSupport {

	private ORU_R01 message;
	
	/**
	 * Pre-processes the HL7 message before parsing.
	 * 
	 * @param hl7Body
	 * 		HL7 body to be pre-processed
	 * @return
	 * 		Returns the pre-processed content that's ready for parsing
	 */
	protected String preProcess(String hl7Body) {
		if (hl7Body == null) {
			return null;
		}
		return hl7Body.replaceAll("\n", "\r");
	}
	
	public byte[] getPdf() {
		NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE();
		String base64EncodedPdfContent;
        try {
	        base64EncodedPdfContent = nte.getComment(0).getValue();
        } catch (HL7Exception e) {
	        return null;
        }
        
		// break on empty PDFs
		if (base64EncodedPdfContent == null || base64EncodedPdfContent.isEmpty()) {
			return null;
		}

		byte[] pdf = Base64.decodeBase64(base64EncodedPdfContent);
		return pdf;
	}
	
	public String getPdfFileName() {
		NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE();
		if (nte == null) {
			return null;
		}
		if (nte.getNte2_SourceOfComment() == null) {
			return null;
		}
		return nte.getNte2_SourceOfComment().getValue();
	}
	
	public String getSourceFacility() {
		MSH mshSegment = message.getMSH();
		String sourceFacility = mshSegment.getSendingApplication().getValue();
		return sourceFacility;
	}
	
	public String getDemographicHash() {
		ORU_R01_PATIENT patientGroup = message.getPATIENT_RESULT().getPATIENT();
		String hash = patientGroup.getPID().getPatientIDExternalID().getCk1_IDNumber().getValue();
		return hash;
	}
	
	public void init(String hl7Body) throws HL7Exception, EncodingNotSupportedException {
		hl7Body = preProcess(hl7Body);
		Parser parser = new GenericParser();
		parser.setValidationContext(new NoValidation());
		Message m = parser.parse(hl7Body);
		if (!(m instanceof ORU_R01)) {
			throw new HL7Exception("Unsupported message type: " + m.getName() + ". Expected ORU^R01 ver 2.2");
		}
		ORU_R01 message = (ORU_R01) m;
		int commentReps = message.getPATIENT_RESULT().getORDER_OBSERVATION().currentReps("NTE");
		if (commentReps < 2) {
			throw new HL7Exception("Expected at least 2 comments in the NTE field.");
		}
		this.message = message;
	}
	
	private String decode(String string) {
		return new Hl7FormattedTextSupport().decode(string);
	}
	
	public String getOrderingProvider() {
		ORU_R01_PATIENT_RESULT patientResult = message.getPATIENT_RESULT();
		if (patientResult == null) {
			return null;
		}
		ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION();
		if (orderObservation == null) {
			return null;
		}
		OBR obr = orderObservation.getOBR();
		if (obr == null) {
			return null;
		}
		CN orderingProvider = obr.getOrderingProvider();
		if (orderingProvider == null) {
			return null;
		}
		if (orderingProvider.getIDNumber() != null) {
			return orderingProvider.getIDNumber().getValue();
		}
		return null;
	}
	
	public String getProviderNote() {
		try {
			NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE(2);
			return decode(nte.getComment(0).getValue());
		} catch (HL7Exception e) {
			throw new IllegalStateException("Unable to get comment field from the message", e);
		}
	}

	public String getRecommendations() {
		try {
			NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE(1);
			String commentValue = nte.getComment(0).getValue();
			return decode(commentValue);
		} catch (HL7Exception e) {
			throw new IllegalStateException("Unable to get comment field from the message", e);
		}
	}

}
