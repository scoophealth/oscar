<!-- 

Source:web/PMmodule/Admin/User/UserEdit.jsp 

-->

<%@ include file="/taglibs.jsp"%>


<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">Change Password</span></th>
	</tr>
	<tr height="18px">
		<td align="left" class="buttonBar2"><html:link
			action="/Home.do"
			style="color:Navy;text-decoration:none;">
			<img border=0 src=<html:rewrite page="/images/close16.png"/> />&nbsp;Close&nbsp;&nbsp;|</html:link>
			
			<html:link href="javascript:submitForm('savePassword');"
				style="color:Navy;text-decoration:none;">
				<img border=0 src=<html:rewrite page="/images/Save16.png"/> />&nbsp;Save&nbsp;&nbsp;|</html:link>
			
			
		</td>

	</tr>

	<tr>
		<td align="left" class="message">
			<br />
			<logic:messagesPresent
			message="true">
			<html:messages id="message" message="true" bundle="pmm">
				<c:out escapeXml="false" value="${message}" />
			</html:messages>
		</logic:messagesPresent>
		<br /></td>
	</tr>


	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">

		

		<html:form action="/PMmodule/Admin/UserManager" method="post">
			<html:hidden property="method" value="" />
			<html:hidden property="securityNo" />
			<html:hidden property="providerNo" />
			
			<div class="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="sinatures">Change Password</th>
				</tr>
			</table>
			</div>
			
			<table>


				<tr>
					<td>Old Password:</td>
					<td><html:password property="oldPassword" tabindex="1" maxlength="15"/></td>
					
				</tr>
				<tr>
					<td>New Password:</td>
					<td><html:password property="password" tabindex="2" maxlength="15"/></td>
					
				</tr>
				<tr>
					<td>Confirm New Password:</td>
					<td><html:password property="confirmPassword"  tabindex="3" maxlength="15"/></td>
				</tr>
			</table>
		</html:form></div>
		</td>
	</tr>
</table>
<script language="javascript" type="text/javascript">
<!--

function submitForm(func){
	trimInputBox();
	document.forms[0].method.value=func;

	var v1 = false;
	var v2 = false;
	var v3 = false;
	
	var fld_password = document.getElementsByName('password')[0];
	var fld_cPassword = document.getElementsByName('confirmPassword')[0];
	
	if ( validateRequired(fld_password, "New Password") && validateLength(fld_password, "Password", 15, 6)){
		v1 = true;
	}
	if ( validateRequired(fld_cPassword, "Confirm New Password") && validateLength(fld_cPassword, "Confirm Password", 15, 6)){
		v2 = true;
	}
	if(validatePwdMatch(fld_password, fld_cPassword)){
		v3 = true;
	}						
	if(v1 && v2 && v3){	
		if (fld_password.value == fld_cPassword.value) {
			document.forms[0].submit();
		}
	}
}

function validateRequired(field, fieldNameDisplayed ){
	field.value = trim(field.value);
	
	if (field.value == null || field.value == ''){
		alert('The field "' + fieldNameDisplayed + '" is required.');
		return(false);
	}
	
	return(true);
}

function validateLength(field, fieldNameDisplayed, maxlength, minlength){
	field.value = trim(field.value);
	
	if (minlength>0 && (field.value == null || field.value == '')){
		alert('The field "' + fieldNameDisplayed + '" should be ' + minlength + ' to ' + maxlength  + ' charactors long.');
		return(false);
	}
    
    if(field.value.length<minlength || field.value.length>maxlength){
		alert('The field "' + fieldNameDisplayed + '" should be ' + minlength + ' to ' + maxlength  + ' charactors long.');
		return(false);
	}
    	
	return(true);
}

function validatePwdMatch(field, cfield){
   if(trim(field.value)!=trim(cfield.value)){
	  alert('New password and Confirm New Password do not match.');
	  return(false);
   }
   return(true);
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

