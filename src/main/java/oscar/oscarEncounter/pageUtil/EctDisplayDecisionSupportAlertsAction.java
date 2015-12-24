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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarEncounter.pageUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.renal.CkdScreener;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *
 * @author apavel
 */
public class EctDisplayDecisionSupportAlertsAction extends EctDisplayAction {
    private String cmd = "Guidelines";
    private static final Logger logger = MiscUtils.getLogger();
    
    private DxresearchDAO dxResearchDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
	

  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

	  long timer=System.currentTimeMillis();
	  
	  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	  boolean a = true;
      Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.decisionSupportAlerts");
      String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
      a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
      if(!a) {
           return true; //decisionSupportAlerts link won't show up on new CME screen.
      } else {    	
  	
        //set lefthand module heading and link
        String winName = "dsalert" + bean.demographicNo;
        String url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/decisionSupport/guidelineAction.do?method=list&provider_no=" + bean.providerNo + "&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "global.decisionSupportAlerts"));
        Dao.setLeftURL(url);

        //set the right hand heading link
        winName = "AddeForm" + bean.demographicNo;
        
        Dao.setRightURL(url);
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

        StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
        String js = "";

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
        DSService  dsService =  (DSService) ctx.getBean("dsService");

        List<DSGuideline> dsGuidelines = dsService.getDsGuidelinesByProvider(bean.providerNo);

        String key;
        
        String BGCOLOUR = request.getParameter("hC");
      
        logger.debug("getInfo part1 TimeMs : "+(System.currentTimeMillis()-timer));
        timer=System.currentTimeMillis();
        
        //ORN CKD Pilot Code
        if(OscarProperties.getInstance().getProperty("ORN_PILOT", "yes").equalsIgnoreCase("yes") && (OscarProperties.getInstance().getProperty("ckd_notification_scheme","dsa").equals("dsa")||OscarProperties.getInstance().getProperty("ckd_notification_scheme","dsa").equals("all"))) {
	        CkdScreener ckdScreener = new CkdScreener();
	        List<String> reasons =new ArrayList<String>();
	        boolean match = ckdScreener.screenDemographic(Integer.parseInt(bean.demographicNo),reasons, null);
	        boolean notify=false;
	        
	        for(Dxresearch dr:dxResearchDao.find(Integer.parseInt(bean.demographicNo), "OscarCode", "CKDSCREEN")) {
	        	//we have an active one, we should notify
	        	if(dr.getStatus() == 'A') {
	        		notify=true;
	        	}
	        }
	        for(Dxresearch dr:dxResearchDao.find(Integer.parseInt(bean.demographicNo), "icd9", "585")) {
	        	if(dr.getStatus() == 'A') {
	        		notify=false;
	        	}
	        }
	        if(!notify) {
	        	//there's no active ones, but let's look at the latest one
	        	List<Dxresearch> drs = dxResearchDao.find(Integer.parseInt(bean.demographicNo), "OscarCode", "CKDSCREEN");
	        	if(drs.size()>0) {
	        		Dxresearch dr = drs.get(0);
	        		Calendar aYearAgo = Calendar.getInstance();
	        		aYearAgo.add(Calendar.MONTH, -12);
	        		if(dr.getUpdateDate().before(aYearAgo.getTime())) {
	        			notify=true;
	        			//reopen it
	        			dr.setStatus('A');
	        			dr.setUpdateDate(new Date());
	        			dxResearchDao.merge(dr);
	        			//need some way to notify that tab to reload
	        			javascript.append("jQuery(document).ready(function(){reloadNav('Dx');});");
	        		}
	        	}
	        }
	        if(match && notify) {
	        	 NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
	        	 url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/renal/CkdDSA.do?method=detail&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";              
	        	 item.setURL(url);	 
	        	 item.setLinkTitle("Based on guidelines, a CKD screening should be performed.");
	        	 item.setTitle("Screen for CKD");
	        	 item.setColour("#ff5409;");
	        	 Dao.addItem(item);
	        }
        }
        
        logger.debug("getInfo part2 TimeMs : "+(System.currentTimeMillis()-timer));
        timer=System.currentTimeMillis();
        
        for(DSGuideline dsGuideline: dsGuidelines) {
        	if(OscarProperties.getInstance().getProperty("dsa.skip."+dsGuideline.getTitle().replaceAll(" ", "_"),"false").equals("true")) {
        		continue;
        	}
            try {
                List<DSConsequence> dsConsequences = dsGuideline.evaluate(loggedInInfo, bean.demographicNo);
                if (dsConsequences == null) continue;
                for (DSConsequence dsConsequence: dsConsequences) {
                    if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.warning)
                        continue;
                    
                    NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                    winName = dsConsequence.getConsequenceType().toString() + bean.demographicNo;
                    
                    url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=" + dsGuideline.getId() + "&provider_no=" + bean.providerNo + "&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
                    //Date date = (Date)curform.get("formDateAsDate");
                    //String formattedDate = DateUtils.getDate(date,dateFormat,request.getLocale());
                    key = StringUtils.maxLenString(dsConsequence.getText(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES);
                    item.setLinkTitle(dsGuideline.getTitle());
                    key = StringEscapeUtils.escapeJavaScript(key);
                    js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
                    javascript.append(js);
                    url += "return false;";
                    item.setURL(url);
                    String strTitle = StringUtils.maxLenString(dsGuideline.getTitle(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                    item.setTitle(strTitle);
                    if (dsConsequence.getConsequenceStrength() == DSConsequence.ConsequenceStrength.warning) {
                        item.setColour("#ff5409;");
                    }
                    //item.setDate(new Date());
                    Dao.addItem(item);
                }
            } catch (Exception e) {
                logger.error("Unable to evaluate patient against a DS guideline '" + dsGuideline.getTitle() + "' of UUID '" + dsGuideline.getUuid() + "'", e);
            }
        }

        javascript.append("</script>");
        Dao.setJavaScript(javascript.toString());

        logger.debug("getInfo part3 TimeMs : "+(System.currentTimeMillis()-timer));

        return true;
      }
  }

  public String getCmd() {
        return cmd;
  }
}
