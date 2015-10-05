<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%--
	Author: Dennis Warren
	Date: April 2012
	
	Modified: June 2012
	- added advanced billing options.
 --%>
 
 <%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@page import="java.util.*, oscar.util.*, 
	org.springframework.web.context.support.WebApplicationContextUtils, 
	org.springframework.web.context.WebApplicationContext" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>Oscar Quick Billing</title>


<link rel="stylesheet" href="<c:out value="${ oscar_context_path }/css/bcbilling.css" />" type="text/css" media="screen" />
<link rel="stylesheet" href="<c:out value="${ oscar_context_path }/css/quickBillingBC.css" />" type="text/css" media="screen" />
<link rel="stylesheet" href="<c:out value="${ oscar_context_path }/css/cupertino/jquery-ui-1.8.18.custom.css" />" type="text/css" />

<link rel="stylesheet" href="<c:out value="${ oscar_context_path }/css/jquery.ui.autocomplete.css" />" type="text/css" />
<style type="text/css">.ui-autocomplete-loading { background: white url('images/ui-anim_basic_16x16.gif') right center no-repeat; }
	</style>
<script type="text/javascript" src="<c:out value="${ oscar_context_path }/js/jquery-1.7.1.min.js" />" ></script>

<!-- script type="text/javascript" src="<c:out value="${ oscar_context_path }/js/jquery-ui/development-bundle/ui/jquery.ui.position.js" />" ></script>
<script type="text/javascript" src="<c:out value="${ oscar_context_path }/js/jquery-ui/development-bundle/ui/jquery.ui.widget.js" />" ></script -->

<script type="text/javascript" src="<c:out value="${ oscar_context_path }/js/jquery-ui-1.8.18.custom.min.js" />" ></script>

<script type="text/javascript">

