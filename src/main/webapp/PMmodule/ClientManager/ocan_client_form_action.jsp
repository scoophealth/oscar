<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

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
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

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

	OcanStaffForm ocanClientForm=OcanFormAction.createOcanStaffForm(loggedInInfo,ocanStaffFormId,clientId,signed);
	ocanClientForm.setLastName(request.getParameter("lastName")==null?"":request.getParameter("lastName"));
	ocanClientForm.setFirstName(request.getParameter("firstName")==null?"":request.getParameter("firstName"));	
	
	ocanClientForm.setAddressLine1(request.getParameter("addressLine1")==null?"":request.getParameter("addressLine1"));
	ocanClientForm.setAddressLine2(request.getParameter("addressLine2")==null?"":request.getParameter("addressLine2"));
	ocanClientForm.setCity(request.getParameter("city")==null?"":request.getParameter("city"));
	ocanClientForm.setProvince(request.getParameter("province")==null?"":request.getParameter("province"));
	ocanClientForm.setPostalCode(request.getParameter("postalCode")==null?"":request.getParameter("postalCode"));
	ocanClientForm.setPhoneNumber(request.getParameter("phoneNumber")==null?"":request.getParameter("phoneNumber"));
	ocanClientForm.setEmail(request.getParameter("email")==null?"":request.getParameter("email"));
	ocanClientForm.setHcNumber(request.getParameter("hcNumber")==null?"":request.getParameter("hcNumber"));
	ocanClientForm.setHcVersion(request.getParameter("hcVersion")==null?"":request.getParameter("hcVersion"));
	ocanClientForm.setDateOfBirth(request.getParameter("date_of_birth")==null?"":request.getParameter("date_of_birth"));	
	ocanClientForm.setGender(request.getParameter("gender")==null?"":request.getParameter("gender"));
	
	
	ocanClientForm.setClientDateOfBirth(request.getParameter("client_date_of_birth")==null?"":request.getParameter("client_date_of_birth"));
	ocanClientForm.setOcanType(request.getParameter("ocanType"));

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
	try {
		if(!StringUtils.isBlank(startDate))
			ocanClientForm.setClientStartDate(formatter.parse(startDate));
			//ocanClientForm.setStartDate(formatter.parse(request.getParameter("startDate")));
	}catch(java.text.ParseException e){}
	try {
		if(!StringUtils.isBlank(completionDate))
			ocanClientForm.setClientCompletionDate(formatter.parse(completionDate));
			//ocanClientForm.setCompletionDate(formatter.parse(request.getParameter("completionDate")));
	}catch(java.text.ParseException e){}
	
	ocanClientForm.setClientFormCreated(new Date());
	ocanClientForm.setClientFormProviderNo(loggedInInfo.getLoggedInProviderNo());
	ocanClientForm.setClientFormProviderName(loggedInInfo.getLoggedInProvider().getFormattedName());		

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
