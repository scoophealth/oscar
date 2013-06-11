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

<%@page import="oscar.oscarSurveillance.*,java.util.*"%>


<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<div class="page-header">
	<h4>
		<bean:message key="oscarSurveillance.Surveillance.msgSurveillance" />
	</h4>
</div>

<%
	SurveillanceMaster sMaster = SurveillanceMaster.getInstance();
%>

<h4>
	Current Surveys:
	<span class="badge badge-info">
		<%=SurveillanceMaster.numSurveys()%>
	</span>
</h4>

<div class="row">
	<div class='span3'>
		<ul class="nav nav-tabs nav-stacked">
			<%
				ArrayList<Survey> list = sMaster.getCurrentSurveys();
					int spanItemCount = (int)Math.ceil(list.size());
					for (int i = 0; i < list.size(); i++) {
				Survey survey = (Survey) list.get(i);
				
				if(i!=0 && i%spanItemCount==0)
					out.println("</ul></div><div class='span3'><ul class='nav nav-tabs nav-stacked'>");
			%>
			<li><a
				href="${ctx}/oscarSurveillance/ReportSurvey.jsp?surveyId=<%=survey.getSurveyId()%>"><%=survey.getSurveyTitle()%></a>
			</li>
			<%
				}
			%>
		</ul>
	</div>
</div>

<div id="survey-content"></div>

<script>
	$(document).ready(function() {
		$("a.surveyLink").click(function(e) {
			//alert('link click')
			e.preventDefault();
			//alert("You clicked the link");
			$("#survey-content").load($(this).attr("href"), 
				function(response, status, xhr) {
			  		if (status == "error") {
				    	var msg = "Sorry but there was an error: ";
				    	$("#survey-content").html(msg + xhr.status + " " + xhr.statusText);
					}
				}
			);
		});
	});
</script>