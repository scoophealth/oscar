
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class DeleteGroupAction extends Action {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         String groupName = request.getParameter("group_name");
         EFormUtil.delEFormGroup(groupName);
         return mapping.findForward("success");
    }
    
}
