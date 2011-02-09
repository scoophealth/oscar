
<%@page import="org.oscarehr.web.admin.KeyManagerUIBean"%><%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String serviceName=request.getParameter("serviceName");
	Integer professionalSpecialistId=null;
	try
	{
		String temp=StringUtils.trimToNull(request.getParameter("professionalSpecialistId"));
		if (temp!=null)	professionalSpecialistId=Integer.parseInt(temp);
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("Unexpected error.", e);
	}
	
	KeyManagerUIBean.updateMatchingProfessionalSpecialist(serviceName, professionalSpecialistId);
%>