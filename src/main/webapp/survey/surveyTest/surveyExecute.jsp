
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
<title>Test Manager</title>
<link rel="stylesheet" href="style/execute.css" type="text/css">
<script>
			function clickTab(name) {
				if(!validateForm(document.surveyTestForm,document.surveyTestForm.elements['view.tab'].value,false)) {
					return;
				}
				document.surveyTestForm.elements['view.tab'].value=name;
				document.surveyTestForm.method.value='refresh';
				document.surveyTestForm.submit();
			}
		</script>
<script type="text/javascript" src="jsCalendar/calendar.js"></script>
<script type="text/javascript" src="jsCalendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="jsCalendar/calendar-setup.js"></script>
<c:if test="${not empty sessionScope.validation_file}">
	<script type="text/javascript"
		src="survey/scripts/<c:out value="${sessionScope.validation_file}"/>.js"></script>
</c:if>
<link rel="stylesheet" type="text/css"
	href="jsCalendar/skins/aqua/theme.css">

<c:if test="${empty sessionScope.validation_file}">
	<script>
			function validateForm(form,tab,submit) {
				return true;
			}
		</script>
</c:if>
</head>

<body>

<%@ include file="/survey/messages.jsp"%>
<html:form action="/SurveyTest"
	onsubmit="return validateForm(this,document.surveyTestForm.elements['view.tab'].value,true);">
	<html:hidden property="view.tab" />
	<html:hidden property="view.id" />
	<input type="hidden" name="method" value="save" />
	<h3>Survey</h3>
	<br />

	<div class="tabs" id="tabs">
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>

			<c:forEach var="tab" items="${tabs}">
				<c:choose>
					<c:when test="${tab eq currentTab}">
						<td style="background-color: #555;"><c:out value="${tab}" /></td>
					</c:when>
					<c:otherwise>
						<td><a style="" href="javascript:void(0)"
							onclick="javascript:clickTab('<c:out value="${tab}"/>');return false;"><c:out
							value="${tab}" /></a></td>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</table>
	</div>

	<c:if test="${requestScope.introduction != null}">
		<p>
		<pre>
				<c:out value="${introduction.text }" />
			</pre>
		</p>
	</c:if>

	<br />
	<table border="0" width="100%" cellspacing="2" cellpadding="2">
		<c:forEach var="qcontainer" items="${page.QContainerArray}">
			<tr>
				<c:choose>
					<c:when test="${qcontainer.question != null}">
						<c:set var="sectionId" value="0" scope="request" />
						<c:set var="question" value="${qcontainer.question}"
							scope="request" />
						<jsp:include page="question.jsp" />
					</c:when>
					<c:otherwise>
						<c:set var="section" value="${qcontainer.section}" scope="request" />
						<jsp:include page="section.jsp" />
					</c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	</table>
	<br />
	<c:if test="${requestScope.closing != null}">
		<p>
		<pre>
				<c:out value="${closing.text }" />
			</pre>
		</p>
	</c:if>
	<br />
	<table width="50%">
		<tr>
			<td colspan="2"><html:submit value="Save" /> <html:cancel
				value="Cancel" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
