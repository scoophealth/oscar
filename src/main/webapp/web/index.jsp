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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
//Initialize some variables
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String userName = loggedInInfo.getLoggedInProvider().getFormattedName();
session.setAttribute("useIframeResizing", "true");  //Temporary Hack
%>

<!DOCTYPE html>
<!-- ng* attributes are references into AngularJS framework -->
<html lang="en" ng-app="oscarProviderViewModule">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="../images/Oscar.ico">

<title>OSCAR</title>

<link href="../library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
<!-- link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"   -->
<link rel="stylesheet" href="css_up/jquery-ui.css">
<link href="../css/font-awesome.css" rel="stylesheet">

<!-- we'll combine/minify later -->
<link href="css/navbar-fixed-top.css" rel="stylesheet">
<link href="css/navbar-demo-search.css" rel="stylesheet">
<link href="css/patient-list.css" rel="stylesheet">

<link href="../library/ng-table/ng-table.css" rel="stylesheet">

<link href="../library/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">

<style>
/*styles temporary, when design complete styles will be moved to css above*/
.hand-hover{cursor: pointer; cursor: hand;}


#parent{
    position:fixed;
    bottom:0px;
    width:100%;   //width should be 100%
 } 
 #child{
    width:100px; //min width should give to center the div.
    margin:0px auto; //here it will make center 
 }

#noteInput {
    position: fixed;
    bottom: 0;
    //width: 100%;
}

#noteInput {
    //background: #ccccFF;
    line-height: 2;
    //text-align: center;
    //color: #042E64;
    margin-bottom:0px;
    
    font-size: 30px;
    font-family: sans-serif;
    font-weight: bold;
    //text-shadow: 0 1px 0 #84BAFF;
    //box-shadow: 0 0 15px #00214B
}

#noteInput2 {
    margin-bottom:0px;    
    font-family: sans-serif;
    font-weight: bold;
    position: fixed;
    bottom: 0;
}

pre.noteInEdit {
	border-color: rgba(126, 239, 104, 0.8);
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(126, 239, 104, 0.6);
	outline: 0 none;
}

</style>

</head>

