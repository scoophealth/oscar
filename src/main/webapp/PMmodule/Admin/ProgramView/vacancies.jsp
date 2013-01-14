
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
<%@page import="org.oscarehr.PMmodule.model.Vacancy"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat" %>

<%@ include file="/taglibs.jsp"%>

<%	
	String currentProgramId = (String) request.getAttribute("id");
	List<Vacancy> vacancies = VacancyTemplateManager.getVacanciesByWlProgramId(Integer.valueOf(currentProgramId));
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
%>

<div class="tabs" id="tabs">
<input type="hidden" name="id" id="id" value="<%= currentProgramId%>" />
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>			
			<th title="Templates">Vacancies</th>
		</tr>
	</table>
</div>

<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="50%" class="beright">Vacancy's Template Name</td>
		<td width="25%">Vacancy Status</td>
		<td width="25%">Vacancy Create Date</td>
	</tr>
<%	for(Vacancy v : vacancies) { 
		VacancyTemplate vt = VacancyTemplateManager.getVacancyTemplateByTemplateId(v.getTemplateId());
%>
	<tr class="b">
		<td class="beright">
		<a onclick="javascript:clickLink('Vacancy Add','Vacancy Add', '<%=v.getId() %>');return false;" href="javascript:void(0)"><%=(vt==null?"No Template for This Vacancy":vt.getName()) %></a>
		<td><%= v.getStatus() %></td>
		<td><%=dateFormatter.format(v.getDateCreated()) %> </td>
	</tr>
<% 	} %>
	
</table>


</form>
