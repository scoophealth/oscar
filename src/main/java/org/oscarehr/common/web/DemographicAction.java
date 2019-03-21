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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtArchiveDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.common.model.DemographicExtArchive;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * This class is to support the address/phone history popup in the master demographic screen.
 * 
 * returns JSON array of DemographicHistoryItems based on changed address, home phone+ext, work phone+ext, cell phone.
 */
public class DemographicAction extends DispatchAction  {

	
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private DemographicArchiveDao demographicArchiveDao = SpringUtils.getBean(DemographicArchiveDao.class);
	private DemographicExtArchiveDao demographicExtArchiveDao = SpringUtils.getBean(DemographicExtArchiveDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	
	public ActionForward getSubdivisionCodes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
	
		String selectedCountry = request.getParameter("country");
		
		org.codehaus.jettison.json.JSONArray results = new org.codehaus.jettison.json.JSONArray();
		org.codehaus.jettison.json.JSONObject obj = null;
		
		try {
			InputStream  in = this.getClass().getClassLoader()
                    .getResourceAsStream("iso-3166-2.json");
			String theString = IOUtils.toString(in, "UTF-8");
			obj = new org.codehaus.jettison.json.JSONObject(theString);
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
		}
		
		if(obj != null) {	
			if(selectedCountry == null) {
				Iterator iter =  obj.keys();
				while(iter.hasNext()) {
					String countryCode = (String)iter.next();
					String countryName = ((org.codehaus.jettison.json.JSONObject)obj.get(countryCode)).getString("name");
					org.codehaus.jettison.json.JSONObject r = new org.codehaus.jettison.json.JSONObject();
					r.put("value", countryCode);
					r.put("label", countryName);
					results.put(r);
				}
			} else {
				org.codehaus.jettison.json.JSONObject country = (org.codehaus.jettison.json.JSONObject)obj.get(selectedCountry);
				org.codehaus.jettison.json.JSONObject divisions = (org.codehaus.jettison.json.JSONObject)country.get("divisions");
				Iterator iter =  divisions.keys();
				while(iter.hasNext()) {
					String divisionCode = (String)iter.next();
					String divisionName = divisions.getString(divisionCode);
					org.codehaus.jettison.json.JSONObject r = new org.codehaus.jettison.json.JSONObject();
					r.put("value", divisionCode);
					r.put("label", divisionName);
					results.put(r);
				}
			}
		}
		
		results.write(response.getWriter());
		
		return null;
	}
	public ActionForward getCountryAndProvinceCodes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
	
		String selectedCountry = request.getParameter("country");
	//	String selectedSubDivision = request.getParameter("subdividion");
		
		org.codehaus.jettison.json.JSONArray results = new org.codehaus.jettison.json.JSONArray();
		
		org.codehaus.jettison.json.JSONObject obj = null;
		try {
			InputStream  in = this.getClass().getClassLoader()
                    .getResourceAsStream("iso-3166-2.json");
			String theString = IOUtils.toString(in, "UTF-8");
			obj = new org.codehaus.jettison.json.JSONObject(theString);
		}catch(Exception e) {
			MiscUtils.getLogger().warn("Warning", e);
		}
		
		if(obj != null) {
			
			if(selectedCountry == null) {
				Iterator iter =  obj.keys();
				while(iter.hasNext()) {
					String countryCode = (String)iter.next();
					String countryName = ((org.codehaus.jettison.json.JSONObject)obj.get(countryCode)).getString("name");
					org.codehaus.jettison.json.JSONObject r = new org.codehaus.jettison.json.JSONObject();
					r.put("value", countryCode);
					r.put("label", countryName);
					results.put(r);
				}
			} else if ("".equals(selectedCountry)) {
			
			}else {
				org.codehaus.jettison.json.JSONObject country = (org.codehaus.jettison.json.JSONObject)obj.get(selectedCountry);
				org.codehaus.jettison.json.JSONObject divisions = (org.codehaus.jettison.json.JSONObject)country.get("divisions");
				Iterator iter =  divisions.keys();
				while(iter.hasNext()) {
					String divisionCode = (String)iter.next();
					String divisionName = divisions.getString(divisionCode);
					org.codehaus.jettison.json.JSONObject r = new org.codehaus.jettison.json.JSONObject();
					r.put("value", divisionCode);
					r.put("label", divisionName);
					results.put(r);
				}
			}
		}
		
