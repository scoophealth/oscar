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
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>
<head>

<title>MyOSCAR Measurement: Blood Pressure</title>

<c:import url="header.jsp" />

<c:import url="plot_javascript.jsp">
	<c:param name="series1" value="${measurements.asMap['SystolicValue']}"/>
	<c:param name="series1Label" value="Systolic"/>
	<c:param name="series2" value="${measurements.asMap['DiastolicValue']}"/>
	<c:param name="series2Label" value="Diastolic"/>
	<c:param name="yaxisLabel" value="mm[Hg]"/>
</c:import>

<html:base />
</head>

<body  >
	<h2>Blood Pressure History</h2>
	<div>
	<c:import url="filter.jsp">
		<c:param name="sourcePage" value="blood_pressure" />
	</c:import>
	</div>
	
	
	<div id="chart">
	</div>
	
	<c:choose>
		<c:when test="${empty measurements}">
		
			<p style="font-size: larger; font-weight: bold; margin: 10px">Measurement information is not available.</p>
			
		</c:when>
		
		<c:otherwise>
		<div class="container-fluid" >
			<div class="col-sm-5">
			<h2>Statistics</h2>
	
			<table class="table table-condensed">
				<tr>
					<td></td>
					<th>Min</th>
					<th>Max</th>
					<th>Average</th>
				</tr>
				<tr>
					<th>Pressure</th>
					<td>
						<c:choose>
							<c:when test="${empty measurements.minima}">N/A</c:when>
							<c:otherwise>
									<c:out value="${measurements.minima['SystolicValue']}"></c:out>
									/
									<c:out value="${measurements.minima['DiastolicValue']}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose> 
							<c:when test="${empty measurements.maxima}">N/A</c:when>
							<c:otherwise>	
								<c:out value="${measurements.maxima['SystolicValue']}"></c:out>
								/
								<c:out value="${measurements.maxima['DiastolicValue']}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty measurements.average}">N/A</c:when> 
							<c:otherwise>
								<c:out value="${measurements.average['SystolicValue']}"></c:out>
								/
								<c:out value="${measurements.average['DiastolicValue']}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>		
				<tr>
					<th>Rate</th>
					<td>
						<c:choose>
							<c:when test="${empty measurements.minima}">N/A</c:when>
							<c:otherwise>
									<c:out value="${measurements.minima['HeartRate']}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
					<c:choose> 
						<c:when test="${empty measurements.maxima}">N/A</c:when>
						<c:otherwise>	
								<c:out value="${measurements.maxima['HeartRate']}"></c:out>
						</c:otherwise>
					</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty measurements.average}">N/A</c:when>
							<c:otherwise>
									<c:out value="${measurements.average['HeartRate']}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			
			</div>
			<div class="col-sm-7">
				<h2>Log 
				</h2>
			<table class="table table-condensed">
				<tr>
					<th>Type</th>
					<th>Measurement date</th>
					<th>Data</th>
					<th>Comments</th>
				</tr>
			
			    <c:forEach var="m" items="${measurements.measurements}">
					<c:choose>
						<c:when test="${m.sentFromPhr}">
				    		<c:set var="className" value="sentFromPhr"></c:set>
						</c:when>
						<c:otherwise>
							<c:set var="className" value=""></c:set>
						</c:otherwise>
					</c:choose>
					<tr class="${className}">
						<td>BP</td>
						<td>
							<fmt:formatDate value="${m.date}" dateStyle="short" />
						</td>
						<td>
							<c:out value="${m}"></c:out>
						</td>
						<td>
							<c:out value="${m.comments}"></c:out>
						</td>
					</tr>
				</c:forEach>
				
			</table>
	
			
			</div>
			
			</div>
		</c:otherwise>
	</c:choose>
	
	
		<br><br><br><br>
		<br><br><br><br><br><br><br><br><br><br><br><br>
		<br><br><br><br><br><br><br><br><br><br><br><br>
		<br><br><br><br><br><br><br><br><br><br><br><br>
		<hr>

</body>

<%
String iframeResize = (String) session.getAttribute("useIframeResizing");
if(iframeResize !=null && "true".equalsIgnoreCase(iframeResize)){ %>
<script src="<%=request.getContextPath() %>/library/pym.js"></script>
<script>
    console.log('hi1');
    pymChild = new pym.Child({ polling: 500 });
</script>
<%}%>    

</html:html>