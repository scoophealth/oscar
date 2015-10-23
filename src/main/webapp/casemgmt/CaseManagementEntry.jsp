
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%-- Updated by Eugene Petruhin on 24 dec 2008 while fixing #2459538 --%>
<%-- Updated by Eugene Petruhin on 09 jan 2009 while fixing #2482832 & #2494061 --%>

<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    boolean showResolved=false;
    try
    {
    	showResolved=Boolean.parseBoolean(request.getParameter("showResolved"));
    }
    catch (NullPointerException e)
    {
    	// do nothing it's okay to not have this parameter
    }
    int count_issues_display=0;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Case Management</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
<script language="JavaScript" src="<c:out value="${ctx}"/>/jspspellcheck/spellcheck-caller.js"></script>
<script type="text/javascript">
	var flag=<%=request.getAttribute("change_flag")%>;                
 
	<%
        
        String demographicNo = request.getParameter("demographicNo");
        String sessionFrmName = "caseManagementEntryForm" + demographicNo;
        org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean form=(org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean) session.getAttribute(sessionFrmName);
        request.setAttribute("caseManagementEntryForm", form);
        
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
	function validateIssuecheck(issueSize){
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
		
	function validateSave(count_issues_display){
	
		var str1="You cannot save a note when there is no issue checked, please add an issue or check a currently available issue before save." ;
		var str2="Are you sure that you want to sign and save without changing the status of any of the issues?";
		var str3="Please choose encounter type before saving the note."
		var str4="Are you sure that you want to save without signing?";
		if (!validateEnounter()){
			alert(str3); return false;
		}
		if (!validateIssuecheck(count_issues_display)){ 
			alert(str1); return false;
		}
		if (!validateSignStatus()){
			if(!confirm(str4)) return false;
		}
			
		
		<oscarProp:oscarPropertiesCheck property="oncall" value="yes">		
			var s = document.caseManagementEntryForm.elements['caseNote.encounter_type'];		
			if(s.options[s.selectedIndex].value == 'telephone encounter weekdays 8am-6pm' || s.options[s.selectedIndex].value == 'telephone encounter weekends or 6pm-8am') {
				document.caseManagementEntryForm.elements['chain'].value='/OnCallQuestionnaire.do?method=form&providerNo='+ document.caseManagementEntryForm.providerNo.value + '&type=' + s.options[s.selectedIndex].value;		
			}				
		</oscarProp:oscarPropertiesCheck>
		return true;
	}

	function toggleGroupNote(el) {
		var checked = el.checked;
		if(checked == true) {
			alert('show group dialog');	
		}
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
			var demographicNo = '<c:out value="${param.demographicNo}"/>';
			var noteId = '<%=request.getParameter("noteId") != null ? request.getParameter("noteId") : request.getAttribute("noteId") != null ? request.getAttribute("noteId") : ""%>';
			var programId = '<c:out value="${case_program_id}"/>';
			XMLHttpRequestObject.send("method=autosave&demographicNo=" + demographicNo + "&programId=" + programId + "&note_id=" + noteId + "&note="  + escape(obj.value));
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
		if(confirm('You have an unsaved note from a previous session.  Click ok to retrieve note.')) {
			document.caseManagementEntryForm.method.value='restore';
			document.caseManagementEntryForm.submit();			
		}		
	}	
</script>

</head>
<body onload="init()">
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u">
<%//get programId
String pId=(String)session.getAttribute("case_program_id");
if (pId==null) pId="";
%>
<nested:form action="/CaseManagementEntry">
	<html:hidden property="chain"/>
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

<b><bean:message key="casemanagementEntry.clientname" /> 
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

	<b><bean:message key="casemanagementEntry.issueassociationview" /></b>

	<table width="90%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr class="title">
			<td></td>
			<td><bean:message key="casemanagementEntry.Issue" /></td>
			<td><bean:message key="casemanagementEntry.Acute" /></td>
			<td><bean:message key="casemanagementEntry.Certain" /></td>
			<td><bean:message key="casemanagementEntry.Major" /></td>
			<td><bean:message key="casemanagementEntry.Resolved" /></td>
			<td><bean:message key="casemanagementEntry.Type" /></td>
			<td></td>
		</tr>

		<nested:iterate indexId="ind" id="issueCheckList" property="issueCheckList" type="org.oscarehr.casemgmt.web.CheckBoxBean">
			<%String submitString = "this.form.method.value='issueChange';";
					submitString = submitString + "this.form.lineId.value=" + "'"
				+ ind.intValue() + "';" + "this.form.submit();";

			org.oscarehr.casemgmt.web.CheckBoxBean cbb = (org.oscarehr.casemgmt.web.CheckBoxBean)pageContext.getAttribute("issueCheckList");
			boolean writeAccess = cbb.getIssueDisplay().isWriteAccess();
			boolean disabled = !"local".equals(cbb.getIssueDisplay().location) ? true : !writeAccess;
			boolean checkBoxDisabled=!"local".equals(cbb.getIssueDisplay().location)? false : disabled;
			boolean resolved="resolved".equals(cbb.getIssueDisplay().resolved);
			int counter=0;
			
			if (!resolved || showResolved)
			{
				count_issues_display++;
				counter++;
			%>

				<tr bgcolor="<%= (counter%2==0)?"#EEEEFF":"white" %>"
					align="center">
					<td>
						<nested:checkbox indexed="true" name="issueCheckList" property="checked" onchange="setChangeFlag(true);" disabled="<%=checkBoxDisabled%>"></nested:checkbox>
					</td>
						<td <%="allergy".equals(cbb.getIssueDisplay().priority)?"bgcolor=\"yellow\"":""%>>
							<nested:write name="issueCheckList"	property="issueDisplay.description" />
						</td>
					<td>
						<nested:select indexed="true" name="issueCheckList"	property="issueDisplay.acute" disabled="<%=disabled%>">
							<html:option value="acute">acute</html:option>
							<html:option value="chronic">chronic</html:option>
						</nested:select>	
					</td>
					<td>
						<nested:select indexed="true" name="issueCheckList" property="issueDisplay.certain"  disabled="<%=disabled%>">
							<html:option value="certain">certain</html:option>
							<html:option value="uncertain">uncertain</html:option>
						</nested:select>
					</td>
					<td>
						<nested:select indexed="true" name="issueCheckList"	property="issueDisplay.major" disabled="<%=disabled%>">
							<html:option value="major">major</html:option>
							<html:option value="not major">not major</html:option>
						</nested:select>
					</td>				
					<td>
						<!-- removed onchange="<%=submitString%>" before disabled="<%=disabled %>" FOR THE ABOVE LINEs in this table -->
						 <nested:select indexed="true" name="issueCheckList" property="issueDisplay.resolved"  disabled="<%=disabled%>">										 	
							<html:option value="resolved">resolved</html:option>
							<html:option value="unresolved">unresolved</html:option>
						</nested:select>
					</td>
					<td>
						<nested:text indexed="true" name="issueCheckList" property="issueDisplay.role" disabled="<%=disabled%>"/>
					</td>
					<td>
	<% if (cbb.getIssueDisplay().location!=null && cbb.getIssueDisplay().location.equals("local")){ %>
				<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.issues" rights="u">
					<nested:equal name="issueCheckList" property="used"
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
						<input type="submit" value="Change Issue" onclick="<%=submitString%>">
				</security:oscarSec>
	<%}
	else{%>
						<bean:message key="casemanagementEntry.activecommunityissue" />
	<%}%>
					</td>
				</tr>
			<%
			}
			%>
		</nested:iterate>

	</table>
	<br>
	<br>
	<%
		if (showResolved)
		{
			%>
				<input id="hideResolved" type="button" value="Hide Resolved Issues" onclick="document.location=document.location.href.replace('&amp;showResolved=true','')" />
			<%
		}
		else
		{
			%>	
			<input id="showResolved" type="button" value="Show Resolved Issues" onclick="document.location='CaseManagementEntry.do?method=edit&note_edit=new&from=casemgmt&demographicNo=<%=request.getParameter("demographicNo")%>&providerNo=<%=request.getParameter("providerNo")%>&showResolved=true'" />
			<%
		}
	%>
	<br>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.issues" rights="w">
		<nested:submit value="add new issue" onclick="this.form.method.value='addNewIssue';" />
	</security:oscarSec>

	<p><b><bean:message key="casemanagementEntry.progressnoteentryview" /> </b></p>
	<%if ("true".equalsIgnoreCase((String)request.getAttribute("change_flag"))) {%>
	<span id="spanMsg" style="color:red"><bean:message key="casemanagementEntry.notenotsavedyet" /></span>
	<%}else{%>
	<span id="spanMsg" style="color:blue">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" bundle="casemgmt">
			<I><c:out value="${message}" /></I>
		</html:messages>
	</logic:messagesPresent>
	</span>
	<%} %>
	
	<br>  <button type="button" onclick="javascript:spellCheck();"><bean:message key="casemanagementEntry.spellcheck" /></button>
	
	<p>
	<table width="90%" border="1">
		<tr>
			<td class="fieldValue" colspan="1">
				<textarea name="caseNote_note" id="caseNote_note" cols="60" rows="20" wrap="hard" onchange="setChangeFlag(true);"><nested:write property="caseNote.note"/></textarea>
			</td>
			<td class="fieldTitle"></td>
 
		</tr>

		<tr>    
			<td class="fieldTitle"><bean:message key="casemanagementEntry.encountertype" /></td>
			<td class="fieldValue"><html:select
				property="caseNote.encounter_type" onchange="setChangeFlag(true);">
				<html:option value=""></html:option>>
				<html:option value="face to face encounter with client"><bean:message key="casemanagementEntry.facetofaceencounterwithclient" /></html:option>>
				<oscarProp:oscarPropertiesCheck property="oncall" value="yes" reverse="true">
					<html:option value="telephone encounter with client"><bean:message key="casemanagementEntry.telephoneencounterwithclient" /></html:option>
				</oscarProp:oscarPropertiesCheck>
				<oscarProp:oscarPropertiesCheck property="oncall" value="yes">
					<html:option value="telephone encounter weekdays 8am-6pm"><bean:message key="casemanagementEntry.telephoneencounterweekdays" /></html:option>
					<html:option value="telephone encounter weekends or 6pm-8am"><bean:message key="casemanagementEntry.telephoneencounterweekends" /></html:option>
				</oscarProp:oscarPropertiesCheck>
				<html:option value="encounter without client"><bean:message key="casemanagementEntry.encounterwithoutclient" /></html:option>
			</html:select></td>
		</tr>

		<!-- tr>
		<td class="fieldTitle">Billing Code:</td>
		<td class="fieldValue"><html:text property="caseNote.billing_code" /><input type="button" value="search"/></td>
	</tr -->

		<tr>
			<td class="fieldTitle"><bean:message key="casemanagementEntry.Sign" /></td>
			<td class="fieldValue"><html:checkbox property="sign" onchange="setChangeFlag(true);" /></td>
		</tr>

		<tr>
			<td class="fieldTitle"><bean:message key="casemanagementEntry.includecheckedissuesinnote" /></td>
			<td class="fieldValue"><html:checkbox property="includeIssue" onchange="setChangeFlag(true);" /></td>
		</tr>
<!-- commented out on Oct 4, 2010
		<tr>
			<td class="fieldTitle">Group Note</td>
			<td class="fieldValue"><html:checkbox property="groupNote" onchange="setChangeFlag(true);toggleGroupNote(this);" /></td>
		</tr>
 -->	
      <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<c:if test="${param.from=='casemgmt' || requestScope.from=='casemgmt'}" >
		<c:url value="${sessionScope.billing_url}" var="url"/>
		<caisirole:SecurityAccess accessName="billing" accessType="access" providerNo='<%=request.getParameter("providerNo")%>' demoNo='<%=request.getParameter("demographicNo")%>' programId="<%=pId%>">
			<tr>
				<td class="fieldTitle"><bean:message key="casemanagementEntry.billing" /></td>
				
				<td class="fieldValue"><nested:text property="caseNote.billing_code" />
				<input type="button" value="add billing"
					onclick="self.open('<%=(String)session.getAttribute("billing_url")%>','','scrollbars=yes,menubars=no,toolbars=no,resizable=yes');return false;"></td>
			</tr>
		</caisirole:SecurityAccess>
		</c:if>		
	 </caisi:isModuleLoad>
		
		<caisi:isModuleLoad moduleName="casemgmt.note.password.enabled">
		<tr>
			<td class="fieldTitle"><bean:message key="casemanagementEntry.password" /></td>
			<td class="fieldValue"><html:password property="caseNote.password"/></td>
		</tr>
		</caisi:isModuleLoad>
		<tr>
			<td class="fieldValue" colspan="2">
			<input type="submit" value="Save"
				onclick="this.form.method.value='save';return validateSave(<%=count_issues_display%>);"> 
			<input type="submit"
				value="Save and Exit" onclick="this.form.method.value='saveAndExit';if (validateSave(<%=count_issues_display%>)) {return true;}else return false;">
			<input type="submit" 
			    value="cancel" onclick = "this.form.method.value='cancel';return true;">
			</td>
			
		</tr>

	</table>
</nested:form>

</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="u" reverse="true">
<b><bean:message key="casemanagementEntry.encounterwithoutclient" />You do not have permission to edit this note.</b>
</security:oscarSec>
</body>
</html>
