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
import org.oscarehr.common.dao.RemoteAttachmentsDao;
import org.oscarehr.common.model.RemoteAttachments;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class MsgProceedAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "w", null)) {
			throw new SecurityException("missing required security object (_msg)");
		}
		
		MsgProceedForm frm = (MsgProceedForm) form;

		String id;
		String demoId;

		oscar.oscarMessenger.pageUtil.MsgSessionBean bean;
		bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");

		demoId = frm.getDemoId();
		id = frm.getId();

		RemoteAttachmentsDao dao = SpringUtils.getBean(RemoteAttachmentsDao.class);
		List<RemoteAttachments> rs = dao.findByDemoNoAndMessageId(ConversionUtils.fromIntString(demoId), ConversionUtils.fromIntString(id));
		if (rs.size() > 0) {
			request.setAttribute("confMessage", "1");
		} else {
			RemoteAttachments ra = new RemoteAttachments();
			ra.setDemographicNo(Integer.parseInt(demoId));
			ra.setMessageId(Integer.parseInt(id));
			ra.setSavedBy(bean.getUserName());
			ra.setDate(new Date());
			ra.setTime(new Date());
			dao.persist(ra);
			request.setAttribute("confMessage", "2");
		}

		bean.nullAttachment();

		return (mapping.findForward("success"));
	}
}
