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

public class RptFluBillingAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

       RptFluBillingForm frm = (RptFluBillingForm)form;
        String requestId = frm.getRequestId();
        request.setAttribute("reqId", requestId);
        return mapping.findForward("success");
    }
}

