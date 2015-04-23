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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
     String strLimit1="0";
    String strLimit2="5"; 
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String demoview = request.getParameter("demoview")==null?"all":request.getParameter("demoview") ;
  
//Retrieve encounter id for updating encounter navbar if info this page changes anything
String parentAjaxId;
if( request.getParameter("parentAjaxId") != null )
    parentAjaxId = request.getParameter("parentAjaxId");
else if( request.getAttribute("parentAjaxId") != null )
    parentAjaxId = (String)request.getAttribute("parentAjaxId");
else
    parentAjaxId = "";
    
String updateParent;
if( request.getParameter("updateParent") != null )
    updateParent = request.getParameter("updateParent");
else
    updateParent = "false";    
%>
<%@ page
	import="java.util.*, java.sql.*,java.text.*, oscar.*, java.net.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbTicker.jspf"%>

<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  
  
  %>
<% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
  String ticklerview=request.getParameter("ticklerview")==null?"A":request.getParameter("ticklerview");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"8888-12-31":request.getParameter("xml_appointment_date");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="tickler.ticklerDemoMain.title" /></title>
<script language="JavaScript">
<!--


function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}

function refresh() {
  var u = self.location.href;  
  //if(u.lastIndexOf("view=1") > 0) {
  //  self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  //} else {
    self.location.reload();
  //}
}
//-->
</script>
<script>
   
    function Check(e)
    {
	e.checked = true;
	//Highlight(e);
    }

    function Clear(e)
    {
	e.checked = false;
	//Unhighlight(e);
    }

    function CheckAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Check(e);
	    }
	}
	//ml.toggleAll.checked = true;
    }

    function ClearAll()
    {
	var ml = document.ticklerform;
	var len = ml.elements.length;
	for (var i = 0; i < len; i++) {
	    var e = ml.elements[i];
	    if (e.name == "checkbox") {
		Clear(e);
	    }
	}
	//ml.toggleAll.checked = false;
    }

    function Highlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnew") {
		r.className = "msgnews";
	    }
	    else if (r.className == "msgold") {
		r.className = "msgolds";
	    }
	}
    }

    function Unhighlight(e)
    {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
	    r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
	    r = e.parentElement.parentElement;
	}
	if (r) {
	    if (r.className == "msgnews") {
		r.className = "msgnew";
	    }
	    else if (r.className == "msgolds") {
		r.className = "msgold";
	    }
	}
    }

    function AllChecked()
    {
	ml = document.messageList;
	len = ml.elements.length;
	for(var i = 0 ; i < len ; i++) {
	    if (ml.elements[i].name == "Mid" && !ml.elements[i].checked) {
		return false;
	    }
	}
	return true;
    }

    function Delete()
    {
	var ml=document.messageList;
	ml.DEL.value = "1"; 
	ml.submit();
    }

    function SynchMoves(which) {
	var ml=document.messageList;
	if(which==1) {
	    ml.destBox2.selectedIndex=ml.destBox.selectedIndex;
	}
	else {
	    ml.destBox.selectedIndex=ml.destBox2.selectedIndex;
	}
    }

    function SynchFlags(which)
    {
	var ml=document.messageList;
	if (which == 1) {
	    ml.flags2.selectedIndex = ml.flags.selectedIndex;
	}
	else {
	    ml.flags.selectedIndex = ml.flags2.selectedIndex;
	}
    }

    function SetFlags()
    {
	var ml = document.messageList;
	ml.FLG.value = "1";
	ml.submit();
    }

    function Move() {
	var ml = document.messageList;
	var dbox = ml.destBox;
	if(dbox.options[dbox.selectedIndex].value == "@NEW") {
	    nn = window.prompt("<bean:message key="tickler.ticklerDemoMain.msgFolderName"/>","");
	    if(nn == null || nn == "null" || nn == "") {
		dbox.selectedIndex = 0;
		ml.destBox2.selectedIndex = 0;
	    }
	    else {
		ml.NewFol.value = nn;
		ml.MOV.value = "1";
		ml.submit();
	    }
	}
	else {
	    ml.MOV.value = "1";
	    ml.submit();
	}
    }
    
    function allYear()
    {       
    var newD = "8888-12-31";
    var beginD = "1900-01-01"
    	document.serviceform.xml_appointment_date.value = newD;
    		document.serviceform.xml_vdate.value = beginD;
}

