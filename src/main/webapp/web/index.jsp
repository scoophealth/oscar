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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
// Force the page to un-cache itself so user cannot go back after logout
// The 3 lines ensure that all browsers are covered
// They are necessary for URL not showing this file name (index.jsp)
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

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

<title><bean:message key="global.title" bundle="ui"/></title>

<link href="../library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
<link href="../css/font-awesome.css" rel="stylesheet">
<link href="../css/loading-bar.css" rel="stylesheet">


<!-- we'll combine/minify later -->
<link href="css/navbar-fixed-top.css" rel="stylesheet">
<link href="css/navbar-demo-search.css" rel="stylesheet">
<link href="css/patient-list.css" rel="stylesheet">
<link href="css/rx.css" rel="stylesheet">

<link href="../library/ng-table/ng-table.css" rel="stylesheet">

<link href="../library/bootstrap2-datepicker/datepicker3.css" rel="stylesheet">

<link href="../css/bootstrap-timepicker.min.css" rel="stylesheet">

<link href="../library/bootstrap/3.0.0/assets/css/bootstrap3_badge_colours.css" rel="stylesheet">


<style>
/*styles temporary, when design complete styles will be moved to css above*/
.hand-hover{cursor: pointer; cursor: hand;}

.hand-hover:hover{
text-decoration:none;
}

#parent{
    position:fixed;
    bottom:0px;
    width:100%;   //width should be 100%
 } 
 #child{
    width:100px; //min width should give to center the div.
    margin:0px auto; //here it will make center 
 }
 


#noteInput, #noteInput2 {
    position: fixed;
    bottom: 0;
    font-family: sans-serif;
	margin-bottom:0px;
	font-weight: bold;
	bottom: 0;
    z-index:999;
}

#noteInput {
    line-height: 2;
    font-size: 30px;
    /*text-shadow: 0 1px 0 #84BAFF;
    //box-shadow: 0 0 15px #00214B
    //background: #ccccFF;
    //text-align: center;
    //color: #042E64;
    */
}

.absolute-left{
left:0px;
}

.absolute-right{
right:0px;
} 

#noteInput ul.dropdown-menu{
max-height:220px;
overflow-y:auto;
overflow-x:none;
}

.absolute-right{
right:0px;
} 

#noteInput ul.dropdown-menu{
max-height:220px;
overflow-y:auto;
overflow-x:none;
}


pre.noteInEdit {
	border-color: rgba(126, 239, 104, 0.8);
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(126, 239, 104, 0.6);
	outline: 0 none;
}

img.navbarlogo {
	height: 40px;
	padding-top:10px;
}

.navbar-form .input-group-btn,
.navbar-form .input-group-addon {
width: auto;
}

/*needed to fix the broken nav dropdown*/
#left_pane{
padding:8px;
}

.add-summary:hover{
color:#333 !important;
text-decoration:none;
}

li.cpp-note-list{
    background:#fff0;
    border:none;
    border-bottom:1px solid #ddd;
    text-align: left;
    padding-top:4px;
    padding-bottom: 10px;
}

li.cpp-note-list{
cursor: pointer;
}

li.cpp-note-list:last-child{
    border-bottom:none;
}

.well-note{
    border-color:#d2d2d2;
    box-shadow:0 1px 0 #cfcfcf;
    border-radius:3px;
    
    /*not sure if this will stay*/
    max-height:300px;
    overflow-y:auto;
}

 .nav .navbar-nav li.dropdown > a .caret {
   border-top-color:  red !important; /*#555555;*/
   border-bottom-color: red !important;/*#555555;*/

 }
 
 .more {
   border-top-color:  #727272 !important; 
   border-bottom-color: #727272 !important;
 }
 
 .more:hover {
   border-top-color:  #333 !important; 
   border-bottom-color: #333 !important;
 }

.highlight, .highlight:hover{
color: #FF0000;
}


.refused, .refused:hover{
color: #EAACAC;
}

.ineligible, .ineligible:hover{
color: #FFCC24;
}

.pending, .pending:hover{
color: #FF00FF;
}

/*think of changing this to be consistent*/
.abnormal-prev, .abnormal-prev:hover{
color: #FF4D4D;
}
 
 .abnormal,  .abnormal:hover{
color:red;
}

.glyphicon-chevron-down-disabled{
color: #999999 !important;
}

