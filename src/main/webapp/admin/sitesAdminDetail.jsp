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
<%-- This JSP is the multi-site admin site detail page --%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="oscar.util.SuperSiteUtil"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="oscar.service.SiteRoleManager" %>
<%@page import="org.oscarehr.common.model.SecRole" %>
<%@page import="org.oscarehr.common.dao.SecRoleDao" %>
<%
	if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	String provider_no=(String) session.getAttribute("user");
	String siteId = (String) request.getParameter("siteId");
	
	String logoError = (String)request.getParameter("logoErrors");
	if(logoError==null || "null".equalsIgnoreCase(logoError))
		logoError = "";
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.Site"%>
<html:html locale="true">
<head>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/javascript/jquery/jquery-ui-1.8.4.custom.css">

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-ui-1.8.4.custom_full.min.js"></script>
<title>Clinic</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
<style>.button {border:1px solid #666666;} </style>
<style type="text/css">
.ui-widget{font-size:12px !important;}
#div_access_roles ul
{
	/*border: 1px solid #999999;*/
    margin: 0 0 4px 10px !important;
    overflow: hidden;
    padding: 3px 4px 0;
}
#div_access_roles ul .li_role
{
	float: left;
	list-style-type: none;
	padding-right: 15px;
	padding-left: 5px;
	 margin-top: 3px;
	background: none repeat scroll 0 0 #DEE7F8;
    border: 1px solid #CAD8F3;
    border-radius: 9px 9px 9px 9px;
    cursor: default;
    line-height: 18px;
    position: relative;
    margin-right: 5px;
}
#div_access_roles ul .li_add_role_btn
{
	float: left;
	list-style-type: none;
	padding-right: 15px;
	padding-left: 5px;
	 margin-top: 3px;
	/*background: none repeat scroll 0 0 #DEE7F8;
    border: 1px solid #CAD8F3;
    border-radius: 9px 9px 9px 9px;*/
    cursor: default;
    line-height: 18px;
    position: relative;
    margin-right: 5px;
}
#div_access_roles ul li a
{
	background: url("../images/delete_role.png") repeat scroll 0 0 transparent !important;
    display: block;
    font-size: 1px;
    height: 7px !important;
    position: absolute;
    right: 4px;
    top: 6px;
    width: 7px !important;
    
}
</style>
</head>
<%
	//SuperSiteUtil superSiteUtil = new SuperSiteUtil();
	SuperSiteUtil superSiteUtil = (SuperSiteUtil) SpringUtils.getBean("superSiteUtil");
	SiteRoleManager siteRoleMgr = new SiteRoleManager();
	
	String currentSiteId = "";
	if(request.getParameter("siteId")!=null && request.getParameter("siteId").trim().length()>0)
		currentSiteId = request.getParameter("siteId").trim();
	else if(request.getAttribute("siteId")!=null && request.getAttribute("siteId").toString().length()>0)
		currentSiteId = request.getAttribute("siteId").toString();
	
	List<SecRole> siteRoleList = null;
	List<SecRole> siteAdmitDischargeRoleList = null;
	
	if(currentSiteId!=null && currentSiteId.length()>0)
	{
		siteRoleList = siteRoleMgr.getAccessRolesAssociatedWithSite(Integer.parseInt(currentSiteId));
		siteAdmitDischargeRoleList = siteRoleMgr.getAdmitDischargeRolesAssociatedWithSite(Integer.parseInt(currentSiteId));
	}
	
	SecRoleDao secroleDao = SpringUtils.getBean(SecRoleDao.class);
	List<SecRole> allRoleList = secroleDao.getRoles();
	
%>
<body vlink="#0000FF" class="BodyStyle" onload="$('colorField').style.backgroundColor=$('colorField').value;">
<nested:form action="/admin/ManageSites">
<input name="roleId" type="hidden" value=""></input>
<input name="roleType" type="hidden" value=""></input>
<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Add New Satellite Site</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<html:messages id="errors" header="errors.header" footer="errors.footer">
				<li><bean:write name="errors" /></li>
			</html:messages>

	<table>
	<tr><td>Site Name:<sup style="color:red">*</sup></td><td><nested:text property="site.name" maxlength="30"></nested:text></td></tr>
	<tr><td>Short Name:<sup style="color:red">*</sup></td><td><nested:text property="site.shortName" maxlength="10"></nested:text></td></tr>
	<tr><td>Theme Color:<sup style="color:red">*</sup></td><td><nested:text styleId="colorField" property="site.bgColor" onclick="popup(350,450,'../colorpicker/colorpicker.htm','colorpicker');return false;"></nested:text>
	</td></tr>
	<tr><td>Active:</td><td><nested:checkbox property="site.status" value="1"/></td></tr>
	<tr><td>Telephone:</td><td><nested:text property="site.phone"></nested:text></td></tr>
	<tr><td>FAX:</td><td><nested:text property="site.fax"></nested:text></td></tr>
	<tr><td>Address:</td><td><nested:text property="site.address"></nested:text></td></tr>
	<tr><td>City:</td><td><nested:text property="site.city"></nested:text></td></tr>
	<tr><td>Province:</td><td><nested:select property="site.province">
					<html:option value="AB" >AB-Alberta</html:option>
					<html:option value="BC" >BC-British Columbia</html:option>
					<html:option value="MB" >MB-Manitoba</html:option>
					<html:option value="NB" >NB-New Brunswick</html:option>
					<html:option value="NL" >NL-Newfoundland & Labrador</html:option>
					<html:option value="NT" >NT-Northwest Territory</html:option>
					<html:option value="NS" >NS-Nova Scotia</html:option>
					<html:option value="NU" >NU-Nunavut</html:option>
					<html:option value="ON" >ON-Ontario</html:option>
					<html:option value="PE" >PE-Prince Edward Island</html:option>
					<html:option value="QC" >QC-Quebec</html:option>
					<html:option value="SK" >SK-Saskatchewan</html:option>
					<html:option value="YT" >YT-Yukon</html:option>	
	</nested:select></td></tr>
	<tr><td>Postal Code:</td><td><nested:text property="site.postal"></nested:text></td></tr>
	<tr><td>Site URL:</td><td><nested:text property="site.siteUrl" maxlength="100"></nested:text></td></tr>
   <% if (org.oscarehr.common.IsPropertiesOn.isProviderFormalizeEnable()) { %>  	
		<tr><td>ProviderID From:</td><td><nested:text property="site.providerIdFrom"></nested:text></td></tr>
		<tr><td>ProviderID To:</td><td><nested:text property="site.providerIdTo"></nested:text></td></tr>
	<% } %>
	<tr>
		<td>Roles Only Access to This Site: </td>
		<td id="td_access_roles">
			<div id="div_access_roles" style="float: left;">
				
				<ul>
					<%if(siteRoleList!=null && siteRoleList.size()>0){ %>
					<%for(int i=0;i<siteRoleList.size();i++){ %>
						<li class="li_role" ><%=siteRoleList.get(i).getName() %>
						<a href="#" title="Delete Role" roleType="access" roleName="<%=siteRoleList.get(i).getName() %>" roleId="<%=siteRoleList.get(i).getId() %>" onclick="onClickDeleteRole(this);"></a>
						</li>
					<%} %>
				<%} %>
					<li class="li_add_role_btn">
						<input type="button" onclick="onClickAddAccessRole('access');" name="btn_change_access_roles" id="btn_change_access_roles" class="button" value="Add Roles">		
					</li>
				</ul>
			</div>
			<div style="float: left;">
			</div>
		</td>
	</tr>
	<tr>
		<td>Roles Can Admit and Discharge: </td>
		<td id="td_admit_desch_roles">
			<div id="div_access_roles" style="float: left;">
				
				<ul >
				<%if(siteAdmitDischargeRoleList!=null && siteAdmitDischargeRoleList.size()>0){ %>
					<%for(int i=0;i<siteAdmitDischargeRoleList.size();i++){ %>
						<li class="li_role"><%=siteAdmitDischargeRoleList.get(i).getName() %>
						<a href="#"  title="Delete Role" roleType="admit_discharge" roleName="<%=siteAdmitDischargeRoleList.get(i).getName() %>" roleId="<%=siteAdmitDischargeRoleList.get(i).getId() %>" onclick="onClickDeleteRole(this);"></a>
						</li>
					<%} %>
				<%} %>
					<li class="li_add_role_btn">
						<input type="button" onclick="onClickAddAccessRole('admit_discharge');" name="btn_change_access_roles" id="btn_change_access_roles" class="button" value="Add Roles">
					</li>
				</ul>
			</div>
			<div style="float: left;">
			</div>
		</td>
	</tr>
	
	<tr><td>&nbsp;</td>
	</tr>
	
	<tr><td>Site Logo:</td><td><a href="<%= request.getContextPath() %>/dms/ManageDocument.do?method=display&doc_no=<bean:write name="siteForm" property="site.siteLogoId"/>&providerNo=<%=provider_no %>" target="_blank"><bean:write name="siteForm" property="site.siteLogoDesc"/></a></td></tr>
	</table>
	<nested:hidden property="site.siteLogoId"/>
	<nested:hidden property="site.siteId"/>
	<input name="method" type="hidden" value="save"></input>
	<nested:submit styleClass="button" >Save</nested:submit> <nested:submit styleClass="button" onclick="this.form.method.value='view'">Cancel</nested:submit>

  		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</nested:form>
<script>
jQuery(document).ready(function(){
	jQuery( "#dialog_roles" ).dialog({
		 autoOpen: false,
		 modal: true
	});
});

var dlgRoleType = "";
function onClickAddAccessRole(roleType)
{
	jQuery("#select_roles").val("-1");
	dlgRoleType = roleType;
	
	if(roleType=="access")
		jQuery("#select_roles").html(getAccessRoleOptions());
	else if(roleType=="admit_discharge")
		jQuery("#select_roles").html(getAdmitDischargeRoleOptions());
	
	jQuery( "#dialog_roles" ).dialog( "open" );
}
function onClickRoleDlgCancel()
{
	jQuery( "#dialog_roles" ).dialog( "close" );
	dlgRoleType = "";
	jQuery("#select_roles").html("");
}

function onClickDeleteRole(liObj)
{
	var roleName = jQuery(liObj).attr("roleName");
	var roleId = jQuery(liObj).attr("roleId");
	var roleType = jQuery(liObj).attr("roleType");
	if(confirm("Are you sure want to delete the role - "+roleName+" ?"))
	{
		document.forms[0].method.value = "deleteRole";
		document.forms[0].roleId.value = roleId;
		document.forms[0].roleType.value = roleType;
		
		document.forms[0].submit();
	}
}

function onClickAddRole()
{
	var selectedRoleId = jQuery("#select_roles").val();
	if(selectedRoleId=="-1")
	{
		alert("Please select role");
		return;
	}
	
	document.forms[0].method.value = "addRole";
	document.forms[0].roleId.value = selectedRoleId;
	document.forms[0].roleType.value = dlgRoleType;
	
	document.forms[0].submit();
}

function getAccessRoleOptions()
{
	<%
	String str = "<option value='-1'>-- Select Role --</option>";
	if(allRoleList!=null && allRoleList.size()>0)
	{
		for(int i=0;i<allRoleList.size();i++)
		{
			SecRole secrole = allRoleList.get(i);
			if(siteRoleList==null || !siteRoleList.contains(secrole))
			{
				str = str+"<option value='"+secrole.getId()+"'>"+secrole.getName()+"</option>";
			}
		}
	}
	%>
	
	return "<%=str%>";
}
function getAdmitDischargeRoleOptions()
{
	<%
	String str_ = "<option value='-1'>-- Select Role --</option>";
	if(allRoleList!=null && allRoleList.size()>0)
	{
		for(int i=0;i<allRoleList.size();i++)
		{
			SecRole secrole = allRoleList.get(i);
			if(siteAdmitDischargeRoleList==null || !siteAdmitDischargeRoleList.contains(secrole))
			{
				str_ = str_+"<option value='"+secrole.getId()+"'>"+secrole.getName()+"</option>";
			}
		}
	}
	%>
	
	return "<%=str_%>";
}
</script>

<div id="dialog_roles" title="Add Role">
	<select id="select_roles" name="select_roles">		
	</select>
	<br><br>
	<input type="button" class="button" onclick="onClickAddRole();" value="Add Role"> &nbsp;
	<input type="button" class="button" onclick="onClickRoleDlgCancel();" value="Cancel">
</div>

</html:html>

<% if(siteId!=null && !"".equals(siteId) ) {%>
<p>Upload Site Logo:<br>
<font color='red'><%=logoError%></font>

	<jsp:include page="../dms/addDocument.jsp">
		<jsp:param name="appointmentNo" value="0"/>
		<jsp:param name="function" value="provider"/>
		<jsp:param name="functionid" value="<%=provider_no%>" />
		<jsp:param name="siteId" value="<%=siteId%>" />
	</jsp:include>
  
<%} %>
