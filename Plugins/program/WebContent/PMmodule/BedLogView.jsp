<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>
<%@ page import="org.caisi.PMmodule.model.Demographic" %>
<%@ page import="org.caisi.PMmodule.model.BedLog" %>
<%@ page import="java.util.*" %>


<html:form action="/PMmodule/BedLog">
<input type="hidden" name="method" value="view"/>
<html:hidden property="sheet.programId"/>
<h4>Bed Log Viewer for <c:out value="${program.name}"/></h4>
<br/>
<br/>
<html:select property="sheet.id" onchange="this.form.submit();">
	<html:options collection="sheets" property="id" labelProperty="dateCreated"/>
</html:select>

<c:if test="${requestScope.bedlogs != null}">
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
				<td><%=status %></td>
			</c:forEach>
		</tr>
		</c:forEach>
	</table>
</c:if>
<br/>
<input type="button" value="Edit Mode" onclick="this.form.method.value='list';this.form.submit();"/>
</html:form>
