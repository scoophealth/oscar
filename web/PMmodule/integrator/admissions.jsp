<%@ include file="/taglibs.jsp" %>
<%@ page import="org.oscarehr.PMmodule.model.*" %>
<%@ page import="org.oscarehr.PMmodule.service.impl.IntegratorManagerImpl" %>
<%@ page import="java.util.*" %>

	<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="20" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  <display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs."/>
	  
	  <display:column sortable="true" title="Agency">
	  	<%
	  		Admission tmpAd = (Admission)pageContext.getAttribute("admission");
		  	Map agencyMap = IntegratorManagerImpl.getAgencyMap();
		  	Agency agency = (Agency)agencyMap.get(tmpAd.getAgencyId());
		  	if(agency != null) {
		  		out.println(agency.getName());
		  	} else {
	  	%>
	  	<c:out value="${admission.agencyId}"/>
	  	<% } %>
	  </display:column>
	  
	  <display:column property="programName" sortable="true" title="Program Name"/>
	  <display:column property="admissionDate" sortable="true" title="Admission Date"/>
  	  <display:column sortable="true" title="Days in Program">
	  	<%
	  		Admission tmpAd = (Admission)pageContext.getAttribute("admission");
	  	
	  		Date admissionDate = tmpAd.getAdmissionDate();
		  	Date dischargeDate = tmpAd.getDischargeDate();
		  	String adNumDays = "";
		  	if(dischargeDate == null) {
		  		dischargeDate = new Date();
		  	} 
	  		long diff1 = dischargeDate.getTime() - admissionDate.getTime();
	  		diff1 = diff1/1000; //seconds;
	  		diff1 = diff1/60; //minutes;
	  		diff1 = diff1/60;	//hours
	  		diff1 = diff1/24;	//days
	  		//diff = diff+1; (round up?)
	  		adNumDays = String.valueOf(diff1);
		  	
	  	%>	
	  	<%=adNumDays %>
	  </display:column>
       <display:column property="temporaryAdmission"  sortable="true" title="Temporary Admission" />
      <display:column property="admissionNotes"  sortable="true" title="Admission Notes" />	
	</display:table>	