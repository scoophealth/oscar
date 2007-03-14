/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 * Jason Gallagher
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of AddPreventionAction
 *
 * AddPreventionAction.java
 *
 * Created on May 25, 2005, 11:54 AM
 */

package oscar.oscarPrevention.pageUtil;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarPrevention.*;

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
         
         System.out.println("id "+id+"  delete "+ delete);
         
         System.out.println("prevention Type "+preventionType);
         
         String given = request.getParameter("given");
         String prevDate = request.getParameter("prevDate");
         String providerName = request.getParameter("providerName");
         String providerNo = request.getParameter("provider");
         
         
         String nextDate = request.getParameter("nextDate");
         String neverWarn = request.getParameter("neverWarn");
         
         
         System.out.println("nextDate "+nextDate+" neverWarn "+neverWarn);
         
	 String refused = "0";
	 if (given != null && given.equals("refused")){
            refused = "1";
         }else if (given != null && given.equals("ineligible")){
            refused = "2";
         }

         
         
         if (neverWarn != null && neverWarn.equals("neverRemind")){
            neverWarn = "1";
         }else{
            neverWarn = "0";
         }
         
         ArrayList extraData = new ArrayList();
         String layout = request.getParameter("layoutType");
                  
         addHashtoArray(extraData,request.getParameter("location"),"location");
         addHashtoArray(extraData,request.getParameter("lot"),"lot");                 
         addHashtoArray(extraData,request.getParameter("route"),"route");                 
         addHashtoArray(extraData,request.getParameter("comments"),"comments");                 
         addHashtoArray(extraData,request.getParameter("result"),"result");                 
         addHashtoArray(extraData,request.getParameter("reason"),"reason");           
         addHashtoArray(extraData,request.getParameter("neverReason"),"neverReason");
         addHashtoArray(extraData,request.getParameter("manufacture"),"manufacture");
         addHashtoArray(extraData,request.getParameter("dosage"),"dosage");
         addHashtoArray(extraData,request.getParameter("product"),"product");
         addHashtoArray(extraData,request.getParameter("workflowId"),"workflowId");
         addHashtoArray(extraData,request.getParameter("formId"),"formId");
         
         
         
         
                                                                                                                  
         PreventionData pd = new  PreventionData() ;         
         if (id == null || id.equals("null")){ //New                                             
            pd.insertPreventionData(sessionUser,demographic_no,prevDate,providerNo,providerName,preventionType,refused,nextDate,neverWarn,extraData);            
         }else if (id != null &&  delete != null  ){  // Delete
            pd.deletePreventionData(id);               
         }else if (id != null && delete == null ){ //Update
            addHashtoArray(extraData,id,"previousId"); 
            pd.updatetPreventionData(id,sessionUser,demographic_no,prevDate,providerNo,providerName,preventionType,refused,nextDate,neverWarn,extraData);
         }
         
         System.out.println("Given "+given+" prevDate "+prevDate+" providerName "+providerName+" provider "+providerNo);

      return mapping.findForward("success");                                
   }
   
         
  private void addHashtoArray(ArrayList list,String s,String key){
     if ( s != null && key != null){
        Hashtable h = new Hashtable();
        h.put(key,s);    
        list.add(h);
     }
  }
}     
