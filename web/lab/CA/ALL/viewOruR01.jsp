<%@page import="oscar.oscarLab.ca.all.pageUtil.ViewOruR01UIBean"%>

<%@include file="/layouts/html_top.jspf"%>

<%
	String segmentId=request.getParameter("segmentId");
	ViewOruR01UIBean viewOruR01UIBean=new ViewOruR01UIBean(segmentId);
%>


<h2 class="oscarBlueHeader">
	View Electronic Data 
	<span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message : segmentId <%=segmentId%>)</span>
</h2>

<table style="border-collapse:collapse;font-size:12px">
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="width:10em">From Provider</td>
		<td><%=viewOruR01UIBean.getFromProviderDisplayString()%></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">To Provider</td>
		<td><%=viewOruR01UIBean.getToProviderDisplayString()%></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">For Client</td>
		<td>
			<table style="border-collapse:collapse; width 100%">
				<tr style="border-bottom:solid grey 1px">
					<td style="font-weight:bold;text-align:right;border-right:solid grey 1px">Name</td>
					<td><%=viewOruR01UIBean.getClientDisplayName()%></td>
				</tr>
				<tr style="border-bottom:solid grey 1px">
					<td style="font-weight:bold;text-align:right;border-right:solid grey 1px">Health Number</td>
					<td><%=viewOruR01UIBean.getHinForDisplay()%></td>
				</tr>
				<tr>
					<td style="font-weight:bold;text-align:right;border-right:solid grey 1px">BirthDay</td>
					<td><%=viewOruR01UIBean.getBirthDayForDisplay()%></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Data Name</td>
		<td><%=viewOruR01UIBean.getDataNameForDisplay()%></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">Text Data</td>
		<td><textarea id="textData" readonly="readonly" style="width:40em;height:8em" ><%=viewOruR01UIBean.getTextDataForDisplay()%></textarea></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="vertical-align:top">Uploaded File</td>
		<td>
			<%
				if (viewOruR01UIBean.hasBinaryFile())
				{
					%>
						<span style="font-weight:bold">File name :</span> <%=viewOruR01UIBean.getBinaryFilenameForDisplay()%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="download file" onclick="document.location='<%=viewOruR01UIBean.getContentRenderingUrl(request, true)%>'" />
						<hr />
						<%=viewOruR01UIBean.getPreviewFileHtml(request)%>
					<%
				}
				else
				{
					 %>
					 	-- No File Uploaded --
					 <%
				}
			%>
		</td>
	</tr>
</table>

<%@include file="/layouts/html_bottom.jspf"%>