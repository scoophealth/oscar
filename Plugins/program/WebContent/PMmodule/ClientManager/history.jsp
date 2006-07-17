<%@ include file="/taglibs.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="org.caisi.PMmodule.model.Admission" %>

	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Admission History</th>
			</tr>
		</table>
	</div>
	
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="admission" name="admissionHistory" export="false" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  <display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs."/>
	  <display:column property="programName" sortable="true" title="Program Name"/>
	  <display:column property="admissionDate" sortable="true" title="Admission Date"/>
	  <display:column property="dischargeDate" sortable="true" title="Discharge Date"/>
	  <display:column sortable="true" title="Days in Program">
	  	<%
	  		Admission tmpAd = (Admission)pageContext.getAttribute("admission");
	  	
	  		Date admissionDate = tmpAd.getAdmissionDate();
		  	Date dischargeDate = tmpAd.getDischargeDate();
		  	String numDays = "";
		  	if(dischargeDate == null) {
		  		dischargeDate = new Date();
		  	} 
		  		long diff = dischargeDate.getTime() - admissionDate.getTime();
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
	<br/><br/>
	
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Referral History</th>
			</tr>
		</table>
	</div>
	
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="referral" name="referralHistory" export="false" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  <display:column property="programName" sortable="true" title="Program Name"/>
	  <display:column property="referralDate"  sortable="true" title="Referral Date" />
	  <display:column property="providerFormattedName"  sortable="true" title="Referring Provider" />
	  <display:column property="status"  sortable="true" title="Status" />
	  <display:column property="notes"  sortable="true" title="Notes" />
	</display:table>
	<br/><br/>
	
	