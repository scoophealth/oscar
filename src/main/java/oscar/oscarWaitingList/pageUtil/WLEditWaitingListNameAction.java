/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarWaitingList.pageUtil;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;

import oscar.oscarWaitingList.bean.WLWaitingListNameBeanHandler;
import oscar.oscarWaitingList.util.WLWaitingListNameUtil;
import oscar.util.UtilDateUtilities;

public final class WLEditWaitingListNameAction extends Action {

	private String message ="";
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        
    	
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): just entering.");
        HttpSession session = request.getSession();
    	LazyValidatorForm wlnForm = (LazyValidatorForm)form;
    	

        String edit = request.getParameter("edit");
        String actionChosen = request.getParameter("actionChosen");
        String providerNo = (String)session.getAttribute("user");
        String groupNo ="";
        String wlNewName = (String)wlnForm.get("wlNewName");
        String wlChangedName = (String)wlnForm.get("wlChangedName");
        String selectedWL = (String)wlnForm.get("selectedWL");
        String selectedWL2 = (String)wlnForm.get("selectedWL2");
        int successCode = 0;
        request.setAttribute("message", "");
        setMessage("");
        
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): edit = "+ edit);
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): actionChosen = "+ actionChosen);
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): selectedWL = "+ selectedWL);
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): selectedWL2 = "+ selectedWL2);

        if(edit != null  &&  !edit.equals("")){
        	
        	if( wlNewName == null  ||  wlNewName.length()<= 0 ){
        		wlNewName = wlChangedName;
        	}
        	
        	ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
            if(providerPreference.getMyGroupNo() != null){
            	groupNo = providerPreference.getMyGroupNo();
            }


	        try{
	        	if( actionChosen != null  &&  actionChosen.length() > 0  &&
	        		providerNo != null  &&  providerNo.length() > 0 ){
	        		
	        		if(actionChosen.equalsIgnoreCase("create")){
	        			if(wlNewName != null  &&  wlNewName.length() > 0){
	        				try{
	        					WLWaitingListNameUtil.createWaitingListName(wlNewName, groupNo, providerNo);
	        					successCode = 1;
	        				}catch(Exception ex1){
	        					if(ex1.getMessage().equals("wlNameExists")){
	        						setMessage("oscar.waitinglistname.wlNameExists");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.wlNameExists"));
	        					}else{
	        						setMessage("oscar.waitinglistname.error");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.error"));
	        					}
	        				}
	        			}
	        		}else if(actionChosen.equalsIgnoreCase("change")){
	        			if( selectedWL != null  &&  selectedWL.length() > 0  &&
	        				wlNewName != null  &&  wlNewName.length() > 0){
	        				try{
	        					WLWaitingListNameUtil.updateWaitingListName(selectedWL, wlNewName, groupNo, providerNo);
	        					successCode = 2;
	        				}catch(Exception ex2){
	        					if(ex2.getMessage().equals("wlNameExists")){
	        						setMessage("oscar.waitinglistname.wlNameExists");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.noSuchWL"));
	        					}else{
	        						setMessage("oscar.waitinglistname.error");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.error"));
	        					}
	        				}
	        			}
	        		}else if(actionChosen.equalsIgnoreCase("remove")){
	        			if(selectedWL2 != null  &&  selectedWL2.length() > 0){
	        				MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): selectedWL2 = "+ selectedWL2);
	        				try{
		        				WLWaitingListNameUtil.removeFromWaitingListName(selectedWL2, groupNo);
	        					successCode = 3;
	        				}catch(Exception ex3){
	        					if(ex3.getMessage().equals("wlNameUsed")){
	        						setMessage("oscar.waitinglistname.wlNameUsed");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.wlNameUsed"));
	        					}else{
	        						setMessage("oscar.waitinglistname.error");
	        						//msgs.add(ActionMessages.GLOBAL_MESSAGE, 
	        						//		 new ActionMessage("oscar.waitinglistname.error"));
	        					}
	        				}
	        			}
	        		}
	        		else{
	        			successCode = 0;
	        		}
	        	}

	        }catch(Exception ex){
	            MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): Exception: "+ ex);
	            setMessage("oscar.waitinglistname.error");
				//msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("oscar.waitinglistname.error"));
	        	return (mapping.findForward("failure"));
	        }
        
        }
        
        if(successCode == 1){
        	setMessage("oscar.waitinglistname.createSuccess");
			//msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("oscar.waitinglistname.createSuccess"));
        }else if(successCode == 2){
        	setMessage("oscar.waitinglistname.editSuccess");
			//msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("oscar.waitinglistname.editSuccess"));
        }else if(successCode == 3){
        	setMessage("oscar.waitinglistname.removeSuccess");
			//msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("oscar.waitinglistname.removeSuccess"));
        }
        else{
        	// no idea if this is good or bad, original author didn't document
        }

        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): groupNo = "+ groupNo);
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): providerNo = "+ providerNo);
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): wlNewName = "+ wlNewName);


        WLWaitingListNameBeanHandler wlNameHd = new WLWaitingListNameBeanHandler(groupNo, providerNo);
        
        String today = UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd");
        
        session.setAttribute("waitingListNames", wlNameHd.getWaitingListNameList());
        
        session.setAttribute("today", today);
        
        MiscUtils.getLogger().debug("WLEditWaitingListNameAction/execute(): getMessage() = "+ getMessage());
        request.setAttribute("message", getMessage());
        //saveMessages(request, msgs); //<-- since only one message is needed each time, this function is not needed
        
        return (mapping.findForward("continue"));
    }
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    

}
