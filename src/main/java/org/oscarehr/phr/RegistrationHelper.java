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


package org.oscarehr.phr;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.myoscar_server.ws.Relation;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;
import org.oscarehr.myoscar.client.ws_manager.MyOscarLoggedInInfoInterface;
import org.oscarehr.phr.util.UsernameHelper;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar.client.ws_manager.MyOscarServerWebServicesManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.RelationshipTransfer4;




public final class RegistrationHelper {
	private static final String MYOSCAR_REGISTRATION_DEFAULTS_SESSION_KEY = "MYOSCAR_REGISTRATION_DEFAULTS";

	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private static PropertyDao propertyDao = (PropertyDao) SpringUtils.getBean("propertyDao");
	private static Random random = new Random();

	public static String getDefaultUserName(int demographicId) {
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		String fn = UsernameHelper.stripName(demographic.getFirstName());
		String ln = UsernameHelper.stripName(demographic.getLastName());	
		
		return (fn + '.' + ln);
	}
	
	public static List<String> getPhrDefaultUserNameList(MyOscarLoggedInInfoInterface user, int demographicId) {

		Demographic demographic = demographicDao.getDemographicById(demographicId);
		String fn = UsernameHelper.stripName(demographic.getFirstName());
		String ln = UsernameHelper.stripName(demographic.getLastName());	
		String email = demographic.getEmail();
		
		return UsernameHelper.suggestUsernames(user, fn, ln, email);
	}
	
