<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/casemgmt/taglibs.jsp" %>

<html>
<head>
<title>Case Management</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
<script language="JavaScript" src="<c:out value="${ctx}"/>/jspspellcheck/spellcheck-caller.js"></script>
<script type="text/javascript">
	var flag=<%=request.getAttribute("change_flag")%>;
 
	<%org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean form=(org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean) session.getAttribute("caseManagementEntryForm");
	int size=form.getIssueCheckList().length;
	
	if (session.getAttribute("newNote")!=null && "true".equalsIgnoreCase((String)session.getAttribute("newNote")))
	{%>
	var newNote=true;
	<%}else{%>
	var newNote=false;
	<%}
	
	if (session.getAttribute("issueStatusChanged")!=null && "true".equalsIgnoreCase((String)session.getAttribute("issueStatusChanged")))
	{%>
	var issueChanged=true;
	<%}else{%>
	var issueChanged=false;
	<%}%>
	
	var issueSize=<%=size%>;
	function setChangeFlag(change){
		flag=change;
		document.getElementById("spanMsg").innerHTML="This note has not been saved yet!";
		document.getElementById("spanMsg").style.color="red";
	}
	
	function validateChange(){
		var str="You haven't saved the change yet. Please save first.";
		if (flag==true){
			/*if (confirm(str)) return true;
			else return false;*/
			alert(str);
			return false;
		}
		return true;
	}
	
	function validateBack(){
		var str="You haven't saved the change yet. Please save first.";
		if (flag==true){
			/*if (confirm(str)) return true;
			else return false;*/
			alert(str);
			return false;
		}else{
			return true;
		}
	}
	function validateIssuecheck(){
		var i=0;
		for (i=0;i<issueSize;i++)
		{
			//alert("checked="+document.caseManagementEntryForm.elements["issueCheckList["+i+"].checked"].checked);
			if (document.caseManagementEntryForm.elements["issueCheckList["+i+"].checked"].checked) 
			{
				//alert("issue check return true");
				return true;
			}
		}
		return false;
			
	}
	function validateEnounter(){
		if (document.caseManagementEntryForm.elements["caseNote.encounter_type"].value=="" ||document.caseManagementEntryForm.elements["caseNote.encounter_type"].value==" ")
		{
			return false;
		}else{
		 	return true;
		}	
	}
	
	function validateIssueStatus(){
		var signed=false;
		
		if (document.caseManagementEntryForm.sign.checked) signed=true;
		
		if (newNote==true && signed==true){
			var i=0;
			/*for (i=0;i<issueSize;i++)
			{
				if (document.caseManagementEntryForm.elements["issueCheckList["+i+"].issue.acute"].value=="true") return true;
				if (document.caseManagementEntryForm.elements["issueCheckList["+i+"].issue.certain"].value=="true") return true;
				if (document.caseManagementEntryForm.elements["issueCheckList["+i+"].issue.major"].value=="true") return true;
				if (document.caseManagementEntryForm.elements["issueCheckList["+i+"].issue.resolved"].value=="true") return true;
			}*/
			if (issueChanged==true) return true;
			else return false;
		}
		return true;
	}
	
	function validateSignStatus() {
		if(document.caseManagementEntryForm.sign.checked) 
			return true;
		else 
			return false;
	}
	
	function validateSave(){
	
		var str1="You cannot save a note when there is no issue checked, please add an issue or check a currently available issue before save." ;
		var str2="Are you sure that you want to sign and save without changing the status of any of the issues?";
		var str3="Please choose encounter type before saving the note."
		var str4="Are you sure that you want to save without signing?";
		if (!validateEnounter()){
			alert(str3); return false;
		}
		if (!validateIssuecheck()){
			alert(str1); return false;
		}
		if (!validateSignStatus()){
			if(confirm(str4)) return true;
			else return false;
		}
		if (!validateIssueStatus())
			if (confirm(str2)) return true;
			else return false;
		return true;
	}

</script>

<script language="JavaScript">

 	function spellCheck()
            {
            
                // Build an array of form elements (not there values)
                var elements = new Array(0);
                
                // Your form elements that you want to have spell checked
                
                elements[elements.length] = document.caseManagementEntryForm.caseNote_note;
                
                       
                // Start the spell checker
                 startSpellCheck('jspspellcheck/',elements);
                
            }

</script>

