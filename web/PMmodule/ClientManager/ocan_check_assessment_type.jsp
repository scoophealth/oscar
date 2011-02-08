
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%
	
	String reasonForAssessment = request.getParameter("reasonForAssessment1");	
	Integer clientId = Integer.valueOf(request.getParameter("demographicId1"));
	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
	//When can we make Initial OCAN
	if("IA".equals(reasonForAssessment)) {
		if(OcanForm.canCreateInitialAssessment(clientId)) { 
			out.print("ia_true");
		} else {
			out.print("ia_false");
		}
	} else if("RA".equals(reasonForAssessment)) { 
		if(OcanForm.canCreateInitialAssessment(clientId)) { 
			out.print("ra_false");
		} else {
			out.print("ra_true");
		}
	} else if("DIS".equals(reasonForAssessment) || "OTHR".equals(reasonForAssessment) || 
			"SC".equals(reasonForAssessment) || "REV".equals(reasonForAssessment) || 
			"REK".equals(reasonForAssessment)) {
		//Firstly must have an intial ocan
		if(OcanForm.haveInitialAssessment(clientId)) { 
			out.print("ia_exists_true");
		} else {
			out.print("ia_exists_false");
		}
	}
	
%>