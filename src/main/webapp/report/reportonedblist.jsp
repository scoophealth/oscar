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

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>

<%
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>


<%
  String [][] dbQueries=new String[][] {
{"select_maxformar_id", "select max(ID) from formONAR where c_finalEDB >= ? and c_finalEDB <= ? group by demographic_no"  },
{"select_formar", "select ID, demographic_no, c_finalEDB, concat(c_lastname,\",\",c_firstname) as c_pName, pg1_age, c_gravida, c_term, pg1_homePhone, provider_no from formONAR where c_finalEDB >= ? and c_finalEDB <= ? order by c_finalEDB desc limit ? offset ?"  },
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
<!--

//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

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
			onClick="window.close()"></td>
	</tr>
</table>
<script type="text/javascript"
	src="../commons/scripts/sort_table/css.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/common.js"></script>
<script type="text/javascript"
	src="../commons/scripts/sort_table/standardista-table-sorting.js"></script>
<CENTER>
<table class="sortable" width="100%" border="0" bgcolor="silver"
	cellspacing="2" cellpadding="2">
	<thead>
		<tr bgcolor='<%=deepcolor%>'>
			<TH style="text-decoration: bold; text-align: center;">#</TH>
			<TH style="text-decoration: bold; text-align: center;" width="10%"
				nowrap><bean:message key="report.reportnewdblist.msgEDB" /></TH>
			<TH style="text-decoration: bold; text-align: center;" width="30%"><bean:message
				key="report.reportnewdblist.msgName" /></TH>
			<!--TH align="center" width="20%"><b>Demog' No </b></TH-->
			<TH style="text-decoration: bold; text-align: center;" width="5%"><bean:message
				key="report.reportnewdblist.msgAge" /></TH>
			<TH style="text-decoration: bold; text-align: center;" width="5%"><bean:message
				key="report.reportnewdblist.msgGravida" /></TH>
			<TH style="text-decoration: bold; text-align: center;" width="10%"><bean:message
				key="report.reportnewdblist.msgTerm" /></TH>
			<TH style="text-decoration: bold; text-align: center;" width="10%"><bean:message
				key="report.reportnewdblist.msgPhone" /></TH>
			<TH style="text-decoration: bold"><bean:message
				key="report.reportnewdblist.msGP" /></TH>
			<TH style="text-decoration: bold; text-align: center;"><bean:message
				key="report.reportnewdblist.msProvider" /></TH>
		</tr>
	</thead>
	<tfoot></tfoot>
	<tbody>
		<%
        for(Provider p: providerDao.getActiveProviders()) {
        providerNameBean.setProperty(p.getProviderNo(), new String(p.getFormattedName() ));
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
        paramI[1]=sdf.format(cal.getTime()); //;
       ResultSet rs = reportMainBean.queryResults(paramI, "select_maxformar_id");
        while (rs.next()) {
        arMaxId.setProperty(""+rs.getInt("max(ID)"), "1");
        }
        Properties demoProp = new Properties();
        
        String[] param =new String[2];
        param[0]=startDate; 
        param[1]=endDate;
        int[] itemp1 = new int[2];
        itemp1[1] = Integer.parseInt(strLimit1);
        itemp1[0] = Integer.parseInt(strLimit2);
        boolean bodd=false;
        int nItems=0;
        rs = reportMainBean.queryResults(param,itemp1, "select_formar");
        while (rs.next()) {
        if (!arMaxId.containsKey(""+rs.getInt("ID")) ) continue;
        if (demoProp.containsKey(reportMainBean.getString(rs,"demographic_no")) ) continue;
        else demoProp.setProperty(reportMainBean.getString(rs,"demographic_no"), "1");
        
        String providerNo = "0";
        // filter the "IN" patient from the list
        Demographic d = demographicDao.getDemographic(reportMainBean.getString(rs,"demographic_no"));
        if (d != null) {
            if(d.getPatientStatus().equals("IN")) continue;
            providerNo =d.getProviderNo();
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
	href="reportonedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportnewdblist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportonedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportnewdblist.msgNextPage" /></a> <%
}
%>

</body>
</html:html>
