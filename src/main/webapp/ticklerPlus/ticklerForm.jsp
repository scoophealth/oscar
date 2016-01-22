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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler"
	rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%-- Updated by Eugene Petruhin on 18 dec 2008 while fixing #2422864 & #2317933 & #2379840 --%>
<%-- Updated by Eugene Petruhin on 20 feb 2009 while fixing check_date() error --%>

<%@include file="/ticklerPlus/header.jsp"%>

<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.common.model.Tickler"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%
		GregorianCalendar now = new GregorianCalendar();
	
		int curYear = now.get(Calendar.YEAR);
		int curMonth = now.get(Calendar.MONTH);
		int curDay = now.get(Calendar.DAY_OF_MONTH);
		int curHour = now.get(Calendar.HOUR);
		int curMinute = now.get(Calendar.MINUTE);
		
		boolean curAm = (now.get(Calendar.HOUR_OF_DAY) <= 12) ? true : false;
	%>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>

<script type="text/javascript" src="../js/checkDate.js"></script>
<script type="text/javascript">
		$(document).ready(function(){
		  changeProgram();
		});

		function check_tickler_service_date() {
			return check_date('tickler.serviceDateWeb');
		}

<%--
		function search_demographic() {
			var popup = window.open('<c:out value="${ctx}"/>/ticklerPlus/demographicSearch.jsp?query=' + document.ticklerForm.elements['tickler.demographic_webName'].value,'demographic_search');
		
			if (popup != null) {
	    			if (popup.opener == null) {
	      			popup.opener = self;
	    			}
	    			popup.focus();
	  			}	
		}

		function search_provider() {
			url = '<c:out value="${ctx}"/>/provider/receptionistfindprovider.jsp';
			url += '?caisi=true&pyear=<%=curYear%>&pmonth=<%=curMonth%>&pday=<%=curDay%>&providername=';
			var popup = window.open(url + document.ticklerForm.elements['tickler.taskAssignedToName'].value,'provider_search');
		}
--%>		
		function validateTicklerForm(form) {
			if (form.elements['tickler.taskAssignedTo'].value == 'none') {
				alert('You must assign the task to a valid provider');
				return false;
			}
			
			if (form.elements['tickler.serviceDateWeb'].value == '') {
				alert('You must provide a valid service date');
				return false;
			}
			
			if (form.elements['tickler.message'].value == '') {
				alert('You must provide a message');
				return false;
			}
			
			
			
			return check_tickler_service_date();
		}
		
		
	
		function changeProgram() {
			//reset the assigned_to list
			var programNo = $("#program_no").val();
			
			$.ajax({url:'../Tickler.do?method=getProvidersByProgram&programNo='+programNo,async:true,dataType:'json', success:function(data) {

			$('#taskAssignedTo')
				    .find('option')
				    .remove()
				    .end()
				    .append(' <option value="none">- select -</option>');				

            for(var x=0;x<data.length;x++) {
            	$('#taskAssignedTo').find('option').end().append('<option value="'+data[x].providerNo+'">'+data[x].name+'</option>');
	        }
          },error:function() {
          	alert('Failed to load providers for program ' + programNo);
          }});
		}
		
	</script>

	<tr>
		<td class="searchTitle" colspan="4">Create New Tickler</td>
	</tr>
</table>

<%
	String demographicName = request.getParameter("tickler.demographic_webName");
	if(demographicName  == null || "undefined".equals(demographicName)) {
		demographicName = (String)request.getAttribute("demographicName");
	}
%>
<%@ include file="/common/messages.jsp"%>

