
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class UnRemEFormAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         String fdid = request.getParameter("fdid");
         if (!(fdid == null)) {
             EFormUtil.unRemoveEForm(fdid);
         }
         return mapping.findForward("success");
    }
    
}