<%@ page import="org.oscarehr.eyeform.model.Macro" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%@ include file="/taglibs.jsp"%>

<html>
<head>
<title>Macros</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/eyeform/displaytag.css" type="text/css">
<style type="text/css">
.boldRow {
	color:red;
}
.commonRow{
	color:black;
}
span.h5 {
  margin-top: 1px;
  border-bottom: 1px solid #000;
  width: 90%;
  font-weight: bold;
  list-style-type: none;
  padding: 2px 2px 2px 2px;
  color: black;
  background-color: #69c;
  font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
  font-size: 10pt;
  text-decoration: none;
  display: block;
  clear: both;
  white-space: nowrap;	

}
</style>
<script>
function editMacro(id) {
	location.href='<%=request.getContextPath()%>/eyeform/Macro.do?method=form&macro.id='+id;	
}
</script>
</head>
<body>

<form name="inputForm" method="post" action="Macro.do">
	<input type="hidden" name="method" value="editMacro">
	<input type="hidden" name="editTarget" value="">
	<h1>Eyeform Macros</h1>
	
	<table style="border:0px">
	<tr>
	<td>
	<input type="button" value="Add" onclick="this.form.method.value='addMacro';this.form.submit();">
	<input type="button" value="Delete" onclick="if(confirm('Are you sure to delete the selected macros?')){this.form.method.value='deleteMacro';this.form.submit();}">
	<input type="button" value="Close" onclick="window.opener.location.reload();window.close();return false;">
	</td>
	</tr>
	</table>

	<display:table name="requestScope.macros" requestURI="Macro.do?method=list" defaultsort="2" sort="list" defaultorder="ascending" id="macro" pagesize="0">
		<%
			Macro m = (Macro)pageContext.getAttribute("macro");												
		%>	
		<display:column style="width:20px;">
		<input type="checkbox" name="selected_id" value="<c:out value="${macro.id}"/>">
		</display:column>
		<display:column title="#" property="displayOrder" sortable="true" style="width:20px;">
		</display:column>		
		<display:column title="Label" sortable="true" style="width:120px;">
		<a href="javascript:void(0)" onclick="editMacro(<c:out value="${macro.id}"/>);"><c:out value="${macro.label}" /></a>
		</display:column>
		<display:column property="impression" sortable="true" title="Append impression with" style="width:250px;"/>
		<display:column title="Followup in" style="width:200px;">			
			<%=getFollowupWeb(m) %>
		</display:column>
		<display:column title="Test Bookings" style="width:80px" >
			<pre><c:out value="${macro.testRecords}" /></pre>
		</display:column>		
		<display:column title="Tickler" >
			<%=getTicklerWeb(m)	%>			
		</display:column>
		<display:column title="Bill" style="width:40px" >
			<%=getBillingWeb(m)%>
		</display:column>		
	</display:table>
	
</form>

</body>
</html>
<%!
public String getFollowupWeb(Macro macro) {
	if(macro.getFollowupNo()>0) {
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		Provider provider = providerDao.getProvider(macro.getFollowupDoctorId());
		return macro.getFollowupNo() + " " + macro.getFollowupUnit() + " with Dr. " + provider.getFormattedName();
	}
	return new String();
}

public String getTicklerWeb(Macro macro) {
	if(macro.getTicklerRecipient().length()>0) {
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		Provider provider = providerDao.getProvider(macro.getTicklerRecipient());
		return provider.getFormattedName();
	}
	return new String();
}

public String getBillingWeb(Macro macro) {
	if(macro.getBillingCodes()!=null && macro.getBillingCodes().trim().length()>0) {
		return "YES";
	}
	return "NO";
}
%>