package org.oscarehr.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ContactManager {

	@SuppressWarnings("unused")
	private Logger logger = MiscUtils.getLogger();
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
