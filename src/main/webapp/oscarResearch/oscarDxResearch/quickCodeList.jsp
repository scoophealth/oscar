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
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"%>
<%
   String demoNO = request.getParameter("demographicNo");
   
   dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
   Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();   
   
   String quickList = oscar.OscarProperties.getInstance().getProperty("DX_QUICK_LIST_DEFAULT");
   
   if (quickList != null){%>
<ul
	<% dxQuickListItemsHandler dxList = new dxQuickListItemsHandler(quickList);
      Collection list = dxList.getDxQuickListItemsVectorNotInPatientsList(patientDx);
      Iterator iter = list.iterator();
      while (iter.hasNext()){
         dxCodeSearchBean code = (dxCodeSearchBean) iter.next();
         %>>
	<li><input type="checkbox" name="xml_research"
		value="<%=code.getType()%>,<%=code.getDxSearchCode()%>" /> <%=code.getDescription()%>
	</li<%
      }%>
      >
</ul>
<%}%>
