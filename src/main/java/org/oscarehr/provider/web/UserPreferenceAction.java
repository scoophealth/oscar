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


package org.oscarehr.provider.web;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormGroupDao;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.dao.MyGroupDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;


/**
 * 
 * Properties are loaded in the following order
 * Hard-coded defaults
 * OSCAR system properties file (pref.<key>)
 * Provider properties (db)
 * 
 * @author Marc Dumontier
 *
 */
public class UserPreferenceAction extends DispatchAction {
	
	private Logger logger = MiscUtils.getLogger();
	protected org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
	protected UserPropertyDAO userPropertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	static Map<String,String> defaults = new HashMap<String,String>();	
	protected Map<String,String> siteDefaults = new HashMap<String,String>();	
	private boolean inited = false;
	
	static {
		defaults.put("pref."+UserProperty.SCHEDULE_START_HOUR, "8");
		defaults.put("pref."+UserProperty.SCHEDULE_END_HOUR, "18");
		defaults.put("pref."+UserProperty.SCHEDULE_PERIOD, "15");
		defaults.put("pref."+UserProperty.NEW_CME, "Enabled");
		defaults.put("pref." + UserProperty.ENCOUNTER_FORM_LENGTH, "3");
		defaults.put("pref." + UserProperty.RX_USE_RX3, "yes");
	}
	
	public String getParameter(HttpServletRequest request, String name) {
		String val = request.getParameter(name);
		if(val == null) {
			return new String();
		}
		return val;
	}
	
	
	protected void init() {
		try {
			InputStream in = this.getServlet().getServletContext().getResourceAsStream("/WEB-INF/classes/pref.defaults");
			Properties p = new Properties();
			p.load(in);
			
			Iterator<Object> i = p.keySet().iterator();
			while(i.hasNext()) {
				String key = (String)i.next();
				String value = p.getProperty(key);
				this.siteDefaults.put(key, value);
				logger.info("site default:" + key + "=" + value);
			}
		} catch(IOException e) {
			logger.info("Error",e);
		}
	}
	
	@Override	 
	public ActionForward unspecified(ActionMapping mapping,ActionForm actionForm, HttpServletRequest request,HttpServletResponse response) {		
		return form(mapping,actionForm,request,response);	 
	}
	 	
	public ActionForward form(ActionMapping mapping,ActionForm actionForm, HttpServletRequest request,HttpServletResponse response) {
		if(!inited) init();
		Map<String,String> prefs = new HashMap<String,String>();

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		List<UserProperty> userProperties = userPropertyDao.getDemographicProperties(providerNo);
		prefs.putAll(defaults);
		prefs.putAll(siteDefaults);
		for(UserProperty up:userProperties) {
			prefs.put(up.getName(), up.getValue());
		}
		request.setAttribute("prefs", prefs);
		return mapping.findForward("form");	 
	}
	
	public ActionForward saveGeneral(ActionMapping mapping,ActionForm actionForm, HttpServletRequest request,HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		//Is there a password change?
		if(!getParameter(request,"new_password").isEmpty()) {
			try {
				changePassword(request);
			}catch(Exception e) {
				logger.error("Error",e);
			}
		}
		
		Iterator<Object> iter = request.getParameterMap().keySet().iterator();
		while(iter.hasNext()) {			
			String key = (String)iter.next();
			if(!key.startsWith("pref.")) continue;
			
			String value = request.getParameter(key);
			
			//a couple special cases
			if(key.equals("pref."+UserProperty.ENCOUNTER_FORM_NAME) || key.equals("pref."+UserProperty.EFORM_NAME)) {
				String[] values = request.getParameterValues(key);
				StringBuilder sb = new StringBuilder();
				for(int x=0;x<values.length;x++) {
					if(x>0) sb.append(",");
					sb.append(values[x]);
				}
				value = sb.toString();				
			}
			
			UserProperty up = userPropertyDao.getProp(providerNo, key);
			if(up != null) {
				up.setValue(value);
				userPropertyDao.saveProp(up);
			} else {
				up = new UserProperty();
				up.setProviderNo(providerNo);
				up.setName(key);
				up.setValue(value);
				userPropertyDao.saveProp(up);
			}
		}
		
		return form(mapping,actionForm,request,response);
	}
	
	
	private void changePassword(HttpServletRequest request) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		String currentPassword = getParameter(request,"current_password");
		String newPassword = getParameter(request,"new_password");
		
		MessageDigest md = MessageDigest.getInstance("SHA");
		
