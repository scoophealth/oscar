package org.oscarehr.survey.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.survey.service.UserManager;

public abstract class AbstractSurveyAction extends DispatchAction {

	protected UserManager userManager;
	
	public void setUserManager(UserManager mgr) {
		this.userManager = mgr;
	}
	
	protected void postMessage(HttpServletRequest request, String key, String val) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key,val));
		saveMessages(request,messages);
	}
	
	protected void postMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key));
		saveMessages(request,messages);
	}
}
