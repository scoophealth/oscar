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
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
String country = request.getLocale() .getCountry();

ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
String curUser_no = (String) session.getAttribute("user");
String mygroupno = providerPreference.getMyGroupNo();
mygroupno = StringUtils.trimToEmpty(mygroupno);
String billingRegion = (oscar.OscarProperties.getInstance()).getProperty("billregion");
%>
<%@ page
	import="java.util.*, oscar.*, java.sql.*, java.text.*, java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="reportMainBeanConn.jspf"%>
<% }  %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%

	
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
    String provider_dboperation = "search_provider";
    String mygroup_dboperation = "search_group";
%>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName2$%>" rights="r" reverse="false">
	<%
		isSiteAccessPrivacy =true;
		provider_dboperation = "site_search_provider";
		mygroup_dboperation = "site_search_group";
	%>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName2$%>" rights="r" reverse="false">
	<%
		isTeamAccessPrivacy =true; 
		provider_dboperation = "team_search_provider";
	%>
	
</security:oscarSec>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportindex.title" /></title>
<link rel="stylesheet" href="../web.css" />

<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>


<script type="text/javascript" src="../dojoAjax/dojo.js"></script>
<script type="text/javascript" language="JavaScript">
            dojo.require("dojo.date.format");
			dojo.require("dojo.widget.*");
			dojo.require("dojo.validate.*");
		</script>
<script type="text/javascript" src="../js/caisi_report_tools.js"></script>


<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.schedule.keyword.focus();
  //document.schedule.keyword.select();
}
function selectprovider(s) {
	self.location.href = "scheduletemplatesetting1.jsp?provider_no="+s.options[s.selectedIndex].value+"&provider_name="+urlencode(s.options[s.selectedIndex].text);
}

function ogo() {
  var region = '<%=billingRegion%>';
  var s = document.getElementsByName("startDate")[0].value;
  s = s.replace('/', '-');
  var e = document.getElementsByName("endDate")[0].value;
  e = e.replace('/', '-');
  var u = '';
  if (region == "BC") u = 'reportbcedblist.jsp?startDate=' + s + '&endDate=' + e;
  else  u = 'reportnewedblist.jsp?startDate=' + s + '&endDate=' + e;
	popupPage(700,900,u);
}

function ogo2() {
  var region = '<%=billingRegion%>';  
  var s = document.getElementsByName("startDate")[0].value;
  s = s.replace('/', '-');
  var e = document.getElementsByName("endDate")[0].value;
  e = e.replace('/', '-');
  var u = '';
  if (region == "BC") u = 'reportbcedblist2007.jsp?startDate=' + s + '&endDate=' + e;
  else u = 'reportonedblist.jsp?startDate=' + s + '&endDate=' + e;
  popupPage(700,900,u);
}

function popupPageNew(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "demographicprofile", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}



function go(r) {
  var s = document.getElementsByName("provider_no")[0].value;
  var t = document.getElementsByName("sdate")[0].value;
  var u = document.getElementsByName("asdate")[0].value;
  var v = document.getElementsByName("aedate")[0].value;
  var y = document.getElementsByName("sTime")[0].value;
  var z = document.getElementsByName("eTime")[0].value;
  var ro = document.getElementById("rosteredOnly").checked;
  var td = document.getElementsByName("tabDay")[0].value;
  var w = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
  var x = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ u + '&edate=' + v + '&sTime=' + y + '&eTime=' + z ;
  var x2 = 'reportdaysheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ u + '&edate=' + v + '&sTime=' + y + '&eTime=' + z + '&rosteredStatus=true';
  var y2 =  'tabulardaysheetreport.jsp?provider_no=' + s +'&sdate=' + td.replace('/', '-');

if (r=='tab')
{
	popupPage(600,750, y2);
}
else if(r=='new') {
    if(confirm("<bean:message key="report.reportindex.msgGoConfirm"/>") ) {
	  popupPage(600,750,w);
	}
 }
 else if( ro == true ) {
        popupPageNew(600,750,x2);
  }else {
	popupPageNew(600,750,x);
  }
}

