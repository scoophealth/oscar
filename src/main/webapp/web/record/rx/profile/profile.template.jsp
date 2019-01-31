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
<fieldset>
	<legend style="margin-bottom:0px;">Current and Long Term
 		<button type="button" class="btn btn-default btn-xs" ng-class="$ctrl.buttonStyle(1)" ng-click="$ctrl.setMode(1)">All</button>
		<button type="button" class="btn btn-primary btn-xs" ng-class="$ctrl.buttonStyle(0)" ng-click="$ctrl.setMode(0)">Current</button>
		<div class="row">
			<div class="col-sm-12">
			    <div class="input-group">
			      <input type="text" class="form-control" placeholder="Search Drug Profile" ng-model="$ctrl.drugProfileFilter">
			      <div class="input-group-btn">
			        <button type="button" class="btn btn-default" ng-click="$ctrl.print()">Print</button>
			        <button type="button" class="btn btn-default" ng-click="$ctrl.rePrint()">Reprint</button>
			        <button type="button" class="btn btn-default" ng-click="$ctrl.timeline()">Timeline</button>
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
		<tr ng-repeat="drug in $ctrl.rxComp.drugs | filter:$ctrl.drugProfileFilter | orderBy: '-rxDate'" 
		    ng-class="{'danger' : ($ctrl.daysToExp(drug) === 0 && drug.longTerm), 'warning': ($ctrl.daysToExp(drug) < 31 && $ctrl.daysToExp(drug) > 0)}"  
		    ng-if="drug.longTerm || $ctrl.daysToExp(drug) > 0">
		<%--  --%>
			<td ng-class="{ 'deletedItem': drug.archived }">
				<a ng-click="$ctrl.medhistory(drug)">{{drug.instructions}}</a>
			</td>
			<td>{{drug.rxDate}}</td>
			<td>{{$ctrl.daysToExp(drug)}}</td>
			<td><a ng-click="$ctrl.reRx({'drug':drug})">rx</a></td>
			<td>
				<div class="btn-group">
					<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						Actions<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
					    <li><a ng-click="$ctrl.discontinue(drug)" >Discontinue</a></li>
					    <li><a ng-click="$ctrl.delete(drug)" >Delete</a></li>
					    <li><a ng-click="$ctrl.addReason(drug)" >Add Reason</a></li>
					    <li ng-if="!drug.longTerm"><a ng-click="$ctrl.setAsLongTermMed(drug)" >Set as Long Term Med</a></li>
					    <li ng-if="drug.longTerm"><a ng-click="$ctrl.unsetAsLongTermMed(drug)" >Unset as Long Term Med</a></li>
					    <li><a ng-click="$ctrl.annotate(drug)" >Annotate</a></li>
					    <%-- Kunal says these are no longer required for ontario md li><a ng-click="$ctrl.hideFromCPP(drug)" >Hide From CPP</a></li>
					    <li><a ng-click="$ctrl.moveUpInList(drug)" >Move up in list</a></li>
					    <li><a ng-click="$ctrl.moveDownInList(drug)" >Move down in list</a></li  --%>
					</ul>
				</div>
			</td>
		</tr>	
	</table>
	<h3>Expired Medications</h3>
	<table class="table table-condensed table-striped">
		<tr>
			<th>Medication</th>
			<th>Start Date</th>
			<th>Days To Exp</th>
			<th>reRx</th>
			<th>Action</th>
		</tr>
		<tr ng-repeat="drug in $ctrl.rxComp.drugs | filter:$ctrl.drugProfileFilter | orderBy: '-rxDate'"
			ng-if="$ctrl.daysToExp(drug) == 0 && !drug.longTerm">
			
			<td ng-class="{ 'deletedItem': drug.archived }">
				<a ng-click="$ctrl.medhistory(drug)">{{drug.instructions}}</a>
			</td>
			<td>{{drug.rxDate}}</td>
			<td>{{$ctrl.daysToExp(drug)}}</td>
			<td><a ng-click="$ctrl.reRx({'drug':drug})">rx</a></td>
			<td>
				<div class="btn-group">
					<button class="btn btn-default btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						Actions<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
					    <li><a ng-click="$ctrl.discontinue(drug)" >Discontinue</a></li>
					    <li><a ng-click="$ctrl.delete(drug)" >Delete</a></li>
					    <li><a ng-click="$ctrl.addReason(drug)" >Add Reason</a></li>
					    <li><a ng-click="$ctrl.setAsLongTermMed(drug)" >Set as Long Term Med</a></li>
					    <li><a ng-click="$ctrl.annotate(drug)" >Annotate</a></li>
					    <%-- Kunal says these are no longer required for ontario md li><a ng-click="$ctrl.hideFromCPP(drug)" >Hide From CPP</a></li>
					    <li><a ng-click="$ctrl.moveUpInList(drug)" >Move up in list</a></li>
					    <li><a ng-click="$ctrl.moveDownInList(drug)" >Move down in list</a></li  --%>
					</ul>
				</div>
			</td>
		</tr>	
	</table>
			
</fieldset>
