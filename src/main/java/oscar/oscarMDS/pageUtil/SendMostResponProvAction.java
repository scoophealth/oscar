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

package oscar.oscarMDS.pageUtil;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;
/**
 *
 * @author jackson
 */
public class SendMostResponProvAction extends Action{
        
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
        public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
    			throw new SecurityException("missing required security object (_lab)");
    		}
        	
                String demoId=request.getParameter("demoId");
                String docLabId=request.getParameter("docLabId");
                String docLabType=request.getParameter("docLabType");
                //MiscUtils.getLogger().info(demoId+"--"+docLabId+"--"+docLabType);
                ArrayList listFlaggedLabs = new ArrayList();
                if(demoId!=null && docLabId!=null && docLabType!=null){
                    demoId=demoId.trim();
                    Demographic demog=demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.parseInt(demoId));
                    String mrp=demog.getProviderNo();
                    //MiscUtils.getLogger().info(mrp);
                     String[] la =  new String[] {docLabId,docLabType};
                      listFlaggedLabs.add(la);
                      CommonLabResultData.updateLabRouting(listFlaggedLabs, mrp);
                }else{
                    //return error in json
                }
            return null;
        }
}
