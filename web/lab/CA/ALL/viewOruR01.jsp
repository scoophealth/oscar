<%@include file="/layouts/html_top.jspf"%>

<%
	int segmentId=Integer.parseInt(request.getParameter("segmentId"));
%>

<h2 class="oscarBlueHeader">
	View Electronic Data 
	<span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message : segmentId <%=segmentId%>)</span>
</h2>


<%@include file="/layouts/html_bottom.jspf"%>