$(document).ready(function() {	

	// prepare the document elements when the DOM is ready.
	// cool moving stuff...
	if(!<c:out value="${quickBillingBC.isHeaderSet}" />) {
		$("#apeture").hide();
		$("#toolBar").hide();
		$("#inputList").hide();
	}
	
   if(<c:out value="${quickBillingBC.isHeaderSet}" />) {
		// lock out the header. 
		$("#provider").attr("disabled", "disabled");
		$("#visitLocation").attr("disabled", "disabled");
		$("#visitDate").attr("disabled", "disabled");

		//set focus back to patient name after each entry.
		$("#ptName").focus();
	}
   
	if(<c:out value="${not empty requestScope.saved}" />) {
		$("#saved").slideDown("fast");
	} 
	
	// date picker function. Don't want it to run 
	// until other elements are chosen.
	$('#visitDate').datepicker({
			dateFormat: "dd-mm-yy",
			beforeShow: function(input, inst) {	
				if($("#provider :selected").val() == "empty") {	
					
					//$(this).datepicker( "hide" )
					alert("Select Billing Physician");
					 $("#provider").focus();
					 
				} else if ($("#visitLocation :selected").val() == "empty") {
					
					//$(this).datepicker( "hide" )
					alert("Select Service Location");
					$("#visitLocation").focus();
				} 
			},
			onClose: function(input, inst) {
				
				$("#apeture").slideDown("fast");
				
				if($("#saved").is(":visible")){
					
					$("#saved").slideUp("fast");
				}
				
				$("#ptName").focus();
				
			}
	});
	
	// serialize for insertion into JSON 
	$.fn.serializeObject = function()
	{
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
	
	
	// for adding patient to billing list. Also does error checking.
	$("#addDemo").click( 
		function() {

		   if(($("#ptNumber").val() == "")||($("#ptNumber").val() == null)) { 
			   
			   alert("Patient not in database.");
			   $("#ptName").focus();
			   return;
			   
		   } else if(($("#billingCode").val() == "")||($("#billingCode").val() == null)) {
				  
			   alert("Billing Code missing.");
			   $("#billingCode").focus();
			   return;
			   
		   } else if(($("#dxCode1").val() == "")||($("#dxCode1").val() == null)) {
				  
			   alert("DX code missing");
			   $("#dxCode1").focus();
			   return;
			   
		   } else if (
				   ($("#halfBilling").val() == "")||
				   ($("#halfBilling").val() == null)||
				   ($("#halfBilling").val() <= 0)||
				   ($("#halfBilling").val() >= 3)
		   ) { 
			   
			   alert("Check unit value for accuracy.");
			   $("#halfBilling").focus();
			   return;
			   
		   } else { 
			   
			   var path = "<c:out value="${ oscar_context_path }" />/quickBillingBC.do";
			   var data = JSON.stringify($(document.quickBillingForm).serializeObject()); 
			   
			   $("#quickBillingForm").attr("action", path + "?data=" + data);
			   $("#quickBillingForm").submit();

		   }
		       
	});
	
	// for incremental patient search
	$( "#ptName" ).autocomplete({		
		source: function(request, response) {
		  $.ajax({
		   	url: "<c:out value="${ oscar_context_path }" />/demographic/SearchDemographic.do?query=" + encodeURIComponent(request.term),
			method: "POST",
		   	dataType: "json",
		   	success: function(data) {
			    response($.map(data.results, function(item) {
				  if(item.status == "AC") {    
			    	return {
					      label: item.formattedName + " : " + item.fomattedDob,
					      value: item.formattedName,
					      id: item.demographicNo
				     }
				  } else {
					  return;
				  }
		    }))
		   }
		  })
		 },
		 minLength: 2,
		 select: function( event, ui ) {

			 		ui.item.id ?
			 			$("#ptNumber").val(ui.item.id)
			 		:
			 			alert("No demographic number!")
			 		;

		 },
		 open: function() {
		  	$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		 },
		 close: function() {
		  	$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		 }

	});	
	
		
	// open advanced input fields.
	//$("#advancedLink").click(function() {
	//	$("#advancedInput").show();
	//});
	

}); // end document ready


// removes an entry from the add invoice list.
function removeBill(bill) {
	
	   var path = "<c:out value="${ oscar_context_path }" />/quickBillingBC.do";
	   var data = "?remove=" + bill; 
	   
	   $("#quickBillingForm").attr("action", path + data);
	   $("#quickBillingForm").submit();
	
}

</script>

</head>
<body id="quickBilling" >

	<div id="heading" class="billingHeading" >
		<h1>BC MSP Quick Billing</h1>
	</div>
	
	<form action="<c:out value="${oscar_context_path}" />/saveQuickBillingBC.do" 
			id="quickBillingForm" 
			name="quickBillingForm"
			method="POST" 
			class="bgLightLilac">
			
		<div id="saved" style="display:none;">			
			<c:out value="${requestScope.saved}" /> Invoice(s) Saved
		</div>
		
		<div id="header" class="bgLilac" >
			<ul>
				<li>
				<bean:message key="billing.provider.billProvider"/>
				
				<select id="provider" name="provider" >	
					<option value="empty" >- Select Provider -</option>	
					<c:forEach var="provider" items="${ quickBillingBC.providerList }" >
						
					<c:if test="${not empty provider.ohipNo}" >
					
							<option value="<c:out value="${ provider.id}" />" 
									id="<c:out value="${ provider.id }" />" 
									<c:if test="${provider.id eq quickBillingBC.billingProviderNo}" >
											selected="selected"
									</c:if> >
														
									<c:out value="${ provider.firstName }" />
									<c:out value="${ provider.lastName }" />									
							</option>
														
					</c:if>
					</c:forEach>
				</select>
				</li>
				
				<li>Service Location
					<select id="visitLocation" name="visitLocation" >	
					<option value="empty" >- Service Location -</option>	
					
					<c:forEach var="visitType" items="${ quickBillingBC.billingVisitTypes }"  varStatus="loop" >	
							
							<option value="<c:out value="${ visitType.visitType }" />" 
									id="<c:out value="${ visitType.visitType }" />" 
									<c:if test="${quickBillingBC.visitLocation eq visitType.visitType}" >
										selected="selected"
									</c:if> >
													
									<c:out value="${ visitType.displayName }" />
									
							</option>
							
					</c:forEach>
					
				</select>
				</li>
				
				<li>
					Visit Date
					<input 	type="text" 
							id="visitDate" 
							name="visitDate" 
							size="10" 
							maxlength="10" 
							value="<c:out value="${quickBillingBC.serviceDate}" />" /> 
				</li>
			</ul>
		</div>
		
		<div id="apeture">
			<div id="ptInput">
				<ul>
				
					<li>
						Pt. Name (last, first)
						<input id="ptName"  name="ptName" type="text" size="30" maxlength="60" />
						<input type="hidden" id="ptNumber" name="ptNumber" />
					</li>	
					
					<li>Billing Code 
						<input type="text" id="billingCode" name="billingCode" size="5" maxlength="6" />
					</li>
					
					<li>Unit <input type="text" id="halfBilling" name="halfBilling" value="1.0" size="3" maxlength="3" /></li>
					
					<li>DX Code <input type="text" id="dxCode1" name="dxCode1" size="4" maxlength="6" /></li>
					
					<li><input type="button" id="addDemo" name="addDemo" value="add" /></li>
								
				</ul>
				
				<%-- a href="#" id="advancedLink">advanced</a>
				<div id="advancedInput" >
			
				</div --%>						
			</div>
			
			<div id="inputList">
				<table>				
					<tr><th colspan="5" >
					
						<span id="tableDate"><c:out value="${quickBillingBC.serviceDate}" /></span> 
						<span id="tableName"><c:out value="${quickBillingBC.billingProvider}" /></span>
						
					</th></tr>
				
					<tr class="bgLilac"><th>HIN</th><th>Pt.Name</th><th>Service</th><th>Total</th><th></th></tr>

					<c:forEach var="billData" items="${quickBillingBC.billingData}" varStatus="loop" >
						
						<c:set value="noErrorStyle" var="classStyle" />
						
						<%-- 
							Check each value for errors 
							Errors are still displayed for review. The styling is changed to 
							indicate a possible error.
						--%>
						
						<c:choose>
							<c:when test="${empty billData.patientPHN}" >																 
								<c:set value="HIN not found!" var="hin" scope="page" />
								<c:set var="classStyle" value="errorStyle" />												 
							</c:when>
							<c:otherwise>
								<c:set value="${billData.patientPHN}" var="hin" scope="page" />
								<c:set var="classStyle" value="noErrorStyle" />
							</c:otherwise>
						</c:choose>	
												
						<tr>
							<td class="<c:out value="${classStyle}" />" > 
								<c:out value="${pageScope.hin}" />
							</td>
							<td> 
								<c:out value="${billData.patientLastName}" />, 
								<c:out value="${billData.patientFirstName}" /> 
							</td>
						
							<c:forEach var="billItem" items="${billData.billItem}" >
							
								<c:choose>
									<c:when test="${empty billItem.description}" >
										<c:set value="Billing Code Not Found" var="servicecode" scope="page" />
										<c:set var="classStyle" value="errorStyle" />
									</c:when>
									<c:otherwise>
										<c:set value="${billItem.description}" var="servicecode" scope="page" />
										<c:set var="classStyle" value="noErrorStyle" />
									</c:otherwise>
								</c:choose>

								<td class="<c:out value="${classStyle}" />" > 
								 
									<c:out value="${billItem.serviceCode}" />: 
									<c:out value="${pageScope.servicecode}" />
									
								</td>
																
							</c:forEach>
							
							<c:choose>
								<c:when test="${billData.grandtotal <= '0.00'}" >
									<c:set var="classStyle" value="errorStyle" />
								</c:when>
								<c:otherwise>
									<c:set var="classStyle" value="noErrorStyle" />
								</c:otherwise>
							</c:choose>	
							
							<td class="<c:out value="${classStyle}" />" > 
							
								<fmt:formatNumber type="currency" 
									groupingUsed="true" 
									minFractionDigits="2" 
									currencySymbol="$"
									value="${billData.grandtotal}" />
									
							</td>
	
							<td>

								<a 	id="removeBill"  
									onclick="javascript:removeBill('<c:out value="${loop.index}" />')" >
									remove
								</a>
							
							</td>
							<td>
								<c:out value="${ billItem.status }" />
							</td>
						</tr>

					</c:forEach>	
				</table>
			</div>
		</div>	
		<div id="toolBar" class="bgLilac">
			<input type="button" id="submitList" name="submitList" value="Submit" onclick="javascript:form.submit();" />
			<input type="button" id="cancelTrans" name="cancelTrans" value="Cancel" onclick="javascript:window.close();" />
		</div>
	</form>
</body>
</html>
