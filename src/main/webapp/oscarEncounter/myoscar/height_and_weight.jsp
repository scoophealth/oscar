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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>

<title>MyOSCAR Measurement: Height and Weight</title>

<c:import url="header.jsp" />

<html:base />

<c:import url="plot_javascript.jsp">
	<c:param name="series1" value="${measurements.asMap['Height']}"/>
	<c:param name="series1Label" value="Height"/>
	<c:param name="series2" value="${measurements.asMap['Weight']}"/>
	<c:param name="series2Label" value="Weight"/>
	<c:param name="yaxisLabel" value=""/>
</c:import>

</head>

<body vlink="#0000FF" class="BodyStyle">
	<body class="mainbody" vlink="#0000FF">
	<table class="MainTable" id="scrollNumber1" name="encounterTable"
		style="margin: 0px;">
		<tr class="topbar">
			<td class="MainTableTopRowLeftColumn" width="60px">MyOSCAR Measurements</td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar">
					<tr>
						<td>Height and Weight</td>
						<td style="text-align: right;"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<h2 style="margin: 1em;">
		Height and Weight History
	</h2>

	<div id="chart">
	</div>

	<c:choose>
		<c:when test="${empty measurements}">
			<h4>Measurements information is not available</h4>
		</c:when>
		<c:otherwise>
			
			<table>
				<tr>
					<th>Type</th>
					<th>Measurement date</th>
					<th>Data</th>
					<th>Comments</th>
				</tr>
				
			    <c:forEach var="m" items="${measurements.measurements}">
				<tr>
					<td>HW</td>
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
			
			<div>
				<c:import url="filter.jsp">
					<c:param name="sourcePage" value="height_and_weight" />
				</c:import>
			</div>

			<h2 style="margin: 1em;">Statistics</h2>
			
			<table>
				<c:choose>
					<c:when test="${empty measurements.minima}">
						<tr>
							<td colspan="2">
						 		<h4>Unable to determine minimum values</h4>
						 	</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
								Min height
							</td>
							<td>
								<c:out value="${measurements.minima['Height']}"></c:out> <br/>
							</td>
						</tr>
						<tr>
							<td>
								Min weight
							</td>
							<td>
								<c:out value="${measurements.minima['Weight']}"></c:out> <br/>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${empty measurements.average}">
						<tr>
							<td colspan="2">
						 		<h4>Unable to determine average values</h4>
						 	</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
								Avg height
							</td>
							<td>
								<c:out value="${measurements.average['Height']}"></c:out> <br/>
							</td>
						</tr>
						<tr>
							<td>
								Avg weight
							</td>
							<td>
								<c:out value="${measurements.average['Weight']}"></c:out> <br/>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${empty measurements.maxima}">
						<tr>
							<td colspan="2">
						 		<h4>Unable to determine maximum values</h4>
						 	</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
								Max height
							</td>
							<td>
								<c:out value="${measurements.maxima['Height']}"></c:out> <br/>
							</td>
						</tr>
						<tr>
							<td>
								Max weight
							</td>
							<td>
								<c:out value="${measurements.maxima['Weight']}"></c:out> <br/>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</c:otherwise>
	</c:choose>
	
	
	<button
		style="margin: 1em 3em;" 
		onclick="window.close()">Close</button>
</body>


</html>