<script>
var XMLHttpRequestObject = false;

 if(window.XMLHttpRequest) {
        XMLHttpRequestObject = new XMLHttpRequest();
 } else if(window.ActiveXObject) {
        XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
 }
 
 	function autoSave() {
		if(XMLHttpRequestObject) {
			var obj = document.getElementById('caseNote_note');
			XMLHttpRequestObject.open("POST",'<html:rewrite action="/CaseManagementEntry"/>',true);
            XMLHttpRequestObject.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
            /*
			XMLHttpRequestObject.onreadystatechange = function()
			{
				if(XMLHttpRequestObject.readyState == 4 &&
						XMLHttpRequestObject.status == 200) {
					alert('saved');
                }
			}
			*/
			var demographicNo = '<c:out value="${demographicNo}"/>';
			var programId = '<c:out value="${case_program_id}"/>';
			XMLHttpRequestObject.send("method=autosave&demographicNo=" + demographicNo + "&programId=" + programId + "&note="  + escape(obj.value));
						
		}	
		
		setTimer();	
	}
	
	function setTimer() {
		setTimeout("autoSave()", 60000);
	}
	
	function init() {
		setTimer();
		window.opener.location.reload(true);
	}

	function restore() {
		if(confirm('The system has detected an unsaved note for this client. By continuing, your current note will be replaced by the existing note in the system')) {
			document.caseManagementEntryForm.method.value='restore';
			document.caseManagementEntryForm.submit();			
		}		
	}	
</script>

</head>
<body onload="init()">
<%//get programId
String pId=(String)session.getAttribute("case_program_id");
if (pId==null) pId="";
%>
<nested:form action="/CaseManagementEntry">
	<html:hidden property="demographicNo"/>
	<c:if test="${param.providerNo==null}">
	<input type="hidden" name="providerNo" value="<%=session.getAttribute("user")%>">
	</c:if>
	<c:if test="${param.providerNo!=null}">
	<html:hidden property="providerNo"/>
	</c:if>
	<input type="hidden" name="caseNote.program_no" value="<%=pId%>"/>
	<input type="hidden" name="method" value="save" />
	<c:if test="${param.from=='casemgmt'||requestScope.from=='casemgmt'}">
	<input type="hidden" name="from" value="casemgmt" />
	</c:if>
	<input type="hidden" name="lineId" value="0" />
	<input type="hidden" name="addIssue" value="null" />
	<input type="hidden" name="deleteId" value="0" />

