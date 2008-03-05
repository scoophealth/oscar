<!--  
/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Gengeneral Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
-->
<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.model.Intake" %>
<%@ page import="org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ page import="org.oscarehr.common.dao.IntakeRequiredFieldsDao" %>
<%
    GenericIntakeEditFormBean intakeEditForm = (GenericIntakeEditFormBean) session.getAttribute("genericIntakeEditForm");
    Intake intake = intakeEditForm.getIntake();
    String clientId = String.valueOf(intake.getClientId());

%>
<html:html xhtml="true" locale="true">
<head>
    <title>Generic Intake Edit</title>
    <style type="text/css">
        @import "<html:rewrite page="/css/genericIntake.css" />";
    </style>
    <script type="text/javascript">
        <!--
        var djConfig = {
            isDebug: false,
            parseWidgets: false,
            searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane", "clientTable", "admissionsTable"]
        };

        var programMaleOnly =<%=session.getAttribute("programMaleOnly")%>;
        var programFemaleOnly =<%=session.getAttribute("programFemaleOnly")%>;
        var programTransgenderOnly =<%=session.getAttribute("programTransgenderOnly")%>;

		var RFQ_Admit = <%=session.getAttribute("RFQ_ADMIT") %>;
		var RFQ_INTAKE_ADMISSION = <%=session.getAttribute("RFQ_INTAKE_ADMISSION")%>;
		
		<%=session.getAttribute("programAgeValidationMethod")%>

        // -->

        function openSurvey(ctl) {
            var formId = ctl.options[ctl.selectedIndex].value;
            if (formId == 0) {
                return;
            }
            var id = document.getElementById('formInstanceId').value;
            var url = '<html:rewrite action="/PMmodule/Forms/SurveyExecute"/>?method=survey&type=provider&formId=' + formId + '&formInstanceId=' + id + '&clientId=' + <%=clientId%>;
            ctl.selectedIndex = 0;
            popupPage(url);
        }

        function popupPage(varpage) {
            var page = "" + varpage;
            windowprops = "height=600,width=700,location=no,"
                    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
            window.open(page, "", windowprops);
        }
        
        function init_intake() {        
        	if(RFQ_Admit == false) {
        		alert("Client must first be discharged from their current bed program before you can admit them to a new bed program or residence location.");
        	} else {
        		if(RFQ_INTAKE_ADMISSION == false) {
        			alert("You cannot make the changes to the admission. Use 'Admit Sign and Save' to change the admission program.");
        		}    
        	}	   	
        }
    </script>
    <script type="text/javascript" src="<html:rewrite page="/dojoAjax/dojo.js" />"></script>
    <script type="text/javascript" src="<html:rewrite page="/js/AlphaTextBox.js" />"></script>
    <script type="text/javascript">
        <!--
        dojo.require("dojo.widget.*");
        dojo.require("dojo.validate.*");
        // -->
    </script>
    <script type="text/javascript" src="<html:rewrite page="/js/genericIntake.js.jsp" />"></script>
    <script type="text/javascript" src="<html:rewrite page="/js/checkDate.js" />"></script>
    <html:base/>
</head>
<body class="edit" onload="init_intake()">

<html:form action="/PMmodule/GenericIntake/Edit" onsubmit="return validateEdit()" >
<html:hidden property="method"/>
<div id="layoutContainer" dojoType="LayoutContainer" layoutChildPriority="top-bottom" class="intakeLayoutContainer">
<div id="topPane" dojoType="ContentPane" layoutAlign="top" class="intakeTopPane">
    <c:out value="${sessionScope.genericIntakeEditForm.title}"/>
</div>
<div id="clientPane" dojoType="ContentPane" layoutAlign="client" class="intakeChildPane">

<div id="clientTable" dojoType="TitlePane" label="Client Information" title="(Fields marked with * are mandatory)"
     labelNodeClass="intakeSectionLabel" containerNodeClass="intakeSectionContainer">
<table class="intakeTable">
<tr>
    <%
        boolean isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_FIRST_NAME);
        String REQUIRED_MARKER = " *";
    %>
    <td><label>First Name<%=isRequired ? REQUIRED_MARKER : ""%><br><html:text property="client.firstName" size="20"
                                                                              maxlength="30"/></label></td>
    <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_LAST_NAME);%>
    <td><label>Last Name<%=isRequired ? REQUIRED_MARKER : ""%><br><html:text property="client.lastName" size="20"
                                                                             maxlength="30"/></label></td>
    <td>
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_GENDER);%>
        <label>Gender<%=isRequired ? REQUIRED_MARKER : ""%><br>
            <html:select property="client.sex">
                <html:optionsCollection property="genders" value="code" label="description"/>
            </html:select>
        </label>
    </td>
    <td>
        <table>
            <tr>
                <td>
                    <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_BIRTH_DATE);%>
                    <label>Birth Date<%=isRequired ? REQUIRED_MARKER : ""%><br>
                        <html:select property="client.monthOfBirth">
                            <html:optionsCollection property="months" value="value" label="label"/>
                        </html:select>
                    </label>
                </td>
                <td>
                    <label>&nbsp;<br>
                        <html:select property="client.dateOfBirth">
                            <html:optionsCollection property="days" value="value" label="label"/>
                        </html:select>
                    </label>
                </td>
                <td>
                    <label>&nbsp;<br>
                        <html:text property="client.yearOfBirth" size="4" maxlength="4"/>&nbsp;(YYYY)
                    </label>
                </td>
            </tr>
        </table>

    </td>
