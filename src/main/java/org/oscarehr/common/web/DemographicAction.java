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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtArchiveDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.DemographicExtArchive;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
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
	private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
	
	
	public ActionForward getAddressAndPhoneHistoryAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		
		String demographicNo = request.getParameter("demographicNo");
		
		Demographic demographic = demographicDao.getDemographic(demographicNo);
		List<DemographicExt> exts = demographicExtDao.getDemographicExtByDemographicNo(Integer.parseInt(demographicNo));
		java.util.Map<String,DemographicExt> extMap = new java.util.HashMap<String,DemographicExt>();
		for(DemographicExt ext:exts) {
			extMap.put(ext.getKey(),ext);
		}
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		if(demographicNo != null) {
			List<DemographicArchive> archives = demographicArchiveDao.findByDemographicNoChronologically(Integer.parseInt(demographicNo));
			Collections.reverse(archives);
			
			String address = demographic.getAddress();
			String city = demographic.getCity();
			String province = demographic.getProvince();
			String postal = demographic.getPostal();
			String phone = demographic.getPhone();
			String phone2 = demographic.getPhone2();
			String cell = extMap.get("demo_cell")!=null?extMap.get("demo_cell").getValue():"";
			String hPhoneExt = extMap.get("hPhoneExt")!=null?extMap.get("hPhoneExt").getValue():"";
			String wPhoneExt = extMap.get("wPhoneExt")!=null?extMap.get("wPhoneExt").getValue():"";
						
			List<DemographicHistoryItem> items = new ArrayList<DemographicHistoryItem>();

			
			for(DemographicArchive archive:archives) {
				
				List<DemographicExtArchive> exts2 = demographicExtArchiveDao.getDemographicExtArchiveByArchiveId(archive.getId());
				java.util.Map<String,DemographicExtArchive> extMap2 = new java.util.HashMap<String,DemographicExtArchive>();
				for(DemographicExtArchive ext2:exts2) {
					extMap2.put(ext2.getKey(),ext2);
				}
					
				String lastUpdateUser = "N/A";
				if(!StringUtils.isEmpty(archive.getLastUpdateUser())) {
					lastUpdateUser = providerManager.getProvider(archive.getLastUpdateUser()).getFormattedName();
				}
				
				if(!address.equals(archive.getAddress()) || !city.equals(archive.getCity())
					||!province.equals(archive.getProvince()) || !postal.equals(archive.getPostal())) {
					
					items.add(new DemographicHistoryItem(new String(archive.getAddress() +", " + archive.getCity() + "," + archive.getProvince() + "," + archive.getPostal()),"address",archive.getLastUpdateDate(),lastUpdateUser));
					address=archive.getAddress();
					city = archive.getCity();
					province = archive.getProvince();
					postal = archive.getPostal();
				}
				
				if(!phone.equals(archive.getPhone()) || (extMap2.get("hPhoneExt") != null && !extMap2.get("hPhoneExt").getValue().equals(hPhoneExt))) {
					//new home phone
					items.add(new DemographicHistoryItem(archive.getPhone() + ((extMap2.get("hPhoneExt")!=null&&!extMap2.get("hPhoneExt").getValue().isEmpty())?"x"+extMap2.get("hPhoneExt").getValue():""),"phone",archive.getLastUpdateDate(),lastUpdateUser));
					phone = archive.getPhone();
					hPhoneExt = extMap2.get("hPhoneExt")!=null?extMap2.get("hPhoneExt").getValue():"";
				}
				
				if(!phone2.equals(archive.getPhone2()) || (extMap2.get("wPhoneExt") != null && !extMap2.get("wPhoneExt").getValue().equals(wPhoneExt))) {
					//new work phone
					items.add(new DemographicHistoryItem(archive.getPhone2() + ((extMap2.get("wPhoneExt")!=null&&!extMap2.get("wPhoneExt").getValue().isEmpty())?"x"+extMap2.get("wPhoneExt").getValue():""),"phone2",archive.getLastUpdateDate(),lastUpdateUser));
					phone2 = archive.getPhone2();
					wPhoneExt = extMap2.get("wPhoneExt")!=null?extMap2.get("wPhoneExt").getValue():"";
				}
				
				if((extMap2.get("demo_cell") != null && !extMap2.get("demo_cell").getValue().equals(cell))) {
					//new cell phone
					items.add(new DemographicHistoryItem(extMap2.get("demo_cell").getValue(),"cell",archive.getLastUpdateDate(),lastUpdateUser));
					cell = extMap2.get("demo_cell").getValue();
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
