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
<%@ include file="/taglibs.jsp"%>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="com.quatro.common.KeyConstants"  %>
<%
	String strLimit1 = "0";
	String strLimit2 = "18";
	if (request.getParameter("limit1") != null)
		strLimit1 = request.getParameter("limit1");
	if (request.getParameter("limit2") != null)
		strLimit2 = request.getParameter("limit2");
	String outcome = request.getParameter("outcome");
	boolean mergedSearch = false;
	if (request.getParameter("dboperation") != null
			&& request.getParameter("dboperation").equals(
			"demographic_search_merged"))
		mergedSearch = true;
	if (outcome != null) {
		if (outcome.equals("success")) {
%>
<script language="JavaScript">
            alert("Records merged successfully");
        </script>
<%
} else if (outcome.equals("failure")) {
%>
<script language="JavaScript">
            alert("Failed to merge records");
        </script>
<%
} else if (outcome.equals("successUnMerge")) {
%>
<script language="JavaScript">
            alert("Record(s) unmerged successfully");
        </script>
<%
} else if (outcome.equals("failureUnMerge")) {
%>
<script language="JavaScript">
            alert("Failed to unmerge records");
        </script>
<%
	}
	}
%>

<script>
	function resetClientFields() {
		var form = document.mergeClientForm;
		form.elements['criteria.demographicNo'].value='';
		form.elements['criteria.firstName'].value='';
		form.elements['criteria.lastName'].value='';
		form.elements['criteria.dob'].value='';
		// form.elements['criteria.healthCardNumber'].value='';
		// form.elements['criteria.healthCardVersion'].value='';
		// form.elements['criteria.searchOutsideDomain'].checked = true;
		// form.elements['criteria.searchUsingSoundex'].checked = true;
		// form.elements['criteria.dateFrom'].value=''; 
		// form.elements['criteria.dateTo'].value=''; 
		form.elements['criteria.bedProgramId'].selectedIndex = 0;
		form.elements['criteria.assignedToProviderNo'].selectedIndex = 0;
		form.elements['criteria.active'].selectedIndex = 0;
		form.elements['criteria.gender'].selectedIndex = 0;
	}

	function popupHelp(type) {
		alert('not yet implemented... will show term definitions');
	}
	function searchClicked()
	{
		if(deferSubmit) {
			setTimeout("submitForm('search')", 200);
		}
		else
		{
			submitForm("search");
		}
	}
	function submitForm(methodVal) {
		trimInputBox();
		if(!isDateValid) return;		
		document.forms[0].method.value = methodVal;
		document.forms[0].submit();
	}
	function setfocus() {
            document.titlesearch.keyword.focus();
            document.titlesearch.keyword.select();
        }
        
        function checkTypeIn() {
            var dob = document.titlesearch.keyword; typeInOK = true;          
            if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){
               document.titlesearch.keyword.value = dob.value.substring(8,18);
               document.titlesearch.search_mode[4].checked = true;             
            }

            if(document.titlesearch.search_mode[2].checked) {
              if(dob.value.length==8) {
                dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
              }
              if(dob.value.length != 10 || dob.value.indexOf(' ') > 0 ) {
                alert("Please format the date as yyyy-mm-dd");
                typeInOK = false;
              }
              return typeInOK;
            } else {
              return true;
            }
        }
        
        function UnMerge(){
            document.mergeform.mergeAction.value = "unmerge";
        }
        
        function searchMerged(){
            document.titlesearch.dboperation.value="demographic_search_merged";
        }
        
        function popupWindow(page) {
            windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
            if(win!=null) win.close();
            win = window.open(page, "labreport", windowprops);
            win.focus();
        }
				function init()
				{
					var form = document.forms[0];
					form.elements['criteria.demographicNo'].focus();
					form.onkeypress=function() {keypress(event);}
				}
				function keypress(event)
				{
					var keynum;
					if(window.event) // IE
			  		{
			  			keynum = event.keyCode;
			  		}
					else if(event.which) // Netscape/Firefox/Opera
			  		{
			  			keynum = event.which;
			  		}
					if (keynum==13) searchClicked();
				}
</script>
	<% String a="1"; %>
