package oscar.oscarReport.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class RptShowConsultAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
       System.out.println("In RptShowConsultAction Jackson");
       // EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
       // if(bean == null)
       //     return mapping.findForward("eject");
        RptShowConsultForm frm = (RptShowConsultForm)form;
        String requestId = frm.getRequestId();
        request.setAttribute("reqId", requestId);
        return mapping.findForward("success");
    }
}
