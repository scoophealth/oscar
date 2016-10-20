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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:oscarSec roleName='${ sessionScope[userrole] }, ${ sessionScope[user] }' rights="w" objectName="_dashboardDrilldown">
	<c:redirect url="securityError.jsp?type=_dashboardDrilldown" />
</security:oscarSec>

<!DOCTYPE html > 
<html lang="" >
<head>
<title>Dashboard Drilldown</title>

	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/web/css/Dashboard.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap2-datepicker/datepicker3.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/bootstrap-timepicker.min.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-ui-1.10.2.custom.min.js"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap2-datepicker/bootstrap-datepicker.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bootstrap-timepicker.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/moment.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/datetime-moment.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/web/dashboard/display/drilldownDisplayController.js" ></script>
</head>
<body>

<div class="container">

<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
	
		<button class="btn btn-default backtoDashboardBtn" id="getDashboard_${ drilldown.dashboardId }" type="button">
			<span class="glyphicon glyphicon-circle-arrow-left text-center" aria-hidden="true"></span>
			Dashboard
		</button>
		
		 <button class="btn btn-default" type="button" onclick="window.print();" >
		 	<span class="glyphicon glyphicon-print text-center" aria-hidden="true"></span>
		 	Print
		 </button>	
		 
		  <button class="btn btn-default exportResults" type="button" id="exportResults_${ drilldown.id }" >
		  	<span class="glyphicon glyphicon-download-alt text-center" aria-hidden="true"></span>
		 	Export
		 </button> 
		 
		 <button class="btn btn-default" type="button" data-toggle="modal" data-target="#indicatorInfo">
			 <span class="glyphicon glyphicon-info-sign text-center" aria-hidden="true"></span>
			 Details
		 </button>
	 </div>

</nav>

