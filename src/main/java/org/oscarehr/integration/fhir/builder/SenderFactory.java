package org.oscarehr.integration.fhir.builder;

import org.oscarehr.common.model.Clinic;
import org.oscarehr.integration.fhir.model.Sender;

import oscar.OscarProperties;

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
public final class SenderFactory {
	
	// TODO these global variables will not remain here.
	
	private static String vendorName = "Oscar EMR";
	private static String softwareName = "Oscar";
	private static String buildName = OscarProperties.getInstance().getProperty("buildtag", "[OSCAR BUILD]");
	private static String senderEndpoint = OscarProperties.getInstance().getProperty("ws_endpoint_url_base", "[OSCAR ENDPOINT]");
	
	// TODO also set up a PHU resource.
		
	//TODO JPA Clinic resource here. The test resource below is temporary. 
	
	private static Clinic clinic = new Clinic() {{
		this.setId( 4321 );
		this.setClinicAddress("123 Clinic Street");
		this.setClinicCity("Vancouver");
		this.setClinicProvince("BC");
		this.setClinicPhone("778-567-3445");
		this.setClinicFax("778-343-3453");
		this.setClinicName("Test Medical Clinic"); 
	}};

	public static final Sender getSender() {
		Sender sender = new Sender(vendorName,softwareName,buildName,senderEndpoint);
		sender.addClinic( clinic );
		return sender;
	}

}
