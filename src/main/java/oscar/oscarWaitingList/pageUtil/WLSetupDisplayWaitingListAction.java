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


package oscar.oscarWaitingList.pageUtil;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SessionConstants;

import oscar.oscarProvider.bean.ProviderNameBean;
import oscar.oscarProvider.bean.ProviderNameBeanHandler;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarWaitingList.bean.WLWaitingListBeanHandler;
import oscar.oscarWaitingList.bean.WLWaitingListNameBeanHandler;
import oscar.oscarWaitingList.util.WLWaitingListUtil;
import oscar.util.UtilDateUtilities;

public final class WLSetupDisplayWaitingListAction extends Action {

	private Logger log = Logger.getLogger(WLSetupDisplayWaitingListAction.class);
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        
    	
        log.debug("\n\nWLSetupDisplayWaitingListAction/execute(): just entering.");
        
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        
        String update = request.getParameter("update");
        String remove = request.getParameter("remove");//actually not used for now, may in future?
        
        String waitingListId = "";
        String demographicNo = "";
        String waitingListNote = "";
        String onListSince = "";
        String groupNo = "";
        String providerNo = "";
        
        log.debug("\n\nWLSetupDisplayWaitingListAction/execute(): update = " + update);
        log.debug("\n\nWLSetupDisplayWaitingListAction/execute(): remove = " + remove);

    	LazyValidatorForm wlForm = (LazyValidatorForm)form;
        log.debug("WLSetupDisplayWaitingListAction/execute(): after  (LazyValidatorForm)form ");

        
        String demographicNumSelected =  request.getParameter("demographicNumSelected");
        String wlNoteSelected = request.getParameter("wlNoteSelected");
        String onListSinceSelected =  request.getParameter("onListSinceSelected");

        log.debug("WLSetupDisplayWaitingListAction/execute(): demographicNumSelected = "+ demographicNumSelected);
        log.debug("WLSetupDisplayWaitingListAction/execute(): wlNoteSelected = "+ wlNoteSelected);
        log.debug("WLSetupDisplayWaitingListAction/execute(): onListSinceSelected = "+ onListSinceSelected);
        
        
        if(request.getParameter("waitingListId") != null){
        	waitingListId = request.getParameter("waitingListId");
        }
        
        log.debug("WLSetupDisplayWaitingListAction/execute(): waitingListId = "+ waitingListId);
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
		            log.error("WLUpdateDisplayWaitingListAction/execute(): Exception: "+ ex);
		        	return (mapping.findForward("failure"));
		        }
        	}
    	}else if(( update == null  ||  update.equals(""))  &&  remove == null ){ 
    		if(waitingListId != null  &&  waitingListId.length() > 0){
    			WLWaitingListUtil.rePositionWaitingList(waitingListId);
    		}
    	}//end of if( !update.equalsIgnoreCase("Y") ) -- could be remove also ???
        
        HttpSession session = request.getSession();
        
        ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
        
        if(providerPreference.getMyGroupNo() != null){
        	groupNo = providerPreference.getMyGroupNo();
        }
        providerNo = (String)session.getAttribute("user");
        
        log.debug("WLSetupDisplayWaitingListAction/execute(): providerNo = "+ providerNo);
        log.debug("WLSetupDisplayWaitingListAction/execute(): groupno = "+ groupNo);

        log.debug("WLSetupDisplayWaitingListAction/execute(): waitingListId = "+ waitingListId);
        log.debug("WLSetupDisplayWaitingListAction/execute(): demographicNo = "+ demographicNo);
        log.debug("WLSetupDisplayWaitingListAction/execute(): waitingListNote = "+ waitingListNote);
        log.debug("WLSetupDisplayWaitingListAction/execute(): onListSince = "+ onListSince);

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
        	if(allProviders.size()==0 && groupNo.equals(".default")) {
        		Provider p = loggedInInfo.getLoggedInProvider();
        		ProviderNameBean pNameBean = new ProviderNameBean(p.getFormattedName(),p.getProviderNo());
        		allProviders.add(pNameBean);
        	}
        	log.debug("WLSetupDisplayWaitingListAction/execute(): allProviders.size() = "+ allProviders.size());
                if (allProviders.size()<=0){
                    ProviderData proData = new ProviderData();
                    proData.getProvider(groupNo);
                    if (proData.getLast_name() != null && !proData.getLast_name().equals("") && proData.getFirst_name() != null && !proData.getFirst_name().equals("")) {
                        ProviderNameBean proNameBean = new ProviderNameBean(proData.getLast_name() + ", " + proData.getFirst_name(), groupNo);
                        allProviders.add(proNameBean);
                    }
                }
                    
        	if(hd != null){
        		nbPatients = Integer.toString(hd.getWaitingList().size());
        	}else{
        		nbPatients = "0";
        	}
        	
        }
        
        today = UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd");
        
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
