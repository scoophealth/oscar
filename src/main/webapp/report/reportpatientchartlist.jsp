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

<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"	errorPage="../appointment/errorpage.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils"%>

<%@ page import="org.oscarehr.common.model.MyGroup"%>
<%@ page import="org.oscarehr.common.dao.MyGroupDao"%>

<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>

<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="patientBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Vector" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%
	
	MyGroupDao dao = SpringUtils.getBean(MyGroupDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
	
	String curProvider_no = (String) session.getAttribute("user");
	String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("last_name") ;
	   
	String [][] dbQueries=new String[][] { 
		{"search_patient", "select d.provider_no,c.cust1,c.cust2, d.last_name, d.first_name, d.chart_no from demographic d LEFT JOIN demographiccust c ON d.demographic_no = c.demographic_no where (d.provider_no = ? or c.cust1=? or c.cust2=? ) and (d.patient_status like 'AC' or d.patient_status like 'UHIP') order by "+orderby }, 
	};
	 
	String[][] responseTargets=new String[][] {  };
	patientBean.doConfigure(dbQueries,responseTargets);
	
	boolean isSiteAccessPrivacy=false;
	boolean isTeamAccessPrivacy=false; 
%>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isSiteAccessPrivacy=true; %></security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isTeamAccessPrivacy=true; %></security:oscarSec>

<% 
//List<Demographic> demoList = demographicDao.findDemographicByProviderCust(curProvider_no);

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
<title><bean:message key="report.reportpatientchartlist.title" /></title>
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


<% 
  String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"175" ;
  ResultSet rsdemo = null ;
  boolean bodd = false;
  boolean bGroup = false;
  
  //initial myGroupBean if neccessary
  if(provider_no.startsWith("_grp_")) {
    bGroup = true;
    List<MyGroup> myGroups = dao.getGroupByGroupNo(provider_no.substring(5));
    Collections.sort(myGroups, MyGroup.LastNameComparator);
    for(MyGroup myGroup:myGroups) {
    	myGroupBean.add(myGroup.getId().getProviderNo());
    }
  }
%>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#CCCCFF">
		<th align=CENTER NOWRAP><font face="Helvetica"><bean:message key="report.reportpatientchartlist.msgTitle" /></font></th>
		<th width="10%" nowrap>
                    <input type="button" name="Button" value="<bean:message key="global.btnPrint"/>" onClick="window.print()">
                    <input type="button" name="Button" value="<bean:message key="global.btnExit" />" onClick="window.close()">
                </th>
	</tr>
</table>

<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String [] param = new String[3];
  int pnum = bGroup?myGroupBean.size():1 ;

  for(int i=0; i<pnum; i++) {
    param[0]=bGroup?((String) myGroupBean.get(i)):provider_no;
    param[1]=param[0];
    param[2]=param[0];
	
    rsdemo = patientBean.queryResults(param, "search_patient");
    while (rsdemo.next()) { 
    	
        //multisites. skip record if not belong to same site/team
        if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
        	if(providerMap.get(rsdemo.getString("provider_no"))== null)  continue;
        }    	
    	
      bodd = bodd?false:true;
	    if(!strTemp.equals(param[0]) ) { //new provider for a new table
	      strTemp = param[0] ;
	      bFistL = true;
	      out.println("</table> <p>") ;
	    }
	    if(bFistL) {
	      bFistL = false;
        bodd = false ;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td><%=providerBean.getProperty(strTemp) %></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1"
	cellpadding="0">
	<tr bgcolor="#CCCCFF" align="center">
		<TH width="40%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=last_name"><bean:message
			key="report.reportpatientchartlist.msgLastName" /></a></b></TH>
		<TH width="40%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=first_name"><bean:message
			key="report.reportpatientchartlist.msgFirstName" /></a> </b></TH>
		<TH width="20%"><b><a
			href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=chart_no"><bean:message
			key="report.reportpatientchartlist.msgChart" /></a> </b></TH>
	</tr>
	<%
    }
%>
	<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
		<td>&nbsp;<%=rsdemo.getString("last_name")%></td>
		<td>&nbsp;<%=rsdemo.getString("first_name")%></td>
		<td align="center"><%=rsdemo.getString("chart_no")%></td>
	</tr>
	<%
  }
  }
%>

</table>
</body>
</html:html>
