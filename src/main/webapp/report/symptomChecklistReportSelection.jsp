<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.myoscar_server.ws.SurveyResultTransfer"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%@page import="org.oscarehr.web.report.SymptomChecklistReportUIBean"%>

<%
	request.setAttribute("HEAD_ELEMENT","<link rel=\"stylesheet\" type=\"text/css\" href=\""+request.getContextPath()+"/css/caisi_css.jsp\">");
%>

<%@include file="/layouts/html_top.jspf"%>

<%
	int PAGE_SIZE=100;
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
	
	List<SurveyResultTransfer> results=SymptomChecklistReportUIBean.getSymptomChecklistReportsSelectList(auth, demographicId, startIndex, PAGE_SIZE);
%>

<h3>Select results to compare</h3>
<table class="genericTable">
	<tr>
		<td class="genericTableHeader"></td>
		<td class="genericTableHeader">Date</td>
		<td class="genericTableHeader">DIN</td>
	</tr>
	<%	
		for (SurveyResultTransfer surveyResultTransfer : results)
		{
			%>
				<tr>
					<td class="genericTableData"><input type="checkbox" /></td>
					<td class="genericTableData"><%=DateUtils.getISODateTimeFormatNoT(surveyResultTransfer.getDateOfData())%></td>
					<td class="genericTableData">din din</td>
				</tr>
			<%
		}
	%>
</table>

<%@include file="/layouts/html_bottom.jspf"%>