<div class="row content">
 
	<h3> 
		<c:out value="${ drilldown.name }" />
	</h3>
	<hr />
	
	<c:set scope="page" value="" var="primaryDataType" />
		
	<c:forEach items="${ drilldown.displayColumns }" var="column" >
		<c:if test="${ column.primary }">
			<c:set scope="page" value="${ column.name }" var="primaryDataType" />
		</c:if>	
	</c:forEach>
	
	<!-- Filter customization. Javascript will integrate this into the DataTable. -->
	<form id="datatableFilterGroup" class="form-inline" style="display:none;">
	<div id="datatableFilterGroupBody" >
	<div class="form-group">		
		<select class="form-control" id="datatableFilterColumnSelector" >
			<option value="0">All</option>
		</select>
	</div>
	<div class="form-group">
		<select id="datatableFilterConditionSelector" class="form-control" >
			<option value="all">All</option>
			<option value="equal">equals</option>
			<option value="gt">greater than</option>
			<option value="lt">less than</option>
			<option value="between">between</option>
		</select>
	</div>
	<div class="form-group">
		<input type="text" class="form-control" placeholder="Parameter" id="datatableFilterConditionGreaterThan" />
	</div>
	<div class="form-group andcondition lessthancondition" style="display:none;">
		<div class="input-group">
	  		<span class="input-group-addon andcondition" >and</span>
			<input type="text" class="form-control andcondition lessthancondition" 
				placeholder="Parameter" id="datatableFilterConditionLessThan" />
		</div>
	</div>
	<div class="form-group">	
		<button class="btn btn-default" type="button" id="datatableFilterExecuteButton" >
			<span class="glyphicon glyphicon-filter text-center" aria-hidden="true"></span>
			Filter
		</button>
	</div>
	<div class="form-group">	
		<button class="btn btn-default" type="button" id="datatableFilterResetButton" >
			<span class="glyphicon glyphicon-refresh text-center" aria-hidden="true"></span>
			Reset
		</button>
	</div>
	</div>
	</form>

	<table class="table table-striped table-condensed" id="drilldownTable" >		
		<c:forEach items="${ drilldown.table }" var="row" varStatus="rowCount">
			<c:choose>
				<c:when test="${ rowCount.index eq 0 }">
					<thead>
						<tr>
							<th class="donotprint" id="0" >
								<div class="dropdown" id="ticklerMenu">
									<a href="#" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" role="button" 
							        	aria-haspopup="true" aria-expanded="false" id="ticklerMenuLink">
							        	<span class="glyphicon glyphicon-check"></span>
							        	Tickler 
							        	<span class="caret"></span>
							        </a>
							        
									
									<ul class="dropdown-menu" aria-labelledby="ticklerMenuLink">
										<li>									
											<a href="#" class="dropdown-item" id="selectAllDrilldown" title="Select all rows in the current view." >
												Select All in View
											</a>
									    </li>
									    <li>
											<a href="#" class="dropdown-item" id="selectNoneDrilldown" title="Deselect all checked rows.">
												Select None
											</a>
										</li>
										<li role="separator" class="divider"></li>
										<li> 
									    	<a href="/web/dashboard/display/AssignTickler.do" class="dropdown-item"  
									    		title="Assign Tickler to Checked Rows." id="assignTicklerChecked" >
												Assign Tickler
											</a>
										</li>
									</ul>						 
							    </div>
							</th>
							<th  class="placeholder" id="1" >&nbsp;</th>
							<c:forEach items="${ row }" var="heading" varStatus="columnCount">
								<!-- Column count ID is this list plus the first 2 static columns -->
								<th id="${ columnCount.index + 2 }" >
									<c:out value="${ heading }" />
								</th>
							</c:forEach>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th id="0"></th>
							<th class="placeholder" id="1"></th>
							<c:forEach items="${ row }" var="heading" varStatus="columnCount">
								<th class="filter" id="${ columnCount.index + 2 }" ></th>
							</c:forEach>
						</tr>
					</tfoot>		
					<tbody>
				</c:when>
				<c:otherwise>
					<tr>
						<c:forEach items="${ row }" var="column" varStatus="columnCount" >
						
							<c:choose>
							<c:when test="${ columnCount.index eq 0 }">	
								<td style="text-align:center;" >&nbsp;</td>								
								<td class="donotprint">
									<c:choose>
									<c:when test="${ column eq 'error' }">
										<c:out value="${ column }" />									
									</c:when>
									<c:otherwise>
										<input type="checkbox" id="${ column }" class="ticklerChecked" />
									</c:otherwise>
									</c:choose>
								</td>
								<td>
									
									<c:choose>
										<c:when test="${ fn:containsIgnoreCase( primaryDataType,'demographic_no' ) }" >
											
											<a class="donotprint" href="${ pageContext.request.contextPath }/demographic/demographiccontrol.jsp?demographic_no=${ column }&amp;displaymode=edit&amp;dboperation=search_detail" 
											 target="_blank" title="Open Patient File" >
									        	<span class="glyphicon glyphicon-folder-open" style="margin-right:10px;" ></span>
									        	<c:out value="${ column }" />
									   		</a>
									   		
									   		<span class="printonly">
									   			<c:out value="${ column }" />
									   		</span>
									   	</c:when>
									   	<c:otherwise>
								   			<c:out value="${ column }" />
								   		</c:otherwise>
							   		</c:choose>
							
								</td>
							</c:when>
							<c:otherwise>
								<td>
									<c:out value="${ column }" />
								</td>
							</c:otherwise>	
							</c:choose>
						
						</c:forEach>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</tbody>
	</table>
	<hr />
	<h3> 
		&nbsp;
	</h3>
</div> <!--  end main content row -->

<!-- modal panel for displaying this indicators details -->	
<div id="indicatorInfo" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<div class="modal-header">	
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<c:out value="${ drilldown.name }" />
				</h4>
			</div>
			
			<div class="modal-body">
			
				<h4>Category</h4>
				<p>
					<c:out value="${ drilldown.category }" />
				</p>
				<h4>Sub Category</h4>
				<p>
					<c:out value="${ drilldown.subCategory }" />
				</p>
				<h4>Definition</h4>
				<p>
					<c:out value="${ drilldown.definition }" />
				</p>
				<h4>Indicator Framework</h4>
				<p>
					<c:out value="${ drilldown.framework }" />
				</p>
				<h4>Indicator Framework Version</h4>
				<p>
					<c:out value="${ drilldown.frameworkVersion }" />
				</p>
				<h4>Notes</h4>
				<p>
					<c:out value="${ drilldown.notes }" />
				</p>
				
				<c:if test="${ not empty drilldown.queryString }">
					<h4>Query</h4>
					<p>
						<c:out value="${ drilldown.queryString }" />
					</p>
				</c:if>				
			</div>
			
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close
				</button>
			</div>						
		</div> 
		<!-- end modal content -->								
	</div>
</div> 
<!--  end indicator modal  -->	

<!-- modal panel for tickler assignment -->
<div id="assignTickler" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4>Assign Tickler</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button id="saveTicklerBtn" type="submit" class="btn btn-primary">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- End Tickler assignment modal panel -->	

</div>	<!-- end container -->
</body>
</html>