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


<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>

<title><bean:message key="admin.admin.btnSelectForm" /></title>


</head>

<body>

<h3><bean:message key="admin.admin.btnSelectForm" /></h3>

<div class="well">

<html:form action="/form/select.do" styleId="selectForm">
	<table id="scrollNumber1" name="encounterTable">
		<tr>
			<td class="MainTableLeftColumn"></td>
			<td class="MainTableRightColumn">
			<table border=0 cellspacing=4 width=400>
				<tr>
					<td>
					<table>
						<tr>
							<th align="left"><bean:message
								key="oscarEncounter.form.msgAllAvailableForms" /></th>
							<th></th>
							<th align="left"><bean:message
								key="oscarEncounter.form.msgSelectedForms" /></th>
						</tr>
						<td><html:select multiple="true" property="selectedAddTypes"
							size="10" style="width:150">
							<html:options collection="formHiddenVector" property="formName"
								labelProperty="formName" />
						</html:select></td>
						<td>
						<table>
							<tr>
								<td><input type="button" name="button" id="add" class="btn function" style="width:80px"
									value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/> >>"
									/></td>
							</tr>
							<tr>
								<td>
<input type="button" name="button" class="btn function" id="delete" style="width:80px" value="<< <bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.deleteBtn"/>"/>
								</td>
							</tr>
						</table>
						</td>
						<td><html:select multiple="true"
							property="selectedDeleteTypes" size="10" style="width:150">
							<html:options collection="formShownVector" property="formName"
								labelProperty="formName" />
						</html:select></td>
						</tr>
						<tr>
							
						</tr>
						<tr>
							<td></td>
							<td></td>
						</tr>
					</table>
					</td>
					<td>
					<input type="button" name="button" class="btn function" value="Move Up" style="width:100px" id="up"/> <br>
					<input type="button" name="button" class="btn function" value="Move Down" style="width:100px" id="down" /></td>
				</tr>
			</table>
			</td>
		</tr>

	</table>

<input type="hidden" name="forward" id="forward" value="error" />
</html:form>

</div>

<script>
registerFormSubmit('selectForm', 'dynamic-content');

$( document ).ready(function() {

$(".function").click(function() {
$("#forward").val($(this).attr("id"));


$("#selectForm").submit();
});

});




</script>

</body>
</html:html>
