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
package oscar.login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

public final class LoginAction extends Action {

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String ip = request.getRemoteAddr();
    String where = "failure"; 
    //String userName, password, pin, propName;
    String   userName = ((LoginForm) form).getUsername();        
    String   password = ((LoginForm) form).getPassword();        
    String   pin = ((LoginForm) form).getPin();
    String   propName = ((LoginForm) form).getPropname();
    if(userName.equals("")) {
      return mapping.findForward(where);
    }

    LoginCheckLogin cl = new LoginCheckLogin();
    String[] strAuth = cl.auth(userName, password, pin, ip, propName) ;

    if(strAuth!=null) { //login successfully
      //invalidate the existing sesson
      HttpSession session = request.getSession(false);
      if(session != null ) {
        session.invalidate();
        session = request.getSession();// Create a new session for this user
      }

      session.setAttribute("user", strAuth[0]);
      session.setAttribute("userfirstname", strAuth[1]);
      session.setAttribute("userlastname", strAuth[2]);
      session.setAttribute("userprofession", strAuth[3]);

      System.out.println("Assigned new session for: " + strAuth[0]+ " : "+ strAuth[3] );
      session.setMaxInactiveInterval(6800);
    
      if(strAuth[3].equalsIgnoreCase("receptionist")|| strAuth[3].equalsIgnoreCase("doctor")) { 
        //get preferences from preference table
        String [] strPreferAuth = cl.getPreferences();
        session.setAttribute("starthour", strPreferAuth[0]);
        session.setAttribute("endhour", strPreferAuth[1]);
        session.setAttribute("everymin", strPreferAuth[2]);
        session.setAttribute("groupno", strPreferAuth[3]);
      }
    
      //initial db setting
      Properties pvar = cl.getOscarVariable() ;
      session.setAttribute("oscarVariables", pvar);
      oscar.oscarDB.DBHandler.init(pvar.getProperty("db_name"),pvar.getProperty("db_driver"),pvar.getProperty("db_uri"),pvar.getProperty("db_username"),pvar.getProperty("db_password")  ) ;
      bean.DBConnect.init(pvar.getProperty("db_name"),pvar.getProperty("db_driver"),pvar.getProperty("db_uri"),pvar.getProperty("db_username"),pvar.getProperty("db_password")  ) ;

      if(strAuth[3].equalsIgnoreCase("receptionist")) { // go to receptionist view
    	  where = "receptionist" ;//receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";
      } else if (strAuth[3].equalsIgnoreCase("doctor")) { // go to provider view
        where = "provider" ; //providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";   
      } else if (strAuth[3].equalsIgnoreCase("admin")) { // go to admin view
        where = "admin"; 
      }

    } else { // go to normal directory
      //request.setAttribute("login", "failed");
      return mapping.findForward(where);
    }
    return mapping.findForward(where);
  }
}
