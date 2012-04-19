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


package org.oscarehr.common.service;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.util.SpringUtils;

public class ContactManager {

	private ContactDao contactDao = (ContactDao)SpringUtils.getBean("contactDao");
	private DemographicContactDao demographicContactDao = (DemographicContactDao)SpringUtils.getBean("demographicContactDao");

	public List<Contact> getContactsByDemographicNo(int demographicNo) {
		List<Contact> contacts = new ArrayList<Contact>();
		List<DemographicContact> dContacts = demographicContactDao.findByDemographicNo(demographicNo);
		for(DemographicContact dContact:dContacts) {
			contacts.add(contactDao.find(dContact.getContactId()));
		}
		return contacts;
	}
}
