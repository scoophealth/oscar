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
<%@ page import="oscar.oscarProvider.data.ProviderColourUpdater"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%
String date = null;
String id = null;
String title = "";
boolean deleted = false;
ScratchPad scratchPad = null;

String user_no = (String) request.getSession().getAttribute("user");
String userfirstname = (String) request.getSession().getAttribute("userfirstname");
String userlastname = (String) request.getSession().getAttribute("userlastname");

String userColour = null;

ProviderColourUpdater colourUpdater = new ProviderColourUpdater(user_no);
userColour = colourUpdater.getColour();

if(request.getAttribute("actionDeleted")!=null){
	deleted = true;	
}else{
	scratchPad = (ScratchPad)request.getAttribute("ScratchPad");    		
	date = DateUtils.formatDateTime(scratchPad.getDateTime(), request.getLocale());
	id = scratchPad.getId().toString();
	title = "Version from " + date;
}
%>

<head>
<title><bean:message key="ScratchPad.title"/> <%=title%></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<%if(userColour!=null){%>
<style>
.TopStatusBar{background-color:<%=userColour%>}
</style>
<%}%>
</head>
<body class="BodyStyle">
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="ScratchPad.title"/></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><h1><%=userfirstname%> <%=userlastname%></h1></td>
				<td style="text-align:center"><bean:message key="ScratchPad.title"/> <%=title%></td>				
				<td style="text-align: right">
				<oscar:help keywords="pad" key="app.top1"/> |
				<a href="javascript:void(0)" onclick="javascript:popup(600,700,'../oscarEncounter/About.jsp')"><bean:message key="global.about" /></a> 
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
	<%if(!deleted){ %>
	<td valign="top" class="MainTableLeftColumn"><button name="deleteVersion" onclick="deleteVersion('<%=id%>')">Delete</button></td>
	<td class="MainTableRightColumn" style="height:900px; width:655px; margin:10px 25px 10px 25px; border-style:groove; background-color:#CCCCCC; overflow:auto;">
		<%=scratchPad.getText().replaceAll("\n","<br>")%>	
	</td>
	<%}else{%>
	<td class="MainTableLeftColumn"></td>
	<td class="MainTableRightColumn">
	<h2>Success! <bean:message key="ScratchPad.title"/> <%=request.getAttribute("actionDeleted")%></h2>
	<button name="updateScratchBtn" onclick="updateScratch()">Close</button>
	
	</td>
	<%}%>
	</tr>
	
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>

<script>
function deleteVersion(id) {
	var action = confirm("Are you sure you would like to delete this entry?");
	
	var url = "Scratch.do?method=delete&id="+id;
	
	if(action){
		window.location = url;
	}
}

function updateScratch(){
	window.opener.location.reload();
	window.close();
}
</script>
</body>
</html>