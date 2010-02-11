
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
	Integer admissionId=Integer.valueOf(parameters.get("admissionId")[0]);	
	parameters.remove("admissionId");

	//Integer clientAge=Integer.valueOf(parameters.get("age")[0]);	
	parameters.remove("age");

	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");

	boolean signed=WebUtils.isChecked(request, "signed");	
	parameters.remove("signed");

	String assessmentStatus = parameters.get("assessment_status")[0];
	
	OcanStaffForm ocanStaffForm=OcanFormAction.createOcanStaffForm(null, clientId, signed);
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
	ocanStaffForm.setDateOfBirth(request.getParameter("dateOfBirth"));	
	ocanStaffForm.setAdmissionId(admissionId);
	ocanStaffForm.setAssessmentStatus(assessmentStatus);
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
	parameters.remove("dateOfBirth");
	
	for (Map.Entry<String, String[]> entry : parameters.entrySet())
	{
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