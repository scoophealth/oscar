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


package org.oscarehr.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalContactDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ContactAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	static ContactDao contactDao = (ContactDao)SpringUtils.getBean("contactDao");
	static ProfessionalContactDao proContactDao = (ProfessionalContactDao)SpringUtils.getBean("professionalContactDao");
	static DemographicContactDao demographicContactDao = (DemographicContactDao)SpringUtils.getBean("demographicContactDao");
	static DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	static ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return manage(mapping,form,request,response);
	}

	public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String demographicNo = request.getParameter("demographic_no");
		List<DemographicContact> dcs = demographicContactDao.findByDemographicNoAndCategory(Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PERSONAL);
		for(DemographicContact dc:dcs) {
			if(dc.getType() == (DemographicContact.TYPE_DEMOGRAPHIC)) {
				dc.setContactName(demographicDao.getClientByDemographicNo(Integer.parseInt(dc.getContactId())).getFormattedName());
			}
			if(dc.getType() == (DemographicContact.TYPE_CONTACT)) {
				dc.setContactName(contactDao.find(Integer.parseInt(dc.getContactId())).getFormattedName());
			}
		}

		request.setAttribute("contacts", dcs);
		request.setAttribute("contact_num", dcs.size());

		List<DemographicContact> pdcs = demographicContactDao.findByDemographicNoAndCategory(Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PROFESSIONAL);
		for(DemographicContact dc:pdcs) {
			if(dc.getType() == (DemographicContact.TYPE_PROVIDER)) {
				dc.setContactName(providerDao.getProvider(dc.getContactId()).getFormattedName());
			}
			if(dc.getType() == (DemographicContact.TYPE_CONTACT)) {
				dc.setContactName(contactDao.find(Integer.parseInt(dc.getContactId())).getFormattedName());
			}
		}
		request.setAttribute("procontacts", pdcs);
		request.setAttribute("procontact_num", pdcs.size());

		if(request.getParameter("demographic_no") != null && request.getParameter("demographic_no").length()>0)
			request.setAttribute("demographic_no", request.getParameter("demographic_no"));

		return mapping.findForward("manage");
	}

	public ActionForward saveManage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	int demographicNo = Integer.parseInt(request.getParameter("demographic_no"));

    	int maxContact = Integer.parseInt(request.getParameter("contact_num"));
    	for(int x=1;x<=maxContact;x++) {
    		String id = request.getParameter("contact_"+x+".id");
    		if(id != null) {
    			String otherId = request.getParameter("contact_"+x+".contactId");
    			if(otherId.length() == 0 || otherId.equals("0")) {
    				continue;
    			}

    			DemographicContact c = new DemographicContact();
    			if(id.length()>0 && Integer.parseInt(id)>0) {
    				c = demographicContactDao.find(Integer.parseInt(id));
    			}

				c.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
    			c.setRole(request.getParameter("contact_"+x+".role"));
    			c.setType(Integer.parseInt(request.getParameter("contact_"+x+".type")));
    			c.setNote(request.getParameter("contact_"+x+".note"));
    			c.setContactId(otherId);
    			c.setCategory(DemographicContact.CATEGORY_PERSONAL);
    			if(request.getParameter("contact_"+x+".sdm") != null) {
    				c.setSdm("true");
    			} else {
    				c.setSdm("");
    			}
    			if(request.getParameter("contact_"+x+".ec") != null) {
    				c.setEc("true");
    			} else {
    				c.setEc("");
    			}
    			c.setFacilityId(LoggedInInfo.loggedInInfo.get().currentFacility.getId());
    			c.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
    			if(c.getId() == null)
    				demographicContactDao.persist(c);
    			else
    				demographicContactDao.merge(c);


    			//internal - do the reverse
    			if(c.getType() == 1) {
    				//check if it exists
    				if(demographicContactDao.find(Integer.parseInt(otherId),Integer.parseInt(request.getParameter("demographic_no"))).size() == 0) {

	    				c = new DemographicContact();
	        			if(id.length()>0 && Integer.parseInt(id)>0) {
	        				c = demographicContactDao.find(Integer.parseInt(id));
	        			}

	    				c.setDemographicNo(Integer.parseInt(otherId));
	    				String role = getReverseRole(request.getParameter("contact_"+x+".role"),demographicNo);
	    				if(role != null) {
		        			c.setRole(role);
		        			c.setType(Integer.parseInt(request.getParameter("contact_"+x+".type")));
		        			c.setNote(request.getParameter("contact_"+x+".note"));
		        			c.setContactId(request.getParameter("demographic_no"));
		        			c.setCategory(DemographicContact.CATEGORY_PERSONAL);
		        			c.setSdm("");
		        			c.setEc("");
		        			c.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		        			if(c.getId() == null)
		        				demographicContactDao.persist(c);
		        			else
		        				demographicContactDao.merge(c);
	    				}
    				}

    			}
    		}
    	}

    	//handle removes
    	String[] ids = request.getParameterValues("contact.delete");
    	if(ids != null) {
    		for(String id:ids) {
    			int contactId = Integer.parseInt(id);
    			DemographicContact dc = demographicContactDao.find(contactId);
    			dc.setDeleted(true);
    			demographicContactDao.merge(dc);
    		}
    	};

    	int maxProContact = Integer.parseInt(request.getParameter("procontact_num"));
    	for(int x=1;x<=maxProContact;x++) {
    		String id = request.getParameter("procontact_"+x+".id");
    		if(id != null) {
    			String otherId = request.getParameter("procontact_"+x+".contactId");
    			if(otherId.length() == 0 || otherId.equals("0")) {
    				continue;
    			}

    			DemographicContact c = new DemographicContact();
    			if(id.length()>0 && Integer.parseInt(id)>0) {
    				c = demographicContactDao.find(Integer.parseInt(id));
    			}

				c.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
    			c.setRole(request.getParameter("procontact_"+x+".role"));
    			c.setType(Integer.parseInt(request.getParameter("procontact_"+x+".type")));
    			c.setContactId(otherId);
    			c.setCategory(DemographicContact.CATEGORY_PROFESSIONAL);
    			c.setFacilityId(LoggedInInfo.loggedInInfo.get().currentFacility.getId());
    			c.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());

    			if(c.getId() == null)
    				demographicContactDao.persist(c);
    			else
    				demographicContactDao.merge(c);
    		}
    	}

    	//handle removes
    	ids = request.getParameterValues("procontact.delete");
    	if(ids != null) {
    		for(String id:ids) {
    			int contactId = Integer.parseInt(id);
    			DemographicContact dc = demographicContactDao.find(contactId);
    			dc.setDeleted(true);
    			demographicContactDao.merge(dc);
    		}
    	}
		return manage(mapping,form,request,response);
	}

	private String getReverseRole(String roleName, int targetDemographicNo) {
		Demographic demographic = demographicDao.getDemographicById(targetDemographicNo);

		if(roleName.equals("Mother") || roleName.equals("Father") || roleName.equals("Parent")) {
			if(demographic.getSex().equalsIgnoreCase("M")) {
				return "Son";
			} else {
				return "Daughter";
			}

		} else if(roleName.equals("Wife")) {
			return "Husband";

		} else if(roleName.equals("Husband")) {
			return "Wife";
		} else if(roleName.equals("Partner")) {
			return "Partner";
		} else if(roleName.equals("Son") || roleName.equals("Daughter")) {
			if(demographic.getSex().equalsIgnoreCase("M")) {
				return "Father";
			} else {
				return "Mother";
			}

		} else if(roleName.equals("Brother") || roleName.equals("Sister")) {
			if(demographic.getSex().equalsIgnoreCase("M")) {
				return "Brother";
			} else {
				return "Sister";
			}
		}

		return null;
	}

	public ActionForward addContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("cForm");
	}

	public ActionForward addProContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("pForm");
	}

	public ActionForward editContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("contact.id");

		if(id != null && id.length()>0) {
			Contact contact = contactDao.find(Integer.parseInt(id));
			request.setAttribute("contact", contact);
		}
		return mapping.findForward("cForm");
	}

	public ActionForward editProContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("pcontact.id");

		if(id != null && id.length()>0) {
			ProfessionalContact contact = proContactDao.find(Integer.parseInt(id));
			request.setAttribute("pcontact", contact);
		}
		return mapping.findForward("pForm");
	}

	public ActionForward saveContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaValidatorForm dform = (DynaValidatorForm)form;
		Contact contact = (Contact)dform.get("contact");
		String id = request.getParameter("contact.id");
		if(id != null && id.length()>0) {
			contact.setId(Integer.valueOf(id));
		}
		if(contact.getId() != null && contact.getId()>0) {
			contactDao.merge(contact);
		} else {
			contact.setId(null);
			contactDao.persist(contact);
		}
	   return mapping.findForward("cForm");
	}

	public ActionForward saveProContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaValidatorForm dform = (DynaValidatorForm)form;
		ProfessionalContact contact = (ProfessionalContact)dform.get("pcontact");
		String id = request.getParameter("pcontact.id");
		if(id != null && id.length()>0) {
			contact.setId(Integer.valueOf(id));
		}
		if(contact.getId() != null && contact.getId()>0) {
			proContactDao.merge(contact);
		} else {
			proContactDao.persist(contact);
		}
	   return mapping.findForward("pForm");
	}


	public static List<Contact> searchContacts(String searchMode, String orderBy, String keyword) {
		List<Contact> contacts = contactDao.search(searchMode, orderBy, keyword);
		return contacts;
	}

	public static List<ProfessionalContact> searchProContacts(String searchMode, String orderBy, String keyword) {
		List<ProfessionalContact> contacts = proContactDao.search(searchMode, orderBy, keyword);
		return contacts;
	}

	public static List<DemographicContact> fillContactNames(List<DemographicContact> contacts) {
		for(DemographicContact c:contacts) {
			if(c.getType() == DemographicContact.TYPE_DEMOGRAPHIC) {
				c.setContactName(demographicDao.getClientByDemographicNo(Integer.parseInt(c.getContactId())).getFormattedName());
			}
			if(c.getType() == DemographicContact.TYPE_PROVIDER) {
				c.setContactName(providerDao.getProvider(c.getContactId()).getFormattedName());
			}
			if(c.getType() == DemographicContact.TYPE_CONTACT) {
				c.setContactName(contactDao.find(Integer.parseInt(c.getContactId())).getFormattedName());
			}
		}

		return contacts;
	}
}
