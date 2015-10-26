<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	String[][] dataList = new String[335][15];
	if (request.getAttribute("dataList") != null) {
		dataList = (String[][]) request.getAttribute("dataList");
	}

	Date[] dateList = (Date[]) request.getAttribute("dateList");
%>

<html:html xhtml="true" locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Street Health Mental Health Report</title>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite page="/css/intakeC.css" />" />
<script language="JavaScript"
	src="<html:rewrite page="/js/ClientSearch.js" />"></script>
<html:base />
</head>
<body>
<script type="text/javascript">
			function download() {
				location.href="<html:rewrite page="/PMmodule/do_download.jsp" />";
			}
		</script>
<table height="15" align="center">
	<tr>
		<td class="style76" align="center"><input type="button"
			name="backToClientSearch" value="Back"
			onclick="javascript:history.back();" /> <input type="button"
			name="downLoadCSVFile" value="Download"
			onclick="javascript:download();" /></td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1" width="95%"
	align="center">
	<%
			for (int i = 0; i < dataList.length; i++) {
			%>
	<tr>
		<%
					for (int j = 0; j < dataList[i].length; j++) {
						if (i == 0 && dataList[i][j].startsWith("Cohort")) {
							int idx = Integer.parseInt(dataList[i][j].substring(7));
							Date tmpDate1 = dateList[idx];
							Date tmpDate2 = dateList[idx + 1];
							String showDate1 = null;
							String showDate2 = null;
							try {
								showDate1 = sdf.format(tmpDate1);
								showDate2 = sdf.format(tmpDate2);
							} catch (Exception e) {
							}
				%>
		<td class="style76" align="center"><%=showDate1%> <br />
		<%=showDate2%> <br />
		<%=dataList[i][j]%></td>
		<%
						} else {
				%>
		<td class="style76" align="center"><%=dataList[i][j]%></td>
		<%
						}
					}
				%>
	</tr>
	<%
			}
			%>
</table>
</body>
</html:html>
