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
<%@page import="oscar.oscarSurveillance.*,java.util.*"%>



<%
String surveyId = request.getParameter("surveyId");
SurveillanceMaster sMaster = SurveillanceMaster.getInstance();
Survey survey = sMaster.getSurveyById(surveyId);

ArrayList answerList = survey.getAnswerCount(surveyId);
ArrayList statusList = survey.getStatusCount(surveyId);

SurveyInfo surveyInfo = new SurveyInfo();

ArrayList fileList = surveyInfo.getFileNames(surveyId);
int total = 0;
%>

<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="oscarSurveillance.Surveillance.title" />
</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">



<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarSurveillance.Surveillance.msgSurveillance" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><%=survey.getSurveyTitle()%></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="survey" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top"><a
			href="ReportSurveillance.jsp">Menu</a> <br />
		<br />
		&nbsp; <%if (survey.hasExport()){ %> <a
			href="CreateSurveillanceFile.do?surveyId=<%=survey.getSurveyId()%>">Create
		File</a> <br />
		<br />
		<%for (int i = 0; i < fileList.size(); i++){ 
                   String[] file = (String[]) fileList.get(i);   %> <a
			href="../Download?dir_property=surveillance_directory&filename=<%=file[0]%>"
			target="_blank" title="<%=file[0]%> create on :<%=file[1]%>"> <%=file[0]%>
		</a> </br>
		<%}%> <%}else{%> No Export <%}%>
		</td>
		<td class="MainTableRightColumn">
		<table>
			<tr>
				<td style="text-align: center">
				<table>
					<tr>
						<td>Survey Question:</td>
						<td><%=survey.getSurveyQuestion()%>
					</tr>
					<tr>
						<td>Randomness:</td>
						<td><%=survey.getRandomness()%>
					</tr>
					<tr>
						<td>Period:</td>
						<td><%=survey.getPeriod()%>
					</tr>
				</table>
				<table>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>

					</tr>
					<tr>
						<td colspan="2">
						<table border=1>
							<tr>
								<td>Answer</td>
								<td>&nbsp;</td>
								<td>Count</td>
							</tr>
							<%for (int i = 0; i < answerList.size(); i++){ 
                              String[] s = (String[]) answerList.get(i);   %>
							<tr>
								<td><%=survey.getAnswerStringById(s[0])%></td>
								<td>&nbsp;</td>
								<td><%=s[1]%></td>
							</tr>
							<%}%>

						</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<table border=1>
							<tr>
								<td>Status</td>
								<td>&nbsp;</td>
								<td>Value</td>
							</tr>
							<%for (int i = 0; i < statusList.size(); i++){ 
                               String[] s = (String[]) statusList.get(i);   
                               total += Integer.parseInt(s[1]);
                          %>
							<tr>
								<td><%=s[0]%></td>
								<td>&nbsp;</td>
								<td><%=s[1]%></td>
							</tr>
							<%}%>
							<tr>
								<td>Total</td>
								<td>&nbsp;</td>
								<td><%=total%></td>
							</tr>
						</table>
						</td>
					</tr>

				</table>
				</td>
			</tr>
			<tr>
				<td style="text-align: center">&nbsp;</td>
			</tr>
			<tr>
				<td style="text-align: center">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>


</html:html>
