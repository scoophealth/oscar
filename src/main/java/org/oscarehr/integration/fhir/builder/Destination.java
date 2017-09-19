package org.oscarehr.integration.fhir.builder;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent;

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
/*
"destination": [
{
  "name": "DHIR",
  "endpoint": "https://wsgateway.prod.ehealthontario.ca/API/FHIR/Immunizations/v1/"
}
*/

/**
 * A class that references a message destination header information.
 * This Object builds a FHIR MessageDestinationComponent.
 * 
 * Future intentions of this class is for it to auto populate specific data 
 * according to its environment.
 *
 */
public final class Destination {
	
	private List<MessageDestinationComponent> messageDestinationComponents;
	
	public Destination() {
		setMessageDestinationComponents(new ArrayList<MessageDestinationComponent>());
	}

	public List<MessageDestinationComponent> getMessageDestinationComponents() {
		return messageDestinationComponents;
	}

	private void setMessageDestinationComponents(List<MessageDestinationComponent> messageDestinationComponents) {
		this.messageDestinationComponents = messageDestinationComponents;
	}
	
	/**
	 * Add a new destination Name and its associated endpoint. 
	 * Multiple destinations can be added. 
	 */
	public void addMessageDestination( String name, String endpoint ){
		MessageDestinationComponent messageDestinationComponent = new MessageDestinationComponent();
		messageDestinationComponent.setName(name);
		messageDestinationComponent.setEndpoint(endpoint);
		getMessageDestinationComponents().add(messageDestinationComponent);
	}

}
