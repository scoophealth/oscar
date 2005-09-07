
package oscar.eform.actions;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;
import oscar.eform.data.ViewGroupForm;

public class ViewGroupAction extends Action {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         ViewGroupForm fm = (ViewGroupForm) form;
         String groupName = fm.getGroupView();
         request.setAttribute("group_view", groupName);
         return mapping.findForward("success");
    }
    
}
