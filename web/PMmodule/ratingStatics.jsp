<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->


<%@ page contentType="text/html"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<!DOCTYPE html PUBLIC "-//Tigris//DTD XHTML 1.0 Transitional//EN"
"tigris_transitional.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rating statistic</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>
<body marginwidth="0" marginheight="0">

<div class="composite">


<table border="0" cellspacing="0" cellpadding="18" width="100%">
	<tr valign="top">
		<td>
		<div class=body>
		<div id="bodycol">

		<div id="apphead">
		<h3>Thank you for rating CAISI page!</h3>
		<h3>If you have any comments or suggestions please visit our
		CAISI <a href="http://caisi.ca/mailman/listinfo">forum</a>.</h3>
		<br>
		<h3>Page rating statistic</h3>
		</div>
		</div>

		<div id="bodycol">

		<div id="projecthome" class="app">


		<div class="axial"><nested:form action="/ratingStatics.do">
			<table border="1" cellspacing="2" cellpadding="3" width="100%">
				<tr>
					<th>Page name</th>
					<th>Average score</th>
					<th>Visitors</th>
				</tr>

				<nested:iterate id="ratingPage" property="rate_table">

					<tr class="b">
						<td><nested:write name="ratingPage" property="pageName" /></td>

						<td><nested:write name="ratingPage" property="avrgScore" /></td>

						<td><nested:write name="ratingPage" property="vstNumber" /></td>

					</tr>

				</nested:iterate>

			</table>

		</nested:form></div>
	</tr>
</table>
</div>
</body>
</html>



