<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@page import="java.util.*"%>

<%
	String searchBy = "searchByName";
	if(request.getAttribute("searchBy")!=null) {
		searchBy = (String)request.getAttribute("searchBy");
	}
	
	String name = (String)request.getAttribute("name");
	String specialty = (String)request.getAttribute("specialty");
	String addressQ = (String)request.getAttribute("address");
	Boolean checked = (Boolean)request.getAttribute("showHidden");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Referral Doctor</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>

<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<script>

function popupOscarRx(vheight,vwidth,varpage) {
	var page = varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
	var popup=window.open(varpage, "ps_add", windowprops);
	if (popup != null) {
		if (popup.opener == null) {
			popup.opener = self;
		}
		popup.focus();
	}
}


function openAddSpecialist() {
	popupOscarRx(625,1024,'../oscarEncounter/oscarConsultationRequest/config/AddSpecialist.jsp');
	return false;
}

function openEditSpecialist(specId) {
	popupOscarRx(625,1024,'../oscarEncounter/EditSpecialists.do?specId='+specId);
}

function checkUncheck(referralId) {
	var checked = $("input[name^='checked_"+referralId+"']").prop("checked");

    $.getJSON("<%= request.getContextPath() %>/admin/ManageBillingReferral.do?method=modifyBatch&id=" + referralId + "&checked=" + checked,
            function(data,textStatus){
             updateCheckedList(data);
   	});	
}

function clearCheckedLabels(referralId) {
    $.getJSON("<%= request.getContextPath() %>/admin/ManageBillingReferral.do?method=modifyBatch&clear=true",
            function(data,textStatus){
              updateCheckedList(data);
   	});
}

function printAllCheckedLabels() {
	$("#checked_items_tbl tbody tr").remove();
	$("#checked_items_tbl tbody").append("<tr><td>Processing. Refresh to get updated list</td></tr>");
	location.href='<%=request.getContextPath() %>/printReferralLabelAction.do?useCheckList=true';
}

function updateCheckedList(data) {
	$("#checked_items_tbl tbody tr").remove();
	
	if(data == null || data.length == 0) {
		$("#checked_items_tbl tbody").append("<tr><td>-None-</td></tr>");		
	}
	for(var x=0;x<data.length;x++) {
		$("#checked_items_tbl tbody").append("<tr><td>"+data[x].formattedName+"</td></tr>");
    }
}

function clearMe() {
	$("#nameQuery").val('');
	$("#specialtyQuery").val('');
	$("#showHidden").prop('checked',false);
	$("#addressQuery").val('');
}
</script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage Referral Doctors</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">&nbsp;
		</td>
		<td class="MainTableRightColumn" valign="top">


<nested:form action="/admin/ManageBillingReferral">
	 <nested:hidden property="method" value="advancedSearch"/>
	 <input type="text" name="nameQuery" id="nameQuery" placeholder="Name or ReferralId" value="<%=(name != null)?name:""%>">
	 &nbsp;
	 <input type="text" name="specialtyQuery" id="specialtyQuery" placeholder="Specialty" value="<%=(specialty != null)?specialty:""%>">
	  &nbsp;
	 <input type="text" name="addressQuery" id="addressQuery" placeholder="Address" value="<%=(addressQ != null)?addressQ:""%>">
	  &nbsp;
	  Include hidden:
	  <input type="checkbox" name="showHidden" id="showHidden" <%=(checked!=null&&checked)?" checked=\"checked\" ":"" %> />
    
	<nested:submit style="border:1px solid #666666;">Search</nested:submit>
	<nested:submit style="border:1px solid #666666;" onclick="clearMe()">Clear</nested:submit>
    <nested:submit style="border:1px solid #666666;" onclick="return openAddSpecialist()">Add</nested:submit>
</nested:form>
<br/>
<%
	if(request.getAttribute("referrals") == null) {
	%>
		<h3 style="color:red">No results found</h3>
	<%	
	} else {
%>
<display:table name="referrals" id="referral" class="its" pagesize="15" style="border:1px solid #666666; width:99%;margin-top:2px;" requestURI="ManageBillingReferral.do?method=list">
	<%
    	ProfessionalSpecialist	ps = (ProfessionalSpecialist)pageContext.getAttribute("referral");
	
		String linkName = ps.getReferralNo();
		if(oscar.util.StringUtils.isNullOrEmpty(ps.getReferralNo())) {
			linkName = "N/A";
		}
    %>
    	
    <display:column><input type="checkbox" name="checked_${referral.id}" onChange="checkUncheck('${referral.id}')"/></display:column>
    <display:column><a href="javascript:void(0)" onclick="openEditSpecialist('${referral.id}')"><%=linkName %></a></display:column>
    <display:column property="firstName" title="First Name" />
    <display:column property="lastName" title="Last Name" />
    <display:column property="specialtyType" title="Specialty" />
    <display:column title="Address">
    	
    	<%=ps.getStreetAddress().replaceAll("\\n", "<br/>") %>
    </display:column>
    <display:column property="phoneNumber" title="Phone" />
    <display:column property="faxNumber" title="Fax" />
    <display:column title="Label" url="/printReferralLabelAction.do" paramId="billingreferralNo" paramProperty="id">label</display:column>
</display:table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn">
			<div id="checked_items">
				<br/>
				<h3>Selected Specialists:</h3>
				<br/>
				<table id="checked_items_tbl">
					<tbody>
					<%
						List<ProfessionalSpecialist> checkedSpecs = (List<ProfessionalSpecialist>)session.getAttribute("billingReferralAdminCheckList");
					if(checkedSpecs != null && checkedSpecs.size()>0) {
						for(ProfessionalSpecialist ps:checkedSpecs) {
					%>
						<tr>
							<td><%=StringEscapeUtils.escapeHtml(ps.getFormattedName()) %></td>
						</tr>
					<%} } else { %>
						<tr>
							<td>-None-</td>
						</tr>
					<% } %>
					</tbody>
				</table>
			</div>
			<br/>
			<input type="button" value="Generate Labels" onClick="printAllCheckedLabels()"/>
			<input type="button" value="Clear List" onClick="clearCheckedLabels()"/>
			
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
<% } %>
</html:html>
