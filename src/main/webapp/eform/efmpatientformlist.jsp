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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
	String demographic_no = request.getParameter("demographic_no");
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
	String country = request.getLocale().getCountry();
	String orderByRequest = request.getParameter("orderby");
	String orderBy = "";
	if (orderByRequest == null) orderBy = EFormUtil.DATE;
	else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
	else if (orderByRequest.equals("form_name")) orderBy = EFormUtil.NAME;

	String groupView = request.getParameter("group_view");
	if (groupView == null)
	{
		groupView = "";
	}

	String appointment = request.getParameter("appointment");
	String parentAjaxId = request.getParameter("parentAjaxId");

	boolean isMyOscarAvailable = EfmPatientFormList.isMyOscarAvailable(Integer.parseInt(demographic_no));	
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

function checkSelectBox() {
    var selectVal = document.forms[0].group_view.value;
    if (selectVal == "default") {
        return false;
    }
}

function updateAjax() {
    var parentAjaxId = "<%=parentAjaxId%>";    
    if( parentAjaxId != "null" ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
        window.opener.updateNeeded = true;    
    }

}
</script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
</head>

<body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
<!--  -->
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
		        <%  if (country.equals("BR")) { %>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail_ptbr"><bean:message key="demographic.demographiceditdemographic.btnMasterFile" /></a>
                <%}else{%>
                    <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographiceditdemographic.btnMasterFile" /></a>
                <%}%>
                <br>
                <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>" class="current"> <bean:message key="eform.showmyform.btnAddEForm"/></a><br/>
                <a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a><br/>
                <a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnDeleted"/></a>
                
				<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>" >
                <br/>
                <a href="#" onclick="javascript: return popup(600, 750, '../eform/efmformmanager.jsp', 'manageeforms');" style="color: #835921;"><bean:message key="eform.showmyform.msgManageEFrm"/></a>
                </security:oscarSec>
		
		<jsp:include page="efmviewgroups.jsp">
			<jsp:param name="url" value="../eform/efmpatientformlist.jsp" />
			<jsp:param name="groupView" value="<%=groupView%>" />
			<jsp:param name="patientGroups" value="1" />
			<jsp:param name="parentAjaxId" value="<%=parentAjaxId%>" />
		</jsp:include>
		
		</td>
		<td class="MainTableRightColumn" valign="top">
			<%
				if (isMyOscarAvailable)
				{
			%>
					<form action="efmpatientformlistSendPhrAction.jsp">
						<input type="hidden" name="clientId" value="<%=demographic_no%>" />
						<table class="elements" width="100%">
							<tr bgcolor=<%=deepColor%>>
								<th>&nbsp;</th>
								<th>
									<a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&orderby=form_name&group_view=<%=groupView%>&parentAjaxId=<%=parentAjaxId%>">
										<bean:message key="eform.showmyform.btnFormName" />
									</a>
								</th>
								<th><a
									href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&orderby=form_subject&group_view=<%=groupView%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
									key="eform.showmyform.btnSubject" /></a></th>
								<th><a
									href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&group_view=<%=groupView%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
									key="eform.showmyform.formDate" /></a></th>
								<th><bean:message key="eform.showmyform.msgAction" /></th>
							</tr>
							<%
								ArrayList<HashMap<String,? extends Object>> eForms;
								if (groupView.equals(""))
								{
									eForms = EFormUtil.listPatientEForms(orderBy, EFormUtil.CURRENT, demographic_no, roleName$);
								}
								else
								{
									eForms = EFormUtil.listPatientEForms(orderBy, EFormUtil.CURRENT, demographic_no, groupView, roleName$);
								}
								
								for (int i = 0; i < eForms.size(); i++)
								{
									HashMap<String,? extends Object> curform = eForms.get(i);
							%>
							<tr bgcolor="<%=((i % 2) == 1)?"#F2F2F2":"white"%>">
								<td>
									<input type="checkbox" name="sendToPhr" value="<%=curform.get("fdid")%>" />
								</td>
								<td><a href="#"
									ONCLICK="popupPage('efmshowform_data.jsp?fdid=<%=curform.get("fdid")%>&appointment=<%=appointment%>', '<%="FormP" + i%>'); return false;"
									TITLE="<bean:message key="eform.showmyform.msgViewFrm"/>"
									onmouseover="window.status='<bean:message key="eform.showmyform.msgViewFrm"/>'; return true"><%=curform.get("formName")%></a></td>
								<td><%=curform.get("formSubject")%></td>
								<td align='center'><%=curform.get("formDate")%></td>
								<td align='center'><a
									href="../eform/removeEForm.do?fdid=<%=curform.get("fdid")%>&group_view=<%=groupView%>&demographic_no=<%=demographic_no%>&parentAjaxId=<%=parentAjaxId%>" onClick="javascript: return confirm('Are you sure you want to delete this eform?');"><bean:message
									key="eform.uploadimages.btnDelete" /></a></td>
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
						<input type="submit" value="Send To PHR" />
					</form>
			<%
				}
			%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
