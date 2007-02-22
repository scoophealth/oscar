<%@ include file="/taglibs.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean"%>

<html:html xhtml="true" locale="true">
	<head>
		<title>Generic Intake Edit</title>
		
		<style type="text/css">
			@import "<html:rewrite page="/css/genericIntake.css" />";
		</style>

		<script type="text/javascript" src="<html:rewrite page="/js/genericIntake.js" />" />
		
		<script type="text/javascript">
			var djConfig = {
				isDebug: false,
				parseWidgets: false,
				searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane"]
			};
		</script>
		<script type="text/javascript" src="<html:rewrite page="/dojoAjax/dojo.js" />" />
		<script type="text/javascript">
			dojo.require("dojo.widget.LayoutContainer");
			dojo.require("dojo.widget.TabContainer");
			dojo.require("dojo.widget.AccordionContainer");
			dojo.require("dojo.widget.ContentPane");
		</script>
		<html:base />
	</head>
	<body>
		<html:form action="/PMmodule/GenericIntake/Edit">
			<div id="layoutContainer" dojoType="LayoutContainer" layoutChildPriority="top-bottom" class="intakeLayoutContainer">
				<div id="topPane" dojoType="ContentPane" layoutAlign="top" class="intakeTopPane">
					Quick Intake
				</div>
				<div id="clientPane" dojoType="ContentPane" layoutAlign="client" class="intakeChildPane">
					<div dojoType="AccordionContainer" class="intakeSectionContainer" >
						<div dojoType="ContentPane" label="Client Information" open="true" class="intakeSection" >
							<%
												GenericIntakeEditFormBean intakeEditForm = new GenericIntakeEditFormBean();
												intakeEditForm.setClient(new Demographic());
												intakeEditForm.setClientBedPrograms(new ArrayList<Program>());
												intakeEditForm.setServiceProgramLabelValues(new ArrayList<Program>());
												intakeEditForm.setServiceProgramIds(new Integer[] { 0, 1, 2, 3, 4, 5 });
												
												session.setAttribute("genericIntakeEditForm", intakeEditForm);
							%>
							<table class="intakeTable">
								<tr>
									<td><label>First Name<br /><html:text property="client.firstName" /></label></td>
									<td><label>Last Name<br /><html:text property="client.lastName" /></label></td>
									<td><label>Sex<br /><html:select property="client.sex"><html:option value=""/><html:option value="M">Male</html:option><html:option value="F">Female</html:option><html:option value="T">Transgendered</html:option></html:select></label></td>
									<td><label>Birth Date<br /><html:select property="client.monthOfBirth"><html:option value="" /><html:option value="1">01</html:option></html:select>&nbsp;<html:select property="client.dateOfBirth"><html:option value="" /><html:option value="1">01</html:option></html:select>&nbsp;<html:text property="client.yearOfBirth" size="4" />&nbsp;(mm/dd/yyyy)</label></td>
								</tr>
								<tr>
									<td><label>Health Card<br /><html:text property="client.hin" /></label></td>
									<td><label>Version<br /><html:text property="client.ver" /></label></td>
								</tr>
								<tr>
									<td><label>Street<br /><html:text property="client.address" /></label></td>
									<td><label>City<br /><html:text property="client.city" /></label></td>
									<td><label>Province<br /><html:text property="client.province" /></label></td>
									<td><label>Postal Code<br /><html:text property="client.postal" /></label></td>
								</tr>
								<tr>
									<td><label>Email<br /><html:text property="client.email" /></label></td>
									<td><label>Phone<br /><html:text property="client.phone" /></label></td>
									<td><label>Secondary Phone<br /><html:text property="client.phone2" /></label></td>
								</tr>
							</table>
						</div>
						<div dojoType="ContentPane" label="Program Admissions" class="intakeSection" >
							<table class="intakeTable">
								<tr>
									<td class="intakeBedProgramCell"><label>Bed Program</label></td>
									<td><label>Service Programs</label></td>
								</tr>
								<tr>
									<td class="intakeBedProgramCell">
 										<html:select property="selectedBedProgramId">
 											<html:option value=""></html:option>
 											<html:option value="1">Bed Program</html:option>
 											<html:option value="2">Another Bed Program</html:option>
 										</html:select>
 									</td>
 									<td>
										Service Program 1&nbsp;<input type="checkbox" name="selectedServiceProgramIds[0]" value="1" /><p>A long description of the program</p><br />
										Service Program 2&nbsp;<input type="checkbox" name="selectedServiceProgramIds[1]" value="2" /><br />
										Service Program 3&nbsp;<input type="checkbox" name="selectedServiceProgramIds[2]" value="3" /><br />
										Service Program 4&nbsp;<input type="checkbox" name="selectedServiceProgramIds[3]" value="4" /><br />
										Service Program 5&nbsp;<input type="checkbox" name="selectedServiceProgramIds[4]" value="5" />
 									</td>
								</tr>
							</table>
						</div>
						<div dojoType="ContentPane" label="" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Notes for completing this intake" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Identifying Data" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Reason for Admission" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Assistance Required" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Identification" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="ON-LINE CHECK" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="HEALTH" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="SURVEY MODULE - ACCESS TO HEALTH CARE" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="EMERGENCY CONTACT" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="STAFF RATINGS AND IDENTIFIED ISSUES" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="ORIENTATION TO AGENCY" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="COMMENTS/SUMMARY" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="COMPLETED BY" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="EMERGENCY CONTACT" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
						<div dojoType="ContentPane" label="Admission" class="intakeSection" >
							<table class="intakeQuestionTable">
							</table> <!-- End Question Table -->
						</div> <!-- End Section -->
					</div> <!-- End Section Container -->
				</div>
				<div id="bottomPane" dojoType="ContentPane" layoutAlign="bottom" class="intakeBottomPane">
					<html:submit>Save</html:submit>
					<html:reset>Reset</html:reset>
					<html:cancel>Cancel</html:cancel>
				</div>
			</div>
		</html:form>
	</body>
</html:html>