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
package org.oscarehr.rx.dispensary;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DrugDispensingDao;
import org.oscarehr.common.dao.DrugDispensingMappingDao;
import org.oscarehr.common.dao.DrugProductDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DrugDispensing;
import org.oscarehr.common.model.DrugDispensingMapping;
import org.oscarehr.common.model.DrugProduct;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DispensaryAction extends DispatchAction {

	private DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private DrugProductDao drugProductDao = SpringUtils.getBean(DrugProductDao.class);
	private DrugDispensingDao drugDispensingDao = SpringUtils.getBean(DrugDispensingDao.class);
	private DrugDispensingMappingDao drugDispensingMappingDao = SpringUtils.getBean(DrugDispensingMappingDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return mapping.findForward("list");
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		
		String id = request.getParameter("id");
		if(id == null) {
			id = (String)request.getAttribute("drugId");
			if(id == null) {
				id = request.getParameter("drugId");
			}
		}
		
		Drug drug = null;
		if(id != null && id.length()>0) {
			drug= drugDao.find(Integer.parseInt(id));
			if(drug != null) {
				Demographic demographic = demographicDao.getDemographicById(drug.getDemographicId());
				request.setAttribute("demographic", demographic);
				request.setAttribute("providers", providerDao.getActiveProviders());
				request.setAttribute("drug", drug);
			}
		}
		
		request.setAttribute("products", drugProductDao.findAllAvailableUnique());
		
		//try to find a direct mapping, from the drug's regional_idenifier (DIN) and special (the instructions)
		DrugDispensingMapping ddm = drugDispensingMappingDao.findMapping(drug.getRegionalIdentifier(), drug.getDuration(),
				drug.getDurUnit(), drug.getFreqCode(), drug.getQuantity(), drug.getTakeMin(), drug.getTakeMax());
		if(ddm != null) {
			request.setAttribute("selectedProductCode",ddm.getProductCode());
		}
		
		Map<String,String> providerNames = new HashMap<String,String>();
		Map<Integer,String> details = new HashMap<Integer,String>();
		Map<Integer,Integer> productAmounts = new HashMap<Integer,Integer>();
		
		
		List<DrugDispensing> dispensingEvents = drugDispensingDao.findByDrugId(Integer.parseInt(id));
		
		
		for(DrugDispensing dd:dispensingEvents) {
			if(dd.isArchived()) {
				continue;
			}
			int totalAmountForDD = 0;
			
			if(providerNames.get(dd.getDispensingProviderNo()) == null) {
				providerNames.put(dd.getDispensingProviderNo(), providerDao.getProvider(dd.getDispensingProviderNo()).getFormattedName());
			}
			if(providerNames.get(dd.getProviderNo()) == null) {
				providerNames.put(dd.getProviderNo(), providerDao.getProvider(dd.getProviderNo()).getFormattedName());
			}
			StringBuilder sb = new StringBuilder("<table id=\"details"+dd.getId()+"\">");
			List<DrugProduct> dps = drugProductDao.findByDispensingId(dd.getId());
			for(int x=0;x<dps.size();x++) {
				DrugProduct dp = dps.get(x);
				sb.append("<tr><td>" + dp.getLotNumber() + "</td><td>"+dp.getExpiryDateAsString()+"</td></tr>");
				totalAmountForDD += dp.getAmount();
			}
			sb.append("</table>");
			details.put(dd.getId(),sb.toString());
			
			
			productAmounts.put(dd.getId(),totalAmountForDD);
			
			
		}
		request.setAttribute("dispensingEvents",dispensingEvents);
		request.setAttribute("productAmounts",productAmounts);
		request.setAttribute("providerNames", providerNames);
		request.setAttribute("details", details);
		request.setAttribute("id",id);

	    return mapping.findForward("list");
	}
	
	public ActionForward saveEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
	
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dispensing", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DrugDispensing dd = new DrugDispensing();
		dd.setDateCreated(new Date());
		dd.setDispensingProviderNo(request.getParameter("dispensedBy"));
		dd.setDrugId(Integer.parseInt(request.getParameter("drugId")));
		dd.setNotes(request.getParameter("notes"));
		dd.setProviderNo(providerNo);
		dd.setPaidFor(false);
		
		String code = request.getParameter("product");
		//if the drop down was read-only, we passed it as "productCode" instead.
		if(code == null) {
			code = request.getParameter("productCode");
		}
		
		if(request.getSession().getAttribute("case_program_id") != null) {
			String programId = (String)request.getSession().getAttribute("case_program_id");
			Integer pId = null;
			try {
			 pId = Integer.valueOf(programId);
			}catch(NumberFormatException e) {
				//nothing
			}
			dd.setProgramNo(pId);
		}
		
		
		dd.setUnit("");
		
		DrugDispensingDao drugDispensingDao = SpringUtils.getBean(DrugDispensingDao.class);
		drugDispensingDao.persist(dd);
		
		
		int qty = 0;
		Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()) {
			String key = params.nextElement();
			if(key.startsWith("dispense_")) {
				String value = request.getParameter(key);
				int amount = Integer.parseInt(value);
				String hash = key.substring("dispense_".length());
				//lets find x of these candidates, and take them
				List<LotBean> lots = drugProductDao.findDistinctLotsAvailableByCode(code); 
				for(LotBean lot:lots) {
					if(lot.getHash().equals(hash)) {
						//ok, we found the right category of them
						//lets get the actual list
						List<DrugProduct> dps = drugProductDao.getAvailableDrugProducts(lot.getName(), lot.getExpiryDate(), lot.getAmount());
						//should check that amount available >= amount here
						
						for(int x=0;x<amount;x++) {
							DrugProduct dp = dps.get(x);
							dp.setDispensingEvent(dd.getId());
							drugProductDao.merge(dp);
							qty++;
						}
						
						break;
					}
				}
			}
			
			dd.setQuantity(qty);
			drugDispensingDao.merge(dd);
			
		}
		
		
		request.setAttribute("drugId", request.getParameter("drugId"));
		ActionForward af = new ActionForward();
		af.setRedirect(true);
		af.setPath("/oscarRx/Dispense.do?method=view&drugId="+request.getParameter("drugId"));
		return af;
	}
	
	public ActionForward getProductsByCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String code = request.getParameter("code");
		
		List<DrugProduct> dps = drugProductDao.findAvailableByCode(code);
		
        JsonConfig config = new JsonConfig();
        config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONArray jsonArray = JSONArray.fromObject( dps , config);
        response.getWriter().print(jsonArray);

        return null;

	}
	
	public ActionForward findDistinctLotsAvailableByCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
	String code = request.getParameter("code");
	
	List<LotBean> dps = drugProductDao.findDistinctLotsAvailableByCode(code);
	
    JsonConfig config = new JsonConfig();
    config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

	JSONArray jsonArray = JSONArray.fromObject( dps , config);
    response.getWriter().print(jsonArray);

    return null;

	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dispensing", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		Integer eventId = null;
		
		try {
			String strEventId = request.getParameter("eventId");
			if(strEventId != null) {
				eventId = Integer.parseInt(strEventId);
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(eventId != null) {
			for(DrugProduct dp:drugProductDao.findByDispensingId(eventId)) {
				DrugDispensing dd = drugDispensingDao.find(dp.getDispensingEvent());
				if(dd != null) {
					dd.setArchived(true);
					drugDispensingDao.merge(dd);
				}
				dp.setDispensingEvent(null);
				drugProductDao.merge(dp);
				MiscUtils.getLogger().debug("deleted from drug product " + dp.getId());
			}
		}
		
		return view(mapping,form,request,response);
	}
}
