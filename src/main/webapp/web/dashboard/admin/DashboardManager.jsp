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

<security:oscarSec roleName='${ sessionScope[userrole] }, ${ sessionScope[user] }' rights="w" objectName="_dashboardManager">
	<c:redirect url="securityError.jsp?type=_admin.dashboardManager" />
</security:oscarSec>

<!DOCTYPE html > 
<html:html locale="true" >
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>
<bean:message key="dashboard.dashboardmanager.title" />
</title>
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/web/css/Dashboard.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/web/dashboard/admin/dashboardManagerController.js"></script>
	
</head>
<body>
<div>
<div class="col-sm-12">
	
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">

	<html:form styleClass="form-inline" styleId="importForm" 
		method="POST" action="/web/dashboard/admin/DashboardManager.do" enctype="multipart/form-data" >
			
		<c:if test="${ not empty param.dashboardId }">
		<div class="form-group">
			<button class="btn btn-default backtoDashboardBtn" id="getDashboard_${ param.dashboardId }" type="button">
				<span class="glyphicon glyphicon-circle-arrow-left text-center" aria-hidden="true"></span>
				Dashboard
			</button>
		</div>
		</c:if>	
		
		<!-- Upload Indicator Buttons -->
		<div class="form-group">
			<div class="input-group" id="import">
					
				<input type="hidden" value="importTemplate" name="method" />
											
				<div class="input-group-btn">
					<button class="btn btn-default" type="button" id="importbutton">
						<span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
						<bean:message key="dashboard.dashboardmanager.import.button" />
					</button>
				</div>
				
				<input type="text" class="form-control" id="importxmltemplate"
					placeholder="<bean:message key='dashboard.dashboardmanager.import.title' />"  />
				
				<div class="input-group-btn">
					<label class="btn btn-default btn-file" id="browsebutton" >
						<bean:message key="dashboard.dashboardmanager.import.browse" />
						<input style="display:none;" type="file" name="indicatorTemplateFile" />
					</label>					
				</div>					
			    	
			</div>		    
		</div>
		
		<!-- Create Dashboard Buttons -->
		<div class="form-group">
			<div class="btn-group" >
		    	<button type="button" class="btn btn-default btn-md" data-toggle="modal" 
		    		data-target="#newDashboard" id="createDashboard" >
		    		<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>	    		
		    		<bean:message key="dashboard.dashboardmanager.dashboard.create" />
		    	</button>
			</div>
		</div>
		
		<!-- Edit Dashboard Buttons -->
		<div class="form-group">
			<div class="dropdown btn-group" id="editDashboardButtonContainer">
				<button class="btn btn-default dropdown-toggle btn-md" type="button"
					id="editDashboardMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<bean:message key="dashboard.dashboardmanager.dashboard.edit" /> 
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="editDashboardMenu">			
					<c:forEach items="${ dashboards }" var="dashboard" varStatus="loop">
						<li>
							<a href="#" id="dashboard_${ dashboard.id }" class="editDashboardSelect" >
								<c:out value="${ dashboard.name }" />
								<input type="hidden" name="selectName" id="selectName" value="${ dashboard.name }" />
								<input type="hidden" name="selectDescription" id="selectDescription" value="${ dashboard.description }" />
								<input type="hidden" name="selectActive" id="selectActive" value="${ dashboard.active }" />
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>		
	</html:form>	
		<span ${ message['status'] eq 'success' ? 'style="color: green;"' : 'style="color: red;"' } >			
			<c:out value="${ message['message'] }" />
		</span>
	</div>  
	</nav><!-- end top nav row -->
	
	<div class="table-responsive" id="libraryTableContainer">
	<h3> 
		Indicator Library
	</h3>
	<hr />
	<div class="col-sm-12">
		<table class="table table-striped table-condensed" id="libraryTable" >
			<thead>
			<tr>
				<th>Disable/Enable</th>
				<th></th>
				<th>Dashboard</th>
				<th>Name</th>
				<th>Category</th>
				<th>Sub Category</th>
				<th>Framework</th>
				<th>Framework Version</th>
			</tr>
			</thead>
		
			<tfoot>
			<tr>
				<th></th>
				<th></th>
				<th>Dashboard</th>
				<th>Name</th>
				<th>Category</th>
				<th>Sub Category</th>
				<th>Framework</th>
				<th>Framework Version</th>
			</tr>
			</tfoot>
			<tbody>
			<c:forEach items="${ indicatorTemplates }" var="indicator" >
				<tr>
					<td>					
						<label class="switch">
						  <input class="form-control toggleActive" type="checkbox" name="IndicatorTemplate_${ indicator.id }" 
								id="toggleActive_${ indicator.id }" ${ indicator.active ? 'checked="checked"' : '' } />
						  	<div class="slider round"></div>
						</label>
					</td>
					<td>
						<button class="btn btn-default exportTemplate text-center" id="exportTemplate_${ indicator.id }" type="button">
							<span class="glyphicon glyphicon-download" aria-hidden="true"></span>
							Export
						</button>

					</td>

					<td>
						<select class="form-control assignDashboard" name="assignDashboard_${ indicator.id }" 
							id="assignDashboard_${ indicator.id }" >
							
							<option value="0" selected="selected" ></option>
							<c:forEach items="${ dashboards }" var="dashboard" varStatus="loop" >
							<c:if test="${ dashboard.active }" >
								<option ${ indicator.dashboardId eq dashboard.id ? 'selected="selected"' : '' } 
									value="${ dashboard.id }"  >
									<c:out value="${ dashboard.name }" />	
								</option>									
							</c:if>		
							</c:forEach>
						</select>
						
					</td>
					
					<td><c:out value="${ indicator.name }" /></td>
					<td><c:out value="${ indicator.category }" /></td>
					<td><c:out value="${ indicator.subCategory }" /></td>
					<td><c:out value="${ indicator.framework }" /></td>
					<td><c:out value="${ indicator.frameworkVersion }" /></td>

				</tr>
			</c:forEach>
			</tbody>
		</table>
		<hr />
		<h3> 
			&nbsp;
		</h3>
	</div>
	</div>

