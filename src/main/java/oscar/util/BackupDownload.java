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

package oscar.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oscarehr.common.dao.SecObjPrivilegeDao;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.util.SpringUtils;

public class BackupDownload extends GenericDownload {

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();

		// check the rights
		String filename = req.getParameter("filename") == null ? "null" : req.getParameter("filename");
		String dir = (String) session.getAttribute("backupfilepath") == null ? "/home/mysql/" : (String) session.getAttribute("backupfilepath");

		boolean adminPrivs = false;

		String roleName = (String) req.getSession().getAttribute("userrole") + "," + (String) req.getSession().getAttribute("user");
		Object[] v = getPrivilegeProp("_admin.backup,_admin");
		if (checkPrivilege(roleName, (Properties) v[0], (List<String>) v[1])) {
			adminPrivs = true;
		}

		boolean bDownload = false;
		if (filename != null && adminPrivs) {
			bDownload = true;
		}
		download(bDownload, res, dir, filename, null);
	}

	//TODO: Refactor this out of the security tag.
	private String rights = "r";

	private Object[] getPrivilegeProp(String objName) {
		String[] objectNames = getVecObjectName(objName);

		SecObjPrivilegeDao dao = SpringUtils.getBean(SecObjPrivilegeDao.class);
		List<SecObjPrivilege> priviledges = dao.findByObjectNames(Arrays.asList(objectNames));

		Properties prop = new Properties();
		List<String> roleInObj = new ArrayList<String>();
		for (SecObjPrivilege p : priviledges) {
			prop.setProperty(p.getId().getRoleUserGroup(), p.getPrivilege());
			roleInObj.add(p.getId().getRoleUserGroup());
		}

		return new Object[] { prop, roleInObj };
	}

	private Properties getVecRole(String roleName) {
		Properties prop = new Properties();
		String[] temp = roleName.split("\\,");
		for (int i = 0; i < temp.length; i++) {
			prop.setProperty(temp[i], "1");
		}
		return prop;
	}

	private String[] getVecObjectName(String objectName) {
		String[] temp = objectName.split("\\,");
		return temp;
	}

	private List<String> getVecPrivilege(String privilege) {
		List<String> vec = new ArrayList<String>();
		String[] temp = privilege.split("\\|");
		for (int i = 0; i < temp.length; i++) {
			if ("".equals(temp[i])) continue;
			vec.add(temp[i]);
		}
		return vec;
	}

	private boolean checkPrivilege(String roleName, Properties propPrivilege, List<String> roleInObj) {
		boolean ret = false;
		Properties propRoleName = getVecRole(roleName);
		for (int i = 0; i < roleInObj.size(); i++) {
			if (!propRoleName.containsKey(roleInObj.get(i))) continue;

			String singleRoleName = roleInObj.get(i);
			String strPrivilege = propPrivilege.getProperty(singleRoleName, "");
			List<String> vecPrivilName = getVecPrivilege(strPrivilege);

			boolean[] check = { false, false };
			for (int j = 0; j < vecPrivilName.size(); j++) {
				check = checkRights(vecPrivilName.get(j), rights);

				if (check[0]) { // get the rights, stop comparing
					return true;
				}
				if (check[1]) { // get the only rights, stop and return the result
					return check[0];
				}
			}
		}
		return ret;
	}

	private boolean[] checkRights(String privilege, String rights1) {
		boolean[] ret = { false, false }; // (gotRights, break/continue)

		if ("x".equals(privilege)) {
			ret[0] = true;
		} else if (privilege.compareTo(rights1.toLowerCase()) >= 0) {
			ret[0] = true;
		}
		return ret;
	}
}
