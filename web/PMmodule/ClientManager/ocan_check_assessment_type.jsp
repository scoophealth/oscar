
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanFormAction"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>

<%@page import="java.util.Map"%>
<%
	
	String reasonForAssessment = request.getParameter("reasonForAssessment1");	
	Integer clientId = Integer.valueOf(request.getParameter("demographicId1"));
	
	//When can we make Initial OCAN
	if("IA".equals(reasonForAssessment)) {
		if(OcanFormAction.canCreateInitialAssessment(clientId)) { 
			out.print("true");
		} else {
			out.print("false");
		}
	}

%>