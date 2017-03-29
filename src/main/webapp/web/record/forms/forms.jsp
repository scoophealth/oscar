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
<div class="col-sm-3">		
	<ul class="nav nav-tabs nav-justified">
		<li ng-repeat="list in page.formlists" ng-class="getListClass(list.id)" class="hand-hover"><a ng-click="changeTo(list.id)">{{list.label}}</a></li>
		<li class="dropdown">
		    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
		      <span class="glyphicon glyphicon-tasks"> </span>
		    </a>
		    <ul class="dropdown-menu">
		      <li ng-show="hasAdminAccess"><a class="hand-hover" onclick="popup(600, 1200, '../administration/?show=Forms', 'manageeforms')" >Manage eForms</a></li>
		      <li ng-show="hasAdminAccess"><a class="hand-hover" onclick="popup(600, 1200, '../administration/?show=Forms&load=Groups', 'editGroups')" >Edit Groups</a></li>
		      <li ng-show="hasAdminAccess" class="divider"></li>
		      <li ng-repeat="opt in page.formOptions"><a ng-click="formOption(opt)">{{opt.label}}</a></li>
		    </ul>
		</li>		
	</ul> 	
	<%--
	<fieldset >
	       		<legend style="margin-bottom:0px;">All Forms</legend>
	       		<input type="search" class="form-control" placeholder="Filter" ng-model="filterFormsQ">
	        	<ul style="padding-left:12px;">
	        	<li ng-repeat="item in page.currentFormList[page.currentlistId] | filter:filterFormsQ"   ><a ng-click="viewForm(item.id)">{{item.label}}<small ng-show="item.type">({{item.type}})</small></a> <span class="pull-right">{{item.date}}</span></li> 
	        	</ul>
	</fieldset>   
	 --%>
	<div class="panel panel-success"> 
	  	<!-- Default panel contents -->
	  	   <input type="search" class="form-control" placeholder="Filter" ng-model="filterFormsQ">
	  	   <ul class="list-group" tabindex="0" ng-keypress="keypress($event)">
   				<li class="list-group-item" ng-repeat="item in page.currentFormList[page.currentlistId] | filter:filterFormsQ" ng-class="getActiveFormClass(item)">
   					<input type="checkbox" ng-model="item.isChecked"/>
   					<a class="list-group-item-text hand-hover" title="{{item.subject}}" ng-click="viewFormState(item,1)"><span  ng-show="item.date" class="pull-right">{{item.date | date : 'd-MMM-y'}}</span>{{item.name}}</a>
   				</li>

   				<li class="list-group-item" ng-repeat="formItem2 in page.encounterFormlist[page.currentlistId] | filter:filterFormsQ" ng-hide="page.currentlistId==1">
   					<a class="list-group-item-text hand-hover" ng-click="viewFormState(formItem2,1)">{{formItem2.name}}</a>
   				</li>
   				
   				<li class="list-group-item" ng-repeat="formItem in page.encounterFormlist[page.currentlistId] | filter:filterFormsQ" ng-hide="page.currentlistId==0">
   					<a class="list-group-item-text hand-hover" ng-click="viewFormState(formItem,1)">{{formItem.formName}} <span ng-show="formItem.date" class="pull-right">{{formItem.date | date : 'd-MMM-y'}}</span></a>
   				</li>
   				
   			</ul>
	</div>
</div>
<div class="col-sm-9">
	<a class="hand-hover pull-right" ng-show="!isEmpty(page.currentForm)" title="Open in new window" ng-click="viewFormState(page.currentForm,2)"><span class="glyphicon glyphicon-new-window"></span></a>
	<div ng-if="isEmpty(page.currentForm)">
		<h2><bean:message key="forms.title.form.library" bundle="ui"/></h2>
		<div>
			
			<div  class="col-sm-4">
				<legend style="margin-bottom:0px;"> 
					<bean:message key="forms.title.form.groups" bundle="ui"/>
				</legend>
				<ul class="list-group" >
	        		<li ng-repeat="mod in page.formGroups" class="list-group-item" ng-class="getGroupListClass(mod)">
	        			 <span class="badge">{{mod.summaryItem.length}}</span>
	        			<a class="list-group-item-text" ng-click="setCurrentEFormGroup(mod)" href="javascript:void(0)"> {{mod.displayName}} </a> 
	        		</li> 			
				</ul>
			</div>

			<div  class="col-sm-4">
				<legend style="margin-bottom:0px;"> 
					&nbsp;{{currentEformGroup.displayName}}  
				</legend>
	        	<ul style="padding-left:12px;">
	        		<li ng-repeat="item in currentEformGroup.summaryItem">
	        			<span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span>
	        			<a ng-click="openFormFromGroups(item)" href="javascript:void(0)" ng-class="item.indicatorClass" >{{item.displayName | limitTo: 34 }} {{item.displayName.length > 34 ? '...' : '' }}<small ng-show="item.classification">({{item.classification}})</small></a> 
	        		</li> 			
				</ul>
			</div>

			<div ng-if="favouriteGroup"  class="col-sm-4">
				<legend style="margin-bottom:0px;"> 
					<bean:message key="forms.title.form.favourite" bundle="ui"/>: {{favouriteGroup.displayName}}
				</legend>
				
				<ul style="padding-left:12px;">
	        		<li ng-repeat="item in favouriteGroup.summaryItem">
	        			<span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span>
	        			<a ng-click="openFormFromGroups(item)" href="javascript:void(0)" ng-class="item.indicatorClass" >{{item.displayName | limitTo: 34 }} {{item.displayName.length > 34 ? '...' : '' }}<small ng-show="item.classification">({{item.classification}})</small></a> 
	        		</li> 			
				</ul>
			</div>
		</div>
    </div>
	<div id="formInViewFrame"></div>
</div>
