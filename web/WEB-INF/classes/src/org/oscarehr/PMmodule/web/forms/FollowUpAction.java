package org.oscarehr.PMmodule.web.forms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.FormFollowUp;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;

public class FollowUpAction extends DispatchAction {
	private static Log log = LogFactory.getLog(FollowUpAction.class);
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private  final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	
	private ProgramManager programManager;
	private ClientManager clientManager;
	private ProviderManager providerManager;
	private LogManager logManager;
	private FormsManager formsManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public void setFormsManager(FormsManager mgr) {
		this.formsManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		inputForm.set("intake", new FormFollowUp());

		String demographicNo = request.getParameter("demographicNo");
		log.debug("form demographicNo=" + demographicNo);
		if(demographicNo==null) {
			demographicNo=(String)request.getAttribute("demographicNo");
		}
		log.debug("(try2) form demographicNo=" + demographicNo);

		FormFollowUp theForm = (FormFollowUp)formsManager.getCurrentForm(demographicNo,FormFollowUp.class);
		boolean update=(theForm != null);
		
		//view form, provider
		Demographic client  = clientManager.getClientByDemographicNo(demographicNo);
		
		if(update) {
			if(client == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.invalid_client"));
				saveMessages(request,messages);			
				return mapping.findForward("error");
			}  
			theForm.setDemographicNo(Long.valueOf(demographicNo));
		} else {
			theForm = new FormFollowUp();
			//set basic client info
			if(client != null) {
				theForm.setDemographicNo(Long.valueOf(demographicNo));
				theForm.setClientFirstName(client.getFirstName());
				theForm.setClientSurname(client.getLastName());
				theForm.setYear(client.getYearOfBirth());
				theForm.setMonth(client.getMonthOfBirth());
				theForm.setDay(client.getDateOfBirth());
			}
		}
		theForm.setDateAssessment(formatter.format(new Date()));
		theForm.setAssessStartTime(timeFormatter.format(new Date()));
		theForm.setProviderNo(Long.valueOf(getProviderNo(request)));		
		inputForm.set("intake",theForm);

		logManager.log(getProviderNo(request),"read","followupform",demographicNo,request.getRemoteAddr());
	   	request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("form");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		FormFollowUp theForm = (FormFollowUp)inputForm.get("intake");
		boolean update = false;
		
		if(theForm.getId() != null && theForm.getId().longValue()>0) {
			update = true;
		}
		
		theForm.setProviderNo(Long.valueOf(this.getProviderNo(request)));
		theForm.setFormEdited(new Date());
		formsManager.saveForm(theForm);
		
		request.setAttribute("demographicNo",String.valueOf(theForm.getDemographicNo()));		
		inputForm.reset(mapping,request);
		
		return mapping.findForward("success");
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm inputForm = (DynaActionForm)form;
		FormFollowUp theForm = (FormFollowUp)inputForm.get("intake");
		
		Long demographicNo = theForm.getDemographicNo();
		request.setAttribute("demographicNo",demographicNo);
		inputForm.reset(mapping,request);
		
		return mapping.findForward("error");
	}
}
