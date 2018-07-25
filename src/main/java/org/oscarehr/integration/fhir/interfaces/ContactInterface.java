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


import org.oscarehr.integration.fhir.resources.constants.ContactRelationship;
import org.oscarehr.integration.fhir.resources.constants.ContactType;


/**
 * An interface used for all contact types in Oscar. 
 */
public interface ContactInterface {

	public void setContactRelationship(ContactRelationship contactRelationship);
	public ContactRelationship getContactRelationship();
	
	public void setContactType(ContactType contactType);
	public ContactType getContactType();

	public Integer getId();
	
	public void setLocationCode( String locationCode ); 
	public String getLocationCode();
	
	public void setFirstName( String firstName );
	public String getFirstName();
	
	public void setLastName( String lastName );
	public String getLastName();
	
	public void setOrganizationName( String organizationName );
	public String getOrganizationName();
	
	public void setAddress( String address );
	public String getAddress();
	
	public void setAddress2( String address2 );
	public String getAddress2();
	
	public void setCity( String city );
	public String getCity();
	
	public void setProvince( String province );
	public String getProvince();
	
	public void setPostal( String postal );
	public String getPostal();
	
	public void setFax( String fax );
	public String getFax();
	
	public void setWorkPhone( String workphone );
	public String getWorkPhone();
	
	public void setPhone( String phone );
	public String getPhone();
	
	public void setProviderCpso( String providerCPSO );
	public String getProviderCpso();
}
