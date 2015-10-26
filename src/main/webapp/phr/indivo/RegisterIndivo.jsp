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

<%--
    Document   : registerIndivo
    Created on : 12-May-2008, 10:59:27 AM
    Author     : apavel
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.MyOscarLoggedInInfoInterface"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.phr.RegistrationHelper"%>
<%@page import="org.oscarehr.myoscar_server.ws.PersonTransfer3"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>
<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.oscarehr.common.model.DemographicExt"%>
<%@ page import="org.oscarehr.common.dao.DemographicExtDao"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="oscar.oscarProvider.data.ProviderMyOscarIdData"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="java.util.*"%>

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

<%
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

String demographicNo = request.getParameter("demographicNo");
int demographicId=Integer.parseInt(demographicNo);

org.oscarehr.common.model.Demographic demographic = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);
request.setAttribute("demographic", demographic);

String hPhoneExt = demographicExtDao.getValueForDemoKey(Integer.parseInt(demographicNo), "hPhoneExt");
String wPhoneExt = demographicExtDao.getValueForDemoKey(Integer.parseInt(demographicNo), "wPhoneExt");
if (hPhoneExt != null)
    request.setAttribute("demographicHomeExt", " " + hPhoneExt);
if (wPhoneExt != null)
    request.setAttribute("demographicWorkExt", " " + wPhoneExt);
%>

