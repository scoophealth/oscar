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

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="oscar.oscarEncounter.data.*, oscar.oscarEncounter.pageUtil.*"%>

<%
String demo = request.getParameter("de");
String proNo = request.getParameter("proNo");

if (demo != null ){
  EctSessionBean bean;
//  oscar.oscarSecurity.SessionBean bean;
  if ( (EctSessionBean ) request.getSession().getAttribute("EctSessionBean") != null){
    bean = (EctSessionBean ) request.getSession().getAttribute("EctSessionBean");
    bean.setDemographicNo(demo);
    bean.consultationRequestId = null;
    //if (proNo != null){
        //bean.providerNo = proNo;
        bean.providerNo =  (String) session.getAttribute("user");
    //}
  }else{
    bean = new EctSessionBean();
    bean.setDemographicNo(demo);
	  bean.consultationRequestId = null;
    //if (proNo != null){
    //    bean.providerNo = proNo;
    //}
    bean.providerNo =  (String) session.getAttribute("user");

    request.getSession().setAttribute("EctSessionBean",bean);
  }
}
%>

<html:html locale="true">
<head>
<html:base/>
<title><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.title"/></title>

<style type="text/css">
body{
    FONT-SIZE: 12px;
    FONT-FAMILY: Verdana, Tahoma, Arial, sans-serif;
}
td.messengerButtonsA{
    /*background-color: #6666ff;*/
    /*background-color: #6699cc;*/
    background-color: #003399;
}
td.messengerButtonsD{
    /*background-color: #84c0f4;*/
    background-color: #555599;
}
a.messengerButtons{
    color: #ffffff;
    font-size: 9pt;
    text-decoration: none;
}

table.messButtonsA{
border-top: 2px solid #cfcfcf;
border-left: 2px solid #cfcfcf;
border-bottom: 2px solid #333333;
border-right: 2px solid #333333;
}

table.messButtonsD{
border-top: 2px solid #333333;
border-left: 2px solid #333333;
border-bottom: 2px solid #cfcfcf;
border-right: 2px solid #cfcfcf;
}

td.Title{
font-size: 14px;
border-bottom: 2px solid #003399;
}

</style>

<script type="text/javascript">
function popupOscarConS(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.oscarConS"/>", windowprops);
  window.close();
}

function makeFocus(secs){
//  setTimeout("window.focus()",secs*1000);
}

function BackToOscar() {
       window.close();
}
</script>
</head>
<body   topmargin="0" marginheight="0">

<table width="100%">
<tr>
    <td class="Title">
        <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.msgTitle"/>
    </td>
</tr>
<tr>
    <td>
        &nbsp;
    </td>
</tr>
</table>


<table  cellspacing=3 >
    <tr>
        <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
           <a href=# onClick="popupOscarConS(700,960,'ConsultationFormRequest.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.btnNewCon"/></a>
            </td></tr></table>
        </td>
        <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
            <a href=# onClick="popupOscarConS(700,960,'DisplayDemographicConsultationRequests.jsp')" class="messengerButtons"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.btnOldCon"/></a>
            </td></tr></table>
        </td>

        <td >
            <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
            <a href="javascript:BackToOscar()" class="messengerButtons"><bean:message key="global.btnCancel"/></a>
            </td></tr></table>
        </td>
    </tr>
</table>

</body>
</html:html>
