
package oscar.eform.actions;

import org.apache.struts.action.*;
import oscar.eform.data.*;
import oscar.eform.*;
import javax.servlet.http.*;
import java.util.*;

public class AddEFormAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         System.out.println("==================SAVING ==============");
         Enumeration paramNamesE = request.getParameterNames();
         //for each name="fieldname" value="myval"
         ArrayList paramNames = new ArrayList();  //holds "fieldname, ...."
         ArrayList paramValues = new ArrayList(); //holds "myval, ...."
         String fid = request.getParameter("efmfid");
         String demographic_no = request.getParameter("efmdemographic_no");
         String provider_no = request.getParameter("efmprovider_no");
         String subject = request.getParameter("subject");
         if (subject == null) subject="";
         String curField = "";
         while (paramNamesE.hasMoreElements()) {
             curField = (String) paramNamesE.nextElement();
             paramNames.add(curField);
             paramValues.add(request.getParameter(curField));
         }
         //----names parsed
         EForm curForm = new EForm(fid, demographic_no, provider_no);
         ActionErrors errors = curForm.setMeasurements(paramNames, paramValues);
         curForm.setFormSubject(subject);
         curForm.setValues(paramNames, paramValues);
         curForm.setImagePath();
         curForm.setAction();
         curForm.setNowDateTime();
         if (!errors.isEmpty()) {
             saveErrors(request, errors);
             request.setAttribute("curform", curForm);
             request.setAttribute("page_errors", "true");
             return mapping.getInputForward();
         }
         String fdid = EFormUtil.addEForm(curForm);
         //adds parsed values
         EFormUtil.addEFormValues(paramNames, paramValues, fdid, fid, demographic_no);
         //add to oscarMeasurements
         
         return(mapping.findForward("close"));
    }
}
