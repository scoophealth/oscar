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
		
		<a href="javascript:void(0)" style="font-size:12px;color:#333;padding-top:10px" class="pull-right" ng-click="$ctrl.openAllergies()" ng-show="mod.summaryCode=='allergies'">
		<span class="glyphicon glyphicon-plus-sign" title="{{mod.summaryCode}}"></span>
		</a>

		{{mod.displayName}}
		</legend>

        	<ul style="padding-left:12px;">
        	<%-- href="{{item.action}}" --%>
        	<li ng-repeat="item in mod.summaryItem | orderBy: displayName" ng-show="$index < mod.displaySize">
        		<span class="pull-right">{{item.date | date : 'dd-MMM-yyyy'}}</span>
        		<a ng-click="$ctrl.gotoState(item,mod,item.id)" href="javascript:void(0)" ng-class="item.indicatorClass" popover="{{item.displayName}} {{item.warning}}" popover-trigger="'mouseenter'">{{item.displayName | limitTo: 34 }} {{item.displayName.length > 34 ? '...' : '' }}<small ng-show="item.classification">({{item.classification}})</small></a> 
        	</li> 			
			<a href="javascript:void(0)" class="text-muted add-summary" ng-if="mod.summaryItem==null" ng-click="$ctrl.openAllergies()" ng-show="mod.summaryCode=='allergies'"><bean:message key="global.btnAdd"/>{{mod.displayName}}</a>
        	</ul>

		<span ng-class="showMoreItemsSymbol(mod)" ng-click="toggleList(mod)" ng-show="showMoreItems(mod)"></span>
       </fieldset>   
    </div>
    
    <div class="col-sm-6" id="middleSpace" ng-click="checkAction($event)" ng-keypress="checkAction($event)" >
    
        <div class="row">
        		<ul class="nav nav-pills nav-justified">
			  <li class="active" <%-- %>ng-class="isCurrentStatus('none')" --%> ><a data-target="#all" ng-click="removeFilter(0)" data-toggle="tab" class="hand-hover">Prescribe</a></li>
			  <li ng-class="isCurrentStatus('Just My Notes')"><a ng-click="changeNoteFilter('Just My Notes')" class="hand-hover">Additional Meds</a></li>
			</ul>
			<div class="tab-content">
			  
			</div>
        		<div class="list-group">
			  <li class="list-group-item <%--active--%>" ng-repeat="med in $ctrl.toRxList">
			  <div class="row" >
			  	<div class="col-sm-8">
			  		<span class="pull-right">
			  			<a ng-click="$ctrl.showMore(med);">
			  				<span ng-if="med.showmore == null">More</span><span ng-if="med.showmore">Less</span> 
			  			</a>
			  			<a ng-click="$ctrl.addToFavourite(med)"><span class="glyphicon glyphicon-heart"></span></a>
			  			<a ng-click="$ctrl.cancelMed(med,$index)"><span class="glyphicon glyphicon-remove-circle"></span></a>
			  		</span>
				    	<h4 class="list-group-item-heading">{{med.getName()}}</h4>
				    	<form>
				    	  <div class="form-group">
					    <input type="text" class="form-control" ng-if="med.custom" id="customInput" placeholder="Custom Drug Name" ng-model="med.customName" auto-focus>
					  </div>	
					  <div class="form-group">
					    <input type="text" class="form-control" id="instructionsInput" placeholder="Instructions" ng-blur="$ctrl.parseInstr(med)" ng-model="med.instructions" auto-focus>
					  </div>
					  <div class="form-group ">
						<div class="row">
						  <div class="col-xs-6" ng-class="$ctrl.checkForQuantityError(med)">
							<label  class="control-label" for="quantityInput">Qty/Mitte</label> 
					    		<input type="text" class="form-control" id="quantityInput" placeholder="Qty/Mitte" ng-model="med.quantity" ng-change="$ctrl.manualQuantityEntry(med)">
					    		<span ng-if="$ctrl.checkForQuantityError(med)" id="helpBlock2" class="help-block">Quantity was not calculated. Manual Calculation required</span>
						  </div>
						  <div class="col-xs-6">
							<label for="repeatsInput">Repeat</label>   
					    		<input type="text" class="form-control" id="repeatsInput" placeholder="Repeats" ng-model="med.repeats" ng-change="$ctrl.repeatsUpdated(med)">    
						  </div>
						</div>
					  	<div class="row" ng-if="med.duration != null" >
					  		<div class="col-xs-12 has-error">
					  			Duration was calculated to  {{med.rxDurationInDays()}} days.  <a ng-click="$ctrl.changeEndDate(med)">Change?</a>
					  			<div class="checkbox">
								    <label>
								      <input ng-model="med.longTerm" type="checkbox"> Long term
								    </label>
								  </div>
					  		</div>
					  	</div>
					  </div>
					  <div class="form-group" ng-if="med.showmore">
					  	<div class="row">
						  <div class="col-xs-6">
							<label for="repeatsInput">Written Date</label>  
							<div class="input-group">
								<input type="text" class="form-control"   
								ng-model="med.writtenDate" 
								uib-datepicker-popup="yyyy-MM-dd" 
								uib-datepicker-append-to-body="false" 
								is-open="startDatePicker" 
								ng-click="startDatePicker = true" 
								placeholder="YYYY-MM-DD"
								/>
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
						  </div>
					  	</div>
					  </div>
					</form>
			    </div>
			  	<div class="col-sm-4">
			  		<div class="alert alert-danger" ng-if="med.custom"><b>Warning</b> you will lose the following functionality:
			  			<ul style="padding:10px;">
					     <li>  Known Dosage Forms / Routes </li>
					     <li>  Drug Allergy Information </li>
					     <li>  Drug-Drug Interaction Information </li>
					     <li>  Drug Information </li>
					     </ul>
					</div>
			  		
				  	<div uib-alert ng-repeat="alert in $ctrl.page.dsMessageHash[med.atc]" class="alert"  ng-class="'alert-' + ($ctrl.getAlertStyl(alert) || 'warning')"  style="padding: 9px;margin-bottom: 3px;"  ng-hide="$ctrl.checkIfHidden(alert)" >  <%-- uib-popover-html="{{alert.body}}" popover-trigger="'mouseenter'"  --%>
		 		   		{{alert.heading}}<br>
	 			   		{{alert.summary | limitTo: 150 }}{{alert.summary.length > 150 ? '...' : ''}}
	 			   		<br>
	 			     	<small>From:{{alert.author}}</small>
		 			</div>
	 			</div>
	 			
			    </div>
			  </li>
			</div>
        		<medsearch med-selected="$ctrl.medSelected(med)" fav-selected="$ctrl.favSelected(fav)" favourite-meds="$ctrl.page.favouriteDrugs" custom-rx="$ctrl.customRx(nam)"></medsearch>	
        		<button type="button" class="btn btn-primary btn-block" style="margin-top:3px;" ng-click="$ctrl.saveAndPrint()">Save And Print</button>
		</div>
		<hr>
		<div class="row">
			<rx-profile fulldrugs="$ctrl.page.fulldrugs" re-rx="$ctrl.reRx(drug)" ds-messages="$ctrl.page.dsMessageHash" show-alert="$ctrl.showAlert(alert)" add-favorite="$ctrl.addToFavourite(drug)"></rx-profile>
		</div>
			  

		
	
    </div><!-- middleSpace -->


	 <div class="col-sm-3">

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
	 	
		<a style="color:white;" ng-click="$ctrl.shortDSMessage()">Refresh Decision Support</a>
	 </div>
