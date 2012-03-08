package oscar.dms.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import oscar.dms.EDocUtil;
import oscar.dms.data.ChangeDocStatusForm;

public class ChangeDocStatusAction extends DispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		ChangeDocStatusForm fm = (ChangeDocStatusForm) form;
		
		
	
		if ((fm.getDocTypeD()!="")&&(fm.getStatusD()!="")) {
				EDocUtil.changeDocTypeStatusSQL(fm.getDocTypeD(),"Demographic",fm.getStatusD());
				
		} 
		
		if ((fm.getDocTypeP()!="")&&(fm.getStatusP()!="")){
				EDocUtil.changeDocTypeStatusSQL(fm.getDocTypeP(),"Provider",fm.getStatusP());
				
		}
		
		return(mapping.findForward("success"));
		
	}
	
	
}
