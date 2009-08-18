<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients.LinkedDemographicHolder"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");

	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	
	Demographic demographic=demographicDao.getDemographicById(currentDemographicId);
	ArrayList<ManageLinkedClients.LinkedDemographicHolder> demographicsToDisplay=ManageLinkedClients.getDemographicsToDisplay(currentDemographicId);
%>

<h3>Manage Linked Clients</h3>

<h4>Demographic</h4>
<form action="manage_linked_clients_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<table style="padding-left:20px" class="genericTable">
		<tr class="genericTableRow">
			<td class="genericTableHeader">Linked</td>
			<td class="genericTableHeader">Photo</td>
			<td class="genericTableHeader">Score</td>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableHeader">Birth Date</td>
			<td class="genericTableHeader">Gender</td>
		</tr>
		<tr class="genericTableRow" style="background-color:#ccffcc">
			<td class="genericTableData">Local</td>
			<td class="genericTableData"><img style="height:96px; width:96px" src="<%=request.getContextPath()+ManageLinkedClients.getLocalImageUrl(currentDemographicId)%>" alt="client_image_<%=currentDemographicId%>" /></td>
			<td class="genericTableData">100</td>
			<td class="genericTableData"><%=demographic.getLastName()%></td>
			<td class="genericTableData"><%=demographic.getFirstName()%></td>
			<td class="genericTableData"><%=demographic.getFormattedDob()%></td>
			<td class="genericTableData"><%=demographic.getSex()%></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
	
		<%
			if (demographicsToDisplay!=null)
			{
				for (LinkedDemographicHolder temp : demographicsToDisplay)
				{
					%>
						<tr class="genericTableRow" style="background-color:#f3f3f3">
							<td class="genericTableData">
								<%
									if (temp.nonChangeableLinkStatus==null)
									{
										%>
											<input type="checkbox" name="linked.<%=temp.linkDestination+'.'+temp.remoteLinkId%>" <%=temp.linked?"checked=\"on\"":""%> />
										<%
									}
									else
									{
										%>
											<%=temp.nonChangeableLinkStatus%>
										<%
									}
								%>
							</td>
							<td class="genericTableData"><img style="height:96px; width:96px" src="<%=request.getContextPath()+temp.imageUrl%>" alt="client_image_<%=temp.linkDestination+'_'+temp.remoteLinkId%>" /></td>
							<td class="genericTableData"><%=temp.matchingScore%></td>
							<td class="genericTableData"><%=temp.lastName%></td>
							<td class="genericTableData"><%=temp.firstName%></td>
							<td class="genericTableData"><%=temp.birthDate%></td>
							<td class="genericTableData"><%=temp.gender%></td>
						</tr>
					<%
				}
			}
		%>
	</table>
	<br />
	<input type="submit" value="save" /> &nbsp;&nbsp; <input type="button" value="cancel" onclick="history.go(-1)" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
