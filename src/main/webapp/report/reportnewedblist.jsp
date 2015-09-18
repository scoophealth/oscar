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

<%
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
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.forms.FormsDao" %>

<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	FormsDao formsDao = SpringUtils.getBean(FormsDao.class);
%>


<jsp:useBean id="providerNameBean" class="java.util.Properties" scope="page" />


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
		for(Provider p :providerDao.getActiveProviders()) {
			providerNameBean.setProperty(p.getProviderNo(), p.getFormattedName());
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
        
        
        for( Integer obj :formsDao.select_maxformar_id2(paramI[0],paramI[1]) ) {        
        	arMaxId.setProperty(obj.toString(),"1");
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
        List<Object[]> results  = formsDao.select_formar2(startDate, endDate, Integer.parseInt(strLimit2), Integer.parseInt(strLimit1));
        
        
        for (Object[] result : results) {
        	Integer id = (Integer)result[0];
        	Integer demographicNo = (Integer)result[1];
        	java.util.Date finalEdb1 = (java.util.Date)result[2];
        	String finalEdb = oscar.util.ConversionUtils.toDateString(finalEdb1);
        	String name= (String)result[3];
        	String age= (String)result[4];
        	String gravida= (String)result[5];
        	String term= (String)result[6];
        	String phone = (String)result[7];
        	String prov = (String)result[8];
        	
        	
        if (!arMaxId.containsKey(""+id) ) {            
            continue;
        }
        if (demoProp.containsKey(demographicNo) ) {            
            continue;
        }
        else demoProp.setProperty(""+demographicNo, "1");
        
        // filter the "IN" patient from the list.
        String providerNo = "0";
        Demographic d = demographicDao.getDemographicById(demographicNo);
        if(d != null) {
        	if(d.getPatientStatus().equals("IN")) 
        		continue;
            providerNo = d.getProviderNo();
        }
        
        bodd=bodd?false:true; //for the color of rows
        nItems++;
        
        %>
		<tr bgcolor="<%=bodd?weakcolor:"white"%>">
			<td><%=nItems%></td>
			<td align="center" nowrap><%=finalEdb!=null?finalEdb.replace('-','/'):"----/--/--"%></td>
			<td><%=name%></td>
			<!--td align="center" ><%=demographicNo%> </td-->
			<td><%=age%></td>
			<td><%=gravida%></td>
			<td><%=term%></td>
			<td nowrap><%=phone%></td>
			<td><%=providerNameBean.getProperty(providerNo, "")%></td>
			<td><%=providerNameBean.getProperty(prov, "")%></td>
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
