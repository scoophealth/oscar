<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>


<%@page import="org.oscarehr.PMmodule.web.AdmissionForHistoryTabDisplay"%><script type="text/javascript">
    function popupAdmissionInfo(admissionId) {
        url = '<html:rewrite page="/PMmodule/ClientManager.do?method=view_admission&admissionId="/>';
        window.open(url + admissionId, 'admission', 'width=600,height=600');
    }

    function popupReferralInfo(referralId) {
        url = '<html:rewrite page="/PMmodule/ClientManager.do?method=view_referral&referralId="/>';
        window.open(url + referralId, 'referral', 'width=500,height=600');
    }
</script>
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
		AdmissionForHistoryTabDisplay admissionForDisplay = (AdmissionForHistoryTabDisplay) pageContext.getAttribute("admission");
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
        <a href="javascript:void(0)" title="Referral details" onclick="popupReferralInfo('<c:out value="${referral.id}" />')">
            <img alt="View details" src="<c:out value="${ctx}" />/images/details.gif" border="0"/>
        </a>
    </display:column>
	<display:column property="programName" sortable="true" title="Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="referralDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Referral Date" />
	<!--  display:column property="providerFormattedName" sortable="true" title="Referring Provider" / -->
	<display:column property="completionDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Completion Date" />
	<!-- display:column property="completionNotes" sortable="true" title="Completion Notes" / -->
	<!-- display:column property="status" sortable="true" title="Status" / -->
	<!-- display:column property="notes" sortable="true" title="Notes" / -->
	<display:column property="completionNotes" sortable="false" title="Referring program/agency" />
	<display:column property="notes" sortable="false" title="External" />
</display:table>
