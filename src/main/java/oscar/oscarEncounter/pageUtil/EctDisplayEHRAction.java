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
package oscar.oscarEncounter.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;

public class EctDisplayEHRAction extends EctDisplayAction {

	private static final String cmd = "ehr";

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_ehr", "r", null)) {
			return true;
		} else {
			String winName = "ehr" + bean.demographicNo;
			String url = "javascript:void(0)";
			Dao.setLeftHeading("Provincial EHR Services");
			Dao.setLeftURL(url);

			url += ";return false;";
			Dao.setRightURL(url);
			Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action

			if(request.getSession().getAttribute("oneid_token") != null) {
				NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
				item.setTitle("Clinical Connect Viewer");
				item.setLinkTitle("Open the Clinical Connect EHR Viewer");
				item.setURL("openCCEHRWindow('"+request.getContextPath()+"/clinicalConnectEHRViewer.do?method=launch&demographicNo="+bean.demographicNo +"','"+bean.demographicNo+"');return false;");
				Dao.addItem(item);
			} else {
				NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
				item.setTitle("Not logged in");
				item.setLinkTitle("");
				item.setURL("javascript:void(0);return false;");
				Dao.addItem(item);
			}

			return true;
		}
	}

	public String getCmd() {
		return cmd;
	}
}
