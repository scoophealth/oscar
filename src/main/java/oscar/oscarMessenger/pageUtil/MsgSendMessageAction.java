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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

public class MsgSendMessageAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

            // Extract attributes we will need
            

            // Setup variables
            ActionMessages errors = new ActionMessages();


            //String surname = ((SearchPatientForm)form).getSurname();
            String message = ((MsgCreateMessageForm)form).getMessage();
            String[] providers = ((MsgCreateMessageForm)form).getProvider();
            String subject = ((MsgCreateMessageForm)form).getSubject();
            //message.trim();
            subject.trim();

            if (message.length() == 0){

              //errors.add(ActionErrors.GLOBAL_ERROR,
                //       new ActionError("error.message.missing"));
                //errors.add(
                //       new ActionError("error.message.missing"));
            }

            if (providers.length == 0){

              //errors.add(ActionErrors.GLOBAL_ERROR,
                //       new ActionError("error.provider.missing"));

                      //               errors.add(ActionErrors.GLOBAL_ERROR,
                      // new ActionError("index.heading"));
            }

            if (true)/*(!errors.empty())*/ {

	      saveErrors(request, errors);
            ActionForward actionForward = new ActionForward(mapping.getInput());



            actionForward.setName(mapping.getInput());


	    //return ( actionForward(mapping.getInput()));
            //return (actionForward);

	    }

            if (subject.length() == 0)
            subject = "none";
            //By this far it should be safe that its a valid message

            //create a string with all the providers it will be sent too

            String sql = "select first_name, last_name from provider where ";
            StringBuilder temp = new StringBuilder(sql);
            StringBuilder sentToWho = new StringBuilder("Sent to : ");



            for (int i =0 ; i < providers.length ; i++)
            {
              if (i == (providers.length -1)){
                 temp.append(" provider_no = "+providers[i]);
              }
              else
              {
                temp.append(" provider_no = "+providers[i]+" or ");
              }
            }

            sql = temp.toString();

            try
            {
              
              java.sql.ResultSet rs;

              rs = DBHandler.GetSQL(sql);
              while (rs.next()) {

              sentToWho.append(" "+oscar.Misc.getString(rs, "first_name") +" " +oscar.Misc.getString(rs, "last_name")+". ");
              //providerFirstName.add(oscar.Misc.getString(rs,"first_name"));
              //providerLastName.add(oscar.Misc.getString(rs,"last_name"));
              }
        rs.close();

      }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

       MsgSentMessageForm  trial = new MsgSentMessageForm();
        trial.setSample(sentToWho.toString());

      //insert message into the messagetbl, get the message id back and insert it into the messagelisttbl
      //insert all the provider ids that will get the message along with the message id plus a status of new

      try
            {
              
              java.sql.ResultSet rs;
              //String sql = "insert into messagetbl (thedate,thetime,themessage,thesubject,sentby,sentto) values ('today','now','"+message+"','"+subject+"','jay','"+sentToWho+"' ";
              DBHandler.RunSQL("insert into messagetbl (thedate,theime,themessage,thesubject,sentby,sentto) values ('today','now','"+message+"','"+subject+"','jay','"+sentToWho+"') ");

	      /* Choose the right command to recover the messageid inserted above */
	      OscarProperties prop = OscarProperties.getInstance();
	      String db_type = prop.getProperty("db_type").trim();
	      if (db_type.equalsIgnoreCase("mysql")) {
		rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID() ");
	      } else if (db_type.equalsIgnoreCase("postgresql")) {
		rs = DBHandler.GetSQL("SELECT CURRVAL('messagetbl_int_seq')");
	      } else
	      throw new java.sql.SQLException("ERROR: Database " + db_type + " unrecognized");

              String messageid = oscar.Misc.getString(rs, 1);

              for (int i =0 ; i < providers.length ; i++)
              {

                DBHandler.RunSQL("insert into messagelisttbl (message,provider_no,status) values ('"+messageid+"','"+providers[i]+"','new')");
              }
        rs.close();

      }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

            // for (int i =0 ; i < providers.length ; i++)
            // {

            // }

            //servlet.getServletContext().setAttribute("SendMessageFormId", sentToWho);
            //((SearchPatientForm)form).getSurname();
            //((SentMessageForm)form).setSample(sentToWho.toString());
            //request.setAttribute(sentToWho.toString(),demoAgain.SentMessageForm);
            //servlet.getServletContext().setAttribute("SentMessageProvs",sentToWho.toString());
            request.setAttribute("SentMessageProvs",sentToWho.toString());

            return (mapping.findForward("success"));
    }
}
