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

<%
	if(session.getAttribute("user") == null){
		response.sendRedirect("../../../logout.jsp");
	}
	String postTo = request.getParameter("postTo");
	if(null == postTo){
		out.println("<script language=\"JavaScript\">javascript:window.close();</SCRIPT>");
		return;
	}
	String startLimit = oscar.Misc.check(request.getParameter("startLimit"), "0"),
	       orderby = oscar.Misc.check(request.getParameter("orderby"), "last_name"),
	       column = oscar.Misc.check(request.getParameter("column"), null, orderby),
	       keyword = oscar.Misc.check(request.getParameter("keyword"), ""),
          
	sql_select = "SELECT demographic.demographic_no, demographic.last_name, demographic.first_name, demographic.chart_no, demographic.sex, demographic.year_of_birth, demographic.month_of_birth, demographic.date_of_birth, demographic.patient_status FROM demographic",
		sql_where =" WHERE demographic.@column LIKE '@keyword%'",
		sql_orderby = " ORDER BY demographic.@orderby LIMIT @startLimit, 10",
		query = sql_select + (keyword.equals("")? "" : sql_where) + sql_orderby,
		url = "demo_select.jsp?keyword=" + keyword + "&postTo=" + postTo + (column.equals("")? "" : "&column=" + column),
		sql = query.replaceAll("@keyword", keyword).replaceAll("@orderby", orderby).replaceAll("@startLimit", startLimit).replaceAll("@column", column);
%>

<%@page import="oscar.oscarDB.DBHandler"%><html>
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
			value="demographic_no"
			<%=(column.equals("demographic_no")? "checked" : "")%> /><a
			href="<%=url%>&orderby=demographic_no">Demo No</a></td>
		<td width="20%" class="Text"><input type="radio" name="column"
			value="last_name" <%=(column.equals("last_name")? "checked" : "")%> /><a
			href="<%=url%>">Last Name</a></td>
		<td width="15%" class="Text"><input type="radio" name="column"
			value="first_name" <%=(column.equals("first_name")? "checked" : "")%> /><a
			href="<%=url%>&orderby=first_name">First Name</a></td>
		<td width="10%" class="Text" align="center"><input type="radio"
			name="column" value="chart_no"
			<%=(column.equals("chart_no")? "checked" : "")%> /><a
			href="<%=url%>&orderby=chart_no">Chart#</a></td>
		<td width="2%" class="Text" align="center"><input type="radio"
			name="column" value="sex" <%=(column.equals("sex")? "checked" : "")%> /><a
			href="<%=url%>&orderby=sex">Sex</a></td>
		<td width="15%" class="Text" align="center"><input type="radio"
			name="column" value="year_of_birth"
			<%=(column.equals("year_of_birth")? "checked" : "")%> /><a
			href="<%=url%>&orderby=year_of_birth,month_of_birth,date_of_birth">DOB</a></td>
		<td width="2%" class="Text" align="center"><input type="radio"
			name="column" value="patient_status"
			<%=(column.equals("patient_status")? "checked" : "")%> /><a
			href="<%=url%>&orderby=patient_status">Status</a></td>
	</tr>
	<%
	java.sql.ResultSet rs = DBHandler.GetSQL(sql);
	boolean other = true;
	int count = 0;
	while (rs.next()){
%>
	<tr class="<%=(other? "LightBG" : "WhiteBG")%>">
		<td class="Text" align="center"><a
			href="javascript:PopupReturn('<%=oscar.Misc.getString(rs,"demographic_no")%>')"><%=oscar.Misc.getString(rs,"demographic_no")%></a></td>
		<td class="Text"><%=oscar.Misc.toUpperLowerCase(oscar.Misc.getString(rs,"last_name"))%></td>
		<td class="Text"><%=oscar.Misc.toUpperLowerCase(oscar.Misc.getString(rs,"first_name"))%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(oscar.Misc.getString(rs,"chart_no"), "")%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(oscar.Misc.getString(rs,"sex"), "")%></td>
		<td class="Text" align="center" nowrap><%=oscar.Misc.getString(rs,"year_of_birth")+"-"+oscar.Misc.getString(rs,"month_of_birth")+"-"+oscar.Misc.getString(rs,"date_of_birth")%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(oscar.Misc.getString(rs,"patient_status"), "")%></td>
	</tr>
	<%
		count++;
		other = !(other);
	}
	rs.close();
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
