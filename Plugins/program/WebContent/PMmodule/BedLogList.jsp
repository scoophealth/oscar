<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>
<%@ page import="org.caisi.PMmodule.model.Demographic" %>
<%@ page import="org.caisi.PMmodule.model.BedLog" %>
<%@ page import="java.util.*" %>
<script>
	function submit_log(input,clientId,time) {
		var form = document.bedLogForm;
		var status = input.options[input.selectedIndex].value;
		//alert(clientId + ',' + time + ',' +status);		
		form.elements['bedlog.demographicNo'].value = clientId;
		form.elements['bedlog.time'].value = time;
		form.elements['bedlog.status'].value = status;
		form.elements['bedlog.programId'].value = form.elements['sheet.programId'].value ;
		
		form.method.value='save_entry';
		form.submit();
	}
</script>
<html:form action="/PMmodule/BedLog">
<input type="hidden" name="method" value=""/>
<html:hidden property="sheet.programId"/>
<html:hidden property="sheet.id"/>

<html:hidden property="bedlog.demographicNo"/>
<html:hidden property="bedlog.time"/>
<html:hidden property="bedlog.status"/>
<html:hidden property="bedlog.programId"/>

<h4>Bed Log for <c:out value="${program.name}"/></h4>
<br/>
<br/>
<c:if test="${requestScope.sheet != null }">
	<h5>Bed Log Sheet #<c:out value="${sheet.id}"/> - <c:out value="${sheet.dateCreated}"/></h5>
	<br/>
<%
	List bedlogs = (List)request.getAttribute("bedlogs");
	BedLogContainer blc = new BedLogContainer(bedlogs);
%>	
	<table border="1" width="100%">
		<tr>
			<th>Client Name</th>
			<th>DOB</th>
			<c:forEach var="time" items="${bedlog_config.checkTimes}">
				<th><c:out value="${time}"/></th>
			</c:forEach>
		</tr>
		<c:forEach var="client" items="${clients}">
		<tr>
			<td><c:out value="${client.formattedName}"/></td>
			<td><c:out value="${client.formattedDob}"/></td>
			<c:forEach var="time" items="${bedlog_config.checkTimes}">
				<%
				String time = (String)pageContext.getAttribute("time");
					Demographic client = (Demographic)pageContext.getAttribute("client");
					BedLog bedlog = blc.getBedLog(client.getDemographicNo(),time);
					String status = "";
					if(bedlog != null) {
						status = bedlog.getStatus();
					}
					pageContext.setAttribute("status",status);
				%>
				<td>
					<select name="" onchange="submit_log(this,'<%=client.getDemographicNo()%>','<%=time %>')">
						<option value=""></option>
						<c:forEach var="status_choice" items="${bedlog_config.statuses}">
							<c:choose>
							<c:when test="${status_choice eq status}">
								<option value="<c:out value="${status_choice}"/>" SELECTED><c:out value="${status_choice }"/></option>
							</c:when>
							<c:otherwise>
								<option value="<c:out value="${status_choice}"/>"><c:out value="${status_choice }"/></option>
							</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</c:forEach>
		</tr>
	</c:forEach>
	</table>
</c:if>

<br/>
<input type="button" value="Create New Sheet" onclick="document.bedLogForm.method.value='new_sheet';document.bedLogForm.submit();"/>

</html:form>
