
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
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.survey.web.formbean.SurveyExecuteDataBean"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="org.oscarehr.surveymodel.*"%>
<%@ page import="org.oscarehr.PMmodule.web.forms.SurveyExecuteAction"%>

<c:set var="id" scope="page">
	<c:out value="${question.id}" />
</c:set>
<%
	//This little piece of code is needed to expose the value-map from the formbean
	//to the scriptlets - too bad html:text doesn't support the id attribute.
	String id = ((String)pageContext.getAttribute("id"));
	DynaValidatorForm form = (DynaValidatorForm)session.getAttribute("surveyExecuteForm");
	SurveyExecuteDataBean data = (SurveyExecuteDataBean)form.get("data");
	Map valueMap = data.getValues();
%>
<script>
	function select_checkbox(name) {
		var element = document.surveyExecuteForm.elements['data.value(' + name + ')'];
		var hiddenElement = document.surveyExecuteForm.elements['data.value(checkbox_' + name + ')'];
		if(element.checked == true) {
			hiddenElement.value="checked";
		} else {
			hiddenElement.value="";
		}
	}
	
	function select_program(ctl) {
		var programId = ctl.options[ctl.selectedIndex].value;
		document.surveyExecuteForm.elements['view.admissionId'].value=programId;
		document.surveyExecuteForm.method.value='refresh';
		document.surveyExecuteForm.submit();
	}
