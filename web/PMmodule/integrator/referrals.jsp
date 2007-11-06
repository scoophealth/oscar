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

<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.PMmodule.service.IntegratorManager" %>

	<display:table class="simple" cellspacing="2" cellpadding="3" id="referral" name="referrals" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  
	  <display:column sortable="true" title="Source Agency Id">
	  <%
	  		ClientReferral tmpReferral = (ClientReferral)pageContext.getAttribute("referral");
		  	Map agencyMap = IntegratorManager.getAgencyMap();
		  	Agency agency = (Agency)agencyMap.get(tmpReferral.getSourceAgencyId());
		  	if(agency != null) {
		  		out.println(agency.getName());
		  	} else {
	  	%>
	  	<c:out value="${referral.sourceAgencyId}"/>
	  	<% } %>
	  </display:column>
	  
	  <display:column sortable="true" title="Agency Id">
	  <%
	  		ClientReferral tmpReferral = (ClientReferral)pageContext.getAttribute("referral");
		  	Map agencyMap = IntegratorManager.getAgencyMap();
		  	Agency agency = (Agency)agencyMap.get(tmpReferral.getAgencyId());
		  	if(agency != null) {
		  		out.println(agency.getName());
		  	} else {
	  	%>
	  	<c:out value="${referral.agencyId}"/>
	  	<% } %>	 
	  </display:column>
	  
	  
	  <display:column property="programName" sortable="true" title="Program Name"/>
	  <display:column property="referralDate"  sortable="true" title="Referral Date" />
	  <display:column property="providerFormattedName"  sortable="true" title="Referring Provider" />
	  <display:column sortable="true" title="Days in Queue">
	  	<%
	  		ClientReferral temp = (ClientReferral)pageContext.getAttribute("referral");
	  		Date referralDate = temp.getReferralDate();
	  		Date currentDate = new Date();
	  		String numDays = "";
		  	
	  		long diff = currentDate.getTime() - referralDate.getTime();
	  		diff = diff/1000; //seconds;
	  		diff = diff/60; //minutes;
	  		diff = diff/60;	//hours
	  		diff = diff/24;	//days
	  		//diff = diff+1; (round up?)
	  		numDays = String.valueOf(diff);
	  	%>	
	  	<%=numDays %>
	  </display:column>	  
	  </display:table>