<body>

	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" ng-controller="NavBarCtrl" ng-cloak>
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				
				<!-- link back to 'classic' view -->
				<a  href="../provider/providercontrol.jsp"><img src="../images/Logo.png" height="40px" title="Go to OSCAR Classic UI" border="0" style="padding-top:10px"/></a>
			</div>
			<div class="navbar-collapse collapse">
				<form class="navbar-form navbar-left form-search" role="search">
					<div class="form-group">
						<input type="text" class="form-control search-query" placeholder="Search Patients" id="demographicQuickSearch" autocomplete="off">
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-default" tabindex="-1"> <!--  ng-click="goToPatientSearch() -->
							<span class="glyphicon glyphicon-search"></span>
						</button>
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a ng-click="newDemographic('sm')">New Patient</a></li>
							<%-- <li ng-repeat="item in demographicSearchDropDownItems"><a href="{{item.url}}">{{item.label}}</a></li> --%>
						</ul>
						<button type="button" class="btn btn-default" ui-sref="dashboard">
							<span class="glyphicon glyphicon-home"></span>
						</button>
					</div>

				</form>
				
				<ul class="nav navbar-nav">
					<li ng-repeat="item in menuItems"  ng-class="{'active': isActive(item.id)}">
						<a href="{{item.url}}" ng-click="changeTab(item.id)" data-toggle="tab" >{{item.label}}
							<span ng-if="item.extra.length>0">({{item.extra}})</span>
						</a>
					</li>
					
					
					<li class="dropdown"><a href="void()" class="dropdown-toggle"
						data-toggle="dropdown">More<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li ng-repeat="item in moreMenuItems">
								<a href="{{item.url}}" ng-class="getMoreTabClass(item.id)" ng-click="changeMoreTab(item.id)">{{item.label}}
								<span ng-if="item.extra.length>0" class="badge">{{item.extra}}</span></a>
							</li>
						</ul>
					</li>
						
						
				</ul>
				
				
				<div class="navbar-text pull-right" style="line-height:20px">
					<a href="javascript: function myFunction() {return false; }" onClick="popup(700,1024,'../scratch/index.jsp','scratch')" title="Scratchpad"><span class="glyphicon glyphicon-edit"></span></a>
					&nbsp;&nbsp;
					
					<a href="#/messenger" title="OSCAR Mail">
						<span  class="glyphicon glyphicon-envelope"></span> 
					</a>
						
					<span title="New OSCAR messages (demographic)">{{unreadMessagesCount}}</span> |
					<span title="Total new OSCAR Messages">{{unreadMessagesCount}}</span> |
					<span title="New messages from patients">-</span> 
					
					&nbsp; &nbsp;
						
					<!-- span class="glyphicon glyphicon-globe"></span -->
					<span class="dropdown">
					<span class="dropdown-toggle hand-hover" data-toggle="dropdown"><u>{{currentProgram.name}}</u></span>
					<ul class="dropdown-menu" role="menu">
                    	<li ng-repeat="item in programDomain">
                        	<a href="#" ng-click="changeProgram(item.program.id)">
					    		<span ng-if="item.program.id === currentProgram.id">&#10004;</span>
					    		<span ng-if="item.program.id != currentProgram.id">&nbsp;&nbsp;</span>
								{{item.program.name}}
					    	</a>
					    </li>
				 	</ul>
				 	
				 	</span>
					&nbsp;
				<!-- span class="glyphicon glyphicon-user"></span  -->	
				<span class="dropdown-toggle hand-hover" data-toggle="dropdown"><u><%=userName %></u></span>
					<ul class="dropdown-menu" role="menu">
					<li ng-repeat="item in userMenuItems"><a href="{{item.url}}">{{item.label}}</a></li>
				  </ul>
				  
				<!-- div class="btn-group pull-right" style="padding-left:10px"  -->
					<a  href="../logout.jsp" title="Logout" style="padding-left:10px;">
						<span class="glyphicon glyphicon-off"></span>
					</a>
				<!--  /div --> <!-- btn-group -->
				
						
				</div>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<!-- nav bar is done here -->

	 
	 <!-- Start patient List template --> 

	<div class="container-fluid" ng-controller="PatientListCtrl">
		<div id="left_pane" class="col-md-2">
		
			<ul class="nav nav-tabs nav-justified">			
				<li ng-repeat="item in tabItems" ng-class="{'active': isActive(item.id)}">
					<a href="javascript:void(0);" ng-click="changeTab(item.id)" data-toggle="tab">{{item.label}}</a>
				</li>
				<!-- 
				<li class="dropdown"><a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">More<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li ng-repeat="item in moreTabItems">
							<a href="javascript:void(0);" ng-class="getMoreTabClass(item.id)" ng-click="changeMoreTab(item.id)">{{item.label}}<span ng-if="item.extra.length>0" class="badge">{{item.extra}}</span></a></li>
						</ul>
				</li>
				-->
			</ul>
			<div class="list-group"  ng-cloak>
			<!-- 
			<button type="button" class="btn btn-default">
 				 <span class="glyphicon glyphicon-arrow-left"></span> 
			</button>
			<button type="button" class="btn btn-default">
 				 <span class="glyphicon glyphicon-eject"></span> 
			</button>
			-->
			
			<!-- <span class="pull-right">-->
			
			<!-- refreshing content simply by reloading tab -->
			<button type="button" class="btn btn-default" ng-click="changeTab(currenttab.id)"> 
 				 <span class="glyphicon glyphicon-refresh"></span> 
			</button>
			
			<button type="button" class="btn btn-default" ng-disabled="currentPage == 0" ng-click="currentPage=currentPage-1">
 				 <span class="glyphicon glyphicon-circle-arrow-up"></span> 
 		 
			</button>
			
			<button type="button" class="btn btn-default" ng-disabled="currentPage == nPages-1"  ng-click="currentPage=currentPage+1">
 				 <span class="glyphicon glyphicon-circle-arrow-down"></span> 
			</button>
			
			<!-- </span>-->
				<form class="form-search" role="search">
					<span class="form-group" class="twitter-typeahead">
						<input type="text"  class="form-control" placeholder="Filter" ng-model="query"/>
					</span>
				</form>
		<div ng-include="template"></div>
		<span class="pull-right">{{currentPage+1}}/{{numberOfPages()}}</span>
		</div>
		</div>
	
	
	<!-- End patient List template -->
		
	<div id="right_pane" class="col-md-10" ui-view ng-cloak></div>
	
	<!-- just for debugging -->
	<p class="text-warning" id="myinfo"></p>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="../js/jquery-1.9.1.js"></script>
	
	<script src="../library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<!-- script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script -->
	
	<script src="../library/hogan-2.0.0.js"></script>
	<script src="../library/typeahead.js/typeahead.min.js"></script>
	<script src="../library/angular.min.js"></script>
	<!-- script src="../library/angular-route.min.js"></script  -->
	<script src="../library/angular-ui-router.js"></script>
 	<script src="../library/angular-resource.min.js"></script>
 	
 	<script src="../library/ui-bootstrap-tpls-0.11.0.js"></script>
 	<script src="../library/pym.js"></script>
 	
 	<script src="../library/ng-infinite-scroll.min.js"></script>
 	
 	<script src="../library/ng-table/ng-table.js"></script>

	<!-- we'll combine/minify later -->
	<script src="common/demographicServices.js"></script>
	<script src="common/securityServices.js"></script>
	<script src="common/formServices.js"></script>
	<script src="common/noteServices.js"></script>
	<script src="common/providerServices.js"></script>
	<script src="common/patientDetailStatusServices.js"></script>
	<script src="common/uxServices.js"></script>
	<script src="filters.js"></script>
	<script src="app.js"></script>
	
	<script src="dashboard/dashboardController.js"></script>
	<script src="common/navBarController.js"></script>
	<script src="patientlist/patientListController.js"></script>
	<script src="record/recordController.js"></script>
	<script src="record/summary/summaryController.js"></script>
	<script src="record/forms/formsController.js"></script>
	<script src="record/details/detailsController.js"></script>
	
	<script src="tickler/ticklerController.js"></script>
	<script src="schedule/scheduleController.js"></script>
	<script src="admin/adminController.js"></script>
	<script src="billing/billingController.js"></script>
	<script src="consults/consultListController.js"></script>	
	<script src="inbox/inboxController.js"></script>
	
	<!-- 
	
	<script src="js/providerViewController.js"></script>
	<script src="js/reportController.js"></script>
	
	<script src="js/patientDetailController.js"></script>

	<script src="js/settingsController.js"></script>
	<script src="js/supportController.js"></script>
	<script src="js/helpController.js"></script>
	<script src="js/patientSearchController.js"></script>
	<script src="js/messengerController.js"></script  -->
	
	
	
	<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

