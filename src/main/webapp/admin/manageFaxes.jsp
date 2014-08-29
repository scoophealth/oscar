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
<%@ page import="org.oscarehr.common.dao.FaxConfigDao, org.oscarehr.common.model.FaxConfig, org.oscarehr.common.model.FaxJob, org.oscarehr.common.dao.FaxJobDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Faxes</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0">                                
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/share/css/jquery-ui.css" type="text/css">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script type="text/javascript">
	$( function() {
		$("#dateBegin").datepicker();
		$("#dateBegin").datepicker("option","showAnim","blind");
		$("#dateBegin").datepicker("option","dateFormat","yy-mm-dd");
		
		$("#dateEnd").datepicker();
		$("#dateEnd").datepicker("option","showAnim","blind");
		$("#dateEnd").datepicker("option","dateFormat","yy-mm-dd");
		
	});
	
	$(document).ready(function() {
		$( "#reportForm" ).submit(function( event ) {
			// Stop form from submitting normally
			event.preventDefault();
			// Get some values from elements on the page:
			var data = $( this ).serialize();
			var url = $(this).attr("action");
			
			var post = $.post(url,data);
			
			post.done(function( resultdata ) {				
				$("#results").empty().append(resultdata);
			});
			
			return false;
		});
	});
	
	function view(id) {
		var url = "<%=request.getContextPath()%>/admin/viewFax.jsp";
		var data = "jobId=" + id;
		
		var post = $.post(url,data);
		
		post.done(function( resultdata ) {
			$("#preview").empty().append( resultdata );
		});
		
		return false;
	}
	
	function _zoom(d) {
		var img = $(d).attr('src');
		var modal = $('img[src$="' + img + '"]').clone();
		
		var t = new Image();
		t.src = img;
		
		var height = t.height;
		var width = t.width;
		
		modal.dialog({
			height: $(window).height() - 40,
			modal: true,
			draggable: false,
			resizable: false,
			width: width 
		});
	}
	
	function resend(id, faxNumber, status) {
		
		var answer = prompt("Is this the correct fax number?", faxNumber);
		
		if( answer == null ) {
			return false;
		}
				
		if( answer.match("^\\d{10,11}$") ) {
			
			var url = $("#reportForm").attr("action");
			var data = "method=ResendFax&jobId="+id+"&faxNumber="+answer;

			$.ajax({
				url: url,
				method: 'POST',
				data: data,
				dataType: "json",
				success: function(data){
					
					if( data.success ) {
						$('#'+status).text("SENT");
					}
					else {
						alert("An error occured trying to resend your fax.  Please contact your system administrator"); 
					}
				}});
		}
		else {
			alert("Fax numbers must be input as 11231234567");
	
		}
		
		return false;
			
	}
	
	function cancel(jobId,status) {
		
		var answer = confirm("Are you sure you want to remove this fax from the queue?");
		
		if( answer == null ) {
			return false;
		}

		var url = $("#reportForm").attr("action");
		var data = "method=CancelFax&jobId="+jobId;

		$.ajax({
			url: url,
			method: 'POST',
			data: data,
			dataType: "json",
			success: function(data){
				
				if( data.success ) {
					$('#'+status).text("CANCELLED");
				}
				else {
 					alert("OSCAR WAS UNABLE TO CANCEL THE FAX"); 
				}
			}});
		
		return false;
	}
	
</script>

</head>
<body>
<div class="page-header text-center">
<h3>Manage Faxes</h3>
</div>
<form id="reportForm" action="<%=request.getContextPath()%>/admin/ManageFaxes.do">
<input type="hidden" name="method" value="fetchFaxStatus"/>
<div class="row-center">

	<select class="span3" name="team">
		<option value="-1">Team</option>
<%
	FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
	List<FaxConfig> faxConfigList = faxConfigDao.findAll(null, null);
	
	for( FaxConfig faxConfig : faxConfigList ) {
%>
		<option value="<%=faxConfig.getFaxUser()%>"><%=faxConfig.getFaxUser() %></option>
<%
	}
%>
	</select>

	<select class="span3" name="status">
		<option value="-1">Status</option>
	
<%
	for( FaxJob.STATUS status : FaxJob.STATUS.values() ) {
%>
		<option value="<%=status%>"><%=status%></option>
<%	    
	}
%>
	</select>
	
	<span class="span3"> From:<input class="span2" type="text"  id="dateBegin" name="dateBegin" value=""/></span>
	
	<span class="span3"> To:<input class="span2" type="text" id="dateEnd" name="dateEnd" value=""/></span>
	



</div>
<div class="text-center">
	<input type="submit" value="Fetch Faxes"/>
</div>
</form>
<hr>
<div class="span6" id="results">
</div>
<div class="span6" id="preview">
</div>
</body>
</html>