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
	String roleName$ = (String) session.getAttribute("userrole") + ","
			+ (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@page import="org.oscarehr.web.admin.KeyManagerUIBean"%>
<%@page import="org.oscarehr.common.model.PublicKey"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>

<%@include file="/layouts/html_top.jspf"%>

<h2 class="oscarBlueHeader">
	Key Manager
</h2>

<br />

<input type="button" value="Create New Key" onclick="document.location='createKey.jsp'" />

<br />
<hr />
<br />
<div class="oscarBlueForeground">Example eData / Lab Upload url</div>
<%
	String requestUrl=request.getRequestURL().toString();
	String servletPath=request.getServletPath();
	String uploadUrl=requestUrl.substring(0, requestUrl.length()-servletPath.length());
	uploadUrl=uploadUrl+"/lab/newLabUpload.do";
%>
<div style="border:solid grey 1px;word-wrap:break-word;font-size:12px; width:95%"><%=uploadUrl%></div>
<div style="font-size:12px">
(You may need to change the server name / port to the externally accessible name / port of your server.)
</div>
<br />
<hr />
<br />
<div class="oscarBlueForeground">Oscar Public Key (Base64 encoded)</div>
<div style="border:solid grey 1px;word-wrap:break-word;font-size:12px; width:95%"><%=KeyManagerUIBean.getPublicOscarKeyEscaped()%></div>
<br />
<hr />
<br />

<script language="javascript">
	function onSelectService()
	{
		var selectKeyList=document.getElementById("selectKeyList");

		if (selectKeyList.options.length<=0) return;
		
		jQuery.getJSON("getPublicKey.json", {id: getSelectListValue(selectKeyList)}, 
			function(xml)
			{
				var privateKeyField=document.getElementById("privateKey");
				privateKeyField.innerHTML=xml.base64EncodedPrivateKey;

				var selectProfessionalSpecialistList=document.getElementById("selectProfessionalSpecialistList");
				selectProfessionalSpecialistList.selectedIndex=0;

				<%-- json anomalie where null is returned as 0 --%> 
				if (xml.matchingProfessionalSpecialistId !=null && xml.matchingProfessionalSpecialistId!=0)
				{
					selectSelectListOption(selectProfessionalSpecialistList, xml.matchingProfessionalSpecialistId);
				}
			}
		);
	}

	function updateMatchingProcessionalSpecialist()
	{
		var selectKeyList=document.getElementById("selectKeyList");
		var selectProfessionalSpecialistList=document.getElementById("selectProfessionalSpecialistList");
		jQuery.post("updateMatchingProfessionalSpecialist.jsp", {serviceName: getSelectListValue(selectKeyList), professionalSpecialistId: getSelectListValue(selectProfessionalSpecialistList)},
			function(xml)
			{
				alert('Changes saved.');
			}
		);
	}
</script>
<div class="oscarBlueForeground">Current Public Keys</div>
<table style="border-collapse:collapse; width:95%; table-layout:fixed;word-wrap:break-word;font-size:12px">
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="width:13em">Service Name</td>
		<td>
			<select id="selectKeyList" onchange="onSelectService()">
				<%
					for (PublicKey publicKey : KeyManagerUIBean.getPublicKeys())
					{
						%>
							<option value="<%=KeyManagerUIBean.getSericeNameEscaped(publicKey)%>"><%=KeyManagerUIBean.getSericeDisplayString(publicKey)%></option>
						<%
					}
				%>
			</select>
			<script language="javascript">
				onSelectService();
			</script>
		</td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Private Service Key (Base64 encoded)</td>
		<td id="privateKey"></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Matching Professional Specialist (optional)</td>
		<td>
			<select id="selectProfessionalSpecialistList">
				<option value="">-- none --</option>
				<%
					for (ProfessionalSpecialist professionalSpecialist : KeyManagerUIBean.getProfessionalSpecialists())
					{
						%>
							<option value="<%=professionalSpecialist.getId()%>"><%=KeyManagerUIBean.getProfessionalSpecialistDisplayString(professionalSpecialist)%></option>
						<%
					}
				%>
			</select>
			<input type="button" value="Update Matching Professional Specialist" onclick="updateMatchingProcessionalSpecialist()" />
		</td>
	</tr>
</table>


<%@include file="/layouts/html_bottom.jspf"%>
