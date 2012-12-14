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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.common.model.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,oscar.oscarLab.ca.on.*"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tasks" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../noRights.html");%>
</security:oscarSec>

<%@ page import="java.util.*,java.text.*, java.sql.*, oscar.*, java.net.*, org.oscarehr.common.dao.*, org.caisi.model.Tickler, org.caisi.model.TicklerComment,org.caisi.dao.TicklerDAO,org.caisi.model.CustomFilter,org.oscarehr.util.SpringUtils, org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="java.util.*,java.text.*, java.sql.*, oscar.*, java.net.*, org.oscarehr.common.dao.ViewDao, org.oscarehr.common.model.View, org.springframework.web.context.WebApplicationContext, org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.util.LocaleUtils" %>


<head>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
    <script>
        jQuery.noConflict();
    </script>
</head>

 <%
 //select * from tickler where status = 'A' and service_date <= now() and task_assigned_to  = '999998' limit 1;
  if(session.getAttribute("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
  String strLimit1="0";
  String strLimit2="5";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");


  WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
  ViewDao viewDao =  (ViewDao) ctx.getBean("viewDao");
  TicklerLinkDao ticklerLinkDao = (TicklerLinkDao) ctx.getBean("ticklerLinkDao");

  String role = (String)request.getSession().getAttribute("userrole");
  Map<String,View> viewMap = viewDao.getView(View.TICKLER_VIEW,role);
  View v;
  String providerview;
  String assignedTo;
  String mrpview;
  if( request.getParameter("providerview")==null ) {
          providerview = "all";
  }
  else {
      providerview = request.getParameter("providerview");
  }

  if( request.getParameter("assignedTo") == null ) {
          assignedTo = "all";
  }
  else {
      assignedTo = request.getParameter("assignedTo");
  }
  
  if( request.getParameter("mrpview") == null ) {
	  mrpview = "all";
  }
  else {
	  mrpview = request.getParameter("mrpview");
  }

%>

<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  %>
<%
 String ticklerview;
   if( request.getParameter("ticklerview") == null ) {
          ticklerview = "A";
  }
  else {
      ticklerview = request.getParameter("ticklerview");
  }


  String xml_vdate;
   if( request.getParameter("xml_vdate") == null ) {
          xml_vdate = "";
  }
  else {
      xml_vdate = request.getParameter("xml_vdate");
  }

  String xml_appointment_date;
   if( request.getParameter("xml_appointment_date") == null ) {
          xml_appointment_date = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
  }
  else {
      xml_appointment_date = request.getParameter("xml_appointment_date");
  }


  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

  String stActive = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stActive");
  String stComplete = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stComplete");
  String stDeleted = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stDeleted");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<%@page import="org.oscarehr.util.DbConnectionFilter"%>
<html:html locale="true">
<head>
<title><bean:message key="tickler.ticklerMain.title"/></title>

<link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/css/print.css" />

<!--Table Sorting Code -->
<script type='text/javascript' src='<c:out value="${ctx}"/>/commons/scripts/sort_table/common.js'></script>
<script type='text/javascript' src='<c:out value="${ctx}"/>/commons/scripts/sort_table/css.js'></script>
<script type='text/javascript' src='<c:out value="${ctx}"/>/commons/scripts/sort_table/standardista-table-sorting.js'></script>

<!-- Prototype and scriptaculous -->
<script src="<c:out value="${ctx}"/>/share/javascript/prototype.js" type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/scriptaculous.js" type="text/javascript"></script>

<script language="JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}



function allYear()
{
var newD = "8888-12-31";
var beginD = "1900-01-01"
	document.serviceform.xml_appointment_date.value = newD;
		document.serviceform.xml_vdate.value = beginD;
}

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

    function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
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
	    nn = window.prompt("<bean:message key="tickler.ticklerMain.msgFolderName"/>","");
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

    function saveView() {

        var url = "<c:out value="${ctx}"/>/saveWorkView.do";
        var role = "<%=(String)session.getAttribute("userrole")%>";
        var params = "method=save&view_name=tickler&userrole=" + role + "&name=ticklerview&value=" + $F("ticklerview") + "&name=dateBegin&value=" + $F("xml_vdate") + "&name=dateEnd&value=" + $F("xml_appointment_date") + "&name=providerview&value=" + encodeURI($F("providerview")) + "&name=assignedTo&value=" + encodeURI($F("assignedTo"));
        var sortables = document.getElementsByClassName('tableSortArrow');

        var attrib = null;
        var columnId = -1;
        for( var idx = 0; idx < sortables.length; ++idx ) {
            attrib = sortables[idx].readAttribute("sortOrder");
            if( attrib != null ) {
                columnId = sortables[idx].previous().readAttribute("columnId");
                break;
            }
        }

        if( columnId != -1 ) {
            params += "&name=columnId&value=" + columnId + "&name=sortOrder&value=" + attrib;
        }

        //console.log(params);
        new Ajax.Request (
            url,
            {
                method: "post",
                postBody: params,
                onSuccess: function(response) {
                    alert("View Saved");
                },
                onFailure: function(request) {
                    alert("View not saved! " + request.status);
                }
            }
        );

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
</head>

<oscar:customInterface section="ticklerMain"/>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#FFFFFF">
    <td height="10" width="70%"> </td>
    <td height="10" width="30%" align=right></td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000">
    <td height="40" width="10%"><input type='button' name='print' value='<bean:message key="global.btnPrint"/>' onClick='window.print()' class="sbttn"></td>
    <td width="90%" align="left">
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4"><bean:message key="tickler.ticklerMain.msgTickler"/></font></b></font>
      </p>
    </td>
  </tr>        
</table>
             
<form name="serviceform" method="get" action="ticklerMain.jsp">
<table width="100%" border="0" bgcolor="#EEEEFF">  
    <tr>
      <td width="19%">
        <div align="right"> <font color="#003366"><font face="Arial, Helvetica, sans-serif" size="2"><b>
          <font color="#333333"><bean:message key="tickler.ticklerMain.formDateRange"/></font></b></font></font> </div>
      </td>
      <td width="41%">
        <div align="center">
          <input type="text" id="xml_vdate" name="xml_vdate" value="<%=xml_vdate%>">
          <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerMain.btnBegin"/>:</a></font>
        </div>
      </td>
      <td width="40%">
        <input type="text" id="xml_appointment_date" name="xml_appointment_date" value="<%=xml_appointment_date%>">
        <font size="1" face="Arial, Helvetica, sans-serif"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerMain.btnEnd"/>:</a> &nbsp; &nbsp; <a href="#" onClick="allYear()"><bean:message key="tickler.ticklerMain.btnViewAll"/></a></font>
      </td>
    </tr>
    <tr>

        <td colspan="3">
        <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message key="tickler.ticklerMain.formMoveTo"/> </b>
        <select id="ticklerview" name="ticklerview">
        <option value="A" <%=ticklerview.equals("A")?"selected":""%>><bean:message key="tickler.ticklerMain.formActive"/></option>
        <option value="C" <%=ticklerview.equals("C")?"selected":""%>><bean:message key="tickler.ticklerMain.formCompleted"/></option>
        <option value="D" <%=ticklerview.equals("D")?"selected":""%>><bean:message key="tickler.ticklerMain.formDeleted"/></option>                   
        </select>
        &nbsp; &nbsp; <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b>MRP</b></font> 
        <select id="mrpview" name="mrpview">
        <option value="all" <%=mrpview.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
        <%
        ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
        List<Provider> providers = providerDao.getActiveProviders(); 
        for (Provider p : providers) {  
        %>
        <option value="<%=p.getProviderNo()%>" <%=mrpview.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>,<%=p.getFirstName()%></option>
        <%
            }

        %>
          </select>
        
        &nbsp; &nbsp; <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message key="tickler.ticklerMain.msgCreator"/> </b></font>

        <select id="providerview" name="providerview">
        <option value="all" <%=providerview.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
        <%  
            
            for (Provider p : providers) {              
        %>
        <option value="<%=p.getProviderNo()%>" <%=providerview.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>,<%=p.getFirstName()%></option>
        <%
            }

        %>
          </select>


          <!-- -->
          &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333"><b><bean:message key="tickler.ticklerMain.msgAssignedTo"/></b></font>
<% if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
{ // multisite start ==========================================
        	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo(user_no);
      %>
      <script>
var _providers = [];
<%	for (int i=0; i<sites.size(); i++) { %>
	_providers["<%= sites.get(i).getSiteId() %>"]="<% Iterator<Provider> iter = sites.get(i).getProviders().iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if ("1".equals(p.getStatus())) {
	%><option value='<%= p.getProviderNo() %>'><%= p.getLastName() %>, <%= p.getFirstName() %></option><% }} %>";
<% } %>
function changeSite(sel) {
	sel.form.assignedTo.innerHTML=sel.value=="none"?"":_providers[sel.value];
}
      </script>
      	<select id="site" name="site" onchange="changeSite(this)">
      		<option value="none">---select clinic---</option>
      	<%
      	for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%= sites.get(i).getSiteId() %>" <%=sites.get(i).getSiteId().toString().equals(request.getParameter("site"))?"selected":"" %>><%= sites.get(i).getName() %></option>
      	<% } %>
      	</select>
      	<select id="assignedTo" name="assignedTo" style="width:140px"></select>
<% if (request.getParameter("assignedTo")!=null) { %>
      	<script>
     	changeSite(document.getElementById("site"));
      	document.getElementById("assignedTo").value='<%=request.getParameter("assignedTo")%>';
      	</script>
<% } // multisite end ==========================================
} else {
%>
        <select id="assignedTo" name="assignedTo">
        <option value="all" <%=assignedTo.equals("all")?"selected":""%>><bean:message key="tickler.ticklerMain.formAllProviders"/></option>
        <%
            List<Provider> providersActive = providerDao.getActiveProviders(); 
            for (Provider p : providersActive) {
        %>
        <option value="<%=p.getProviderNo()%>" <%=assignedTo.equals(p.getProviderNo())?"selected":""%>><%=p.getLastName()%>, <%=p.getFirstName()%></option>
        <%}%>
        </select>
<% } %>


      <!--/td>
      <td -->
        <font color="#333333" size="2" face="Verdana, Arial, Helvetica, sans-serif">
        <input type="hidden" name="Submit" value="">
        <input type="button" value="<bean:message key="tickler.ticklerMain.btnCreateReport"/>" class="mbttn" onclick="document.forms['serviceform'].Submit.value='Create Report'; document.forms['serviceform'].submit();">
        <oscar:oscarPropertiesCheck property="TICKLERSAVEVIEW" value="yes">
        &nbsp;
        <input type="button" value="<bean:message key="tickler.ticklerMain.msgSaveView"/>" class="mbttn" onclick="saveView();">
        </oscar:oscarPropertiesCheck>
        </font>
        </td>
    </tr>  
