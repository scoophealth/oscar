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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div class="page-header" style="margin-top: 0px; margin-bottom: 0px;">
		<h1 class="patientHeaderName" style="margin-top: 0px;" ng-cloak>
			<b>{{demographic.lastName}}, {{demographic.firstName}}</b>  <span ng-show="demographic.title">({{demographic.title}})</span> 
			
			<small class="patientHeaderExt pull-right"> 
				<i><bean:message key="demographic.patient.context.born"/>: </i>
				<b>{{demographic.dobYear}}-{{demographic.dobMonth}}-{{demographic.dobDay}}</b> (<b>{{demographic.age | age}}</b>) &nbsp;&nbsp; <i><bean:message key="demographic.patient.context.sex"/>:</i> <b>{{demographic.sex}}</b>
				<i> &nbsp;&nbsp; <bean:message key="Appointment.msgTelephone"/>:</i> <b>{{demographic.phone}}</b> 
				<!-- <span class="glyphicon glyphicon-new-window"></span>-->
			</small>
		</h1>
</div>

			
	<nav class="navbar navbar-default" role="navigation"
		style="padding-top: 0px;margin-bottom:3px;">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-ex1-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand navbar-toggle pull-left" href="#">Select Module</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling   removed data-toggle="tab"  from a ngclick changeTab3 -->
		<div class="collapse navbar-collapse navbar-ex1-collapse"
			style="padding-left: 0px;">
			<ul class="nav navbar-nav" id="myTabs">
				<li ng-repeat="tab in recordtabs2" ng-class="isTabActive(tab)">
					<a href="javascript:void(0)" ng-if="!tab.dropdown" ng-click="changeTab(tab)" >{{tab.label}} 
					<strong class="text-danger" ng-show="tab.extra=='outstanding'" title="<bean:message key="oscarEncounter.Index.ConsultOutstanding"/>">!</strong>
					
					<span ng-if="tab.label=='Tickler' && overdueTicklersCount>0" class="badge badge-danger" title="{{overdueTicklersCount+' overdue ticklers'}}">{{overdueTicklersCount}}</span>
					</a>
					<a href="javascript:void(0)" ng-if="tab.dropdown"  class="dropdown-toggle" data-toggle="dropdown">{{tab.label}} <strong class="text-danger" ng-show="tab.extra=='outstanding'" title="<bean:message key="oscarEncounter.Index.ConsultOutstanding"/>">!</strong><span class="caret"></span></a>
						<ul ng-if="tab.dropdown" class="dropdown-menu" role="menu">
							<li ng-repeat="dropdownItem in tab.dropdownItems" >
								<a href="javascript:void(0)" ng-click="changeTab(dropdownItem)" >{{dropdownItem.label}} <strong class="text-danger" ng-show="dropdownItem.extra=='outstanding'" title="<bean:message key="oscarEncounter.Index.ConsultOutstanding"/>">!</strong></a>
							</li>
						</ul>
				</li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</nav>
			<!-- -->
	<div class="row">
        <div class="include-record-peice" ui-view></div>
    </div>
    
    <div class="row noprint">
    	<div id="noteInput2" class="center-block well col-md-4 col-md-offset-3 text-center hand-hover" style="padding:0px;" ng-click="toggleNote();" ng-show="!hideNote">
    		<span class="glyphicon glyphicon-chevron-up"></span><span class="glyphicon glyphicon-chevron-up"></span><span class="glyphicon glyphicon-chevron-up"></span>
    	</div>
    	<div id="noteInput" class="center-block well col-md-4 col-md-offset-3" ng-show="hideNote" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
			<div style="position:absolute;top:0px;Right:0px;font-size:10px">
			<span class="glyphicon glyphicon-arrow-left hand-hover" ng-click="moveNote('l');" title="move left"></span>  			
			<span class="glyphicon glyphicon-stop hand-hover" ng-click="moveNote('c');" title="center"></span>   
			<span class="glyphicon glyphicon-arrow-right hand-hover" ng-click="moveNote('r');" title="move right"></span>
			</div>
			<div class="col-xs-4">
			
			    
			    <input type="text" ng-model="options.magicVal" placeholder="Template" 
				typeahead="t.encounterTemplateName as t.encounterTemplateName for t in searchTemplates($viewValue)" 
				typeahead-on-select="insertTemplate($item, $model, $label)"
				class="form-control">
				
				
		    </div>
			<div class="col-xs-3 text-center hand-hover" style="padding:0px;line-height:1;font-size:14px;" ng-click="toggleNote();"  >
			<span class="glyphicon glyphicon-chevron-down"></span><span class="glyphicon glyphicon-chevron-down"></span><span class="glyphicon glyphicon-chevron-down"></span>
			
			</div>
			<div class="col-xs-4 " >
			    <input type="text" class="form-control" placeholder="Search" data-ng-disabled="true">
			</div>
    		
    		
    		<textarea id="noteEditor{{demographicNo}}" class="form-control input-lg col-lg-4" rows="6" ng-model="page.encounterNote.note" ng-disabled="page.cannotChange"></textarea>
    		
    		<div style="font-size:8pt" ng-if="page.assignedCMIssues != null  && page.assignedCMIssues.length > 0">
			    <label>Assigned Issues:</label>
			    <table class="table">
					<tr ng-repeat="i in page.assignedCMIssues">
						<td>
							<input type="button" value="remove" ng-click="removeIssue(i)" ng-if="i.unchecked==null || i.unchecked==false"/>
						</td>
						<td>{{i.issue.description}} ({{i.issue.code}})</td>
					</tr>
					
				</table>
			</div>
			
			<div class="pull-left">
				<input type="text" class="form-control" placeholder="Assign Issue"  
					typeahead="i.issueId as i.code for i in searchIssues($viewValue)" 
					typeahead-on-select="assignIssue($item, $model, $label);selectedIssue='';" 
					ng-model="selectedIssue" 
					typeahead-loading="loadingIssues"
					typeahead-min-length="3" typeahead-append-to-body="true"/>
			</div>
			
			
			<input type="hidden" id="startTag" value="<bean:message key="oscarEncounter.Index.startTime"/>">
			<input type="hidden" id="endTag" value="<bean:message key="oscarEncounter.Index.endTime"/>">
			
    		<div class="btn-group btn-group-sm pull-right">
			  <button type="button" class="btn btn-default" ng-click="pasteTimer()" id="aTimer" title="<bean:message key="oscarEncounter.Index.pasteTimer"/>">00:00</button>
			  <button type="button" class="btn btn-default" ng-click="toggleTimer()" title="<bean:message key="oscarEncounter.Index.toggleTimer"/>">
				<span class="glyphicon glyphicon-pause"  id="aToggle"></span>
			  </button>
			  <button type="button" class="btn btn-default" ng-click="saveNote()" id="saveButton"  data-ng-disabled="page.encounterNote.isSigned" title="<bean:message key="oscarEncounter.Index.btnSave"/>">
			  				<span class="glyphicon glyphicon-save"  id="theSave"></span>
			  </button>
			  <button type="button" class="btn btn-default" ng-click="saveSignNote()" title="<bean:message key="oscarEncounter.Index.btnSignSave"/>">
			  				<span class="glyphicon glyphicon-pencil"  id="Sign"></span>
			  </button>
			  <button type="button" class="btn btn-default" ng-click="saveSignVerifyNote()" title="<bean:message key="oscarEncounter.Index.btnSign"/>">
			  				<span class="glyphicon glyphicon-thumbs-up"  id="SaveSignVerify"></span>
			  </button>
			  <button type="button" class="btn btn-default" ng-click="saveSignBillNote()" title="<bean:message key="oscarEncounter.Index.btnSignSaveBill"/>">
			  				<span class="glyphicon glyphicon-usd"  id="bill"></span>
			  </button>
			</div>
    		
    	</div>
    </div>
    
