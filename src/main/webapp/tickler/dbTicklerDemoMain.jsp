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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>
<%
	String demoview = request.getParameter("demoview")==null?"all":request.getParameter("demoview") ;
 String parentAjaxId = request.getParameter("parentAjaxId")==null?"":request.getParameter("parentAjaxId");
 String updateParent = request.getParameter("updateParent")==null?"false":request.getParameter("updateParent");

String[] param = new String[2];
String[] temp = request.getParameterValues("checkbox");
if (temp== null){
%>
<jsp:forward page='ticklerDemoMain.jsp'>
         <jsp:param name="demoview" value='<%=demoview%>' />
         <jsp:param name="parentAjaxId" value="<%=parentAjaxId%>" />
         <jsp:param name="updateParent" value="<%=updateParent%>" />
</jsp:forward>
<%
	}else{
	for (int i=0; i<temp.length; i++){
		param[0] = request.getParameter("submit_form").substring(0,1);
		param[1] = temp[i];
		Tickler t = ticklerManager.getTickler(loggedInInfo,Integer.parseInt(temp[i]));
		if(t != null) {
	Tickler.STATUS status = Tickler.STATUS.A;
        	char tmp = request.getParameter("submit_form").toCharArray()[0];
        	if(tmp == 'C' || tmp == 'c') {
        		status = Tickler.STATUS.C;
        	}
        	if(tmp == 'D' || tmp == 'd') {
        		status = Tickler.STATUS.D;
        	}
        	ticklerManager.updateStatus(loggedInInfo, t.getId(),loggedInInfo.getLoggedInProviderNo(),status);
		}

	} //end for
}
%>
<jsp:forward page='ticklerDemoMain.jsp'>
    <jsp:param name="demoview" value='<%=demoview%>' />
    <jsp:param name="parentAjaxId" value="<%=parentAjaxId%>" />
    <jsp:param name="updateParent" value="<%=updateParent%>" />
</jsp:forward>