function gonew(r) {

var s = document.getElementsByName("provider_no")[0].value;
var t = document.getElementsByName("sdate")[0].value;
var u = document.getElementsByName("asdate")[0].value;
var v = document.getElementsByName("aedate")[0].value;
var y = document.getElementsByName("sTime")[0].value;
var z = document.getElementsByName("eTime")[0].value;
var ro = document.getElementById("rosteredOnly").checked;
var td = document.getElementsByName("tabDay")[0].value;

var w = 'reportdaysheet2.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
var x = 'reportdaysheet2.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ u + '&edate=' + v + '&sTime=' + y + '&eTime=' + z ;
var x2 = 'reportdaysheet2.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ u + '&edate=' + v + '&sTime=' + y + '&eTime=' + z + '&rosteredStatus=true';
var y2 =  'tabulardaysheetreport.jsp?provider_no=' + s +'&sdate=' + td.replace('/', '-');

if (r=='tab')
 {
popupPage(600,750, y2);
}
else if(r=='new') {
if(confirm("<bean:message key="report.reportindex.msgGoConfirm"/>") ) {
popupPage(600,750,w);
  }
      }
 else if( ro == true ) {
 popupPage(600,750,x2);
  }else {
 popupPageNew(600,750,x);
  }
   }

function ggo(r) {
  var s = document.getElementsByName("pprovider_no")[0].value;
  var t = document.getElementsByName("ssdate")[0].value;
  
  var u = 'reportapptsheet.jsp?dsmode=' + r + '&provider_no=' + s +'&sdate='+ t;
	popupPage(600,750,u);
}
ONCLICK ="popupPage(600,750,'reportpatientchartlist.jsp')"
function pcgo() {
  var s = document.getElementsByName("pcprovider_no")[0].value;
  var u = 'reportpatientchartlist.jsp?provider_no=' + s;
	popupPage(600,750,u);
}
function opcgo() {
  var s = document.getElementsByName("opcprovider_no")[0].value;
  var a = document.getElementsByName("age")[0].value;
  var u = 'reportpatientchartlistspecial.jsp?provider_no=' + s + '&age=' + a;
	popupPage(600,1010,u);
}
function nsgo() {
  var s = document.getElementsByName("nsprovider_no")[0].value;
  var t = document.getElementsByName("nsdate")[0].value;
  var u = 'reportnoshowapptlist.jsp?provider_no=' + s +'&sdate='+ t;
	popupPage(600,750,u);
}
function popUpWaitingList(vheight,vwidth,varpage) {
	var listIdObject = document.getElementsByName("list_id")[0];
	var page = varpage + listIdObject.options[listIdObject.selectedIndex].value;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
    var popup=window.open(page, "Waiting List", windowprops);
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<%
GregorianCalendar now = new GregorianCalendar();
GregorianCalendar cal = (GregorianCalendar) now.clone();
String today = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
%>
<form name='report'>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<td align=LEFT><font face="Helvetica" color="#FFFFFF"><bean:message key="report.reportindex.msgTitle" /></td>
		<td align=RIGHT>
		<span class="HelpAboutLogout">
			<oscar:help keywords="&Title=Report+Tab&portal_type%3Alist=Document" key="app.top1" style="color:white; font-size:10px;font-style:normal;"/>&nbsp;
        		<a style="color:white; font-size:10px;font-style:normal;" href="<%=request.getContextPath()%>/oscarEncounter/About.jsp" target="_new"><bean:message key="global.about" /></a>
		</span> </font>
		</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="95%">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<%int j = 1; %>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300">
		<% if (billingRegion.equals("BC")) { %> <a HREF="#" ONCLICK="ogo()"><bean:message
			key="report.reportindex.btnEDDList" /></a> &nbsp;<a HREF="#"
			ONCLICK="ogo2()">07</a> <% } else { %> <a HREF="#" ONCLICK="ogo()"><bean:message
			key="report.reportindex.btnEDBList" /></a> &nbsp;<a HREF="#"
			ONCLICK="ogo2()">05</a> <% } %>
		</td>
		<td><a HREF="#"
			onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.getElementsByName('startDate')[0].value")%>')">
		<bean:message key="report.reportindex.formFrom" /></a> 
		<%-- any early default start date should suffice for reporting all --%>
		<INPUT TYPE="text" NAME="startDate" VALUE="<%=today%>" size='10'>
		</td>
		<td><a HREF="#"
			onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.getElementsByName('endDate')[0].value")%>')">
		<bean:message key="report.reportindex.formTo" /></a> <INPUT TYPE="text"
			NAME="endDate" VALUE="<%=today%>" size='10'></td>
		<td><INPUT TYPE="button" NAME="button"
			VALUE="<bean:message key="report.reportindex.btnCreateReport"/>"
			onClick="ogo()"></td>
		<td></td>
	</tr>
	
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a HREF="#"
			ONCLICK="popupPage(600,750,'reportactivepatientlist.jsp')"><bean:message
			key="report.reportindex.btnActivePList" /></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><bean:message
			key="report.reportindex.formDaySheet" /></td>
		<td><select name="provider_no">
			<%
               ResultSet rsgroup = reportMainBean.queryResults(mygroup_dboperation);
                     while (rsgroup.next()) {
                    	 if (isTeamAccessPrivacy) 
                    		 continue;	//skip mygroup display if user have TeamAccessPrivacy 
            %>
			<option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>"
				<%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
			<%
                     }
            %>
			<%
                 rsgroup = reportMainBean.queryResults(provider_dboperation);
                     while (rsgroup.next()) {
            %>
			<option value="<%=rsgroup.getString("provider_no")%>"
				<%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%>><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
                     }
            %>
			<option value="*"><bean:message
				key="report.reportindex.formAllProviders" /></option>
		</select></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"></td>
		<td width="1">&nbsp;</td>
                        <td width="300">
<oscar:oscarPropertiesCheck property="NEW_DAY_SHEET_STYLE" value="yes">
                        <sup>*</sup><a HREF="#" ONCLICK="gonew('all')">Hospital Appointment</a><br>
</oscar:oscarPropertiesCheck>
                        <sup>*</sup><a HREF="#" ONCLICK="go('all')"><bean:message
			key="report.reportindex.btnAllAppt" /></a><br>&nbsp;&nbsp; <bean:message key="report.reportindex.chkRostered"/> <input type="checkbox" id="rosteredOnly" value="true"> </td>
		<td><a HREF="#"
			onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.getElementsByName('asdate')[0].value")%>')"><bean:message
			key="report.reportindex.formFrom" /></a> <input type='text' name="asdate"
			VALUE="<%=today%>" size=10></td>
		<td><a HREF="#"
			onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.getElementsByName('aedate')[0].value")%>')"><bean:message
			key="report.reportindex.formTo" /> </a> <input type='text' name="aedate"
			VALUE="<%=today%>" size=10></td>
		<td><select name="sTime">
			<%
              for(int i=0; i<24; i++) {
                String timeString = i<12&&i>=0? (i+" am") : ((i==12?i:i-12)+ " pm") ;
            %>
			<option value="<%=""+i%>" <%=i==8?"selected":""%>><%=timeString%></option>
			<% } %>
		</select> - <select name="eTime">
			<%
              for(int i=0; i<24; i++) {
                String timeString = i<12&&i>=0? (i+" am") : ((i==12?i:i-12)+ " pm") ;
            %>
			<option value="<%=""+i%>" <%=i==20?"selected":""%>><%=timeString%></option>
			<% } %>
		</select></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"></td>
		<td width="1">&nbsp;</td>
                    <td width="300"><sup>*</sup><a HREF="#" ONCLICK="go('new')"
			title="<bean:message key="report.reportindex.msgNewApptsOld"/>"><bean:message
			key="report.reportindex.btnPrintDaySheet" /></a></td>
		<td><select name="sdate">
			<%
              cal.add(cal.DATE, -1) ;
              for(int i=0; i<31; i++) {
                String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
            %>
			<option value="<%=dateString%>"
				<%=dateString.equals(today)?"selected":""%>><%=dateString%></option>
			<%
                cal.add(cal.DATE, 1) ;
                    }
            %>
		</select></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
        
    <!-- add lab day sheet -->
        <script type="text/javascript">
        function labgo(s) {
                var t = document.getElementsByName("apptDate")[0].value;
                var u = 'printLabDaySheetAction.do?xmlStyle=labDaySheet.xml&input_date=' + t;
                popupPage(600,1000,u);
        }
        function labgo1(s) {
        	var t = document.getElementsByName("apptDate1")[0].value;
                var u = 'printLabDaySheetAction.do?xmlStyle=billDaySheet.xml&input_date=' + t;
                popupPage(600,1000,u);
        }
        </script>
    <tr>
        <td width="2"></td>
        <td width="1">*</td>
        <td width="300"><a HREF="#" ONCLICK ="labgo('labDaySheet')" title="lab day sheet"/>Lab Day Sheet</a></td>
        <td>
            <select name="apptDate" >
            <%
                          cal.add(cal.DATE, -31) ;
              cal.add(cal.DATE, -1) ;
              for(int i=0; i<31; i++) {
                String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
            %>
              <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
            <%
                cal.add(cal.DATE, 1) ;
                    }
            %>
            </select>
        </td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td width="2"></td>
        <td width="1">*</td>
        <td width="300"><a HREF="#" ONCLICK ="labgo1('billingDaySheet')" title="billing day sheet"/>Billing Day Sheet</a></td>
        <td>
            <select name="apptDate1" >
            <%
              cal.add(cal.DATE, -31) ;
              cal.add(cal.DATE, -1) ;
              for(int i=0; i<31; i++) {
                String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
            %>
              <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
            <%
                cal.add(cal.DATE, 1) ;
                    }
            %>
            </select>
        </td>
        <td></td>
        <td></td>
        <td></td>
    </tr>

    <!-- add lab day sheet -->

	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="#" ONCLICK="go('tab')"><bean:message key="report.reportindex.btnDaySheetTable"/></a></td>
		<td><input type='text' name="tabDay" VALUE="<%=today%>" size=10></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a HREF="#" ONCLICK="ggo('all')"><bean:message
			key="report.reportindex.formBadAppt" /></a></td>
		<td><select name="pprovider_no">
			<%
               rsgroup = reportMainBean.queryResults(mygroup_dboperation);
                     while (rsgroup.next()) {
                    	 if (isTeamAccessPrivacy) 
                    		 continue;	//skip mygroup display if user have TeamAccessPrivacy 
            %>
			<option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>"
				<%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
			<%
                     }
            %>
			<%
                 rsgroup = reportMainBean.queryResults(provider_dboperation);
                     while (rsgroup.next()) {
            %>
			<option value="<%=rsgroup.getString("provider_no")%>"
				<%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%>><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
                     }
            %>
			<option value="*"><bean:message
				key="report.reportindex.formAllProviders" /></option>
		</select></td>
		<td><select name="ssdate">
			<%
              cal.add(cal.DATE, -31) ;
              for(int i=0; i<31; i++) {
                String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
            %>
			<option value="<%=dateString%>"
				<%=dateString.equals(today)?"selected":""%>><%=dateString%></option>
			<%
                cal.add(cal.DATE, 1) ;
                    }
            %>
		</select></td>
		<td></td>
		<td></td>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a HREF="#" ONCLICK="pcgo()"><bean:message
			key="report.reportindex.btnPatientChartList" /></a></td>
		<td><select name="pcprovider_no">
			<%
               rsgroup = reportMainBean.queryResults(mygroup_dboperation);
                     while (rsgroup.next()) {
                    	 if (isTeamAccessPrivacy) 
                    		 continue;	//skip mygroup display if user have TeamAccessPrivacy 
            %>
			<option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>"
				<%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
			<%
                     }
            %>
			<%
                 rsgroup = reportMainBean.queryResults(provider_dboperation);
                     while (rsgroup.next()) {
            %>
			<option value="<%=rsgroup.getString("provider_no")%>"
				<%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%>><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
                     }
            %>
		</select></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a HREF="#" ONCLICK="opcgo()"><bean:message
			key="report.reportindex.btnOldPatient" /></a></td>
		<td><select name="opcprovider_no">
			<%
               rsgroup = reportMainBean.queryResults(mygroup_dboperation);
                     while (rsgroup.next()) {
                    	 if (isTeamAccessPrivacy) 
                    		 continue;	//skip mygroup display if user have TeamAccessPrivacy 
            %>
			<option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>"
				<%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
			<%
                     }
            %>
			<%
                 rsgroup = reportMainBean.queryResults(provider_dboperation);
                     while (rsgroup.next()) {
            %>
			<option value="<%=rsgroup.getString("provider_no")%>"
				<%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%>><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
                     }
            %>
		</select></td>
		<td><bean:message key="report.reportindex.btnOldPatientAge" /><input
			type=text name=age value='65'></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a HREF="#" ONCLICK="nsgo()"><bean:message
			key="report.reportindex.btnNoShowAppointmentList" /></a></td>
		<td><select name="nsprovider_no">
			<%
               rsgroup = reportMainBean.queryResults(mygroup_dboperation);
                     while (rsgroup.next()) {
                    	 if (isTeamAccessPrivacy) 
                    		 continue;	//skip mygroup display if user have TeamAccessPrivacy 
            %>
			<option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>"
				<%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%="GRP: "+rsgroup.getString("mygroup_no")%></option>
			<%
                     }
            %>
			<%
                 rsgroup = reportMainBean.queryResults(provider_dboperation);
                     while (rsgroup.next()) {
            %>
			<option value="<%=rsgroup.getString("provider_no")%>"
				<%=curUser_no.equals(rsgroup.getString("provider_no"))?"selected":""%>><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
                     }
            %>
		</select></td>
		<td>
		<%
		cal.add(cal.DATE, 0);
                String NoShowEDate = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
            	%> <bean:message key="report.reportindex.msgStart"/>: <input name="nsdate" type="input" size="8"
			id="NoShowDate" <%=NoShowEDate%>> <a HREF="#"
			onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../report/reportindex.jsp&year=<%=now.get(Calendar.YEAR)%>&month=<%=now.get(Calendar.MONTH)+1%>&param=<%=URLEncoder.encode("&formdatebox=document.getElementsByName('nsdate')[0].value")%>')"><img
			title=Calendar " src="../images/cal.gif" alt="Calendar" border="0"><a>



		<!-- 
            <select name="nsdate" >
 <%
              cal.add(cal.DATE, -61) ;
              for(int i=0; i<31; i++) {
                String dateString = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) ;
            %>

              <option value="<%=dateString%>" <%=dateString.equals(today)?"selected":""%> ><%=dateString%></option>
            <%
                cal.add(cal.DATE, 1) ;
                    }
            %>
            </select>
