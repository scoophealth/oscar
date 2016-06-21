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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgDemoMap;
import oscar.util.ParameterActionForward;

public class MsgViewMessageAction extends Action {
	
	private MessageListDao messageListDao = SpringUtils.getBean(MessageListDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "r", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
    	
        // Extract attributes we will need
        

        oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
        String providerNo= LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
        if(bean!=null)
            providerNo = bean.getProviderNo();
        else
            MiscUtils.getLogger().debug("MsgSessionBean is null");
        
        String messageNo = request.getParameter("messageID");  
        String messagePosition = request.getParameter("messagePosition");
        String linkMsgDemo = request.getParameter("linkMsgDemo");           
        String demographic_no = request.getParameter("demographic_no");   
        String orderBy = request.getParameter("orderBy");
        String msgCount = request.getParameter("msgCount");
        String from = request.getParameter("from")==null?"oscarMessenger":request.getParameter("from");
        String boxType = request.getParameter("boxType")==null?"":request.getParameter("boxType");
        
        if(msgCount==null){
            MsgDisplayMessagesBean DisplayMessagesBeanId = new MsgDisplayMessagesBean();
            Vector theMessages2 = DisplayMessagesBeanId.estDemographicInbox(orderBy,demographic_no);
            msgCount = Integer.toString(theMessages2.size());
        }
        
        int  i = 1;

        try{
           
           java.sql.ResultSet rs;
           
              //print out message
              String sql = new String("Select * from messagetbl where messageid = \'"+messageNo+"\' ");
              rs = DBHandler.GetSQL(sql);

              if (rs.next()) {
                 String attach, pdfAttach;
                 String message = (oscar.Misc.getString(rs, "themessage"));
                 String subject = (oscar.Misc.getString(rs, "thesubject"));
                 String sentby  = (oscar.Misc.getString(rs, "sentby"));
                 String sentto  = (oscar.Misc.getString(rs, "sentto"));
                 String thetime = (oscar.Misc.getString(rs, "theime"));
                 String thedate = (oscar.Misc.getString(rs, "thedate"));
                 String att     = rs.getString("attachment");
                 String pdfAtt  = rs.getString("pdfattachment");
                 String msgType = (oscar.Misc.getString(rs, "type"));
                 String msgType_link = (oscar.Misc.getString(rs, "type_link"));

                 if (att == null || att.equalsIgnoreCase("null") ){
                    attach ="0";
                 }else{
                    attach ="1";
                 }


                 if (pdfAtt == null || pdfAtt.equalsIgnoreCase("null") ){
                    pdfAttach ="0";
                 }else{
                    pdfAttach ="1";
                 }

                 request.setAttribute("viewMessageMessage",message);
                 request.setAttribute("viewMessageSubject",subject);
                 request.setAttribute("viewMessageSentby",sentby);
                 request.setAttribute("viewMessageSentto",sentto);
                 request.setAttribute("viewMessageTime",thetime);
                 request.setAttribute("viewMessageDate",thedate);
                 request.setAttribute("viewMessageAttach",attach);
                 request.setAttribute("viewMessagePDFAttach",pdfAttach);
                 request.setAttribute("viewMessageId",messageNo);                 
                 // not from query
                 request.setAttribute("viewMessageNo",messageNo);
                 request.setAttribute("viewMessagePosition",messagePosition);
                 request.setAttribute("providerNo",providerNo); 
                 if(orderBy!=null){
                     request.setAttribute("orderBy", orderBy);
                 }
                                  
                 if( msgType != null && !"".equalsIgnoreCase(msgType) ) {
                     request.setAttribute("msgType", msgType);
                     
                     if( Integer.valueOf(msgType).equals(OscarMsgType.OSCAR_REVIEW_TYPE) ) {
                         if( msgType_link != null ) {
                            HashMap<String,List<String>>hashMap = new HashMap<String,List<String>>();
                            String[] keyValues = msgType_link.split(",");

                            for( String s : keyValues ) {
                                String[] keyValue = s.split(":");
                                if( keyValue.length == 4 ) {
                                    if( hashMap.containsKey(keyValue[0]) ) {
                                        hashMap.get(keyValue[0]).add(keyValue[1]+":"+keyValue[2]+":"+keyValue[3]);
                                    }
                                    else {
                                        List<String> list = new ArrayList<String>();
                                        list.add(keyValue[1]+":"+keyValue[2]+":"+keyValue[3]);
                                        hashMap.put(keyValue[0], list);
                                    }
                                }
                            }
                            request.setAttribute("msgTypeLink", hashMap);
                         }
                     }
                 }
                                  
                 MiscUtils.getLogger().debug("viewMessagePosition: " + messagePosition + "IsLastMsg: " + request.getAttribute("viewMessageIsLastMsg"));
              }
              else{
                 i=0; // something wrong no message there
              }

              if (i == 1){
            	  for(MessageList ml:messageListDao.findByProviderNoAndMessageNo(providerNo, Long.valueOf(messageNo))) {
            		  if(!ml.getStatus().equals("del")) {
            			  ml.setStatus("read");
            			  messageListDao.merge(ml);
            		  }
            	  }
              }
              
              if (linkMsgDemo !=null && demographic_no!=null){
                  if(linkMsgDemo.equalsIgnoreCase("true")){
                      MsgDemoMap msgDemoMap = new MsgDemoMap();
                      msgDemoMap.linkMsg2Demo(messageNo, demographic_no);
                      
                  }
              }
                  

          //}
         rs.close();

        }
        catch (java.sql.SQLException e){ 
           MiscUtils.getLogger().error("Error", e); 
        }
        
        request.setAttribute("today", oscar.util.UtilDateUtilities.getToday("dd-MMM-yyyy"));
        
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward("success"));
        actionforward.addParameter("boxType", boxType);
        if(from.equalsIgnoreCase("encounter")){
            actionforward = new ParameterActionForward(mapping.findForward("viewFromEncounter"));
            actionforward.addParameter("demographic_no", demographic_no);
            actionforward.addParameter("msgCount", msgCount);
        }
        else{          
            actionforward.addParameter("linkMsgDemo", linkMsgDemo);
        }
                
        return actionforward;
    }

}
