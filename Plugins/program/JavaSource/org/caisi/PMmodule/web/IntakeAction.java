package org.caisi.PMmodule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.service.IntakeAManager;
import org.caisi.PMmodule.service.IntakeCManager;

public class IntakeAction extends DispatchAction {

	private static Log log = LogFactory.getLog(IntakeAction.class);

	private IntakeAManager intakeAManager;
	private IntakeCManager intakeCManager;
	
	public void setIntakeAManager(IntakeAManager mgr) {
		this.intakeAManager = mgr;
	}

	public void setIntakeCManager(IntakeCManager mgr) {
		this.intakeCManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String value = "intakea";
		
		if(intakeAManager.isNewClientForm()) {
			value = "intakea";
		}
		if(intakeCManager.isNewClientForm()) {
			value = "intakec";
		}
		return mapping.findForward(value);
	}
}
