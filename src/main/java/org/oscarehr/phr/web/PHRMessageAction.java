/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.phr.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarMessageManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.UtilDateUtilities;

/**
 * @author jay
 */
public class PHRMessageAction extends DispatchAction {

	private static Logger log = MiscUtils.getLogger();

	PHRDocumentDAO phrDocumentDAO;
	PHRActionDAO phrActionDAO;
	PHRService phrService;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.execute(mapping, form, request, response);
	}

	/** Creates a new instance of PHRMessageAction */
	public PHRMessageAction() {
	}

	public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
		this.phrDocumentDAO = phrDocumentDAO;
	}

	public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
		this.phrActionDAO = phrActionDAO;
	}

	public void setPhrService(PHRService phrService) {
		this.phrService = phrService;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return viewMessages(mapping, form, request, response);
	}

	public ActionForward viewMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		log.debug("AUTH " + auth);
		clearSessionVariables(request);

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String providerNo = loggedInInfo.loggedInProvider.getProviderNo();
		List docs = phrDocumentDAO.getDocumentsReceived("MESSAGE", providerNo);
		ArrayList<PHRMessage> messages = null;
//		List<PHRAction> actionsPendingApproval = phrActionDAO.getActionsByStatus(PHRAction.STATUS_APPROVAL_PENDING, providerNo);
		if (docs != null) {
			messages = new ArrayList<PHRMessage>(docs.size());
			for (int idx = 0; idx < docs.size(); ++idx) {
				PHRDocument doc = (PHRDocument) docs.get(idx);
				PHRMessage msg = new PHRMessage(doc);
				messages.add(msg);
			}
		}


		request.getSession().setAttribute("indivoMessages", docs);
		request.getSession().setAttribute("indivoMessageBodies", messages);

//		if (actionsPendingApproval != null) {
//			request.getSession().setAttribute("actionsPendingApproval", actionsPendingApproval);
//		}

		return mapping.findForward("view");
	}

	public ActionForward viewSentMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {

		return mapping.findForward("view");
	}

	public ActionForward viewArchivedMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("view");
	}

	public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); log.debug("AUTH "+auth); String indivoId = auth.getUserId(); String ticket = auth.getToken();
		 */

		return mapping.findForward("read");
	}

	//
	// Reply is a create but displays the message being re
	//
	public ActionForward reply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return mapping.findForward("create");
	}

	public ActionForward createMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		String demographicNo = request.getParameter("demographicNo");
		String provNo = (String) request.getSession().getAttribute("user");
		
		PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
        
        if (auth == null || auth.getMyOscarUserId() == null) {
            request.setAttribute("forwardToOnSuccess", request.getContextPath() + "/phr/PhrMessage.do?method=createMessage&providerNo="+provNo+"&demographicNo=" + demographicNo);
            return mapping.findForward("loginAndRedirect");
        }
		
		
		
		DemographicData dd = new DemographicData();
		org.oscarehr.common.model.Demographic d = dd.getDemographic(demographicNo);
		ProviderData pp = new ProviderData();
		String providerName = ProviderData.getProviderName(provNo);

		String toName = d.getFirstName() + " " + d.getLastName();
		String toId = demographicNo;
		String toType = "" + PHRDocument.TYPE_DEMOGRAPHIC;

		String fromName = providerName;
		String fromId = provNo;
		String fromType = "" + PHRDocument.TYPE_PROVIDER;

		request.setAttribute("toName", toName);
		request.setAttribute("toId", toId);
		request.setAttribute("toType", toType);

		request.setAttribute("fromName", fromName);
		request.setAttribute("fromId", fromId);
		request.setAttribute("fromType", fromType);

		return mapping.findForward("create");
	}

	public ActionForward sendReply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long replyToMessageId=new Long(request.getParameter("replyToMessageId"));
		String message=StringUtils.trimToNull(request.getParameter("body"));

		PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(request.getSession());
		Long messageId = MyOscarMessageManager.sendReply(auth.getMyOscarUserId(), auth.getMyOscarPassword(), replyToMessageId, message);

		if(request.getParameter("andPasteToEchart")!= null && request.getParameter("andPasteToEchart").equals("yes")){
			ActionRedirect redirect = new ActionRedirect(mapping.findForward("echart"));
			redirect.addParameter("myoscarmsg", messageId.toString());
			redirect.addParameter("remyoscarmsg",replyToMessageId.toString());
			redirect.addParameter("appointmentDate",UtilDateUtilities.DateToString(new Date())); //Makes echart note have todays date, which makes sense because we are reply now
			redirect.addParameter("demographicNo",request.getParameter("demographicNo"));
			return redirect;
		}


		return mapping.findForward("view");
	}

	public ActionForward sendPatient(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subject = request.getParameter("subject");
		String messageBody = request.getParameter("body");
		String demographicId = request.getParameter("demographicId");

		PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);

		DemographicData demo = new DemographicData();
		Long recipientMyOscarUserId = MyOscarUtils.getMyOscarUserId(auth, demo.getDemographic(demographicId).getMyOscarUserName());

		MyOscarMessageManager.sendMessage(auth.getMyOscarUserId(), auth.getMyOscarPassword(), recipientMyOscarUserId, subject, messageBody);

		return mapping.findForward("view");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); log.debug("AUTH "+auth); String indivoId = auth.getUserId(); String ticket = auth.getToken();
		 */

		String id = request.getParameter("id");
		if (id == null) return viewSentMessages(mapping, form, request, response);
		log.debug("Id to delete:" + id);
		PHRAction action = phrActionDAO.getActionById(id);
		if (action.getStatus() != PHRAction.STATUS_SENT) {
			action.setStatus(PHRAction.STATUS_NOT_SENT_DELETED);
			phrActionDAO.update(action);
		}

		return viewSentMessages(mapping, form, request, response);
	}

	public ActionForward resend(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH); log.debug("AUTH "+auth); String indivoId = auth.getUserId(); String ticket = auth.getToken();
		 */

		String id = request.getParameter("id");
		if (id != null) {
			PHRAction action = phrActionDAO.getActionById(id);
			if (action.getStatus() != PHRAction.STATUS_SENT) {
				action.setStatus(PHRAction.STATUS_SEND_PENDING);
				phrActionDAO.update(action);
			}
		}
		return viewSentMessages(mapping, form, request, response);
	}


	public ActionForward flipActive(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long messageId=new Long(request.getParameter("messageId"));

		PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		MyOscarMessageManager.flipActive(auth.getMyOscarUserId(), auth.getMyOscarPassword(), messageId);

		return mapping.findForward("view");
	}

	private void clearSessionVariables(HttpServletRequest request) {
		request.getSession().setAttribute("indivoMessages", null);
		request.getSession().setAttribute("indivoSentMessages", null);
		request.getSession().setAttribute("indivoMessageActions", null);
		request.getSession().setAttribute("indivoArchivedMessages", null);
		request.getSession().setAttribute("indivoOtherActions", null);

	}
}
