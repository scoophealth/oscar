package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarMDS.data.MDSResultsData;

public class PatientMatchAction extends Action
{

    public PatientMatchAction()
    {
    }

    public ActionForward perform(ActionMapping mapping, 
                                 ActionForm form, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
        throws ServletException, IOException
    {                        
        
        String demographicNo = request.getParameter("demographicNo");
        String labNo = request.getParameter("labNo");
        
        String newURL = "";
        
        // System.out.println("In ReportReassignAction: labNo is: "+labNo+"  demographicNo is: "+demographicNo);
        
        try {
            MDSResultsData.updatePatientLabRouting(labNo, demographicNo);
            newURL = mapping.findForward("success").getPath();
            newURL = newURL + "?demographicNo="+demographicNo;            
        } catch (Exception e) {
            System.out.println("exception in ReportReassignAction:"+e);
            newURL = mapping.findForward("failure").getPath();
        }
        // System.out.println("In ReportReassignAction: newURL is: "+newURL);
        return (new ActionForward(newURL));                
    }
}