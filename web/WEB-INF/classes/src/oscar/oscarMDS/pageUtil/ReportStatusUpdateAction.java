package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarMDS.data.MDSResultsData;
import java.util.Properties;

public class ReportStatusUpdateAction extends Action
{

    public ReportStatusUpdateAction()
    {
    }

    public ActionForward perform(ActionMapping mapping, 
                                 ActionForm form, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
        throws ServletException, IOException
    {        
        // System.out.println("inside ReportStatusUpdateAction");
        
        int labNo = Integer.parseInt(request.getParameter("segmentID"));
        int providerNo = Integer.parseInt(request.getParameter("providerNo"));
        char status = request.getParameter("status").charAt(0);
        String comment = request.getParameter("comment");
        Properties props = (Properties) request.getSession().getAttribute("oscarVariables");
        
        try {
            MDSResultsData.updateReportStatus(props, labNo, providerNo, status, comment);
            return mapping.findForward("success");
        } catch (Exception e) {
            System.out.println("exception in ReportStatusUpdateAction:"+e);
            return mapping.findForward("failure");
        }
    }
}