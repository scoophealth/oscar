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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.ContactSpecialtyDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProfessionalContactDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ContactSpecialty;
import org.oscarehr.common.model.ContactType;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.ProgramContactType;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.ContactManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.HealthCareTeamCreator;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oscar.OscarProperties;

public class ContactAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();
	private static ContactDao contactDao = (ContactDao)SpringUtils.getBean("contactDao");
	private static ProfessionalContactDao proContactDao = (ProfessionalContactDao)SpringUtils.getBean("professionalContactDao");
	private static DemographicContactDao demographicContactDao = (DemographicContactDao)SpringUtils.getBean("demographicContactDao");
	private static DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	private static ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	private static ProfessionalSpecialistDao professionalSpecialistDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
	private static ContactSpecialtyDao contactSpecialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);
	private static DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private static ContactManager contactManager = SpringUtils.getBean(ContactManager.class);
	
	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return manage(mapping,form,request,response);
	}

	public ActionForward manage(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		String demographicNo = request.getParameter("demographic_no");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", demographicNo)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		//List<DemographicContact> dcs = demographicContactDao.findByDemographicNoAndCategory(Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PERSONAL);
		List<DemographicContact> dcs = demographicManager.getDemographicContacts(loggedInInfo, Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PERSONAL);
		
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

		//List<DemographicContact> pdcs = demographicContactDao.findByDemographicNoAndCategory(Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PROFESSIONAL);
		List<DemographicContact> pdcs = demographicManager.getDemographicContacts(loggedInInfo,Integer.parseInt(demographicNo),DemographicContact.CATEGORY_PROFESSIONAL);
		
		for(DemographicContact dc:pdcs) {
			// workaround: UI allows to enter specialist with  a type that is not set, prevent NPE and display 'Unknown' as name
			// user then can choose to delete this entry
			String contactName = null;
			if(dc.getType() == (DemographicContact.TYPE_PROVIDER)) {
				Provider provider = providerDao.getProvider(dc.getContactId()); 
				contactName = (provider == null)?"Unknown":provider.getFormattedName();
			}
			if(dc.getType() == (DemographicContact.TYPE_CONTACT)) {
				Contact contact = contactDao.find(Integer.parseInt(dc.getContactId()));
				contactName = (contact == null)?"Unknown":contact.getFormattedName();
			}
			if(dc.getType() == (DemographicContact.TYPE_PROFESSIONALSPECIALIST)) {
				ProfessionalSpecialist profSpecialist = professionalSpecialistDao.find(Integer.parseInt(dc.getContactId()));
				contactName = (profSpecialist == null)?"Unknown":profSpecialist.getFormattedName();
			}
			StringUtils.trimToEmpty(contactName);
			dc.setContactName(contactName);
		}
		request.setAttribute("procontacts", pdcs);
		request.setAttribute("procontact_num", pdcs.size());

		if(request.getParameter("demographic_no") != null && request.getParameter("demographic_no").length()>0)
			request.setAttribute("demographic_no", request.getParameter("demographic_no"));

		return mapping.findForward("manage");
	}

	public ActionForward saveManage(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		int demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
    	int maxContact = Integer.parseInt(request.getParameter("contact_num"));
    	String forward = "windowClose";
    	String postMethod = request.getParameter("postMethod");
   	
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "w", String.valueOf(demographicNo))) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
    	
    	if( "ajax".equalsIgnoreCase( postMethod ) || "ajax2".equalsIgnoreCase( postMethod ) ) {
    		forward = postMethod;
    		request.setAttribute("ajax", true);
    	}
    	
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
    			
    			String strContactTypeId = request.getParameter("contact_"+x+".contactTypeId");
    			if(StringUtils.isNotEmpty(strContactTypeId)) {
    				c.setContactTypeId(Integer.parseInt(strContactTypeId));
    			}
    		
				c.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
    			c.setRole(request.getParameter("contact_"+x+".role"));
    			
    			if (request.getParameter("contact_"+x+".type") != null) {
    			    c.setType(Integer.parseInt(request.getParameter("contact_"+x+".type")));
    			}
    			c.setNote(request.getParameter("contact_"+x+".note"));
    			c.setContactId(otherId);
    			
    			c.setCategory(request.getParameter("contact_"+x+".category"));
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
    			c.setFacilityId(loggedInInfo.getCurrentFacility().getId());
    			c.setCreator(loggedInInfo.getLoggedInProviderNo());
    			
    			if(request.getParameter("contact_"+x+".consentToContact").equals("1")) {
    				c.setConsentToContact(true);
    			} else {
    				c.setConsentToContact(false);
    			}
    			
    			if(request.getParameter("contact_"+x+".programId") != null && !request.getParameter("contact_"+x+".programId").equals("0") && !request.getParameter("contact_"+x+".programId").equals("undefined")) {
    				c.setProgramNo(Integer.parseInt(request.getParameter("contact_"+x+".programId")));
    			} else {
    				c.setProgramNo(null);
    			}
    			
    			if(request.getParameter("contact_"+x+".active").equals("1")) {
    				c.setActive(true);
    			} else {
    				c.setActive(false);
    			}
    			
    			
    			if(c.getId() == null) {
    				demographicContactDao.persist(c);
    			} else {
    				demographicContactDao.merge(c);
    			}

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
		        			c.setCreator(loggedInInfo.getLoggedInProviderNo());
		        			
		        			if(c.getId() == null)
		        				demographicContactDao.persist(c);
		        			else
		        				demographicContactDao.merge(c);
	    				}
    				}

    			}
    		}
    	}

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
    			if (request.getParameter("procontact_"+x+".type") != null) {
    			    c.setType(Integer.parseInt(request.getParameter("procontact_"+x+".type")));
    			}
    			
    			String strContactTypeId = request.getParameter("procontact_"+x+".contactTypeId");
    			if(StringUtils.isNotEmpty(strContactTypeId)) {
    				c.setContactTypeId(Integer.parseInt(strContactTypeId));
    			}
    			c.setContactId(otherId);
    			c.setCategory(DemographicContact.CATEGORY_PROFESSIONAL);
    			c.setFacilityId(loggedInInfo.getCurrentFacility().getId());
    			c.setCreator(loggedInInfo.getLoggedInProviderNo());
    			
    			if( "1".equals(request.getParameter("procontact_"+x+".consentToContact")) ) {
    				c.setConsentToContact(true);
    			} else {
    				c.setConsentToContact(false);
    			}
    			
    			if(request.getParameter("procontact_"+x+".programId") != null && !request.getParameter("procontact_"+x+".programId").equals("0") && !request.getParameter("procontact_"+x+".programId").equals("undefined")) {
    				c.setProgramNo(Integer.parseInt(request.getParameter("procontact_"+x+".programId")));
    			} else {
    				c.setProgramNo(null);
    			}
    			
    			if("1".equals( request.getParameter("procontact_"+x+".active") )) {
    				c.setActive(true);
    			} else {
    				c.setActive(false);
    			}
    			
    			if(c.getId() == null) {
    				demographicContactDao.persist(c);
    			} else {
    				demographicContactDao.merge(c);
    			}
    		}
    	}

    	//handle removes
    	removeContact(mapping, form, request, response);

		return mapping.findForward( forward );
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
	
	public ActionForward removeContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {

		ArrayList<String> arrayListIds = null;
		String[] ids = null;
		String[] proContactIds = request.getParameterValues("procontact.delete");
		String[] contactIds = request.getParameterValues("contact.delete");
		String postMethod = request.getParameter("postMethod");
		String removeSingleId = request.getParameter("contactId");
		ActionForward actionForward = null;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
    	if( "ajax".equalsIgnoreCase( postMethod ) || "ajax2".equalsIgnoreCase( postMethod ) ) {
    		actionForward = mapping.findForward( postMethod );
    		request.setAttribute("ajax", true);
    	}
    	
    	if(removeSingleId != null) {
    		ids = new String[]{removeSingleId};
    	}
    	
		if( proContactIds != null || contactIds != null ) {
			arrayListIds = new ArrayList<String>(); 
			
			if(proContactIds != null) {
				arrayListIds.addAll(Arrays.asList( proContactIds ) );
			}
			
			if(contactIds != null) {
				arrayListIds.addAll(Arrays.asList( contactIds ) );
			}
			
			ids = arrayListIds.toArray(new String[arrayListIds.size()]);
		}
		
    	if( ids != null ) {
    		int contactId;
    		for( String id : ids ) {
    			contactId = Integer.parseInt(id);
    			DemographicContact dc = demographicContactDao.find( contactId );
    			dc.setDeleted(true);
    			demographicContactDao.merge(dc);
    		}
    	}
    	
    	return actionForward; 

	}

	public ActionForward addContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		
		String keyword = request.getParameter("keyword");
		
		
		  org.apache.struts.validator.DynaValidatorForm contactForm = (org.apache.struts.validator.DynaValidatorForm)form;
		  Contact cForm = (Contact) contactForm.get("contact");
		  if(keyword != null) {
			  String[] parts = keyword.split(",");
			  if(parts.length == 1) {
				  cForm.setLastName(parts[0]);
			  }
			  if(parts.length == 2) {
				  cForm.setLastName(parts[0]);
				  cForm.setFirstName(parts[1]);
			  }
			  
		  }
		  
		  request.setAttribute("contact", cForm);
		  
		return mapping.findForward("cForm");
	}

	public ActionForward addProContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		ContactSpecialtyDao specialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);
		List<ContactSpecialty> specialties = specialtyDao.findAll();
		OscarProperties prop = OscarProperties.getInstance();
		request.setAttribute( "region", prop.getProperty("billregion") );
		request.setAttribute( "specialties", specialties );
		request.setAttribute( "pcontact.lastName", request.getParameter("keyword") );
		request.setAttribute( "contactRole", request.getParameter("contactRole")  );
		
		String keyword = request.getParameter("keyword");
		
		
		org.apache.struts.validator.DynaValidatorForm contactForm = (org.apache.struts.validator.DynaValidatorForm)form;
		ProfessionalContact cForm = (ProfessionalContact) contactForm.get("pcontact");
		if(keyword != null) {
			  String[] parts = keyword.split(",");
			  if(parts.length == 1) {
				  cForm.setLastName(parts[0]);
			  }
			  if(parts.length == 2) {
				  cForm.setLastName(parts[0]);
				  cForm.setFirstName(parts[1]);
			  }
			  
		}
		  
		request.setAttribute("pcontact", cForm);
		  
		return mapping.findForward("pForm");
	}
	
	public ActionForward displayHealthCareTeam(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String demographicNo = request.getParameter("demographicNo");
		String forward = null;
		if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", demographicNo) ) {
			
			Demographic demographic = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);
			List<DemographicContact> demographicContacts = demographicManager.getHealthCareTeam(LoggedInInfo.getLoggedInInfoFromSession(request),Integer.parseInt(demographicNo));
			ContactSpecialtyDao specialtyDao = SpringUtils.getBean( ContactSpecialtyDao.class );
			List<ContactSpecialty> contactSpecialties = specialtyDao.findAll();
			
			logger.debug("Displaying demographic contacts for " + demographicNo + ": " + demographicContacts);
			
			request.setAttribute("demographicContacts", demographicContacts);
			request.setAttribute("demographic", demographic);
			request.setAttribute("specialty", contactSpecialties);
			forward = "healthCareTeam";
			
		} else {
			// do nothing
		}
		
		return mapping.findForward(forward);
	}
	
	public ActionForward editHealthCareTeam(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String demographicContactId = request.getParameter("contactId");
		DemographicContact demographicContact = null;
		Integer contactType = null;
		String contactCategory = "";
		String contactId = "";
		ProfessionalSpecialist professionalSpecialist = null;
		String contactRole = "";
		List<ContactSpecialty> specialtyList = null;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		
		if( StringUtils.isNotBlank( demographicContactId ) ) {
			
			specialtyList = contactSpecialtyDao.findAll();						
			demographicContact = demographicContactDao.find( Integer.parseInt( demographicContactId ) );
			contactType = demographicContact.getType();
			contactCategory = demographicContact.getCategory();
			contactId = demographicContact.getContactId();
			contactRole = demographicContact.getRole();
			
			if( DemographicContact.CATEGORY_PROFESSIONAL.equalsIgnoreCase( contactCategory ) ) {
				
				if( DemographicContact.TYPE_CONTACT == contactType ) {
					
					ProfessionalContact contact = proContactDao.find( Integer.parseInt( contactId ) );
					request.setAttribute("pcontact", contact);

				} else if( DemographicContact.TYPE_PROFESSIONALSPECIALIST == contactType ) {
					
					professionalSpecialist = professionalSpecialistDao.find( Integer.parseInt( contactId ) );
					
					if( professionalSpecialist != null ) { 
						request.setAttribute( "pcontact", HealthCareTeamCreator.buildContact( professionalSpecialist ) );
					}
				}			
			}
			
			// specialty should be from the relational table via specialty id.
			// converting back to id here.
			if( ! StringUtils.isNumeric( contactRole ) ) {
				String specialtyDesc;
				for( ContactSpecialty specialty : specialtyList ) {
					specialtyDesc = specialty.getSpecialty().trim();
					if( specialtyDesc.equalsIgnoreCase( contactRole ) ) {
						 contactRole = specialty.getId()+"";
					}
				}
			}

			request.setAttribute( "specialties", specialtyList );			
			request.setAttribute( "contactRole", contactRole );
			request.setAttribute( "demographicContactId", demographicContactId );
		}
		
		return mapping.findForward("pForm");
	}
	
	public ActionForward editContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("contact.id");
		Contact contact = null;
		if(StringUtils.isNotBlank(id)) {
			id = id.trim();
			contact = contactDao.find(Integer.parseInt(id));
			request.setAttribute("contact", contact);
		}
		return mapping.findForward("cForm");
	}

	public ActionForward editProContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("pcontact.id");
		ProfessionalContact contact = null;
		if( StringUtils.isNotBlank(id) ) {
			id = id.trim();
			contact = proContactDao.find(Integer.parseInt(id));
			request.setAttribute("pcontact", contact);
		}
		return mapping.findForward("pForm");
	}

	public ActionForward saveContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		DynaValidatorForm dform = (DynaValidatorForm)form;
		Contact contact = (Contact)dform.get("contact");
		String id = request.getParameter("contact.id");
		if(id != null && id.length()>0) {
			Contact savedContact = contactDao.find(Integer.parseInt(id));
			if(savedContact != null) {
				BeanUtils.copyProperties(contact, savedContact, new String[]{"id"});
				if(savedContact.getProgramNo() != null && savedContact.getProgramNo().intValue() == 0) {
					savedContact.setProgramNo(null);
				}
				savedContact.setLastName(savedContact.getLastName().trim());
				savedContact.setFirstName(savedContact.getFirstName().trim());
				contactDao.merge(savedContact);
			}
		}
		else {
			contact.setId(null);
			if(contact.getProgramNo() != null && contact.getProgramNo().intValue() == 0) {
				contact.setProgramNo(null);
			}
			contact.setLastName(contact.getLastName().trim());
			contact.setFirstName(contact.getFirstName().trim());
			contactDao.persist(contact);
		}
	   return mapping.findForward("cForm");
	}

	public ActionForward saveProContact(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {		
		DynaValidatorForm dform = (DynaValidatorForm)form;
		ProfessionalContact contact = (ProfessionalContact) dform.get("pcontact");
		
		String id = request.getParameter("pcontact.id");
		String demographicContactId = request.getParameter("demographicContactId");
		DemographicContact demographicContact = null;
		Integer contactType = null; // this needs to be null as there are -1 and 0 contact types
		String contactRole = ""; 
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		if(id != null && id.length() > 0) {
			
			logger.info("Editing a current Professional Contact with id " + contact.getId());
			
			// changes for the DemographicContact table
			if( StringUtils.isNumeric( demographicContactId )) {
				demographicContact = demographicContactDao.find( Integer.parseInt( demographicContactId ) );		
				contactType = demographicContact.getType();
			}
			
			// changes for the ProfessionalSpecialist table
			if( DemographicContact.TYPE_PROFESSIONALSPECIALIST == contactType ) { 
				// convert from a ProfessionalContact to ProfessionalSpecialist				
				ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.find( Integer.parseInt( id ) );

				String address =  contact.getAddress().trim() + " " + 
						contact.getAddress2().trim() + " " +
						contact.getPostal().trim() + ", " +
						contact.getCity().trim() + ", " + 
						contact.getProvince().trim()  + ", " +
						contact.getCountry().trim();
				
				professionalSpecialist.setStreetAddress( address );
				professionalSpecialist.setFirstName( contact.getFirstName() );
				professionalSpecialist.setLastName( contact.getLastName() );				
				professionalSpecialist.setEmailAddress( contact.getEmail() );
				professionalSpecialist.setPhoneNumber( contact.getWorkPhone() ); 
				professionalSpecialist.setFaxNumber( contact.getFax() );
				professionalSpecialist.setReferralNo( contact.getCpso() );
				
				professionalSpecialistDao.merge( professionalSpecialist );
			
			// changes for the Contact table.
			} else {
			
				ProfessionalContact savedContact = proContactDao.find( Integer.parseInt( id ) );
				if(savedContact != null) {
					
					BeanUtils.copyProperties( contact, savedContact, new String[]{"id"} );
					
					if(savedContact.getProgramNo() != null && savedContact.getProgramNo().intValue() == 0) {
						savedContact.setProgramNo(null);
					}
					
					proContactDao.merge( savedContact );
					contactRole = savedContact.getSpecialty();
				}
			}
		
		// persist by default for new contacts.
		} else {
			
			logger.info("Saving a new Professional Contact with id " + contact.getId());
			
			if(contact.getProgramNo() != null && contact.getProgramNo().intValue() == 0) {
				contact.setProgramNo(null);
			}
			
			proContactDao.persist(contact);
			
			contactRole = contact.getSpecialty();
			id = contact.getId() + "";
			
		}
		
		// slingshot the DemographicContact details back to the request.
		// the saveManage method is to difficult to re-engineer
		request.setAttribute("demographicContactId", demographicContactId);
		request.setAttribute( "contactId", id );
		request.setAttribute( "contactRole", contactRole );
		request.setAttribute( "contactType", contactType );
		request.setAttribute( "contactName", contact.getFormattedName() );	
		
	   return mapping.findForward("pForm");
	}
	
	/**
	 * Assigns the given provider with a Most Responsible Provider status. 
	 */
	public ActionForward setMRP( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response ) {

		String contactId = request.getParameter("contactId");
		String forward = null;
		
		if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", contactId) ) {
			
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			DemographicContact demographicContactMRP = demographicManager.getHealthCareMemberbyId(
					loggedInInfo, 
					Integer.parseInt(contactId));
			List<DemographicContact> demographicContacts = null;				
			if( demographicContactMRP != null ) {
				demographicContacts = demographicManager.getHealthCareTeam( loggedInInfo, demographicContactMRP.getDemographicNo() );
			}
	 
			// set all contacts in this demographic group to false to ensure no duplicates are made. 
			if( demographicContacts != null ) {
				for( DemographicContact demographicContact : demographicContacts ) {
					if( demographicContact.isMrp() ) {
						demographicContact.setMrp(Boolean.FALSE);
						demographicContactDao.merge( demographicContact );
					}			
				}
			}
			
			demographicContactMRP.setMrp(Boolean.TRUE);
			demographicContactDao.merge( demographicContactMRP );
			forward = "healthCareTeam";
		}
		
		return mapping.findForward( forward );
	}
	
	/**
	 * Return a list of of all the contacts in Oscar's database.
	 * Contact, Professional Contact, and Professional Specialists
	 * @param searchMode
	 * @param orderBy
	 * @param keyword
	 * @return
	 */
	public static List<Contact> searchAllContacts(String searchMode, String orderBy, String keyword, String programNo, String providerNo, String relatedTo) {
		List<Contact> contacts = new ArrayList<Contact>();
		List<ProfessionalSpecialist> professionalSpecialistContact = professionalSpecialistDao.search(keyword);		
		
		// if there is a future in adding personal contacts.
		// contacts.addAll( contactDao.search(searchMode, orderBy, keyword) );		
		contacts.addAll( proContactDao.search(searchMode, orderBy, keyword, programNo, providerNo, relatedTo) );		
		contacts.addAll( HealthCareTeamCreator.buildContact( professionalSpecialistContact ) );
		
		Collections.sort(contacts, HealthCareTeamCreator.byLastName);

		return contacts;
	}


	public static List<Contact> searchContacts(String searchMode, String orderBy, String keyword, String programNo, String providerNo, String relatedTo) {
		List<Contact> contacts = contactDao.search(searchMode, orderBy, keyword, programNo, providerNo, relatedTo);
		return contacts;
	}

	public static List<ProfessionalContact> searchProContacts(String searchMode, String orderBy, String keyword, String programNo, String providerNo, String relatedTo) {
		List<ProfessionalContact> contacts = proContactDao.search(searchMode, orderBy, keyword, programNo, providerNo, relatedTo);
		return contacts;
	}
	
	public static List<ProfessionalSpecialist> searchProfessionalSpecialists(String keyword) {
		List<ProfessionalSpecialist> contacts = professionalSpecialistDao.search(keyword);
		return contacts;
	}

	
	@Deprecated
	/**
	 * use DemographicManager.getDemographicContacts
	 * or Use org.oscarehr.util.HealthCareTeamCreator getHealthCareTeam
	 */
	public static List<DemographicContact> getDemographicContacts(Demographic demographic) {
		List<DemographicContact> contacts = demographicContactDao.findByDemographicNo(demographic.getDemographicNo());	
		return fillContactNames(contacts);
	}
	
	public static List<DemographicContact> getDemographicContacts(Demographic demographic, String category) {
		List<DemographicContact> contacts = demographicContactDao.findByDemographicNoAndCategory(demographic.getDemographicNo(),category);	
		return fillContactNames(contacts);
	}

	@Deprecated 
	/**
	 * Use org.oscarehr.util.HealthCareTeamCreator getHealthCareTeam
	 */
	public static List<DemographicContact> fillContactNames(List<DemographicContact> contacts) {

		Provider provider;
		Contact contact; 
		ProfessionalSpecialist professionalSpecialist;
		ContactSpecialty specialty;
		String providerFormattedName = ""; 
		String role = "";
		
		for( DemographicContact c : contacts ) {
			role = c.getRole();
			if( StringUtils.isNumeric( c.getRole() ) && ! role.isEmpty() ) {
				specialty = contactSpecialtyDao.find( Integer.parseInt( c.getRole().trim() ) );
				c.setRole( specialty.getSpecialty() );
			}

			if( c.getType() == DemographicContact.TYPE_DEMOGRAPHIC ) {
				c.setContactName(demographicDao.getClientByDemographicNo( Integer.parseInt( c.getContactId() ) ).getFormattedName() );
			}
			
			if( c.getType() == DemographicContact.TYPE_PROVIDER ) {
				provider = providerDao.getProvider( c.getContactId() );
				if(provider != null){
					providerFormattedName = provider.getFormattedName();
				}
				if(StringUtils.isBlank(providerFormattedName)) {
					providerFormattedName = "Error: Contact Support";
					logger.error("Formatted name for provder was not avaialable. Contact number: " + c.getContactId());
				}
				c.setContactName(providerFormattedName);
				contact = new ProfessionalContact();
				contact.setWorkPhone("internal");
				contact.setFax("internal");
				c.setDetails(contact);
			}
			
			if( c.getType() == DemographicContact.TYPE_CONTACT ) {
				contact = contactDao.find( Integer.parseInt( c.getContactId() ) );
				c.setContactName( contact.getFormattedName() );
				c.setDetails(contact);
			}
			
			if( c.getType() == DemographicContact.TYPE_PROFESSIONALSPECIALIST ) {
				professionalSpecialist = professionalSpecialistDao.find( Integer.parseInt( c.getContactId() ) );
				c.setContactName( professionalSpecialist.getFormattedName() );				
				contact = buildContact( professionalSpecialist );
				c.setDetails(contact);
			}
		}

		return contacts;
	}
	
	
	@Deprecated 
	/**
	 * Use HealthCareTeamCreator.buildContact
	 * @param contact
	 * @return
	 */
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

	@Deprecated
	/**
	 * Use HealthCareTeamCreator.buildContact
	 * @param contact
	 * @return
	 */
	private static final Contact buildContact(final Object contactobject) {
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
	
	@Deprecated
	/**
	 * Use HealthCareTeamCreator.byLastName
	 * @param contact
	 * @return
	 */
	public static Comparator<Contact> byLastName = new Comparator<Contact>() {
		public int compare(Contact contact1, Contact contact2) {
			String lastname1 = contact1.getLastName().toUpperCase();
			String lastname2 = contact2.getLastName().toUpperCase();
			return lastname1.compareTo(lastname2);
		}
	};
	
	
	//get list of contact types
	public ActionForward listContactTypes( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response ) throws IOException {

		List<ContactType> contactTypes = contactManager.getContactTypes(LoggedInInfo.getLoggedInInfoFromSession(request));

		Collections.sort(contactTypes, new Comparator<ContactType>() {
			 public int compare(ContactType o1, ContactType o2) {
                

                 return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
         }

		});
		JSONArray arr = JSONArray.fromObject(contactTypes);
		
		arr.write(response.getWriter());
		
		return null;
	}
	
	//save (new/existing)contact type
	public ActionForward saveContactType( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response ) throws IOException {

		String id = request.getParameter("contactTypeId");
		String name = request.getParameter("contactTypeName");
		String male = request.getParameter("contactTypeMale");
		String female = request.getParameter("contactTypeFemale");
	//	String inverseId = request.getParameter("inverseId");
		String active = request.getParameter("contactTypeActive");
		
		ContactType contactType = new ContactType();
		if(!StringUtils.isEmpty(id) && !"0".equals(id)) {
			contactType.setId(Integer.parseInt(id));
		}
		contactType.setName(name);
		contactType.setMale((male != null &&  "on".equals(male))?true:false);
		contactType.setFemale((female != null &&  "on".equals(female))?true:false);
		contactType.setActive((active != null &&  "on".equals(active))?true:false);
		/*
		if(!StringUtils.isEmpty(inverseId) && !"0".equals(inverseId)) {
			contactType.setInverseRelationship(Integer.parseInt(inverseId));
		}*/
		
		contactManager.saveContactType(LoggedInInfo.getLoggedInInfoFromSession(request),contactType);
		
		JSONObject json = JSONObject.fromObject(contactType);
		
		json.write(response.getWriter());
		
		return null;
	}
	
	
	//inactivate contact type
	public ActionForward archiveContactType( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response ) {

		String id = request.getParameter("id");
		
		contactManager.archiveContactType(LoggedInInfo.getLoggedInInfoFromSession(request),Integer.parseInt(id));
		
		return null;
	}
	
	
	//restore contact type
	public ActionForward restoreContactType( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response ) {

		String id = request.getParameter("id");
		
		contactManager.restoreContactType(LoggedInInfo.getLoggedInInfoFromSession(request),Integer.parseInt(id));
		
		
		return null;
	}
	

	public ActionForward getContactTypeDataForProgram( ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response )throws IOException {

		String programId = request.getParameter("programId");
		
		List<ProgramContactType> results = contactManager.getContactTypesForProgramAndCategory(LoggedInInfo.getLoggedInInfoFromSession(request),Integer.parseInt(programId),null);
		
		JSONArray arr = JSONArray.fromObject(results);
		
		arr.write(response.getWriter());
		
		return null;
	}
	
	
	private String convertCategoryToName(Integer id) {
		if(id == 0) {
			return "Personal";
		}
		if(id == 1) {
			return "Health Care Provider";
		}
		if(id == 2) {
			return "Other";
		}
		
		return "Other";
	}
	//save (new/existing)contact type
		public ActionForward saveProgramContactTypes( ActionMapping mapping, ActionForm form, 
				HttpServletRequest request, HttpServletResponse response ) throws IOException {

			String programId = request.getParameter("programId");
			
			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
			
			contactManager.removeProgramContactTypes(loggedInInfo,Integer.parseInt(programId));
			
			Enumeration<String> params = request.getParameterNames();
			while(params.hasMoreElements()) {
				String parameterName = params.nextElement();
				if(parameterName.startsWith("pct_")) {
					String category = parameterName.substring(4, 5);
					String contactTypeId = parameterName.substring(6);
					
					contactManager.addProgramContactType(loggedInInfo,Integer.parseInt(programId),Integer.parseInt(contactTypeId),convertCategoryToName(Integer.parseInt(category)));
					logger.info("adding contactTypeId " + contactTypeId + " for program " + programId +" using category " + convertCategoryToName(Integer.parseInt(category)));
				}
			}
			
			JSONObject json = new JSONObject();
			json.put("success", true);
			
			json.write(response.getWriter());
			
			return null;
		}
		
		
		//get list of contact types
		public ActionForward listProgramContactTypes( ActionMapping mapping, ActionForm form, 
				HttpServletRequest request, HttpServletResponse response ) throws IOException {

			Map<String,List<ProgramContactType>> contactTypesByProgram = contactManager.getProgramContactTypes(LoggedInInfo.getLoggedInInfoFromSession(request));

			JSONObject jsonObject = JSONObject.fromObject( contactTypesByProgram );  
			
			
			jsonObject.write(response.getWriter());
			
			return null;
		}
		
}
