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
	Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;
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


<style>
.red{color:red}

</style>

<script>
	
	function editJob(jobId) {
		jQuery.getJSON("../ws/rs/jobs/job/"+jobId, {},
        function(xml) {
			if(xml.jobs) {
				var job;
				if(xml.jobs instanceof Array) {
					job = xml.jobs[0];
				} else {
					job = xml.jobs;
				}
				
				$('#jobName').val(job.name);
				$('#jobType').val(job.oscarJobTypeId);
				$('#jobDescription').val(job.description);
				$('#jobEnabled').prop('checked',job.enabled);
				$('#jobProvider').val(job.providerNo);
				$('#jobId').val(job.id);
			}
        });
		$('#new-product').dialog('open');
	}
	
	function addNewProduct() {	
		$('#productName').val('');
		$('#productCode').val('');
		$('#productLocation').val('');
		$('#productLot').val('');
		$('#productExpiryDate').val('');
		$('#productId').val('0');
		$('#new-product').dialog('open');
	}
	
	function clearProducts() {
		$("#productTable tbody tr").remove();
	}
	
	function listProducts() {
		jQuery.getJSON("../../ws/rs/productDispensing/drugProducts?offset=0&limit=20", {},
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
				
					var html = '<tr>';
					html += '<td>'+drugProduct.name+'</td>';
					html += '<td>'+drugProduct.code+'</td>';
					html += '<td>'+drugProduct.lotNumber+'</td>';
					html += '<td>'+drugProduct.expiryDate+'</td>';
					html += '<td>'+drugProduct.amount+'</td>';
					html += '<td>'+drugProduct.id+'</td>';
					
					html += '</tr>';
				
					jQuery('#productTable tbody').append(html);
					
				}
			} else {
        		alert('error retrieving drug products');
			}
        });
	}
	

	
	$(document).ready(function(){
		listProducts();
		
		$( "#new-product" ).dialog({
			autoOpen: false,
			height: 560,
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
 
<table id="productTable" name="productTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>Name</th>
			<th>Code</th>
			<th>Lot#</th>
			<th>Expiry Date</th>
			<th>Amount</th>
			<th>Id</th>
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
						<option value="1">1</option>
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
				</div>
			</div>
			
			
		</fieldset>
	</form>
</div>


</body>
</html:html>