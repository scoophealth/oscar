package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.Properties;
import oscar.oscarReport.data.*;
public class ObecAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
			Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");

          	 ObecForm frm = (ObecForm)form;
             ObecData obecData1 = new ObecData();
		     String startDate = frm.getXml_vdate()==null?"":frm.getXml_vdate();
		     String endDate = frm.getXml_appointment_date()==null?"":frm.getXml_appointment_date();
           	 String obectxt = obecData1.generateOBEC(startDate, endDate, proppies)==null?"":obecData1.generateOBEC(startDate, endDate, proppies);
             request.setAttribute("obectxt", obectxt);

    		 return mapping.findForward("success");
    }
}