function setup() {
    
    var parentId = "<%=parentAjaxId%>";
    var Url = window.opener.URLs;
    var update = "<%=updateParent%>";    
    
    if( update == "true" && parentId != "" && !window.opener.closed ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value=parentId;
        window.opener.updateNeeded = true;
    }
    else if( update == "true" && parentId == "" && !window.opener.closed )
        window.opener.location.reload();
  }

</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body onload="setup();" bgcolor="#FFFFFF" text="#000000" leftmargin="0"
	rightmargin="0" topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<td height="10" width="70%"></td>
		<td height="10" width="30%" align=right></td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"><input type='button' name='print'
			value=<bean:message key="global.btnPrint"/>
			' onClick='window.print()' class="sbttn"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4"><bean:message
			key="tickler.ticklerDemoMain.msgTitle" /></font></b></font></p>
		</td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="get" action="ticklerDemoMain.jsp">
	<tr>
		<td width="20%">
		<div align="right"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message
			key="tickler.ticklerDemoMain.formMoveTo" /> </b> <select
			name="ticklerview">
			<option value="A" <%=ticklerview.equals("A")?"selected":""%>><bean:message
				key="tickler.ticklerDemoMain.formActive" /></option>
			<option value="C" <%=ticklerview.equals("C")?"selected":""%>><bean:message
				key="tickler.ticklerDemoMain.formCompleted" /></option>
			<option value="D" <%=ticklerview.equals("D")?"selected":""%>><bean:message
				key="tickler.ticklerDemoMain.formDeleted" /></option>
		</select> </font></div>
		</td>
		<td width="30%">
		<div align="center"><input type="text" name="xml_vdate"
			value="<%=xml_vdate%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"> <a href="#"
			onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message
			key="tickler.ticklerDemoMain.btnBegin" />:</a></font></div>
		</td>
		<td width="30%"><input type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message
			key="tickler.ticklerDemoMain.btnEnd" />:</a></font></td>
		<td width="20%">
		<div align="right"><input type="hidden" name="demoview"
			value="<%=demoview%>"> <input type="hidden" name="Submit"
			value=""> <input type="hiden" name="parentAjaxId"
			value="<%=parentAjaxId%>"> <input type="submit"
			value="<bean:message key="tickler.ticklerDemoMain.btnCreateReport"/>"
			class="mbttn"
			onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
		</div>
		</td>
	</tr>
	</form>
</table>

<table bgcolor=#666699 border=0 cellspacing=0 width=100%>
	<form name="ticklerform" method="post" action="dbTicklerDemoMain.jsp">
	<tr>
		<td><input type="hidden" name="demoview" value="<%=demoview%>">
		<input type="hiden" name="parentAjaxId" value="<%=parentAjaxId%>">
		<input type="hidden" name="updateParent" value="true">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<TR bgcolor=#666699>
				<TD width="3%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B></B></FONT></TD>
				<% 
            boolean ticklerEditEnabled = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("tickler_edit_enabled")); 
            if (ticklerEditEnabled) { %>
            <TD width="5%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B></B></FONT></TD>
            <% }%>
				<TD width="17%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgDemographicName" /></B></FONT></TD>
				<TD width="8%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgDoctorName" /></B></FONT></TD>
				<TD width="9%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgDate" /></B></FONT></TD>
				<TD width="9%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgCreationDate" /></B></FONT></TD>
				<TD width="6%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.Priority" /></B></FONT></TD>
				<TD width="12%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.taskAssignedTo" /></B></FONT></TD>

				<TD width="6%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgStatus" /></B></FONT></TD>
				<TD width="39%"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B><bean:message
					key="tickler.ticklerMain.msgMessage" /></B></FONT></TD>
			</TR>
			<%