<b>Client name: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoName}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoName}" />
</logic:empty>
</I>
<br>
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Age: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoAge}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoAge}" />
</logic:empty>
</I>
<br>
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; DOB: 
<I>
<logic:notEmpty name="demoName" scope="request">
<c:out value="${requestScope.demoDOB}" />
</logic:notEmpty>
<logic:empty name="demoName" scope="request">
<c:out value="${param.demoDOB}" />
</logic:empty>
</I></b>
<br><br>




	<b>Issue Association View:</b>

	<table width="90%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr class="title">
			<td></td>
			<td>Issue</td>
			<td>Acute</td>
			<td>Certain</td>
			<td>Major</td>
			<td>Resolved</td>
			<td>Type</td>
			<td></td>
		</tr>

		<nested:iterate indexId="ind" id="issueCheckList"
			property="issueCheckList" type="org.oscarehr.casemgmt.web.CheckBoxBean">
			<tr bgcolor="<%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>"
				align="center">
				<%String submitString = "this.form.method.value='issueChange';";
						submitString = submitString + "this.form.lineId.value=" + "'"
					+ ind.intValue() + "';" + "this.form.submit();";
				%>
				<%
				org.oscarehr.casemgmt.web.CheckBoxBean cbb = (org.oscarehr.casemgmt.web.CheckBoxBean)pageContext.getAttribute("issueCheckList");
				boolean writeAccess = cbb.getIssue().isWriteAccess();
				boolean disabled = !writeAccess;
				%>
				<td>
					<nested:checkbox indexed="true" name="issueCheckList" property="checked" onchange="setChangeFlag(true);" disabled="<%=disabled%>"></nested:checkbox>
				</td>
				<td>
				
				</td>				
					<logic:equal name="issueCheckList" property="issue.issue.priority" value="allergy">
						<td bgcolor="yellow">
							<nested:write name="issueCheckList"	property="issue.issue.description" />
						</td>
					</logic:equal>
					<logic:notEqual name="issueCheckList" property="issue.issue.priority" value="allergy">
						<td>
							<nested:write name="issueCheckList"	property="issue.issue.description" />
						</td>
					</logic:notEqual>
				<td>
					<nested:select indexed="true" name="issueCheckList"	property="issue.acute" onchange="<%=submitString%>" disabled="<%=disabled%>">
						<html:option value="true">acute</html:option>
						<html:option value="false">chronic</html:option>
					</nested:select>	
				</td>
				<td>
					<nested:select indexed="true" name="issueCheckList" property="issue.certain" onchange="<%=submitString%>" disabled="<%=disabled%>">
						<html:option value="true">certain</html:option>
						<html:option value="false">uncertain</html:option>
					</nested:select>
				</td>
				<td>
					<nested:select indexed="true" name="issueCheckList"	property="issue.major" onchange="<%=submitString%>" disabled="<%=disabled%>">
						<html:option value="true">major</html:option>
						<html:option value="false">not major</html:option>
					</nested:select>
				</td>
				<td>
					<nested:select indexed="true" name="issueCheckList" property="issue.resolved" onchange="<%=submitString%>" disabled="<%=disabled%>">
						<html:option value="true">resolved</html:option>
						<html:option value="false">unresolved</html:option>
					</nested:select>
				</td>
				<td>
					<nested:text indexed="true" name="issueCheckList" property="issue.type" disabled="<%=disabled%>"/>
				</td>
				<td><nested:equal name="issueCheckList" property="used"
					value="false">
					<%submitString = "this.form.method.value='issueDelete';";
			submitString = submitString + "this.form.deleteId.value=" + "'"
					+ ind.intValue() + "';";
			%>
					<input type="submit" value="delete" onclick="<%=submitString%>">
				</nested:equal>
				
					<!--  change diagnosis button -->
					<%submitString = "this.form.method.value='changeDiagnosis';";
			submitString = submitString + "this.form.deleteId.value=" + "'"
					+ ind.intValue() + "';";
			%>					
					<input type="submit" value="Change Diagnosis" onclick="<%=submitString%>">
				</td>
			</tr>
		</nested:iterate>
	</table>

	<br>
	<br>
	<nested:submit value="add new issue"
		onclick="this.form.method.value='addNewIssue';" />


	<p><b>Progress Note Entry View </b></p>
	<%if ("true".equalsIgnoreCase((String)request.getAttribute("change_flag"))) {%>
	<span style="color:red">this note has not been saved yet!</span>
	<%}else{%>
	<span id="spanMsg" style="color:blue">
	
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" bundle="casemgmt">
			<I><c:out value="${message}" /></I>
		</html:messages>
	</logic:messagesPresent>
	</span>
	<%} %>
	
	<br>  <button type="button" onclick="javascript:spellCheck();">Spell Check</button>
	
	<p>
	<table width="90%" border="1">
		<tr>
			<td class="fieldValue" colspan="1">
				<textarea name="caseNote_note" id="caseNote_note" cols="60" rows="20" wrap="hard" onchange="setChangeFlag(true);"><nested:write property="caseNote.note"/></textarea>
			</td>
			<td class="fieldTitle"></td>
 
		</tr>

		<tr>    
			<td class="fieldTitle">Encounter Type:</td>
			<td class="fieldValue"><html:select
				property="caseNote.encounter_type" onchange="setChangeFlag(true);">
				<html:option value=""></html:option>>
				<html:option value="face to face encounter with client">face to face encounter with client</html:option>>
				<html:option value="telephone encounter with client">telephone encounter with client</html:option>>
				<html:option value="encounter without client">encounter without client</html:option>
			</html:select></td>
		</tr>

		<!-- tr>
		<td class="fieldTitle">Billing Code:</td>
		<td class="fieldValue"><html:text property="caseNote.billing_code" /><input type="button" value="search"/></td>
	</tr -->

		<tr>
			<td class="fieldTitle">Sign</td>
			<td class="fieldValue"><html:checkbox property="sign" onchange="setChangeFlag(true);" /></td>
		</tr>

		<tr>
			<td class="fieldTitle">include checked issues in note</td>
			<td class="fieldValue"><html:checkbox property="includeIssue" onchange="setChangeFlag(true);" /></td>
		</tr>

		<c:if test="${param.from=='casemgmt' || requestScope.from=='casemgmt'}" >
		<c:url value="${sessionScope.billing_url}" var="url"/>
		<caisirole:SecurityAccess accessName="billing" accessType="access" providerNo="<%=request.getParameter("providerNo")%>" demoNo="<%=request.getParameter("demographicNo")%>" programId="<%=pId%>">
			<tr>
				<td class="fieldTitle">Billing:</td>
				
				<td class="fieldValue"><nested:text property="caseNote.billing_code" />
				<input type="button" value="add billing"
					onclick="self.open('<%=(String)session.getAttribute("billing_url")%>','','scrollbars=yes,menubars=no,toolbars=no,resizable=yes');return false;"></td>
			</tr>
		</caisirole:SecurityAccess>
		</c:if>
		<c:if test="${requestScope.passwordEnabled=='true'}">
		<tr>
			<td class="fieldTitle">Password:</td>
			<td class="fieldValue"><html:password property="caseNote.password"/></td>
		</tr>
		</c:if>
		<tr>
			<td class="fieldValue" colspan="2">
			<input type="submit" value="Save"
				onclick="this.form.method.value='save';return validateSave();"> 
			<input type="submit"
				value="Save and Exit" onclick="this.form.method.value='saveAndExit';if (validateSave()) {return true;}else return false;">
			<input type="submit" 
			    value="cancel" onclick = "this.form.method.value='cancel';return true;">
			</td>
			
		</tr>

	</table>
</nested:form>
</body>
</html>
