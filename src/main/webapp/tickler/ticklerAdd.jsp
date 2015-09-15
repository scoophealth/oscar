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
String user_no = (String) session.getAttribute("user");
int  nItems=0;
String strLimit1="0";
String strLimit2="5";
if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
boolean bFirstDisp=true; //this is the first time to display the window
if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
String ChartNo;
String demoNo = "";
String demoName = request.getParameter("name");
if ( request.getAttribute("demographic_no") != null){
    demoNo = (String) request.getAttribute("demographic_no");
    demoName = (String) request.getAttribute("demoName");
    bFirstDisp = false;
}
if(demoName == null){demoName ="";}

//Retrieve encounter id for updating encounter navbar if info this page changes anything
String parentAjaxId;
if( request.getParameter("parentAjaxId") != null )
    parentAjaxId = request.getParameter("parentAjaxId");
else
    parentAjaxId = "";
    
String updateParent;
if( request.getParameter("updateParent") != null )
    updateParent = request.getParameter("updateParent");
else
    updateParent = "true";  

%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarEncounter.pageUtil.EctSessionBean" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
%>

<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);   
  
  %><% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay):request.getParameter("xml_appointment_date");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%><html:html locale="true">
<head>
<title><bean:message key="tickler.ticklerAdd.title"/></title>
<link rel="stylesheet" href="../billing/billing.css" >
<style type="text/css">
<!--
.bodytext
{
  font-family: Arial, Helvetica, sans-serif;
  font-size: 14px;
  font-style: bold;
  line-height: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  color: #FFFFFF;
  text-decoration: none;
}
-->
</style>
      <script language="JavaScript">
<!--
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
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
}

function validate(form){
if (validateDemoNo(form)){
form.action = "dbTicklerAdd.jsp";
form.submit();

}
else{}
}
function validateDemoNo() {
  if (document.serviceform.demographic_no.value == "") {
alert("<bean:message key="tickler.ticklerAdd.msgInvalidDemographic"/>");
	return false;
 }
 else{  if (document.serviceform.xml_appointment_date.value == "") {
alert("<bean:message key="tickler.ticklerAdd.msgMissingDate"/>");
	return false;
 }
<% if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { %>
 else if (document.serviceform.site.value=="none"){
alert("Must assign task to a provider.");
	return false;
 } 
<% } %>
 else{
 return true;
}
 }


}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}





function DateAdd(startDate, numDays, numMonths, numYears)
{
	var returnDate = new Date(startDate.getTime());
	var yearsToAdd = numYears;
	
	var month = returnDate.getMonth()	+ numMonths;
	if (month > 11)
	{
		yearsToAdd = Math.floor((month+1)/12);
		month -= 12*yearsToAdd;
		yearsToAdd += numYears;
	}
	returnDate.setMonth(month);
	returnDate.setFullYear(returnDate.getFullYear()	+ yearsToAdd);
	
	returnDate.setTime(returnDate.getTime()+60000*60*24*numDays);
	
	return returnDate;

}

function YearAdd(startDate, numYears)
{
		return DateAdd(startDate,0,0,numYears);
}

function MonthAdd(startDate, numMonths)
{
		return DateAdd(startDate,0,numMonths,0);
}

function DayAdd(startDate, numDays)
{
		return DateAdd(startDate,numDays,0,0);
}


function addMonth(no)
{       var gCurrentDate = new Date();
	var newDate = DateAdd(gCurrentDate, 0, no,0 );
var newYear = newDate.getFullYear() 
var newMonth = newDate.getMonth()+1;
var newDay = newDate.getDate();
var newD = newYear + "-" + newMonth + "-" + newDay;
	document.serviceform.xml_appointment_date.value = newD;
}









//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"> </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4"><bean:message key="tickler.ticklerAdd.msgTickler"/></font></b></font> 
      </p>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0"bgcolor="#EEEEFF">
 <form name="ADDAPPT" method="post" action="../appointment/appointmentcontrol.jsp">
<tr> 
      <td width="35%"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b><bean:message key="tickler.ticklerAdd.formDemoName"/>: </b></font></font></td>
      <td colspan="2" width="65%">
