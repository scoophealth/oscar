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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>

<%@ page import="org.oscarehr.common.model.MyGroup"%>
<%@ page import="org.oscarehr.common.dao.MyGroupDao"%>

<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="patientBean" class="oscar.AppointmentMainBean"	scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Vector" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"	scope="session" />

<%
  
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("last_name") ;
  int age = Integer.parseInt(request.getParameter("age"));

  MyGroupDao dao = SpringUtils.getBean(MyGroupDao.class);
  ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);


  String [][] dbQueries;
      dbQueries = new String[][] { 
      //{"search_patient", "select provider_no, last_name, first_name, chart_no from demographic where provider_no = ? order by "+orderby }, 
      {"search_patient", "select distinct(d.demographic_no), d.last_name, d.first_name, d.sex, d.chart_no, d.patient_status, a.appointment_date, d.address, d.city, d.province, d.postal, DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d') as dob from demographic d, appointment a where d.provider_no = ? and d.demographic_no=a.demographic_no and (d.patient_status like 'AC' or d.patient_status like 'UHIP') and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) -(RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >? order by d.last_name, d.first_name, a.appointment_date desc" }, 
      };

  String[][] responseTargets=new String[][] {  };
  patientBean.doConfigure(dbQueries,responseTargets);

  String curProvider_no = (String) session.getAttribute("user");
    
  boolean isSiteAccessPrivacy=false;
  boolean isTeamAccessPrivacy=false; 
%>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isSiteAccessPrivacy=true; %></security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isTeamAccessPrivacy=true; %></security:oscarSec>

<% 
List<ProviderData> pdList = null;
HashMap<String,String> providerMap = new HashMap<String,String>();

//multisites function
if (isSiteAccessPrivacy || isTeamAccessPrivacy) {

	if (isSiteAccessPrivacy) 
		pdList = providerDataDao.findByProviderSite(curProvider_no);
	
	if (isTeamAccessPrivacy) 
		pdList = providerDataDao.findByProviderTeam(curProvider_no);

	for(ProviderData providerData : pdList) {
		providerMap.put(providerData.getId(), "true");
	}
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportpatientchartlistspecial.title" /></title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}

//-->
</SCRIPT>
</head>

<% 
  String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"175" ;
  ResultSet rsdemo = null ;
  boolean bodd = false;
  boolean bGroup = false;
  
  //initial myGroupBean if neccessary
  if(provider_no.startsWith("_grp_")) {
    bGroup = true;
    List<MyGroup> myGroups = dao.findAll();
    Collections.sort(myGroups, MyGroup.MyGroupNoComparator);
    for(MyGroup myGroup:myGroups) {
    	myGroupBean.add(myGroup.getId().getProviderNo());
    }
  }
%>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#CCCCFF">
		<th align=CENTER NOWRAP><font face="Helvetica"><bean:message
			key="report.reportpatientchartlistspecial.msgTitle" /></font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="<bean:message key="global.btnPrint" />"
			onClick="window.print()"><input type="button" name="Button"
			value="<bean:message key="global.btnExit" />"
			onClick="window.close()"></th>
	</tr>
</table>
<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String [] param = new String[1];
  int pnum = bGroup?myGroupBean.size():1 ;
  int dnoTemp = 0;

  for(int i=0; i<pnum; i++) {
    param[0]=bGroup?((String) myGroupBean.get(i)):provider_no;
    rsdemo = patientBean.queryResults(param,(new int[]{age}), "search_patient");

    while (rsdemo.next()) { 
    	
        //multisites. skip record if not belong to same site/team
        if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
        	if(providerMap.get(rsdemo.getString("provider_no"))== null)  continue;
        }
    	
      if (rsdemo.getInt("demographic_no") != dnoTemp) dnoTemp = rsdemo.getInt("demographic_no");
	  else continue;
      bodd = bodd?false:true;
	    if(!strTemp.equals(param[0]) ) { //new provider for a new table
	      strTemp = param[0] ;
	      bFistL = true;
	      out.println("</table> <p>") ;
	    }
	    if(bFistL) {
	      bFistL = false;
          bodd = false ;
          dnoTemp = 0;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td><%=providerBean.getProperty(strTemp) %></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="1" cellpadding="2">
	<tr bgcolor="#CCCCFF" align="center">
		<TH width="12%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=last_name"><bean:message
			key="report.reportpatientchartlistspecial.btnLastName" /></a></b></TH>
		<TH width="12%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=first_name"><bean:message
			key="report.reportpatientchartlistspecial.btnFisrtName" /></a> </b></TH>
		<TH width="2%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=sex"><bean:message
			key="report.reportpatientchartlistspecial.btnSex" /></a> </b></TH>
		<TH width="5%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=chart_no"><bean:message
			key="report.reportpatientchartlistspecial.btnChart" /></a> </b></TH>
		<TH width="12%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=appointment_date"><bean:message
			key="report.reportpatientchartlistspecial.btnApptDate" /></a> </b></TH>
		<TH width="20%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=address"><bean:message
			key="report.reportpatientchartlistspecial.btnAddress" /></a> </b></TH>
		<TH width="10%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=city"><bean:message
			key="report.reportpatientchartlistspecial.btnCity" /></a> </b></TH>
		<TH width="10%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=postal"><bean:message
			key="report.reportpatientchartlistspecial.btnPostal" /></a> </b></TH>
		<TH width="12%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=dob"><bean:message
			key="report.reportpatientchartlistspecial.btnDOB" /></a> </b></TH>
		<TH width="2%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=patient_status"><bean:message
			key="report.reportpatientchartlistspecial.btnStatus" /></a> </b></TH>
	</tr>
	<%
    }
%>
	<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
		<td><%=rsdemo.getString("last_name")%></td>
		<td><%=rsdemo.getString("first_name")%></td>
		<td><%=rsdemo.getString("sex")%></td>
		<td align="center"><%=rsdemo.getString("chart_no")%></td>
		<td align="center"><%=rsdemo.getString("appointment_date")%></td>
		<td align="center"><%=rsdemo.getString("address")%></td>
		<td align="center"><%=rsdemo.getString("city") + ", " + rsdemo.getString("province")%></td>
		<td align="center"><%=rsdemo.getString("postal")%></td>
		<td align="center"><%=rsdemo.getString("dob")%></td>
		<td align="center"><%=rsdemo.getString("patient_status")%></td>
	</tr>
	<%
  }
  }
%>

</table>
</body>
</html:html>
