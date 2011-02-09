<!-- 

Source:web/PMmodule/Admin/Role/RoleEdit.jsp

-->

<%@ include file="/taglibs.jsp"%>
<%String a="debug"; %>
<html:form action="/PMmodule/Admin/RoleManager" method="post">
<html:hidden property="method" value="save" />
<input type="hidden" id="scrollPosition" name="scrollPosition" value='<c:out value="${scrPos}"/>' />
<input type="hidden" id="pageChanged" name="pageChanged" value='<c:out value="${pageChanged}"/>' />
<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
<tr><th class="pageTitle" align="center">
	<span align="left">Role Management - 
		<logic:present	name="secroleForEdit">Edit Role</logic:present>
		<logic:notPresent name="secroleForEdit">Add Role</logic:notPresent> </span></th></tr>
<tr><td align="left" class="buttonBar2">
  <html:link	action="/PMmodule/Admin/RoleManager.do" style="color:Navy;text-decoration:none;">
  <img border=0 src=<html:rewrite page="/images/Back16.png"/> />&nbsp;Back to Role List&nbsp;&nbsp;|</html:link>
  
  <c:if test="${!isReadOnly}" >
	  <logic:present name="secroleForEdit">
		<html:link href="javascript:submitForm('saveChange');" onclick="javascript:setNoConfirm();" 	style="color:Navy;text-decoration:none;">
		<img border=0 src=<html:rewrite page="/images/Save16.png"/> />&nbsp;Save&nbsp;&nbsp;|</html:link>
	  </logic:present>
	  <logic:notPresent name="secroleForEdit">
		<html:link href="javascript:submitForm('saveNew');"	style="color:Navy;text-decoration:none;" onclick="javascript:setNoConfirm();">
		<img border=0 src=<html:rewrite page="/images/Save16.png"/> />&nbsp;Save&nbsp;&nbsp;|</html:link>
	  </logic:notPresent>
  </c:if>
  </td></tr>
<tr><td align="left" class="message"><logic:messagesPresent	message="true"><br />
	<html:messages id="message" message="true" bundle="pmm"><c:out escapeXml="false" value="${message}" /></html:messages>
	<br /></logic:messagesPresent></td></tr>
<tr><td align="left"></td></tr>
<tr><td>
    <div class="tabs" id="tabs">
   	 <table cellpadding="3" cellspacing="0" border="0">
	  <tr><th title="Programs">
	    <logic:present name="secroleForEdit">Edit Role</logic:present>
	    <logic:notPresent name="secroleForEdit">Add Role</logic:notPresent></th></tr>
	 </table>
	</div>
</td></tr>

<tr><td>
  <br />
  <table width="100%">
  <logic:present name="secroleForEdit">
	<tr><td>Role Name:</td>
	<td><html:text property="roleName" size="50" readonly="true" style="border: none" /></td></tr>
  </logic:present>
  
  <logic:notPresent name="secroleForEdit">
	<tr><td>Role Name:</td>
	<td><html:text property="roleName" size="50" maxlength="30" /></td></tr>
  </logic:notPresent>
  
  <tr><td>Description:</td>
  <td><html:text property="description" size="50" maxlength="60" /></td></tr>
  <tr><td>Active:</td>
  <td><html:checkbox property="active" /></td></tr>
  <tr><td colspan="2">&nbsp;</td></tr>
  <tr><td colspan="2">Functions:</td></tr>
  <tr><td colspan="2">
</table>
  </td></tr>
  
  <logic:present name="secroleForEdit">