/*for patient header*/
@media (max-width: 1320px) {
.patientHeaderName{
font-size:26px
}

.patientHeaderExt{
font-size:20px
}

}

@media print {
 .noprint {display:none;}
 .patientHeaderName{font-size:16px;}
 .patientHeaderExt{font-size:16px;}
}
</style>

</head>
	
<body ng-controller="OscarCtrl">

	<!-- Fixed navbar -->
	<div class="navbar navbar-default navbar-fixed-top" ng-controller="NavBarCtrl" ng-show="me != null" ng-cloak>
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				
				<!-- link back to 'classic' view -->
				<a  href="../provider/providercontrol.jsp"><img class="navbarlogo" src="../images/Logo2.png" title="<bean:message key="global.goToClassic" bundle="ui"/>" border="0" /></a>
			</div>
			
			 
			<div class="navbar-collapse collapse">
			
				<form class="navbar-form navbar-left" role="search">
	 				<div class="form-group">
		 				<div class="input-group">
			 				<input type="text" class="form-control search-query" placeholder="<bean:message key="navbar.searchPatients" bundle="ui"/>" id="demographicQuickSearch" autocomplete="off" value="">
			 				<span class="input-group-addon btn-default hand-hover" ng-click="goToPatientSearch()" title="<bean:message key="navbar.searchPatients" bundle="ui"/>"><span class="glyphicon glyphicon-search" ></span></span>
			 				<span class="input-group-addon btn-default hand-hover"  ng-click="newDemographic('sm')" title="<bean:message key="navbar.newPatient" bundle="ui"/>"><span class="glyphicon glyphicon-plus"></span></span>	 				
						</div>						
					</div>			
				</form>
			
				<!-- large view -->
				<ul class="nav navbar-nav visible-lg hidden-md hidden-sm hidden-xs">
					<li style="margin-right:5px"><span class="navbar-text glyphicon glyphicon-chevron-right hand-hover" 
						ng-show="showPtList === false" ng-click="showPatientList()" 
						title="<bean:message key="navbar.showPatientList" bundle="ui"/>"></span></li>

					<%--New Dashboard Menu --%>
					<li style="margin-right:5px"><span class="navbar-text glyphicon glyphicon-home hand-hover" ui-sref="dashboard" title="<bean:message key="navbar.dashboard" bundle="ui"/>"></span></li>

					<li style="margin-right:5px">
						<a href="javascript:void(0)" ng-if="!dashboardMenu.dropdown" ng-click="transition(dashboardMenu)" >
							<bean:message key="navbar.menu.dashboard" bundle="ui" />

						</a>
						<a href="javascript:void(0)" ng-if="dashboardMenu.dropdown"  class="dropdown-toggle" data-toggle="dropdown">
							<bean:message key="navbar.menu.dashboard" bundle="ui" />
							<span class="caret more"></span>
						</a>
						<ul ng-if="dashboardMenu.dropdown" class="dropdown-menu" role="menu">
							<li class="navbar-text hand-hover" ui-sref="dashboard" 
									title="<bean:message key="navbar.dashboard" bundle="ui" />" >Home</li>
							<li ng-repeat="dashboardMenuItem in dashboardMenu.dropdownItems" >
								<a href="javascript:void(0)" ng-click="transition(dashboardMenuItem)" >{{dashboardMenuItem.label}}</a>
							</li>
						</ul>
					</li>
				
					<li ng-repeat="item in menuItems" ng-class="isActive(item)">
						<a href="javascript:void(0)" ng-if="!item.dropdown" ng-click="transition(item)" >{{item.label}} 
							<span ng-if="item.label=='Inbox' && unAckLabDocTotal>0" class="badge badge-danger">{{unAckLabDocTotal}}</span>
						</a>
						<a href="javascript:void(0)" ng-if="item.dropdown"  class="dropdown-toggle" data-toggle="dropdown">{{item.label}}
							<span class="caret more"></span>
						</a>
						<ul ng-if="item.dropdown" class="dropdown-menu" role="menu">
							<li ng-repeat="dropdownItem in item.dropdownItems" >
								<a href="javascript:void(0)" ng-click="transition(dropdownItem)" >{{dropdownItem.label}}</a>
							</li>
						</ul>
					</li>
				</ul>

				<!-- more condensed version -->
				<ul class="nav navbar-nav hidden-lg visible-md visible-sm visible-xs">	
					<li style="margin-right:5px"><span class="navbar-text glyphicon glyphicon-chevron-right hand-hover" ng-show="showPtList === false" ng-click="showPatientList()" title="<bean:message key="navbar.showPatientList" bundle="ui"/>"></span></li>

					<%-- 
					New Dashboard Menu
					<li style="margin-right:5px"><span class="navbar-text glyphicon glyphicon-home hand-hover" ui-sref="dashboard" title="<bean:message key="navbar.dashboard" bundle="ui"/>"></span></li> --%>
					
					<li style="margin-right:5px">
						<a href="javascript:void(0)" ng-if="!item.dropdown" ng-click="transition(item)" >
							Dashboard
							<span ng-if="item.label=='Inbox' && unAckLabDocTotal>0" class="badge badge-danger">{{unAckLabDocTotal}}</span>
						</a>
						<a href="javascript:void(0)" ng-if="item.dropdown"  class="dropdown-toggle" data-toggle="dropdown">
							Dashboard
							<span class="caret more"></span>
						</a>
						<ul ng-if="item.dropdown" class="dropdown-menu" role="menu">
							<li>
								<span class="navbar-text hand-hover" ui-sref="dashboard" 
									title="<bean:message key="navbar.dashboard" bundle="ui" />" >Home</span>
							</li>
							<li ng-repeat="dropdownItem in " >
							
							</li>
						</ul>
					</li>
						
					<li class="dropdown hand-hover"><a href="void()" class="dropdown-toggle"><bean:message key="navbar.modules" bundle="ui"/><b class="caret"></b></a>
						<ul class="dropdown-menu">
						<li ng-repeat="item in menuItems"  ng-class="{'active': isActive(item) }">
						<a ng-click="transition(item)" data-toggle="tab" >{{item.label}}
							<span ng-if="item.extra.length>0">({{item.extra}})</span>
						</a>
					</li>
						<li class="divider"></li>
							<li ng-repeat="item in moreMenuItems">
								<a ng-class="{'active': isActive(item) }" ng-click="transition(item)">{{item.label}}
								<span ng-if="item.extra.length>0" class="badge">{{item.extra}}</span></a>
							</li>
						</ul>
					</li>		
				</ul>
				
				
				<div class="navbar-text pull-right" style="line-height:20px">
					<a onClick="popup(700,1024,'../scratch/index.jsp','scratch')" title="<bean:message key="navbar.scratchpad" bundle="ui"/>" class="hand-hover">
					 	<span class="glyphicon glyphicon-edit"></span>
					</a>
					&nbsp;&nbsp;
					<span ng-show="messageRights === true">
						<a ng-click="openMessenger()" title="<bean:message key="navbar.messenger" bundle="ui"/>" class="hand-hover">
							<span  class="glyphicon glyphicon-envelope"></span> 
						</a>
						<span ng-repeat="item in messengerMenu">
						   <a ng-click="openMessenger(item)"  title="{{item.label}}" class="hand-hover">{{item.extra}}</a> <span ng-if="!$last">|</span>
						</span>
					</span>
					&nbsp; &nbsp;
					
					<span class="dropdown">
						<span class="dropdown-toggle hand-hover" title="<bean:message key="navbar.changeProgram" bundle="ui"/>"><span class="glyphicon glyphicon-globe"></span></span>
						<ul class="dropdown-menu" role="menu">
	                    	<li ng-repeat="item in programDomain">
	                        	<a ng-click="changeProgram(item.program.id)">
						    		<span ng-if="item.program.id === currentProgram.id">&#10004;</span>
						    		<span ng-if="item.program.id != currentProgram.id">&nbsp;&nbsp;</span>
									{{item.program.name}}
						    	</a>
						    </li>
					 	</ul>
				 	</span>
				 	
					&nbsp;
					
					<span class="dropdown-toggle hand-hover" data-toggle="dropdown" title="<bean:message key="navbar.user" bundle="ui"/>"><span class="glyphicon glyphicon-user"></span><u>{{me.firstName}}</u></span>
					<ul class="dropdown-menu" role="menu">
						<li ng-repeat="item in userMenuItems">
							<a ng-click="transition(item)" ng-class="{'more-tab-highlight': isActive(item) }" class="hand-hover" >{{item.label}}</a>
						</li>
				  	</ul>
				  	
					<a href="../logout.jsp" title="<bean:message key="navbar.logout" bundle="ui"/>" style="padding-left:10px;">
						<span class="glyphicon glyphicon-off"></span>
					</a>


				</div>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<!-- nav bar is done here -->

	 
	 <!-- Start patient List template --> 

	<div class="container-fluid" ng-controller="PatientListCtrl" >
		<div id="left_pane" class="col-md-2 noprint" ng-if="showPatientList()">
		
			<ul class="nav nav-tabs">			
				<li ng-repeat="item in tabItems" ng-class="{'active': isActive(item.id)}" class="hand-hover">
					<a ng-click="changeTab(item.id)" data-toggle="tab">{{item.label}}</a>
				</li>
				
				<li class="dropdown" ng-class="{'active': currentmoretab != null}"><a class="dropdown-toggle hand-hover" ><b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li ng-repeat="item in moreTabItems">
							<a ng-class="getMoreTabClass(item.id)" ng-click="changeMoreTab(item.id)" class="hand-hover">{{item.label}}<span ng-if="item.extra.length>0" class="badge">{{item.extra}}</span></a></li>
						</ul>
				</li>
				
			</ul>
			<div class="list-group"  ng-cloak>
			
			
			<button type="button" class="btn btn-default" ng-click="hidePatientList()" title="<bean:message key="patientList.hide" bundle="ui"/>">
 				 <span class="glyphicon glyphicon-chevron-left"></span> 
			</button>
			
			<button type="button" class="btn btn-default" ng-click="refresh()" title="<bean:message key="patientList.refresh" bundle="ui"/>"> 
 				 <span class="glyphicon glyphicon-refresh"></span> 
			</button>
			
			<button type="button" class="btn btn-default" ng-disabled="currentPage == 0" ng-click="changePage(currentPage-1)" title="<bean:message key="patientList.pageUp" bundle="ui"/>">
 				 <span class="glyphicon glyphicon-circle-arrow-up"></span> 
			</button>
			
			<button type="button" class="btn btn-default" ng-disabled="currentPage == nPages-1"  ng-click="changePage(currentPage+1)" title="<bean:message key="patientList.pageDown" bundle="ui"/>">
 				 <span class="glyphicon glyphicon-circle-arrow-down"></span> 
			</button>
			
			<form class="form-search" role="search">
				<span ng-show="showFilter === true" class="form-group twitter-typeahead">
					<input type="text"  class="form-control" placeholder="<bean:message key="patientList.filter" bundle="ui"/>" ng-model="query"/>
				</span>
			</form>
		
			<div ng-include="sidebar.location"></div>
			
			<span class="pull-right" title="<bean:message key="patientList.pagination" bundle="ui"/>">{{currentPage+1}}/{{numberOfPages()}}</span>
		</div>
	</div>
	<!-- End patient List template -->
		
	<div id="right_pane" ng-class="getRightClass('col-md')" ui-view ng-cloak></div>
	
	
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
 	<script src="../js/loading-bar.js"></script>
 	

	<!-- we'll combine/minify later -->
	<script src="common/demographicServices.js"></script>
	<script src="common/programServices.js"></script>
	<script src="common/scheduleServices.js"></script>
	<script src="common/securityServices.js"></script>
	<script src="common/staticDataServices.js"></script>
	<script src="common/billingServices.js"></script>
	<script src="common/ticklerServices.js"></script>
	<script src="common/formServices.js"></script>
	<script src="common/noteServices.js"></script>
	<script src="common/providerServices.js"></script>
	<script src="common/patientDetailStatusServices.js"></script>
	<script src="common/uxServices.js"></script>
	<script src="common/rxServices.js"></script>
	<script src="common/messageServices.js"></script>
	<script src="common/inboxServices.js"></script>
	<script src="common/k2aServices.js"></script>
	<script src="common/personaServices.js"></script>
	<script src="common/consultServices.js"></script>
	<script src="common/appServices.js"></script>
	<script src="common/systemInfoServices.js"></script>
	<script src="common/diseaseRegistryServices.js"></script>
	<script src="filters.js"></script>
	<script src="app.js"></script>
	
	<script src="oscarController.js"></script>
	<script src="dashboard/dashboardController.js"></script>
	<script src="common/navBarController.js"></script>
	<script src="patientlist/patientListController.js"></script>
	<script src="record/recordController.js"></script>
	<script src="record/summary/summaryController.js"></script>
	<script src="record/forms/formsController.js"></script>
	<script src="record/details/detailsController.js"></script>
	<script src="record/details/detailsHistoryListController.js"></script>
	<script src="record/phr/phrController.js"></script>
	<script src="record/rx/rxController.js"></script>
	<script src="record/tracker/trackerController.js"></script>
	
	<script src="tickler/ticklerController.js"></script>
	<script src="tickler/ticklerViewController.js"></script>
	<script src="tickler/ticklerAddController.js"></script>
	
	<script src="schedule/scheduleController.js"></script>
	<script src="admin/adminController.js"></script>
	<script src="billing/billingController.js"></script>
	<script src="consults/consultRequestListController.js"></script>
	<script src="consults/consultRequestController.js"></script>	
	<script src="consults/consultResponseListController.js"></script>
	<script src="consults/consultResponseController.js"></script>	
	<script src="inbox/inboxController.js"></script>
	<script src="patientsearch/patientSearchController.js"></script>
	
	<script src="report/reportsController.js"></script>
	<script src="document/documentsController.js"></script>
	<script src="settings/settingsController.js"></script>
	<script src="help/supportController.js"></script>
	<script src="help/helpController.js"></script>
	<script src="clinicalconnect/ccController.js"></script>
	
	<script src="schedule/appointmentAddController.js"></script>
	<script src="schedule/appointmentViewController.js"></script>
	
	<!-- 
	
	<script src="js/providerViewController.js"></script>
	<script src="js/messengerController.js"></script  -->	
	
	<script type="text/javascript" src="../share/javascript/Oscar.js"></script>

	<script type="text/javascript" src="../js/bootstrap-timepicker.min.js"></script>

