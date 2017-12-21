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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
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
<%@page import="org.apache.commons.lang.StringUtils"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();	
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR Products</title>
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


<link href="<%=request.getContextPath() %>/library/bootstrap2-datepicker/datepicker3.css" rel="stylesheet">

<script src="<%=request.getContextPath() %>/library/bootstrap2-datepicker/bootstrap-datepicker.js"></script>
 
<style>
.red{color:red}

</style>

<script>

	var currentPage = 1;
	var pageSize = 10;
	
	var templates = new Array();
	
	
	$(document).ready(function(){
		$(".help-inline").hide();
	});
	
	
	function updatePageSize() {
		pageSize = $("#selPageSize").val();
		currentPage = 1;
		
		console.log('updating page size to ' + pageSize);
		updatePage();
	}
	
	function deleteProduct(productId) {
		if(confirm("Are you sure you want to delete this?")) {
			jQuery.getJSON("../../ws/rs/productDispensing/deleteDrugProduct/" + productId, {},
			        function(xml) {
						if(xml.message) {
							updatePage();
						} else {
			        		alert('error deleting drug products');
						}
			        });
		}
	}
	
	function editProduct(productId) {
		jQuery.getJSON("../../ws/rs/productDispensing/drugProduct/"+productId, {},
        function(xml) {
			if(xml.drugProduct) {
				var drugProduct;
				if(xml.drugProduct instanceof Array) {
					drugProduct = xml.drugProduct[0];
				} else {
					drugProduct = xml.drugProduct;
				}
				
					
				$('#productName').val(drugProduct.name);
				$('#productCode').val(drugProduct.code);
				$('#productLocation').val(drugProduct.location);
				$('#productLot').val(drugProduct.lotNumber);
				$('#productExpiryDate').val(dateToYMD(drugProduct.expiryDate));
				$('#productAmount').val(drugProduct.amount);
				$('#productId').val(drugProduct.id);
				$('#totalEntriesToCreateGroup').hide();
				$('#new-product').dialog('open');
				
				fetchCurrentNamesAndCodes();
				
			}
        });
	
	}
	
	function addNewProduct() {	
		$('#productName').val('');
		$('#productCode').val('');
		$('#productLocation').val('');
		$('#productLot').val('');
		$('#productExpiryDate').val('');
		$('#productAmount').val('1');
		$('#productId').val('0');
		$('#productBulkTotal').val('1');
		$('#totalEntriesToCreateGroup').show();
		$('#new-product').dialog('open');
		
		fetchCurrentNamesAndCodes();
	}
	
	

	function fetchCurrentNamesAndCodes() {
		jQuery.getJSON("../../ws/rs/productDispensing/drugProductTemplates", {},
		        function(xml) {
					$("#productNameTemplate option").remove();
					if(xml.templates) {	
						templates = xml.templates;
						$("#productNameTemplate").append("<option value=\"\"></option>");
						for(var x=0;x<xml.templates.length;x++) {
							//Line below needs to be commented out to work for Google Chrome
							//console.log(xml.templates[x].toSource());
							$("#productNameTemplate").append("<option value=\""+xml.templates[x].name+"\">"+xml.templates[x].name+"</option>");
							
						}
					}
		        });
	}

	function copyNameFromTemplate() {
		var name = $("#productNameTemplate").val();
		if(name == '') {
			$("#productName").val('');
			$("#productCode").val('');
			$("#productAmount").val('');
			$("#productName").attr("disabled", false);
			$("#productCode").attr("disabled", false);
			$("#productAmount").attr("disabled", false);
		} else {
			for(var x=0;x<templates.length;x++) {
				if(name === templates[x].name) {
					$("#productName").val(templates[x].name);
					$("#productCode").val(templates[x].code);
					$("#productAmount").val(templates[x].amount);
					
					$("#productName").attr("disabled", true);
					$("#productCode").attr("disabled", true);
					$("#productAmount").attr("disabled", true);
				}
			}
		}
	}
	
	function dateToYMD(date) {
	    var changeDate = new Date(date.toString());
	    var d = changeDate.getDate();
	    var m = changeDate.getMonth() + 1;
	    var y = changeDate.getFullYear();
	    return '' + y + '-' + (m<=9 ? '0' + m : m) + '-' + (d <= 9 ? '0' + d : d);
	}


	
	function listProducts() {
		var productNameFilterValue = $('#productNameFilter').val();
		var productLotFilterValue = $('#productLotFilter').val();
		var productLocationFilterValue = $('#productLocationFilter').val();
		var availableOnly = $('#availableOnly').is(':checked');
		
		console.log('listing products - current page is ' + currentPage + ', page size is ' + pageSize + ', name=' + productNameFilterValue + ',lot='+productLotFilterValue+',location='+productLocationFilterValue);
		
		var startIndex = ((currentPage-1) * pageSize)
		startIndex = (startIndex<0)?0:startIndex; //just in case
		
		console.log('start index is ' + startIndex);
		
		jQuery.getJSON("../../ws/rs/productDispensing/drugProducts?offset="+startIndex+"&limit="+pageSize+"&limitByName="+productNameFilterValue + "&limitByLot=" + productLotFilterValue + "&limitByLocation=" + productLocationFilterValue + "&availableOnly=" + availableOnly, {},
        function(xml) {
			$("#productTable tbody tr").remove();
			
			var total = xml.total;
			
			
			$("#productFilterMessage").empty();
			
			if(xml.content) {
				var arr = new Array();
				if(xml.content instanceof Array) {
					arr = xml.content;
				} else {
					arr[0] =xml.content;
				}
				
				
				for(var i=0;i<arr.length;i++) {
					var drugProduct = arr[i];
				
					var expDate = drugProduct.expiryDate;
					if(expDate.length > 9) {
						expDate = expDate.substring(0,10);
					}
					
					var html = '<tr>';
					html += '<td><a href="javascript:void(0)" onclick="editProduct('+drugProduct.id+')">Edit</a>&nbsp;|&nbsp;<a href="javascript:void(0)" onclick="deleteProduct('+drugProduct.id+')">Delete</a></td>';
					html += '<td>'+drugProduct.name+'</td>';
					html += '<td>'+drugProduct.code+'</td>';
					html += '<td>'+drugProduct.lotNumber+'</td>';
					html += '<td>'+expDate+'</td>';
					html += '<td>'+drugProduct.amount+'</td>';
					//html += '<td>'+drugProduct.id+'</td>';
					
					html += '</tr>';
				
					jQuery('#productTable tbody').append(html);
				
					
				}
				
				//pagination here
				// $('button').prop('disabled', true);
				//$('#btnPrevPage').
				
				//next page..add condition for full last page
				
				$('#btnPrevPage').prop('disabled', startIndex==0);
				$('#btnNextPage').prop('disabled', arr.length < pageSize);
				
				$("#productFilterMessage").append("<b>Showing Results:</b>&nbsp;" + (startIndex+1) + "-" + (startIndex+arr.length)  + " of " + total);
				
			} 
			
			
				
        });
	}
	

	function setProductLocations() {
		jQuery.getJSON("../../ws/rs/productDispensing/productLocations", {},
		        function(xml) {
					if(xml.productLocations) {
						var arr = new Array();
						if(xml.productLocations instanceof Array) {
							arr = xml.productLocations;
						} else {
							arr[0] =xml.productLocations;
						}
						
						for(var i=0;i<arr.length;i++) {
							$('#productLocation').append($('<option>', {
							    value: arr[i].id,
							    text: arr[i].name
							}));
							$('#productLocationFilter').append($('<option>', {
							    value: arr[i].id,
							    text: arr[i].name
							}));
						}
					}
		        });
			
	}
	
	function updateProductNames() {
		jQuery.getJSON("../../ws/rs/productDispensing/drugProducts/uniqueNames", {},
		        function(xml) {
					if(xml.content) {
						var arr = new Array();
						if(xml.content instanceof Array) {
							arr = xml.content;
						} else {
							arr[0] =xml.content;
						}
						
						var currentVal = $("#productNameFilter").val();
						$("#productNameFilter").empty();
						$("#productNameFilter").append('<option value=""></option>');
						
						for(var i=0;i<arr.length;i++) {
							$('#productNameFilter').append($('<option>', {
							    value: arr[i],
							    text: arr[i]
							}));
						}
						$("#productNameFilter").val(currentVal);
						
						updateProductLots();
						
					}
		        });
	}
	
	function updateProductLots() {
		var currentVal = $("#productLotFilter").val();
		
		
		$('#productLotFilter').html("<option value=''></option>");
		jQuery.getJSON("../../ws/rs/productDispensing/drugProducts/uniqueLots?name=" + $('#productNameFilter').val(), {},
		        function(xml) {
					if(xml.content) {
						var arr = new Array();
						if(xml.content instanceof Array) {
							arr = xml.content;
						} else {
							arr[0] =xml.content;
						}
						
						 $("#productLotFilter").val();
						
						$("#productLotFilter").empty();
						$("#productLotFilter").append('<option value=""></option>');
						
							for(var i=0;i<arr.length;i++) {
							$('#productLotFilter').append($('<option>', {
							    value: arr[i],
							    text: arr[i]
							}));
						}
						$("#productLotFilter").val(currentVal);
						
					}
		        });
	}
	
	
	
	function updatePage() {
		updateProductNames();
		listProducts();
	}
	
	function loadPreviousResults() {
		currentPage--;
		updatePage();
	}
	
	function loadNextResults() {
		currentPage++;
		updatePage();
	}
	
	$(document).ready(function(){
		setProductLocations();
		
		updatePage();

		$('#productNameFilter').bind('change',function(){
			currentPage=1;
			updatePage();
		});
		
		$('#productLotFilter').bind('change',function(){
			currentPage=1;
			updatePage();
		});
		
		$('#productLocationFilter').bind('change',function(){
			currentPage=1;
			updatePage();
		});
		
		$('#availableOnly').bind('change',function(){
			currentPage=1;
			updatePage();
		});
		
		
		$( "#new-product" ).dialog({
			autoOpen: false,
			height: 450,
			width: 800,
			modal: true,
			buttons: {
				"Save Product": {class:"btn btn-primary", text:"Save", click: function() {	
					if(validateSaveProduct()) {
					
						var dbEntryDate = new Date($("#productExpiryDate").val());
						//Must set the attr disabled to false so the values get passed properly
						//The date was still being set to one day in the past so this corrects the error.
						dbEntryDate.setDate(dbEntryDate.getDate() + 2);
						
						$("#productExpiryDate").val(dateToYMD(dbEntryDate));
						
						$("#productName").attr("disabled",false);
						$("#productCode").attr("disabled",false);
						$("#productAmount").attr("disabled",false);
						$.post('../../ws/rs/productDispensing/saveDrugProduct',$('#productForm').serialize(),
								function(data){
									updatePage();
								 });
						$( this ).dialog( "close" );	
					}
					
				} },
				Cancel: { class:"btn", text:"Cancel", click:function() {
					
					$("#productName").attr("disabled",false);
					$("#productCode").attr("disabled",false);
					$("#productAmount").attr("disabled",false);
					$( this ).dialog( "close" );
				} }
			},
			close: function() {
				
			}
		});
		
	

	});
	
	function toggleFormFieldErrorDisplay(name,hasError) {
		if(hasError) {
			$("#"+name+"Group").addClass("error");
			$("#"+name+"Group .help-inline").show();
			hasErrors=true;
		} else {
			$("#"+name+"Group").removeClass("error");
			$("#"+name+"Group .help-inline").hide();
		}
	}
	

	function validateSaveProduct() {
		
		var hasErrors = false;
		
		if($("#productName").val() == '') {
			toggleFormFieldErrorDisplay('productName',true);
			hasErrors=true;
		} else {
			toggleFormFieldErrorDisplay('productName',false);
		}
		if($("#productCode").val() == '') {
			toggleFormFieldErrorDisplay('productCode',true);
			hasErrors=true;
		} else {
			toggleFormFieldErrorDisplay('productCode',false);
		}
		if($("#productLot").val() == '') {
			toggleFormFieldErrorDisplay('productLot',true);
			hasErrors=true;
		} else {
			toggleFormFieldErrorDisplay('productLot',false);
		}
		if($("#productExpiryDate").val() == '') {
			toggleFormFieldErrorDisplay('productExpiryDate',true);
			hasErrors=true;
		} else {
			toggleFormFieldErrorDisplay('productExpiryDate',false);
		}
		if($("#productAmount").val() == '') {
			toggleFormFieldErrorDisplay('productAmount',true);
			hasErrors=true;
		} else {
			toggleFormFieldErrorDisplay('productAmount',false);
		}
		
		
		return !hasErrors;
		
	}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Manage Drug Products</h4>

