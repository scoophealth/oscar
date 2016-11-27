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
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission" %>
<%@page import="org.oscarehr.common.dao.FunctionalCentreAdmissionDao" %>
<%@page import="org.oscarehr.common.model.FunctionalCentre" %>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao" %>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.oscarehr.common.model.CdsClientForm"%>


<% 
	FunctionalCentreAdmissionDao admissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
   	ProgramDao ProgramDao = (ProgramDao) SpringUtils.getBean("programDao");
   	String currentDemographicId= (String)request.getParameter("demographicId");
   	String currentAdmissionId=(String)request.getParameter("admissionId");
   	String programId = (String)request.getParameter("programId");
   	String cdsFormId = (String) request.getParameter("cdsFormId");
   	String view =(String) request.getParameter("view");
   
   	CdsClientForm cdsForm = new CdsClientForm();
   	FunctionalCentreAdmission ad = new FunctionalCentreAdmission();
   
   
	if(cdsFormId!=null && !cdsFormId.equals("") && !"null".equals(cdsFormId)) {
		   cdsForm = CdsForm4.getCdsClientFormByCdsFormId(Integer.valueOf(cdsFormId));	
	} else {
		if(currentAdmissionId!=null && !currentAdmissionId.equals("") && Integer.valueOf(currentAdmissionId).intValue()>0) {	   
		   	ad = admissionDao.find(Integer.valueOf(currentAdmissionId));
		   	cdsForm.setAssessmentDate(ad.getAdmissionDate()); //admission date
		   	cdsForm.setInitialContactDate(ad.getReferralDate()); //referral date
		   	cdsForm.setServiceInitiationDate(ad.getServiceInitiationDate());
	   	}   
	}
   
%>
    		
<div id="cds_dates">
  <table width="100%">  
	<tr>
		<td class="genericTableHeader">7a. Waiting list / assessment</td>
		<td class="genericTableData">
			<table>
				<tr>						
					<td class="genericTableHeader" >Date of initial contact (FC Referral Date)</td>
					<td class="genericTableData" >			
						<input id="initialContactDate" name="initialContactDate" onfocus="this.blur()" class="systemData {validate: {required:true}}" type="text" readonly="readonly" value="<%=cdsForm.getInitialContactDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(cdsForm.getInitialContactDate())%>">			
					</td>			
				</tr>
			<tr>
				<td class="genericTableHeader">Date of assessment interview (FC Admission Date)</td>
				<td class="genericTableData" >
					<input id="assessmentDate" name="assessmentDate" onfocus="this.blur()" class="systemData {validate: {required:true}}" type="text" readonly="readonly" value="<%=cdsForm.getAssessmentDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(cdsForm.getAssessmentDate())%>">	
				</td>			
			</tr>
		<tr>
			<td class="genericTableHeader" >Service Initiation Date</td>
			<td class="genericTableData" >
				<input id="serviceInitiationDate" name="serviceInitiationDate" onfocus="this.blur()" class="systemData {validate: {required:true}}" type="text" readonly="readonly" value="<%=cdsForm.getServiceInitiationDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(cdsForm.getServiceInitiationDate())%>">	
			</td>		
		</tr>
		
	</table>	
	
</div>
