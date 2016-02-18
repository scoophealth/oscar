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
package oscar.oscarEncounter.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ContactDao;
import org.oscarehr.common.dao.ContactSpecialtyDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.ContactSpecialty;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.StringUtils;

public class EctDisplayContactsAction extends EctDisplayAction {

    private static final String cmd = "contacts";
    private static final Logger logger = Logger.getLogger(EctDisplayContactsAction.class);

    DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
    ContactDao contactDao = SpringUtils.getBean(ContactDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	ProfessionalSpecialistDao professionalSpecialistDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
	
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
 		try {
 			
 			String healthCareTeamEnabled = OscarProperties.getInstance().getProperty("DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM","true").toString();
		    //Set left hand module heading and link
		    String winName = "contact" + bean.demographicNo;
		    String pathview, pathedit;
		    int width = 0;
		    int height = 0;

		    if("true".equalsIgnoreCase( healthCareTeamEnabled ) ){
			    pathview = request.getContextPath() + 
			    		"/demographic/Contact.do?method=displayHealthCareTeam&view=detached&demographicNo=" + 
			    		bean.demographicNo;
			    pathedit = request.getContextPath() + 
			    		"/demographic/manageHealthCareTeam.jsp?view=detached&demographicNo=" + 
			    		bean.demographicNo;
			    width = 650;
			    height = 400;
		    } else {
		    	pathview = request.getContextPath() + "/demographic/professionalSpecialistSearch.jsp?keyword=&submit=Search";
			    pathedit = request.getContextPath() + "/demographic/Contact.do?method=manage&demographic_no=" + bean.demographicNo;
			    width = 650;
			    height = 900;
		    }

		    String url = "popupPage(" + height + "," + width + ",'" + winName + "','" + pathview + "')";
		    
		    if("true".equalsIgnoreCase( healthCareTeamEnabled ) ){
		    	Dao.setLeftHeading("Health Care Team");
		    	width = 700;
			    height = 500; 
		    } else {
		    	Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.contacts"));
		    	width = 800;
			    height = 1000; 
		    }
		    
		    Dao.setLeftURL(url);

		    //set right hand heading link
		    winName = "AddContact" + bean.demographicNo;
		    url = "popupPage(" + height + "," + width + ",'" + winName + "','" + pathedit + "'); return false;";
		    Dao.setRightURL(url);
		    Dao.setRightHeadingID(cmd);

		    List<DemographicContact> contacts = demographicManager.getDemographicContacts(loggedInInfo, Integer.parseInt(bean.demographicNo));
		    ContactSpecialtyDao contactSpecialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);

		    for(DemographicContact contact:contacts) {
		    	//only show professional contacts
		    	if(contact.getCategory().equals(DemographicContact.CATEGORY_PERSONAL)) {
		    		continue;
		    	}
		    	String name="N/A";
		    	String specialty = "";
		    	String workPhone = "";
		    	//String consent = "";
		    	
		    	if(contact.getType() == DemographicContact.TYPE_CONTACT) {
		    		Contact c = contactDao.find(Integer.parseInt(contact.getContactId()));
		    		name = c.getLastName() + "," + c.getFirstName();
		    		workPhone = c.getWorkPhone();
		    		
		    		if(contact.getCategory() == DemographicContact.CATEGORY_PROFESSIONAL) {
		    			specialty = ((ProfessionalContact) c).getSpecialty();
		    			logger.info("Found professional contact specialty " 
		    			+  specialty 
		    			+ " for demographicContact_id " 
		    			+ contact.getId()
		    			+ " in the *Contact* object.");
		    		}
	
		    		if( StringUtils.isNullOrEmpty(specialty) ) {
		    			specialty = contact.getRole();
		    		} 
		    		
		    		if(StringUtils.isNumeric( specialty )) {
		    			ContactSpecialty contactSpecialty = contactSpecialtyDao.find( Integer.parseInt(specialty) );
		    			if(contactSpecialty != null) {
		    				specialty = contactSpecialty.getSpecialty();
		    			}
		    			logger.info("Found professional contact specialty "
		    					+ specialty 
		    					+ " for demographicContact_id"
		    					+  contact.getId()
		    					+ " in the *DemographicContact* object ");
		    		}
		    		
		    	} else if(contact.getType() == DemographicContact.TYPE_PROVIDER) {
		    		Provider p = providerDao.getProvider(contact.getContactId());
		    		name = p.getFormattedName();
		    		specialty = p.getSpecialty();
		    		workPhone = p.getWorkPhone();
		    	} else if(contact.getType() == DemographicContact.TYPE_PROFESSIONALSPECIALIST) {
		    		ProfessionalSpecialist p = professionalSpecialistDao.find(Integer.parseInt(contact.getContactId()));
		    		name = p.getFormattedName();
		    		specialty = p.getSpecialtyType()!=null?p.getSpecialtyType():"";
		    		workPhone = p.getPhoneNumber();
		    	}
		    	//contactDao.find(Integer.parseInt(contact.getContactId()));
		    	NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
		    	//48.45
		    	String itemHeader = StringUtils.maxLenString(name, 20, 17, ELLIPSES) +
		    			((specialty.length()>0)?StringUtils.maxLenString("  "+ specialty, 14, 11, ELLIPSES):""); 
		    			//+((workPhone.length()>0)?StringUtils.maxLenString("  "+workPhone, 17, 14, ELLIPSES):"");
		        item.setTitle((contact.isConsentToContact()?"":"*") + itemHeader);
		        String consent = contact.isConsentToContact()?"Ok to contact":"Do not contact";
		        item.setLinkTitle(name + " " + specialty + " " + workPhone + " " + consent);
		        
		        //item.setDate(contact.getUpdateDate());
		        int hash = Math.abs(winName.hashCode());
		        
		        if("true".equalsIgnoreCase( healthCareTeamEnabled ) ){
		        	
		        	if( contact.getType() == DemographicContact.TYPE_PROVIDER ) {
		        		url = "alert('Edit internal providers from the provider menu');return false;";
		        	} else {
		        		url = "popupPage(650,500,'" + hash + "','" + 
		        				request.getContextPath() + 
		        				"/demographic/Contact.do?method=editHealthCareTeam&contactId="+ 
		        				contact.getId() +"&role=" + 
		        				contact.getRole() + 
		        				"'); return false;";
		        	}
		        	
		        } else {
		        
			        if(contact.getType() == DemographicContact.TYPE_CONTACT) {
			        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/demographic/Contact.do?method=editProContact&pcontact.id="+ contact.getContactId() +"'); return false;";
			        } else if (contact.getType() == DemographicContact.TYPE_CONTACT){
			        	String roles =(String) request.getSession().getAttribute("userrole");
			        	if(roles.indexOf("admin") != -1)
			        		url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/admin/providerupdateprovider.jsp?keyword="+ contact.getContactId() +"'); return false;";
			        	else
			        		url = "alert('Cannot Edit');return false;";
			        } else if(contact.getType() == DemographicContact.TYPE_PROFESSIONALSPECIALIST) {
			        	url = "popupPage(500,900,'" + hash + "','" + request.getContextPath() + "/oscarEncounter/EditSpecialists.do?specId="+ contact.getContactId() +"'); return false;";
			        }
		        
		        }
		        
		        item.setURL(url);
		        Dao.addItem(item);
		    }

		 }catch( Exception e ) {
		     MiscUtils.getLogger().error("Error", e);
		     return false;
		 }
		return true;
    	
    }

    public String getCmd() {
    	return cmd;
    }
}
