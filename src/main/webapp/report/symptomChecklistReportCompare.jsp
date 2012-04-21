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

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Map"%>
<%@page import="org.oscarehr.web.report.MumpsSurveyResultsDisplayObject"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%@page import="org.oscarehr.web.report.SymptomChecklistCompareDisplayObject"%>
<%@page import="org.oscarehr.web.report.SymptomChecklistReportUIBean"%>
<%
	request.setAttribute("HEAD_ELEMENT","<link rel=\"stylesheet\" type=\"text/css\" href=\""+request.getContextPath()+"/css/caisi_css.jsp\">");
%>

<%@include file="/layouts/html_top.jspf"%>

<%
	String[] selectedResultIds=request.getParameterValues("resultIds");
	PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);

	SymptomChecklistCompareDisplayObject result=SymptomChecklistReportUIBean.getCompareDisplayObject(auth, selectedResultIds);
	
	if (result.getResultsToCompare().size()<=0)
	{
		%>
			No results selected.
		<%
		return;
	}
%>

<h3>Sympton Checklist Results</h3>
<br />
Patient : <%=result.getPatientNameHtmlEscaped()%>
<br />


<table class="genericTable">
	<tr>
		<td class="genericTableHeader">Date</td>
		<%
			for (MumpsSurveyResultsDisplayObject displayObject : result.getResultsToCompare())
			{
			%>
				<td class="genericTableHeader">
					<%=displayObject.getDateIsoFormatted()%>
				</td>
			<%
			}
		%>
	</tr>
	<tr>
		<td class="genericTableData">DIN</td>
		<%
			for (MumpsSurveyResultsDisplayObject displayObject : result.getResultsToCompare())
			{
			%>
				<td class="genericTableData"><%=StringEscapeUtils.escapeHtml(displayObject.getMumpsResultWrapper().getFirstAnswerByQuestionId("E2"))%></td>
			<%
			}
		%>
	</tr>
	<tr>
		<td class="genericTableData">General Health</td>
		<%
			for (MumpsSurveyResultsDisplayObject displayObject : result.getResultsToCompare())
			{
			%>
				<td class="genericTableData"><%=StringEscapeUtils.escapeHtml(displayObject.getMumpsResultWrapper().getFirstAnswerByQuestionId("E1"))%></td>
			<%
			}
		%>
	</tr>
	<%
		for (int i=3; i<=result.getLastQuestion(); i++ )
		{
			String questionId="E"+i;
			%>
				<tr>
					<td class="genericTableData"><%=StringEscapeUtils.escapeHtml(result.getQuestionsList().get(questionId))%></td>
					<%
						for (MumpsSurveyResultsDisplayObject displayObject : result.getResultsToCompare())
						{
						%>
							<td class="genericTableData"><%=StringEscapeUtils.escapeHtml(displayObject.getMumpsResultWrapper().getFirstAnswerByQuestionId(questionId))%></td>
						<%
						}
					%>
				</tr>
			<%
		}
	%>
</table>

<%@include file="/layouts/html_bottom.jspf"%>
