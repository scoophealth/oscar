<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Clinic" %>
<%@ page import="org.oscarehr.common.dao.ClinicDAO" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Allergy" %>
<%@ page import="org.oscarehr.common.dao.AllergyDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    String formClass = "CostQuestionnaire";
    String formLink = "formcostquestionnaire.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    
    ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
    DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
    AllergyDao allergyDao = SpringUtils.getBean(AllergyDao.class);
    ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    
    Clinic clinic = clinicDao.getClinic();
    Demographic demographic = demographicDao.getDemographicById(demoNo);
    StringBuilder allergyString = new StringBuilder();
    List<Allergy> allergies = allergyDao.findActiveAllergies(demoNo);
	for(int x=0;x<allergies.size();x++) {
		Allergy allergy = allergies.get(x);
		if(x>0)
			allergyString.append(",");
    	allergyString.append(allergy.getDescription());
    }

    String providerName = providerDao.getProvider(demographic.getProviderNo()).getFormattedName();
    
    Appointment appt = null;
    if(request.getParameter("appointmentNo") != null)
    	appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointmentNo")));
    
    SimpleDateFormat dateFormatter =new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormatter =new SimpleDateFormat("HH:mm");
    
    //get a few things we need.
    //family doc
    //referring doc
    //allergies
    //clinic info
    
    
   // FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
   //java.util.Properties props = rec.getFormRecord(demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

    //get project_home
    String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Patient Encounter Worksheet</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" src="formScripts.js">          
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">



	<h4 style="font-weight:bold;font-size:15px;text-align:center">Patient Encounter Worksheet</h4>

	<div align="center">
	<form action="../form/createpdf" method="POST">
	<input type="hidden" name="demographic_no" value="<%=request.getParameter("demographic_no") %>" />
	<input type="hidden" name="form_id" value="<%=request.getParameter("form_id") %>" />
	<input type="hidden" name="__title" value="PatientEcounterWorksheet" />
	<input type="hidden" name="__cfgfile" value="patientEncounterWorksheetCfg" />
	<input type="hidden" name="__template" value="patientEncounterWorksheet" />

	<table border="1" cellspacing="1" cellpadding="1" width="90%" >
		
		<tr>
			<td valign="top" width="50%">
				<table border="0" cellspacing="2" cellpadding="2">
				<input type="hidden" name="clinic_name" value="<%=clinic.getClinicName() %>"/>
				<input type="hidden" name="clinic_address1" value="<%=clinic.getClinicAddress() %>"/>
				<input type="hidden" name="clinic_address2" value="<%=clinic.getClinicCity() + ", " + clinic.getClinicProvince() + ", " + clinic.getClinicPostal() %>"/>
				<input type="hidden" name="clinic_phone" value="<%=clinic.getClinicPhone() %>"/>
				<input type="hidden" name="clinic_fax" value="<%=clinic.getClinicFax() %>"/>
					<tr>
						<td valign="top"><b>Office:</b></td>
						<td valign="top">
						<%=clinic.getClinicName() %>
						<br/>
						<%=clinic.getClinicAddress() %>
						<br/>
						<%=clinic.getClinicCity() %>, <%=clinic.getClinicProvince() %>, <%=clinic.getClinicPostal() %>
						</td>
					</tr>
					<tr>
						<td>Phone:</td>
						<td><%=clinic.getClinicPhone() %></td>
					</tr>
					<tr>
						<td>Fax:</td>
						<td><%=clinic.getClinicFax() %></td>
					</tr>
				</table>
			</td>
			<td valign="top" width="50%">
				<table border="0" cellspacing="2" cellpadding="2">
					<input type="hidden" name="demo_name" value="<%=demographic.getFormattedName() + " (" + demographic.getSex().toUpperCase()  + ")" %>"/>
					<input type="hidden" name="demo_address1" value="<%=demographic.getAddress() %>"/>
					<input type="hidden" name="demo_address2" value="<%=demographic.getCity() + ", " + demographic.getProvince() + ", " + demographic.getPostal() %>"/>
					<input type="hidden" name="demo_id" value="<%=demographic.getDemographicNo() %>"/>
					<input type="hidden" name="demo_bday" value="<%=demographic.getBirthDayAsString() + " (" + demographic.getAgeInYears() + ")" %>"/>
					<input type="hidden" name="demo_hin" value="<%=demographic.getHin() + " (" + demographic.getHcType() + ")" %>"/>
					<tr>
						<td valign="top"><b>Patient:</b></td>
						<td>
						<b><%=demographic.getFormattedName() %></b> (<%=demographic.getSex().toUpperCase() %>)<br/>
						<%=demographic.getAddress() %><br/>
						<%=demographic.getCity() %>, <%=demographic.getProvince() %>, <%=demographic.getPostal() %>
						</td>
					</tr>
					<tr>
						<td>Pat ID:</td>
						<td><%=demographic.getDemographicNo() %></td>
					</tr>
					<tr>
						<td>DOB:</td>
						<td><%=demographic.getBirthDayAsString() %>(<%=demographic.getAgeInYears() %>)</td>
					</tr>
					<tr>
						<td>HC #:</td>
						<td><%=demographic.getHin() %> (<%=demographic.getHcType() %>)</td>
					</tr>
				</table>
			</td>
		</tr>
		
		
		<tr>
			<td valign="top" width="50%">
				<table border="0" cellspacing="2" cellpadding="2">
					<input type="hidden" name="mrp_provider" value="<%=providerName %>"/>
					<input type="hidden" name="fam_provider" value="test,test"/>
					<input type="hidden" name="ref_provider" value="test,test"/>
					
					<tr>
						<td>Provider:</td>
						<td><%=providerName %></td>
					</tr>
					<tr>
						<td>Family Doctor:</td>
						<td>Smith, John</td>
					</tr>
					<tr>
						<td>Referring Doctor:</td>
						<td>Smith, John</td>
					</tr>
				</table>
			</td>
			<td valign="top" width="50%">
				<table border="0" cellspacing="2" cellpadding="2">
					<input type="hidden" name="appt_date" value="<%=dateFormatter.format(appt.getAppointmentDate()) + " " + timeFormatter.format(appt.getStartTime()) %>"/>
					<input type="hidden" name="appt_type" value="<%=appt.getType() %>"/>
					<input type="hidden" name="appt_reason" value="<%=appt.getReason() %>"/>
					
					<tr>
						<td>Appt. Date:</td>
						<td><%=dateFormatter.format(appt.getAppointmentDate()) %>&nbsp;<%=timeFormatter.format(appt.getStartTime()) %></td>
					</tr>
					<tr>
						<td>Appt. Type:</td>
						<td><%=appt.getType() %></td>
					</tr>
					<tr>
						<td>Reason:</td>
						<td><%=appt.getReason() %></td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<input type="hidden" name="allergies" value="<%=allergyString.toString() %>"/>
		 <td colspan="2">
			 Allergies:<br/>
			<%=allergyString.toString() %>
		 </td>
		</tr>
		
		<tr>
		
		 <td colspan="2">
			 Encounter Notes:<br/>
			 <textarea cols="138" rows="50" name="encounter_notes"></textarea>
		 </td>
		</tr>
		
		<tr>
		 <td colspan="2">
			<table border="0" cellspacing="2" cellpadding="2">
				
					<tr>
						<td>Diagnosis:</td>
						<td><input name="diagnosis" type="text" value=""/><td>
					</tr>
					<tr>
						<td>Signature:</td>
						<td><input name="signature" type="text" value=""/></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Dr. <%=loggedInInfo.getLoggedInProvider().getFormattedName() %></td>
					</tr>
				</table>
		 </td>
		</tr>
		
		
		<tr>
			<td valign="top" colspan="2">
			<table class="Head" class="hidePrint" height="5%" border="0">
				<tr>
					<td align="left">
					<input type="button" value="Exit" onclick="javascript:return onExit();" /> 
					<input type="submit" value="Print" /></td>
					</td>
				</tr>
				
			</table>
			</td>
		</tr>
	</table>
	</form>	
	</div>

</body>
</html:html>

