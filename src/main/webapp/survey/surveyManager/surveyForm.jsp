
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

	<script language="JavaScript">
 
		function OpenQuestionEditor(page,section,question) {
			window.open('<html:rewrite action="/SurveyManager"/>?method=edit_question&page=' + page + '&section=' + section + '&id=' + question,'question_editor','width=500,height=500');
		}
			
		function navigate(pageName) {
			document.surveyForm.method.value="navigate";
			document.surveyForm.elements['web.page'].value=pageName;
    		document.surveyForm.submit();
		}

		function save() {
			document.surveyForm.method.value="save";
			document.surveyForm.submit();
		}

        function addQuestionType(toSection, qType) {
        	document.surveyForm.method.value="add_question";
        	document.surveyForm.elements['web.section'].value=toSection;
        	document.surveyForm.elements['web.questionTypeData'].value=qType;
        	document.surveyForm.submit(); 
        }
        
        function addSection() {
	        document.surveyForm.method.value="add_section";
        	document.surveyForm.submit(); 
        }
        
        function updateSection() {
			document.surveyForm.method.value="update_section";
			document.surveyForm.submit();
        }
        
        function addPage() {
			document.surveyForm.method.value="add_page";
    		document.surveyForm.submit();
        }
        
        function removePage(pageName) {
			document.surveyForm.method.value="remove_page";
			document.surveyForm.elements['web.page'].value=pageName;
    		document.surveyForm.submit();
        }
        
        function addIntroPage() {
    		document.surveyForm.method.value="add_introduction";
    		document.surveyForm.submit();
        }

        function addClosingPage() {
		    document.surveyForm.method.value="add_closing";
    		document.surveyForm.submit();
        }

        function removeIntro() {
            document.surveyForm.method.value="remove_introduction";
    		document.surveyForm.submit();
        }

        function removeClose() {
            document.surveyForm.method.value="remove_closing";
    		document.surveyForm.submit();
        }

        function removePage(pageId) {
        
            var pages = document.getElementById('surveyPages');
            var pageDel = document.getElementById(pageId);
            pages.removeChild(pageDel);
        }
        
        function removeSection(pageNum, sectionNum) {
            var sections = document.getElementById('page'+ pageNum + '_Container');
            var sectionDel = document.getElementById('page' + pageNum + '_Section' + sectionNum);
            sections.removeChild(sectionDel);
        }
        
        
        function removeQuestion(pageNum, sectionNum, questionNum) {
            var container = document.getElementById('page'+ pageNum + '_Container');
            if(parseInt(sectionNum) > 0) {
                container = document.getElementById('page'+ pageNum + '_Section' + sectionNum + '_Container');
            }
            
            var questionDel = document.getElementById('page' + pageNum + '_Section' + sectionNum + '_Question' + questionNum);
            container.removeChild(questionDel);
        }
        
    
    </script>

	<input type="hidden" name="method" value="save" />
	<html:hidden property="web.page" />
	<html:hidden property="web.section" />
	<html:hidden property="web.questionTypeData" />

	<input type="hidden" name="numPages" id="numPages" value="1" />

	<html:hidden property="survey.id" />
	<br />
	<table width="100%">
		<tr>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="center">
			<table>
				<tr>
					<td class="field">Form Name:</td>
					<td><html:text property="survey.description"
						styleClass="formElement" disabled="true" /></td>
				</tr>
				<tr>
					<td class="field">Version:</td>
					<td><html:text property="survey.version"
						styleClass="formElement" disabled="true" /></td>
				</tr>
				<tr>
					<td colspan="2">
					<table border="0" cellpadding="3" cellspacing="0">
						<tr>
							<td><input type="button" value="Add Intro Text"
								onclick="addIntroPage()" style="" /></td>
							<td><input type="button" value="Add New Page"
								onclick="addPage()" /></td>
							<td><input type="button" value="Add Closing Text"
								onclick="addClosingPage()" style="" /></td>
							<td><input type="button" value="Save Survey"
								onclick="save()" /></td>
							<td><html:cancel /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>

		<tr>
			<td align="center">
			<table>
				<tr>
					<td><!-- navigate pages of the survey --> <c:forEach
						var="page" items="${pages}">
						<input type="button" value="<c:out value="${page.pageName}"/>"
							onclick="navigate('<c:out value="${page.pageNumber}"/>')" />
					</c:forEach></td>
				</tr>
			</table>
			</td>
		</tr>

		<!-- error messages -->
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
			<td>
			<hr />
			</td>
		</tr>
		<tr>
			<td align="left" id="surveyPages"><c:choose>
				<c:when test="${surveyForm.map.web.page eq 'Introduction'}">
					<div id="page_intro">
					<table class="surveyPage" width="100%">
						<tr>
							<td colspan="2" class="pageTitle" width="1%"><a
								href="javascript:removeIntro();">Remove Introduction</a></td>
						</tr>
						<tr>
							<td width="1%" class="pageTitle" nowrap>Include on First
							Page</td>
							<td align="left"><html:radio
								property="model.survey.introduction.includeOnFirstPage"
								value="true" />Yes&nbsp; <html:radio
								property="model.survey.introduction.includeOnFirstPage"
								value="false" />No&nbsp;</td>
						</tr>
						<tr>
							<td width="1%" class="pageTitle" nowrap>Introduction Text:</td>
							<td align="left"><html:textarea cols="80" rows="10"
								property="model.survey.introduction.text"></html:textarea></td>
						</tr>
					</table>
					</div>
				</c:when>

				<c:when test="${surveyForm.map.web.page eq 'Closing'}">
					<div id="page_intro">
					<table class="surveyPage" width="100%">
						<tr>
							<td colspan="2" class="pageTitle" width="1%"><a
								href="javascript:removeClose();">Remove Closing</a></td>
						</tr>
						<tr>
							<td width="1%" class="pageTitle" nowrap>Include on Last Page</td>
							<td align="left"><html:radio
								property="model.survey.closing.includeOnLastPage" value="true" />Yes&nbsp;
							<html:radio property="model.survey.closing.includeOnLastPage"
								value="false" />No&nbsp;</td>
						</tr>
						<tr>
							<td width="1%" class="pageTitle" nowrap>Closing Text:</td>
							<td align="left"><html:textarea cols="80" rows="10"
								property="model.survey.closing.text"></html:textarea></td>
						</tr>
					</table>
					</div>
				</c:when>

				<c:otherwise>
					<!--  real page -->

					<div id="<c:out value="page${page_number}"/>">
					<table class="surveyPage" width="100%">
						<tr>
							<td class="pageTitle" colspan="3"><a
								href="<html:rewrite action="/SurveyManager"/>?method=remove_page&id=<c:out value="${page_number }"/>"><img
								src="images/delete.png" border="0"></a> &nbsp; Page
							Title:&nbsp; <html:text property="pageModel.description"
								size="60" styleClass="formElement" /></td>
						</tr>
						<tr>
							<td width="1%"><html:select property="web.questionType"
								onchange="addQuestionType('0', this.options[this.selectedIndex].value);"
								styleClass="formElement">
								<html:option value="">Add New Question:</html:option>
								<html:options collection="QuestionTypes" property="value"
									labelProperty="label" />
							</html:select></td>

							<td align="right"><input type="button"
								value="Add New Section" onclick="addSection()" /></td>

							<td align="left"><input type="button" value="Update Section"
								onclick="updateSection()" /></td>
						</tr>

						<c:set var="page" scope="page" value="${surveyForm.map.pageModel}"></c:set>
						<%
                        	//loop through the questions/sections
                        	org.oscarehr.surveymodel.Page p = (org.oscarehr.surveymodel.Page)pageContext.getAttribute("page");
                   		    org.oscarehr.surveymodel.Page.QContainer[] containers =  p.getQContainerArray();	
    				
	                        for(int x=0;x<containers.length;x++) {
	                        	if(containers[x].isSetQuestion()) {
	                        		org.oscarehr.surveymodel.Question question = containers[x].getQuestion();
	                        		pageContext.setAttribute("question",containers[x].getQuestion());
    
                        %>

						<tr>
							<td id="page1_Container" colspan="3" class="container">
							<table class="section" width="90%">
								<tr>
									<td width="10%"><a
										href="<html:rewrite action="/SurveyManager"/>?method=remove_question&id=<c:out value="${question.id }"/>&section=0"><img
										src="images/delete.png" border="0"></a> &nbsp; <!--
										  	<a href="javascript:void(0);return false;" onclick="OpenQuestionEditor('<c:out value="${surveyForm.map.web.page}"/>','0','<c:out value="${question.id}"/>')"><img src="images/edit.png" border="0"></a>
									  		&nbsp;
									  		  --> <a
										href="<html:rewrite action="/SurveyManager"/>?method=edit_question&page=<c:out value="${surveyForm.map.web.page}"/>&section=0&id=<c:out value="${question.id}"/>"><img
										src="images/edit.png" border="0"></a></td>
									<td align="left"><c:out value="${question.description}" />
									</td>
									<td align="right">
									<%
	                            				if(question.getType().isSetRank()) { out.print("Rank"); }
		                            			if(question.getType().isSetScale()) { out.print("Scale"); }
		                            			if(question.getType().isSetOpenEnded()) { out.print("Open Ended"); }
		                            			if(question.getType().isSetSelect()) { out.print("Select"); }
		                            			if(question.getType().isSetDate()) { out.print("Date"); }
	                            			%>
									</td>
								</tr>
							</table>
							</td>
						</tr>

						<% } else if(containers[x].isSetSection()) {
                     		pageContext.setAttribute("section",containers[x].getSection());
                        		
                         %>

						<tr>
							<td id="page1_Container" colspan="3" class="container">
							<table class="section" width="90%">
								<tr>
									<td class="sectionDescr" width="1%"><a
										href="<html:rewrite action="/SurveyManager"/>?method=remove_section&id=<c:out value="${section.id}"/>"><img
										src="images/delete.png" border="0"></a> &nbsp; <input
										type="text"
										name="section_description_<%=containers[x].getSection().getId() %>"
										value="<%=containers[x].getSection().getDescription() %>"
										size="50" /> &nbsp;&nbsp;&nbsp;&nbsp; <%
                     						String bold_checked="", underline_checked="", italics_checked="", selected_color="";
                     						if(containers[x].getSection().getBold() != null && containers[x].getSection().getBold().equals("true")) {
                     							bold_checked="checked";
                     						}
                     						if(containers[x].getSection().getUnderline() != null && containers[x].getSection().getUnderline().equals("true")) {
                     							underline_checked="checked";
                     						}
                     						if(containers[x].getSection().getItalics() != null && containers[x].getSection().getItalics().equals("true")) {
                     							italics_checked="checked";
                     						}
                     						if(containers[x].getSection().getColor() != null) {
                     							selected_color=containers[x].getSection().getColor();
                     							pageContext.setAttribute("selected_color",selected_color);
                     						}
                     					%> <input type="checkbox" value="true"
										name="section_bold_<%=containers[x].getSection().getId() %>"
										<%=bold_checked%> />Bold&nbsp;&nbsp; <input type="checkbox"
										value="true"
										name="section_underline_<%=containers[x].getSection().getId() %>"
										<%=underline_checked%> />Underline&nbsp;&nbsp; <input
										type="checkbox" value="true"
										name="section_italics_<%=containers[x].getSection().getId() %>"
										<%=italics_checked%> />Italics&nbsp;&nbsp; <select
										name="section_color_<%=containers[x].getSection().getId() %>">
										<option value="">&nbsp;</option>
										<c:forEach var="color" items="${colors}">
											<c:choose>
												<c:when test="${selected_color eq color}">
													<option value="<c:out value="${color}"/>" selected><c:out
														value="${color}" /></option>
												</c:when>
												<c:otherwise>
													<option value="<c:out value="${color}"/>"><c:out
														value="${color}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select></td>

								</tr>
								<tr>
									<td colspan="3"><html-el:select
										property="web.questionType"
										onchange="addQuestionType('${section.id}', this.options[this.selectedIndex].value);"
										styleClass="formElement">
										<html:option value="">Add New Question:</html:option>
										<html:options collection="QuestionTypes" property="value"
											labelProperty="label" />
									</html-el:select></td>
								</tr>
								<!--  loop through questions at this level -->
								<c:forEach var="question" items="${section.questionArray}">
									<% org.oscarehr.surveymodel.Question question = (org.oscarehr.surveymodel.Question)pageContext.getAttribute("question"); %>
									<tr>
										<td id="page1_Container" colspan="3" class="container">
										<table class="section" width="90%">
											<tr>
												<td width="10%"><a
													href="<html:rewrite action="/SurveyManager"/>?method=remove_question&id=<c:out value="${question.id }"/>&web.section=<c:out value="${section.id}"/>"><img
													src="images/delete.png" border="0"></a> &nbsp; <!--
										  	<a href="javascript:void(0);return false;" onclick="OpenQuestionEditor('<c:out value="${surveyForm.map.web.page}"/>','<c:out value="${section.id}"/>','<c:out value="${question.id}"/>')"><img src="images/edit.png" border="0"></a>
									  		&nbsp;
									  		 --> <a
													href="<html:rewrite action="/SurveyManager"/>?method=edit_question&page=<c:out value="${surveyForm.map.web.page}"/>&section=<c:out value="${section.id}"/>&id=<c:out value="${question.id}"/>"><img
													src="images/edit.png" border="0"></a></td>
												<td align="left"><c:out value="${question.description}" />
												</td>
												<td align="right">
												<%
	                            				if(question.getType().isSetRank()) { out.print("Rank"); }
		                            			if(question.getType().isSetScale()) { out.print("Scale"); }
		                            			if(question.getType().isSetOpenEnded()) { out.print("Open Ended"); }
		                            			if(question.getType().isSetSelect()) { out.print("Select"); }
		                            			if(question.getType().isSetDate()) { out.print("Date"); }
	                            			%>
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</c:forEach>
							</table>
							</td>
						</tr>
						<% } } %>
					</table>
					</div>
				</c:otherwise>

			</c:choose></td>
		</tr>
	</table>
</html:form>
