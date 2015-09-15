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
<%-- Updated by Eugene Petruhin on 20 feb 2009 while fixing check_date() error --%>

<%@ include file="/ticklerPlus/header.jsp"%>

<%@ page import="java.util.*"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.oscarehr.common.model.Provider"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	Calendar rightNow = Calendar.getInstance();              
 	int year = rightNow.get(Calendar.YEAR);
 	int month = rightNow.get(Calendar.MONTH)+1;
 	int day = rightNow.get(Calendar.DAY_OF_MONTH);
 	String formattedDate = year + "-" + month + "-" + day;
%>
<script type="text/javascript" src="../js/checkDate.js"></script>
<script>	
		function search_demographic() {
			var popup = window.open('<c:out value="${ctx}"/>/ticklerPlus/demographicSearch.jsp?form=customFilterForm&elementName=filter.demographic_webName&elementId=filter.demographic_no&query=' + document.customFilterForm.elements['filter.demographic_webName'].value,'demographic_search');
			
			if (popup != null) {
    			if (popup.opener == null) {
      			popup.opener = self;
    			}
    			popup.focus();
  			}	
		}

		function check_custom_filter_date() {
			return check_date('filter.startDate') && check_date('filter.endDate');
		}
		
		function validateCustomFilterForm(form) {
			if(form.elements['filter.name'].value == '') {
				alert('You must provider a filter name.');
				return false;
			}
			if(form.elements['filter.demographic_webName'].value != '' &&
			 	(form.elements['filter.demographic_no'].value == '' || form.elements['filter.demographic_no'].value == '0')) {
				alert('You should provide patient information by using the search button');
				return false;
			}
						
			if (form.elements['filter.startDate'].value == '') {
				alert('You must provide a valid start date');
				return false;
			}
			
			if (form.elements['filter.endDate'].value == '') {
				alert('You must provide a valid end date');
				return false;
			}
			
			return check_custom_filter_date();
		}
		
	</script>


<tr>
	<c:choose>
		<c:when test="${not empty custom_filter.name}">
			<td class="searchTitle" colspan="4">Update Custom Filter</td>
		</c:when>
		<c:otherwise>
			<td class="searchTitle" colspan="4">Create New Custom Filter</td>
		</c:otherwise>
	</c:choose>
</tr>
</table>

<%@ include file="messages.jsp"%>

<br />

<table width="60%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">

	<html:form action="/CustomFilter"
		onsubmit="return validateCustomFilterForm(this);">

		<input type="hidden" name="method" value="save" />

		<c:if test="${not empty custom_filter.name}">
			<html:hidden property="filter.id" />
		</c:if>
		<tr>
			<td class="fieldTitle">Filter Name:</td>
			<td class="fieldValue"><c:choose>
				<c:when test="${custom_filter.name == '*Myticklers*'}">
                      	*Myticklers*
                      	 <html:hidden property="filter.name" />
				</c:when>
				<c:otherwise>
					<html:text property="filter.name" maxlength="255" />
				</c:otherwise>
			</c:choose></td>
		</tr>
		<tr>
			<td class="fieldTitle">Demographic:</td>
			<td class="fieldValue"><html:hidden
				property="filter.demographic_no" /> <html:text
				property="filter.demographic_webName" maxlength="60" /> <input
				type="button" value="Search" onclick="search_demographic();" /></td>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">Start Date:</td>
			<td class="fieldValue"><html:text property="filter.startDate"
				maxlength="10" /> <span
				onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=customFilterForm&amp;openerElement=filter.startDate&amp;year=<%=year%>&amp;month=<%=month%>','','width=300,height=300')"><img
				border="0" src="images/calendar.jpg" /></span></td>
		</tr>
		<tr>
			<td class="fieldTitle">End Date:</td>
			<td class="fieldValue"><html:text property="filter.endDate"
				maxlength="10" /> <span
				onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=customFilterForm&amp;openerElement=filter.endDate&amp;year=<%=year%>&amp;month=<%=month%>','','width=300,height=300')"><img
				border="0" src="images/calendar.jpg" /></span></td>
		</tr>
		<tr>
			<td class="fieldTitle">Status:</td>
			<td class="fieldValue"><html:select property="filter.status">
				<html:options collection="statusList" property="property"
					labelProperty="labelProperty" />
			</html:select></td>
		</tr>
		<tr>
			<td class="fieldTitle">Priority:</td>
			<td class="fieldValue"><html:select property="filter.priority">
				<html:options collection="priorityList" property="property"
					labelProperty="labelProperty" />
			</html:select></td>
		</tr>

		<tr>
			<td class="fieldTitle">Program:</td>
			<td class="fieldValue"><html:select property="filter.programId">
				<option value="All Programs">All Programs</option>
				<html:options collection="programs" property="id"
					labelProperty="name" />
			</html:select></td>
		</tr>



		<tr>
			<td class="fieldTitle">Provider:</td>
			<td class="fieldValue"><c:forEach var="provider"
				items="${providers}">
				<%
					String checked="";
				                    				Provider p = (Provider)pageContext.getAttribute("provider");
						                      		CustomFilter filter = (CustomFilter)request.getAttribute("custom_filter");
						                      		if(filter != null) {
					                      		Set providerList = filter.getProviders();
					                      		if(providerList.contains(p)){
					    	                  				checked="checked";
						    	                  		}
						                      		}
				%>
				<input type="checkbox" name="provider" <%=checked %>
					value="<c:out value="${provider.providerNo}"/>" />
				<c:out value="${provider.lastName}" />,<c:out
					value="${provider.firstName}" />
				<br />
			</c:forEach></td>
		</tr>
		<tr>
			<td class="fieldTitle">Task Assigned To:</td>
			<td class="fieldValue"><c:forEach var="provider"
				items="${providers}">
				<%
					String checked="";
				                    				Provider p = (Provider)pageContext.getAttribute("provider");
						                      		CustomFilter filter = (CustomFilter)request.getAttribute("custom_filter");
						                      		if(filter != null) {
					                      		Set providerList = filter.getAssignees();
					                      		if(providerList.contains(p)){
					    	                  				checked="checked";
				%>
				<!--  	<input name="assignee" type="hidden" value="<c:out value='${provider.provider_no}'/>"/>
	    	                  			-->
				<%
		    	                  		}
		                      		}
                      		%>

				<c:choose>
					<c:when test="${custom_filter.name != '*Myticklers*'}">

						<input name="assignee" <%=checked %> type="checkbox"
							value="<c:out value='${provider.providerNo}'/>" />


						<c:out value="${provider.lastName}" />,<c:out
							value="${provider.firstName}" />
						<br />
					</c:when>
				</c:choose>
			</c:forEach> <c:choose>
				<c:when test="${custom_filter.name == '*Myticklers*'}">Myself, <c:out
						value="${me}" />
					<input name="assignee" type=hidden
						value="<c:out value='${me_no}'/>" />
				</c:when>
			</c:choose></td>
		</tr>
		<tr>
			<td class="fieldValue" colspan="3" align="left"><html:submit
				styleClass="button">Save</html:submit> <input type="button"
				class="button" value="Cancel"
				onclick="location.href='<html:rewrite action="CustomFilter"/>'" /></td>
		</tr>
	</html:form>
</table>

</body>
</html>