		//the current password
		byte[] btOldPasswd= md.digest(currentPassword.getBytes());		 
		StringBuilder sbTemp = new StringBuilder();
		for(int i=0; i<btOldPasswd.length; i++) 
			sbTemp = sbTemp.append(btOldPasswd[i]);	    
		String stroldpasswd = sbTemp.toString();
	    	    
		//get password from db
		Security secRecord = securityManager.findByProviderNo(loggedInInfo, providerNo);		
		String strDBpasswd = secRecord.getPassword();
		if (strDBpasswd.length()<20) {
			sbTemp = new StringBuilder();		
			byte[] btDBPasswd= md.digest(secRecord.getPassword().getBytes());	         
			for(int i=0; i<btDBPasswd.length; i++) 
				sbTemp = sbTemp.append(btDBPasswd[i]);	         
			 strDBpasswd = sbTemp.toString();
		}
	     
		if( stroldpasswd.equals(strDBpasswd)) {
		       sbTemp = new StringBuilder();
		       byte[] btNewPasswd= md.digest(newPassword.getBytes());
		       for(int i=0; i<btNewPasswd.length; i++) 
		    	   sbTemp = sbTemp.append(btNewPasswd[i]);

		       secRecord.setPassword(sbTemp.toString());
		       securityManager.updateSecurityRecord(loggedInInfo, secRecord);
		       
		       logger.info("password changed for provider");
		} else {					
			throw new Exception("Current password did not match.");   
		}
		
	}

	public static String getTextData(Map<String,String> prefs,String key) {
		String val = prefs.get(key);
		if(val == null) {
			val=new String();
		}
		return "name=\""+key+"\" value=\"" + val + "\"";
	}
	
	public static String getSelect(Map<String,String> prefs,String key) {
		StringBuilder sb = new StringBuilder();
		
		List<LabelValueBean> options = getOptions(key);
		String selectedValue = prefs.get(key);
		if(selectedValue == null) {
			selectedValue = new String();
		}
		sb.append("<select name=\""+key+"\">");
		for(LabelValueBean option:options) {
			String selected = (option.getValue().equals(selectedValue))?"selected=\"selected\"":"";
			sb.append("<option value=\""+option.getValue()+"\" "+selected+">"+option.getLabel()+"</option>\n");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	private static List<LabelValueBean> getOptions(String key) {
		List<LabelValueBean> options = new ArrayList<LabelValueBean>();
		if(key.equals("pref."+UserProperty.SEX)) {
			options.add(new LabelValueBean("",""));
			options.add(new LabelValueBean("Male","M"));
			options.add(new LabelValueBean("Female","F"));
		}
		if(key.equals("pref."+UserProperty.HC_TYPE)) {
			options.add(new LabelValueBean("",""));
			options.addAll(constructProvinceList());
		}
		if(key.equals("pref."+UserProperty.WORKLOAD_MANAGEMENT)) {
			options.add(new LabelValueBean("none",""));
			options.addAll(constructWorkloadManagementList());
		}
		if(key.equals("pref."+UserProperty.SCHEDULE_START_HOUR)) {			
			options.addAll(constructScheduleHourList());
		}
		if(key.equals("pref."+UserProperty.SCHEDULE_END_HOUR)) {			
			options.addAll(constructScheduleHourList());
		}
		if(key.equals("pref."+UserProperty.SCHEDULE_PERIOD)) {			
			options.addAll(constructSchedulePeriodList());
		}
		if(key.equals("pref."+UserProperty.MYGROUP_NO)) {			
			options.addAll(constructMyGroupList());
		}		
		if(key.equals("pref."+UserProperty.NEW_CME)) {			
			options.add(new LabelValueBean("Enabled","Enabled"));
			options.add(new LabelValueBean("Disabled","Disabled"));
		}
		if(key.equals("pref."+UserProperty.STALE_NOTEDATE)) {			
			options.add(new LabelValueBean("All","All"));
			for(int x=1;x<=36;x++) {
				options.add(new LabelValueBean(String.valueOf(x),String.valueOf(x)));
			}
		}
		if(key.equals("pref."+UserProperty.RX_USE_RX3)) {			
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));			
		}
		if(key.equals("pref."+UserProperty.RX_SHOW_QR_CODE)) {			
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));			
		}
		if(key.equals("pref."+UserProperty.RX_PAGE_SIZE)) {			
			options.add(new LabelValueBean("A4","A4"));
			options.add(new LabelValueBean("A6","A6"));			
		}
		if(key.equals("pref."+UserProperty.RX_SHOW_PATIENT_DOB)) {			
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));		
		}
		if(key.equals("pref."+UserProperty.EFORM_FAVOURITE_GROUP)) {	
			EFormGroupDao eFormGroupDao = (EFormGroupDao) SpringUtils.getBean("EFormGroupDao");	
			options.add(new LabelValueBean("None",""));
			List<String> groups = eFormGroupDao.getGroupNames();
			for(String group:groups) {
				options.add(new LabelValueBean(group,group));
			}
		}
		if(key.equals("pref."+UserProperty.CONSULTATION_TEAM_WARNING)) {	
			options.add(new LabelValueBean("All","-1"));
			options.addAll(constructProviderTeamList());
			options.add(new LabelValueBean("None",""));
		}
		if(key.equals("pref."+UserProperty.CONSULTATION_REQ_PASTE_FMT)) {	
			options.add(new LabelValueBean("Single Line","single"));
			options.add(new LabelValueBean("Multi Line","multi"));
		}		
		if(key.equals("pref."+UserProperty.MYMEDS)) {	
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));	
		}
		if(key.equals("pref."+UserProperty.NEW_TICKLER_WARNING_WINDOW)) {	
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));	
		}
		if(key.equals("pref."+UserProperty.CAISI_DEFAULT_PMM)) {	
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));	
		}
		if(key.equals("pref."+UserProperty.CAISI_PREV_BILLING)) {	
			options.add(new LabelValueBean("Yes","yes"));
			options.add(new LabelValueBean("No","no"));	
		}
		if(key.equals("pref."+UserProperty.DEFAULT_BILLING_FORM)) {	
			options.add(new LabelValueBean("-- no --","no"));
			options.addAll(constructWorkloadManagementList());
		}
		if(key.equals("pref."+UserProperty.DEFAULT_REFERRAL_TYPE)) {	
			options.add(new LabelValueBean("Refer To","1"));
			options.add(new LabelValueBean("Refer By","2"));
			options.add(new LabelValueBean("Neither","3"));
		}
		if(key.equals("pref."+UserProperty.DEFAULT_PAYEE)) {	
			options.add(new LabelValueBean("","0"));	
			options.addAll(constructPayeeList());
		}
		return options;
	}
	
    public static ArrayList<LabelValueBean> constructProvinceList() {

        ArrayList<LabelValueBean> provinces = new ArrayList<LabelValueBean>();

        provinces.add(new LabelValueBean("AB-Alberta", "AB"));
        provinces.add(new LabelValueBean("BC-British Columbia", "BC"));
        provinces.add(new LabelValueBean("MB-Manitoba", "MB"));
        provinces.add(new LabelValueBean("NB-New Brunswick", "NB"));
        provinces.add(new LabelValueBean("NL-Newfoundland", "NL"));
        provinces.add(new LabelValueBean("NT-Northwest Territory", "NT"));
        provinces.add(new LabelValueBean("NS-Nova Scotia", "NS"));
        provinces.add(new LabelValueBean("NU-Nunavut", "NU"));
        provinces.add(new LabelValueBean("ON-Ontario", "ON"));
        provinces.add(new LabelValueBean("PE-Prince Edward Island", "PE"));
        provinces.add(new LabelValueBean("QC-Quebec", "QC"));
        provinces.add(new LabelValueBean("SK-Saskatchewan", "SK"));
        provinces.add(new LabelValueBean("YT-Yukon", "YK"));
        provinces.add(new LabelValueBean("US resident", "US"));
        provinces.add(new LabelValueBean("US-AK-Alaska", "US-AK"));
        provinces.add(new LabelValueBean("US-AL-Alabama","US-AL"));
        provinces.add(new LabelValueBean("US-AR-Arkansas","US-AR"));
        provinces.add(new LabelValueBean("US-AZ-Arizona","US-AZ"));
        provinces.add(new LabelValueBean("US-CA-California","US-CA"));
        provinces.add(new LabelValueBean("US-CO-Colorado","US-CO"));
        provinces.add(new LabelValueBean("US-CT-Connecticut","US-CT"));
        provinces.add(new LabelValueBean("US-CZ-Canal Zone","US-CZ"));
        provinces.add(new LabelValueBean("US-DC-District of Columbia","US-DC"));
        provinces.add(new LabelValueBean("US-DE-Delaware","US-DE"));
        provinces.add(new LabelValueBean("US-FL-Florida","US-FL"));
        provinces.add(new LabelValueBean("US-GA-Georgia","US-GA"));
        provinces.add(new LabelValueBean("US-GU-Guam","US-GU"));
        provinces.add(new LabelValueBean("US-HI-Hawaii","US-HI"));
        provinces.add(new LabelValueBean("US-IA-Iowa","US-IA"));
        provinces.add(new LabelValueBean("US-ID-Idaho","US-ID"));
        provinces.add(new LabelValueBean("US-IL-Illinois","US-IL"));
        provinces.add(new LabelValueBean("US-IN-Indiana","US-IN"));
        provinces.add(new LabelValueBean("US-KS-Kansas","US-KS"));
        provinces.add(new LabelValueBean("US-KY-Kentucky","US-KY"));
        provinces.add(new LabelValueBean("US-LA-Louisiana","US-LA"));
        provinces.add(new LabelValueBean("US-MA-Massachusetts","US-MA"));
        provinces.add(new LabelValueBean("US-MD-Maryland","US-MD"));
        provinces.add(new LabelValueBean("US-ME-Maine","US-ME"));
        provinces.add(new LabelValueBean("US-MI-Michigan","US-MI"));
        provinces.add(new LabelValueBean("US-MN-Minnesota","US-MN"));
        provinces.add(new LabelValueBean("US-MO-Missouri","US-MO"));
        provinces.add(new LabelValueBean("US-MS-Mississippi","US-MS"));
        provinces.add(new LabelValueBean("US-MT-Montana","US-MT"));
        provinces.add(new LabelValueBean("US-NC-North Carolina","US-NC"));
        provinces.add(new LabelValueBean("US-ND-North Dakota","US-ND"));
        provinces.add(new LabelValueBean("US-NE-Nebraska","US-NE"));
        provinces.add(new LabelValueBean("US-NH-New Hampshire","US-NH"));
        provinces.add(new LabelValueBean("US-NJ-New Jersey","US-NJ"));
        provinces.add(new LabelValueBean("US-NM-New Mexico","US-NM"));
        provinces.add(new LabelValueBean("US-NU-Nunavut","US-NU"));
        provinces.add(new LabelValueBean("US-NV-Nevada","US-NV"));
        provinces.add(new LabelValueBean("US-NY-New York","US-NY"));
        provinces.add(new LabelValueBean("US-OH-Ohio","US-OH"));
        provinces.add(new LabelValueBean("US-OK-Oklahoma","US-OK"));
        provinces.add(new LabelValueBean("US-OR-Oregon","US-OR"));
        provinces.add(new LabelValueBean("US-PA-Pennsylvania","US-PA"));
        provinces.add(new LabelValueBean("US-PR-Puerto Rico","US-PR"));
        provinces.add(new LabelValueBean("US-RI-Rhode Island","US-RI"));
        provinces.add(new LabelValueBean("US-SC-South Carolina","US-SC"));
        provinces.add(new LabelValueBean("US-SD-South Dakota","US-SD"));
        provinces.add(new LabelValueBean("US-TN-Tennessee","US-TN"));
        provinces.add(new LabelValueBean("US-TX-Texas","US-TX"));
        provinces.add(new LabelValueBean("US-UT-Utah","US-UT"));
        provinces.add(new LabelValueBean("US-VA-Virginia","US-VA"));
        provinces.add(new LabelValueBean("US-VI-Virgin Islands","US-VI"));
        provinces.add(new LabelValueBean("US-VT-Vermont","US-VT"));
        provinces.add(new LabelValueBean("US-WA-Washington","US-WA"));
        provinces.add(new LabelValueBean("US-WI-Wisconsin","US-WI"));
        provinces.add(new LabelValueBean("US-WV-West Virginia","US-WV"));
        provinces.add(new LabelValueBean("US-WY-Wyoming","US-WY"));

        return provinces;
   }

    public static ArrayList<LabelValueBean> constructWorkloadManagementList() {
        ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

        CtlBillingServiceDao ctlBillingServiceDao = (CtlBillingServiceDao)SpringUtils.getBean("ctlBillingServiceDao");
        List<Object[]> cbsList = ctlBillingServiceDao.getUniqueServiceTypes();
        for(Object[] cbs:cbsList) {
        	results.add(new LabelValueBean((String)cbs[1],(String)cbs[0]));
        }
        return results;
    }
    
    public static ArrayList<LabelValueBean> constructScheduleHourList() {
        ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();
        for(int x=0;x<23;x++) {
        	results.add(new LabelValueBean(String.valueOf(x),(String.valueOf(x))));
        }
        return results;
    }
    
    public static ArrayList<LabelValueBean> constructSchedulePeriodList() {
        ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();
        results.add(new LabelValueBean("5","5"));
        results.add(new LabelValueBean("10","10"));
        results.add(new LabelValueBean("15","15"));
        results.add(new LabelValueBean("20","20"));
        results.add(new LabelValueBean("30","30"));
        results.add(new LabelValueBean("60","60"));
        return results;
    }
    
    public static ArrayList<LabelValueBean> constructMyGroupList() {
        ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

        MyGroupDao myGroupDao = (MyGroupDao)SpringUtils.getBean("myGroupDao");
        List<String> cbsList = myGroupDao.getGroups();
        for(String cbs:cbsList) {
        	results.add(new LabelValueBean(cbs,cbs));
        }
        return results;
    }
    
    public static ArrayList<LabelValueBean> constructEncounterFormList() {
    	ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

    	EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean("encounterFormDao");
    	List<EncounterForm> forms = encounterFormDao.findAll();
    	Collections.sort(forms, EncounterForm.FORM_NAME_COMPARATOR);
    	for(EncounterForm form:forms) {
    		results.add(new LabelValueBean(form.getFormName(),form.getFormName()));
    	}
    	
    	return results;
    }
    
    public static String getEncounterFormHTML(Map<String,String> prefs,String key) {
    	StringBuilder sb = new StringBuilder();
    	List<LabelValueBean> forms = constructEncounterFormList();
    	for(LabelValueBean lvb:forms) {
    		String checked = new String();
    		
    		if(prefs.get(key) != null) {
    			String[] savedValues = prefs.get(key).split(",");
    			for(int x=0;x<savedValues.length;x++) {
    				if(savedValues[x].equals(lvb.getValue())) {
    					checked="checked=\"checked\"";		
    				}
    			}    			
    		}
    		sb.append("<input name=\"pref."+UserProperty.ENCOUNTER_FORM_NAME+"\" value=\""+lvb.getValue()+"\" type=\"checkbox\" "+checked+"/>"+lvb.getLabel()+"\n");
    		sb.append("<br/>\n");
    	}
    	return sb.toString();
    }
    
    public static ArrayList<LabelValueBean> constructEformList() {
    	ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

    	EFormDao eFormDao = (EFormDao) SpringUtils.getBean("EFormDao");
    	List<EForm> forms = eFormDao.findAll(true);
		Collections.sort(forms, EForm.FORM_NAME_COMPARATOR);
		
    	for(EForm form:forms) {
    		results.add(new LabelValueBean(form.getFormName(),String.valueOf(form.getId())));
    	}
    	
    	return results;
    }
    public static String getEformHTML(Map<String,String> prefs,String key) {
    	StringBuilder sb = new StringBuilder();
    	List<LabelValueBean> forms = constructEformList();
    	for(LabelValueBean lvb:forms) {
    		String checked = new String();
    		
    		if(prefs.get(key) != null) {
    			String[] savedValues = prefs.get(key).split(",");
    			for(int x=0;x<savedValues.length;x++) {
    				if(savedValues[x].equals(lvb.getValue())) {
    					checked="checked=\"checked\"";		
    				}
    			}    			
    		}
    		sb.append("<input name=\"pref."+UserProperty.EFORM_NAME+"\" value=\""+lvb.getValue()+"\" type=\"checkbox\" "+checked+"/>"+lvb.getLabel()+"\n");
    		sb.append("<br/>\n");
    	}
    	return sb.toString();
    }
    
    public static ArrayList<LabelValueBean> constructProviderTeamList() {
    	ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	List<String> teams = providerDao.getUniqueTeams();
    	for(String team:teams) {
    		if(team.length()>0) {
    			results.add(new LabelValueBean(team,team));
    		}
    	}
    	
    	return results;
    }
    
    public static ArrayList<LabelValueBean> constructPayeeList() {
    	ArrayList<LabelValueBean> results = new ArrayList<LabelValueBean>();

    	MSPReconcile rec = new MSPReconcile();
    	@SuppressWarnings("unchecked")
    	List<oscar.entities.Provider> providers = rec.getAllProviders();

    	for(oscar.entities.Provider provider:providers) {
    		results.add(new LabelValueBean(provider.getLastName() + "," + provider.getFirstName(),provider.getProviderNo()));
    	}

    	return results;
    }
}
