
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
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Question Editor</title>
</head>

<body>
<script>
		function save() {
			document.surveyForm.method.value="save_question";
			document.surveyForm.submit();
			window.close();
			opener.document.surveyForm.method.value="form";
			opener.document.surveyForm.submit();
			
		}
		
		function adjust_possible_answers() {
			document.surveyForm.method.value='question_adjust_possible_answers';
			document.surveyForm.submit();
		}	
	</script>

<html:form action="/SurveyManager">
	<input type="hidden" name="method" value="save_question" />
	<center>
	<h2>Question Editor</h2>
	</center>
	<br />
	<table>
		<tr>
			<td colspan="2">Question:<br />
			<html:textarea property="questionModel.description" rows="5"
				cols="50"></html:textarea></td>
		</tr>
		<tr>
			<td colspan="2"><html:checkbox property="questionModel.bold"
				value="true" />Bold&nbsp;&nbsp; <html:checkbox
				property="questionModel.underline" value="true" />Underline&nbsp;&nbsp;
			<html:checkbox property="questionModel.italics" value="true" />Italics&nbsp;&nbsp;
			<html-el:select property="questionModel.color">
				<html-el:option value="">&nbsp;</html-el:option>
				<c:forEach var="color" items="${colors}">
					<html-el:option value="${color}">
						<c:out value="${color}" />
					</html-el:option>
				</c:forEach>
			</html-el:select></td>
		</tr>
		<tr>
			<td colspan="2"><br />
			</td>
		</tr>
		<tr>
			<td>Units (optional)</td>
			<td><html:text property="questionModel.unit" /></td>
		</tr>
		<tr>
			<td>Orientation</td>
			<td><html:select property="questionModel.orientation">
				<html:option value="vertical">vertical</html:option>
				<html:option value="horizontal">horizontal</html:option>
			</html:select></td>
		</tr>
		<tr>
			<td>Data Link</td>
			<td><html-el:select property="questionModel.dataLink">
				<html-el:option value="">&nbsp;</html-el:option>
				<c:forEach var="obj" items="${oscarVars}">
					<html-el:option value="${obj.value}">
						<c:out value="${obj.label}" />
					</html-el:option>
				</c:forEach>
			</html-el:select></td>
		</tr>
		<tr>
			<td>Pre-filled Data</td>
			<td><html-el:select property="questionModel.caisiObject">
				<html-el:option value="">&nbsp;</html-el:option>
				<c:forEach var="obj" items="${caisiobjects}">
					<html-el:option value="${obj}">
						<c:out value="${obj}" />
					</html-el:option>
				</c:forEach>
			</html-el:select></td>
		</tr>
		<tr>
			<td colspan="2"><br />
			</td>
		</tr>

		<tr>
			<td>Type:</td>
			<td><c:set var="question" scope="page"
				value="${surveyForm.map.questionModel}"></c:set> <%
						org.oscarehr.surveymodel.Question question = (org.oscarehr.surveymodel.Question)pageContext.getAttribute("question");						    
              			if(question.getType().isSetRank()) { out.print("Rank"); }
               			if(question.getType().isSetScale()) { out.print("Scale"); }
               			if(question.getType().isSetOpenEnded()) { out.print("Open Ended"); }
               			if(question.getType().isSetSelect()) { out.print("Select"); }
               			if(question.getType().isSetDate()) { out.print("Date"); }
              			%>
			</td>
		</tr>

		<tr>
			<td colspan="2"><br />
			</td>
		</tr>

		<tr>
			<!--  type specific -->
			<td colspan="2">
			<%if(question.getType().isSetOpenEnded()) {  %>
			<table width="100%">
				<tr>
					<td>Rows:</td>
					<td><html:text property="questionModel.type.openEnded.rows"
						size="4" /></td>
				</tr>
				<tr>
					<td>Cols:</td>
					<td><html:text property="questionModel.type.openEnded.cols"
						size="4" /></td>
				</tr>
			</table>
			<% } %> <%if(question.getType().isSetDate()) {  %>
			<table width="100%">
				<tr>
					<td>Format:</td>
					<td><html:select property="web.dateFormat">
						<html:options collection="dateFormats" property="value"
							labelProperty="label" />
					</html:select></td>
				</tr>
			</table>
			<% } %> <%if(question.getType().isSetSelect()) {  %> <script>
								function set_select_type() {
									var selectType = document.surveyForm.elements['questionModel.type.select.renderType'];
									for(var x=0;x<selectType.length;x++) {
										if(selectType[x].checked) {
											var value = selectType[x].value;
											if(value == 'checkbox') {
												document.surveyForm.elements['questionModel.type.select.otherAnswer'].disabled=false;
											}
											if(value == 'radio') {
												document.surveyForm.elements['questionModel.type.select.otherAnswer'].disabled=false;											
											}
											if(value == 'select') {
												document.surveyForm.elements['questionModel.type.select.otherAnswer'].disabled=true;
												document.surveyForm.elements['questionModel.type.select.otherAnswer'].value=false;
											}
										}
									}
								}
							</script>
			<table width="100%">
				<tr>
					<td><html:radio
						property="questionModel.type.select.renderType" value="radio"
						onclick="set_select_type()" /></td>
					<td>Radio Buttons</td>
				</tr>
				<tr>
					<td><html:radio
						property="questionModel.type.select.renderType" value="select"
						onclick="set_select_type()" /></td>
					<td>Combo Box</td>
				</tr>
				<tr>
					<td><html:radio
						property="questionModel.type.select.renderType" value="checkbox"
						onclick="set_select_type()" /></td>
					<td>Checkboxes (Multi-Select)</td>
				</tr>
				<tr>
					<td>Orientation</td>
					<td><html:select
						property="questionModel.type.select.orientation">
						<html:option value="vertical">vertical</html:option>
						<html:option value="horizontal">horizontal</html:option>
					</html:select></td>
				</tr>
				<tr>
					<td>Allow "Other":</td>
					<td><html:select
						property="questionModel.type.select.otherAnswer">
						<html:option value="true" />
						<html:option value="false" />
					</html:select></td>
				</tr>

				<tr>
					<td>Number of Answers</td>
					<td><html:select property="web.numAnswers"
						onchange="adjust_possible_answers()">
						<%for(int x=1;x<50;x++) { %>
						<html:option value="<%=String.valueOf(x)%>" />
						<%} %>
					</html:select></td>
				</tr>
				<tr>
					<td>Answers</td>
					<td><c:forEach var="answer"
						items="${surveyForm.map.questionModel.type.select.possibleAnswers.answerArray}"
						varStatus="status">
						<input type="text"
							name="answer_<c:out value="${status.index+1}"/>"
							value="<c:out value="${answer}"/>" />
						<br />
					</c:forEach></td>
				</tr>
			</table>
			<% } %>
			</td>
		</tr>
	</table>
	<!-- 
			<input type="button" value="Save" onClick="save();"/>
			<input type="button" value="Cancel" onclick="window.close();"/>
			 -->
	<html:submit styleClass="button">Save</html:submit>
	<html:cancel value="Cancel" />
</html:form>
</body>
</html:html>
