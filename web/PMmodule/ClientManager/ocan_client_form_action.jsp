
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

	OcanClientForm ocanClientForm=OcanFormAction.createOcanClientForm(clientId);
	ocanClientForm.setLastName(request.getParameter("lastName"));
	ocanClientForm.setFirstName(request.getParameter("firstName"));	
	ocanClientForm.setDateOfBirth(request.getParameter("dateOfBirth"));	
	
	OcanFormAction.saveOcanClientForm(ocanClientForm);
	
	parameters.remove("lastName");
	parameters.remove("firstName");	
	parameters.remove("dateOfBirth");
	
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