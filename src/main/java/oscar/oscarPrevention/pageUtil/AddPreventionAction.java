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


package oscar.oscarPrevention.pageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.provider.model.PreventionManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarPrevention.PreventionData;
/**
 *
 * @author Jay Gallagher
 */
public class AddPreventionAction  extends Action {
   

   public AddPreventionAction() {
   }
   
      public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)  {
                                   
         String sessionUser  = (String) request.getSession().getAttribute("user");
         if ( sessionUser == null){
            return mapping.findForward("Logout");
         }
         String preventionType = request.getParameter("prevention");
         String demographic_no = request.getParameter("demographic_no");
         String id = request.getParameter("id");
         String delete = request.getParameter("delete");
         
         MiscUtils.getLogger().debug("id "+id+"  delete "+ delete);
         
         MiscUtils.getLogger().debug("prevention Type "+preventionType);
         
         String given = request.getParameter("given");
         String prevDate = request.getParameter("prevDate");
         String providerName = request.getParameter("providerName");
         String providerNo = request.getParameter("provider");
         
         
         String nextDate = request.getParameter("nextDate");
         String neverWarn = request.getParameter("neverWarn");
         
         
         MiscUtils.getLogger().debug("nextDate "+nextDate+" neverWarn "+neverWarn);
         
         String refused = "0";
         if (given != null && given.equals("refused")){
        	 refused = "1";
         }else if (given != null && given.equals("ineligible")){
        	 refused = "2";
         }else if (given != null && given.equals("never")){
        	 refused = "1";
         }else if (given != null && given.equals("previous")){
        	 refused = "2";
         }
         
         
         if (neverWarn != null && neverWarn.equals("neverRemind")){
            neverWarn = "1";
         }else{
            neverWarn = "0";
         }
         
         ArrayList<Map<String,String>> extraData = new ArrayList<Map<String,String>>();
                  
         addHashtoArray(extraData,request.getParameter("location"),"location");
         addHashtoArray(extraData,request.getParameter("lot"),"lot");                 
         addHashtoArray(extraData,request.getParameter("route"),"route");
         addHashtoArray(extraData,request.getParameter("dose"),"dose");
         addHashtoArray(extraData,request.getParameter("comments"),"comments");                 
         addHashtoArray(extraData,request.getParameter("result"),"result");                 
         addHashtoArray(extraData,request.getParameter("reason"),"reason");           
         addHashtoArray(extraData,request.getParameter("neverReason"),"neverReason");
         addHashtoArray(extraData,request.getParameter("manufacture"),"manufacture");
         addHashtoArray(extraData,request.getParameter("dosage"),"dosage");
         addHashtoArray(extraData,request.getParameter("product"),"product");
         addHashtoArray(extraData,request.getParameter("workflowId"),"workflowId");
         addHashtoArray(extraData,request.getParameter("formId"),"formId");
         addHashtoArray(extraData,request.getParameter("dose1"),"dose1");
         addHashtoArray(extraData,request.getParameter("dose2"),"dose2");
         addHashtoArray(extraData,request.getParameter("chronic"),"chronic");
         addHashtoArray(extraData,request.getParameter("pregnant"),"pregnant");
         addHashtoArray(extraData,request.getParameter("remote"),"remote");
         addHashtoArray(extraData,request.getParameter("healthcareworker"),"healthcareworker");
         addHashtoArray(extraData,request.getParameter("householdcontact"),"householdcontact");
         addHashtoArray(extraData,request.getParameter("firstresponderpolice"),"firstresponderpolice");
         addHashtoArray(extraData,request.getParameter("firstresponderfire"),"firstresponderfire");
         addHashtoArray(extraData,request.getParameter("swineworker"),"swineworker");
         addHashtoArray(extraData,request.getParameter("poultryworker"),"poultryworker");
         addHashtoArray(extraData,request.getParameter("firstnations"),"firstnations");
         addHashtoArray(extraData,request.getParameter("name"),"name");
                                                                                                                           
         if (id == null || id.equals("null")){ //New                                             
        	 PreventionData.insertPreventionData(sessionUser,demographic_no,prevDate,providerNo,providerName,preventionType,refused,nextDate,neverWarn,extraData);            
         }else if (id != null &&  delete != null  ){  // Delete
        	 PreventionData.deletePreventionData(id);               
         }else if (id != null && delete == null ){ //Update
            addHashtoArray(extraData,id,"previousId"); 
            PreventionData.updatetPreventionData(id,sessionUser,demographic_no,prevDate,providerNo,providerName,preventionType,refused,nextDate,neverWarn,extraData);
         }

         ServletContext servletCtx = request.getSession().getServletContext();
         WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletCtx);
         PreventionManager prvMgr = (PreventionManager)ctx.getBean("preventionMgr");
         prvMgr.removePrevention(demographic_no); 
         MiscUtils.getLogger().debug("Given "+given+" prevDate "+prevDate+" providerName "+providerName+" provider "+providerNo);

      return mapping.findForward("success");                                
   }
   
         
  private void addHashtoArray(ArrayList<Map<String,String>> list,String s,String key){
     if ( s != null && key != null){
        Map<String,String> h = new HashMap<String,String>();
        h.put(key,s);    
        list.add(h);
     }
  }
}
