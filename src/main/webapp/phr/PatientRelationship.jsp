<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_phr" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_phr");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%-- TODO:Only works for local patients right now. Not sure if that's a big deal. Probably should be a warning that this isn't a local patient --%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.AccountManager"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.apache.http.HttpRequest"%>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.phr.RegistrationHelper"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<%
MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
String myOscarUserName = getWebMember(request,"myOscarUserName");
String demographic = getWebMember(request,"demoNo");
Long myOscarUserId=AccountManager.getUserId(myOscarLoggedInInfo, myOscarUserName);



if(myOscarLoggedInInfo !=null && myOscarLoggedInInfo.isLoggedIn() && myOscarUserName != null && demographic != null){ 
	if(!RegistrationHelper.iHavePatientRelationship(myOscarLoggedInInfo,myOscarUserId)){ %>
    	<span id="relationshipMessage" style="color:red; font-size:x-small;padding-left:3px;"><bean:message key="phr.verification.patient.not.respond" />
      		<a id="relationshipAdder" href="javascript:void();"><bean:message key="phr.verification.addPatientRelationship"/></a>
      	</span>
	<%}else{ %>
		<span id="relationshipMessage" style="font-size:x-small;">
			<bean:message key="phr.verification.patientRelationshipExists"/>
		</span>
	<%}
}else if(myOscarLoggedInInfo ==null || !myOscarLoggedInInfo.isLoggedIn()){ MiscUtils.getLogger().debug("should show not logged in message");%>
	<bean:message key="phr.verification.notloggedin"/>
<%}%>	      
<script type="text/javascript">
$(function() {
  $("#relationshipAdder").click(function() {
    $(this).attr("disabled", "true");
    $.ajax({
      url: '<c:out value="${ctx}"/>/phr/UserManagement.do?method=addPatientRelationship&demoNo=<%=demographic%>&myOscarUserName=<%=myOscarUserName%>',
      dataType: "html",
      timeout: 5000,
      error: function() {
        alert("Error talking to server.");
        $(this).attr("disabled", "false");
      },
      success: function(data) {
        $("#relationshipMessage").html(data);
        $(this).attr("disabled", "false");
      }
    });
  });
});
</script>
<%!
String getWebMember(HttpServletRequest request,String str){
	String ret = request.getParameter(str);
	if(ret == null){
		ret = (String) request.getAttribute(str);
	}
	return ret;
}
%>