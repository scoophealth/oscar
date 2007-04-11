<%@ include file="/ticklerPlus/header.jsp" %>

<%@ page import="java.util.*" %>
<%
 	Calendar rightNow = Calendar.getInstance();              
 	int year = rightNow.get(Calendar.YEAR);
 	int month = rightNow.get(Calendar.MONTH)+1;
 	int day = rightNow.get(Calendar.DAY_OF_MONTH);
 	String formattedDate = year + "-" + month + "-" + day;
%>

	<script>
		function search_demographic() {
			window.open('./ticklerPlus/demographicSearch.jsp?form=customFilterForm&elementName=filter.demographic_webName&elementId=filter.demographic_no&query=' + document.customFilterForm.elements['filter.demographic_webName'].value,'demographic_search');
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

<%@ include file="messages.jsp" %>

<br/>

<table width="60%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">

	<html:form action="/CustomFilter" onsubmit="return validateCustomFilterForm(this);">
	
		<input type="hidden" name="method" value="save"/>
		
		<c:if test="${not empty custom_filter.name}">
	        <html:hidden property="filter.id"/>
	    </c:if>
              <tr>
                      <td class="fieldTitle">Filter Name:</td>
                      <td class="fieldValue">
                      <c:choose>
                      <c:when test="${custom_filter.name == '*Myticklers*'}">
                      	*Myticklers*
                      	 <html:hidden property="filter.name"/>
                      </c:when>
                      <c:otherwise>
                        <html:text property="filter.name"/>        
                      </c:otherwise>
                      	</c:choose>
                      	
	                 </td>
              </tr>
              <tr>
                      <td class="fieldTitle">Demographic:</td>
                      <td class="fieldValue">
                      	<html:hidden property="filter.demographic_no"/>
                      	<html:text property="filter.demographic_webName"/>
                      	<input type="button" value="Search" onclick="search_demographic();"/>
			</td>
	                 </td>
              </tr>
              <tr>
                      <td class="fieldTitle">Start Date:</td>
                      <td class="fieldValue">
                      	<html:text property="filter.startDate"/>
                      	<span onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=customFilterForm&amp;openerElement=filter.startDate&amp;year=<%=year %>&amp;month=<%=month %>','','width=300,height=300')"><img border="0" src="images/calendar.jpg"/></span>
	                 </td>
              </tr>
              <tr>
                      <td class="fieldTitle">End Date:</td>
                      <td class="fieldValue">
                      	<html:text property="filter.endDate"/>
                      	<span onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=customFilterForm&amp;openerElement=filter.endDate&amp;year=<%=year %>&amp;month=<%=month %>','','width=300,height=300')"><img border="0" src="images/calendar.jpg"/></span>
	                 </td>
              </tr>
              <tr>
                      <td class="fieldTitle">Status:</td>
                      <td class="fieldValue">
                      	<html:select property="filter.status">
	                     	<html:options collection="statusList" property="property" labelProperty="labelProperty"/>
                      	</html:select>
	                 </td>
              </tr>
              <tr>
                      <td class="fieldTitle">Priority:</td>
                      <td class="fieldValue">
                     	<html:select property="filter.priority">
	                     	<html:options collection="priorityList" property="property" labelProperty="labelProperty"/>
                     	</html:select>
	                 </td>
              </tr>
			  <tr>
                      <td class="fieldTitle">Provider:</td>
                      <td class="fieldValue">
                      	<c:forEach var="provider" items="${providers}">
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
                      		<input type="checkbox" name="provider" <%=checked %> value="<c:out value="${provider.provider_no}"/>"/><c:out value="${provider.lastName}"/>,<c:out value="${provider.firstName}"/><br/>
                      	</c:forEach>
                      </td>
              </tr>
			  <tr>
                      <td class="fieldTitle">Task Assigned To:</td>
                      <td class="fieldValue">
                      	
                      	<c:forEach var="provider" items="${providers}">
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
                      		
                      		<input name="assignee" <%=checked %> type="checkbox" value="<c:out value='${provider.provider_no}'/>"/>
                      	
                      	
                      		<c:out value="${provider.lastName}"/>,<c:out value="${provider.firstName}"/><br/>
                      	</c:when>
                      	</c:choose>
                      	</c:forEach>
                      	
                      	
                      	<c:choose>  
                      	<c:when test="${custom_filter.name == '*Myticklers*'}">Myself, <c:out value="${me}"/>
                      		<input name="assignee" type=hidden value="<c:out value='${me_no}'/>"/>
                		</c:when>
                      	</c:choose>
                      	
                      	
                      	
                      </td>
              </tr>
				<tr>
	              	<td class="fieldValue" colspan="3" align="left">
					        <html:submit styleClass="button">Save</html:submit>
					        <input type="button" class="button" value="Cancel" onclick="location.href='<html:rewrite action="CustomFilter"/>'"/>
					</td>
				</tr>
		</html:form>
</table>     

</body>
</html>