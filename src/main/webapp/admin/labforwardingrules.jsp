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
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@ page import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList, oscar.oscarLab.ForwardingRules, oscar.OscarProperties"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%

ForwardingRules fr = new ForwardingRules();
String providerNo = request.getParameter("providerNo");
if (providerNo == null)
    providerNo = "0";

ArrayList frwdProviders = fr.getProviders(providerNo);
%>

<html>
<head>

<title><bean:message key="admin.admin.labFwdRules" /></title>

<script type="text/javascript">
                        
            function removeProvider(remProviderNo, providerName){
                var answer = confirm ("Are you sure you would like to stop forwarding labs to "+providerName)
                if (answer){
                    document.RULES.operation.value="remove";
                    document.RULES.remProviderNum.value = remProviderNo;
                    return true;
                }else{
                    return false;
                }
                
            }
            
            function setActionClear(){
                var answer = confirm ("Are you sure you would like to clear the forwarding rules?")
                if (answer){
                    document.RULES.operation.value="clear";
                    return true;
                }else{
                    return false;
                }
            }
            
            function confirmUpdate(){
                <%
                OscarProperties props = OscarProperties.getInstance();
                String autoFileLabs = props.getProperty("AUTO_FILE_LABS");
                if (providerNo.equals("0")){%>                    
                    alert("You must select a provider to set the rules for.");
                    return false;
                <%}else if(autoFileLabs != null && autoFileLabs.equalsIgnoreCase("yes")){%>
                    return confirm ("Are you sure you would like to update the forwarding rules?")
                <%}else{%>
                    if (document.RULES.providerNums.value == '' && document.RULES.status[1].checked && <%= (frwdProviders.size() == 0)%>){
                        alert("You must select a provider to forward the incoming labs to if you wish to automatically file them.");
                        return false;
                    }else{
                        return confirm ("Are you sure you would like to update the forwarding rules?")
                    }
                <%}%>
            }
        </script>

</head>

<body>

<h3><bean:message key="admin.admin.labFwdRules" /></h3>



<form id="ForwardRulesForm" name="RULES" action="${ctx}/admin/ForwardingRules.do" method="post">

<input type="hidden" name="operation" value="update"> 
<input type="hidden" name="remProviderNum" value="">



<div class="well">
<h5>Select Provider</h5>
Please Select the provider to set fowarding rules for:

<select name="providerNo" id="provider-selection">
	<option value="0">None Selected</option>
	<% ArrayList providers = ProviderData.getProviderList();
                                            for (int i=0; i < providers.size(); i++) { 
                                            String prov_no = (String) ((ArrayList) providers.get(i)).get(0);%>
	<option value="<%= prov_no %>"
		<% if (prov_no.equals(providerNo)) {%> <%="selected"%> <%}%>><%= (String) ((ArrayList) providers.get(i)).get(1) %>
	<%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
	<% }%>
</select> 

<i class="icon-question-sign"></i> <oscar:help keywords="lab forwarding" key="app.top1"/>
<br>
				
</div>

		
		
<div class="well">	
<h5>Current Forwarding Rules</h5>
<%
String status = "N";
if(providerNo.equals("0")){
%>
	<p>No provider has been selected.</p>
<%	
}else if(!fr.isSet(providerNo)){%>
<p class="text-info">There are no forwarding rules set</p>
<%}else{
status = fr.getStatus(providerNo);
%>



<%if (frwdProviders != null && frwdProviders.size() > 0) {%>
<table class="table table-condensed table-striped" style="width:44%;">
    
 <thead>
 <tr>
 <th>Provider</th>
 <th>Incoming Status</th>
 <th></th>
 </tr>
 </thead>   

<tbody>
<%for (int i=0; i < frwdProviders.size(); i++){%> 
<tr> 
<td><%= (String) ((ArrayList) frwdProviders.get(i)).get(1) %> <%= (String) ((ArrayList) frwdProviders.get(i)).get(2) %></td>
<td><%= status.equals("N") ? "New" : "Filed" %></td>
<td><button type="submit" class="btn btn-small" onclick="return removeProvider('<%= (String) ((ArrayList) frwdProviders.get(i)).get(0) %>', '<%= StringEscapeUtils.escapeJavaScript((String) ((ArrayList) frwdProviders.get(i)).get(1)) %> <%= StringEscapeUtils.escapeJavaScript((String) ((ArrayList) frwdProviders.get(i)).get(2)) %>')" title="remove provider"><i class="icon-trash"></i> remove</button></td>
</tr> 

<br />
<%}%>

</table>
<%}else{%>



    <div class="alert alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong>Warning!</strong> The incoming labs are not being forwarded.
    </div>
        

<%}%>
<br />
<button type="submit" class="btn btn-danger" onclick="return setActionClear()"><i class="icon-trash"></i> Clear All Forwarding Rules</button>

<%}%>

</div>			
			
			
	
	<div class="well">		

				<h5>Update Forwarding Rules</h5>
			
				Set incoming status:
				<input type="radio" name="status" value="N"	<%= status.equals("F") ? "" : "checked" %>> <bean:message key="oscarMDS.search.formReportStatusNew" /> 
				<input type="radio" name="status" value="F" <%= status.equals("F") ? "checked" : "" %>> Filed
				
				<br />
				
				Forward incoming reports to the	following providers:<br />
				
				<small>(Hold 'Ctrl' to select multiple providers)</small>
				<br />
				
				<select multiple name="providerNums" style="height: 200px">
					<optgroup
						label="&#160&#160Doctors&#160&#160&#160&#160&#160&#160&#160&#160">
						<% //ArrayList providers = ProviderData.getProviderList();
                                                for (int i=0; i < providers.size(); i++) { 
                                                String prov_no = (String) ((ArrayList) providers.get(i)).get(0);
                                                if ( !providerNo.equals(prov_no) && !frwdProviders.contains(providers.get(i))){%>
						<option value="<%= prov_no %>"><%= (String) ((ArrayList) providers.get(i)).get(1) %>
						<%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
						<% }
                                                } %>
					</optgroup>
				</select>
			
<br />
<input type="submit" class="btn btn-primary" value="Update" onclick="return confirmUpdate()"> 
	
		</div>

</form>

</body>

<script>
var pageTitle = $(document).attr('title');
$(document).attr('title', 'Administration Panel | Lab Forwarding Rules');

registerFormSubmit('ForwardRulesForm', 'dynamic-content');

$("#provider-selection").change(function(e) {
	e.preventDefault();
	$("#dynamic-content").load('${ctx}/admin/labforwardingrules.jsp?providerNo='+$("#provider-selection").val(), 
		function(response, status, xhr) {
	  		if (status == "error") {
		    	var msg = "Sorry but there was an error: ";
		    	$("#dynamic-content").html(msg + xhr.status + " " + xhr.statusText);
			}
		}
	);
});




</script>
</html>
