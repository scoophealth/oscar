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
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.pageUtil;
import oscar.oscarDB.DBHandler;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

public  class MsgHandleMessagesAction extends Action {

    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        MsgSessionBean bean = (MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
        String providerNo = bean.getProviderNo();
        String messageNo = ((MsgHandleMessagesForm)form).getMessageNo();
        String sentbyNo ;
        String sentByLocation;
        String reply = ((MsgHandleMessagesForm)form).getReply();
        String replyAll = ((MsgHandleMessagesForm)form).getReplyAll();
        String delete = ((MsgHandleMessagesForm)form).getDelete();
        String forward = ((MsgHandleMessagesForm)form).getForward();

        oscar.oscarMessenger.data.MsgReplyMessageData replyMessageData;
        replyMessageData = new oscar.oscarMessenger.data.MsgReplyMessageData();
        replyMessageData.estLists();

        HttpSession session = request.getSession(true);
        java.util.Enumeration ty = session.getAttributeNames();

        if (delete.compareToIgnoreCase("Delete") == 0){
          try{    //sents this message status to del
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             java.sql.ResultSet rs;
             String sql = new String("update messagelisttbl set status = \"del\" where provider_no = \""+providerNo+"\" and message = \""+messageNo+"\"");
             rs = db.GetSQL(sql);
            rs.close();
            db.CloseConn();

          }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }

        }
        else if(reply.compareToIgnoreCase("Reply") == 0 || (replyAll.compareToIgnoreCase("reply All") == 0)){

          String[] replyval = new String[] {};
          java.util.Vector vector = new java.util.Vector();
          StringBuffer subject = new StringBuffer("Re:");
          String themessage = new String();
          StringBuffer theSendMessage = new StringBuffer();

          try{   //gets the sender
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             java.sql.ResultSet rs;
             String sql = new String("Select sentbyNo,thesubject,themessage,sentByLocation from messagetbl where messageid = \""+messageNo+"\"");
             rs = db.GetSQL(sql);

             if ( rs.next()){
                vector.add(rs.getString("sentbyNo"));
                subject.append(rs.getString("thesubject"));
                themessage= rs.getString("themessage");
                sentByLocation = rs.getString("sentByLocation");
                themessage = themessage.replace('\n','>');        //puts > at the beginning
                theSendMessage = new StringBuffer(themessage);    //of each line
                theSendMessage.insert(0,"\n\n\n>");
                replyMessageData.add( (String) vector.elementAt(0) ,sentByLocation);
              }
              replyval = new String[vector.size()];
              for (int k =0; k < vector.size(); k++){
                 replyval[k] = (String) vector.elementAt(k);
              }

              if(replyAll.compareToIgnoreCase("reply All") == 0){  // add every one that got the message
                 rs = db.GetSQL("select provider_no, remoteLocation from messagelisttbl where message = \""+messageNo+"\"");
                 while (rs.next()){
                     System.out.println("pro no "+rs.getString("provider_no")+" remo Loco "+rs.getString("remoteLocation"));
                     vector.add(rs.getString("provider_no"));
                     replyMessageData.add(rs.getString("provider_no"),rs.getString("remoteLocation"));
                 }
                 replyval = new String[vector.size()];  //no need for the old replyval
                 for (int k =0; k < vector.size(); k++){
                    replyval[k] = (String) vector.elementAt(k);
                 }
              }
             rs.close();
             db.CloseConn();

           }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }


        request.setAttribute("ReText",theSendMessage.toString());
        request.setAttribute("ReMessage",replyval);  // used to set the providers that will get the reply message
        request.setAttribute("ProvidersClassObject",replyMessageData);
        request.setAttribute("ReSubject",subject.toString());
        return (mapping.findForward("reply"));
        }
        else if (forward.equals("Forward")){
           String[] replyval = new String[] {};
           java.util.Vector vector = new java.util.Vector();
           StringBuffer subject = new StringBuffer("Fwd:");
           String themessage = new String();
           StringBuffer theSendMessage = new StringBuffer();

           try{   //gets the sender
              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              java.sql.ResultSet rs;
              String sql = new String("Select sentbyNo,thesubject,themessage,sentByLocation from messagetbl where messageid = \""+messageNo+"\"");
              rs = db.GetSQL(sql);

              if ( rs.next()){
//                 vector.add(rs.getString("sentbyNo"));
                 subject.append(rs.getString("thesubject"));
                 themessage= rs.getString("themessage");
//                 sentByLocation = rs.getString("sentByLocation");
                 themessage = themessage.replace('\n','>');        //puts > at the beginning
                 theSendMessage = new StringBuffer(themessage);    //of each line
                 theSendMessage.insert(0,"\n\n\n>");
              }


             rs.close();
             db.CloseConn();

           }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }


           request.setAttribute("ReText",theSendMessage.toString());                //this one is a goody
//           request.setAttribute("ReMessage",replyval);  // used to set the providers that will get the reply message
//           request.setAttribute("ProvidersClassObject",replyMessageData);       //NOT NEEDED
           request.setAttribute("ReSubject",subject.toString());
           return (mapping.findForward("reply"));
        }

  return (mapping.findForward("success"));
  }
}
