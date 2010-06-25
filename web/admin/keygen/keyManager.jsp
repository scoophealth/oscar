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
<div class="oscarBlueForeground">Oscar Public Key</div>
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
		<td class="oscarBlueHeader">Private Key</td>
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