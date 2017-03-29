
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

<%@page import="org.oscarehr.PMmodule.model.Vacancy"%>
<%@page import="org.oscarehr.PMmodule.model.VacancyTemplate"%>
<%@page import="org.oscarehr.PMmodule.model.Criteria"%>
<%@page import="org.oscarehr.PMmodule.dao.CriteriaDao"%>
<%@page import="org.oscarehr.PMmodule.model.CriteriaType"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.text.SimpleDateFormat"%>



<%
	CriteriaDao criteriaDAO = SpringUtils.getBean(CriteriaDao.class);
	List<Criteria> criterias = new ArrayList<Criteria>();
	Vacancy vacancy = null;
	Integer templateId = null, vacancyId_int = null;
	String reasonClosed = "", dateClosed = "";
	String vacancyId = (String) request.getAttribute("vacancyOrTemplateId");
	String vacancyName = "";
	boolean dontSave = false;
	String vacancyStatus = "";
	if (!StringUtils.isBlank(vacancyId) && !vacancyId.equalsIgnoreCase("null"))	{	
		dontSave = true;
		vacancyId_int = Integer.valueOf(vacancyId);	
		vacancy = VacancyTemplateManager.getVacancyById(vacancyId_int);
		if(vacancy!=null) {
			templateId = vacancy.getTemplateId();
			
			criterias = VacancyTemplateManager.getCriteriasByVacancyId(vacancyId_int);
			
			reasonClosed = vacancy.getReasonClosed();
			if(reasonClosed == null) 
				reasonClosed = "";
			Date dc = vacancy.getDateClosed();
			vacancyName = vacancy.getName();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy H:mm", request.getLocale());
			ResourceBundle props = ResourceBundle.getBundle("oscarResources", request.getLocale());
			if (dc !=null ) {			
				try {
					//Date dateClosedFormatted = formatter.parse(dc);			
					dateClosed = DateFormatUtils.ISO_DATE_FORMAT.format(dc);
				} catch (Exception e){
						//do nothing for now
				}
			}
						
		}
		criterias = VacancyTemplateManager.getCriteriasByVacancyId(Integer.valueOf(vacancyId));
		vacancyStatus = vacancy.getStatus();
	}	else	{	
		vacancyId = "";		
		vacancy= new Vacancy();	
		VacancyTemplate selectedTemplate = (VacancyTemplate) request.getAttribute("selectedTemplate");		
		if(selectedTemplate != null) {
			templateId = selectedTemplate.getId();
			criterias = VacancyTemplateManager.getRefinedCriteriasByTemplateId(templateId);				
		}
		vacancyStatus = "Active";
	}
	
	
%>
<script type="text/javascript">

	function save() {
		document.programManagerForm.elements['view.tab'].value="vacancies";
		document.programManagerForm.elements['view.subtab'].value="vacancies";
		document.programManagerForm.method.value='save_vacancy';
		document.programManagerForm.submit();
	}  
	
	function chooseTemplate(selectBox) {
		var template_id = selectBox.options[selectBox.selectedIndex].value;		
		document.programManagerForm.elements['requiredVacancyTemplateId'].value=template_id;	
		document.programManagerForm.elements['vacancyOrTemplateId'].value="";
		document.programManagerForm.method.value='chooseTemplate';				
		document.programManagerForm.submit();
	}
	
	function cancel2() {
		document.programManagerForm.elements['view.tab'].value="vacancies";
		document.programManagerForm.elements['view.subtab'].value="vacancies";
		document.programManagerForm.method.value='edit';
		document.programManagerForm.submit();
	}
	    
	
</script>
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Templates">Vacancies</th>
		</tr>
	</table>
</div>
<input type="hidden" name="id" id="id" value="<%=request.getAttribute("id")%>" />
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
<input type="hidden" name="vacancyId" id="vacancyId" value="<%=vacancyId%>" />
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Requirement Template:</td>
		<td><select name="requiredVacancyTemplateId" onchange="chooseTemplate(this);" <%= (dontSave==true)?"disabled":"" %>>
			<option value="0">&nbsp;</option>
		<% 				
			Integer programId_int = null;
			String programId = (String) request.getAttribute("id");
			if(programId !=null) 
				programId_int = Integer.valueOf(programId);
			List<VacancyTemplate> templates = VacancyTemplateManager.getActiveVacancyTemplatesByWlProgramId(programId_int);
			for(VacancyTemplate vt : templates) {
				String selectedOrNot = "";							
				if(templateId != null && templateId.intValue() == vt.getId().intValue())
					selectedOrNot = "selected";
		%>	
				<option value="<%=vt.getId()%>" <%=selectedOrNot%> ><%=vt.getName() %></option>
		<%} %>
		</select></td>
	</tr>
	<tr class="b">
		<td class="beright">Vacancy Name:</td>
		<td><input type="text" name="vacancyName" value="<%= vacancyName %>" size="40" <%=(dontSave==true)?"disabled":"" %>/></td>
	</tr>
	<legend>Additional Criteria For this Vacancy/Service Opening</legend>
	
	<% 
		
		for(Criteria criteria : criterias) {
			CriteriaType type = VacancyTemplateManager.getCriteriaTypeById(criteria.getCriteriaTypeId());
			//Integer vacancyId_int = null;
			//if(!StringUtils.isBlank(vacancyId))
			//	vacancyId_int = Integer.valueOf(vacancyId);
			
			
			/*
			List<CriteriaType> typeList = VacancyTemplateManager.getAllCriteriaTypes();
			for(CriteriaType type : typeList) {
			*/	
		%>
				<%=VacancyTemplateManager.renderAllSelectOptions(templateId, vacancyId_int, type.getId())%>
		<%	}			
		%>
	
	
</fieldset>	
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Status:</td>
		<td><select name="vacancyStatus">		
				<option value="Active" <%=(vacancyStatus.equalsIgnoreCase("Active"))?"selected":"" %> >Active</option>
				<option value="Withdrawn" <%=(vacancyStatus.equalsIgnoreCase("Withdrawn"))?"selected":"" %> >Withdrawn</option>
				<option value="Filled" <%=(vacancyStatus.equalsIgnoreCase("Filled"))?"selected":"" %>>Filled</option>
		</td>
	</tr>
	<tr class="b">
		<td class="beright">Date Closed:</td>
		<td><input type="text" name="dateClosed" id="dateClosed" value="<%= dateClosed %>" size="10"><img src="<%=request.getContextPath()%>/images/cal.gif" id="dateClosed_cal"></td>
		<script type="text/javascript">
					Calendar.setup({ inputField : "dateClosed", ifFormat : "%Y-%m-%d", showsTime :false, button : "dateClosed_cal", singleClick : true, step : 1 });
					
		</script>	
	</tr>
	<tr class="b">
		<td class="beright">Reason Closed:</td>
		<td><input type="text" name="reasonClosed" size="100" value="<%= reasonClosed %>"/></td>
	</tr>
</table>

<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr>
	<td colspan="2">
	
		 <input type="button" value="Save" onclick="return save()" /> 
	
		 <input type="button" value="Cancel" onclick="return cancel2()" /></td>
	</tr>
</table>
