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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String user_no = (String) session.getAttribute("user");
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
<%@ page import="java.util.*,java.text.*, oscar.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.common.model.TicklerComment" %>
<%@ page import="org.oscarehr.common.model.TicklerUpdate" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>

<%
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>


<%
	String labReqVer = oscar.OscarProperties.getInstance().getProperty("onare_labreqver","07");
if(labReqVer.equals("")) {labReqVer="07";}
%>

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
%>
<%
	//String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
  String ticklerview=request.getParameter("ticklerview")==null?"A":request.getParameter("ticklerview");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"8888-12-31":request.getParameter("xml_appointment_date");
%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
 <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
 <script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
 <script>
jQuery.noConflict();
</script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script>
jQuery(document).ready(function() {
	jQuery( "#note-form" ).dialog({
		autoOpen: false,
		height: 340,
		width: 450,
		modal: true,
		
		close: function() {
			
		}
	});		
		
});

function openNoteDialog(demographicNo, ticklerNo) {
	jQuery("#tickler_note_demographicNo").val(demographicNo);
	jQuery("#tickler_note_ticklerNo").val(ticklerNo);
	
	//is there an existing note?
	jQuery.ajax({url:'../CaseManagementEntry.do',
		data: { method: "ticklerGetNote", ticklerNo: jQuery('#tickler_note_ticklerNo').val()  },
		async:false, 
		dataType: 'json',
		success:function(data) {
			if(data != null) {
				jQuery("#tickler_note_noteId").val(data.noteId);
				jQuery("#tickler_note").val(data.note);
				jQuery("#tickler_note_revision").html(data.revision);
				jQuery("#tickler_note_revision_url").attr('onclick','window.open(\'../CaseManagementEntry.do?method=notehistory&noteId='+data.noteId+'\');return false;');
				jQuery("#tickler_note_editor").html(data.editor);
				jQuery("#tickler_note_obsDate").html(data.obsDate);
			}
		},
		error: function(jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
		});
	
	jQuery( "#note-form" ).dialog( "open" );
}
function closeNoteDialog() {
	jQuery( "#note-form" ).dialog( "close" );
}
function saveNoteDialog() {
	//alert('not yet implemented');
	jQuery.ajax({url:'../CaseManagementEntry.do',
		data: { method: "ticklerSaveNote", noteId: jQuery("#tickler_note_noteId").val(), value: jQuery('#tickler_note').val(), demographicNo: jQuery('#tickler_note_demographicNo').val(), ticklerNo: jQuery('#tickler_note_ticklerNo').val()  },
		async:false, 
		success:function(data) {
		 // alert('ok');		  
		},
		error: function(jqXHR, textStatus, errorThrown ) {
			alert(errorThrown);
		}
		});	
	
	jQuery( "#note-form" ).dialog( "close" );
}



</script>
  
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
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
  
  
function generateRenalLabReq(demographicNo) {
	var url = '<%=request.getContextPath()%>/form/formlabreq<%=labReqVer%>.jsp?demographic_no='+demographicNo+'&formId=0&provNo=<%=session.getAttribute("user")%>&fromSession=true';
	jQuery.ajax({url:'<%=request.getContextPath()%>/renal/Renal.do?method=createLabReq&demographicNo='+demographicNo,async:false, success:function(data) {
		popupPage(900,850,url);
	}});
}

</script>
<style type="text/css">
	<!--
	A, BODY, INPUT, OPTION ,SELECT , TABLE, TEXTAREA, TD, TR {font-family:tahoma,sans-serif; font-size:11px;}

	TD.black              {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	TD.lilac              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;  padding-top: 4px;padding-bottom: 4px;}
	TD.lilacRed              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;  padding-top: 4px;padding-bottom: 4px;}
        
	TD.boldlilac          {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF  ;}
	TD.lilac A:hover      {font-weight: normal;                                                                            color: #000000; background-color: #CDCFFF  ;}
        TD.lilac A:focus    {font-weight: bold; font-size: 8pt ; font-family: verdana,arial,helvetica; color: white; background-color: #666699  ;}
        
	TD.lilacRed A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;}
	TD.lilacRed A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #EEEEFF  ;}
	TD.lilacRed A:hover      {font-weight: normal;                                                                            color: red; background-color: #CDCFFF  ;}
        TD.lilacRed A:focus    {font-weight: bold; font-size: 8pt ; font-family: verdana,arial,helvetica; color: white; background-color: #666699  ;}
        
	TD.whiteRed              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;  padding-top: 4px;padding-bottom: 4px;}

	TD.white              {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;  padding-top: 4px;padding-bottom: 4px;}
	TD.heading            {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	H2                    {font-weight: bold  ; font-size: 12pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H3                    {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H4                    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	H6                    {font-weight: bold  ; font-size: 7pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	A:link                {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:visited             {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #336666; }
	A:hover               {                                                                            color: red; background-color: #CDCFFF  ;}
	TD.cost               {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
	TD.black A:link       {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #FFFFFF;}
	TD.black A:visited    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #FFFFFF;}
	TD.black A:hover      {                                                                            color: #FDCB03; background-color: #FFFFFF;}
        TD.title              {font-weight: bold  ; font-size: 10pt; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
        
        TD.white A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
	TD.white A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #CDCFFF  ;}
        TD.white A:focus    {font-weight: bold; font-size: 8pt ; font-family: verdana,arial,helvetica; color: white; background-color: #666699  ;}
	
        TD.whiteRed A:link       {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
		TD.whiteRed A:visited    {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #FFFFFF;}
		TD.whiteRed A:hover      {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: red; background-color: #CDCFFF  ;}
        TD.whiteRed A:focus    {font-weight: bold; font-size: 8pt ; font-family: verdana,arial,helvetica; color: white; background-color: #666699  ;}
	#navbar               {                     font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FDCB03; background-color: #666699   ;}
	SPAN.navbar A:link    {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #FFFFFF; background-color: #666699   ;}
	SPAN.navbar A:visited {font-weight: bold  ; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #EFEFEF; background-color: #666699   ;}
	SPAN.navbar A:hover   {                                                                            color: #FDCB03; background-color: #666699   ;}
	SPAN.bold             {font-weight: bold  ;                                                                                            background-color: #666699   ;}
	.sbttn {background: #EEEEFF;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}
.mbttn {background: #D7DBF2;border-bottom: 1px solid #104A7B;border-right: 1px solid #104A7B;border-left: 1px solid #AFC4D5;border-top:1px solid #AFC4D5;color:#000066;height:19px;text-decoration:none;cursor: hand}

	-->
</style>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" type="text/css" media="all" href="../css/print.css"  />

</head>
<oscar:customInterface section="ticklerMain"/>
<body onload="setup();" bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10">


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%" class="noprint"><input type='button' name='print'
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
<form name="serviceform" method="get" action="ticklerDemoMain.jsp">
<table width="100%" border="0" bgcolor="#EEEEFF">
	<tr class="noprint">
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
			value=""> <input type="hidden" name="parentAjaxId"
			value="<%=parentAjaxId%>"> <input type="submit"
			value="<bean:message key="tickler.ticklerDemoMain.btnCreateReport"/>"
			class="mbttn"
			onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
		</div>
		</td>
	</tr>
</table>
	</form>
	
	<form name="ticklerform" method="post" action="dbTicklerDemoMain.jsp">
<table bgcolor=#666699 border=0 cellspacing=0 width=100%>
	
	<tr>
		<td><input type="hidden" name="demoview" value="<%=demoview%>">
		<input type="hidden" name="parentAjaxId" value="<%=parentAjaxId%>">
		<input type="hidden" name="updateParent" value="true">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<TR bgcolor=#666699>
				<TD width="3%" class="noprint"><FONT FACE="verdana,arial,helvetica"
					COLOR="#FFFFFF" SIZE="-2"><B></B></FONT></TD>
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
				<td COLOR="#FFFFFF" SIZE="-2" class="noprint">&nbsp;</td>
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

				List<Tickler> ticklers = ticklerManager.search_tickler_bydemo(loggedInInfo, request.getParameter("demoview")==null?null: Integer.parseInt(request.getParameter("demoview")),ticklerview,ConversionUtils.fromDateString(dateBegin),ConversionUtils.fromDateString(dateEnd));
				String rowColour = "lilac";
				for (Tickler t:ticklers) {
				    Demographic d = demographicDao.getDemographicById(t.getDemographicNo());
				    Provider p = null;
				    Provider assignedP = null;
					String providerNumber = d.getProviderNo();
				    
				    if(d != null && providerNumber != null && ! providerNumber.isEmpty()) {
				            p = providerDao.getProvider(providerNumber);
				    }
				    if(t.getTaskAssignedTo().length()>0) {
				            assignedP = providerDao.getProvider(t.getTaskAssignedTo());
				    }

				    nItems = nItems +1; 
				    
				    if (p == null){
				      provider = "";
				    } else {
				      provider = p.getFormattedName();
				    }
				    
				    if (assignedP == null){
				      taskAssignedTo = "";
				    }
				    else{
				      taskAssignedTo = assignedP.getFormattedName();
				    }

				    vGrantdate = t.getServiceDate() + " 00:00:00.0";
				    java.util.Date grantdate = dateFormat.parse(vGrantdate);
				    java.util.Date toDate = new java.util.Date();
				    long millisDifference = toDate.getTime() - grantdate.getTime();

				    long ONE_DAY_IN_MS = (1000 * 60 * 60 * 24);                                                      
				    long daysDifference = millisDifference / (ONE_DAY_IN_MS);

				    String numDaysUntilWarn = OscarProperties.getInstance().getProperty("tickler_warn_period");
				    long ticklerWarnDays = Long.parseLong(numDaysUntilWarn);
				    boolean ignoreWarning = (ticklerWarnDays < 0);


				    //Set the colour of the table cell 
				    String warnColour = "";
				    if (!ignoreWarning && (daysDifference >= ticklerWarnDays)){
				       warnColour = "Red";
				    }

				    if (rowColour.equals("lilac")){
				       rowColour = "white";
				    }else {
				       rowColour = "lilac";
				    }

				    String cellColour = rowColour + warnColour;
			%>

			<tr>
				<TD ROWSPAN="1" class="<%=cellColour%> noprint"><input
					type="checkbox" name="checkbox"
					value="<%=t.getId()%>"></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><a
					href=#
					onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=t.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')"><%=d.getLastName()%>,<%=d.getFirstName()%></a></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><%=provider%></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>">
				<%
					java.util.Date service_date = t.getServiceDate();
						SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
						String service_date_str = dateFormat2.format(service_date);
						out.print(service_date_str);
				%>
				</TD>
				<TD ROWSPAN="1" class="<%=cellColour%>">
				<%
					service_date = t.getUpdateDate();
						dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
						service_date_str = dateFormat2.format(service_date);
						out.print(service_date_str);
				%>
				</TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getPriority()%></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><%=taskAssignedTo%></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><%=String.valueOf(t.getStatus()).equals("A")?"Active":String.valueOf(t.getStatus()).equals("C")?"Completed":String.valueOf(t.getStatus()).equals("D")?"Deleted":String.valueOf(t.getStatus())%></TD>
				<TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getMessage()%></TD>
				  <td ROWSPAN="1" class="<%=cellColour%> noprint">
                	<a href="javascript:void(0)" onClick="openNoteDialog('<%=t.getDemographicNo() %>','<%=t.getId() %>');return false;">
                		<img border="0" src="<%=request.getContextPath()%>/images/notepad.gif"/>
                	</a>
                </td>
			</tr>
        <%
        	boolean ticklerEditEnabled = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("tickler_edit_enabled")); 
                        Set<TicklerComment> tcomments = t.getComments();
                        if (ticklerEditEnabled && !tcomments.isEmpty()) {
                            for (TicklerComment tc : tcomments) {
        %>
                        <tr>
                            <td width="3%" ROWSPAN="1" class="<%=cellColour%>"></td>
                            <td width="12%" ROWSPAN="1" class="<%=cellColour%>"></td>
                            <td ROWSPAN="1" class="<%=cellColour%>"><%=tc.getProvider().getLastName()%>,<%=tc.getProvider().getFirstName()%></td>
                            <td ROWSPAN="1" class="<%=cellColour%>"></td>
                            <% if (tc.isUpdateDateToday()) { %>
                            <td ROWSPAN="1" class="<%=cellColour%>"><%=tc.getUpdateTime(request.getLocale())%></td>
                            <% } else { %>
                            <td ROWSPAN="1" class="<%=cellColour%>"><%=tc.getUpdateDate(request.getLocale())%></td>
                            <% } %>
                            <td ROWSPAN="1" class="<%=cellColour%>"></td>
                            <td ROWSPAN="1" class="<%=cellColour%>"></td>
                            <td ROWSPAN="1" class="<%=cellColour%>"></td>
                            <td ROWSPAN="1" class="<%=cellColour%>"><%=tc.getMessage()%></td>
                        </tr>
       <%           }
                }  
}                                                     
                                    
if (nItems == 0) {
%>
			<tr>
				<td colspan="8" class="white"><bean:message
					key="tickler.ticklerDemoMain.msgNoMessages" /></td>
			</tr>
			<%}
			Demographic d = demographicDao.getDemographicById(request.getParameter("demoview")==null?null: Integer.parseInt(request.getParameter("demoview")));
			%>
			<tr bgcolor=#FFFFFF class="noprint">
				<td colspan="10" class="white"><a id="checkAllLink" name="checkAllLink" href="javascript:CheckAll();"><bean:message
					key="tickler.ticklerDemoMain.btnCheckAll" /></a> - <a
					href="javascript:ClearAll();"><bean:message
					key="tickler.ticklerDemoMain.btnClearAll" /></a> &nbsp; &nbsp; &nbsp;
				&nbsp; &nbsp; <input type="button" name="button"
					value="<bean:message key="tickler.ticklerDemoMain.btnAddTickler"/>"
					onClick="popupPage('400','600', 'ticklerAdd.jsp?updateParent=true&parentAjaxId=<%=parentAjaxId%>&bFirstDisp=false&messageID=null&demographic_no=<%=d.getDemographicNo()%>&chart_no=<%=d.getChartNo()%>&name=<%=d.getDisplayName()%>')"
					class="sbttn"> <input type="hidden" name="submit_form"
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
</table>
	</form>

<div id="note-form" title="Tickler Note">
	<form>
		
			<table>
				<tbody>
					<textarea id="tickler_note" name="tickler_note" style="width:100%;height:80%"></textarea>		
					<input type="hidden" name="tickler_note_demographicNo" id="tickler_note_demographicNo" value=""/>	
					<input type="hidden" name="tickler_note_ticklerNo" id="tickler_note_ticklerNo" value=""/>	
					<input type="hidden" name="tickler_note_noteId" id="tickler_note_noteId" value=""/>	
				</tbody>
			</table>
			<br/>
			<table>
				<tr>
					<td >
						<a href="javascript:void()" onclick="saveNoteDialog();return false;">
							<img src="<%=request.getContextPath()%>/oscarEncounter/graphics/note-save.png"/>
						</a>
						<a href="javascript:void()" onclick="closeNoteDialog();return false;">
							<img src="<%=request.getContextPath()%>/oscarEncounter/graphics/system-log-out.png"/>
						</a>
					</td>
					<td style="width:40%" nowrap="nowrap">
					Date: <span id="tickler_note_obsDate"></span> rev <a id="tickler_note_revision_url" href="javascript:void(0)" onClick=""><span id="tickler_note_revision"></span></a><br/>
					Editor: <span id="tickler_note_editor"></span>
					</td>
				</tr>
			
			</table>
			
	</form>	
</div>


<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
<p class="noprint">
<%@ include file="../demographic/zfooterbackclose.jsp"%>
</p>


</body>
</html:html>
