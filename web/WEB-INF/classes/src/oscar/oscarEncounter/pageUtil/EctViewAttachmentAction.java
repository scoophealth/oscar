package oscar.oscarEncounter.pageUtil;

import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;

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


public final class EctViewAttachmentAction extends Action {


    public ActionForward perform(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {


    EctViewAttachmentForm frm = (EctViewAttachmentForm) form;

    String mesId = frm.getMesId();
    String thedate = null;
    String theime = null;
    String themessage = null;
    String thesubject = null;
    String attachment = null;
    String remoteName = null;
    String sentBy     = null;


    System.out.println("mess id = "+mesId);

    try{
       DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
       ResultSet rs;


       rs = db.GetSQL("SELECT m.thesubject, m.theime, m.thedate, m.attachment, m.themessage, m.sentBy, ocl.locationDesc  "
                     +"FROM messagetbl m, oscarcommlocations ocl where m.sentByLocation = ocl.locationId and "
                     +" messageid = '"+mesId+"'");
       if(rs.next()){
          remoteName = rs.getString("locationDesc");
          themessage = rs.getString("themessage");
          theime     = rs.getString("theime");
          thedate    = rs.getString("thedate");
          attachment = rs.getString("attachment");
          thesubject = rs.getString("thesubject");
          sentBy     = rs.getString("sentBy");
       }
       rs.close();
       db.CloseConn();
    }catch(SQLException e){System.out.println("CrAsH"); e.printStackTrace();}

    request.setAttribute("remoteName",remoteName);
    request.setAttribute("themessage",themessage);
    request.setAttribute("theime",theime);
    request.setAttribute("thedate",thedate);
    request.setAttribute("attachment",attachment);
    request.setAttribute("thesubject",thesubject);
    request.setAttribute("sentBy",sentBy);
    return (mapping.findForward("success"));
    }
}