--></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="../oscarReport/ConsultationReport.jsp"
			target="_blank"><bean:message
			key="report.reportindex.btnConsultationReport" /></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>

	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="../oscarReport/LabReqReport.jsp"
			target="_blank"><bean:message
			key="report.reportindex.btnLaboratoryRequisition" /></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a
			href="../oscarReport/ReportDemographicReport.jsp" target="_blank"><bean:message
			key="report.reportindex.btnDemographicReportTool" /></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="../oscarReport/demographicSetEdit.jsp"
			target="_blank"><bean:message key="report.reportindex.btnDemoSetEdit"/></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>



	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a
			href="../oscarPrevention/PreventionReporting.jsp" target="_blank"><bean:message key="report.reportindex.btnReport18n"/></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href=#
			onClick="popupPage(600,750,'demographicstudyreport.jsp')"><bean:message
			key="report.reportindex.btnDemographicStudyList" /></a></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a
			href='../oscarReport/oscarMeasurements/SetupSelectCDMReport.do'><bean:message
			key="report.reportindex.chronicDiseaseManagement" /></a></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a
			href="../oscarWaitingList/SetupDisplayWaitingList.do?waitingListId="><bean:message key="report.reportindex.btnWaiting"/></a></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="reportFormRecord.jsp"><bean:message key="report.reportindex.btnFormReport"/></a></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>

	<% //if((oscar.OscarProperties.getInstance()).getProperty("demographicExt") != null && (oscar.OscarProperties.getInstance()).getProperty("demographicExt").startsWith("Approximate EDD")) {
