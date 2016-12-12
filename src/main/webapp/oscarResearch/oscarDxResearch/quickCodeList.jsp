<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_dxresearch" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_dxresearch");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="java.math.*, java.util.*, java.sql.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"%>
<%
	String demoNO = request.getParameter("demographicNo");
	
	dxQuickListBeanHandler dxQlBeanHandler = new dxQuickListBeanHandler();
	Collection quickLists = dxQlBeanHandler.getDxQuickListBeanVector();
	
	if (!quickLists.isEmpty()) {
		dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
		Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();
%>
<table width="100%">
<tr>
<%
		ArrayList<dxQuickListBean> quickListsA = new ArrayList<dxQuickListBean>();
		Iterator iterQ = quickLists.iterator();
		while (iterQ.hasNext()) {
			dxQuickListBean qlBean = (dxQuickListBean)iterQ.next();
			//sort the column by quick list names
			int i=0;
			for (i=0; i<quickListsA.size(); i++) {
				if (quickListsA.get(i).getQuickListName().compareToIgnoreCase(qlBean.getQuickListName())>=0) {
					quickListsA.add(i, qlBean); break;
				}
			}
			if (i==quickListsA.size()) quickListsA.add(qlBean);
		}
		for (dxQuickListBean qlBean : quickListsA) {
			dxQuickListItemsHandler dxList = new dxQuickListItemsHandler(qlBean.getQuickListName());
			if (dxList.getDxQuickListItemsVector().isEmpty()) continue;
			
			Collection list = dxList.getDxQuickListItemsVectorNotInPatientsList(patientDx);
%>	<td valign="top">
		<table style="font-size:small">
		<tr>
			<td colspan="2" valign="top">
				<b><%=qlBean.getQuickListName()%></b>
				<%=list.isEmpty()?"<p><i>All items already in patient's Dx</i></p>" : ""%>
			</td>
		</tr>
<%			ArrayList<dxCodeSearchBean> listA = new ArrayList<dxCodeSearchBean>();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				dxCodeSearchBean code = (dxCodeSearchBean) iter.next();
				//sort the list by descriptions
				int i=0;
				for (i=0; i<listA.size(); i++) {
					if (listA.get(i).getDescription().compareToIgnoreCase(code.getDescription())>=0) {
						listA.add(i, code); break;
					}
				}
				if (i==listA.size()) listA.add(code);
			}
			for (dxCodeSearchBean code : listA) {
%>		<tr>
			<td valign="top"><input type="checkbox" name="xml_research" value="<%=code.getType()%>,<%=code.getDxSearchCode()%>" /></td>
			<td valign="top"><%=code.getDescription()%></td>
		</tr>
<%			}
%>		</table>
	</td>
<%		}
%>
</tr>
</table>
<%	}
%>
