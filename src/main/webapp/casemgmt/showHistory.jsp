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

<%@ include file="/casemgmt/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="org.springframework.web.context.*,org.springframework.web.context.support.*, org.oscarehr.PMmodule.service.ProviderManager, org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    ProviderManager pMgr = (ProviderManager)ctx.getBean("providerManager");
 %>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Note History</title>
</head>
<body>
<h3 style="text-align: center;"><nested:write name="title" /></h3>
<h3 style="text-align: center;"><nested:write name="demoName" /></h3>
<nested:iterate indexId="idx" id="note" name="history">
	<div
		style="width: 99%; background-color: #EFEFEF; font-size: 12px; border-left: thin groove #000000; border-bottom: thin groove #000000; border-right: thin groove #000000;">
	<div><p><nested:write name="note" property="note" /></p></div>
	<div style="color: #0000FF;"><nested:notEmpty name="current">
		<c:if test="${current[idx] == false}">
			<div style="color: #FF0000;">REMOVED</div>
		</c:if>
	</nested:notEmpty>
        <c:if test="${note.archived == true}">
                <div style="color: #336633;">ARCHIVED</div>
        </c:if>
        
        Documentation Date: <nested:write name="note"
		property="observation_date" format="dd-MMM-yyyy H:mm" /><br>
	<nested:equal name="note" property="signed" value="true"> 
                             Signed by 
                             <%                               
                               CaseManagementNote n = (CaseManagementNote)note;
                               out.println(pMgr.getProvider(n.getSigning_provider_no()).getFormattedName());
                             %>
	</nested:equal> <nested:notEqual name="note" property="signed" value="true"> 
                             Saved by 
                             <nested:write name="note"
			property="provider.formattedName" />:
                         </nested:notEqual> <nested:write name="note"
		property="update_date" format="dd-MMM-yyyy H:mm" /></div>
	</div>
</nested:iterate>
</body>
</html>
