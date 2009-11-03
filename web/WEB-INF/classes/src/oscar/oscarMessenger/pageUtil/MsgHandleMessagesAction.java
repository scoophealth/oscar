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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import oscar.oscarDB.DBHandler;

public  class MsgHandleMessagesAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        MsgSessionBean bean = (MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
        String providerNo = bean.getProviderNo();
        MsgHandleMessagesForm frm = (MsgHandleMessagesForm) form;
        String messageNo = frm.getMessageNo();
        String demographicNo = frm.getDemographic_no();
        String sentbyNo ;
        String sentByLocation;
        String reply = frm.getReply();
        String replyAll = frm.getReplyAll();
        String delete = frm.getDelete();
        String forward = frm.getForward();

        oscar.oscarMessenger.data.MsgReplyMessageData replyMessageData;
        replyMessageData = new oscar.oscarMessenger.data.MsgReplyMessageData();
        replyMessageData.estLists();

        HttpSession session = request.getSession(true);
        java.util.Enumeration ty = session.getAttributeNames();

        /*
         *edit 2006-0801-01 by wreby
         *This will search the database to determine if any demographic was
         *associated with the parent message.  If so, it will now set the
         *demographic_no attribute so that the demographic is associated with
         *the child message too.
         */
        oscar.oscarMessenger.util.MsgDemoMap msgDemoMap = new oscar.oscarMessenger.util.MsgDemoMap();
        java.util.Hashtable demoMap = msgDemoMap.getDemoMap( messageNo );
        java.util.Enumeration demoKeys = demoMap.keys();
        if (demoKeys.hasMoreElements()) {
            demographicNo = demoKeys.nextElement().toString();
        }
        // end edit 2006-0801-01 by wreby
        
        //Set Demographic_no attribute if there's any
        if(demographicNo != null){
            request.setAttribute("demographic_no", demographicNo);
        }
        
	/*
	 * This is a little fix:
	 * 
	 * Look the parameter != null and set it correctly
	 */

	java.util.Enumeration enumeration = request.getParameterNames();
	while (enumeration.hasMoreElements()){
	    String param = ((String) enumeration.nextElement());
	    if (param.equals("delete")) {
		delete = "Delete";
	    } else if (param.equals("reply")) {
		reply = "Reply";
	    } else if (param.equals("replyAll")) {
		replyAll = "reply All";
	    } else if (param.equals("forward")) {
		forward = "Forward";
	    }
	}

        if (delete.compareToIgnoreCase("Delete") == 0){
          try{    //sents this message status to del
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             java.sql.ResultSet rs;
             String sql = "update messagelisttbl set status = 'del' where provider_no = '"+providerNo+"' and message = '"+messageNo+"'";
             db.RunSQL(sql);

          }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }

        }
        else if(reply.equalsIgnoreCase("Reply") || (replyAll.equalsIgnoreCase("reply All") )){

          String[] replyval = new String[] {};
          java.util.Vector vector = new java.util.Vector();
          StringBuffer subject = new StringBuffer("Re:");
          String themessage = new String();
          StringBuffer theSendMessage = new StringBuffer();

          try{   //gets the sender
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             java.sql.ResultSet rs;
             String sql = new String("Select sentbyNo,thesubject,themessage,sentByLocation from messagetbl where messageid = '"+messageNo+"'");
             rs = db.GetSQL(sql);

             if ( rs.next()){
                vector.add(db.getString(rs,"sentbyNo"));
                subject.append(db.getString(rs,"thesubject"));
                themessage= db.getString(rs,"themessage");
                sentByLocation = db.getString(rs,"sentByLocation");
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
                 rs = db.GetSQL("select provider_no, remoteLocation from messagelisttbl where message = '"+messageNo+"'");
                 while (rs.next()){
                     System.out.println("LOOK4ME pro no "+db.getString(rs,"provider_no")+" remo Loco "+db.getString(rs,"remoteLocation"));
                     vector.add(db.getString(rs,"provider_no"));
                     replyMessageData.add(db.getString(rs,"provider_no"),db.getString(rs,"remoteLocation"));
                 }
                 replyval = new String[vector.size()];  //no need for the old replyval
                 for (int k =0; k < vector.size(); k++){
                    replyval[k] = (String) vector.elementAt(k);
                 }
              }
             rs.close();

           }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }


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
              String sql = new String("Select sentbyNo,thesubject,themessage,sentByLocation from messagetbl where messageid = '"+messageNo+"'");
              rs = db.GetSQL(sql);

              if ( rs.next()){
//                 vector.add(db.getString(rs,"sentbyNo"));
                 subject.append(db.getString(rs,"thesubject"));
                 themessage= db.getString(rs,"themessage");
//                 sentByLocation = db.getString(rs,"sentByLocation");
                 themessage = themessage.replace('\n','>');        //puts > at the beginning
                 theSendMessage = new StringBuffer(themessage);    //of each line
                 theSendMessage.insert(0,"\n\n\n>");
              }


             rs.close();

           }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }


           request.setAttribute("ReText",theSendMessage.toString());                //this one is a goody
//           request.setAttribute("ReMessage",replyval);  // used to set the providers that will get the reply message
//           request.setAttribute("ProvidersClassObject",replyMessageData);       //NOT NEEDED
           request.setAttribute("ReSubject",subject.toString());
           return (mapping.findForward("reply"));
        }

  return (mapping.findForward("success"));
  }
}
