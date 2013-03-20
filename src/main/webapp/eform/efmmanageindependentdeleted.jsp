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

<%@page import="java.util.*,oscar.eform.*"%>
<%@page import="org.oscarehr.web.eform.EfmPatientFormList"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
	String country = request.getLocale().getCountry();
	
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	String orderByRequest = request.getParameter("orderby");
	String orderBy = "";
	if (orderByRequest == null) orderBy = EFormUtil.DATE;
	else orderBy = orderByRequest;
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><bean:message key="eform.showmyform.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/eformStyle.css">
<script type="text/javascript">
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

function checkSelectBox() {
    var selectVal = document.forms[0].group_view.value;
    if (selectVal == "default") {
        return false;
    }
}
</script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="eform.showmyform.msgMyForm" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message key="eform.showmyform.msgFormLybrary" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="eform" key="app.top1"/> | <a
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
                <a href="../admin/admin.jsp"><bean:message key="eform.independent.btnBack" /></a><br>
                <a href="efmmanageindependent.jsp"><bean:message key="eform.independent.btnCurrent"/></a><br/>
                <a href="efmmanageindependentdeleted.jsp"><bean:message key="eform.independent.btnDeleted"/></a>
		</td>
		<td class="MainTableRightColumn" valign="top">

				<table class="elements" style="width:100%">
					<tr bgcolor=<%=deepColor%>>
						<th>
							<a href="efmmanageindependent.jsp?orderby=<%=EFormUtil.NAME%>">
								<bean:message key="eform.showmyform.btnFormName" />
							</a>
						</th>
						<th><a
							href="efmmanageindependent.jsp?orderby=<%=EFormUtil.SUBJECT%>"><bean:message
							key="eform.showmyform.btnSubject" /></a></th>
						<th><a
							href="efmmanageindependent.jsp?orderby=<%=EFormUtil.PROVIDER%>"><bean:message
							key="eform.showmyform.btnFormProvider" /></a></th>
						<th><a
							href="efmmanageindependent.jsp"><bean:message
							key="eform.showmyform.formDate" /></a></th>
						<th><bean:message key="eform.showmyform.msgAction" /></th>
					</tr>
					<%
						ArrayList<HashMap<String,? extends Object>> eForms;
						eForms = EFormUtil.listPatientIndependentEForms(orderBy, EFormUtil.DELETED);
						
						for (int i = 0; i < eForms.size(); i++)
						{
							HashMap<String,? extends Object> curform = eForms.get(i);
					%>
					<tr bgcolor="<%=((i % 2) == 1)?"#F2F2F2":"white"%>">
						<td><a href="#"
							ONCLICK="popupPage('efmshowform_data.jsp?fdid=<%=curform.get("fdid")%>', '<%="FormP" + i%>'); return false;"
							TITLE="<bean:message key="eform.showmyform.msgViewFrm"/>"
							onmouseover="window.status='<bean:message key="eform.showmyform.msgViewFrm"/>'; return true"><%=curform.get("formName")%></a></td>
						<td><%=curform.get("formSubject")%></td>
						<td align='center'><%=providerDao.getProviderNameLastFirst((String)curform.get("providerNo"))%></td>
						<td align='center'><%=curform.get("formDate")%></td>
						<td align='center'><a
							href="../eform/unRemoveEForm.do?callpage=independent&fdid=<%=curform.get("fdid")%>" onClick="javascript: return confirm('Are you sure you want to restore this eform?');"><bean:message
							key="global.btnRestore" /></a></td>
					</tr>
					<%
						}
							if (eForms.size() <= 0)
							{
					%>
					<tr>
						<td align='center' colspan='5'><bean:message
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
