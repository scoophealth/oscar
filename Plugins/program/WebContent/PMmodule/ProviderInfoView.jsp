<%@ include file="/taglibs.jsp" %>
<script>
	function popupHelp(topic) {
		url = '<html:rewrite page="/help.jsp?topic="/>';
		window.open(url + topic,'help','width=450,height=500');
	}
</script>
Welcome <b><c:out value="${requestScope.provider.formattedName}"/></b>
<br/>
<br/>
<br/>

Your Agency Domain<a href="javascript:void(0)" onclick="popupHelp('agency_domain')">?</a> includes:
<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="agency" name="agencyDomain" export="false" requestURI="/PMmodule/ProviderInfo.do">
  <display:setProperty name="basic.msg.empty_list" value="No agencies."/>
  <display:column property="name" sortable="true" title="Name"/>
</display:table>

<br/><br/>
Your Program Domain<a href="javascript:void(0)" onclick="popupHelp('program_domain')">?</a> includes:
<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="program" name="programDomain" export="false" requestURI="/PMmodule/ProviderInfo.do">
  <display:setProperty name="basic.msg.empty_list" value="No programs."/>
  <display:column  sortable="true" title="Program Name">
  	<a href="<html:rewrite action="/PMmodule/ProgramManagerView"/>?id=<c:out value="${program.programId}"/>"><c:out value="${program.program.name}"/></a>
  </display:column>
  <display:column property="role.name" sortable="true" title="Role"/>
  <display:column>
  	<c:if test="${program.program.type eq 'Bed' }">
  	<a href="<html:rewrite action="/PMmodule/BedLog"/>?&programId=<c:out value="${program.programId}"/>"><img src="images/bed.gif" border="0"/></a>
  	</c:if>
  </display:column>
</display:table>

<br/>
<br/>

<!--
<html:link action="/PMmodule/ClientSearch2.do">I want to manage an existing client</html:link>
<br/>
 
<html:link action="/PMmodule/IntakeA.do">I want to enter a new client into the system using IntakeA</html:link>
<br/>
<html:link action="/PMmodule/IntakeC.do">I want to enter a new client into the system using IntakeC</html:link>
<br/>

<html:link action="/PMmodule/ProgramManager.do">I want to administer a program</html:link>

-->