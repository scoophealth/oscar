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
package oscar.oscarMDS.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.TicklerDao;
import org.oscarehr.common.dao.TicklerLinkDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.common.model.TicklerLink;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;


public class ReportMacroAction extends DispatchAction {
	private static Logger logger = MiscUtils.getLogger();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private TicklerDao ticklerDao = SpringUtils.getBean(TicklerDao.class);
    private TicklerLinkDao ticklerLinkDao = SpringUtils.getBean(TicklerLinkDao.class);
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	JSONObject result = new JSONObject();
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}
  
    	String name = request.getParameter("name");
    	
    	logger.info("RunMacro called with name = " + name);
    	
    	if(name == null) {
    		result.put("success", false);
    		result.put("error","No macro name provided");
        	result.write(response.getWriter());
    		return null;
    	}
    	
    	UserPropertyDAO upDao = SpringUtils.getBean(UserPropertyDAO.class);
		UserProperty up = upDao.getProp(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo(),UserProperty.LAB_MACRO_JSON);
		
		boolean success=false;
		
		//find and run specific macro
		if(up != null && !StringUtils.isEmpty(up.getValue())) {
	    	JSONArray macros = (JSONArray) JSONSerializer.toJSON(up.getValue());
	    	if(macros != null) {
			  	for(int x=0;x<macros.size();x++) {
			  		JSONObject macro = macros.getJSONObject(x);
			  		if(name.equals(macro.getString("name"))) {
			  			success=runMacro(macro,request);
			  		}
			  	}
	    	}
		} else {
			result.put("success", false);
    		result.put("error","No macros defined in provider preferences");
        	result.write(response.getWriter());
    		return null;
		}
    	
    	
    	
    	result.put("success", success);
    	result.write(response.getWriter());
    	return null;
    }
    
    protected boolean runMacro(JSONObject macro, HttpServletRequest request) {
    	logger.info("running macro " + macro.getString("name"));
    	String segmentID = request.getParameter("segmentID");
    	String providerNo = request.getParameter("providerNo");
    	String labType = request.getParameter("labType");
    	String demographicNo = request.getParameter("demographicNo");
    	
    	if(macro.has("acknowledge")) {
    		logger.info("Acknowledging lab " + labType + ":" + segmentID);
    		JSONObject jAck = macro.getJSONObject("acknowledge");
    		String comment = jAck.getString("comment");
    		CommonLabResultData.updateReportStatus(Integer.parseInt(segmentID), providerNo, 'A', comment,labType,skipComment(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo()));	
    	}
    	if(macro.has("tickler") && !StringUtils.isEmpty(demographicNo)) {
    		JSONObject jTickler = macro.getJSONObject("tickler");
    		
    		if(jTickler.has("taskAssignedTo") && jTickler.has("message")) {
    			logger.info("Sending Tickler");
        		Tickler t = new Tickler();
        		t.setTaskAssignedTo(jTickler.getString("taskAssignedTo"));
        		t.setDemographicNo(Integer.parseInt(demographicNo));
        		t.setMessage(jTickler.getString("message"));
        		t.setCreator(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
        		ticklerDao.persist(t);
        		
        		TicklerLink tl = new TicklerLink();
        		tl.setTableId(Long.valueOf(segmentID));
        		tl.setTableName(LabResultData.HL7TEXT);
        		tl.setTicklerNo(t.getId());
        		ticklerLinkDao.persist(tl);
    		} else {
    			logger.info("Cannot sent tickler. Not enough information in macro definition. provider taskAssignedTo and message");
    		}
    		
    	}
    	
    	return true;
    }
    
    private boolean skipComment(String providerNo) {
		UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
		UserProperty uProp = userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);
		boolean skipComment = false;
		if( uProp != null && uProp.getValue().equalsIgnoreCase("yes")) {
			skipComment = true;
		}
		return skipComment;
    }
}