<div align="left"><INPUT TYPE="TEXT" NAME="keyword" size="25" VALUE="<%=bFirstDisp?"":demoName.equals("")?session.getAttribute("appointmentname"):demoName%>">
   	 <input type="submit" name="Submit" value="<bean:message key="tickler.ticklerAdd.btnSearch"/>">
  </div>
</td>
    </tr>
  <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name" >
  <%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%>
				      <INPUT TYPE="hidden" NAME="search_mode" VALUE="<%=searchMode%>" >
				      <INPUT TYPE="hidden" NAME="originalpage" VALUE="../tickler/ticklerAdd.jsp" >
				      <INPUT TYPE="hidden" NAME="limit1" VALUE="0" >
				      <INPUT TYPE="hidden" NAME="limit2" VALUE="5" >
              <!--input type="hidden" name="displaymode" value="TicklerSearch" -->
              <INPUT TYPE="hidden" NAME="displaymode" VALUE="Search "> 

<% ChartNo = bFirstDisp?"":request.getParameter("chart_no")==null?"":request.getParameter("chart_no"); %>
   <INPUT TYPE="hidden" NAME="appointment_date" VALUE="2002-10-01" WIDTH="25" HEIGHT="20" border="0" hspace="2">
       <INPUT TYPE="hidden" NAME="status" VALUE="t"  WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="start_time" VALUE="10:45" WIDTH="25" HEIGHT="20" border="0"  onChange="checkTimeTypeIn(this)">
              <INPUT TYPE="hidden" NAME="type" VALUE="" WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="duration" VALUE="15" WIDTH="25" HEIGHT="20" border="0" hspace="2" >
              <INPUT TYPE="hidden" NAME="end_time" VALUE="10:59" WIDTH="25" HEIGHT="20" border="0" hspace="2"  onChange="checkTimeTypeIn(this)">
       

 <input type="hidden" name="demographic_no"  readonly value="" width="25" height="20" border="0" hspace="2">
         <input type="hidden" name="location"  tabindex="4" value="" width="25" height="20" border="0" hspace="2">
              <input type="hidden" name="resources"  tabindex="5" value="" width="25" height="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="user_id" readonly VALUE='oscardoc, doctor' WIDTH="25" HEIGHT="20" border="0" hspace="2">
     	        <INPUT TYPE="hidden" NAME="dboperation" VALUE="search_demorecord">
              <INPUT TYPE="hidden" NAME="createdatetime" readonly VALUE="2002-10-1 17:53:50" WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="provider_no" VALUE="115">
              <INPUT TYPE="hidden" NAME="creator" VALUE="oscardoc, doctor">
              <INPUT TYPE="hidden" NAME="remarks" VALUE="">
              <input type="hidden" name="parentAjaxId" value="<%=parentAjaxId%>"/>
              <input type="hidden" name="updateParent" value="<%=updateParent%>"/> 
 </form>
</table>
<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="post" >
      <input type="hidden" name="parentAjaxId" value="<%=parentAjaxId%>"/>
      <input type="hidden" name="updateParent" value="<%=updateParent%>"/>
     <tr> 
      <td width="35%"> <div align="left"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><strong><bean:message key="tickler.ticklerAdd.formChartNo"/>:</strong> </font></font></div></td>
      <td colspan="2"> <div align="left"><INPUT TYPE="hidden" NAME="demographic_no" VALUE="<%=bFirstDisp?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>"><%=ChartNo%></div></td>
    </tr>

    <tr> 
      <td><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong><bean:message key="tickler.ticklerAdd.formServiceDate"/>:</strong></font></td>
      <td><input type="text" name="xml_appointment_date" value="<%=xml_appointment_date%>"> 
        <font color="#003366" size="1" face="Verdana, Arial, Helvetica, sans-serif">
        <a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerAdd.btnCalendarLookup"/></a> &nbsp; &nbsp; 
        <a href="#" onClick="addMonth(6)"><bean:message key="tickler.ticklerAdd.btn6Month"/></a>&nbsp; &nbsp;
        <a href="#" onClick="addMonth(12)"><bean:message key="tickler.ticklerAdd.btn1Year"/></a></font> </td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td height="21" valign="top"><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong><bean:message key="tickler.ticklerMain.Priority"/></strong></font></td>
      <td valign="top"> 
	<select name="priority" style="font-face:Verdana, Arial, Helvetica, sans-serif">
 	<option value="<bean:message key="tickler.ticklerMain.priority.high"/>"><bean:message key="tickler.ticklerMain.priority.high"/>
	<option value="<bean:message key="tickler.ticklerMain.priority.normal"/>" SELECTED><bean:message key="tickler.ticklerMain.priority.normal"/>
	<option value="<bean:message key="tickler.ticklerMain.priority.low"/>"><bean:message key="tickler.ticklerMain.priority.low"/>	
     	</select>
      </td>
      <td>&nbsp;</td>
    </tr>

    <tr> 
      <td height="21" valign="top"><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong><bean:message key="tickler.ticklerMain.taskAssignedTo"/></strong></font></td>
      <td valign="top"> <font face="Verdana, Arial, Helvetica, sans-serif" size="2" color="#333333">