<script>

$(document).ready(function(){

	$('#demographicQuickSearch').typeahead({
		name: 'patients',
		valueKey:'name',
		limit: 11,
		
		remote: {
	        url: '../ws/rs/demographics/quickSearch?query=%QUERY',
	        cache:false,
	        //I needed to override this to handle the differences in the JSON when it's a single result as opposed to multiple.
	        filter: function (parsedResponse) {
	            retval = [];
	            if(parsedResponse.content instanceof Array) {
	            	for (var i = 0;  i < parsedResponse.content.length;  i++) {
	            		var tmp = parsedResponse.content[i];
	            		if(tmp.hin != null && tmp.hin == '') {
	            			tmp.hin = null;
	            		}
	            		if(tmp.formattedDOB != null && tmp.formattedDOB == '') {
	            			tmp.formattedDOB = null;
	            		}
	            		
	            		tmp.name = tmp.lastName + ", " + tmp.firstName;
	            		tmp.blah = "";
	            		retval.push(tmp);
	                 }
	            } else {
	            	retval.push(parsedResponse.content);
	            }
	            
	            console.log("total:"+parsedResponse.total);
	            var scope = angular.element($("#demographicQuickSearch")).scope();
	            scope.setQuickSearchTerm("");
	            
	            if(parsedResponse.total > 10) {
	            	retval.push({name:"<bean:message key="navbar.moreResults" bundle="ui"/>",hin:parsedResponse.total+" total","demographicNo":-1,"more":true});
	            	scope.setQuickSearchTerm(parsedResponse.query);
	            }
	            
	            return retval;
	        }
	    },
	    
		template: [
		        "<p class='demo-quick-name'>{{name}}</p>",
		        '{{#hin}}<p class="demo-quick-hin">&nbsp;<em>{{hin}}</em></p>{{/hin}}',
		       	'{{#dob}}<p class="demo-quick-dob">&nbsp;{{formattedDOB}}</p>{{/dob}}'
		 ].join(''),
		       	engine: Hogan
		}).on('typeahead:selected', function (obj, datum) {
			$('input#demographicQuickSearch').on('blur',function(event){$("#demographicQuickSearch").val("");});

			var scope = angular.element($("#demographicQuickSearch")).scope();
						
			if(datum.more != null && datum.more == true) {
				scope.switchToAdvancedView();
			} else {
				scope.loadRecord(datum.demographicNo);
			}
			
	});
});

</script>
</body>
</html>
