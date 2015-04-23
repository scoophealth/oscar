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

package oscar.oscarMessenger.pageUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class MsgSendMessageAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Extract attributes we will need
		// Setup variables
		ActionMessages errors = new ActionMessages();

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "w", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
		
		String message = ((MsgCreateMessageForm) form).getMessage();
		String[] providers = ((MsgCreateMessageForm) form).getProvider();
		String subject = ((MsgCreateMessageForm) form).getSubject();
		subject.trim();

		saveErrors(request, errors);
		ActionForward actionForward = new ActionForward(mapping.getInput());
		actionForward.setName(mapping.getInput());

		if (subject.length() == 0) {
			subject = "none";
		}

		//By this far it should be safe that its a valid message
		//create a string with all the providers it will be sent too
		ProviderDao pDao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> ps = pDao.getProviders(providers);
		StringBuilder sentToWho = new StringBuilder("Sent to : ");
		for (Provider p : ps) {
			sentToWho.append(" " + p.getFirstName() + " " + p.getLastName() + ". ");
		}

		MsgSentMessageForm trial = new MsgSentMessageForm();
		trial.setSample(sentToWho.toString());

		MessageTbl mt = new MessageTbl();
		mt.setDate(new Date());
		mt.setTime(new Date());
		mt.setMessage(message);
		mt.setSubject(subject);
		mt.setSentBy("jay");
		mt.setSentTo(sentToWho.toString());

		MessageTblDao dao = SpringUtils.getBean(MessageTblDao.class);
		dao.persist(mt);

		MessageListDao mld = SpringUtils.getBean(MessageListDao.class);

		for (int i = 0; i < providers.length; i++) {
			MessageList ml = new MessageList();
			ml.setMessage(mt.getId());
			ml.setProviderNo(providers[i]);
			ml.setStatus("new");
			mld.persist(ml);
		}
		request.setAttribute("SentMessageProvs", sentToWho.toString());
		return (mapping.findForward("success"));
	}
}
