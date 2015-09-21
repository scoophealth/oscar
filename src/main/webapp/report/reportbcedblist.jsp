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
String strLimit2="5000";  
if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

String startDate =null, endDate=null;
if(request.getParameter("startDate")!=null) startDate = request.getParameter("startDate");  
if(request.getParameter("endDate")!=null) endDate = request.getParameter("endDate");
%>

<%@ page import="java.util.*, java.sql.*" errorPage="../errorpage.jsp"%>

<jsp:useBean id="providerNameBean" class="java.util.Properties" scope="page" />

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.forms.FormsDao" %>
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	FormsDao formsDao = SpringUtils.getBean(FormsDao.class);
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
    
  boolean bodd=false;
  int nItems=0;
  
  for(Object[] result : formsDao.selectBcFormAr(startDate, endDate, Integer.parseInt(strLimit1), Integer.parseInt(strLimit2))) {
 	String demographicNo = ((Integer)result[0]).toString();
 	String cEDD = (String)result[1];
 	String surname = (String)result[2];
 	String givenName  = (String)result[3];
 	String pg1_ageAtEDD = (String)result[4];
 	String dob = (String)result[5];
 	String langPref = (String)result[6];
 	String phn = (String)result[7];
 	String gravida = (String)result[8];
 	String term = (String)result[9];
 	String phone = (String)result[10];
 	String doula = (String)result[12];
 	String doulaNo = (String)result[13];
 	
 	
 	
    if (demoProp.containsKey(demographicNo) ) continue;
    else demoProp.setProperty(demographicNo, "1");
    bodd=bodd?false:true; //for the color of rows
    nItems++; 
%>
	<tr bgcolor="<%=bodd?weakcolor:"white"%>">
		<td align="center" nowrap><%=cEDD!=null?cEDD.replace('-','/'):"----/--/--"%></td>
		<td><%=surname + ", " + givenName%></td>
		<!--td align="center" ><%=demographicNo%> </td-->
		<td><%=dob!=null?dob:""%></td>
		<td><%=gravida!=null?gravida:""%></td>
		<td><%=term!=null?term:""%></td>
		<td nowrap><%=phone%></td>
		<td><%=langPref%></td>
		<td><%=phn%></td>
		<td><%=doula%></td>
		<td><%=doulaNo%></td>
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
