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

<%@page import="org.oscarehr.web.report.MumpsSurveyResultsDisplayObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%@page import="org.oscarehr.web.report.SymptomChecklistReportUIBean"%>

<%
	request.setAttribute("HEAD_ELEMENT","<link rel=\"stylesheet\" type=\"text/css\" href=\""+request.getContextPath()+"/css/caisi_css.jsp\">");
%>

<%@include file="/layouts/html_top.jspf"%>

<%
	// we really need to page this screen but I'm being lazy right now and hope we'll get to it before some one does 200 surveys that need to be compared.

	int PAGE_SIZE=200;
	int startIndex=0;
	String startIndexString=request.getParameter("startIndex");
	if (startIndexString!=null) startIndex=Integer.parseInt(startIndexString);

	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	
	PHRAuthentication auth  = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
	if (auth==null)
	{
		%>
			Not logged into myoscar yet.
		<%
		return;
	}
	
	ArrayList<MumpsSurveyResultsDisplayObject> results=SymptomChecklistReportUIBean.getSymptomChecklistReportsResultList(session, auth, demographicId, startIndex, PAGE_SIZE);
%>

<h3>Select results to compare</h3>
<br />
<form action="symptomChecklistReportCompare.jsp">
	<input type="submit" value="Compare Selected" />
	<br />
	<table class="genericTable">
		<tr>
			<td class="genericTableHeader">Compare</td>
			<td class="genericTableHeader">Date</td>
			<td class="genericTableHeader">DIN</td>
		</tr>
		<%	
			for (MumpsSurveyResultsDisplayObject result : results)
			{
				%>
					<tr>
						<td class="genericTableData"><input type="checkbox" name="resultIds" value="<%=result.getSurveyResultTransfer().getId()%>" /></td>
						<td class="genericTableData"><%=result.getDateIsoFormatted()%></td>
						<%-- E2 is the DIN question --%>
						<td class="genericTableData"><%=result.getMumpsResultWrapper().getFirstAnswerByQuestionId("E2")%></td>
					</tr>
				<%
			}
		%>
	</table>
</form>

<%@include file="/layouts/html_bottom.jspf"%>
