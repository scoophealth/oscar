<%-- This page is included by "appointmentprovideradminday.jsp".
     User defined JavaScript functions here can be found in the above file. --%>


<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page import="org.oscarehr.util.*"%>

<%
String questr=(String)session.getAttribute("infirmaryView_OscarQue"); 
//remove "infirmaryView_programId" from querystring
questr=oscar.caisi.CaisiUtil.removeAttr(questr,"infirmaryView_programId=");
questr=oscar.caisi.CaisiUtil.removeAttr(questr,"infirmaryView_clientStatusId=");
questr=oscar.caisi.CaisiUtil.removeAttr(questr,"viewall");
questr += "&viewall=1";

String providerurlString="providercontrol.jsp?"+questr;
//remove "infirmaryView_isOscar" from querystring
session.setAttribute("infirmaryView_OscarQue",oscar.caisi.CaisiUtil.removeAttr(questr,"infirmaryView_isOscar="));
%>

<script>
function submitProgram(ctrl) {
	var url = "<%=providerurlString%>"+"&infirmaryView_programId="+ctrl.value;	
	document.location.href = url;
}
function submitStatus(ctrl) {
	var programCtrl = document.getElementById("bedprogram_no");
	document.location.href = "<%=providerurlString%>"+"&infirmaryView_programId="+programCtrl.value+"&infirmaryView_clientStatusId="+ctrl.value;
}
</script>

<logic:notEqual name="infirmaryView_isOscar" value="true">
	<br>
	<b>Program:</b>
	<select id="bedprogram_no" name="bedprogram_no"
		onchange="submitProgram(this)">
		<%java.util.List programBean=(java.util.List)session.getAttribute("infirmaryView_programBeans");
	String programId=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
	if (programBean.size()==0 || programId.equalsIgnoreCase("0")){%>
		<option value="0" selected>-No assigned program-</option>
		<%}else{ %>
		<logic:iterate id="pb" name="infirmaryView_programBeans"
			type="org.apache.struts.util.LabelValueBean">
			<logic:equal name="infirmaryView_programId"
				value="<%=pb.getValue()%>">
				<option value="<%=pb.getValue()%>" selected><%= pb.getLabel() %></option>
			</logic:equal>
			<logic:notEqual name="infirmaryView_programId"
				value="<%=pb.getValue()%>">
				<option value="<%=pb.getValue()%>"><%= pb.getLabel() %></option>
			</logic:notEqual>
		</logic:iterate>
		<%} %>
	</select>
</logic:notEqual>
<logic:notEqual name="infirmaryView_isOscar" value="false">
	<caisi:ProgramExclusiveView providerNo="<%=curUser_no%>"
		value="appointment">
		<br>
		<b>Program:</b>
		<select id="bedprogram_no" name="bedprogram_no"
			onchange="submitProgram(this)">
			<%java.util.List programBean=(java.util.List)session.getAttribute("infirmaryView_programBeans");
	String programId=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
	if (programBean.size()==0 || programId.equalsIgnoreCase("0")){%>
			<option value="0" selected>-No assigned program-</option>
			<%}else{ %>
			<logic:iterate id="pb" name="infirmaryView_programBeans"
				type="org.apache.struts.util.LabelValueBean">
				<logic:equal name="infirmaryView_programId"
					value="<%=pb.getValue()%>">
					<option value="<%=pb.getValue()%>" selected><%= pb.getLabel() %></option>
				</logic:equal>
				<logic:notEqual name="infirmaryView_programId"
					value="<%=pb.getValue()%>">
					<option value="<%=pb.getValue()%>"><%= pb.getLabel() %></option>
				</logic:notEqual>
			</logic:iterate>
			<%} %>
		</select>
	</caisi:ProgramExclusiveView>
</logic:notEqual>

<logic:notEqual name="infirmaryView_isOscar" value="true">
  &nbsp;
  <select id="program_clientstatus" name="program_clientstatus"
		onchange="submitStatus(this)">
		<c:choose>
			<c:when
				test="${empty infirmaryView_clientStatusId or infirmaryView_clientStatusId == 0}">
				<option value="0" selected>&nbsp;</option>
			</c:when>
			<c:otherwise>
				<option value="0">&nbsp;</option>
			</c:otherwise>
		</c:choose>

		<c:forEach var="status" items="${program_client_statuses}">
			<c:choose>
				<c:when test="${infirmaryView_clientStatusId == status.id}">
					<option selected value="<c:out value="${status.id}"/>"><c:out
						value="${status.name}" /></option>
				</c:when>
				<c:otherwise>
					<option value="<c:out value="${status.id}"/>"><c:out
						value="${status.name}" /></option>
				</c:otherwise>
			</c:choose>

		</c:forEach>
	</select>
	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="no">
			<a
				href='providercontrol.jsp?infirmaryView_isOscar=true&<%=session.getAttribute("infirmaryView_OscarQue") %>'>|
			Oscar View</a>
		</caisi:ProgramExclusiveView>
	</caisi:isModuleLoad>
</logic:notEqual>

<logic:equal name="infirmaryView_isOscar" value="true">
	<caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="no">
		<div align="right"><a
			href='providercontrol.jsp?infirmaryView_isOscar=false&<%=session.getAttribute("infirmaryView_OscarQue") %>'>|
		Case Management View</a></div>
	</caisi:ProgramExclusiveView>
</logic:equal>
</td>
</tr>
</table>
