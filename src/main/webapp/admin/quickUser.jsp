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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
	
	ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	List<Provider> providers = providerManager.getProviders(loggedInInfo, true);
	
	UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);
	
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Create Quick User</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/js/checkPassword.js.jsp"></script>
<style>
.red{color:red}

</style>
<script>
	function validateForm(form) {
		var firstName = $("input[name='firstName']").val();
		var lastName = $("input[name='lastName']").val();
		var username = $("input[name='username']").val();
		var password = $("input[name='password']").val();
		var confirm = $("input[name='confirmPassword']").val();
		var copyProviderNo = $("select[name='copyProviderNo']").val();
		var pin = $("input[name='pin']").val();
		
		var errors = "";
		
		if(firstName.length == 0) {
			errors += "<span>Please enter a first name</span><br/>";
		}
		if(lastName.length == 0) {
			errors += "<span>Please enter a last name</span><br/>";
		}
		if(username.length == 0) {
			errors += "<span>Please enter a username</span><br/>";
		}
		if(password.length == 0) {
			errors += "<span>Please enter a password</span><br/>";
		}
		
		if(confirm != password) {
			errors += "<span>Passwords do not match</span><br/>";
		}
		
		if(password.length > 0 && !validatePassword(password)) {
			errors += "<span>Password not suitable</span><br/>";
		}
		
		if(pin.length != 4) {
			errors += "<span>Please enter a 4-digit PIN</span><br/>";
		}
		
		
		if(copyProviderNo.length == 0) {
			errors += "<span>Please choose a provider</span><br/>";
		}
		
		
		if(errors.length > 0) {
			$("#errors").html(errors);
			return false;
		}
		
		return true;
	}
	
	function usernameChanged() {
		$("#submitButton").prop("disabled", true).addClass("ui-state-disabled");
		
		var username = $("#username").val();
		
		if(username.length == 0) {
			return;
		}
		
		//check the username
		var ctx = '<%=request.getContextPath()%>';
		jQuery.getJSON(ctx + "/admin/createQuickUser.do?method=checkUsername&user_name="+username, {},
        	function(xml) {
        	    if(xml.usernameExists != null && !xml.usernameExists) {
        	    	 $("#submitButton").prop("disabled", false).removeClass("ui-state-disabled"); 	
        	    }
        	    if(xml.usernameExists != null && xml.usernameExists) {
       	    	 alert('Username already exists. Please try another');
       	    	}
        	    
        	    if(xml.error == true) {
        	    	alert('Error checking username.');
        	    	//$("#submitButton").prop("disabled", false).removeClass("ui-state-disabled"); 	
        	    }
        	
        });
		
		
		
	}
	
	$(document).ready(function(){
		usernameChanged();	
	});
	
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Create new User</h4>

<c:if test="not empty ${errors}">
<ul>
<c:forEach var="error" items="errors"></c:forEach>
<li><c:out value="error" /></li>
</ul>
</c:if>

<html:form action="/admin/createQuickUser" onsubmit="return validateForm(this);">
       <fieldset>
               <div class="control-group">
                       <div class="controls">
                               <input name="firstName" type="text" size="25" placeholder="First Name" />&nbsp;&nbsp;
                                <input name="lastName" type="text" size="25" placeholder="Last Name"/>
                       </div>
                       <div class="controls">
                               <%
                        	       oscar.OscarProperties props = oscar.OscarProperties.getInstance();

	                               UserProperty sexProp = userPropertyDAO.getProp(provider.getProviderNo() ,  UserProperty.DEFAULT_SEX);
	                               String sex = "";
	                               if (sexProp != null) {
	                                   sex = sexProp.getValue();
	                               } else {
	                                   // Access defaultsex system property
	                                   sex = props.getProperty("defaultsex","");
	                               }
                               %>
                               <select  name="gender" id="gender">
			                        <option value=""></option>
			                		<% for(org.oscarehr.common.Gender gn : org.oscarehr.common.Gender.values()){ %>
			                        <option value="<%=gn.name()%>" <%=((sex.toUpperCase().equals(gn.name())) ? "selected=\"selected\"" : "") %>><%=gn.getText()%></option>
			                        <% } %>
			                        </select>
			                        
			                       
                               <br/>
                              
                       </div>
                       <div class="controls">
                               <input name="username" id="username" type="text" size="25" placeholder="Username" onchange="usernameChanged()"/>&nbsp;&nbsp;
                               <input name="pin" type="text" size="25" placeholder="PIN"/><br/>
                       </div>
                       
                        <div class="controls">
                              	<input name="password" type="password" size="25" placeholder="Password"/>&nbsp;&nbsp;
                                <input name="confirmPassword" type="password" size="25" placeholder="Confirm Password"/><br/>
                       </div>
                       
                        <div class="controls">
                              	<input name="billingNo" type="text" size="25" placeholder="Provincial Billing #"/> 
                              	<%if("true".equals(oscar.OscarProperties.getInstance().getProperty("quickUser.showGenerateBillingNo","false"))) { %>
                              	<input type="checkbox" name="generateBillingNo"/>Generate Random Billing #<br/>
                              	<% } else { %>
                              		<br/>
                              	<% } %>
                                <input name="thirdPartyBillingNo" type="text" size="25" placeholder="3rd Party Billing #"/>
                                <%if("true".equals(oscar.OscarProperties.getInstance().getProperty("quickUser.showGenerateThirdPartyBillingNo","false"))) { %>                              	
                                <input type="checkbox" name="generateThirdPartyBillingNo"/>Generate Random 3rd Party Billing #<br/>
                               	<% } else { %>
                              		<br/>
                              	<% } %>
                       </div>
                       
                       <div class="controls">
                       		<b>Force Password Reset:</b>
                             <select name="forcePasswordReset">
                          	   <option value="0" selected="selected">false</option>
								<option value="1">true</option>
								
							</select>	
                       </div>
                       
                        <div class="controls">
                               <b>Copy role info from:</b> <select name="copyProviderNo">
                               <option value=""><i>[Select Below]</i></option>
                               		
                               <%for(Provider p:providers) { %>
                               		<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName() %></option>
                               	<%} %>
                               </select>
                               &nbsp;
                             
                              <input type="checkbox" name="includeDrugFavourites"/>Include Drug Favourites<br/>
                               
                       </div>
                       
               </div>
                <div id="errors" class="control-group" style="color:red">
                
                </div>
                
               <div class="control-group">
                        <input type="submit" id="submitButton" class="btn btn-primary" value="Save"/>&nbsp;
                        
                </div>
               
	</fieldset>
</html:form>
</body>
</html:html>
