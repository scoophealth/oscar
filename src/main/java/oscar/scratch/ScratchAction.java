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


package oscar.scratch;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ScratchPadDao;
import org.oscarehr.common.model.ScratchPad;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author jay
 */
public class ScratchAction extends DispatchAction {
    
    public ActionForward showVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String id = request.getParameter("id");
    	ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);
    	ScratchPad scratchPad = dao.find(Integer.parseInt(id));
    	
    	request.setAttribute("ScratchPad", scratchPad);
    	return mapping.findForward("scratchPadVersion");
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String providerNo =  (String) request.getSession().getAttribute("user");
        String pNo = request.getParameter("providerNo");
                
        if(providerNo.equals(pNo)){
        String id = request.getParameter("id");
        String dirty = request.getParameter("dirty");
        String scratchPad = request.getParameter("scratchpad");
        String windowId = request.getParameter("windowId");
        String returnId = "";
        String returnText = scratchPad;
        MiscUtils.getLogger().debug("pro "+providerNo+" id "+id+" dirty "+dirty+" scatchPad "+scratchPad);
        ScratchData scratch = new ScratchData();
        Map<String, String> h = scratch.getLatest(providerNo);
        
        
        if (h == null){  //FIRST TIME USE
           if(dirty != null && dirty.equals("1")){
               returnId = scratch.insert(providerNo,scratchPad); 
               returnText = scratchPad;
           }               
        }else{
           returnText = h.get("text");  
        //Get current Id in scratch table
           int databaseId = Integer.parseInt(h.get("id"));
           returnId = ""+databaseId;
           MiscUtils.getLogger().debug( "database Id = "+databaseId+" request id "+id);
           if (databaseId > Integer.parseInt(id)){           //check to see if the id in database is higher than in the request
              MiscUtils.getLogger().debug(" DAtabase greater than id");
              if (dirty.equals("1")){//Is dirty field set?
                 //BIG PROBS,return warning that there is was concurrent editing, would you like to update to the latest.
              }else{//No Dirty flag?  return latest Text
                  
              }
           }else{
               if (dirty.equals("1")){               //if its the same, is the dirty field set
                 returnId = scratch.insert(providerNo,scratchPad);   //save new record and return new id.
                  returnText = scratchPad;
                  MiscUtils.getLogger().debug("dirty field set");
               }
           }    
           
        }
        response.getWriter().print("id="+URLEncoder.encode(returnId,"utf-8")+"&text="+URLEncoder.encode(returnText,"utf-8")+"&windowId="+URLEncoder.encode(windowId,"utf-8"));
        
        }else{
        	MiscUtils.getLogger().error("Scratch pad trying to save data for user " + pNo + " but session user is " + providerNo);
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        return null;      
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String id = request.getParameter("id");
    	
    	ScratchPadDao scratchDao = SpringUtils.getBean(ScratchPadDao.class);
    	ScratchPad scratch = scratchDao.find(Integer.parseInt(id));
    	scratch.setStatus(false);
        scratchDao.merge(scratch);
  	    	
    	request.setAttribute("actionDeleted", "version " + id + " was deleted!");
    	return mapping.findForward("scratchPadVersion");
    }
    
    
}