<div> 
<select name="productNameFilter" id="productNameFilter">
	<option value=""></option>
</select>
&nbsp;
<select name="productLotFilter" id="productLotFilter">
	<option value=""></option>
</select>
&nbsp;
<select name="productLocationFilter" id="productLocationFilter">
	<option value=""></option>
</select>
&nbsp;
<input type="checkbox" name="availableOnly" id="availableOnly" value="true"/>Show Available Only
</div>

<div>
<span id="productFilterMessage"></span>
</div>

<table id="productTable" name="productTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th></th>
			<th>Name</th>
			<th>Code</th>
			<th>Lot#</th>
			<th>Expiry Date</th>
			<th>Amount</th>
		<!-- 	<th>Id</th> -->
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>


<div id="paginationControls">
	<button class="btn" id="btnPrevPage" onClick="loadPreviousResults();" >Prev</button>
	<button class="btn" id="btnNextPage" onClick="loadNextResults()" >Next</button>
	&nbsp;&nbsp;&nbsp;
	<select id="selPageSize" onChange="updatePageSize()">
		<option value="10">10</option>
		<option value="25">25</option>
		<option value="50">50</option>
		<option value="100">100</option>
	</select>
	
	&nbsp;&nbsp;&nbsp;
	<input type="button" class="btn btn-primary" value="Add New" onClick="addNewProduct()"/>	
	
