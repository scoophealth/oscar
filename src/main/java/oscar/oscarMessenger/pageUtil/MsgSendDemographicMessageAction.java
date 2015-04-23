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
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author jay
 */
public class MsgSendDemographicMessageAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /**
     * Creates a new instance of MsgSendDemographicMessageAction 
     */
    public MsgSendDemographicMessageAction() {
    }

    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
       String provNo = (String) request.getSession().getAttribute("user");
       
       if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "w", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
       
       if ( request.getSession().getAttribute("msgSessionBean") == null){
           MsgSessionBean bean = new MsgSessionBean();
           bean.setProviderNo(provNo);
           ProviderData pd = new ProviderData();
           bean.setUserName(ProviderData.getProviderName(provNo));
           request.getSession().setAttribute("msgSessionBean", bean); 
       } 
        
       request.setAttribute("demographic_no", request.getParameter("demographic_no"));
       return mapping.findForward("success");   
    }
}
