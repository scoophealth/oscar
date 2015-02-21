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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<div class="modal-body">
	<div class="row">
		<div class="col-xs-12">
			<h4>Create an appointment</h4>
		</div>
	</div>

	<div class="row" ng-show="showErrors === true">
		<div class="col-xs-12">
			<ul>
				<li class="text-danger" ng-repeat="error in errors">{{error}}</li>
			</ul>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-7">
			<div class="form-group">
				<label>Patient:</label> <input type="text"
					ng-model="appointment.demographicName" placeholder="Patient"
					typeahead="pt.demographicNo as pt.name for pt in searchPatients($viewValue)"
					typeahead-on-select="updateDemographicNo($item, $model, $label)"
					class="form-control">

			</div>

		</div>
		<div class="col-xs-5">
			<div class="row" ng-if="appointment.demographic != null">
				<div class="col-xs-3">
					<img width="60px"
						ng-src="../imageRenderingServlet?source=local_client&clientId={{appointment.demographic.demographicNo}}" />
				</div>
				<div class="col-xs-9">
					<div>{{appointment.demographic.lastName}},
						{{appointment.demographic.firstName}}</div>
					<div>{{appointment.demographic.hin}}</div>
					<div>{{appointment.demographic.dateOfBirth | date :
						'yyyy-MM-dd'}}</div>
				</div>
			</div>

		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			<div class="form-group">
				<label>Provider:</label> <input type="text"
					ng-model="appointment.providerName"
					placeholder="Provider"
					typeahead="pt.providerNo as pt.name for pt in searchProviders($viewValue)"
					typeahead-on-select="updateProviderNo($item, $model, $label)"
					class="form-control">

			</div>

		</div>
	</div>

	<div class="row">
		<div class="col-xs-6" class="bootstrap-timepicker">
			<label>Start Time:</label> <input ng-model="appointment.startTime" id="startTime"
				placeholder="Start Time" class="form-control" />
		</div>
		<div class="col-xs-6">
			<label>Duration:</label> <input ng-model="appointment.duration"
				placeholder="Duration" class="form-control" />
		</div>
	</div>

	<div class="row">

		<div class="col-xs-6">
			<label>Type:</label> 
			 <select ng-model="appointment.type" ng-init="appointment.type=''" ng-options="p.name as p.name for p in types" class="form-control" ng-change="selectType()">
				  </select>

		</div>

		<div class="col-xs-6">
			<label>Critical:</label> 
			<select ng-model="appointment.critical" ng-options="p.value as p.label for p in urgencies" class="form-control">
				  </select>
			
		</div>

	</div>


	<div class="row">
		<div class="col-xs-6">
			<label>Reason:</label>
			<textarea ng-model="appointment.reason" placeholder="Reason"
				class="form-control" rows="5"></textarea>
		</div>
		<div class="col-xs-6">
			<label>Notes:</label>
			<textarea ng-model="appointment.notes" type="text"
				placeholder="Notes" class="form-control" rows="5"></textarea>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-6">
			<label>Location:</label> <input ng-model="appointment.location"
				placeholder="Location" class="form-control" />
		</div>
		<div class="col-xs-6">
			<label>Resources:</label> <input ng-model="appointment.resources"
				type="text" placeholder="Resources" class="form-control" />
		</div>
	</div>
	

</div>
<div class="modal-footer">

	<button class="btn btn-primary" ng-click="save()">Save</button>
	<button class="btn btn-default" ng-click="close()">
		<bean:message key="global.close" bundle="ui" />
	</button>
</div>

<script>
	$(document).ready(function(){
		$("#startTime").timepicker();
	});
</script>