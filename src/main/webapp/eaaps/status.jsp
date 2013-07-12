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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<title>eAAPS Status</title>
</head>

<body class="mainbody" vlink="#0000FF">
	<table class="MainTable" id="scrollNumber1" name="encounterTable"
		style="margin: 0px;">
		<tr class="topbar">
			<td class="MainTableTopRowLeftColumn" width="60px">eAAPS</td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar">
					<tr>
						<td>Status Information</td>
						<td style="text-align: right;"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table cellspacing="0" id="queue_provider_table" style="margin: 0px;">
		<tr>
			<td colspan="2"><a id="dbInfo"></a></td>
		</tr>
		<tr>
			<td>
				<h1>
					<c:out value="${message}" />
				</h1>
			</td>
		</tr>
	</table>
</body>
</html>