<% if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) 
{ // multisite start ==========================================
        	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo(user_no);
          	String appNo = (String) session.getAttribute("cur_appointment_no");
          	String location = null;
          	if (appNo != null) {
          		Appointment a  = appointmentDao.find(Integer.parseInt(appNo));
          		if(a != null) {
          			location = a.getLocation();
          		}
          		
          	}
      %> 
      <script>
var _providers = [];
<%	
Site site = null;
for (int i=0; i<sites.size(); i++) { %>
	_providers["<%= sites.get(i).getSiteId() %>"]="<% Iterator<Provider> iter = sites.get(i).getProviders().iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if ("1".equals(p.getStatus())) {
	%><option value='<%= p.getProviderNo() %>'><%= p.getLastName() %>, <%= p.getFirstName() %></option><% }} %>";
<%	if (sites.get(i).getName().equals(location))
		site = sites.get(i);
	} %>
function changeSite(sel) {
	sel.form.task_assigned_to.innerHTML=sel.value=="none"?"":_providers[sel.value];
}
      </script>
      	<select id="site" name="site" onchange="changeSite(this)">
      		<option value="none">---select clinic---</option>
      	<%
      	for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%= sites.get(i).getSiteId() %>"><%= sites.get(i).getName() %></option>
      	<% } %>
      	</select>
      	<select name="task_assigned_to" style="width:140px"></select>
      	<script>
      		document.getElementById("site").value = '<%= site==null?"none":site.getSiteId() %>';
      		changeSite(document.getElementById("site"));
      	</script>
<% // multisite end ==========================================
} else {
%>
      <select name="task_assigned_to">           
            <%  String proFirst="";
                String proLast="";
                String proOHIP="";
				
                for(Provider p : providerDao.getActiveProviders()) {
               
                    proFirst =p.getFirstName();
                    proLast = p.getLastName();
                    proOHIP = p.getProviderNo();

            %> 
            <option value="<%=proOHIP%>" <%=user_no.equals(proOHIP)?"selected":""%>><%=proLast%>, <%=proFirst%></option>
            <%
                }
            %>
      </select>
<% } %>
          
           <input type="hidden" name="docType" value="<%=request.getParameter("docType")%>"/>
           <input type="hidden" name="docId" value="<%=request.getParameter("docId")%>"/>
      </td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td height="21" valign="top"><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong><bean:message key="tickler.ticklerAdd.formReminder"/>:</strong></font></td>
      <td valign="top"> <textarea style="font-face:Verdana, Arial, Helvetica, sans-serif"name="textarea" cols="50" rows="10"></textarea></td>
      <td>&nbsp;</td>
    </tr>
        
     <INPUT TYPE="hidden" NAME="user_no" VALUE="<%=user_no%>">
    <tr> 
      <td><input type="button" name="Button" value="<bean:message key="tickler.ticklerAdd.btnCancel"/>" onClick="window.close()"></td>
      <td><input type="button" name="Button" value="<bean:message key="tickler.ticklerAdd.btnSubmit"/>" onClick="validate(this.form)"></td>
      <td></td>
	  </tr>
  </form>
</table>
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
<%@ include file="../demographic/zfooterbackclose.jsp" %> 

</body>
</html:html>
