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
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";

  String strLimit1="0";
  String strLimit2="15000";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  String startDate =null, endDate=null;
  if(request.getParameter("startDate")!=null) startDate = request.getParameter("startDate");
  if(request.getParameter("endDate")!=null) endDate = request.getParameter("endDate");
%>
<%@ page import="java.util.*, java.sql.*" errorPage="../errorpage.jsp"%>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="providerNameBean" class="java.util.Properties"
	scope="page" />

<%
  String [][] dbQueries=new String[][] {
{"select_maxformar_id", "select max(ID) from formAR where c_finalEDB >= ? and c_finalEDB <= ? group by demographic_no"  },
{"select_formar", "select ID, demographic_no, c_finalEDB, c_pName, pg1_age, c_gravida, c_term, pg1_homePhone, provider_no from formAR where c_finalEDB >= ? and c_finalEDB <= ? order by c_finalEDB desc limit ? offset ?"  },
{"select_backwardscompatible", "(select ID, demographic_no, c_finalEDB, c_pName, pg1_age, c_gravida, c_term, pg1_homePhone, provider_no from formAR where c_finalEDB >= ? and c_finalEDB <= ?) union " +
                               "(select id as ID,demographic_no, edb as c_finalEDB, patient_name as c_pName, age as pg1_age, gravida as c_gravida, term as c_term, phone as pg1_homePhone, provider_no from edbrept where edb >= ? and edb <= ?) order by c_finalEDB desc limit ? offset ?"  },
{"search_provider", "select provider_no, last_name, first_name from provider order by last_name"},
{"select_patientStatus", "select patient_status, provider_no from demographic where demographic_no = ?"  },
  };
  reportMainBean.doConfigure(dbQueries);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="report.reportnewdblist.title" /></title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css">

<script language="JavaScript">
function loadPage() {
   setfocus();
}
//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body onLoad="loadPage()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica"><bean:message
			key="report.reportnewdblist.msgEDBList" /></font></th>
	</tr>
	<tr>
		<td align="right"><input type="button" name="Button"
			value="<bean:message key="global.btnPrint"/>"
			onClick="window.print()"> <input type="button" name="Button"
			value="<bean:message key="global.btnCancel" />"
			onClick="window.close()">
		</th>
	</tr>
</table>
<script type="text/javascript"
	src="../commons/scripts/sort_table/css.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/common.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/standardista-table-sorting.js"></script>
