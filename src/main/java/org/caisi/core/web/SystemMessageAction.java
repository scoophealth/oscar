/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.core.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.SystemMessageDao;
import org.oscarehr.common.model.SystemMessage;

public class SystemMessageAction extends DispatchAction {

	private SystemMessageDao systemMessageDao = null;
	
	public void setSystemMessageDao(SystemMessageDao systemMessageDao) {
		this.systemMessageDao = systemMessageDao;
	}
	
	public ActionForward unspecified(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
		
	public ActionForward list(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<SystemMessage> activeMessages = systemMessageDao.findAll();
		request.setAttribute("ActiveMessages",activeMessages);
		return mapping.findForward("list");
	}
	
	public ActionForward edit(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm systemMessageForm = (DynaActionForm)form;
		String messageId = request.getParameter("id");
		
		if(messageId != null) {
			SystemMessage msg = systemMessageDao.find(Integer.parseInt(messageId));
			
			if(msg == null) {
				ActionMessages webMessage = new ActionMessages();
				webMessage.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("system_message.missing"));
				saveErrors(request,webMessage);
				return list(mapping,form,request,response);
			}
			systemMessageForm.set("system_message",msg);
			request.getSession().setAttribute("systemMessageId",messageId);
		} else {
			request.getSession().setAttribute("systemMessageId","");
		}
		
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm userForm = (DynaActionForm)form;
		SystemMessage msg = (SystemMessage)userForm.get("system_message");
		msg.setCreationDate(new Date());
		int messageId = 0;
		String messageId_str= (String)request.getSession().getAttribute("systemMessageId");
		if(messageId_str!=null && messageId_str!="") {
			messageId = Integer.valueOf(messageId_str).intValue();
		}
		
		if(messageId>0 || (msg.getId() != null && msg.getId().intValue()>0)) {			
			msg.setId(messageId);
			systemMessageDao.merge(msg);
		} else {
			systemMessageDao.persist(msg);
		}
		
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("system_message.saved"));
        saveMessages(request,messages);

        return list(mapping,form,request,response);
	}
	
	public ActionForward view(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<SystemMessage> messages = systemMessageDao.findAll();
		if(messages.size()>0) {
			request.setAttribute("messages",messages);
		}
		return mapping.findForward("view");
	}
}
