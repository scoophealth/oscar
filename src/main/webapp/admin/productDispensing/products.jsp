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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Provider" %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.loggedInProvider;
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

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all" href="../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
        src="../../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
        src="../../share/calendar/calendar-setup.js"></script>



<style>
.red{color:red}

</style>

<script>

	function deleteProduct(productId) {
		if(confirm("Are you sure you want to delete this?")) {
			jQuery.getJSON("../../ws/rs/productDispensing/deleteDrugProduct/" + productId, {},
			        function(xml) {
						if(xml.message) {
							clearProducts();
							listProducts();
						} else {
			        		alert('error retrieving drug products');
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
				$('#productExpiryDate').val(drugProduct.expiryDate);
				$('#productAmount').val(drugProduct.amount);
				$('#productId').val(drugProduct.id);
				$('#totalEntriesToCreateGroup').hide();
				$('#new-product').dialog('open');
				
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
	}
	
	function clearProducts() {
		$("#productTable tbody tr").remove();
	}
	


	function dateToYMD(date) {
	    var d = date.getDate();
	    var m = date.getMonth() + 1;
	    var y = date.getFullYear();
	    return '' + y + '-' + (m<=9 ? '0' + m : m) + '-' + (d <= 9 ? '0' + d : d);
	}


	
	function listProducts() {
		var productNameFilterValue = $('#productNameFilter').val();
		
		jQuery.getJSON("../../ws/rs/productDispensing/drugProducts?offset=0&limit=20&limitByName="+productNameFilterValue, {},
        function(xml) {
			if(xml.drugProduct) {
				var arr = new Array();
				if(xml.drugProduct instanceof Array) {
					arr = xml.drugProduct;
				} else {
					arr[0] =xml.drugProduct;
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
			} else {
        		alert('error retrieving drug products');
			}
        });
	}
	

	function updateProductLocations() {
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
						
						for(var i=0;i<arr.length;i++) {
							$('#productNameFilter').append($('<option>', {
							    value: arr[i],
							    text: arr[i]
							}));
						}
					}
		        });
	}
	
	$(document).ready(function(){
		listProducts();
		updateProductLocations();
		updateProductNames();

		$('#productNameFilter').bind('change',function(){
			clearProducts();
			listProducts();
		});
		
		$( "#new-product" ).dialog({
			autoOpen: false,
			height: 600,
			width: 620,
			modal: true,
			buttons: {
				"Save Product": function() {	
					if(validateSaveProduct()) {
						$.post('../../ws/rs/productDispensing/saveDrugProduct',$('#productForm').serialize(),
								function(data){
									clearProducts();
									listProducts();
								 });
						$( this ).dialog( "close" );	
					}
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
	

	});
	
	function validateSaveProduct() {
		var errorMsg = '';
		
		if(errorMsg.length>0) {
			alert(errorMsg);
			return false;
		}
		return true;
		
	}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Manage Drug Products</h4>

<div> 
<select name="productNameFilter" id="productNameFilter">
	<option value=""></option>
</select>
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

<input type="button" class="btn btn-primary" value="Add New" onClick="addNewProduct()"/>	


<div id="new-product" title="OSCAR Drug Product Editor">
	<p class="validateTips"></p>
	
	<form id="productForm">
		<input type="hidden" name="product.id" id="productId" value="0"/>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="productName">Name:*</label>
				<div class="controls">
					<input type="text" name="product.name" id="productName" value=""/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="productCode">Code:*</label>
				<div class="controls">
					<input type="text" name="product.code" id="productCode" value=""/>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="productLocation">Location:</label>
				<div class="controls">
					<select name="product.location" id="productLocation">
						
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="productLot">Lot:</label>
				<div class="controls">
					<input type="text" name="product.lotNumber" id="productLot" value=""/>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="productExpiryDate">Expiry Date:</label>
				<div class="controls">
					<input type="text" name="product.expiryDate" id="productExpiryDate" value=""/>
					<img src="../../images/cal.gif" id="xml_vdate_cal" />
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="productAmount">Amount:</label>
				<div class="controls">
					<input type="text" name="product.amount" id="productAmount" value=""/>
				</div>
			</div>
			
			<div class="control-group" id="totalEntriesToCreateGroup">
				<label class="control-label" for="productExpiryDate">Total Entries to create:</label>
				<div class="controls">
					<input type="text" name="productBulkTotal" id="productBulkTotal" value="1"/>
				</div>
			</div>
		</fieldset>
	</form>
</div>

<script type="text/javascript">
Calendar.setup( { inputField : "productExpiryDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );
</script>



</body>
</html:html>