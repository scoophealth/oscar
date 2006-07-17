<%@ include file="/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/tld/caisirole-tag.tld" prefix="caisirole"%>
<html>
<head>
<title>Case Management</title>
<link rel="stylesheet" href="css/casemgmt.css" type="text/css">
<script language="JavaScript" src="jspspellcheck/spellcheck-caller.js"></script>
<script type="text/javascript">
	var flag=<%=request.getAttribute("change_flag")%>;
 
	<%org.caisi.casemgmt.web.formbeans.CaseManagementEntryFormBean form=(org.caisi.casemgmt.web.formbeans.CaseManagementEntryFormBean) session.getAttribute("caseManagementEntryForm");
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
	
	function validateSave(){
	
		var str1="You cannot save a note when there is no issue checked, please add an issue or check a currently available issue before save." 
		var str2="Are you sure that you want to sign and save without changing the status of any of the issues?";
		var str3="Please choose encounter type before saving the note."
		if (!validateEnounter()){
			alert(str3); return false;
		}
		if (!validateIssuecheck()){
			alert(str1); return false;
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
                 startSpellCheck("jspspellcheck/",elements);
                
            }

</script>

</head>
<body onload="window.opener.location.reload(true);">
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
			property="issueCheckList" type="org.caisi.casemgmt.web.CheckBoxBean">
			<tr bgcolor="<%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>"
				align="center">
				<%String submitString = "this.form.method.value='issueChange';";
			submitString = submitString + "this.form.lineId.value=" + "'"
					+ ind.intValue() + "';" + "this.form.submit();";

			%>

				<td><nested:checkbox indexed="true" name="issueCheckList"
					property="checked" onchange="setChangeFlag(true);"></nested:checkbox></td>

				<td><nested:write name="issueCheckList"
					property="issue.issue.description" /></td>
				<td><nested:select indexed="true" name="issueCheckList"
					property="issue.acute" onchange="<%=submitString%>">
					<html:option value="true">acute</html:option>
					<html:option value="false">chronic</html:option>
				</nested:select></td>
				<td><nested:select indexed="true" name="issueCheckList"
					property="issue.certain" onchange="<%=submitString%>">
					<html:option value="true">certain</html:option>
					<html:option value="false">uncertain</html:option>
				</nested:select></td>
				<td><nested:select indexed="true" name="issueCheckList"
					property="issue.major" onchange="<%=submitString%>">
					<html:option value="true">major</html:option>
					<html:option value="false">not major</html:option>
				</nested:select></td>
				<td><nested:select indexed="true" name="issueCheckList"
					property="issue.resolved" onchange="<%=submitString%>">
					<html:option value="true">resolved</html:option>
					<html:option value="false">unresolved</html:option>
				</nested:select></td>
				<td><nested:text indexed="true" name="issueCheckList"
					property="issue.type" /></td>
				<td><nested:equal name="issueCheckList" property="used"
					value="false">
					<%submitString = "this.form.method.value='issueDelete';";
			submitString = submitString + "this.form.deleteId.value=" + "'"
					+ ind.intValue() + "';";
			//System.out.println("submitString="+submitString);

			%>
					<input type="submit" value="delete" onclick="<%=submitString%>">
				</nested:equal></td>
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
		<html:messages id="message" message="true">
			<I><c:out value="${message}" /></I>
		</html:messages>
	</logic:messagesPresent>
	</span>
	<%} %>
	
	<br>  <button type="button" onclick="javascript:spellCheck();">Spell Check</button>
	
	<p>
	<table width="90%" border="1">
		<tr>
			<td class="fieldValue" colspan="2"><textarea name="caseNote_note"
				cols="80" rows="20" wrap="hard" onchange="setChangeFlag(true);"><nested:write
				property="caseNote.note" /></textarea></td>
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
				
				<td><nested:text property="caseNote.billing_code" />
				<input type="button" value="add billing"
					onclick="self.open('<%=(String)session.getAttribute("billing_url")%>','','scrollbars=yes,menubars=no,toolbars=no,resizable=yes');return false;"></td>
			</tr>
		</caisirole:SecurityAccess>
		</c:if>
		<tr>
			<td class="fieldValue" colspan="2">
			<input type="submit" value="Save"
				onclick="this.form.method.value='save';return validateSave();"> 
			<input type="submit"
				value="Save and Exit" onclick="this.form.method.value='saveAndExit';if (validateSave()) {return true;}else return false;">

			</td>
			
		</tr>

	</table>
</nested:form>
</body>
</html>
