package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarMDS.data.MDSResultsData;

public class ReportReassignAction extends Action
{

    public ReportReassignAction()
    {
    }

    public ActionForward perform(ActionMapping mapping, 
                                 ActionForm form, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
        throws ServletException, IOException
    {                        
        
        String providerNo = request.getParameter("providerNo");
        String searchProviderNo = request.getParameter("searchProviderNo");        
        String status = request.getParameter("status");
                
        String[] flaggedLabs = request.getParameterValues("flaggedLabs"); 
        String selectedProviders = request.getParameter("selectedProviders");
        
        String newURL = "";
        
        try {
            MDSResultsData.updateLabRouting(flaggedLabs, selectedProviders);
            newURL = mapping.findForward("success").getPath();
            newURL = newURL + "?providerNo="+providerNo+"&searchProviderNo="+searchProviderNo+"&status="+status;
            if (request.getParameter("lname") != null) { newURL = newURL + "&lname="+request.getParameter("lname"); }
            if (request.getParameter("fname") != null) { newURL = newURL + "&fname="+request.getParameter("fname"); }
            if (request.getParameter("hnum") != null) { newURL = newURL + "&hnum="+request.getParameter("hnum"); }            
        } catch (Exception e) {
            System.out.println("exception in ReportReassignAction:"+e);
            newURL = mapping.findForward("failure").getPath();
        }
        // System.out.println("In ReportReassignAction: newURL is: "+newURL);
        return (new ActionForward(newURL));                
    }
}