<tr><td height="100%">  
     <div id="scrollBar" onscroll="getDivPosition()"
	   style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
        height: 100%; width: 100%; overflow: auto;">
        <table align="center" class="simple" width="100%">
		<tr style="background-color: #f0f0f0"><td align="center">Select</td>
		<td align="center">Function</td>
		<td align="center">AccessType</td></tr>
		<logic:iterate id="secobjprivilege" name="secroleForm" property="secobjprivilegeLst" indexId="rIndex">
		  <tr><td align="center" width="5%"><input type="hidden" name="lineno" value="<%=String.valueOf(rIndex)%>" />
 			<input type="checkbox" name="p<%=String.valueOf(rIndex)%>" value="" /></td>
		  <td width="60%">
			<table cellpadding="0" style="border:0px;" cellspacing="0" width="100%">
			  <tr><td style="border:0px;" width="1px">
				<input style="width:1px;" type="text"	name="function_code<%=String.valueOf(rIndex)%>"
					value='<c:out value="${secobjprivilege.objectname_code}"/>'></td>
			  <td style="border:0px;" width="94%">
				<input style="width:99%;" type="text" name="function_description<%=String.valueOf(rIndex)%>"
					value='<c:out value="${secobjprivilege.objectname_desc}"/>' readonly></td>
			  <td style="border:0px;" width="35px">
				<a href="javascript:void1();"
				onclick="return showLookup('FUN', '', '', 'secroleForm','function_code<%=String.valueOf(rIndex)%>','function_description<%=String.valueOf(rIndex)%>', true, '<c:out value="${ctx}"/>');">
				<img src="<c:out value="${ctx}"/>/images/microsoftsearch.gif" border="0"></a>
			 </td>
			  <td style="border:0px;" width="35px">
				<a href="javascript:void1();"
				onclick="return clearLookupValue('secroleForm','function_code<%=String.valueOf(rIndex)%>','function_description<%=String.valueOf(rIndex)%>');"><img
				src="<c:out value="${ctx}"/>/images/Reset16.gif" border="0"></a>											
				</td></tr>
			</table></td>
		  <td>
			<table cellpadding="0" style="border:0px;" cellspacing="0" width="100%">
			  <tr><td style="border:0px;" width="1px"><input type="text"
				style="width:1px;"	name="accessTypes_code<%=String.valueOf(rIndex)%>"
				value='<c:out value="${secobjprivilege.privilege_code}"/>'></td>
			  <td style="border:0px;" width="85%"><input	style="width:96%;" type="text"
				name="accessTypes_description<%=String.valueOf(rIndex)%>"
				value='<c:out value="${secobjprivilege.privilege_desc}"/>' readonly></td>
			  <td style="border:0px;" width="35px">
				<a href="javascript:void1();"
				onclick="return showLookup('PRV', '', '', 'secroleForm','accessTypes_code<%=String.valueOf(rIndex)%>','accessTypes_description<%=String.valueOf(rIndex)%>', true, '<c:out value="${ctx}"/>');">
				<img src="<c:out value="${ctx}"/>/images/microsoftsearch.gif" border="0"></a>
			  </td>
			  <td style="border:0px;" width="35px">
				<a href="javascript:void1();"
				onclick="return clearLookupValue('secroleForm','accessTypes_code<%=String.valueOf(rIndex)%>','accessTypes_description<%=String.valueOf(rIndex)%>');">
				<img src="<c:out value="${ctx}"/>/images/Reset16.gif" border="0"></a>				
				</td></tr>
			</table>
		  </td></tr>
		</logic:iterate>
		<tr><td colspan="2" class="clsButtonBarText" width="100%">&nbsp;&nbsp;			
			<c:if test="${!isReadOnly}" >
		   <a href="javascript:submitForm('addFunctionInEdit');" onclick="javascript:setNoConfirm();">Add</a>&nbsp;&nbsp;&nbsp;|
			&nbsp;&nbsp;<a href="javascript:submitForm('removeFunctionInEdit');" onclick="javascript:setNoConfirm();">Remove</a>
			</c:if>	
			</td></tr>
	  </table>
	</div>
	  </td></tr>
	</logic:present>
</table>
<%@ include file="/common/readonly.jsp" %>
</html:form>
<script language="javascript" type="text/javascript">
<!--
	function gotoRoleList(){
	 	window.open("<c:out value='${ctx}'/>/PMmodule/Admin/RoleManager.do?method=list", "_self") ;
	}
	function submitForm(mthd){
		trimInputBox();
		var roleName = document.getElementsByName("roleName")[0].value;
		roleName = trim(roleName);
		if(roleName.length == 0){
			alert("'Role Name' field can not be empty!");
			return;
		}
		if((mthd == "saveChange" || mthd=="saveNew") && noChanges())
		{
			alert("There are no changes detected to save");
		}
		else
		{
			document.forms[0].method.value=mthd;
			document.forms[0].submit();
		}
	}

	// trim leading and ending spaces
	function trim (str) {
		var	str = str.replace(/^\s\s*/, ''),
			ws = /\s/,
			i = str.length;
		while (ws.test(str.charAt(--i)));
		return str.slice(0, i + 1);
	}
//-->
</script>

