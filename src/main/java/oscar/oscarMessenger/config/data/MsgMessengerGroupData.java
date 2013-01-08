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

package oscar.oscarMessenger.config.data;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.BeanComparator;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.GroupMembersDao;
import org.oscarehr.common.dao.GroupsDao;
import org.oscarehr.common.model.GroupMembers;
import org.oscarehr.common.model.Groups;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MsgMessengerGroupData {

	public java.util.Vector<String> groupMemberVector;
	public int numGroups;

	public String getMyName(String grpNo) {
		String retval = new String("Root");
		if (Integer.parseInt(grpNo) > 0) {
			GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
			Groups groups = dao.find(ConversionUtils.fromIntString(grpNo));
			if (groups != null) {
				retval = groups.getGroupDesc();
			}
		}
		return retval;
	}

	public String parentDirectory(String grpNo) {
		String retval = "";
		GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
		Groups groups = dao.find(ConversionUtils.fromIntString(grpNo));
		if (groups != null) {
			retval = "" + groups.getParentId();
		}
		return retval;
	}

	public String printGroups(String groupNo) {
		StringBuilder stringBuffer = new StringBuilder();
		numGroups = 0;

		GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
		for (Groups g : dao.findByParentId(ConversionUtils.fromIntString(groupNo))) {
			stringBuffer.append("<a href=\"MessengerAdmin.jsp?groupNo=" + g.getId() + "\">" + g.getGroupDesc() + "</a><br>");
			numGroups++;
		}
		return stringBuffer.toString();
	}

	////----------------------------------------------------------------------------

	public java.util.Vector<String> membersInGroups(String grpNo) {
		groupMemberVector = new java.util.Vector<String>();
		GroupMembersDao dao = SpringUtils.getBean(GroupMembersDao.class);
		for (GroupMembers g : dao.findByGroupId(ConversionUtils.fromIntString(grpNo))) {
			groupMemberVector.add(g.getProviderNo());
		}
		return groupMemberVector;
	}

	@SuppressWarnings("unchecked")
	public void printAllProvidersWithMembers(Locale locale, String grpNo, JspWriter out) {
		java.util.Vector<String> vector = membersInGroups(grpNo);
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> ps = dao.getProviders();
		Collections.sort(ps, new BeanComparator("lastName"));
		try {
			for (Provider p : ps) {
				out.print("   <tr>");
				out.print("      <td>");
				if (vector.contains(p.getProviderNo())) {
					out.print("<input type=\"checkbox\" name=providers value=" + p.getProviderNo() + " checked >");
				} else {
					out.print("<input type=\"checkbox\" name=providers value=" + p.getProviderNo() + ">");
				}
				out.print("      </td>");
				out.print("      <td>");
				out.print(p.getLastName());
				out.print("      </td>");
				out.print("      <td>");
				out.print(p.getFirstName());
				out.print("      </td>");
				out.print("      <td>");

				String strProviderType = p.getProviderType();

				out.print(strProviderType);

				out.print("      </td>");

				out.print("   </tr>");
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

	}

	public String printAllBelowGroups(String grpNo) {
		StringBuilder stringBuffer = new StringBuilder();
		int untilZero = Integer.parseInt(grpNo);

		try {
			GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
			while (untilZero != 0) {				
				Groups g = dao.find(untilZero);
				if (g != null) {
					untilZero = g.getParentId();
					stringBuffer.insert(0, " <a href=\"MessengerAdmin.jsp?groupNo=" + g.getId() + "\"> > " 
					+ g.getGroupDesc() + "</a>");
				} else {
					untilZero = 0;
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		stringBuffer.insert(0, "<a href=\"MessengerAdmin.jsp?groupNo=0\">Root</a>");
		return stringBuffer.toString();
	}

}
