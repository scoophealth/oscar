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

<!-- this CSS makes it so the modals don't have the vertical sliding animation. Not sure if I will keep this or how I will use this yet -->
<style>
.modal.fade {
	opacity: 1;
}

.modal.fade .modal-dialog, .modal.in .modal-dialog {
	-webkit-transform: translate(0, 0);
	-ms-transform: translate(0, 0);
	transform: translate(0, 0);
}
</style>

<div ng-show="consultReadAccess" class="col-lg-12">


	<form name="searchForm" id="searchForm">

		<div class="row">
			<div class="col-xs-2">
				<input ng-model="search.referralStartDate" type="text"
					id="referralStartDate" name="referralStartDate"
					class="form-control" datepicker-popup="yyyy-MM-dd"
					datepicker-append-to-body="true" is-open="data.isOpen"
					ng-click="data.isOpen = true" placeholder="<bean:message key="consult.list.referralStartDate" bundle="ui"/>">
			</div>
			<div class="col-xs-2">
				<input ng-model="search.referralEndDate" type="text"
					id="referralEndDate" name="referralEndDate" class="form-control"
					datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true"
					is-open="data2.isOpen" ng-click="data2.isOpen = true"
					placeholder="<bean:message key="consult.list.referralEndDate" bundle="ui"/>">
			</div>
			<div class="col-xs-2">
				<select class="form-control" ng-model="search.status" name="status" id="status" ng-options="status.value as status.name for status in statuses">
					<option value=""><bean:message key="consult.list.status.all" bundle="ui"/></option>
				</select>
			</div>
			<div class="col-xs-2">
				<select ng-model="search.team" name="team" id="team"
					class="form-control" ng-init="search.team='<bean:message key="consult.list.team.all" bundle="ui"/>'" ng-options="t for t in teams">
				</select>
			</div>
		</div>


		<div style="height: 5px"></div>

		<div class="row">
			<div class="col-xs-2">
				<input ng-model="search.appointmentStartDate" type="text"
					id="appointmentStartDate" name="appointmentStartDate"
					class="form-control" datepicker-popup="yyyy-MM-dd"
					datepicker-append-to-body="true" is-open="data3.isOpen"
					ng-click="data3.isOpen = true" placeholder="<bean:message key="consult.list.appointmentStartDate" bundle="ui"/>">
			</div>
			<div class="col-xs-2">
				<input ng-model="search.appointmentEndDate" type="text"
					id="appointmentEndDate" name="appointmentEndDate"
					class="form-control" datepicker-popup="yyyy-MM-dd"
					datepicker-append-to-body="true" is-open="data4.isOpen"
					ng-click="data4.isOpen = true" placeholder="<bean:message key="consult.list.appointmentEndDate" bundle="ui"/>">
			</div>
			
		<div class="col-xs-2">
		 <div class="input-group">
   		   <div class="input-group-addon"><span class="glyphicon glyphicon-remove" ng-click="removeDemographicAssignment()"></span></div>
      
				 <input type="text" ng-model="consult.demographicName" placeholder="<bean:message key="consult.list.patient" bundle="ui"/>" 
				typeahead="pt.demographicNo as pt.name for pt in searchPatients($viewValue)" 
				typeahead-on-select="updateDemographicNo($item, $model, $label)"
				class="form-control">
			</div>	
		</div>

		</div>


		<div style="height: 5px"></div>

		<div class="row">
			<div class="col-xs-12">
				<button class="btn btn-primary" type="button" ng-click="doSearch()"><bean:message key="global.search" bundle="ui"/></button>
				<button class="btn btn-default" type="button" ng-click="clear()"><bean:message key="global.clear" bundle="ui"/></button>
				<button class="btn btn-success" type="button" ng-click="addConsult()" ng-disabled="search.demographicNo==null">
					<bean:message key="consult.list.new" bundle="ui"/>
				</button>
			</div>
		</div>
	</form>

	<div style="height: 15px"></div>

	<table ng-table="tableParams" show-filter="false" class="table">
		<tbody>

			<tr ng-repeat="consult in $data" ng-mouseover="consult.$selected=true" ng-mouseout="consult.$selected=false"
    	 ng-class="{'active': consult.$selected}">
				<td ng-show="consultWriteAccess"><input type="checkbox"
					ng-model="consult.checked"></td>
				<td ng-show="consultWriteAccess"><a
					ng-click="editConsult(consult)" class="hand-hover"><bean:message key="global.edit" bundle="ui"/></a></td>
				<td data-title="'<bean:message key="consult.list.header.patient" bundle="ui"/>'" class="text-center" sortable="'Demographic'">
					{{consult.demographic.formattedName}}</td>
				<td data-title="'<bean:message key="consult.list.header.service" bundle="ui"/>'" class="text-center" sortable="'Service'">{{consult.serviceName}}</td>
				<td data-title="'<bean:message key="consult.list.header.consultant" bundle="ui"/>'" class="text-center" sortable="'Consultant'">{{consult.consultant.formattedName}}</td>
				<td data-title="'<bean:message key="consult.list.header.team" bundle="ui"/>'" class="text-center" sortable="'Team'">{{consult.teamName}}</td>
				<td data-title="'<bean:message key="consult.list.header.status" bundle="ui"/>'" class="text-center" sortable="'Status'">{{consult.statusDescription}}</td>
				<td data-title="'<bean:message key="consult.list.header.priority" bundle="ui"/>'" class="text-center {{consult.urgencyColor}}" sortable="'Urgency'">{{consult.urgencyDescription}}</td>
				<td data-title="'<bean:message key="consult.list.header.mrp" bundle="ui"/>'" class="text-center" sortable="'MRP'">{{consult.mrp.formattedName}}</td>

				<td data-title="'<bean:message key="consult.list.header.appointmentDate" bundle="ui"/>'" class="text-center" sortable="'AppointmentDate'">
					{{consult.appointmentDate | date: 'yyyy-MM-dd HH:mm'}}</td>
				<td data-title="'<bean:message key="consult.list.header.lastFollowUp" bundle="ui"/>'" class="text-center" sortable="'FollowUpDate'">
					{{consult.lastFollowUp | date: 'yyyy-MM-dd'}}</td>
				<td data-title="'<bean:message key="consult.list.header.referralDate" bundle="ui"/>'" class="text-center" sortable="'ReferralDate'">
					{{consult.referralDate | date: 'yyyy-MM-dd'}}</td>
			</tr>
		</tbody>

		<tfoot ng-show="consultWriteAccess">
			<tr>
				<td colspan="12" class="white">
					<a ng-click="checkAll()" class="hand-hover"><bean:message key="consult.list.checkAll" bundle="ui"/></a> - <a ng-click="checkNone()" class="hand-hover"><bean:message key="consult.list.checkNone" bundle="ui"/></a>								
				</td>
			</tr>
		</tfoot>
		
	</table>


	<!-- 
<pre>{{search}}</pre>
<pre>{{lastResponse}}</pre>
-->

</div>



<div ng-show="consultReadAccess != null && consultReadAccess == false"
	class="col-lg-12">
	<h3 class="text-danger">
		<span class="glyphicon glyphicon-warning-sign"></span><bean:message key="consult.list.access_denied" bundle="ui"/>
	</h3>
</div>

