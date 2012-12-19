<%
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
String curUser_no = (String) session.getAttribute("user");
String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";

String strLimit1="0";
String strLimit2="5000";  
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
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>

<% 
String [][] dbQueries=new String[][] { 
//{"select_bcformar", "select distinct(demographic_no) from formBCAR where c_EDD >= ? and c_EDD <= ? order by c_EDD desc limit ? offset ?"  }, 
{"select_bcformar", "select demographic_no, c_EDD, c_surname,c_givenName, pg1_ageAtEDD, pg1_dateOfBirth, pg1_langPref, c_phn, pg1_gravida, pg1_term, c_phone, c_phyMid, ar2_doula, ar2_doulaNo, provider_no from formBCAR where c_EDD >= ? and c_EDD <= ? order by c_EDD desc, ID desc  limit ? offset ?"  }, 
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
			key="report.reportnewdblist.msgEDDList" /></font></th>
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

<CENTER>
<table width="100%" border="0" bgcolor="silver" cellspacing="2"
	cellpadding="2">
	<tr bgcolor='<%=deepcolor%>'>
		<TH align="center" width="6%" nowrap><b><bean:message
			key="report.reportnewdblist.msgEDD" /></b></TH>
		<TH align="center" width="20%"><b><bean:message
			key="report.reportnewdblist.msgName" /> </b></TH>
		<!--TH align="center" width="20%"><b>Demog' No </b></TH-->
		<TH align="center" width="9%"><b><bean:message
			key="report.reportnewdblist.msgDOB" /></b></TH>
		<TH align="center" width="5%"><b>G</b><font size="-2">ravida</font></TH>
		<TH align="center" width="5%"><b><bean:message
			key="report.reportnewdblist.msgTerm" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="report.reportnewdblist.msgPhone" /></b></TH>
		<TH align="center" width="10%"><b><bean:message
			key="report.reportnewdblist.msLanguage" /></b></TH>
		<TH align="center" width="8%"><b><bean:message
			key="report.reportnewdblist.msPHN" /></b></TH>
		<TH align="center" width="20%"><b>Doula</b></TH>
		<TH align="center"><b>Doula#</b></TH>
	</tr>
	<%
	for(Provider p : providerDao.getActiveProviders()) {
    providerNameBean.setProperty(p.getProviderNo(), new String( p.getFormattedName()));
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
  ResultSet rs = reportMainBean.queryResults(param,itemp1, "select_bcformar");
  while (rs.next()) {
    if (demoProp.containsKey(reportMainBean.getString(rs,"demographic_no")) ) continue;
    else demoProp.setProperty(reportMainBean.getString(rs,"demographic_no"), "1");
    bodd=bodd?false:true; //for the color of rows
    nItems++; 
%>
	<tr bgcolor="<%=bodd?weakcolor:"white"%>">
		<td align="center" nowrap><%=reportMainBean.getString(rs,"c_EDD")!=null?reportMainBean.getString(rs,"c_EDD").replace('-','/'):"----/--/--"%></td>
		<td><%=reportMainBean.getString(rs,"c_surname") + ", " + reportMainBean.getString(rs,"c_givenName")%></td>
		<!--td align="center" ><%=reportMainBean.getString(rs,"demographic_no")%> </td-->
		<td><%=reportMainBean.getString(rs,"pg1_dateOfBirth")!=null?reportMainBean.getString(rs,"pg1_dateOfBirth"):""%></td>
		<td><%=reportMainBean.getString(rs,"pg1_gravida")!=null?reportMainBean.getString(rs,"pg1_gravida"):""%></td>
		<td><%=reportMainBean.getString(rs,"pg1_term")!=null?reportMainBean.getString(rs,"pg1_term"):""%></td>
		<td nowrap><%=reportMainBean.getString(rs,"c_phone")%></td>
		<!--td><%--=reportMainBean.getString(rs,"c_phyMid")--%><%--=providerNameBean.getProperty(reportMainBean.getString(rs,"provider_no"), "")--%></td-->
		<td><%=reportMainBean.getString(rs,"pg1_langPref")%></td>
		<td><%=reportMainBean.getString(rs,"c_phn")%></td>
		<td><%=reportMainBean.getString(rs,"ar2_doula")%></td>
		<td><%=reportMainBean.getString(rs,"ar2_doulaNo")%></td>
	</tr>
	<%
  }
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportbcedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>"><bean:message
	key="report.reportnewdblist.msgLastPage" /></a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportbcedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
<bean:message key="report.reportnewdblist.msgNextPage" /></a> <%
}
%>

</body>
</html:html>
