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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DrugDao"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.Drug"%>
<%@page import="oscar.oscarRx.data.RxPrescriptionData"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.oscarRx.StaticScriptBean"%>
<%@page import="oscar.oscarRx.util.RxUtil" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.apache.struts.util.LabelValueBean" %>
<%@page import="org.oscarehr.common.model.DrugProduct" %>
<%@page import="org.oscarehr.common.model.DrugDispensing" %>
<%@page import="java.util.Map" %>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_dispensing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_dispensing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<title>Dispensary</title>

<html:base />

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="../styles.css">

<%
	String curUser_no = (String) session.getAttribute("user");
	Demographic demographic = (Demographic)request.getAttribute("demographic");
	Drug drug = (Drug)request.getAttribute("drug");
	List<Provider> providers = (List<Provider>)request.getAttribute("providers");
	List<Object[]> products =  (List<Object[]>)request.getAttribute("products");
	Map<String,String> providerNames = (Map<String,String>)request.getAttribute("providerNames");
	Map<Integer,String> details = (Map<Integer,String>)request.getAttribute("details");
	Map<Integer,Integer> productAmounts = (Map<Integer,Integer>)request.getAttribute("productAmounts");
	
	Integer totalDosesAvailable = null;
	String strTotalDosesAvailable = "<Unknown>";
	try {
		int quantity = Integer.parseInt(drug.getQuantity());
		totalDosesAvailable  = quantity + (quantity * drug.getRepeat());
		strTotalDosesAvailable = totalDosesAvailable.toString();
	}catch(NumberFormatException e){
		//that didn't work
	}
	
	int totalDosesDispensed = 0;
	int totalDispensingEvents = ((List<DrugDispensing>)request.getAttribute("dispensingEvents")).size();
	int totalQuantitiesDispensed = 0;
	
	for(DrugDispensing dd:(List<DrugDispensing>)request.getAttribute("dispensingEvents")) {
		if(dd.isArchived()) {
			continue;
		}
		totalDosesDispensed += productAmounts.get(dd.getId());
		totalQuantitiesDispensed += dd.getQuantity();
	}
	
	Integer totalDosesRemaining = (totalDosesAvailable==null)?null:new Integer(totalDosesAvailable-totalDosesDispensed);
	String strTotalDosesRemaining = (totalDosesRemaining==null)?"<Unknown>":String.valueOf(totalDosesRemaining);
	
	String selectedProductCode = (String)request.getAttribute("selectedProductCode");
	
	
%>

<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>

<script>
var pAmounts = {};

function updateLots() {
	$.getJSON(
			"<%= request.getContextPath() %>/oscarRx/Dispense.do?method=findDistinctLotsAvailableByCode&code=" +  $("#product").val(),
	    	function(data,textStatus){
				var html = "";
				for(var x=0;x<data.length;x++) {
					html += "<tr><td><input type='button' value='-' name='remove' id='remove_"+data[x].hash+"' />&nbsp;<input type='input' length='20' readonly='readonly' name='lot' id='lot_"+data[x].hash+"' amount='"+data[x].amount+"' value='"+data[x].name+ " - " + data[x].amount + " - "+ "(" + data[x].expiryDateAsString  + ")" + "' style='text-align:center'>&nbsp;<input type='button' value='+' name='add' id='add_"+data[x].hash+"'/>&nbsp;<span id='current_"+data[x].hash+"'>0</span>/<span id='max_"+data[x].hash+"'>"+data[x].numberAvailable+"</span>&nbsp;<span id='error_"+data[x].hash+"' style='color:red'></span></td></tr>";
				}
				$("#td_lots").html(html);
			}
	);
}
var amountsSelected = new Object(); 


function getCurrentDoses() {
	var total = 0;
	
	$("input[name='lot']").each(function(){
		var hash = $(this).attr('id').substring("lot_".length);
		var amount = $(this).attr('amount');
		var qty = amountsSelected[hash];
		if(qty == undefined) qty = 0;
		total += (qty * amount);
	});
	
	return total;
}

function clearErrors() {
	$("input[name='lot']").each(function(){
		var hash = $(this).attr('id').substring("lot_".length);
		$("#error_"+hash).html("");
	});
}

function postError(hash,msg) {
	$("#error_"+hash).html(msg);
}

function doSubmit() {
	//we need to have a list of form parameters that specify what has been chosen.
	//so basically a list of qty/hash
	
	if(getCurrentDoses() == 0) {
		alert('You have not chosen to dispense any product');
		return false;
	}
	
	if($("select[name='dispensedBy']").val() == 'Select Below') {
		alert('You must choose a dispensing provider');
		return false;
	}

	
	$("input[name='lot']").each(function(){
		var hash = $(this).attr('id').substring("lot_".length);
		var amount = amountsSelected[hash];
		if(amount == undefined) amount = 0;
		
		$("form").append("<input type='hidden' name='dispense_"+hash+"' value='"+amount+"'/>"); 
		
	});
	$("#productCode").val($("#product").val());
	
}

