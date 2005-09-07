
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class RemoveFromGroupAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         String fid = request.getParameter("fid");
         String groupName = request.getParameter("groupName");
         EFormUtil.remEFormFromGroup(groupName, fid);
         request.setAttribute("group_view", groupName);
         return mapping.findForward("success");
    }
    
}
