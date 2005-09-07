package oscar.eform.actions;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.eform.EFormUtil;

public class DelEFormAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, HttpServletResponse response) {
        String fid = request.getParameter("fid");
        EFormUtil.delEForm(fid);
        return(mapping.findForward("success"));
    }
}