$(document).ready(function(){
	
	$("input[name='remove']").live('click',function(){
		clearErrors();
		
		var hash = $(this).attr('id').substring("remove_".length);
		//alert(hash);
		if(amountsSelected[hash] == undefined) {
			amountsSelected[hash] = 0;
		} else {
			if(amountsSelected[hash] == 0)
				return;
			amountsSelected[hash] = amountsSelected[hash] - 1;
			$('#current_'+hash).html(amountsSelected[hash]);
		}
	}); 
	
	$("input[name='add']").live('click',function(){
		clearErrors();
		
		var hash = $(this).attr('id').substring("add_".length);
		if(amountsSelected[hash] == undefined) {
			amountsSelected[hash] = 0;
		}
		
		//can we add this as a dose?
		var amount = parseInt($("#lot_"+hash).attr('amount'));
		var currentDoses = parseInt(getCurrentDoses());
		
		if((currentDoses + amount) <= <%=totalDosesAvailable%>) {
			var maxAvailable = parseInt($("#max_"+hash).html());
			if((amountsSelected[hash]+1)<=maxAvailable) {
				amountsSelected[hash] = amountsSelected[hash] + 1;
			} else {
				postError(hash,'No more units available');
			}
		} else {
			postError(hash,'No more doses available under this prescription.');
		}
		
		
		$('#current_'+hash).html(amountsSelected[hash]);
	
		
	}); 
	
	$("#quantity,#product").bind('change',function(){
		if($("#product").val() == '' || $("#quantity").val() == '0') {
			$("#td_lots").html("");
			return;
		}
		$("#current_doses").html('0');
		
		updateLots();

	}); //close the bind
	
	
	
	<%
	if(selectedProductCode != null) {
		
		%>
		$("#product").val('<%=selectedProductCode%>');
		$("#product").attr("disabled", true); 
		updateLots();
		<%
	}
	%>
}); //ready


function updateCurrentDoses() {
	var x=0;
	var arr = new Array();
	
	var x=0;
	var arr = new Array();
	
	while(true) {
		if($("#lot"+x).length == 0) {
			break;
		}
		var curLot = $("#lot"+x).val();
		if($.inArray(curLot, arr) == -1) 
			arr.push(curLot);
		
		x++;
		
	}
	
	
	var totalDosesBeingAsked=0;
	for(var i=0;i<arr.length;i++) {
		if(arr[i].length>0) {
			totalDosesBeingAsked += parseInt(pAmounts[arr[i]]);
		}
	}
	$("#current_doses").css('color','black');
	<%if(totalDosesAvailable != null){%>
	if(totalDosesBeingAsked > <%=totalDosesAvailable%>) {
		$("#current_doses").css('color','red');
	}
	<% } %>
	$("#current_doses").html(totalDosesBeingAsked+'');
}



function validateLotNumbers() {
	//check if we can dispense this much
	
	//generate the list of DrugProduct ids chosen. We then need to check the amount on each, and total it up, for the
	//total doses they are requesting.
	
	var x=0;
	var arr = new Array();
	
	while(true) {
		if($("#lot"+x).length == 0) {
			break;
		}
		var curLot = $("#lot"+x).val();
		if(curLot.length>0) {
			//make sure no duplicates
			if($.inArray(curLot, arr) == -1) 
				arr.push(curLot);
		}
			
		x++;
	}
	
	//check for duplicates
	if($("#quantity").val() != arr.length) {
		alert('Please choose a unique item to dispense for each row presented.');
		return false;
	}
	
	
	
	var totalDosesBeingAsked=0;
	for(var i=0;i<arr.length;i++) {
		totalDosesBeingAsked += pAmounts[arr[i]];
	}
	
	if(totalDosesBeingAsked > <%=totalDosesRemaining%>) {
		alert('You may only dispense a maximum of <%=totalDosesRemaining%> doses for this prescription');
		
		return false;
	}
	
	//if the product is disabled, it doesn't send it in the query..here's a backup
	$("#productCode").val($("#product").val());
	
	return true;
	
	/*
	arr.sort();
	var last = arr[0];
	for (var i=1; i<arr.length; i++) {
	   if (arr[i] == last) {
		   alert('You cannot choose to dispense multiple products with the same lot#');
		   return false;
	   }
	   last = arr[i];
	}
	*/
	return true;
	
}

	function deleteDispensingEvent(id) {
		if(confirm("Are you sure you want to delete this record?")) {
			location.href="<%=request.getContextPath()%>/oscarRx/Dispense.do?method=delete&eventId=" + id + "&id=" + <%=request.getAttribute("id")%>;
		}
	}
</script>