<html:form action="/PMmodule/MergeClient.do">
	<input type="hidden" name="method" />
	<input type="hidden" name="mergeAction" />
	<table width="100%" height="100%" cellpadding="1px" cellspacing="1px">
		<tr>
			<th class="pageTitle">Merge Search<c:out value="${moduleName}" /></th>
		</tr>
		<tr>
			<td class="buttonBar2" align="left" height="18px">
				<a	href="javascript:submitForm('search')"	style="color:Navy;text-decoration:none;"> 
					<img border=0	src=<html:rewrite page="/images/search16.gif"/> height="16px" 	width="16px" />&nbsp;Search&nbsp;&nbsp;|</a> 
				<a	style="color:Navy;text-decoration:none;" href="javascript:void1();" onclick="return deferedSubmit('mergedSearch');"> 
					<img border=0	src=<html:rewrite page="/images/search16.gif" /> height="16px"	width="16px" />&nbsp;Search Merged Records&nbsp;&nbsp;</a> 
				<a	style="color:Navy;text-decoration:none;" href="javascript:resetClientFields();"> 
					<img border=0	src=<html:rewrite page="/images/searchreset.gif" /> height="16px"	width="16px" />&nbsp;Reset&nbsp;&nbsp;|</a>
				<%
					String securityRole = "" + session.getAttribute("userrole") + "," + session.getAttribute("user");
				%>
				<security:oscarSec roleName="<%= securityRole %>" objectName="<%=KeyConstants.FUN_ADMIN_MERGECLIENT %>" rights="<%=KeyConstants.ACCESS_WRITE%>">
				<c:choose>
					<c:when test="${mergeAction eq 'unmerge'}">				
						<a	href="javascript:void1();" onclick="this.disabled=true;return deferedSubmit('unmerge')"	style="color:Navy;text-decoration:none;"> 				
							<img border=0	src=<html:rewrite page="/images/search16.gif"/> height="16px" 	width="16px" />&nbsp;Unmerge&nbsp;&nbsp;|</a> 	 
					</c:when>
					<c:otherwise>
						<a	href="javascript:void1();" onclick="this.disabled=true;return deferedSubmit('merge')"	style="color:Navy;text-decoration:none;"> 
							&nbsp;Merge&nbsp;&nbsp;|</a>
					</c:otherwise>					
				</c:choose>
				</security:oscarSec>
				<html:link action="/PMmodule/Admin/SysAdmin.do"  style="color:Navy;text-decoration:none;">				
					<img border=0 src=<html:rewrite page="/images/close16.png"/> />&nbsp;Close&nbsp;&nbsp;</html:link>
				</td>
		</tr>
		<tr height="18px">
			<td align="left" class="message">
			      <logic:messagesPresent message="true">
			        <html:messages id="message" message="true" bundle="pmm"><c:out escapeXml="false" value="${message}" />
			        </html:messages> 
			      </logic:messagesPresent>
			</td>
		</tr>
		<tr>
			<td>
			<div id="projecthome" class="app">
			<div class="axial">
			<table border="0" cellspacing="1" cellpadding="1" width="100%">
				<tr>
					<th align="right" width="20%"><bean-el:message key="ClientSearch.clientNo" bundle="pmm" /></th>
					<td align="left" width="80%"><html:text property="criteria.demographicNo" size="15" /></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="ClientSearch.firstName" bundle="pmm" /></th>
					<td align="left" width="80%"><html:text property="criteria.firstName" size="15" /></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="ClientSearch.lastName" bundle="pmm" />
					</th>
					<td align="left" width="80%"><html:text property="criteria.lastName" size="15" /></td>
				</tr>

				<tr>
					<th width="20%" align="right"><bean-el:message key="ClientSearch.dateOfBirth"  bundle="pmm"/> <br>
					(yyyy/mm/dd)</th>
					<th align="left" width="80%">
					<quatro:datePickerTag property="criteria.dob" openerForm="mergeClientForm" width="180px"></quatro:datePickerTag>
					</th>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="ClientSearch.active" bundle="pmm" /></th>
					<td align="left" width="80%"><html:select property="criteria.active">
						<html:option value="">Any</html:option>
						<html:option value="1">Yes</html:option>
						<html:option value="0">No</html:option>
					</html:select></td>
				</tr>
				<tr>
					<th align="right" width="20%"><bean-el:message key="ClientSearch.gender" bundle="pmm" /></th>
					<td align="left" width="80%"><html-el:select property="criteria.gender">
						<html-el:option value="">Any</html-el:option>
						<c:forEach var="gen" items="${genders}">
							<html-el:option value="${gen.code}">
								<c:out value="${gen.description}" />
							</html-el:option>
						</c:forEach>
					</html-el:select></td>
				</tr>
				<tr>
					<th  align="right" width="20%"><bean-el:message key="ClientSearch.assignedTo"  bundle="pmm"/> </th>
			          <th align="left" width="80%">
			            <html:select property="criteria.assignedToProviderNo">
			                <html:option value="">
			                </html:option>
			              	<html:options collection="allProviders" property="providerNo" labelProperty="fullName" />
			            </html:select>
			          </th>
				</tr>
				<tr>
					<th  align="right" width="20%"><bean-el:message key="ClientSearch.program"  bundle="pmm"/> </th>
			          <th align="left" width="80%" >
			            <html:select property="criteria.bedProgramId">
			                <html:option value="">
			                </html:option>
			                <html:option value="MyP">My Programs
			                </html:option>
			              	<html:options collection="allBedPrograms" property="id" labelProperty="name" />
			            </html:select>
			          </th>
				</tr>
			</table>
			</div>
			</div>
			</td>
		</tr>
	
		<c:if test="${requestScope.clients != null}">			
			<tr style="height: 100%">
				<td>
				<div style="color: Black; background-color: White; border-style: ridge; border-width: 1px;   width: 100%; height: 100%; overflow: auto">
				<c:choose>	
						<c:when test="${mergeAction eq 'unmerge'}">				
							<display:table class="simple" sort="list" cellspacing="2" cellpadding="3"	id="client" name="clients" export="false" pagesize="100"
							requestURI="/PMmodule/UnmergeClient.do" >
							<display:setProperty name="paging.banner.placement" value="bottom" />
							<display:setProperty name="basic.msg.empty_list"
								value="No clients found." />							
							<display:column title="">	
								<c:choose>
										<c:when test="${client.merged}">
										 	<input type="checkbox" name="records"	value="<c:out value='${client.demographicNo}'/>">
										</c:when>								
										<c:otherwise>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Merged to
										</c:otherwise>
							   	</c:choose>
							   	</display:column>
							<display:column sortable="true" title="Client No">
								<c:out	value="${client.demographicNo}" />
							</display:column>
							<display:column sortable="true" title="Name">
								<c:out	value="${client.formattedName}" />
							</display:column>
							<display:column sortable="true" title="Date of Birth">
								<c:out value="${client.dob}" />
							</display:column>
							<display:column sortable="true" title="Gender">
								<c:out value="${client.sexDesc}" />
							</display:column>
							<display:column sortable="true" title="Active">
								<logic:equal value="0" property="activeCount" name="client">No</logic:equal>
								<logic:notEqual value="0" property="activeCount" name="client">Yes</logic:notEqual>
							</display:column>					
						</display:table>
					</c:when>
					<c:otherwise>
							<display:table class="simple" sort="list" cellspacing="2" cellpadding="3"	id="client" name="clients" export="false"
							requestURI="/PMmodule/MergeClient.do" >
							<display:setProperty name="paging.banner.placement" value="bottom" />
							<display:setProperty name="basic.msg.empty_list"
								value="No clients found." />
							<display:column title="">										
								<c:choose>
										<c:when test="${client.merged}">
										 	&nbsp;
										</c:when>								
										<c:otherwise>
											<input type="checkbox" name="records"	value="<c:out value='${client.demographicNo}'/>">
										</c:otherwise>
							   	</c:choose>						
							</display:column>
							<display:column title="As main record">						
								<c:choose>
								<c:when test="${!client.merged}">
								 	<input type="radio" name="head"
											value="<c:out value='${client.demographicNo}'/>">
								</c:when>
								<c:otherwise>
											(merged)
								</c:otherwise>
								</c:choose>		
							</display:column>	
							<display:column sortable="true" title="Client No">
								<c:out	value="${client.demographicNo}" />
							</display:column>
							<display:column sortable="true" title="Name">
								<c:out	value="${client.formattedName}" />
							</display:column>
							<display:column sortable="true" title="Date of Birth">
								<c:out value="${client.dob}" />
							</display:column>
							<display:column sortable="true" title="Gender">
								<c:out value="${client.sexDesc}" />
							</display:column>
							<display:column sortable="true" title="Active">
								<logic:equal value="0" property="activeCount" name="client">No</logic:equal>
								<logic:notEqual value="0" property="activeCount" name="client">Yes</logic:notEqual>
							</display:column>					
						</display:table>						
					</c:otherwise>
				</c:choose>		
				</div>
				</td>
			</tr>
	</table>	
	</c:if>
	</html:form>
