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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eform" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%  
  String demographic_no = request.getParameter("demographic_no"); 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>

<%@ page import="java.util.*, oscar.eform.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%
String country = request.getLocale().getCountry();
String orderByRequest = request.getParameter("orderby");
String orderBy = "";
if (orderByRequest == null) orderBy = EFormUtil.DATE;
else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
else if (orderByRequest.equals("form_name")) orderBy = EFormUtil.NAME;

String appointment = request.getParameter("appointment");
String parentAjaxId = request.getParameter("parentAjaxId");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.showmyform.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" href="../share/css/eforms.css">
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
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

function updateAjax() {
    var parentAjaxId = "<%=parentAjaxId%>";    
    if( parentAjaxId != "null" ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
        window.opener.updateNeeded = true;    
    }

}
</script>

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
                <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographiceditdemographic.btnMasterFile" /></a>
                
                <br>
                <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>" class="current"> <bean:message key="eform.showmyform.btnAddEForm"/></a><br/>
                <a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a><br/>
                <a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnDeleted"/></a>
                <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>" >
                <br/>
                <a href="#" onclick="javascript: return popup(600, 1200, '../administration/?show=Forms', 'manageeforms');" style="color: #835921;"><bean:message key="eform.showmyform.msgManageEFrm"/></a>
                </security:oscarSec>
		</td>
		
		<td class="MainTableRightColumn" valign="top">
		<table class="elements" width="100%">
			<tr bgcolor=<%=deepColor%>>
				<th><a
					href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&orderby=form_name&parentAjaxId=<%=parentAjaxId%>"><bean:message
					key="eform.showmyform.btnFormName" /></a></th>
				<th><a
					href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&orderby=form_subject&parentAjaxId=<%=parentAjaxId%>"><bean:message
					key="eform.showmyform.btnSubject" /></a></th>
				<th><a
					href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
					key="eform.showmyform.formDate" /></a></th>
				<th><bean:message key="eform.showmyform.msgAction" /></th>
			</tr>
			<%
			ArrayList<HashMap<String, ? extends Object>> forms = EFormUtil.listPatientEForms(LoggedInInfo.getLoggedInInfoFromSession(request), orderBy, EFormUtil.DELETED, demographic_no, null);
			    for (int i=0; i< forms.size(); i++) {
			    	HashMap<String, ? extends Object> curform = forms.get(i);
			%>
			<tr bgcolor="<%= ((i%2) == 1)?"#F2F2F2":"white"%>">
				<td><a href="#"
					ONCLICK="popupPage('efmshowform_data.jsp?fdid=<%= curform.get("fdid")%>', '<%="FormPD" + i%>'); return false;"
					TITLE="View Form"
					onmouseover="window.status='View This Form'; return true"><%=curform.get("formName")%></a></td>
				<td><%=curform.get("formSubject")%></td>
				<td align='center'><%=curform.get("formDate")%></td>
				<td align='center'><a
					href="../eform/unRemoveEForm.do?fdid=<%=curform.get("fdid")%>&demographic_no=<%=demographic_no%>&parentAjaxId=<%=parentAjaxId%>" onClick="javascript: return confirm('Are you sure you want to restore this eform?');"><bean:message
					key="global.btnRestore" /></a></td>
			</tr>
			<%
  }
 if (forms.size() <= 0) {
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