	/**
	 * Generate a password of length 12 using numbers and letters. This will ommit i/l/o/1/o to prevent abiguity. Due to the length the permutations are still large, i.e. (24^8 ~= 110 billion) * (8^4 = 4096) ~= 450,868,486,864,896 permutations ~= 450
	 * trillion
	 */
	public static String getNewRandomPassword() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			// the specific purpose for enforcing a number after 2 letters is to prevent accidental rude words from being generated.
			// It's not a security concern that the sequence of numbers and letters is predictable as we could just make the string longer to increase the permutations.
			sb.append(getRandomPasswordLetter());
			sb.append(getRandomPasswordLetter());
			sb.append(getRandomPasswordDigit());
		}

		return (sb.toString());
	}

	/**
	 * @return digit from 2 to 9 inclusive.
	 */
	private static Object getRandomPasswordDigit() {
		// generate 0 to 7, then add 2, this will skip 0 and 1 so there's no ambiguity between 0/O and 1/I/l
		int i = random.nextInt(6);
		return (i + 2);
	}

	/**
	 * @return a lower case letter excluding i/l,o to prevent ambiguity with 1,0 respectively
	 */
	private static char getRandomPasswordLetter() {
		int i = random.nextInt('z' - 'a');
		i = i + 'a';

		if (i == 'i' || i == 'l' || i == 'o') return (getRandomPasswordLetter());

		return (char) (i);
	}

	/**
	 * @return map of <MyOscarUserName,Provider>
	 */
	public static TreeMap<String, Provider> getMyOscarProviders() {
		TreeMap<String, Provider> results = new TreeMap<String, Provider>();

		List<Property> tempProperties = propertyDao.findByName("MyOscarId");
		for (Property property : tempProperties) {
			Provider provider = providerDao.getProvider(property.getProviderNo());
			if (provider != null) results.put(property.getValue(), provider);
		}

		return (results);
	}

	public static String renderRelationshipSelect(HttpSession session, String widgetName) {
		StringBuilder sb = new StringBuilder();

		sb.append("<select name=\"" + widgetName + "\">");

		sb.append("<option value=\"" + Relation.PRIMARY_CARE_PROVIDER.name() + "\" "+getSelectedString(session, widgetName, Relation.PRIMARY_CARE_PROVIDER.name())+" >");
		sb.append(Relation.PRIMARY_CARE_PROVIDER.name());
		sb.append("</option>");

		sb.append("<option value=\"" + Relation.RESEARCH_ADMINISTRATOR.name() + "\" "+getSelectedString(session, widgetName, Relation.RESEARCH_ADMINISTRATOR.name())+" >");
		sb.append(Relation.RESEARCH_ADMINISTRATOR.name());
		sb.append("</option>");

		sb.append("</select>");

		return (sb.toString());
	}

	public static String getCheckedString(HttpSession session, String elementName) {
		HashMap<String, Object> defaults=(HashMap<String, Object>) session.getAttribute(MYOSCAR_REGISTRATION_DEFAULTS_SESSION_KEY);
		if (defaults==null) return("");
		Boolean b=(Boolean) defaults.get(elementName);
		return(WebUtils.getCheckedString(b!=null && b));
	}

	public static String getCheckedStringWithValueString(HttpSession session, String elementName,String elementValue) {
		HashMap<String, Object> defaults=(HashMap<String, Object>) session.getAttribute(MYOSCAR_REGISTRATION_DEFAULTS_SESSION_KEY);
		if (defaults==null) return("");
		String relationString=(String) defaults.get(elementName);
		return(WebUtils.getCheckedString(relationString!=null && relationString.equals(elementValue)));
	}
	
	public static String getSelectedString(HttpSession session, String elementName, String elementValue) {
		HashMap<String, Object> defaults=(HashMap<String, Object>) session.getAttribute(MYOSCAR_REGISTRATION_DEFAULTS_SESSION_KEY);
		if (defaults==null) return("");
		String relationString=(String) defaults.get(elementName);
		return(WebUtils.getSelectedString(relationString!=null && relationString.equals(elementValue)));
	}
	
	public static void storeSelectionDefaults(HttpServletRequest request) {
		// 2011-09-08 15:53:39,632 ERROR [WebUtils:43] --- Dump Request Parameters Start ---
		// 2011-09-08 15:53:39,632 ERROR [WebUtils:49] dob=1988/06/15
		// 2011-09-08 15:53:39,632 ERROR [WebUtils:49] firstName=MMM0
		// 2011-09-08 15:53:39,632 ERROR [WebUtils:49] province=ON
		// 2011-09-08 15:53:39,633 ERROR [WebUtils:49] password=mu2xd3sr6kd2
		// 2011-09-08 15:53:39,633 ERROR [WebUtils:49] reverse_relation_3456=PATIENT
		// 2011-09-08 15:53:39,633 ERROR [WebUtils:49] reverse_relation_999998=PATIENT
		// 2011-09-08 15:53:39,633 ERROR [WebUtils:49] method=registerUser
		// 2011-09-08 15:53:39,633 ERROR [WebUtils:49] demographicNo=8
		// 2011-09-08 15:53:39,634 ERROR [WebUtils:49] address=
		// 2011-09-08 15:53:39,634 ERROR [WebUtils:49] username=mmm0.mmm0
		// 2011-09-08 15:53:39,634 ERROR [WebUtils:49] postal=
		// 2011-09-08 15:53:39,634 ERROR [WebUtils:49] enable_reverse_relation_999998=on
		// 2011-09-08 15:53:39,634 ERROR [WebUtils:49] phone=905-
		// 2011-09-08 15:53:39,635 ERROR [WebUtils:49] enable_reverse_relation_3456=on
		// 2011-09-08 15:53:39,635 ERROR [WebUtils:49] enable_primary_relation_22=on
		// 2011-09-08 15:53:39,635 ERROR [WebUtils:49] primary_relation_22=RESEARCH_ADMINISTRATOR
		// 2011-09-08 15:53:39,635 ERROR [WebUtils:49] enable_primary_relation_999998=on
		// 2011-09-08 15:53:39,635 ERROR [WebUtils:49] reverse_relation_22=RESEARCH_SUBJECT
		// 2011-09-08 15:53:39,636 ERROR [WebUtils:49] lastName=MMM0
		// 2011-09-08 15:53:39,636 ERROR [WebUtils:49] phone2=
		// 2011-09-08 15:53:39,636 ERROR [WebUtils:49] email=
		// 2011-09-08 15:53:39,636 ERROR [WebUtils:49] city=
		// 2011-09-08 15:53:39,636 ERROR [WebUtils:49] primary_relation_3456=PRIMARY_CARE_PROVIDER
		// 2011-09-08 15:53:39,637 ERROR [WebUtils:49] primary_relation_999998=PRIMARY_CARE_PROVIDER
		// 2011-09-08 15:53:39,637 ERROR [WebUtils:49] enable_primary_relation_3456=on
		// 2011-09-08 15:53:39,637 ERROR [WebUtils:52] --- Dump Request Parameters End ---

		HashMap<String, Object> defaults = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();

			if (key.startsWith("enable_primary_relation_") || key.startsWith("enable_reverse_relation_")) {
				boolean b = WebUtils.isChecked(request, key);
				if (b) defaults.put(key, b);
			}

			if (key.startsWith("primary_relation_") || key.startsWith("reverse_relation_")) {
				String relation=request.getParameter(key);
				defaults.put(key, relation);
			}
		}

		request.getSession().setAttribute(MYOSCAR_REGISTRATION_DEFAULTS_SESSION_KEY, defaults);
	}
	
	/**
	Checks to see if:
	 a. PatientPrimaryCareProvider exists with the myoscar user 
	 b. If the current provider has verified this relationship 
	**/
	public static boolean iHavePatientRelationship(MyOscarLoggedInInfo myOscarLoggedInInfo,Long myOscarUserId){
		
	   AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(myOscarLoggedInInfo);
	   final int REASONABLE_RELATIONSHIP_LIMIT = 256;
	   int startIndex = 0;
	   
	   while(true){           
	      java.util.List<RelationshipTransfer4> relationList = accountWs.getRelationshipsByPersonId2(myOscarLoggedInInfo.getLoggedInPersonId(), startIndex, REASONABLE_RELATIONSHIP_LIMIT);
	      if (relationList.size() == 0) break;
	    
		  for (RelationshipTransfer4 rt : relationList) {
			 if (rt.getRelation().equals("PatientPrimaryCareProvider")){
	            if (rt.getPerson1().getPersonId().equals(myOscarUserId) && rt.getPerson2VerificationDate()!=null) {
	               return true;
	            }
			 }
	      }
		  startIndex = startIndex + relationList.size();
	   }
	   return false;
	}
	
}
