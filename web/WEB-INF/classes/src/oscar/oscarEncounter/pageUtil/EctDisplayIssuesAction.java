// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import oscar.util.StringUtils;

/**
 *
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayIssuesAction extends EctDisplayAction {    
    private String cmd = "issues";
    
    private CaseManagementManager caseManagementMgr;
    private static Log log = LogFactory.getLog(EctDisplayIssuesAction.class);
    
    public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
        this.caseManagementMgr = caseManagementMgr;
    }
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

        //set lefthand module heading and link
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.Issues"));
        Dao.setLeftURL("$('check_issue').value='';document.caseManagementViewForm.submit();");
        
        //set righthand link to same as left so we have visual consistency with other modules
        String url = "return false;";
        Dao.setRightURL(url);        
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action 
        
        //grab all of the diseases associated with patient and add a list item for each
        List issues = null;        
        issues = caseManagementMgr.getIssues(Integer.parseInt(bean.getDemographicNo()));             
        String programId = (String)request.getSession().getAttribute("case_program_id");
        issues = caseManagementMgr.filterIssues(issues,programId);

        for(int idx = 0; idx < issues.size(); ++idx ) {
            NavBarDisplayDAO.Item item = Dao.Item();                                    
            
            CaseManagementIssue issue = (CaseManagementIssue)issues.get(idx);
            String tmp = issue.getIssue().getDescription();
            String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
            
            item.setTitle(strTitle);
            item.setLinkTitle(tmp);
            url = "$('check_issue').value=" + issue.getIssue_id() + ";return filter();";
            item.setURL(url);
            Dao.addItem(item);
        }
        
                
        return true;        
    }
    
    public String getCmd() {
      return cmd;
    }
}
