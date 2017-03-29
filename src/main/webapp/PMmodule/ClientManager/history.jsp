
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



<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>

<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramProviderDAO"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%@page import="org.oscarehr.PMmodule.web.AdmissionForDisplay"%>
<%@page import="org.oscarehr.PMmodule.web.FunctionalCentreAdmissionDisplay"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ReferralHistoryDisplay" %>

<script type="text/javascript">
    function popupAdmissionInfo(admissionId) {
        url = '<html:rewrite page="/PMmodule/ClientManager.do?method=view_admission&admissionId="/>';
        window.open(url + admissionId, 'admission', 'width=600,height=600');
    }

    function popupReferralInfo(referralId) {
        url = '<html:rewrite page="/PMmodule/ClientManager.do?method=view_referral&referralId="/>';
        window.open(url + referralId, 'referral', 'width=500,height=600');
    }

    function popupFcAdmissionInfo(fcId) {
        url = '<html:rewrite page="/PMmodule/ClientManager.do?method=view_fcAdmission&fcAdmissionId="/>';
        window.open(url + fcId, 'fcAdmission', 'width=650,height=400');
    }

    function changeDate(admissionId) {
    	var newDate = prompt('Please enter a new Admission Date (yyyy-MM-dd HH:mm)');
    	if(newDate != null && newDate.length>0) {
    		$.ajax({url:'ClientManager/ClientManager.json?method=save_admission_date&admissionId='+admissionId + '&date='+newDate,async:true,dataType:'json', success:function(data) {
    			if(!data.success) {
    				alert(data.error);
    			} else {
    				location.href='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=((Demographic)request.getAttribute("client")).getDemographicNo()%>&view.tab=History&method=edit';
    			}
    		}});	
    	}
    	
    }
    
    function changeDischargeDate(admissionId) {
    	var newDate = prompt('Please enter a new Discharge Date (yyyy-MM-dd HH:mm)');
    	if(newDate != null && newDate.length>0) {
    		$.ajax({url:'ClientManager/ClientManager.json?method=save_discharge_date&admissionId='+admissionId + '&date='+newDate,async:true,dataType:'json', success:function(data) {
    			if(!data.success) {
    				alert(data.error);
    			} else {
    				location.href='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=((Demographic)request.getAttribute("client")).getDemographicNo()%>&view.tab=History&method=edit';
    			}
    		}});	
    	}
    	
    }
    
    function changeReferralDate(referralId) {
    	var newDate = prompt('Please enter a new Referral Date (yyyy-MM-dd HH:mm)');
    	if(newDate != null && newDate.length>0) {
    		$.ajax({url:'ClientManager/ClientManager.json?method=save_referral_date&referralId='+referralId + '&date='+newDate,async:true,dataType:'json', success:function(data) {
    			if(!data.success) {
    				alert(data.error);
    			} else {
    				location.href='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=((Demographic)request.getAttribute("client")).getDemographicNo()%>&view.tab=History&method=edit';
    			}
    		}});	
    	}
    	
    }
    
    function changeCompletionDate(referralId) {
    	var newDate = prompt('Please enter a new Completion Date (yyyy-MM-dd HH:mm)');
    	if(newDate != null && newDate.length>0) {
    		$.ajax({url:'ClientManager/ClientManager.json?method=save_completion_date&referralId='+referralId + '&date='+newDate,async:true,dataType:'json', success:function(data) {
    			if(!data.success) {
    				alert(data.error);
    			} else {
    				location.href='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=((Demographic)request.getAttribute("client")).getDemographicNo()%>&view.tab=History&method=edit';
    			}
    		}});	
    	}
    	
    }
