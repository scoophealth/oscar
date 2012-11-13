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

<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.eyeform.web.EyeformAction"%>
<%@page import="org.oscarehr.common.model.Appointment"%>
<%@page import="org.oscarehr.common.model.Measurement"%>
<%@page import="java.util.List"%>
<%@page import="oscar.util.StringUtils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%
	String sdate = StringUtils.transformNullInEmptyString((String)request.getAttribute("sdate"));
	String edate = StringUtils.transformNullInEmptyString((String)request.getAttribute("edate"));
%>
<html>
	<head>
    	<title>Examination History Results</title>
    	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />
		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
    	<link rel="stylesheet" href="<%=request.getContextPath()%>/eyeform/display2.css" type="text/css">
    	<style type="text/css">
			* { font-family: Trebuchet MS, Lucida Sans Unicode, Arial, Helvetica, sans-serif; color: #000; margin: 0px; padding: 0px; }
			body { padding: 10px; }

			td.inner{
			border:1px solid #666;
			}

			table.common{
			border:0;
			font-size: 10pt;
			}
			h5{
				margin-top: 1px;
				border-bottom: 1px solid #000;
				font-weight: bold;
				list-style-type: none;
				font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
				font-size: 10pt;
				overflow: hidden;
				background-color: #ccccff;
				padding: 0px;
				color: black;
				width: 300px;
			}
			th {white-space:nowrap}

			.centered {text-align:center}
		</style>
	</head>

	<body>
		<form action="<%=request.getContextPath()%>/eyeform/ExaminationHistory.do" method="POST" id="inputForm" name="inputForm">
		<input type="hidden" name="method" value="query"/>
		<input type="hidden" name="demographicNo" value="<c:out value="${demographic.demographicNo}"/>"/>
		<input type="hidden" name="refPage" value="<c:out value="${refPage}"/>"/>
		<c:forEach var="field" items="${fields}">
			<input type="hidden" name="fromlist2" value="<c:out value="${field}"/>"/>
		</c:forEach>
		<table class="common">
	  		<tr>
	  			<td>
	  				<h4 style="background-color: #69c">Demographic name:<c:out value="${demographic.formattedName}"/></h4>
	  			</td>
	  		</tr>
			<tr>
				<table style="background-color: #efefff">
					<tr>
						<td>Start Date:</td>
						<td>
			 				<input type="text" class="plain" name="sdate" id="sdate" size="12" onfocus="this.blur()" readonly="readonly" value="<%=sdate%>"/>
			 				<img src="<%=request.getContextPath()%>/images/cal.gif" id="sdate_cal">
			 				<script type="text/javascript">
								Calendar.setup({ inputField : "sdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "sdate_cal", singleClick : true, step : 1 });
							</script>
		    			</td>
						<td>End Date:</td>
						<td>
							<input type="text" class="plain" name="edate" id="edate" size="12" onfocus="this.blur()" readonly="readonly" value="<%=edate%>"/>
							<img src="<%=request.getContextPath()%>/images/cal.gif" id="edate_cal">
			 				<script type="text/javascript">
								Calendar.setup({ inputField : "edate", ifFormat : "%Y-%m-%d", showsTime :false, button : "edate_cal", singleClick : true, step : 1 });
							</script>
						</td>
						<td></td>
						<td>
							<input type="submit" onclick="this.form.refPage.value=null" value="Search"/>
						</td>
					</tr>
				</table>
			</tr>
	 	</table>

		<h5>Simple field history:</h5>
		<table class="display" style="width:20%">
		<tr style="background-color: rgb(204, 204, 255);">
	    <td>Total <c:out value="${numPages}"/> pages.</td>
		<td align="right">
		<%
			int numPages = (Integer)request.getAttribute("numPages");
			int pageNumber = (Integer)request.getAttribute("refPage");
			if(pageNumber>1) {
				%><a href="#" onclick="document.inputForm.refPage.value=<%=(pageNumber-1)%>;return document.inputForm.submit();">prev</a><%
			} else {
				%>prev<%
			}
		%>
		&nbsp;
		<%
			if(numPages > 1 && pageNumber<numPages) {
				%><a href="#" onclick="document.inputForm.refPage.value=<%=(pageNumber+1)%>;return document.inputForm.submit();">next</a><%
			} else {
				%>next<%
			}
		%>
		</td>
	  </tr>
		<table class="display" style="width:20%">
			<tr style="background-color: rgb(204, 204, 255);">
		  		<td nowrap="nowrap"></td>
		  		<%
		  			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		  			for(Appointment appointment:(List<Appointment>)request.getAttribute("appointments")) {
		  				out.println("<td nowrap=\"nowrap\">"+formatter.format(appointment.getAppointmentDate())+"</td>");
		  			}
		  		%>
			</tr>
			<%
				@SuppressWarnings("unchecked")
				List<String> simpleFieldNames = (List<String>)request.getAttribute("simpleFieldNames");
				Measurement simpleFields[][] = (Measurement[][])request.getAttribute("simpleFields");
				for(int x=0;x<simpleFields.length;x++) {
					out.println("<tr class=\""+(((x%2)==0)?"even":"odd")+"\">");
					out.println("<td nowrap=\"nowrap\">"+simpleFieldNames.get(x)+"</td>");
					for(int y=0;y<simpleFields[x].length;y++) {
						out.println("<td nowrap=\"nowrap\">"+((simpleFields[x][y]!=null)?simpleFields[x][y].getDataField():"")+"</td>");
					}
					out.println("</tr>");
				}
			%>
</table>
<table class="display">
  <tr>
  <td><h5>AR history</h5>
  <display:table name="ars" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;white-space: nowrap;">
			<c:out value="${map.od_ar_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;white-space: nowrap;">
			<c:out value="${map.od_ar_cyl}"/>

			</display:column>
			 <display:column title="OD Axis" style="width:30px;white-space: nowrap;">
		    <c:out value="${map.od_ar_axis}"/>
			</display:column>

			<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;white-space: nowrap;">
			<c:out value="${map.os_ar_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;white-space: nowrap;">
			<c:out value="${map.os_ar_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_ar_axis}"/>
			</display:column>

	</display:table>
   </td>
   </tr>

  <tr>
  <td><h5>K history</h5>
  <display:table name="ks" requestURI="/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD K1" style="width:30px;">
			<c:out value="${map.od_k1}"/>
			</display:column>

			<display:column title="OD K2" style="width:30px;">
			<c:out value="${map.od_k2}"/>

			</display:column>
			 <display:column title="OD K2-Axis" style="width:30px;">
		    <c:out value="${map.od_k2_axis}"/>
			</display:column>

			<display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS K1" style="width:30px;">
			<c:out value="${map.os_k1}"/>
			</display:column>

			<display:column title="OS K2" style="width:30px;">
			<c:out value="${map.os_k2}"/>
			</display:column>

			 <display:column title="OS K2-Axis" style="width:30px;">
		    <c:out value="${map.os_k2_axis}"/>
			</display:column>

	</display:table>
   </td>
   </tr>


  <tr>
  <td><h5>Manifest Refraction history</h5>
  <display:table name="manifestRefraction" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;">
			<c:out value="${map.od_manifest_refraction_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;">
			<c:out value="${map.od_manifest_refraction_cyl}"/>

			</display:column>

			 <display:column title="OD Axis" style="width:30px;">
		    <c:out value="${map.od_manifest_refraction_axis}"/>
			</display:column>


			 <display:column title="OD Add" style="width:30px;">
		    <c:out value="${map.od_manifest_refraction_add}"/>
			</display:column>

		   <display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;">
			<c:out value="${map.os_manifest_refraction_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;">
			<c:out value="${map.os_manifest_refraction_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_manifest_refraction_axis}"/>
			</display:column>

		 <display:column title="OS Add" style="width:30px;">
		    <c:out value="${map.os_manifest_refraction_add}"/>
			</display:column>

  </display:table>
   </td>
   </tr>


  <tr>
  <td><h5>Cycloplegic refraction history</h5>
  <display:table name="cycloplegicRefraction" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;">
			<c:out value="${map.od_cycloplegic_refraction_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;">
			<c:out value="${map.od_cycloplegic_refraction_cyl}"/>

			</display:column>

			 <display:column title="OD Axis" style="width:30px;">
		    <c:out value="${map.od_cycloplegic_refraction_axis}"/>
			</display:column>


			 <display:column title="OD Add" style="width:30px;">
		    <c:out value="${map.od_cycloplegic_refraction_add}"/>
			</display:column>

		   <display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;">
			<c:out value="${map.os_cycloplegic_refraction_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;">
			<c:out value="${map.os_cycloplegic_refraction_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_cycloplegic_refraction_axis}"/>
			</display:column>

		 <display:column title="OS Add" style="width:30px;">
		    <c:out value="${map.os_cycloplegic_refraction_add}"/>
			</display:column>

  </display:table>
   </td>
   </tr>

  <tr>
  <td><h5>Angle history</h5>
  <display:table name="angle" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD" >
			<table style="border:0px">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.od_angle_up}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${map.od_angle_middle0}"/></td>
			<td class="inner"><c:out value="${map.od_angle_middle1}"/></td>
			<td class="inner"><c:out value="${map.od_angle_middle2}"/></td>
			</tr>
			<tr>
			<td></td>
			<td class="inner"><c:out value="${map.od_angle_down}"/></td>
			<td></td>
			</tr>
			</table>
			</display:column>
			<display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS" >
			<table border="1">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.os_angle_up}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${map.os_angle_middle0}"/></td>
			<td class="inner"><c:out value="${map.os_angle_middle1}"/></td>
			<td class="inner"><c:out value="${map.os_angle_middle2}"/></td>
			</tr>
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.os_angle_down}"/></td>
			<td width="33%"></td>
			</tr>
			</table>
			</display:column>

  </display:table>
   </td>
   </tr>

		</table>

	</body>
</html>
