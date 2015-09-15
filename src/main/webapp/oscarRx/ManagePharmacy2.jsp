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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="oscar.oscarRx.pageUtil.*,oscar.oscarRx.data.*,java.util.*"%>
	
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="ManagePharmacy.title" /></title>
<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>

<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");

if (request.getParameter("ID") != null && request.getParameter("type")!=null && request.getParameter("type").equals("Delete")){
	RxPharmacyData rxp = new RxPharmacyData();
	rxp.deletePharmacy(request.getParameter("ID"));
	response.sendRedirect(request.getContextPath() + "/oscarRx/SelectPharmacy2.jsp");
	return;
}
%>

<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksNoEditFavorites2.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug3.jsp"> <bean:message
					key="SearchDrug.title" /></a></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><bean:message
					key="ManagePharmacy.title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead">
				<% if (request.getParameter("ID") ==  null){ %> <bean:message
					key="ManagePharmacy.subTitle.add" /> <%}else{%> <bean:message
					key="ManagePharmacy.subTitle.update" /> <%}%>
				</div>
				</td>
			</tr>
			<tr>
				<td><html:form action="/oscarRx/managePharmacy2">
					<%
                            if (request.getParameter("ID") != null){
                               RxManagePharmacyForm frm = (RxManagePharmacyForm) request.getAttribute("RxManagePharmacyForm");
                               String ID = request.getParameter("ID");
                               RxPharmacyData pharmacy = new RxPharmacyData();
                               org.oscarehr.common.model.PharmacyInfo ph = pharmacy.getPharmacy(ID);
                               frm.setID(ID);
                               frm.setAddress(ph.getAddress());
                               frm.setCity(ph.getCity());
                               frm.setEmail(ph.getEmail());
                               frm.setFax(ph.getFax());
                               frm.setName(ph.getName());
                               frm.setNotes(ph.getNotes());
                               frm.setPhone1(ph.getPhone1());
                               frm.setPhone2(ph.getPhone2());
                               frm.setPostalCode(ph.getPostalCode());
                               frm.setProvince(ph.getProvince());
                               frm.setServiceLocationIdentifier(ph.getServiceLocationIdentifier());
                            }%>
					<table>
						<tr>
							<td>
							<%String type = request.getParameter("type"); %>
                            <html:hidden property="pharmacyAction" value="<%=type%>"/>
							<html:hidden property="pharmacyAction" value="<%=type%>" />
								 <html:hidden property="ID" /> <bean:message
								key="ManagePharmacy.txtfld.label.pharmacyName" /> :</td>
							<td><html:text property="name" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.address" />
							:</td>
							<td><html:text property="address" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.city" />
							:</td>
							<td><html:text property="city" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.province" />
							:</td>
							<td><html:text property="province" /></td>
						</tr>
						<tr>
							<td><bean:message
								key="ManagePharmacy.txtfld.label.postalCode" /> :</td>
							<td><html:text property="postalCode" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.phone1" />
							:</td>
							<td><html:text property="phone1" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.phone2" />
							:</td>
							<td><html:text property="phone2" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.fax" /> :
							</td>
							<td><html:text property="fax" /></td>
						</tr>
						<tr>
							<td><bean:message key="ManagePharmacy.txtfld.label.email" />
							:</td>
							<td><html:text property="email" /></td>
						</tr>
                                                <tr>
                                                    <td><bean:message key="ManagePharmacy.txtfld.label.serviceLocationIdentifier" /> :
                                                    </td>
                                                    <td><html:text property="serviceLocationIdentifier" /></td>
                                                </tr>

						<tr>
							<td colspan="2"><bean:message
								key="ManagePharmacy.txtfld.label.notes" /> :</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><html:textarea property="notes" /></td>
						</tr>

						<tr>
                                                    <td><input type="submit"
								value="<bean:message key="ManagePharmacy.submitBtn.label.submit"/>" />
							</td>
						</tr>
					</table>
				</html:form></td>
			</tr>

			<tr>
				<td>
				<%
                        String sBack="SearchDrug3.jsp";
                      %> <input type=button class="ControlPushButton"
					onclick="javascript:window.location.href='<%=sBack%>';"
					value="Back to Search Drug" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>

</body>

</html:html>
