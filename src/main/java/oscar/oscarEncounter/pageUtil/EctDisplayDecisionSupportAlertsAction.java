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

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *
 * @author apavel
 */
public class EctDisplayDecisionSupportAlertsAction extends EctDisplayAction {
    private String cmd = "Guidelines";
    private static final Logger logger = MiscUtils.getLogger();

  public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

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

        int index = 0;
        for(DSGuideline dsGuideline: dsGuidelines) {
            try {
                List<DSConsequence> dsConsequences = dsGuideline.evaluate(bean.demographicNo);
                if (dsConsequences == null) continue;
                for (DSConsequence dsConsequence: dsConsequences) {
                    if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.warning)
                        continue;
                    index++;
                    NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                    winName = dsConsequence.getConsequenceType().toString() + bean.demographicNo;
                    
                    url = "popupPage(500,950,'" + winName + "','" + request.getContextPath() + "/oscarEncounter/decisionSupport/guidelineAction.do?method=detail&guidelineId=" + dsGuideline.getId() + "&provider_no=" + bean.providerNo + "&demographic_no=" + bean.demographicNo + "&parentAjaxId=" + cmd + "'); return false;";
                    //Date date = (Date)curform.get("formDateAsDate");
                    //String formattedDate = DateUtils.getDate(date,dateFormat,request.getLocale());
                    key = StringUtils.maxLenString(dsConsequence.getText(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES);
                    item.setLinkTitle(dsConsequence.getText());
                    key = StringEscapeUtils.escapeJavaScript(key);
                    js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
                    javascript.append(js);
                    url += "return false;";
                    item.setURL(url);
                    String strTitle = StringUtils.maxLenString(dsConsequence.getText(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
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
        return true;
      }
  }

  public String getCmd() {
        return cmd;
  }
}
