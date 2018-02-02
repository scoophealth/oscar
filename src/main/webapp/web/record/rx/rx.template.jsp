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
       <fieldset ng-repeat="mod in $ctrl.page.columnOne.modules">
       		<legend style="margin-bottom:0px;"> 

		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="openPreventions(demographicNo)" ng-show="mod.summaryCode=='preventions'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="gotoState('add', mod)" ng-disabled="page.cannotAdd" ng-hide="mod.summaryCode=='meds' || mod.summaryCode=='assessments' || mod.summaryCode=='allergies' || mod.summaryCode=='preventions' || page.cannotAdd">
			<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a> 

		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="openRx(demographicNo)" ng-show="mod.summaryCode=='meds'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>
		
		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="openAllergies(demographicNo)" ng-show="mod.summaryCode=='allergies'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		<a href="#/record/{{demographicNo}}/forms" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-show="mod.summaryCode=='assessments'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		{{mod.displayName}}
		</legend>

        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem" ng-show="$index < mod.displaySize"><span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span><a ng-click="gotoState(item,mod,item.id)" href="javascript:void(0)" ng-class="item.indicatorClass" popover="{{item.displayName}} {{item.warning}}" popover-trigger="mouseenter">{{item.displayName | limitTo: 34 }} {{item.displayName.length > 34 ? '...' : '' }}<small ng-show="item.classification">({{item.classification}})</small></a> </li> 			
			<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="openPreventions(demographicNo)" ng-show="mod.summaryCode=='preventions'"><bean:message key="global.btnAdd"/> {{mod.displayName}}</a>
			<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="gotoState('add', mod)" ng-hide="mod.summaryCode=='meds' || mod.summaryCode=='assessments' || mod.summaryCode=='allergies' || mod.summaryCode=='preventions' || page.cannotAdd"><bean:message key="global.btnAdd"/> {{mod.displayName}}</a>
			<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="openRx(demographicNo)" ng-show="mod.summaryCode=='meds'"><bean:message key="global.btnAdd"/> {{mod.displayName}}</a>
			<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="openAllergies(demographicNo)" ng-show="mod.summaryCode=='allergies'"><bean:message key="global.btnAdd"/> {{mod.displayName}}</a>
			<a href="#/record/{{demographicNo}}/forms" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-show="mod.summaryCode=='assessments'"><bean:message key="global.btnAdd"/> {{mod.displayName}}</a>
        	</ul>

		<span ng-class="showMoreItemsSymbol(mod)" ng-click="toggleList(mod)" ng-show="showMoreItems(mod)"></span>
       </fieldset>   
    </div>
    
    <div class="col-sm-6" id="middleSpace" ng-click="checkAction($event)" ng-keypress="checkAction($event)" >
    
        
	
        
        <div class="row">
        		<form >
	        <div class="form-group">
	    			<input type="text" class="form-control" id="exampleInputEmail1" placeholder="Search:">
	  		</div>
	  		</form>
		</div>
		
		<div class="row">
			<fieldset>
       		<legend style="margin-bottom:0px;">Drug Profile
				<div class="row">
					<div class="col-sm-12">
					    <div class="input-group">
					      <input type="text" class="form-control" placeholder="Search for..." ng-model="$ctrl.drugProfileFilter">
					      <div class="input-group-btn">
					        <button type="button" class="btn btn-default" ng-click="exportDemographic()">Print</button>
					        <button type="button" class="btn btn-default" ng-click="exportDemographic()">Reprint</button>
					        <button type="button" class="btn btn-default" ng-click="exportDemographic()">Timeline</button>
					      </div>
					    </div><!-- /input-group -->
				  	</div>
				</div>
			</legend>
			
			<table class="table table-condensed table-striped">
				<tr>
					<th>Medication</th>
					<th>Start Date</th>
					<th>Days To Exp</th>
					<th>reRx</th>
					<th>Action</th>
				</tr>
				<tr ng-repeat="drug in $ctrl.page.drugs | filter:$ctrl.drugProfileFilter">
					<td>
					{{drug.instructions}}
					</td>
					<td>{{drug.rxDate}}</td>
					<td>0</td>
					<td>rx</td>
					<td>
						<div class="btn-group">
						  <button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						    Actions<span class="caret"></span>
						  </button>
						  <ul class="dropdown-menu">
						    <li><a>Discontinue</a></li>
						    <li><a>Delete</a></li>
						    <li><a>Add Reason</a></li>
						    <li><a>Set as Long Term Med</a></li>
						    <li><a>Annotate</a></li>
						    <li><a>Hide From CPP</a></li>
						    <li><a>Move up in list</a></li>
						    <li><a>Move down in list</a></li>
						  </ul>
						</div>
					</td>
				</tr>	
			</table>
			
			</fieldset>
		
		</div>
			  

		
	
    </div><!-- middleSpace -->


	 <div class="col-sm-3" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
	 	<fieldset ng-repeat="mod in $ctrl.page.columnThree.modules">
       		<legend style="margin-bottom:0px;">{{mod.displayName}}
       			<div class="form-group">
					<input type="text" class="form-control search-query" ng-model="incomingQ" placeholder="Search">
				</div>
			</legend>
        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem | filter:incomingQ" ng-show="$index < mod.displaySize"  ><span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span><a ng-click="gotoState(item)" class="hand-hover" ng-class="{true: 'abnormal', false: ''}[item.abnormalFlag]">{{item.displayName}}<small ng-show="item.classification">({{item.classification}})</small></a> </li> 
        	</ul>
        	       	
        	<span ng-class="showMoreItemsSymbol(mod)" ng-click="toggleList(mod)" ng-show="showMoreItems(mod)"></span>
       </fieldset>
	 	
	
	 </div>
