package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarMDS.data.MDSResultsData;

public class SearchPatientAction extends Action
{

    public SearchPatientAction()
    {
    }

    public ActionForward perform(ActionMapping mapping, 
                                 ActionForm form, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
        throws ServletException, IOException
    {        
        // System.out.println("inside ReportStatusUpdateAction");
        
        String labNo = request.getParameter("segmentID");
        String newURL = "";
                
        try {
            String demographicNo = MDSResultsData.searchPatient(labNo);
            if ( ! demographicNo.equals("0") ) {
                newURL = mapping.findForward("success").getPath();
                newURL = newURL + "?demographicNo="+demographicNo;
            } else {
                newURL = mapping.findForward("failure").getPath();
            }
        } catch (Exception e) {
            System.out.println("exception in SearchPatientAction:"+e);
            newURL = mapping.findForward("failure").getPath();            
        }
        newURL = newURL + "&labNo="+labNo;                
        return new ActionForward(newURL);
    }
}