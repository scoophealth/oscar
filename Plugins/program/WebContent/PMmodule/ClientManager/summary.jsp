<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.model.*" %>
<%@ page import="java.util.*" %>
<script>
	function openIntakeA(view) {
		var url = '<html:rewrite action="/PMmodule/IntakeA.do"/>';
		url += "?formIntakeALock=N&viewIntakeA=" + view + "&demographicNo=";
		url += '<c:out value="${client.demographicNo}"/>';
		
		location.href = url;
	}
	
	function openIntakeC(view) {
		var url = '<html:rewrite action="/PMmodule/IntakeC.do"/>';
		url += "?formIntakeCLock=N&viewIntakeC=" + view + "&demographicNo=";
		url += '<c:out value="${client.demographicNo}"/>';
		
		location.href = url;
	}
</script>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Personal Information</th>
			</tr>
		</table>
	</div>

	<table width="100%" border="1" cellspacing="2" cellpadding="3" class="b">
		<tr class="b">
			<td width="20%">Name:</td>
			<td><c:out value="${client.formattedName}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Date of Birth:</td>
			<td><c:out value="${client.yearOfBirth}"/>/<c:out value="${client.monthOfBirth}"/>/<c:out value="${client.dateOfBirth}"/></td>
		</tr>
		<tr class="b">
			<td width="20%">Resources</td>
			<%
				Integer iDemographicNo = ((org.caisi.PMmodule.model.Demographic)request.getAttribute("client")).getDemographicNo();
				String demographicNo = String.valueOf(iDemographicNo);
			%>
			<td><caisi:OscarDemographicLink demographicNo="<%=demographicNo %>"/></td>
		</tr>
	</table>

	<br/>
	<br/>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Intake Forms</th>
			</tr>
		</table>
	</div>
	<table width="100%" border="1" cellspacing="2" cellpadding="3" class="b">
		<thead>
		<tr>
			<th style="color:black">Form Name</th>
			<th style="color:black">Most Recent</th>
			<th style="color:black">Actions</th>
		</tr>
		</thead>
		<c:if test="${requestScope.intakeAEnabled eq 'true'}">
		<tr class="b">
			<td width="20%">Intake A:</td>
			<c:if test="${intakeADate != null}">
				<td>
					<c:out value="${intakeADate}"/>
				</td>
				<td>
					<input type="button" value="Update" onclick="javascript:openIntakeA('Y')"/>
				</td>
			</c:if>
			<c:if test="${intakeADate == null}">
				<td>
					<span style="color:red">None found</span>
				</td>
				<td>
					<input type="button" value="New Form" onclick="javascript:openIntakeA('N')"/>
				</td>
			</c:if>				
		</tr>
		</c:if>
		<c:if test="${requestScope.intakeCEnabled eq 'true'}">
		
		<tr class="b">
			<td width="20%">Intake C:</td>
			<c:if test="${intakeCDate != null}">
				<td>
					<c:out value="${intakeCDate}"/>
				</td>
				<td>
					<input type="button" value="Update" onclick="javascript:openIntakeC('Y')"/>
				</td>
			</c:if>
			<c:if test="${intakeCDate == null}">
				<td>
					<span style="color:red">None found</span>
				</td>
				<td>
					<input type="button" value="New Form" onclick="javascript:openIntakeC('N')"/>
				</td>
			</c:if>				
		</tr>
		</c:if>
		<!-- 
		<tr class="b">
			<td width="20%">Intake B:</td>
			<td>
				<c:if test="${intakeBDate != null}">
					<c:out value="${intakeBDate}"/>
				</c:if>
				<c:if test="${intakeBDate == null}">
					<span style="color:red">None found</span>
				</c:if>	
			</td>
			<td>
				<input type="button" value="New Form"/>
			</td>
		</tr>
		-->
	</table>
	
	<br/>
	<br/>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Current Programs</th>
			</tr>
		</table>
	</div>
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="admission" name="admissions" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
	  <display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs."/>
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
      <display:column property="admissionNotes"  sortable="true" title="Admission Notes" />	
	</display:table>	
	
	<br/><br/>
	
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Referrals</th>
			</tr>
		</table>
	</div>
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="referral" name="referrals" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	  <display:setProperty name="paging.banner.placement" value="bottom"/>
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