<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*,org.oscarehr.hospitalReportManager.model.HRMCategory"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_hrm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_hrm");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	String demographic_no = request.getParameter("demographic_no");
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	String orderBy = request.getParameter("orderBy") != null ?  request.getParameter("orderBy") : "report_date";
	String orderAsc = request.getParameter("orderAsc") != null ?  request.getParameter("orderAsc") : "false";
	boolean asc = new Boolean(orderAsc);
	
	ArrayList<HashMap<String,? extends Object>> hrmdocs;
	hrmdocs = HRMUtil.listHRMDocuments(loggedInInfo,orderBy, asc, demographic_no);
	
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title>HRM Document List</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/eformStyle.css">
<script type="text/javascript" language="javascript">
function popupPage(varpage, windowname) {
    var page = "" + varpage;
    windowprops = "height=700,width=800,location=no,"
    + "scrollbars=yes,menubars=no,status=yes,toolbars=no,resizable=yes,top=10,left=200";
    var popup = window.open(page, windowname, windowprops);
    if (popup != null) {
       if (popup.opener == null) {
          popup.opener = self;
       }
       popup.focus();
    }
}
</script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">HRM</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message key="hrm.displayHRMDocList.displaydocs" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
			</td>
		<td class="MainTableRightColumn" valign="top">
		
				<table class="elements" width="100%">
					
					<tr bgcolor=<%=deepColor%>>
						
						<th>
							<a href="displayHRMDocList.jsp?demographic_no=<%=demographic_no%>&orderBy=report_name&orderAsc=<%=("report_name".equals(orderBy)) ? !asc : "true"%>">
								<bean:message key="hrm.displayHRMDocList.reportType" />
							</a>
						</th>
						<th><bean:message	key="hrm.displayHRMDocList.description" /></th>
						<th><bean:message	key="hrm.displayHRMDocList.reportStatus" /></th>
						<th><a href="displayHRMDocList.jsp?demographic_no=<%=demographic_no%>&orderBy=report_date&orderAsc=<%=("report_date".equals(orderBy)) ? !asc : "false"%>">Report Date</a></th>
						<th><a href="displayHRMDocList.jsp?demographic_no=<%=demographic_no%>&orderBy=time_received&orderAsc=<%=("time_received".equals(orderBy)) ? !asc : "false"%>"><bean:message key="hrm.displayHRMDocList.timeReceived" /></a></th>
						<th><a href="displayHRMDocList.jsp?demographic_no=<%=demographic_no%>&orderBy=category&orderAsc=<%=("category".equals(orderBy)) ? !asc : "true"%>">Category</a></th>
						<th>Class/Subclass/Accompanying Subclass</th>
					</tr>
					<%
						
						
						for (int i = 0; i < hrmdocs.size(); i++)
						{
							HashMap<String,? extends Object> curhrmdoc = hrmdocs.get(i);
					%>
					<tr bgcolor="<%=((i % 2) == 1)?"#F2F2F2":"white"%>">
						
						<td><a href="#"
							ONCLICK="popupPage('<%=request.getContextPath() %>/hospitalReportManager/Display.do?id=<%=curhrmdoc.get("id")%>', 'HRM Report'); return false;"
							><%=curhrmdoc.get("report_type")%></a></td>
						<td><%=curhrmdoc.get("description")%></td>
						<td><%=curhrmdoc.get("report_status")%></td>
						<td align='center'><%=curhrmdoc.get("report_date")%></td>
						<td align='center'><%=curhrmdoc.get("time_received")%></td>
						<td><%=curhrmdoc.get("category") != null ? curhrmdoc.get("category")  : "" %>
						<td><%=curhrmdoc.get("class_subclass") != null ? curhrmdoc.get("class_subclass")  : "" %>
					</tr>
					<%
						}
							if (hrmdocs.size() <= 0)
							{
					%>
					<tr>
						<td align='center' colspan='7'><bean:message
							key="eform.showmyform.msgNoData" /></td>
					</tr>
					<%
						}
					%>
				</table>
				
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