String vGrantdate = "1980-01-07 00:00:00.0";
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm.SSS", request.getLocale()); 
 

  String dateBegin = xml_vdate;
  String dateEnd = xml_appointment_date;
  String provider = "";
  String taskAssignedTo = "";
  
  if (dateEnd.compareTo("") == 0) dateEnd = "8888-12-31";
  if (dateBegin.compareTo("") == 0) dateBegin="1900-01-01";
  ResultSet rs=null ;
  String[] param =new String[4];
  boolean bodd=false;
  param[0] = ticklerview;
 
  param[1] = dateBegin;
  param[2] = dateEnd;
  param[3] = request.getParameter("demoview")==null?"": request.getParameter("demoview");
  rs = apptMainBean.queryResults(param, "search_tickler_bydemo");
  while (rs.next()) {
     nItems = nItems +1;
     bodd=bodd?false:true;
     if (apptMainBean.getString(rs,"provider_last")==null || apptMainBean.getString(rs,"provider_first")==null){
        provider = "";
     }
     else{
        provider = apptMainBean.getString(rs,"provider_last") + "," + apptMainBean.getString(rs,"provider_first");
     }
     if (apptMainBean.getString(rs,"assignedLast")==null || apptMainBean.getString(rs,"assignedFirst")==null){
        taskAssignedTo = "";
     }
     else{
        taskAssignedTo = apptMainBean.getString(rs,"assignedLast") + ", " + apptMainBean.getString(rs,"assignedFirst");
     }
     java.util.Date grantdate =  rs.getDate("service_date");  
     java.util.Date toDate = new java.util.Date(); 
     long millisDifference = toDate.getTime() - grantdate.getTime(); 
     long daysDifference = millisDifference / (1000 * 60 * 60 * 24); 
     if (daysDifference > 0){
%>

			<tr>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><input
					type="checkbox" name="checkbox"
					value="<%=apptMainBean.getString(rs,"tickler_no")%>"></TD>
				<% if (ticklerEditEnabled) { %>
            <TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>">
            	<a href=#  onClick="popupPage(600,800, '../tickler/ticklerEdit.jsp?tickler_no=<%=apptMainBean.getString(rs,"tickler_no")%>')"><bean:message key="tickler.ticklerMain.editTickler"/></a>
            </TD>
            <% }%>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><a
					href=#
					onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&displaymode=edit&dboperation=search_detail')"><%=apptMainBean.getString(rs,"last_name")%>,<%=apptMainBean.getString(rs,"first_name")%></a></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=provider%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>">
				<%
		java.util.Date service_date = rs.getDate("service_date");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String service_date_str = dateFormat2.format(service_date);
		out.print(service_date_str);
	%>
				</TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>">
				<%
		service_date = rs.getDate("update_date");
		dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		service_date_str = dateFormat2.format(service_date);
		out.print(service_date_str);
	%>
				</TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=apptMainBean.getString(rs,"priority")%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=taskAssignedTo%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=apptMainBean.getString(rs,"status").equals("A")?"Active":apptMainBean.getString(rs,"status").equals("C")?"Completed":apptMainBean.getString(rs,"status").equals("D")?"Deleted":apptMainBean.getString(rs,"status")%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>"><%=apptMainBean.getString(rs,"message")%></TD>
			</tr>
			<%
}else {
%>
			<tr>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><input
					type="checkbox" name="checkbox"
					value="<%=apptMainBean.getString(rs,"tickler_no")%>"></TD>
					<% if (ticklerEditEnabled) { %>
            <TD ROWSPAN="1" class="<%=bodd?"lilacRed":"whiteRed"%>">
            	<a href=#  onClick="popupPage(600,800, '../tickler/ticklerEdit.jsp?tickler_no=<%=apptMainBean.getString(rs,"tickler_no")%>')"><bean:message key="tickler.ticklerMain.editTickler"/></a>
            </TD>
            <% }%>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><a href=#
					onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&displaymode=edit&dboperation=search_detail')"><%=apptMainBean.getString(rs,"last_name")%>,<%=apptMainBean.getString(rs,"first_name")%></a></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=provider%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>">
				<%
		java.util.Date service_date = rs.getDate("service_date");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String service_date_str = dateFormat2.format(service_date);
		out.print(service_date_str);
	%>
				</TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>">
				<%
		service_date = rs.getDate("update_date");
		dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		service_date_str = dateFormat2.format(service_date);
		out.print(service_date_str);
	%>
				</TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=apptMainBean.getString(rs,"priority")%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=taskAssignedTo%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=apptMainBean.getString(rs,"status").equals("A")?"Active":apptMainBean.getString(rs,"status").equals("C")?"Completed":apptMainBean.getString(rs,"status").equals("D")?"Deleted":apptMainBean.getString(rs,"status")%></TD>
				<TD ROWSPAN="1" class="<%=bodd?"lilac":"white"%>"><%=apptMainBean.getString(rs,"message")%></TD>
			</tr>
			<%
}

%>

			<%}

