<!-- 

Source:web/PMmodule/Admin/User/AddRoles.jsp 

-->

<%@ include file="/taglibs.jsp"%>

<html:form action="/PMmodule/Admin/UserManager" method="post">
<html:hidden property="method" value="save" />
<input type="hidden" id="pageChanged" name="pageChanged" value='<c:out value="${pageChanged}"/>' />
<html:hidden property="providerNo" />
<input type="hidden" id="scrollPosition" name="scrollPosition" value='<c:out value="${scrPos}"/>' />

<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
 <tr><th class="pageTitle" align="center"><span	id="lblTitle" align="left">User Management - Role/Org Security </span></th></tr>
 <tr><td align="left" class="buttonBar2">
	<html:link	href="javascript:submitForm('edit');" style="color:Navy;text-decoration:none;">
	<img border=0 src=<html:rewrite page="/images/Back16.png"/> />&nbsp;Back to User Profile&nbsp;&nbsp;|</html:link>
	<c:if test="${!isReadOnly}">
	<html:link href="javascript:submitForm('saveRoles');" onclick="javascript: setNoConfirm();"
		style="color:Navy;text-decoration:none;">
	<img border=0 src=<html:rewrite page="/images/Save16.png"/> />&nbsp;Save&nbsp;&nbsp;|</html:link>
	</c:if>
  </td></tr>
  <tr><td align="left" class="message">
      <logic:messagesPresent message="true"><br />
        <html:messages id="message" message="true" bundle="pmm"><c:out escapeXml="false" value="${message}" /></html:messages> 
       <br /></logic:messagesPresent>
  </td></tr>
   
  <tr><td><br/><div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
	  <tr><th title="Programs">Role/Org Security</th></tr>
	</table>
  </div></td></tr>
		
  <tr><td>		
	<table width="100%" class="simple" cellspacing="2" cellpadding="3">
		<tr><td width="20%">User ID:</td>
		<td width="80%"><html:text property="userName" readonly="true" style="border: none" /></td></tr>
		<tr><td>Last Name:</td>
		<td><html:text property="lastName" readonly="true" style="border: none" /></td>
		<tr><td>First Name:</td>
		<td><html:text property="firstName" readonly="true" style="border: none" /></td></tr>
	</table>
  </td></tr>		
  
   <tr><td><br/>Org/Role Profile:</td></tr>
   <tr><td height="100%">
	 <div style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
               height: 100%; width: 100%; overflow: auto;" id="scrollBar" onscroll="getDivPosition()">
		<TABLE align="center" class="simple" width="100%">
		<TR bgcolor="#f0f0f0"><td align="center">Select</td>
		<td align="center">Org</td>
		<td align="center">Role</td></TR>
		<logic:iterate id="secUserRole" name="secuserForm" property="secUserRoleLst" indexId="rIndex">
		  <TR><TD align="center" width="5%"><input type="checkbox"
				name="p<%=String.valueOf(rIndex)%>" value="" />
			<input type="hidden" name="lineno" value="<%=String.valueOf(rIndex)%>" /></TD>
		  <TD width="55%"><table cellpadding="0" style="border:0px;" cellspacing="0" width="100%">
			<tr><td style="border:0px;" width="20%">
			  <input style="width:100%;" type="text"
				name="org_code<%=String.valueOf(rIndex)%>"
			    value='<c:out value="${secUserRole.orgcd}"/>' readonly></td>
			<td style="border:0px;" width="70%"><input id="ORGfld<%=String.valueOf(rIndex)%>" 
				style="width:99%;" type="text" name="org_description<%=String.valueOf(rIndex)%>"
				value='<c:out value="${secUserRole.orgcd_desc}"/>' readonly></td>
			<td style="border:0px;" width="10%"><a href="javascript:void1();" onclick="return showLookupTree('ORG', '', '', 'secuserForm','org_code<%=String.valueOf(rIndex)%>','org_description<%=String.valueOf(rIndex)%>', true, '<c:out value="${ctx}"/>');">
			    <img src="<c:out value="${ctx}"/>/images/microsoftsearch.gif" border="0"></a>
				<a href="javascript:void1();" onclick="return clearLookupValue('secuserForm','org_code<%=String.valueOf(rIndex)%>','org_description<%=String.valueOf(rIndex)%>');">
				    <img src="<c:out value="${ctx}"/>/images/Reset16.gif" border="0"></a>			    
			    </td></tr>
		    </table>
		  </TD>
		  <TD width="40%">
			<table cellpadding="0" style="border:0px;" cellspacing="0" width="100%">
			<tr><td style="border:0px;" width="85%"><input type="text"
				style="width:99%;" name="role_code<%=String.valueOf(rIndex)%>"
				value='<c:out value="${secUserRole.roleName}"/>' readonly></td>
		    <td style="border:0px;" width="1px">
		       <input style="width:1px;" type="text" 
				  name="role_description<%=String.valueOf(rIndex)%>"
				  value='<c:out value="${secUserRole.roleName}"/>' readonly></td>
			<td style="border:0px;" width="15%">
			<a href="javascript:void1()" onclick="return showLookup2('ORGfld<%=String.valueOf(rIndex)%>','ROL', '', '', 'secuserForm','role_code<%=String.valueOf(rIndex)%>','role_description<%=String.valueOf(rIndex)%>', true, '<c:out value="${ctx}"/>');">
			  <img src="<c:out value="${ctx}"/>/images/microsoftsearch.gif" border="0"></a>
			<a href="javascript:void1()" onclick="return clearLookupValue('secuserForm','role_code<%=String.valueOf(rIndex)%>','role_description<%=String.valueOf(rIndex)%>');">
			  <img src="<c:out value="${ctx}"/>/images/Reset16.gif" border="0"></a>			  
			  </td></tr>
			</table>
		  </TD></TR>
		</logic:iterate>
	    <tr><td colspan="3">&nbsp;
	    	<c:if test="${!isReadOnly}">
			    <a href="javascript:submitForm('addRole');"  onclick="javascript:setNoConfirm()">Add</a>&nbsp;&nbsp;&nbsp;|
				&nbsp;&nbsp;<a href="javascript:submitForm('removeRole');"  onclick="javascript:setNoConfirm()">Remove</a>
			</c:if>
	     </td></tr>
	  </table>
	  </div>
	</td></tr>
</table>
<%@ include file="/common/readonly.jsp" %>
</html:form>
<script language="javascript" type="text/javascript">
<!--
function submitForm(mthd){
	trimInputBox();
	if(mthd == "saveRoles" && noChanges())
	{
		alert("There are no changes detected to save");
	}
	else if(mthd=="removeRole" && !getChecks())
	{
		alert ("Please select a line to remove");
	}
	else
	{
		document.forms[0].method.value=mthd;
		document.forms[0].submit();
	}
}
function getChecks()
{
	for(var i=0; i<1000; i++) 
	{
		var cs = document.getElementsByName("p" + i.toString());
		var c = cs[0];
		if (c) {
			if (c.checked) return true;
		}
		else
		{
			return false;
		}
	}
}

//-->
</script>

