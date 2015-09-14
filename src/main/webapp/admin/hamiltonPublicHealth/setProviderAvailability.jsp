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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.managers.ProviderManager2" %>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>		 

<%
	ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");

    if("save".equals(request.getParameter("method"))) {
    	for(Object keyO:request.getParameterMap().keySet()) {
    		String key = (String)keyO;
    		if(key.startsWith("availabilityCode_")) {
    			String provNo = key.split("_")[1];
    			String val = request.getParameter(key);
    			userPropertyDAO.saveProp(provNo, "availabilityCode", val);
    		}
    	}
    }
    
    
    List<Provider> providers = new ArrayList<Provider>();
    
    String programId = request.getParameter("programId");
    if(programId != null && programId.length()>0) {
    	//set the providers 
    	
    	for(ProgramProvider pp: programManager.getProgramProviders(programId)) {
    		providers.add(pp.getProvider());
    	}
    }
    
    if(programId == null) {
    	programId = "";
    }
    
    
%>


<div class="page-header">
        <h4>Set all Availabilities</h4>
</div>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">

<script>
	var editing = false;
	
	function getProviders() {
		var programId = $("#programId").val();
		//alert(programId);
		$("#theForm").submit();
	}
	
	function edit() {
		$("#viewBar").hide();
		$("#editBar").show();
		return false;
	}
	
	function save() {
		$("#method").val('save');
		$("#theForm").submit();
	}
	
	function cancel() {
		$("#viewBar").show();
		$("#editBar").hide();
		return false;
	}
	
</script>


<form class="well " action="setProviderAvailability.jsp" id="theForm">	
		<input type="hidden" name="method" id="method" value=""/>
		
		<div id="programOptions" class="control-group">
			<label class="control-label">Program:</label>
			<div class="controls">
				<select name="programId" id="programId" class="input-medium" onChange="getProviders()">
					<option value=""></option>
					<%
						List<Program> programs=programManager.getPrograms(loggedInInfo.getCurrentFacility().getId());
					
						for (Program program : programs)
						{
							String selected="";
							if(program.getId().toString().equals(programId)) {
								selected=" selected=\"selected\" ";
							}
							%>
								<option <%=selected%> value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName()+" ("+program.getType()+")")%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>
		
	<div class="control-group">
<%
	if(providers.size()>0) {
%>
<div id="viewBar">
	<button id="editBtn" class="btn" onClick="return edit();">Edit Mode</button>
	<br/>

	<table class="table table-striped  table-condensed">
		<tr>
			<th>Name</th>
			<th>Status</th>
		</tr>
		<%
		for(Provider p:providers) {
		%>
			<tr>
				<td><%=p.getLastName()%>,<%=p.getFirstName() %></td>
				<td>
				<%
					String tmpUp = userPropertyDAO.getStringValue(p.getProviderNo(), "availabilityCode");
				%>
				<%=(tmpUp != null && tmpUp.length()>0)?tmpUp:"<i>Not Set</i>" %>
				
				</td>
			</tr>
		<%} %>
	</table>

</div>


<div id="editBar" style="display:none">
	<button id="cancelBtn" class="btn" onClick="return cancel()">Cancel</button>
	<button id="saveBtn" class="btn" onClick="save()">Save</button><br/>
	
	<br/>
	
	<table class="table table-striped  table-condensed">
		<tr>
			<th>Name</th>
			<th>Status</th>
		</tr>
		<%
		for(Provider p:providers) {
			String tmpUp = userPropertyDAO.getStringValue(p.getProviderNo(), "availabilityCode");
		%>
			<tr>
				<td><%=p.getLastName()%>,<%=p.getFirstName() %></td>
				<td>
					<select name="availabilityCode_<%=p.getProviderNo()%>">
						<option value=""></option>
						<option <%=("GREEN".equals(tmpUp))?" selected=\"selected\" ":"" %> value="GREEN">GREEN</option>
						<option <%=("YELLOW".equals(tmpUp))?" selected=\"selected\" ":"" %> value="YELLOW">YELLOW</option>
						<option <%=("RED".equals(tmpUp))?" selected=\"selected\" ":"" %> value="RED">RED</option>
					</select>
				</td>
				
			</tr>
		<%} %>
	</table>
</div>




<%
}
%>
</div>
		
</form>
