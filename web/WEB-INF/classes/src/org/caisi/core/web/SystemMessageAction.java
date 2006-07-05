package org.caisi.core.web;

import java.util.Date;
import java.util.List;

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
import org.caisi.model.SystemMessage;
import org.caisi.service.SystemMessageManager;

public class SystemMessageAction extends DispatchAction {

	private static Log log = LogFactory.getLog(SystemMessageAction.class);
	
	protected SystemMessageManager mgr = null;
	
	public void setSystemMessageManager(SystemMessageManager mgr) {
		this.mgr = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
		
	public ActionForward list(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List activeMessages = mgr.getMessages();
		request.setAttribute("ActiveMessages",activeMessages);
		return mapping.findForward("list");
	}
	
	public ActionForward edit(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm systemMessageForm = (DynaActionForm)form;
		String messageId = request.getParameter("id");
		
		if(messageId != null) {
			SystemMessage msg = mgr.getMessage(messageId);
			
			if(msg == null) {
				ActionMessages webMessage = new ActionMessages();
				webMessage.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("system_message.missing"));
				saveErrors(request,webMessage);
				return list(mapping,form,request,response);
			}
			systemMessageForm.set("system_message",msg);
		}
		
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm userForm = (DynaActionForm)form;
		SystemMessage msg = (SystemMessage)userForm.get("system_message");
		msg.setCreation_date(new Date());
		mgr.saveSystemMessage(msg);
		
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("system_message.saved"));
        saveMessages(request,messages);

        return list(mapping,form,request,response);
	}
	
	public ActionForward view(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List messages = mgr.getMessages();
		if(messages.size()>0) {
			request.setAttribute("messages",messages);
		}
		return mapping.findForward("view");
	}
}
