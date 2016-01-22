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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html ng-app="k2aConfig">
<head>
	<title><bean:message key="admin.admin.Know2ActNotifications"/></title>
	<link href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/library/angular.min.js"></script>	
	<script src="<%=request.getContextPath() %>/web/common/k2aServices.js"></script>	
</head>

<body     >
	<div  ng-controller="k2aConfig">
		<h2  style="margin-left:5px"><bean:message key="admin.admin.Know2ActNotifications"/></h2>
		<div >
	 		<blockquote ng-repeat="notif in page.notifications" class="">
				<h4> <a href="#">{{notif.summary}}</a> </h4>
				<pre>{{notif.body}}</pre>
				
				<small>{{notif.author}} -- {{notif.createdAt | date }}</small>
				<a class="btn btn-default" ng-click="readmore(notif)" role="button"><bean:message key="admin.admin.Know2ActNotifications.readmore"/></a>
				<a class="btn btn-default" ng-click="archive(notif)" role="button"><bean:message key="admin.admin.Know2ActNotifications.archive"/></a>	
			</blockquote>
  		</div>
 	</div>
		
	
	<script>
		var app = angular.module("k2aConfig", ['k2aServices']);
		
		app.controller("k2aConfig", function($scope,k2aService) {
		    $scope.readmore = function(notif){
		    	k2aService.getMoreNotification(notif).then(function(data){
			    	var win = window.open(data,'moreInfo','width=700,height=700');
					win.focus();
				});
		    };
		    
		    $scope.archive = function(notif){
		    	k2aService.ackNotification(notif).then(function(data){
			    	getNotifications();
				});
		    };
		    
		    $scope.page = {};
		    $scope.page.notifications = [];
		    
		    getNotifications = function(){
		    	k2aService.getNotifications().then(function(data){
			    	$scope.page.notifications = data;
				});
		    };
		    getNotifications();
  
		});
	
	</script>
</html>
