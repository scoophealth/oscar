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
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.pageUtil;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgDemoMap;
import oscar.util.ParameterActionForward;

public class MsgViewMessageAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        // System.out.println("in view message action jackson");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
        String providerNo= null;
        if(bean!=null)
            providerNo = bean.getProviderNo();
        else
            System.out.println("MsgSessionBean is null");
        
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
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           
              //print out message
              String sql = new String("Select * from messagetbl where messageid = \'"+messageNo+"\' ");
              rs = db.GetSQL(sql);

              if (rs.next()) {
                 String attach, pdfAttach;
                 String message = (db.getString(rs,"themessage"));
                 String subject = (db.getString(rs,"thesubject"));
                 String sentby  = (db.getString(rs,"sentby"));
                 String sentto  = (db.getString(rs,"sentto"));
                 String thetime = (db.getString(rs,"theime"));
                 String thedate = (db.getString(rs,"thedate"));
                 String att     = db.getString(rs,"attachment");
                 String pdfAtt     = db.getString(rs,"pdfattachment");
                 // System.out.println("attach "+att);
                 if (att == null || att.equals("null") ){
                    attach ="0";
                 }else{
                    attach ="1";
                 }


                 if (pdfAtt == null || pdfAtt.equals("null") ){
                    pdfAttach ="0";
                 }else{
                    pdfAttach ="1";
                 }

                 // System.out.println("the message "+message+" "+subject);
                 

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
                                  
                 System.out.println("viewMessagePosition: " + messagePosition + "IsLastMsg: " + request.getAttribute("viewMessageIsLastMsg"));
              }
              else{
                 i=0; // somethin wrong no message there
              }

              if (i == 1){
                 db.RunSQL("update messagelisttbl set status = \'read\' where provider_no = \'"+providerNo+"\' and message = \'"+messageNo+"\' and status not like 'del'");
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
            e.printStackTrace(System.out); 
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
