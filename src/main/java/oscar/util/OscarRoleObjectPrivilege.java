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

package oscar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.SecObjPrivilegeDao;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OscarRoleObjectPrivilege {

	private static PageContext pageContext;
	private static String rights = "r";

	public static Vector<Object> getPrivilegeProp(String objName) {
		Properties prop = new Properties();
		Vector<String> roleInObj = new Vector<String>();
		ArrayList<String> priority = new ArrayList<String>();

		String[] objectNames = getVecObjectName(objName);
		SecObjPrivilegeDao dao = SpringUtils.getBean(SecObjPrivilegeDao.class);
		for (SecObjPrivilege s : dao.findByObjectNames(Arrays.asList(objectNames))) {
			prop.setProperty(s.getId().getRoleUserGroup(), s.getPrivilege());
			roleInObj.add(s.getId().getRoleUserGroup());
			priority.add("" + s.getPriority());
		}

		Vector<Object> ret = new Vector<Object>();
		ret.add(prop);
		ret.add(roleInObj);
		ret.add(priority);
		return ret;
	}

	public static ArrayList<Object> getPrivilegePropAsArrayList(String objName) {
		ArrayList<Object> ret = new ArrayList<Object>();
		Properties prop = new Properties();

		SecObjPrivilegeDao dao = (SecObjPrivilegeDao) SpringUtils.getBean("secObjPrivilegeDao");
		String[] objectNames = getVecObjectName(objName);
		ArrayList<String> objects = new ArrayList<String>();

		for (String t : objectNames) {
			objects.add(t);
		}

		List<SecObjPrivilege> privileges = dao.findByObjectNames(objects);

		ArrayList<String> roleInObj = new ArrayList<String>();
		for (SecObjPrivilege sop : privileges) {
			prop.setProperty(sop.getId().getRoleUserGroup(), sop.getPrivilege());
			roleInObj.add(sop.getId().getRoleUserGroup());
		}
		ret.add(prop);
		ret.add(roleInObj);

		return ret;
	}

	/**
	 * returns the providers roles as properties object
	 */
	private static Properties getVecRole(String roleName) {
		Properties prop = new Properties();
		String[] temp = roleName.split("\\,");
		for (int i = 0; i < temp.length; i++) {
			prop.setProperty(temp[i], "1");
		}
		return prop;
	}

	private static String[] getVecObjectName(String objectName) {
		String[] temp = objectName.split("\\,");
		return temp;
	}

	private static ArrayList<String> getPrivilege(String privilege) {
		ArrayList<String> vec = new ArrayList<String>();
		if (privilege != null) {
			String[] temp = privilege.split("\\|");
			for (int i = 0; i < temp.length; i++) {
				temp[i] = StringUtils.trimToNull(temp[i]);
				if (temp[i] == null) continue;
				vec.add(temp[i]);
			}
		}

		return vec;
	}

	
	public static boolean checkPrivilege(String roleName, Properties propPrivilege, List<String> roleInObj) {
		return checkPrivilege(roleName, propPrivilege, roleInObj, rights);
	}

	public static boolean checkPrivilege(String roleName, Properties propPrivilege, List<String> roleInObj, String rightCustom) {
		return checkPrivilege(roleName, propPrivilege, roleInObj, null, rightCustom);
	}

	public static boolean checkPrivilege(String roleName, Properties propPrivilege, List<String> roleInObj, List<String> priority, String rightCustom) {
		boolean ret = false;
		Properties propRoleName = getVecRole(roleName);
		for (int i = 0; i < roleInObj.size(); i++) {
			if (!propRoleName.containsKey(roleInObj.get(i))) continue;

			String singleRoleName = roleInObj.get(i);
			String strPrivilege = propPrivilege.getProperty(singleRoleName);
			List<String> vecPrivilName = getPrivilege(strPrivilege);

			boolean[] check = { false, false };
			for (int j = 0; j < vecPrivilName.size(); j++) {
				check = checkRights(vecPrivilName.get(j), rightCustom);

				if (check[0]) { // get the rights, stop comparing
					return true;
				}
				if (check[1]) { // get the only rights, stop and return the result
					return check[0];
				}
			}
			if (priority != null && priority.get(i) != null) {
				// Since higher priority goes first in the list, if priority>0 we can skip the rest
				if (!priority.get(i).trim().equals("") && !priority.get(i).trim().equals("0")) break;
			}
		}
		return ret;
	}

	private static boolean[] checkRights(String privilege, String rights1) {
		boolean[] ret = { false, false }; // (gotRights, break/continue)
		/*
		 * if ("*".equals(privilege)) { ret[0] = true; } else if (privilege.equals(rights1.toLowerCase()) || (privilege.length() > 1 && privilege.startsWith("o") && privilege.substring(1).equals( rights1.toLowerCase()))) { ret[0] = true; if
		 * (privilege.startsWith("o")) ret[1] = true; // break } else if (privilege.equals("o")) { // for "o" ret[0] = false; ret[1] = true; // break }
		 */
		if ("x".equals(privilege)) {
			ret[0] = true;
		} else if (privilege.compareTo(rights1.toLowerCase()) >= 0) {
			ret[0] = true;
		}
		return ret;
	}

	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}
}
