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
// * Date         Implemented By  Company                 Comments
// * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.pageUtil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;

import oscar.oscarProvider.bean.ProviderNameBean;
import oscar.oscarProvider.bean.ProviderNameBeanHandler;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarWaitingList.bean.WLWaitingListBeanHandler;
import oscar.oscarWaitingList.bean.WLWaitingListNameBeanHandler;
import oscar.oscarWaitingList.util.WLWaitingListUtil;
import oscar.util.UtilDateUtilities;

public final class WLSetupDisplayWaitingListAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        
    	
        System.out.println("\n\nWLSetupDisplayWaitingListAction/execute(): just entering.");
        
        String update = request.getParameter("update");
        String remove = request.getParameter("remove");//actually not used for now, may in future?
        
        String waitingListId = "";
        String demographicNo = "";
        String waitingListNote = "";
        String onListSince = "";
        String groupNo = "";
        String providerNo = "";
        
        System.out.println("\n\nWLSetupDisplayWaitingListAction/execute(): update = " + update);
        System.out.println("\n\nWLSetupDisplayWaitingListAction/execute(): remove = " + remove);

    	LazyValidatorForm wlForm = (LazyValidatorForm)form;
        System.out.println("WLSetupDisplayWaitingListAction/execute(): after  (LazyValidatorForm)form ");

        
        String demographicNumSelected =  request.getParameter("demographicNumSelected");
        String wlNoteSelected = request.getParameter("wlNoteSelected");
        String onListSinceSelected =  request.getParameter("onListSinceSelected");

        System.out.println("WLSetupDisplayWaitingListAction/execute(): demographicNumSelected = "+ demographicNumSelected);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): wlNoteSelected = "+ wlNoteSelected);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): onListSinceSelected = "+ onListSinceSelected);
        
        
        if(request.getParameter("waitingListId") != null){
        	waitingListId = (String) request.getParameter("waitingListId");
        }
        
        System.out.println("WLSetupDisplayWaitingListAction/execute(): waitingListId = "+ waitingListId);
        if( update != null  &&  update.equalsIgnoreCase("Y") ){
	        
            demographicNo = request.getParameter(demographicNumSelected);
            waitingListNote = request.getParameter(wlNoteSelected);
            onListSince = request.getParameter(onListSinceSelected);
//	        demographicNo = (String)wlForm.get(demographicNumSelected);
//	        waitingListNote = (String)wlForm.get(wlNoteSelected);
//	        onListSince =  (String)wlForm.get(onListSinceSelected);

        	if(waitingListId == null && wlForm.get("selectedWL") != null){
        		waitingListId = (String)wlForm.get("selectedWL");
        	}
        	
        	if(waitingListId != null){
		        try{
		        	if( demographicNo != null  &&  !demographicNo.equals("")  && 
		        		waitingListNote != null  &&  !waitingListNote.equals("")  &&  
		        		onListSince != null  &&  !onListSince.equals("")){
		        		WLWaitingListUtil.updateWaitingListRecord(waitingListId, waitingListNote, demographicNo, onListSince);
		        	}
		        	else{
		        		WLWaitingListUtil.rePositionWaitingList(waitingListId);
		        	}
	
		        }catch(Exception ex){
		            System.out.println("WLUpdateDisplayWaitingListAction/execute(): Exception: "+ ex);
		        	return (mapping.findForward("failure"));
		        }
        	}
    	}else if(( update == null  ||  update.equals(""))  &&  remove == null ){ 
    		if(waitingListId != null  &&  waitingListId.length() > 0){
    			WLWaitingListUtil.rePositionWaitingList(waitingListId);
    		}
    	}//end of if( !update.equalsIgnoreCase("Y") ) -- could be remove also ???
        
        HttpSession session = request.getSession();
        
        
        if(session.getAttribute("groupno") != null){
        	groupNo = (String)session.getAttribute("groupno");
        }
        providerNo = (String)session.getAttribute("user");
        
        System.out.println("WLSetupDisplayWaitingListAction/execute(): providerNo = "+ providerNo);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): groupno = "+ groupNo);

        System.out.println("WLSetupDisplayWaitingListAction/execute(): waitingListId = "+ waitingListId);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): demographicNo = "+ demographicNo);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): waitingListNote = "+ waitingListNote);
        System.out.println("WLSetupDisplayWaitingListAction/execute(): onListSince = "+ onListSince);

        WLWaitingListBeanHandler hd = null;
        WLWaitingListNameBeanHandler wlNameHd = null;
        Collection allProviders = null;
        String nbPatients = "";
        String today = "";
        
        if(waitingListId != null  &&  waitingListId.length() > 0){
        	hd = new WLWaitingListBeanHandler(waitingListId);
        }else{
        	//even though waitingListId is null, still need to create hd for hd.getWaitingListArrayList()
        	// to display in DisplayWaitingList.jsp
        	hd = new WLWaitingListBeanHandler(waitingListId);
        }
        
        if(groupNo != null  &&  providerNo != null){
        	wlNameHd = new WLWaitingListNameBeanHandler(groupNo, providerNo);
        }
        ProviderNameBeanHandler phd = new ProviderNameBeanHandler();
        
        
        if(groupNo != null){
        	phd.setThisGroupProviderVector(groupNo);
        	allProviders = phd.getThisGroupProviderVector();
        	System.out.println("WLSetupDisplayWaitingListAction/execute(): allProviders.size() = "+ allProviders.size());
                if (allProviders.size()<=0){
                    ProviderData proData = new ProviderData();
                    proData.getProvider(groupNo);
                    if (!proData.getLast_name().equals("") && proData.getLast_name() != null && !proData.getFirst_name().equals("") && proData.getFirst_name() != null) {
                        ProviderNameBean proNameBean = new ProviderNameBean(proData.getLast_name() + ", " + proData.getFirst_name(), groupNo);
                        allProviders.add(proNameBean);
                    }
                }
                    
        	if(hd != null){
        		nbPatients = Integer.toString(hd.getWaitingListArrayList().size());
        	}else{
        		nbPatients = "0";
        	}
        	
        }
        
        today = UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd");
        
        request.setAttribute("WLId", waitingListId);
        session.setAttribute( "waitingList", hd );     
        if(hd != null){
        	session.setAttribute("waitingListName", hd.getWaitingListName());
        }else{
        	session.setAttribute("waitingListName", null);
        }
        if(wlNameHd != null){
        	session.setAttribute("waitingListNames", wlNameHd.getWaitingListNames());
        }else{
        	session.setAttribute("waitingListNames", null);
        }
        session.setAttribute("allProviders", allProviders);
        
        session.setAttribute("nbPatients", nbPatients);

        //session.setAttribute("allWaitingListName", allWaitingListName);
        session.setAttribute("today", today);
        
        return (mapping.findForward("continue"));
    }

}
