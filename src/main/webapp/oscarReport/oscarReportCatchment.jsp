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
<%      
  String user_no = (String) session.getAttribute("user");
  int  nItems=0;  
     String strLimit1="0"; 
    String strLimit2="50";
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  boolean bodd = true;
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ include file="../admin/dbconnection.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String clinic="";
  String clinicview = oscarVariables.getProperty("clinic_view");
   
  int[] itemp1 = new int[2];
  itemp1[0] = Integer.parseInt(strLimit1);
  itemp1[1] = Integer.parseInt(strLimit2);
  
  
  %>
<% 
  	int flag = 0, rowCount=0;
  String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarReport.oscarReportCatchment.title" /></title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--
 

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}


function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"><input type='button' name='print'
			value='<bean:message key="global.btnPrint"/>'
			onClick='window.print()'></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Report - PCN Catchment Area</font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#666699">
		<td width="23%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgDemographic" /></font></strong></div>
		</td>
		<td width="4%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgSex" /></font></strong></div>
		</td>
		<td width="11%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgDOB" /></font></strong></div>
		</td>
		<td width="15%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgCity" /></font></strong></div>
		</td>
		<td width="17%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgProvince" /></font></strong></div>
		</td>
		<td width="19%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgPostal" /></font></strong></div>
		</td>
		<td width="11%">
		<div align="center"><strong><font color="#CCCCFF"
			size="2" face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><bean:message
			key="oscarReport.oscarReportCatchment.msgStatus" /></font></strong></div>
		</td>
	</tr>
	<%
  
     for(Demographic d: demographicDao.search_catchment("RO", Integer.parseInt(strLimit1),Integer.parseInt(strLimit2))){  
    bodd=bodd?false:true; //for the color of rows 
    nItems++; 
%>
	<tr bgcolor="<%=bodd?"#EEEEEE":"white"%>">
		<td>
		<div align="left"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"> <%=d.getLastName()%>,<%=d.getFirstName()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getSex()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getDateOfBirth()%>-<%=d.getMonthOfBirth()%>-<%=d.getYearOfBirth()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getCity()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getProvince()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getPostal()%></font></div>
		</td>
		<td>
		<div align="center"><font size="2"
			face="Tahoma, Verdana, Arial, Helvetica, sans-serif"><%=d.getPatientStatus()%></font></div>
		</td>
	</tr>
	<%  }
%>

	<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
	<a
		href="oscarReportCatchment.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
		key="oscarReport.oscarReportCatchment.msgLastPage" /></a>
	|
	<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
	<a
		href="oscarReportCatchment.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
	<bean:message key="oscarReport.oscarReportCatchment.msgNextPage" /></a>
	<%
}
%>
</table>
</body>
</html:html>