</div>

<div id="new-product" title="OSCAR Drug Product Editor">
	<p class="validateTips"></p>
	
	<form id="productForm">
		<input type="hidden" name="product.id" id="productId" value="0"/>
			
<div>
	<div class="controls controls-row">
		<div class="control-group span6" id="productNameTemplateGroup">
				<label class="control-label" for="productNameTemplate">Choose Existing:</label>
				<div class="controls">
					<select id="productNameTemplate" name="productNameTemplate" onChange="copyNameFromTemplate()">
						<option></option>
					</select>
				</div>
			</div>
	</div>
	<div class="controls controls-row">
			<div class="control-group span3" id="productNameGroup">
				<label class="control-label" for="productName">Name:</label>
				<div class="controls">
					<input type="text" name="product.name" id="productName" value=""/>
					<span class="help-inline">Required</span>
				</div>
			</div>
			<div class="control-group span3" id="productCodeGroup">
				<label class="control-label" for="productCode">Code:</label>
				<div class="controls">
					<input type="text" name="product.code" id="productCode" value=""/>
					<span class="help-inline">Required</span>
				</div>
			</div>
			
	</div>
<div class="controls controls-row">
			
			<div class="control-group span3" id="productLotGroup">
				<label class="control-label" for="productLot">Lot:</label>
				<div class="controls">
					<input type="text" name="product.lotNumber" id="productLot" value=""/>
					<span class="help-inline">Required</span>
				</div>
			</div>
			
			<div class="control-group span3" id="productExpiryDateGroup">
				<label class="control-label" for="productExpiryDate">Expiry Date:</label>
				<div class="controls">
					<input type="text" name="product.expiryDate" id="productExpiryDate" value=""/>
					<span class="help-inline">Required</span>
				</div>
			</div>
			
</div>
<div class="controls controls-row">
			<div class="control-group span3" id="productAmountGroup">
				<label class="control-label" for="productAmount">Amount:</label>
				<div class="controls">
					<input type="text" name="product.amount" id="productAmount" value=""/>
					<span class="help-inline">Required (number)</span>
				</div>
			</div>
			
			<div class="control-group span3" id="totalEntriesToCreateGroup">
				<label class="control-label" for="productExpiryDate">Total Entries to create:</label>
				<div class="controls">
					<input type="text" name="productBulkTotal" id="productBulkTotal" value="1"/>
				</div>
			</div>
			
</div>

<div class="controls controls-rw">
			<div class="control-group span3">
				<label class="control-label" for="productLocation">Location:</label>
				<div class="controls">
					<select name="product.location" id="productLocation">
						
					</select>
				</div>
			</div>

</div>

</div>

	</form>
</div>

<script type="text/javascript">

$('#productExpiryDate').datepicker({
        format: "yyyy-mm-dd",
        todayBtn: "linked",
        autoclose: true,
        todayHighlight: true
    });
</script>



</body>
</html:html>
