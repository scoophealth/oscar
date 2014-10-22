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
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/share/css/OscarStandardLayout.css" />

<style type="text/css">
	tr.sentFromPhr td {
		color: blue !important;
	}
</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jqplot/jquery.jqplot.min.css" />
<link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">

<script src="${pageContext.request.contextPath}/js/jquery-1.9.1.js" language="javascript" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.10.2.custom.min.js"  language="javascript" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jqplot/jquery.jqplot.min.js" language="javascript" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jqplot/jqplot.dateAxisRenderer.min.js" type="text/javascript" ></script>
<script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script>var pymChild = {}; </script>

<script language="javascript">
<!--
	jQuery.noConflict();
	
	jQuery(document).ready(function() {
		jQuery("#from").datepicker({
			 defaultDate: "+1w",
			 changeMonth: true,
			 numberOfMonths: 2,
			 dateFormat: "yy-mm-dd",
			 onClose: function( selectedDate ) {
			 	jQuery( "#to" ).datepicker( "option", "minDate", selectedDate );
			 }
		});
		
		jQuery("#to").datepicker({
			defaultDate: "+1w",
			 changeMonth: true,
			 numberOfMonths: 2,
			 dateFormat: "yy-mm-dd",
			 onClose: function( selectedDate ) {
			 	jQuery( "#from" ).datepicker( "option", "maxDate", selectedDate );
			 }	 
		});
	});
-->
</script>