</script>
<td>
<table>
	<tR>
		<c:choose>

			<c:when test="${question.type.openEnded != null}">
				<c:choose>
					<c:when test="${question.type.openEnded.rows == 1}">
						<td><c:if test="${question.bold eq 'true'}">
							<b>
						</c:if> <c:if test="${question.underline eq 'true'}">
							<u>
						</c:if> <c:if test="${question.italics eq 'true'}">
							<i>
						</c:if> <c:choose>
							<c:when test="${not empty question.color}">
								<span style="color:<c:out value="${question.color}"/>"><c:out
									value="${question.description}" /></span>
							</c:when>
							<c:otherwise>
								<c:out value="${question.description}" />
							</c:otherwise>
						</c:choose> <c:if test="${question.bold eq 'true'}">
							</b>
						</c:if> <c:if test="${question.underline eq 'true'}">
							</u>
						</c:if> <c:if test="${question.italics eq 'true'}">
							</i>
						</c:if></td>
						<td><html-el:text
							property="data.value(${pageNumber}_${sectionId}_${question.id})"
							size="${question.type.openEnded.cols}" /></td>
					</c:when>
					<c:otherwise>
						<td colspan="2"><c:if test="${question.bold eq 'true'}">
							<b>
						</c:if> <c:if test="${question.underline eq 'true'}">
							<u>
						</c:if> <c:if test="${question.italics eq 'true'}">
							<i>
						</c:if> <c:choose>
							<c:when test="${not empty question.color}">
								<span style="color:<c:out value="${question.color}"/>"><c:out
									value="${question.description}" /></span>
							</c:when>
							<c:otherwise>
								<c:out value="${question.description}" />
							</c:otherwise>
						</c:choose> <c:if test="${question.bold eq 'true'}">
							</b>
						</c:if> <c:if test="${question.underline eq 'true'}">
							</u>
						</c:if> <c:if test="${question.italics eq 'true'}">
							</i>
						</c:if> <br />
						<html-el:textarea
							property="data.value(${pageNumber}_${sectionId}_${question.id})"
							rows="${question.type.openEnded.rows}"
							cols="${question.type.openEnded.cols}"></html-el:textarea></td>
					</c:otherwise>
				</c:choose>
			</c:when>

			<c:when test="${question.type.date != null}">
				<!-- calendar -->
				<td><c:if test="${question.bold eq 'true'}">
					<b>
				</c:if> <c:if test="${question.underline eq 'true'}">
					<u>
				</c:if> <c:if test="${question.italics eq 'true'}">
					<i>
				</c:if> <c:choose>
					<c:when test="${not empty question.color}">
						<span style="color:<c:out value="${question.color}"/>"><c:out
							value="${question.description}" /></span>
					</c:when>
					<c:otherwise>
						<c:out value="${question.description}" />
					</c:otherwise>
				</c:choose> <c:if test="${question.bold eq 'true'}">
					</b>
				</c:if> <c:if test="${question.underline eq 'true'}">
					</u>
				</c:if> <c:if test="${question.italics eq 'true'}">
					</i>
				</c:if> &nbsp;(<c:out value="${question.type.date}" />)</td>
				<td><html-el:text
					property="data.value(${pageNumber}_${sectionId}_${question.id})"
					styleId="${pageNumber}_${sectionId}_${question.id}" /> &nbsp; <img
					src="<c:out value="${ctx}"/>/images/calendar.jpg"
					id="<c:out value="${pageNumber}"/>_<c:out value="${sectionId}"/>_<c:out value="${question.id}"/>_trigger"
					style="cursor: pointer;" title="Date selector" /> <%
					Question q = (Question)request.getAttribute("question");
					String format = q.getType().getDate().toString();
					String calFormat = SurveyExecuteAction.getCalendarFormat(format);
				%> <script type="text/javascript">
				    Calendar.setup({
				        inputField     :    "<c:out value="${pageNumber}"/>_<c:out value="${sectionId}"/>_<c:out value="${question.id}"/>",     // id of the input field
				        ifFormat       :    "<%=calFormat%>",      // format of the input field
				        button         :    "<c:out value="${pageNumber}"/>_<c:out value="${sectionId}"/>_<c:out value="${question.id}"/>_trigger",  // trigger for the calendar (button ID)
				        align          :    "Tl",           // alignment (defaults to "Bl")
				        singleClick    :    true
				    });
				</script></td>
			</c:when>

			<c:when test="${question.type.select != null}">
				<td valign="top"><c:if test="${question.bold eq 'true'}">
					<b>
				</c:if> <c:if test="${question.underline eq 'true'}">
					<u>
				</c:if> <c:if test="${question.italics eq 'true'}">
					<i>
				</c:if> <c:choose>
					<c:when test="${not empty question.color}">
						<span style="color:<c:out value="${question.color}"/>"><c:out
							value="${question.description}" /></span>
					</c:when>
					<c:otherwise>
						<c:out value="${question.description}" />
					</c:otherwise>
				</c:choose> <c:if test="${question.bold eq 'true'}">
					</b>
				</c:if> <c:if test="${question.underline eq 'true'}">
					</u>
				</c:if> <c:if test="${question.italics eq 'true'}">
					</i>
				</c:if></td>
				<td><c:choose>
					<c:when test="${question.type.select.renderType eq 'select'}">

						<c:choose>
							<c:when test="${question.caisiObject eq 'Program Selector'}">
								<html-el:select
									property="data.value(${pageNumber}_${sectionId}_${question.id})"
									onchange="select_program(this);">
									<html-el:option value="0">&nbsp;</html-el:option>
									<c:forEach var="admission" items="${admissions}">
										<html-el:option value="${admission.id}">
											<c:out value="${admission.programName}" />
										</html-el:option>
									</c:forEach>
								</html-el:select>
							</c:when>
							<c:otherwise>
								<html-el:select
									property="data.value(${pageNumber}_${sectionId}_${question.id})">
									<c:forEach var="answer"
										items="${question.type.select.possibleAnswers.answerArray}">
										<html-el:option value="${answer}">
											<c:out value="${answer}" />
										</html-el:option>
									</c:forEach>
								</html-el:select>
							</c:otherwise>
						</c:choose>

					</c:when>
					<c:when test="${question.type.select.renderType eq 'radio'}">
						<table width="100%">
							<c:choose>
								<c:when
									test="${question.type.select.orientation eq 'horizontal'}">
									<tr>
										<c:forEach var="answer"
											items="${question.type.select.possibleAnswers.answerArray}">
											<td><html-el:radio
												property="data.value(${pageNumber}_${sectionId}_${question.id})"
												value="${answer}" />&nbsp;<c:out value="${answer}" /></td>
										</c:forEach>
										<c:if test="${question.type.select.otherAnswer eq true}">
											<td><html-el:radio
												property="data.value(${pageNumber}_${sectionId}_${question.id})"
												value="other" />&nbsp;Other&nbsp;<html-el:text
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other_value)" /></td>
										</c:if>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="answer"
										items="${question.type.select.possibleAnswers.answerArray}">
										<tr>
											<td><html-el:radio
												property="data.value(${pageNumber}_${sectionId}_${question.id})"
												value="${answer}" />&nbsp;<c:out value="${answer}" /></td>
										</tr>
									</c:forEach>
									<c:if test="${question.type.select.otherAnswer eq true}">
										<tr>
											<td><html-el:radio
												property="data.value(${pageNumber}_${sectionId}_${question.id})"
												value="other" />&nbsp;Other&nbsp;<html-el:text
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other_value)" /></td>
										</tr>
									</c:if>
								</c:otherwise>
							</c:choose>
						</table>
					</c:when>
					<c:when test="${question.type.select.renderType eq 'checkbox'}">
						<table width="100%">
							<c:choose>
								<c:when
									test="${question.type.select.orientation eq 'horizontal'}">
									<tr>
										<c:forEach var="answer"
											items="${question.type.select.possibleAnswers.answerArray}">
											<td><html-el:hidden
												property="data.value(checkbox_${pageNumber}_${sectionId}_${question.id}_${answer})" />
											<html-el:checkbox
												property="data.value(${pageNumber}_${sectionId}_${question.id}_${answer})"
												value="${answer}"
												onclick="select_checkbox('${pageNumber}_${sectionId}_${question.id}_${answer}')" />&nbsp;<c:out
												value="${answer}" /></td>
										</c:forEach>

										<c:if test="${question.type.select.otherAnswer eq true}">
											<td><html-el:hidden
												property="data.value(checkbox_${pageNumber}_${sectionId}_${question.id}_other)" />
											<html-el:checkbox
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other)"
												value="other"
												onclick="select_checkbox('${pageNumber}_${sectionId}_${question.id}_other')" />&nbsp;Other&nbsp;<html-el:text
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other_value)" />
											</td>
										</c:if>
								</c:when>
								<c:otherwise>
									<c:forEach var="answer"
										items="${question.type.select.possibleAnswers.answerArray}">
										<tr>
											<td><html-el:hidden
												property="data.value(checkbox_${pageNumber}_${sectionId}_${question.id}_${answer})" />
											<html-el:checkbox
												property="data.value(${pageNumber}_${sectionId}_${question.id}_${answer})"
												value="${answer}"
												onclick="select_checkbox('${pageNumber}_${sectionId}_${question.id}_${answer}')" />&nbsp;<c:out
												value="${answer}" /></td>
										</tr>
									</c:forEach>

									<c:if test="${question.type.select.otherAnswer eq true}">
										<tr>
											<td><html-el:hidden
												property="data.value(checkbox_${pageNumber}_${sectionId}_${question.id}_other)" />
											<html-el:checkbox
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other)"
												value="other"
												onclick="select_checkbox('${pageNumber}_${sectionId}_${question.id}_other')" />&nbsp;Other&nbsp;<html-el:text
												property="data.value(${pageNumber}_${sectionId}_${question.id}_other_value)" />
											</td>
										</tr>
									</c:if>
								</c:otherwise>
							</c:choose>
						</table>
					</c:when>
				</c:choose></td>
			</c:when>

		</c:choose>
	</tr>
</table>
</td>