%>
	<tr>
		<td width="2"><%=j%>
		<%j++;%>
		</td>
		<td width="1"></td>
		<td width="300"><a href="reportBCARDemo.jsp"><bean:message key="report.reportindex.btnSCBPDemoRept"/></a></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	<% //}
%>


   <tr>
        <td width="2"><%=j%><%j++;%></td>
        <td width="1"></td>
        <td width="300"><a href="ClinicalReports.jsp"><bean:message key="report.reportindex.btnClinicalReport"/> </a></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>

     <tr>
        <td width="2"><%=j%><%j++;%></td>
        <td width="1"></td>
        <td width="300"><a href="../oscarReport/InjectionReport2.jsp" target="_blank"><bean:message key="report.reportindex.btnInjectionReport"/></a></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>

    <tr>
        <td width="2"><%=j%><%j++;%></td>
        <td width="1"></td>
        <td width="300"><a href="../oscarReport/OSISReport.jsp" title="Off Streets into Shelters (OSIS) is a report based off of a Hamilton Public Health eForm" target="_blank"><bean:message key="report.reportindex.btnOSISReport"/></a></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
     <tr>
	<td width="2"><%=j%><%j++;%></td>
	<td width="1"></td>
	<td width="300"><a title="Report that is based off of a Hamilton Public Health eForm for One Time Consults" href="../oscarReport/CDSOneTimeConsultReport.jsp" target="_blank"><bean:message key="report.reportindex.btnCDSOneTimeConsultReport"/></a></td>

     </tr>   
     <tr></tr>  
     <tr></tr>
     
    <tr><td width="2"></td>
    	<td colspan='3' align="left">
    <c:if
	test="${sessionScope.userrole ne 'er_clerk' and sessionScope.userrole ne 'Vaccine Provider'}">

	<div><span>CAISI Reporting Tools</span> 
	<caisi:isModuleLoad moduleName="caisi">
		<div><a HREF="../PMmodule/ClientManager.do?method=getGeneralFormsReport" target="generalFormsReport">General Forms Reports</a></div>
		<div><a href="javascript:void(0);" onclick="javascript:getIntakeReport('quick');return false;">Registration Intake Report</a></div>
		<div><a href="javascript:void(0);" onclick="javascript:getIntakeReport('indepth');return false;">Follow-up Intake Report</a></div>
		<div><html:link action="/PMmodule/Reports/ProgramActivityReport.do">Activity Report</html:link></div>
		<div><html:link action="/SurveyManager.do?method=reportForm">User Created Form Report</html:link></div>
		<div><html:link action="QuatroReport/ReportList.do">Quatro Report Runner</html:link></div>
		<div><a href="javascript:void(0);" onclick="javascript:createStreetHealthReport();return false;">Street Health Mental Health Report</a></div>
	</caisi:isModuleLoad></div>
</c:if> 
	
    </td>
    </tr>


<security:oscarSec roleName="<%=roleName2$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=false%>">
    <tr>
	<td width="2"><%=j%><%j++;%></td>
	<td width="1"></td>
	<td width="300"><a href="javascript:void(0);" onclick="popupPage(600,800,'../oscarReport/reportByTemplate/homePage.jsp')"><bean:message key="admin.admin.rptbyTemplate" /></a></td>

    </tr>      
</security:oscarSec>

    <tr><td>&nbsp;</td></tr>
    <tr>
        <td colspan='3' align="left"><input type="button" name="Button" value="<bean:message key="report.reportindex.btnCancel"/>" onClick="window.close()"></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
</table>
</body>
</form>
</html:html>
