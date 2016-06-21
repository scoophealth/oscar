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
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.data.MsgProviderData;
import oscar.oscarMessenger.util.MsgDemoMap;

public class MsgCreateMessageAction extends Action {


	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "w", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
    	
            // Extract attributes we will need
            oscar.oscarMessenger.pageUtil.MsgSessionBean bean;
            bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
            String userNo   = bean.getProviderNo();
            String userName = bean.getUserName();
            String att      = bean.getAttachment();
            String pdfAtt      = bean.getPDFAttachment();
            bean.nullAttachment();
            String message      = ((MsgCreateMessageForm)form).getMessage();
            String[] providers  = ((MsgCreateMessageForm)form).getProvider();
            String subject      = ((MsgCreateMessageForm)form).getSubject();
	    bean.setMessage(null);
            bean.setSubject(null);
            
            MiscUtils.getLogger().debug("Providers: " + Arrays.toString(providers));
            MiscUtils.getLogger().debug("Subject: " + subject);
            MiscUtils.getLogger().debug("Message: " + message);
            
            String sentToWho    = null;
            String currLoco     = null;
            String messageId    = null;
            String demographic_no = ((MsgCreateMessageForm)form).getDemographic_no();

            java.util.ArrayList<MsgProviderData> providerListing, localProviderListing, remoteProviderListing;


            subject.trim();
            if (subject.length() == 0) {subject = "none";}

            oscar.oscarMessenger.data.MsgMessageData messageData = new oscar.oscarMessenger.data.MsgMessageData();
            providers               = messageData.getDups4(providers);
            providerListing         = messageData.getProviderStructure(providers);
            localProviderListing    = messageData.getLocalProvidersStructure();
            remoteProviderListing   = messageData.getRemoteProvidersStructure();
            currLoco                = messageData.getCurrentLocationId();



            if (messageData.isLocals()){
            sentToWho = messageData.createSentToString(localProviderListing);
            }else{
            sentToWho = "";
            }

            if (messageData.isRemotes()){
                sentToWho = sentToWho+" "+messageData.getRemoteNames(remoteProviderListing);
            }

            messageId = messageData.sendMessage2(message,subject,userName,sentToWho,userNo,providerListing,att, pdfAtt, OscarMsgType.GENERAL_TYPE);

            //link msg and demogrpahic if both messageId and demographic_no are not null
            if (demographic_no != null && (demographic_no.equals("") || demographic_no.equals("null")) ){
               demographic_no = null;
            }
            if(messageId!=null && demographic_no!=null){
                MsgDemoMap msgDemoMap = new MsgDemoMap();
                msgDemoMap.linkMsg2Demo(messageId, demographic_no);
            }


    request.setAttribute("SentMessageProvs",sentToWho.toString());

    return (mapping.findForward("success"));
    }

}
