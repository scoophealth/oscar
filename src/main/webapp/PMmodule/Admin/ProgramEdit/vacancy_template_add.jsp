
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
<%@page import="org.oscarehr.PMmodule.model.Criteria"%>
<%@page import="org.oscarehr.PMmodule.model.CriteriaType"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
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
// is only populated if it's an existing form, i.e. new one off existing form
	

	// must be populated some how
	int currentDemographicId=0;
	
	// must be populated some how
	VacancyTemplate template = null;
	String templateId = (String) request.getAttribute("vacancyOrTemplateId");
	boolean dontSave = false;
	
	if (!StringUtils.isBlank(templateId) && !templateId.equalsIgnoreCase("null"))	{				
		template=VacancyTemplateManager.getVacancyTemplateByTemplateId(Integer.parseInt(templateId));	
		dontSave = true;
	}	else	{	
		template= new VacancyTemplate();		
	}
%>
<script type="text/javascript">

	function save() {
		document.programManagerForm.elements['view.tab'].value='General';
		document.programManagerForm.elements['view.subtab'].value='vacancy templates';			
		document.programManagerForm.method.value='save_vacancy_template';
		document.programManagerForm.submit();
	}  
	function cancel2() {
		document.programManagerForm.elements['view.tab'].value='General';
		document.programManagerForm.elements['view.subtab'].value='vacancy templates';			
		document.programManagerForm.method.value='edit';
		document.programManagerForm.submit();
	}

	function changeVacancyTemplateType(selectBox, type) {
		var selectBoxId = selectBox.id;
		var selectBoxValue = selectBox.options[selectBox.selectedIndex].value;
		var typeName = type.toLowerCase().replace(/ /g,"_");
		
		if(document.getElementById("sourceOf" + typeName) == null) {
				$.get('Admin/ProgramEdit/vacancy_template_range.jsp?typeSelected='+type+'&optionValueSelected='+selectBoxValue, function(data) {
					  $("#block_"+typeName).append(data);					 
					});														
			}
		
			if(document.getElementById("sourceOf" + typeName) != null) {
				
				$("#block_vacancyType_"+typeName).remove();
								
				$.get('Admin/ProgramEdit/vacancy_template_range.jsp?typeSelected='+type+'&optionValueSelected='+selectBoxValue, function(data) {
					  $("#block_"+typeName).append(data);					 
					});	
			}
	}
	
	function template_status_change(template_id) {
		if (template_id==null){
			return;
		}
		document.programManagerForm.elements['view.tab'].value='General';
		document.programManagerForm.elements['view.subtab'].value='Vacancy Template Add';
		document.programManagerForm.elements['vacancyOrTemplateId'].value=template_id;
		if (document.getElementById("vacancy_tpl_status_ckbox").checked){
			document.programManagerForm.method.value='activeTmplStatus';
		} else {
			document.programManagerForm.method.value='inactiveTmplStatus';
		}
		document.programManagerForm.submit();
	}

</script>
<div class="tabs" id="tabs">

<input type="hidden" name="vacancyOrTemplateId" id="vacancyOrTemplateId" value="<%=template.getId()%>" />
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
<input type="hidden" name="id" id="id" value="<%=request.getAttribute("id")%>" />
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
		<td width="30%" class="beright">Template is <%=(template.getId()==null || template.getActive()==true)?"active":"inactive" %>:</td>
		<td><input id="vacancy_tpl_status_ckbox" type="checkbox" onchange="template_status_change(<%=template.getId()%>)" 
		<%if(template.getId()==null || template.getActive()==true){%>
		<%="checked"%>
		<%}%>
		name="templateActive"></td>
	</tr>
	<tr class="b">
		<td class="beright">Template Name:</td>
		<td><input type="text" size="50" maxlength="50" value="<%=template.getName()==null?"":template.getName() %>"
			name="templateName"></td>
	</tr>
	
</table>

<fieldset>
	<legend>Criteria Required For this Template</legend>	
	
	<% 
		
		//List<CriteriaType> typeList = VacancyTemplateManager.getAllCriteriaTypes();
		List<CriteriaType> typeList = VacancyTemplateManager.getAllCriteriaTypesByWlProgramId(Integer.parseInt((String)request.getAttribute("id")));
		for(CriteriaType criteriaType : typeList) {
	%>
			<%=VacancyTemplateManager.renderAllSelectOptions(template.getId(), null, criteriaType.getId())%>
	<%	}			
	%>
	
	
</fieldset>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr>
	<% if(!dontSave) {%>
		 <td colspan="2"><input type="button" value="Save" onclick="return save()" /> 
	<% } %>
		 <input type="button" value="Cancel" onclick="return cancel2()" />
		 </td>
	</tr>
</table>

