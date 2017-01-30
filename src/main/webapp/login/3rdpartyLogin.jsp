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
<%@page import="org.apache.cxf.rs.security.oauth.services.OOBAuthorizationResponse"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.cxf.rs.security.oauth.data.OAuthAuthorizationData" %>
<%@page import="org.apache.cxf.rs.security.oauth.data.OAuthPermission" %>
<%@page import="oscar.login.OAuthSessionMerger" %>

<%
	boolean loggedIn= false;
	if(session.getAttribute("user") != null) {
		loggedIn=true;

        // Merge the session into our request token
        
        boolean didMerge = OAuthSessionMerger.mergeSession(request);
        if (!didMerge) {
            loggedIn = false; // Couldn't get the merge so request credentials again.
        }
	}
	
	OAuthAuthorizationData oauthData = (OAuthAuthorizationData)request.getAttribute("oauthauthorizationdata");
	OOBAuthorizationResponse oauthOobResponse = (OOBAuthorizationResponse)request.getAttribute("oobauthorizationresponse");
	
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>


<!DOCTYPE html>
<html>
    <head>
            <title>Login and Authorize 3rd party application</title>
            

    <link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"],
       {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
    <link href="<%=request.getContextPath()%>/css/bootstrap-responsive.css" rel="stylesheet">
            
            
            
            <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
            <script>
            	function deny() {
            		$("#oauthDecision").val('deny');
            		$("#scopeForm").submit();
            	}
            	
            	function submitCredentials() {
            		//ajax call to post to login page, check if it's successful or not
            		$.post('<%=request.getContextPath()%>/login.do;jsessionid=<%=session.getId()%>',
            				{username:$('#username').val(), password: $('#password').val(),pin:$('#pin').val(),ajaxResponse:'true',invalidate_session:'false',oauth_token:$('#oauth_token').val()},
            				function(data){
            					
            					if(data.success) {
            						$('#login_div').hide();
                        			$('#scope_div').show();
                        			$('#loggedin_div').show();
                        			$('#providerName').html(data.providerName);
                                } else {
                                    $('#login_error').show();
                                    $('#login_error > span > span').html(data.error);
                                }
            					
            		}, 'json');
            	}
            	
            	$(document).ready(function(){
            		if('<%=loggedIn%>' == 'true') {
            			
            			$('#login_div').hide();
            			$('#scope_div').show();
            			$('#loggedin_div').show();
            			$('#providerName').html('<%if(loggedIn) {out.print(loggedInInfo.getLoggedInProvider().getFormattedName());}%>');
            		} else {
            			$('#login_div').show();
            			$('#scope_div').hide();
            			$('#loggedin_div').hide();
            		}
            		
            		if('<%=(oauthOobResponse!=null)%>' == 'true') {
            			$('#login_div').hide();
            		} 
            	});
            </script>
    </head>
    <body style="background-color:white">
    
    	<div class="container">
    		<div class="row">
    			<div class="span5" >
					<div style="margin-top:25px;">
				               <img src="<%=request.getContextPath() %>/images/OSCAR-LOGO.gif" width="450" height="274">
				               
				               <p>
				               <font size="-1" face="Verdana, Arial, Helvetica, sans-serif">
				                   
				                   OSCAR McMaster <br>For more information, visit <a href="http://www.oscarcanada.org">www.oscarcanada.org</a><br>
				                   
				               </font>
				       </p>
				       
				       </div>    				
    			</div>
    			<div class="span7">
    				<div id="login_div">
    				<br/><br/>
				       <form class="form-signin">
				        <h2 class="form-signin-heading">Please sign in</h2>
				        <input class="input-block-level" placeholder="Username" type="text" id="username">
				        <input class="input-block-level" placeholder="Password" type="password" id="password">
				        <input class="input-block-level" placeholder="Pin" type="password" id="pin">

                        <div id="login_error" class="help-block" style="display: none;">
                            <span class="text-error"><strong>Login Failed: </strong><span></span></span>
                        </div>

				        <button class="btn btn-large btn-primary" type="button" onclick="submitCredentials()">Sign in</button>
				      </form>    	
			      </div>
			      
			      <div id="oob_div">
			      
			      	<%
			      		if(oauthOobResponse != null) {
			    
			      	%>
			     	 <h5>The Request token <%=oauthOobResponse.getRequestToken()%> has verifier <%=oauthOobResponse.getVerifier() %></h5>
			      
			      	<% } %>
			      </div>
			      <div id="loggedin_div">
			      <%if(oauthData != null) {
			      %>
			      		<br/><br/>
				       <form class="form-signin">
				        <h2 class="form-signin-heading">Welcome</h2>
				        <h4><span id="providerName"></span></h4>      
				      </form> 
				      <br/><br/>
                      <h5>The 3rd party application "<%=oauthData.getApplicationName()%>" is requesting access to your OSCAR account.<br>
				          URL for <%=oauthData.getApplicationName() %> is <%=oauthData.getApplicationURI() %>.</h5>

                      <h5>Permissions requested:</h5>
				        <form id="scopeForm" method="post" action="<%=oauthData.getReplyTo()%>;jsessionid=<%=session.getId()%>">
				        	
                            
				        	<%
				        	for(Object p:oauthData.getPermissions()) {
				        		OAuthPermission perm = (OAuthPermission)p;
				        	%>
                            <div class="control group">
                                <div class="controls">
                                    <label class="checkbox">
                                        <input type="checkbox" readonly="readonly" checked="checked"> <%=perm.getDescription()%> <% if ("".equals(perm.getDescription().trim())) { %><em>Permission with no description</em><% } %>
                                    </label>
                                </div>
                            </div>
				        	<% } %>
				        	
				            <input type="hidden" name="session_authenticity_token" value="<%=oauthData.getAuthenticityToken()%>"/>
				            <input type="hidden" name="oauth_token" id="oauth_token" value="<%=oauthData.getOauthToken()%>"/>
				            <input type="hidden" name="oauthDecision" id="oauthDecision" value="allow"/>
                            <input type="submit" value="Authorize <%= oauthData.getApplicationName() %>" class="btn btn-primary"/>
				           	<input type="button" value="Cancel" onClick="deny();" class="btn btn-danger" />
				        </form>
	        		<% } %>
       	
			      </div>			
    			</div>
    		</div>
    	</div>

    </body>
</html>
