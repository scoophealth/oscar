package oscar.oscarEncounter.immunization.config.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oscar.oscarEncounter.immunization.config.data.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EctImmInitConfigDeleteImmuSetAction extends Action {

	public ActionForward perform(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	throws ServletException, IOException {
		
		EctImmInitConfigDeleteImmuSetForm frm =
			(EctImmInitConfigDeleteImmuSetForm) form;
		
		EctImmImmunizationSetData aSet = new EctImmImmunizationSetData();
		
		String[] strId = frm.getChkSetId();
		int stat = frm.getAction().equals("Delete") ? 2 : 0; // 2-delete, 0-undelete
		if (strId != null && strId.length > 0) {
			for (int i=0; i<strId.length; i++) {
				aSet.updateImmunizationSetStatus(strId[i], stat);				
			}
		}
		
		if (stat == 0) request.setAttribute("stat", "2"); // like ?stat=2

		return mapping.findForward("success");
	}
}
