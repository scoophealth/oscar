<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.model.*" %>
<%@ page import="java.util.*" %>
<script>
	function resetClientFields() {
		var form = document.clientManagerForm;
		form.elements['program.name'].value='';
	}
	
	function search_programs() {
		var form = document.clientManagerForm;
		form.method.value='refer_select_program';
		var programName = form.elements['program.name'].value;
		var url = '<html:rewrite action="/PMmodule/ClientManager.do"/>';
		url += '?method=search_programs&program.name=' + programName;
		url += '&formName=clientManagerForm&formElementName=program.name&formElementId=program.id&formElementType=program.type&submit=true';
		window.open(url,'program_search','width=500,height=550');
	}

	function do_referral() {
		var form = document.clientManagerForm;
		form.method.value='refer';
		form.submit();
		
	}
</script>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Referrals</th>
			</tr>
		</table>
	</div>
	<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="referral" name="referrals" export="false" pagesize="0" requestURI="/PMmodule/ClientManager.do">
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
	  <display:column property="notes"  sortable="true" title="Notes" />
	</display:table>
	<br/><br/>
	<div class="tabs" id="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
	
			<tr>
				<th title="Programs">Search Programs</th>
			</tr>
		</table>
	</div>
		<html:hidden property="program.id"/>
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Agency</td>
				<td>
					<span style="color:red"><i>Not yet implemented</i></span>
				</td>
			</tr>
			<tr class="b">
				<td width="20%">Program Name</td>
				<td><html:text property="program.name"/></td>
			</tr>
		</table>
		
		<table>
			<tr>
				<td align="center">
					<input type="button" value="search" onclick="search_programs()"/>
		    	</td>
				<td  align="center">
					<input type="button"  name="reset" value="reset" onclick="javascript:resetClientFields();"/>
		    	</td>
			</tr>
		</table>	
		
	<br/>
	<c:if test="${requestScope.do_refer != null}">
		<table class="b" border="0" width="100%">
			<tr>
				<th style="color:black">Program Name</th>
				<th style="color:black">Type</th>
				<th style="color:black">Participation</th>
				<th style="color:black">Phone</th>
				<th style="color:black">Email</th>
			</tr>
			<tr>
				<td><c:out value="${program.name }"/></td>
				<td><c:out value="${program.type }"/></td>
				<td><c:out value="${program.numOfMembers}"/>/<c:out value="${program.maxAllowed}"/> (<c:out value="${program.queueSize}"/> waiting)</td>
				<td><c:out value="${program.phone }"/></td>
				<td><c:out value="${program.email }"/></td>
			</tr>
		</table>
	</c:if>
	<br/>
	<c:if test="${requestScope.do_refer != null}">
		<table width="100%" border="1" cellspacing="2" cellpadding="3">
			<tr class="b">
				<td width="20%">Referral Notes:</td>
				<td><html:textarea cols="50" rows="7" property="referral.notes"/></td>
			</tr>
			<tr class="b">
				<td colspan="2">
					<input type="button" value="Process Referral" onclick="do_referral()"/>
					<input type="button" value="Cancel"/>
				</td>
			</tr>
		</table>
	</c:if>
	
