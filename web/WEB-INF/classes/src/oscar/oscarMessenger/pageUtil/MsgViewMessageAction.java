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

public class MsgViewMessageAction extends Action {

    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
System.out.println("in view message action jackson");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();

        oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)request.getSession().getAttribute("msgSessionBean");
        String providerNo= bean.getProviderNo();

        System.out.println(request.getAttributeNames());
        System.out.println(request.getQueryString());
        String messageNo = request.getParameter("messageID");
        int  i = 1;

        try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           //rs = db.GetSQL("select count * from messagelisttbl where message = \""+messageNo+"\" and provider_no = \""+providerNo+"\"");
           //int count = rs.getInt(1);
           //if (count == 0){
           //     System.out.println("test from view action \n\n\n" );
           //   return (mapping.findForward("noRights"));
           //}else{
           System.out.println("theres a message  ");
              String sql = new String("Select * from messagetbl where messageid = \""+messageNo+"\" ");
              rs = db.GetSQL(sql);

              if (rs.next()) {
                 String attach;
                 String message = (rs.getString("themessage"));
                 String subject = (rs.getString("thesubject"));
                 String sentby  = (rs.getString("sentby"));
                 String sentto  = (rs.getString("sentto"));
                 String thetime = (rs.getString("theime"));
                 String thedate = (rs.getString("thedate"));
                 String att     = rs.getString("attachment");
System.out.println("attach "+att);
                 if (att == null || att.equals("null") ){
                    attach ="0";
                 }else{
                    attach ="1";
                 }
                 System.out.println("the message "+message+" "+subject);

                 request.setAttribute("viewMessageMessage",message);
                 request.setAttribute("viewMessageSubject",subject);
                 request.setAttribute("viewMessageSentby",sentby);
                 request.setAttribute("viewMessageSentto",sentto);
                 request.setAttribute("viewMessageTime",thetime);
                 request.setAttribute("viewMessageDate",thedate);
                 request.setAttribute("viewMessageAttach",attach);
                 request.setAttribute("viewMessageId",messageNo);
                 // not from query
                 request.setAttribute("viewMessageNo",messageNo);
              }
              else{
                 i=0; // somethin wrong no message there
              }

              if (i == 1){
                 rs = db.GetSQL("update messagelisttbl set status = \"read\" where provider_no = \""+providerNo+"\" and message = \""+messageNo+"\" and status not like 'del'");
              }


          //}
         rs.close();
         db.CloseConn();

        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }

    return (mapping.findForward("success"));
    }

}
