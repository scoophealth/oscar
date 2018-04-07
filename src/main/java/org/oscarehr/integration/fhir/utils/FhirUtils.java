package org.oscarehr.integration.fhir.utils;

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

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.codesystems.ContactPointSystem;
import org.hl7.fhir.dstu3.model.codesystems.IdentifierUse;

public final class FhirUtils {
	
	public static final List<String> fhirAddressLineToString( List<Address> addresses ) {
		List<String> addressList = null;
		for( Address address : addresses ) {
			if( addressList == null ) {
				addressList = new ArrayList<String>();
			}
			addressList.add( fhirAddressLineToString( address ) );
		}
		
		return addressList;
	}

	public static final String fhirAddressLineToString( Address address ) {
		List<StringType> addressLine = address.getLine();
		String street = "";
		for(StringType line : addressLine ) {
			street += line.asStringValue() + " ";
		}
		return street;
	}
	
	public static final String getFhirFax( List<ContactPoint> contactPointList ) {
		return loopContactPointList( contactPointList, ContactPointSystem.FAX );
	}

	public static final String getFhirPhone( List<ContactPoint> contactPointList  ) {
		return loopContactPointList( contactPointList, ContactPointSystem.PHONE );
	}

	public static final String getFhirEmail( List<ContactPoint> contactPointList  ) {
		return loopContactPointList( contactPointList, ContactPointSystem.EMAIL );
	}

	private static final String loopContactPointList( List<ContactPoint> contactPointList, ContactPointSystem contactPointSystem ) {
		String contact = "";
		for( ContactPoint contactPoint : contactPointList ) {
			contact = getContactPointBySystem( contactPoint, contactPointSystem );
		}
		return contact;
	}
	
	private static final String getContactPointBySystem( ContactPoint contactPoint, ContactPointSystem contactPointSystem ) {
		String contact = "";
		switch ( contactPointSystem ) {
		case EMAIL: contact = contactPoint.getValue();
			break;
		case FAX: contact = contactPoint.getValue(); 
			break;
		case NULL:
			break;
		case OTHER:
			break;
		case PAGER:
			break;
		case PHONE: contact = contactPoint.getValue();
			break;
		case SMS:
			break;
		case URL:
			break;
		default:
			break;		
		}
		return contact;
	}
	
	public static final String getFhirOfficialIdentifier( List<Identifier> identifierList  ) {
		return loopIdentifierList( identifierList, IdentifierUse.OFFICIAL );
	}
	
	public static final String getFhirSecondaryIdentifier( List<Identifier> identifierList  ) {
		return loopIdentifierList( identifierList, IdentifierUse.SECONDARY );
	}
	
	private static final String loopIdentifierList( List<Identifier> identifierList, IdentifierUse identifierUse ) {
		String id = "";
		for( Identifier identifier : identifierList ) {
			id = getIdentifierByIdentifierUse( identifier, identifierUse );
		}
		return id;
	}
	
	private static final String getIdentifierByIdentifierUse( Identifier identifier, IdentifierUse identifierUse ) {
		String id = "";
		switch( identifierUse ) {
		case NULL:
			break;
		case OFFICIAL: id = identifier.getValue();
			break;
		case SECONDARY: id = identifier.getValue();
			break;
		case TEMP:
			break;
		case USUAL:
			break;
		default:
			break;		
		}
		
		return id;
	}
	
}
