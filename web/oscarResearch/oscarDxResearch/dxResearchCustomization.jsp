<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
 <%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title>
dxResearch - Customize Disease Registry Quick List
</title>
<style type="text/css">
body{
    FONT-SIZE: 12px;
    FONT-FAMILY: Verdana, Tahoma, Arial, sans-serif;
}
</style>
<html:base/>
<script type="text/javascript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function setfocus(){
    window.focus();
    window.resizeTo(450,150);
}
</script>
</head>

<link rel="stylesheet" type="text/css" href="../../oscarEncounter/styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="setfocus();">
<html:errors/>
<table>
    <tr>
    <td class=Title colspan="2">Customize Disease Registry Quick List</td>
    </tr>
    <tr>
        <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="200">
            <a href=# onClick="popupPage(300,1000,'dxResearchNewQuickList.jsp')" class="messengerButtons">Add New Quick List</a>
            </td></tr></table>
        </td>
        <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA" width="200">
            <a href=# onClick="popupPage(300,1000,'dxResearchLoadQuickList.do')" class="messengerButtons">Edit Exisiting Quick List</a>
            </td></tr></table>
        </td>
    </tr>
    <tr><td></td></tr>
    <tr>
       <td>
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
            <a href="javascript:window.close()" class="messengerButtons"><bean:message key="global.btnClose"/></a>
            </td></tr></table>
        </td>
    </tr>
</table>
</body>
</html:html>
                             
                                   
                                
