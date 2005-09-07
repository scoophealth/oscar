
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.data.AddToGroupForm;
import oscar.eform.EFormUtil;

public class AddToGroupAction extends Action {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         AddToGroupForm fm = (AddToGroupForm) form;
         String fid = fm.getFid();
         String groupName = fm.getGroupName();
         if (fid != null) {
             EFormUtil.addEFormToGroup(groupName, fid);
         }
         request.setAttribute("group_view", groupName);
         return mapping.findForward("success");
    }
    
}
