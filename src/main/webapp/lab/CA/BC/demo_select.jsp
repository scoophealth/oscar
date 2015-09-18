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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.util.ConversionUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%

	String postTo = request.getParameter("postTo");
	if(null == postTo){
		out.println("<script language=\"JavaScript\">javascript:window.close();</SCRIPT>");
		return;
	}
	
	
	String startLimit = oscar.Misc.check(request.getParameter("startLimit"), "0");
	String orderby = oscar.Misc.check(request.getParameter("orderby"), "LastName");
	String column = oscar.Misc.check(request.getParameter("column"), null, orderby);
	Object keyword = oscar.Misc.check(request.getParameter("keyword"), "");
	if (column != null && column.equals("DemographicNo")) {
		keyword = ConversionUtils.fromIntString(keyword);
	}
	
	String url = "demo_select.jsp?keyword=" + keyword + "&postTo=" + postTo + (column.equals("")? "" : "&column=" + column);
          
	DemographicDao dao = SpringUtils.getBean(DemographicDao.class);	
	
	String keywordForSearch = (keyword == null || "".equals(keyword)) ? "" : keyword + "%";
	List<Demographic> demographics = dao.findByField(column, (Object) keyword, orderby, ConversionUtils.fromIntString(startLimit).intValue());
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR Patient Search</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
function PopupReturn(index){
	window.opener.location = '<%=postTo.replaceAll("-","&")%>' + index;
	window.opener.focus();
	window.close();
};
</SCRIPT>
</head>
<body>
<form method="post" action="demo_select.jsp">
<table width="100%" class="DarkBG">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">Search For Patient</font></font></b></font></td>
	</tr>
</table>
<table>
	<tr>
		<td><input type="hidden" name="postTo" value="<%=postTo%>" /> <input
			type="text" name="keyword" value="<%=keyword%>" /> <input
			type="submit" value="Search" /></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="2"
	cellpadding="2">
	<tr>
		<td width="10%" class="Text"><input type="radio" name="column"
			value="DemographicNo"
			<%=(column.equals("DemographicNo")? "checked" : "")%> /><a
			href="<%=url%>&orderby=DemographicNo">Demo No</a></td>
		<td width="20%" class="Text"><input type="radio" name="column"
			value="LastName" <%=(column.equals("LastName")? "checked" : "")%> /><a
			href="<%=url%>">Last Name</a></td>
		<td width="15%" class="Text"><input type="radio" name="column"
			value="FirstName" <%=(column.equals("FirstName")? "checked" : "")%> /><a
			href="<%=url%>&orderby=FirstName">First Name</a></td>
		<td width="10%" class="Text" align="center"><input type="radio"
			name="column" value="ChartNo"
			<%=(column.equals("ChartNo")? "checked" : "")%> /><a
			href="<%=url%>&orderby=ChartNo">Chart#</a></td>
		<td width="2%" class="Text" align="center"><input type="radio"
			name="column" value="Sex" <%=(column.equals("Sex")? "checked" : "")%> /><a
			href="<%=url%>&orderby=Sex">Sex</a></td>
		<td width="15%" class="Text" align="center"><input type="radio"
			name="column" value="YearOfBirth"
			<%=(column.equals("YearOfBirth")? "checked" : "")%> /><a
			href="<%=url%>&orderby=YearOfBirth">DOB</a></td>
		<td width="2%" class="Text" align="center"><input type="radio"
			name="column" value="PatientStatus"
			<%=(column.equals("PatientStatus")? "checked" : "")%> /><a
			href="<%=url%>&orderby=PatientStatus">Status</a></td>
	</tr>
	<%
	boolean other = true;
	int count = 0;
	for(Demographic d : demographics) {
%>
	<tr class="<%=(other? "LightBG" : "WhiteBG")%>">
		<td class="Text" align="center"><a
			href="javascript:PopupReturn('<%=d.getDemographicNo()%>')">
			<%=d.getDemographicNo()%></a></td>
		<td class="Text"><%=oscar.Misc.toUpperLowerCase(d.getLastName())%></td>
		<td class="Text"><%=oscar.Misc.toUpperLowerCase(d.getFirstName())%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(d.getChartNo(), "")%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(d.getSex(), "")%></td>
		<td class="Text" align="center" nowrap><%= d.getBirthDayAsString()%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(d.getPatientStatus(), "")%></td>
	</tr>
	<%
		count++;
		other = !(other);
	}
	
	int start = Integer.parseInt(startLimit);
	String next = url + "&orderby=" + orderby + "&startLimit=" + (start + 10),
	previous = url + "&orderby=" + orderby + "&startLimit=" + (start - 10);
%>
	<tr>
		<td width="50%" colspan="3" align="right" class="SmallerText">&nbsp;<%=(start>0? "<a href=" + previous + ">previous</a>" : "")%></td>
		<td width="50%" colspan="4" align="left" class="SmallerText">| <%=(count==10? "<a href=" + next + ">next</a>" : "" )%>&nbsp;</td>
	</tr>
</table>
</form>
</body>
</html>
