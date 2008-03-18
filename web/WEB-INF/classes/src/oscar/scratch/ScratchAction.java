/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. 
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * ScratchAction.java
 *
 * Created on September 2, 2006, 8:18 AM
 *
 */

package oscar.scratch;

import java.net.URLEncoder;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jay
 */
public class ScratchAction extends Action {
    
    /** Creates a new instance of ScratchAction */
    public ScratchAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //String request.getParameter("");
        //System.out.println("ScratchAction Jackson");
        String providerNo =  (String) request.getSession().getAttribute("user");
        String id = (String) request.getParameter("id");
        String dirty = (String) request.getParameter("dirty");
        String scratchPad = (String) request.getParameter("scratchpad");
        String windowId = (String) request.getParameter("windowId");
        String returnId = "";
        String returnText = scratchPad;
        System.out.println("pro "+providerNo+" id "+id+" dirty "+dirty+" scatchPad "+scratchPad);
        ScratchData scratch = new ScratchData();
        Hashtable h = scratch.getLatest(providerNo);
        
        
        if (h == null){  //FIRST TIME USE
           if(dirty != null && dirty.equals("1")){
               returnId = scratch.insert(providerNo,scratchPad); 
               returnText = scratchPad;
           }               
        }else{
           returnText = (String) h.get("text");  
        //Get current Id in scratch table
           int databaseId = Integer.parseInt( (String) h.get("id"));
           returnId = ""+databaseId;
           System.out.println( "database Id = "+databaseId+" request id "+id);
           if (databaseId > Integer.parseInt(id)){           //check to see if the id in database is higher than in the request
              System.out.println(" DAtabase greater than id");
              if (dirty.equals("1")){//Is dirty field set?
                 //BIG PROBS,return warning that there is was concurrent editing, would you like to update to the latest.
              }else{//No Dirty flag?  return latest Text
                  
              }
           }else{
               if (dirty.equals("1")){               //if its the same, is the dirty field set
                  System.out.println("INSERTING NEW TEXT");
                  returnId = scratch.insert(providerNo,scratchPad);   //save new record and return new id.
                  returnText = scratchPad;
               }
           }    
           
        }
        response.getWriter().print("id="+URLEncoder.encode(returnId,"utf-8")+"&text="+URLEncoder.encode(returnText,"utf-8")+"&windowId="+URLEncoder.encode(windowId,"utf-8"));
        return null;
               
    }
    
    
}
