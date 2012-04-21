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
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Program Management Module</title>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/tigris.css" />' />
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/displaytag.css" />' />
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/core.css" />' />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript"
	src="<html:rewrite page="/js/quatroLookup.js" />"></script>
<script type="text/javascript"
	src="<html:rewrite page="/js/validation.js" />"></script>
<script type="text/javascript"
	src="<html:rewrite page="/js/checkDate.js" />"></script>

<script type="text/javascript">
			var isInFrame = true;
			var readOnly = false;
		    var win=null;
			
			/* date picker variables */
			var timerId = 0;
			var isDateValid = true;
			var doOnBlur = true;
			var deferSubmit = false;
			/* end of datepicker variables */
			
			function setDivPosition()
			{
				 var ele = document.getElementById("scrollBar");
				 if(ele==null) return;
			     if (ele)
			     {
			        if(document.getElementById("scrollPosition")==null) return;
			        var sp = document.getElementById("scrollPosition").value;
			        if (!sp) sp = "0";
			        document.getElementById("scrollBar").scrollTop = sp;
			     }
			 }
			
			 function getDivPosition()
			 {
			 	 var ele = document.getElementById("scrollBar");
				 if(ele==null) return;
			     if (ele) {
			        if(document.getElementById("scrollPosition")==null) return;
			     	document.getElementById("scrollPosition").value = ele.scrollTop;
			     }
			 }
			 function initPage()
			 {
				setDivPosition();
				if (typeof(init) == "function") eval("init()");
				if (typeof(initHash) == "function") eval("initHash()");
			 }
		
			
			function unloadMe(){
			  	if(win!=null) win.close();
			}
		</script>
<html:base />
</head>
<body onload="initPage()" onunload="unloadMe()">
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	height="100%">
	<tr height="60px">
		<td><tiles:insert name="Header_quatro.jsp">
		</tiles:insert></td>
	</tr>
	<tr valign="top" height="100%">
		<td>
		<table width="100%" height="100%">
			<tr>
				<td id="leftcol" width="200px"><tiles:insert
					attribute="leftNav" /></td>
				<td valign="top" width="3px"><img
					src='<html:rewrite page="/images/1x1.gif" />' width="3px" /></td>
				<td align="left"><!--  div class="body" align="left"  this is the layout-->
				<tiles:insert attribute="body" /> <!--  /div --></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>

</html:html>
