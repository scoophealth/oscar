<%
    if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
    String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("start_time") ;
    String deepColor = "#CCCCFF", weakColor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
    String [][] dbQueries=new String[][] { 
{"search_daysheetall", "select a.appointment_date, a.provider_no, a.start_time, a.end_time, a.reason, p.last_name, p.first_name, d.last_name,d.first_name,d.chart_no,d.roster_status from appointment a, demographic d, provider p where a.appointment_date>=? and a.appointment_date<=? and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no and a.status != 'C' order by p.last_name, p.first_name, a.appointment_date, "+orderby }, 
{"search_daysheetsingleall", "select a.appointment_date, a.provider_no,a.start_time,a.end_time, a.reason,p.last_name,p.first_name,d.last_name,d.first_name,d.chart_no,d.roster_status from appointment a,demographic d,provider p where a.appointment_date>=? and a.appointment_date<=? and a.provider_no=? and a.status != 'C' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no order by a.appointment_date,"+orderby }, 
{"search_daysheetnew", "select a.appointment_date as appointment_date, a.provider_no as provider_no, a.start_time, a.end_time, a.reason, p.last_name, p.first_name, d.last_name,d.first_name,d.chart_no,d.roster_status from appointment a, demographic d, provider p where a.appointment_date=? and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no and a.status = 't' order by p.last_name, p.first_name, a.appointment_date,"+orderby }, 
{"search_daysheetsinglenew", "select a.appointment_date as appointment_date, a.provider_no as provider_no,a.start_time,a.end_time, a.reason,p.last_name,p.first_name,d.last_name,d.first_name,d.chart_no,d.roster_status from appointment a,demographic d,provider p where a.appointment_date=? and a.provider_no=? and a.status = 't' and a.demographic_no=d.demographic_no and a.provider_no=p.provider_no order by a.appointment_date,"+orderby }, 
{"searchmygroupall", "select * from mygroup where mygroup_no= ?"}, 
{"update_apptstatus", "update appointment set status='T' where appointment_date=? and status='t' " }, 
{"update_apptstatussingle", "update appointment set status='T' where appointment_date=? and provider_no=? and status='t' " }, 
    };
    daySheetBean.doConfigure(dbParams,dbQueries);
%>
<html:html locale="true">
<head>
<title><bean:message key="report.reportdaysheet.title"/></title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
<link rel="stylesheet" href="../web.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}
//-->
</SCRIPT>
</head>
<% 
    GregorianCalendar now=new GregorianCalendar();
    String createtime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) ;
    now.add(now.DATE, 1);
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);

    String sdate = request.getParameter("sdate")!=null?request.getParameter("sdate"):(curYear+"-"+curMonth+"-"+curDay) ;
    String edate = request.getParameter("edate")!=null?request.getParameter("edate"):"" ;
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
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepColor%>"><th><bean:message key="report.reportdaysheet.msgMainLabel"/></th>
    <th width="10%" nowrap><%=createtime%> 
      <input type="button" name="Button" value="<bean:message key="report.reportdaysheet.btnPrint"/>" onClick="window.print()"><input type="button" name="Button" value="<bean:message key="global.btnExit"/>" onClick="window.close()"></th></tr>
</table>

<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String [] param = new String[2];
  param[0] = sdate;
  param[1] = provider_no;
  String [] parama = new String[3];
  parama[0] = sdate;
  parama[1] = edate;
  parama[2] = provider_no;
  if(request.getParameter("dsmode")!=null && request.getParameter("dsmode").equals("all") ) {
    if(!provider_no.equals("*") && !provider_no.startsWith("_grp_") ) {
	  rsdemo = daySheetBean.queryResults(parama, "search_daysheetsingleall");
    } else { //select all providers
	  rsdemo = daySheetBean.queryResults(new String[] {parama[0], parama[1]}, "search_daysheetall");
    }
  } else { //new appt, need to update status
    if(!provider_no.equals("*") && !provider_no.startsWith("_grp_") ) {
	  rsdemo = daySheetBean.queryResults(param, "search_daysheetsinglenew");
	  int rowsAffected = daySheetBean.queryExecuteUpdate(param, "update_apptstatussingle");
    } else { //select all providers
	  rsdemo = daySheetBean.queryResults(param[0], "search_daysheetnew");
	  int rowsAffected = daySheetBean.queryExecuteUpdate(param[0], "update_apptstatus");
    }
  }
	
  while (rsdemo.next()) { 
    //if it is a group and a group member
	if(!myGroupBean.isEmpty()) {
	  if(myGroupBean.getProperty(rsdemo.getString("provider_no"))==null) continue;
	}
  
  bodd = bodd?false:true;
	if(!strTemp.equals(rsdemo.getString("provider_no")) ) { //new provider for a new table
	  strTemp = rsdemo.getString("provider_no") ;
	  bFistL = true;
	  out.println("</table> <p>") ;
	}
	if(bFistL) {
	  bFistL = false;
    bodd = false ;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0" ><tr> 
<td><%=providerBean.getProperty(rsdemo.getString("provider_no")) + " - " +sdate + " " + edate%>  </td>
<td align="right"></td>
</tr></table>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1" cellpadding="0" > 
<tr bgcolor="#CCCCFF" align="center">
<TH width="14%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=appointment_date"><bean:message key="report.reportdaysheet.msgAppointmentDate"/></a></b></TH>
<TH width="10%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=start_time"><bean:message key="report.reportdaysheet.msgAppointmentTime"/></a></b></TH>
<TH width="20%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=last_name"><bean:message key="report.reportdaysheet.msgPatientLastName"/></a> </b></TH>
<TH width="20%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=first_name"><bean:message key="report.reportdaysheet.msgPatientFirstName"/></a> </b></TH>
<TH width="10%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=chart_no"><bean:message key="report.reportdaysheet.msgChartNo"/></a></b></TH>
<TH width="6%"><b><a href="reportdaysheet.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&edate=<%=edate%>&orderby=roster_status"><bean:message key="report.reportdaysheet.msgRosterStatus"/></a></b></TH>
<TH width="20%"><b><bean:message key="report.reportdaysheet.msgComments"/></b></TH>yy
</tr>
<%
    }
%> 
<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
      <td align="center" nowrap><%=rsdemo.getString("appointment_date")%></td>
      <td align="center" nowrap title="<%="End Time: "+rsdemo.getString("end_time")%>"><%=rsdemo.getString("start_time")%></td>
      <td align="center"><%=Misc.toUpperLowerCase(rsdemo.getString("last_name"))%></td>
      <td align="center"><%=Misc.toUpperLowerCase(rsdemo.getString("first_name"))%></td>
      <td align="center"><%=rsdemo.getString("chart_no")%></td>
      <td align="center"><%=rsdemo.getString("roster_status")%></td>
      <td><%=rsdemo.getString("reason")%>&nbsp;</td>
</tr>
<%
  }
  daySheetBean.closePstmtConn();
%> 

</table>
</body>
</html:html>
