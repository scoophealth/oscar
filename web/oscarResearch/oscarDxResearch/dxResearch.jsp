<%--  
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
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%   
if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");

String user_no = (String) session.getAttribute("user");
String providerview = request.getParameter("providerview")==null?"":request.getParameter("providerview") ;
String asstProvider_no = "";
String color ="";
String service_form="";
%>

<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="../../errorpage.jsp" %>
<%@ include file="../../admin/dbconnection.jsp" %>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="dbDxResearch.jsp" %>
<html:html locale="true">
<head>
<title><bean:message key="oscarResearch.oscarDxResearch.dxResearch.title"/></title>
<script language="JavaScript">
<!--
function setfocus() {
	document.serviceform.xml_research1.focus();
	document.serviceform.xml_research1.select();
}

function RecordAttachments(Files, File0, File1, File2) {
	window.document.serviceform.elements["File0Data"].value = File0;
	window.document.serviceform.elements["File1Data"].value = File1;
	window.document.serviceform.elements["File2Data"].value = File2;
	window.document.all.Atts.innerText = Files;
}

var remote=null;

function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
			remote.opener = self;
	}
	if (x == 1) { return remote; }
}

function popPage(url) {
	awnd=rs('',url ,400,200,1);
	awnd.focus();
}

var awnd=null;

function ResearchScriptAttach() {
	t0 = escape(document.serviceform.xml_research1.value);
	t1 = escape(document.serviceform.xml_research2.value);
	t2 = escape(document.serviceform.xml_research3.value);
	t3 = escape(document.serviceform.xml_research4.value);
	t4 = escape(document.serviceform.xml_research5.value);

	awnd=rs('att','dxResearchCodeSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&name3=' + t3 + '&name4=' + t4 +'&search=',600,600,1);
	awnd.focus();
}

function submitform(form){
	document.serviceform.action = "dxResearchAdd.jsp"
	document.serviceform.submit()
}
//-->
</SCRIPT>
<style type="text/css">
	<!--
	BODY                  {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD                    {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000                                                    }
	TD.black              {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	TD.lilac              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.boldlilac          {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:hover      {font-weight: normal;                                                                            color: #000000; background-color: #CDCFFF  ;}
	TD.white              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.heading            {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	H2                    {font-weight: bold  ; font-size: 12pt; font-family: verdana,arial,helvetica; color: #000000; }
	H3                    {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H4                    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H6                    {font-weight: bold  ; font-size: 7pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	A:link                {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:visited             {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:hover               {                                                                            color: red; }
	TD.cost               {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	TD.black A:link       {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699;}
	TD.black A:visited    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699;}
	TD.black A:hover      {                                                                            color: #FDCB03; background-color: #666699;}
	TD.title              {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white 	      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #CDCFFF  ;}
	#navbar               {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	SPAN.navbar A:link    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	SPAN.navbar A:visited {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #EFEFEF; background-color: #666699   ;}
	SPAN.navbar A:hover   {                                                                            color: #FDCB03; background-color: #666699   ;}
	SPAN.bold             {font-weight: bold  ;                                                                                            background-color: #666699   ;}
	.sbttn {background: #EEEEFF;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}
	.mbttn {background: #D7DBF2;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}

	-->
</style>  
</head>

<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0" topmargin="5" marginwidth="0" marginheight="0" onLoad="setfocus()">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr bgcolor="#000000"> 
	<td height="40" colspan="2"> <font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF" size="5"><b> 
	&nbsp;&nbsp;&nbsp;<bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch"/></b></font></td>
</tr>
</table>

<form name=serviceform method=post>
<table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#EEEEFF" height="173">
<tr> 
	<td width="12%" valign="top"> 

	<table width="100%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#EEEEFF">
	<tr> 
		<td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgResearch"/> 
		</font></b></td>
		<td><b></b></td>
	</tr>
	<tr> 
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
		<input type="text" name="xml_research1" size="20"><input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>">
		</font></td>
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
	</tr>
	<tr> 
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
		<input type="text" name="xml_research2" size="20">
		</font></td>
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
	</tr>
	<tr> 
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
		<input type="text" name="xml_research3" size="20">
		</font></td>
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
	</tr>
	<tr> 
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
		<input type="text" name="xml_research4" size="20">
		</font></td>
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
	</tr>
	<tr> 
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
		<input type="text" name="xml_research5" size="20">
		</font></td>
		<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> </font></td>
	</tr>
	<tr> 
		<td colspan="2"> 
		<input type=button name=button value=code-search class=mbttn onClick=ResearchScriptAttach()>
		<input type="button" name="button" value="add" class=mbttn onClick=submitform(this.form)>
		</td>
	</tr>
	</table>

	</td>
	<td width="88%" valign="top">

	<table width="100%" border="0"  cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<tr bgcolor="#CCCCFF"> 
		<td width="32%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgDiagnosis"/></b></td>
		<td width="20%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgFirstVisit"/></b></td>
		<td width="20%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgLastVisit"/></b></td>
		<td width="28%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgAction"/></b></td>
	</tr>

<%
ResultSet rsdemo2 = null;
int count1 = 0;
int ctlCount = 0;
String[] param4=new String[1];
param4[0] = request.getParameter("demographicNo");

rsdemo2 = apptMainBean.queryResults(param4, "search_dxresearch_history");
while (rsdemo2.next()) {   
	count1++;
%>
	<tr bgcolor="<%=count1%2==0?"#FFFFFF":"#EEEEFF"%>"> 
		<td width="32%"><%=rsdemo2.getString("description")%></td>
		<td width="20%"><%=rsdemo2.getString("start_date")%></td>
		<td width="20%"><%=rsdemo2.getString("update_date")%></td>
		<td width="28%">
<% 
	if (rsdemo2.getString("status").compareTo("A")==0) { 
%>
<a href='dxResearchUpdate.jsp?status=C&did=" + rsdemo2.getString("dxresearch_no") + "&demographicNo=" +request.getParameter("demographicNo")+"'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnResolve"/></a> | <a href='dxResearchUpdate.jsp?status=D&did=" + rsdemo2.getString("dxresearch_no")+"&demographicNo=" +request.getParameter("demographicNo")+"'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnDelete"/></a>
<%	} else { %>
<bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnResolved"/>| <a href='dxResearchUpdate.jsp?status=D&did=" + rsdemo2.getString("dxresearch_no")+"&demographicNo=" +request.getParameter("demographicNo")+"'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnDelete"/></a></td>
<%	} %>
	</tr>      	  		 	   
<%}%>

	</table>

	</td>
</tr>
</table>
</form>

</body>
</html:html>
