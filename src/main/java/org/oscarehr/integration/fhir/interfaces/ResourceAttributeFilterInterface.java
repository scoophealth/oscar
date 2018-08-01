package org.oscarehr.integration.fhir.interfaces;
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


import org.oscarehr.integration.fhir.resources.ResourceAttributeFilter;

public interface ResourceAttributeFilterInterface {
	
	public enum OptionalFHIRAttribute {
		address, 
		telecom, 
		fax, 
		oranizationName,
		annotation,
		nameExtension, 
		nameUse, 
		namePrefix, 
		workPhone, 
		email,
		qualification, 
		otherphone,
		dateIsEstimated,
		mrn,
		language
	}
	
	public enum MandatoryFHIRAttribute {
		practitionerNo,
		oneid
	} 
	
	/**
	 * Indicates if the passed in parameter is optional 
	 * true = include this parameter in every message.
	 * false = do not include this paramter in every message.
	 */
	public abstract boolean include( OptionalFHIRAttribute attribute );
	
	/**
	 * The OptionalFHIRAttribute optional selector works inversely in this situation. ie:
	 * 
	 * parameter = providerNo
	 * - default = true
	 * - optional = true 
	 * - mandatory = false
	 * 
	 * practitionerNo = false : this value is mandatory
	 * practitionerNo = true : this value is not mandatory
	 * empty : this value is not mandatory
	 */
	public abstract boolean isMandatory( MandatoryFHIRAttribute attribute );
	
	
	public abstract ResourceAttributeFilter getFilter( Class<?> targetResource );

}