<html>
    <head>
        <title>Register for PHR</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
        
        <style type="text/css" language="JavaScript">
            .headingTop {
                width: 100%;
                text-align: center;
                font-weight: bold;
            }
            td.error {
            	color: red;
            }
            tr.userrow td {
            	border-bottom:1px solid black;

            }
        </style>
        <script type="text/javascript" language="JavaScript">
            function validateNSubmit() {
                var error = false;
                
            	$("#username-group").removeClass('error');
            	$("#username-required").hide();
            	$("#username-invalid").hide();
            	$("#password-group").removeClass('error');
            	$("#pwd-help").hide();
                
            	
	            if ($("#username").val() == '') {
	            	$("#username-group").addClass('error');
	            	$("#username-required").show();
	                $("#username").focus();
	                error = true;
	            } 
	            
	            if(!error) {
	            	var usernameRegex = /^[a-zA-Z0-9._]+$/;
		            if (!usernameRegex.test($("#username").val())) {
		            	$("#username-group").addClass('error');
		            	$("#username-invalid").show();
		                $("#username").focus();
		                error = true;
		            } 
            	}	                   
                
                if ($("#password").val().length < 8) {
	            	$("#password-group").addClass('error');
	            	$("#pwd-help").show();
	            	$("#password").focus();
	            	error = true;
	            }        
                
                if (error) return;                	

                //if everything is cool:
                document.getElementById("submitButton").disabled = true;
                document.getElementById("closeButton").disabled = false;
                document.getElementById("submitButton").value = "Creating user...";
                document.getElementById("registrationForm").submit();
            }
            function enableSubmit() {
                document.getElementById("submitButton").disabled = false;
                document.getElementById("closeButton").disabled = false;
            }
            function disableForm() {
                var formElements = document.getElementById("registrationForm").elements;
                for (var i=0; i<formElements.length; i++) {
                    formElements[i].disabled = true;
                }
                document.getElementById("submitButton").disabled = true;
                document.getElementById("closeButton").disabled = false;
            }
            
            <%if(request.getParameter("success") != null) {%>
	            $(document).ready(function() {  
	               disableForm();
	            });
       		<%}%>

        </script>
    </head>
    <body>
	    <%
	    	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
	    	if (myOscarLoggedInInfo == null || !myOscarLoggedInInfo.isLoggedIn())
	    	{
	    		%>
		            <jsp:include page="../AuthInclude.jsp">
		                <jsp:param name="forwardto" value="<%=\"/RegisterIndivo.jsp?demographicNo=\" + demographicNo%>"/>
		                <jsp:param name="pathtophr" value="<%=request.getContextPath() + \"/phr\"%>"/>
		            </jsp:include>
		        <%
	    	}
	    	
	    	String dispDocNo = request.getParameter("DocId");
	    	String user = (String) session.getAttribute("user");
	    	
	    	String password = null;;	    	
	    	List<String> ulist;
	    	boolean emailEmpty = false;
	    	ulist = new ArrayList<String>();
	    	
	    	//flag for if the error message coming back is that the username already exists
	    	boolean usernameNotUnique=false;	 
			
	    	if (myOscarLoggedInInfo != null && myOscarLoggedInInfo.isLoggedIn()) {
		        //if there is a success or failure message, that means the page has been redirected
		        //to itself, so we load the parameters that were sent on the last submit
		    	if (request.getParameter("success") != null || request.getParameter("failmessage") != null) {
		    		password = request.getParameter("password");
		    		String un = request.getParameter("username");
		    		
		    		ulist.add(un);
		    		String fail = request.getParameter("nonUnique");
		    		if(fail != null && fail.equals("true")) {
		    			//this flag will tell the jsp not to display the serverside error, but to display an inline error
		    			usernameNotUnique=true;
		    			
		    			//we still want to display the username suggestions on a failure
		    			ulist.addAll(RegistrationHelper.getPhrDefaultUserNameList(myOscarLoggedInInfo, demographicId));
		    		}
		    		
		    		
		    	} else {
			    	ulist = RegistrationHelper.getPhrDefaultUserNameList(myOscarLoggedInInfo, demographicId);
			    	password = RegistrationHelper.getNewRandomPassword();
		    	}
		    	
		        
		        //evaluate the email address. if empty, set flag to true
		        if (demographic.getEmail() == null || demographic.getEmail().trim().equals("")) {
		        	emailEmpty=true;
		        }
	    	}
	    	
	    	if(ulist.isEmpty()){
	    		ulist.add("");
	    	}
	    		

		%>
	   
        <html-el:form styleClass="form form-horizontal" action="/phr/UserManagement" styleId="registrationForm" method="POST" >
                <html-el:hidden property="method" value="registerUser"/>
                <html-el:hidden property="demographicNo" value="<%=demographicNo%>"/>
				<div class="page-header">			
					<h3>PHR Patient Registration</h3>
				</div>

			
				<table class="table table-condensed table-striped">					
					<!-- Responses coming from server side -->
					<%if (request.getParameter("failmessage") != null && usernameNotUnique==false) {%>
			           <tr class="error">
			           		<td>
			           			<span class="span2"></span>"
			               		<span class="span3 text-error"><%=request.getParameter("failmessage")%></span>
			               	</td>
			         	</tr>
			        <%} 
					if (request.getParameter("success") != null){%>                       
						<tr class="success">
							<td>	
								<script type="text/javascript" language="JavaScript">
		                           if(window.opener.document.forms[1].myOscarUserName){
		                              window.opener.document.forms[1].myOscarUserName.value = '<%=ulist.get(0)%>'
		                 		   }
		                        </script>
								<span class="span2"></span>		            
								<span class="span3 text-success">User was successfully added</span>
	
					            <span class="span3">
					            	<a class="btn btn-primary btn-small" href="../../dms/ManageDocument.do?method=display&doc_no=<%=dispDocNo%>&providerNo=<%=user%>">Download Registration Letter</a></div>
					            </span>
					        </td>
					    </tr>	
			        <%}%>
				</table>
				<div class="controls-row">
			        <div class="span5">
			        
						<div class="control-group <%if(usernameNotUnique){%>error <%}%> " id="username-group">
							<label class="control-label" for="username">Username</label>
							<div class="controls">
								<html-el:text styleId="username" property="username" value="<%=ulist.get(0)%>" />
								<div class="help-block" id="username-required" style="display:none">Username is required</div>
								<div class="help-block" id="username-invalid" style="display:none">Username is invalid</div>
								<%
									if(usernameNotUnique){%>
										<div class="help-block" id="username-non-unique">Username is already in use</div>
										<%if(ulist.size() > 1) {%>
										<div class="help-block">Available:
										<%	
											
											for (int i = 1; i < ulist.size() ; i++){
												%>
													<span><%=ulist.get(i)%><%if(i < ulist.size()-1 && ulist.size() > 2){ %>,<%}%></span>
												<%
											}
										%>
											</div>
										<%}%>	
									<%} %>
								
								
							</div>
						</div>
						<div class="control-group" id="password-group">
							<label class="control-label" for="password">Password (generated)</label>
							<div class="controls">
								<html-el:text styleId="password" property="password" value="<%=password%>"/>
								<div class="help-block" id="pwd-help" style="display:none">Password must be at least 8 characters long</div>
							</div>
						</div>
										
						<div class="control-group <%if(emailEmpty){%> warning <%}%>">
							<label class="control-label" for="email">E-mail</label>
							<div class="controls">
								<html-el:text property="email" value="${demographic.email}" />
								<%if(emailEmpty) {%>
									<div class="help-block" id="email-help">Please enter email address</div>
								<%} %>
							</div>
						</div>	
					</div>			

					<div class="span4 text-center">					
						<div class="control-group">
							<a class="btn btn-link" href="../../demographic/demographiccontrol.jsp?demographic_no=<%=demographicId%>&displaymode=edit&dboperation=search_detail">Edit</a>
							<address>
							  <strong>${demographic.firstName}&nbsp;${demographic.lastName}</strong><br />
							  ${demographic.address}<br />
							  ${demographic.city},&nbsp;${demographic.province}&nbsp;${demographic.postal}<br />
							  <abbr title="Phone">P:</abbr>&nbsp;${demographic.phone}&nbsp;${demographicHomeExt}<br />
							  <abbr title="Work">W:</abbr>&nbsp;${demographic.phone2}&nbsp;${demographicWorkExt}<br />
							  <abbr title="Date of Birth">DOB:</abbr>&nbsp;<%=DemographicData.getDob(demographic,"/")%>
							</address>
						</div>	
					</div>		
				</div>		

				
		
		
				<html-el:hidden property="address" value="${demographic.address}" />
				<html-el:hidden property="city" value="${demographic.city}" />
				<html-el:hidden property="province" value="${demographic.province}" />
				<html-el:hidden property="postal" value="${demographic.postal}" />
				<html-el:hidden property="phone" value="${demographic.phone}${demographicHomeExt}" />
				<html-el:hidden property="phone2" value="${demographic.phone2}${demographicWorkExt}" />
				<html-el:hidden property="dob" value="<%=DemographicData.getDob(demographic,\"/\")%>" />
				<html-el:hidden property="firstName" value="${demographic.firstName}" />
               	<html-el:hidden property="lastName" value="${demographic.lastName}" />
               	
               	
				<h4>Relationships</h4>
			
				
				<table class="table table-condensed table-striped">

					<tr>
						<td>Provider</td>
						<td>Relationship</td>
						<td>Allow to send messages</td>
					</tr>
                	<%
                		TreeMap<String, Provider> myOscarProviders=RegistrationHelper.getMyOscarProviders();
                		
                		//isolate the currently-logged-in Provider, in order to display this user 
                		//in the first row
	            		Long curProviderID = null;
	            		String curProviderUserName = "";
                		
                		
                		if (myOscarLoggedInInfo != null) {
                			curProviderID= myOscarLoggedInInfo.getLoggedInPersonId();
                			curProviderUserName = myOscarLoggedInInfo.getLoggedInPerson().getUserName();
                   		
                		
	                		Provider curProvider = myOscarProviders.get(curProviderUserName);
	                		
	                		//the display row for the current Provider
			                %><tr>
		                		<%if (curProviderID != null && curProvider != null){ %>
		                		<td>
		                			<label class="checkbox">
		                			<input type="checkbox" name="enable_primary_relation_<%=curProviderID%>" <%=RegistrationHelper.getCheckedString(session, "enable_primary_relation_"+curProviderID)%> />
		                			<strong><%=StringEscapeUtils.escapeHtml(curProviderUserName+" ("+curProvider.getFormattedName()+')')%></strong>
		                			</label>
		                		</td>
		                		<td><%=RegistrationHelper.renderRelationshipSelect(session, "primary_relation_"+curProviderID)%></td>
		                        <td align="center"><input type="checkbox" name="reverse_relation_<%=curProviderID%>" value="PATIENT" <%=RegistrationHelper.getCheckedStringWithValueString(session, "reverse_relation_"+curProviderID,"PATIENT")%> ></td>
		                        <%}else{ %>
		                		<td>&nbsp</td>
		                        <td class="error" ><!--  %=StringEscapeUtils.escapeHtml(curProviderUserName+" ("+curProvider.getFormattedName()+')')%>--></td>
		                		<td class="error">Not Found on Server</td>
		                		<%} %>
		                	</tr><%
	                		
	                		
		                	//Iterate through the rest of the Providers, display a row for each
	                		for (Map.Entry<String, Provider> entry : myOscarProviders.entrySet())
	                		{
	                			Long providerMyOscarId=MyOscarUtils.getMyOscarUserIdFromOscarProviderNo(myOscarLoggedInInfo, entry.getValue().getProviderNo());
	                			//We've already displayed the current Provider's row, so omit this user subsequently
	                			if(providerMyOscarId != curProviderID){
				                	%>
				                	<tr>
				                		
				                		<%if (providerMyOscarId != null){ %>
				                		<td>
				                			<label class="checkbox">
				                			<input type="checkbox" name="enable_primary_relation_<%=providerMyOscarId%>" <%=RegistrationHelper.getCheckedString(session, "enable_primary_relation_"+providerMyOscarId)%> />
				                			<strong><%=StringEscapeUtils.escapeHtml(entry.getKey()+" ("+entry.getValue().getFormattedName()+')')%></strong>
				                			</label>
				                		</td>
				                		<td><%=RegistrationHelper.renderRelationshipSelect(session, "primary_relation_"+providerMyOscarId)%></td>
				                        <td align="center"><input type="checkbox" name="reverse_relation_<%=providerMyOscarId%>" value="PATIENT" <%=RegistrationHelper.getCheckedStringWithValueString(session, "reverse_relation_"+providerMyOscarId,"PATIENT")%> ></td>
				                        <%}else{ %>
				                		<td>&nbsp</td>
				                        <td class="error" ><%=StringEscapeUtils.escapeHtml(entry.getKey()+" ("+entry.getValue().getFormattedName()+')')%></td>
				                		<td class="error">Not Found on Server</td>
				                		<%} %>
				                	</tr>
				                    <%
	                			}
	                		}
                		}
                    %>					
				
				</table>
				<div class="controls-row">
					<div class="control-group">
						<span class="controls span1">
							<html-el:button styleClass="btn btn-primary" onclick="validateNSubmit();" property="submitButton" styleId="submitButton" value="Submit"/>
						 </span>
						<span class="controls span1"> 					
							<html-el:button styleClass="btn" property="closeButton" styleId="closeButton" value="Close" onclick="window.close();"/>
						</span>
					</div>	
				</div>			
				
       </html-el:form>
       <script type="text/javascript" language="JavaScript">
           enableSubmit();
       </script>
	    <%
	    	if (myOscarLoggedInInfo == null || !myOscarLoggedInInfo.isLoggedIn())
	    	{
	    		%>
		           <script type="text/javascript" language="JavaScript">
		               disableForm();
		           </script>
		        <%
	    	}
		%>
    </body>