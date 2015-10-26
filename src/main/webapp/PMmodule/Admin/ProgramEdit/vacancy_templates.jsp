
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>


<%@page import="org.oscarehr.PMmodule.model.VacancyTemplate"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="java.util.List"%>

<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>



<%	
	String currentProgramId = (String) request.getAttribute("id");
	List<VacancyTemplate> templates = VacancyTemplateManager.getVacancyTemplateByWlProgramId(Integer.valueOf(currentProgramId));
		
%>

				
<div class="tabs" id="tabs">
<input type="hidden" name="id" id="id" value="<%= currentProgramId%>" />	
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs" class="nofocus"><a
				onclick="javascript:clickTab2('General', 'General');return false;"
				href="javascript:void(0)">General Information</a></th>
			<th title="Templates">Vacancy Templates</th>
		</tr>
	</table>
</div>

<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="50%" class="beright">Template Name</td>
		<td width="50%">Active</td>
	</tr>
<%	for(VacancyTemplate vt : templates) { %>
	<tr class="b">
		<td class="beright">
		<a onclick="javascript:clickLink('General','Vacancy Template Add', '<%=vt.getId() %>');return false;" href="javascript:void(0)"><%=vt.getName() %></a>
		<td><%=vt.getActive()==true?"Yes":"No" %></td>
	</tr>
<% 	} %>
	<tr class="b">
		<td class="beright">		
			<a onclick="javascript:clickLink('General','Vacancy Template Add', '');return false;" href="javascript:void(0)">Create New Vacancy Template</a>
		</td>		
		<td></td>
	</tr>
</table>


</form>
