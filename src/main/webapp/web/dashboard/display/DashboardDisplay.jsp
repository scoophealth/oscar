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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<security:oscarSec roleName='${ sessionScope[userrole] }, ${ sessionScope[user] }' rights="w" objectName="_dashboardDisplay">
	<c:redirect url="securityError.jsp?type=_dashboardDisplay" />
</security:oscarSec>

<!DOCTYPE html > 
<html lang="" >
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<title>
	<c:out value="${ dashboard.name }" />
</title>
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/web/css/Dashboard.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/js/jqplot/jquery.jqplot2.min.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>		
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/web/dashboard/display/dashboardDisplayController.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jqplot/jquery.jqplot2.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jqplot/plugins/jqplot.pieRenderer.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jqplot/plugins/jqplot.json2.js" ></script>
</head>

<body>

<div class="container">
<div class="row" id="dashboardPanel" >
<div class="col-md-12" >

	<!-- Dashboard Heading -->
	<div class="row dashboardHeading" >
		<h2>
			<c:out value="${ dashboard.name }" />
		</h2>
		<hr />
	</div>
	<div class="row dashboardSubHeading" >
		<div class="col-md-6">
			Last loaded: 
			<c:out value="${ dashboard.lastChecked }" />
			<a href="#" title="refresh" class="reloadDashboardBtn" id="getDashboard_${ dashboard.id }" >
				<span class="glyphicon glyphicon-refresh"></span>
			</a>
		</div>
		<div class="col-md-6">
			<a href="#" title="Dashboard Manager" class="pull-right dashboardManagerBtn" id="${ dashboard.id }" >
				<span class="glyphicon glyphicon glyphicon-cog"></span>
			</a>
		</div>
	</div>
	<!-- end Dashboard Heading -->
	
	<div class="row dashboardBody">	
	
		<!-- dashboardPanels - by category.  -->
		<c:forEach items="${ dashboard.panelBeans }" var="panelBean" >
		
			<div class="panel panel-primary categoryPanel" >
	
				<div class="panel-heading">				
					<strong><c:out value="${ panelBean.category }" /></strong>				
				</div>
				
				<div class="panel-body" >
				
				<c:forEach items="${ panelBean.indicatorPanelBeans }" var="indicatorPanel" >
					
					<!-- Begin display of Indicator Panel -->
					<div class="panel panel-default indicatorPanel" >
		
						<!-- Indicator panel heading - by sub category -->
						<div class="panel-heading">									
							<c:out value="${ indicatorPanel.category }" />						
						</div>
	
						<div class="panel-body" >							
							<c:forEach items="${ indicatorPanel.indicatorIdList }" var="indicatorId" >																
								<div class="col-md-3 indicatorWrapper" id="indicatorId_${ indicatorId }">				
									<div>
										<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> 
										Loading...
									</div>
								</div>
							</c:forEach>
							<!-- end indicator loop -->
						</div> 
						<!-- end indicatorPanel body -->
					</div>	
					<!-- end indicatorPanel -->
					
				</c:forEach> 
				<!-- end indicatorPanel loop -->
				</div>			
			</div> 	
			<!--  end dashboardPanels -->	
		</c:forEach> 
		<!-- end Dashboard Panel loop -->
		
	</div>
	<!-- End dashboard body -->

</div>	
</div> 	
</div> 
<!-- end container -->

</body>
</html>