</table>
</form>
        
<form name="ticklerform" method="post" action="dbTicklerMain.jsp">
<table bgcolor=#666699 border=0 cellspacing=0 width=100%>
<tr><td>


<table class="sortable" border="0" cellpadding="0" cellspacing="0" width="100%">
    <thead>
        <TR bgcolor=#EEEEFF>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="3%"></th>
            <% 
            boolean ticklerEditEnabled = Boolean.parseBoolean(OscarProperties.getInstance().getProperty("tickler_edit_enabled")); 
            if (ticklerEditEnabled) { %>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="3%"></th>
            <% }%>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="17%"><bean:message key="tickler.ticklerMain.msgDemographicName"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="8%"><bean:message key="tickler.ticklerMain.msgCreator"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="9%"><bean:message key="tickler.ticklerMain.msgDate"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="9%"><bean:message key="tickler.ticklerMain.msgCreationDate"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="6%"><bean:message key="tickler.ticklerMain.Priority"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="12%"><bean:message key="tickler.ticklerMain.taskAssignedTo"/></th>

            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="6%"><bean:message key="tickler.ticklerMain.msgStatus"/></th>
            <th style="color:#000000; font-size:xsmall; font-family:verdana,arial,helvetica;" width="39%"><bean:message key="tickler.ticklerMain.msgMessage"/></th>
        </TR>
    </thead>
    <tfoot>
                                <tr bgcolor=#666699><td colspan="8" class="white"><a id="checkAllLink" name="checkAllLink" href="javascript:CheckAll();"><bean:message key="tickler.ticklerMain.btnCheckAll"/></a> - <a href="javascript:ClearAll();"><bean:message key="tickler.ticklerMain.btnClearAll"/></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                                    <input type="button" name="button" value="<bean:message key="tickler.ticklerMain.btnAddTickler"/>" onClick="popupPage('400','600', 'ticklerAdd.jsp')" class="sbttn">
                                    <input type="hidden" name="submit_form" value="">
                                    <% if (ticklerview.compareTo("D") == 0){%>
                                    <input type="button" value="<bean:message key="tickler.ticklerMain.btnEraseCompletely"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Erase Completely'; document.forms['ticklerform'].submit();">
                                    <%} else{%>
                                    <input type="button" value="<bean:message key="tickler.ticklerMain.btnComplete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Complete'; document.forms['ticklerform'].submit();">
                                    <input type="button" value="<bean:message key="tickler.ticklerMain.btnDelete"/>" class="sbttn" onclick="document.forms['ticklerform'].submit_form.value='Delete'; document.forms['ticklerform'].submit();">
                                    <%}%>
                            <input type="button" name="button" value="<bean:message key="global.btnCancel"/>" onClick="window.close()" class="sbttn"> </td></tr>
                        </tfoot>
                        <tbody>

                            <%
                            String dateBegin = xml_vdate;
                            String dateEnd = xml_appointment_date;
                            
                            String vGrantdate = "1980-01-07 00:00:00.0";
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm.SSS", request.getLocale());

                            if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
                            if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // any early start date should suffice for selecting since the beginning
                                                      
                            CustomFilter filter = new CustomFilter();
                            
                            filter.setStatus(ticklerview);
                            filter.setStartDate(dateBegin);
                            filter.setEndDate(dateEnd);
                            
                            if( !mrpview.isEmpty() && !mrpview.equals("all")) {
                            	filter.setMrp(mrpview);
                            }
                            
                            if (!providerview.isEmpty() && !providerview.equals("all")) {
                                filter.setProvider(providerview);
                            }
                            
                            if (!assignedTo.isEmpty() && !assignedTo.equals("all")) {
                                filter.setAssignee(assignedTo);
                            }

                            String colNames[] = new String[] {"last_name", "provider_last", "service_date", "update_date", "priority", "assignedLast", "status", "message"};
                            v = null;
                            String order = "service_date desc";
                            int col;
                            if( viewMap != null ) {
                                v = viewMap.get("columnId");
                                if( v != null ) {
                                   col = Integer.parseInt(v.getValue()) - 1;
                                   order = colNames[col] + " ";
                                   v = viewMap.get("sortOrder");
                                   if( v!= null ) {
                                       order += v.getValue();
                                   }
                                }
                            }
                            filter.setSort_order("desc");
                                                                                  
                            TicklerDAO ticklerDao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
                            List<Tickler> ticklers = ticklerDao.getTicklers(filter);
                            
                            String rowColour = "lilac";

                            for (Tickler t : ticklers) {
                                Demographic demo = t.getDemographic(); 
                               
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

                                <tr >
                                    <TD width="3%"  ROWSPAN="1" class="<%=cellColour%>"><input type="checkbox" name="checkbox" value="<%=t.getTickler_no()%>"></TD>
                                    <%if (ticklerEditEnabled) { %>
                                    <td width="3%" ROWSPAN="1" class="<%=cellColour%>"><a href=#  onClick="popupPage(600,800, '../tickler/ticklerEdit.jsp?tickler_no=<%=t.getTickler_no()%>')"><bean:message key="tickler.ticklerMain.editTickler"/></a></td>
                                    <%}%>                                    
                                    <TD width="12%" ROWSPAN="1" class="<%=cellColour%>"><a href=# onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=demo.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')"><%=demo.getLastName()%>,<%=demo.getFirstName()%></a></TD>                                                                       
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getProvider() == null ? "N/A" : t.getProvider().getFormattedName()%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getServiceDate()%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getUpdateDate()%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getPriority()%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getAssignee() != null ? t.getAssignee().getLastName() + ", " + t.getAssignee().getFirstName() : "N/A"%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getStatusDesc(request.getLocale())%></TD>
                                    <TD ROWSPAN="1" class="<%=cellColour%>"><%=t.getMessage()%>
                                        
                                        <%
                                        List<TicklerLink> linkList = ticklerLinkDao.getLinkByTickler(t.getTickler_no().intValue());
                                        if (linkList != null){
                                            for(TicklerLink tl : linkList){
                                                String type = tl.getTableName();
                                                %>

                                                <% if ( LabResultData.isMDS(type) ){ %>
                                                <a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <% }else if (LabResultData.isCML(type)){ %>
                                                <a href="javascript:reportWindow('../lab/CA/ON/CMLDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <% }else if (LabResultData.isHL7TEXT(type)){ %>
                                                <a href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <% }else if (LabResultData.isDocument(type)){ %>
                                                <a href="javascript:reportWindow('../dms/ManageDocument.do?method=display&doc_no=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <% }else {%>
                                                <a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=tl.getTableId()%>&providerNo=<%=user_no%>&searchProviderNo=<%=user_no%>&status=')">ATT</a>
                                                <% }%>
                                        <%  }
                                        }
                                        %>
                                        
                                    </TD>
                                </tr>
                                <%
                                    Set<TicklerComment> tcomments = t.getComments();
                                    if (ticklerEditEnabled && !tcomments.isEmpty()) {
                                        for (TicklerComment tc : tcomments) {
                                %>
                                    <tr>
                                        <td width="3%"  ROWSPAN="1" class="<%=cellColour%>"></td>
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
                                <%      }                                        
                                    }
                            }

                            if (ticklers.isEmpty()) {
                            %>
                            <tr><td colspan="8" class="white"><bean:message key="tickler.ticklerMain.msgNoMessages"/></td></tr>                                                            
                            <%}%>
                        </tbody>

        
</table></td></tr></table></form>
                        
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
<%@ include file="../demographic/zfooterbackclose.jsp" %>

<p class="yesprint">
	<%=OscarProperties.getConfidentialityStatement()%>
</p>

</body>
</html:html>