</head>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
	<%@ include file="../TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top">
		<table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
			<tr>
				
			</tr>
			<!----Start new rows here-->

			<tr>
				<td style="font-size: small;">
					<table>
						<tr>
							<td><b>Patient Name:</b></td>
							<td><%=demographic.getFormattedName() %></td>
						</tr>
						<tr>
							<td><b>Entered Date:</b></td>
							<td><%=oscar.util.UtilDateUtilities.DateToString(drug.getCreateDate())%></td>
						</tr>
						<tr>
							<td><b>Medication:</b></td>
							<td><%=RxPrescriptionData.getFullOutLine(drug.getSpecial()).replaceAll(";", " ") %></td>
						</tr>
						<tr style="height: 8px;">
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td><b>
							<%
								String status="Active";
							if(totalDosesRemaining != null && totalDosesRemaining > 0) {
								status = "Active";
							} else {
									status="Filled";
							}
							if(status.equals("Active") && drug.isExpired()) {
								status="Expired";	
							}
							%>
							<%="Status:" + status %>
							</b></td>
							<td>
								
							</td>
						</tr>
					</table>
					
				</td>
			</tr>
			<tr style="height: 14px;">
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<h4>Prior dispensing events for this medication:</h4>
					<table style="width:100%" border="1">
						<thead>
							<tr style="text-align:center">
								<th></th>
								<th>Date</th>
								<th>Quantity</th>
								<th>Dispensed By</th>
								<th>Recorded By</th>
								<th>LOT/Expiry Dates (s)</th>
								<th>Notes</th>
							</tr>
						</thead>
						
						<tbody>
						<%
						for(DrugDispensing dd:(List<DrugDispensing>)request.getAttribute("dispensingEvents")) {
							if(dd.isArchived()) {
								continue;
							}
						%>
							<tr valign="top">
								<td>
									<security:oscarSec roleName="<%=roleName$%>" objectName="_dispensing" rights="w">
										<a href="javascript:void(0)" onClick="deleteDispensingEvent('<%=dd.getId()%>')"><img border="0" src="<%=request.getContextPath()%>/images/delete.png"/></a>
									</security:oscarSec>
								</td>
								<td><%=dd.getDateCreatedAsString() %></td>
								<td style="text-align:right"><%=dd.getQuantity() %></td>
								<td><%=providerNames.get(dd.getDispensingProviderNo()) %></td>
								<td><%=providerNames.get(dd.getProviderNo()) %></td>
								
								<td><%=details.get(dd.getId()) %></td>
								<td><%=dd.getNotes() %></td>
							</tr>
						<% } %>
						</tbody>
					</table>
				</td>
			</tr>
			<tr style="height:15px">
				<td></td>
			</tr>
			<tr style="height:15px">
				<td>
				There are <%=strTotalDosesAvailable%> doses available in this prescription.
				<br/>
				<%=totalDosesDispensed %> doses have already been dispensed, leaving <%=strTotalDosesRemaining%> doses available.
				<br/>
				
				</td>
			</tr>
			
			<%if((totalDosesRemaining != null && totalDosesRemaining > 0) /*  && !drug.isExpired() */) { %>
			<tr style="height:15px">
				<td></td>
			</tr>
			<tr>
				<td>Current doses in this dispensing event:<span id="current_doses">0</span></td>
			</tr>
			<tr style="height:15px">
				<td></td>
			</tr>
			<tr>
				<td>
					
					<h3>Create a new dispensing event for this prescription</h3>
					<form action="<%=request.getContextPath()%>/oscarRx/Dispense.do">
					<input type="hidden" name="method" value="saveEvent"/>
					<input type="hidden" name="drugId" value="<%=drug.getId()%>"/>
					<input type="hidden" name="productCode" id="productCode" value=""/>
						<table>
							<tr>
								<td>Product:</td>
								<td>
									<select name="product" id="product">
										<option value="">Select Below</option>
										<%for(Object[] p:products) { %>
											<option value="<%=p[0]%>"><%=p[1] %></option>
										<% } %>
									</select>
								</td>
							</tr>
							
							<tr>
								<td valign="top">Lots Available</td>
								<td id="td_lots">
									
								</td>
								
							</tr>
							
							<tr>
								<td>Dispensed By:</td>
								<td>
									<select name="dispensedBy">
										<option value="Select Below"/>
										<%for(Provider p:providers) { %>
											<option value="<%=p.getProviderNo()%>"><%=p.getFormattedName() %></option>
										<% } %>
									</select>
								</td>
							</tr>
							<tr>
								<td>Notes:</td>
								<td><input name="notes" type="text"/></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="submit" value="Save" class="ControlPushButton" onclick="return doSubmit();"/>
								</td>
							</tr>
							
						</table>
					</form>
				</td>
			</tr>
			<%} %>
			<tr>
				<td><br />
				<br />
				<input type="button" value="Close" class="ControlPushButton" onclick="javascript:window.close();" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
	</tr>
</table>

</body>
</html:html>
