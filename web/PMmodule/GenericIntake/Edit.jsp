<%@include file="/taglibs.jsp"%>
<%@page	import="org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean"%>

<html:html xhtml="true" locale="true">
	<head>
		<title>Generic Intake Edit</title>
		
		<style type="text/css">
			@import "<html:rewrite page="/css/genericIntake.css" />";
		</style>

		<script type="text/javascript" src="<html:rewrite page="/js/genericIntake.js" />"></script>
		
		<script type="text/javascript">
			var djConfig = {
				isDebug: false,
				parseWidgets: false,
				searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane", "clientTable", "admissionsTable"]
			};
		</script>
		<script type="text/javascript" src="<html:rewrite page="/dojoAjax/dojo.js" />"></script>
		<script type="text/javascript">
			dojo.require("dojo.widget.LayoutContainer");
			dojo.require("dojo.widget.ContentPane");
			dojo.require("dojo.widget.TabContainer");
			dojo.require("dojo.widget.TitlePane");
		</script>
		<html:base />
	</head>
	<body>
		<html:form action="/PMmodule/GenericIntake/Edit">
			<html:hidden property="method" />
			<!-- Container -->
			<div id="layoutContainer" dojoType="LayoutContainer" layoutChildPriority="top-bottom" class="intakeLayoutContainer">
				<%
					GenericIntakeEditFormBean intakeEditForm = (GenericIntakeEditFormBean) session.getAttribute("genericIntakeEditForm");
				%>
				<!-- Top -->
				<div id="topPane" dojoType="ContentPane" layoutAlign="top" class="intakeTopPane">
					<c:out value="${sessionScope.genericIntakeEditForm.title}" />
				</div>
				<!-- Client -->
				<div id="clientPane" dojoType="ContentPane" layoutAlign="client" class="intakeChildPane">
					<!-- Client Information -->
					<div id="clientTable" dojoType="TitlePane" label="Client Information" labelNodeClass="intakeSectionLabel" containerNodeClass="intakeSectionContainer">
						<table class="intakeTable">
							<tr>
								<td><label>First Name<br><html:text property="client.firstName" /></label></td>
								<td><label>Last Name<br><html:text property="client.lastName" /></label></td>
								<td><label>Gender<br>
										<html:select property="client.sex">
											<html:optionsCollection property="genders" value="value" label="label" />
										</html:select>
									</label>
								</td>
								<td>
									<label>Birth Date<br>
										<html:select property="client.monthOfBirth">
											<html:optionsCollection property="months" value="value" label="label" />
										</html:select>&nbsp;
										<html:select property="client.dateOfBirth">
											<html:optionsCollection property="days" value="value" label="label" />
										</html:select>&nbsp;
										<html:text property="client.yearOfBirth" size="4" />&nbsp;(YYYY)
									</label>
								</td>
							</tr>
							<tr>
								<td><label>Health Card<br><html:text property="client.hin" /></label></td>
								<td><label>Version<br><html:text property="client.ver" /></label></td>
							</tr>
							<tr>
								<td><label>Email<br><html:text property="client.email" /></label></td>
								<td><label>Phone<br><html:text property="client.phone" /></label></td>
								<td><label>Secondary Phone<br><html:text property="client.phone2" /></label></td>
							</tr>
							<tr>
								<td><label>Street<br><html:text property="client.address" /></label></td>
								<td><label>City<br><html:text property="client.city" /></label></td>
								<td><label>Province<br><html:text property="client.province" /></label></td>
								<td><label>Postal Code<br><html:text property="client.postal" /></label></td>
							</tr>
							<!-- Case Management -->
							<c:if test="${!empty sessionScope.genericIntakeEditForm.client.demographicNo}">
								<tr>
									<td>
										<a href="javascript:void(0);" onclick="window.open('<caisi:CaseManagementLink demographicNo="<%=intakeEditForm.getIntake().getClientId()%>" providerNo="<%=intakeEditForm.getIntake().getStaffId()%>" providerName="<%=intakeEditForm.getIntake().getStaffName()%>" />', '', 'width=800,height=600,resizable')" >
											<span>Case Management Notes</span>
										</a>
									</td>
								</tr>
							</c:if>
						</table>
					</div>
					<!-- Program Admissions -->
					<div id="admissionsTable" dojoType="TitlePane" label="Program Admissions" labelNodeClass="intakeSectionLabel" containerNodeClass="intakeSectionContainer">
						<table class="intakeTable">
							<tr>
								<td class="intakeBedCommunityProgramCell"><label><c:out value="${sessionScope.genericIntakeEditForm.bedCommunityProgramLabel}" /></label></td>
								<td><label>Service Programs</label></td>
							</tr>
							<tr>
								<td class="intakeBedCommunityProgramCell">
									<html:select property="bedCommunityProgramId">
										<html:optionsCollection property="bedCommunityPrograms" value="value" label="label" />
									</html:select>
								</td>
								<td>
									<c:forEach var="serviceProgram" items="${sessionScope.genericIntakeEditForm.servicePrograms}">
										<html-el:multibox property="serviceProgramIds" value="${serviceProgram.value}" />&nbsp;<c:out value="${serviceProgram.label}" />
									</c:forEach>
								</td>
							</tr>
						</table>
					</div>
					<caisi:intake base="<%=5%>" intake="<%=intakeEditForm.getIntake()%>" />
				</div>
				<!-- Bottom -->
				<div id="bottomPane" dojoType="ContentPane" layoutAlign="bottom" class="intakeBottomPane">
					<table class="intakeTable">
						<tr>
							<td>
								<html:submit onclick="save()">Save</html:submit>
								<html:reset>Reset</html:reset>
							</td>
							<td align="right">
								<html:cancel>Close</html:cancel>
							</td>
						</tr>
					</table>
					<%@ include file="/common/messages.jsp"%>
				</div>
			</div>
		</html:form>
	</body>
</html:html>