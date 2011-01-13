
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanFormAction"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());

	
	// for these values get them and pop them from map so subsequent iterating through map doesn't process these parameters again.
	//Integer admissionId=Integer.valueOf(parameters.get("admissionId")[0]);	
	//parameters.remove("admissionId");
	Integer admissionId=0;   //useless default value.
	
	
	//Integer clientAge=Integer.valueOf(parameters.get("age")[0]);	
	parameters.remove("age");

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");

	boolean signed=WebUtils.isChecked(request, "signed");	
	parameters.remove("signed");

	String assessmentStatus = request.getParameter("assessment_status");
	String startDate = parameters.get("startDate")[0];
	String completionDate = parameters.get("completionDate")[0];
	String reasonForAssessment = parameters.get("reasonForAssessment")[0];
	String gender = parameters.get("gender")[0];	
	String ocanStaffFormId = parameters.get("ocanStaffFormId")[0];
	
	OcanStaffForm ocanStaffForm=OcanFormAction.createOcanStaffForm(ocanStaffFormId, clientId, signed);
	
	
	ocanStaffForm.setLastName(request.getParameter("lastName"));
	ocanStaffForm.setFirstName(request.getParameter("firstName"));
	ocanStaffForm.setAddressLine1(request.getParameter("addressLine1"));
	ocanStaffForm.setAddressLine2(request.getParameter("addressLine2"));
	ocanStaffForm.setCity(request.getParameter("city"));
	ocanStaffForm.setProvince(request.getParameter("province"));
	ocanStaffForm.setPostalCode(request.getParameter("postalCode"));
	ocanStaffForm.setPhoneNumber(request.getParameter("phoneNumber"));
	ocanStaffForm.setEmail(request.getParameter("email"));
	ocanStaffForm.setHcNumber(request.getParameter("hcNumber"));
	ocanStaffForm.setHcVersion(request.getParameter("hcVersion"));
	ocanStaffForm.setDateOfBirth(request.getParameter("date_of_birth"));
	ocanStaffForm.setClientDateOfBirth(request.getParameter("client_date_of_birth"));
	ocanStaffForm.setGender(gender);
	//ocanStaffForm.setAdmissionId(admissionId);
	ocanStaffForm.setOcanType(request.getParameter("ocanType"));
	
	//Once ocan assessment was completed, it can not be changed to other status.
	if(!"Completed".equals(ocanStaffForm.getAssessmentStatus())) {	
		ocanStaffForm.setAssessmentStatus(assessmentStatus);
	}
	
	ocanStaffForm.setReasonForAssessment(reasonForAssessment);
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
	try {
		ocanStaffForm.setStartDate(formatter.parse(startDate));
		ocanStaffForm.setClientStartDate(formatter.parse(request.getParameter("clientStartDate")));
	}catch(java.text.ParseException e){}
	try {
		ocanStaffForm.setCompletionDate(formatter.parse(completionDate));
		ocanStaffForm.setClientCompletionDate(formatter.parse(request.getParameter("clientCompletionDate")));
	}catch(java.text.ParseException e){}
	
	OcanFormAction.saveOcanStaffForm(ocanStaffForm);
	
	parameters.remove("lastName");
	parameters.remove("firstName");
	parameters.remove("addressLine1");
	parameters.remove("addressLine2");
	parameters.remove("city");
	parameters.remove("province");
	parameters.remove("postalCode");
	parameters.remove("phoneNumber");
	parameters.remove("email");
	parameters.remove("hcNumber");
	parameters.remove("hcVersion");
	parameters.remove("date_of_birth");
	parameters.remove("startDate");
	parameters.remove("completionDate");
	parameters.remove("reasonForAssessment");
	parameters.remove("gender");
	
	
	for (Map.Entry<String, String[]> entry : parameters.entrySet())
	{
		System.out.println(entry.getKey());
		if (entry.getValue()!=null)
		{
			for (String value : entry.getValue())
			{
				OcanFormAction.addOcanStaffFormData(ocanStaffForm.getId(), entry.getKey(), value);				
			}
		}
	}
	
	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>