</tr>
<tr>
    <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_ALIAS);%>
        <td><label>Alias<%=isRequired ? REQUIRED_MARKER : ""%>
        	<br><html:text size="40" maxlength="70" property="client.alias"/></label>
       </td>
    </caisi:isModuleLoad>
    
	<caisi:isModuleLoad moduleName="GET_OHIP_INFO" reverse="false">    
        <td><label>Health Card #<br>
            <input type="text" size="10" maxlength="10" dojoType="IntegerTextBox" name="client.hin"
                   value="<bean:write name="genericIntakeEditForm"  property="client.hin"/>"/>
        </label></td>
        <td><label>Version<br>
            <input type="text" size="2" maxlength="2" dojoType="AlphaTextBox" name="client.ver"
                   value="<bean:write name="genericIntakeEditForm"  property="client.ver"/>"/>
        </label></td>    
	</caisi:isModuleLoad>
</tr>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
    <tr>
        <td>
            <label>Email<br>
                <input type="text" size="20" maxlength="100" dojoType="EmailTextBox" name="client.email"
                       value="<bean:write name="genericIntakeEditForm"  property="client.email"/>"/>
            </label></td>
        <td>
            <label>Phone #<br>
                <input type="text" size="20" maxlength="20" dojoType="UsPhoneNumberTextbox" name="client.phone"
                       value="<bean:write name="genericIntakeEditForm"  property="client.phone"/>"/>
            </label></td>
        <td>
            <label>Secondary Phone #<br>
                <input type="text" size="20" maxlength="20" dojoType="UsPhoneNumberTextbox" name="client.phone2"
                       value="<bean:write name="genericIntakeEditForm"  property="client.phone2"/>"/>
            </label>
        </td>
    </tr>
    <tr>
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_STREET);%>
        <td><label>Street<%=isRequired ? REQUIRED_MARKER : ""%><br><html:text size="20" maxlength="60"
                                                                              property="client.address"/></label></td>
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_CITY);%>
        <td><label>City<%=isRequired ? REQUIRED_MARKER : ""%><br><html:text size="20" maxlength="20"
                                                                            property="client.city"/></label></td>
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_PROVINCE);%>
        <td><label>Province<%=isRequired ? REQUIRED_MARKER : ""%><br>
            <html:select property="client.province">
                <html:optionsCollection property="provinces" value="value" label="label"/>
            </html:select>
        </label>
        </td>
        <!-- <td><label>Province<br><html:text property="client.province" /></label></td> -->
        <%isRequired = IntakeRequiredFieldsDao.isRequired(IntakeRequiredFieldsDao.FIELD_POSTAL_CODE);%>
        <td><label>Postal Code<%=isRequired ? REQUIRED_MARKER : ""%><br><html:text property="client.postal" size="9"
                                                                                   maxlength="9"/></label></td>
    </tr>

<c:if test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
    <tr>
        <td>
            <a href="javascript:void(0);"
               onclick="window.open('<caisi:CaseManagementLink demographicNo="<%=intake.getClientId()%>" providerNo="<%=intake.getStaffId()%>" providerName="<%=intake.getStaffName()%>" />', '', 'width=800,height=600,resizable=1,scrollbars=1')">
                <span>Case Management Notes</span>
            </a>
        </td>
    </tr>
</c:if>
</caisi:isModuleLoad>
</table>
</div>


<c:if test="${not empty sessionScope.genericIntakeEditForm.bedCommunityPrograms || not empty sessionScope.genericIntakeEditForm.servicePrograms}">
    <div id="admissionsTable" dojoType="TitlePane" label="Program Admissions" labelNodeClass="intakeSectionLabel"
         containerNodeClass="intakeSectionContainer">
        <logic:messagesPresent>
            <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
                <html:messages id="error" bundle="pmm">
                    <tr>
                        <td class="error"><c:out value="${error}"/></td>
                    </tr>
                </html:messages>
            </table>
        </logic:messagesPresent>
        <table class="intakeTable">
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.bedCommunityPrograms}">
                    <td class="intakeBedCommunityProgramCell"><label><c:out
                            value="${sessionScope.genericIntakeEditForm.bedCommunityProgramLabel}"/></label></td>
                </c:if>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.servicePrograms}">
                    <td><label>Service Programs</label></td>
                </c:if>
            </tr>
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.bedCommunityPrograms}">
                    <td class="intakeBedCommunityProgramCell">
                        <html:select property="bedCommunityProgramId">
                            <html:optionsCollection property="bedCommunityPrograms" value="value" label="label"/>
                        </html:select>
                    </td>
                </c:if>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.servicePrograms}">
                    <td>
                        <c:forEach var="serviceProgram" items="${sessionScope.genericIntakeEditForm.servicePrograms}">
                            <html-el:multibox property="serviceProgramIds" value="${serviceProgram.value}"/>&nbsp;<c:out
                                value="${serviceProgram.label}"/><br/>
                        </c:forEach>
                    </td>
                </c:if>
            </tr>
        </table>
    </div>

