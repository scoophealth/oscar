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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%@ page errorPage="errorpage.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@ page import="org.oscarehr.appointment.web.NextAppointmentSearchHelper" %>
<%@ page import="org.oscarehr.appointment.web.NextAppointmentSearchBean" %>
<%@ page import="org.oscarehr.appointment.web.NextAppointmentSearchResult" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
  SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

  //providers
  String providerNo = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"";
  ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
  List<Provider> providers = providerDao.getActiveProviders();
  
  //day of week
  String dayOfWeek = request.getParameter("dayOfWeek")!=null?request.getParameter("dayOfWeek"):"daily";
  List<LabelValueBean> dayOfWeekOptions = new ArrayList<LabelValueBean>();
  dayOfWeekOptions.add(new LabelValueBean("Any Weekday","daily"));
  dayOfWeekOptions.add(new LabelValueBean("Monday",String.valueOf(Calendar.MONDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Tuesday",String.valueOf(Calendar.TUESDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Wednesday",String.valueOf(Calendar.WEDNESDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Thursday",String.valueOf(Calendar.THURSDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Friday",String.valueOf(Calendar.FRIDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Saturday",String.valueOf(Calendar.SATURDAY)));
  dayOfWeekOptions.add(new LabelValueBean("Sunday",String.valueOf(Calendar.SUNDAY)));
  
  //time of day
  String startTime = request.getParameter("startTime")!=null?request.getParameter("startTime"):"9";
  String endTime = request.getParameter("endTime")!=null?request.getParameter("endTime"):"17";
  List<LabelValueBean> startTimeOfDayOptions = new ArrayList<LabelValueBean>();
  startTimeOfDayOptions.add(new LabelValueBean("midnight","0"));  
  for(int x=1;x<=11;x++) {
	  startTimeOfDayOptions.add(new LabelValueBean(x + " am",String.valueOf(x)));	 
  }
  startTimeOfDayOptions.add(new LabelValueBean("noon","12"));
  for(int x=1;x<=11;x++) {
	  startTimeOfDayOptions.add(new LabelValueBean(x + " pm",String.valueOf(x+12)));	 
  }
  List<LabelValueBean> endTimeOfDayOptions = new ArrayList<LabelValueBean>();
  for(int x=1;x<=11;x++) {
	  endTimeOfDayOptions.add(new LabelValueBean(x + " am",String.valueOf(x)));	 
  }
  endTimeOfDayOptions.add(new LabelValueBean("noon","12"));
  for(int x=1;x<=11;x++) {
	  endTimeOfDayOptions.add(new LabelValueBean(x + " pm",String.valueOf(x+12)));	 
  }
 // endTimeOfDayOptions.add(new LabelValueBean("midnight","0"));  
  
  //code
  String code = request.getParameter("code")!=null?request.getParameter("code"):"";  
  ScheduleTemplateCodeDao scheduleTemplateCodeDao = (ScheduleTemplateCodeDao)SpringUtils.getBean("scheduleTemplateCodeDao");
  List<ScheduleTemplateCode> codes = scheduleTemplateCodeDao.findAll();
  
  //numberOfResults
  String numberOfResults = request.getParameter("numberOfResults")!=null?request.getParameter("numberOfResults"):"3";  
  List<LabelValueBean> numberOfResultsOptions = new ArrayList<LabelValueBean>();
  for(int x=1;x<=10;x++) {
	  numberOfResultsOptions.add(new LabelValueBean(String.valueOf(x),String.valueOf(x)));	 
  }
  
  List<NextAppointmentSearchResult> results = null;
  String method = request.getParameter("method");
  if(method != null && method.equals("search")) {
	  NextAppointmentSearchBean searchBean = new NextAppointmentSearchBean();
	  searchBean.setProviderNo(providerNo);
	  searchBean.setDayOfWeek(dayOfWeek);
	  searchBean.setStartTimeOfDay(startTime);
	  searchBean.setEndTimeOfDay(endTime);
	  searchBean.setCode(code);
	  searchBean.setNumResults(Integer.parseInt(numberOfResults));
	  results = NextAppointmentSearchHelper.search(searchBean);
  }
  
%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="appointment.searchnext.title" /></title>
<link rel="stylesheet" href="../web.css">
<script>
function popupPage2(varpage, windowname, vheight, vwidth) {
	// Provide default values for windowname, vheight, and vwidth incase popupPage2
	// is called with only 1 or 2 arguments (must always specify varpage)
	windowname  = typeof(windowname)!= 'undefined' ? windowname : 'apptProviderSearch';
	vheight     = typeof(vheight)   != 'undefined' ? vheight : '700px';
	vwidth      = typeof(vwidth)    != 'undefined' ? vwidth : '1024px';
	var page = "" + varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
	var popup = window.open(page, windowname, windowprops);
	if (popup != null) {
		if (popup.opener == null) {
	  		popup.opener = self;
		}
		popup.focus();
		}
	}

function selectSlot(providerNo,year,month,day,startTime,endTime,duration) {
	var queryString = '<%=request.getContextPath()%>/appointment/addappointment.jsp?provider_no='+providerNo + '&year='+year+'&month='+month+'&day='+day+'&start_time='+startTime+'&end_time='+endTime+'&duration='+duration;
	popupPage2(queryString, 'appointment', 400, 780);

}

function validate() {
	var startTime = parseInt(document.forms["searchForm"].elements["startTime"].value);
	var endTime = parseInt(document.forms["searchForm"].elements["endTime"].value);
	if(startTime >= endTime) {
		alert('Start time must be less than end time.');
		return false;
	}
	return true;
}
</script>
</head>

<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<th NOWRAP bgcolor="#CCCCFF">
			<font face="Helvetica"><bean:message key="appointment.searchnext.2ndtitle" /></font>
		</th>
	</tr>
</table>
<form name="searchForm" action="<%=request.getContextPath()%>/appointment/appointmentsearch.jsp" method="get" onsubmit="return validate()">
<input type="hidden" name="method" value="search"/>
<table width="100%" border="0">
	<tr>
		<td><bean:message key="appointment.searchnext.provider" />:</td>
		<td>
			<select name="provider_no">
				<option value="">All</option>
				<%
					for(Provider provider:providers) {
						String selected = new String();
						if(providerNo.equals(provider.getProviderNo())) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=provider.getProviderNo()%>" <%=selected%>><%=provider.getFormattedName()%></option><%			
					}
				%>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="appointment.searchnext.day_of_week" />:</td>
		<td>
			<select name="dayOfWeek">
				<%
					for(LabelValueBean lvb:dayOfWeekOptions) {
						String selected = new String();
						if(lvb.getValue().equals(dayOfWeek)) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=lvb.getValue()%>" <%=selected%>><%=lvb.getLabel()%></option><%
					}
				%>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="appointment.searchnext.time_of_day" />:</td>
		<td>
			<select name="startTime">
				<%
					for(LabelValueBean lvb:startTimeOfDayOptions) {
						String selected = new String();
						if(lvb.getValue().equals(startTime)) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=lvb.getValue()%>" <%=selected%>><%=lvb.getLabel()%></option><%
					}
				%>
			</select>
			&nbsp;<bean:message key="appointment.searchnext.to" />&nbsp;
			<select name="endTime">
				<%
					for(LabelValueBean lvb:endTimeOfDayOptions) {
						String selected = new String();
						if(lvb.getValue().equals(endTime)) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=lvb.getValue()%>" <%=selected%>><%=lvb.getLabel()%></option><%
					}
				%>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="appointment.searchnext.appt_type" />:</td>
		<td>
			<select name="code">
				<option value="">Any</option>
				<%
					for(ScheduleTemplateCode c:codes) {
						String selected = new String();
						if(String.valueOf(c.getCode()).equals(code)) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=c.getCode()%>" <%=selected%>><%=c.getCode()%> - <%=c.getDescription() %></option><%
					}
				%>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="appointment.searchnext.num_results" />:</td>
		<td>
			<select name="numberOfResults">
				<%
					for(LabelValueBean lvb:numberOfResultsOptions) {
						String selected = new String();
						if(lvb.getValue().equals(numberOfResults)) {
							selected=" selected=\"selected\" ";
						}
						%><option value="<%=lvb.getValue()%>" <%=selected%>><%=lvb.getLabel()%></option><%
					}
				%>
			</select>		
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="Search" />
			&nbsp;&nbsp;
			<input type="button" value="Close" onclick="window.close();window.opener.location.reload();"/>
		</td>
	</tr>
</table>
</form>

<%if(results != null)  { %>
<CENTER>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1" cellpadding="0">
	<tr bgcolor="#CCCCFF">
		<TH width="20%"><bean:message key="appointment.searchnext.date" /></TH>
		<TH width="20%"><bean:message key="appointment.searchnext.time" /></TH>
		<TH width="60%"><bean:message key="appointment.searchnext.provider" /></TH>
	</tr>
	<%for(int x=0;x<Math.min(results.size(),Integer.parseInt(numberOfResults));x++)  {
		NextAppointmentSearchResult result = results.get(x);
	%>
		<tr onmouseover="this.style.cursor='hand';this.style.backgroundColor='pink';" onmouseout="this.style.backgroundColor='#FFFFFF';" onclick="selectSlot('<%=result.getProviderNo()%>','<%=result.getYear()%>','<%=result.getMonth()%>','<%=result.getDay()%>','<%=result.getStartTime()%>','<%=result.getEndTime()%>','<%=result.getDuration()%>');">
			<td><%=dayFormatter.format(result.getDate()) %></td>
			<td><%=timeFormatter.format(result.getDate()) %></td>
			<td><%=result.getProvider().getFormattedName() %></td>
		</tr>
	<% } %>
</table>
<% } %>
<br>

</center>
</body>
</html>