if (nItems == 0) {
%>
			<tr>
				<td colspan="8" class="white"><bean:message
					key="tickler.ticklerDemoMain.msgNoMessages" /></td>
			</tr>
			<%}
			rs = apptMainBean.queryResults(demoview, "search_demographic_details");
			while (rs.next()) {
			%>
			<tr bgcolor=#666699>
				<td colspan="8" class="white"><a href="javascript:CheckAll();"><bean:message
					key="tickler.ticklerDemoMain.btnCheckAll" /></a> - <a
					href="javascript:ClearAll();"><bean:message
					key="tickler.ticklerDemoMain.btnClearAll" /></a> &nbsp; &nbsp; &nbsp;
				&nbsp; &nbsp; <input type="button" name="button"
					value="<bean:message key="tickler.ticklerDemoMain.btnAddTickler"/>"
					onClick="popupPage('400','600', 'ticklerAdd.jsp?updateParent=true&parentAjaxId=<%=parentAjaxId%>&bFirstDisp=false&messageID=null&demographic_no=<%=apptMainBean.getString(rs,"demographic_no")%>&chart_no=<%=apptMainBean.getString(rs,"chart_no")%>&name=<%=apptMainBean.getString(rs,"last_name")%>,<%=apptMainBean.getString(rs,"first_name")%>')"
					class="sbttn"> <%}%>
					<input type="hidden" name="submit_form"
					value=""> <% if (ticklerview.compareTo("D") == 0){%> <input
					type="button"
					value="<bean:message key="tickler.ticklerDemoMain.btnErase"/>"
					class="sbttn"
					onclick="document.forms['ticklerform'].submit_form.value='Erase Completely'; document.forms['ticklerform'].submit();">
				<%} else{%> <input type="button"
					value="<bean:message key="tickler.ticklerDemoMain.btnComplete"/>"
					class="sbttn"
					onclick="document.forms['ticklerform'].submit_form.value='Complete'; document.forms['ticklerform'].submit();">
				<input type="button"
					value="<bean:message key="tickler.ticklerDemoMain.btnDelete"/>"
					class="sbttn"
					onclick="document.forms['ticklerform'].submit_form.value='Delete'; document.forms['ticklerform'].submit();">
				<%}%> <input type="button" name="button"
					value="<bean:message key="global.btnCancel"/>"
					onClick="window.close()" class="sbttn"></td>
			</tr>
		</table>
		</td>
	</tr>
	</form>
</table>
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
<p>&nbsp;</p>
<%@ include file="../demographic/zfooterbackclose.jsp"%>

</body>
</html:html>
