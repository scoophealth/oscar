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
package org.oscarehr.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.ContactSpecialtyDao;
import org.oscarehr.common.dao.ContactTypeDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ContactSpecialty;
import org.oscarehr.common.model.ContactType;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;

/**
 * this class contains the utility methods for creating generic Contact objects and adds these
 * objects to a DemographicContact object as contact details.
 * This is a little backwards in order to combine several contact systems into one. Eventually 
 * we could live without this class.
 */
public class HealthCareTeamCreator {
	
	static Logger logger = MiscUtils.getLogger();
	
	public static List<DemographicContact> addContactDetailsToDemographicContact(List<DemographicContact>  demographicContactList) {
		return fillContactNames(demographicContactList);
	}
	
	public static DemographicContact addContactDetailsToDemographicContact(DemographicContact  demographicContact) {
		return fillContactName(demographicContact);
	}

	private static List<DemographicContact> fillContactNames(List<DemographicContact> demographicContacts) {

		if( demographicContacts == null || demographicContacts.size() < 1 ) {
			return demographicContacts;
		}
		
		for( DemographicContact demographicContact : demographicContacts ) {	
			fillContactName( demographicContact );
		}

		return demographicContacts;
	}
	
	private static DemographicContact fillContactName( DemographicContact demographicContact ) {
		if(demographicContact == null) {
			return demographicContact;
		}
		Provider provider;
		Contact contact; 
		ProfessionalSpecialist professionalSpecialist;
		ContactSpecialty specialty;
		String providerFormattedName = ""; 
		ContactDao contactDao = SpringUtils.getBean(ContactDao.class);
		ContactSpecialtyDao contactSpecialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);
		ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
		ProfessionalSpecialistDao professionalSpecialistDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		String demographicContactRole = ( demographicContact.getRole() ).trim();
		Integer roleid = null;
		
		if( StringUtils.isNumeric(demographicContactRole) ) {		
			roleid = oscar.util.ConversionUtils.fromIntString(demographicContactRole);
		} else {
			demographicContact.setRole( demographicContactRole );
		}
		
		if( roleid != null ) {
			specialty = contactSpecialtyDao.find( roleid );
			demographicContact.setRole( specialty.getSpecialty() );
		}
		
		//give preference to this.
		if(demographicContact.getContactTypeId() != null && demographicContact.getContactTypeId().intValue() != 0) {
			ContactTypeDao dao = SpringUtils.getBean(ContactTypeDao.class);
			ContactType ct = dao.find(demographicContact.getContactTypeId());
			if(ct != null) {
				demographicContact.setRole(ct.getName());
			}
		}
		

		if( demographicContact.getType() == DemographicContact.TYPE_DEMOGRAPHIC ) {
			Demographic demographic = demographicDao.getDemographic(demographicContact.getContactId());
			demographicContact.setContactName(demographic.getFormattedName());
		}
		
		if( demographicContact.getType() == DemographicContact.TYPE_PROVIDER ) {
			provider = providerDao.getProvider( demographicContact.getContactId() );
			if(provider != null){
				providerFormattedName = provider.getFormattedName();
			}
			if(StringUtils.isBlank(providerFormattedName)) {
				providerFormattedName = "Error: Contact Support";
				logger.error("Formatted name for provder was not avaialable. Contact number: " + demographicContact.getContactId());
			}
			demographicContact.setContactName(providerFormattedName);
			contact = new ProfessionalContact();
			contact.setWorkPhone("internal");
			contact.setFax("internal");
			demographicContact.setDetails(contact);
		}
		
		if( demographicContact.getType() == DemographicContact.TYPE_CONTACT ) {			
			contact = contactDao.find( Integer.parseInt( demographicContact.getContactId() ) );			
			demographicContact.setContactName( contact.getFormattedName() );
			demographicContact.setDetails(contact);
		}
		
		if( demographicContact.getType() == DemographicContact.TYPE_PROFESSIONALSPECIALIST ) {
			professionalSpecialist = professionalSpecialistDao.find( Integer.parseInt( demographicContact.getContactId() ) );
			demographicContact.setContactName( professionalSpecialist.getFormattedName() );				
			contact = buildContact( professionalSpecialist );
			demographicContact.setDetails(contact);
		}

		return demographicContact;
	}
	
	public static final List<Contact> buildContact(final List<?> contact) {
		List<Contact> contactlist = new ArrayList<Contact>();
		Contact contactitem;
		Iterator<?> contactiterator = contact.iterator();
		while( contactiterator.hasNext() ) {
			contactitem = buildContact( contactiterator.next() );
			contactlist.add( contactitem );
		}		
		return contactlist;
	}

	/**
	 * Return a generic Contact class from any other class of 
	 * contact. 
	 * @return
	 */
	public static final Contact buildContact(final Object contactobject) {
		ProfessionalContact contact = new ProfessionalContact();
		
		Integer id = null;
		String systemId = "";
		String firstName = ""; 
		String lastName = "";
		String address = "";
		String address2 = "";
		String city = "";
		String country = "";
		String postal = "";
		String province = "";
		boolean deleted = false;
		String cellPhone = "-";
		String workPhone = "";
		String email = "";
		String residencePhone = "";
		String fax = ""; 
		String specialty = "";
		String cpso = "";
		
		if(contactobject instanceof ProfessionalSpecialist) {
			
			ProfessionalSpecialist professionalSpecialist = (ProfessionalSpecialist) contactobject;
			
			// assuming that the address String is always csv.
			address = professionalSpecialist.getStreetAddress();
			
			if( address.contains(",") ) {		
				String[] addressArray = address.split(",");
				address = addressArray[0].trim();
				if(addressArray.length > 3) {
					city = addressArray[1].trim();
					province = addressArray[2].trim();
					country = addressArray[3].trim();
				} else {
					province = addressArray[1].trim();
					country = addressArray[2].trim();
				}
			}
			
			// mark the contact with Specialist Type - Later parsed in client Javascript.
			// using SystemId as a transient parameter only.
			systemId = DemographicContact.TYPE_PROFESSIONALSPECIALIST+"";
			id = professionalSpecialist.getId();
			firstName = professionalSpecialist.getFirstName();
			lastName = professionalSpecialist.getLastName();
			email = professionalSpecialist.getEmailAddress();
			residencePhone = professionalSpecialist.getPhoneNumber();
			workPhone = professionalSpecialist.getPhoneNumber(); 
			fax = professionalSpecialist.getFaxNumber();
			cpso = professionalSpecialist.getReferralNo();
			
		}
		
		contact.setId(id);
		contact.setSystemId(systemId);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setAddress(address);
		contact.setAddress2(address2);
		contact.setCity(city);
		contact.setCountry(country);
		contact.setPostal(postal);
		contact.setProvince(province);
		contact.setDeleted(deleted);
		contact.setCellPhone(cellPhone);
		contact.setWorkPhone(workPhone);
		contact.setResidencePhone(residencePhone);
		contact.setFax(fax);
		contact.setEmail(email);
		contact.setSpecialty(specialty);
		contact.setCpso(cpso);

		return contact;
	}
	
	/**
	 * Sort Contacts Alpha
	 */
	public static Comparator<Contact> byLastName = new Comparator<Contact>() {
		public int compare(Contact contact1, Contact contact2) {
			String lastname1 = contact1.getLastName().toUpperCase();
			String lastname2 = contact2.getLastName().toUpperCase();
			return lastname1.compareTo(lastname2);
		}
	};
	
}