<div id="newDashboard" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<bean:message key="dashboard.dashboardmanager.dashboard.create" />
				</h4>
			</div>
			
			<form action="${ pageContext.request.contextPath }/web/dashboard/admin/DashboardManager.do"  
				method="POST" id="addEditDashboardForm" >
			
			<input type="hidden" name="method" value="saveDashboard" />
			<input type="hidden" name="dashboardId" class="editDashboard" value="" />
					
			<div class="modal-body">
			
				<div class="row">
					<label><bean:message key="dashboard.dashboardmanager.dashboard.name" /></label>
					<input class="form-control editDashboard" type="text" name="dashboardName" />				
				</div>
				
				<div class="row">
					<label><bean:message key="dashboard.dashboardmanager.dashboard.description" /></label>
					<textarea class="form-control editDashboard" name="dashboardDescription" ></textarea>	
				</div>
				
				<div class="checkbox" id="dashboardActiveRow" style="display:none;" >
					<label class="pull-right">
					<input type="checkbox" class="editDashboard" name="dashboardActive" 
						id="dashboardActive"  />
						
					<bean:message key="dashboard.dashboardmanager.dashboard.active" /></label>	
				</div>

			</div>
			<div class="modal-footer">
				<button type="submit" class="btn btn-default btn-success" id="saveDashboard" >
					<bean:message key="dashboard.dashboardmanager.dashboard.save" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<bean:message key="dashboard.dashboardmanager.dashboard.close" />
				</button>
			</div>
			</form>
		</div>

	</div>
</div> <!-- end modal window -->

</div>	
</div> <!-- end container -->
</body>
</html:html>