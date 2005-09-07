
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class RestoreEFormAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         String fid = request.getParameter("fid");
         EFormUtil.restoreEForm(fid);
         return mapping.findForward("success");
    }
    
}