		results.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward getAddressAndPhoneHistoryAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		
		String demographicNo = request.getParameter("demographicNo");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		if(demographicNo != null) {
			List<DemographicArchive> archives = demographicArchiveDao.findByDemographicNoChronologically(Integer.parseInt(demographicNo));
			
			String address;
			String city;
			String province;
			String postal;
			String phone;
			String phone2;
			String cell;
			String hPhoneExt;
			String wPhoneExt;
						
			List<DemographicHistoryItem> items = new ArrayList<DemographicHistoryItem>();
			
			for(DemographicArchive archive:archives) {
				
				address="";
				city="";
				province="";
				postal="";
				phone="";
				phone2="";
				cell="";
				hPhoneExt="";
				wPhoneExt="";
				
				List<DemographicExtArchive> exts = demographicExtArchiveDao.getDemographicExtArchiveByArchiveId(archive.getId());
				java.util.Map<String,DemographicExtArchive> extMap = new java.util.HashMap<String,DemographicExtArchive>();
				for(DemographicExtArchive ext:exts) {
					extMap.put(ext.getKey(),ext);
				}
					
				if(!address.equals(archive.getAddress()) || !city.equals(archive.getCity())
					||!province.equals(archive.getProvince()) || !postal.equals(archive.getPostal())) {
					
					items.add(new DemographicHistoryItem(new String(archive.getAddress() +", " + archive.getCity() + "," + archive.getProvince() + "," + archive.getPostal()),"address",archive.getLastUpdateDate()));
					address=archive.getAddress();
					city = archive.getCity();
					province = archive.getProvince();
					postal = archive.getPostal();
				}
				
				if(!phone.equals(archive.getPhone()) || (extMap.get("hPhoneExt") != null && !extMap.get("hPhoneExt").getValue().equals(hPhoneExt))) {
					//new home phone
					items.add(new DemographicHistoryItem(archive.getPhone() + (extMap.get("hPhoneExt")!=null?"x"+extMap.get("hPhoneExt").getValue():""),"phone",archive.getLastUpdateDate()));
					phone = archive.getPhone();
					hPhoneExt = extMap.get("hPhoneExt")!=null?extMap.get("hPhoneExt").getValue():"";
				}
				
				if(!phone2.equals(archive.getPhone2()) || (extMap.get("wPhoneExt") != null && !extMap.get("wPhoneExt").getValue().equals(wPhoneExt))) {
					//new work phone
					items.add(new DemographicHistoryItem(archive.getPhone2() + (extMap.get("wPhoneExt")!=null?"x"+extMap.get("wPhoneExt").getValue():""),"phone2",archive.getLastUpdateDate()));
					phone2 = archive.getPhone2();
					wPhoneExt = extMap.get("wPhoneExt")!=null?extMap.get("wPhoneExt").getValue():"";
				}
				
				if((extMap.get("demo_cell") != null && extMap.get("demo_cell").getValue() != null && !extMap.get("demo_cell").getValue().equals(cell))) {
					//new cell phone
					items.add(new DemographicHistoryItem(extMap.get("demo_cell").getValue(),"cell",archive.getLastUpdateDate()));
					cell = extMap.get("demo_cell").getValue();
				}
				
			}
			
			 response.getWriter().print(JSONArray.fromObject(items));			

		}
	
		
		return null;
	}
	

	public ActionForward checkForDuplicates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String yearOfBirth = request.getParameter("yearOfBirth");
		String monthOfBirth = request.getParameter("monthOfBirth");
		String dayOfBirth = request.getParameter("dayOfBirth");
		
		List<Demographic> duplicateList = demographicDao.getDemographicWithLastFirstDOBExact(lastName,firstName,
				yearOfBirth,monthOfBirth,dayOfBirth);
		
		JSONObject result = new JSONObject();
		result.put("hasDuplicates", false);
		if(duplicateList.size()>0) {
			result.put("hasDuplicates", true);
		}
		
        try {
            JSONObject json = JSONObject.fromObject(result);
            json.write(response.getWriter());
        }catch (IOException e) {        
        	log.error(e.getMessage(), e);
        }
        
        return null;
	}
}
