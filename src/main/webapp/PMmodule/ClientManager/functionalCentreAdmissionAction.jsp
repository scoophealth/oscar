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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission" %>
<%@page import="org.oscarehr.common.dao.FunctionalCentreAdmissionDao" %>
<%@page import="org.oscarehr.common.model.CdsClientForm" %>
<%@page import="org.oscarehr.common.dao.CdsClientFormDao" %>
<%@page import="org.oscarehr.common.model.OcanStaffForm" %>
<%@page import="org.oscarehr.common.dao.OcanStaffFormDao" %>

<%@page import="org.oscarehr.util.SpringUtils"%>

<%
	@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());
	
	Integer clientId=Integer.valueOf(parameters.get("clientId")[0]);	
	parameters.remove("clientId");

	FunctionalCentreAdmissionDao functionalCentreAdmissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
	
	String id = parameters.get("fcAdmissionId")!=null?parameters.get("fcAdmissionId")[0] : "";
	
	String referralDate = parameters.get("referralDate")!=null?parameters.get("referralDate")[0] : "";
	String admissionDate = parameters.get("admissionDate")!=null?parameters.get("admissionDate")[0] : "";
	String serviceInitiationDate = parameters.get("serviceInitiationDate")!=null?parameters.get("serviceInitiationDate")[0] : "";
	String dischargeDate = parameters.get("dischargeDate")!=null?parameters.get("dischargeDate")[0] : "";
	
	FunctionalCentreAdmission fca = functionalCentreAdmissionDao.find(Integer.valueOf(id));
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		
	if(referralDate!=null && referralDate.length()>=10) {
		fca.setReferralDate(formatter.parse(referralDate.substring(0,10)));		
	} else {
		fca.setReferralDate(null);
	}
	
	if(admissionDate!=null && admissionDate.length()>=10) {
		fca.setAdmissionDate(formatter.parse(admissionDate.substring(0,10)));		
	} else {
		fca.setAdmissionDate(null);
	}
	
	if(serviceInitiationDate!=null && serviceInitiationDate.length()>=10) {
		fca.setServiceInitiationDate(formatter.parse(serviceInitiationDate.substring(0,10)));		
	} else {
		fca.setServiceInitiationDate(null);
	}
	
	if(dischargeDate!=null && dischargeDate.length()>=10) {
		fca.setDischargeDate(formatter.parse(dischargeDate.substring(0,10)));		
	} else {
		fca.setDischargeDate(null);
	}
	
	functionalCentreAdmissionDao.merge(fca);
	
	// The dates should also be updated in cds form.
	CdsClientFormDao cdsClientFormDao = (CdsClientFormDao) SpringUtils.getBean("cdsClientFormDao");
	CdsClientForm cdsForm = cdsClientFormDao.findCdsFormsByAdmissionId(clientId, fca.getId());
	if(cdsForm != null)	{
		cdsForm.setAssessmentDate(fca.getAdmissionDate()); //admission date
		cdsForm.setInitialContactDate(fca.getReferralDate()); //referral date
		cdsForm.setServiceInitiationDate(fca.getServiceInitiationDate());
		cdsClientFormDao.merge(cdsForm);
	}
	
	// The dates should aslo be updated in cbi form.
	OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	OcanStaffForm cbiForm = ocanStaffFormDao.findCbiFormByAdmissionId(clientId, fca.getId(), "CBI");
	if(cbiForm != null) {
		cbiForm.setAdmissionDate(fca.getAdmissionDate());
		cbiForm.setReferralDate(fca.getReferralDate());
		cbiForm.setServiceInitDate(fca.getServiceInitiationDate());
		cbiForm.setDischargeDate(fca.getDischargeDate());
		
		ocanStaffFormDao.merge(cbiForm);	
	}
//	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+clientId);
%>

<html>
<head>
<script type="text/javascript">
function RefreshParent() {
            if (window.opener != null && !window.opener.closed) {
                window.opener.location.reload();
            }
}
</script>
</head>
<body onLoad="window.close();"></body>
</html>  