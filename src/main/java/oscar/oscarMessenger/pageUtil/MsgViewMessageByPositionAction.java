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

/**
 * @author ichan
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.SpringUtils;

import oscar.util.ParameterActionForward;

public class MsgViewMessageByPositionAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// Extract attributes we will need
		String provNo = (String) request.getSession().getAttribute("user");

		if (request.getSession().getAttribute("msgSessionBean") == null) {
			MsgSessionBean bean = new MsgSessionBean();
			bean.setProviderNo(provNo);

			ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
			ProviderData pd = dao.findByProviderNo(provNo);
			if (pd != null) {
				bean.setUserName(pd.getFirstName() + " " + pd.getLastName());
			}
			request.getSession().setAttribute("msgSessionBean", bean);
		}

		String orderBy = request.getParameter("orderBy") == null ? "date" : request.getParameter("orderBy");
		String messagePosition = request.getParameter("messagePosition");
		String demographic_no = request.getParameter("demographic_no");

		MsgDisplayMessagesBean displayMsgBean = new MsgDisplayMessagesBean();
		ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward("success"));

		String sql = "select m.messageid  " 
				+ "from  messagetbl m, msgDemoMap map where map.demographic_no = '" + demographic_no + "'  " 
				+ "and m.messageid = map.messageID  order by " + displayMsgBean.getOrderBy(orderBy) 
				+ " limit " + messagePosition + ", 1";
		FormsDao dao = SpringUtils.getBean(FormsDao.class);
		for (Object[] o : dao.runNativeQuery(sql)) {
			String messageId = String.valueOf(o[0]);
			actionforward.addParameter("messageID", messageId);
			actionforward.addParameter("from", "encounter");
			actionforward.addParameter("demographic_no", demographic_no);
			actionforward.addParameter("messagePostion", messagePosition);
		}

		return actionforward;
	}

}
