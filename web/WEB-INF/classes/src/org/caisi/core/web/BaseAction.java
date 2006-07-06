package org.caisi.core.web;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.caisi.service.InfirmBedProgramManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.DispatchActionSupport;

public class BaseAction extends DispatchActionSupport
{
    public void addError(HttpServletRequest req, String message)
    {
        ActionMessages msgs = getErrors(req);
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "errors.detail", message));
        addErrors(req, msgs);
    }

    public void addMessage(HttpServletRequest req, String message)
    {
        ActionMessages msgs = getMessages(req);
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "errors.detail", message));
        addMessages(req, msgs);
    }

    public ApplicationContext getAppContext()
    {
        return WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext());
    }

	public InfirmBedProgramManager getInfirmBedProgramManager() {
		InfirmBedProgramManager bpm = (InfirmBedProgramManager) getAppContext()
				.getBean("infirmBedProgramManager");
		return bpm;
	}
    
}

