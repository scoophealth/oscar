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
			<h4>Appointment</h4>
		</div>
	</div>

	<div class="row" style="height: 5px"></div>


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

	<div class="row" style="height: 5px"></div>

	<div class="row">
		<div class="col-xs-2">
			<strong>Provider:</strong>
		</div>
		<div class="col-xs-6" ng-show="!showProviderFormControl">
			{{appointment.provider.lastName}},{{appointment.provider.firstName}}
			<span ng-show="appointmentWriteAccess"
				class="glyphicon glyphicon-pencil" ng-click="editProvider()"></span>
		</div>
		<div class="col-xs-6" ng-show="showProviderFormControl">
			<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">
						<span class="glyphicon glyphicon-remove"
							ng-click="cancelProviderUpdate()"></span>
					</div>
					<input type="text" ng-model="appointmentUpdate.providerName"
						placeholder="Provider"
						typeahead="pt.providerNo as pt.name for pt in searchProviders($viewValue)"
						typeahead-on-select="updateProvider($item, $model, $label)"
						class="form-control input-sm">
				</div>
			</div>
		</div>


	</div>


	<div class="row">
		<div class="col-xs-4">
			<b>Date:</b>{{appointment.appointmentDate | date: 'yyyy-MM-dd'}}
		</div>

		<div class="col-xs-4">
			<b>Start Time:</b>{{appointment.startTime | date: 'hh:mm a'}}
		</div>

		<div class="col-xs-4">
			<b>Duration:</b>{{appointment.duration}} mins
		</div>
	</div>

	<div class="row">
		<div class="col-xs-4">
			<b>Status:</b>{{getStatus(appointment.status)}}
		</div>

		<div class="col-xs-4">
			<b>Type:</b>{{appointment.type}}
		</div>

		<div class="col-xs-4">
			<b>Critical:</b>{{appointment.urgency}}
		</div>
	</div>

	<div class="row">
		<div class="col-xs-4">
			<b>Location:</b>{{appointment.location}}
		</div>

		<div class="col-xs-4">
			<b>Resources:</b>{{appointment.resources}}
		</div>

		<div class="col-xs-4"></div>
	</div>

	<div class="row">
		<div class="col-xs-4">
			<b>Reason:</b>{{appointment.reason}}
		</div>

		<div class="col-xs-4">
			<b>Notes:</b>{{appointment.notes}}
		</div>

		<div class="col-xs-4"></div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			
		</div>
			
	</div>

<!-- 
	 	<pre>{{appointmentUpdate}}</pre> 
-->

</div>
<div class="modal-footer">
	<!-- 
    <button class="btn" ng-click="save()">Save</button>
    
-->

<button class="btn btn-default" ng-click="noShowAppointment()">
		No Show</button>
		<button class="btn btn-default" ng-click="cancelAppointment()">
		Cancel</button>
		
	<button class="btn btn-danger" ng-click="deleteAppointment()">
		<span class="glyphicon glyphicon-trash"></span>Delete
	</button>



	<!-- 
	<button class="btn btn-primary" ng-click="showAppointmentHistory()">
		Appt History
	</button>
-->

	<button class="btn btn-primary" ng-click="close()">
		<bean:message key="global.close" bundle="ui" />
	</button>
</div>