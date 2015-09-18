<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%

  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("a.start_time") ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*,org.oscarehr.common.model.*,org.apache.commons.lang.time.*" errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<% 
	org.oscarehr.common.model.ProviderPreference providerPreference=org.oscarehr.web.admin.ProviderPreferencesUIBean.updateOrCreateProviderPreferences(request);
	int endHour = providerPreference.getEndHour();
	int startHour = providerPreference.getStartHour();
	
  String [][] dbQueries=new String[][] { 
	{"search_daysheetall", "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, p.last_name, p.first_name, d.last_name,d.first_name,d.chart_no, d.phone, d.date_of_birth, d.month_of_birth, d.year_of_birth, d.hin from appointment a,demographic d,provider p, mygroup m where a.appointment_date=? and  m.mygroup_no=? and BINARY a.status != 'C' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no AND p.provider_no=m.provider_no order by p.provider_no, a.appointment_date, "+orderby }, 
	{"search_daysheetsingleall", "select a.appointment_date, a.provider_no,a.start_time,a.end_time, a.reason,p.last_name,p.first_name,d.last_name,d.first_name,d.chart_no, d.phone, d.date_of_birth, d.month_of_birth, d.year_of_birth, d.hin from appointment a,demographic d,provider p where a.appointment_date=? and a.provider_no=? and BINARY a.status != 'C' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
  };
 	String[][] responseTargets=new String[][] {  };
  	daySheetBean.doConfigure(dbQueries,responseTargets);
   	GregorianCalendar now = new GregorianCalendar();
  	String createtime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) ;
	String date = request.getParameter("sdate");
  	String provider_no = request.getParameter("provider_no");
  	boolean bodd = false;
  	boolean printed = false;
  	ResultSet rsdemo = null;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR - <bean:message key="report.tabulardaysheetreport.title"/>=</title>
<link rel="stylesheet" href="../share/css/oscar.css">
<link rel="stylesheet" href="../share/css/reporting.css">
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table width="100%" bgcolor="#D3D3D3" border="0">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left">
		<p><font color="#4D4D4D"><b><font size="4">oscar<font
			size="3"><bean:message key="report.tabulardaysheetreport.msgTitle"/> (<%=createtime%>)</font></font></b></font></p>
		</td>
		<td><input type="button" name="Button" value="<bean:message key="report.tabulardaysheetreport.btnPrint"/>" onClick="window.print()">
            <input type="button" name="Button" value=" <bean:message key="report.tabulardaysheetreport.btnExit"/> " onClick="window.close()">
        </td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="1"
	class="smallerTable">
	<tr>
		<td><font size=4><b><%=providerBean.getProperty(request.getParameter("provider_no")) + "</b>  (" + date + ")"%><font></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="0"
	cellpadding="0" class="smallerTable">
	<tr>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgTime"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgChart"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgName"/></b></td>
		<td class="items" align="center" width="79"><b><bean:message key="report.tabulardaysheetreport.msgDoB"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgPHN"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgFee1"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag1"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag2"/></b></td>
		<td class="items" align="center" width="55"><b><bean:message key="report.tabulardaysheetreport.msgDiag3"/></b></td>
		<td class="items"><b><bean:message key="report.tabulardaysheetreport.msgDescription"/></b></td>
	</tr>
<%
	boolean bFistL = true;
	if ((5 >= provider_no.length()) || (5 < provider_no.length()) && !(provider_no.substring(0, 5).compareTo("_grp_") == 0)) {
		rsdemo = daySheetBean.queryResults(new String[] {date, provider_no}, "search_daysheetsingleall");
	} else {
		rsdemo = daySheetBean.queryResults(new String[] {date, provider_no.substring(5, provider_no.length())}, "search_daysheetall");
    }
		
	java.util.Date indexDate = DateUtils.parseDate(date + " " + startHour + ":00", new String[]{"yyyy-mm-dd HH:mm"});;
	java.util.Date endDate = DateUtils.parseDate(date + " " + endHour + ":00", new String[]{"yyyy-mm-dd HH:mm"});
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
	java.util.Date previousEndDate = null;
	
	while (indexDate.before(endDate)) {
		if (rsdemo.next()) {
			java.util.Date currentEndDate = DateUtils.parseDate(rsdemo.getString("a.appointment_date") + " " + rsdemo.getString("a.end_time"), new String[]{"yyyy-mm-dd HH:mm:ss"});
			java.util.Date appointmentDate = DateUtils.parseDate(rsdemo.getString("a.appointment_date") + " " + rsdemo.getString("a.start_time"), new String[]{"yyyy-mm-dd HH:mm:ss"});
			while ((!printed && DateUtils.addMinutes(indexDate, 14).before(appointmentDate)) || (printed && DateUtils.addMinutes(indexDate, 29).before(appointmentDate))) {
			    if (printed) {
			        indexDate = DateUtils.addMinutes(indexDate, 15);
			        printed = false;
			    }
				bodd = !bodd;
%>
				<tr bgcolor="<%=bodd?"#F6F6F6":"#FFFFFF"%>">
					<td class="items"><%=formatter.format(indexDate)%></td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
				</tr>
<%			
				indexDate = DateUtils.addMinutes(indexDate, 15);
			}
			bodd = !bodd;
			if (DateUtils.addMinutes(indexDate, 14).before(appointmentDate)) {
			    indexDate = DateUtils.addMinutes(indexDate, 15);
			}
%>
				<tr bgcolor="<%=((bodd)?"#F6F6F6":"#FFFFFF")%>">
					<td class="items"><%=formatter.format(indexDate)%></td>
					<td class="items"><%=rsdemo.getString("d.chart_no")%>&nbsp;</td>
					<td class="items"><%=Misc.toUpperLowerCase(rsdemo.getString("d.last_name")) + ", " + Misc.toUpperLowerCase(rsdemo.getString("d.first_name")) + " Ph:" + rsdemo.getString("d.phone")%></td>
					<td class="items"><%=rsdemo.getString("d.date_of_birth") + "-" + rsdemo.getString("d.month_of_birth") + "-" + rsdemo.getString("d.year_of_birth")%></td>
					<td class="items"><%=rsdemo.getString("d.hin")%>&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items">&nbsp;</td>
					<td class="items"><%=rsdemo.getString("a.reason")%>&nbsp;</td>
				</tr>
<%
			previousEndDate = currentEndDate;
			printed = true;
			// indexDate = DateUtils.addMinutes(indexDate, 15);
		} else {
			bodd = !bodd;
			indexDate = DateUtils.addMinutes(indexDate, 15);
%>
			<tr bgcolor="<%=bodd?"#F6F6F6":"#FFFFFF"%>">
				<td class="items"><%=formatter.format(indexDate)%></td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
				<td class="items">&nbsp;</td>
			</tr>
<%					
		}
	}
%>
</table>
</body>
</html>