<CENTER>
<table class="sortable" id="table-1" width="100%" border="0"
	bgcolor="silver" cellspacing="2" cellpadding="2">
	<thead>
		<tr bgcolor='<%=deepcolor%>'>
			<th style="text-decoration: bold">#</th>
			<th style="text-decoration: bold" width="10%" nowrap><bean:message
				key="report.reportnewdblist.msgEDB" /></th>
			<TH style="text-decoration: bold" align="center" width="30%"><bean:message
				key="report.reportnewdblist.msgName" /></TH>
			<!--TH align="center" width="20%"><b>Demog' No </b></TH-->
			<TH style="text-decoration: bold" align="center" width="5%"><bean:message
				key="report.reportnewdblist.msgAge" /></TH>
			<TH style="text-decoration: bold" align="center" width="5%"><bean:message
				key="report.reportnewdblist.msgGravida" /></TH>
			<TH style="text-decoration: bold" align="center" width="10%"><bean:message
				key="report.reportnewdblist.msgTerm" /></TH>
			<TH style="text-decoration: bold" align="center" width="10%"><bean:message
				key="report.reportnewdblist.msgPhone" /></TH>
			<TH style="text-decoration: bold" align="center"><bean:message
				key="report.reportnewdblist.msGP" /></TH>
			<TH style="text-decoration: bold" align="center"><bean:message
				key="report.reportnewdblist.msProvider" /></TH>

		</tr>
	</thead>
	<tfoot></tfoot>
	<tbody>
		<%
        ResultSet rs=null ;
        rs = reportMainBean.queryResults("search_provider");
        while (rs.next()) {
        providerNameBean.setProperty(reportMainBean.getString(rs,"provider_no"), new String( reportMainBean.getString(rs,"last_name")+","+reportMainBean.getString(rs,"first_name") ));
        }
        Properties arMaxId = new Properties();
        String[] paramI =new String[2];
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDate.substring(0,4)), Integer.parseInt(startDate.substring(5,startDate.lastIndexOf('-'))) , Integer.parseInt(startDate.substring(startDate.lastIndexOf('-')+1)) );
        cal.add(Calendar.YEAR,-1);
        paramI[0]=sdf.format(cal.getTime());
        cal.set(Integer.parseInt(endDate.substring(0,4)), Integer.parseInt(endDate.substring(5,endDate.lastIndexOf('-'))) , Integer.parseInt(endDate.substring(endDate.lastIndexOf('-')+1)) );
        cal.add(Calendar.YEAR, 1);
        paramI[1]=sdf.format(cal.getTime());
        rs = reportMainBean.queryResults(paramI, "select_maxformar_id");
        while (rs.next()) {
        arMaxId.setProperty(""+rs.getInt("max(ID)"), "1");
        }
        
        Properties demoProp = new Properties();
        
        String[] param =new String[2];
        param[0]=startDate; 
        param[1]=endDate; 
        String[] paramb = new String[4];
        paramb[0]=startDate; 
        paramb[1]=endDate; 
        paramb[2]=startDate; 
        paramb[3]=endDate; 
        int[] itemp1 = new int[2];
        itemp1[1] = Integer.parseInt(strLimit1);
        itemp1[0] = Integer.parseInt(strLimit2);
        boolean bodd=false;
        int nItems=0;
        try {  // First try the version which also checks the edbrept table.  This will throw an exception if the edbrept table does not exist...
        rs = reportMainBean.queryResults(paramb,itemp1, "select_backwardscompatible");
        } catch (Exception e) {  // ...in which case we go with the standard version
        rs = reportMainBean.queryResults(param,itemp1, "select_formar");
        }
        while (rs.next()) {
        if (!arMaxId.containsKey(""+rs.getInt("ID")) ) continue;
        if (demoProp.containsKey(reportMainBean.getString(rs,"demographic_no")) ) continue;
        else demoProp.setProperty(reportMainBean.getString(rs,"demographic_no"), "1");
        
        // filter the "IN" patient from the list.
        String providerNo = "0";
        ResultSet rs1=reportMainBean.queryResults(reportMainBean.getString(rs,"demographic_no"), "select_patientStatus");
        if (rs1.next()) {
        if(rs1.getString("patient_status").equals("IN")) continue;
        providerNo = rs1.getString("provider_no");
        }
        
        bodd=bodd?false:true; //for the color of rows
        nItems++;
        
        %>
		<tr bgcolor="<%=bodd?weakcolor:"white"%>">
			<td><%=nItems%></td>
			<td align="center" nowrap><%=reportMainBean.getString(rs,"c_finalEDB")!=null?reportMainBean.getString(rs,"c_finalEDB").replace('-','/'):"----/--/--"%></td>
			<td><%=reportMainBean.getString(rs,"c_pName")%></td>
			<!--td align="center" ><%=reportMainBean.getString(rs,"demographic_no")%> </td-->
			<td><%=reportMainBean.getString(rs,"pg1_age")%></td>
			<td><%=reportMainBean.getString(rs,"c_gravida")%></td>
			<td><%=reportMainBean.getString(rs,"c_term")%></td>
			<td nowrap><%=reportMainBean.getString(rs,"pg1_homePhone")%></td>
			<td><%=providerNameBean.getProperty(providerNo, "")%></td>
			<td><%=providerNameBean.getProperty(reportMainBean.getString(rs,"provider_no"), "")%></td>
		</tr>
		<% 
        }
        %>

	</tbody>
</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportnewdblist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportnewdblist.msgNextPage" /></a> <%
}
%>

</body>
</html:html>