<table width="60%" border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<html:form action="/Tickler" focus="tickler.demographicNo" onsubmit="return validateTicklerForm(this);">
	
		<input type="hidden" name="method" value="save" />
		<html:hidden property="tickler.creator" value='<%=(String) session.getAttribute("user")%>' />
		<html:hidden property="tickler.id" />
		
		<tr>
			<td class="fieldTitle">
				Demographic:
			</td>
			<td class="fieldValue">
				<html:hidden property="tickler.demographicNo" />
				<%=demographicName%>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">Program:</td>
			<td class="fieldValue">
				<select name="tickler.program_no" id="program_no" onChange="changeProgram()">
				<%
					String demographicNo = request.getParameter("tickler.demographicNo");
					
					List<Program> programs = (List<Program>)request.getAttribute("programDomain");
					
					String currentProgramId = (String)request.getAttribute("currentProgramId");
					
					for(Program p:programs) {
						String selected = (p.getId().toString().equals(currentProgramId))?" selected=\"selected\" ":"";
				%>
					<option value="<%=p.getId()%>" <%=selected%>><%=p.getName() %></option>
					
				<% } %>
				</select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">Service Date:</td>
			<%
				Calendar rightNow = Calendar.getInstance();
				int year = rightNow.get(Calendar.YEAR);
				int month = rightNow.get(Calendar.MONTH) + 1;
				int day = rightNow.get(Calendar.DAY_OF_MONTH);
				String formattedDate = year + "-" + month + "-" + day;
			%>
			<td class="fieldValue">
				<html:text property="tickler.serviceDateWeb" value="<%=formattedDate%>" maxlength="10"/>
				<span onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=tickler.serviceDateWeb&amp;year=<%=year%>&amp;month=<%=month%>','','width=300,height=300')">
					<img border="0" src="images/calendar.jpg" />
				</span>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Service Time:
			</td>
			<td class="fieldValue">
				<select name="tickler.service_hour">
				<%
					for (int x = 1; x < 13; x++) {
						String selected = "";
						
						if (curHour == x) {
							selected = "selected";
						}
				%>
					<option value="<%=x%>" <%=selected%>><%=x%></option>
				<%
					}
				%>
				</select> : <select name="tickler.service_minute">
				<%
					for (int x = 0; x < 60; x += 15) {
						String selected = "";
						
						if (curMinute >= x && curMinute < (x + 15)) {
							selected = "selected";
						}
						
						String val = String.valueOf(x);
						
						if (val.equals("0")) {
							val = "00";
						}
				%>
					<option value="<%=val%>" <%=selected %>><%=val%></option>
				<%
					}
				%>
				</select> &nbsp; <select name="tickler.service_ampm">
					<option value="AM">AM</option>
				<%
					if (!curAm) {
				%>
					<option value="PM" selected>PM</option>
				<%
					} else {
				%>
					<option value="PM">PM</option>
				<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Priority:
			</td>
			<td class="fieldValue">
				<html:select property="tickler.priorityWeb">
					<option value="Normal">Normal</option>
					<option value="High">High</option>
					<option value="Low">Low</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Task Assigned To:
			</td>
			<td class="fieldValue">
				<html:hidden property="tickler.taskAssignedToName" />
	            <html:select property="tickler.taskAssignedTo" styleId="taskAssignedTo" value="none">
        		    <option value="none">- select -</option>
        		    <html:options collection="providers" property="providerNo" labelProperty="formattedName"/>
            	</html:select>
				<%--html:hidden property="tickler.taskAssignedTo" />
				<html:text property="tickler.taskAssignedToName" />
				<input type="button" value="Search" onclick="search_provider();" /--%> 
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Status:
			</td>
			<td class="fieldValue">
				<html:select property="tickler.statusWeb">
					<option value="A">Active</option>
					<option value="C">Completed</option>
					<option value="D">Deleted</option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="fieldTitle">
				Message:
			</td>
			<td class="fieldValue">
				<html:textarea cols="40" rows="10" property="tickler.message"></html:textarea>
			</td>
		</tr>
		<!-- 
		<tr>
			<td class="fieldTitle">
				Post to eChart:
			</td>
			<td class="fieldValue">
				<input name="echart" value="true" type="checkbox" />
			</td>
		</tr>
		-->
		<tr>
			<td class="fieldValue" colspan="3" align="left">
			    	<input type="hidden" name="docType" value="<%=request.getParameter("docType")%>"/>
           			<input type="hidden" name="docId" value="<%=request.getParameter("docId")%>"/>
				<html:submit styleClass="button">Save</html:submit>
				<input type="button" value="Cancel" onclick="window.close()"/>
			</td>
		</tr>
	</html:form>
</table>

<c:if test="${requestScope.from ne 'CaseMgmt'}">
	</body>
</html>
</c:if>
