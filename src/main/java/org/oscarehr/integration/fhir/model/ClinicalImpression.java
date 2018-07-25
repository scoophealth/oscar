package org.oscarehr.integration.fhir.model;
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

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.model.AbstractModel;


/**
 *  STU Note: Unlike many other resources, there is little prior art with regard to exchanging records of clinical assessments. 
 *  For this reason, this resource should be regarded as particularly prone to ongoing revision. In terms of scope and usage, 
 *  the Patient Care workgroup wishes to draw the attention of reviewers and implementers to the following issues:
 *
 *   When is an existing clinical impression revised, rather than a new one created (that references the existing one)? 
 *   How does that affect the status? what's the interplay between the status of the diagnosis and the status of the impression? 
 *   (e.g. for a 'provisional' impression, which bit is provisional?)
 *   This structure doesn't differentiate between a working and a final diagnosis. Given an answer to the previous question, should it?
 *   Further clarify around the relationship between care plan and impression is needed. Both answers to the previous questions 
 *   and ongoing discussions around revisions to the care plan will influence the design of clinical impression
 *   Should prognosis be represented, and if so, how much structure should it have?
 *   Should an impression reference other impressions that are related? (how related?)
 *   Investigations - the specification needs a good value set for the code for the group, and will be considering the 
 *   name "investigations" further
 *
 * For the time being, this class is written with the intention to map patient encounter notes or various text messages 
 * such as BORN transmissions. 
 * 
 */
public class ClinicalImpression extends AbstractOscarFhirResource< org.hl7.fhir.dstu3.model.ClinicalImpression, AbstractModel<?> > {

	private String annotation;
	private CaseManagementNote caseManagementNote;
	private String encodedBytes;
	
	
	/**
	 * Automatically encodes all string to Base64 encoding.
	 */
	public ClinicalImpression( String annotation ) {
		org.hl7.fhir.dstu3.model.ClinicalImpression clinicalImpression = new org.hl7.fhir.dstu3.model.ClinicalImpression();
		setAnnotation( annotation );
		setFhirResource( clinicalImpression );
		mapAttributes( clinicalImpression );
	}
	
	public ClinicalImpression( org.oscarehr.casemgmt.model.CaseManagementNote caseManagementNote ) {
		setCaseManagementNote( caseManagementNote );
		setFhirResource( new org.hl7.fhir.dstu3.model.ClinicalImpression() );
		// mapAttributes( getFhirResource(), getFilter( this.getClass() ) );
	}
	
	/**
	 * Convert to a basic Attachment resource with ContentType as text/plain, 
	 * a Title, and Data as Base64.
	 */
	public Attachment copyToAttachement( Attachment attachment ) {
		attachment.setContentType("text/plain");
		attachment.setTitle( getDescription() );
		attachment.setData( getEncodedBytes() );
		return attachment;
	}

	@Override
	protected void setId(org.hl7.fhir.dstu3.model.ClinicalImpression fhirResource) {
		if( getCaseManagementNote() != null ) {
			fhirResource.setId( getCaseManagementNote().getId() + "" );
		} else {
			super.setId( fhirResource );
		}
	}

	@Override
	protected void mapAttributes(org.hl7.fhir.dstu3.model.ClinicalImpression fhirResource ) {

		fhirResource.setStatus( ClinicalImpressionStatus.COMPLETED );

		if( ! getAnnotation().isEmpty() ) {
			//setEncodedBytes( getAnnotation() );
			this.encodedBytes = getAnnotation();
		} else if ( getCaseManagementNote() != null ) {
			setEncodedBytes( getCaseManagementNote().getNote() );
		}
		
		fhirResource.setSummary( getEncodedBytes().toString() );
		setDescription( fhirResource );
	}

	@Override
	protected void mapAttributes(AbstractModel<?> oscarResource) {
		// TODO This should convert an incoming clinical impression into a readable annotation for Oscar to consume
	}

	private void setDescription( org.hl7.fhir.dstu3.model.ClinicalImpression fhirResource ) {
		if ( getCaseManagementNote() != null ) {
			fhirResource.setDescription( getCaseManagementNote().getEncounter_type() );
		}
	}
	
	/**
	 * Overrides any pre-set description inside the ClinicalImpression resource.
	 */
	public void setDescription( String description ) {
		getFhirResource().setDescription( description );
	}
	
	public String getDescription() {
		return getFhirResource().getDescription();
	}

	public String getAnnotation() {
		if( annotation == null ) {
			return "";
		}
		return annotation;
	}

	private void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public CaseManagementNote getCaseManagementNote() {
		return caseManagementNote;
	}

	private void setCaseManagementNote(CaseManagementNote caseManagementNote) {
		this.caseManagementNote = caseManagementNote;
	}
	
	private byte[] getEncodedBytes() {
		byte[] bytes = null;
		try {
			bytes = encodedBytes.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			MiscUtils.getLogger().error( "Error encoding attachment. " );
		}
		return bytes;
	}

	private void setEncodedBytes( String string ) {		
		try {
			encodedBytes = DatatypeConverter.printBase64Binary( string.getBytes("UTF-8") );
		} catch (UnsupportedEncodingException e) {
			MiscUtils.getLogger().error( "Error encoding attachment. " );
		}
	}


}
