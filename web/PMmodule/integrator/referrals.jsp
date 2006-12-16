<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.PMmodule.service.impl.IntegratorManagerImpl" %>

	<display:table class="simple" cellspacing="2" cellpadding="3" id="referral" name="referrals" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  
	  <display:column sortable="true" title="Source Agency Id">
	  <%
	  		ClientReferral tmpReferral = (ClientReferral)pageContext.getAttribute("referral");
		  	Map agencyMap = IntegratorManagerImpl.getAgencyMap();
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
		  	Map agencyMap = IntegratorManagerImpl.getAgencyMap();
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