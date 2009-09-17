
<%@page import="org.oscarehr.common.model.CdsClientForm"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4Action"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());

	// for these values get them and pop them from map so subsequent iterating through map doesn't process these parameters again.
	Integer admissionId=Integer.valueOf(parameters.get("admissionId")[0]);	
	parameters.remove("admissionId");

	Integer clientAge=Integer.valueOf(parameters.get("age")[0]);	
	parameters.remove("age");

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");

	boolean signed=WebUtils.isChecked(request, "signed");	
	parameters.remove("signed");

	CdsClientForm cdsClientForm=CdsForm4Action.createCdsClientForm(admissionId, clientAge, clientId, signed);
	
	for (Map.Entry<String, String[]> entry : parameters.entrySet())
	{
		if (entry.getValue()!=null)
		{
			for (String value : entry.getValue())
			{
				CdsForm4Action.addCdsClientFormData(cdsClientForm.getId(), entry.getKey(), value);				
			}
		}
	}
	
	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>