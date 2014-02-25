<!DOCTYPE html>
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

<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarBilling.ca.bc.Teleplan.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<% TeleplanUserPassDAO dao = new TeleplanUserPassDAO();
   String superUser = (String) session.getAttribute("user");
%>
<!DOCTYPE html>
<html:html locale="true">

<head>
<title><bean:message key="admin.admin.manageTeleplan"/></title>
<html:base/>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<style type="text/css">
	input[type="submit"] {
		margin-bottom:10px;
	}
</style>

</head>

<body>
	<h3><bean:message key="admin.admin.manageTeleplan"/></h3>
    
    <div class="container-fluid well well-small">

    <div class="alert alert-info">
		Teleplan
		<%
		TeleplanSequenceDAO seq = new TeleplanSequenceDAO();
		oscar.OscarProperties op = oscar.OscarProperties.getInstance();
		%>
		Last Sequence # = <%=seq.getLastSequenceNumber()%>   Current Datacenter # = <%=op.getProperty("dataCenterId","Not Set")%>
    </div>

	
	    <%if (request.getAttribute("error") != null) { %>
	    <div class="alert alert-error"> 
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<%=request.getAttribute("error")%>
	    </div>
	    <%}%>
	    
	    <oscar:oscarPropertiesCheck property="BILLING_SUPERUSER" value="<%=superUser%>" >
            <h4>Manually Set Sequence #</h4>
                <html:form action="/billing/CA/BC/ManageTeleplan">
                    <input type="hidden" name="method" value="setSequenceNumber"/> 
                    Sequence #: <input type="text" name="num"/>
                    <input class="btn" type="submit" value="save" />
                </html:form>
	    </oscar:oscarPropertiesCheck>
	   
	    <oscar:oscarPropertiesCheck property="BILLING_SUPERUSER" value="<%=superUser%>" >
	      <h4>Set Teleplan UserName Password</h4>
	        <html:form action="/billing/CA/BC/ManageTeleplan">
	             <input type="hidden" name="method" value="setUserName"/>
	             Username: <input type="text" name="user"/>
	             Password: <input type="password" name="pass" />
	             <input class="btn" type="submit" value="save" />
	        </html:form>
	   </oscar:oscarPropertiesCheck>
	   
	   <% if ( dao.hasUsernamePassword()){ %> 
	   
		<oscar:oscarPropertiesCheck property="BILLING_SUPERUSER" value="<%=superUser%>" >
	      <h4>Get Teleplan Sequence #</h4>
	        <html:form action="/billing/CA/BC/ManageTeleplan">
	             <input type="hidden" name="method" value="getSequenceNumber"/>
	             <input class="btn" type="submit" value="Get Teleplan Sequence #" />
	        </html:form>
	   	</oscar:oscarPropertiesCheck>
	   
		<h4>Update Billing Codes</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="updateBillingCodes"/>
             <input class="btn" type="submit" value="update" />
        </html:form>
	
      	<h4>Update Explanatory Codes</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="updateExplanatoryCodesList"/>
             <input class="btn" type="submit" value="update" />
        </html:form>
	
      	<h4>Update MSP ICD9 Codes</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="updateteleplanICDCodesList"/>
             <input class="btn" type="submit" value="update" />
        </html:form>
	    
      	<h4>Change Teleplan Password</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="changePass"/>
             Current Password: <input type="password" name="oldpass" />
             <br>
             New Password:     <input type="password" name="newpass" />
             Confirm Password: <input type="password" name="confpass" />
             <input class="btn" type="submit" value="save" />
        </html:form>
	   
      	<h4>Set Teleplan Password</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="setPass"/>
             New Password:     <input type="password" name="newpass" />
             <input class="btn" type="submit" value="save" />
        </html:form>
	   
      	<h4>Get Remittance</h4>
        <html:form action="/billing/CA/BC/ManageTeleplan">
             <input type="hidden" name="method" value="remit"/>
             <input class="btn" type="submit" value="Get Remittance" />
        </html:form>
	    <%}%> 
	     
	    </form>
    </div>
</body>
</html:html>
