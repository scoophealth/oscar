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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="oscar.oscarSurveillance.*,java.util.*,org.commonmark.node.*,org.commonmark.parser.Parser,org.commonmark.renderer.html.HtmlRenderer"%><% 
   Survey survey = (Survey) request.getAttribute("survey"); 
   Integer curr = (Integer) request.getAttribute("currSurveyNum");
   String currSurveyNum = curr.toString();
   Parser parser = Parser.builder().build();
   Node document = parser.parse(survey.getSurveyQuestion());
   HtmlRenderer renderer = HtmlRenderer.builder().build();
   String surveyQuestion = renderer.render(document); 
%>
<html:html locale="true">

<%-- 

<%=survey.getSurveyQuestion()%>

 --%>

<head>
	<html:base />
	<title><bean:message key="admin.admin.surveillanceConfig"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/k2aServices.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/providerServices.js"></script>	
</head>

<body vlink="#0000FF" class="BodyStyle">
	<div class="container">
		<div class="page-header">
			<h4><%=survey.getSurveyTitle()%> <small>Chart Saved</small></h4>
		</div>

		<div class="jumbotron" style="text-align: center;">
  			<%=surveyQuestion%>
  			 <br/>
  			<html:form action="/oscarSurveillance/SurveillanceAnswer">
					<html:hidden property="proceed" value='<%=(String) request.getAttribute(\"proceedURL\")%>' />
					<html:hidden property="demographicNo" value='<%=(String) request.getAttribute(\"demographic_no\")%>' />
					<html:hidden property="surveyId" value="<%=(String) survey.getSurveyId()%>" />
					<html:hidden property="currentSurveyNum" value="<%=currSurveyNum%>" />

					<% for (int i =0 ; i < survey.numAnswers(); i++){%>
					<input type="submit" name="answer" class="btn btn-primary btn-lg"
						value="<%=survey.getAnswerString(i)%>" />
					<%}%>

				</html:form>
		</div>
</body>
</html:html>