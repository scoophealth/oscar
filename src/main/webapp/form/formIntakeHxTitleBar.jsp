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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@ page import="oscar.form.FrmRecord"%>
<%@ page import="oscar.form.FrmRecordFactory"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>

<%
    String formClass = "IntakeHx";
    int idx = request.getRequestURI().lastIndexOf("/");
    int idx2 =request.getRequestURI().lastIndexOf(".jsp"); 
    String formLink = request.getRequestURI().substring(idx+1,idx2); 
    formLink += ".jsp";
    int formId = Integer.parseInt(request.getParameter("formId"));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));

    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
    
    String projectHome = request.getContextPath().substring(1);
    ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
    String providerNo = props.getProperty("provider_no","");
    String providerName = "";
    if (providerNo != null && !providerNo.isEmpty() && !providerNo.equals("999998")){
        providerName = providerDao.getProviderName(providerNo);
    }
    else {
        providerName = LocaleUtils.getMessage(request.getLocale(),"oscarEncounter.formIntakeHx.notValidated");    
    }
    
%>
<script type="text/javascript">
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=projectHome%>/form/formname.do" ;
    }

    function onSave() {
        document.forms[0].submit.value="save";        
        reset();
        return confirm("Are you sure you want to save this form?");
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";       
        reset();
        return confirm("Are you sure you wish to save and close this window?");
    }
    function onPrint() {
        document.forms[0].submit.value="printall"; 
        
        document.forms[0].action = "../form/formname.do?__title=Intake+History&__cfgfile=intakeHxPrintCfgPg1&__cfgfile=intakeHxPrintCfgPg2&__cfgfile=intakeHxPrintCfgPg3&__cfgfile=intakeHxPrintCfgPg4&__cfgfile=intakeHxPrintCfgPg5&__cfgfile=intakeHxPrintCfgPg6&__cfgfile=intakeHxPrintCfgPg7&__cfgfile=intakeHxPrintCfgPg8&__cfgfile=intakeHxPrintCfgPg9&__cfgfile=intakeHxPrintCfgPg10&__cfgfile=intakeHxPrintCfgPg11&__template=intakeHx";
        document.forms[0].target="_blank";       
        return true;
    }
</script>
<!--Sections Title Bar-->
<div class="sectionTitleBar">
    <% if ("formIntakeHx.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHx.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.sectionDemographicTitle"/></a><% if ("formIntakeHx.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg2.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.sectionAllergiesTitle"/></a><% if ("formIntakeHxPg2.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg3.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.sectionRxTitle"/></a><% if ("formIntakeHxPg3.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg4.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.sectionMedHistoryTitle"/></a><% if ("formIntakeHxPg4.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg5.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg5.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.medicalConditionsTitle"/></a><% if ("formIntakeHxPg5.jsp".equals(formLink)) { %></div><%}%> |
    <br/>
    <% if ("formIntakeHxPg6.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg6.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.immunizationsTitle"/></a><% if ("formIntakeHxPg6.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg7.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg7.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.socialHistoryTitle"/></a><% if ("formIntakeHxPg7.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg8.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg8.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.familyHistoryTitle"/></a><% if ("formIntakeHxPg8.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg9.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg9.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.currentIssuesTitle"/></a><% if ("formIntakeHxPg9.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg10.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg10.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.womensHealthTitle"/></a><% if ("formIntakeHxPg10.jsp".equals(formLink)) { %></div><%}%> |
    <% if ("formIntakeHxPg11.jsp".equals(formLink)) { %><div class="thisLink"><%}%><a href="formIntakeHxPg11.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formIntakeHx.sexualHealthTitle"/></a><% if ("formIntakeHxPg11.jsp".equals(formLink)) { %></div><%}%>
</div>

<div class="buttonBar">
    <input type="submit" value="<bean:message key='oscarEncounter.formIntakeHx.save'/>" onclick="javascript:return onSave();" /> 
    <input type="submit" value="<bean:message key='oscarEncounter.formIntakeHx.print'/>" onclick="javascript:return onPrint();" />
    <input type="submit" value="<bean:message key='oscarEncounter.formIntakeHx.saveAndExit'/>" onclick="javascript:return onSaveExit();" />
    <input type="submit" value="<bean:message key='oscarEncounter.formIntakeHx.exit'/>" onclick="javascript:return onExit();" />     
</div>

<input type="hidden" name="submit" value="exit" />
<input type="hidden" name="demographic_no" value="<%=demoNo%>" />
<input type="hidden" name="c_lastVisited" value="0" />
<input type="hidden" name="formValidatedBy" value="<%=providerName%>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />