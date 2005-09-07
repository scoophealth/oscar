
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;
import oscar.eform.data.AddGroupForm;

public class AddGroupAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         AddGroupForm fm = (AddGroupForm) form;
         String groupName = fm.getGroupName();
         EFormUtil.addEFormToGroup(groupName, "0");  //marker for group
         request.setAttribute("group_view", groupName);
         return mapping.findForward("success");
         
    }
    
}
