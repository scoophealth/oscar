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
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.AdmissionDao"%>
<%@page import="org.oscarehr.PMmodule.model.ClientReferral"%>
<%@page import="org.oscarehr.PMmodule.dao.ClientReferralDAO"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>

<% AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
   String currentDemographicId= (String)request.getParameter("demographicId");
   String currentAdmissionId=(String)request.getParameter("admissionId");
   String programId = (String)request.getParameter("programId");
   String ocanStaffFormId = (String) request.getParameter("ocanStaffFormId");
   String view =(String) request.getParameter("view");
   
   OcanStaffForm ocanStaffForm = new OcanStaffForm();
   Admission ad = new Admission();
   
   if(view!=null && "history".equals(view)) {
	   OcanStaffForm ocanStaffForm2 = OcanForm.getOcanStaffForm(Integer.valueOf(ocanStaffFormId));
	   ocanStaffForm.setAdmissionId(ocanStaffForm2.getAdmissionId());
	   ocanStaffForm.setServiceInitDate(ocanStaffForm2.getServiceInitDate());
	   ocanStaffForm.setAdmissionDate(ocanStaffForm2.getAdmissionDate());
	   ocanStaffForm.setDischargeDate(ocanStaffForm2.getDischargeDate());
	   ocanStaffForm.setReferralDate(ocanStaffForm2.getReferralDate());	   
   } else {
	   if(ocanStaffFormId!=null && !ocanStaffFormId.equals("") && Integer.valueOf(ocanStaffFormId).intValue()>0) {
		   OcanStaffForm ocanStaffForm2 = OcanForm.getOcanStaffForm(Integer.valueOf(ocanStaffFormId));
		   ocanStaffForm.setAdmissionId(ocanStaffForm2.getAdmissionId());		   
		   ocanStaffForm.setServiceInitDate(ocanStaffForm2.getServiceInitDate());
		   ocanStaffForm.setAdmissionDate(ocanStaffForm2.getAdmissionDate());
		   ocanStaffForm.setDischargeDate(ocanStaffForm2.getDischargeDate());
		   ocanStaffForm.setReferralDate(ocanStaffForm2.getReferralDate());	 
		   
		   // The admission may have new discharge date as the client may be discharged from the program.
		   ad = admissionDao.getAdmission(Long.valueOf(ocanStaffForm2.getAdmissionId()));
		   if(ad!=null) {			    
		   		ocanStaffForm.setDischargeDate(ad.getDischargeDate());
		   }
		   
	   } else {
		   if(currentAdmissionId!=null && !currentAdmissionId.equals("") && Integer.valueOf(currentAdmissionId).intValue()>0) {	   
		   		ad = admissionDao.getAdmission(Long.valueOf(currentAdmissionId));
		   		ocanStaffForm.setAdmissionId(ad.getId().intValue());
				ocanStaffForm.setServiceInitDate(ad.getAdmissionDate());
				ocanStaffForm.setAdmissionDate(ad.getAdmissionDate());
				ocanStaffForm.setDischargeDate(ad.getDischargeDate());
	   		} else if( programId!=null && !programId.equals("") && Integer.valueOf(programId).intValue()>0) {
			   	ad = admissionDao.getCurrentAdmission(Integer.valueOf(programId), Integer.valueOf(currentDemographicId));
			   	ocanStaffForm.setAdmissionId(ad.getId().intValue());
				ocanStaffForm.setServiceInitDate(ad.getAdmissionDate());
				ocanStaffForm.setAdmissionDate(ad.getAdmissionDate());
				ocanStaffForm.setDischargeDate(ad.getDischargeDate());	   
	   		}
   
		    //Find referral date
			ClientReferralDAO clientReferralDao = (ClientReferralDAO) SpringUtils.getBean("clientReferralDAO");
			List<ClientReferral> referrals = clientReferralDao.getActiveReferralsByClientAndProgram(Long.valueOf(currentDemographicId), Long.valueOf(ad.getProgramId()));
			if(referrals.size() > 0 ) {
				ClientReferral ref = referrals.get(0);
				if(ref.getReferralDate()!=null)
					ocanStaffForm.setReferralDate(ref.getReferralDate());
				else
					ocanStaffForm.setReferralDate(ad.getAdmissionDate());
			} else {
				ocanStaffForm.setReferralDate(ad.getAdmissionDate());
			} 	
	   }
   }
%>
    		
<div id="cbi_dates">
  <table width="100%">
		<tr>
			<td class="genericTableHeader" width="36%">Referral Date</td>
			<td class="genericTableData" width="64%">			
				<input id="referralDate" name="referralDate" onfocus="this.blur()" class="systemData {validate: {required:true}}" type="text" readonly="readonly" value="<%=ocanStaffForm.getReferralDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(ocanStaffForm.getReferralDate())%>">			
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader" width="36%">Admission Date</td>
			<td class="genericTableData" width="64%">
				<input id="admissionDate" name="admissionDate" onfocus="this.blur()" class="systemData {validate: {required:true}}" type="text" readonly="readonly" value="<%=ocanStaffForm.getAdmissionDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(ocanStaffForm.getAdmissionDate())%>">	
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader" width="36%">Service Initiation Date</td>
			<td class="genericTableData" width="64%">
				<input id="serviceInitDate" name="serviceInitDate" onfocus="this.blur()" class="userInputedData mandatoryData {validate: {required:true}}" type="text" value="<%=ocanStaffForm.getServiceInitDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(ocanStaffForm.getServiceInitDate())%>"> <img title="Calendar" id="cal_serviceInitDate" src="../../images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'serviceInitDate',ifFormat :'%Y-%m-%d',button :'cal_serviceInitDate',align :'cr',singleClick :true,firstDay :1});</script><img src="../../images/icon_clear.gif" border="0" onclick="clearDate('serviceInitDate');">	
			</td>		
		</tr>
		<tr>
			<td class="genericTableHeader" width="36%">Discharge Date</td>
			<td class="genericTableData" width="64%">
				<input id="dischargeDate" name="dischargeDate" onfocus="this.blur()" class="systemData {validate: {required:false}}" type="text" readonly="readonly" value="<%=ocanStaffForm.getDischargeDate()==null?"":DateFormatUtils.ISO_DATE_FORMAT.format(ocanStaffForm.getDischargeDate())%>"> 
			</td>			
		</tr>
	</table>	
	
</div>
