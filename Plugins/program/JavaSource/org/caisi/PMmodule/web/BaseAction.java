package org.caisi.PMmodule.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.AgencyManager;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.IntakeAManager;
import org.caisi.PMmodule.service.IntakeBManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProgramQueueManager;
import org.caisi.PMmodule.service.ProviderManager;
import org.caisi.PMmodule.service.RatePageManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseAction extends Action
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

    /*public LoginManager getLoginManager()
    {
        return (LoginManager) getAppContext().getBean("loginManager");
    }
    */
    public RatePageManager getRateManager()
    {
        return (RatePageManager) getAppContext().getBean("ratePageManager");
    }

    public ClientManager getClientManager()
    {
        return (ClientManager) getAppContext().getBean("clientManager");
    }

    public ProviderManager getProviderManager()
    {
        return (ProviderManager) getAppContext().getBean("providerManager");
    }
     
    public IntakeAManager getIntakeAManager()
    {
        return (IntakeAManager) getAppContext().getBean("intakeAManager");
    }
    
    public IntakeBManager getIntakeBManager()
    {
        return (IntakeBManager) getAppContext().getBean("intakeBManager");
    }

    public ProgramManager getProgramManager()
    {
        return (ProgramManager) getAppContext().getBean("programManager");
    }

    public ProgramQueueManager getProgramQueueManager()
    {
        return (ProgramQueueManager) getAppContext().getBean("programQueueManager");
    }
   
    public AdmissionManager getAdmissionManager()
    {
        return (AdmissionManager) getAppContext().getBean("admissionManager");
    }
     
    public AgencyManager getAgencyManager()
    {
        return (AgencyManager) getAppContext().getBean("agencyManager");
    }
}
