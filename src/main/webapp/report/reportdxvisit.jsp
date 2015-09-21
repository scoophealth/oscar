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

<%@page import="org.oscarehr.common.dao.BillingDao"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.common.dao.DxresearchDAO"%>
<%@page import="org.oscarehr.common.model.Ichppccode"%>
<%@page import="org.oscarehr.common.dao.IchppccodeDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
  //reportdxvisit.jsp?sdate=2003-04-01&edate=2003-12-31
  //
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("b.billing_date") ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, oscar.oscarDB.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT LIST</title>
<meta http-equiv=Expires content=-1>
<!--link rel="stylesheet" href="../web.css" -->
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<%
out.flush();

// get total patientNum/rosterNR/rosterRO/sexF/sexM/
String sdate = request.getParameter("sdate")!=null?request.getParameter("sdate") : "2003-04-01" ;
String edate = request.getParameter("edate")!=null?request.getParameter("edate") : "2003-12-31" ;
Vector vNurse = new Vector();
Vector vNurseNo = new Vector();
Vector vServiceCode = new Vector();
Vector vServiceDesc = new Vector();
Vector vBillingDx = new Vector();
Properties props = new Properties();

// get nurse name list
ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
for(org.oscarehr.common.model.Provider p : dao.getProvidersByTypePattern("nurse%")) {
	vNurse.add(p.getLastName() + ", " + p.getFirstName());
	vNurseNo.add(p.getProviderNo());
}

// get dx code list
IchppccodeDao iDao = SpringUtils.getBean(IchppccodeDao.class);
for(Ichppccode c : iDao.findAll()) { 
	vServiceCode.add("" + c.getId());
	vServiceDesc.add(c.getDescription());
	vBillingDx.add(c.getDiagnosticCode());
}

DxresearchDAO dDao = SpringUtils.getBean(DxresearchDAO.class);
BillingDao bDao = SpringUtils.getBean(BillingDao.class);
for (int i = 0; i < vServiceCode.size(); i++) {
	Integer count = dDao.countResearches(String.valueOf(vServiceCode.get(i)), ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate));
	// get total vis
	props.setProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i), "" + count);

	// get pat num for Nurses
	for (int j = 0; j < vNurseNo.size(); j++) {
		Integer researchCount = dDao.countBillingResearches(String.valueOf(vServiceCode.get(i)), String.valueOf(vBillingDx.get(i)), String.valueOf(vNurseNo.get(j)), ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate));
		props.setProperty(vServiceCode.get(i) + "patNurse" + j + vServiceDesc.get(i), "" + researchCount);
		
		Integer billingCount = bDao.countBillings(String.valueOf(vBillingDx.get(i)), String.valueOf(vNurseNo.get(j)), ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate));
		props.setProperty(vServiceCode.get(i) + "visNurse" + j + vServiceDesc.get(i), "" + billingCount);
	}
}

%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor=<%=deepcolor%>>
		<th><font face="Helvetica">PATIENT VISIT LIST </font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="Print" onClick="window.print()"><input type="button"
			name="Button" value=" Exit " onClick="window.close()"></th>
	</tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td>Period: (<%=sdate%> ~ <%=edate%>)</td>
	</tr>
</table>

<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1"
	cellpadding="0">
	<tr bgcolor=<%=deepcolor%> align="center">
		<TH width="5%">DxCode</TH>
		<TH width="10%">Desc</TH>
		<TH width="6%">Pat_Total</TH>
		<TH width="6%">Vis_Total</TH>

		<!--TH width="6%">Pat_Phys</TH>
<TH width="6%">Vis_Phys</TH>

<TH width="6%">Pat_Res</TH>
<TH width="6%">Vis_Res</TH-->

		<% for (int i = 0; i < vNurseNo.size(); i++) { %>
		<TH width="6%"><%="pat_"+vNurse.get(i)%></TH>
		<TH width="6%"><%="vis_"+vNurse.get(i)%></TH>
		<% } %>
	</tr>

	<% for (int i = 0; i < vServiceCode.size(); i++) { %>
	<tr bgcolor="<%= i%2==0?weakcolor:"white"%>">
		<td align="center"><%=vServiceCode.get(i)%></td>
		<td><%=vServiceDesc.get(i)%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i))%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i))%></td>

		<!--td align="center"><%--=props.getProperty(vServiceCode.get(i) + "patPhy" + vServiceDesc.get(i))--%></td>
      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "visPhy" + vServiceDesc.get(i))--%></td>

      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "patRes" + vServiceDesc.get(i))--%></td>
      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "visRes" + vServiceDesc.get(i))--%></td-->

		<% for (int j = 0; j < vNurseNo.size(); j++) { %>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "patNurse" + j  + vServiceDesc.get(i))%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "visNurse" + j  + vServiceDesc.get(i))%></td>
		<% } %>
	</tr>
	<% } %>


</table>
</body>
</html>
