
<%@page import="org.oscarehr.common.model.OcanClientForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanFormAction"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");
	
	String startDate = parameters.get("startDate")[0];
	String completionDate = parameters.get("completionDate")[0];
	
	String assessmentStatus = parameters.get("assessment_status")[0];
	

	OcanClientForm ocanClientForm=OcanFormAction.createOcanClientForm(clientId);
	ocanClientForm.setLastName(request.getParameter("lastName"));
	ocanClientForm.setFirstName(request.getParameter("firstName"));	
	ocanClientForm.setDateOfBirth(request.getParameter("dateOfBirth"));	
	ocanClientForm.setAssessmentStatus(assessmentStatus);

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
	try {
		ocanClientForm.setStartDate(formatter.parse(startDate));
	}catch(java.text.ParseException e){}
	try {
		ocanClientForm.setCompletionDate(formatter.parse(completionDate));
	}catch(java.text.ParseException e){}
	
	
	OcanFormAction.saveOcanClientForm(ocanClientForm);
	
	parameters.remove("lastName");
	parameters.remove("firstName");	
	parameters.remove("dateOfBirth");
	parameters.remove("startDate");
	parameters.remove("completionDate");
	
	
	for (Map.Entry<String, String[]> entry : parameters.entrySet())
	{
		if (entry.getValue()!=null)
		{
			for (String value : entry.getValue())
			{
				OcanFormAction.addOcanClientFormData(ocanClientForm.getId(), entry.getKey(), value);				
			}
		}
	}
	
	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>