<%
    
    String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("start_time") ;
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF" ;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, oscar.login.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<%
    java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    String [][] dbQueries;

    if (vLocale.getCountry().equals("BR")) {
        dbQueries=new String[][] {
            {"search_daysheetall",       "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name from (appointment a, provider p, demographic d) left join provider p2 on d.provider_no=p2.provider_no where a.provider_no=p.provider_no and a.demographic_no=d.demographic_no and d.demographic_no = a.demographic_no and a.appointment_date>=? and a.appointment_date<=? and a.provider_no=p.provider_no and a.status != 'C' order by p.last_name, p.first_name, a.appointment_date, "+orderby },
            {"search_daysheetsingleall", "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name from (appointment a, provider p, demographic d) left join provider p2 on d.provider_no=p2.provider_no where a.provider_no=p.provider_no and a.demographic_no=d.demographic_no and d.demographic_no = a.demographic_no and a.appointment_date>=? and a.appointment_date<=? and a.provider_no=? and a.status != 'C' and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
            {"search_daysheetnew",       "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name from (appointment a, provider p, demographic d) left join provider p2 on d.provider_no=p2.provider_no where a.provider_no=p.provider_no and a.demographic_no=d.demographic_no and d.demographic_no = a.demographic_no and a.appointment_date=? and a.provider_no=p.provider_no and a.status like 't%' order by p.last_name, p.first_name, a.appointment_date,"+orderby },
            {"search_daysheetsinglenew", "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name from (appointment a, provider p, demographic d) left join provider p2 on d.provider_no=p2.provider_no where a.provider_no=p.provider_no and a.demographic_no=d.demographic_no and d.demographic_no = a.demographic_no and a.appointment_date=? and a.provider_no=? and a.status like 't%' and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
            {"searchmygroupall",         "select * from mygroup where mygroup_no= ?"},
            {"update_apptstatus",        "update appointment set status='T', lastupdateuser=?, updatedatetime=now() where appointment_date=? and status='t' " },
            {"update_apptstatussingle",  "update appointment set status='T', lastupdateuser=?, updatedatetime=now() where appointment_date=? and provider_no=? and status='t' " },
            {"archive_appt",             "insert into appointmentArchive (select * from appointment where appointment_date=? and status='t')"},
            {"archive_apptsingle",       "insert into appointmentArchive (select * from appointment where appointment_date=? and provider_no=? and status='t')"}
        };
    } else {
        dbQueries=new String[][] {
            {"search_daysheetall",       "select concat(d.year_of_birth,'/',d.month_of_birth,'/',d.date_of_birth)as dob, d.family_doctor, a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name, d.hin from (appointment a, provider p) left join demographic d on a.demographic_no=d.demographic_no left join provider p2 on d.provider_no=p2.provider_no where a.appointment_date>=? and a.appointment_date<=? and a.start_time>=? and a.end_time<? and a.provider_no=p.provider_no and a.status != 'C' order by p.last_name, p.first_name, a.appointment_date, "+orderby },
            {"search_daysheetsingleall", "select concat(d.year_of_birth,'/',d.month_of_birth,'/',d.date_of_birth)as dob, d.family_doctor, a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name, d.hin  from (appointment a, provider p )left join demographic d on a.demographic_no=d.demographic_no left join provider p2 on d.provider_no=p2.provider_no where a.appointment_date>=? and a.appointment_date<=? and a.start_time>=? and a.end_time<? and a.provider_no=? and a.status != 'C' and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
            {"search_daysheetnew",       "select concat(d.year_of_birth,'/',d.month_of_birth,'/',d.date_of_birth)as dob, d.family_doctor, a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name, d.hin  from (appointment a, provider p) left join demographic d on a.demographic_no=d.demographic_no left join provider p2 on d.provider_no=p2.provider_no where a.appointment_date=? and a.provider_no=p.provider_no and a.status like binary 't' order by p.last_name, p.first_name, a.appointment_date,"+orderby },
            {"search_daysheetsinglenew", "select concat(d.year_of_birth,'/',d.month_of_birth,'/',d.date_of_birth)as dob, d.family_doctor, a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, a.name, p.last_name, p.first_name, d.provider_no as doc_no, d.chart_no, d.roster_status, p2.last_name as doc_last_name, p2.first_name as doc_first_name, d.hin  from (appointment a, provider p) left join demographic d on a.demographic_no=d.demographic_no left join provider p2 on d.provider_no=p2.provider_no where a.appointment_date=? and a.provider_no=? and a.status like binary 't' and a.provider_no=p.provider_no order by a.appointment_date,"+orderby },
            {"searchmygroupall",         "select * from mygroup where mygroup_no= ?"},
            {"update_apptstatus",        "update appointment set status='T', lastupdateuser=?, updatedatetime=now() where appointment_date=? and status='t' " },
            {"update_apptstatussingle",  "update appointment set status='T', lastupdateuser=?, updatedatetime=now() where appointment_date=? and provider_no=? and status='t' " },
            {"archive_appt",             "insert into appointmentArchive (select * from appointment where appointment_date=? and status='t')"},
            {"archive_apptsingle",       "insert into appointmentArchive (select * from appointment where appointment_date=? and provider_no=? and status='t')"}
        };
    }
  	
    daySheetBean.doConfigure(dbQueries);
%>
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportdaysheet.title" /></title>
<link rel="stylesheet" href="../web.css">
<style>
td {
	font-size: 16px;
}
</style>
<script language="JavaScript">
<!--

//-->
</script>
</head>
<%
	boolean bDob = oscarVariables.getProperty("daysheet_dob", "").equalsIgnoreCase("true") ? true : false;

    GregorianCalendar now=new GregorianCalendar();
    String createtime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) ;
    now.add(now.DATE, 1);
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);

    String sdate = request.getParameter("sdate")!=null?request.getParameter("sdate"):(curYear+"-"+curMonth+"-"+curDay) ;
    String edate = request.getParameter("edate")!=null?request.getParameter("edate"):"" ;
    String sTime = request.getParameter("sTime")!=null? (request.getParameter("sTime")+":00:00") : "00:00:00" ;
    String eTime = request.getParameter("eTime")!=null? (request.getParameter("eTime")+":00:00") : "24:00:00" ;
    String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"175" ;
    ResultSet rsdemo = null ;
    boolean bodd = false;

    //initial myGroupBean if neccessary
    if(provider_no.startsWith("_grp_")) {
	    rsdemo = daySheetBean.queryResults(provider_no.substring(5), "searchmygroupall");
        while (rsdemo.next()) {
	        myGroupBean.setProperty(rsdemo.getString("provider_no"),"true");
        }
    }
%>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepColor%>">
		<th><bean:message key="report.reportdaysheet.msgMainLabel" /></th>
		<th width="10%" nowrap><%=createtime%> <input type="button"
			name="Button"
			value="<bean:message key="report.reportdaysheet.btnPrint"/>"
			onClick="window.print()"><input type="button" name="Button"
			value="<bean:message key="global.btnExit"/>" onClick="window.close()"></th>
	</tr>
</table>

<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String dateTemp = "";
  String [] param = new String[3];
  param[0] = (String) session.getAttribute("user");
  param[1] = sdate;
  param[2] = provider_no;
  String [] parama = new String[5];
  parama[0] = sdate;
  parama[1] = edate;
  parama[2] = sTime;
  parama[3] = eTime;
  parama[4] = provider_no;
  if(request.getParameter("dsmode")!=null && request.getParameter("dsmode").equals("all") ) {
	  if(!provider_no.equals("*") && !provider_no.startsWith("_grp_") ) {
	  rsdemo = daySheetBean.queryResults(parama, "search_daysheetsingleall");
	  
    } else { //select all providers
	  rsdemo = daySheetBean.queryResults(new String[] {parama[0], parama[1], sTime, eTime}, "search_daysheetall");
    }
  } else { //new appt, need to update status
    if(!provider_no.equals("*") && !provider_no.startsWith("_grp_") ) {
	  rsdemo = daySheetBean.queryResults(param, "search_daysheetsinglenew");
          daySheetBean.queryExecuteUpdate(new String[]{param[1],param[2]}, "archive_apptsingle");
	  daySheetBean.queryExecuteUpdate(param, "update_apptstatussingle");
    } else { //select all providers
	  rsdemo = daySheetBean.queryResults(param[0], "search_daysheetnew");
          daySheetBean.queryExecuteUpdate(param[1], "archive_appt");
	  daySheetBean.queryExecuteUpdate(new String[]{param[0],param[1]}, "update_apptstatus");
    }
  }
  while (rsdemo.next()) {
    //if it is a group and a group member
	if(!myGroupBean.isEmpty()) {
	  if(myGroupBean.getProperty(rsdemo.getString("provider_no"))==null) continue;
	}

  bodd = bodd?false:true;
	if(!strTemp.equals(rsdemo.getString("provider_no")) || !dateTemp.equals(rsdemo.getString("appointment_date")) ) { //new provider for a new table
	  strTemp = rsdemo.getString("provider_no") ;
          dateTemp = rsdemo.getString("appointment_date");
	  bFistL = true;
	  out.println("</table> <p>") ;
	}
	if(bFistL) {
	  bFistL = false;
    bodd = false ;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td><%=providerBean.getProperty(rsdemo.getString("provider_no")) + " - " +dateTemp + (request.getParameter("sTime")!=null? (" " + sTime + "-" + eTime) : "") %>
		</td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="0"
	cellpadding="1">
	<tr bgcolor="#CCCCFF" align="center">
		<!--<TH width="14%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=appointment_date"><bean:message key="report.reportdaysheet.msgAppointmentDate"/></a></b></TH>-->
		<TH width="10%"><b><a
			href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=start_time<%=request.getParameter("dsmode")==null?"":"&dsmode="+request.getParameter("dsmode")%>"><bean:message
			key="report.reportdaysheet.msgAppointmentTime" /></a></b></TH>
		<TH width="26%"><b><a
			href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=name<%=request.getParameter("dsmode")==null?"":"&dsmode="+request.getParameter("dsmode")%>"><bean:message
			key="report.reportdaysheet.msgPatientLastName" /></a> </b></TH>
		<!--<TH width="20%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=p_first_name"><bean:message key="report.reportdaysheet.msgPatientFirstName"/></a> </b></TH>-->
		<TH width="10%"><b><a
			href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=chart_no<%=request.getParameter("dsmode")==null?"":"&dsmode="+request.getParameter("dsmode")%>"><bean:message
			key="report.reportdaysheet.msgChartNo" /></a></b></TH>
                <TH width="6%"><b><a
			href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=hin<%=request.getParameter("dsmode")==null?"":"&dsmode="+request.getParameter("dsmode")%>"><bean:message
			key="oscarEncounter.search.demographicSearch.msgHin" /></a></b></TH>
		<% if(!bDob) {%>
		<TH width="6%"><b><a
			href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=roster_status<%=request.getParameter("dsmode")==null?"":"&dsmode="+request.getParameter("dsmode")%>"><bean:message
			key="report.reportdaysheet.msgRosterStatus" /></a></b></TH>
		<% } else {%>
		<TH width="6%"><b>DOB</b></TH>
		<% }%>
		<TH width="48%"><b><bean:message
			key="report.reportdaysheet.msgComments" /></b></TH>
	</tr>
	<%
    }
%>
<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
      <!--<td align="center" nowrap><%=rsdemo.getString("appointment_date")%></td>-->
      <td align="center" nowrap title="<%="End Time: "+rsdemo.getString("end_time")%>"><%=rsdemo.getString("start_time").substring(0,5)%></td>
      <td align="left"><%=rsdemo.getString("chart_no")==null?".":""%><%=Misc.toUpperLowerCase(rsdemo.getString("name"))%></td>
      <td align="center">&nbsp;<%=rsdemo.getString("chart_no")==null?"":rsdemo.getString("chart_no")%>&nbsp;</td>
      <td><%=rsdemo.getString("hin")%></td>
<% if(!bDob) {%>
      <td align="center">&nbsp;<%=rsdemo.getString("roster_status")==null?"":rsdemo.getString("roster_status")%>&nbsp;</td>
<% } else {
		String dob = rsdemo.getString("dob");
		//String sql = "select year_of_birth,month_of_birth,date_of_birth from demographic where demographic_no=" + rsdemo.getString("demographic_no");
		//ResultSet rs = dbObj.searchDBRecord(sql);
		//if (rs.next()) {
		//	dob = dbObj.getString(rs,"year_of_birth") + "/" + dbObj.getString(rs,"month_of_birth")+ "/" + dbObj.getString(rs,"date_of_birth");
		//}
%>
		<td align="center">&nbsp;<%=dob==null?"":dob%></td>
		<% }%>
		<td>
		<% if ( rsdemo.getString("doc_no") != null && ! daySheetBean.getString(rsdemo,"doc_no").equals("") && ! daySheetBean.getString(rsdemo,"doc_no").equals(daySheetBean.getString(rsdemo,"provider_no")) ) {

                    String doc_first_name = daySheetBean.getString(rsdemo,"doc_first_name");
                    char initial = 0x20;
                    if( doc_first_name.length() > 0 ) {
                        initial = doc_first_name.charAt(0);
                    }
%>
		[<%=daySheetBean.getString(rsdemo,"doc_last_name")%>, <%=initial%>]
		&nbsp; <% } %> <% if ( bDob && daySheetBean.getString(rsdemo,"family_doctor") != null) {
              String rd = SxmlMisc.getXmlContent(daySheetBean.getString(rsdemo,"family_doctor"),"rd");
              rd = rd !=null ? rd : "" ;
          %> [<%=rd%>]&nbsp; <% } %> <%=daySheetBean.getString(rsdemo,"reason")%>&nbsp;</td>
	</tr>
	<%
  }
%>

</table>
</body>
</html:html>
