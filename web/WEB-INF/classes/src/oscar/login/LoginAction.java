package oscar.login;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class LoginAction extends Action {

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String ip = request.getRemoteAddr();
    String where = "failure"; 
    //String userName, password, pin, propName;
    LoginForm frm = (LoginForm) form;
    String   userName = ((LoginForm) form).getUsername();        
    String   password = ((LoginForm) form).getPassword();        
    String   pin = ((LoginForm) form).getPin();
    //String   propName = ((LoginForm) form).getPropname();
    String propName = request.getContextPath().substring(1)+".properties";
    if(userName.equals("")) {    
      return mapping.findForward(where);
    }

    String pathSeparator = System.getProperty("file.separator");

    //Main configuration file. This file must be saved on WEB-INF/classes at the webapp diretory.
    //The file name is defined on the page an its read as a parameter (propName).
    //String mainConfigFileName = servlet.getServletContext().getRealPath("")+pathSeparator+"WEB-INF"+pathSeparator+
    //	    "classes"+pathSeparator+propName;    
    LoginCheckLogin cl = new LoginCheckLogin(propName);    
    if(!cl.propFileFound) {
        String newURL = mapping.findForward("error").getPath();
        newURL = newURL + "?errormsg=Unable to open the properties file "+cl.propFileName+".";
        return (new ActionForward(newURL));
    }
    
    if(cl.isBlock(ip)) return mapping.findForward(where);  //go to block page

    String[] strAuth;
    try {
        strAuth = cl.auth(userName, password, pin, ip) ;
    } catch (Exception e) {
        String newURL = mapping.findForward("error").getPath();
        if (e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
            newURL = newURL + "?errormsg=Database driver "+e.getMessage().substring(e.getMessage().indexOf(':')+2)+" not found.";
        } else {
            newURL = newURL + "?errormsg=Database connection error: "+e.getMessage()+".";
        }
        return (new ActionForward(newURL));
    }

    if(strAuth!=null) { //login successfully
      //invalidate the existing sesson
      HttpSession session = request.getSession(false);
      if(session != null ) {
        session.invalidate();
        session = request.getSession();// Create a new session for this user
      }

      java.util.Date date = new java.util.Date();
      System.out.println("Assigned new session for: " + strAuth[0]+ " : "+ strAuth[3] + "   " + date.toString()); 
      session.setMaxInactiveInterval(6800);
    
      //initial db setting
      Properties pvar = cl.getOscarVariable() ;
      session.setAttribute("oscarVariables", pvar);
      oscar.oscarDB.DBHandler.init(pvar.getProperty("db_name"),pvar.getProperty("db_driver"),pvar.getProperty("db_uri"),pvar.getProperty("db_username"),pvar.getProperty("db_password")  ) ;
      System.setProperty("drugref_url",pvar.getProperty("drugref_url"));
      //get View Type
      String viewType = LoginViewTypeHlp.getInstance().getProperty(strAuth[3].toLowerCase());

      session.setAttribute("user", strAuth[0]);
      session.setAttribute("userfirstname", strAuth[1]);
      session.setAttribute("userlastname", strAuth[2]);
      session.setAttribute("userprofession", viewType);

      if(viewType.equalsIgnoreCase("receptionist")|| viewType.equalsIgnoreCase("doctor")) { 
        //get preferences from preference table
        String [] strPreferAuth = cl.getPreferences();
        session.setAttribute("starthour", strPreferAuth[0]);
        session.setAttribute("endhour", strPreferAuth[1]);
        session.setAttribute("everymin", strPreferAuth[2]);
        session.setAttribute("groupno", strPreferAuth[3]);
      }

      if(viewType.equalsIgnoreCase("receptionist")) { // go to receptionist view
    	  where = "receptionist" ;//receptionistcontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";
      } else if (viewType.equalsIgnoreCase("doctor")) { // go to provider view
        where = "provider" ; //providercontrol.jsp?year="+nowYear+"&month="+(nowMonth)+"&day="+(nowDay)+"&view=0&displaymode=day&dboperation=searchappointmentday";   
      } else if (viewType.equalsIgnoreCase("admin")) { // go to admin view
        where = "admin"; 
      }

    } else { // go to normal directory
      //request.setAttribute("login", "failed");
      cl.updateLoginList(ip);
      return mapping.findForward(where);
    }
    return mapping.findForward(where);
  }
}