</script>
<%
	try
	{	
%>
<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Admission History</th>
		</tr>
	</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissionHistory" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />
	<%
		AdmissionForDisplay admissionForDisplay = (AdmissionForDisplay) pageContext.getAttribute("admission");
	%>
	<%
		ProgramProviderDAO ppd =(ProgramProviderDAO)SpringUtils.getBean("programProviderDAO");
		
	%>
	<% boolean bShowEncounterLink = false; 
	
	%>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
	<% bShowEncounterLink = true; %>
	</security:oscarSec>
	<%
	String curUser_no = (String) session.getAttribute("user");
	String rsAppointNO="0";
	String status = "T";
	String userfirstname = (String) session.getAttribute("userfirstname");;
	String userlastname = (String) session.getAttribute("userlastname");
	String reason ="";
	%>
	
    <display:column sortable="false">
    <%
    	if (!admissionForDisplay.isFromIntegrator())
    	{
    		%>
		        <a href="javascript:void(0)" onclick="popupAdmissionInfo('<%=admissionForDisplay.getAdmissionId()%>')">
		            <img alt="View details" src="<c:out value="${ctx}" />/images/details.gif" border="0"/>
		        </a>
    		<%
    	}
    %>
    </display:column>
	<display:column property="facilityName" sortable="false" title="Facility Name" />
    <display:column property="programName" sortable="true" title="Program Name" />
    
    <display:column sortable="true" title="">	
		<% if(bShowEncounterLink && !admissionForDisplay.isFromIntegrator()) {	
			HttpSession se = request.getSession();			
			Integer programId = admissionForDisplay.getProgramId();
			
			//Integer demographic_no = admissionForDisplay.getClientId();???
			Demographic currentDemographic=(Demographic)request.getAttribute("client");
			Integer demographic_no = currentDemographic.getDemographicNo();
			
			//Check program is in provider's program domain:
			if(ppd.isThisProgramInProgramDomain(curUser_no,programId)) {
				String eURL = "../oscarEncounter/IncomingEncounter.do?programId="+programId+"&providerNo="+curUser_no+"&appointmentNo="+rsAppointNO+"&demographicNo="+demographic_no+"&curProviderNo="+curUser_no+"&reason="+java.net.URLEncoder.encode(reason)+"&encType="+java.net.URLEncoder.encode("face to face encounter with client","UTF-8")+"&userName="+java.net.URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate=null&appointmentDate=null&startTime=0:0"+"&status="+status+"&source=cm";
		%>	
		<logic:notEqual value="community" property="programType" name="admission">
		
		<a href=#
			onClick="popupPage(710, 1024,'../oscarSurveillance/CheckSurveillance.do?programId=<%=programId%>&demographicNo=<%=demographic_no%>&proceed=<%=java.net.URLEncoder.encode(eURL)%>');return false;"
			title="<bean:message key="global.encounter"/>"> <bean:message
			key="provider.appointmentProviderAdminDay.btnE" /></a> 
		
		</logic:notEqual>
	<% 		} 
		}
	%>
		</display:column>
	
		
    
    
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column property="facilityAdmission" title="Facility<br />Admission" />
	<display:column property="dischargeDate" sortable="true" title="Discharge Date" />
	<display:column property="facilityDischarge" title="Facility<br />Discharge" />
	<display:column property="daysInProgram" sortable="true" title="Days in Program" />
	<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
	<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	</caisi:isModuleLoad>
</display:table>
<br />
<br />
<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Referral History</th>
		</tr>
	</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="referral" name="referralHistory" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	
    <display:column sortable="false">
    	<c:if test="${!referral.isRemoteReferral}">
	        <a href="javascript:void(0)" title="Referral details" onclick="popupReferralInfo('<c:out value="${referral.id}" />')">
	            <img alt="View details" src="<c:out value="${ctx}" />/images/details.gif" border="0"/>
	        </a>
        </c:if>
    </display:column>
	<display:column property="destinationProgramName" sortable="true" title="Program Name" />
	<display:column property="destinationProgramType" sortable="true" title="Program Type" />
	<display:column property="referralDate" sortable="true" title="Referral Date" />
	<display:column property="completionDate" sortable="true" title="Completion Date" />
	<display:column property="sourceProgramName" sortable="false" title="Referring program/agency" />
	<display:column property="external" sortable="false" title="External" />
</display:table>
<br />
<br />
<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Functional Centre Admission History</th>
		</tr>
	</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3" id="fcAdmission" name="fcAdmissionsHistory" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
     <display:column sortable="false">
    <%  FunctionalCentreAdmissionDisplay temp = (FunctionalCentreAdmissionDisplay) pageContext.getAttribute("fcAdmission");
        //if(temp.getDischargeDate().equals(""))
    	//{
    		%>
		        <a href="javascript:void(0)" onclick="popupFcAdmissionInfo('<%=temp.getId()%>')">
		            <img alt="View details" src="<c:out value="${ctx}" />/images/details.gif" border="0"/>
		        </a>
    		<%
    	//}
    %>
    </display:column>
    <display:column property="functionalCentre" sortable="true" title="Functional Centre" />
	<display:column property="referralDate" sortable="true" title="Referral Date" />
	<display:column property="admissionDate" sortable="true" title="Admission Date" />
	<display:column property="serviceInitiationDate" sortable="true" title="Service Initiation Date" />
	<display:column property="dischargeDate" sortable="false" title="Discharge Date" />
	
</display:table>
<%
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("Error", e);
	}
%>
