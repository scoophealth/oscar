<%@ include file="/taglibs.jsp" %>

<html:html>
	<head>
		<title>Program Activity Report Generator</title>
	</head>
	
	<body>
	<Br/><br/>
		<html:form action="/PMmodule/Reports/ProgramActivityReport">
		<input type="hidden" name="method" value="generate"/>
<div id="projecthome" class="app">

	<br />
	<div class="h4">
		<h5>Enter Report Criteria</h5>
	</div>
	<div class="axial">
		<table border="0" cellspacing="2" cellpadding="3">
			<tr>
				<th>Start Date</th>
				<td><html:text property="form.startDate"  size="15" /> (yyyy-mm-dd)</td>
			</tr>
			<tr>
			    <th>End Date</th>
				<td>
					<html:text property="form.endDate"  size="15" /> (yyyy-mm-dd)
				</td>
			</tr>
			<!-- 
			<tr>
				<th>Program</th>
				<td>
					<html:select property="form.programId">
						<html:options collection="programs"  property="id" labelProperty="name"/>
					</html:select>
				</td>
			</tr>
			-->
			<tr>
				<td align="center" colspan="2">
					<html:submit value="Generate Report" />
		    	</td>
			</tr>
		</table>
	</div>
</div>
</html:form>

	</body>
	
</html:html>