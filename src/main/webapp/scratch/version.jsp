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
<html>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.common.model.ScratchPad" %>
<%@ page import="oscar.util.DateUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%

ScratchPad scratchPad = (ScratchPad)request.getAttribute("ScratchPad");
String date = DateUtils.formatDateTime(scratchPad.getDateTime(), request.getLocale());
%>

<head>
<title>Scratch Pad Version from <%=date%></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
</head>
<body class="BodyStyle">
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">scratch</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td style="text-align:center">Scratchpad version <%=date%></td>				
				<td style="text-align: right"><oscar:help keywords="pad" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td style="height:900px; width:655px; margin:10px 25px 10px 25px; border-style:groove; background-color:#CCCCCC; overflow:auto;">
		<%=scratchPad.getText().replaceAll("\n","<br>")%>	
	</td>
	</tr>
</table>
</body>
</html>