<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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

<%@ page errorPage="../error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.billing&type=_billing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.math.BigInteger,java.util.*,org.oscarehr.integration.mcedt.web.DetailDataCustom,org.oscarehr.integration.mcedt.web.ActionUtils" %>

<%    

	List<DetailDataCustom> resourceListDL = (ArrayList<DetailDataCustom>)session.getAttribute("resourceListDL");
	BigInteger resultSize = (BigInteger)session.getAttribute("resultSize");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="../js/jquery-1.7.1.min.js"></script>
<script language="javascript">	
$(window).load(function(){
	$('input[type="checkbox"]').click(function () {
   	 var pass = 5; //5 files at a time
   	 var numOfFiles = $('input[type="checkbox"]:checked').length;
   	 if (numOfFiles == pass) {
        $('input[type="checkbox"]').not(':checked').prop('disabled', true);
   	 } else {
        $('input[type="checkbox"]').not(':checked').prop('disabled', false);
   	 }
   	 if (numOfFiles>0) {
  	  	$("#unSelDL").prop('disabled', false);
  	  	$("#userDL").prop('disabled', false);  	  	
    	} else {
   	 	$("#unSelDL").prop('disabled', true);
   	 	$("#userDL").prop('disabled', true);   	 	
  	  }
	})
	
	$("#unSelDL").click(function(){	
		$('input[type="checkbox"]').filter(':checked').prop('checked', false);
		$('input[type="checkbox"]').filter(':disabled').prop('disabled', false);
		$("#unSelDL").prop('disabled', true);
		$("#userDL").prop('disabled', true);		
	})
});

	function changeDisplayDL(control) {
		//submitFormSent("changeDisplay", control);
		var method = jQuery("#methodDownload");
		method.val('changeDisplay');
		
		var form = jQuery("#formDownload");
		form.submit();
		return true;
	}	
	
	function downloadSelected(control) {
		return submitFormDownload('userDownload', control);
	}			
	
	function submitFormDownload(methodType, control){
		if (control) {
			control.disabled = true;
		}
		
		var method = jQuery("#methodDownload");
		method.val(methodType);
		
		var form = jQuery("#formDownload");
		form.submit();
		return true;
	}
	
</script>
<title>Download</title>
</head>
<body>
	<html:form action="/mcedt/download" method="post" styleId="formDownload">
		<jsp:include page="../messages.jsp" />		
		<input id="methodDownload" name="method" type="hidden" value="" />	
	<div>
	<div>
			Billing Number:
		<html:select property="serviceId" styleId="serviceId"
			styleClass="serviceId">
			<c:forEach var="r" items="${serviceIds}">
            	<html:option value="${r}" >
					<c:out value="${r}" />
				</html:option>
							</c:forEach>
		</html:select>
		Page #:
		<html:select property="pageNo" styleId="pageNo">
			<c:forEach var="i" begin="1"
				end="${resultSize}">
				<html:option value="${i}">
					<c:out value="${i}" />
				</html:option>
			</c:forEach>
		</html:select>
		<button class="noBorder blackBox flatLink font12 small" onclick="ShowSpin(true); return changeDisplayDL();">Load Page</button>
	</div>
	* You may select a maximum of 5 files at a time to download from MC-EDT
	<br />** to process downloads, click <a href="<%= request.getContextPath() %>/billing/CA/ON/viewMOHFiles.jsp">here to view MOH files</a>
	<%
		if(resourceListDL!=null){									
	%>
	<table class="table scrollable whiteBox" width="100%" border="0" cellspacing="0" cellpadding="5" style="margin:5px 0 15px;">
				<thead>
					<tr class="greenBox">							
						<th>Select</th>
						<th>ID</th>
						<th>Date</th>
						<th>Type</th>
						<!-- <th>Result</th>
						<th>Status</th> -->
						<th>File Name</th>
						<th>Status</th>												
					</tr>
				</thead>
				<c:forEach var="r" items="${resourceListDL}" varStatus="loopStatus">						
					<tr bgcolor="${loopStatus.index % 2 == 0 ? '#FFF' : '#EEE'}">
						<td><input type="checkbox" value="${r.resourceID}" name="resourceId" /></td>							
						<td><c:out value="${r.resourceID}" /></td>
						<td>														
							<fmt:formatDate value="${i:toDate(r.createTimestamp)}" pattern="MM/dd/yyyy hh:mm"/>
						</td>
						<td><c:out value="${r.resourceType}" /></td>						
						<td><c:out value="${r.description}" /></td>													
						<td><c:out value="${r.status}" /></td>													
					</tr>						
				</c:forEach>
			</table>
			<%
				}
				else{
			%>		
				<h3>No new documents to download.</h3>
			<%
				}
			%>
	</div>
	<div>
		<button type="button"  id="unSelDL" class="noBorder blackBox flatLink font12 rightMargin5" disabled="true">Un-Select All</button>
		<button type="button"  id="userDL" class="noBorder blackBox flatLink font12" disabled="true" onclick="ShowSpin(true);return downloadSelected();">Download</button>		
	</div>
		
	</html:form>
</body>
</html>