</c:if>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
	<div id="admissionsTable" dojoType="TitlePane" label="Intake Location" labelNodeClass="intakeSectionLabel"
         containerNodeClass="intakeSectionContainer">
        <table class="intakeTable">
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.programsInDomain}">
                    <td class="intakeBedCommunityProgramCell"><label>
                            <c:out
                            value="${sessionScope.genericIntakeEditForm.programInDomainLabel}"/></label></td>
                 </c:if>

            </tr>
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.programsInDomain}">
                    <td class="intakeBedCommunityProgramCell">
                        <html:select property="programInDomainId">
                            <html:optionsCollection property="programsInDomain" value="value" label="label"/>
                        </html:select>
                    </td>
                </c:if>
            </tr>
        </table>
    </div>

    <div id="admissionsTable" dojoType="TitlePane" label="External Referral" labelNodeClass="intakeSectionLabel"
         containerNodeClass="intakeSectionContainer">
        <table class="intakeTable">
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.externalPrograms}">
                    <td class="intakeBedCommunityProgramCell"><label><c:out
                            value="${sessionScope.genericIntakeEditForm.externalProgramLabel}"/></label></td>
                </c:if>

            </tr>
            <tr>
                <c:if test="${not empty sessionScope.genericIntakeEditForm.externalPrograms}">
                    <td class="intakeBedCommunityProgramCell">
                        <html:select property="externalProgramId">
                            <html:optionsCollection property="externalPrograms" value="value" label="label"/>
                        </html:select>
                    </td>
                </c:if>
            </tr>
        </table>
    </div>
    <br/>

    <c:if test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
        <div id="admissionsTable" dojoType="TitlePane" label="Intake Assessment" labelNodeClass="intakeSectionLabel"
             containerNodeClass="intakeSectionContainer">
            <table class="intakeTable">
                <tr>
                    <td><input type="hidden" id="formInstanceId" value="0"/></td>
                </tr>
                <tr>
                    <td>
                        <select property="form.formId" onchange="openSurvey(this);">
                            <option value="0">&nbsp;</option>
                            <c:forEach var="survey" items="${survey_list}">
                                <option value="<c:out value="${survey.formId}"/>"><c:out
                                        value="${survey.description}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
        </div>
    </c:if>
</caisi:isModuleLoad>

<br/>

<caisi:intake base="<%=5%>" intake="<%=intake%>"/>
</div>
<div id="bottomPane" dojoType="ContentPane" layoutAlign="bottom" class="intakeBottomPane">
    <table class="intakeTable">
        <logic:messagesPresent>
            <html:messages id="error" bundle="pmm">
                <tr>
                    <td class="error"><c:out value="${error}"/></td>
                </tr>
            </html:messages>
        </logic:messagesPresent>
        <logic:messagesPresent message="true">
            <html:messages id="message" message="true" bundle="pmm">
                <tr>
                    <td class="message"><c:out value="${message}"/></td>
                </tr>
            </html:messages>
        </logic:messagesPresent>
        <tr>
            <td>
                <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
                    <html:submit onclick="return save()">Save</html:submit>&nbsp;
                </caisi:isModuleLoad>
                <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
                    <!--
       				<c:choose>
                        <c:when test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                            <html:submit onclick="save()">Save</html:submit>&nbsp;
                        </c:when>
                        <c:otherwise>
                            <html:submit onclick="save()">Save And Do Intake Accessment</html:submit>&nbsp;
                        </c:otherwise>
                    </c:choose>
                    -->
                    
                    <html:submit onclick="return save_temp()">Temporary Save</html:submit>&nbsp;
                    <html:submit onclick="return save_admit()">Admit, Sign And Save</html:submit>&nbsp;
                    <html:submit onclick="save_notAdmit()">Intake Without Admission, Sign And Save</html:submit>
                </caisi:isModuleLoad>
                <html:reset>Reset</html:reset>
            </td>
            <td align="right">
                <c:choose>
                    <c:when test="${not empty sessionScope.genericIntakeEditForm.client.demographicNo}">
                        <html:submit onclick="clientEdit()">Close</html:submit>
                    </c:when>
                    <c:otherwise>
                        <input type="button" value="Close" onclick="history.go(-1)"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</div>
</div>
</html:form>
</body>
</html:html>