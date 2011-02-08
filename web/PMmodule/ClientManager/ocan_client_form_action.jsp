<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.OcanStaffFormData"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanFormAction"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");
	
	boolean signed=WebUtils.isChecked(request, "signed");	
	parameters.remove("signed");
	
	String startDate = parameters.get("clientStartDate")[0];
	String completionDate = parameters.get("clientCompletionDate")[0];
	
	//String assessmentStatus = parameters.get("assessment_status")[0];
	String ocanStaffFormId = parameters.get("ocanStaffFormId")[0];

	OcanStaffForm ocanClientForm=OcanFormAction.createOcanStaffForm(ocanStaffFormId,clientId,signed);
	ocanClientForm.setLastName(request.getParameter("lastName"));
	ocanClientForm.setFirstName(request.getParameter("firstName"));	
	//ocanClientForm.setDateOfBirth(request.getParameter("dateOfBirth"));
	ocanClientForm.setClientDateOfBirth(request.getParameter("client_date_of_birth"));
	ocanClientForm.setOcanType(request.getParameter("ocanType"));

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
	try {
		ocanClientForm.setClientStartDate(formatter.parse(startDate));
		//ocanClientForm.setStartDate(formatter.parse(request.getParameter("startDate")));
	}catch(java.text.ParseException e){}
	try {
		ocanClientForm.setClientCompletionDate(formatter.parse(completionDate));
		//ocanClientForm.setCompletionDate(formatter.parse(request.getParameter("completionDate")));
	}catch(java.text.ParseException e){}
	
	ocanClientForm.setClientFormCreated(new Date());
	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	ocanClientForm.setClientFormProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
	ocanClientForm.setClientFormProviderName(loggedInInfo.loggedInProvider.getFormattedName());		

	OcanFormAction.saveOcanStaffForm(ocanClientForm);
	
	parameters.remove("lastName");
	parameters.remove("firstName");	
	parameters.remove("client_date_of_birth");
	parameters.remove("clientStartDate");
	parameters.remove("clientCompletionDate");
	
	Integer ocanStaffFormId_Int=0;
	if(ocanStaffFormId!=null && !"".equals(ocanStaffFormId) && !"null".equals(ocanStaffFormId)) {
		ocanStaffFormId_Int = Integer.parseInt(ocanStaffFormId);
	}		
	
	List<OcanStaffFormData> oldData = OcanForm.getOcanFormDataByFormId(ocanStaffFormId_Int);
	if(oldData.size()>0) {
		for(OcanStaffFormData d : oldData) {	
			if(!d.getQuestion().contains("client_"))
				OcanFormAction.addOcanStaffFormData(ocanClientForm.getId(),d.getQuestion(),d.getAnswer());
		}
	
	}
		for (Map.Entry<String, String[]> entry : parameters.entrySet())
		{
			if (entry.getValue()!=null)
			{
				for (String value : entry.getValue())
				{
					OcanFormAction.addOcanStaffFormData(ocanClientForm.getId(), entry.getKey(), value);				
				}
			}
		}
	
	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>