<script>

$(document).ready(function(){
	
	$('#demographicQuickSearch').typeahead({
		name: 'patients',
		valueKey:'name',
		limit: 10,
		
		remote: {
	        url: '../ws/rs/demographics/search?query=%QUERY',
	        cache:false,
	        //I needed to override this to handle the differences in the JSON when it's a single result as opposed to multiple.
	        filter: function (parsedResponse) {
	        	var maxResults = 10;
	            retval = [];
	            if(parsedResponse.items instanceof Array) {
	            	for (var i = 0;  i < parsedResponse.items.length;  i++) {
	            		if(i > maxResults) {
	            			reval.push({'more':'true','numResults':parsedResponse.items.length});
	            		} else {
	            			retval.push(parsedResponse.items[i]);
	            		}
	                 }
	            } else {
	            	retval.push(parsedResponse.items);
	            }
	            return retval;
	        }
	    },
	    
		//TODO:change this to an anonymous function which loads the template from somewhere else
		//that way we can inject the type formatting we want with results
		template: [
		        '<p class="demo-quick-name">{{name}}</p>',
		        '{{#hin}}<p class="demo-quick-hin">&nbsp;"{{hin}}"</p>{{/hin}}',
		       	'{{#dob}}<p class="demo-quick-dob">&nbsp;{{dobString}}</p>{{/dob}}'
		 ].join(''),
		       	engine: Hogan
		}).on('typeahead:selected', function (obj, datum) {
		    console.log('chose demographic ' + datum.id);
		    window.location.href='#/record/'+datum.id + "/summary";
	});
	
});

</script>
</body>
</html>
