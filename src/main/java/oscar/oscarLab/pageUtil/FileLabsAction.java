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


package oscar.oscarLab.pageUtil;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;

public class FileLabsAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   public FileLabsAction() {
   }

   public ActionForward unspecified(ActionMapping mapping,
   ActionForm form,
   HttpServletRequest request,
   HttpServletResponse response)
   throws ServletException, IOException {
	   
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}

      String providerNo = (String) request.getSession().getAttribute("user");
      String searchProviderNo = request.getParameter("searchProviderNo");
      String status = request.getParameter("status");

      String[] flaggedLabs = request.getParameterValues("flaggedLabs");

      String[] labTypes = CommonLabResultData.getLabTypes();
      ArrayList<String[]> listFlaggedLabs = new ArrayList<String[]>();

      if(flaggedLabs != null && labTypes != null){
         for (int i = 0; i < flaggedLabs.length; i++){
            for (int j = 0; j < labTypes.length; j++){
               String s =  request.getParameter("labType"+flaggedLabs[i]+labTypes[j]);

               if (s != null){  //This means that the lab was of this type.
                  String[] la =  new String[] {flaggedLabs[i],labTypes[j]};
                  MiscUtils.getLogger().debug("ADDING lab "+flaggedLabs[i]+" of lab type "+labTypes[j]);
                  listFlaggedLabs.add(la);
                  j = labTypes.length;

               }
            }
         }
      }

      String newURL = "";


         CommonLabResultData.fileLabs(listFlaggedLabs, providerNo);
         newURL = mapping.findForward("success").getPath();
         newURL = newURL + "&providerNo="+providerNo+"&searchProviderNo="+searchProviderNo+"&status="+status;
         if (request.getParameter("lname") != null) { newURL = newURL + "&lname="+request.getParameter("lname"); }
         if (request.getParameter("fname") != null) { newURL = newURL + "&fname="+request.getParameter("fname"); }
         if (request.getParameter("hnum")  != null) { newURL = newURL + "&hnum="+request.getParameter("hnum"); }
         //MiscUtils.getLogger().info(newURL);
      return (new ActionForward(newURL));
   }

   public ActionForward fileLabAjax(ActionMapping mapping,
   ActionForm form,
   HttpServletRequest request,
   HttpServletResponse response)
   {

	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}
	   
      String providerNo = (String) request.getSession().getAttribute("user");
      String flaggedLab=request.getParameter("flaggedLabId").trim();
      String labType=request.getParameter("labType").trim();

      ArrayList<String[]> listFlaggedLabs = new ArrayList<String[]>();
      String[] la =  new String[] {flaggedLab,labType};
      listFlaggedLabs.add(la);
      CommonLabResultData.fileLabs(listFlaggedLabs, providerNo);

      return null;
}
}
