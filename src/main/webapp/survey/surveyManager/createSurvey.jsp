
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



<%@ include file="/survey/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<html:form action="/SurveyManager" method="POST" styleId="surveyForm">
	<input type="hidden" name="method" value="create_survey" />
	<input type="hidden" name="numPages" id="numPages" value="1" />

	<html:hidden property="survey.id" />
	<br />
	<table width="100%">
		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" bundle="survey">
				<tr>
					<td colspan="3" class="message"><c:out value="${message}" /></td>
				</tr>
			</html:messages>
		</logic:messagesPresent>
		<logic:messagesPresent>
			<html:messages id="error" bundle="survey">
				<tr>
					<td colspan="3" class="error"><c:out value="${error}" /></td>
				</tr>
			</html:messages>
		</logic:messagesPresent>
		<tr>
			<td class="leftfield">Form Name:&nbsp;&nbsp; <html:text
				property="survey.description" styleClass="formElement" />&nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td class="leftfield">Template:&nbsp;&nbsp; <html:select
				property="web.templateId" styleClass="formElement">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="templates" property="id"
					labelProperty="description" />
			</html:select></td>
		</tr>
		<tr>
			<td><br />
			<html:submit>Create Form</html:submit></td>
		</tr>
	</table>
</html:form>
