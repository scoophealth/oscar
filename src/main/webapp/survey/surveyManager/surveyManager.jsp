
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


<%
	boolean showMfhExport=false;
	oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
	String mfhExport = pros.getProperty("MFH_UFC_EXPORT");
	if(mfhExport != null && mfhExport.equalsIgnoreCase("true")) {
		showMfhExport=true;
	}
%>
<%@ include file="/survey/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<table width="100%">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" bundle="survey">
			<tr>
				<td colspan="3" class="message"><c:out value="${message}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>
	<logic:messagesPresent>
		<html:messages id="error" bundle="survey">
			<tr>
				<td colspan="3" class="error"><c:out value="${error}" /></td>
			</tr>
		</html:messages>
	</logic:messagesPresent>
</table>
<br />
<display:table cellspacing="2" cellpadding="3" id="entry" name="surveys"
	export="false" pagesize="10" requestURI="/SurveyManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list"
		value="No Forms found." />

	<display:column title="" style="white-space: nowrap; ">
		<c:if test="${entry.status eq 0}">
			<!--  in review -->
			<a
				href="<html:rewrite action="/SurveyManager"/>?method=edit&id=<c:out value="${entry.id}"/>"><img
				src="images/edit.png" border="0"></a>
  			&nbsp;
	  		<a
				href="<html:rewrite action="/SurveyManager"/>?method=delete&id=<c:out value="${entry.id}"/>"><img
				src="images/delete.png" border="0"></a>
		  	&nbsp;
	        <a
				href="<html:rewrite action="/SurveyManager"/>?method=test&id=<c:out value="${entry.id}"/>"><img
				src="images/test.png" border="0"></a>
		</c:if>
		<c:if test="${entry.status eq 1}">
			<!-- test -->
			<a
				href="<html:rewrite action="/SurveyManager"/>?method=edit&id=<c:out value="${entry.id}"/>"><img
				src="images/edit.png" border="0"></a>
  			&nbsp;
	  		<a
				href="<html:rewrite action="/SurveyManager"/>?method=delete&id=<c:out value="${entry.id}"/>"><img
				src="images/delete.png" border="0"></a>
		  	&nbsp;
			<a
				href="<html:rewrite action="/SurveyManager"/>?method=test&id=<c:out value="${entry.id}"/>"><img
				src="images/test.png" border="0"></a>
	    	&nbsp;
	    	<a
				href="<html:rewrite action="/SurveyManager"/>?method=clear_test_data&id=<c:out value="${entry.id}"/>"><img
				src="images/clear.png" border="0"></a>
	    	&nbsp;
	   		<a
				href="<html:rewrite action="/SurveyManager"/>?method=launch&id=<c:out value="${entry.id}"/>"><img
				src="images/launch.png" border="0"></a>
		</c:if>
		<c:if test="${entry.status eq 2}">
			<!-- launched -->
			<a
				href="<html:rewrite action="/SurveyManager"/>?method=close&id=<c:out value="${entry.id}"/>"><img
				src="images/close.png" border="0"></a>
	        &nbsp;
	        <a
				href="<html:rewrite action="/SurveyManager"/>?method=export&id=<c:out value="${entry.id}"/>"><img
				src="images/export.gif" border="0"></a>
		</c:if>
		<c:if test="${entry.status eq 3}">
			<!--  closed -->
			<a
				href="<html:rewrite action="/SurveyManager"/>?method=reopen&id=<c:out value="${entry.id}"/>"><img
				src="images/launch.png" border="0"></a>
	        &nbsp;
	        <a
				href="<html:rewrite action="/SurveyManager"/>?method=export&id=<c:out value="${entry.id}"/>"><img
				src="images/export.gif" border="0"></a>
		</c:if>

	</display:column>
	<display:column property="id" sortable="false" title="SurveyId" />
	<display:column style="width:60%" property="description"
		sortable="false" title="Description" />
	<display:column property="version" sortable="false" title="Version" />
	<display:column title="Status">
		<c:choose>
			<c:when test="${entry.status == 0}">IN REVIEW</c:when>
			<c:when test="${entry.status == 1}">TEST</c:when>
			<c:when test="${entry.status == 2}">LAUNCHED</c:when>
			<c:when test="${entry.status == 3}">CLOSED</c:when>
		</c:choose>
	</display:column>

</display:table>
<br />
<br />
<input type="button" value="Create New Form"
	onclick="location.href='<html:rewrite action="/SurveyManager"/>?method=new_survey'" />
<br />
<br />
<input type="button" value="Import"
	onclick="location.href='<html:rewrite action="/SurveyManager"/>?method=show_import_form'" />
<br />
<br />
<script>
	function export_csv(selectObj) {
		var formId = selectObj.options[selectObj.selectedIndex].value;
		if(formId != "") {
			//alert('<html:rewrite action="/SurveyManager"/>?method=export_csv&id=' + formId);	
			location.href='<html:rewrite action="/SurveyManager"/>?method=export_csv&id=' + formId;		
		}
		//run the command
		
		selectObj.selectedIndex=0;
	}

	function export_inverse_csv(selectObj) {
		var formId = selectObj.options[selectObj.selectedIndex].value;
		if(formId != "") {
			//alert('<html:rewrite action="/SurveyManager"/>?method=export_csv&id=' + formId);	
			location.href='<html:rewrite action="/SurveyManager"/>?method=export_inverse_csv&id=' + formId;		
		}
		//run the command
		
		selectObj.selectedIndex=0;
	}
		
	function export_to_db(selectObj) {
		var formId = selectObj.options[selectObj.selectedIndex].value;
		if(formId != "") {
			//alert('<html:rewrite action="/SurveyManager"/>?method=export_csv&id=' + formId);	
			location.href='<html:rewrite action="/SurveyManager"/>?method=export_to_db&id=' + formId;		
		}
		//run the command
		
		selectObj.selectedIndex=0;
	}	
</script>
Export Form Data:&nbsp;
<select onchange="export_csv(this);">
	<option value=""></option>
	<c:forEach var="f" items="${released_forms}">
		<option value="<c:out value="${f.id}"/>"><c:out
			value="${f.description}" /></option>
	</c:forEach>
</select>

<%if(showMfhExport) { %>
<br/><br/>
Export Form Data (MFH):&nbsp;
<select onchange="export_inverse_csv(this);">
	<option value=""></option>
<c:forEach var="f" items="${released_forms}">
	<option value="<c:out value="${f.id}"/>"><c:out value="${f.description}"/></option>
</c:forEach>
</select>
<% } %>

<br />
<br />
Export form structure to database:&nbsp;
<select onchange="export_to_db(this);">
	<option value=""></option>
	<c:forEach var="f" items="${released_forms}">
		<option value="<c:out value="${f.id}"/>"><c:out
			value="${f.description}" /></option>
	</c:forEach>
</select>
