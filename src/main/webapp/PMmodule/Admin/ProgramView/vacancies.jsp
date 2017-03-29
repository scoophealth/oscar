
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
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%	
	String statusFilter = "Active";
    List<Vacancy> vacancies = null;
    String currentProgramId = (String) request.getAttribute("id");
    		
   
    if(request.getParameter("statusFilter") != null && request.getParameter("statusFilter").length() == 0) {
    	statusFilter = ""; 
    	vacancies = VacancyTemplateManager.getVacanciesByWlProgramId(Integer.valueOf(currentProgramId));
    } 
    if(request.getParameter("statusFilter") != null && request.getParameter("statusFilter").length() > 0){
    	statusFilter = request.getParameter("statusFilter");
    	vacancies = VacancyTemplateManager.getVacanciesByWlProgramIdAndStatus(Integer.valueOf(currentProgramId),statusFilter);
    }
    if(request.getParameter("statusFilter") == null) {
    	vacancies = VacancyTemplateManager.getVacanciesByWlProgramIdAndStatus(Integer.valueOf(currentProgramId),statusFilter);
    }
    
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
%>

<script>

var sortOrder = 'desc';

$(document).ready(function(){
$('#sortVacancies').click(function(e) {
	  

    var rows = $('#vacancyTable tbody  tr').get();
    
    
    rows.sort(function(a, b) {
    
      var A = $(a).children('td').eq(0).text().toUpperCase();
      var B = $(b).children('td').eq(0).text().toUpperCase();
      
      if(sortOrder == 'asc') {
	      if(A < B) {
	        return -1;
	      }
	      
	      if(A > B) {
	        return 1;
	      }
	      
	      return 0;     
      } else {
    	  if(A < B) {
  	        return 1;
  	      }
  	      
  	      if(A > B) {
  	        return -1;
  	      }
  	      
  	      return 0; 
      }
    
    
    });
    
    $.each(rows, function(index, row) {
 		$('#vacancyTable').children('tbody').append(row);
    });
    
    if(sortOrder == 'asc')
    	sortOrder = 'desc';
    else
    	sortOrder = 'asc';
    
    e.preventDefault();
    
});

    
});

function saveStatus(vacancyId) {
	var newStatus = $('#status'+vacancyId).val();
	
    $.getJSON("<%= request.getContextPath() %>/PMmodule/ProgramManager.do?method=saveVacancyStatus&status="+newStatus+"&vacancyId="+vacancyId,
            function(data,textStatus){
              if(data.success) {
            	  alert('Status has been updated');
              } else {
            	  alert("Error saving status - " + data.error);
              }
            });

}

function filterByStatus() {
	var status = $("#statusFilter").val();
	var id = $("#id").val();
	var programId = $("#programId").val();
	location.href='<%=request.getContextPath()%>/PMmodule/ProgramManagerView.do?method=view&tab=Vacancies&subtab=Vacancies&status=Active&vacancyOrTemplateId=&id='+id+"&programId="+programId+"&statusFilter="+status;
}
</script>

<table id="vt" width="100%"  border="1" cellspacing="2" cellpadding="3">
	<thead>
		<tr class="b" >
			<td width="10%"><b>Filter:</b></td>
			<td>
				<select onchange="filterByStatus()" name="statusFilter" id="statusFilter">
					<option value="Active" <%=(statusFilter != null && statusFilter.equals("Active"))?"selected=\"selected\"":"" %>>Active</option>
					<option value="Filled" <%=(statusFilter != null && statusFilter.equals("Filled"))?"selected=\"selected\"":"" %>>Filled</option>
					<option value="Withdrawn" <%=(statusFilter != null && statusFilter.equals("Withdrawn"))?"selected=\"selected\"":"" %>>Withdrawn</option>
					<option value=""  <%=(statusFilter != null && statusFilter.equals(""))?"selected=\"selected\"":"" %>>Any</option>
				</select>
			</td>
		</tr>
	</thead>
</table>

<br/>
<div class="tabs" id="tabs">
<input type="hidden" name="id" id="id" value="<%= currentProgramId%>" />
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>			
			<th title="Templates">Vacancies</th>
		</tr>
	</table>
</div>



<table id="vacancyTable" width="100%" border="1" cellspacing="2" cellpadding="3">
	<thead>
		<tr class="b">
			<td width="25%" style="text-align:center;font-weight:bold"><a href="javascript:void(0)" id="sortVacancies">Vacancy Name</a></td>
			<td width="25%" class="beright" style="text-align:center;font-weight:bold">Vacancy's Template Name</td>
			<td width="25%" style="text-align:center;font-weight:bold">Vacancy Status</td>
			<td width="25%" style="text-align:center;font-weight:bold">Vacancy Create Date</td>
		</tr>
	</thead>
	<tbody>
<%	for(Vacancy v : vacancies) { 
		VacancyTemplate vt = VacancyTemplateManager.getVacancyTemplateByTemplateId(v.getTemplateId());
%>
		<tr class="b">
			<td style="text-align:left;"><%=v.getName() %></td>
			<td style="text-align:left;" class="beright">
				<a onclick="javascript:clickLink('Vacancy Add','Vacancy Add', '<%=v.getId() %>');return false;" href="javascript:void(0)"><%=(vt==null?"No Template for This Vacancy":vt.getName()) %></a>
			</td>
			<td style="text-align:center;">
				<select name="status" id="status<%=v.getId()%>">
					<option value="Active" <%=(v.getStatus().equals("Active"))?"selected=\"selected\"":"" %>>Active</option>
					<option value="Filled" <%=(v.getStatus().equals("Filled"))?"selected=\"selected\"":"" %>>Filled</option>
					<option value="Withdrawn" <%=(v.getStatus().equals("Withdrawn"))?"selected=\"selected\"":"" %>>Withdrawn</option>
				</select>
				&nbsp;
				<input type="button" value="save" onClick="saveStatus('<%=v.getId()%>')"/>
			</td>
			<td style="text-align:center;"><%=dateFormatter.format(v.getDateCreated()) %> </td>
		</tr>
	
<% 	} %>
	</tbody>
</table>


</form>
