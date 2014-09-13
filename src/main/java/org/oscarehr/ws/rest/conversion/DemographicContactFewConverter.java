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
package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.ws.rest.to.model.DemographicContactFewTo1;
import org.springframework.stereotype.Component;

@Component
public class DemographicContactFewConverter {
	
	public DemographicContactFewTo1 getAsTransferObject(DemographicContact d, Object obj) throws ConversionException {
		DemographicContactFewTo1 t = new DemographicContactFewTo1();
		
		t.setRole(d.getRole());
		t.setEc(d.getEc());
		t.setSdm(d.getSdm());
		t.setCategory(d.getCategory());
		
		if (d.getType()==DemographicContact.TYPE_DEMOGRAPHIC) {
			Demographic demographic = (Demographic) obj;
			t.setFirstName(demographic.getFirstName());
			t.setLastName(demographic.getLastName());
			
			if (isPreferredPhone(demographic.getPhone())) {
				t.setPhone(demographic.getPhone());
			}
			else if (isPreferredPhone(demographic.getPhone2())) {
				t.setPhone(demographic.getPhone2());
			}
			else {
				if (demographic.getPhone()!=null) t.setPhone(demographic.getPhone());
				else if (demographic.getPhone2()!=null) t.setPhone(demographic.getPhone2());
			}
		}
		else if (d.getType()==DemographicContact.TYPE_PROVIDER) {
			Provider provider = (Provider) obj;
			t.setFirstName(provider.getFirstName());
			t.setLastName(provider.getLastName());
			t.setPhone(provider.getPhone());
		}
		else if (d.getType()==DemographicContact.TYPE_PROFESSIONALSPECIALIST) {
			ProfessionalSpecialist specialist = (ProfessionalSpecialist) obj;
			t.setFirstName(specialist.getFirstName());
			t.setLastName(specialist.getLastName());
			t.setPhone(specialist.getPhoneNumber());
		}
		else if (d.getType()==DemographicContact.TYPE_CONTACT) {
			Contact contact = (Contact) obj;
			t.setFirstName(contact.getFirstName());
			t.setLastName(contact.getLastName());
			if (contact.getResidencePhone()!=null) t.setPhone(contact.getResidencePhone());
			else if (contact.getWorkPhone()!=null) t.setPhone(contact.getWorkPhone());
			else if (contact.getCellPhone()!=null) t.setPhone(contact.getCellPhone());
		}
		return t;
	}

	private boolean isPreferredPhone(String phone) {
		if (phone!=null) {
			if (phone.length()>0) {
				if (phone.charAt(phone.length()-1)=='*') return true;
			}
		}
		return false;
	}
}
