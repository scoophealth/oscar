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
 * MsgDisplayDemographicMessagesAction.java
 *
 * Created on May 8, 2005, 12:20 AM
 */

package oscar.oscarMessenger.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.util.MsgDemoMap;

/**
 *
 * @author root
 */
public class MsgDisplayDemographicMessagesAction extends Action {


	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "r", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
    	
            // Extract attributes we will need
        
            MiscUtils.getLogger().debug("this is displayDemographicMessages.Action");
            
            // Setup variables            
            oscar.oscarMessenger.pageUtil.MsgSessionBean bean = null;
            String[] messageNo = ((MsgDisplayDemographicMessagesForm)form).getMessageNo();
            //String unlinkMsg = ((MsgDisplayDemographicMessagesForm)form).getUnlinkMsg();
            
            //Initialize forward location
            String findForward = "success";

            //if(request.getParameter("providerNo")!=null & request.getParameter("userName")!=null)
            if(request.getSession().getAttribute("msgSessionBean") == null)
            {

                bean = new oscar.oscarMessenger.pageUtil.MsgSessionBean();
                bean.setProviderNo((String) request.getSession().getAttribute("user"));
                bean.setUserName(request.getParameter("userName"));
                bean.setDemographic_no(request.getParameter("demographic_no"));

                request.getSession().setAttribute("msgSessionBean", bean);
                                
            }//if
            else
            {
                bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
            }//else
            
            
            //Unlinked selected messages
            if(messageNo!=null){              
                MsgDemoMap msgDemoMap = new MsgDemoMap();                                              
                for (int i =0 ; i < messageNo.length ; i++){
                    msgDemoMap.unlinkMsg(request.getParameter("demographic_no"), messageNo[i]);                            
                }
                                 
                //Forward to DisplayDemographiMessage.jsp
                findForward = "demoMsg";                
            }
            
    return (mapping.findForward(findForward));
    }
}
